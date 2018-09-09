import {Table} from "../interface/table.type";

export class Subject implements Table {
  id: number;
  name: string;
  size: number;

  getType(): string{
    return 'Subject';
  }

  getTitle(): string {
    return 'Zarządzanie przedmiotami';
  }

  getParams(dataTableValues: string[]): any {
    const params = {
      name : dataTableValues[0],
      size: dataTableValues[1]
    };
    return params;
  }

  getDataTableParameters() : any {
    const params = [
      {
        value : 'Nazwa',
        data : 'name',
        type : 'Input'
      },
      {
        value : 'Liczba',
        data : 'size',
        type : 'Input'
      }

    ];
    return params;
  }

  getRelationParameterName() : string {
    return null;
  }

  getApiUrl() : string {
    return '/api/subject/';
  }

}
