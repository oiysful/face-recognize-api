package com.ian.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <h3>API 환경 설정 YAML 속성</h3>
 * - 설정 값 분리 적용을 위한 컴포넌트
 * <pre>
 *   ex) - ApiProp.Naver
 *       - ApiProp.OtherApi···
 * </pre>
 * @author IAN
 */
public class ApiProp {

    /**
     * <h3>Clova Face Recognition API</h3>
     * <pre>
     * 1. apiUrl: naver API 요청 URL
     * 2. client: naver API Client 정보
     *   - id: 앱 등록 시 발급받은 Client ID
     *   - secret: 앱 등록 시 발급 받은 Client Secret
     * </pre>
     * @see <a href="https://developers.naver.com/docs/clova/api/CFR/API_Guide.md#FaceAPI">Clova Face Recognition API 가이드</a>
     * @author IAN
     */
    @Getter
    @Setter(AccessLevel.PACKAGE)
    @Component
    @ConfigurationProperties(prefix = "naver")
    public static class Naver {
        private String api;
        private Client client;
    }

    /**
     * <h3>NUGU facecan API</h3>
     * <pre>
     * 1. apiUrl: SK NUGU API 요청 URL
     * 2. client: SK NUGU Client 정보
     *   - id: 앱 등록 시 발급받은 Client ID
     *   - secret: 앱 등록 시 발급 받은 Client Secret
     * </pre>
     * @see <a href="https://openapi.sk.com/products/detail?svcSeq=50&menuSeq=132">NUGU facecan API</a>
     * @author IAN
     */
    @Getter
    @Setter(AccessLevel.PACKAGE)
    @Component
    @ConfigurationProperties(prefix = "sk")
    public static class Sk {
        private String api;
        private Client client;
    }


    /**
     * <h3>API Client 정보</h3>
     * API 사용 조건에 따라 추가/변경 가능
     * @author IAN
     */
    @Getter
    @Setter(AccessLevel.MODULE)
    public static class Client {
        private String id;
        private String secret;
        private String group_id;
    }
}
