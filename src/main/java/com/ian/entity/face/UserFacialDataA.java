package com.ian.entity.face;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity @Table(name = "USER_FACIAL_DATA_A")
public class UserFacialDataA implements Persistable<Long> {

    @Id
    private Long sequence;

    @Column(nullable = false)
    private Long userId;

    @Column(name = "FACIAL_DATA_A", nullable = false, length = 2000)
    private String facialDataA;

    @CreatedDate
    @Column(updatable = false)
    transient private LocalDateTime regDate;

    @Builder
    public UserFacialDataA(Long sequence, Long userId, String facialDataA) {
        this.sequence = sequence;
        this.userId = userId;
        this.facialDataA = facialDataA;
    }

    @Override
    public Long getId() {
        return sequence;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
