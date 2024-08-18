import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SettingsComponent } from './settings/settings.component';
import { MeasurementComponent } from './measurement/measurement.component';
import { VisualizationComponent } from './visualization/visualization.component';
import { DataComponent } from './data/data.component';

const routes: Routes = [
  {path: 'settings', component: SettingsComponent },
  {path: 'measurement', component: MeasurementComponent},
  {path: 'visualization', component: VisualizationComponent},
  {path: 'data', component: DataComponent},
  {path: '', redirectTo: 'settings', pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
