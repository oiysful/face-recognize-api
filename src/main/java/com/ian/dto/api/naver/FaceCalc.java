package com.ian.dto.api.naver;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 안면 정보의 Landmark를 통한 계산 결과를 담는 객체
 * @see com.ian.service.face.CalculateService
 * @author IAN
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FaceCalc {

    private Double eyeToEye;  // 눈과 눈 거리
    private Double leftEyeToNose;  // 왼쪽 눈과 코 거리
    private Double rightEyeToNose;  // 오른쪽 눈과 코 거리
    private Double leftEyeToLeftMouth;  // 왼쪽 눈과 왼쪽 입꼬리 거리
    private Double leftEyeToRightMouth;  // 왼쪽 눈과 오른쪽 입꼬리 거리
    private Double rightEyeToLeftMouth;  // 오른쪽 눈과 왼쪽 입꼬리 거리
    private Double rightEyeToRightMouth;  // 오른쪽 눈과 오른쪽 입꼬리 거리
    private Double noseToLeftMouth;  // 코와 왼쪽 입꼬리 거리
    private Double noseToRightMouth;  // 코와 오른쪽 입꼬리 거리

    @Builder
    public FaceCalc(Double eyeToEye, Double leftEyeToNose, Double rightEyeToNose, Double leftEyeToLeftMouth, Double leftEyeToRightMouth, Double rightEyeToLeftMouth, Double rightEyeToRightMouth, Double noseToLeftMouth, Double noseToRightMouth) {
        this.eyeToEye = eyeToEye;
        this.leftEyeToNose = leftEyeToNose;
        this.rightEyeToNose = rightEyeToNose;
        this.leftEyeToLeftMouth = leftEyeToLeftMouth;
        this.leftEyeToRightMouth = leftEyeToRightMouth;
        this.rightEyeToLeftMouth = rightEyeToLeftMouth;
        this.rightEyeToRightMouth = rightEyeToRightMouth;
        this.noseToLeftMouth = noseToLeftMouth;
        this.noseToRightMouth = noseToRightMouth;
    }
}
