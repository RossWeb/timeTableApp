import { Component, OnInit, Input, Output,ViewChild, ViewChildren } from '@angular/core';
import { TableBaseComponent } from '../table-component/table-base.component';
import {TableServiceProvider} from "../table-service/table.service.provider";
import { ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-table-parameter',
  templateUrl: './table-parameter.component.html',
  styleUrls: ['./table-parameter.component.css',
  '../../../node_modules/@swimlane/ngx-datatable/release/index.css',
    '../../../node_modules/@swimlane/ngx-datatable/release/themes/material.css',
  '../../../node_modules/@swimlane/ngx-datatable/release/assets/icons.css'],
  encapsulation: ViewEncapsulation.None,
})
export class TableParameterComponent extends TableBaseComponent implements OnInit  {


  @Input('tableTypeName') tableTypeName: string;
  @Input('dataTableValuesSearched') dataTableValuesSearched: string[] = [];
  @Output('buttonParameterName') buttonParameterName: string;

  constructor(tableServiceProvider: TableServiceProvider) {
    super(tableServiceProvider, false);
  }

  ngOnInit() {
    super.ngOnInit();
    console.log('Table parameter typeName ' + this.tableTypeName);
    this.service = this.tableServiceProvider.getServiceByName(this.tableTypeName);
    this.service.setParameterTable(this.isParameter);
    this.find();
    console.log('Table parameter service ' + this.service);
    this.dataTableNames = this.service.getDataTableParameters();
    this.title = this.service.getTitle();
    this.dataTableNames = [
      {
        value : 'Nazwa',
        data: 'name',
        type : 'Input'
      }
    ]
  }

  setPrimaryKey(primaryKey: number){
    this.service.setPrimaryKey(primaryKey);
  }

  addParameters(rowId: number){
    this.service.createParameters(rowId).subscribe(
      data => {this.refreshTable()},
      err => {console.log("Error occured when add.")}
    );
  }

}
