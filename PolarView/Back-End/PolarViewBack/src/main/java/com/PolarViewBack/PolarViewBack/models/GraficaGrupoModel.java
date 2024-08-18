package com.PolarViewBack.PolarViewBack.models;

import jakarta.persistence.*;

@Entity
@Table(name = "GraficaGrupo")

public class GraficaGrupoModel {
    public GraficaGrupoModel() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "ID") 
    private Long id;

    @ManyToOne
    @JoinColumn(name = "GraficaID")
    private GraficaModel grafica;

    @ManyToOne
    @JoinColumn(name = "GrupoID")
    private GrupoModel grupo;

    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GraficaModel getGrafica() {
        return grafica;
    }

    public void setGrafica(GraficaModel grafica) {
        this.grafica = grafica;
    }

    public GrupoModel getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoModel grupo) {
        this.grupo = grupo;
    }
}
