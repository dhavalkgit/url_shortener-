package com.example.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Document(collection = "UrlDetails")
public class Url {
    @Id
    private String id;

    private final String longUrl;

    @Indexed(unique = true)
    private final String shortUrl;

    @CreatedDate
    private Date createdAt;
    private final String userId;

    public Url(String longUrl, String shortUrl, String userId) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
        this.userId=userId;
    }

}
