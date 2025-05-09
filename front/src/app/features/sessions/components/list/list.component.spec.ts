import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ListComponent } from './list.component';
import { SessionApiService } from '../../services/session-api.service';
import { SessionService } from '../../../../services/session.service';
import { of } from 'rxjs';
import { Session } from '../../interfaces/session.interface';
import { SessionInformation } from '../../../../interfaces/sessionInformation.interface';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const mockSessions: Session[] = [
    {
      id: 1,
      teacher_id: 1,
      date: new Date(),
      name: 'Yoga 1',
      description: 'A yoga session',
      users: [],
      createdAt: new Date(),
      updatedAt: new Date()
    },
    {
      id: 2,
      teacher_id: 2,
      date: new Date(),
      name: 'Yoga 2',
      description: 'Another yoga session',
      users: [],
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ];

  const mockSessionApiService = {
    all: jest.fn().mockReturnValue(of(mockSessions))
  };

  const mockSessionInformation: SessionInformation = {
    id: 5,
    username: 'alice',
    firstName: 'Alice',
    lastName: 'Wonder',
    admin: true,
    token: 'mock-token',
    type: 'Bearer'
  };

  const mockSessionService = {
    sessionInformation: mockSessionInformation
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [RouterTestingModule],
      providers: [
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: SessionService, useValue: mockSessionService }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize sessions$ observable using SessionApiService.all()', (done) => {
    component.sessions$.subscribe((sessions) => {
      expect(sessions).toEqual(mockSessions);
      done();
    });

    expect(mockSessionApiService.all).toHaveBeenCalled();
  });

  it('should return session information from SessionService via user getter', () => {
    expect(component.user).toEqual(mockSessionInformation);
  });
});
