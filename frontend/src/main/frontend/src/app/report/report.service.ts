import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Report} from "../model/report.type";

@Injectable()
export class ReportService {

  private timeTableId: number = 0;

  constructor(private http: HttpClient) {
  }

  setTimeTableId(tableId: number) : void {
    this.timeTableId = tableId;
  }

  getTimeTableId() : number {
    return this.timeTableId;
  }

  get(): any {
    return  this.http.get<Report>('api/timetable/' + this.timeTableId + "/report");
  }
}
