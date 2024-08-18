import { Component } from '@angular/core';
import {FormControl} from '@angular/forms';
import { PlotService } from '../services/plot.service';
import { GrupoModel } from '../models/grupo.model';
import { GrupoService } from '../services/grupo.service';
import { GraficaService } from '../services/grafica.service';
import { GraficaCoordenadaModel } from '../models/graficaCoordenada.model';
import { SinFitService } from '../services/sin-fit.service';
import { ExportService } from '../services/export.service';



export interface SinAnalysis {
  name: string;
  fun: string;
  alpha: number;
  classification: string;
}

export interface SinFunData {
  name: string
  id: string
  params: {
    A: number,
    C: number,
    D: number
  }
}

const ELEMENT_DATA: SinAnalysis[] = [
  {name: "Water", fun: "y=sen(x)", alpha: 0, classification: "" },
  {name: "40%", fun: "y=sen(x) + 2", alpha: 2, classification: "38%" },
  {name: "50%", fun: "y=sen(x) + 8.31", alpha: 8.31, classification: "51%" },
  {name: "60%", fun: "y=sen(x) + 12.46", alpha: 12.46, classification: "60%" },
  {name: "70%", fun: "y=sen(x) + 19.3958", alpha: 19.3958, classification: "69%" },

]



@Component({
  selector: 'app-visualization',
  templateUrl: './visualization.component.html',
  styleUrls: ['./visualization.component.css']
})
export class VisualizationComponent {
  xData: number[] = []
  yData: number[] = []
  grupos: GrupoModel[] = []
  grupoForm = new FormControl() 

  graficas: GraficaCoordenadaModel[] = [];
  chart: any;
  isFit: boolean = false;
  isAnalyse: boolean = false;
  sinData: SinFunData[] = [];
  idGrupo: number = 0;


  displayedColumns: string[] = ['name', 'fun', 'alpha', 'classification'];
  dataSource = ELEMENT_DATA;



  esModal = false;
  

  constructor(private plot: PlotService, private grupo: GrupoService, private grafica: GraficaService, private sinFit: SinFitService, private exportar: ExportService){}

  ngAfterViewInit(): void {
    this.chart = this.plot.crearPlotVacio('chart-view')    

    this.grupo.getGrupos().subscribe(response =>{
      console.log(response);
      this.grupos = response

      
    }, error => {
      console.error(error)
    })
  }  

  onSelectionChange(event: any): void{
    
    this.graficarPlots()                    
  }

    
  graficarPlots(): void {
      this.graficas = [];
      this.chart.destroy()
      this.chart = this.plot.crearPlotVacio('chart-view')      
      console.log(this.grupoForm.value.ids)
      console.log(this.grupoForm.value.idReferencia);
      if(this.isFit){
        this.idGrupo = this.grupoForm.value.id
        this.sinData = []
      }
      for (let i = 0; i < this.grupoForm.value.ids.length; i++) {
        const id = this.grupoForm.value.ids[i];

        this.grafica.getMediciones(id).subscribe(response =>{          
          console.log(response);          
          const GraficaCoordenada = response as GraficaCoordenadaModel;
          this.graficas.push(GraficaCoordenada);
          if(!this.isFit){          
            this.plot.anadirDataSet(this.chart,GraficaCoordenada.nombre,GraficaCoordenada.id.toString(),GraficaCoordenada.coordenadas)
          }else{          
            //Obtener parámetros de la sinusoidal
            console.log("Parametros:")
            const parametros = this.sinFit.calcularSinusoidal(GraficaCoordenada.coordenadas)
            this.sinData.push({name: GraficaCoordenada.nombre,id: GraficaCoordenada.id.toString(),params:parametros})            
            this.plot.graficarSeno(this.chart,GraficaCoordenada.nombre,GraficaCoordenada.id.toString(),parametros.A,parametros.C,parametros.D)
            console.log(this.sinData);
          }

        }, error => {
          console.error(error);
        })
        
      }
      
    }
    
    
    

    fit() {
      this.isFit = !this.isFit;
      this.graficarPlots();
    }

    analyse() {
      let sins: SinFunData[]  = [];
      let sinsTable: SinAnalysis[] = [];
      let A=0;
      let C=0;
      let D=0;

      this.chart.destroy()
      this.chart = this.plot.crearPlotVacio('chart-view')
      //this.isAnalyse = !this.isAnalyse;
      if(this.idGrupo != 0){
        //Se obtienen los parámetros de la referencia
        for (let i = 0; i < this.sinData.length; i++) {
          const sin = this.sinData[i];
          if(sin.id == this.grupoForm.value.idReferencia){
            A = sin.params.A;
            C = sin.params.C;
            break;
          }          
        }
        //Calcular D
        if(Math.abs(2*A) < 4096){
          D = Math.abs(A);
        }else{
          D=4096-A;
        }

        //Normalizar
        for (let i = 0; i < this.sinData.length; i++) {
          const sin = this.sinData[i];          
          const sinAux: SinFunData = {name:sin.name,
                                      id:sin.id,
                                      params:{
                                        A:A,
                                        C:sin.params.C-C,
                                        D: D
                                      }}
          sins.push(sinAux)
          this.plot.graficarSeno(this.chart,sinAux.name,sinAux.id,sinAux.params.A,sinAux.params.C,sinAux.params.D);
          let signC = "+";          
          if(sinAux.params.C <0){
             signC = ""
          }
          
          const fun:string = sinAux.params.A.toFixed(3)+"sin( 2x"+signC+sinAux.params.C.toFixed(3) +") +"+sinAux.params.D.toFixed(3)
          let classification : string = this.sinFit.calcularKNN((sinAux.params.C*(180/Math.PI))*-1)
          if(sinAux.id == this.grupoForm.value.idReferencia){
            classification= "-"
          }
          
          const sinRow: SinAnalysis = {
            name:sin.name,fun:fun,alpha: Math.abs(parseFloat((sinAux.params.C*(180/Math.PI)).toFixed(3))), classification: classification
          }
          sinsTable.push(sinRow);

          
        }

        this.sinData = sins;
        console.log("uwu")
        console.log(this.sinData)
        this.dataSource = sinsTable;


      }
      

    }





  //Boton de Delete
  
  confirmarBorrar(){
    /*
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
  */
    if(this.grupoForm.value.id){
      const indice = this.grupos.findIndex(grupo => grupo.id === this.grupoForm.value.id);
      this.grupos.splice(indice,1);
      console.log(this.grupoForm.value.id);
      this.grupo.deleteGrupo(this.grupoForm.value.id).subscribe(response =>{
        console.log(response);
      }, error => {
        console.error("Error al borrar grupo: ", error);
      })
    }

  }
  
  abrirModal(){
    this.esModal = true;    
  }

  cerrarModal(){
    this.esModal= false;

  }

  exportarDatos(){
    this.exportar.exportarCSV(this.graficas, this.grupoForm.value.nombreGrupo)
  }
  
}
