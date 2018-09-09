import { Injectable, Input, Output, EventEmitter } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {InitProcess} from '../model/initProcess.type';
import {TimeTablePage} from '../model/page.type';
import {BaseResponse} from "../model/base.response.type";
import {Observable} from 'rxjs/Rx';

@Injectable()
export class MainService {


  constructor(private http: HttpClient) {
  }



  initProcess(initProcess: InitProcess): any {
    return  this.http.post('api/timetable/init', initProcess);
  }

  checkStatus(timeTableId: number): Observable<BaseResponse> {
    return  this.http.get<BaseResponse>('api/timetable/' + timeTableId + "/status");
  }

  getGroup(): any {
    return  this.http.get('api/group');
  }

  public getResults(page: TimeTablePage, timeTableId: number): any  {
        return  this.http.post('api/timetable/' + timeTableId + "/result", page);
    }
}
