package tacos.security;

import org.springframework.security.crypto.password.PasswordEncoder;

public class NoEncodingPasswordEncoder implements PasswordEncoder {
	@Override
	public String encode(CharSequence rawPwd) {
		return rawPwd.toString();
	}
	
	@Override
	public boolean matches(CharSequence rawPwd, String encodePwd) {
		return rawPwd.toString().equals(encodePwd);
	}
}
