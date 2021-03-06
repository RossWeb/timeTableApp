import {Table} from "../interface/table.type";

export class Course extends Table {
  id: number;
  name: string;
  subjectSet: any;

  getType(): string{
    return 'Course';
  }

  getTitle(): string {
    return 'Zarządzanie kierunkami';
  }

  getParams(dataTableValues: string[]): any {
    const params = {
      name : dataTableValues[0],
      subject : dataTableValues[1]
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
    return 'Course';
  }

  getApiUrl() : string {
    return '/api/course/';
  }


}
