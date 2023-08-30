package com.ian.service.face;

import com.ian.dto.api.naver.FaceCalc;
import com.ian.dto.api.naver.model.Face;
import com.ian.dto.api.naver.model.Landmark;
import com.ian.dto.api.naver.model.PlaceObject;
import com.ian.service.ParallelBusinesses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 안면정보에 대한 여러 계산을 위한 Service
 * @author IAN
 */
@RequiredArgsConstructor
@Service
public class CalculateService {

    /** 계산 및 비교를 병렬 처리하기 위한 Service */
    private final ParallelBusinesses parallelBusinesses;

    /**
     * 안면 정보의 Landmark를 통해 계산
     * @param faceList Clova Face Recognition API의 안면정보 응답 값
     * @return {@code List<FaceCalc> 계산된 결과를 담은 List}
     * @see FaceCalc
     * @author IAN
     */
    public List<FaceCalc> calculateFaceInfo(List<Face> faceList) {

        List<FaceCalc> faceCalcList = new ArrayList<>();

        for (Face face : faceList) {
            Landmark landmark = face.getLandmark();
            faceCalcList.add(calculatedLandmarks(landmark));
        }

        return faceCalcList;
    }

    /**
     * DB에 저장된 전체 안면계산정보와 요청 받은 안면정보를 비교
     * @param requestList 요청 받은 안면 정보 List
     * @param savedData DB에 저장된 전체 안면계산정보
     * @return {@code Map<사용자ID, 비교결과> 사용자 ID별 비교 점수의 합}
     * @see FaceCalc
     * @author IAN
     */
    public Map<Long, Double> compareToAllFaces(List<FaceCalc> requestList, Map<Long, List<FaceCalc>> savedData) throws ExecutionException, InterruptedException {

        Map<Long, Double> compareRates = new HashMap<>();  // [{userId, rate}] return
        List<CompletableFuture<Map<Long, Double>>> compareFutureList = new ArrayList<>();  // 병렬 처리를 담는 List<Future>

        // DB에 저장된 전체 안면계산정보와 요청 받은 안면 정보를 병렬 비교
        for (FaceCalc requestFace : requestList)
            compareFutureList.add(parallelBusinesses.getCompareRatesGroupById(requestFace, savedData));
        // 병렬 비교 결과 취득
        for (CompletableFuture<Map<Long, Double>> future : compareFutureList)
            compareRates.putAll(future.get());

        return compareRates;
    }

    /**
     * 안면 정보의 Landmark를 계산
     * @param landmark 안면정보 특정점: 눈(좌,우) / 코 / 입꼬리(좌,우)
     * @return {@link FaceCalc} 특정점 계산 결과
     * @author IAN
     */
    private FaceCalc calculatedLandmarks(Landmark landmark) {

        return FaceCalc.builder()
                .eyeToEye(getDistance(landmark.getLeftEye(), landmark.getRightEye()))  // 눈과 눈 거리
                .leftEyeToNose(getDistance(landmark.getLeftEye(), landmark.getNose()))  // 왼쪽 눈과 코 거리
                .rightEyeToNose(getDistance(landmark.getRightEye(), landmark.getNose()))  // 오른쪽 눈과 코 거리
                .leftEyeToLeftMouth(getDistance(landmark.getLeftEye(), landmark.getLeftMouth()))  // 왼쪽 눈과 왼쪽 입꼬리 거리
                .leftEyeToRightMouth(getDistance(landmark.getLeftEye(), landmark.getRightMouth()))  // 왼쪽 눈과 오른쪽 입꼬리 거리
                .rightEyeToLeftMouth(getDistance(landmark.getRightEye(), landmark.getLeftMouth()))  // 오른쪽 눈과 왼쪽 입꼬리 거리
                .rightEyeToRightMouth(getDistance(landmark.getRightEye(), landmark.getRightMouth()))  // 오른쪽 눈과 오른쪽 입꼬리 거리
                .noseToLeftMouth(getDistance(landmark.getNose(), landmark.getLeftMouth()))  // 코와 왼쪽 입꼬리 거리
                .noseToRightMouth(getDistance(landmark.getNose(), landmark.getRightMouth()))  // 코와 오른쪽 입꼬리 거리
                .build();
    }

    /**
     * 두 좌표 쌍(x,y) 거리 비교
     * @param a 좌표 쌍 a
     * @param b 좌표 쌍 b
     * @return 좌표 쌍의 거리
     * @author IAN
     */
    private double getDistance(PlaceObject a, PlaceObject b) {

        double xd = b.getX()-a.getX();
        double yd = b.getY()-a.getY();

        double abs = Math.abs(xd + yd);
        double result = Math.sqrt(abs);

        return Double.isNaN(result) ? 0 : result;
    }
}
