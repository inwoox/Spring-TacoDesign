package tacos.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.integration.mail.transformer.AbstractMailMessageTransformer;
import org.springframework.integration.support.AbstractIntegrationMessageBuilder;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;


//	EmailToOrderTransformer가 상속하는 AbstractMailMessageTransformer는 이미 Transformer 인터페이스를 구현하고 있다.
//	AbstractMailMessageTransformer는 페이로드가 이메일인 메시지를 처리하는데 편리한 베이스 클래스이다.
//	입력 메시지로부터 이메일 정보를 Message 객체(doTransform() 메서드의 인자로 전달)로 추출하는 일을 지원한다.

@Component
public class EmailToOrderTransformer extends AbstractMailMessageTransformer<Order> {
	
	private static final String SUBJECT_KEYWORDS = "TACO ORDER";
	
	//	이메일을 Order 객체로 파싱하고, Order 객체를 포함하는 페이로드를 갖는 MessageBuilder를 반환.
	//	메시지 빌더에 의해 생성된 메시지는 통합 플로우의 마지막 컴포넌트인 메시지 핸들러로 전달된다.
	@Override
	protected AbstractIntegrationMessageBuilder<Order> doTransform(Message mailMessage) throws Exception {
		
		Order tacoOrder = processPayload(mailMessage); 
		return MessageBuilder.withPayload(tacoOrder);
	}
	
	// 입력 메시지로부터 이메일 정보를 Message 객체로 받아, 거기에서 페이로드를 추출하여, 그 페이로드를 가지고 Order 파싱 메서드를 호출한 결과를 리턴
	private Order processPayload(Message mailMessage) {
		try {
			String subject = mailMessage.getSubject();
			if (subject.toUpperCase().contains(SUBJECT_KEYWORDS)) {
				String email = ((InternetAddress) mailMessage.getFrom()[0]).getAddress();
				String content = mailMessage.getContent().toString();
				return parseEmailToOrder(email, content);
			}
		} catch (MessagingException e) {
		}	catch (IOException e) {}
		return null;
	}
	
	// Message 객체에서 추출한 이메일 페이로드를 가지고, Order 객체로 파싱하는 메서드
	private Order parseEmailToOrder(String email, String content) {
		Order order = new Order(email);
		String[] lines = content.split("\\r?\\n");
		for (String line : lines) {
			if (line.trim().length() > 0 && line.contains(":")) {
				String[] lineSplit = line.split(":");
				String tacoName = lineSplit[0].trim();
				
				String ingredients = lineSplit[1].trim();
				String[] ingredientsSplit = ingredients.split(",");
				List<String> ingredientCodes = new ArrayList<>();
				
				for (String ingredientName : ingredientsSplit) {
					String code = lookupIngredientCode(ingredientName.trim());
					if (code != null) {
						ingredientCodes.add(code);
					}
				}
				
				Taco taco = new Taco(tacoName);
				taco.setIngredients(ingredientCodes);
				order.addTaco(taco);
			}
		}
		return order;
	}
	
	// 식자재 이름을 가지고 식자재 코드를 찾아 리턴하는 메서드
	private String lookupIngredientCode(String ingredientName) {
		for (Ingredient ingredient : ALL_INGREDIENTS) {
			String ucIngredientName = ingredientName.toUpperCase();
			
			if (LevenshteinDistance.getDefaultInstance().apply(ucIngredientName, ingredient.getName()) < 3 || 
					ucIngredientName.contains(ingredient.getName()) || ingredient.getName().contains(ucIngredientName))
			{
				return ingredient.getCode();
			}
		}
		return null;
	}
	
	// 식자재 코드를 검색하는데 참고할 식자재 리스트 변수
	private static Ingredient[] ALL_INGREDIENTS = new Ingredient[] {
      new Ingredient("FLTO", "FLOUR TORTILLA"),
      new Ingredient("COTO", "CORN TORTILLA"),
      new Ingredient("GRBF", "GROUND BEEF"),
      new Ingredient("CARN", "CARNITAS"),
      new Ingredient("TMTO", "TOMATOES"),
      new Ingredient("LETC", "LETTUCE"),
      new Ingredient("CHED", "CHEDDAR"),
      new Ingredient("JACK", "MONTERREY JACK"),
      new Ingredient("SLSA", "SALSA"),
      new Ingredient("SRCR", "SOUR CREAM")
  };
}