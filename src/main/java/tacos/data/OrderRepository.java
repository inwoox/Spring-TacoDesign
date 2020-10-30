package tacos.data;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import tacos.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {  
	// JPA는 JDBC와 달리, 실행시 인터페이스 구현체(클래스)를 자동으로 생성해준다. (JdbcOrderRepository 같은 인터페이스 구현체 클래스를 만들 필요가 없다)
	// 그러므로 JDBC에서 했던 바와 같이 그것들을 컨트롤러에 주입만 하면 된다.
	
	
	// 스프링 데이터는 이 메서드가 Order 객체를 찾을려고 한다는 것을 안다. CrudRepo의 매개변수로 지정했기 때문 / 또한 메서드의 이름을 분석한다.
	// 그래서 이 메서드는 Order의 deliveryZip 속성과 일치하는 모든 개체를 찾게 된다. 
	
	// @Query("select.... ") - 복잡한 쿼리의 경우, 이런 형태로 메서드에 쿼리를 매핑할 수 있다.
	List<Order> findByDeliveryZip(String deliveryZip);
}
