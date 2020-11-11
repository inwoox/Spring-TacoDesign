package tacos.email;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;


//	이 클래스는 고객의 배달 정보와 대금 청구 정보를 갖지 않고,
//	입력 이메일에서 얻는 고객의 이메일 정보만 갖는다.
@Data
public class Order {
	
	private final String email;
	private List<Taco> tacos = new ArrayList<>();
	
	public void addTaco(Taco taco) {
		this.tacos.add(taco);
	}
}