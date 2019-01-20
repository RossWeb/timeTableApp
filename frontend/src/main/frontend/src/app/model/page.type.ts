export class Page {
    //The number of elements in the page
    size: number = 0;
    //The total number of elements
    totalElements: number = 0;
    //The total number of pages
    totalPages: number = 0;
    //The current page number
    pageNumber: number = 0;

    messages = {
                emptyMessage: 'Brak danych do wyświetlenia',
                totalMessage: 'Liczba elementów'
               }

    constructor(){

    }
}

export class TimeTablePage extends Page {

  groupId: number;
  days: number;

}

export class TablePage extends Page {

  constructor(){
    super();
  }

  data : any;
}
