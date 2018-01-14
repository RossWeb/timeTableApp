
export interface TableService<T> {


  create(name: string, dataTableValues: string[]): any;

  update(dataTableValues: string[], id: string): any;

  remove(id: string): any;

  list() : any;

  getName(): string;

  getDataTableParameters(): string[];

  getTitle(): string;

  getDefinions(): any;

}
