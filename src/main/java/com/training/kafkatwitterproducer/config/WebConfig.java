package com.training.kafkatwitterproducer.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class WebConfig {

    @Bean
    @Qualifier("blockingQueue")
    public BlockingQueue<String> configureDefaultBlockingQueue() {
        return new LinkedBlockingQueue<>(100000);
    }

    @Bean
    @Qualifier("weather-api-headers")
    public HttpHeaders configureHeadersForWeatherAPI() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-RapidAPI-Host", "community-open-weather-map.p.rapidapi.com");
        httpHeaders.add("X-RapidAPI-Key", "8c630f6d7amsh31688caff0ef527p13ab90jsnf767883c6c75");

        return httpHeaders;
    }
}
