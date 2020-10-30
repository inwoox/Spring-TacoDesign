package tacos.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import tacos.User;

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

		public String getUsername() {
			return username;
		}

		public String getPassword() {
			return password;
		}

		public String getFullname() {
			return fullname;
		}

		public String getStreet() {
			return street;
		}

		public String getCity() {
			return city;
		}

		public String getState() {
			return state;
		}

		public String getZip() {
			return zip;
		}

		public String getPhone() {
			return phone;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public void setFullname(String fullname) {
			this.fullname = fullname;
		}

		public void setStreet(String street) {
			this.street = street;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public void setState(String state) {
			this.state = state;
		}

		public void setZip(String zip) {
			this.zip = zip;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}
		
		
}
