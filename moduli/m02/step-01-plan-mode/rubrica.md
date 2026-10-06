# Rubrica di revisione del piano

Assegnare **0, 1 o 2 punti** per ciascuna voce: 0 = assente o errato; 1 = presente ma generico; 2 = preciso e sostenuto da file/risultati. Totale massimo 12. Il punteggio serve a discutere il piano, non a dichiarare un modello vincitore su una sola prova.

| Voce | Evidenza attesa |
| --- | --- |
| Fonti e confini | Cita il caso checkout, la sequenza di retry e i punti pertinenti di OpenAPI senza inventare endpoint |
| Retry | Distingue errore transitorio da rifiuto definitivo e propone un test con fallimento seguito da approvazione |
| Idempotenza | Riconosce che `Idempotency-Key` riguarda `POST /payments`; non deduce automaticamente l'unicità di `POST /orders` |
| Ambiguità | Elenca decisioni aperte su chiave, durata, payload diverso, limiti di tentativi e stato dell'ordine |
| Piano di modifica | Propone passi piccoli, file plausibili e verifica prima/dopo; non modifica il codice in Plan Mode |
| Verifica | Specifica test per caso felice, errore transitorio, esaurimento tentativi e richiesta ripetuta |

**Segnale di stop:** un piano che presume una politica di retry/idempotenza non documentata va corretto dal team prima di implementare, anche se ottiene un punteggio alto nelle altre voci.
