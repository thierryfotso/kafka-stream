package com.thierry.kafkastreams.producer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaProducerConfig {

	@Value("${spring.cloud.kafka.binder.brokers}")
	private String bootstrapAddress;

	@Bean
	ProducerFactory<String, List<String>> producerFactory() {
		final Map<String, Object> configProps = getConfig();
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	private Map<String, Object> getConfig() {
		final Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return configProps;
	}

	@Bean
	KafkaTemplate<String, List<String>> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

}
