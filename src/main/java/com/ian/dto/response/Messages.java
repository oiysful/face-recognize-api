package com.ian.dto.response;

import lombok.Getter;


@Getter
public enum Messages {
    SUCCESS("성공"),
    UNREGISTERED_USER("[인식 실패] 등록되지 않은 사용자입니다."),
    MISSING_CPN_CD("요청 헤더에 업체 코드가 누락되었습니다."),
    MISSION_USER_NAME("요청 본문에 회원 이름이 누락되었습니다."),
    MISSING_ID_IMAGE("요청 본문에 신분증 이미지가 누락되었습니다."),
    MISSING_FACE_IMAGE("요청 본문에 안면 이미지가 누락되었습니다.");

    private final String message;

    Messages(String message) {
        this.message = message;
    }
}
