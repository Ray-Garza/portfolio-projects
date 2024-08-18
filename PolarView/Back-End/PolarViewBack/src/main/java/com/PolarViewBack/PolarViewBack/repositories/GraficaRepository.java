package com.PolarViewBack.PolarViewBack.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.PolarViewBack.PolarViewBack.models.GraficaModel;


@Repository
public interface GraficaRepository extends CrudRepository<GraficaModel, Long> {
    
}
