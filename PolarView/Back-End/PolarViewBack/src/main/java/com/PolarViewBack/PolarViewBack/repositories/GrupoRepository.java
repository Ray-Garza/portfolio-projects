package com.PolarViewBack.PolarViewBack.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.PolarViewBack.PolarViewBack.models.GrupoModel;

@Repository
public interface GrupoRepository extends CrudRepository<GrupoModel, Long>{
    
}
