package com.training.kafkatwitterproducer;

import com.training.kafkatwitterproducer.kafka.KafkaProducerRunner;
import com.training.kafkatwitterproducer.weather.WeatherAPIService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KafkaTwitterProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaTwitterProducerApplication.class, args);
    }


    @Bean
    public CommandLineRunner commandLineRunner(WeatherAPIService weatherAPIProducer, KafkaProducerRunner kafkaProducerRunner) {
        return args -> {
            new Thread(weatherAPIProducer).start();
            new Thread(kafkaProducerRunner).start();
        };
    }
}
