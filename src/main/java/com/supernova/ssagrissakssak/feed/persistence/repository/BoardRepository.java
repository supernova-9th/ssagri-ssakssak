package com.supernova.ssagrissakssak.feed.persistence.repository;

import com.supernova.ssagrissakssak.core.config.DefaultJpaRepository;
import com.supernova.ssagrissakssak.feed.controller.request.BoardSearchRequest;
import com.supernova.ssagrissakssak.feed.persistence.repository.custom.BoardRepositoryCustom;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface BoardRepository extends DefaultJpaRepository<BoardEntity, Long>, BoardRepositoryCustom {

    /**
     * Id로 BoardEntity를 찾는 메서드입니다.
     *
     * @param id 찾고자 하는 게시물의 Id
     * @return 찾은 BoardEntity. 없으면 null을 반환합니다.
     */
    Optional<BoardEntity> findById(Long id);

    Page<BoardEntity> getAllBoards(BoardSearchRequest searchRequest, PageRequest pageRequest);
}
