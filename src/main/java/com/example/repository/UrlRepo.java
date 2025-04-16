package com.example.repository;

import com.example.model.Url;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableMongoRepositories
public interface UrlRepo extends MongoRepository<Url, String> {
    Optional<Url> findByShortUrl(String shortUrl);

    @Query("{ 'shortUrl': ?0 }")
    @Update("{ '$inc': { 'clickCnt': 1 } }")
    void incrementClickCount(String shortUrl);
}
