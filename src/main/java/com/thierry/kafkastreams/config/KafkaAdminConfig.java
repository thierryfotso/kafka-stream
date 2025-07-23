package com.thierry.kafkastreams.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaAdminConfig {

	@Value("${spring.cloud.kafka.binder.brokers}")
	private String binder;

	private String inputTopic;

	private String outputTopic;

	@Bean
	KafkaAdmin kafkaAdmin() {
		final Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, binder);
		return new KafkaAdmin(configs);
	}

	@Bean
	NewTopic input() {
		return TopicBuilder.name(inputTopic).partitions(10).replicas(3).compact().build();
	}

	@Bean
	NewTopic output() {
		return TopicBuilder.name(outputTopic).partitions(10).replicas(3).build();
	}
}
