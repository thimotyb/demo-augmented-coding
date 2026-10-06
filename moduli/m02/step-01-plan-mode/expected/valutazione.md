# Valutazione di riferimento dei piani editoriali

Il **Piano A** dovrebbe ricevere 0–1 punti per quasi tutte le voci. Dice “tre volte” senza specificare *solo* errori transitori, assume che `cartId` deduplichi l'ordine senza fonte e propone “aggiornare API e test” senza casi verificabili. Può essere una bozza iniziale, ma non è autorizzabile.

Il **Piano B** copre le sei voci della rubrica: cita file, distingue quanto è scritto da quanto va deciso, separa idempotenza del pagamento dall'unicità dell'ordine e propone test che espongono l'attuale difetto. Anche questo piano richiede una scelta umana su retry e deduplicazione prima degli edit; la rubrica valuta la qualità della preparazione, non garantisce la correttezza futura del codice.

Una risposta valida può formulare alternative diverse. Il punto indispensabile è non trasformare un'ipotesi in un requisito già approvato.
