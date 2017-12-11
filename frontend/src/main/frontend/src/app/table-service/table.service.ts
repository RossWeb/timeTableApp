import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {TableTypeProvider} from "./table.type.provider";
import {Table} from "../interface/table.type";



@Injectable()
export class TableService {

  constructor(private http: HttpClient, private tableTypeProvider: TableTypeProvider) { }

  create<T>(name: string, dataTable: string[]) {
      var tableType = this.tableTypeProvider.getTypeByName(name);
      this.http.post<T>('api/room', tableType.getParams(dataTable)).subscribe(data => {console.log(data);})
  }

  list<T>() : Promise<T>  {
    return this.http.get<T>('/api/room').toPromise();
  }

}
