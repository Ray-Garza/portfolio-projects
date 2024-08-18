package com.PolarViewBack.PolarViewBack.controllers;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.CopyOnWriteArrayList;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.PolarViewBack.PolarViewBack.services.AzureIoTHubService;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.PolarViewBack.PolarViewBack.services.AzureBusService;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.bind.annotation.RequestBody;

import static com.PolarViewBack.PolarViewBack.config.*;






@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/iot")
public class IoTController {

    private static final String contra = CONTRA; 
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    private final AzureIoTHubService azureIoTHubService;   
    private final AzureBusService azureBusService; 

    private boolean esModoLibre = false;
    
    private ServiceBusProcessorClient clienteProceso;
    
    public IoTController(AzureIoTHubService azureIoTHubService, AzureBusService azureBusService) {
        this.azureIoTHubService = azureIoTHubService;
        this.azureBusService = azureBusService;

    }
    /*Controladores para manejar el evento SSE */
    
    @GetMapping("/iniciarEventoSse")
    public SseEmitter handleSSE() {
        // Crear un nuevo SseEmitter
        SseEmitter emitter = new SseEmitter((long) (4*60*1000));
        
        // Añadir el emisor a la lista de emisores
        emitters.add(emitter);

        // Configurar acciones para cuando el emisor se complete o expire el tiempo de espera
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        // Retornar el emisor al cliente
        return emitter;
    }

    
    @GetMapping("/rotarYmedir")
    public ResponseEntity<String> rotarYmedir() throws IOException, URISyntaxException, InterruptedException {
        System.out.println("Rotar y medir");  
        this.azureIoTHubService.mensajeIoT("rotar");    
        System.out.println("Creando cliente Proceso");        
        this.clienteProceso = azureBusService.crearClienteProceso(emitters);
        
        System.out.println("Recibiendo mensajes");
        this.clienteProceso.start();        
        /*
        azureBusService.startReceivingMessages(this.clienteAsincrono, emitters);
        
        for(SseEmitter emitter: emitters){
            try{
                
                System.out.println("Enviando codigo");
                emitter.send("{ \"codigo\": \"rotar\", \"contenido\": {\"x\": 0, \"y\": 0}}");
            } catch (IOException e) {
                System.out.println("Error al enviar al emisor");
                emitter.complete();
                emitters.remove(emitter);
            }
        }*/

        return ResponseEntity.ok("ok");
    }
    

    
    @PostMapping("/detenerRotacion")     
    public void detener() throws IOException, URISyntaxException, InterruptedException {
        this.azureIoTHubService.mensajeIoT("detenerRotacion");
        this.clienteProceso.close();
        for(SseEmitter emitter: emitters){
            emitter.complete();
            emitters.remove(emitter);
        }
    }
    
    
    @GetMapping("/modoLibre")
    public String modoLibre() {
        try{
            if(!esModoLibre){
                System.out.println("Iniciando Modo Libre");
                this.azureIoTHubService.mensajeIoT("modoLibre");
                
                System.out.println("Creando cliente Proceso");        
                this.clienteProceso = azureBusService.crearClienteProceso(emitters);                    
                System.out.println("Recibiendo mensajes");
                this.clienteProceso.start();   
            }else{
                System.out.println("Deteniendo Modo Libre");
                this.azureIoTHubService.mensajeIoT("modoLibre");
            }


            return "Modo libre activado";
        }catch(Exception e){
            return e.getMessage();
        }        
    }
    

    @GetMapping("/rotar")
    public String rotar() {
        try{
            this.azureIoTHubService.mensajeIoT("rotar");            
            return "Rotar...";
        }catch(Exception e){
            return e.getMessage();
        }    
    }        

    @GetMapping("/girar")
    public String girar() {
        try{
            this.azureIoTHubService.mensajeIoT("girar");
            return "Girar...";
        }catch(Exception e){
            return e.getMessage();
        }    
    }

    @GetMapping("/subscribe")
    public String subscribeToIoTHub() {
        try{
            
            return this.azureIoTHubService.telemetriaIoT();
        }catch(Exception e){
            return e.getMessage();
        }    
    }

    @GetMapping("/iniciarMotor")
    public String iniciarMotor(){
        try{
            this.azureIoTHubService.mensajeIoT("iniciarMotor");
            return "Iniciando...";
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    @GetMapping("/configuracion")
    public ResponseEntity<String> configuracion(@RequestHeader("Authorization") String apiKey) throws IOException, URISyntaxException, InterruptedException {        
        if (apiKey != null && apiKey.equals("Bearer " + contra)) {            
            
            this.azureIoTHubService.mensajeIoT("configuracion");
            String responseData = this.azureBusService.configuracion();
            return ResponseEntity.ok(responseData);
            
        } else {
            // La API Key es inválida, devolver un error 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("API Key inválida");
        }

        
    }

    @PostMapping("/actualizarConfiguracion")
    public ResponseEntity<String> postMethodName(@RequestBody String json) {        
        try{
            this.azureIoTHubService.mensajeIoT(json);
            System.out.println("Mensaje enviado");
            return ResponseEntity.ok("Actualizando");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
                
    }
    
    
}
