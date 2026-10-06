# M02 — Codex, Claude Code e Plan Mode

**Stato:** step 01 pronto come laboratorio guidato. La parte offline usa due piani editoriali di esempio; il confronto live richiede i client e l'accesso ai rispettivi modelli.

**Obiettivo:** ottenere e revisionare un piano per correggere il retry del checkout senza nascondere il rischio di ordini duplicati. Codex e Claude Code ricevono **lo stesso prompt** e leggono gli stessi file del [caso guida](../../caso-guida/README.md). Nessun codice va modificato durante la fase di piano.

| Step | Input | Output |
| --- | --- | --- |
| [01 — Plan Mode](step-01-plan-mode/README.md) | Prototipo M01, requisiti, diagramma di retry e OpenAPI | Due piani confrontati con una rubrica; decisioni aperte e test proposti |

**Durata indicativa:** 35–50 minuti. **Prerequisiti offline:** Python 3.10+ per verificare la baseline M01. **Per la parte live:** Codex e Claude Code installati e autenticati sui propri account. Non servono chiavi nei file del laboratorio.

**Criterio di riuscita:** l'allievo distingue requisiti espliciti da decisioni aperte, riconosce un piano che inventa l'idempotenza dell'ordine e propone test prima di autorizzare qualsiasi edit.
