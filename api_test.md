# API Test Suite - Portale Gestione Magazzino

Questo documento raccoglie 10 test HTTP chiave per testare il sistema. Per ogni richiesta protetta è necessario passare l'header `Authorization: Bearer <TOKEN>` estratto dal Test 1.

## 1. Autenticazione (Creazione Token Amministratore)

Estrazione del Token "chiave passpartout" per navigare il sistema.

* **Metodo e Endpoint:** `POST http://localhost:8080/api/auth/login`
* **Body:**

```json
{
  "username": "mario",
  "password": "hash_pwd_mario_123!"
}
```

* **Risultato atteso:** Status `200 OK`. Ritorna un JSON contenente `{ "token": "ey....." }`. Memorizzare questo token.

Test SQL

```sql
SELECT
    u.id            AS utente_id,
    u.username,
    r.id            AS ruolo_id,
    r.nome          AS ruolo_nome
FROM Utente u
JOIN Ruolo r ON u.ruolo_id = r.id
WHERE u.username = 'mario';
```

---

## 2. Creazione Categoria

* **Metodo e Endpoint:** `POST http://localhost:8080/api/categories`
* **Body:**

```json
{
  "name": "Surgelati"
}
```

* **Risultato atteso:** Status `201 Created`. Test riuscito se l'ID è un nuovo numero progressivo.

Test SQL

```sql
SELECT id, nome
FROM Categoria
WHERE nome = 'Surgelati';
```

---

## 3. Creazione Tipologia Prodotto

* **Metodo e Endpoint:** `POST http://localhost:8080/api/product-types`
* **Body:**

```json
{
  "eanCode": "8001010101010",
  "name": "Pizza Surgelata Margherita",
  "brand": "Pizzeria Finta",
  "price": 3.50,
  "stockThreshold": 10,
  "categoryId": 7 
}
```

> Nota: L'id Categoria dipenderà dal risultato del "Test 2".

* **Risultato atteso:** Status `201 Created`.

Test SQL

```sql
SELECT codice_ean, nome, marca, prezzo, soglia_scorta, categoria_id
FROM TipologiaProdotto
WHERE codice_ean = '8001010101010';
```

---

## 4. Gestione Spazi: Creazione nuova Locazione (Area Frigo)

* **Metodo e Endpoint:** `POST http://localhost:8080/api/locations`
* **Body:**

```json
{
  "area": "Celle Frigo",
  "aisle": 1,
  "shelf": 1
}
```

* **Risultato atteso:** Status `201 Created`. Test della chiave composta andato a buon fine.

Test SQL

```sql
SELECT *
FROM Locazione
WHERE area = 'Celle Frigo'
  AND corsia = 1
  AND scaffale = 1;
```

---

## 5. Inserimento Fiscale di un Prodotto Fisico

Associa il lotto di pizze arrivate direttamente nello scaffale frigo appena cablato.

* **Metodo e Endpoint:** `POST http://localhost:8080/api/products`
* **Body:**

```json
{
  "eanCode": "8001010101010",
  "quantity": 50,
  "expirationDate": "2026-10-31T00:00:00",
  "batch": "LOTTO_ZERO_1",
  "locationArea": "Celle Frigo",
  "locationAisle": 1,
  "locationShelf": 1
}
```

* **Risultato atteso:** Status `201 Created`. Il prodotto è in vita col suo ID di DB.

Test SQL

```sql
SELECT id, codice_ean, quantita, data_scadenza, lotto,
       locazione_area, locazione_corsia, locazione_scaffale
FROM Prodotto
WHERE lotto = 'LOTTO_ZERO_1';
```

---

## 6. Test di Carico Movimento (Update Dinamico Giacenza)

L'Amministratore inserisce un carico di 20 pezzi per questo nuovo prodotto.

* **Metodo e Endpoint:** `POST http://localhost:8080/api/movements/load`
* **Body:**

```json
{
  "quantity": 20,
  "productId": 4, 
  "userId": 1
}
```

> Nota: Inserire l'ultimo `productId` generato alla fine del passaggio precedente.

* **Risultato atteso:** Status `201 Created`. **Controllo Cruciale**: Ora richiamando l'endpoint del prodotto originario (`GET /api/products/4`) la `quantity` globale deve essere dinamicamente salita a **70** (50 + 20).

Test SQL

```sql
SELECT id, quantita, prodotto_id, utente_id, data_movimento
FROM Movimento
WHERE prodotto_id = 4
ORDER BY data_movimento DESC
LIMIT 1;
```

---

## 7. Scarico Movimento standard

Un mulettista segna un'uscita di 15 pezzi verso lo shop.

* **Metodo e Endpoint:** `POST http://localhost:8080/api/movements/unload`
* **Body:**

```json
{
  "quantity": -15,
  "productId": 4,
  "userId": 1
}
```

* **Risultato atteso:** Status `201 Created`. La Quantità sul prodotto calerà a **55**.

Test SQL

```sql
SELECT quantita
FROM Prodotto
WHERE id = 4;
```

---

## 8. Scudo Sicurezza Giacenze (Richiesta Invalida)

Forzare volutamente un errore mandando un quantitativo che farebbe sbordare lo stock sotto lo zero.

* **Metodo e Endpoint:** `POST http://localhost:8080/api/movements/unload`
* **Body:**

```json
{
  "quantity": -100,
  "productId": 4,
  "userId": 1
}
```

* **Risultato atteso:** Status `500 Internal Server Error`. La transazione abortisce, intercettata e neutralizzata. Lo stock resta invariato a 55.

Test SQL

```sql
SELECT quantita
FROM Prodotto
WHERE id = 4;

Quantità invariata
```

```sql
SELECT COUNT(*)
FROM Movimento
WHERE prodotto_id = 4
  AND quantita = -100;

Nessun nuovo movimento
```

---

## 9. Filtro Data Range

Estrapola un resoconto reportistico del mese in corso filtrandolo tramite i Path Variables della stringa Query.

* **Metodo e Endpoint:** `GET http://localhost:8080/api/movements/date-range?startDate=2024-01-01&endDate=2026-12-31`
* Non c'è Body, solo parametri url e header del Token.
* **Risultato atteso:** Ritorna `Status 200 OK` con un array JSON contenente tutti e tre i movimenti generati dai vecchi script e quelli nuovi, per tutti i dipendenti all'interno dell'arco temporale richiesto.

Test SQL

```sql
SELECT id, data_movimento, quantita, prodotto_id, utente_id
FROM Movimento
WHERE data_movimento
BETWEEN '2024-01-01' AND '2026-12-31'
ORDER BY data_movimento;
```

---

## 10. Movimento per utente

Restituisce tutti i movimenti effettuati da un utente.

* **Metodo e Endpoint:** `GET http://localhost:8080/api/movements/user/1`
* Non c'è Body, solo header del Token.
* **Risultato atteso:** Ritorna `Status 200 OK` con un array JSON contenente tutti i movimenti effettuati da un utente. Ogni movimento dovrà avere `userID = 1`.

Test SQL

```sql
SELECT
    m.id,
    m.data_movimento,
    m.quantita,
    m.prodotto_id,
    u.id        AS utente_id,
    u.username,
    r.nome      AS ruolo
FROM Movimento m
JOIN Utente u ON m.utente_id = u.id
JOIN Ruolo r  ON u.ruolo_id = r.id
WHERE u.id = 1
ORDER BY m.data_movimento;
```

---

## 11. Movimento per prodotto

Restituisce tutti i movimenti di un prodotto.

* **Metodo e Endpoint:** `GET http://localhost:8080/api/movements/product/4`
* Non c'è Body, solo header del Token.
* **Risultato atteso:** Ritorna `Status 200 OK` con un array JSON contenente tutti i movimenti di un prodotto. Ogni movimento dovrà avere `productId = 4`.

Test SQL

```sql
SELECT
    m.id,
    m.data_movimento,
    m.quantita,
    m.prodotto_id,
    p.codice_ean,
    p.lotto
FROM Movimento m
JOIN Prodotto p ON m.prodotto_id = p.id
WHERE m.prodotto_id = 4
ORDER BY m.data_movimento;
```

---

## 12. Movimento per tipologia di prodotto

Restituisce tutti i movimenti di una tipologia di prodotto.

* **Metodo e Endpoint:** `GET http://localhost:8080/api/movements/product-type/8001010101010`
* Non c'è Body, solo header del Token.
* **Risultato atteso:** Ritorna `Status 200 OK` con un array JSON contenente tutti i movimenti di una tipologia di prodotto. Ogni movimento dovrà avere `codiceEan = 8001010101010`.

Test SQL

```sql
SELECT
    m.id                AS movimento_id,
    m.data_movimento,
    m.quantita,
    p.id                AS prodotto_id,
    p.lotto,
    p.codice_ean,
    tp.nome             AS nome_prodotto,
    tp.marca,
    u.username          AS operatore,
    r.nome              AS ruolo_operatore
FROM Movimento m
JOIN Prodotto p           ON m.prodotto_id = p.id
JOIN TipologiaProdotto tp ON p.codice_ean = tp.codice_ean
JOIN Utente u             ON m.utente_id = u.id
JOIN Ruolo r              ON u.ruolo_id = r.id
WHERE tp.codice_ean = '8001010101010'
ORDER BY m.data_movimento;
```

---

## 13. Verifica Blocco Sicurezza Permessi

Eseguire nuovamente il `Login` usando l'utenza dell'ultimo operatore: `username: giulia` e  `password: hash_pwd_giulia_789!`.
Copiare questo suo Token "limitiato" di "Addetto di reparto". Inserire in Postman e provare a eliminare un ProductType:

* **Metodo e Endpoint:** `DELETE http://localhost:8080/api/product-types/8001234567890` (Header: `Bearer <TOKEN_GIULIA>`)
* **Risultato atteso:** `Status 403 Forbidden`. Il livello di permessi non concede di accedere al metodo decorato da `@PreAuthorize` e protegge l'integrità master dei database.

Test SQL

```sql
SELECT codice_ean, nome
FROM TipologiaProdotto
WHERE codice_ean = '8001234567890';
```
