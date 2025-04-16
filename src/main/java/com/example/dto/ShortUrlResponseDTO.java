package com.example.dto;

import lombok.Getter;

public class ShortUrlResponseDTO {
    @Getter
    private String shortURL;

    public ShortUrlResponseDTO(String shortURL) {
        String domain = "http://localhost:8080/api/v1/url/";
        this.shortURL = domain +shortURL;
    }
}
