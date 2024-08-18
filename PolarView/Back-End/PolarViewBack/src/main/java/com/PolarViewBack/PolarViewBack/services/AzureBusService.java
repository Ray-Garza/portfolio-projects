package com.PolarViewBack.PolarViewBack.services;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusErrorContext;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;

import static com.PolarViewBack.PolarViewBack.config.*;






@Service
public class AzureBusService {

    private static final String connStringTopic = CONNSTRINGTOPIC;
    private static final String topicName = "esp32java";
    private static final String subscriptionName = "PolarViewSuscripcion";
    
    private final AtomicBoolean receiving = new AtomicBoolean(false);    

    private static String ultimoMensaje = null;

        
    public void stopReceivingMessages(SseEmitter emitter) {
        receiving.set(false); // Indicar que se ha detenido la recepción de mensajes
        emitter.complete(); // Completar el emitter para indicar que no se enviarán más eventos
    }

    public String configuracion() throws InterruptedException{
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ServiceBusProcessorClient processorClient = new ServiceBusClientBuilder()
        .connectionString(connStringTopic)
        .processor()
        .topicName(topicName)
        .subscriptionName(subscriptionName)
        .processMessage(AzureBusService::processMessage)
        .processError(context -> processError(context, countDownLatch))
        .buildProcessorClient();
        System.out.println("iniciando el processor");
        processorClient.start();
        TimeUnit.SECONDS.sleep(3    );
        System.out.println("Deteniendo el processor");
        processorClient.close();
        return ultimoMensaje;
    }

    public ServiceBusProcessorClient crearClienteProceso(CopyOnWriteArrayList<SseEmitter> emitters){
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ServiceBusProcessorClient clienteProceso = new ServiceBusClientBuilder()
        .connectionString(connStringTopic)
        .processor()
        .topicName(topicName)
        .subscriptionName(subscriptionName)
        .processMessage(context->procesarMensajeSse(context, emitters))
        .processError(context -> processError(context, countDownLatch))
        .buildProcessorClient();

        return clienteProceso;        
    }

    private static void procesarMensajeSse(ServiceBusReceivedMessageContext context, CopyOnWriteArrayList<SseEmitter> emitters){
        try{
            ServiceBusReceivedMessage message = context.getMessage();        
            for(SseEmitter emitter: emitters){
                emitter.send(message.getBody().toString());
                System.out.println(message.getBody().toString());
            }
        }catch(Exception e){

        }

    }
    private static void processMessage(ServiceBusReceivedMessageContext context){
        ServiceBusReceivedMessage message = context.getMessage();
        System.out.printf("Procesando mensaje. Sesión: %s, Secuencia #: %s, Contenido: %s%n", message.getMessageId(), message.getSequenceNumber(), message.getBody());
        ultimoMensaje = message.getBody().toString();
        
    }

    private static void processError(ServiceBusErrorContext context, CountDownLatch countDownLatch){
        
    }

}
