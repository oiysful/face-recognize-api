package com.ian.dto.api.naver.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 얼굴의 눈, 코, 입의 위치를 가지는 객체
 * @see <a href="https://developers.naver.com/docs/clova/api/CFR/API_Guide.md#FaceAPI">Clova Face Recognition API 가이드</a>
 * @author IAN
 */
@Getter @Setter
public class Landmark {

    private PlaceObject leftEye;  // 왼쪽 눈의 위치
    private PlaceObject rightEye;  // 오른쪽 눈의 위치
    private PlaceObject nose;  // 코의 위치
    private PlaceObject leftMouth;  // 왼쪽 입 꼬리의 위치
    private PlaceObject rightMouth;  // 오른쪽 입 꼬리의 위치
}
