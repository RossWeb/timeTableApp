import {Table} from "../interface/table.type";

export class Teacher extends Table {
  id: number;
  name: string;
  surname: string;

  getType(): string{
    return 'Teacher';
  }

  getTitle(): string {
    return 'Zarządzanie wykładowcami';
  }

  getParams(dataTableValues: string[]): any {
    const params = {
      name : dataTableValues[0],
      surname: dataTableValues[1]
    };
    return params;
  }

  getDataTableParameters() : any {
    const params = [
      {
        value : 'Imie',
        data: 'name',
        type : 'Input'
      },
      {
        value : 'Nazwisko',
        data: 'surname',
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
    return 'Teacher';
  }

  getApiUrl() : string {
    return '/api/teacher/';
  }


}
