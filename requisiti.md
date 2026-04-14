Progettazione e sviluppo di un sistema di gestione magazzino

Contents

[Obiettivo del progetto 3](#_Toc225778497)

[High Level Design (HLD) 3](#_Toc225778498)

[Modello dati - ER Diagram 3](#_Toc225778499)

[Architettura Backend (HLD) 3](#_Toc225778500)

[Vincoli tecnologici e di deployment 4](#_Toc225778501)

[Funzionalità richieste 5](#_Toc225778502)

[Requisiti non funzionali 6](#_Toc225778503)

[Deliverable richiesti 6](#_Toc225778504)

# Obiettivo del progetto

Progettare un **portale di gestione magazzino** definendo una **High Level Design (HLD)** prima dell'implementazione.

Tecnologie di riferimento:

- Backend: Spring Boot
- Frontend: Angular + Angular Material
- Database relazionale

# Funzionalità richieste

**1\. Gestione prodotti**

Il sistema deve permettere di:

- Creare, modificare, eliminare e visualizzare prodotti

**2\. Gestione categorie**

- Creazione e gestione categorie prodotti
- Relazione tra prodotto e categoria

**3\. Movimentazioni di magazzino**

Gestione dei movimenti:

- Carico (entrata merce)
- Scarico (uscita merce)

**4\. Giacenze**

- Evidenziazione prodotti sotto scorta
- Filtri e ricerca

**6\. Autenticazione (base)**

- Login/logout
- Gestione utente base (anche hardcoded inizialmente)
- ruoli (admin / user)

# Requisiti non funzionali

- Codice pulito e organizzato
- Naming consistente
- README con:
  - istruzioni di avvio
- Logging base lato backend

# High Level Design (HLD)

Prima di iniziare lo sviluppo, deve essere prodotta una progettazione ad alto livello che descriva **come sarà strutturato il sistema**, senza entrare nei dettagli implementativi.

## Modello dati - ER Diagram

Definire il **modello concettuale dei dati** tramite diagramma ER.

**Obiettivi**

- Identificare le principali entità del dominio (es. Prodotto, Categoria, Movimento, ecc.)
- Definire le relazioni tra le entità
- Evidenziare cardinalità (1-N, N-N)
- Individuare eventuali vincoli logici (es. obbligatorietà, dipendenze)
- Gestione Utenti, Ruoli e permessi (prima fase solo utente admin)

**Output richiesto**

- Diagramma ER

## Architettura Backend

Definire la struttura logica del backend basato su Spring Boot.

**Obiettivi**

Descrivere:

- Come è organizzata l'applicazione

**Scelta architetturale**

L'applicazione di deve basare sul pattern Layered Architecture (Controller - Service - Repository)

**Aspetti da descrivere**

**1\. Struttura a layer**

Descrivere i layer logici, ad esempio:

- Layer di esposizione (API REST)
- Layer di business logic
- Layer di accesso ai dati

**2\. Responsabilità dei layer**

Spiegare:

- cosa fa ogni layer

**3\. Flusso delle richieste**

Descrivere a livello logico:

- come una richiesta attraversa il sistema (es. creazione prodotto, registrazione movimento)
- senza entrare nel dettaglio del codice

**4\. Gestione della complessità**

Indicare eventuali scelte come:

- uso di DTO (solo a livello concettuale)
- gestione delle validazioni

**Output richiesto**

- Schema architetturale

**Non è richiesto**:

- elenco classi
- firme dei metodi
- dettagli di implementazione

## Vincoli tecnologici e di deployment

Il sistema deve essere progettato e sviluppato tenendo conto dei seguenti vincoli architetturali e di rilascio:

**Packaging e build**

- L'applicazione backend deve essere pacchettizzata come **EAR (Enterprise Archive)**
- Il processo di build deve essere gestito tramite Apache Maven
- La struttura del progetto deve essere compatibile con un packaging multi-modulo (es. separazione tra moduli applicativi - )

**Application Server**

- Il deploy deve avvenire su WildFly (distribuzione open source di JBoss EAP)
- L'applicazione deve essere progettata per essere eseguita all'interno di un application server Java EE / Jakarta EE

**Implicazioni architetturali (da considerare in HLD)**

Durante la fase di progettazione, devono essere considerate le seguenti scelte:

**1\. Strutturazione dei moduli**

Definire a livello concettuale:

- separazione tra moduli (es. core, web, API)
- organizzazione dell'EAR (senza dettaglio tecnico eccessivo)

**2\. Gestione delle dipendenze**

- uso di Apache Maven per la gestione delle dipendenze
- coerenza tra moduli

**3\. Integrazione con application server**

- progettare il backend in modo compatibile con WildFly
- considerare configurazioni standard (datasource, deployment, ecc.) a livello concettuale

# Deliverable richiesti

- Codice sorgente (FE + BE + procedure di build)
- Diagramma ER
- Script SQL di creazione database
- Script di inizializzazione dei prodotti e utenti
- Demo funzionante