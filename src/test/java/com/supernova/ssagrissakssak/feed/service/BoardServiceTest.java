package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.feed.controller.response.BoardDetailResponse;
import com.supernova.ssagrissakssak.feed.controller.response.BoardResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.BoardRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.HashtagRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import com.supernova.ssagrissakssak.fixture.BoardFixture;
import com.supernova.ssagrissakssak.fixture.HashtagFixture;
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
        boardRepository.deleteAllInBatch();
        hashtagRepository.deleteAllInBatch();
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
        assertEquals(result.getViewCount(), viewCount + 1);
    }

    @Test
    void 모든파라미터_입력해서_게시물_목록_검색한다() {

        // Given
        BoardEntity board1 = boardRepository.save(BoardFixture.get(10, 2, 3));
        BoardEntity board2 = boardRepository.save(BoardFixture.get(20, 2, 3));

        hashtagRepository.save(HashtagFixture.get(board1.getId()));
        hashtagRepository.save(HashtagFixture.get(board2.getId()));

        // Given
        String hashtag = "test";
        String type = "FACEBOOK";
        String orderBy = "created_at";
        String searchBy = "title";
        String search = "Test";
        Integer pageCount = 10;
        Integer page = 0;

        // When
        List<BoardResponse> result = boardService.getBoards(hashtag, type, orderBy, searchBy, search, pageCount, page);

        // Then
        assertEquals(2, result.size());
        assertEquals(board1.getContent(), result.get(0).getContent());
        assertEquals(board2.getContent(), result.get(1).getContent());
        assertEquals(board1.getTitle(), result.get(0).getTitle());
        assertEquals(board2.getTitle(), result.get(1).getTitle());
    }

    @Test
    void 해시태그_완전일치_하지않으면_게시물목록_검색되지_않는다() {

        // Given
        BoardEntity board1 = boardRepository.save(BoardFixture.get(10, 2, 3));
        BoardEntity board2 = boardRepository.save(BoardFixture.get(20, 2, 3));

        hashtagRepository.save(HashtagFixture.get(board1.getId()));
        hashtagRepository.save(HashtagFixture.get(board2.getId()));

        // Given
        String hashtag = "tes";
        String type =null;
        String orderBy = null;
        String searchBy = null;
        String search = null;
        Integer pageCount = null;
        Integer page = null;

        // When
        List<BoardResponse> result = boardService.getBoards(hashtag, type, orderBy, searchBy, search, pageCount, page);

        // Then
        assertEquals(0, result.size());
    }

    @Test
    void 게시물목록_조회시_내용_글자수_20_제한() {

        // Given
        BoardEntity board1 = boardRepository.save(BoardFixture.get("제목","내용 테스트 20자 이상 문자열입니다.",10, 2, 3));

        hashtagRepository.save(HashtagFixture.get(board1.getId()));

        // Given
        String hashtag = null;
        String type =null;
        String orderBy = null;
        String searchBy = null;
        String search = null;
        Integer pageCount = null;
        Integer page = null;

        // When
        List<BoardResponse> result = boardService.getBoards(hashtag, type, orderBy, searchBy, search, pageCount, page);

        // Then
        assertNotEquals(board1.getContent().length(), result.get(0).getContent().length());
        assertTrue(result.get(0).getContent().length() <= 20);
    }
}