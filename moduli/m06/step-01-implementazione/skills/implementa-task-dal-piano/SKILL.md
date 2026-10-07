---
name: implementa-task-dal-piano
description: Implementa un singolo task M06 identificato dal suo codice nel piano del checkout, confrontandolo con il work item Azure DevOps tramite MCP e verificando criteri di accettazione e test. Usare quando viene chiesto di svolgere un task come PROJ-01, API-01 o PAY-02; non per generare l'intero backend in una volta.
---

# Implementa un task dal piano M06

Usa questa skill dal repository `demo-augmented-coding`. L'input è un codice task stabile del piano M06, per esempio `PAY-02`, e il work item Azure DevOps associato (ID o URL). Se l'ID non è indicato, cercalo tramite l'MCP di Azure DevOps usando il codice task e il progetto configurato; prosegui solo se la corrispondenza è univoca. Non inserire PAT, token o contenuti riservati nei file del repository o nel resoconto.

## Fonti

- Piano: `moduli/m06/step-01-implementazione/planning/implementation-plan.md`.
- Stato e istruzioni: `AGENTS.md`, `docs/team-context/state.md` e README di M06.
- Requisiti, specifiche, diagrammi e OpenAPI: i percorsi richiamati dal task e dalla sezione *Sources and current baseline* del piano. Apri solo quelli pertinenti.
- Work item: leggi tramite l'MCP Azure DevOps titolo, descrizione, criteri di accettazione, stato e collegamenti. Usa gli strumenti di lettura disponibili nel client; i loro nomi possono variare.

## Procedura

1. Verifica branch, stato Git e codice task richiesto. Trova una sola voce con quel codice nel piano; se manca o è già `DONE`, riferisci lo stato prima di modificare file.
2. Leggi risultato atteso, dipendenze, probabili file, verifiche, criteri di accettazione e rischi della voce. Controlla nei file reali che le dipendenze siano soddisfatte: la casella `DONE` da sola non basta.
3. Leggi il work item ADO associato via MCP. Confronta perimetro, criteri e stato con il piano e le fonti versionate. Se manca il collegamento, la ricerca produce più ticket plausibili, l'MCP non è disponibile o le fonti sono in conflitto materiale, fermati e riporta precisamente ciò che serve per proseguire. Non usare dati ADO ricordati da una sessione precedente come verifica corrente.
4. Verifica le decisioni aperte che incidono sul task. In particolare, non dedurre unicità dell'ordine dal retry di autorizzazione o da `Idempotency-Key` del pagamento. Non scegliere versioni, persistenza, endpoint o risposte HTTP che il team non ha approvato. Se il task è bloccato, produci una breve diagnosi con riferimenti al piano e al work item, senza avviare l'implementazione.
5. Se il task è eseguibile, modifica solo i file necessari al suo risultato e ai test pertinenti. Per i task decisionali come `DEC-01` o `DEC-02`, produci il documento previsto dal piano e non generare codice applicativo. Mantieni fixture sintetiche e un diff revisionabile; preserva le modifiche preesistenti non pertinenti.
6. Esegui i controlli richiesti dal task con comandi realmente disponibili nel repository. Se un comando è solo proposto nel piano, definiscilo dopo aver creato il relativo progetto e poi eseguilo. Distingui test superati, non eseguiti e limiti dell'ambiente.
7. Spunta `[x] DONE` nel piano solo quando tutte le verifiche e i criteri di accettazione del task sono soddisfatti; altrimenti lascia `[ ] DONE` e spiega cosa manca. Aggiorna la nota di handoff in `docs/team-context/` secondo `AGENTS.md`.

## Consegna

Indica codice task e ID del work item letto, fonti usate, file modificati, comandi e risultati, criteri soddisfatti, stato `DONE` e decisioni residue. Cita il work item con ID o link, senza copiarne dati sensibili. Non cambiare stato, campi o commenti su Azure DevOps e non pubblicare commit o PR come effetto automatico di questa skill; esegui tali azioni soltanto se richieste esplicitamente.
