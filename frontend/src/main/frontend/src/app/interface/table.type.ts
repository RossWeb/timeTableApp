export abstract class Table {

  abstract getType(): string;

  abstract getTitle(): string;

  abstract getParams(dataTableValues: string[]): any;

  getValuesSearch(dataTableValues: string[]): any {
    var dataForBackend = [];
    var dataTableParameters = this.getDataTableParameters();
    dataTableParameters.forEach((elementData) => {
      if(dataTableValues[elementData.value] != null){
        dataForBackend[elementData["data"]] = dataTableValues[elementData.value]
      }
    });
    return {...dataForBackend};
  }

  abstract getDataTableParameters() : any;

  abstract getRelationParameterName() : string;

  abstract getApiUrl() : string;

}
