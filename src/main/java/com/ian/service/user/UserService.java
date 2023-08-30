package com.ian.service.user;

import com.ian.dto.user.UserCreateRequest;
import com.ian.entity.user.UserInformation;
import com.ian.entity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 사용자 정보 Service
 * @author IAN
 */
@RequiredArgsConstructor
@Service
public class UserService {

    // [USER_INFORMATION] JPA Repository
    private final UserRepository userRepository;

    /**
     * 사용자 정보 DB INSERT
     * @param request 사용자 정보 저장 DTO
     * @return {@link UserInformation} 사용자 정보 Entity
     * @author IAN
     */
    public UserInformation createUser(UserCreateRequest request) {

        return userRepository.save(request.toEntity());
    }

    /**
     * 사용자 ID로 회원 정보 Select
     * @param userId 사용자 ID
     * @return UserInformation 사용자 정보
     * @author IAN
     */
    public UserInformation findById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
