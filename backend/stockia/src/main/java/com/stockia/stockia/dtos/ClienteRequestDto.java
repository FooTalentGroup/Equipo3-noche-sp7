package com.stockia.stockia.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ClienteRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico es inválido")
    private String correoElectronico;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(
        regexp = "^\\+[1-9][0-9]{1,3}[0-9]{8,12}$", 
        message = "El teléfono debe comenzar con código de país (+XX) seguido de 8-12 dígitos. Ejemplo: +54911234567"
    )
    private String telefono;

    @NotNull(message = "Debe indicar si es cliente frecuente")
    private Boolean clienteFrecuente;
   
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public Boolean getClienteFrecuente() { return clienteFrecuente; }
    public void setClienteFrecuente(Boolean clienteFrecuente) { this.clienteFrecuente = clienteFrecuente; }
}