package com.ian.dto.api.naver;

import com.ian.dto.api.naver.model.Face;
import com.ian.dto.api.naver.model.Info;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Clova Face Recognition API 응답 model
 * @author IAN
 */
@Getter @Setter
public class NaverApiResponse {

    private Info info;  // 입력된 이미지 크기와 인식된 얼굴의 개수 정보를 가지는 객체
    private List<Face> faces; // 감지된 얼굴의 개별 분석 결과를 가지는 객체 배열
}
