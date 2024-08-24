package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.client.SnsApiClient;
import com.supernova.ssagrissakssak.core.enums.ContentType;
import com.supernova.ssagrissakssak.feed.persistence.repository.BoardRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        testBoard = BoardEntity.builder()
                .id(1L)
                .contentId("test123")
                .type(ContentType.FACEBOOK)
                .likeCount(10)
                .shareCount(5)
                .build();
    }
    @Test
    void addLikeBoardContent_Success() {
        when(boardRepository.findByContentId("test123")).thenReturn(testBoard);
        when(snsApiClient.callSnsLikeApi(any())).thenReturn(new SnsApiClient.SnsApiResponse(true));

        boardService.addLikeBoardContent("test123");

        verify(boardRepository).findByContentId("test123");
        verify(snsApiClient).callSnsLikeApi(any());
        verify(boardRepository).save(any(BoardEntity.class));
        assert testBoard.getLikeCount() == 11;
    }
}