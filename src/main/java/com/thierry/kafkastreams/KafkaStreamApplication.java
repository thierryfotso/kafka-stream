package com.thierry.kafkastreams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class KafkaStreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaStreamApplication.class, args);
	}

}
