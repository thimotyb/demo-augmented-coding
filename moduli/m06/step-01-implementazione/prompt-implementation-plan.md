# Prompt — piano di implementazione M06

Agisci come software architect e technical planner. Analizza il repository e le fonti indicate. Non modificare codice e non implementare i task: produci soltanto un piano Markdown per la demo M06.

## Fonti autorevoli

- Requisiti e scenari: `caso-guida/ecomm-requirements.md`
- Contratto API ridotto e playbook: `moduli/m05/step-01-contratto/openapi/checkout-api.yaml`, `moduli/m05/step-01-contratto/scenarios/postman-playbooks.yaml` e relativo README
- Diagrammi: `caso-guida/req-seq-checkout-success.puml`, `caso-guida/req-seq-payment-retry.puml`, `caso-guida/req-domain-entities.puml`
- Specifica SDD e decisioni: `moduli/m03/step-02-speckit/specifiche-speckit/specs/001-payment-retry/spec.md`, `tasks.md`, `decisions.md`
- Viste e requisiti architetturali: `moduli/m04/step-01-architettura/architecture-requirements.md` e diagrammi associati
- Struttura prevista M06: `moduli/m06/README.md` e `moduli/m06/step-01-implementazione/README.md`

Prima di pianificare, controlla i file esistenti e le istruzioni `AGENTS.md`. Distingui i requisiti espliciti dalle decisioni ancora aperte e dai vincoli già presenti nel repository.

## Obiettivo della demo

Pianifica una fetta verticale didattica e riproducibile che mostri come un coding agent possa implementare codice professionale a partire da requisiti, diagrammi e contratto verificati. Considera vincoli del corso: API Java 21 con Spring Boot e front-end React con Material UI. Le fonti possono non fissare versioni, generatore Maven, struttura del repository e confini di persistenza: elencali come decisioni da approvare, senza inventare versioni o dipendenze. Il gateway resta simulato e le fixture sintetiche. Riutilizza come baseline i playbook funzionali M05.

## Formato del piano

Salva il risultato in `moduli/m06/step-01-implementazione/planning/implementation-plan.md`.

Organizza il lavoro in stadi ordinati, con codici `STAGE-01`, `STAGE-02`, ... e task con codici univoci e stabili, per esempio `DEC-01`, `API-01`, `PAY-01`, `UI-01`, `TEST-01`. I codici devono permettere di richiedere in seguito l'implementazione di un task preciso senza ambiguità.

Per ogni task indica:

- as the first line, one Markdown task-list checkbox: `- [ ] DONE — TASK-ID — Title`. In a newly generated plan, leave the box unchecked; an unchecked `DONE` means the task is not done. After verifying the task's acceptance criteria, check it: `- [x] DONE — TASK-ID — Title`. Use only this single checkbox and the English label `DONE`;
- risultato osservabile e requisito/scenario coperto, citando i percorsi delle fonti;
- dipendenze tramite codici di task;
- file o aree probabili da modificare, distinguendo ipotesi da file già esistenti;
- passi circoscritti;
- test o controlli e comandi verificati nel repository, oppure marcati da definire;
- criteri di accettazione verificabili;
- rischi, decisioni aperte o condizioni bloccanti.

Includi una matrice requisito/scenario → task → verifica, le decisioni umane necessarie prima dell'implementazione e gli elementi esplicitamente fuori scope. Mantieni piccoli i task e fai dipendere l'UI dal contratto API concordato. Non assumere che retry, idempotenza di `POST /payments` e unicità dell'intero checkout siano la stessa regola. Non decidere durata/riuso della chiave, comportamento sui timeout o persistenza/concorrenza se le fonti non lo specificano. Non includere cattura reale dei fondi, Stripe, servizi esterni o dati reali.

Close the document with a concise execution table that makes the suggested order clear at a glance. For each task include order, stage, ID, short title, dependencies, and one `DONE` checkbox (`[ ] DONE` initially; `[x] DONE` only after acceptance criteria are verified); add a brief note where tasks can proceed in parallel. Respect the dependencies in the detailed plan: the numeric order is a suggested sequence and must not turn independent work into mandatory dependencies. Table checkboxes must match the task checklists.

Use task lists for decision gates that are represented as tasks too. Do not mark a task `DONE` merely because it appears in the plan or its specification has been read; verify the requested result first. Keep the Markdown hierarchy with stages as headings and tasks as checklist items beneath each stage.

Scrivi in inglese. Non avviare build, installazioni, modifiche al codice o implementazione. Al termine mostra un breve riepilogo delle decisioni bloccanti e del primo task eseguibile; la tabella sintetica deve essere l'ultima sezione del file.
