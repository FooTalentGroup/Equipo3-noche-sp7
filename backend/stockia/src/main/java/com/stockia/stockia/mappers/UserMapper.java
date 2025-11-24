package com.stockia.stockia.mappers;

import com.stockia.stockia.dtos.auth.JwtDataDto;
import com.stockia.stockia.dtos.auth.RegisterRequestDto;
import com.stockia.stockia.dtos.auth.RegisterResponseDto;
import com.stockia.stockia.dtos.user.UserSearchResponseDto;
import com.stockia.stockia.dtos.user.UserUpdateRequestDto;
import com.stockia.stockia.enums.Role;
import com.stockia.stockia.models.User;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", imports = { Role.class })
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "encodedPassword")
    @Mapping(target = "email", source = "requestDto.email")
    @Mapping(target = "name", source = "requestDto.name")
    @Mapping(target = "role", source = "requestDto.role")
    @Mapping(target = "accountStatus", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(RegisterRequestDto requestDto, String encodedPassword);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "accountStatus", source = "user.accountStatus")
    JwtDataDto toJwtDataDto(User user);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "accountStatus", source = "user.accountStatus")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "deleted", source = "user.deleted")
    @Mapping(target = "createdAt", source = "user.createdAt")
    @Mapping(target = "updatedAt", source = "user.updatedAt")
    RegisterResponseDto toDto(User user);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "accountStatus", source = "user.accountStatus")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "deleted", source = "user.deleted")
    @Mapping(target = "createdAt", source = "user.createdAt")
    @Mapping(target = "updatedAt", source = "user.updatedAt")
    UserSearchResponseDto toUserSearchResponseDto(User user);

    default Page<UserSearchResponseDto> toUserSearchResponseDto(Page<User> users) {
        return users.map(this::toUserSearchResponseDto);
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", source = "dto.email")
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "role", source = "dto.role")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "accountStatus", source = "dto.accountStatus")
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateUserFromDto(UserUpdateRequestDto dto, @MappingTarget User user);
}
