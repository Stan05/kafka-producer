package com.training.kafkatwitterproducer.kafka;

import com.twitter.hbc.core.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.concurrent.BlockingQueue;

public class KafkaTwitterProducer implements DisposableBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaTwitterProducer.class);
    @Autowired
    private Client hosebirdClient;

    @Autowired
    @Qualifier("blockingQueue")
    private BlockingQueue<String> msgQueue;


    public KafkaTwitterProducer(Client hosebirdClient) {
        this.hosebirdClient = hosebirdClient;
    }

    public void start(){
        this.hosebirdClient.connect();
        while (!hosebirdClient.isDone()) {
            try {
                String msg = msgQueue.take();
                LOGGER.info("Message received.");
                LOGGER.info(msg);
            } catch (InterruptedException e) {
                LOGGER.error("Exception during message retrieval", e);
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        this.hosebirdClient.stop();
    }
}
