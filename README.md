# Demo — Dal vibe coding all'augmented coding

Repository degli esempi del [corso](https://github.com/thimotyb/corso-augmented-coding). Tutti i moduli usano lo stesso caso: **checkout di un carrello, autorizzazione del pagamento simulata ed eventuale retry**. Requisiti, diagrammi e OpenAPI sono in [`caso-guida/`](caso-guida/README.md).

## Stato e percorso

| Modulo | Tema | Stato |
| --- | --- | --- |
| [M01](moduli/m01/README.md) | Dal prototipo al software professionale | **Demo pronta**, eseguibile con Python 3 |
| [M02](moduli/m02/README.md) | Codex, Claude Code e Plan Mode | Struttura predisposta |
| [M03](moduli/m03/README.md) | Requisiti e SDD leggero | Struttura predisposta |
| [M04](moduli/m04/README.md) | Progettazione e PlantUML | Struttura predisposta |
| [M05](moduli/m05/README.md) | Contratto OpenAPI | Struttura predisposta |
| [M06](moduli/m06/README.md) | Coding assistito e React | Struttura predisposta |
| [M07](moduli/m07/README.md) | Reverse engineering | Struttura predisposta |
| [M08](moduli/m08/README.md) | Test e regressione | Struttura predisposta |
| [M09](moduli/m09/README.md) | Sicurezza | Struttura predisposta |
| [M10](moduli/m10/README.md) | DevOps e CI | Struttura predisposta |
| [M11](moduli/m11/README.md) | GitHub e Azure DevOps MCP | Struttura predisposta |
| [M12](moduli/m12/README.md) | Skill e memoria del team | Struttura predisposta |
| [M13](moduli/m13/README.md) | Modelli locali e progetto finale | Struttura predisposta |

Le cartelle M02–M13 sono uno **scheletro editoriale**: gli step sono descritti, ma non ancora eseguibili. Non usarli come laboratori pronti.

## Avvio rapido di M01

Serve **Python 3.10 o successivo**; il percorso base usa solo la libreria standard e non richiede rete, account, GPU o API key. Dalla radice del repository:

```bash
python3 moduli/m01/step-01-prototipo/demo.py
python3 scripts/check_m01.py
```

Il secondo comando verifica che il prototipo mostri le tre lacune previste. Un controllo riuscito conferma la **riproducibilità del difetto didattico**, non la correttezza professionale del prototipo. Proseguire con il [README di M01](moduli/m01/README.md) per la discussione guidata.

## Convenzioni per i prossimi moduli

Ogni `moduli/mXX/` avrà un README con obiettivo, prerequisiti, step, comandi, risultati attesi e reset. Ogni `step-NN-*` conterrà input e file necessari per partire da quello step senza ricostruire manualmente gli step precedenti. Le integrazioni con MCP e servizi esterni avranno fixture locali più un percorso live esplicitamente separato. I risultati di riferimento vanno confrontati con prove ottenute sul proprio clone; token, credenziali e dati reali restano fuori dal repository.

Questo repository è separato dal sito del corso ed è pubblicato come [`thimotyb/demo-augmented-coding`](https://github.com/thimotyb/demo-augmented-coding).
