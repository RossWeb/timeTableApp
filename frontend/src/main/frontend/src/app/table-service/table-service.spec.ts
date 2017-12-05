import { TestBed, inject } from '@angular/core/testing';

import { TableServiceService } from './table.service';

describe('TableServiceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TableServiceService]
    });
  });

  it('should ...', inject([TableServiceService], (service: TableServiceService) => {
    expect(service).toBeTruthy();
  }));
});
