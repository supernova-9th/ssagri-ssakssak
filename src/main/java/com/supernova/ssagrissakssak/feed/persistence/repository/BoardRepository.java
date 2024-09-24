package com.supernova.ssagrissakssak.feed.persistence.repository;

import com.supernova.ssagrissakssak.core.config.DefaultJpaRepository;
import com.supernova.ssagrissakssak.feed.controller.request.BoardSearchRequest;
import com.supernova.ssagrissakssak.feed.persistence.repository.custom.BoardRepositoryCustom;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

/**
 * 게시물 엔티티에 대한 데이터 액세스를 담당하는 리포지토리 인터페이스입니다.
 * 이 인터페이스는 JPA 기본 기능과 커스텀 쿼리 기능을 모두 제공합니다.
 */
public interface BoardRepository extends DefaultJpaRepository<BoardEntity, Long>, BoardRepositoryCustom {

    /**
     * 주어진 ID에 해당하는 게시물 조회
     *
     * @param id 조회할 게시물의 ID
     * @return 조회된 게시물을 Optional로 감싸서 반환. 게시물이 없는 경우 빈 Optional 반환
     */
    Optional<BoardEntity> findById(Long id);

    Page<BoardEntity> getAllBoards(BoardSearchRequest searchRequest, PageRequest pageRequest);
}
