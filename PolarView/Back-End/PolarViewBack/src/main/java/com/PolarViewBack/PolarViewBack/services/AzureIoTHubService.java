package com.PolarViewBack.PolarViewBack.services;


import org.springframework.stereotype.Service;

import com.microsoft.azure.sdk.iot.service.exceptions.IotHubException;
import com.microsoft.azure.sdk.iot.service.exceptions.IotHubInternalServerErrorException;
import com.microsoft.azure.sdk.iot.service.exceptions.IotHubUnauthorizedException;
import com.microsoft.azure.sdk.iot.service.messaging.ErrorContext;
import com.microsoft.azure.sdk.iot.service.messaging.IotHubServiceClientProtocol;
import com.microsoft.azure.sdk.iot.service.messaging.Message;
import com.microsoft.azure.sdk.iot.service.messaging.MessagingClient;
import com.microsoft.azure.sdk.iot.service.messaging.MessagingClientOptions;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusReceiverClient;

import static com.PolarViewBack.PolarViewBack.config.CONNSTRINGSERVICE;
import static com.PolarViewBack.PolarViewBack.config.CONNSTRINGTOPIC;

import java.io.IOException;
import java.net.URISyntaxException;

//import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;



@Service
public class AzureIoTHubService {


    // Define el ID del dispositivo al que deseas enviar el mensaje
    private static final String deviceId = "Eugenio32";
    
    
    private static final String connStringService= CONNSTRINGSERVICE;
    
    
    private static final String connStringTopic = CONNSTRINGTOPIC;
    private static final String topicName = "esp32java";
    private static final String subscriptionName = "PolarViewSuscripcion";

    public String telemetriaIoT() throws IOException, IotHubException{
        ServiceBusReceiverClient receiverClient = new ServiceBusClientBuilder()
        .connectionString(connStringTopic)
        .receiver()
        .topicName(topicName)
        .subscriptionName(subscriptionName).
        buildClient();

        receiverClient.receiveMessages(3).forEach(message -> {
            System.out.println("Mensaje recibido: "+message.getBody().toString());                        
        });        
        
        return "uwu";
        
    }

    public void mensajeIoT(String cadena) throws IOException, URISyntaxException, InterruptedException{
        try {
            final Object connectionEventLock = new Object();
             Consumer<ErrorContext> errorProcessor = errorContext ->
            {
                if (errorContext.getIotHubException() != null)
                {
                    System.out.println("Encountered an IoT hub level error while sending events " + errorContext.getIotHubException().getMessage());
                }
                else
                {
                    System.out.println("Encountered a network error while sending events " + errorContext.getNetworkException().getMessage());
                }

                synchronized (connectionEventLock)
                {
                    connectionEventLock.notify();
                }
            };

            MessagingClientOptions messagingClientOptions =
            MessagingClientOptions.builder()
                .errorProcessor(errorProcessor)
                .build();

            MessagingClient messagingClient = new MessagingClient(connStringService, IotHubServiceClientProtocol.AMQPS, messagingClientOptions);

            openMessagingClientWithRetry(messagingClient);

            Message messageToSend = new Message(String.valueOf(cadena));
            messagingClient.send(deviceId, messageToSend, 1000);

            messagingClient.close();
            
             
        } catch (Exception e) {
            System.err.println("Error al enviar el mensaje al IoT Hub: " + e.getMessage());
            e.printStackTrace();            
            
        }
    }
    


    // return true if the client was opened successfully, false if the client encountered a terminal exception and the sample should stop
    private static boolean openMessagingClientWithRetry(MessagingClient messagingClient) throws InterruptedException
    {
        int i = 0;
        while (i < 10)
        {
            try
            {
                System.out.println("Attempting to open the messaging client");
                messagingClient.open(1000);
                System.out.println("Successfully opened the messaging client");
                return true;
            }
            catch (IotHubUnauthorizedException e)
            {
                System.out.println("Failed to open messaging client due to invalid or out of date credentials: " + e.getMessage());
                return false;
            }
            catch (IotHubInternalServerErrorException e)
            {
                System.out.println("Failed to open messaging client due to internal server error: " + e.getMessage());
            }
            catch (IotHubException e)
            {
                System.out.println("Failed to open messaging client due to hub level issue: " + e.getMessage());
            }
            catch (IOException e)
            {
                System.out.println("Failed to open messaging client due to network issue: " + e.getMessage());
            }
            catch (TimeoutException e)
            {
                System.out.println("Failed to open messaging client due to service taking too long to respond.");
            }

            System.out.println("Retrying to open messaging client");
            i++;
            //noinspection BusyWait
            Thread.sleep(1000);
        }
        return false;
    }

}
