package tacos.email;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data

// 모든 변수들이 final 변수인 것들은 생성자 초기화 애노테이션을 붙인다?
// 초기화 되지 않은 모든 final 필드, @NonNull로 마크돼있는 모든 필드들에 대한 생성자를 자동으로 생성
@RequiredArgsConstructor

// 필드들이 final로 생성되어 있으면 필드를 초기화할 수 없기 때문에 오류가 발생하는데, 
// 이때 (force=true) 옵션을 이용해서 final 필드를 0, false, null 등으로 초기화를 강제로 시켜서 생성자를 만들 수 있다.
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
public class Ingredient {
	private final String code;
	private final String name;
}

