package dauphine.fr.microservices.registre_eureka.registre_eureka;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class RegistreEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegistreEurekaApplication.class, args);
	}

}

