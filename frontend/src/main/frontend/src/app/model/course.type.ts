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
        type : 'Input'
      },
      {
        value : 'Przedmioty',
        type : 'Input'
      }
    ];
    return params;
  }

  getRelationParameterName() : string {
    return 'Subject';
  }


}
