import { NgTimeTablePage } from './app.po';

describe('ng-time-table App', () => {
  let page: NgTimeTablePage;

  beforeEach(() => {
    page = new NgTimeTablePage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
