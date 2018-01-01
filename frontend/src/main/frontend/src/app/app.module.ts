import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import {HttpClientModule} from '@angular/common/http';
import { AppComponent } from './app.component';
import { TableComponentComponent } from './table-component/table-component.component';
import { ValuesPipe } from './table-component/table-component.component';
import { AdminPanelComponent } from './admin-panel-component/admin-panel-component.component';
import {TableTypeProvider} from "./table-service/table.type.provider";

@NgModule({
  declarations: [
    AppComponent,
    TableComponentComponent,
    AdminPanelComponent,
    ValuesPipe
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent, AdminPanelComponent]
})
export class AppModule { }
