package cn.com.liurz.flow;

import org.springframework.boot.SpringApplication;
// import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// activiti7集成了SpringBootSecurity，禁用后以后老的API即可用，新的API，比如ProcessRuntime和TaskRuntime还是需要验证
@SpringBootApplication(exclude =
        {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class})
public class ActivitiApplication {
	public static void main(String[] args) {
         SpringApplication.run(ActivitiApplication.class, args);
   }
}
