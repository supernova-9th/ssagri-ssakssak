package com.supernova.ssagrissakssak.feed.persistence.repository;

import com.supernova.ssagrissakssak.core.config.DefaultJpaRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;

import java.util.Optional;

public interface BoardRepository extends DefaultJpaRepository<BoardEntity, Long> {

    /**
     * contentId로 BoardEntity를 찾는 메서드입니다.
     *
     * @param contentId 찾고자 하는 게시물의 contentId
     * @return 찾은 BoardEntity. 없으면 null을 반환합니다.
     */
    Optional<BoardEntity> findByContentId(String contentId);
}
