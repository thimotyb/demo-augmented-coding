# M02 — Step 01: due Plan Mode, un caso guida

## Preparazione riproducibile

Lavorare da un clone pulito di questo repository e annotare il commit (`git rev-parse HEAD`). Per un confronto controllato usare lo stesso commit e lo stesso contenuto dei file su entrambe le macchine. I file da leggere sono:

- [`demo.py`](../../m01/step-01-prototipo/demo.py), il prototipo intenzionalmente incompleto;
- [`ecomm-requirements.md`](../../../caso-guida/ecomm-requirements.md), sezioni Checkout e Payment authorization retry;
- [`req-seq-payment-retry.puml`](../../../caso-guida/req-seq-payment-retry.puml);
- [`req-apis.yaml`](../../../caso-guida/req-apis.yaml), `POST /payments`, `POST /orders` e `Idempotency-Key`.

Dalla radice:

```bash
python3 scripts/check_m01.py
git status --short
git rev-parse HEAD
```

Il check conferma che il punto di partenza mostri le lacune previste. Salvare il commit e il modello usato nel [modello di confronto](compare-template.md), senza copiare transcript completi nel repository.

## Parte offline: imparare a valutare il piano

Leggere il [piano superficiale](fixtures/piano-superficiale.md) e il [piano ragionato](fixtures/piano-ragionato.md). Sono **esempi editoriali**, non output attribuiti a Codex o Claude Code. Valutarli con la [rubrica](rubrica.md), compilare `compare-template.md` in una propria cartella di lavoro e confrontare con la [valutazione di riferimento](expected/valutazione.md). Questa parte funziona senza rete o accesso a LLM.

## Parte live: lo stesso prompt nei due client

1. Avviare Codex dalla radice del clone, scegliere `/plan` e incollare il contenuto di [`prompt-comune.md`](prompt-comune.md). Attendere il piano; annotare modello e versione del client. [OpenAI Docs: `/plan`](https://learn.chatgpt.com/docs/developer-commands).
2. In un secondo clone pulito allo **stesso commit**, avviare `claude --permission-mode plan` e incollare lo stesso prompt. Annotare modello e versione. [Claude Code: Plan Mode](https://code.claude.com/docs/en/permission-modes).
3. Confrontare piani e domande con la rubrica. Correggere a mano i punti non supportati dalle fonti. **Non uscire dal Plan Mode per implementare in questo step.**
4. Verificare con `git status --short` che il codice non sia cambiato. Se un client ha prodotto file di piano locali, identificarli e annotarli prima di pulire il clone. La valutazione considera il contenuto del piano, non il solo rispetto della modalità.

**Reset:** ripartire da un clone pulito al commit annotato. Non sono richiesti servizi esterni oltre ai client LLM nella parte live. I piani live variano tra esecuzioni; sono riproducibili **input, condizioni e criteri di valutazione**, non il testo generato dal modello. L'output personale può stare in `local-output/`, escluso da Git.
