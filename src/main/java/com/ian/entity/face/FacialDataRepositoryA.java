package com.ian.entity.face;

import com.ian.dto.face.FindAllFacialDataA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FacialDataRepositoryA extends JpaRepository<UserFacialDataA, Long> {

    @Query("SELECT new com.ian.dto.face.FindAllFacialDataA(u.userId, u.sequence, u.facialDataA) FROM UserFacialDataA u order by u.userId asc, u.sequence asc")
    List<FindAllFacialDataA> findAllFaces();
}
