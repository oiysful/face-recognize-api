package com.ian.dto.face;

import com.ian.dto.api.naver.FaceCalc;
import com.ian.service.AES256;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FacialDataSaveRequest {

    private Long userId;
    private AES256 aes256;
    private List<FaceCalc> faceCalcList;

    @Builder
    public FacialDataSaveRequest(Long userId, AES256 aes256, List<FaceCalc> faceCalcList) {
        this.userId = userId;
        this.aes256 = aes256;
        this.faceCalcList = faceCalcList;
    }
}
