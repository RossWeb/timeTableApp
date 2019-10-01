import { Component, OnInit, Input, Output, ViewChild, TemplateRef } from '@angular/core';
import {TableServiceProvider} from "../table-service/table.service.provider";
import {RoomService} from "../table-service/room.service";
import {GroupService} from "../table-service/group.service";
import {CourseService} from "../table-service/course.service";
import {SubjectService} from "../table-service/subject.service";
import {TeacherService} from "../table-service/teacher.service";
import {HoursLectureService} from "../table-service/hours.lecture.service";
import {Table} from "../interface/table.type";
import {TablePage} from '../model/page.type';
import { Pipe, PipeTransform } from '@angular/core';
import {TableService} from "../interface/table.service";
import { AdminPanelComponent } from '../admin-panel-component/admin-panel-component.component';


const ADD_NAME = 'Dodaj';
const REMOVE_NAME = "Usuń";
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
  selector: 'app-table-base-component',
  templateUrl: './table-component.component.html',
  styleUrls: ['./table-component.component.css',
  '../../../node_modules/@swimlane/ngx-datatable/release/index.css',
  '../../../node_modules/@swimlane/ngx-datatable/release/themes/material.css',
  '../../../node_modules/@swimlane/ngx-datatable/release/assets/icons.css'
  ],
  providers: [SubjectService, CourseService, RoomService, GroupService, TableServiceProvider, HoursLectureService, TeacherService]


})
export class TableBaseComponent implements OnInit {
  @Input('isParameter') isParameter: boolean = false;
  @Input('dataTableValues') dataTableValues: string[] = [];
  @Input('dataTableSearch') dataTableSearch: string[] = [];
  @Input('tableTypeName') tableTypeName: string;
  @Output('tableRows') tableRows: any;
  @Output('dataTableNames') dataTableNames: any;
  @Output('title') title : string;
  @Output('rowActive') rowActive : string;
  @Output('selectDefinions') selectDefinions : any;
  @Output('hiddenParameters') hiddenParameters : boolean = true;
  @Output('addButtonName') addButtonName : string = ADD_NAME;
  @Output('relationParameterName') relationParameterName : string;
  @ViewChild('editTmpl') editTmpl: TemplateRef<any>;
  @ViewChild('indexTmpl') indexTmpl: TemplateRef<any>;
  @ViewChild('addDelTmpl') addDelTmpl: TemplateRef<any>;
  @ViewChild('searchTmpl') searchTmpl: TemplateRef<any>;
  private page = new TablePage();
  private rows = [];
  private cols = [];
  protected service : TableService<Table>;

  constructor(protected tableServiceProvider: TableServiceProvider, private isEditButton : boolean, private parentComponent: AdminPanelComponent){
    this.isEditButton = isEditButton;
  }

  ngOnInit() {
    this.service = this.tableServiceProvider.getServiceByName(this.tableTypeName);
    // this.service.setParameterTable(false);
    // this.list();
    this.getDefinions();
    this.dataTableNames = this.service.getDataTableParameters();
    this.title = this.service.getTitle();
    this.relationParameterName = this.service.getRelationParameterName();
    this.page.pageNumber = 0;
    this.page.size = 5;
    this.setPage({ offset: 0 });
  }

  getDefinions(){
    this.selectDefinions =
    this.service.getDefinions().subscribe(
      dataDefinions => {
        dataDefinions.subscribe(
          firstSubscribers => {
            console.log("Definions : " + firstSubscribers);
            this.selectDefinions = firstSubscribers;
          }
        );
      },
      err => {console.log("Error occured when get definions")}
    );
  }

  protected refreshTable(){
    console.log("call refresh");
    this.setPage({offset : this.page.pageNumber});
    this.service.refreshOtherTableIfNeeded(this.parentComponent);
    // this.dataTableValues = [];
    // this.list();
    // this.addButtonName = ADD_NAME;
  }

  add(){
    if(this.addButtonName === ADD_NAME){
      this.service.create(this.tableTypeName, this.dataTableValues).subscribe(
        data => {this.refreshTable()},
        err => {console.log("Error occured when add.")}
      );
    }else{
      this.service.update(this.dataTableValues, this.rowActive).subscribe(
        data => {this.refreshTable()},
        err => {console.log("Error occured when update.")}
      );
    }
  };

  remove(rowId, event){
    event.stopPropagation();
    this.service.remove(rowId).subscribe(
      data => {this.refreshTable()},
      err => {console.log("Error occured when remove.")}
    );
  };

  edit(row: any, event){
    event.stopPropagation();
    this.rowActive = row.id;
    this.dataTableValues = (<any>Object).values(row).splice(1);
    this.addButtonName = SAVE_NAME;
  };

  list(){
    this.service.list().subscribe(
      data => {
        let dataFromPage = data;
        console.log(data);
        this.tableRows = dataFromPage;
      },
      err => {console.log("Error occured when get all elements.")}
    );
  }

  filter(){
    this.setPage({ offset: 0 });
  }

  find(){
    this.service.find(this.page, this.dataTableValues).subscribe(
      data => {
        let dataFromPage = data.data;
        if(this.isParameter !== true && dataFromPage !== undefined){
          dataFromPage = this.service.transformValues(dataFromPage);
        }
        if(data.totalElements === 0){
            this.tableRows = [];
        }else{
          this.tableRows = dataFromPage;
        }
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
      tempCols.push({name:"#",width:"10", cellTemplate: this.indexTmpl});
    this.dataTableNames.forEach((element, index) => {
      if(element.type === 'Input'){
        tempCols.push({name:element.value, headerTemplate: this.searchTmpl});
      }else{
        tempCols.push({name:element.value});
      }
    });
    if(this.isEditButton){
      tempCols.push({name:"Edytuj",width:"80", cellTemplate: this.editTmpl});
      tempCols.push({name:"Usun",width:"80",cellTemplate: this.addDelTmpl});
    }else{
      tempCols.push({name:"Modyfikuj",width:"100",cellTemplate: this.addDelTmpl});
    }

    this.tableRows.forEach((elementData, indexData) => {
      let cellData = {};
      this.dataTableNames.forEach((elementDefinition, indexDefinition) => {
        cellData[elementDefinition.value.toLowerCase()] = elementData[elementDefinition.data];
      });
      if(this.isEditButton){
        cellData['edytuj'] = elementData;
        cellData['usun'] = elementData.id;
      }else{
        cellData['modyfikuj'] = elementData;
      }
      tempRows.push(cellData);
    });
    this.cols = tempCols;
    this.rows = tempRows;
  }

  setPage(pageInfo){
    this.page.pageNumber = pageInfo.offset;
    this.page.size = 5;
    this.find();
  }


}
