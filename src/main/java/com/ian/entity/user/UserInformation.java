package com.ian.entity.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * [USER_INFORMATION] 사용자 정보 Entity
 * @author IAN
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
public class UserInformation {

    /** [PK] 사용자 아이디 */
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "USER_SEQUENCE_GENERATOR")
    @SequenceGenerator(name="USER_SEQUENCE_GENERATOR", sequenceName = "USER_SEQUENCE", allocationSize = 1)
    private Long userId;

    /** 사용자 이름 */
    @Column(nullable = false, length = 100)
    private String userNm;

    /** 사용자 진료번호 */
    @Column(length = 20)
    private String userCareNo;

    /** 사용자 등록일시 */
    @CreatedDate
    @Column(updatable = false)
    transient private LocalDateTime regDate;

    /** 사용자 수정일시 */
    @LastModifiedDate
    private LocalDateTime lastMdfDate;

    @Builder
    public UserInformation(Long userId, String userNm, String userCareNo) {
        this.userId = userId;
        this.userNm = userNm;
        this.userCareNo = userCareNo;
    }
}
