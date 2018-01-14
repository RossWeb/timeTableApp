import { Injectable } from '@angular/core';
import {Table} from "../interface/table.type";
import {Room} from "../model/room.type";
import {Group} from "../model/group.type";
import {Course} from "../model/course.type";
import {Subject} from "../model/subject.type";

@Injectable()
export class TableTypeProvider {

  private tableTypeArray: Table[];

  constructor() {
      this.tableTypeArray = [
        new Room(),
        new Group(),
        new Course(),
        new Subject()
      ];
  }

  getTypeByName(name: string) : Table{
    console.log(this.tableTypeArray[0].getType());
    console.log("equals" + this.tableTypeArray[0].getType() == name);
    console.log(this.tableTypeArray.filter(tableType => tableType.getType() === name));
    return this.tableTypeArray.filter(tableType => tableType.getType() === name)[0];
  }

}
