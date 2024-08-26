package com.supernova.ssagrissakssak.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContentType {
    FACEBOOK("https://www.facebook.com"),
    TWITTER("https://www.twitter.com"),
    INSTAGRAM("https://www.instagram.com"),
    THREADS("https://www.threads.net");

    private final String baseUrl;

    public String getLikeUrl(Long id) {
        return this.baseUrl + "/likes/" + id;
    }

    public String getShareUrl(Long id) {
        return this.baseUrl + "/share/" + id;
    }
}