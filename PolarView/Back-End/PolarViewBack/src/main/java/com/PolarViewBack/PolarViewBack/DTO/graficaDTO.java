package com.PolarViewBack.PolarViewBack.DTO;

import java.util.List;

public class graficaDTO {
    private Long id;
    private String nombre;
    private List<coordenada> coordenadas;
    
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
    public List<coordenada> getCoordenadas() {
        return coordenadas;
    }
    public void setCoordenadas(List<coordenada> coordenadas) {
        this.coordenadas = coordenadas;
    }
    

    
}
