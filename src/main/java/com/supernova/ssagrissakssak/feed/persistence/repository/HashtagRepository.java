package com.supernova.ssagrissakssak.feed.persistence.repository;

import com.supernova.ssagrissakssak.core.config.DefaultJpaRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.HashtagEntity;

import java.util.List;

public interface HashtagRepository extends DefaultJpaRepository<HashtagEntity, Long> {

    List<HashtagEntity> findAllByBoardId(Long boardId);

}
