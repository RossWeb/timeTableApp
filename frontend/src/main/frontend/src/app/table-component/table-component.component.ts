import { Component, OnInit, Input, Output } from '@angular/core';
import {TableService} from "../table-service/table.service";
import {RoomResponse} from "../interface/room.type";

@Component({
  selector: 'app-table-component',
  templateUrl: './table-component.component.html',
  styleUrls: ['./table-component.component.css'],
  providers: [TableService]
})
export class TableComponentComponent implements OnInit {

  @Input('dataTableParameters') dataTable: string[];
  @Output('tableRows') tableRows: RoomResponse;

  constructor(private tableService: TableService){}

  ngOnInit() {
    this.list();
    console.log(this.dataTable);
  }

  create(name: string, number: string){
    this.tableService.create(name, number);
  };

  list(){
     this.tableService.list().then(data => this.tableRows = data);
  }

}
