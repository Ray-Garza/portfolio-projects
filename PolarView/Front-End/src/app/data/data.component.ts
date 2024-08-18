import { Component } from '@angular/core';
import {Form, FormControl} from '@angular/forms';
import { PlotService } from '../services/plot.service';
import { GraficaService } from '../services/grafica.service';
import { GraficaModel } from '../models/grafica.model';
import { GraficaCoordenadaModel } from '../models/graficaCoordenada.model';
import { GrupoModel } from '../models/grupo.model';
import { GrupoService } from '../services/grupo.service';




@Component({
  selector: 'app-data',
  templateUrl: './data.component.html',
  styleUrls: ['./data.component.css'],  
})

export class DataComponent {  
  xData: number[] = [];
  yData: number[] = [];
  plots: FormControl<GraficaModel[] | null> = new FormControl<GraficaModel[]>([]);  
  nombreGrupo: string = this.obtenerFecha();
  chart: any;
  graficas: GraficaModel[] = [];  
  
  
  selectedGrafica: string = "";
  nombreGrafica: string="";
  listaGraficasGraficadas: number[] = [];
  listaAnteriorGraficasGraficadas: number[] = [];

  plotsModal = new FormControl();
  esModal=false;
  graficaBorrar=-1;

  constructor(private plot: PlotService, private grafica: GraficaService, private grupo: GrupoService){}

  ngAfterViewInit(): void {
    this.chart = this.plot.crearPlotVacio('chart-data')

    this.grafica.getNombres().subscribe(response =>{
      console.log(response);
      this.graficas = response

      
    }, error => {
      console.error(error)
    })
    
  }

  //Método cuando el select del main plot cambie
  onGraficaChange(event: Event): void {
    const target = event.target as HTMLSelectElement;
    this.selectedGrafica = target.value;
    console.log('Selected grafica:', this.selectedGrafica);
    
  }

  //Metodo cuando el select de los datos de los plots cambie
  onSelectionChange(event: any): void {     
    if(this.plots.value) {
      if(this.plots.value.length === 1){    
        if(this.plots.value[0].id != null)    
          this.selectedGrafica = this.plots.value[0].id.toString();
      }
      
      console.log('Selected values:', this.plots.value[this.plots.value?.length-1]);

      for (let i = 0; i < this.plots.value.length; i++) {

        const plot = this.plots.value[i];        
        if(plot && plot.id != null){
          // Si el plot actual no está en la lista anterior, agregarlo
          if(!this.listaAnteriorGraficasGraficadas.includes(plot.id)){


            this.grafica.getMediciones(plot.id).subscribe(response =>{
              console.log(response);
              const GraficaCoordenada = response as GraficaCoordenadaModel;
              this.plot.anadirDataSet(this.chart,GraficaCoordenada.nombre,GraficaCoordenada.id.toString(),GraficaCoordenada.coordenadas)
            }, error => {
              console.error(error);
            })                                          
          }
          // Agregar el ID del plot actual a la lista de gráficos graficados
          this.listaGraficasGraficadas.push(plot.id)
        }                        
      }
      
      // Filtrar los gráficos que deben ser eliminados
      const listaGraficasBorrar = this.listaAnteriorGraficasGraficadas.filter(valor => !this.listaGraficasGraficadas.includes(valor));
      
      // Borrar los gráficos que ya no están seleccionados
      for( let i = 0;i<listaGraficasBorrar.length; i++){
          this.plot.borrarDataSet(this.chart,listaGraficasBorrar[i].toString())
      }
      // Actualizar las listas de gráficos
      this.listaAnteriorGraficasGraficadas = [...this.listaGraficasGraficadas];
      this.listaGraficasGraficadas = [];
    }
        
  }

  //Boton de Save
  guardarGrupo(){
    console.log('Nombre del Grupo:', this.nombreGrupo);
    console.log('ID de la Grafica Seleccionada:', this.selectedGrafica);
    console.log('Lista id de graficas graficadas: ', this.listaAnteriorGraficasGraficadas)
    const grupo : GrupoModel = {
      ids: this.listaAnteriorGraficasGraficadas,
      nombreGrupo: this.nombreGrupo,
      idReferencia: parseInt(this.selectedGrafica)
    };

    console.log(grupo);
    this.grupo.saveGrupo(grupo).subscribe(response =>{
      console.log(response)
    }, error =>{
      console.log(error)
    })


  }

  //Boton de Delete
  confirmarBorrar(){
    console.log(this.graficaBorrar);
    if(this.graficaBorrar>-1){ 
      const indice = this.graficas.findIndex(grafica => grafica.id === this.graficaBorrar);
      this.graficas.splice(indice,1);  
      this.grafica.deleteGrafica(this.graficaBorrar).subscribe(response => {
        console.log("Grafica eliminada satisfactoriamente: ",response);
      }, error => {
        console.error("Error al eliminar la grafica: ", error)
      })
    }


  }

  abrirModal(){
    this.esModal = true;    
  }

  cerrarModal(){
    this.esModal= false;

  }

  onSelectionModalChange(event: any){
    this.graficaBorrar = this.plotsModal.value.id;
    console.log(this.graficaBorrar);

  }

  obtenerFecha(): string {
    const now = new Date();
    const year = now.getFullYear();
    const month = (now.getMonth() + 1).toString().padStart(2, '0');
    const day = now.getDate().toString().padStart(2, '0');
    const hours = now.getHours().toString().padStart(2, '0');
    const minutes = now.getMinutes().toString().padStart(2, '0');
    const seconds = now.getSeconds().toString().padStart(2, '0');

    const formattedDateTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    return formattedDateTime;
    
  }
}
