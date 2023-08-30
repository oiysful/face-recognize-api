package com.ian.dto.id;

import com.ian.service.AES256;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class IdentificationSaveRequest {

    private Long userId;
    private AES256 aes256;
    private String id;

    @Builder
    public IdentificationSaveRequest(Long userId, AES256 aes256, String id) {
        this.userId = userId;
        this.aes256 = aes256;
        this.id = id;
    }
}
