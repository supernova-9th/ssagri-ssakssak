package com.supernova.ssagrissakssak.feed.service;


import com.supernova.ssagrissakssak.client.SnsApiClient;
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

    /**
     * 게시물에 좋아요를 추가합니다.
     *
     * @param contentId 좋아요를 추가할 게시물의 ID
     */
    @Transactional
    public void addLikeToBoardContent(String contentId) {
        BoardEntity board = boardRepository.findByContentId(contentId);
        if (board != null) {
            SnsApiClient.SnsApiRequest request =
                    new SnsApiClient.SnsApiRequest(board.getType(), contentId);
            SnsApiClient.SnsApiResponse response = snsApiClient.callSnsLikeApi(request);
            if (response.isSuccess()) {
                board.incrementLikeCount();
                boardRepository.save(board);
            }
        }
    }
}
