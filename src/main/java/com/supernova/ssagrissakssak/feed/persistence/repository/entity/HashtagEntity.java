package com.supernova.ssagrissakssak.feed.persistence.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BoardHashtagEntity> boardHashtags;

}
