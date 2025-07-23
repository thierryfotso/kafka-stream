package com.thierry.kafkastreams.serdes;

import java.util.ArrayList;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public final class CustomSerdes {

	// https://kafka.apache.org/31/documentation/streams/developer-guide/datatypes
	public static Serde<ArrayList> MessageList() {
		final JsonSerializer<ArrayList> serializer = new JsonSerializer<>();
		final JsonDeserializer<ArrayList> deserializer = new JsonDeserializer<>(ArrayList.class);
		return Serdes.serdeFrom(serializer, deserializer);
	}
}