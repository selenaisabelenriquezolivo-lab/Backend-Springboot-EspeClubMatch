package com.especlub.match.services.impl;

import com.especlub.match.models.Club;
import com.especlub.match.models.ClubMember;
import com.especlub.match.models.Student;
import com.especlub.match.repositories.ClubRepository;
import com.especlub.match.repositories.StudentRepository;
import com.especlub.match.services.interfaces.StudentClubService;
import com.especlub.match.shared.exceptions.CustomExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentClubServiceImpl implements StudentClubService {

    private final ClubRepository clubRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public String enrollStudent(Long studentId, Long clubId) {
        Club club = clubRepository.findByIdAndRecordStatusTrue(clubId)
                .orElseThrow(() -> new CustomExceptions("Club no encontrado o inactivo", 404));
        Student student = studentRepository.findByIdAndRecordStatusTrue(studentId)
                .orElseThrow(() -> new CustomExceptions("Estudiante no encontrado o inactivo", 404));

        if (club.getMembers() == null) club.setMembers(new HashSet<>());
        if (student.getMemberships() == null) student.setMemberships(new HashSet<>());

        Optional<ClubMember> existing = club.getMembers().stream()
                .filter(cm -> Boolean.TRUE.equals(cm.getRecordStatus()))
                .filter(cm -> cm.getStudent() != null && studentId.equals(cm.getStudent().getId()))
                .findAny();

        if (existing.isPresent()) {
            return club.getWhatsappGroupLink();
        }

        long activeCount = club.getMembers().stream()
                .filter(cm -> Boolean.TRUE.equals(cm.getRecordStatus()))
                .count();

        if (club.getCapacity() != null && activeCount >= club.getCapacity()) {
            throw new CustomExceptions("El club ya alcanzó su capacidad", 400);
        }

        ClubMember membership = ClubMember.builder()
                .club(club)
                .student(student)
                .recordStatus(true)
                .build();

        club.getMembers().add(membership);
        student.getMemberships().add(membership);

        clubRepository.save(club);

        return club.getWhatsappGroupLink();
    }

    @Override
    @Transactional
    public void leaveClub(Long studentId, Long clubId) {
        Club club = clubRepository.findByIdAndRecordStatusTrue(clubId)
                .orElseThrow(() -> new CustomExceptions("Club no encontrado o inactivo", 404));
        Student student = studentRepository.findByIdAndRecordStatusTrue(studentId)
                .orElseThrow(() -> new CustomExceptions("Estudiante no encontrado o inactivo", 404));

        if (club.getMembers() == null || club.getMembers().isEmpty()) {
            throw new CustomExceptions("El estudiante no está inscrito en el club", 400);
        }

        Optional<ClubMember> membership = club.getMembers().stream()
                .filter(cm -> Boolean.TRUE.equals(cm.getRecordStatus()))
                .filter(cm -> cm.getStudent() != null && studentId.equals(cm.getStudent().getId()))
                .findAny();

        if (membership.isPresent()) {
            ClubMember cm = membership.get();
            club.getMembers().remove(cm);
            if (student.getMemberships() != null) {
                student.getMemberships().remove(cm);
            }
            clubRepository.save(club);
        } else {
            throw new CustomExceptions("El estudiante no está inscrito en el club", 400);
        }
    }
}