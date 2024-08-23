package com.supernova.ssagrissakssak.feed.service;


import com.supernova.ssagrissakssak.feed.external.SnsApiClient;
import com.supernova.ssagrissakssak.feed.persistence.repository.BoardRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final SnsApiClient snsApiClient;

    @Transactional
    public void likeBoardContent(String contentId) {

        BoardEntity board = boardRepository.findByContentId(contentId);

        if (board != null) {
            boolean success = snsApiClient.likeApiStatus(board.getType(), contentId);
            if (success) {
                board.incrementLikeCount();
                boardRepository.save(board);
            }
        }
    }

}
