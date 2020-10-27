package br.edu.ufcg.computacao.alumni;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;

@SpringBootApplication(exclude = RepositoryRestMvcAutoConfiguration.class)
public class AlumniApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlumniApplication.class, args);
	}
}
