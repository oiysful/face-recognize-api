package com.ian.dto.api.naver.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 인식된 값에 대한 정보
 * @see <a href="https://developers.naver.com/docs/clova/api/CFR/API_Guide.md#FaceAPI">Clova Face Recognition API 가이드</a>
 * @author IAN
 */
@Getter @Setter
public class ValueObject {

    private String value;  // 인식된 값
    private Double confidence;  // 인식된 값에 대한 확신 정도
}
