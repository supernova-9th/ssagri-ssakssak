package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.core.enums.ContentType;
import com.supernova.ssagrissakssak.feed.controller.response.BoardDetailResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.BoardRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.HashtagRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.HashtagEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    private BoardEntity savedBoard1;

    @BeforeEach
    void setUp() {
        boardRepository.deleteAll();
        hashtagRepository.deleteAll();

        BoardEntity board = BoardEntity.builder()
                .id(1L)
                .type(ContentType.FACEBOOK)
                .title("제목 테스트1")
                .content("내용 테스트1")
                .viewCount(10)
                .likeCount(5)
                .shareCount(2)
                .build();
        savedBoard1 = boardRepository.save(board);

        HashtagEntity hashtag1 = HashtagEntity.builder()
                .id(1L)
                .hashtag("#맛집")
                .boardId(savedBoard1.getId())
                .build();
        hashtagRepository.save(hashtag1);

        HashtagEntity hashtag2 = HashtagEntity.builder()
                .id(2L)
                .hashtag("#여행")
                .boardId(savedBoard1.getId())
                .build();
        hashtagRepository.save(hashtag2);

    }

    @Test
    void 게시물_상세조회_하면_모든_데이터를_가져온다() {
        // When
        BoardDetailResponse result = boardService.getBoard(savedBoard1.getId());

        // Then
        assertNotNull(result);
        assertEquals(savedBoard1.getId(), result.getId());
        assertEquals(savedBoard1.getTitle(), result.getTitle());
        assertEquals(savedBoard1.getContent(), result.getContent());
        assertEquals(savedBoard1.getType(), result.getType());
        assertEquals(savedBoard1.getLikeCount(), result.getLikeCount());
        assertEquals(savedBoard1.getCreatedAt(), result.getCreatedAt());
        assertEquals(savedBoard1.getUpdatedAt(), result.getUpdatedAt());

        List<String> hashtags = result.getHashtags();
        assertEquals(2, hashtags.size()); // 예상값 2
        assertTrue(hashtags.contains("#맛집"));
        assertTrue(hashtags.contains("#여행"));
    }

    @Test
    void 게시물_상세조회시_조회수가_1_증가한다() {
        // Given
        int viewCount = savedBoard1.getViewCount();

        // When
        BoardDetailResponse result = boardService.getBoard(savedBoard1.getId());

        // Then
        assertEquals(result.getViewCount(), viewCount+1);
    }

}