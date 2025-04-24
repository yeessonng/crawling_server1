package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CrawlingController {

    private final CrawlingService crawlingService;

    @PostMapping("/crawl")
    public ResponseEntity<Map<String, String>> crawl(@RequestBody Map<String, String> request) {
        String url = request.get("url");
        Map<String, String> result = crawlingService.fetchWishData(url);
        return ResponseEntity.ok(result);
    }
}
