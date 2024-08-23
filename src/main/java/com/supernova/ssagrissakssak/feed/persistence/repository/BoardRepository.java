package com.supernova.ssagrissakssak.feed.persistence.repository;

import com.supernova.ssagrissakssak.core.config.DefaultJpaRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends DefaultJpaRepository<BoardEntity, Long> {

    BoardEntity findByContentId(String contentId);

}
