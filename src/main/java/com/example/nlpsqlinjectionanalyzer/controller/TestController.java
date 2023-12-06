package com.example.nlpsqlinjectionanalyzer.controller;

import com.example.nlpsqlinjectionanalyzer.tokenizer.SQLSchemaTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TestController {

    @Autowired
    private SQLSchemaTokenizer sqlSchemaTokenizer;
    @GetMapping("/test")
    public String testText(@RequestParam(name = "text", required = true) String text) throws IOException {
        return sqlSchemaTokenizer.getEntities(text);
    }
}
