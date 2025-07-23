package com.thierry.kafkastreams.consumer;

import java.time.Duration;
import java.util.ArrayList;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.thierry.kafkastreams.config.KafkaConfiguration;
import com.thierry.kafkastreams.serdes.CustomSerdes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Configuration
@RequiredArgsConstructor
public class SampleConsumer {

	private final KafkaConfiguration kafkaConfiguration;

	@Bean
	KStream<String, ArrayList> kStream(final StreamsBuilder kStreamBuilder) {

		final KStream<String, ArrayList> stream = kStreamBuilder.stream(kafkaConfiguration.getInputTopic(),
				Consumed.with(Serdes.String(), CustomSerdes.MessageList()));

		final Duration windowSize = Duration.ofSeconds(10);
		final Duration gracePeriod = Duration.ofSeconds(5);
		final SessionWindows sessionWindow = SessionWindows.ofInactivityGapAndGrace(windowSize, gracePeriod);

		stream.groupByKey(Grouped.with(Serdes.String(), CustomSerdes.MessageList())).windowedBy(sessionWindow)
				.aggregate(ArrayList::new, (key, value, aggregate1) -> {
					aggregate1.addAll(value);
					return aggregate1;
				}, (key, aggregate1, aggregate2) -> {
					aggregate1.addAll(aggregate2);
					return aggregate1;
				}, Materialized.with(Serdes.String(), CustomSerdes.MessageList())).toStream()
				.map((key, value) -> new KeyValue<>(key.key(), value))
				.to(kafkaConfiguration.getOutputTopic(), Produced.with(Serdes.String(), CustomSerdes.MessageList()));

		return stream;
	}
}