package com.example.nlpsqlinjectionanalyzer.client;

import com.example.nlpsqlinjectionanalyzer.tokenizer.SQLSchemaTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestClientService {

    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(SQLSchemaTokenizer.class);

    public RestClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getUsers() {
        String url = "http://localhost:8080/users";
        return restTemplate.getForObject(url, String.class);
    }

    public String runQuery(String input) {
        StringBuilder allInfo = new StringBuilder();
        String url = "http://localhost:8080/" + input;
        logger.info("Calling safe");
        String result = restTemplate.getForObject(url, String.class);
        logger.info("Received :" + result);
        allInfo.append("Safe Call: " + result);
        logger.info("Calling unsafe");
        String resultUnsafe = restTemplate.getForObject(url + "' OR '1'='1", String.class);
        logger.info("Received Unsafe: " + resultUnsafe);
        allInfo.append(resultUnsafe);
        return resultUnsafe;
    }
}
