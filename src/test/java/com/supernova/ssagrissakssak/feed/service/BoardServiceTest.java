package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.core.enums.ContentType;
import com.supernova.ssagrissakssak.feed.controller.response.BoardDetailResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.BoardHashtagRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.BoardRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.HashtagRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardHashtagEntity;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.HashtagEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    @Autowired
    private BoardHashtagRepository boardHashtagRepository;

    private BoardEntity board;

    @BeforeEach
    void setUp() {

        // 해시태그 엔티티 생성
        HashtagEntity hashtag = HashtagEntity.builder()
                .hashtag("#example")
                .build();

        hashtagRepository.save(hashtag);

        // 게시물 엔티티 생성
        LocalDateTime now = LocalDateTime.now();
        board = BoardEntity.builder()
                .type(ContentType.FACEBOOK)
                .title("제목 테스트1")
                .content("테스트 내용 입니다.")
                .viewCount(10)
                .likeCount(5)
                .shareCount(2)
                .build();
        boardRepository.save(board);

        // BoardHashtag 엔티티 생성 및 관계 설정
        BoardHashtagEntity boardHashtag = BoardHashtagEntity.builder()
                .board(board)
                .hashtag(hashtag)
                .build();
        boardHashtagRepository.save(boardHashtag);

        board.getBoardHashtags().add(boardHashtag);

    }

    @Test
    @DisplayName("게시물 상세 조회 시 통과")
    void getBoard() {

        // When
        BoardDetailResponse result = boardService.getBoard(board.getId());

        // Then
        assertNotNull(result);
        assertEquals(board.getId(), result.getId());
    }

    @Test
    @DisplayName("게시물 상세 조회 시 조회수 1씩 증가 테스트")
    void getBoard_ViewCount_Up() {

        // Given
        int viewCount = board.getViewCount();

        // When
        BoardDetailResponse result = boardService.getBoard(board.getId());

        // Then
        assertEquals(result.getViewCount(), viewCount+1);
    }

}