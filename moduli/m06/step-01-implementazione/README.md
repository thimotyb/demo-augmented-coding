# M06 — step-01-implementazione

**Stato: piano disponibile; nessun codice API o UI è incluso in questo step.**

- [Prompt di pianificazione](prompt-implementation-plan.md): istruzioni per analizzare le fonti e produrre il piano senza modificare il codice.
- [Piano di implementazione](planning/implementation-plan.md): risultato applicato al caso guida, con task codificati, stadi, dipendenze e verifiche. Ogni task parte da `[ ] DONE`; spunta la casella solo quando i criteri di accettazione sono verificati. La tabella finale riassume ordine suggerito, dipendenze, parallelismo e stato.
- [Skill `implementa-task-dal-piano`](skills/implementa-task-dal-piano/SKILL.md): legge un task del piano e il work item collegato tramite Azure DevOps MCP, controlla prerequisiti e fonti, implementa solo il task eseguibile e registra le verifiche. È una skill sorgente da provare prima di usarla per generare codice.
- Input per i task successivi: decisioni approvate e fonti indicate nel piano.
- Output previsto dei task successivi: API Java 21/Spring Boot e front-end React/Material UI verificabili localmente.

Per richiedere un task all'agente, citarne il codice (per esempio `PAY-02`), chiedergli di controllare dipendenze e criteri nel piano e di fermarsi in presenza di decisioni ancora aperte. Il piano non dichiara che build o test M06 siano già disponibili.

Per provare la skill, indicare il suo `SKILL.md`, il codice task e l'ID o URL del work item ADO. Se l'ID non è noto, la skill lo cerca via MCP e procede solo con una corrispondenza univoca. Il file `SKILL.md` è il sorgente della skill; un eventuale pacchetto `.skill` si potrà generare da questa cartella senza duplicare le istruzioni versionate.

Riferimento comune: [caso guida](../../../caso-guida/README.md).
