package com.training.kafkatwitterproducer.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Component
public class WeatherAPIService implements Runnable{

    private final static Logger LOGGER = LoggerFactory.getLogger(WeatherAPIService.class);

    private final List<String> cities;

    @Autowired
    @Qualifier("weather-api-headers")
    private HttpHeaders httpHeaders;

    @Autowired
    @Qualifier("blockingQueue")
    private BlockingQueue<String> blockingQueue;


    public WeatherAPIService(HttpHeaders httpHeaders, BlockingQueue<String> blockingQueue) {
        this.httpHeaders = httpHeaders;
        this.blockingQueue = blockingQueue;
        this.cities = Arrays.asList("London", "Sofia", "Paris", "Manchester", "Zagreb", "Varna","Sliven");
    }

    @Override
    public void run() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://community-open-weather-map.p.rapidapi.com/weather";

        for (String city : cities) {
            LOGGER.info("City : " + city);
            URI uri = UriComponentsBuilder.fromHttpUrl(url).queryParam("q", city).build().toUri();
            ResponseEntity<String> exchange = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(httpHeaders), String.class);
            String responseBody = exchange.getBody();
            blockingQueue.add(responseBody);
            LOGGER.info(responseBody);
        }
    }
}
