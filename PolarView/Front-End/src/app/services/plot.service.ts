import { Injectable } from '@angular/core';
import { Chart, ChartDataset, Plugin } from 'chart.js';
import { CoordenadaModel } from '../models/coordenadaModel';

interface ExtendedChartDataset extends ChartDataset<'line'> {
  id: string;
}

/*Este servicio se encarga de manejar la interfaz de las graficas, hace uso de chart.js para 
manejar todos los detalles visuales. */


@Injectable({
  providedIn: 'root'
})
export class PlotService {
  contadorGraficas = 0;
  

  constructor() { }

  crearPlotVacio(id: string): Chart {
    let chart = new Chart(id,{type: 'line',
      data: {datasets: [],
      labels: ["uwu"]
    },

      options:{responsive: true,
        maintainAspectRatio: false,
        elements: {
          line: {
            tension: 0.5,
          },
          point: {
            radius: 1, // Ajustar el tamaño de los puntos
            hoverRadius: 1
          }
        },
        scales: {      
          x: {
            type: 'linear',
            min: 0,
            max: 360,
            ticks: {
              stepSize: 45
            }
          },
          y: {
            type: 'linear',
            min: 0,
            max: 4096,
            ticks: {
              stepSize: 512 
            }
          }
        },
    
        plugins: {          
          legend: { display: true }
          
        },}                      
        
      },
      )
      
      return chart;
  }

  crearPlot(id: string, yData: number[], xData: number[] ): Chart{
    let chart = new Chart(id,{type: 'line',
      data: {datasets: [
        {
          data: xData.map((x, index) => ({ x: x, y: yData[index] })),
          label: 'Data',
          backgroundColor: 'rgba(148,159,177,0.2)',
          borderColor: 'rgba(255,0,0,1)',
          pointBackgroundColor: 'rgba(255,0,0,1)',
          pointBorderColor: '#fff',
          pointHoverBackgroundColor: '#fff',
          pointHoverBorderColor: 'rgba(148,159,177,0.8)',
          fill: 'false',
          
        },      
      ],
      labels: xData.map(x => x.toString()),
    },

      options:{responsive: true,
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
              stepSize: 45 
            },
            title: {
              text: 'Degrees',
              display: true
            }
            
          },
          y: {
            type: 'linear',
            min: 0,
            max: 4096,
            ticks: {
              stepSize: 512
            },
            
            

          }
        },
    
        plugins: {
          legend: { display: true }
        },}
      })
      return chart;
  }

  anadirDataSet(chart: Chart,nombre: string,id: string, coordenadas: CoordenadaModel[]){
      this.contadorGraficas++;
      let rojo = Math.floor(Math.random()*255);
      let azul = Math.floor(Math.random()*255)
      let verde = Math.floor(Math.random()*255)
      const newDataset: ExtendedChartDataset = {
        id: id,
        label: nombre,
        backgroundColor: 'rgba(148,159,177,0.2)',
        borderColor: 'rgba('+rojo+','+verde+','+azul+',0.77)',
        pointBackgroundColor: 'rgba('+rojo+','+verde+','+azul+',0.77)',
        pointBorderColor: '#000',
        pointHoverBackgroundColor: '#fff',
        pointHoverBorderColor: 'rgba(148,159,177,0.8)',
        fill: 'false',     
        pointBorderWidth:0,   
        //borderColor: dsColor,
        data: coordenadas,
      };      
      chart.data.datasets.push(newDataset);
      
      chart.update();

  }

  borrarDataSet(chart: Chart, id: string): void {
    this.contadorGraficas--;
    const datasetIndex = chart.data.datasets.findIndex((dataset) => (dataset as ExtendedChartDataset).id === id);
    // Si se encontró el dataset
    if (datasetIndex !== -1) {
      // Eliminar el dataset del arreglo de datasets
      chart.data.datasets.splice(datasetIndex, 1);      
      chart.update(); // Actualizar el gráfico para reflejar los cambios
    }
  }

  reestablecerPlot(chart: Chart, id:string): void{

  }


  //función para graficar el seno con todos los parámetros
  graficarSeno(chart: Chart, nombre: string, id: string, amplitude: number = 1, phase: number = 0, offset: number = 0) {
    const xData = Array.from({ length: 361 }, (_, i) => i);
    const yData = xData.map(x => amplitude * Math.sin(2 * (x * Math.PI / 180) + phase) + offset);
    const coordenadas: CoordenadaModel[] = xData.map((x, index) => ({ x: x, y: yData[index] }));
    
    this.anadirDataSet(chart, nombre, id, coordenadas);
  }

  graficarSenoAnalizado(chart: Chart, nombre: string, id: string, amplitude: number = 1, phase: number = 0, offset: number = 0){
    
  }


}
