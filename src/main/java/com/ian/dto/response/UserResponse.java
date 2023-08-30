package com.ian.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ian.entity.user.UserInformation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserResponse {

    private String userNm;
    private String userCareNo;
    private String idCard;

    @Builder
    public UserResponse(UserInformation user, String idCard) {
        this.userNm = user.getUserNm();
        this.userCareNo = user.getUserCareNo();
        this.idCard = idCard;
    }
}