import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SettingsComponent } from './settings/settings.component';

//Importes mios
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { IonicModule} from '@ionic/angular';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatTabsModule } from '@angular/material/tabs';
import { MeasurementComponent } from './measurement/measurement.component';
import { VisualizationComponent } from './visualization/visualization.component';
import { DataComponent } from './data/data.component';
import {MatSelectModule} from '@angular/material/select';
import {MatFormFieldModule, getMatFormFieldMissingControlError} from '@angular/material/form-field';
import { HttpClientModule } from '@angular/common/http';
import { NgChartsModule } from 'ng2-charts';
import { MatTableModule } from '@angular/material/table';



@NgModule({
  declarations: [
    AppComponent,
    SettingsComponent,
    MeasurementComponent,
    VisualizationComponent,
    DataComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    IonicModule.forRoot(),
    BrowserAnimationsModule,
    MatTabsModule,
    MatSelectModule,
    MatFormFieldModule,
    HttpClientModule,
    NgChartsModule,
    MatTableModule    
    
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
