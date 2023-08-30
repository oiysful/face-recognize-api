package com.ian.config;

import lombok.Getter;

/**
 * <h3>업체 암호 키 복호화를 위한 마스터 키</h3>
 * - DB에 마스터키로 AES256 암호화 되어있는 업체 별 암호화 키를 복호화 하기 위한 마스터 키
 * @author IAN
 */
@Getter
public enum CpnMaster {

    MASTER_KEY("cks1309");

    private final String key;
    CpnMaster(String name) {
        this.key = name;
    }
}
