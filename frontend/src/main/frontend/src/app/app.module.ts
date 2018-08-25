import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import {HttpClientModule} from '@angular/common/http';
import { AppComponent } from './app.component';
import { TableComponentComponent } from './table-component/table-component.component';
import { ValuesPipe } from './table-component/table-base.component';
import { AdminPanelComponent } from './admin-panel-component/admin-panel-component.component';
import {TableTypeProvider} from "./table-service/table.type.provider";
import { TableParameterComponent } from './table-parameter/table-parameter.component';
import { AppRoutingModule } from './app-routing.module';
import { MainComponent } from './main/main.component';
import { ChartsModule } from 'ng2-charts';
import { ReportComponent } from './report/report.component';

@NgModule({
  declarations: [
    AppComponent,
    TableComponentComponent,
    AdminPanelComponent,
    ValuesPipe,
    TableParameterComponent,
    MainComponent,
    ReportComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule,
    NgxDatatableModule,
    ChartsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})

export class AppModule { }
