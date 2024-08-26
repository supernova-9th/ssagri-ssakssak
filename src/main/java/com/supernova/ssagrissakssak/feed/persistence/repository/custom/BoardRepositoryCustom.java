package com.supernova.ssagrissakssak.feed.persistence.repository.custom;

import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;

import java.util.List;

public interface BoardRepositoryCustom {
    List<BoardEntity> getBoards(String hashtag, String type, String orderBy, String searchBy, String search, Integer pageCount, Integer page);
}
