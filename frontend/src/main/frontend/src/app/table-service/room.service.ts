import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/zip';
import {TableService} from "../interface/table.service";
import {Room} from "../model/room.type";



@Injectable()
export class RoomService implements TableService<Room> {
  private type;

  constructor(private http: HttpClient) {
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

  getName(): string {
    return 'Room';
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
