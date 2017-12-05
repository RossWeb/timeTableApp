import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {RoomResponse} from "../interface/room.type";



@Injectable()
export class TableService {

  constructor(private http: HttpClient) { }

  create(name: string, number: string) {
      const params = {
        name : name,
        number : number
      }
      this.http.post<RoomResponse>('api/room', params).subscribe(data => {console.log(data);})
  }

  list() : Promise<RoomResponse>  {
    return this.http.get<RoomResponse>('/api/room').toPromise();
  }

}
