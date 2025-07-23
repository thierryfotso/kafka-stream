package com.thierry.kafkastreams.producer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

@Service
public class MessageGenerator {

	private final Random random = new Random();
	private final AtomicInteger atomicInteger = new AtomicInteger();

	public List<String> generateMessage() {
		final List<String> contents = new ArrayList<>();
		final int messageCount = random.nextInt(1, 10);
		for (int i = 0; i < messageCount; i++) {
			contents.add("message_" + atomicInteger.getAndIncrement());
		}
		return contents;
	}
}
