from django.contrib import admin
from .models import Sensor,Calibrar,Fecha,Usuario

# Register your models here.



admin.site.register(Usuario)
admin.site.register(Fecha)
admin.site.register(Calibrar)
admin.site.register(Sensor)