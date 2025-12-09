package com.especlub.match.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ClubMemberAdminDto {
    private Long membershipId; // ClubMember.id
    private Long studentId;
    private Long userInfoId;
    private String email;
    private String fullName;
    private Boolean recordStatus;
    private LocalDateTime joinedAt;
}

