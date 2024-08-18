import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GraficaModel } from '../models/grafica.model';
import { GraficaCoordenadaModel } from '../models/graficaCoordenada.model';
import { environment } from 'src/environment/environment';

/*Este servicio se encarga de trabajar con toda la lógica detrás de los puntos de las graficas.
Ofrece métodos http para guardar los objetos en el servidor*/

@Injectable({
  providedIn: 'root'
})
export class GraficaService {

  private apiUrl = environment.apiUrl+'/grafica'; // URL del backend Spring Boot

  constructor(private http: HttpClient) { }

  saveGrafica(grafica: GraficaModel): Observable<GraficaModel> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<GraficaModel>(this.apiUrl.concat("/guardarGrafica"), grafica, { headers });
  }

  saveMediciones(xData: number[], yData: number[], graficaId: number){
    const payload = {
      xData: xData,
      yData: yData,
      graficaId: graficaId
    };
    return this.http.post(this.apiUrl.concat("/guardarMediciones"), payload)
  }

  getNombres() {
    return this.http.get<GraficaModel[]>(this.apiUrl.concat("/obtenerGraficas"))
  }

  getMediciones(graficaId: number){
    return this.http.get<GraficaCoordenadaModel>(this.apiUrl.concat("/obtenerMediciones/").concat(graficaId.toString()));
  }

  deleteGrafica(graficaId: number){
    return this.http.delete<void>(this.apiUrl.concat("/borrarGrafica/").concat(graficaId.toString()));
  }
}
