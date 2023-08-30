package com.ian.service;

import com.ian.config.ApiProp;
import com.ian.dto.api.naver.FaceCalc;
import com.ian.dto.api.naver.NaverApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 비즈니스 로직을 병렬 처리하기 위한 Service
 * @author IAN
 */
@RequiredArgsConstructor
@Service
public class ParallelBusinesses {

    /** Clova Face Recognition API 관련 설정 값 */
    private final ApiProp.Naver naverProp;

    @Async
    public CompletableFuture<NaverApiResponse> getApiResult(Resource resource) throws URISyntaxException, InterruptedException {
        HttpHeaders headers = setHeaderAttributes();

        MultiValueMap<String, Resource> body = new LinkedMultiValueMap<>();

        body.set("image", resource);
        HttpEntity<MultiValueMap<String, Resource>> entity = new HttpEntity<>(body, headers);

        return CompletableFuture.completedFuture(
                new RestTemplate().exchange(
                        new URI(naverProp.getApi()),
                        HttpMethod.POST,
                        entity,
                        NaverApiResponse.class
                ).getBody()
        );
    }

    @Async
    public CompletableFuture<Map<Long, Double>> getCompareRatesGroupById(FaceCalc requestFace, Map<Long, List<FaceCalc>> savedData) {
        // Map<userId, rate>
        Map<Long, Double> compareRates = new HashMap<>();

        for (Long userId : savedData.keySet()) {
            List<FaceCalc> savedFaces = savedData.get(userId);
            for (FaceCalc savedFace : savedFaces) {
                double compareRate = getRateAverage(savedFace, requestFace);
                compareRates.merge(userId, compareRate, Double::sum);
            }
        }

        return CompletableFuture.completedFuture(compareRates);
    }

    private double getRateAverage(FaceCalc savedFace, FaceCalc requestFace) {

        return (calculateRate(savedFace.getEyeToEye(), requestFace.getEyeToEye()) +
                calculateRate(savedFace.getLeftEyeToNose(), requestFace.getLeftEyeToNose()) +
                calculateRate(savedFace.getRightEyeToNose(), requestFace.getRightEyeToNose()) +
                calculateRate(savedFace.getLeftEyeToLeftMouth(), requestFace.getLeftEyeToLeftMouth()) +
                calculateRate(savedFace.getLeftEyeToRightMouth(), requestFace.getLeftEyeToRightMouth()) +
                calculateRate(savedFace.getRightEyeToLeftMouth(), requestFace.getRightEyeToLeftMouth()) +
                calculateRate(savedFace.getRightEyeToRightMouth(), requestFace.getRightEyeToRightMouth()) +
                calculateRate(savedFace.getNoseToLeftMouth(), requestFace.getNoseToLeftMouth()) +
                calculateRate(savedFace.getNoseToRightMouth(), requestFace.getNoseToRightMouth())
        ) / 9;
    }

    private double calculateRate(Double registered, Double request) {
        double rate = registered / request * 100;

        if (rate > 100) return 200 - rate;

        return rate;
    }

    private HttpHeaders setHeaderAttributes() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.add("X-Naver-Client-Id", naverProp.getClient().getId());
        headers.add("X-Naver-Client-Secret", naverProp.getClient().getSecret());

        return headers;
    }
}
