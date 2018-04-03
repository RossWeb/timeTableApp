
export abstract TableService<T> {

  private type : T;

  create(name: string, dataTableValues: string[]): any;

  update(dataTableValues: string[], id: string): any;

  remove(id: string): any;

  list() : any;

  get(id: string): any;

  getName(): string;

  getDataTableParameters(): string[];

  getTitle(): string;

  getDefinions(): any;

  getRelationParameterName() : string;

  transformValues() : any;

}
