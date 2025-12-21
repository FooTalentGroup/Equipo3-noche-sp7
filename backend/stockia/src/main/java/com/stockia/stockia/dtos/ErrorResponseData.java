package com.stockia.stockia.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseData {

    private Map<String, String> fields;

    @Builder.Default
    private List<String> errors = new ArrayList<>();
}
