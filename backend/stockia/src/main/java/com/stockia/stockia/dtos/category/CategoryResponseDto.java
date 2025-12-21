package com.stockia.stockia.dtos.category;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {

    private UUID id;

    private String name;

    private String description;

    private Boolean isActive;

    private Integer productCount;
}
