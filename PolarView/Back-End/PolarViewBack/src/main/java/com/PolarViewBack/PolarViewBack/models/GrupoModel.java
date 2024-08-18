package com.PolarViewBack.PolarViewBack.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Grupo")

public class GrupoModel {
    public GrupoModel(){
        
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "GraficaReferenciaID")
    private GraficaModel graficaReferencia;

    public GraficaModel getGraficaReferencia() {
        return graficaReferencia;
    }
    public void setGraficaReferencia(GraficaModel graficaReferencia) {
        this.graficaReferencia = graficaReferencia;
    }

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
    
}
