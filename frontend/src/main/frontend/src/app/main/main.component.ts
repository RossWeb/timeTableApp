import {Component, OnInit, ViewChild} from '@angular/core';


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
    ]
 })
export class MainComponent implements OnInit {

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

  constructor() {
  }

  ngOnInit() {
  }
}
