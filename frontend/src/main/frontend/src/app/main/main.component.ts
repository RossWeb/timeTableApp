import { Component, OnInit, Input, Output, ViewChild } from '@angular/core';
import {ReportService} from '../report/report.service';
import { ViewEncapsulation } from '@angular/core';

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
    providers: [ReportService],
    encapsulation: ViewEncapsulation.None
 })
export class MainComponent implements OnInit {

  private timeTableId: number;

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

  constructor(private reportService : ReportService) {
  }

  ngOnInit() {
    this.reportService.setTimeTableId(2);
  }
}
