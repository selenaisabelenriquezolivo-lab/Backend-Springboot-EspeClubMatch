package com.especlub.match.controller.admin;

import com.especlub.match.dto.request.CreateEventRequestDto;
import com.especlub.match.dto.response.EventAdminDto;
import com.especlub.match.dto.response.JsonDtoResponse;
import com.especlub.match.services.interfaces.AdminEventService;
import com.especlub.match.services.interfaces.ClubMemberService;
import com.especlub.match.services.interfaces.EventNotificationService;
import com.especlub.match.shared.utils.RolePermissions;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/events")
@RequiredArgsConstructor
@PreAuthorize(RolePermissions.ADMIN_GENERAL)
@Validated
public class AdminEventController {

    private final AdminEventService adminEventService;
    private final EventNotificationService eventNotificationService;

    @PostMapping
    public ResponseEntity<JsonDtoResponse<EventAdminDto>> create(@Valid @RequestBody CreateEventRequestDto dto) {
        EventAdminDto created = adminEventService.create(dto);
        return JsonDtoResponse.created("Evento creado", created).toResponseEntity();
    }

    @PostMapping("/{id}/notify")
    public ResponseEntity<JsonDtoResponse<Void>> notifyMembers(@PathVariable("id") Long eventId) {
        try {
            return JsonDtoResponse.ok("Notificaciones encoladas", eventNotificationService.notifyEventToMembers(eventId)).toResponseEntity();
        } catch (Exception ex) {
            log.error("Error while notifying members for event id={}: {}", eventId, ex.getMessage(), ex);
            return JsonDtoResponse.<Void>error("Error al encolar notificaciones", 500).toResponseEntity();
        }
    }
}