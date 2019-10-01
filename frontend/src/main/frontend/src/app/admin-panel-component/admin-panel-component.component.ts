import { Component,ViewChild, OnInit, ElementRef } from '@angular/core';


@Component({
  selector: 'app-admin-panel-component',
  templateUrl: './admin-panel-component.component.html',
  styleUrls: ['./admin-panel-component.component.css']
})
export class AdminPanelComponent implements OnInit {

  @ViewChild('groupTable') groupTable: ElementRef;
  @ViewChild('courseTable') courseTable: ElementRef;
  @ViewChild('teacherTable') teacherTable: ElementRef;


  private tableComponentArray = new Map();

  constructor() {

  }

  ngOnInit() {
    this.tableComponentArray.set('groupTable',this.groupTable);
    this.tableComponentArray.set('courseTable', this.courseTable);
    this.tableComponentArray.set('teacherTable', this.teacherTable);
  }

  refreshTableByName(nameTable : string){
    this.tableComponentArray.get(nameTable).getDefinions();

  }
}
