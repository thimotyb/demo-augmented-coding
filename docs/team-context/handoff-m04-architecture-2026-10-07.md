# Handoff — M04 requisiti e viste architetturali

- Data e autore/macchina: 2026-10-07, Codex (ambiente condiviso)
- Branch e commit di partenza: `main`, `a1fa5d7` (`Aggiungi playbook Postman al modulo M05`)
- Obiettivo e criterio di accettazione: realizzare la fonte di requisiti architetturali, una vista di componenti e una vista ArchiMate a tre livelli; mantenere tracciabilità e non inventare API.
- File modificati e motivo: `moduli/m04/README.md`, `moduli/m04/step-01-architettura/README.md`, `architecture-requirements.md`, `architecture-view.puml/.svg`, `archimate-view.puml/.svg`; `README.md` e `docs/team-context/state.md` allineati allo stato eseguibile.
- Decisioni confermate, con fonte: il diagramma usa le sequenze e `caso-guida/req-apis.yaml`; `Cart.total()` e `createShipment(orderId)` sono interne e non diventano endpoint. Checkout orchestrator e servizi sono un'allocazione logica proposta. Runtime, persistenza, deployment, SLA e consistenza restano aperti. Gateway di pagamento e carrier sono servizi applicativi esterni.
- Comandi eseguiti ed esiti osservati: PlantUML `1.2026.8`, sorgenti renderizzati in SVG senza errori; anteprima PNG controllata visivamente; entrambi i rendering salvati accanto ai sorgenti.
- Problemi aperti e assunzioni da verificare: la fonte OpenAPI non specifica la creazione della spedizione; la sequenza la richiede. La proposta non determina se i componenti sono moduli o servizi distribuibili.
- Prossimo passo concreto: rivedere con il team la separazione dei componenti e assegnare owner alle decisioni aperte se serve passare alla realizzazione.
- Branch/commit da cui riprendere: `main` dopo la pubblicazione delle modifiche M04.
