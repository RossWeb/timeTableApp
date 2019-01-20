import {Table} from "../interface/table.type";

export class Room extends Table {
  id: number;
  name: string;
  number: string;

  getType(): string{
    return 'Room';
  }

  getTitle(): string {
    return 'Zarządzanie salami';
  }

  getParams(dataTableValues: string[]): any {
    const params = {
      name : dataTableValues[0],
      number : dataTableValues[1]
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
        value : 'Number',
        data : 'number',
        type : 'Input'
      }
    ];
    return params;
  }

  getRelationParameterName() : string {
    return null;
  }

  getApiUrl() : string {
    return '/api/room/';
  }

}
