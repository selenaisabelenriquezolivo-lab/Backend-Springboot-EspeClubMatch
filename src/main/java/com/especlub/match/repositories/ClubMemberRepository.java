package com.especlub.match.repositories;

import com.especlub.match.models.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    @Query("select cm from ClubMember cm " +
            "join fetch cm.student s " +
            "join fetch s.userInfo ui " +
            "where cm.club.id = :clubId and cm.recordStatus = true")
    List<ClubMember> findAllByClubIdAndRecordStatusTrue(@Param("clubId") Long clubId);
}