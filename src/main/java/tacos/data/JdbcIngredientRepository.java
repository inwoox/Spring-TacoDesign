package tacos.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import tacos.Ingredient;

@Repository  // 이 Annotation을 통해 스프링 컴포넌트 검색 - 스프링 컨텍스트의 빈으로 생성 - 생성자 + Autowired를 통해 해당 빈을 JdbcTemplate에 주입
public class JdbcIngredientRepository implements IngredientRepository {

		private JdbcTemplate jdbc;
		
		@Autowired 
		public JdbcIngredientRepository(JdbcTemplate jdbc) {
			this.jdbc = jdbc;
		}
		
		@Override
		public Iterable<Ingredient> findAll(){
			return jdbc.query("select id, name, type from Ingredient", this::mapRowToIngredient);
		}
		
		@Override
		public Ingredient findById(String id) {
			return jdbc.queryForObject("select id, name, type from Ingredient where id=?", this::mapRowToIngredient, id);
		}
		
		@Override
		public Ingredient save(Ingredient ingredient) {
			jdbc.update("insert into Ingredient (id, name, type) values (?,?,?)", ingredient.getId(), ingredient.getName(), ingredient.getType().toString());
			return ingredient;
		}
		
		// RowMapper 인터페이스를 구현한 메서드, 쿼리로 생성된 결과 세트(rs)의 행 개수만큼 호출되고, 행을 하나의 객체로 만들어낸다.
		private Ingredient mapRowToIngredient(ResultSet rs, int rowNum) throws SQLException {
			return new Ingredient(rs.getString("id"), rs.getString("name"),Ingredient.Type.valueOf(rs.getString("type")));
		}
}
