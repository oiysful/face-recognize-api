package com.ian.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@JsonPropertyOrder({"code", "message", "timestamp"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Responses {

    private Messages code;
    private String message;
    private String user;
    private final ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.systemDefault());

    private Responses(Messages code) {
        this.code = code;
        this.message = code.getMessage();
    }

    private Responses(String encryptedUser) {
        this.code = Messages.SUCCESS;
        this.message = Messages.SUCCESS.getMessage();
        this.user = encryptedUser;
    }

    public static Responses of(Messages message) {
        return new Responses(message);
    }

    public static Responses of(String encryptedUser) {
        return new Responses(encryptedUser);
    }
}
