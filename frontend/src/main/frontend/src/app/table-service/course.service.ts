import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import {TableService} from "../interface/table.service";
import {Course} from "../model/course.type";
import {TablePage} from '../model/page.type';
import {PagedData} from '../model/paged-data.type';
import {SubjectService} from "../table-service/subject.service";


@Injectable()
export class CourseService extends TableService<Course> {
  // private type;

  constructor(protected http: HttpClient, private subjectService: SubjectService) {
    super(http);
    this.type = new Course();
  }


  create(name: string, dataTableValues: string[]) {
    return this.http.post('api/course/', this.type.getParams(dataTableValues),
      {responseType: 'text'});
  }

  createParameters(id: number) {
    return this.http.post('api/course/' + this.getParametersUrlIfNeeded()  + id, null,
      {responseType: 'text'});
  }

  remove(id: string){
    return this.http.delete('api/course/' + id,{responseType: 'text'})
  }

  update(dataTableValues: string[], id: string){
    return this.http.put('api/course/'+ this.getParametersUrlIfNeeded() + id, this.type.getParams(dataTableValues),
    {responseType: 'text'});
  }

  list() {
    return this.http.get<Course>('api/course/' + this.getParametersUrlIfNeeded());
  }

  // find(page: TablePage, dataTableValues: string[]){
  //   page.data =  this.type.getParams(dataTableValues);
  //   return this.http.post<PagedData<Course>>('api/course/find/' + this.getParametersUrlIfNeeded(), page);
  // }

  get(id: string): any {
    return  this.http.get<Course>('api/course/' + this.getParametersUrlIfNeeded() + id);
  }

  getName(): string {
    return 'Course';
  }

  getDataTableParameters(): string[] {
    return this.type.getDataTableParameters();
  }

  getTitle(): string {
    return this.type.getTitle();
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

  getRelationParameterName() : string {
    return this.type.getRelationParameterName();
  }

  transformValues(data) : any {
    for(let value of data) {
        value.subjectSet = value.subjectSet.length;
    }
    return data;
  }

}
