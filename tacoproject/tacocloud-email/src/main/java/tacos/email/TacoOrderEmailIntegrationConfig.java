package tacos.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.mail.dsl.Mail;



//	타코 주문 이메일 플로우 정의  ->  3개의 서로 다른 컴포넌트로 구성 (DSL 방식 구성)

//	IMAP 이메일 인바운드 채널 어댑터  ->  이 채널 어댑터는 EmailProperties의 getImapUrl()으로 생성된 IMAP URL로 생성 되며
//	EmailProperties의 pollRate 속성에 설정된 지연 시간이 될 때마다 이메일을 확인하고, 받은 이메일은 변환기에 연결하는 채널로 전달

//	이메일을 Order 객체로 변환하는 변환기  ->  이 변환기는 tacoOrderEmailFlow 메서드로 주입되는 EmailToOrderTransformer에 구현됨
//	변환된 주문 데이터 (Order 객체)는 다른 채널을 통해 최종 컴포넌트로 전달

//	핸들러는 Order 객체를 받아서 타코 클라우드의 REST API로 제출한다.
//	Mail.imapInboundAdapter()를 호출하려면, spring-integration-mail 의존성을 추가한다.
// 여기서 정의한대로 설정 시간마다 이메일 확인, Order로 변환, Order를 타코 클라우드의 REST API로 제출하여 등록한다.


@Configuration
public class TacoOrderEmailIntegrationConfig {
	
	@Bean
	public IntegrationFlow tacoOrderEmailFlow(
			EmailProperties emailProps, EmailToOrderTransformer emailToOrderTransformer, OrderSubmitMessageHandler orderSubmitHandler)
	{
		return IntegrationFlows
				.from(Mail.imapInboundAdapter(emailProps.getImapUrl()),
						e -> e.poller(Pollers.fixedDelay(emailProps.getPollRate())))
				.transform(emailToOrderTransformer)
				.handle(orderSubmitHandler)
				.get();
	}
}