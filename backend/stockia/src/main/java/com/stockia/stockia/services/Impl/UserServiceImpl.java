package com.stockia.stockia.services.Impl;

import com.stockia.stockia.dtos.user.UserSearchRequestDto;
import com.stockia.stockia.dtos.user.UserSearchResponseDto;
import com.stockia.stockia.dtos.user.UserUpdateRequestDto;
import com.stockia.stockia.enums.AccountStatus;
import com.stockia.stockia.enums.Role;
import com.stockia.stockia.exceptions.DuplicateResourceException;
import com.stockia.stockia.exceptions.UnauthorizedException;
import com.stockia.stockia.exceptions.UserNotFoundException;
import com.stockia.stockia.mappers.UserMapper;
import com.stockia.stockia.models.User;
import com.stockia.stockia.repositories.UserRepository;
import com.stockia.stockia.services.AuthService;
import com.stockia.stockia.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthService authService;

    @Override
    public Page<UserSearchResponseDto> searchUsers(UserSearchRequestDto params, Pageable pageable) {
        Page<User> users = userRepository.searchUsers(params.email(),
                params.name(),
                params.role(),
                params.accountStatus(),
                pageable);
        return userMapper.toUserSearchResponseDto(users);
    }

    @Override
    public UserSearchResponseDto findById(UUID id) {
        User userAuth = authService.getAuthenticatedUser()
                .orElseThrow(() -> new UnauthorizedException("Usuario no autenticado"));

        if (!userAuth.getRole().equals(Role.ADMIN) && !userAuth.getId().equals(id)) {
            throw new AccessDeniedException("No tienes permiso para ver este perfil");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        return userMapper.toUserSearchResponseDto(user);
    }

    @Override
    public UserSearchResponseDto updateById(UUID id, @Valid UserUpdateRequestDto request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        if (request.email() != null && !request.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.email())) {
                throw new DuplicateResourceException("El email '" + request.email() + "' ya está registrado");
            }
        }

        userMapper.updateUserFromDto(request, user);

        return userMapper.toUserSearchResponseDto(userRepository.save(user));
    }

    @Override
    public void deleteById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        user.setDeleted(true);
        user.setAccountStatus(AccountStatus.INACTIVE);

        userRepository.save(user);
    }
}
