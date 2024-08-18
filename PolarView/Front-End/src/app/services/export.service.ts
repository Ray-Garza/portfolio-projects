import { Injectable } from '@angular/core';
import { GraficaCoordenadaModel } from '../models/graficaCoordenada.model';

@Injectable({
  providedIn: 'root'
})
export class ExportService {





  constructor() { }

  exportarCSV(datos: any[], nombre: string) {
    const csvData = this.convertirArrayACSV(datos);
    const cadena = 'PolarViewData ' + nombre + '.csv'
    this.descargarArchivo(csvData, cadena, 'text/csv');
  }



  convertirArrayACSV(data: GraficaCoordenadaModel[]): string {
    const header = ['id', 'nombre', 'x', 'y'];
    const rows: any = [];

    data.forEach(item => {
        item.coordenadas.forEach(coord => {
            rows.push([item.id, item.nombre, coord.x, coord.y]);
        });
    });

    const csvContent = [header.join(',')].concat(rows.map((row: any[]) => row.join(','))).join('\n');
    return csvContent;
  }

  descargarArchivo(data: string, nombreArchivo: string, tipo: string) {
    const blob = new Blob([data], { type: tipo });
    const a = document.createElement('a');
    a.href = URL.createObjectURL(blob);
    a.download = nombreArchivo;
    a.click();
    URL.revokeObjectURL(a.href);
  }


}
