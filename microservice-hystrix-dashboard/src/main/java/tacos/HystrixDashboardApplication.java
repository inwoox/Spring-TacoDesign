
package tacos.hystrixdashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard
public class HystrixDashboardApplication {
	public static void main(String[] args) {
		SpringApplication.run(HystrixDashboardApplication.class, args);
	}
}


// @HystrixCommand - 메서드에 이 애노테이션으로 서킷 브레이커를 적용한다. 메서드 내에서 에러를 처리하지 않으면,
// 호출 스택의 그다음 상위 호출자로 예외가 전달되고, 어떤 호출자도 예외를 처리하지 않는다면, 결구 최상위 호출자 (마이크로서비스나 클라이언트)에서 에러로 처리된다.
// 이처럼 처리가 되지 않은 unchecked 예외는 어떤 애플리케이션에서도 골칫거리이며, 특히 마이크로서비스의 경우가 그렇다.
// 장애가 생기면 베가스 규칙을 적용해야한다. (마이크로서비스에서 생긴 에러는, 해당 마이크로서비스에서 처리하는 것) / 서킷 브레이커를 선언하면 이런 규칙을 충족시킨다.
// 서킷 브레이커를 선언할 때는 @HystrixCommand를 메서드에 지정하고, 실패시 동작할 폴백 메서드를 지정하면 된다.

// 폴백 메서드는 원래 메서드와 시그니처(매개변수, 리턴 타입)가 동일해야한다.
// 또한 폴백 체인을 만들 수 있지만, 폴백 스택의 제일 밑에는 실행에 실패하지 않는, 서킷 브레이커가 필요없는 메서드가 있어야 한다.
// 기본적으로 @HystrixCommand가 지정된 모든 메서드는 1초 후에 타임아웃 되고, 이 메서드의 폴백 메서드가 호출된다.

// 고려 시간(20초) 동안에, 호출 임계 횟수와 실패 호출 비율이 모두 초과될 경우 , 서킷은 열림 상태가 되며, 이후의 모든 호출은 폴백 메서드가 처리한다.
// 열림 유지 시간 (60초) 이후, 절반-열림 상태가 되며, 원래 메서드에 대한 호출이 가능하다. 