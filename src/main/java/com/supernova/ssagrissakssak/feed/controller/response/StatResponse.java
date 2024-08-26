package com.supernova.ssagrissakssak.feed.controller.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatResponse {
    private String createdAt;
    private int count;

    public StatResponse(LocalDateTime createdAt, int count) {
        this.createdAt = String.valueOf(createdAt);
        this.count = count;
    }

    public StatResponse(Date createdAt, int count) {
        this.createdAt = String.valueOf(createdAt);
        this.count = count;
    }
}
