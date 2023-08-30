package com.ian.controller.api;

import com.google.gson.Gson;
import com.ian.config.CpnMaster;
import com.ian.dto.api.naver.FaceCalc;
import com.ian.dto.api.naver.model.Face;
import com.ian.dto.client.AuthenticationRequest;
import com.ian.dto.client.RegistrationRequest;
import com.ian.dto.face.FacialDataSaveRequest;
import com.ian.dto.id.IdentificationSaveRequest;
import com.ian.dto.response.Messages;
import com.ian.dto.response.Responses;
import com.ian.dto.response.UserResponse;
import com.ian.dto.user.UserCreateRequest;
import com.ian.entity.user.UserInformation;
import com.ian.service.AES256;
import com.ian.service.api.NaverApiService;
import com.ian.service.company.CompanyService;
import com.ian.service.data.DataService;
import com.ian.service.face.CalculateService;
import com.ian.service.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <h3>Clova Face Recognition API를 통한 안면 인증 구현 Controller</h3>
 * @author IAN
 */
@Tag(name = "안면 인증", description = "Clova Face Recognition API 활용")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class NaverApiController {

    private final UserService userService;
    private final NaverApiService apiService;
    private final CalculateService calcService;
    private final DataService dataService;
    private final CompanyService companyService;
    
    private final Gson gson;

    @GetMapping("/test")
    public ResponseEntity<String> test(String request) throws Exception {
        AES256 aes256 = new AES256(CpnMaster.MASTER_KEY.getKey());
        String decrypt = aes256.decrypt(request);

        return ResponseEntity.ok(decrypt);
    }

//    @PostMapping("/test")
//    public ResponseEntity<String> test(MultipartHttpServletRequest request) throws Exception {
//    public ResponseEntity<String> test(@RequestBody String request) throws Exception {
//        List<String> faceList = new ArrayList<>(10);
//        List<MultipartFile> reqFiles = request.getFiles("test");
//        MultipartFile reqFile = request.getFile("test");

//        for (MultipartFile file : reqFiles) {
//            String base64String = Base64.getEncoder().encodeToString(file.getBytes());
//            System.out.println(base64String);
//            faceList.add(base64String);
//        }
//        String base64String = Base64.getEncoder().encodeToString(reqFile.getBytes());
//        System.out.println(base64String);

//        AES256 aes256 = new AES256("gaon1309");
//        String abc = request.replaceAll(System.lineSeparator(), "").trim();
//        String encrypted = aes256.encrypt(abc);
//        System.out.println(encrypted);
//        System.out.println("END");
//
//        return ResponseEntity.ok(encrypted);
//        return ResponseEntity.ok(gson.toJson(faceList));
//    }

    /**
     * <h3>[안면 등록]</h3>
     * @param headers Client Request Headers : company 필드의 업체 코드를 통해 업체 Key DB 조회
     * @param encryptedRequest Client Request Body : 전체 JSON을 업체 Key로 AES256 암호화한 String
     * <pre>
     * {@code AES256(
     *     {
     *       "faces": [
     *         "Base64(binary)",
     *         "Base64(binary)",
     *         "Base64(binary)",
     *         …
     *       ],
     *       "userNm": "테스트",
     *       "id": "Base64(binary)"
     *     }
     * )}
     * </pre>
     * @return ResponseEntity
     * <pre>
     * {@code
     *   - 201 Created
     *     {
     *         "code": "SUCCESS",
     *         "message": "성공",
     *         "timestamp": "2023-08-25T16:53:32.1825366+09:00"
     *     }
     *   - 400 Bad Request
     *     {
     *         "code": "{$ERROR_CODE}",
     *         "message": "{$ERROR_MSG}",
     *         "timestamp": "2023-08-25T16:53:32.1825366+09:00"
     *     }
     * }
     * </pre>
     * @author IAN
     */
    @PostMapping("/registration")
    public ResponseEntity<Responses> registration(@RequestHeader HttpHeaders headers, @RequestBody String encryptedRequest) throws Exception {
        // 업체 코드 확인
        String cpnCd = headers.getOrEmpty("company").get(0);
        if (cpnCd.isEmpty()) return ResponseEntity.badRequest().body(Responses.of(Messages.MISSING_CPN_CD));

        // DB에 저장된 업체 고유 Key
        String cpnKey = companyService.getCpnKey(cpnCd);

        // 업체 고유 Key → 요청 Body 복호화
        AES256 aes256 = new AES256(cpnKey);
        String decryptedRequest = aes256.decrypt(encryptedRequest);
        RegistrationRequest request = gson.fromJson(decryptedRequest, RegistrationRequest.class);

        // 회원 이름 확인
        String userNm = request.getUserNm();
        if (userNm.isEmpty()) return ResponseEntity.badRequest().body(Responses.of(Messages.MISSION_USER_NAME));

        // 얼굴 이미지 확인
        List<String> faceRequestList = request.getFaces();
        if (faceRequestList.isEmpty()) return ResponseEntity.badRequest().body(Responses.of(Messages.MISSING_FACE_IMAGE));

        // 신분증 이미지 확인
        String idCard = request.getId();
        if (idCard.isEmpty()) return ResponseEntity.badRequest().body(Responses.of(Messages.MISSING_ID_IMAGE));

        // 회원 생성
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .userNm(userNm).build();
        long userId = userService.createUser(userCreateRequest).getUserId();

        // 네이버 API 안면정보 응답 List
        List<Face> faceInfoList = apiService.getFacesInformation(faceRequestList);

        // 안면정보 계산된 List
        List<FaceCalc> faceCalcList = calcService.calculateFaceInfo(faceInfoList);

        // 안면 계산 정보 저장
        dataService.saveFacialData(FacialDataSaveRequest.builder()
                .userId(userId)
                .faceCalcList(faceCalcList)
                .aes256(aes256).build());

        // 신분증 정보 저장
        dataService.saveIdentification(IdentificationSaveRequest.builder()
                .userId(userId)
                .aes256(aes256)
                .id(request.getId()).build());

        return ResponseEntity.status(HttpStatus.CREATED).body(Responses.of(Messages.SUCCESS));
    }

    /**
     * <h3>[안면 인증]</h3>
     * @param headers Client Request Headers : company 필드의 업체 코드를 통해 업체 Key DB 조회
     * @param encryptedRequest Client Request Body : 전체 JSON을 업체 Key로 AES256 암호화한 String
     * <pre>
     * {@code AES256(
     *     {
     *       "faces": [
     *         "Base64(binary)",
     *         "Base64(binary)",
     *         "Base64(binary)",
     *         …
     *       ]
     *     }
     * )}
     * </pre>
     * @return ResponseEntity
     * <pre>
     * {@code
     *   - 200 OK
     *     {
     *         "code": "SUCCESS",
     *         "message": "성공",
     *         "timestamp": "2023-08-29T11:35:58.1321893+09:00",
     *         "user": "9XoztMvFXXizeAo23baimq5bpvnZiYTRAuOSG7BVr+w="(AES256 암호화된 사용자 정보)
     *     }
     *   - 400 Bad Request
     *     {
     *         "code": "{$ERROR_CODE}",
     *         "message": "{$ERROR_MSG}",
     *         "timestamp": "2023-08-25T16:53:32.1825366+09:00"
     *     }
     * }
     * </pre>
     * @author IAN
     */
    @PostMapping("/authentication")
    public ResponseEntity<Responses> authentication(@RequestHeader HttpHeaders headers, @RequestBody String encryptedRequest) throws Exception {
        // 업체 코드 확인
        String cpnCd = headers.getOrEmpty("company").get(0);
        if (cpnCd.isEmpty()) return ResponseEntity.badRequest().body(Responses.of(Messages.MISSING_CPN_CD));

        // DB에 저장된 업체 고유 Key
        String cpnKey = companyService.getCpnKey(cpnCd);

        // 업체 고유 Key → 요청 Body 복호화
        AES256 aes256 = new AES256(cpnKey);
        String decryptedRequest = aes256.decrypt(encryptedRequest);

        AuthenticationRequest request = gson.fromJson(decryptedRequest, AuthenticationRequest.class);

        // 얼굴 이미지 확인
        List<String> faceRequestList = request.getFaces();
        if (faceRequestList.isEmpty()) return ResponseEntity.badRequest().body(Responses.of(Messages.MISSING_FACE_IMAGE));

        // 네이버 API 안면정보 응답 List
        List<Face> faceInfoList = apiService.getFacesInformation(faceRequestList);

        // 안면정보 계산된 List
        List<FaceCalc> faceCalcList = calcService.calculateFaceInfo(faceInfoList);

        // 저장된 전체 안면정보 select 및 복호화
        Map<Long, List<FaceCalc>> completedDataList = dataService.getAllFacialData(aes256);

        // 안면정보 리스트와 전체 데이터 비교
        Map<Long, Double> comparedMap = calcService.compareToAllFaces(faceCalcList, completedDataList);

        // 가장 높은 점수 합을 가진 안면 정보 계산
        Map.Entry<Long, Double> findMaxComparePoint = comparedMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(RuntimeException::new);

        // 점수 합을 1회 촬영 횟수로 나눠 백분율 계산
        double maxPointPercentage = findMaxComparePoint.getValue() / faceInfoList.size();

        // 가장 높은 점수가 80 미만이면 등록되지 않은 사용자 return
        if (maxPointPercentage < 80) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Responses.of(Messages.UNREGISTERED_USER));

        // 80점 이상이면 등록된 회원 조회
        UserInformation user = userService.findById(findMaxComparePoint.getKey());

        // 회원정보 DTO를 JSON 직렬화 - 응답에 신분증 이미지 추가 시 DB SELECT 추가가 필요합니다~
        String userInfo = gson.toJson(
                UserResponse.builder()
                        .user(user)
                        //.idCard("신분증이미지") - 응답에 신분증 이미지 추가 시 DB SELECT 추가가 필요합니다~
                        .build());
        String encryptedUser = aes256.encrypt(userInfo);

        // 성공 메세지와 암호화된 회원 정보 return
        return ResponseEntity.ok(Responses.of(encryptedUser));
    }
}
