package com.PolarViewBack.PolarViewBack.DTO;

import java.util.List;

public class GrupoDTO {
    private List<Long> ids;
    private String nombreGrupo;
    private Long idReferencia;
    private Long id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public List<Long> getIds() {
        return ids;
    }
    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
    public String getNombreGrupo() {
        return nombreGrupo;
    }
    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }
    public Long getIdReferencia() {
        return idReferencia;
    }
    public void setIdReferencia(Long idReferencia) {
        this.idReferencia = idReferencia;
    }

    
}
