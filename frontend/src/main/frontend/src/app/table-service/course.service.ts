import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import {TableService} from "../interface/table.service";
import {Course} from "../model/course.type";



@Injectable()
export class CourseService extends TableService<Course> {
  // private type;
  private isParameterTable : boolean;
  private primaryKey : number;

  constructor(private http: HttpClient) {
    super();
    this.type = new Course();
  }

  private getParametersUrlIfNeeded() : string {
    if(this.isParameterTable){
      return this.primaryKey + '/parameters/';
    }else{
      return '';
    }
  }

  setParameterTable(isParameterTable: boolean){
    this.isParameterTable = isParameterTable;
  }

  setPrimaryKey(primaryKey : number){
    this.primaryKey = primaryKey;
  }

  create(name: string, dataTableValues: string[]) {
    return this.http.post('api/course/' + this.getParametersUrlIfNeeded(), this.type.getParams(dataTableValues),
      {responseType: 'text'});
  }

  createParameters(id: number) {
    return this.http.post('api/course/' + this.getParametersUrlIfNeeded()  + id, null,
      {responseType: 'text'});
  }

  remove(id: string){
    return this.http.delete('api/course/' + this.getParametersUrlIfNeeded() + id,{responseType: 'text'})
  }

  update(dataTableValues: string[], id: string){
    return this.http.put('api/course/'+ this.getParametersUrlIfNeeded() + id, this.type.getParams(dataTableValues),
    {responseType: 'text'});
  }

  list() {
    return this.http.get<Course>('api/course/' + this.getParametersUrlIfNeeded());
  }

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
    return new Observable(observer => {});
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
