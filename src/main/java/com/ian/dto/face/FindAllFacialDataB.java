package com.ian.dto.face;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FindAllFacialDataB {

    private Long userId;
    private Long sequence;
    private String facialDataB;

    public FindAllFacialDataB(Long userId, Long sequence, String facialDataB) {
        this.userId = userId;
        this.sequence = sequence;
        this.facialDataB = facialDataB;
    }
}
