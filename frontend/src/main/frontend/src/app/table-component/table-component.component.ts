import { Component, OnInit, Input, Output } from '@angular/core';
import {TableService} from "../table-service/table.service";
import {Room} from "../model/room.type";

@Component({
  selector: 'app-table-component',
  templateUrl: './table-component.component.html',
  styleUrls: ['./table-component.component.css'],
  providers: [TableService]
})
export class TableComponentComponent implements OnInit {

  @Input('dataTableParameters') dataTable: string[];
  @Input('tableTypeName') tableTypeName: string;
  @Output('tableRows') tableRows: Room;

  constructor(private tableService: TableService){}

  ngOnInit() {
    this.list();
    console.log(this.dataTable);
  }

  create(dataTable: string[]){
    this.tableService.create<Room>(this.tableTypeName, dataTable);
  };

  list(){
     this.tableService.list<Room>().then(data => this.tableRows = data);
  }

}
