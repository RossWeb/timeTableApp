import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import {TableService} from "../interface/table.service";
import {Teacher} from "../model/teacher.type";
import {TablePage} from '../model/page.type';
import {PagedData} from '../model/paged-data.type';
import {SubjectService} from "../table-service/subject.service";


@Injectable()
export class TeacherService extends TableService<Teacher> {
  // private type;


  constructor(protected http: HttpClient, private subjectService: SubjectService) {
    super(http);
    this.type = new Teacher();
  }

  getDefinions(): any {
    return new Observable(observer => {
        observer.next(this.subjectService.list()
          .map(
            data => {
              const params = {
                Przedmioty : data
              };
              return params
            }
          ));
          observer.complete();
    });
  }

  createParameters(id: number) {
    return this.http.post('api/teacher/' + this.getParametersUrlIfNeeded()  + id, null,
      {responseType: 'text'});
  }

  transformValues(data) : any {
    for(let value of data) {
        value.subjectSet = value.subjectSet.length;
    }
    return data;
  }

}
