package com.example.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "AnalyticsDetails")
@Getter
@Setter
public class Analytics {
    @Id
    @Field(name = "_id")
    private String id;

    private long clickCnt;

    private Map<String, Long> clickByCountry;

    @Indexed(unique = true)
    private String hashedUrl;

    private String userId;

    public Analytics(String hashedUrl, String userId) {
        this.hashedUrl = hashedUrl;
        this.userId=userId;
        this.clickCnt=0L;
        this.clickByCountry=new HashMap<>();
    }

    public void incrementCnt(){
        clickCnt++;
    }
    public void incrementCountryCnt(String country){
        clickByCountry.put(country, clickByCountry.getOrDefault(country, 0L)+1);
    }
}

