package com.example.repository;

import com.example.model.Analytics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlAnalyticsRepo extends MongoRepository<Analytics, String> {
    Optional<Analytics> findByHashedUrl(String hashedUrl);

    @Query("{ 'hashedUrl': ?0 }")
    @Update("{ '$inc': { 'clickCnt': 1, 'clickByCountry.?1': 1 } }")
    void incrementClickData(String hashedUrl, String country);

    List<Analytics> findByUserId(String userId);
}
