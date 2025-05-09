describe('NotFound page', () => {
  it('Should redirect to /404 for unknown routes and display correct message', () => {
    cy.visit('/route-inexistante', { failOnStatusCode: false });

    cy.url().should('include', '/404');

    cy.get('h1').should('contain.text', 'Page not found !');
  });
});
