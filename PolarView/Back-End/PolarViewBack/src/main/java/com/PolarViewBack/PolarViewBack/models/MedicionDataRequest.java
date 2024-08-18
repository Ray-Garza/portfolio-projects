package com.PolarViewBack.PolarViewBack.models;

import java.util.List;

public class MedicionDataRequest {
    private List<Float> xData;
    private List<Float> yData;
    private Long graficaId;
    
    public List<Float> getxData() {
        return xData;
    }
    public void setxData(List<Float> xData) {
        this.xData = xData;
    }
    public List<Float> getyData() {
        return yData;
    }
    public void setyData(List<Float> yData) {
        this.yData = yData;
    }
    public Long getGraficaId() {
        return graficaId;
    }
    public void setGraficaId(Long graficaId) {
        this.graficaId = graficaId;
    }
    
}
