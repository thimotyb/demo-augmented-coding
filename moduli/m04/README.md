# M04 — Progettazione e PlantUML

**Stato: struttura predisposta; contenuto della demo da definire a partire dai requisiti architetturali.**

**Obiettivo previsto:** Partire da requisiti architetturali Markdown e usare l'AI per elaborare un diagramma PlantUML di architettura e una vista ArchiMate che colleghi processo di business, servizi/API e backend. Il caso resta quello in [caso-guida](../../caso-guida/README.md).

| Step | Input previsto | Output previsto |
| --- | --- | --- |
| [step-01-architettura](step-01-architettura/README.md) | Requisiti architetturali Markdown, caso checkout e contratto API | ADR, diagramma di architettura e vista ArchiMate PlantUML a tre livelli |

**TODO:** definire `architecture-requirements.md` con obiettivi, vincoli, attributi di qualità, confini, responsabilità, decisioni aperte e ID stabili. Generare da quella fonte:

1. una vista architetturale PlantUML con componenti, servizi, API, backend e sistemi esterni;
2. una vista ArchiMate PlantUML che mappi il processo di business ai servizi e alle API applicative, quindi ai backend che li realizzano.

Usare una legenda stabile: livello business giallo, livello applicativo azzurro, livello tecnologico verde. Tracciare i requisiti fino a elementi e relazioni dei diagrammi e agli endpoint del contratto M05; non introdurre operazioni API non presenti nella specifica. Aggiungere sorgenti, render, istruzioni e controlli di tracciabilità nello step quando la demo viene realizzata.

Sintassi di riferimento: [PlantUML ArchiMate](https://plantuml.com/archimate-diagram).
