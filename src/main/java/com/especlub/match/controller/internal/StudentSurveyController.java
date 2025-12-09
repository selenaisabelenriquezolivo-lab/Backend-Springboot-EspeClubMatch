package com.especlub.match.controller.internal;

import com.especlub.match.dto.request.CreateSurveyRequestDto;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.dto.response.RecommendationListDto;
import com.especlub.match.dto.response.StudentSurveyResponseDto;
import com.especlub.match.models.StudentSurvey;
import com.especlub.match.services.StudentSurveyService;
import com.especlub.match.docs.StudentSurveyControllerDoc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentSurveyController implements StudentSurveyControllerDoc {

    private final StudentSurveyService studentSurveyService;

    @PostMapping("/{studentId}/survey")
    public ResponseEntity<JsonDtoResponse<StudentSurveyResponseDto>> createSurvey(
            @PathVariable Long studentId,
            @Valid @RequestBody CreateSurveyRequestDto dto) {

        StudentSurvey saved = studentSurveyService.saveSurvey(studentId, dto);
        StudentSurveyResponseDto resp = studentSurveyService.toResponseDto(saved);

        return JsonDtoResponse.created("Survey saved", resp).toResponseEntity();
    }

    @PostMapping("/{studentId}/survey/recommendation")
    public ResponseEntity<JsonDtoResponse<RecommendationListDto>> generateRecommendation(@PathVariable Long studentId) {
        studentSurveyService.generateRecommendation(studentId);
        RecommendationListDto recommendations = studentSurveyService.recommendClubs(studentId);
        return JsonDtoResponse.ok("Recommendations generated", recommendations).toResponseEntity();
    }
}