package com.supernova.ssagrissakssak.feed.controller.response;

import com.supernova.ssagrissakssak.core.enums.ContentType;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponse {

    private Long id;
    private ContentType type;
    private String title;
    private String content;
    private int viewCount;
    private int likeCount;
    private int shareCount;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    /* Entity -> DTO 변환 */
    public static BoardResponse of(BoardEntity board) {
        return BoardResponse.builder()
                .id(board.getId())
                .type(board.getType())
                .title(board.getTitle())
                .content(board.getContent() != null && board.getContent().length() > 20
                        ? board.getContent().substring(0, 20)
                        : board.getContent())
                .viewCount(board.getViewCount())
                .likeCount(board.getLikeCount())
                .shareCount(board.getShareCount())
                .updatedAt(board.getUpdatedAt())
                .createdAt(board.getCreatedAt())
                .build();
    }
}
