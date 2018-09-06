import {Table} from "../interface/table.type";

export class Subject implements Table {
  id: number;
  name: string;

  getType(): string{
    return 'Subject';
  }

  getTitle(): string {
    return 'Zarządzanie przedmiotami';
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
        data : 'name',
        type : 'Input'
      }
    ];
    return params;
  }

  getRelationParameterName() : string {
    return null;
  }


}
