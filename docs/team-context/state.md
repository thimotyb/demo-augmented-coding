# Stato condiviso del progetto

**Aggiornato:** 6 ottobre 2026. Questa nota orienta il passaggio tra macchine; verificare sempre il branch e i file prima di agire.

## Decisioni stabili

- Repository delle demo: `thimotyb/demo-augmented-coding`; specifiche del corso: `thimotyb/corso-augmented-coding`.
- Caso guida unico: checkout, autorizzazione simulata, esito e retry. I sette file in `caso-guida/` sono una copia invariata di `plant-uml-example` al commit `654dfb269faada6a82d292f20a6346c039b5dd2b`.
- Git contiene le istruzioni e gli handoff comuni. Obsidian può consultare il clone; MemPalace può indicizzare note approvate. Le chat personali non sono il deposito comune.

## Moduli

| Modulo | Stato verificato | Prossimo passo |
| --- | --- | --- |
| M01 | Demo offline pronta; `python3 scripts/check_m01.py` passa | Conservare come baseline; usare le tre lacune per M02/M03 |
| M02 | Step 01 predisposto: prompt comune, criteri e piani editoriali di esempio | Eseguire Codex e Claude Code in Plan Mode sullo stesso commit, registrare differenze e decisioni |
| M03–M12 | Scaffold documentale | Implementare in ordine, una demo riproducibile per volta |
| M13 | Scaffold per modelli locali | Macchina GPU: rilevare ambiente e provare i task solo quando lo step sarà preparato |

## Domande aperte

- Definire idempotenza dell'intero checkout e rapporto con `Idempotency-Key` di `POST /payments`; la fonte non la specifica completamente.
- Fissare versioni e tag dei modelli per i laboratori sulla GPU da 16 GB; registrare misure sul sistema reale prima di dichiarare un confronto.

## Coordinamento

La macchina GPU può iniziare dal [brief di inventario M13](gpu-brief.md) in branch separato. Chi lavora su M02 evita di modificare i file M13. Ogni passaggio usa [`handoff-template.md`](handoff-template.md) e una PR o un commit identificabile.
