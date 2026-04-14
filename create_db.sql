CREATE DATABASE IF NOT EXISTS db_magazzino;

USE db_magazzino;

CREATE TABLE IF NOT EXISTS Ruolo (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Persona (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS Utente (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    persona_id INTEGER NOT NULL UNIQUE,
    ruolo_id INTEGER NOT NULL,
    FOREIGN KEY (ruolo_id) REFERENCES Ruolo(id) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (persona_id) REFERENCES Persona(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Notifica (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    messaggio TEXT NOT NULL,
    data_creazione DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    letto BOOLEAN NOT NULL DEFAULT FALSE,
    priorita ENUM('LOW', 'MEDIUM', 'HIGH') NOT NULL,
    utente_id INTEGER NOT NULL,
    FOREIGN KEY (utente_id) REFERENCES Utente(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Categoria (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS TipologiaProdotto (
    codice_ean VARCHAR(20) PRIMARY KEY NOT NULL,
    nome VARCHAR(100) NOT NULL,
    marca VARCHAR(100) NOT NULL,
    prezzo DECIMAL(10, 2) NOT NULL CHECK (prezzo > 0),
    soglia_scorta INTEGER NOT NULL CHECK (soglia_scorta > 0),
    categoria_id INTEGER NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES Categoria(id) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS Locazione (
    area VARCHAR(50) NOT NULL,
    corsia INTEGER NOT NULL CHECK (corsia > 0),
    scaffale INTEGER NOT NULL CHECK (scaffale > 0),
    PRIMARY KEY (area, corsia, scaffale)
);

CREATE TABLE IF NOT EXISTS Prodotto (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    codice_ean VARCHAR(20) NOT NULL,
    quantita INTEGER NOT NULL CHECK (quantita >= 0),
    data_scadenza DATETIME,
    lotto VARCHAR(50) NOT NULL,
    locazione_area VARCHAR(50),
    locazione_corsia INTEGER,
    locazione_scaffale INTEGER,
    FOREIGN KEY (codice_ean) REFERENCES TipologiaProdotto(codice_ean) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (locazione_area, locazione_corsia, locazione_scaffale) REFERENCES Locazione(area, corsia, scaffale) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS Movimento (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    data_movimento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    quantita INTEGER NOT NULL CHECK (quantita != 0),
    prodotto_id INTEGER NOT NULL,
    utente_id INTEGER NOT NULL,
    FOREIGN KEY (prodotto_id) REFERENCES Prodotto(id) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (utente_id) REFERENCES Utente(id) ON UPDATE CASCADE ON DELETE RESTRICT
);

USE db_magazzino;

-- Inserimento dei dati di esempio per testare le funzionalità del sistema

INSERT INTO Ruolo (nome) VALUES 
('Amministratore'),
('Responsabile carico merci'),
('Addetto di reparto');

INSERT INTO Persona (nome, cognome, email, telefono) VALUES 
('Mario', 'Rossi', 'mario.rossi@azienda.it', '+39 333 1234567'),
('Luigi', 'Bianchi', 'luigi.bianchi@azienda.it', '+39 334 9876543'),
('Giulia', 'Verdi', 'giulia.verdi@azienda.it', NULL);

INSERT INTO Categoria (nome) VALUES 
('Pasta'),
('Frutta'),
('Verdura'),
('Biscotti'),
('Elettronica'),
('Detersivi e Pulizia');

INSERT INTO Locazione (area, corsia, scaffale) VALUES 
('Settore A', 2, 1),
('Settore A', 1, 2),
('Settore B', 1, 1),
('Settore B', 2, 5);


-- Inserimento Tabelle di Primo Livello (Dipendono dalle tabelle indipendenti)

INSERT INTO Utente (username, password, persona_id, ruolo_id) VALUES 
('mario', 'hash_pwd_mario_123!', 1, 1),
('luigi', 'hash_pwd_luigi_456!', 2, 2),
('giulia', 'hash_pwd_giulia_789!', 3, 3);

INSERT INTO TipologiaProdotto (codice_ean, nome, marca, prezzo, soglia_scorta, categoria_id) VALUES 
('8001234567890', 'Pasta Spaghetti 500g', 'Barilla', 1.20, 50, 1),
('8009876543210', 'Banana', 'Dole', 1.50, 100, 2),
('8005555555555', 'Pomodoro', 'Mutti', 0.90, 80, 3),
('8001111111111', 'Biscotti al Cioccolato', 'Mulino Bianco', 2.50, 40, 4),
('0012345678905', 'Smartphone Model X', 'TechBrand', 499.99, 10, 5),
('8002222222222', 'Detersivo Liquido 1L', 'CleanCo', 3.99, 30, 6);


-- Inserimento Tabelle di Secondo Livello (Dipendono dal Primo Livello)

INSERT INTO Prodotto (codice_ean, quantita, data_scadenza, lotto, locazione_area, locazione_corsia, locazione_scaffale) VALUES 
('8001234567890', 200, '2025-12-31', 'L-2024-A1', 'Settore A', 2, 1),
('8001234567890', 50, '2024-06-30', 'L-2024-B2', 'Settore A', 1, 2),
('0012345678905', 12, NULL, 'P-24-X', 'Settore B', 2, 5);


-- Inserimento Tabelle di Terzo Livello (Dipendono dal Secondo Livello)

INSERT INTO Movimento (quantita, prodotto_id, utente_id) VALUES 
(100, 1, 2), -- Carico di 100 unità di Pasta Spaghetti 500g da parte di Luigi
(-20, 1, 3), -- Scarico di 20 unità di Pasta Spaghetti 500g da parte di Giulia
(12, 3, 2); -- Carico di 12 unità di Smartphone Model X da parte di Luigi

INSERT INTO Notifica (messaggio, priorita, utente_id) VALUES 
('La quantità di Pasta Spaghetti 500g è scesa sotto la soglia di sicurezza.', 'HIGH', 2),
('Il prodotto Smartphone Model X è in esaurimento.', 'MEDIUM', 1),
('Il prodotto Detersivo Liquido 1L ha superato la data di scadenza.', 'HIGH', 3);
