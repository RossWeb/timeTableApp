import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import {TableService} from "../interface/table.service";
import {Subject} from "../model/subject.type";
import {TablePage} from '../model/page.type';
import {PagedData} from '../model/paged-data.type';



@Injectable()
export class SubjectService extends TableService<Subject> {
  // private type;

  constructor(protected http: HttpClient) {
    super(http);
    this.type = new Subject();
  }

  create(name: string, dataTableValues: string[]) {
    return this.http.post('api/subject', this.type.getParams(dataTableValues),
      {responseType: 'text'});
  }

  remove(id: string){
    return this.http.delete('api/subject/' + id,{responseType: 'text'})
  }

  update(dataTableValues: string[], id: string){
    return this.http.put('api/subject/' + id, this.type.getParams(dataTableValues),
    {responseType: 'text'});
  }

  list() {
    return this.http.get<Subject>('api/subject');
  }

  // find(page: TablePage, dataTableValues: string[]){
  //   page.data =  this.type.getParams(dataTableValues);
  //   return this.http.post<PagedData<Subject>>('api/subject/find', page);
  // }

  getName(): string {
    return 'Subject';
  }

  get(id: string): any {
    return  this.http.get<Subject>('api/subject/' + id);
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
    return data;
  }

}
