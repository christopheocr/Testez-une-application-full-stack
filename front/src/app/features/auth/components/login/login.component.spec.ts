import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { SessionService } from 'src/app/services/session.service';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  const mockSessionInfo: SessionInformation = {
    id: 1,
    username: 'jdoe',
    firstName: 'John',
    lastName: 'Doe',
    admin: false,
    token: 'abc123',
    type: 'Bearer'
  };

  const mockAuthService = {
    login: jest.fn()
  };

  const mockSessionService = {
    logIn: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should have an invalid form initially', () => {
    expect(component.form.invalid).toBe(true);
  });

  it('should call authService.login and navigate on successful login', () => {
    component.form.setValue({ email: 'test@example.com', password: '12345' });
    mockAuthService.login.mockReturnValue(of(mockSessionInfo));

    component.submit();

    expect(mockAuthService.login).toHaveBeenCalledWith({ email: 'test@example.com', password: '12345' });
    expect(mockSessionService.logIn).toHaveBeenCalledWith(mockSessionInfo);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should set onError to true on login error', () => {
    component.form.setValue({ email: 'fail@example.com', password: 'wrong' });
    mockAuthService.login.mockReturnValue(throwError(() => new Error('Invalid')));

    component.submit();

    expect(component.onError).toBe(true);
  });
});
