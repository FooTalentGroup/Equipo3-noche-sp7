package com.stockia.stockia.controllers;

import com.stockia.stockia.documentation.user.DeleteUserEndpointDoc;
import com.stockia.stockia.documentation.user.GetAllUsersEndpointDoc;
import com.stockia.stockia.documentation.user.GetUserByIdEndpointDoc;
import com.stockia.stockia.documentation.user.UpdateUserEndpointDoc;
import com.stockia.stockia.dtos.user.UserSearchRequestDto;
import com.stockia.stockia.dtos.user.UserSearchResponseDto;
import com.stockia.stockia.dtos.user.UserUpdateRequestDto;
import com.stockia.stockia.services.UserService;
import com.stockia.stockia.utils.ApiResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "02 - Usuarios", description = "Endpoints para la gestión de usuarios ")
public class UserController {

    private final UserService userService;

    @GetAllUsersEndpointDoc
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllWithSearch(@ParameterObject @Valid UserSearchRequestDto params,
            @ParameterObject Pageable pageable) {
        Page<UserSearchResponseDto> response = userService.searchUsers(params, pageable);
        return ResponseEntity.ok()
                .body(ApiResult.success(response, "Operación exitosa"));
    }

    @GetUserByIdEndpointDoc
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        UserSearchResponseDto response = userService.findById(id);
        return ResponseEntity.ok().body(ApiResult.success(response, "Usuario encontrado"));
    }

    @UpdateUserEndpointDoc
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable UUID id, @RequestBody @Valid UserUpdateRequestDto request) {
        UserSearchResponseDto response = userService.updateById(id, request);
        return ResponseEntity.ok().body(ApiResult.success(response, "Actualizacion exitosa"));
    }

    @DeleteUserEndpointDoc
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
