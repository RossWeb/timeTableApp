import {TablePage} from '../model/page.type';

export abstract class TableService<T> {

  protected type : T;

  abstract create(name: string, dataTableValues: string[]): any;

  abstract update(dataTableValues: string[], id: string): any;

  abstract remove(id: string): any;

  abstract list() : any;

  abstract find(page: TablePage, dataTableValues: string[]) : any;

  abstract get(id: string): any;

  abstract getName(): string;

  abstract getDataTableParameters(): string[];

  abstract getTitle(): string;

  abstract getDefinions(): any;

  abstract getRelationParameterName() : string;

  abstract transformValues(data: any) : any;

}
