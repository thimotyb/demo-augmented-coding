# Handoff — M03 Spec Kit

- Data e autore/macchina: 6 ottobre 2026, Codex locale.
- Branch e commit di partenza: `main`, `c766473` dopo `git pull --ff-only`.
- Obiettivo e criterio di accettazione: introdurre il caso checkout/retry tramite il quickstart GitHub Spec Kit, con artefatti separati, mini demo implementabile e ambiguità esplicite.
- File modificati e motivo: `moduli/m03/README.md`, `moduli/m03/step-02-speckit/` (guida, prompt, artefatti, starter/test/soluzione di riferimento), `README.md` (stato M03), `docs/team-context/state.md` (stato e prossimo passo).
- Decisioni confermate, con fonte: il caso specifica fino a tre tentativi per errori transitori, arresto all'approvazione e notifica con alternativa dopo tre fallimenti; le ambiguità su chiave, timeout e checkout ripetuto restano aperte in `decisions.md`.
- Comandi eseguiti ed esiti osservati: `git pull --ff-only` riuscito; i tre test dello starter danno un pass e due fallimenti attesi; i tre test sulla soluzione di riferimento passano. Il sito del corso segnala 14 pagine e zero link rotti; 16 file Markdown demo hanno zero link locali mancanti. Il CLI Spec Kit e il flusso live non sono stati eseguiti.
- Problemi aperti e assunzioni da verificare: verificare il flusso live con una versione corrente di Spec Kit e con le integrazioni Claude e Codex; confrontare gli output con gli artefatti di riferimento. I comandi disponibili possono variare con l'integrazione e la versione.
- Prossimo passo concreto: eseguire il quickstart in due workspace temporanei, una volta con `--integration claude --script py` e una con `--integration codex --script py`; usare gli stessi input e registrare differenze e decisioni.
- Branch/commit da cui riprendere: `main` a partire da `c766473`; modifiche M03 locali da rivedere e committare.
