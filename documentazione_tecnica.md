# Documentazione portale gestione magazzino

## 🏗️ Struttura dell'Architettura

L'applicazione segue fedelmente un'architettura **Layered** (a strati) basata su **Spring Boot**. Il core del backend è suddiviso seguendo il classico approccio **Controller - Service - Repository**, esponendo logicamente le comunicazioni sotto forma di **API REST**.

### Di cosa si occupa ogni Package?

Ecco una panoramica dei moduli principali in cui è articolato il backend:

* **`controller`**: Il "front-desk" dell'app. Oltre ad esporre tutte le rotte REST, riceve le richieste in entrata (effettuate da Frontend o Thunder Client), ne intercetta i payload, valida gli input e instrada il lavoro direttamente ai Service in modo ordinato.
* **`service`**: Qui si trova il "cervello", ossia la pura **Business Logic**. I service applicano le regole di business, effettuano operazioni complesse orchestrando i salvataggi e interpellano le repository (le uniche "autorizzate" a comunicare col DB) secondo la necessità.
* **`repository`**: Il "magazzino del magazzino", detto anche Data Access Layer. Risiede in questo layer tutta la logica di scambio dati col database fisico (in questo caso MySQL). Si occupa unicamente di salvare, ritrovare, unire ed eliminare informazioni.
* **`entity`**: Contiene le rappresentazioni Java che mappano le tabelle reali presenti nel database tramite Hibernate.
* **`dto` (Data Transfer Object)**: Dei contenitori senza logica usati unicamente per ritirare informazioni o inviarne al frontend (ad esempio, per non esporre informazioni riservate o interazioni di oggetti complessi in un'informazione troppo verbosa).
* **`security`**: Il filtro. Contiene i dettagli di Spring Security, implementazioni specifiche come il controllo delle sessioni e la gestione (creazione/validazione) dei Token JWT!

---

## 💾 La Gestione delle Repository: JPA e EntityManager

In questo progetto l'accesso alla base dati è gestito integrando due approcci del mondo **Spring Data / JPA**:

1. **Spring Data JpaRepository**: Per la stragrande maggioranza degli oggetti (come `CategoryRepository`, `MovementRepository`, ecc.), si sfrutta la magia di Spring. Creando un'interfaccia che estende `JpaRepository` si ottengono metodi completi e pronti all'uso (es. `save()`, `findAll()`, `findById()`). Tutto astratto ed estremamente rapido.
2. **EntityManager personalizzato**: Per poter mostrare appieno come si gestisce la persistenza a basso livello, la repository principale che orchestra i Prodotti (`ProductRepositoryImpl`) utilizza invece un approccio Totalmente Custom con l' **`EntityManager`**. Attraverso annotazioni essenziali d'accesso come `@PersistenceContext` e direttive sulle transazioni come `@Transactional`, scrivendo "a mano" ogni query, le operazioni d'inserimento o sincronizzazione (persist ed entity merge) e le rimozioni in db si ha una panoramica di cosa accade dietro il velo di JPA.

---

## 🔒 Sicurezza: Login e Token JWT

Ogni rotta di questo magazzino è messa in sicurezza tramite protocollo basato su **JWT (JSON Web Tokens)** e Spring Security.

1. **Emissione (Login)**: Un utente registrato invia tramite richiesta le proprie credenziali. Se username e password appaiono nei record, la funzione genera e firma crittograficamente un token (Token JWT) servendosi della libreria _jjwt_. Questo token viene recapitato come risposta.
2. **Uso (Autorizzazione)**: Ad ogni invocazione successiva al login, il client deve obbligatoriamente allegare negli Header (`Authorization: Bearer <token>`) il suo token JWT.
3. Il **`JwtAuthenticationFilter`** cattura la richiesta, estrae il token, lo spacchetta per verificare che non sia stato corrotto o semplicemente scaduto nel tempo; autorizza l'utente e lo inserisce nel _SecurityContext_ in conformità ai suoi permessi e ruoli (es. admin o user).

---

## 🛠️ Funzionalità Implementate (Le features)

* **Autenticazione e Sicurezza**: Incastonamento di un login (e logout potenziale) con validazione ruoli per filtrare chi può vedere e agire su cosa.
* **Gestione Categorie**: Permette di categorizzare coerentemente i prodotti definendo famiglie di articoli.
* **Gestione Prodotti (Anagrafica)**: Esposizione di operatività Create, Read, Update, Delete (CRUD) su qualsiasi prodotto fisico in vendita.
* **Movimenti di Magazzino**: Registrare ogni tracciamento logistico, ovvero gli spostamenti di entrata merci (Carico) per aumentare disponibilità, ed uscita (Scarico), diminuendole.
* **Rapporti Giacenze (Stock)**: Monitoraggio logico delle quantità possedute in base agli elenchi prodotti, con controlli base o reportistica di sottoscorta, integrando pure ricerca rapida.
* **Gestione Utenti**: Esposizione di operatività Create, Read, Update, Delete (CRUD) su qualsiasi utente del sistema. Autorizzazione fornita solo all'amministratore.

---

## 🧩 Dettaglio Tecnico Aggiuntivo: Logging Centralizzato

Oltre all'infrastruttura di base, il progetto presenta una classe per il logging centralizzato per mantenere il codice pulito e favorire il debugging:

* **Centralizzazione del Logging tramite AOP**: Nel codice è presente la classe `LoggingAspect.java`. Utilizzando il paradigma AOP (_Aspect-Oriented Programming_ basato su Spring AOP/AspectJ), il sistema "intercetta" in modo invisibile le invocazioni ai metodi del layer **Service**. Questo permette al sistema di loggare lo stack in console: stampa in modo totalmente automatico l'inizio dei processi e intercetta in caso di eccezione (_AfterThrowing_), loggando l'errore senza richiedere tonnellate di `try/catch` o `Logger.info()` dentro i service.

---

## 🚀 Guida all'Avvio

### 1. Avvio Database da WSL (MySQL)

* **Per avviare il servizio:**

    ```bash
    sudo service mysql start
    ```

* **Accedere all'interfaccia a riga di comando** per gestire il database:

    ```bash
    sudo mysql -u root -p
    ```

### 2. Compilazione e Avvio del Magazzino Backend

Dal terminale nativo di Windows, nella cartella principale contenente il codice backend (`magazzino-backend`).

* **Clean & Compilazione delle dipendenze**:

    ```cmd
    mvn clean install
    ```

* **Lancio di Spring boot**

    ```cmd
    mvn spring-boot:run
    ```
