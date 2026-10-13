# Handoff — pubblicazione demo M04, M05, M06 e M11

- Data: 13 ottobre 2026. Branch: `main`. Commit dei contenuti: `2c1738f` (`Complete M04-M06 demos and add MemPalace lab`), a partire da `6e21d7f`.
- Ambito: viste architetturali M04, export Spectral e procedura M05, prototipo React UI-02 M06, laboratorio CLI MemPalace M11, skill di progetto per Codex e Claude Code, screenshot e documentazione di stato. Il vecchio handoff M04 conserva il contesto della sua realizzazione.
- Prove eseguite il 13 ottobre: `python3 scripts/check_m01.py` passato; `npm run build` in M06 passato con avvisi di bundling MUI sulle direttive `use client`; `bash moduli/m11/step-02-mempalace-room-wing/run-demo.sh` passato con tre drawer e le risposte documentate; `git diff --cached --check` passato.
- Spectral M05: `./lint.sh` eseguito. Gli esiti sono quelli attesi e documentati: OWASP 60 errori e 42 avvisi; URL Style Guidelines 2 errori, 3 avvisi e 1 informazione; Documentation 42 avvisi. Lo script termina con codice 1 per i finding, quindi la governance resta da valutare e non è una quality gate verde.
- M04: il rendering SVG è già versionato e riporta PlantUML 1.2026.8; in questa sessione `plantuml` non era installato, quindi il rendering non è stato ripetuto.
- Decisioni e limiti: UI-02 è un prototipo con fixture e non completa il task applicativo; il laboratorio M11 verifica la CLI e non la connessione MCP; idempotenza dell'intero checkout, creazione spedizione e scelte di deployment restano aperte.
- Prossimi passi: riesaminare i finding Spectral sul contratto, concordare le decisioni aperte M04/M06 e preparare separatamente la demo MCP e l'handoff M11. Prima di riprendere, verificare `git status`, commit e tag correnti.
