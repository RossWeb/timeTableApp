import {Table} from "../interface/table.type";

export class HoursLecture extends Table {
  id: number;
  startLectureTime: string;
  position: number;

  getType(): string{
    return 'HoursLecture';
  }

  getTitle(): string {
    return 'Zarządzanie godzinami zajęć';
  }

  getParams(dataTableValues: string[]): any {
    const params = {
      startLectureTime : dataTableValues[0],
      position: dataTableValues[1]
    };
    return params;
  }

  getDataTableParameters() : any {
    const params = [
      {
        value : 'Poczatek',
        data: "startLectureTime",
        type : 'Input'
      },
      {
        value : 'Pozycja',
        data: 'position',
        type : 'Input'
      }
    ];
    return params;
  }

  getRelationParameterName() : string {
    return null;
  }

  getApiUrl() : string {
    return '/api/hoursLecture/';
  }

}
