# M08 — Scansione SAST con SonarQube locale

Questa demo usa un **piccolo backend Java 21 sintetico** ispirato al checkout del [caso guida](../../../caso-guida/README.md). Non implementa l'API di M06 e non usa un gateway reale. Tre rischi intenzionali sono presenti per discutere il triage: una query SQL composta con input del client, una credenziale scritta nel codice e un generatore casuale non crittografico. **Non riutilizzare questo codice in un servizio reale.**

## Prerequisiti

- Docker Engine con `docker compose` (su WSL, attivare l'integrazione della distribuzione in Docker Desktop e avviare Docker Desktop).
- Java 21 e Maven 3.9 o compatibile.
- Circa 4 GB di RAM liberi per SonarQube e PostgreSQL.

## 1. Avviare SonarQube

Da questa cartella:

```bash
cp .env.example .env
# Sostituire la password d'esempio in .env con una password locale non riutilizzata.
docker compose up -d
docker compose ps
curl -fsS http://localhost:9000/api/system/status
```

Attendere `"status":"UP"` e inizializzare la **nuova installazione** con `python3 bootstrap_local.py`. Lo script cambia la password iniziale, crea il progetto `m08-checkout-sast` e genera un user token. Scrive le credenziali soltanto in `.sonar-local.env`, ignorato da Git e leggibile dal proprietario. Per caricare le variabili nella shell:

```bash
set -a
. ./.sonar-local.env
set +a
```

Aprire <http://localhost:9000> e accedere come `admin` usando `SONAR_ADMIN_PASSWORD` dal file locale. Lo script è pensato per una istanza appena creata; se `.sonar-local.env` esiste già, rifiuta di sovrascriverlo. Non copiare password o token nel repository.

La configurazione usa PostgreSQL con volumi Docker persistenti. La porta 9000 è esposta solo su `127.0.0.1`. `docker compose down` ferma i servizi e conserva i dati. `docker compose down -v` li elimina: usarlo solo per un reset intenzionale.

## 2. Compilare e inviare l'analisi

```bash
mvn -B -f backend/pom.xml clean verify
mvn -B -f backend/pom.xml org.sonarsource.scanner.maven:sonar-maven-plugin:5.5.0.6356:sonar \
  -Dsonar.host.url=http://localhost:9000
```

In SonarQube aprire il progetto `m08-checkout-sast`, quindi **Issues** e **Security Hotspots**. I candidati da esaminare sono `CheckoutRepository.findOrderByCustomer` (SQL concatenato), `PaymentGatewayClient.AUTH_TOKEN` (credenziale nel sorgente) e `PaymentGatewayClient.authorizationReference` (generatore casuale inadatto a un riferimento difficile da indovinare). Le regole attive e il numero di finding possono cambiare con il profilo di qualità e la versione del server.

### Risultato osservato il 7 ottobre 2026

Con SonarQube Community Build `26.9.0.129388`, profilo Java **Sonar way** e MCP `1.28.0.4397`, la ricerca MCP ha restituito:

| Ricerca | Esito | Triage iniziale |
| --- | --- | --- |
| Issue di sicurezza `java:S2077`, `CheckoutRepository.java:19` | Query SQL dinamica, severità `MAJOR`, stato `OPEN` | `customerId` entra nella query senza parametro JDBC: il percorso verso una SQL injection è concreto. Correzione proposta: `PreparedStatement` con parametro. |
| Issue di sicurezza `java:S2245`, `PaymentGatewayClient.java:8` | Uso di `Random`, severità `MAJOR`, stato `OPEN` | Il numero è prevedibile. La gravità dipende dal ruolo del riferimento: se autorizza operazioni, usare un generatore crittografico; se è solo un'etichetta, documentare perché non è un segreto. |
| `search_security_hotspots` | Zero hotspot | Non inventare hotspot a partire dalle issue. |
| Token hardcoded | Nessun finding rilevato in questa scansione | La revisione manuale resta necessaria; valutare una regola aggiuntiva o un controllo dei segreti. |
| `get_project_quality_gate_status` | `OK`, condizioni vuote nella risposta MCP | Il gate predefinito non ha bloccato queste issue nella prima analisi. In M09 configurare e provare una condizione che fallisca davvero. |

Questi risultati sono evidenza di questa esecuzione, non una promessa che ogni versione produca gli stessi alert.

## 3. Interrogare i risultati tramite MCP

Il [SonarQube MCP ufficiale](https://github.com/SonarSource/sonarqube-mcp-server) si configura nel client, non nel `compose.yaml`. Copiare il modello [mcp-codex.toml.example](mcp-codex.toml.example) nella configurazione personale di Codex; il processo Codex deve ereditare `SONARQUBE_TOKEN` dalla shell. Il container MCP condivide la rete Docker `m08-sonarqube_default` con il server e opera in sola lettura. Riavviare il client dopo averlo configurato.

Esempi di richieste all'agente, **dopo** l'analisi Maven:

1. «Usa `search_sonar_issues_in_projects` sul progetto `m08-checkout-sast`, filtrando `impactSoftwareQualities: [SECURITY]`. Riporta file, regola, gravità ed evidenza; non correggere ancora.»
2. «Usa `search_security_hotspots` sul progetto `m08-checkout-sast`, poi confronta il risultato con le issue di sicurezza. Per `java:S2077` indica quale input è controllabile dal client e se il percorso è sfruttabile nel codice mostrato.»
3. «Usa `get_project_quality_gate_status` per `m08-checkout-sast`. Spiega se il gate bloccherebbe questa revisione e confrontalo con le issue trovate.»
4. «Esamina anche il token hardcoded nel codice, benché questa scansione non lo abbia segnalato. Proponi un controllo dei segreti separato e una correzione verificabile.»

L'MCP legge risultati già prodotti dal SonarScanner; il prompt non sostituisce la scansione. Per un confronto prima/dopo, applicare una correzione alla volta, rieseguire `mvn verify` e SonarScanner e confrontare il finding nel progetto. Non classificare come vulnerabilità confermata un hotspot senza triage.

## Pipeline futura (M09)

Una pipeline raggiungibile dal server SonarQube può eseguire `mvn verify` e lo stesso goal Maven, passando `SONAR_TOKEN` come segreto CI. Aggiungendo `-Dsonar.qualitygate.wait=true`, il job attende il quality gate e fallisce se il gate fallisce. SonarQube Community Build è pensato qui per l'analisi del ramo principale; le funzioni complete di analisi dei rami/PR dipendono dall'edizione.

## Stato della verifica

`mvn clean verify` è riuscito. La scansione Maven è stata caricata con successo su SonarQube locale e le ricerche MCP in sola lettura hanno prodotto i risultati riportati sopra. La fixture non ha test funzionali: serve a esercitare analisi, triage e successiva correzione, non a dimostrare il comportamento del checkout.
