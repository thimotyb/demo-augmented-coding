# Handoff M06 — piano di implementazione

**Data:** 7 ottobre 2026
**Branch:** `main`
**HEAD osservato:** `a1fa5d7` (le modifiche M06 qui descritte non sono committate)

## Risultato

- Aggiunto il prompt contestualizzato in `moduli/m06/step-01-implementazione/prompt-implementation-plan.md`.
- Salvato il piano task-based in `moduli/m06/step-01-implementazione/planning/implementation-plan.md`; i 13 task iniziano con `[ ] DONE` e hanno criteri per spuntare lo stato. La tabella finale riassume ordine suggerito, dipendenze, parallelismo e stato.
- Aggiornati README M06, indice del repository e stato condiviso.
- Nessun codice applicativo generato; il piano descrive task e verifiche future.

Il piano è stato rielaborato seguendo il prompt e verificando le fonti elencate. Il processo locale `codex exec` non è stato completato: l'avvio sandboxato non riusciva a inizializzare l'app-server in filesystem read-only e il successivo tentativo fuori sandbox è stato interrotto durante l'aggiornamento del prompt. Non attribuire quindi il testo a un transcript di una sessione CLI.

## Verifiche

- `git diff --check` nel repository demo: passato.
- `python3 site/build_site.py`: generati homepage e 12 moduli nel repository del corso.
- `python3 site/check_links.py`: 13 pagine, 0 errori.
- `git diff --check` nel repository del corso: passato.
- Non sono stati eseguiti build o test applicativi: M06 non contiene ancora codice.

## Decisioni e prossimo passo

Prima del primo task di implementazione (`PROJ-01`), approvare versioni, generatore Maven, layout backend/frontend e fetta API. Consultare D-01…D-06 e la specifica M03; non inferire semantiche di idempotenza dal retry del pagamento. Dopo le decisioni, iniziare da `PROJ-01` e registrare comandi/build effettivamente verificati.

## Stato Git

Il repository contiene modifiche non correlate M04 e M05. Non includerle automaticamente in uno staging o commit M06; verificare lo stato prima della pubblicazione.

## Integrazione successiva — skill M06

- Aggiunto `moduli/m06/step-01-implementazione/skills/implementa-task-dal-piano/SKILL.md` e collegato dal README dello step. Il sorgente della skill legge un codice task e il work item corrispondente tramite Azure DevOps MCP; controlla dipendenze, decisioni e criteri prima di modificare file.
- Il collegamento MCP ad ADO è stato indicato come pronto dal maintainer; in questa sessione non è stata eseguita una lettura di work item né una prova di implementazione. Non associare un ticket a un task per sola somiglianza del titolo.
- `quick_validate.py` di `skill-creator` ha validato struttura e frontmatter della skill. Restano da provare con un work item reale il caso eseguibile e il caso bloccato; nessun task del piano è stato marcato `DONE`.
