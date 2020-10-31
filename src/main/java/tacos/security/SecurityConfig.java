package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	// 스프링 시큐리티의 사용자 DB 스키마를 사용 전에 , 이미 사용자 정보 테이블 및 열 이름 정해져있고 , 쿼리가 미리 생성 되어 있다.
	// 사전 지정된 테이블 & 쿼리 사용하려면, 그것에 맞춰서 관련 테이블을 생성 및 사용자 데이터 추가 필요 (schema.sql, data.sql을 사용)
	// 사전 지정된 사용자 및 권한 테이블이 어떻게 구성되어있는지는 schema.sql을 참고
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {       // 경로 접근 제어 (HTTP 보안을 구성하는 메서드)
		
		http.authorizeRequests()
		.antMatchers("/design", "/orders").access("hasRole('ROLE_USER')")			// 이 메서드는 ExpressionInterceptUrlRegistry 객체를 반환
		.antMatchers("/", "/**").access("permitAll")												  // access의 인자로 SpEL (스프링 표현식 언어) 를 사용할 수 있다. 
		.and().formLogin().loginPage("/login")																// 로그인이 필요할 경우 이 경로로 리다이렉트
		.and().logout().logoutSuccessUrl("/")
		.and().csrf();
		
		//.defaultSuccessUrl("/design", true)                                 // 두번째 인자로 true를 전달하면, 로그인 전에 어떤 페이지였든 design 페이지로 이동
	}
	
//	@Autowired
//	DataSource dataSource;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder encoder() {     // Bean을 통해 이 메서드가 생성한 BCrypt.. 인스턴스가 등록 관리되며, 이 인스턴스가 주입되어 반환된다.
		return new BCryptPasswordEncoder();  // 이렇게하여 원하는 종류의 encoder 빈 객체를 스프링 관리 하에 사용 (클래스와 인스턴스 생성 및 주입의 전과정을 스프링이 관리하는 @Component와는 다르다)
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {  // 사용자 정보 등록 (사용자 인증 정보를 구성하는 메서드)
																																							 // (InMemory, JDBC, LDAP, 커스텀사용자 명세) 사용자 스토어 중에 선택하여, 여기서 구성한다.
		
		auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
		
		//		auth.jdbcAuthentication().dataSource(dataSource)                           // DB 액세스 방법을 알 수 있도록 dataSource를 설정해야한다.
		//		// 이 메서드들을 사용해 , 스프링 시큐리티의 기본 테이블이 아닌 다른 테이블에서 쿼리할 수 있다 (열의 데이터 타입과 길이는 같아야한다 / 매개변수는 하나, username이어야하고, 아래 3개의 값을 반환해야함)
		//		.usersByUsernameQuery("select username, password, enabled from users where username=?")
		//		.authoritiesByUsernameQuery("select username, authority from authorities where username=?")
		//		.passwordEncoder(new NoEncodingPasswordEncoder());  // 스프링 시큐리티의 PasswordEncoder 인터페이스를 구현하는 어떤 객체도 인자로 받을 수 있다.
		
		//		auth.inMemoryAuthentication()																							 
		//		.withUser("user1").password("{noop}password1").authorities("ROLE_USER")    // 지금은 InMemory 사용자 스토어에 사용자를 정의하고 있다.
		//		.and()																																		 // 테스트를 위해 {noop}을 이용하여 비암호화, 스프링5부터는 원래 반드시 암호화해야한다.
		//		.withUser("user2").password("{noop}password2").authorities("ROLE_USER");
	}
}
