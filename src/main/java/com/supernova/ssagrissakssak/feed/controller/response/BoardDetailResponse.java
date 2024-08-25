package com.supernova.ssagrissakssak.feed.controller.response;

import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDetailResponse {

    private Long id;
    private String  type;
    private String title;
    private String content;
    private int viewCount;
    private int likeCount;
    private int shareCount;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private List<String> hashtags;

    /* DTO -> Entity 반환 */
    public static BoardDetailResponse from(BoardEntity board, List<String> hashtags) {
        return BoardDetailResponse.builder()
                .id(board.getId())
                .type(board.getType().toString())
                .title(board.getTitle())
                .content(board.getContent())
                .viewCount(board.getViewCount())
                .likeCount(board.getLikeCount())
                .shareCount(board.getShareCount())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .hashtags(hashtags)
                .build();
    }
}
