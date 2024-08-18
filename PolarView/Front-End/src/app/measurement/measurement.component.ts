import { Component} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { delay } from 'rxjs';

import { ChartConfiguration, ChartEvent, ChartType, Chart } from 'chart.js';
import { GraficaService } from '../services/grafica.service';
import { GraficaModel } from '../models/grafica.model';
import { NgForm } from '@angular/forms';
import { PlotService } from '../services/plot.service';
import { environment } from 'src/environment/environment';


interface coordenada {
  codigo: string;
  contenido: {
    x: number[];
    y: number[];
  };
}

@Component({
  selector: 'app-measurement',
  templateUrl: './measurement.component.html',
  styleUrls: ['./measurement.component.css']
})
export class MeasurementComponent {  
  private eventSource: EventSource | null = null;
  ultimoMensaje: string = "uwu";
  ultimoError: string = "owo";
  xData: number[] = [];
  yData: number[] = [];
  chart: any;
  errorForm: boolean = false;
  
  nombreGrafica: string = this.obtenerFecha();
  dominioInicioGrafica: number = 0;
  dominioFinalGrafica: number = 360;
  
  //url: string = "http://localhost:8080"
  url: String = environment.apiUrl;

  constructor(private http: HttpClient, private graficaService: GraficaService, private plot: PlotService ) {}

  ngAfterViewInit(): void {
    this.chart = this.plot.crearPlot('chart-canvas',this.yData,this.xData)    
  }  


  //Boton de Start
  iniciarRotacionYMedir(): void {
    this.ultimoMensaje = "iniciando...";
    
    this.obtenerEventoSSE()    
    delay(1000);

    this.http.get(this.url.concat('/iot/rotarYmedir'), {responseType:'text'}).subscribe(
      response => {
        console.log('Respuesta de rotarYmedir:', response);
      },
      error => {
        console.error('Error al llamar a rotarYmedir:', error);
        this.ultimoError = 'Error al llamar a rotarYmedir';
      }
    );
  }

  //Boton de Stop
  detenerRotacion(): void {
    this.ultimoMensaje= "Deteniendo..."
    this.http.post(this.url.concat('/iot/detenerRotacion'), {}).subscribe(
      () => {
        console.log('Comunicación detenida correctamente.');
        this.ultimoMensaje = "Rotacion detenida";
      },
      (error) => {
        console.error('Error al detener la comunicación:', error);
        this.ultimoError = "Error en detener";
      }
    );

    
  }

  //Boton de Reset
  resetGrafica(): void {    
    this.yData = []
    this.xData = []
    //TODO hay que destruir primero el canvas
    this.chart.destroy()
    this.chart = this.plot.crearPlot('chart-canvas',this.yData,this.xData)  
  }


  //Boton de Save
  guardarGrafica(form: NgForm ): void {
    
    if(form.valid){
      const fechaActual = new Date().toISOString();  
      let graficaId = 0;

      this.errorForm=false;
      const grafica: GraficaModel = {
        nombre: this.nombreGrafica,
        fecha: fechaActual
      };
          
      this.graficaService.saveGrafica(grafica)
      .subscribe({
        next: (response) => {
          console.log('Gráfica guardada:', response);
          if(response.id){
            graficaId = response.id
          }
          
          this.graficaService.saveMediciones(this.xData,this.yData,graficaId)
          .subscribe({
            next: (response) => {
              console.log(response);
            },
            error: (err) => {
              console.error("Error al guardar los datos: ", err);
            }
          })

        },
        error: (err) => {
          console.error('Error al guardar la gráfica:', err);        
        }
      })



    }else{
      this.errorForm = true;
    }
  }

  modoLibre(): void{

    this.obtenerEventoSSE();
    delay(1000);

    this.http.get(this.url.concat('/iot/modoLibre'), {responseType:'text'}).subscribe(
      response => {
        console.log('Respuesta de ModoLibre:', response);
      },
      error => {
        console.error('Error al llamar a ModoLibre:', error);        
      }
    );

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

  obtenerEventoSSE(): void {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = null;
      this.resetGrafica();
    }else{    

      console.log("Iniciando Evento");
      this.eventSource = new EventSource(this.url.concat('/iot/iniciarEventoSse'));

      this.eventSource.onmessage = (event) => {
        console.log('Mensaje recibido:', event.data);
        this.ultimoMensaje = String(event.data);
        const mensaje: coordenada = JSON.parse(event.data)
        console.log(mensaje.contenido);
        if(mensaje.codigo == "rotar"){
          if(mensaje.contenido.x[4-1] <363){                    
            this.xData.push(...mensaje.contenido.x);
            this.yData.push(...mensaje.contenido.y);
            
            (this.chart.data.datasets[0].data as number[]).push(...mensaje.contenido.y);
            this.chart.data.labels?.push(...mensaje.contenido.x);
            this.chart.update();
            
            /*console.log("Datos:");
            console.log(this.lineChartData.datasets[0].data)
            console.log("Indices:")
            console.log(this.lineChartData.labels)*/              
          }else{
            this.resetGrafica()
          }
        }
      


      };

      this.eventSource.onerror = (error) => {
        console.error('Error de SSE:', error);
        this.ultimoError = String(error);
      };
    }
    
  }

  //////////////////////////////////////////////////////////////////////////    
    

  
  public lineChartData: ChartConfiguration['data'] = {
    datasets: [
      {
        data: [],
        label: 'Data',
        backgroundColor: 'rgba(148,159,177,0.2)',
        borderColor: 'rgba(148,159,177,1)',
        pointBackgroundColor: 'rgba(148,159,177,1)',
        pointBorderColor: '#fff',
        pointHoverBackgroundColor: '#fff',
        pointHoverBorderColor: 'rgba(148,159,177,0.8)',
        fill: 'origin',
      },      
    ],
    labels: [],
    
  };


  public lineChartOptions: ChartConfiguration['options'] = {    
    responsive: true,
    maintainAspectRatio: false,
    elements: {
      line: {
        tension: 0.5,
      },
    },
    scales: {      
      x: {
        type: 'linear',
        min: 0,
        max: 360,
        ticks: {
          stepSize: 45 // Puedes ajustar el tamaño del paso según tus necesidades
        }
      },
      y: {
        type: 'linear',
        min: 0,
        max: 4096,
        ticks: {
          stepSize: 512 // Puedes ajustar el tamaño del paso según tus necesidades
        }
      }
    },

    plugins: {
      legend: { display: true }
    },    
  };

  public lineChartType: ChartType = 'line';
}
