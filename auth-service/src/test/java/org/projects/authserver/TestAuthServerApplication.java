package org.projects.authserver;

import org.springframework.boot.SpringApplication;

public class TestAuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.from(AuthServerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
