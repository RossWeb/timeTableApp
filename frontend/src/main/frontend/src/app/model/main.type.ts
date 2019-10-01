import {Room} from "./room.type";
import {Subject} from "./subject.type";
import {Teacher} from "./teacher.type";
import {BaseResponse} from "./base.response.type";

export class Main extends BaseResponse {
  subject: Subject;
  room: Room;
  teacher: Teacher;
  lectureNumber: number;
  day: number;
}
