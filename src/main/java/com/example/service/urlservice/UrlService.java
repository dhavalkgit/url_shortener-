package com.example.service.urlservice;

import com.example.dto.ShortUrlRequestDto;
import com.example.dto.ShortUrlResponseDTO;
import com.example.dto.UrlRedirectionResponseDto;

public interface UrlService {
    public ShortUrlResponseDTO shortenUrl(String url, String token);
    public ShortUrlResponseDTO shortenUrl(ShortUrlRequestDto requestDto, String token);
    public UrlRedirectionResponseDto getLongUrl(String alias);
}
