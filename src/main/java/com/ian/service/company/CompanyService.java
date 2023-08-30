package com.ian.service.company;

import com.ian.config.CpnMaster;
import com.ian.entity.company.CompanyMaster;
import com.ian.entity.company.CompanyMasterRepository;
import com.ian.service.AES256;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyMasterRepository companyRepository;

    /**
     * 요청 받은 업체 코드로 DB에 저장된 업체 고유 Key 반환
     * @param cpnCd 업체 코드
     * @return 업체 고유 Key
     * @author IAN
     */
    public String getCpnKey(String cpnCd) throws Exception {
        // 업체 코드로 암호화된 업체 Key SELECT
        CompanyMaster company = companyRepository.findById(cpnCd).orElseThrow(IllegalArgumentException::new);
        String encodedKey = company.getCpnKey();

        // 업체 Key 복호화하여 반환
        return new AES256(CpnMaster.MASTER_KEY.getKey())
                .decrypt(encodedKey);
    }
}
