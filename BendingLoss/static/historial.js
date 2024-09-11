const selectElement = document.getElementById('select');
const voltajeEstableLabel = document.getElementById('voltaje_estable');
const graph = document.getElementById('graph-container');
const deleteButton = document.getElementById('delete');

document.getElementById("weigh-button").addEventListener('click', () => {
    if(sessionStorage.getItem('voltajeEstable') == null){
        alert('Perform sensor calibration before proceeding');
    }else{
        location.href = '/weigh';
    }
})

//Crea el Select con las fechas
fetch('/recuperarFecha/', {
    method: 'GET',
  })
  .then(response => response.json())
  .then(data => {        
    selectElement.innerHTML = '';
  
    JSON.parse(data).forEach(function(elemento) {
      var optionElement = document.createElement('option');
      optionElement.value = elemento.pk;
      optionElement.textContent = new Date(elemento.fields.fecha).toLocaleString();
      selectElement.appendChild(optionElement);
    });
    if(selectElement.value){
        const fechaID = selectElement.value
        fetch(`/fecha/${fechaID}/sensor/`, {
            method: 'GET',
        })
        .then(response => response.json())
        .then(data => {  
            //Hace la grafica y actualiza el label
            crearGrafica(data.voltaje_sensor,data.voltaje_estable)                
            voltajeEstableLabel.textContent = "Stable voltage level: "+ String(data.voltaje_estable);
        })
        .catch(error => {
            console.error('Error al obtener los elementos:', error);
        });
    }else{
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
    }
  })
  .catch(error => {
    console.error('Error al obtener los elementos:', error);
  });

//Crea la grafica en base a la fecha que ponga el usuario
selectElement.addEventListener('change', () => {
    const fechaID = selectElement.value
    fetch(`/fecha/${fechaID}/sensor/`, {
        method: 'GET',
      })
      .then(response => response.json())
      .then(data => {  
        //Hace la grafica y actualiza el label
        crearGrafica(data.voltaje_sensor,data.voltaje_estable)                
        voltajeEstableLabel.textContent = "Stable voltage level: "+ String(data.voltaje_estable);
      })
      .catch(error => {
        console.error('Error al obtener los elementos:', error);
      });
});

//Borra la gráfica cuando el usuario haga clic al botón borrar
deleteButton.addEventListener('click', () => {
    const fechaID = selectElement.value
    

    fetch(`/fecha/${fechaID}/borrar`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRFToken': document.cookie.match(/csrftoken=([^;]+)/)[1] // Incluir el token CSRF en el encabezado de la solicitud
          }
    })
    .then(response => response.json())
    .then(data=>{
        if (data.success) {
            // La solicitud se procesó correctamente
            console.log('Valor eliminado de la base de datos');
            window.location.reload();
          } else {
            // Hubo un error en la solicitud
            console.error('Error al eliminar el valor:', data.error);
          }
        })
    .catch(error => {
      console.error('Error en la solicitud AJAX:', error);
    });
});
//Función que dibuja la grafica
function crearGrafica(valor, voltaje_estable) {
    var yData = valor;
    var xRangoMax = 0;
    var xRangoMin = 0;  
        
    for(i=0;i<yData.length;i++){
        yData[i] = 10*Math.log10((yData[i]/voltaje_estable));        
    }

    /*
    for (i = 0; i <= yData.length; i++) {
        ydB[i] = voltaje_estable;
    } 
    */

    // Agregar el nuevo punto a los arreglos de datos                        
    if (yData.length < 50) {
        xRangoMax = 50;
    }else if(yData.length < 300){
        xRangoMax = 300;
    } else {
        xRangoMax = yData.length;
        xRangoMin = xRangoMax-300;
    }

    // Actualizar la gráfica
    Plotly.newPlot(
        //ID del div (graph)
        graph,
        //Datos a graficar (data)
        [
            {
                y: yData,
                type: "scatter",
                name: "Data",
                line: {
                    color: 'green'
                }
            }/*,
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

            }*/

        ],
        //Personalización (layout)
        {
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
        },
        //Configuración (config)
        {
            responsive: true,
        }
    );

}
