import { Component, OnInit, Input, Output } from '@angular/core';
import {TableServiceProvider} from "../table-service/table.service.provider";
import {RoomService} from "../table-service/room.service";
import {Table} from "../interface/table.type";
import {Room} from "../model/room.type";

@Component({
  selector: 'app-table-component',
  templateUrl: './table-component.component.html',
  styleUrls: ['./table-component.component.css'],
  providers: [RoomService, TableServiceProvider]
})
export class TableComponentComponent implements OnInit {

  private service;

  @Input('dataTableValues') dataTableValues: string[] = [];
  @Input('tableTypeName') tableTypeName: string;
  @Output('tableRows') tableRows: Table;
  @Output('dataTable') dataTable: string[];


  constructor(private tableServiceProvider: TableServiceProvider){}

  ngOnInit() {
    this.service = this.tableServiceProvider.getServiceByName(this.tableTypeName);
    this.list();
    this.dataTable = this.service.getDataTableParameters();
  }

  create(){
    this.service.create(this.tableTypeName, this.dataTableValues);
    this.list();
  };

  list(){
    this.service.list().then(data => this.tableRows = data);
  }

}
