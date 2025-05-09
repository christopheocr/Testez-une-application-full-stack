describe('Login spec', () => {
  it('Login successful', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        token: 'fake-token',
        type: 'Bearer',
        id: 1,
        username: 'john.doe@example.com',
        firstName: 'John',
        lastName: 'Doe',
        admin: true
      }
    });

    cy.intercept('GET', '/api/session', []).as('session');

    cy.get('input[formControlName=email]').type('john.doe@example.com');
    cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

    cy.url({ timeout: 10000 }).should('include', '/sessions');
  });

  it('Login failed', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: { message: 'Invalid credentials' }
    });

    cy.get('input[formControlName=email]').type('john.doe@example.com');
    cy.get('input[formControlName=password]').type('wrongpass{enter}{enter}');

    cy.get('p.error').should('exist');
    cy.get('p.error').should('contain.text', 'An error occurred');
  });

  it('Should show validation errors when fields are empty', () => {
  cy.visit('/login');

  cy.get('button[type=submit]').should('be.disabled');

  cy.get('input[formControlName=email]').focus().blur();
  cy.get('input[formControlName=password]').focus().blur();

  cy.get('button[type=submit]').should('be.disabled');
  });
});
