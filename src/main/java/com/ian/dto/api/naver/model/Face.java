package com.ian.dto.api.naver.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Clova Face Recognition API의 응답 JSON Model
 * @see <a href="https://developers.naver.com/docs/clova/api/CFR/API_Guide.md#FaceAPI">Clova Face Recognition API 가이드</a>
 * @author IAN
 */
@Getter @Setter
public class Face {

    private PlaceObject roi;  // 감지된 특정 얼굴의 좌표 및 크기 정보를 가지는 객체
    private Landmark landmark;  // 감지된 얼굴의 눈, 코, 입의 위치를 가지는 객체
    private ValueObject gender;  // 감지된 얼굴의 성별을 추정한 정보를 가지는 객체
    private ValueObject age;  // 감지된 얼굴의 나이를 추정한 정보를 가지는 객체
    private ValueObject emotion;  // 감지된 얼굴의 감정을 추천한 정보를 가지는 객체
    private ValueObject pose;  // 감지된 얼굴이 어떤 포즈인지 추정한 정보를 가지는 객체
}
