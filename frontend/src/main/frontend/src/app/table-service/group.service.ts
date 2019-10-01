import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import {TableService} from "../interface/table.service";
import {CourseService} from "../table-service/course.service";
import {Group} from "../model/group.type";
import {TablePage} from '../model/page.type';
import {PagedData} from '../model/paged-data.type';

@Injectable()
export class GroupService extends TableService<Group> {

  constructor(private courseService: CourseService, protected http: HttpClient) {
    super(http);
    this.type = new Group();
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

  get(id: string): any {
    return  this.http.get<Group>('api/group/' + id);
  }

  getDataTableParameters(): any {
    return this.type.getDataTableParameters();
  }

  getTitle(): string {
    return this.type.getTitle();
  }

  getRelationParameterName() : string {
    return this.type.getRelationParameterName();
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

  transformValues(data) : any {
    for(let value of data) {
      value.course = value.course.name;
    }
    return data;
  }

}
