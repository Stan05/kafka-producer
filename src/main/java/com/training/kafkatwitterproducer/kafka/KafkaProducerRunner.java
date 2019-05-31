package com.training.kafkatwitterproducer.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class KafkaProducerRunner implements Runnable, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerRunner.class);

    @Autowired
    private KafkaProducer<String, String> kafkaProducer;

    @Autowired
    @Qualifier("blockingQueue")
    private BlockingQueue<String> blockingQueue;

    public KafkaProducerRunner(KafkaProducer<String, String> kafkaProducer, BlockingQueue<String> blockingQueue) {
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void run() {
        while(true) {
            String value = null;
            try {
                value = blockingQueue.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (value != null) {
                ProducerRecord<String, String> record = new ProducerRecord<>("first_topic", value);
                send(record);
            }
        }
    }

    private void send(ProducerRecord<String, String> record) {
        kafkaProducer.send(record, (recordMetadata, ex) -> {
            if(ex == null){
                LOGGER.info("Received new metatdata. \n" +
                        "Topic :" + recordMetadata.topic() +
                        "\nPartition :" + recordMetadata.partition() +
                        "\nOffset :" +recordMetadata.offset() +
                        "\nTimestamp :" + recordMetadata.timestamp());
            }else {
                LOGGER.error("Error while producing.", ex);
            }
        });
    }

    @Override
    public void destroy() throws Exception {
        LOGGER.info("Flushing and closing Kafka Producer.");
        kafkaProducer.flush();
        kafkaProducer.close();
    }
}
