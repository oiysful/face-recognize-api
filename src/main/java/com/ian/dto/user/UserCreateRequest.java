package com.ian.dto.user;

import com.ian.entity.user.UserInformation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 생성 DTO(Controller → Service)
 * @author IAN
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserCreateRequest {

    // 사용자 이름
    private String userNm;
    // 사용자 진료번호
    private String userCareNo;

    @Builder
    public UserCreateRequest(String userNm, String userCareNo) {
        this.userNm = userNm;
        this.userCareNo = userCareNo;
    }

    /**
     * DTO → Entity 변환
     * @return UserInformation 회원 정보 Entity
     * @author IAN
     */
    public UserInformation toEntity() {
        return UserInformation.builder()
                .userNm(userNm)
                .userCareNo(userCareNo)
                .build();
    }
}
