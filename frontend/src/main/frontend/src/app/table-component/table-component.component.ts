import { Component, OnInit, Input, Output } from '@angular/core';
import {TableServiceProvider} from "../table-service/table.service.provider";
import {RoomService} from "../table-service/room.service";
import {GroupService} from "../table-service/group.service";
import {CourseService} from "../table-service/course.service";
import {SubjectService} from "../table-service/subject.service";
import {Table} from "../interface/table.type";
import { Pipe, PipeTransform } from '@angular/core';

const ADD_NAME = 'Dodaj';
const SAVE_NAME = 'Zapisz';

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
  providers: [SubjectService, CourseService, RoomService, GroupService, TableServiceProvider]
})
export class TableComponentComponent implements OnInit {

  private service;

  @Input('dataTableValues') dataTableValues: string[] = [];
  @Input('tableTypeName') tableTypeName: string;
  @Output('tableRows') tableRows: Table;
  @Output('dataTableNames') dataTableNames: string[];
  @Output('title') title : string;
  @Output('rowActive') rowActive : string;
  @Output('addButtonName') addButtonName : string = ADD_NAME;


  constructor(private tableServiceProvider: TableServiceProvider){}

  ngOnInit() {
    this.service = this.tableServiceProvider.getServiceByName(this.tableTypeName);
    this.list();
    this.dataTableNames = this.service.getDataTableParameters();
    this.title = this.service.getTitle();
  }

  private refreshTable(){
    this.dataTableValues = [];
    this.list();
    this.addButtonName = ADD_NAME;
  }

  add(){
    if(this.addButtonName === ADD_NAME){
      this.service.create(this.tableTypeName, this.dataTableValues).subscribe(
        data => {this.refreshTable()},
        err => {console.log("Error occured.")}
      );
    }else{
      this.service.update(this.dataTableValues, this.rowActive).subscribe(
        data => {this.refreshTable()},
        err => {console.log("Error occured.")}
      );
    }
  };

  remove(rowId){
    this.service.remove(rowId).subscribe(
      data => {this.refreshTable()},
      err => {console.log("Error occured.")}
    );
  };

  edit(row: any){
    this.rowActive = row.id;
    this.dataTableValues = (<any>Object).values(row).splice(1);
    this.addButtonName = SAVE_NAME;
  };

  list(){
    this.service.list().then(data =>
      this.tableRows = data);
  }

}
