import { TestBed } from '@angular/core/testing';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  const mockUser: SessionInformation = {
    token: 'abc123',
    type: 'Bearer',
    id: 1,
    username: 'johndoe',
    firstName: 'John',
    lastName: 'Doe',
    admin: false
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should emit false by default', (done) => {
    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(false);
      done();
    });
  });

  it('should emit true after login', (done) => {
    service.logIn(mockUser);
    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(true);
      done();
    });
  });

  it('should emit false after logout', (done) => {
    service.logIn(mockUser);
    service.logOut();
    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(false);
      done();
    });
  });

  it('should store sessionInformation after login', () => {
    service.logIn(mockUser);
    expect(service.sessionInformation).toEqual(mockUser);
    expect(service.isLogged).toBe(true);
  });

  it('should clear sessionInformation after logout', () => {
    service.logIn(mockUser);
    service.logOut();
    expect(service.sessionInformation).toBeUndefined();
    expect(service.isLogged).toBe(false);
  });
});
