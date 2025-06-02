# Yoga App

## Tests & Couverture de code

### Lancer tous les tests unitaires

```bash
mvn test
````

### Lancer uniquement les tests d’intégration

Les tests d’intégration sont identifiés par la convention `*IT.java`.

```bash
mvn verify -Dtest=*IT
```

### Générer le rapport de couverture de code (JaCoCo)

```bash
mvn clean verify
```

Le rapport HTML est généré à l’emplacement suivant :

```
target/site/jacoco/index.html
```
