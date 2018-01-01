import { Component, OnInit, Input, Output } from '@angular/core';
import {TableServiceProvider} from "../table-service/table.service.provider";
import {RoomService} from "../table-service/room.service";
import {Table} from "../interface/table.type";
import {Room} from "../model/room.type";
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'values',
    pure: false
})

export class ValuesPipe implements PipeTransform {
    transform(value, args: string[]): any {
        return (<any>Object).values(value).splice(1);
    }
}

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
  @Output('rowActive') rowActive : string;


  constructor(private tableServiceProvider: TableServiceProvider){}

  ngOnInit() {
    this.service = this.tableServiceProvider.getServiceByName(this.tableTypeName);
    this.list();
    this.dataTable = this.service.getDataTableParameters();
  }

  create(){
    this.service.create(this.tableTypeName, this.dataTableValues);
    this.dataTableValues = [];
    this.list();
  };

  edit(row: any){
    console.log(row);
    this.rowActive = row.id;
    this.dataTableValues = (<any>Object).values(row).splice(1);
  };

  list(){
    this.service.list().then(data =>
      this.tableRows = data);
  }

}
