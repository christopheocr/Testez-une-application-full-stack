describe('Register spec', () => {
  it('Should register successfully', () => {
    cy.visit('/register');

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200
    });

    cy.get('input[formControlName=firstName]').type('John');
    cy.get('input[formControlName=lastName]').type('Doe');
    cy.get('input[formControlName=email]').type('john.doe@example.com');
    cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

    cy.url({ timeout: 10000 }).should('include', '/login');
  });

  it('Should block submission when fields are empty', () => {
    cy.visit('/register');

    // Tous les champs vides → bouton désactivé
    cy.get('button[type=submit]').should('be.disabled');

    // Blur pour activer les validations si implémentées plus tard
    cy.get('input[formControlName=firstName]').focus().blur();
    cy.get('input[formControlName=lastName]').focus().blur();
    cy.get('input[formControlName=email]').focus().blur();
    cy.get('input[formControlName=password]').focus().blur();

    // Toujours désactivé
    cy.get('button[type=submit]').should('be.disabled');
  });
});
