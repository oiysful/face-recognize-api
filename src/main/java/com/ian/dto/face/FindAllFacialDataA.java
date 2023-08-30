package com.ian.dto.face;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FindAllFacialDataA {

    private Long userId;
    private Long sequence;
    private String facialDataA;

    public FindAllFacialDataA(Long userId, Long sequence, String facialDataA) {
        this.userId = userId;
        this.sequence = sequence;
        this.facialDataA = facialDataA;
    }
}
