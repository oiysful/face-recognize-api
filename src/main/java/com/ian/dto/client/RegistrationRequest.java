package com.ian.dto.client;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 사용자 및 안면 정보 등록 요청 DTO(client → Server)
 * @author IAN
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RegistrationRequest {

    // 안면 정보 리스트
    private List<String> faces;
    // 사용자 이름
    private String userNm;
    // 사용자 신분증 이미지 base65(binary)
    private String id;
    // 사용자 진료번호
    private String userCareNo;

    @Builder
    public RegistrationRequest(List<String> faces, String userNm, String id, String userCareNo) {
        this.faces = faces;
        this.userNm = userNm;
        this.id = id;
        this.userCareNo = userCareNo;
    }
}
