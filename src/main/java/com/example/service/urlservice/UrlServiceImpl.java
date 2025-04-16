package com.example.service.urlservice;

import com.example.dto.ShortUrlRequestDto;
import com.example.dto.ShortUrlResponseDTO;
import com.example.dto.UrlRedirectionResponseDto;
import com.example.exception.AliasException;
import com.example.exception.ResourceNotFound;
import com.example.exception.ShortUrlException;
import com.example.exception.URLException;
import com.example.model.Url;
import com.example.repository.UrlRepo;
import com.example.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final UrlRepo urlRepo;
    private final JwtService jwtService;

    private boolean isValidURL(String url){
        if(url.isEmpty()) return false;
        String urlRegex = "((http|https)://)(www.)?"
                + "[a-zA-Z0-9@:%._\\+~#?&//=]"
                + "{2,256}\\.[a-z]"
                + "{2,6}\\b([-a-zA-Z0-9@:%"
                + "._\\+~#?&//=]*)";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    private boolean isValidAlias(String alias){
        String regex = "^[A-Za-z0-9]{7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(alias);
        return matcher.matches();
    }

    @Override
    public ShortUrlResponseDTO shortenUrl(String url, String token) {
        boolean flag = true;

        if(!isValidURL(url)){
            throw new URLException("Entered Url is not valid. Please check your Url.");
        }
        String hashURL = null;
        do{
            hashURL = HashGenerator.generateHash(url);
            if(!hashURL.isEmpty()){
                Optional<Url> byShortUrl = urlRepo.findByShortUrl(hashURL);
                if(byShortUrl.isPresent()){
                    boolean res = byShortUrl.get().getLongUrl().equals(url);
                    if(res) return new ShortUrlResponseDTO(hashURL);
                }
                else{
                    flag=false;
                }
            }
        }while (flag);
        String userId = null;
        if(token != null){
            userId = jwtService.getEmail(token);
        }
        urlRepo.save(new Url(url, hashURL, userId));
        return new ShortUrlResponseDTO(hashURL);
    }

    @Override
    public ShortUrlResponseDTO shortenUrl(ShortUrlRequestDto requestDto, String token) {
        if(!isValidURL(requestDto.getLongUrl())){
            throw new URLException("Entered Url is not valid. Please check your Url.");
        }
        if(!isValidAlias(requestDto.getAlias())){
            throw new AliasException("Entered alias is not valid. Please check your alias.");
        }
        Optional<Url> byShortUrl = urlRepo.findByShortUrl(requestDto.getAlias());
        if(byShortUrl.isPresent()){
            throw new ShortUrlException("Entered alias is already present, please enter new alias.");
        }
        String userId=null;
        if(token!=null){
            userId = jwtService.getEmail(token);
        }
        urlRepo.save(new Url(requestDto.getLongUrl(), requestDto.getAlias(),userId));
        return new ShortUrlResponseDTO(requestDto.getAlias());
    }

    public UrlRedirectionResponseDto getLongUrl(String alias){
        Optional<Url> byShortUrl = urlRepo.findByShortUrl(alias);
        if(byShortUrl.isPresent()){
//            return byShortUrl.get().getLongUrl();
            return new UrlRedirectionResponseDto(byShortUrl.get().getLongUrl(),
                    byShortUrl.get().getUserId());
        }
        else{
            throw new ResourceNotFound("short url is not exist");
        }
    }
}
