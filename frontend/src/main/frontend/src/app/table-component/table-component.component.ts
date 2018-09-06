import { Component, OnInit, Input, Output, ViewChildren, ViewChild } from '@angular/core';
import { ViewEncapsulation } from '@angular/core';
import { TableParameterComponent } from '../table-parameter/table-parameter.component';
import { TableBaseComponent } from './table-base.component';
import {TableServiceProvider} from "../table-service/table.service.provider";
import {RoomService} from "../table-service/room.service";
import {GroupService} from "../table-service/group.service";
import {CourseService} from "../table-service/course.service";
import {SubjectService} from "../table-service/subject.service";
import {TablePage} from '../model/page.type';

@Component({
  selector: 'app-table-component',
  templateUrl: './table-component.component.html',
  styleUrls: ['./table-component.component.css',
  '../../../node_modules/@swimlane/ngx-datatable/release/index.css',
  '../../../node_modules/@swimlane/ngx-datatable/release/themes/material.css',
  '../../../node_modules/@swimlane/ngx-datatable/release/assets/icons.css'
  ],
  providers: [SubjectService, CourseService, RoomService, GroupService, TableServiceProvider],
  encapsulation: ViewEncapsulation.None
})
export class TableComponentComponent extends TableBaseComponent implements OnInit {

  @Output('relationParameterName') relationParameterName : string;
  @ViewChildren(TableParameterComponent) parameterComponent;
  @ViewChild('editTmpl') editTmpl: TemplateRef<any>;
  @ViewChild('indexTmpl') indexTmpl: TemplateRef<any>;
  @ViewChild('addDelTmpl') addDelTmpl: TemplateRef<any>;
  private page = new TablePage();

  constructor(tableServiceProvider: TableServiceProvider) {
    super(tableServiceProvider);
  }

  ngOnInit() {
    super.ngOnInit();
    this.page.pageNumber = 0;
    this.page.limit = 5;
    this.relationParameterName = this.service.getRelationParameterName();
    console.log(this.relationParameterName);
    this.setPage({ offset: 0 });
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

  find(){
    this.service.find(this.page, this.dataTableValues).subscribe(
      data => {
        let dataFromPage = data.data;
        if(this.relationParameterName !== null){
          dataFromPage = this.service.transformValues(dataFromPage);
        }
        this.tableRows = dataFromPage;
        console.log(data);
        this.page.totalElements = data.totalElements;
        this.initTable();
      },
      err => {console.log("Error occured when get all elements.")}
    );
  }

  handlePageChange(event){
    this.setPage({ offset: (event.page -1)});
  }

  initTable(){
    // this.page.pageNumber = pageInfo.offset;
    let tempCols = [];
    let tempRows = [];

    console.log(this.tableRows);
      tempCols.push({name:"#", cellTemplate: this.indexTmpl});
    this.dataTableNames.forEach((element, index) => {
      tempCols.push({name:element.value});
    });
      tempCols.push({name:"Edytuj",cellTemplate: this.editTmpl}, {name:"Dodaj/Usun",cellTemplate: this.addDelTmpl})
    this.tableRows.forEach((elementData, indexData) => {
      let cellData = {};
      this.dataTableNames.forEach((elementDefinition, indexDefinition) => {
        cellData[elementDefinition.value.toLowerCase()] = elementData[elementDefinition.data];
      });
      cellData['edytuj'] = elementData;
      cellData['dodaj/Usun'] = elementData.id;
      tempRows.push(cellData);
    });
    this.cols = tempCols;
    this.rows = tempRows;
  }

  setPage(pageInfo){
    this.page.pageNumber = pageInfo.offset;
    this.page.size = 5;
    this.find();
    // this.page.size = this.lecturePerDay;
    // this.page.days = this.daysPerWeek;
    // this.page.groupId = this.groupId;

      // this.page.totalPages = pagedData.totalPages;
      // this.page.size = this.lecturePerDay;
      // if(pagedData != null && pagedData.totalElements != 0){
      //   this.mapRowsAndCols(pagedData.data);
      // }
      // this.rows = pagedData.data;
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
