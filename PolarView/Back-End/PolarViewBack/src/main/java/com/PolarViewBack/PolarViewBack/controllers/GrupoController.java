
package com.PolarViewBack.PolarViewBack.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PolarViewBack.PolarViewBack.DTO.GrupoDTO;
import com.PolarViewBack.PolarViewBack.models.GraficaModel;
import com.PolarViewBack.PolarViewBack.models.GrupoModel;
import com.PolarViewBack.PolarViewBack.repositories.GraficaRepository;

import com.PolarViewBack.PolarViewBack.services.GrupoService;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/grupo")
public class GrupoController {

    @Autowired
    private GraficaRepository graficaRepository;
    
    @Autowired GrupoService grupoService;

    @PostMapping("/guardarGrupo")
    public ResponseEntity<GrupoModel> guardarGrupo(@RequestBody GrupoDTO grupoDTO) {
        GrupoModel grupo = new GrupoModel();
        System.out.println(grupoDTO.getNombreGrupo());
        grupo.setNombre(grupoDTO.getNombreGrupo());
        

        GraficaModel graficaReferencia = graficaRepository.findById(grupoDTO.getIdReferencia()).orElseThrow(()-> new RuntimeException("Grafica not found"));
        grupo.setGraficaReferencia(graficaReferencia);

        grupo = grupoService.guardarGrupo(grupo);

        grupoService.guardarGraficaGrupo(grupoDTO.getIds(), grupo);


        
        return ResponseEntity.ok(grupo);
    }

    @GetMapping("/obtenerGrupos")
    public ResponseEntity<List<GrupoDTO>> obtenerGrupos() {
        List<GrupoDTO> grupos = grupoService.obtenerGrupos();
        return ResponseEntity.ok(grupos);
    }
    
    @DeleteMapping("/borrarGrupo/{grupoId}")
    public ResponseEntity<String> borrarGrupo(@PathVariable Long grupoId){
        System.out.println(grupoId);
        grupoService.borrarGrupo(grupoId);
        return ResponseEntity.ok(null);
    }
    

}
