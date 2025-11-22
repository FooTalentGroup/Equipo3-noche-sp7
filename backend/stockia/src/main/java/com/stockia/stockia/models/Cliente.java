package com.stockia.stockia.models;

import jakarta.persistence.*; 

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true, nullable = false)
    private String correoElectronico; 

    @Column(unique = true, nullable = false)
    private String telefono; 

    private Boolean clienteFrecuente;

    
    public Cliente() {} 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public Boolean getClienteFrecuente() { return clienteFrecuente; }
    public void setClienteFrecuente(Boolean clienteFrecuente) { this.clienteFrecuente = clienteFrecuente; }
}
