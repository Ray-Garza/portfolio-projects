/*
This is an arduino-based Azure IoT Hub program for ESP32. It sends and recieves telemetry to a
server and performs actions to move a step-motor and sends signals to plot a sin function.

This project is a sample of the functions of the PolarView project, more info can be found in the repo.
*/


// C99 libraries
#include <cstdlib>
#include <string.h>
#include <time.h>

// Libraries for MQTT client and WiFi connection
#include <WiFi.h>
#include <mqtt_client.h>

// Azure IoT SDK for C includes
#include <az_core.h>
#include <az_iot.h>
#include <azure_ca.h>

// Additional sample headers
#include "AzIoTSasToken.h"
#include "SerialLogger.h"
#include "iot_configs.h"

//Manejador de JSON
#include "ArduinoJson.h"

//Motor a paso
#include <Stepper.h>   
int contador = 0;
const int stepsPerRevolution = 2048; // change this to fit the number of steps per revolution


String auxn = "";

//set steps and the connection with MCU
Stepper stepper(stepsPerRevolution, 18, 19, 21, 22);
int RPM = 2;  // Adjustable range of 28BYJ-48 stepper is 0~17 rpm
int laps = 1;
int DPT = 10;
int MPD = 1;
//TASK
TaskHandle_t motorTask;
TaskHandle_t fotodiodoTask;
bool ejecutarMotorTask = false;
bool ejecutarFotodiodoTask = false;

int contadorFotodiodo=0;
int mediciones[4];
int medicion;
int posiciones[4];

const int pinFotodiodo = 32;

void motorTaskFunc(void * pvParameters){
  for(;;){
    if(ejecutarMotorTask){
      for(int j = 0;j<laps;j++){        
        stepper.setSpeed(RPM);     
              
        stepper.step(12288);        
        digitalWrite(12, HIGH);
        vTaskDelay(2000);
        digitalWrite(12,LOW);   
        ejecutarMotorTask = false;
      }
    }
    vTaskDelay(100);
  }
}

void fotodiodoTaskFunc(void * pvParameters){
  for(;;){
    if(ejecutarFotodiodoTask){  
      for(int i = 0;i<(360*laps*MPD);i++){        
        if(ejecutarFotodiodoTask){
          if(contadorFotodiodo < 4){
            
            //In this demo the ESP32 takes stablished data, but in the real project
            //It reads an analog pin which is connected to the sensor
              //medicion = analogRead(pinFotodiodo);
            medicion = round(2000*sin(i*(PI/180))+2000);

            mediciones[contadorFotodiodo] = medicion;
            posiciones[contadorFotodiodo] = i;
            contadorFotodiodo++;
          }else{


          
          String cadenaInfo = "{ \"codigo\": \"rotar\", \"contenido\": {\"x\": "+cadenaArreglo(posiciones,4)+", \"y\": "+cadenaArreglo(mediciones,4)+"}}";

          Serial.println(cadenaInfo);
          sendTelemetry(cadenaInfo);
          contadorFotodiodo=0;

          //medicion = analogRead(pinFotodiodo);
          medicion = round(2000*sin(i*(PI/180))+2000);
          mediciones[contadorFotodiodo] = medicion;
          posiciones[contadorFotodiodo] = i;
          contadorFotodiodo++;
          }          
          vTaskDelay(1000/(MPD*RPM));    
        }          
      }
      String cadenaInfo = "{ \"codigo\": \"rotar\", \"contenido\": {\"x\": "+cadenaArreglo(posiciones,4)+", \"y\": "+cadenaArreglo(mediciones,4)+"}}";

      Serial.println(cadenaInfo);
      sendTelemetry(cadenaInfo);
      contadorFotodiodo=0;
      Serial.println("Fotodiodo off");
      ejecutarFotodiodoTask=false;
    }
    vTaskDelay(100);
  }
}

// When developing for your own Arduino-based platform,
// please follow the format '(ard;<platform>)'.
#define AZURE_SDK_CLIENT_USER_AGENT "c%2F" AZ_SDK_VERSION_STRING "(ard;esp32)"

// Utility macros and defines
#define sizeofarray(a) (sizeof(a) / sizeof(a[0]))
#define NTP_SERVERS "pool.ntp.org", "time.nist.gov"
#define MQTT_QOS1 1
#define DO_NOT_RETAIN_MSG 0
#define SAS_TOKEN_DURATION_IN_MINUTES 60
#define UNIX_TIME_NOV_13_2017 1510592825

#define PST_TIME_ZONE -8
#define PST_TIME_ZONE_DAYLIGHT_SAVINGS_DIFF 1

#define GMT_OFFSET_SECS (PST_TIME_ZONE * 3600)
#define GMT_OFFSET_SECS_DST ((PST_TIME_ZONE + PST_TIME_ZONE_DAYLIGHT_SAVINGS_DIFF) * 3600)

// Translate iot_configs.h defines into variables used by the sample
static const char* ssid = IOT_CONFIG_WIFI_SSID;
static const char* password = IOT_CONFIG_WIFI_PASSWORD;
static const char* host = IOT_CONFIG_IOTHUB_FQDN;
static const char* mqtt_broker_uri = "mqtts://" IOT_CONFIG_IOTHUB_FQDN;
static const char* device_id = IOT_CONFIG_DEVICE_ID;
static const int mqtt_port = AZ_IOT_DEFAULT_MQTT_CONNECT_PORT;

// Memory allocated for the sample's variables and structures.
static esp_mqtt_client_handle_t mqtt_client;
static az_iot_hub_client client;

static char mqtt_client_id[128];
static char mqtt_username[128];
static char mqtt_password[200];
static uint8_t sas_signature_buffer[256];
static unsigned long next_telemetry_send_time_ms = 0;
static char telemetry_topic[128];
static uint32_t telemetry_send_count = 0;
static String telemetry_payload = "{}";

#define INCOMING_DATA_BUFFER_SIZE 128
static char incoming_data[INCOMING_DATA_BUFFER_SIZE];

// Auxiliary functions
#ifndef IOT_CONFIG_USE_X509_CERT
static AzIoTSasToken sasToken(
    &client,
    AZ_SPAN_FROM_STR(IOT_CONFIG_DEVICE_KEY),
    AZ_SPAN_FROM_BUFFER(sas_signature_buffer),
    AZ_SPAN_FROM_BUFFER(mqtt_password));
#endif // IOT_CONFIG_USE_X509_CERT

static void connectToWiFi()
{
  Logger.Info("Connecting to WIFI SSID " + String(ssid));

  WiFi.mode(WIFI_STA);
  WiFi.disconnect();
  delay(100);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");

  Logger.Info("WiFi connected, IP address: " + WiFi.localIP().toString());
}

static void initializeTime()
{
  Logger.Info("Setting time using SNTP");

  configTime(GMT_OFFSET_SECS, GMT_OFFSET_SECS_DST, NTP_SERVERS);
  time_t now = time(NULL);
  while (now < UNIX_TIME_NOV_13_2017)
  {
    delay(500);
    Serial.print(".");
    now = time(nullptr);
  }
  Serial.println("");
  Logger.Info("Time initialized!");
}

void receivedCallback(char* topic, byte* payload, unsigned int length)
{
  Logger.Info("Received [");
  Logger.Info(topic);
  Logger.Info("]: ");
  for (int i = 0; i < length; i++)
  {
    Serial.print((char)payload[i]);
  }
  Serial.println("");
}

static esp_err_t mqtt_event_handler(esp_mqtt_event_handle_t event)
{
  switch (event->event_id)
  {
    int i, r;

    case MQTT_EVENT_ERROR:
      Logger.Info("MQTT event MQTT_EVENT_ERROR");
      break;
    case MQTT_EVENT_CONNECTED:
      Logger.Info("MQTT event MQTT_EVENT_CONNECTED");

      r = esp_mqtt_client_subscribe(mqtt_client, AZ_IOT_HUB_CLIENT_C2D_SUBSCRIBE_TOPIC, 1);
      if (r == -1)
      {
        Logger.Error("Could not subscribe for cloud-to-device messages.");
      }
      else
      {
        Logger.Info("Subscribed for cloud-to-device messages; message id:" + String(r));
      }

      break;
    case MQTT_EVENT_DISCONNECTED:
      Logger.Info("MQTT event MQTT_EVENT_DISCONNECTED");
      break;
    case MQTT_EVENT_SUBSCRIBED:
      Logger.Info("MQTT event MQTT_EVENT_SUBSCRIBED");
      break;
    case MQTT_EVENT_UNSUBSCRIBED:
      Logger.Info("MQTT event MQTT_EVENT_UNSUBSCRIBED");
      break;
    case MQTT_EVENT_PUBLISHED:
      Logger.Info("MQTT event MQTT_EVENT_PUBLISHED");
      break;
    case MQTT_EVENT_DATA:
      Logger.Info("MQTT event MQTT_EVENT_DATA");

      for (i = 0; i < (INCOMING_DATA_BUFFER_SIZE - 1) && i < event->topic_len; i++)
      {
        incoming_data[i] = event->topic[i];
      }
      incoming_data[i] = '\0';
      Logger.Info("Topic: " + String(incoming_data));

      for (i = 0; i < (INCOMING_DATA_BUFFER_SIZE - 1) && i < event->data_len; i++)
      {
        incoming_data[i] = event->data[i];
      }
      incoming_data[i] = '\0';
      Logger.Info("Data: " + String(incoming_data));

      //Manejo del motor
      if(String(incoming_data).equals("girar")){
        stepper.step(DPT);
        Serial.println("girando...");
      }else
      if(String(incoming_data).equals("rotar")){                
        Serial.println("rotando...");
        ejecutarFotodiodoTask=true;
        ejecutarMotorTask=true;
      }else
      if(String(incoming_data).equals("detenerRotacion")){
        Serial.println("Deteniendo rotacion");
        ejecutarMotorTask=false;
        ejecutarFotodiodoTask=false;
      }else
      if(String(incoming_data).equals("modoLibre")){
        if(ejecutarFotodiodoTask){
          Serial.println("Modo libre desactivado");
          ejecutarFotodiodoTask = false;  
        }else {
          Serial.println("Modo libre");
          ejecutarFotodiodoTask = true;
        }
        
      }else      
      if(String(incoming_data).equals("configuracion")){
        Serial.println("Enviando configuracion");

        String cadenaInfo = "{ \"codigo\": \"configuracion\", \"contenido\": {\"Device id\": \""+String(mqtt_client_id)+"\", \"SensorValue\": "+String(analogRead(pinFotodiodo))+", \"RPM\": "+String(RPM)+", \"laps\": "+String(laps)+", \"DPT\": "+String(DPT)+"}}";

        Serial.println(cadenaInfo);
        sendTelemetry(cadenaInfo);

      }else
      if(isValidJson(String(incoming_data))){
        Serial.println("json recibido");
        JsonDocument doc;
        deserializeJson(doc,String(incoming_data));
        String codigo = doc["codigo"];
        if(codigo.equals("actualizar")){
          RPM = int(doc["contenido"]["angularSpeed"]);
          laps = int(doc["contenido"]["degreesToRotate"]);
          DPT = int(doc["contenido"]["turnsToRotate"]);
          MPD = int(doc["contenido"]["messagesPerDegree"])
          Serial.println("Actualizando...");
          Serial.println("RPM: "+String(RPM)+"  laps: "+String(laps)+ "  DPT: "+String(DPT)+ "  MPD: "+String(MPD));
        }
      }

      break;
    case MQTT_EVENT_BEFORE_CONNECT:
      Logger.Info("MQTT event MQTT_EVENT_BEFORE_CONNECT");
      break;
    default:
      Logger.Error("MQTT event UNKNOWN");
      break;
  }

  return ESP_OK;
}

static void initializeIoTHubClient(){
  az_iot_hub_client_options options = az_iot_hub_client_options_default();
  options.user_agent = AZ_SPAN_FROM_STR(AZURE_SDK_CLIENT_USER_AGENT);

  if (az_result_failed(az_iot_hub_client_init(
          &client,
          az_span_create((uint8_t*)host, strlen(host)),
          az_span_create((uint8_t*)device_id, strlen(device_id)),
          &options)))
  {
    Logger.Error("Failed initializing Azure IoT Hub client");
    return;
  }

  size_t client_id_length;
  if (az_result_failed(az_iot_hub_client_get_client_id(
          &client, mqtt_client_id, sizeof(mqtt_client_id) - 1, &client_id_length)))
  {
    Logger.Error("Failed getting client id");
    return;
  }

  if (az_result_failed(az_iot_hub_client_get_user_name(
          &client, mqtt_username, sizeofarray(mqtt_username), NULL)))
  {
    Logger.Error("Failed to get MQTT clientId, return code");
    return;
  }

  Logger.Info("Client ID: " + String(mqtt_client_id));
  Logger.Info("Username: " + String(mqtt_username));
}

static int initializeMqttClient(){
#ifndef IOT_CONFIG_USE_X509_CERT
  if (sasToken.Generate(SAS_TOKEN_DURATION_IN_MINUTES) != 0)
  {
    Logger.Error("Failed generating SAS token");
    return 1;
  }
#endif

  esp_mqtt_client_config_t mqtt_config;
  memset(&mqtt_config, 0, sizeof(mqtt_config));
  
  mqtt_config.uri = mqtt_broker_uri;  
  mqtt_config.port = mqtt_port;
  mqtt_config.client_id = mqtt_client_id;
  mqtt_config.username = mqtt_username;

#ifdef IOT_CONFIG_USE_X509_CERT
  Logger.Info("MQTT client using X509 Certificate authentication");
  mqtt_config.client_cert_pem = IOT_CONFIG_DEVICE_CERT;
  mqtt_config.client_key_pem = IOT_CONFIG_DEVICE_CERT_PRIVATE_KEY;
#else // Using SAS key
  mqtt_config.password = (const char*)az_span_ptr(sasToken.Get());
#endif

  mqtt_config.keepalive = 30;
  mqtt_config.disable_clean_session = 0;
  mqtt_config.disable_auto_reconnect = false;
  mqtt_config.event_handle = mqtt_event_handler;
  mqtt_config.user_context = NULL;
  mqtt_config.cert_pem = (const char*)ca_pem;

  mqtt_client = esp_mqtt_client_init(&mqtt_config);

  if (mqtt_client == NULL)
  {
    Logger.Error("Failed creating mqtt client");
    return 1;
  }

  esp_err_t start_result = esp_mqtt_client_start(mqtt_client);

  if (start_result != ESP_OK)
  {
    Logger.Error("Could not start mqtt client; error code:" + start_result);
    return 1;
  }
  else
  {
    Logger.Info("MQTT client started");
    return 0;
  }
}

/*
 * @brief           Gets the number of seconds since UNIX epoch until now.
 * @return uint32_t Number of seconds.
 */
static uint32_t getEpochTimeInSecs() { return (uint32_t)time(NULL); }

static void establishConnection()
{
  connectToWiFi();
  initializeTime();
  initializeIoTHubClient();
  (void)initializeMqttClient();
}

static void generateTelemetryPayload(String mensaje)
{
  // You can generate the JSON using any lib you want. Here we're showing how to do it manually, for simplicity.
  // This sample shows how to generate the payload using a syntax closer to regular delevelopment for Arduino, with
  // String type instead of az_span as it might be done in other samples. Using az_span has the advantage of reusing the 
  // same char buffer instead of dynamically allocating memory each time, as it is done by using the String type below.
  
  //telemetry_payload = "{ \"msgCount\": " + String(telemetry_send_count++) + " }";
  telemetry_payload = mensaje;
}

static void sendTelemetry(String mensaje)
{
  Logger.Info("Sending telemetry ...");

  // The topic could be obtained just once during setup,
  // however if properties are used the topic need to be generated again to reflect the
  // current values of the properties.
  if (az_result_failed(az_iot_hub_client_telemetry_get_publish_topic(
          &client, NULL, telemetry_topic, sizeof(telemetry_topic), NULL)))
  {
    Logger.Error("Failed az_iot_hub_client_telemetry_get_publish_topic");
    return;
  }

  generateTelemetryPayload(mensaje);

  if (esp_mqtt_client_publish(
          mqtt_client,
          telemetry_topic,
          (const char*)telemetry_payload.c_str(),
          telemetry_payload.length(),
          MQTT_QOS1,
          DO_NOT_RETAIN_MSG)
      == 0)
  {
    Logger.Error("Failed publishing");
  }
  else
  {
    Logger.Info("Message published successfully");
  }
}

// Arduino setup and loop main functions.

void setup() {
  xTaskCreatePinnedToCore(
    motorTaskFunc,
    "motorTask",
    3000,
    NULL,
    4,
    &motorTask,
    1
  );  
  delay(500);

  xTaskCreatePinnedToCore(
    fotodiodoTaskFunc,
    "fotodiodoTask",
    2000,
    NULL,
    5,
    &fotodiodoTask,
    0
  );
  delay(500);

  establishConnection(); 
  //Motor a paso
  stepper.setSpeed(1);
  //Bocina
  pinMode(12,OUTPUT);
}

void loop()
{    
  delay(1000);    
}


bool isValidJson(const String& jsonString) {
  // Reservar memoria para el documento JSON
  const size_t capacity = JSON_OBJECT_SIZE(2) + JSON_OBJECT_SIZE(3) + 70;
  DynamicJsonDocument doc(capacity);

  // Intentar parsear el JSON
  DeserializationError error = deserializeJson(doc, jsonString);

  // Devolver true si no hay errores, false en caso contrario
  return !error;
}

String cadenaArreglo(int arreglo[], int tamanio){
  String cadena = "[";
  for(int i=0;i<tamanio;i++){
    cadena += String(arreglo[i]);
    if(i<3){
      cadena += ", ";
    } 
  }
  cadena += "]";
  return cadena;
}


