import { Injectable } from '@angular/core';
import { levenbergMarquardt } from 'ml-levenberg-marquardt';
import { CoordenadaModel } from '../models/coordenadaModel';




const initialValues = [1200,0, 1000];

const options = {
  damping: 1.5,
  initialValues,
  gradientDifference: 10e-2,
  maxIterations: 100,
  errorTolerance: 10e-3
};


@Injectable({
  providedIn: 'root'
})
export class SinFitService {

  phase40: number[] = [4.281,1.878,1.519,1.535];
  phase50: number[] = [6.258,6.988,8.53,8.978];
  phase60: number[] = [9.7, 9.708, 10.773, 10.668];
  phase70: number[] = [18.126, 16.171,22.353, 17.062];
  
  labeledData: {value:number,label:string}[] = [
    ...this.phase40.map(value => ({ value, label: '40%' })),
    ...this.phase50.map(value => ({ value, label: '50%' })),
    ...this.phase60.map(value => ({ value, label: '60%' })),
    ...this.phase70.map(value => ({ value, label: '70%' }))
  ];

  constructor() { }

  

  // Modelo sinusoidal
  sinusoidal([A, C, D]: number[], x: number):  (x: number) => number {
    return (x:number) => A * Math.sin(2* (x* Math.PI / 180) + C) + D;
  }


  calcularSinusoidal(coordenadas: CoordenadaModel[]): {A: number, C:number, D:number}{

    const data = {
      x: coordenadas.map(coord => coord.x),
      y:coordenadas.map(coord => coord.y)
    }

    const result = levenbergMarquardt(data,this.sinusoidal, options);
    const [A,C,D] = result.parameterValues;
    result.parameterError
    
    
    return {A:A, C:C, D:D}

  }


  calcularKNN(phase: number, k:number = 3){
    const distances = this.labeledData.map(data => ({
      ...data,
      distance: Math.abs(phase -data.value)
    }));
  
    distances.sort((a, b) => a.distance - b.distance);
  
    console.log("Distances: ");
    console.log(distances);
    const nearestNeighbors = distances.slice(0, k);

    // Contar la frecuencia de cada etiqueta
    const labelCounts: { [key: string]: number } = {};

    nearestNeighbors.forEach(neighbor => {
      if (labelCounts[neighbor.label]) {
        labelCounts[neighbor.label]++;
      } else {
        labelCounts[neighbor.label] = 1;
      }
    });

    // Encontrar la etiqueta con la mayor frecuencia
    let mostFrequentLabel = '';
    let maxCount = 0;

    for (const label in labelCounts) {
      if (labelCounts[label] > maxCount) {
        mostFrequentLabel = label;
        maxCount = labelCounts[label];
      }
    }

    return mostFrequentLabel;
  
  }

  
}
