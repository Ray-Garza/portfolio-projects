var connectButton = document.getElementById('start');
const stableDecibelLabel = document.getElementById('stable-decibel');
const outputLabel = document.getElementById('output');
const outputLabelW = document.getElementById('outputW');
var graph = document.getElementById('graph-container');
const weighButton = document.getElementById('weigh-button');
const calibrationContainer = document.getElementById('calibration-container');
const weighContainer = document.getElementById('weigh-container');
const weighLabel = document.getElementById('weigh');
var closeButton = document.getElementById('stop');
var pauseButton = document.getElementById('pause');

// Variable para almacenar los datos recibidos
let receivedData = ''; 
var stableDecibelLevel = 0;
var DvoltEstI = -1;
var DfechI = -1;

//Variables para manejar los puertos
var pausa = false;
var reader;
//Datos de la grafica
var yData = [];

//Bool de pestaña de calibracion
var isCalibration = true;
var isDatoCalibration = false;

//Variables para tratar datos
var volts = [];
var index = [];
var nIndex = 0;
//Pasar al pesaje
weighButton.addEventListener('click', () => {
    if(!stableDecibelLevel && sessionStorage.getItem('voltajeEstable') == null){
        alert('Perform sensor calibration before proceeding');
    }else if(!stableDecibelLevel && sessionStorage.getItem('voltajeEstable') !== null){        
        location.href = '/weigh';

    }else
    //Cambiar a la vista de pesaje
    if(isCalibration){
        isCalibration = false;
        calibrationContainer.style.display = 'none';
        weighContainer.style.display = 'block';
        graph = document.getElementById('graph-weigh');
        connectButton = document.getElementById('startW');
        pauseButton = document.getElementById('pauseW');        
        closeButton = document.getElementById('stopW');
        botones();
        yData = []
        
        if(!isDatoCalibration){
        //Mandar el voltaje estable a la base de datos
        fetch('/voltajeEstable/', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded',
              'X-CSRFToken': document.cookie.match(/csrftoken=([^;]+)/)[1], 
            },
            body: new URLSearchParams({
              voltajeEstable: stableDecibelLevel,
            }),
          })
          //TODO borrar estos logs
            .then(response => response.json())
            .then(data => {
              if (data.success) {
                // La solicitud se procesó correctamente
                
                    sessionStorage.setItem('stableDecibel', stableDecibelLevel);
                    DvoltEstI = data.DvoltEstI;
                    sessionStorage.setItem('voltajeEstable', data.DvoltEstI);
                    DfechI = data.DfechI;                
                    console.log('Valor guardado en la base de datos');                
              } else {
                // Hubo un error en la solicitud
                console.error('Error al guardar el valor:', data.error);
              }
            })
            .catch(error => {
              console.error('Error en la solicitud AJAX:', error);
            });    
        }    
    }
    
});

async function botones(){
    //Boton para conectar con arudino
    connectButton.addEventListener('click', async () => {
        //Abrir el puerto

        const port = await navigator.serial.requestPort();
        await port.open({ baudRate: 9600 });
        //Objeto para leer el puerto
        reader = port.readable.getReader();
        isDatoCalibration = false;
        // Función para leer los datos de manera asincrónica
        readData(port);            
    });



    //Botón para cancelar la comunicación
    closeButton.addEventListener('click', async () => {
        reader.cancel(); // Cancelar la lectura de datos  
        if(isCalibration){ 
            //Mandar el voltaje estable a la base de datos
            fetch('/voltajeEstable/', {
                method: 'POST',
                headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-CSRFToken': document.cookie.match(/csrftoken=([^;]+)/)[1], 
                },
                body: new URLSearchParams({
                voltajeEstable: stableDecibelLevel,
                }),
            })
            //TODO borrar estos logs
                .then(response => response.json())
                .then(data => {
                if (data.success) {
                    // La solicitud se procesó correctamente
                    sessionStorage.setItem('stableDecibel', stableDecibelLevel);
                    DvoltEstI = data.DvoltEstI;
                    sessionStorage.setItem('voltajeEstable', data.DvoltEstI);
                    DfechI = data.DfechI;                
                    console.log('Valor guardado en la base de datos');
                    isDatoCalibration = true;
                } else {
                    // Hubo un error en la solicitud
                    console.error('Error al guardar el valor:', data.error);
                }
                })
                .catch(error => {
                console.error('Error en la solicitud AJAX:', error);
                });     
        }else{
            guardarSensor(volts, index);
            volts = [];
            index = [];            
        }   

    });

    //Botón para pausar la comunicación
    pauseButton.addEventListener('click', () => {        
        if(pausa){                
            pausa = false;
            pauseButton.style.backgroundColor = '#3498db';
        }else{    
            
            pausa = true;
            pauseButton.style.backgroundColor = '#003b62';
        }
    });
}
//Crea los eventos de los botones
botones();

//Creación de la función que lee los datos
const readData = async (port) => {
    while (true) {

        const { value, done } = await reader.read();
        if (done) {

            reader.releaseLock()
            port.close()
            break;
        }
        if (!pausa) {
            //Recibe el caracter
            var data = new TextDecoder('utf-8').decode(value);

            //Lo anida        
            receivedData += data;
            ////console.log(receivedData);                

            //Analiza si el caracter es el caracter nulo
            const delimitedIndex = receivedData.indexOf('\0');

            //Si lo es, significa que ya no hay mas caracteres y se puede imprimir el resultado
            if (delimitedIndex !== -1) {

                const completeData = receivedData.substring(0, delimitedIndex);
                //Checa si el usuario está en calibración o en pesaje
                if (isCalibration) {
                    stableDecibelLevel = agregarPunto(parseFloat(completeData), true);
                    stableDecibelLabel.textContent = "Stable voltage level: " + stableDecibelLevel
                    outputLabel.textContent = "Output: " + completeData.trim();
                } else {
                    agregarPunto(parseFloat(completeData), false);
                    //TODO función del peso
                    weighLabel.textContent = "Weigh: "
                    outputLabelW.textContent = "Output: " + completeData.trim();
                    volts.push(parseFloat(completeData));
                    index.push(nIndex);
                    nIndex++;

                    if (nIndex % 5 == 0) {
                        console.log("index llega a 5 uwu")
                        guardarSensor(volts, index);
                        volts = [];
                        index = [];
                    }                    
                }

                receivedData = ''
            }
        }
    }
};




/*
    
    GRAFICA

*/

//Dibujar grafica inicial
Plotly.newPlot(
    //ID del div (graph)
    graph,
    //Datos a graficar (data)
    [
        {
            y: [0,1,2,4,3,5,4],
            type: "scatter",
            name: "Data",
            line: {
                color: 'green'
            }
        }
    ],
    //Personalización (layout)
    {                
        xaxis: {
            title: "Index",
            tickformat: 'd', //Mostrar solo valores enteros

            range: [0, 8],
        },
        yaxis: {
            title: "Output",
            range: [0, 6]
        },
        dragmode: 'pan',
    },
    //Configuración (config)
    {
        responsive: true,
    }
);

// Función para agregar un punto a los datos y actualizar la gráfica
function agregarPunto(valor, isCal) {
    var yValue = valor;
    var suma = 0;
    var media = 0;
    var xRangoMax = 0;
    var xRangoMin = 0;
    var ydB = [];

    if (!isNaN(yValue)) {
        // Agregar el nuevo punto a los arreglos de datos                
        
        if (yData.length < 49) {
            xRangoMax = 50;
        } else if(yData.length < 299){
            xRangoMax = 300;
        } else {
            xRangoMax = yData.length;
            xRangoMin = xRangoMax-300;
        }

        if(isCal){
                
            for (i = 0; i < yData.length; i++) {
                suma += yData[i];
            }
            media = suma / yData.length;

            for (i = 0; i <= yData.length; i++) {
                ydB[i] = media;
            }

            datos = [
                {
                    y: yData,
                    type: "scatter",
                    name: "Data",
                    line: {
                        color: 'green'
                    }
                },
                {
                    y: ydB,
                    type: "scatter",
                    name: "Decibel Level",
                    opacity: 0.9, // Poca opacidad (30%)
                    line: {
                        dash: 'dot', // Línea punteada
                        color: 'violet'
                    },
                    hoverinfo: 'none' // Desactivar la selección de puntos
    
                }    
            ]

            personalizacion = {                
                xaxis: {
                    title: "Index",
                    tickformat: 'd', //Mostrar solo valores enteros
    
                    range: [xRangoMin, xRangoMax],
                },
                yaxis: {
                    title: "Output",
                    range: [0, 6]
                },
                dragmode: 'pan',
            }

        }else{
            //Pasar voltaje a decibelios
            yData.push(10*Math.log10((yValue/stableDecibelLevel)));
            /*
            for (i = 0; i <= yData.length; i++) {
                ydB[i] = stableDecibelLevel;
                //10log(Vsalida/Vreferencia)
            } */
            datos = [
                {
                    y: yData,
                    type: "scatter",
                    name: "Data",
                    line: {
                        color: 'green'
                    }
                }
            ]

            personalizacion = {                
                xaxis: {
                    title: "Index",
                    tickformat: 'd', //Mostrar solo valores enteros
    
                    range: [xRangoMin, xRangoMax],
                },
                yaxis: {
                    title: "Output",
                    range: [-3, 1]
                },
                dragmode: 'pan',
            }
        }




        



        // Actualizar la gráfica
        Plotly.newPlot(
            //ID del div (graph)
            graph,
            //Datos a graficar (data)
            datos,
            //Personalización (layout)
            personalizacion,
            //Configuración (config)
            {
                responsive: true,
            }
        );

    }

    return media;

}

//Función para guardar el valor individual del sensor en la base de datos
function guardarSensor(volt, index){
    
    fetch('/voltajeActual/', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
          'X-CSRFToken': document.cookie.match(/csrftoken=([^;]+)/)[1], 
        },
        body: JSON.stringify({ voltajesActuales: volt, DvoltEstI: DvoltEstI, DfechI: DfechI, index: index}),
      })
      //TODO borrar estos logs
        .then(response => response.json())
        .then(data => console.log(data));    
}
