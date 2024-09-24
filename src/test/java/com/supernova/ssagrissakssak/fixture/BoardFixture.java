package com.supernova.ssagrissakssak.fixture;

import com.supernova.ssagrissakssak.core.enums.ContentType;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;

public class BoardFixture {

    public static BoardEntity get() {
        return BoardEntity.builder()
                .type(ContentType.FACEBOOK)
                .title("Test")
                .content("Content")
                .viewCount(10)
                .likeCount(5)
                .shareCount(2)
                .build();
    }

    public static BoardEntity get(int viewCount, int likeCount, int shareCount) {
        return BoardEntity.builder()
                .type(ContentType.FACEBOOK)
                .title("Test")
                .content("Content")
                .viewCount(viewCount)
                .likeCount(likeCount)
                .shareCount(shareCount)
                .build();
    }

    public static BoardEntity get(ContentType contentType, int viewCount, int likeCount, int shareCount) {
        return BoardEntity.builder()
                .type(contentType)
                .title("Test")
                .content("Content")
                .viewCount(viewCount)
                .likeCount(likeCount)
                .shareCount(shareCount)
                .build();
    }

    public static BoardEntity get(String title, String content, int viewCount, int likeCount, int shareCount) {
        return BoardEntity.builder()
                .type(ContentType.FACEBOOK)
                .title(title)
                .content(content)
                .viewCount(viewCount)
                .likeCount(likeCount)
                .shareCount(shareCount)
                .build();
    }

    public static BoardEntity get(String title, String content) {
        return BoardEntity.builder()
                .type(ContentType.FACEBOOK)
                .title(title)
                .content(content)
                .viewCount(10)
                .likeCount(5)
                .shareCount(2)
                .build();
    }
}
