package com.PolarViewBack.PolarViewBack.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.PolarViewBack.PolarViewBack.models.MedicionModel;

@Repository
public interface MedicionRepository extends CrudRepository<MedicionModel, Long> {
    List<MedicionModel> findByGrafica_Id(Long graficaId);
}
