package com.example.service.urlanalytis;

import com.example.exception.ResourceNotFound;
import com.example.repository.UrlAnalyticsRepo;
import com.example.model.Analytics;
import io.ipinfo.api.IPinfo;
import io.ipinfo.api.model.IPResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UrlAnalyticsImpl implements UrlAnalytics {
    private final UrlAnalyticsRepo urlAnalyticsRepo;
    private final IPinfo iPinfo;

    @Override
    public Analytics getFullAnalytics(String hashedUrl) {
        Optional<Analytics> byHashedUrl = urlAnalyticsRepo.findByHashedUrl(hashedUrl);
        if(byHashedUrl.isPresent()) return byHashedUrl.get();
        throw new ResourceNotFound("resource is not found");
    }

    @Override
    @Async
    public void incrementCounts(String hashedUrl, String ip, String userId) {
        try {
            log.info("thread is called");
            IPResponse ipResponse = iPinfo.lookupIP(ip);
            Optional<Analytics> byHashedUrl = urlAnalyticsRepo.findByHashedUrl(hashedUrl);
            Analytics analytics;
            if(byHashedUrl.isPresent()){
                String country = ipResponse.getCountryCode();
                urlAnalyticsRepo.incrementClickData(hashedUrl, country);
            }
            else{
                analytics = new Analytics(hashedUrl, userId);
                analytics.incrementCnt();
                analytics.incrementCountryCnt(ipResponse.getCountryCode());
                Analytics save = urlAnalyticsRepo.save(analytics);
            }
        }
        catch (Exception e){
            log.info(e.getMessage());
        }
    }

    @Override
    public Map<String, Long> getClickByCountry(String hashedUrl) {
        Optional<Analytics> byHashedUrl = urlAnalyticsRepo.findByHashedUrl(hashedUrl);
        if(byHashedUrl.isPresent()) return byHashedUrl.get().getClickByCountry();
        throw new ResourceNotFound("resource is not found");
    }

    @Override
    public Long getTotalClick(String hashedUrl) {
        Optional<Analytics> byHashedUrl = urlAnalyticsRepo.findByHashedUrl(hashedUrl);
        if(byHashedUrl.isPresent()) return byHashedUrl.get().getClickCnt();
        throw new ResourceNotFound("resource is not found");
    }

    @Override
    public List<Analytics> getAnalyticsByUser(String userId) {
        return urlAnalyticsRepo.findByUserId(userId);
    }
}
