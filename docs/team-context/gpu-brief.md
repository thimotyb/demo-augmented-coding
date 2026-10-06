# Brief per il Codex sulla macchina GPU

Questo è un incarico **preparatorio** per M12, indipendente dal lavoro M02 in corso. La GPU dichiarata per il corso è una RTX 5060 Ti con 16 GB VRAM; rilevare i dati effettivi sulla macchina prima di scegliere modello e contesto.

## Prompt da passare al Codex della macchina GPU

> Clona `https://github.com/thimotyb/demo-augmented-coding.git`, avvia Codex dalla radice e leggi `AGENTS.md`, `docs/team-context/state.md` e `docs/team-context/gpu-brief.md`. Lavora in una branch `demo/m12-gpu-inventory`. Rileva GPU, VRAM, driver, RAM, sistema operativo, versioni di Ollama e Codex e modelli locali già presenti, senza installare o scaricare nulla. Scrivi in `moduli/m12/ambiente-gpu.md` una scheda sintetica con comandi, risultati pertinenti, data e limiti. Escludi seriali, hostname, nomi utente, token e percorsi personali. Esegui `git diff --check`, aggiorna un handoff in `docs/team-context/`, poi proponi una PR o condividi branch e commit. Non modificare M02.

## Comandi di rilevazione suggeriti

```bash
nvidia-smi --query-gpu=name,memory.total,driver_version --format=csv,noheader
free -h
ollama --version
ollama list
codex --version
```

Se un comando non è disponibile, annotarlo senza dedurre che il componente sia assente in assoluto. Per il primo passaggio bastano inventario e misure a riposo; i benchmark di modelli e l'installazione di runtime appartengono allo step M12 successivo. La scheda non deve contenere output grezzo che esponga dati della macchina.
