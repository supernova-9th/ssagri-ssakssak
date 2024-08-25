package com.supernova.ssagrissakssak.feed.controller.response;

import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public static BoardDetailResponse from(BoardEntity boardEntity) {
        return BoardDetailResponse.builder()
                .id(boardEntity.getId())
                .type(boardEntity.getType().name())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .viewCount(boardEntity.getViewCount())
                .likeCount(boardEntity.getLikeCount())
                .shareCount(boardEntity.getShareCount())
                .updatedAt(boardEntity.getUpdatedAt())
                .createdAt(boardEntity.getCreatedAt())
                .hashtags(boardEntity.getBoardHashtags().stream()
                        .map(boardHashtag -> boardHashtag.getHashtag().getHashtag()) // HashtagEntity의 해시태그 문자열을 추출
                        .collect(Collectors.toList()))
                .build();
    }

}
