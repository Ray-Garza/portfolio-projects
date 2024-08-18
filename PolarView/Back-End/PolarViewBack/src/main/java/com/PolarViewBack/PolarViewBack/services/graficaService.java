package com.PolarViewBack.PolarViewBack.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PolarViewBack.PolarViewBack.DTO.coordenada;
import com.PolarViewBack.PolarViewBack.DTO.graficaDTO;
import com.PolarViewBack.PolarViewBack.models.GraficaModel;
import com.PolarViewBack.PolarViewBack.models.MedicionModel;
import com.PolarViewBack.PolarViewBack.repositories.GraficaRepository;
import com.PolarViewBack.PolarViewBack.repositories.MedicionRepository;

@Service
public class graficaService {

    @Autowired
    private GraficaRepository graficaRepository;

    @Autowired
    private MedicionRepository medicionRepository;

    public graficaDTO getGraficaDTO(Long id){
        GraficaModel grafica = graficaRepository.findById(id).orElseThrow(()-> new RuntimeException("Grafica not found"));

        List<MedicionModel> mediciones = medicionRepository.findByGrafica_Id(id);

        List<coordenada> coordenadas = mediciones.stream()
            .map(medicion -> new coordenada(medicion.getPosicion(), medicion.getValor()))
            .collect(Collectors.toList());

        graficaDTO graficaDTO = new graficaDTO();
        graficaDTO.setId(grafica.getId());
        graficaDTO.setNombre(grafica.getNombre());
        graficaDTO.setCoordenadas(coordenadas);

        return graficaDTO;

    }

    public List<GraficaModel> obtenerGraficas(){
        return (List<GraficaModel>) graficaRepository.findAll();
    }

    public void deleteGrafica(Long id) {
        List<MedicionModel> mediciones = medicionRepository.findByGrafica_Id(id);
        for (MedicionModel medicion : mediciones) {
            medicionRepository.delete(medicion);                
        }
        graficaRepository.deleteById(id);
    }
    
}
