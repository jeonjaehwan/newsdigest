package com.newsdigest.newsdigest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NewsDigestApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsDigestApplication.class, args);
	}

}
