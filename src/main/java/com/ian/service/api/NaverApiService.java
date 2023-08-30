package com.ian.service.api;

import com.ian.dto.api.naver.NaverApiResponse;
import com.ian.dto.api.naver.model.Face;
import com.ian.service.ParallelBusinesses;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Clova Face Recognition API와 통신하는 Service
 * @see <a href="https://developers.naver.com/docs/clova/api/CFR/API_Guide.md#FaceAPI">Clova Face Recognition API 가이드</a>
 * @author IAN
 */
@RequiredArgsConstructor
@Service
public class NaverApiService {

    /** API 요청을 병렬처리 하기 위한 Service */
    private final ParallelBusinesses parallelBusinesses;

    /**
     * Clova Face Recognition API에 안면 정보를 요청
     * @param requests 암호화된 안면 이미지 binary List
     * @return {@code List<Face> Clova Face Recognition API로부터 응답받은 안면정보 List}
     * @see Face
     * @author IAN
     */
    public List<Face> getFacesInformation(List<String> requests) throws URISyntaxException, ExecutionException, InterruptedException {

        List<Face> apiResponseList = new ArrayList<>();  // Clova Face Recognition API의 응답 중 안면 정보를 반환하는 List
        List<CompletableFuture<NaverApiResponse>> apiFutureList = new ArrayList<>();  // 병렬처리된 결과를 받는 List<Future>
        List<Resource> requestResources = new ArrayList<>();  // 요청 받은 암호화 이미지 binary를 복호화하여 담는 List

        // 요청 받은 암호화 이미지 binary를 복호화하여 네이버 API에 전송할 Resource 객체로 변환
        for (String face : requests) {
            byte[] decodedFace = Base64.getDecoder().decode(face);
            ByteArrayInputStream byteIs = new ByteArrayInputStream(decodedFace);
            Resource resource = new InputStreamResource(byteIs);
            requestResources.add(resource);
        }
        // 네이버 API에 병렬 요청 전송하여 List<Future>에 add()
        for (Resource resource : requestResources) apiFutureList.add(parallelBusinesses.getApiResult(resource));
        // 처리 완료된 Future에서 네이버 응답 안면 정보 취득
        for (CompletableFuture<NaverApiResponse> future : apiFutureList) apiResponseList.add(future.get().getFaces().get(0));

        return apiResponseList;
    }
}
