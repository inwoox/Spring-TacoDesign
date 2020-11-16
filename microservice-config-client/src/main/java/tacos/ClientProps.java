
package tacos;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


// 구성 속성을 가지는 Git 리포지토리에, application.yml 같은 원천 속성 파일을 올리고,
// 예를 들어 그 안에 아래와 같이 구성 속성을 위치시키면, 속성으로 값을 가져올 수 있다.
// client:
// 	 message: Hello Inwoo!!

// 그러고나서 구성 서버를 실행하고, 구성 클라이언트를 실행하면,
// 구성 속성 GitRepo에 올라가있는 application.yml에 설정된 구성 속성을 가지고, 구성 클라이언트가 실행된다.

// 그리고 그 값은 속성을 이용해 아래와 같이 사용할 수도 있다.


@ConfigurationProperties(prefix = "client")
@Component
public class ClientProps {

	private String message;
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
