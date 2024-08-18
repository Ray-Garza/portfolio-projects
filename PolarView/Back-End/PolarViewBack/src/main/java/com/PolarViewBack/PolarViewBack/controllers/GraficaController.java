package com.PolarViewBack.PolarViewBack.controllers;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PolarViewBack.PolarViewBack.DTO.graficaDTO;
import com.PolarViewBack.PolarViewBack.models.GraficaModel;
import com.PolarViewBack.PolarViewBack.models.MedicionDataRequest;
import com.PolarViewBack.PolarViewBack.models.MedicionModel;
import com.PolarViewBack.PolarViewBack.repositories.GraficaRepository;
import com.PolarViewBack.PolarViewBack.repositories.MedicionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.PolarViewBack.PolarViewBack.services.graficaService;





@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/grafica")
public class GraficaController {
    
    @Autowired
    private GraficaRepository graficaRepository;

    @Autowired
    private MedicionRepository medicionRepository;

    @Autowired
    private graficaService graficaService;
    
    @PostMapping("/guardarGrafica")
    public ResponseEntity<GraficaModel> guardarGrafica(@RequestBody GraficaModel graficaModel){
        GraficaModel savedGrafica = graficaRepository.save(graficaModel);
        return ResponseEntity.ok(savedGrafica);
    }

    @PostMapping("/guardarMediciones")
    public ResponseEntity<?> saveGraficaData(@RequestBody MedicionDataRequest request) {
        // Obtener la gráfica correspondiente
        GraficaModel grafica = graficaRepository.findById(request.getGraficaId()).orElseThrow(() -> new RuntimeException("Grafica not found"));

        // Crear lista para las mediciones
        List<MedicionModel> mediciones = new ArrayList<>();

        // Asegurarse de que los datos de xData y yData tengan la misma longitud
        if (request.getxData().size() != request.getyData().size()) {
            return ResponseEntity.badRequest().body("xData and yData must have the same length");
        }

        // Procesar los datos y crear objetos de medición
        for (int i = 0; i < request.getxData().size(); i++) {
            MedicionModel medicion = new MedicionModel();
            medicion.setValor(request.getyData().get(i));
            medicion.setPosicion(request.getxData().get(i));  
            medicion.setGrafica(grafica);
            mediciones.add(medicion);
        }

        // Guardar todas las mediciones en la base de datos
        medicionRepository.saveAll(mediciones);

        return ResponseEntity.ok(mediciones);
    }

    @GetMapping("/obtenerGraficas")
    public  ResponseEntity<List<GraficaModel>> getNombresGraficas() {
        List<GraficaModel> graficas = graficaService.obtenerGraficas();                                                
        return ResponseEntity.ok(graficas);
    }
    
    @GetMapping("/obtenerMediciones/{graficaId}")
    public ResponseEntity<graficaDTO> getGraficaDTO(@PathVariable Long graficaId) {
        
        graficaDTO graficaDTO = graficaService.getGraficaDTO(graficaId);        
        
        return ResponseEntity.ok(graficaDTO);
    }
    

    @DeleteMapping("/borrarGrafica/{graficaId}")
    public ResponseEntity<String> borrarGrafica(@PathVariable Long graficaId){
        graficaService.deleteGrafica(graficaId);
        return ResponseEntity.ok(null);
    }

}
