import { Component, OnInit, Input, Output,Injectable, ViewChild, TemplateRef  } from '@angular/core';
import {ReportService} from '../report/report.service';
import {InitProcess} from '../model/initProcess.type';
import {TimeTable} from '../model/timeTable.type';
import {Observable} from 'rxjs/Rx';
import { ViewEncapsulation } from '@angular/core';
import {MainService} from './main.service';
import { FormGroup, FormControl,FormBuilder, Validators } from '@angular/forms';
import {ReportComponent} from '../report/report.component';
import {PagedData} from '../model/paged-data.type';
import {TimeTablePage} from '../model/page.type';
import {Main} from '../model/main.type';

@Injectable()
export class DateValidator {

  constructor() {
  }

  static date(c: FormControl) {
    const dateRegEx = new RegExp(/^\d{4}\.\d{1,2}\.\d{1,2}$/);
    return dateRegEx.test(c.value) ? null : true;
  }
}

@Injectable()
export class StartedDateValidator {

  constructor() {
  }

  static startedDateValidate(c: FormControl) {
    let sum = 0;
    if(c.touched){
      sum = 7 - c.parent.get('daysPerWeek').value - c.value;
    }
    return sum < 1 ? null : true;
  }
}


/**
 * @title Data table with sorting, pagination, and filtering.
 */
 @Component({
   selector: 'app-main',
   templateUrl: './main.component.html',
   styleUrls: ['./main.component.css',
   '../../../node_modules/@swimlane/ngx-datatable/release/index.css',
   '../../../node_modules/@swimlane/ngx-datatable/release/themes/material.css',
   '../../../node_modules/@swimlane/ngx-datatable/release/assets/icons.css'
    ],
    providers: [ReportService, MainService],
    encapsulation: ViewEncapsulation.None
 })
export class MainComponent implements OnInit {

  @Output('weeksPerSemestrData') @Input('weeksPerSemestrData') weeksPerSemestr = 2;
  // private timeTableId: number;
  private errorGlobalHidden: boolean = true;
  @Output("errorGlobalText") errorGlobalText: String = "";
  @ViewChild('form') form;
  @ViewChild(ReportComponent) reportComponent;
  @ViewChild('editTmpl') editTmpl: TemplateRef<any>;
  private initProcess : InitProcess;
  private lecturePerDay: number;
  private daysPerWeek: number;
  private startedDay: number;
  private groupId: number;
  private page = new TimeTablePage();
  private rows = [];
  private cols = [];
  private dayMap = new Map();
  private groups = [];


  constructor(private reportService : ReportService, private mainService : MainService,
              private formBuilder : FormBuilder) {
    this.page.pageNumber = 0;
    this.page.size = 0;
    this.reportService.setTimeTableId(1);
    this.groupId = 1;
    this.lecturePerDay = 5;
    this.daysPerWeek = 4;
    this.startedDay = 2;
    this.dayMap.set(1, 'Poniedziałek');
    this.dayMap.set(2, 'Wtorek');
    this.dayMap.set(3, 'Sroda');
    this.dayMap.set(4, 'Czwartek');
    this.dayMap.set(5, 'Piatek');
    this.dayMap.set(6, 'Sobota');
    this.dayMap.set(7, 'Niedziela');

  }


  ngOnInit() {

    this.form = this.formBuilder.group({
      weeksPerSemester: [null, [Validators.required]],
      daysPerWeek: [null, [Validators.required]],
      numberPerDay: [null, [Validators.required]],
      startedDate: [null, [Validators.required, DateValidator.date]],
      startedDay: [null, [Validators.required, StartedDateValidator.startedDateValidate]],
      mutationValue: [null, [Validators.required]],
      populationSize: [null, [Validators.required]]
    });
    this.form.get('numberPerDay').value = (this.lecturePerDay);
    this.form.get('daysPerWeek').value = (this.daysPerWeek);
    this.form.get('weeksPerSemester').value = (5);
    this.form.get('mutationValue').value = (0.15);
    this.form.get('populationSize').value = (100);
    this.form.get('startedDay').value = (this.startedDay);
    this.form.get('startedDate').value = ('2018-10-02');
    this.form.statusChanges.subscribe(
      result => {
        if(result === 'VALID'){
          this.errorGlobalHidden = true;
        }else{
          this.errorGlobalHidden = false;
          this.errorGlobalText = "Popraw formularz"
        }
      }
    );
    this.initDataTable();
    // this.form.valueChanges.subscribe(
    //    (value, keys) => {
    //      console.log(value, keys)
    //    }
    // );
  }

  mapRowsAndCols(data: Main[]){
    let tempCols = [];
    let tempRows = [];
    let endPosition = this.startedDay + this.daysPerWeek;
    tempCols.push({name:'Godziny'});

    for(var i = this.startedDay; i < endPosition ; i++){
      tempCols.push({name:this.dayMap.get(i), cellTemplate: this.editTmpl});
    }


    let startPosition = this.startedDay;
    let tempRow = {};
    let tempDataRows = [];
    let endData = data.length;
    let lectureStartPosition = 0;
    let lectureEndPosition = this.lecturePerDay -1;

    for(var i = 0; i < this.lecturePerDay; i++){
      tempRow = {};
      tempRow['godziny'] = i;
      tempDataRows[i] = tempRow;
    }

    data.forEach((value, index) => {
        let cellText = {subject: "", room: ""};
        let actualLecture = value.lectureNumber;
        if(value.subject !== null){
            cellText.subject = value.subject.name;
        }
        if(value.room !== null){
          cellText.room = 'Sala ' + value.room.name + ' numer: '+  value.room.number;
        }

        if(value.lectureNumber > this.lecturePerDay){
          actualLecture = (this.lecturePerDay ) -  (this.lecturePerDay * value.day -  value.lectureNumber);
        }

        if( (actualLecture === 0 || value.lectureNumber % this.lecturePerDay === 0 ) && value.subject !== null){
          tempDataRows[lectureStartPosition][this.dayMap.get(startPosition).toLowerCase()] = cellText;
        }else if (actualLecture !== 0 && actualLecture % lectureStartPosition == 0){
          tempDataRows[lectureStartPosition][this.dayMap.get(startPosition).toLowerCase()] = cellText;
        }
        // tempRow[this.dayMap.get(startPosition).toLowerCase()] = cellText;

        if(lectureStartPosition == lectureEndPosition){
          lectureStartPosition = 0;
          startPosition++;
        }else{
            lectureStartPosition++;
        }

        if(startPosition == endPosition){
          startPosition = this.startedDay;
          // tempRows.push(tempRow);
        }
        //
        // if(endData == index + 1){
        //   tempRows.push(tempRow);
        // }


    });

    this.cols = tempCols;
    this.rows = tempDataRows;
  }

  initDataTable(){
    this.groups = [];
    this.mainService.getGroup().subscribe(
      data => {
        data.forEach((value, index) => {
          this.groups.push({id: value.id, name: value.name});
        });
      },
      err => {console.log("Error when get group.")}
    );
    this.setPage({ offset: 0 });
  }

  groupSelect(id: number){
    console.log(id);
    this.groupId = id;
    this.setPage({ offset: 0 });
  }

  setPage(pageInfo){
    this.page.pageNumber = pageInfo.offset;
    this.page.size = this.lecturePerDay;
    this.page.days = this.daysPerWeek;
    this.page.groupId = this.groupId;
    this.mainService.getResults(this.page, this.reportService.getTimeTableId()).subscribe(pagedData => {
      this.page.totalElements = pagedData.totalElements;
      // this.page.totalPages = pagedData.totalPages;
      // this.page.size = this.lecturePerDay;
      if(pagedData != null && pagedData.totalElements != 0){
        this.mapRowsAndCols(pagedData.data);
      }
      // this.rows = pagedData.data;
    });
  }

  isFieldValid(field: string) {
    return !this.form.get(field).valid && this.form.get(field).touched;

  }

  displayFieldCss(field: string) {
    return {
      'has-error': this.isFieldValid(field),
      'has-feedback': this.isFieldValid(field)
    };
  }

  checkStatus(){
    Observable.interval(5000)
              .startWith(0)
              .switchMap(() => this.mainService.checkStatus(this.reportService.getTimeTableId()))

      .takeWhile(data => data.status === "PENDING")
      .subscribe(res => console.log(res), error => console.log("error when status check "+error), ()=>
      {
        this.reportComponent.initReport();
        this.initDataTable();
      });
  }

  init() {
    this.initProcess = new InitProcess(this.form);
    this.lecturePerDay = this.initProcess.numberPerDay;
    this.daysPerWeek = this.initProcess.daysPerWeek;
    this.mainService.initProcess(this.initProcess).subscribe(
      data => {
        this.reportService.setTimeTableId(data.timeTableId);
        this.checkStatus();
      },
      err => {console.log("Error when init process.")}
    );
  }
}
