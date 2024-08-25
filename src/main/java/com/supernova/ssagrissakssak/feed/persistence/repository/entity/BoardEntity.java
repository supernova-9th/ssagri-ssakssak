package com.supernova.ssagrissakssak.feed.persistence.repository.entity;

import com.supernova.ssagrissakssak.core.enums.ContentType;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "board")
public class BoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ContentType type;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "like_count", nullable = false)
    private int likeCount;

    @Column(name = "share_count", nullable = false)
    private int shareCount;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<BoardHashtagEntity> boardHashtags = new HashSet<>();

    public void viewCountUp() {
        this.viewCount += 1;
    }
}
