import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MeComponent } from './me.component';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { of } from 'rxjs';
import { SessionService } from '../../services/session.service';
import { UserService } from '../../services/user.service';
import { User } from '../../interfaces/user.interface';
import { SessionInformation } from '../../interfaces/sessionInformation.interface';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockUser: User = {
    id: 123,
    firstName: 'John',
    lastName: 'Doe',
    email: 'john@example.com',
    admin: false,
    password: '',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockSessionInformation: SessionInformation = {
    id: 123,
    username: 'jdoe',
    firstName: 'John',
    lastName: 'Doe',
    admin: false,
    token: 'token123',
    type: 'Bearer'
  };

  const mockSessionService = {
    sessionInformation: mockSessionInformation,
    logOut: jest.fn()
  };

  const mockUserService = {
    getById: jest.fn().mockReturnValue(of(mockUser)),
    delete: jest.fn().mockReturnValue(of({}))
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  const mockMatSnackBar = {
    open: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockMatSnackBar }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA] // Ignore les balises Angular Material dans le HTML
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should load user on init using getById', () => {
    fixture.detectChanges();
    expect(mockUserService.getById).toHaveBeenCalledWith(mockSessionInformation.id.toString());
    expect(component.user).toEqual(mockUser);
  });

  it('should call window.history.back() when back() is called', () => {
    const historySpy = jest.spyOn(window.history, 'back').mockImplementation();
    component.back();
    expect(historySpy).toHaveBeenCalled();
  });

  it('should delete account, show snackbar, logout, and navigate on delete()', () => {
    fixture.detectChanges();
    component.delete();

    expect(mockUserService.delete).toHaveBeenCalledWith(mockSessionInformation.id.toString());
    expect(mockMatSnackBar.open).toHaveBeenCalledWith(
      'Your account has been deleted !',
      'Close',
      { duration: 3000 }
    );
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });
});
