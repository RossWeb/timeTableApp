import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import {HttpClientModule} from '@angular/common/http';
import { AppComponent } from './app.component';
import { TableComponentComponent } from './table-component/table-component.component';
import { AdminPanelComponent } from './admin-panel-component/admin-panel-component.component';
import {TableTypeProvider} from "./table-service/table.type.provider";

@NgModule({
  declarations: [
    AppComponent,
    TableComponentComponent,
    AdminPanelComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [TableTypeProvider],
  bootstrap: [AppComponent, AdminPanelComponent]
})
export class AppModule { }
