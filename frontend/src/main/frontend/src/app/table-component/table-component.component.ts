import { Component, OnInit, Input, Output, ViewChildren } from '@angular/core';
import { TableParameterComponent } from '../table-parameter/table-parameter.component';
import { TableBaseComponent } from './table-base.component';
import {TableServiceProvider} from "../table-service/table.service.provider";
import {RoomService} from "../table-service/room.service";
import {GroupService} from "../table-service/group.service";
import {CourseService} from "../table-service/course.service";
import {SubjectService} from "../table-service/subject.service";

@Component({
  selector: 'app-table-component',
  templateUrl: './table-component.component.html',
  styleUrls: ['./table-component.component.css'],
  providers: [SubjectService, CourseService, RoomService, GroupService, TableServiceProvider]
})
export class TableComponentComponent extends TableBaseComponent implements OnInit {

  @Output('relationParameterName') relationParameterName : string;
  @ViewChildren(TableParameterComponent) parameterComponent;


  constructor(tableServiceProvider: TableServiceProvider) {
    super(tableServiceProvider);
  }

  ngOnInit() {
    super.ngOnInit();
    this.relationParameterName = this.service.getRelationParameterName();
    console.log(this.relationParameterName);
  }

  list(){
    this.service.list().subscribe(
      data => {
        if(this.relationParameterName !== null){
          data = this.service.transformValues(data);
        }
        this.tableRows = data;
        console.log(data);
      },
      err => {console.log("Error occured when get all elements.")}
    );
  }


  private onSelectRelationParameter(row: any) {
      if(this.relationParameterName !== null){
        this.hiddenParameters = false;
        console.log(this.parameterComponent);
        this.parameterComponent.first.setPrimaryKey(row.id);
        this.parameterComponent.first.refreshTable();
        // this.service.get(row.id).subscribe(
        //   data => {
        //     console.log("Get element with id " + row.id + " " + data);
        //   },
        //   err => {console.log("Error occured when get elements.")}
        // );
      }

    console.log('Select parameter ' + row.id);
  }

}
