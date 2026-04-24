# Portale Gestione Magazzino - Manuale DevOps

Questo documento contiene tutte le informazioni necessarie per la gestione dell'infrastruttura, le build, i test e la configurazione delle pipeline CI/CD per il progetto **Portale Gestione Magazzino**.

## 1. Architettura dell'Applicazione

L'applicazione è suddivisa in tre componenti principali:

1. **Frontend**: Angular 16+ con Angular Material e Tailwind CSS.
2. **Backend**: Spring Boot 3.x (Java 25), con Spring Security, Spring Data JPA. Genera un pacchetto `.war`.
3. **Database**: MySQL 8.x, database relazionale.

---

## 2. Dipendenze e Prerequisiti

### Ambiente di Sviluppo e Build

- **Java JDK**: 25 (necessario per compilare il backend).
- **Maven**: 3.8+ (oppure usare il Maven Wrapper `./mvnw` incluso).
- **Node.js**: 18+ o 20+ (raccomandato) con `npm` 9+.
- **Angular CLI**: 16+ (installabile via `npm install -g @angular/cli`).
- **Database**: MySQL 8.x.

---

## 3. Comandi di Build e Test

### Backend (Spring Boot)

Il backend utilizza Maven. Posizionarsi nella directory `magazzino-backend` per eseguire i comandi:

- **Pulizia del progetto**:

  ```bash
  ./mvnw clean
  ```

- **Esecuzione dei Test Unitari**:
  *(I test sono scritti in JUnit 5 e Mockito)*

  ```bash
  ./mvnw test
  ```

- **Build dell'artefatto (WAR)**:

  ```bash
  ./mvnw clean package -DskipTests
  ```

  L'artefatto finale verrà generato nella cartella `target/magazzino-backend-0.0.1-SNAPSHOT.war`.
- **Esecuzione in locale**:

  ```bash
  ./mvnw spring-boot:run
  ```

### Frontend (Angular)

Il frontend utilizza npm e l'Angular CLI. Posizionarsi nella directory `magazzino-frontend` per eseguire i comandi:

- **Installazione Dipendenze**:

  ```bash
  npm install
  ```

- **Avvio Server di Sviluppo** (Porta 4200):

  ```bash
  npm run start
  ```

- **Build per Produzione**:

  ```bash
  npm run build --configuration=production
  ```

  La build ottimizzata verrà generata nella directory `dist/magazzino-app/`.

---

## 4. Configurazione Database

Il database MySQL richiede la creazione iniziale dello schema e dei dati di default.

1. **Creazione Schema**:
   Utilizzare il file `create_db.sql` presente nella root del progetto per inizializzare il DB.

   ```bash
   mysql -u root -p < create_db.sql
   ```

2. **Connessione Backend**:
   Il backend si aspetta che il database risponda sulla porta `3306`. La configurazione attuale in `application.properties` è:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/db_magazzino?serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=root
   ```

---

## 5. Dockerizzazione (Suggerimenti)

Per un ambiente di Produzione o Staging, l'approccio raccomandato è la containerizzazione con Docker.

### Dockerfile per il Backend

```dockerfile
# Usa un'immagine base con Java 25 (se disponibile, altrimenti 21 come LTS più recente)
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/magazzino-backend-0.0.1-SNAPSHOT.war app.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]
```

### Dockerfile per il Frontend (con Nginx)

```dockerfile
# Stage 1: Build
FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --configuration=production

# Stage 2: Nginx per servire i file
FROM nginx:alpine
COPY --from=build /app/dist/magazzino-app /usr/share/nginx/html
# Sostituire la configurazione di default di Nginx per abilitare il routing di Angular
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
```

### docker-compose.yml

```yaml
version: '3.8'
services:
  db:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: db_magazzino
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
      - ./create_db.sql:/docker-entrypoint-initdb.d/init.sql

  backend:
    build:
      context: ./magazzino-backend
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/db_magazzino?serverTimezone=UTC
    depends_on:
      - db

  frontend:
    build:
      context: ./magazzino-frontend
      dockerfile: Dockerfile
    ports:
      - "4200:80"
    depends_on:
      - backend

volumes:
  mysql-data:
```

---

## 6. Configurazione Pipeline CI/CD

Una pipeline tipica per questo progetto dovrebbe consistere nei seguenti step (es. GitLab CI o GitHub Actions):

### Fase di Build (CI)

1. **Linting & Code Quality**:
   - `npm run lint` per il Frontend (se configurato).
2. **Unit Tests**:
   - Esecuzione automatica dei test Spring Boot: `mvn test`.
3. **Build degli Artefatti**:
   - Backend: `mvn package -DskipTests` (salva il file `.war` come artifact della pipeline).
   - Frontend: `npm run build` (salva la cartella `dist` come artifact).

### Fase di Deploy (CD)

1. **Creazione Immagini Docker**:
   - La pipeline esegue il `docker build` per Frontend e Backend e pusha le immagini in un Container Registry (es. Docker Hub, AWS ECR, GitLab Registry).
2. **Rilascio (Deploy)**:
   - Aggiornamento dei server (tramite SSH, Ansible, Kubernetes, o AWS ECS) per tirare le nuove immagini Docker ed eseguire `docker-compose up -d`.

### Sicurezza e Best Practice

- **Gestione Segreti**: Non hard-codare mai le password del database nel `application.properties`. Usare variabili d'ambiente fornite dalla pipeline o dal server (es. `SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}`).
- **CORS**: Per la produzione, aggiornare la configurazione CORS nel backend Spring Boot affinché accetti richieste solo dal dominio reale del frontend (non `localhost:4200`).

---

## 7. Deploy su Kubernetes (K8s) e Helm

Se l'infrastruttura di destinazione è basata su **Kubernetes**, è fortemente consigliato l'utilizzo di **Helm** per gestire in modo dichiarativo e parametrizzato le release dell'applicazione.

### Struttura consigliata della Chart Helm

Creare una directory `helm-chart/magazzino-app` nella root del progetto:

```plaintext
magazzino-app/
├── Chart.yaml          # Metadati della chart
├── values.yaml         # Variabili di default (immagini, repliche, porte)
├── templates/
│   ├── backend-deployment.yaml
│   ├── backend-service.yaml
│   ├── frontend-deployment.yaml
│   ├── frontend-service.yaml
│   ├── ingress.yaml    # Per esporre il frontend al mondo esterno
│   ├── mysql-statefulset.yaml
│   ├── mysql-service.yaml
│   ├── configmap.yaml  # Variabili d'ambiente non sensibili
│   └── secret.yaml     # Password DB (generato o iniettato via pipeline)
```

### Esempio di Deployment Backend (`templates/backend-deployment.yaml`)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ include "magazzino-app.fullname" . }}-backend
spec:
  replicas: {{ .Values.backend.replicaCount }}
  selector:
    matchLabels:
      app: backend
  template:
    metadata:
      labels:
        app: backend
    spec:
      containers:
        - name: backend
          image: "{{ .Values.backend.image.repository }}:{{ .Values.backend.image.tag }}"
          ports:
            - containerPort: 8080
          envFrom:
            - configMapRef:
                name: {{ include "magazzino-app.fullname" . }}-config
            - secretRef:
                name: {{ include "magazzino-app.fullname" . }}-secret
          livenessProbe:
            httpGet:
              path: /actuator/health
              port: 8080
            initialDelaySeconds: 30
```

### Comandi Operativi Helm

Una volta configurata la Chart, la pipeline CI/CD o il team DevOps potrà deployare l'intera architettura con comandi semplici:

- **Installazione (Deploy Iniziale)**:
  ```bash
  helm install magazzino-prod ./helm-chart/magazzino-app \
      --set backend.image.tag=v1.0.0 \
      --set frontend.image.tag=v1.0.0 \
      --set db.password=$SECRET_DB_PWD \
      --namespace magazzino-namespace --create-namespace
  ```

- **Aggiornamento (Release Successive)**:
  ```bash
  helm upgrade magazzino-prod ./helm-chart/magazzino-app \
      --set backend.image.tag=v1.0.1
  ```

- **Rollback (in caso di errori)**:
  ```bash
  helm rollback magazzino-prod 1
  ```

### Vantaggi di K8s + Helm per questo progetto:
- **Auto-healing**: I pod del frontend o backend si riavviano in automatico se crashano.
- **Scalabilità**: Aumentare `.Values.backend.replicaCount` per gestire picchi di richieste.
- **Rollback rapido**: Se una release del frontend rompe la dashboard, si torna indietro con un singolo comando.
- **Gestione Segreti**: Le password del db passano in modo sicuro ai Secret di Kubernetes, invece di restare in chiaro nei file di properties.
