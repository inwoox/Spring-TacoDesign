package tacos.data;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import tacos.Ingredient;
import tacos.Taco;

@Repository // 타코 저장, 타코 id에 맞는 식자재를 함께 저장
public class JdbcTacoRepository implements TacoRepository {
	private JdbcTemplate jdbc;
	
	public JdbcTacoRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
	
	@Override
	public Taco save(Taco taco) {
		long tacoId = CreateTacoNgetTacoId(taco);
		taco.setId(tacoId);
		for (Ingredient ingredient : taco.getIngredients()) {
			saveIngredientToTaco(ingredient,tacoId);
		}
		return taco;
	}
	
	private long CreateTacoNgetTacoId(Taco taco) {  // KeyHolder가 DB에서 생성되는 타코 ID를 제공한다. / 이전에 식자재 저장시 사용한 update 메서드로는 생성된 타코 ID를 얻을 수 없다.
		taco.setCreatedAt(new Date());                // 타코를 저장할 때, 해당 타코의 이름과 생성 시간을 Taco 테이블에 저장한다. 
		PreparedStatementCreator psc = new PreparedStatementCreatorFactory("insert into Taco (name, createdAt) values (?,?)", Types.VARCHAR, Types.TIMESTAMP)
				.newPreparedStatementCreator(Arrays.asList(taco.getName(), new Timestamp(taco.getCreatedAt().getTime())));
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(psc,keyHolder);
		return keyHolder.getKey().longValue();
	}
	
	private void saveIngredientToTaco(Ingredient ingredient, long tacoId) {
		jdbc.update("insert into Taco_Ingredients (taco, ingredient) values (?,?)", tacoId, ingredient);
	}
}
