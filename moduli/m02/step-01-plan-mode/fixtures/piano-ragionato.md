# Piano B — Esempio editoriale ragionato

1. **Rilevare il comportamento:** `moduli/m01/step-01-prototipo/demo.py` chiama `authorize` una sola volta e crea sempre un ordine dopo l'approvazione. `scripts/check_m01.py` fissa gli esiti osservabili.
2. **Vincoli espliciti:** `caso-guida/req-seq-payment-retry.puml` descrive fino a tre tentativi per un errore transitorio; `ecomm-requirements.md` richiede pricing validation prima della creazione dell'ordine. `req-apis.yaml` documenta `Idempotency-Key` su `POST /payments` come header opzionale. Non descrive una chiave di idempotenza per `POST /orders`.
3. **Decisioni del team:** quali errori sono ritentabili? La chiave di pagamento resta uguale in tutti i tentativi? Cosa accade se arriva la stessa richiesta di checkout dopo un successo o con payload diverso? Per quanto tempo si conserva l'esito? Quando è lecito creare l'ordine?
4. **Alternative:** gestire il retry nel client con chiave stabile per i tentativi di pagamento, oppure nel servizio che orchestra il checkout. Entrambe richiedono confini chiari tra autorizzazione e creazione dell'ordine. Non scegliere la persistenza/deduplicazione dell'ordine senza requisito approvato.
5. **Test prima del fix:** gateway con errore transitorio poi approvazione; gateway che fallisce tre volte; rifiuto definitivo che non viene ritentato; stessa richiesta due volte dopo successo; prezzo alterato. Registrare quali test sono già richiesti dalle fonti e quali dipendono da nuove decisioni.
6. **Passi proposti:** aggiornare `spec.md` e criteri; scrivere i test approvati; modificare in piccoli diff orchestrazione del pagamento e creazione ordine; eseguire test e confrontare contratto/diagrammi. In questo step non apportare edit e non dichiarare conclusa la sicurezza del checkout.

Questo è un riferimento per la discussione. Una soluzione reale dipenderà dalle decisioni del team e dall'architettura implementata nei moduli successivi.
