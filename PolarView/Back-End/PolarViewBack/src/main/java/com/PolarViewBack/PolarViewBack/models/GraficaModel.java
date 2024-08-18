package com.PolarViewBack.PolarViewBack.models;


import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;;

@Entity
@Table(name = "Grafica")
public class GraficaModel {
    
    public GraficaModel(){

    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;    
    private String nombre;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime fecha;



    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public OffsetDateTime getFecha() {
        return fecha;
    }
    public void setFecha(OffsetDateTime fecha) {
        this.fecha = fecha;
    }
    
}
