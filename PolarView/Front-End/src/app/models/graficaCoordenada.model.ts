import { CoordenadaModel } from "./coordenadaModel";
export interface GraficaCoordenadaModel {
    id: number;
    nombre: string;
    coordenadas : CoordenadaModel[]
}