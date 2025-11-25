package com.especlub.match.services.impl;

import com.especlub.match.dto.request.CreateEventRequestDto;
import com.especlub.match.dto.response.EventAdminDto;
import com.especlub.match.models.Club;
import com.especlub.match.models.Event;
import com.especlub.match.models.UserInfo;
import com.especlub.match.repositories.ClubRepository;
import com.especlub.match.repositories.EventRepository;
import com.especlub.match.repositories.UserInfoRepository;
import com.especlub.match.security.jwt.JwtProvider;
import com.especlub.match.services.interfaces.AdminEventService;
import com.especlub.match.shared.exceptions.CustomExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;
    private final ClubRepository clubRepository;
    private final UserInfoRepository userInfoRepository;
    private final JwtProvider jwtProvider;

    @Override
    @Transactional
    public EventAdminDto create(CreateEventRequestDto dto) {
        Club club = clubRepository.findByIdAndRecordStatusTrue(dto.getClubId())
                .orElseThrow(() -> new CustomExceptions("Club not found or inactive", 404));

        UserInfo creator = userInfoRepository.findByIdAndRecordStatusTrue(dto.getCreatedByUserInfoId())
                .orElseThrow(() -> new CustomExceptions("Creator user not found or inactive", 404));

        if (dto.getEndAt() != null && dto.getEndAt().isBefore(dto.getStartAt())) {
            throw new CustomExceptions("endAt must be after startAt", 400);
        }

        Event event = Event.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startAt(dto.getStartAt())
                .endAt(dto.getEndAt())
                .location(dto.getLocation())
                .virtualLink(dto.getVirtualLink())
                .club(club)
                .createdBy(creator)
                .recordStatus(true)
                .build();

        Event saved = eventRepository.save(event);

        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventAdminDto> findAllActiveByUser(String jwt) {
        String username = jwtProvider.getNombreUsuarioFromToken(jwt);
        UserInfo user = userInfoRepository.findByUsernameAndRecordStatusTrue(username);
        if (user == null) {
            throw new CustomExceptions("Usuario no encontrado o inactivo", org.springframework.http.HttpStatus.NOT_FOUND.value());
        }
        List<Event> events = eventRepository.findAllByRecordStatusTrueAndCreatedByIdOrderByStartAtAsc(user.getId());
        return events.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public EventAdminDto findById(Long id) {
        Optional<Event> opt = eventRepository.findById(id);
        return opt.map(this::toDto).orElse(null);
    }

    private EventAdminDto toDto(Event e) {
        if (e == null) return null;
        return EventAdminDto.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .startAt(e.getStartAt())
                .endAt(e.getEndAt())
                .location(e.getLocation())
                .virtualLink(e.getVirtualLink())
                .clubId(e.getClub() != null ? e.getClub().getId() : null)
                .createdByUserInfoId(e.getCreatedBy() != null ? e.getCreatedBy().getId() : null)
                .recordStatus(e.getRecordStatus())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}