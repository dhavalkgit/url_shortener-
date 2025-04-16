package com.example.service.urlanalytis;

import com.example.model.Analytics;

import java.util.List;
import java.util.Map;

public interface UrlAnalytics {
    public Analytics getFullAnalytics(String hashedUrl);
    public void incrementCounts(String hashedUrl, String ip, String userId);
    public Map<String, Long> getClickByCountry(String hashedUrl);
    public Long getTotalClick(String hashedUrl);
    public List<Analytics> getAnalyticsByUser(String userId);
}
