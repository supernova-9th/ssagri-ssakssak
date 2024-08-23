package com.supernova.ssagrissakssak.feed.external;

import com.supernova.ssagrissakssak.core.enums.ContentType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SnsApiClient {

    private final RestTemplate restTemplate;

    public SnsApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean likeApiStatus(ContentType type, String contentId) {
        String url = getLikeUrl(type, contentId);
        try {
            restTemplate.postForEntity(url, null, String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private String getLikeUrl(ContentType type, String contentId) {
        switch (type) {
            case FACEBOOK: return "https://www.facebook.com/likes/" + contentId;
            case TWITTER: return "https://www.twitter.com/likes/" + contentId;
            case INSTAGRAM: return "https://www.instagram.com/likes/" + contentId;
            case THREADS: return "https://www.threads.net/likes/" + contentId;
            default: throw new IllegalArgumentException("지원하지 않는 서비스 입니다.");
        }
    }

}
