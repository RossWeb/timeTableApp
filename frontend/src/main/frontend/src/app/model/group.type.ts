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
        type : 'Input'
      },
      {
        value : 'Kierunek',
        type : 'Select'
      }
    ];
    return params;
  }


}
