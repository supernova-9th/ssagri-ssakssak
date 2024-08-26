package com.supernova.ssagrissakssak.fixture;

import com.supernova.ssagrissakssak.feed.persistence.repository.entity.HashtagEntity;

public class HashtagFixture {

    public static HashtagEntity get(Long boardId) {
        return HashtagEntity.builder()
                .hashtag("test")
                .boardId(boardId)
                .build();
    }

    public static HashtagEntity get(String hashtag, Long boardId) {
        return HashtagEntity.builder()
                .hashtag(hashtag)
                .boardId(boardId)
                .build();
    }
}
