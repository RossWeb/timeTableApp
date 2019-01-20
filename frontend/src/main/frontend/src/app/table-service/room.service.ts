import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import {TableService} from "../interface/table.service";
import {Room} from "../model/room.type";
import {TablePage} from '../model/page.type';
import {PagedData} from '../model/paged-data.type';



@Injectable()
export class RoomService extends TableService<Room> {
  // private type;

  constructor(protected http: HttpClient) {
    super(http);
    this.type = new Room();

  }

  create(name: string, dataTableValues: string[]) {
    return this.http.post('api/room', this.type.getParams(dataTableValues),
      {responseType: 'text'});
  }

  remove(id: string){
    return this.http.delete('api/room/' + id,{responseType: 'text'})
  }

  update(dataTableValues: string[], id: string){
    return this.http.put('api/room/' + id, this.type.getParams(dataTableValues),
    {responseType: 'text'});
  }

  list() {
    return this.http.get<Room>('/api/room');
  }

  // find(page: TablePage, dataTableValues: string[]){
  //   page.data =  this.type.getParams(dataTableValues);
  //
  //   return this.http.post<PagedData<Room>>('api/room/find', page);
  // }

  getName(): string {
    return 'Room';
  }

  get(id: string): any {
    return  this.http.get<Room>('api/room/' + id);
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
