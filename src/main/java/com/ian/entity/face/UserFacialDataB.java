package com.ian.entity.face;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity @Table(name = "USER_FACIAL_DATA_B")
public class UserFacialDataB implements Persistable<Long> {

    @Id
    private Long sequence;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "FACIAL_DATA_B", nullable = false, length = 2000)
    private String facialDataB;

    @CreatedDate
    @Column(updatable = false)
    transient private LocalDateTime regDate;

    @Builder
    public UserFacialDataB(Long sequence, Long userId, String facialDataB) {
        this.sequence = sequence;
        this.userId = userId;
        this.facialDataB = facialDataB;
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
