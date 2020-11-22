
package tacos;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;



@Configuration
public class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 수동으로 클라이언트 등록시, 인증 절차가 없어야한다.
//    	http.authorizeRequests().anyRequest().permitAll();
    	
    	// 자동으로 클라이언트 등록시
    	http.authorizeRequests().antMatchers("/actuator/**").hasRole("ADMIN")
    	.antMatchers("/instances").hasRole("ADMIN")
    	.anyRequest().permitAll()
    	.and().httpBasic();
    }
    
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.inMemoryAuthentication()
    	.withUser("admin")
    	.password("{noop}genesis32")
    	.authorities("ROLE_ADMIN");
    }
}