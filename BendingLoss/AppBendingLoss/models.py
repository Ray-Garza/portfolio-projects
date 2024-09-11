from django.contrib.auth.models import User
from django.db import models

# Create your models here.
"""from django.utils import timezone"""

   
class Fecha(models.Model):
    fecha = models.DateTimeField(auto_now_add=True)
    def __str__(self):
        return str(self.fecha)

class Calibrar(models.Model):
    voltajeEstable = models.FloatField()

class Usuario(models.Model):
    nombre = models.CharField(max_length=200)
    correo = models.EmailField(max_length=200)
    contra = models.CharField(max_length=100)

class Sensor(models.Model):
    fecha = models.ForeignKey(Fecha, on_delete=models.CASCADE)
    calibrar = models.ForeignKey(Calibrar, on_delete=models.CASCADE)
    usuario = models.ForeignKey(Usuario, on_delete=models.CASCADE)
    voltaje = models.FloatField()
    indice = models.IntegerField()


    
 