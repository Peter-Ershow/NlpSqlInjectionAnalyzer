package com.example.nlpsqlinjectionanalyzer.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestClientService {

    private final RestTemplate restTemplate;

    public RestClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getUsers() {
        String url = "http://localhost:8080/users";
        return restTemplate.getForObject(url, String.class);
    }
}
