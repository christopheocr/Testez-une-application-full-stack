import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { AuthService } from './features/auth/services/auth.service';
import { Router } from '@angular/router';
import { SessionService } from './services/session.service';
import { of } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;

  const mockSessionService = {
    $isLogged: jest.fn().mockReturnValue(of(true)),
    logOut: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RouterTestingModule, MatToolbarModule],
      declarations: [AppComponent],
      providers: [
        { provide: AuthService, useValue: {} },
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter }
      ],
      schemas: [NO_ERRORS_SCHEMA] // Ignore les erreurs liées au DOM (routerLink, mat-icon, etc.)
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should call sessionService.$isLogged when $isLogged() is called', (done) => {
    const spy = jest.spyOn(mockSessionService, '$isLogged');
    component.$isLogged().subscribe((value) => {
      expect(value).toBe(true);
      expect(spy).toHaveBeenCalled();
      done();
    });
  });

  it('should call sessionService.logOut and navigate to root on logout()', () => {
    component.logout();
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['']);
  });
});
