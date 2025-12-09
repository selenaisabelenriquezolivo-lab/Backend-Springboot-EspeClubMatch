package com.especlub.match.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.http.ResponseEntity;

import com.especlub.match.dto.request.CreateEventRequestDto;
import com.especlub.match.dto.response.EventAdminDto;
import com.especlub.match.dto.response.JsonDtoResponse;

@Tag(name = "Admin Events", description = "Operaciones administrativas sobre eventos")
public interface AdminEventControllerDoc {

    @Operation(summary = "Crear evento", description = "Crea un nuevo evento en la plataforma (ámbito administrativo)")
    @RequestBody(description = "DTO con los datos para crear un evento", required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateEventRequestDto.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Evento creado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<JsonDtoResponse<EventAdminDto>> create(CreateEventRequestDto dto);

    @Operation(summary = "Notificar asistentes a un evento", description = "Encola notificaciones para los miembros asociados al evento especificado por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificaciones encoladas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonDtoResponse.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    ResponseEntity<JsonDtoResponse<Void>> notifyMembers(Long id);
}

