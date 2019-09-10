package it.plansoft.kafka.consumer.example;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class AppConsumer {

    private static final String KAFKA_BROKERS = "172.18.0.3:9092";
    private static final String TOPIC_NAME = "test-topic";
    private static final int PAUSE_UNTIL_MESSAGE = 1000;
    private static final int MESSAGE_COUNT = 30;

    public static void main(String[] args) {
        runConsumer();
    }

    private static void runConsumer() {
        Consumer<Long, String> consumer = createConsumer();

        int message_counter = 0;

        while (message_counter < MESSAGE_COUNT) {
            ConsumerRecords<Long, String> consumerRecords = consumer.poll(Duration.ofMillis(PAUSE_UNTIL_MESSAGE));

            consumerRecords.forEach(record -> {
                System.out.println(String.format("Record key %s", record.key()));
                System.out.println(String.format("Record value %s", record.value()));
                System.out.println(String.format("Record partition %s", record.partition()));
                System.out.println(String.format("Record offset %s", record.offset()));
            });

            consumer.commitAsync();

            message_counter += 1;
        }

        consumer.close();
    }

    private static Consumer<Long, String> createConsumer() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKERS);

        Consumer<Long, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(TOPIC_NAME));

        return consumer;
    }

}
