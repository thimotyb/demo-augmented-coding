Esamina il prototipo `moduli/m01/step-01-prototipo/demo.py` e il caso in `caso-guida/`: `ecomm-requirements.md`, `req-seq-payment-retry.puml` e `req-apis.yaml`.

Prepara **solo un piano**, senza modificare file, per gestire gli errori transitori di autorizzazione del pagamento e le richieste ripetute di checkout. Il piano deve:

1. citare le fonti effettive e distinguere requisiti espliciti da scelte ancora aperte;
2. spiegare il rapporto tra retry dell'autorizzazione, `Idempotency-Key` su `POST /payments` e rischio di creare più ordini;
3. proporre alternative e domande da risolvere prima degli edit, senza inventare garanzie già definite;
4. indicare file probabili, passi piccoli e test che fallirebbero sul prototipo attuale;
5. chiarire cosa resta fuori dallo scope di questo primo intervento.

Non eseguire implementazione, comandi che cambiano file, commit o push. Se il caso non basta per decidere un comportamento, evidenzialo come decisione del team.
