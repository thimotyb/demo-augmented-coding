# Contesto condiviso tra i due Codex

## Scelta operativa

Usiamo **Git come fonte autorevole** per istruzioni, decisioni e passaggi di consegne. Ogni macchina clona lo stesso repository; Codex legge `AGENTS.md` del progetto e, quando serve, [`state.md`](state.md). La [documentazione ufficiale OpenAI](https://learn.chatgpt.com/docs/customization/memories) descrive le memorie dei client Codex locali come file generati sotto `~/.codex/memories/`: sono utili come richiamo personale, ma non sono il deposito delle decisioni di team. Non sincronizzare l'intera `~/.codex`, che contiene anche stato e configurazioni personali. `/resume` riprende una chat salvata nel client, ma il secondo Codex deve poter continuare usando i file versionati. [OpenAI Docs: istruzioni di progetto](https://learn.chatgpt.com/docs/agent-configuration/agents-md), [progetti e chat](https://learn.chatgpt.com/docs/projects).

**Questo repository è pubblico.** Le note qui devono essere pubblicabili insieme alle demo. Per contesto interno o riservato, usare un repository privato distinto oppure un vault con accessi appropriati e riportare qui soltanto le decisioni didattiche non sensibili.

All'inizio del lavoro su ciascuna macchina:

```bash
git pull --ff-only
git status --short --branch
```

Aprire Codex dalla radice del clone e chiedere: «Leggi `AGENTS.md` e `docs/team-context/state.md`; verifica in Git lo stato corrente e proponi il prossimo passo del modulo assegnato». Assegnare **branch diversi** alle due macchine, per esempio `demo/m02-plan-mode` e `demo/m13-gpu`; integrare tramite PR o commit revisionato. Non far scrivere a entrambi `main` contemporaneamente. Prima del passaggio, compilare una copia di [`handoff-template.md`](handoff-template.md), includere commit e test effettivi, e pubblicarla insieme ai file del task.

Dopo una modifica ad `AGENTS.md`, avviare una nuova sessione Codex per caricare le istruzioni aggiornate: la scoperta dei file avviene all'avvio della sessione. Il file `CLAUDE.md` importa le stesse istruzioni per Claude Code. [OpenAI Docs: scoperta di `AGENTS.md`](https://learn.chatgpt.com/docs/agent-configuration/agents-md), [Claude Code: importazioni](https://code.claude.com/docs/en/memory).

Per avviare subito un lavoro indipendente sulla seconda macchina, usa il [brief della GPU](gpu-brief.md). Chiede un inventario tecnico per M13 senza installare modelli né modificare M02.

## Obsidian

Obsidian può aprire il clone Git come vault Markdown: le note sono normali file del repository, quindi si condividono con commit/pull. Tenere `.obsidian/` locale per non propagare preferenze e stato dell'editor. Se si sceglie Obsidian Sync per un vault distinto, stabilire quale deposito prevale e come riportare nel repository le decisioni approvate; non usare due meccanismi che modificano contemporaneamente gli stessi file. [Obsidian: formato dei dati](https://obsidian.md/help/data-storage), [collaborazione](https://obsidian.md/help/sync/collaborate).

## MemPalace

MemPalace può **indicizzare** le note approvate per la ricerca tra progetti e sessioni. Il contenuto autorevole resta nei file versionati; una risposta trovata nell'indice va ricontrollata nel file e nel commit sorgente. Su questa macchina la CLI `mempalace` e il palace montato in `/mnt/g/Il mio Drive/mempalace` sono presenti; sulla macchina GPU occorre verificare installazione, accesso e sincronizzazione dello stesso deposito. Non scrivere manualmente file dentro il palace: usare `mempalace mine` dopo aver aggiornato e revisionato le note del repository. L'indicizzazione non viene eseguita automaticamente da questo repository.

Esempio **facoltativo**, da eseguire solo dopo aver verificato i permessi del mount e la CLI sulla macchina interessata:

```bash
mempalace --palace "/mnt/g/Il mio Drive/mempalace" mine docs/team-context --mode projects --wing demo-augmented-coding --agent codex --limit 20
mempalace --palace "/mnt/g/Il mio Drive/mempalace" search "demo augmented coding M02 Plan Mode"
```

Non indicizzare `.env`, credenziali, transcript integrali o dati non approvati. Se la macchina GPU non monta lo stesso Drive, Git è già sufficiente per condividere lo stato; MemPalace può essere aggiunto dopo.
