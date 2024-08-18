import { TestBed } from '@angular/core/testing';

import { SinFitService } from './sin-fit.service';

describe('SinFitService', () => {
  let service: SinFitService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SinFitService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
