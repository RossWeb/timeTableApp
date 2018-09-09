export interface Table {

  getType(): string;

  getTitle(): string;

  getParams(dataTableValues: string[]): any;

  getDataTableParameters() : string[];

  getRelationParameterName() : string;

  getApiUrl() : string; 

}
