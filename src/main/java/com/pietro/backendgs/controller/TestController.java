package com.pietro.backendgs.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public Map<String, Object> testEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "API is running");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    @GetMapping("/api/test")
    public Map<String, Object> testApiEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "API endpoint is accessible");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
} 