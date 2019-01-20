import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import {TableService} from "../interface/table.service";
import {HoursLecture} from "../model/hours.lecture.type";
import {TablePage} from '../model/page.type';
import {PagedData} from '../model/paged-data.type';



@Injectable()
export class HoursLectureService extends TableService<HoursLecture> {

  constructor(protected http: HttpClient) {
    super(http);
    this.type = new HoursLecture();
  }

}
