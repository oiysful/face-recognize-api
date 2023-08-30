package com.ian.dto.face;

import com.ian.dto.api.naver.FaceCalc;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CompletedFacialData {

    private Long userId;
    private Long sequence;
    private FaceCalc facialData;

    @Builder
    public CompletedFacialData(Long userId, Long sequence, FaceCalc facialData) {
        this.userId = userId;
        this.sequence = sequence;
        this.facialData = facialData;
    }
}
