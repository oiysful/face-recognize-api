package com.ian.dto.client;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AuthenticationRequest {

    private List<String> faces;

    @Builder
    public AuthenticationRequest(List<String> faces) {
        this.faces = faces;
    }
}
