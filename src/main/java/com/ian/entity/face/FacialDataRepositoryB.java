package com.ian.entity.face;

import com.ian.dto.face.FindAllFacialDataB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FacialDataRepositoryB extends JpaRepository<UserFacialDataB, Long> {

    @Query("SELECT new com.ian.dto.face.FindAllFacialDataB(u.userId, u.sequence, u.facialDataB) FROM UserFacialDataB u order by u.userId asc, u.sequence asc")
    List<FindAllFacialDataB> findAllFaces();
}
