package tacos.web;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import tacos.data.UserRepository;
import tacos.security.RegistrationForm;

@Controller
@RequestMapping("/register")
public class RegistrationController {
	
	private UserRepository userRepo;
	private PasswordEncoder passwordEncoder;
	
	public RegistrationController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}
	
	@GetMapping
	public String registerForm() {
		return "registration";
	}
	
	@PostMapping
	public String processRegistration(RegistrationForm form) {  // 폼에서 유저를 등록하면, 제출된 폼 데이터가 RegistrationForm 객체와 바인딩 된다.
		userRepo.save(form.toUser(passwordEncoder));   // 폼 제출이 처리될 때 컨트롤러가 Encoder 객체를, toUser 메서드의 인자로 전달해 패스워드를 암호화한 User 객체를 만든다.
		return "redirect:/login";
	}
}
