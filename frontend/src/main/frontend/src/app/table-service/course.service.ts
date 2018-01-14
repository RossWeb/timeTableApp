import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/zip';
import {TableService} from "../interface/table.service";
import {Course} from "../model/course.type";



@Injectable()
export class CourseService implements TableService<Course> {
  private type;

  constructor(private http: HttpClient) {
    this.type = new Course();
  }

  create(name: string, dataTableValues: string[]) {
    return this.http.post('api/course', this.type.getParams(dataTableValues),
      {responseType: 'text'});
  }

  remove(id: string){
    return this.http.delete('api/course/' + id,{responseType: 'text'})
  }

  update(dataTableValues: string[], id: string){
    return this.http.put('api/course/' + id, this.type.getParams(dataTableValues),
    {responseType: 'text'});
  }

  list() {
    return this.http.get<Course>('api/course');
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
    return Observable.zip();
  }

}
