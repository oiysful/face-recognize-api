package com.ian.service.data;

import com.google.gson.Gson;
import com.ian.dto.api.naver.FaceCalc;
import com.ian.dto.face.FacialDataSaveRequest;
import com.ian.dto.id.IdentificationSaveRequest;
import com.ian.entity.face.FacialDataRepositoryA;
import com.ian.entity.face.FacialDataRepositoryB;
import com.ian.entity.face.UserFacialDataA;
import com.ian.entity.face.UserFacialDataB;
import com.ian.entity.identification.IdentificationA;
import com.ian.entity.identification.IdentificationB;
import com.ian.entity.identification.IdentificationRepositoryA;
import com.ian.entity.identification.IdentificationRepositoryB;
import com.ian.service.AES256;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 안면계산정보 및 신분증이미지를 암호화, 분리하여 DB에 저장하는 Service
 * @see UserFacialDataA
 * @see UserFacialDataB
 * @see IdentificationA
 * @see IdentificationB
 * @author IAN
 */
@RequiredArgsConstructor
@Service
public class DataService {

    private final FacialDataRepositoryA faceRepositoryA;  //암호화하여 분리된 안면 계산 정보 A
    private final FacialDataRepositoryB faceRepositoryB;  //암호화하여 분리된 안면 계산 정보 B
    private final IdentificationRepositoryA idRepositoryA;  //암호화하여 분리된 신분증 이미지 A
    private final IdentificationRepositoryB idRepositoryB;  //암호화하여 분리된 신분증 이미지 B

    private final Gson gson;

    /**
     * 계산된 안면 정보 리스트를 각각 암호화하여 이분할하고, userId에 sequence 값과 함께 각각 저장
     * @param request 안면정보저장에 필요한 정보를 갖고 있는 DTO
     * @see FacialDataSaveRequest
     * @see UserFacialDataA
     * @see UserFacialDataB
     * @see FaceCalc
     * @see AES256
     * @author IAN
     */
    public void saveFacialData(FacialDataSaveRequest request) throws Exception {

        List<UserFacialDataA> facialDataAList = new ArrayList<>();  // 암호화하어 분리된 안면 계산 정보 A
        List<UserFacialDataB> facialDataBList = new ArrayList<>();  // 암호화하어 분리된 안면 계산 정보 B

        long userId = request.getUserId();  // 사용자 ID(PK)

        // 안면정보 갯수만큼 반복하여 처리
        long idx = 0L;
        for (FaceCalc faceCalc : request.getFaceCalcList()) {
            // 안면 계산 정보 객체를 JSON 직렬화
            String faceCalcJson = gson.toJson(faceCalc);
            // 직렬화된 JSON을 AES256 암호화
            String encryptedFacialData = request.getAes256().encrypt(faceCalcJson);
            // 암호화된 JSON을 길이 기준으로 이분할
            String splitDataA = encryptedFacialData.substring(0, encryptedFacialData.length()/2);
            String splitDataB = encryptedFacialData.substring(encryptedFacialData.length()/2);
            // 분리된 각각의 데이터와 userId, sequence를 저장할 DTO build
            facialDataAList.add(UserFacialDataA.builder()
                    .sequence(idx)
                    .userId(userId)
                    .facialDataA(splitDataA).build());
            facialDataBList.add(UserFacialDataB.builder()
                    .sequence(idx)
                    .userId(userId)
                    .facialDataB(splitDataB).build());
            idx++;
        }
        // 반으로 나눠진 회원/안면정보 저장
        faceRepositoryA.saveAll(facialDataAList);
        faceRepositoryB.saveAll(facialDataBList);
    }

    /**
     * 신분증 이미지의 binary를 암호화하여 이분할하고 userId와 함께 저장
     * @param request 신분증 이미지 저장에 필요한 정보를 담고 있는 DTO
     * @see IdentificationSaveRequest
     * @see IdentificationA
     * @see IdentificationB
     * @see AES256
     * @author IAN
     */
    public void saveIdentification(IdentificationSaveRequest request) throws Exception {
        // 등록 대상 사용자 ID
        long userId = request.getUserId();
        // 신분증 이미지 binary 암호화
        String encrypted = request.getAes256().encrypt(request.getId());
        // 암호화된 신분증 이미지 binary를 길이 기준으로 이분할
        byte[] splitDataA = encrypted.substring(0, encrypted.length()/2).getBytes(StandardCharsets.UTF_8);
        byte[] splitDataB = encrypted.substring(encrypted.length()/2).getBytes(StandardCharsets.UTF_8);
         // 반으로 나눠진 회원/신분증정보 저장
        idRepositoryA.save(IdentificationA.builder()
                .userId(userId)
                .identificationA(splitDataA).build());
        idRepositoryB.save(IdentificationB.builder()
                .userId(userId)
                .identificationB(splitDataB).build());
    }

    /**
     * DB에 저장된 전체 안면 정보를 가져와 회원 아이디와 매칭 시켜 반환
     * <pre>
     *{@code
     *{
     *    "71": [
     *        {
     *            "eyeToEye": 11.61895003862225,
     *            "leftEyeToNose": 12.489995996796797,
     *            …
     *        },
     *        …
     *    ],
     *    "72": [ … ],
     *    …
     *}}</pre>
     * @param aes256 AES256 암호화에 사용
     * @return {@code Map<userID, List<안면정보>>}
     * @see UserFacialDataA
     * @see UserFacialDataB
     * @see FaceCalc
     * @author IAN
     */
    public Map<Long, List<FaceCalc>> getAllFacialData(AES256 aes256) throws Exception {
        // 분리되어 저장된 전체 안면 정보 SELECT하여 userId와 sequence로 정렬
        List<UserFacialDataA> allFacialDataA = faceRepositoryA.findAll(Sort.by(Sort.Order.asc("userId"), Sort.Order.asc("sequence")));
        List<UserFacialDataB> allFacialDataB = faceRepositoryB.findAll(Sort.by(Sort.Order.asc("userId"), Sort.Order.asc("sequence")));

        int listSize = allFacialDataA.size();

        Map<Long, List<FaceCalc>> allFaceDataById = new HashMap<>(listSize);  // 안면 계산 정보 List를 userId로 묶은 Map
        List<FaceCalc> completedDataList = new ArrayList<>(listSize);  // 분리되어있는 안면 계산 정보를 완성하여 담을 List

        for (int i=0; i<listSize; i++) {
            String facialDataA = allFacialDataA.get(i).getFacialDataA();
            String facialDataB = allFacialDataB.get(i).getFacialDataB();
            String decryptedData = aes256.decrypt(facialDataA + facialDataB);

            completedDataList.add(gson.fromJson(decryptedData, FaceCalc.class));

            allFaceDataById.put(allFacialDataA.get(i).getUserId(), completedDataList);
        }

        return allFaceDataById;
    }
}
