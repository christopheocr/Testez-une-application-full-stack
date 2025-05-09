import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormComponent } from './form.component';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SessionService } from '../../../../services/session.service';
import { TeacherService } from '../../../../services/teacher.service';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { Session } from '../../interfaces/session.interface';
import { RouterTestingModule } from '@angular/router/testing';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockSession: Session = {
    id: 1,
    name: 'Yoga',
    description: 'Relaxing session',
    date: new Date(),
    teacher_id: 1,
    users: [],
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockRoute = {
    snapshot: {
      paramMap: {
        get: jest.fn().mockReturnValue('1')
      }
    }
  };

  const mockRouter = {
    url: '/sessions/update/1',
    navigate: jest.fn()
  };

  const mockMatSnackBar = {
    open: jest.fn()
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  };

  const mockTeacherService = {
    all: jest.fn().mockReturnValue(of([]))
  };

  const mockSessionApiService = {
    create: jest.fn().mockReturnValue(of(mockSession)),
    update: jest.fn().mockReturnValue(of(mockSession)),
    detail: jest.fn().mockReturnValue(of(mockSession))
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormComponent],
      imports: [ReactiveFormsModule, RouterTestingModule],
      providers: [
        { provide: ActivatedRoute, useValue: mockRoute },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: SessionService, useValue: mockSessionService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        FormBuilder
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should load session if in update mode', () => {
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
    expect(component.onUpdate).toBe(true);
    expect(component.sessionForm?.value.name).toBe(mockSession.name);
  });

  it('should call create() and navigate on submit in create mode', () => {
    component.onUpdate = false;
    component.sessionForm?.setValue({
      name: 'Yoga',
      date: new Date().toISOString().split('T')[0],
      teacher_id: 1,
      description: 'Relaxing session'
    });

    component.submit();

    expect(mockSessionApiService.create).toHaveBeenCalled();
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should call update() and navigate on submit in update mode', () => {
    component.onUpdate = true;
    component.sessionForm?.setValue({
      name: 'Yoga',
      date: new Date().toISOString().split('T')[0],
      teacher_id: 1,
      description: 'Relaxing session'
    });

    component.submit();

    expect(mockSessionApiService.update).toHaveBeenCalledWith('1', expect.any(Object));
    expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  });
});
