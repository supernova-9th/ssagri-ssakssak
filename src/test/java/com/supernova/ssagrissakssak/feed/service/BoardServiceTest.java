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

    @BeforeEach
    void tearDown() {
        boardRepository.deleteAllInBatch();
        hashtagRepository.deleteAllInBatch();
    }

    @Test
    void 게시물_상세조회시_조회수가_1_증가한다() {
        // Given
        BoardEntity board = boardRepository.save(BoardFixture.get(10, 2, 3));
        hashtagRepository.save(HashtagFixture.get(board.getId()));

        int viewCount = board.getViewCount();

        // When
        BoardDetailResponse result = boardService.getBoard(board.getId());

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
        Integer pageCount = 1;
        Integer page = 20;

        // When
        List<BoardResponse> result = boardService.getBoards(hashtag, type, orderBy, searchBy, search, pageCount, page);

        // Then
        assertEquals(0, result.size());
    }

}