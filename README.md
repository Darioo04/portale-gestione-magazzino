# 📦 Portale Gestione Magazzino

Sistema Enterprise per la gestione delle scorte, movimenti merci e notifiche automatiche, sviluppato con architettura multi-modulo Java e Spring Boot.

## 🚀 Caratteristiche Principali

* **Architettura Multi-modulo**: Suddivisione chiara tra `backend` (Spring Boot), `frontend` (Web App) e `ear` (Packaging Enterprise).
* **Logging Centralizzato (AOP)**: Monitoraggio automatico di tutti i Service tramite Aspect-Oriented Programming (AOP). I log vengono gestiti in modo trasversale senza sporcare la logica di business.
* **Sicurezza JWT**: Autenticazione e autorizzazione basata su ruoli (`Amministratore`, `Responsabile`, `Addetto`) con token stateless.
* **Gestione Scorte Intelligente**: Generazione automatica di notifiche per tutti gli utenti Admin quando un prodotto scende sotto la soglia minima (`Stock Threshold`).
* **Gestione Errori Custom**: Restituzione di codici HTTP mirati (es. `400 Bad Request`) in caso di tentativi di scarico merce superiori alla disponibilità.

## 🛠️ Tech Stack

* **Java 26** & **Spring Boot 4.0.5**
* **Maven**: Gestione delle dipendenze e del ciclo di vita del progetto.
* **Spring Data JPA**: Persistenza dei dati su database **MySQL**.
* **Spring Security + JWT**: Gestione della sicurezza e dei token.
* **Spring AOP**: Implementazione del logger unico per tutto il backend.

## 📁 Struttura del Progetto (File POM)

* `magazzino-parent`: Modulo radice che centralizza le versioni e le configurazioni Maven.
* `magazzino-backend`: Logica di business, API REST e gestione del database.
* `magazzino-frontend`: Interfaccia utente del portale.
* `magazzino-ear`: Modulo per il packaging Enterprise (EAR) destinato a server come WildFly.

## ⚙️ Setup e Installazione

1. **Database**: Configura un'istanza MySQL e aggiorna le credenziali in `magazzino-backend/src/main/resources/application.properties`.
2. **Compilazione**: Dalla cartella root, esegui il comando:

    ```bash
    mvn clean install
    ```

3. **Esecuzione**: Per avviare il backend:

    ```bash
    cd magazzino-backend
    mvn spring-boot:run
    ```

## 📝 API Principali (Esempi)

| Metodo | Endpoint | Descrizione |
| :--- | :--- | :--- |
| `POST` | `/api/movements/load` | Carico merce (Admin/Responsabile) |
| `POST` | `/api/movements/unload` | Scarico merce con controllo scorte (Admin/Addetto) |
| `GET` | `/api/notifications` | Elenco notifiche per prodotti sotto-soglia |
| `POST` | `/api/auth/login` | Login e rilascio del token JWT (validità 1h) |
