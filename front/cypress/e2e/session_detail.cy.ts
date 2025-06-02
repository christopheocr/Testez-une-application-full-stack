describe('Session detail spec', () => {
  describe('Logged in as Admin', () => {
    beforeEach(() => {
      cy.visit('/login');

      cy.intercept('POST', '/api/auth/login', {
        body: {
          id: 1,
          username: 'adminUser',
          firstName: 'Admin',
          lastName: 'User',
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
          createdAt: new Date(),
          updatedAt: new Date(),
        },
      ]).as('sessionList');

      cy.intercept('GET', '/api/session/1', {
        id: 1,
        name: 'Test session',
        description: 'Test session description',
        date: new Date(),
        teacher_id: 1,
        users: [1],
        createdAt: new Date(),
        updatedAt: new Date(),
      });

      cy.intercept('GET', '/api/teacher/1', {
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
        createdAt: new Date(),
        updatedAt: new Date(),
      });

      cy.get('input[formControlName=email]').type('admin@example.com');
      cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');
      cy.get('div.items mat-card:first-child mat-card-actions button:first-child').click();
    });

    it('should show session details', () => {
      cy.url().should('include', '/sessions/detail/1');
      cy.get('mat-card-title h1').should('contain.text', 'Test Session');
    });

    it('should allow admin to delete session', () => {
      cy.intercept('DELETE', '/api/session/1', {});
      cy.intercept('GET', '/api/session', []).as('sessionListAfterDelete');

      cy.get('mat-card-title div div:nth-child(2) > button').click();
      cy.url({ timeout: 10000 }).should('include', '/sessions');
    });

    it('should return to session list on back', () => {
      cy.get('mat-card-title button').first().click();
      cy.url().should('include', '/sessions');
    });
  });

  describe('Logged in as a user not participating yet', () => {
    beforeEach(() => {
      cy.visit('/login');

      cy.intercept('POST', '/api/auth/login', {
        body: {
          id: 1,
          username: 'user1',
          firstName: 'First',
          lastName: 'Last',
          admin: false,
        },
      });

      cy.intercept('GET', '/api/session', [
        {
          id: 1,
          name: 'Test session',
          description: 'Test session description',
          date: new Date(),
          teacher_id: 1,
          users: [2], // autre utilisateur inscrit, pas celui connecté
          createdAt: new Date(),
          updatedAt: new Date(),
        },
      ]);

      cy.intercept('GET', '/api/session/1', {
        id: 1,
        name: 'Test session',
        description: 'Test session description',
        date: new Date(),
        teacher_id: 1,
        users: [2],
        createdAt: new Date(),
        updatedAt: new Date(),
      });

      cy.intercept('GET', '/api/teacher/1', {
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
        createdAt: new Date(),
        updatedAt: new Date(),
      });

      cy.get('input[formControlName=email]').type('user@example.com');
      cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');
      cy.get('div.items mat-card:first-child mat-card-actions button:first-child').click();
    });

    it('should allow user to participate in session', () => {
      cy.intercept('POST', '/api/session/1/participate/1', {});

      // Simule que l'utilisateur est maintenant inscrit
      cy.intercept('GET', '/api/session/1', {
        id: 1,
        name: 'Test session',
        description: 'Test session description',
        date: new Date(),
        teacher_id: 1,
        users: [1], // utilisateur connecté maintenant inscrit
        createdAt: new Date(),
        updatedAt: new Date(),
      });

      cy.get('mat-card-title div div:nth-child(2) > div > button span')
        .should('contain.text', 'Participate');

      cy.get('mat-card-title div div:nth-child(2) > div > button').click();

      cy.get('mat-card-title div div:nth-child(2) > div > button span')
        .should('contain.text', 'Do not participate');
    });
  });

  describe('Logged in as a user already participating', () => {
    beforeEach(() => {
      cy.visit('/login');

      cy.intercept('POST', '/api/auth/login', {
        body: {
          id: 1,
          username: 'user1',
          firstName: 'First',
          lastName: 'Last',
          admin: false,
        },
      });

      cy.intercept('GET', '/api/session', [
        {
          id: 1,
          name: 'Test session',
          description: 'Test session description',
          date: new Date(),
          teacher_id: 1,
          users: [1],
          createdAt: new Date(),
          updatedAt: new Date(),
        },
      ]);

      cy.intercept('GET', '/api/session/1', {
        id: 1,
        name: 'Test session',
        description: 'Test session description',
        date: new Date(),
        teacher_id: 1,
        users: [1],
        createdAt: new Date(),
        updatedAt: new Date(),
      });

      cy.intercept('GET', '/api/teacher/1', {
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
        createdAt: new Date(),
        updatedAt: new Date(),
      });

      cy.get('input[formControlName=email]').type('user@example.com');
      cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');
      cy.get('div.items mat-card:first-child mat-card-actions button:first-child').click();
    });

    it('should allow user to unparticipate from session', () => {
      cy.intercept('DELETE', '/api/session/1/participate/1', {});

      cy.intercept('GET', '/api/session/1', {
        id: 1,
        name: 'Test session',
        description: 'Test session description',
        date: new Date(),
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date(),
      });

      cy.get('mat-card-title div div:nth-child(2) > div > button span')
        .should('contain.text', 'Do not participate');

      cy.get('mat-card-title div div:nth-child(2) > div > button').click();

      cy.get('mat-card-title div div:nth-child(2) > div > button span')
        .should('contain.text', 'Participate');
    });
  });
});
