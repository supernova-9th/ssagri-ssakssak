package com.supernova.ssagrissakssak.feed.controller.request;

import org.springframework.data.domain.PageRequest;

public record BoardSearchRequest(

        String hashtag, // 해시태그
        String type,    // SNS 타입
        String orderBy, // 정렬순서, 기본값 (created_at)
        String searchBy,    // 검색 기준, 기본값 (title, content)
        String search,  // 사용자 검색어
        Integer pageCount,  // 페이지 크기, 기본값 (10)
        Integer page    // 페이지 번호, 기본값 (0)
) {

    // 기본값을 설정하는 생성자
    public BoardSearchRequest {
        if (page == null || page < 0) {
            page = 0; // 기본값 0
        }
        if (pageCount == null || pageCount <= 0) {
            pageCount = 10; // 기본값 10
        }
        if (orderBy == null) {
            orderBy = "created_at";
        }
        if (searchBy == null) {
            searchBy = "title,content";
        }
    }

    public PageRequest toPageRequest() {
        return PageRequest.of(page, pageCount);
    }

}
