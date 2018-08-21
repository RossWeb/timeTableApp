import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
// import { RouterModule, Routes } from '@angular/router';

import {HttpClientModule} from '@angular/common/http';
import { AppComponent } from './app.component';
import { TableComponentComponent } from './table-component/table-component.component';
import { ValuesPipe } from './table-component/table-base.component';
import { AdminPanelComponent } from './admin-panel-component/admin-panel-component.component';
import {TableTypeProvider} from "./table-service/table.type.provider";
import { TableParameterComponent } from './table-parameter/table-parameter.component';
import { AppRoutingModule } from './app-routing.module';
import { MainComponent } from './main/main.component';

// const appRoutes: Routes = [
  // { path: 'konfiguracja', component: AppComponent },
  // { path: 'main',      component: MainComponent },
  // { path: '',
  //   redirectTo: '/main',
  //   pathMatch: 'full'
  // },
  // { path: '**', component: MainComponent }
// ];


@NgModule({
  declarations: [
    AppComponent,
    TableComponentComponent,
    AdminPanelComponent,
    ValuesPipe,
    TableParameterComponent,
    MainComponent

  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule
    // RouterModule.forRoot(
    //   appRoutes,
    //   { enableTracing: true } // <-- debugging purposes only
    // )
  ],
  providers: [],
  bootstrap: [AppComponent]
})

export class AppModule { }
