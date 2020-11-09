package tacos.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;
import tacos.User;

@Data
public class RegistrationForm { // 폼에서 제출된 데이터를 담는 클래스 객체

		private String username;
		private String password;
		private String fullname;
		private String street;
		private String city;
		private String state;
		private String zip;
		private String phone;
		
	
		// 폼에서 제출된 데이터를 가지고 , User 객체를 만드는 메서드
		public User toUser(PasswordEncoder passwordEncoder) {
			return new User(username, passwordEncoder.encode(password),fullname,street,city,state,zip,phone);
		}
}
