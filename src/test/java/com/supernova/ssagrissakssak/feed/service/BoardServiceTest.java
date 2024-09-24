package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.core.wrapper.PageResponse;
import com.supernova.ssagrissakssak.feed.controller.request.BoardSearchRequest;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        BoardEntity board = boardRepository.save(BoardFixture.get());
        hashtagRepository.save(HashtagFixture.get(board.getId()));
        BoardSearchRequest searchRequest = new BoardSearchRequest("test", "FACEBOOK", "created_at", "title", "Test", 10, 0);

        PageRequest pageRequest = PageRequest.of(searchRequest.page(), searchRequest.pageCount());

        // When
        PageResponse<BoardResponse> result = boardService.getAllDetailBoards(searchRequest, pageRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContents()).isNotEmpty();
        assertEquals(1, result.getContents().size());

    }

    @Test
    void 해시태그_완전일치_하지않으면_게시물목록_검색되지_않는다() {

        // Given
        BoardEntity board = boardRepository.save(BoardFixture.get());
        hashtagRepository.save(HashtagFixture.get(board.getId()));
        BoardSearchRequest searchRequest = new BoardSearchRequest("tes", null, null, null, null, null, null);

        PageRequest pageRequest = PageRequest.of(searchRequest.page(), searchRequest.pageCount());

        // When
        PageResponse<BoardResponse> result = boardService.getAllDetailBoards(searchRequest, pageRequest);

        // Then
        assertEquals(0, result.getContents().size());
    }

}