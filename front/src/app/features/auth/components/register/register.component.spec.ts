import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  const mockAuthService = {
    register: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should have an invalid form initially', () => {
    expect(component.form.invalid).toBe(true);
  });

  it('should call authService.register and navigate on success', () => {
    component.form.setValue({
      email: 'john@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'securepassword'
    });

    mockAuthService.register.mockReturnValue(of(void 0));

    component.submit();

    const expectedPayload: RegisterRequest = {
      email: 'john@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'securepassword'
    };

    expect(mockAuthService.register).toHaveBeenCalledWith(expectedPayload);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should set onError to true on registration failure', () => {
    component.form.setValue({
      email: 'fail@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'weak'
    });

    mockAuthService.register.mockReturnValue(throwError(() => new Error('Error')));

    component.submit();

    expect(component.onError).toBe(true);
  });
});
