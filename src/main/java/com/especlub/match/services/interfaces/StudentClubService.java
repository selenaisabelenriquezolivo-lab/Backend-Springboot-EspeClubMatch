package com.especlub.match.services.interfaces;

public interface StudentClubService {
    String enrollStudent(Long studentId, Long clubId);
    void leaveClub(Long studentId, Long clubId);
}