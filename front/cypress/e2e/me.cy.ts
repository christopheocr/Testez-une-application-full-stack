describe('Me page', () => {
  beforeEach(() => {
    // Connexion simulée
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        token: 'fake-token',
        type: 'Bearer',
        id: 1,
        username: 'john.doe@example.com',
        firstName: 'John',
        lastName: 'Doe',
        admin: false,
      },
    });

    cy.intercept('GET', '/api/session', []).as('session');

    cy.get('input[formControlName=email]').type('john.doe@example.com');
    cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

    cy.url().should('include', '/sessions');
  });

  it('Should display user information correctly', () => {
    cy.intercept('GET', '/api/user/1', {
      id: 1,
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      admin: false,
      createdAt: '2023-01-01T00:00:00.000Z',
      updatedAt: '2023-01-02T00:00:00.000Z',
    }).as('getUser');

    cy.get('span[routerLink="me"]').click();
    cy.wait('@getUser');

    cy.contains('Name: John DOE').should('exist');
    cy.contains('Email: john.doe@example.com').should('exist');
    cy.contains('Create at:').should('exist');
    cy.contains('Last update:').should('exist');
  });

  it('Should show delete account button only if not admin', () => {
    cy.intercept('GET', '/api/user/1', {
      id: 1,
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      admin: false,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    }).as('getUser');

    cy.get('span[routerLink="me"]').click();
    cy.wait('@getUser');

    cy.contains('Delete my account:').should('exist');
    cy.get('button').contains('Detail').should('exist');
  });

  it('Should delete account and redirect to home', () => {
    cy.intercept('GET', '/api/user/1', {
      id: 1,
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      admin: false,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    }).as('getUser');

    cy.intercept('DELETE', '/api/user/1', { statusCode: 200 }).as('deleteUser');

    cy.get('span[routerLink="me"]').click();
    cy.wait('@getUser');

    cy.get('button').contains('Detail').click();
    cy.wait('@deleteUser');

    cy.url({ timeout: 10000 }).should('eq', 'http://localhost:4200/');
  });

  it('Should go back to previous page when back button clicked', () => {
    cy.intercept('GET', '/api/user/1', {
      id: 1,
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      admin: false,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    }).as('getUser');

    cy.get('span[routerLink="me"]').click();
    cy.wait('@getUser');

    cy.go('back');
    cy.url().should('include', '/sessions');
  });

    it('Should not show delete button if user is admin', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        token: 'fake-token',
        type: 'Bearer',
        id: 2,
        username: 'admin@example.com',
        firstName: 'Admin',
        lastName: 'Root',
        admin: true,
      },
    });

    cy.intercept('GET', '/api/session', []).as('session');

    cy.get('input[formControlName=email]').type('admin@example.com');
    cy.get('input[formControlName=password]').type('adminpass{enter}{enter}');

    cy.url().should('include', '/sessions');

    cy.intercept('GET', '/api/user/2', {
      id: 2,
      firstName: 'Admin',
      lastName: 'Root',
      email: 'admin@example.com',
      admin: true,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    }).as('getAdminUser');

    cy.get('span[routerLink="me"]').click();
    cy.wait('@getAdminUser');

    cy.contains('Name: Admin ROOT').should('exist');
    cy.contains('Email: admin@example.com').should('exist');

    cy.contains('Delete my account:').should('not.exist');
    cy.get('button').contains('Detail').should('not.exist');
  });

});
