# 🧘‍♂️ Yoga App – Projet Fullstack

Ce projet est une application complète composée de deux parties :

- 📦 [`backend/`](./back/README.md) – API Java (tests + JaCoCo)
- 🌐 [`frontend/`](./front/README.md) – Application Angular (Jest + Cypress)

---

## 🐬 Base de données – MySQL + phpMyAdmin

Une configuration Docker est fournie pour lancer l’environnement de base de données :

```bash
cd ressources/sql
docker-compose up -d
````

Cela démarre :

* MySQL (port `3306`)
* phpMyAdmin (port `8080`)

> Accès phpMyAdmin : [http://localhost:8080](http://localhost:8080)
> Utilisateur : `root`
> Mot de passe : `password`

---

## 🔗 Liens vers les tests

* ✅ [Tests backend (Java + JaCoCo)](./back/README.md)
* ✅ [Tests frontend (Angular + Jest + Cypress)](./front/README.md)


