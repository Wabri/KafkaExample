package it.plansoft.kafka.producer.example;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;


public class AppProducer {

    private final static String TOPIC = "test-topic";
    private final static String BOOTSTRAP_SERVERS = "";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        runProducer(100);
    }

    private static void runProducer(int sendMessageCount) throws ExecutionException, InterruptedException {
        final Producer<String, String> producer = createProducer();
        long time = System.currentTimeMillis();

        try {
            for (long index = time; index < time + sendMessageCount; index++) {
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>(
                        TOPIC,
                        String.valueOf(index),
                        String.format("My message %s", index)
                );
                RecordMetadata recordMetadata = producer.send(producerRecord).get();

                long elapsedTime = System.currentTimeMillis() - time;

                System.out.println(String
                        .format("Producer send message with partition %s and topic %s in %s milliseconds ",
                                String.valueOf(recordMetadata.partition()),
                                recordMetadata.topic(),
                                String.valueOf(elapsedTime)
                        )
                );
            }
        } finally {
            producer.flush();
            producer.close();
        }

    }

    private static Producer<String, String> createProducer() {
        Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "Java-Producer");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return new KafkaProducer<String, String>(properties);
    }

}
