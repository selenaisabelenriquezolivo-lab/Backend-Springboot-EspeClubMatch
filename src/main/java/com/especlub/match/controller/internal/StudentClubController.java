package com.especlub.match.controller.internal;

import com.especlub.match.docs.StudentClubControllerDoc;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.services.interfaces.StudentClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clubs")
@RequiredArgsConstructor
public class StudentClubController implements StudentClubControllerDoc {

    private final StudentClubService studentClubService;

    @PostMapping("/{clubId}/enroll/{studentId}")
    public ResponseEntity<JsonDtoResponse<String>> enroll(@PathVariable Long clubId, @PathVariable Long studentId) {
        String whatsappLink = studentClubService.enrollStudent(studentId, clubId);
        return JsonDtoResponse.ok("Inscripción realizada", whatsappLink).toResponseEntity();
    }

    @PostMapping("/{clubId}/leave/{studentId}")
    public ResponseEntity<JsonDtoResponse<Void>> leave(@PathVariable Long clubId, @PathVariable Long studentId) {
        studentClubService.leaveClub(studentId, clubId);
        return JsonDtoResponse.<Void>ok("Salida del club realizada", null).toResponseEntity();
    }
}