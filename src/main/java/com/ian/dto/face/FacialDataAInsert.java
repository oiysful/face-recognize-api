package com.ian.dto.face;

import com.ian.entity.face.UserFacialDataA;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FacialDataAInsert {

    private Long sequence;
    private Long userId;
    private String facialDataA;

    @Builder
    public FacialDataAInsert(Long sequence, Long userId, String facialDataA) {
        this.sequence = sequence;
        this.userId = userId;
        this.facialDataA = facialDataA;
    }

    public UserFacialDataA toEntity() {
        return UserFacialDataA.builder()
                .sequence(sequence)
                .userId(userId)
                .facialDataA(facialDataA)
                .build();
    }
}
