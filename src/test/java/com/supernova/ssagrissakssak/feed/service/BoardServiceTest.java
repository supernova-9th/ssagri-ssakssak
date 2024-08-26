package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.client.SnsApiClient;
import com.supernova.ssagrissakssak.core.enums.ContentType;
import com.supernova.ssagrissakssak.core.exception.BoardNotFoundException;
import com.supernova.ssagrissakssak.feed.persistence.repository.BoardRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private SnsApiClient snsApiClient;

    private BoardEntity testBoard;

    @BeforeEach
    void setUp() {
        Long boardId = 1L;
        testBoard = BoardEntity.builder()
                .id(boardId)
                .type(ContentType.FACEBOOK)
                .likeCount(10)
                .build();
    }

    @Test
    void addLikeBoardContent_Success() {
        // given
        when(boardRepository.findById(testBoard.getId())).thenReturn(Optional.of(testBoard));
        when(snsApiClient.callSnsLikeApi(any())).thenReturn(new SnsApiClient.SnsApiResponse(true));

        // when
        boardService.addLikeBoardContent(testBoard.getId());

        // then
        verify(boardRepository).findById(testBoard.getId());
        verify(snsApiClient).callSnsLikeApi(any());
        assertEquals(11, testBoard.getLikeCount());
    }

    @Test
    void addLikeBoardContent_BoardNotFound() {
        // given
        when(boardRepository.findById(testBoard.getId())).thenReturn(Optional.empty());

        // when & then
        assertThrows(BoardNotFoundException.class, () -> boardService.addLikeBoardContent(testBoard.getId()));
        verify(boardRepository).findById(testBoard.getId());
        verifyNoInteractions(snsApiClient);
    }

    @Test
    void addLikeBoardContent_SnsApiFailure() {
        // given
        when(boardRepository.findById(testBoard.getId())).thenReturn(Optional.of(testBoard));
        when(snsApiClient.callSnsLikeApi(any())).thenReturn(new SnsApiClient.SnsApiResponse(false));

        // when
        boardService.addLikeBoardContent(testBoard.getId());

        // then
        verify(boardRepository).findById(testBoard.getId());
        verify(snsApiClient).callSnsLikeApi(any());
        assertEquals(10, testBoard.getLikeCount());
    }

}