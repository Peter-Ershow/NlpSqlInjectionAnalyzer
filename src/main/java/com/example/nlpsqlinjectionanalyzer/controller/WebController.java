package com.example.nlpsqlinjectionanalyzer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/runner")
    public String showButtonPage() {
        return "buttonPage";
    }

}
