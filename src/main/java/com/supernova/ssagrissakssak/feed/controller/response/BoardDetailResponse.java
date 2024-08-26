package com.supernova.ssagrissakssak.feed.controller.response;

import com.supernova.ssagrissakssak.core.enums.ContentType;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.HashtagEntity;
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
    private ContentType type;
    private String title;
    private String content;
    private List<String> hashtags;
    private int viewCount;
    private int likeCount;
    private int shareCount;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    /* Entity -> DTO 변환 */
    public static BoardDetailResponse of(BoardEntity board, List<HashtagEntity> hashtags) {
        return BoardDetailResponse.builder()
                .id(board.getId())
                .type(board.getType())
                .hashtags(hashtags.stream()
                        .map(HashtagEntity::getHashtag)
                        .toList())
                .title(board.getTitle())
                .content(board.getContent())
                .viewCount(board.getViewCount())
                .likeCount(board.getLikeCount())
                .shareCount(board.getShareCount())
                .updatedAt(board.getUpdatedAt())
                .createdAt(board.getCreatedAt())
                .build();
    }

}
