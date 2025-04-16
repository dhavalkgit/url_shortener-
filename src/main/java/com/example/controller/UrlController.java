package com.example.controller;

import com.example.dto.ShortUrlRequestDto;
import com.example.dto.ShortUrlResponseDTO;
import com.example.service.urlanalytis.UrlAnalytics;
import com.example.service.urlservice.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
@Tag(name = "Url controller")
public class UrlController {
    private static final Logger log = LoggerFactory.getLogger(UrlController.class);
    private final UrlService URLService;
    private final UrlAnalytics urlAnalytics;

    @PostMapping("/api/v1/short-url")
    @Operation(
            description = "Using this endpoint you create short url"
    )
    public ResponseEntity<ShortUrlResponseDTO> createShortUrl(@RequestParam String longUrl,
                                                              HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        String token = null;
        if(authorization != null && authorization.startsWith("Bearer ")){
            token = authorization.substring(7);
        }

        String res = request.getServerName()+"/"+URLService.shortenUrl(longUrl, token);


        return ResponseEntity.status(HttpStatus.CREATED).body(new ShortUrlResponseDTO(res));
    }

    @PostMapping("/api/v1/short-url/custom")
    @Operation(
            description = "using this endpoint create custom short url. alias for the short url is length 7" +
                    "and contains Base62 characters"
    )
    public ResponseEntity<ShortUrlResponseDTO> createCustomShortUrl(@RequestBody ShortUrlRequestDto requestDto,
                                                                    HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        String token = null;
        if(authorization != null && authorization.startsWith("Bearer ")){
            token = authorization.substring(7);
        }

        String res = request.getServerName()+"/"+URLService.shortenUrl(requestDto, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ShortUrlResponseDTO(res));
    }

    @GetMapping("/{shortUrl}")
    @Operation(
            description = "This endpoint is used to find long url based on input and redirect to long url"
    )
    public void getUrl(@PathVariable String shortUrl, HttpServletResponse response,
                       HttpServletRequest request) throws IOException {
        var res = URLService.getLongUrl(shortUrl);
        String ipAddress = request.getRemoteAddr();

        log.info("the ip address is {}", ipAddress);
        log.info("this is referer header {}", request.getHeader("referer"));
        if(res.username() != null)
            urlAnalytics.incrementCounts(shortUrl, ipAddress, res.username());

        response.sendRedirect(res.url());
        response.setStatus(307);
    }
}
