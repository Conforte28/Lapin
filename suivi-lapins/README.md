# Suivi Lapins

Application web de suivi de production de lapins adaptée à une petite exploitation au Bénin (devise `FCFA`, dates au format `jj/mm/aaaa`, fuseau horaire `Africa/Porto-Novo`).

Technologies utilisées : Java 17, Maven, Spring Boot 3, Spring MVC, Thymeleaf, Spring Data JPA, Spring Security et PostgreSQL.

---

## 1. Logiciels à installer

Pour travailler en local et préparer le déploiement, installez :

1. **JDK 17** (Eclipse Temurin 17 ou Oracle JDK 17).
2. **Apache Maven** (version 3.8 ou supérieure ; souvent inclus avec NetBeans).
3. **Apache NetBeans** (version récente compatible Java 17 et Maven).
4. **PostgreSQL** (version 14 ou supérieure, avec pgAdmin ou `psql`).
5. **Git** et un compte **GitHub**.
6. Un compte **Render** (pour l'hébergement en ligne).

---

## 2. Ouvrir le projet Maven dans NetBeans

1. Ouvrez **Apache NetBeans**.
2. Cliquez sur **File > Open Project...** (Fichier > Ouvrir un projet).
3. Sélectionnez le dossier `suivi-lapins` (celui qui contient le fichier `pom.xml`).
4. Cliquez sur **Open Project**.
5. Attendez que NetBeans télécharge les dépendances Maven (vérifiez la barre de progression en bas à droite).

---

## 3. Créer et configurer la base PostgreSQL locale

1. Ouvrez `psql` ou **pgAdmin**.
2. Créez une base de données dédiée, par exemple `suivi_lapins` :
   ```sql
   CREATE DATABASE suivi_lapins;
   ```
3. Notez votre utilisateur PostgreSQL (ex. `postgres`) et son mot de passe.
4. Les tables (`utilisateurs`, `parametres_exploitation`, `lapins`, `mises_bas`, `sorties`, `depenses`) seront créées automatiquement par Hibernate au premier démarrage grâce à `spring.jpa.hibernate.ddl-auto=update`.

---

## 4. Définir les variables d'environnement et lancer le projet

L'application lit ses paramètres depuis les variables d'environnement suivantes :

- `PORT` : port d'écoute HTTP (`8080` par défaut en local).
- `DB_URL` : URL JDBC PostgreSQL au format exact `jdbc:postgresql://HOTE:PORT/NOM_DE_LA_BASE` (exemple local : `jdbc:postgresql://localhost:5432/suivi_lapins`). Ne collez pas une URL commençant par `postgres://`.
- `DB_USERNAME` : nom d'utilisateur PostgreSQL.
- `DB_PASSWORD` : mot de passe PostgreSQL.
- `DEMO_MODE` : `true` pour initialiser les comptes et données de démonstration lors du premier démarrage sur une base vide, `false` sinon.
- `DEMO_PASSWORD` : mot de passe initial attribué aux comptes de démonstration (par exemple `demo1234` pour vos tests).

### Lancement en ligne de commande (Linux / macOS)

```bash
export PORT=8080
export DB_URL=jdbc:postgresql://localhost:5432/suivi_lapins
export DB_USERNAME=postgres
export DB_PASSWORD=votre_mot_de_passe
export DEMO_MODE=true
export DEMO_PASSWORD=demo1234

mvn spring-boot:run
```

### Lancement en ligne de commande (Windows PowerShell)

```powershell
$env:PORT="8080"
$env:DB_URL="jdbc:postgresql://localhost:5432/suivi_lapins"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="votre_mot_de_passe"
$env:DEMO_MODE="true"
$env:DEMO_PASSWORD="demo1234"

mvn spring-boot:run
```

### Lancement depuis NetBeans

1. Faites un clic droit sur le projet **Suivi Lapins** > **Properties**.
2. Allez dans **Run** > **Environment Variables** (ou **Set Properties** selon la version de NetBeans).
3. Ajoutez `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `DEMO_MODE` et `DEMO_PASSWORD`.
4. Lancez le projet avec le bouton **Run** (`F6`).
5. Ouvrez votre navigateur à l'adresse : `http://localhost:8080`.

### Comptes de démonstration et avertissement de sécurité

Lorsque `DEMO_MODE=true` et `DEMO_PASSWORD=demo1234` au premier démarrage (base vide) :
- **Administrateur** : `admin@lapins.bj` / `demo1234`
- **Agent** : `agent@lapins.bj` / `demo1234`

Des données fictives cohérentes (lapins, une mise bas, une vente, un décès et des dépenses) sont également insérées une seule fois.

**Important :** ces comptes sont strictement réservés à la démonstration avec des données fictives. Après la démonstration, vous devez impérativement changer leurs mots de passe (depuis l'interface d'administration ou les paramètres) ou désactiver ces comptes. Passer uniquement `DEMO_MODE` à `false` empêche la création future de comptes de démo sur une base vide, mais **ne suffit pas à supprimer ni à désactiver les comptes déjà enregistrés dans PostgreSQL**.

---

## 5. Créer un dépôt GitHub et y envoyer le projet

Vérifiez qu'aucun secret ni mot de passe réel n'est présent dans les fichiers (`.env` est ignoré par `.gitignore`).

1. Créez un nouveau dépôt vide sur [GitHub](https://github.com) (ex. `suivi-lapins`).
2. Dans le dossier `suivi-lapins`, exécutez :
   ```bash
   git init
   git add .
   git commit -m "Initialisation du projet Suivi Lapins"
   git branch -M main
   git remote add origin https://github.com/VOTRE_COMPTE/suivi-lapins.git
   git push -u origin main
   ```

---

## 6. Créer une base PostgreSQL et un Web Service Docker sur Render

1. Connectez-vous sur [Render](https://render.com).
2. **Base de données PostgreSQL :**
   - Cliquez sur **New +** > **PostgreSQL**.
   - Choisissez un nom (ex. `suivi-lapins-db`), sélectionnez la région la plus proche et le forfait souhaité, puis créez la base.
   - Dans l'onglet **Info** de cette base, relevez l'**Internal Database URL** (ou **External Database URL**), le **Host**, le **Port** (généralement `5432`), le **Database Name**, le **Username** et le **Password**.
   - *Remarque :* si une base PostgreSQL Render n'est pas disponible dans votre forfait, vous pouvez utiliser n'importe quelle autre base PostgreSQL accessible depuis Internet tout en hébergeant l'application sur Render.
3. **Web Service Docker :**
   - Cliquez sur **New +** > **Web Service**.
   - Connectez votre dépôt GitHub `suivi-lapins`.
   - Pour **Language / Runtime**, sélectionnez **Docker** (Render détectera automatiquement le `Dockerfile` à la racine du projet).
   - Sélectionnez la branche `main`.

---

## 7. Renseigner les variables d'environnement sur Render

Dans la section **Environment Variables** de votre Web Service Render, ajoutez :

- `DB_URL` : construisez l'URL JDBC à partir des informations de votre base PostgreSQL :
  `jdbc:postgresql://HOTE:PORT/NOM_DE_LA_BASE`
  (Ne collez pas directement une URL commençant par `postgres://` dans `DB_URL`.)
- `DB_USERNAME` : l'utilisateur de votre base PostgreSQL.
- `DB_PASSWORD` : le mot de passe de votre base PostgreSQL.
- `DEMO_MODE` : `true` (pour l'initialisation de démonstration, puis `false` ensuite si vous le souhaitez, tout en changeant les mots de passe ou en désactivant les comptes de démo).
- `DEMO_PASSWORD` : `demo1234` (uniquement pour la phase de démonstration).

Note : Render fournit automatiquement la variable `PORT` ; l'application écoute sur `0.0.0.0` avec ce port.

---

## 8. Obtenir l'URL réelle de l'application

Une fois le déploiement Docker terminé avec succès :

1. Ouvrez la page de votre Web Service dans le tableau de bord Render.
2. En haut de la page (sous le nom du service), copiez l'URL publique affichée par Render (de la forme `https://suivi-lapins-xxxx.onrender.com`).
3. Ouvrez cette URL dans votre navigateur pour accéder à la page de connexion.

---

## 9. Vérifier la persistance des données après redémarrage

1. Connectez-vous avec le compte administrateur ou agent.
2. Ajoutez un nouveau lapin (par exemple `LAP-TEST-01`) ou une dépense.
3. Dans le tableau de bord Render, cliquez sur **Manual Deploy > Restart service** (ou redémarrez le service).
4. Attendez la fin du redémarrage, reconnectez-vous à l'application et vérifiez que `LAP-TEST-01` (ou la dépense) est toujours présent et que les indicateurs du Dashboard correspondent exactement aux données enregistrées.

---

## 10. Limites éventuelles du forfait Render choisi

Selon le forfait Render utilisé, prenez en compte les limites suivantes :

- **Mise en veille (spin-down) :** sur les instances d'entrée de gamme, le service Web peut être mis en veille après une période d'inactivité ; le premier chargement suivant peut prendre plusieurs dizaines de secondes.
- **Disponibilité :** aucun engagement de disponibilité continue ou de permanence n'est garanti sur les formules d'essai ou à ressources limitées.
- **Conservation de la base de données :** selon le forfait PostgreSQL choisi sur Render, la durée de conservation ou de disponibilité de la base peut être limitée dans le temps ; pensez à exporter régulièrement vos données importantes et à vérifier les conditions de votre forfait.
