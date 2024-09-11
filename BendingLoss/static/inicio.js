document.getElementById("weigh-button").addEventListener('click', () => {
    if(sessionStorage.getItem('voltajeEstable') == null){
        alert('Perform sensor calibration before proceeding');        
    }else{
        location.href = '/weigh';
    }
})