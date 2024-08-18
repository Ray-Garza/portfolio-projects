package com.PolarViewBack.PolarViewBack.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Medicion")
public class MedicionModel {
    
    public MedicionModel(){

    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private float valor;
    private float posicion;

    @ManyToOne
    @JoinColumn(name = "graficaID")    
    private GraficaModel grafica;

    
    public GraficaModel getGrafica() {
        return grafica;
    }
    public void setGrafica(GraficaModel grafica) {
        this.grafica = grafica;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public float getValor() {
        return valor;
    }
    public void setValor(float valor) {
        this.valor = valor;
    }
    public float getPosicion() {
        return posicion;
    }
    public void setPosicion(float posicion) {
        this.posicion = posicion;
    }
}
