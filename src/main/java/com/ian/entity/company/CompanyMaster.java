package com.ian.entity.company;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class CompanyMaster implements Persistable<String> {

    @Id
    private String cpnCd;

    @Column(nullable = false, length = 20)
    private String cpnType;

    @Column(length = 100)
    private String cpnKey;

    @Builder
    public CompanyMaster(String cpnCd, String cpnType, String cpnKey) {
        this.cpnCd = cpnCd;
        this.cpnType = cpnType;
        this.cpnKey = cpnKey;
    }

    @Override
    public String getId() {
        return cpnCd;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
