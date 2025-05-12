describe('Edit Form spec', () => {
  beforeEach(() => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
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
        updatedAt: new Date('2023-01-01T00:00:00Z'),
      },
    ]).as('sessionList');

    cy.get('input[formControlName=email]').type('john.doe@example.com');
    cy.get('input[formControlName=password]').type(`${'test!1234'}{enter}{enter}`);
  });

  it('Should allow to edit a session', () => {
    cy.intercept('GET', '/api/teacher', {
      body: [
        { id: 1, firstName: 'John', lastName: 'Doe', createdAt: '2023-01-01T00:00:00Z', updatedAt: '2023-01-01T00:00:00Z' },
        { id: 2, firstName: 'Maria', lastName: 'Dane', createdAt: '2023-01-01T00:00:00Z', updatedAt: '2023-01-01T00:00:00Z' },
        { id: 3, firstName: 'Mark', lastName: 'Smith', createdAt: '2023-01-01T00:00:00Z', updatedAt: '2023-01-01T00:00:00Z' },
      ],
    }).as('teachers');

    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: 'Test session',
        description: 'Test session description',
        date: new Date('2023-01-01T00:00:00Z'),
        teacher_id: 1,
        users: [1],
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
      },
    }).as('session1');

    cy.get('div.items mat-card:first-child mat-card-actions button:last-child')
      .should('exist')
      .click();

    cy.wait('@session1');

    cy.get('input[formControlName=name]')
      .should('have.value', 'Test session')
      .type(' Edited');

    cy.intercept('PUT', '/api/session/1', {
      body: {
        id: 1,
        name: 'Test session Edited',
        description: 'Test session description',
        date: new Date('2023-01-01T00:00:00Z'),
        teacher_id: 1,
        users: [1],
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
      },
    }).as('session1Edited');

    cy.intercept('GET', '/api/session', [
      {
        id: 1,
        name: 'Test session Edited',
        description: 'Test session description',
        date: new Date('2023-01-01T00:00:00Z'),
        teacher_id: 1,
        users: [1],
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
      },
    ]).as('sessionListUpdated');

    cy.get('button[type=submit]').click();

    cy.url().should('include', '/sessions');
  });
});
