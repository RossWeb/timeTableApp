import {TablePage} from '../model/page.type';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import {PagedData} from '../model/paged-data.type';
import { HttpClient } from '@angular/common/http';
import {Table} from "../interface/table.type";

export class TableService<T extends Table> {

  protected type : T;

  constructor(protected http: HttpClient) {
  }

  create(name: string, dataTableValues: string[]) {
    return this.http.post(this.type.getApiUrl(), this.type.getParams(dataTableValues),
      {responseType: 'text'});
  }


  remove(id: string){
    return this.http.delete(this.type.getApiUrl() + id,{responseType: 'text'})
  }

  update(dataTableValues: string[], id: string){
    return this.http.put(this.type.getApiUrl() + id, this.type.getParams(dataTableValues),
    {responseType: 'text'});
  }

  list() {
    return this.http.get<T>(this.type.getApiUrl());
  }

  find(page: TablePage, dataTableValues: string[]){
    page.data =  this.type.getParams(dataTableValues);
    return this.http.post<PagedData<T>>(this.type.getApiUrl() + 'find', page);
  }

  get(id: string): any {
    return  this.http.get<T>(this.type.getApiUrl() + id);
  }

  getName(): string {
    return this.type.getType();
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
