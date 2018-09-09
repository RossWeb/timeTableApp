import { Injectable } from '@angular/core';
import {Table} from "../interface/table.type";
import {TableService} from "../interface/table.service";
import {RoomService} from "../table-service/room.service";
import {GroupService} from "../table-service/group.service";
import {CourseService} from "../table-service/course.service";
import {SubjectService} from "../table-service/subject.service";
import {HoursLectureService} from "../table-service/hours.lecture.service";

@Injectable()
export class TableServiceProvider {

  private tableServiceArray: TableService<Table>[];

  constructor(private roomService : RoomService, private groupService : GroupService,
      private courseService: CourseService, private subjectService: SubjectService,
      private hoursLectureService: HoursLectureService
    ) {
      this.tableServiceArray = [
        roomService, groupService, courseService, subjectService, hoursLectureService
      ];
  }

  getServiceByName(name: string) : TableService<Table>{
    console.log(this.tableServiceArray[0]);
    console.log(this.tableServiceArray.filter(tableService => tableService.getName() === name));
    return this.tableServiceArray.filter(tableService => tableService.getName() === name)[0];
  }

}
