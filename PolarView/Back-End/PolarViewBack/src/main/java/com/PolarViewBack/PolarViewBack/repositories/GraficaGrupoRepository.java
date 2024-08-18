package com.PolarViewBack.PolarViewBack.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.PolarViewBack.PolarViewBack.models.GraficaGrupoModel;


@Repository
public interface GraficaGrupoRepository extends CrudRepository<GraficaGrupoModel, Long>{

    List<GraficaGrupoModel> findByGrupo_Id(Long grupoId);
    
}
