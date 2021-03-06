import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MainComponent } from './main/main.component';
import { AdminPanelComponent } from './admin-panel-component/admin-panel-component.component';

const routes: Routes = [
    { path: 'konfiguracja', component: AdminPanelComponent },
    { path: 'start',      component: MainComponent },
    { path: '',
      redirectTo: '/start',
      pathMatch: 'full'
    },
    { path: '**', component: MainComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
