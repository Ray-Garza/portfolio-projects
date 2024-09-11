const graph = document.getElementById('graph-weigh');
const weighLabel = document.getElementById('weigh');
const outputLabelW = document.getElementById('outputW');
const closeButton = document.getElementById('stop');
const pauseButton = document.getElementById('pause');

//Variables para tratar datos
var volts = [];
var index = [];
var nIndex = 0;
var receivedData = ''; 
var DfechI = 0;
var DvoltEstI = 0;
var stableDecibelLevel = 0;
var reader;
var pausa = false;
//Datos de la grafica
var yData = [];


if(sessionStorage.getItem('voltajeEstable') !== null){
    DvoltEstI = sessionStorage.getItem('voltajeEstable');    
    stableDecibelLevel = sessionStorage.getItem('stableDecibel');
    fetch('/guardarFecha/', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
          'X-CSRFToken': document.cookie.match(/csrftoken=([^;]+)/)[1], 
        },        
      })      
        .then(response => response.json())
        .then(data => {
              DfechI = data.DfechI  
        });          
}else{
    alert('Perform sensor calibration before proceeding');    
    location.href = '/calibration';
}

//Dibujar gráfica inicial
Plotly.new  (
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

//Botón de inicio
document.getElementById('start').addEventListener('click', async () => {
    //Abrir el puerto

    const port = await navigator.serial.requestPort();
    await port.open({ baudRate: 9600 });
    //Objeto para leer el puerto
    reader = port.readable.getReader();

    // Función para leer los datos de manera asincrónica
    readData(port);
});

//Función para leer datos
const readData = async (port) => {
    while (true) {
        const { value, done } = await reader.read();
        if (done) {
            reader.releaseLock()   
            port.close()                     
            break;
        }
        if(!pausa){
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

                console.log(completeData);
                agregarPunto(parseFloat(completeData));
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
                receivedData = ''
                
            }
        }
        
    }
};

//Botón para detener la comunicación
closeButton.addEventListener('click', async () => {
    reader.cancel(); // Cancelar la lectura de datos   

    guardarSensor(volts, index);
    volts = [];
    index = [];
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

function agregarPunto(valor) {
    var yValue = valor;
    var xRangoMax = 0;
    var xRangoMin = 0;

    if (!isNaN(yValue)) {
        // Agregar el nuevo punto a los arreglos de datos                

        if (yData.length < 49) {
            xRangoMax = 50;
        } else if (yData.length < 299) {
            xRangoMax = 300;
        } else {
            xRangoMax = yData.length;
            xRangoMin = xRangoMax - 300;
        }
        //Pasar voltaje a decibelios
        yData.push(10 * Math.log10((yValue / stableDecibelLevel)));

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
