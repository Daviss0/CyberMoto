package com.cybermoto;

import com.cybermoto.entity.User;
import com.cybermoto.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CybermotoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CybermotoApplication.class, args);
		System.out.println("running");


	}

	@Bean
	CommandLineRunner init(UserRepository userRepository) {
		return args -> {
			String email = "teste@cybermoto.com";

			User user = userRepository.findByEmail(email).orElse(null);

			if (user == null) {
				user = new User();
				user.setEmail(email);
				user.setType(com.cybermoto.enums.TypeUser.ADMIN);
			}

			// Sempre força a senha para "123456"
			user.setPassword(new BCryptPasswordEncoder().encode("123456"));

			userRepository.save(user);
			System.out.println(">>> Usuário atualizado/criado: teste@cybermoto.com / 123456");
		};
	}



}
