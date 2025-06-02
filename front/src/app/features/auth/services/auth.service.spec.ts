import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should call register with correct URL and payload', () => {
    const request: RegisterRequest = {
      email: 'test@test.com',
      password: '123456',
      firstName: 'John',
      lastName: 'Doe'
    };

    service.register(request).subscribe();

    const req = httpMock.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(request);
    req.flush({});
  });

  it('should call login and return session information', () => {
    const request: LoginRequest = { email: 'test@test.com', password: '123456' };
    const sessionInfo: SessionInformation = {
      id: 1,
      username: 'user',
      firstName: 'Test',
      lastName: 'User',
      admin: true,
      token: 'fake-jwt-token',
      type: 'Bearer'
    };

    service.login(request).subscribe((res) => {
      expect(res).toEqual(sessionInfo);
    });

    const req = httpMock.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(request);
    req.flush(sessionInfo);
  });
});
