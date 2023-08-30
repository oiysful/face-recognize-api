package com.ian.entity.identification;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity @Table(name = "USER_IDENTIFICATION_B")
public class IdentificationB implements Persistable<Long> {

    @Id
    private Long userId;

    @Lob
    @Column(name = "IDENTIFICATION_B", nullable = false)
    private byte[] identificationB;

    @CreatedDate
    @Column(updatable = false)
    transient private LocalDateTime regDate;

    @LastModifiedDate
    private LocalDateTime lastMdfDate;

    @Builder
    public IdentificationB(Long userId, byte[] identificationB) {
        this.userId = userId;
        this.identificationB = identificationB;
    }

    @Override
    public Long getId() {
        return userId;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
