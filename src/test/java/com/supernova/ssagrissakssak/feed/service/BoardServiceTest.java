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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        // given
        when(boardRepository.findByContentId(testBoard.getContentId())).thenReturn(Optional.of(testBoard));
        when(snsApiClient.callSnsLikeApi(any())).thenReturn(new SnsApiClient.SnsApiResponse(true));

        // when
        boardService.addLikeBoardContent(testBoard.getContentId());

        // then
        verify(boardRepository).findByContentId(testBoard.getContentId());
        verify(snsApiClient).callSnsLikeApi(any());
        verify(boardRepository).save(testBoard);
        assertEquals(11, testBoard.getLikeCount());
    }
}