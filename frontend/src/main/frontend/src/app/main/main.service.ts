import { Injectable, Input, Output, EventEmitter } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {InitProcess} from '../model/initProcess.type';

@Injectable()
export class MainService {


  constructor(private http: HttpClient) {
  }



  initProcess(initProcess: InitProcess): any {
    return  this.http.post('api/timetable/init', initProcess);
  }

  checkStatus(timeTableId: number): any {
    return  this.http.get('api/timetable/' + timeTableId + "/status");
  }

  getGroup(): any {
    return  this.http.get('api/group');
  }

  public getResults(page: TimeTablePage, timeTableId: number): any  {
        return  this.http.post('api/timetable/' + timeTableId + "/result", page);
    }
}
