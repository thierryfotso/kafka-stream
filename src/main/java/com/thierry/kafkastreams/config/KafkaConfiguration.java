package com.thierry.kafkastreams.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka")
//https://docs.spring.io/spring-kafka/docs/2.8.2/reference/html/#record-listener
@EnableKafka
@EnableKafkaStreams
public class KafkaConfiguration {

	private String inputTopic;

	private String outputTopic;

	@Value("${spring.cloud.kafka.binder.brokers}")
	private String binder;

	// https://docs.spring.io/spring-kafka/docs/2.8.2/reference/html/#kafka-streams-example
	@Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
	KafkaStreamsConfiguration kStreamsConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "testStreams");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, binder);
		props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName());
		return new KafkaStreamsConfiguration(props);
	}

}