# M11 — step-02: MemPalace con wing e room

## Obiettivo

Usare MemPalace per registrare e ritrovare risposte di lavoro in una struttura `wing → room → drawer`. Il laboratorio usa un palace locale e temporaneo: non legge né modifica il palace personale configurato sulla macchina.

## Prerequisiti

- Python e MemPalace CLI installati; il flusso verificato qui usa MemPalace **3.0.0**.
- Terminale Linux, macOS o WSL.
- Una cartella temporanea scrivibile.

Le skill MemPalace distribuite con questo repository sono in [`.agents/skills/`](../../../.agents/skills/). L'installazione delle skill fornisce procedure all'agente, ma non installa il runtime Python né configura automaticamente una connessione MCP.

## Dati della demo

La cartella [`corpus/`](corpus/) contiene tre risposte sintetiche del caso checkout, divise per argomento:

- `team_memory/`: decisioni aperte e passaggio di consegne;
- `cost_control/`: instradamento tra modello locale e remoto;
- `mempalace.yaml`: wing e room del laboratorio.

Non inserire qui conversazioni, ticket o dati riservati. I file vengono indicizzati nel palace locale.

## Esecuzione

Dalla root del repository, il comando più semplice esegue il laboratorio completo e conserva la palace in una cartella temporanea:

```bash
bash moduli/m11/step-02-mempalace-room-wing/run-demo.sh
```

Lo script copia il corpus in una directory temporanea, conferma le room rilevate, archivia i tre file e prova tre query con filtri wing/room. La directory viene mantenuta per consentire l'ispezione dei dati registrati.

Per eseguire i comandi uno alla volta, sempre dalla root del repository:

```bash
RUN_DIR="$(mktemp -d)"
PALACE="$RUN_DIR/palace"
CORPUS="$RUN_DIR/corpus"
mkdir -p "$CORPUS"
cp -R moduli/m11/step-02-mempalace-room-wing/corpus/. "$CORPUS/"

# Osserva e conferma le room suggerite dalle directory.
mempalace --palace "$PALACE" init "$CORPUS"

# init ricrea mempalace.yaml; mine usa il nome wing passato qui.
mempalace --palace "$PALACE" mine "$CORPUS" --wing augmented-coding --agent codex-m11-demo
mempalace --palace "$PALACE" status

mempalace --palace "$PALACE" search \
  "Quali decisioni sul retry bloccano il task PAY-02?" \
  --wing augmented-coding --room team_memory --results 2

mempalace --palace "$PALACE" search \
  "Quando conviene passare dal modello locale a quello remoto e cosa trasferire?" \
  --wing augmented-coding --room cost_control --results 2
```

`init` propone le room dalle directory `team_memory/` e `cost_control/`. Conferma entrambe le room. La configurazione inclusa consente di saltare `init` ed eseguire direttamente `mine`; `--wing augmented-coding` imposta esplicitamente la wing della sessione di indicizzazione.

`mktemp` crea il percorso esterno al repository. Conservare `PALACE` nello stesso terminale per tutta la prova; alla fine si può rimuovere la directory temporanea dopo aver deciso se tenere i risultati. Il comando non usa la palace personale predefinita.

## Output osservati

Le risposte registrate e gli estratti dell'esecuzione reale sono in [`risultati-osservati.md`](risultati-osservati.md). La ricerca ristretta alla room `team_memory` ritrova sia l'handoff sia la decisione sul retry; `cost_control` restituisce la nota sull'instradamento dei modelli. Una ricerca tematicamente incongruente può comunque mostrare un risultato con similarità bassa o negativa: controllare sempre il testo e la pertinenza, non trattare il punteggio come una soglia di correttezza.

## Cosa verificare

1. `status` deve mostrare la wing `augmented-coding` e le room `team_memory` e `cost_control`.
2. La query sul task deve restituire il drawer `handoff-task.md` e la decisione `decisioni-checkout.md`.
3. La query sul cambio di modello deve restituire `model-routing.md` nella room `cost_control`.
4. Confrontare ogni risposta con il file sorgente. Il retrieval aiuta a trovare il contesto, ma non sostituisce la verifica della fonte e della sua attualità.

## Relazione con MCP

Questo step documenta una prova ripetibile con la CLI. Le skill `mempalace-recall` e `mempalace-task` descrivono workflow che richiedono i rispettivi tool MCP; la loro presenza nel repository non dimostra che un MCP server sia stato configurato o collegato. Per l'esercizio MCP servono runtime installato, server avviato e tool visibili nel client.

Riferimenti: [repository ufficiale e installazione skill](https://github.com/MemPalace/mempalace), [concetti wing/room/drawer](https://github.com/MemPalace/mempalace/blob/develop/website/concepts/the-palace.md), [MemPalace recall skill](https://github.com/MemPalace/mempalace/blob/develop/skills/mempalace-recall/SKILL.md).
