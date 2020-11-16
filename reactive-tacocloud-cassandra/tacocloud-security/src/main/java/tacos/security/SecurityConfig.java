package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation
             .authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web
             .builders.HttpSecurity;
import org.springframework.security.config.annotation.web
                        .configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web
                        .configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;



// 스프링 시큐리티의 웹 보안 모델은 서블릿 필터를 중심으로 만들어졌다. (권한을 확인하기 위해, 클라이언트의 요청을 서블릿이 받기 전에 가로채려면 서블릿 필터가 좋은 방법이다)
// 그러나 스프링 WebFlux에서는 이런 방법을 사용하기 어렵다. 리액티브 웹 애플리케이션은 Netty 등 non-서블릿 서버에 구축될 가능성이 높기 때문이다.

// 서블릿 필터를 사용할 수는 없지만, 스프링 시큐리티 5.0.0 버전부터, 스프링의 WebFilter가 이 일을 해준다.
// WebFilter는 서블릿 API에 의존하지 않는 스프링 특유의 서블릿 필터 같은 것이다. 
// 의존성은 MVC와 동일하게 spring-boot-starter-security를 사용한다.


@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  
  @Autowired
  private UserDetailsService userDetailsService;
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .authorizeRequests()
        .antMatchers(HttpMethod.OPTIONS).permitAll() // needed for Angular/CORS
        .antMatchers(HttpMethod.POST, "/api/ingredients").permitAll()
        .antMatchers("/design", "/orders/**")
            .permitAll()
            //.access("hasRole('ROLE_USER')")
        .antMatchers(HttpMethod.PATCH, "/ingredients").permitAll()
        .antMatchers("/**").access("permitAll")
        
      .and()
        .formLogin()
          .loginPage("/login")
          
      .and()
        .httpBasic()
          .realmName("Taco Cloud")
          
      .and()
        .logout()
          .logoutSuccessUrl("/")
          
      .and()
        .csrf()
          .ignoringAntMatchers("/h2-console/**", "/ingredients/**", "/design", "/orders/**", "/api/**")

      // Allow pages to be loaded in frames from the same origin; needed for H2-Console
      .and()  
        .headers()
          .frameOptions()
            .sameOrigin()
      ;
  }

  @Bean
  public PasswordEncoder encoder() {
//    return new StandardPasswordEncoder("53cr3t");
    return NoOpPasswordEncoder.getInstance();
  }
  
  
  @Override
  protected void configure(AuthenticationManagerBuilder auth)
      throws Exception {

    auth
      .userDetailsService(userDetailsService)
      .passwordEncoder(encoder());
    
  }

}


//	스프링 WebFlux 애플리케이션의 스프링 시큐리티 구성
//	EnableWebSecurity 대신, EnableWebFluxSecurity 애노테이션이 사용된다.

//	구성 클래스가 WebSecurityConfigurerAdapter의 서브 클래스가 아니며, 다른 곳에서 상속 받지도 않는다.
//	따라서 configure() 메서드도 오버라이딩하지 않는다.

//	configure() 대신 SecurityWebFilterChain 타입의 빈을 선언한다.
//	마지막에는 build 메서드를 호출해, 모든 보안 규칙을 SecurityWebFilterChain으로 조립하고 반환해야한다.
	
//	@Configuration
//	@EnableWebFluxSecurity
//	public class SecurityConfig {
//		@Bean
//		public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//			return http.authorizeExchange()
//					.pathMatchers("/design", "/orders").hasAuthority("USER")
//					.anyExchange().permitAll()
//					.and()
//					.build();
//		}
//	}

//	리액티브 사용자 명세 서비스 구성

//	리액티브 구성에서는 configure를 오버라이딩 하지 않고, 대신에 ReactiveUserDetailsService 빈을 선언한다.
//	이것은 UserDetailsService의 리액티브 버전이며, UserDetailsService처럼 하나의 메서드만 구현하면 된다.

//	이때 리포지토리는 리액티브 스프링 데이터 리포지토리가 되어야하며, findByUsername은 Mono를 반환한다.
//	따라서 Mono 타입에 사용 가능한 map 같은 오퍼레이션들을 연쇄적으로 호출할 수 있다.

//	@Service
//	public ReactiveUserDetailsService userDetailsService(UserRepository userRepo) {
//		return new ReactiveUserDetailsService() {
//			@Override
//			public Mono<UserDetails> findByUsername(String username) {
//				return userRepo.findByUsername(username)
//						.map(user -> { return user.toUserDetails(); });
//			}
//		};
//	}
