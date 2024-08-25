package com.supernova.ssagrissakssak.feed.service;


import com.supernova.ssagrissakssak.client.SnsApiClient;
import com.supernova.ssagrissakssak.core.exception.BoardNotFoundException;
import com.supernova.ssagrissakssak.core.exception.SnsApiException;
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
     * @throws BoardNotFoundException 게시물을 찾을 수 없을 때 발생합니다.
     * * @throws SnsApiException SNS API 호출 중 오류가 발생했을 때 발생합니다.
     */
    @Transactional
    public void addLikeBoardContent(String contentId) {
        BoardEntity board = boardRepository.findByContentId(contentId)
                .orElseThrow(() -> new BoardNotFoundException(contentId));

        SnsApiClient.SnsApiResponse response = snsApiClient.callSnsLikeApi(
                new SnsApiClient.SnsApiRequest(board.getType(), contentId));

        if (response.isSuccess()) {
            board.incrementLikeCount();
            boardRepository.save(board);
        } else {
            throw new SnsApiException("좋아요 API 호출 실패");
        }
    }
}
