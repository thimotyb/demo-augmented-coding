# M11 — risultati osservati MemPalace

Esecuzione effettuata il 2026-10-07 con MemPalace CLI 3.0.0, palace isolata in `/tmp/mempalace-m11-final-palace`, agent label `codex-m11-demo`. Corpus sintetico incluso nello step. I punteggi dipendono dal modello locale di embedding e possono variare tra installazioni.

## Struttura registrata

```text
WING: augmented-coding
  ROOM: team_memory      2 drawers
  ROOM: cost_control     1 drawer
```

La fase `mine` ha archiviato tre file: `decisioni-checkout.md`, `handoff-task.md` e `model-routing.md`.

## Interazione 1 — recuperare decisioni e handoff

**Query:** «Quali decisioni sul retry bloccano il task PAY-02?»

**Scope:** wing `augmented-coding`, room `team_memory`.

**Risultato MemPalace:**

1. `handoff-task.md` — similarità osservata `0.334`. Il task è bloccato se richiede una politica per retry checkout o deduplicazione ordine non ancora decisa; prima occorre verificare piano, contratto e prerequisiti.
2. `decisioni-checkout.md` — similarità osservata `0.051`. I requisiti prevedono fino a tre tentativi per errori transitori; restano aperte la politica di retry dell'intero checkout e la relazione fra `Idempotency-Key` e unicità dell'ordine.

Il testo completo è verificabile nei file sorgente della room. Il retrieval mette in evidenza il passaggio operativo e la decisione di dominio senza richiedere che l'agente mantenga tutta la chat precedente nel contesto corrente.

## Interazione 2 — preparare uno switch di modello

**Query:** «Quando conviene passare dal modello locale a quello remoto e cosa trasferire?»

**Scope:** wing `augmented-coding`, room `cost_control`.

**Risultato MemPalace:** `model-routing.md` — similarità osservata `0.269`. Modello locale per note non sensibili e iterazioni a basso rischio; modello remoto più capace per analisi architetturale complessa o revisione critica. Prima dello switch, trasferire obiettivo, fonti autorevoli, decisioni, file e test; registrare modello/provider e risultato.

## Interazione 3 — controllare il limite della ricerca

**Query:** «Quale decisione sul retry è bloccante?»

**Scope impostato:** wing `augmented-coding`, room `cost_control`.

**Risultato osservato:** la ricerca ha restituito comunque `model-routing.md` con similarità `-0.218`. Il filtro ha ristretto la ricerca a quella room, ma non garantisce che esista una risposta pertinente. L'agente deve controllare contenuto e pertinenza e dichiarare quando la room interrogata non contiene la decisione richiesta.

## Comandi ripetibili

```bash
mempalace --palace "$PALACE" status
mempalace --palace "$PALACE" search \
  "Quali decisioni sul retry bloccano il task PAY-02?" \
  --wing augmented-coding --room team_memory --results 2
mempalace --palace "$PALACE" search \
  "Quando conviene passare dal modello locale a quello remoto e cosa trasferire?" \
  --wing augmented-coding --room cost_control --results 2
```

Queste risposte sono state effettivamente recuperate dal palace locale di prova; non sono chiamate a un server MCP. La skill MemPalace installata nel repository documenta anche il percorso MCP, ma per provarlo occorre avviare e collegare il relativo server al client.
