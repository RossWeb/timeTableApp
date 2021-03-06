import {TablePage} from '../model/page.type';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import {PagedData} from '../model/paged-data.type';
import { HttpClient } from '@angular/common/http';
import {Table} from "../interface/table.type";
import { AdminPanelComponent } from '../admin-panel-component/admin-panel-component.component';

export class TableService<T extends Table> {

  protected type : T;
  protected isParameterTable : boolean;
  protected primaryKey : number;

  constructor(protected http: HttpClient) {
  }

  setParameterTable(isParameterTable: boolean){
    this.isParameterTable = isParameterTable;
  }

  setPrimaryKey(primaryKey : number){
    this.primaryKey = primaryKey;
  }

  getParametersUrlIfNeeded() : string {
    if(this.isParameterTable && this.primaryKey !== undefined){
      return this.primaryKey + '/parameters/';
    }else{
      return '';
    }
  }

  create(name: string, dataTableValues: string[]) {
    return this.http.post(this.type.getApiUrl(), this.type.getParams(dataTableValues),
      {responseType: 'text'});
  }

  createParameters(id: number) {
    return this.http.post(this.type.getApiUrl() + this.getParametersUrlIfNeeded()  + id, null,
      {responseType: 'text'});
  }

  removeParameters(id: number) {
    return this.http.delete(this.type.getApiUrl() + this.getParametersUrlIfNeeded()  + id, 
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

  find(page: TablePage, dataTableValues: string[]): any{
    page.data = this.type.getValuesSearch(dataTableValues);
    return this.http.post<PagedData<T>>(this.type.getApiUrl() + 'find/' +  this.getParametersUrlIfNeeded(), page);
  }

  get(id: string): any {
    return  this.http.get<T>(this.type.getApiUrl() + id);
  }

  getName(): string {
    return this.type.getType();
  }

  refreshOtherTableIfNeeded(parent: AdminPanelComponent): void {
    //specify implementation on concrete class
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
