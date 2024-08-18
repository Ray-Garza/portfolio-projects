package com.PolarViewBack.PolarViewBack.services;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PolarViewBack.PolarViewBack.DTO.GrupoDTO;
import com.PolarViewBack.PolarViewBack.models.GraficaGrupoModel;
import com.PolarViewBack.PolarViewBack.models.GraficaModel;
import com.PolarViewBack.PolarViewBack.models.GrupoModel;
import com.PolarViewBack.PolarViewBack.repositories.GraficaGrupoRepository;
import com.PolarViewBack.PolarViewBack.repositories.GraficaRepository;
import com.PolarViewBack.PolarViewBack.repositories.GrupoRepository;

@Service
public class GrupoService {
    
    @Autowired
    private GraficaRepository graficaRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired 
    private GraficaGrupoRepository graficaGrupoRepository;

    
    
    public GrupoModel guardarGrupo(GrupoModel grupo){
        GrupoModel saveGrupo = grupoRepository.save(grupo);

        return saveGrupo;
        
    }

    public void guardarGraficaGrupo(List<Long> ids, GrupoModel grupo){
        for (Long id : ids) {
            GraficaGrupoModel graficaGrupo = new GraficaGrupoModel();
            GraficaModel grafica = graficaRepository.findById(id).orElseThrow(()-> new RuntimeException("Grafica not found"));
            graficaGrupo.setGrafica(grafica);
            graficaGrupo.setGrupo(grupo);
            graficaGrupoRepository.save(graficaGrupo);            
        }        

        
    }
    public List<GrupoDTO> obtenerGrupos(){
        List<GrupoModel> grupos = (List<GrupoModel>) grupoRepository.findAll();
        List<GrupoDTO> gruposDTO = new ArrayList<>();        
        GrupoDTO grupoDTO;


        for (GrupoModel grupo : grupos) {

            grupoDTO = new GrupoDTO();
            grupoDTO.setId(grupo.getId());
            grupoDTO.setIdReferencia(grupo.getGraficaReferencia().getId());
            grupoDTO.setNombreGrupo(grupo.getNombre());
            grupoDTO.setIds(obtenerGraficasDeGrupo(grupo.getId()));





            gruposDTO.add(grupoDTO);

        }

        return gruposDTO;

    }

    public List<Long> obtenerGraficasDeGrupo(Long id){
        List<GraficaGrupoModel> graficasGrupo = graficaGrupoRepository.findByGrupo_Id(id);
        List<Long> ids = new ArrayList<>();        

        for (GraficaGrupoModel graficaGrupo : graficasGrupo) {
            ids.add(graficaGrupo.getGrafica().getId());            
        }

        return ids;

    }

    public void borrarGrupo(Long grupoId){
        List<GraficaGrupoModel> graficasGrupo = graficaGrupoRepository.findByGrupo_Id(grupoId);
        for(GraficaGrupoModel graficaGrupo: graficasGrupo) {
            graficaGrupoRepository.deleteById(graficaGrupo.getId());
        }
        grupoRepository.deleteById(grupoId);
    }
    
}
