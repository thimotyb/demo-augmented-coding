# Decisione di team: checkout

Fonte: caso guida M01, stato didattico del 2026-10-07.

Il gateway di pagamento è simulato. I requisiti chiedono fino a tre tentativi per errori transitori. Non è ancora definita la politica per ripetere l'intero checkout dopo un errore, né la relazione tra `Idempotency-Key` del pagamento e unicità dell'ordine. L'agente deve fermarsi e chiedere una decisione prima di implementare questi aspetti.
