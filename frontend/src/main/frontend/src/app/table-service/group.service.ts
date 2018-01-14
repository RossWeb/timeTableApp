import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import {TableService} from "../interface/table.service";
import {CourseService} from "../table-service/course.service";
import {Group} from "../model/group.type";

@Injectable()
export class GroupService implements TableService<Group> {
  private type;

  constructor(private courseService: CourseService, private http: HttpClient) {
    this.type = new Group();
  }

  create(name: string, dataTableValues: string[]) {
    return this.http.post('api/group', this.type.getParams(dataTableValues),
      {responseType: 'text'});
  }

  remove(id: string){
    return this.http.delete('api/group/' + id,{responseType: 'text'})
  }

  update(dataTableValues: string[], id: string){
    return this.http.put('api/group/' + id, this.type.getParams(dataTableValues),
    {responseType: 'text'});
  }

  list() {
    return this.http.get<Group>('/api/group').map(res => {return this.getResponse(res)});
  }

  private getResponse(res: any): any{
    for (var i = 0; i < res.length; i++){
      if(res[i].course !== null)
      res[i].course = res[i].course.name;
    }
    return res;
  }

  getName(): string {
    return 'Group';
  }

  getDataTableParameters(): any {
    return this.type.getDataTableParameters();
  }

  getTitle(): string {
    return this.type.getTitle();
  }

  getDefinions(): any {
    return new Observable(observer => {
        observer.next(this.courseService.list()
          .map(
            data => {
              const params = {
                Kierunek : data
              };
              return params
            }
          ));
          observer.complete();
    });
  }

}
