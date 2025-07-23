package com.thierry.kafkastreams.producer;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import com.thierry.kafkastreams.config.KafkaConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@EnableAsync
public class Producer {

	@Autowired
	private MessageGenerator messageGenerator;

	@Autowired
	private KafkaConfiguration kafkaConfiguration;

	@Autowired
	private KafkaTemplate<String, List<String>> kafkaTemplate;

	private final AtomicInteger atomicInteger = new AtomicInteger();

	@Async
	@Scheduled(fixedRate = 10000, initialDelay = 2000)
	public void sendMessage() {

		final List<String> content = messageGenerator.generateMessage();

		// final UUID key = UUID.randomUUID();
		final String key = "key_" + atomicInteger.getAndIncrement();

		final ProducerRecord<String, List<String>> message = new ProducerRecord<>(kafkaConfiguration.getInputTopic(),
				key.toString(), content);
		message.headers().add("message-id", UUID.randomUUID().toString().getBytes());

		final ListenableFuture<SendResult<String, List<String>>> listenable = this.kafkaTemplate.send(message);
		final CompletableFuture<SendResult<String, List<String>>> sendResult = listenable.completable();
		sendResult.whenComplete((result, ex) -> {
			if (ex != null) {
				log.error("An error occured while sending message", ex);
			} else {
				log.info("message sent with success" + result);
			}
		});

		final List<String> content1 = messageGenerator.generateMessage();
		final ProducerRecord<String, List<String>> message1 = new ProducerRecord<>(kafkaConfiguration.getInputTopic(),
				key.toString(), content1);
		message1.headers().add("message-id", UUID.randomUUID().toString().getBytes());
		this.kafkaTemplate.send(message1);
	}
}
