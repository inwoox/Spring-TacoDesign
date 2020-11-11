package tacos.email;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;


//	EmailProperties를 구성 속성으로 설정 가능하게 하고, 이 속성들을 통해, 설정 시간마다 이메일을 확인한다.

@Data
@ConfigurationProperties(prefix="tacocloud.email")
@Component
public class EmailProperties {
	
	private String username;
	private String password;
	private String host;
	private String mailbox;
	private long pollRate = 30000;
	
	public String getImapUrl() {
		return String.format("imaps://%s:%s@%s/%s", this.username, this.password, this.host, this.mailbox);
	}
}