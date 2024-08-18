import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { interval } from 'rxjs';
import { ChangeDetectorRef } from '@angular/core';
import { environment } from 'src/environment/environment';



// Define una interfaz para la estructura de datos esperada
interface Configuracion {
  codigo: string;
  contenido: {
    "Device id": string;
    SensorValue: number;
    RPM: number;
    laps: number;
    DPT: number;
  };
}

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})


export class SettingsComponent {
  sensorValue: String = '1';
  degree: String = '0';
  deviceId: String = '';
  angularSpeed: String = '';
  degreesToRotate: String = '';
  turnsToRotate: String = '';
  rotateValue: String = '';
  pointsPerDegree: String = '1';
  valor: String = "1";
  contra = "uwuuwuuwu";
  uploadMensaje= "";
  ultimoMensaje ="";

  url: String = environment.apiUrl;
  //url: String = "http://localhost:8080"
  
  

  movimientoSubscription: Subscription | undefined;

  constructor(private http: HttpClient, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.setup();    
  }

  

  

  conexion(): void {
    //this.conexionAzure.connect()
  }

  //Función para encender o apagar el sensor
  turn(): void {
    this.movimientoSubscription = interval(1000).subscribe(() => {
      this.http.get(this.url.concat('/iot/girar')).subscribe(data => {
        console.log(data); // Manejar la respuesta del servidor aquí
      });
    });
  }

  unTurn(): void {
    if (this.movimientoSubscription) {
      this.movimientoSubscription.unsubscribe();
    }
  }

  rotate(): void {
    this.http.get(this.url.concat('/iot/rotar')).subscribe(data =>{
      console.log("rotar");
      console.log(data);
    });
    
  }

  upload(): void {        
    const data = {
      codigo: 'actualizar',
      contenido: {
        angularSpeed: this.angularSpeed,
        degreesToRotate: this.degreesToRotate,
        turnsToRotate: this.turnsToRotate
      }
    };
    console.log("post");
    console.log(data);
    //this.http.post('https://polarviewback.azurewebsites.net/iot/actualizarConfiguracion',data);
    this.http.post(this.url.concat("/iot/actualizarConfiguracion"),data).subscribe(response => {
      console.log(response);
    }, error => {
      console.error('Error:', error);
    });
    console.log("uwu");
    
  }

  //http://localhost:8080/iot/configuracion

  /*
  setup(): void {
    this.http.get<Configuracion>('https://polarviewback.azurewebsites.net/iot/configuracion').subscribe((data: Configuracion) =>{
      this.ultimoMensaje = String(data);
      this.uploadMensaje = "Data uwu";    
      if(data.codigo == "configuracion"){
        this.uploadMensaje = "Data uwu config";
        this.sensorValue = String(data.contenido.SensorValue);
        this.deviceId = data.contenido['Device id'];
        this.angularSpeed = String(data.contenido.RPM);
        this.degreesToRotate = String(data.contenido.DPT);
        this.turnsToRotate = String(data.contenido.laps);
        this.cdr.detectChanges();
      }      
    })    
  }
  */
  setup(): void {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.contra}`);

    this.http.get<Configuracion>(this.url.concat('/iot/configuracion'), { headers }).subscribe(
      (data) => {
        this.uploadMensaje = String(data);
        console.log('Respuesta del servidor:', data);
        this.ultimoMensaje = String(data);
      this.uploadMensaje = "Data uwu";    
      if(data.codigo == "configuracion"){
        this.uploadMensaje = "Data uwu config";
        this.sensorValue = String(data.contenido.SensorValue);
        this.deviceId = data.contenido['Device id'];
        this.angularSpeed = String(data.contenido.RPM);
        this.degreesToRotate = String(data.contenido.DPT);
        this.turnsToRotate = String(data.contenido.laps);
        console.log(data.contenido.SensorValue);
        console.log(this.sensorValue);
        this.cdr.detectChanges();
      }
      },
      (error) => {
        console.error('Error al obtener datos:', error);
      }
    );
  }
  
  
}
