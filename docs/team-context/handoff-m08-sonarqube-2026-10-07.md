# Handoff M08 — SonarQube locale

- Data: 7 ottobre 2026.
- Branch/commit: `main` a `46c9d8f` prima delle modifiche M08; il lavoro M08 non è ancora committato.
- Scopo: demo indipendente M08 con checkout Java 21 sintetico, SonarQube Community Build + PostgreSQL via Docker Compose, SonarScanner Maven e modello di configurazione MCP in sola lettura.
- Decisione: il codice Java contiene vulnerabilità intenzionali per il triage; non è il backend M06 e non implementa l'API del caso guida.
- Verifiche eseguite: `mvn -B -f backend/pom.xml clean verify` riuscito; `docker compose config --quiet` riuscito; `python3 -m py_compile bootstrap_local.py` riuscito; `git diff --check` riuscito; `.env`, `.sonar-local.env` e `backend/target/` verificati come ignorati da Git.
- Stato runtime: Compose avviato; SonarQube Community Build `26.9.0.129388` ha risposto `UP`; analisi Maven caricata con successo; MCP `1.28.0.4397` interrogato in sola lettura. Due issue di sicurezza osservate: `java:S2077` SQL dinamico e `java:S2245` `Random`; zero security hotspot; quality gate `OK` con condizioni vuote. Il token hardcoded non è stato rilevato.
- Credenziali: password PostgreSQL locale in `.env`; password admin e token SonarQube in `.sonar-local.env`; entrambi i file sono ignorati da Git. Non copiarli in commit o output.
- Prossimo passo: in M08 scegliere una issue per fix e seconda scansione; in M09 configurare un quality gate che blocchi una regressione reale. Verificare che Docker Desktop sia avviato nelle sessioni successive.
