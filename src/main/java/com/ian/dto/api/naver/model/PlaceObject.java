package com.ian.dto.api.naver.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Clova Face Recognition API 응답 중 크기와 위치 정보를 나타내는 객체
 * @see <a href="https://developers.naver.com/docs/clova/api/CFR/API_Guide.md#placeObject">Place object</a>
 * @author IAN
 */
@Getter @Setter
public class PlaceObject {

    private Double width;  // 입력된 이미지, 인식된 얼굴의 너비 정보(px)
    private Double height;  // 입력된 이미지, 인식된 얼굴의 높이 정보(px)
    private Double x;  // 인식된 얼굴 및 얼굴 부위의 위치 정보를 나타내기 위한 x 좌표(px), 기준점은 이미지의 좌상단 모서리
    private Double y;  // 인식된 얼굴 및 얼굴 부위의 위치 정보를 나타내기 위한 y 좌표(px), 기준점은 이미지의 좌상단 모서리

}
