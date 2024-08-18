import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GrupoModel } from '../models/grupo.model';
import { Observable } from 'rxjs';
import { environment } from 'src/environment/environment';

/*Este servicio maneja la lógica detrás de todo lo relacionado con los grupo.
Ofrece métodos para guardar los objetos en el servidor */

@Injectable({
  providedIn: 'root'
})
export class GrupoService {

  private apiUrl = environment.apiUrl+'/grupo'; // URL del backend Spring Boot

  constructor(private http: HttpClient) { }  

  saveGrupo(grupo: GrupoModel): Observable<GrupoModel> {
    return this.http.post<GrupoModel>(this.apiUrl.concat("/guardarGrupo"), grupo);
  }

  getGrupos(){
    return this.http.get<GrupoModel[]>(this.apiUrl.concat("/obtenerGrupos"))
  }

  deleteGrupo(id: number){
    return this.http.delete<void>(this.apiUrl.concat("/borrarGrupo/").concat(id.toString()))
  }

}
