import json
from django.shortcuts import render
from .models import *
from django.http import JsonResponse
from django.core import serializers
# Create your views here.

#Pestaña de inicio
def aboutUs(request):
    return render(request, 'inicio.html')

#Pestaña de calibración
def calibration(request):
    return render(request, 'calibrar.html')

#Pestaña de pesar
def pesar(request):    
    return render(request, 'pesar.html')

#Pestaña de historial
def historial(request):    
    return render(request, 'historial.html')

def guardar_voltaje_estable(request):

    if request.method == 'POST':        
        objetoCalibrar = Calibrar(voltajeEstable = float(request.POST.get('voltajeEstable')))
        objetoFecha = Fecha()
        objetoCalibrar.save()
        objetoFecha.save()
        idObjetoCalibrar = objetoCalibrar.id    
        idObjetoFecha = objetoFecha.id            

        return JsonResponse({'success': True, 'DvoltEstI' : idObjetoCalibrar, 'DfechI' : idObjetoFecha})
    else:
        return JsonResponse({'success': False, 'error': 'Método no permitido'})

def guardar_fecha(request):
    if request.method == 'POST':
        objetoFecha = Fecha()
        objetoFecha.save()
        idObjetoFecha = objetoFecha.id
        return JsonResponse({'success': True,'DfechI' : idObjetoFecha})
    else:
        return JsonResponse({'success': False, 'error': 'Método no permitido'})
    

#TODO Cambiar usuario 

def guardar_voltaje_actual(request):
    try:
        if request.method == 'POST':
            data = json.loads(request.body)
            voltaje = data['voltajesActuales']
            DvoltEstI = data['DvoltEstI']
            calibrar = Calibrar.objects.get(id=DvoltEstI)
            DfechI = data['DfechI']
            fecha = Fecha.objects.get(id=DfechI)
            index = data['index']
            usuario = Usuario.objects.get(id=1)

            for i in range(len(voltaje)):                
                objetoVoltaje = Sensor(fecha = fecha, calibrar = calibrar, usuario = usuario, voltaje = voltaje[i], indice = index[i] )
                objetoVoltaje.save()

            return JsonResponse({'success': True})
        else:
            return JsonResponse({'success': False, 'error': 'Método no permitido'})
    except Fecha.DoesNotExist:
        return JsonResponse({'success': False, 'error': 'Objeto Fecha no encontrado'})
    except Calibrar.DoesNotExist:
        return JsonResponse({'success': False, 'error': 'Objeto calibrar no encontrado'})
    except Usuario.DoesNotExist:
        return JsonResponse({'success': False, 'error': 'Objeto Usuario no encontrado'})

def recuperar_fechas(request):
    try:
        if request.method == 'GET':
            fecha = Fecha.objects.all()
            fechaSerializada = serializers.serialize('json', fecha)            
            return JsonResponse(fechaSerializada, safe=False)
    except Fecha.DoesNotExist:
        return JsonResponse({'success' : False, 'error': 'Objeto Fecha no encontrado'})
    
def recuperar_voltajes(request, fechaID):
    try:
        if request.method == 'GET':
            voltaje_sensor = list(Sensor.objects.filter(fecha_id=fechaID).values_list('voltaje', flat=True).order_by('indice'))
            voltaje_estable = Sensor.objects.filter(fecha_id=fechaID).order_by('indice').first().calibrar.voltajeEstable

            data = {
                'voltaje_sensor' : voltaje_sensor,
                'voltaje_estable' : voltaje_estable
            }
            return JsonResponse(data)
    except Fecha.DoesNotExist:
        return JsonResponse({'success' : False, 'error': 'Objeto Fecha no encontrado'})

def borrar_fecha(request, fechaID):
    try:
        if request.method == 'DELETE':
            Fecha.objects.get(id=fechaID).delete()
            return JsonResponse({'success':True})
    except Fecha.DoesNotExist:
        return JsonResponse({'success':False, 'error':'Objeto Fecha no encontrado'})