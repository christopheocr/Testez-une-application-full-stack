describe('Form spec', () => {
  describe('Not Logged In', () => {
    it('Should forbid access to user if not logged in', () => {
      cy.visit('/sessions/create');
      cy.url().should('include', '/login');
    });
  });

  describe('Logged In as an Admin', () => {
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

      cy.intercept('GET', '/api/session', []).as('session');

      cy.get('input[formControlName=email]').type('john.doe@example.com');
      cy.get('input[formControlName=password]').type(`${'test!1234'}{enter}{enter}`);
      cy.wait('@session');
    });

    it('Should allow to create new Rental', () => {
      cy.get('mat-card-header button').should('exist');
      cy.get('mat-card-header button').should('contain.text', 'Create');

      cy.intercept('GET', '/api/teacher', {
        body: [
          {
            id: 1,
            firstName: 'John',
            lastName: 'Doe',
            createdAt: '2023-01-01T00:00:00.000Z',
            updatedAt: '2023-01-01T00:00:00.000Z',
          },
        ],
      }).as('teachers');

      cy.get('mat-card-header button').click();
      cy.wait('@teachers');

      cy.get('input[formControlName=name]').type('Test Rental');
      cy.get('input[formControlName=date]').type('2023-01-01');
      cy.get('mat-select[formControlName=teacher_id]').click();
      cy.get('span.mat-option-text').first().click();
      cy.get('textarea[formControlName=description]').type('Test Description');

      cy.get('button[type=submit]').should('exist');
      cy.get('button[type=submit]').should('contain.text', 'Save');

      cy.intercept('POST', '/api/session', {
        body: {
          id: 1,
          name: 'Test Rental',
          date: '2023-01-01',
          teacher_id: 1,
          description: 'Test Description',
          createdAt: '2023-01-01T00:00:00.000Z',
          updatedAt: '2023-01-01T00:00:00.000Z',
        },
      }).as('createRental');

      cy.get('button[type=submit]').click();
      cy.wait('@createRental');
    });
  });
});
