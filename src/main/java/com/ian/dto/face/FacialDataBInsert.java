package com.ian.dto.face;

import com.ian.entity.face.UserFacialDataA;
import com.ian.entity.face.UserFacialDataB;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FacialDataBInsert {

    private Long sequence;
    private Long userId;
    private String facialDataB;

    @Builder
    public FacialDataBInsert(Long sequence, Long userId, String facialDataB) {
        this.sequence = sequence;
        this.userId = userId;
        this.facialDataB = facialDataB;
    }

    public UserFacialDataB toEntity() {
        return UserFacialDataB.builder()
                .sequence(sequence)
                .userId(userId)
                .facialDataB(facialDataB)
                .build();
    }
}
