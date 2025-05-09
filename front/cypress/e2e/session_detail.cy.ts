describe('Session detail spec', () => {
  beforeEach(() => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      }
    });

    cy.intercept('GET', '/api/session', [
      {
        id: 1,
        name: 'Test session',
        description: 'Test session description',
        date: new Date('2023-01-01T00:00:00Z'),
        teacher_id: 1,
        users: [1],
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z')
      }
    ]).as('getSessions');

    cy.intercept('GET', '/api/session/1', {
      id: 1,
      name: 'Test session',
      description: 'Test session description',
      date: new Date('2023-01-01T00:00:00Z'),
      teacher_id: 1,
      users: [1],
      createdAt: new Date('2023-01-01T00:00:00Z'),
      updatedAt: new Date('2023-01-01T00:00:00Z')
    }).as('getSession1');

    cy.intercept('GET', '/api/teacher/1', {
      id: 1,
      firstName: 'John',
      lastName: 'Doe',
      createdAt: new Date('2023-01-01T00:00:00Z'),
      updatedAt: new Date('2023-01-01T00:00:00Z')
    }).as('getTeacher1');

    cy.get('input[formControlName=email]').type('john.doe@example.com');
    cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

    cy.get('div.items mat-card:first-child mat-card-actions button:first-child').click();
  });

  it('should show session details', () => {
    cy.url().should('include', '/sessions/detail/1');
    cy.get('mat-card-title h1').should('contain.text', 'Test Session');
  });

  it('should allow admin to delete session', () => {
    cy.intercept('DELETE', '/api/session/1', {}).as('deleteSession');
    cy.intercept('GET', '/api/session', []).as('getSessionsAfterDelete');

    cy.get('mat-card-title div div:nth-child(2) > button').click();

    cy.wait('@deleteSession');
    cy.wait('@getSessionsAfterDelete');

    cy.url({ timeout: 10000 }).should('include', '/sessions');
  });

  it('should return to session list on back', () => {
    cy.get('mat-card-title button').first().click();
    cy.url().should('include', '/sessions');
  });
});
