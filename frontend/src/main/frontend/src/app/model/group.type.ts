import {Table} from "../interface/table.type";

export class Group implements Table {
  id: number;
  name: string;

  getType(): string{
    return 'Group';
  }

  getTitle(): string {
    return 'Zarządzanie grupami';
  }

  getParams(dataTableValues: string[]): any {
    const params = {
      name : dataTableValues[0],
      courseId : dataTableValues[1]
    };
    return params;
  }

  getDataTableParameters() : any {
    const params = [
      {
        value : 'Nazwa',
        data: 'name',
        type : 'Input'
      },
      {
        value : 'Kierunek',
        data: 'course',
        type : 'Select'
      }
    ];
    return params;
  }

  getRelationParameterName() : string {
    return 'Course';
  }

}
