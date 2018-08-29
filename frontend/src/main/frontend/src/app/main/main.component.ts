import { Component, OnInit, Input, Output,Injectable, ViewChild } from '@angular/core';
import {ReportService} from '../report/report.service';
import {InitProcess} from '../model/initProcess.type';
import {Observable} from 'rxjs/Rx';
import { ViewEncapsulation } from '@angular/core';
import {MainService} from './main.service';
import { FormGroup, FormControl,FormBuilder, Validators } from '@angular/forms';
import {ReportComponent} from '../report/report.component';

@Injectable()
export class DateValidator {

  constructor() {
  }

  static date(c: FormControl) {
    const dateRegEx = new RegExp(/^\d{4}\.\d{1,2}\.\d{1,2}$/);
    return dateRegEx.test(c.value) ? null : true;
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
  private initProcess : InitProcess;

  // private mapValue = new Map();

  rows = [
    { name: 'Austin', gender: 'Male', company: 'Swimlane' },
    { name: 'Dany', gender: 'Male', company: 'KFC' },
    { name: 'Molly', gender: 'Female', company: 'Burger King' },
  ];
  columns = [
    { prop: 'name' },
    { name: 'Gender' },
    { name: 'Company' }
  ];


  // form : FormGroup;

  constructor(private reportService : ReportService, private mainService : MainService,
              private formBuilder : FormBuilder) {
  }


  ngOnInit() {
    // this.mapValue.set("weeksPerSemestrForm",false);
    // this.mapValue.set("daysPerWeek",false);
    // this.mapValue.set("lecturePerDay",false);
    // this.mapValue.set("startedDate",false);
    // this.mapValue.set("startedDay",false);

    this.form = this.formBuilder.group({
      weeksPerSemester: [null, [Validators.required]],
      daysPerWeek: [null, [Validators.required]],
      numberPerDay: [null, [Validators.required]],
      startedDate: [null, [Validators.required, DateValidator.date]],
      startedDay: [null, [Validators.required]],
      mutationValue: [null, [Validators.required]],
      populationSize: [null, [Validators.required]]
    });
    this.form.get('numberPerDay').value = (4);
    this.form.get('daysPerWeek').value = (5);
    this.form.get('weeksPerSemester').value = (2);
    this.form.get('mutationValue').value = (0.15);
    this.form.get('populationSize').value = (100);
    this.form.get('startedDay').value = (1);
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
    // this.form.valueChanges.subscribe(
    //    (value, keys) => {
    //      console.log(value, keys)
    //    }
    // );
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
    // Observable.interval(1500)
    //     .switchMap(() => this.http.get(this.apiEndpoint + "Definition/" + type + "/Progress/" + requestId))
    //     .map((data) => data.json().Data)
    //     .takeWhile((data) => data.InProgress)
    //     .subscribe(
    //     (data) => {
    //         ...
    //     },
    //     error => this.handleError(error));

    Observable.interval(5000)
              .startWith(0)
              .switchMap(() => this.mainService.checkStatus(this.reportService.getTimeTableId()))

      .takeWhile(data => data.status === "PENDING")
      .subscribe(res => console.log(res), error => console.log("error when status check "+error), complete=> this.reportComponent.initReport()););
  }

  init() {
    this.initProcess = new InitProcess(this.form);
    this.mainService.initProcess(this.initProcess).subscribe(
      data => {
        this.reportService.setTimeTableId(data.timeTableId);
        this.checkStatus();
      },
      err => {console.log("Error when init process.")}
    );
  }
}
