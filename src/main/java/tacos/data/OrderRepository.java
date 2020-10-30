package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {  
	// JPA는 JDBC와 달리, 실행시 인터페이스 구현체(클래스)를 자동으로 생성해준다. (JdbcOrderRepository 같은 인터페이스 구현체 클래스를 만들 필요가 없다)
	// 그러므로 JDBC에서 했던 바와 같이 그것들을 컨트롤러에 주입만 하면 된다.
}
