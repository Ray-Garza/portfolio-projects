from django.urls import path
from . import views

urlpatterns = [
    path('', views.aboutUs, name='Menu'),
    path('calibration/', views.calibration, name='calibration'),
    path('weigh/', views.pesar, name='weigh'),
    path('history/', views.historial, name='historial'),
    path('voltajeEstable/', views.guardar_voltaje_estable, name='voltajeEstable'),
    path('voltajeActual/', views.guardar_voltaje_actual, name='voltajeActual'),
    path('guardarFecha/', views.guardar_fecha, name='guardarFecha'),
    path('recuperarFecha/', views.recuperar_fechas, name='recuperarFecha'),
    path('fecha/<int:fechaID>/sensor/', views.recuperar_voltajes, name='recuperarVoltaje'),
    path('fecha/<int:fechaID>/borrar', views.borrar_fecha, name='borrarFecha'),
    ]
