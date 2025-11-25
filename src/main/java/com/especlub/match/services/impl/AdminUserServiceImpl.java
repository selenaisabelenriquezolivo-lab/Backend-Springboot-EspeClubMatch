package com.especlub.match.services.impl;

import com.especlub.match.dto.request.CreateUserRequestDto;
import com.especlub.match.dto.request.UpdateUserRequestDto;
import com.especlub.match.dto.request.UserAdminDto;
import com.especlub.match.models.UserInfo;
import com.especlub.match.repositories.UserInfoRepository;
import com.especlub.match.services.interfaces.AdminUserService;
import com.especlub.match.shared.exceptions.CustomExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserInfoRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String USER_NOT_FOUND_MSG = "User not found";

    @Override
    @Transactional(readOnly = true)
    public List<UserAdminDto> listAllActive() {
        return userRepository.findAll()
                .stream()
                .filter(u -> Boolean.TRUE.equals(u.getRecordStatus()))
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserAdminDto getById(Long id) {
        UserInfo u = userRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions(USER_NOT_FOUND_MSG, 404));
        return toDto(u);
    }

    @Override
    @Transactional
    public UserAdminDto create(CreateUserRequestDto dto) {
        // basic existence checks
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new CustomExceptions("Email already in use", 400);
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new CustomExceptions("Username already in use", 400);
        }

        UserInfo user = new UserInfo();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setNames(dto.getFirstName());
        user.setSurnames(dto.getLastName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRecordStatus(true);
        UserInfo saved = userRepository.save(user);
        return toDto(saved);
    }

    @Override
    @Transactional
    public UserAdminDto update(Long id, UpdateUserRequestDto dto) {
        UserInfo user = userRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions(USER_NOT_FOUND_MSG, 404));
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getFirstName() != null) user.setNames(dto.getFirstName());
        if (dto.getLastName() != null) user.setSurnames(dto.getLastName());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        UserInfo saved = userRepository.save(user);
        return toDto(saved);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        UserInfo user = userRepository.findById(id)
                .orElseThrow(() -> new CustomExceptions(USER_NOT_FOUND_MSG, 404));
        user.setRecordStatus(false);
        userRepository.save(user);
        return true;
    }

    private UserAdminDto toDto(UserInfo u) {
        return UserAdminDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .phone(u.getPhone())
                .firstName(u.getNames())
                .lastName(u.getSurnames())
                .build();
    }
}
