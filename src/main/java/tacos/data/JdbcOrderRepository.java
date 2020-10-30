package tacos.data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import tacos.Order;
import tacos.Taco;

@Repository // 오더를 저장 (오더 id 추출 및 오더 id에 해당되는 taco들 저장)
public class JdbcOrderRepository implements OrderRepository {
		private SimpleJdbcInsert orderInserter;
		private SimpleJdbcInsert orderTacoInserter;
		private ObjectMapper objectMapper;
		
		Logger logger = LoggerFactory.getLogger(this.getClass());
		
		@Autowired // JdbcTemplate 주입 , 인스턴스 변수에 직접 주입하는 대신 JdbcTemplate을 사용해서 두 개의 SimpleJdbcInsert 인스턴스를 생
		public JdbcOrderRepository(JdbcTemplate jdbc) {
			this.orderInserter = new SimpleJdbcInsert(jdbc)
					.withTableName("Taco_Order")
					.usingGeneratedKeyColumns("id");
			
			this.orderTacoInserter = new SimpleJdbcInsert(jdbc)
					.withTableName("Taco_Order_Tacos");
			
			this.objectMapper = new ObjectMapper();
		}
		
		@Override
		public Order save(Order order) {
			order.setPlacedAt(new Date());
			long orderId = getOrderId(order);
			order.setId(orderId);
			List<Taco> tacos = order.getTacos();
			
			for (Taco taco : tacos) {
				saveTacoToOrder(taco, orderId);
			}
			
			return order;
		}
		
		private long getOrderId(Order order) {
			@SuppressWarnings("unchecked")
			Map<String, Object> values = objectMapper.convertValue(order, Map.class); // order를 Map으로 변환, 키가 placedAt인 항목의 값을 order의 placedAt 값으로 변경
			values.put("placedAt",  order.getPlacedAt());
			
			long orderId = orderInserter.executeAndReturnKey(values).longValue();
			return orderId;
		}
		
		private void saveTacoToOrder(Taco taco, long orderId) {
			Map<String, Object> values = new HashMap<>(); // map의 키는 테이블의 열 이름 , 값은 테이블의 열의 값. 
			values.put("tacoOrder", orderId);
			values.put("taco", taco.getId());
			orderTacoInserter.execute(values);
		}
}
