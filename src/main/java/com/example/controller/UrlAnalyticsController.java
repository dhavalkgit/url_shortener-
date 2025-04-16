package com.example.controller;

import com.example.model.Analytics;
import com.example.service.urlanalytis.UrlAnalytics;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
@AllArgsConstructor
public class UrlAnalyticsController {
    private final UrlAnalytics urlAnalytics;

    @GetMapping("/{hashedUrl}")
    public ResponseEntity<?> FullAnalytics(@PathVariable String hashedUrl){
        Analytics fullAnalytics = urlAnalytics.getFullAnalytics(hashedUrl);
        return ResponseEntity.status(HttpStatus.FOUND).body(fullAnalytics);
    }

    @GetMapping("/country/{hashedUrl}")
    public ResponseEntity<?> ClickByCountry(@PathVariable String hashedUrl){
        Map<String, Long> clickByCountry = urlAnalytics.getClickByCountry(hashedUrl);
        return ResponseEntity.status(HttpStatus.FOUND).body(clickByCountry);
    }

    @GetMapping("/click/{hashedUrl}")
    public ResponseEntity<?> totalClick(@PathVariable String hashedUrl){
        Long totalClick = urlAnalytics.getTotalClick(hashedUrl);
        return ResponseEntity.status(HttpStatus.FOUND).body(Map.of("total Click", totalClick));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getAnalyticsByUser(@PathVariable String userId){
        List<Analytics> analyticsByUser = urlAnalytics.getAnalyticsByUser(userId);

        if(analyticsByUser.isEmpty()){
            ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(Map.of("Message", "There is no analytics data present for given user"));
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(analyticsByUser);
    }
}
