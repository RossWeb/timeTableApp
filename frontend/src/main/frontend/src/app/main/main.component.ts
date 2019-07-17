import { Component, OnInit, Input, Output,Injectable, ViewChild, TemplateRef  } from '@angular/core';
declare var $: any;
import {ReportService} from '../report/report.service';
import {InitProcess} from '../model/initProcess.type';
import {TimeTable} from '../model/timeTable.type';
import {Observable} from 'rxjs/Rx';
import { ViewEncapsulation } from '@angular/core';
import {MainService} from './main.service';
import {HoursLectureService} from '../table-service/hours.lecture.service';
import { FormGroup, FormControl,FormBuilder, Validators } from '@angular/forms';
import {ReportComponent} from '../report/report.component';
import {PagedData} from '../model/paged-data.type';
import {TimeTablePage} from '../model/page.type';
import {TablePage} from '../model/page.type';
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
    providers: [ReportService, MainService, HoursLectureService],
    encapsulation: ViewEncapsulation.None
 })
export class MainComponent implements OnInit {

  @Output('weeksPerSemestrData') @Input('weeksPerSemestrData') weeksPerSemestr = 2;
  // private timeTableId: number;
  private errorGlobalHidden: boolean = true;
  @Output("errorGlobalText") errorGlobalText: String = "";
  @Output("errorSpecificText") errorSpecificText: String = "Przeładuj stronę i spróbuj jeszcze raz";
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
  private lectureHour = 0;

  constructor(private reportService : ReportService, private mainService : MainService,
              private hoursLectureService : HoursLectureService,
              private formBuilder : FormBuilder) {
    this.page.pageNumber = 0;
    this.page.size = 0;
    this.reportService.setTimeTableId(1);
    this.groupId = 1;
    this.lecturePerDay = 5;
    this.daysPerWeek = 4;
    this.startedDay = 2;
    this.lectureHour = 90;
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
      lectureHour: [null, [Validators.required]],
      populationSize: [null, [Validators.required]]
    });
    this.form.get('numberPerDay').value = (this.lecturePerDay);
    this.form.get('daysPerWeek').value = (this.daysPerWeek);
    this.form.get('weeksPerSemester').value = (5);
    this.form.get('mutationValue').value = (0.15);
    this.form.get('populationSize').value = (100);
    this.form.get('startedDay').value = (this.startedDay);
    this.form.get('startedDate').value = ('2018-10-02');
    this.form.get('lectureHour').value = (this.lectureHour);
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

  mapRowsAndCols(data: Main[], hoursLecture: any){
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
      tempRow['godziny'] = hoursLecture.get(i+1);
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

  getHoursLecture(pagedData: any){
    let  hoursLecturePage = new TablePage();
    hoursLecturePage.pageNumber = 0;
    hoursLecturePage.size = this.lecturePerDay;
    this.hoursLectureService.find(hoursLecturePage, new Array()).subscribe(
      hoursLecture => {
        let hoursLectureMap = new Map();
        hoursLecture.data.forEach((hour) => {
          let timeTokens = hour.startLectureTime.split(':');
          let angularHour = new Date(1970,0,1, Number(timeTokens[0]), Number(timeTokens[1]),0);
          let angularHourText = angularHour.getHours() + ':' + angularHour.getMinutes();
          angularHourText += ' - ';
          angularHour.setMinutes(angularHour.getMinutes() + this.lectureHour);
          angularHourText += angularHour.getHours() + ':' + angularHour.getMinutes();
          hoursLectureMap.set(hour.position, angularHourText);
        });
        this.mapRowsAndCols(pagedData.data, hoursLectureMap);
      },
      err => {this.showModalError("Error when get hours lecture for timetable.")}
    );
  }

  showModalError(errorMsg: string){
    this.errorSpecificText = errorMsg;
    $('#errorModal').modal("show");
    $('#generateTimeTableModal').modal("hide");
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
        this.getHoursLecture(pagedData);
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
      .subscribe(res => {

        if(res.status === "ERROR"){
            this.showModalError("error when generating timetable");
        }else {
          this.reportComponent.initReport();
          this.initDataTable();

        }
      }, error => this.showModalError("error when status check "+error),
      ()=>{$('#generateTimeTableModal').modal("hide");}    );
  }

  init() {
    this.initProcess = new InitProcess(this.form);
    this.lecturePerDay = this.initProcess.numberPerDay;
    this.daysPerWeek = this.initProcess.daysPerWeek;
    this.lectureHour = this.form.get('lectureHour').value;
    $('#generateTimeTableModal').modal({ backdrop: 'static',
    keyboard: false});
    this.mainService.initProcess(this.initProcess).subscribe(
      data => {
        this.reportService.setTimeTableId(data.timeTableId);
        this.checkStatus();
      },
      err => {this.showModalError("Error when init process.")}

    );
  }
}
