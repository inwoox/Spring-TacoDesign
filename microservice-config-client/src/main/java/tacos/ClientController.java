package tacos;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

	private final ClientProps props;
	public ClientController(ClientProps props) {
		this.props = props;
	}
	
	@GetMapping("/hello")
	public String message() {
		return props.getMessage();
	}
}
