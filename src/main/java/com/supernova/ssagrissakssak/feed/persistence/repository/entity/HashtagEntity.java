package com.supernova.ssagrissakssak.feed.persistence.repository.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "hashtag")
public class HashtagEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "hashtag", nullable = false)
    private String hashtag;

    @Column(name = "board_id", nullable = false)
    private Long boardId;
}
