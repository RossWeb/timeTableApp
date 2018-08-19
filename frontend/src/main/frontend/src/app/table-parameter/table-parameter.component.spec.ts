import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TableParameterComponent } from './table-parameter.component';

describe('TableParameterComponent', () => {
  let component: TableParameterComponent;
  let fixture: ComponentFixture<TableParameterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TableParameterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TableParameterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
