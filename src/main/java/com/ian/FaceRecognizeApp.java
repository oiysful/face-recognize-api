package com.ian;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableJpaAuditing
@SpringBootApplication
public class FaceRecognizeApp {

    public static void main(String[] args) {
        new SpringApplicationBuilder(FaceRecognizeApp.class)
                .profiles("api")  // application-api.yml: api URL, Client ID, Client Secret 등 외부 설정 정보
                .run(args);
    }
}
