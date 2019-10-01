import { Component, OnInit, Input,Inject, Output, ViewChildren, ViewChild } from '@angular/core';
import { ViewEncapsulation } from '@angular/core';
import { TableParameterComponent } from '../table-parameter/table-parameter.component';
import { TableBaseComponent } from './table-base.component';
import { AdminPanelComponent } from '../admin-panel-component/admin-panel-component.component';
import {TableServiceProvider} from "../table-service/table.service.provider";
import {RoomService} from "../table-service/room.service";
import {GroupService} from "../table-service/group.service";
import {CourseService} from "../table-service/course.service";
import {SubjectService} from "../table-service/subject.service";
import {HoursLectureService} from "../table-service/hours.lecture.service";
import {TeacherService} from "../table-service/teacher.service";


@Component({
  selector: 'app-table-component',
  templateUrl: './table-component.component.html',
  styleUrls: ['./table-component.component.css',
  '../../../node_modules/@swimlane/ngx-datatable/release/index.css',
  '../../../node_modules/@swimlane/ngx-datatable/release/themes/material.css',
  '../../../node_modules/@swimlane/ngx-datatable/release/assets/icons.css'
  ],
  providers: [SubjectService, CourseService, RoomService, GroupService, TableServiceProvider, HoursLectureService, TeacherService],
  encapsulation: ViewEncapsulation.None
})
export class TableComponentComponent extends TableBaseComponent implements OnInit {


  @ViewChildren(TableParameterComponent) parameterComponent;

  constructor(tableServiceProvider: TableServiceProvider,@Inject(AdminPanelComponent) private parent: AdminPanelComponent) {
    super(tableServiceProvider, true, parent);
  }

  ngOnInit() {
    super.ngOnInit();
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
        // this.page.totalElements = data.totalElements;
        // this.initTable();
      },
      err => {console.log("Error occured when get all elements.")}
    );
  }


  onSelectRelationParameter(row: any) {
      if(this.relationParameterName !== null){
        this.hiddenParameters = false;
        console.log(this.parameterComponent);
        this.parameterComponent.first.setPrimaryKey(row.selected[0].edytuj.id);
        this.parameterComponent.first.refreshTable();
      }

  }

  remove(rowId, event){
    super.remove(rowId, event);
    if(this.relationParameterName !== null){
      this.hiddenParameters = true;
      console.log(this.parameterComponent);
      this.parameterComponent.first.setPrimaryKey(undefined);
      this.parameterComponent.first.refreshTable();
    }
  }

  edit(row: any, event){
    super.edit(row, event)
    if(this.relationParameterName !== null){
      this.hiddenParameters = true;
      console.log(this.parameterComponent);
      this.parameterComponent.first.setPrimaryKey(undefined);
      this.parameterComponent.first.refreshTable();
    }
  };


}
