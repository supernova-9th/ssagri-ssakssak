package com.supernova.ssagrissakssak.feed.persistence.repository;

import com.supernova.ssagrissakssak.core.config.DefaultJpaRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import com.supernova.ssagrissakssak.feed.persistence.repository.custom.BoardRepositoryCustom;

public interface BoardRepository extends DefaultJpaRepository<BoardEntity, Long>, BoardRepositoryCustom {
}
