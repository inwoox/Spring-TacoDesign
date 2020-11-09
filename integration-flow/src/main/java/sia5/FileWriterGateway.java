package sia5;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel="textInChannel") // 인터페이스 구현체를 런타임 시에 생성하라고 스프링 통합에 알려준다.
public interface FileWriterGateway {					

  // 이 메서드는 파일에 쓰고, 메시지를 생성하는데, 생성된 메시지는 'textInChannel'라는 메시지 채널로 전송 된다.
	// Header 애노테이션은 filename에 전달되는 값이, 메시지 헤더에 있다는 것을 나타낸다.
	// data 값은 메시지 페이로드로 전달 된다.
  void writeToFile( @Header(FileHeaders.FILENAME) String filename, String data );
  
  
  // 메시지 게이트웨이를 생성한 다음에는 통합 플로우를 구성해야한다.
  // 의존성을 빌드에 추가했으므로, 스프링 통합의 자동 구성이 수행될 수 있지만,
  // 애플리케이션 요구를 충족하는 플로우를 정의하는 구성은 우리가 추가로 작성해야한다. (xml , JAVA 구성, DSL을 사용한 JAVA 구성, 3가지 방법)
}
