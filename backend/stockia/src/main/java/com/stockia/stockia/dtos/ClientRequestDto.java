package com.stockia.stockia.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ClientRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico es inválido")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\+[1-9][0-9]{1,3}[0-9]{8,12}$", message = "El teléfono debe comenzar con código de país (+XX) seguido de 8-12 dígitos. Ejemplo: +54911234567")
    private String phone;

    @NotNull(message = "Debe indicar si es cliente frecuente")
    private Boolean isFrequentClient;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getIsFrequentClient() {
        return isFrequentClient;
    }

    public void setIsFrequentClient(Boolean isFrequentClient) {
        this.isFrequentClient = isFrequentClient;
    }
}