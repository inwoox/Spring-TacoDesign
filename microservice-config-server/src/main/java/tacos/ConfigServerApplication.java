
package tacos.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;



// 구성 속성의 값이 변경될 일이 거의 없거나, 애플리케이션에 종속되는 경우는 보통 application.yml이나 properties에 지정한다.

// 하지만 자바 시스템 속성이나 운영체제의 환경 변수에 구성 속성을 설정하는 경우, 해당 속성의 변경으로 인해 애플리케이션을 다시 시작해야한다.
// 그리고 JAR이나 WAR 안에 구성 속성을 포함시키는 경우, 구성 속성 변경시 다시 빌드하여 배포해야한다.

// 이런 경우에는 속성만 변경하기 위해 재시작하기 어렵다.
// 다수의 인스턴스에서 속성을 관리해야하는 마이크로서비스 같은 경우 실행중인 모든 서비스 인스턴스에 동일한 변경을 적용하는 것이 어렵다.
// 또한 보안에 민감한 값들을 프로그램 안이나, 운영체제 환경 변수에 설정하기도 바람직하지 않다.


// 이런 맥락에서, 스프링 클라우드 구성 서버가 제공하는 중앙 집중식 구성 서버를 사용한다.

// 애플리케이션을 재빌드하거나, 재시작하지 않아도 실행 중에 구성 변경 가능
// 공통적인 구성을 공유하는 마이크로서비스의 속성 변경시 한 곳에서 한번만 변경하면 된다.
// 보안에 민감한 구성 속성은 애플리케이션 코드와 별도로 암호화하고 유지 및 관리, 복호화된 속성 값을 언제든지 어플리케이션에서 사용할 수 있다. (복호화 로직 불필요)


@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}
}
