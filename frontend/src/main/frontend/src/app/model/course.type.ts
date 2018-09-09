import {Table} from "../interface/table.type";

export class Course implements Table {
  id: number;
  name: string;

  getType(): string{
    return 'Course';
  }

  getTitle(): string {
    return 'Zarządzanie kierunkami';
  }

  getParams(dataTableValues: string[]): any {
    const params = {
      name : dataTableValues[0],
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
        value : 'Przedmioty',
        data: "subjectSet",
        type : 'Select'
      }
    ];
    return params;
  }

  getRelationParameterName() : string {
    return 'Subject';
  }

  getApiUrl() : string {
    return '/api/course/';
  }


}
