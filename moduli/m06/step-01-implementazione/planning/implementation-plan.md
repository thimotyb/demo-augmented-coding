# M06 Implementation Plan — Checkout API and React UI

## Purpose and status

This plan turns the checkout case guide into a sequence of reviewable coding-agent tasks. It is a planning artifact only: no task below is implemented by this document. The project-specific stack constraints are Java 21, a Spring Boot API, and a React user interface using Material UI. The exact framework and dependency versions, Maven archetype, and repository layout still need approval because the source specifications do not define them.

The payment gateway remains deterministic and simulated. M05's Prism/Postman playbooks are useful contract and regression references; they do not prove backend state, retry-loop behavior, or server-side idempotency.

## Sources and current baseline

| Source | Use in this plan |
| --- | --- |
| `caso-guida/ecomm-requirements.md` | Domain, checkout flow, payment authorization retry and order creation sequence |
| `caso-guida/req-seq-checkout-success.puml` | Successful checkout ordering and expected interactions |
| `caso-guida/req-seq-payment-retry.puml` | At most three authorization attempts, stopping on approval, customer notification after exhaustion |
| `caso-guida/req-domain-entities.puml` | Domain concepts and relationships |
| `caso-guida/req-apis.yaml` | Source API contract |
| `moduli/m03/step-02-speckit/specifiche-speckit/specs/001-payment-retry/spec.md` | FR-1–FR-3, AC-1–AC-3, explicit exclusions and unresolved behavior |
| `moduli/m03/step-02-speckit/specifiche-speckit/specs/001-payment-retry/tasks.md` | Acceptance gate and reference test scenarios |
| `moduli/m03/step-02-speckit/specifiche-speckit/specs/001-payment-retry/decisions.md` | Open payment and checkout decisions |
| `moduli/m04/step-01-architettura/architecture-requirements.md` and its diagrams | Architectural boundaries and requirement traceability |
| `moduli/m05/step-01-contratto/openapi/checkout-api.yaml` | Reduced OAS profile with named examples |
| `moduli/m05/step-01-contratto/scenarios/postman-playbooks.yaml` | Scenario-to-operation mapping for the existing Postman playbooks |
| `moduli/m05/step-01-contratto/README.md` | Current mock, Newman, and contract-test limitations |

M06 currently contains only README scaffolding. No Java API, React application, Maven archetype output, or M06 test command exists yet. Proposed paths and commands below are therefore targets to confirm when the project layout is approved, not existing repository facts.

## Decisions required before implementation

| ID | Decision | Why it blocks work | Suggested owner |
| --- | --- | --- | --- |
| D-01 | Select exact Spring Boot, Maven plugin, React, Node/npm, and Material UI versions; define how versions are pinned | Reproducible project generation and builds require fixed versions; the course constraint specifies technology families, not releases | Course maintainer |
| D-02 | Select Maven archetype or project generator, and decide whether the repository is a monorepo with `backend/` and `frontend/` | The current M06 scaffold only says Maven; it does not identify a generator or layout | Course maintainer |
| D-03 | Approve the reduced API surface and error response for exhausted retries against M05 OAS examples | The OpenAPI profile models HTTP requests/responses, but does not by itself implement the internal gateway retry loop; the exact customer-facing failure representation needs confirmation | API owner |
| D-04 | Decide the demo's persistence boundary: in-memory state for one process, or another explicitly scoped approach | The sources do not require persistence; a teaching demo should not imply durability or concurrency guarantees | Course maintainer |
| D-05 | Resolve or explicitly defer `decisions.md` D-1 through D-6, especially key scope/reuse, timeout classification, and exhausted-retry API response | These affect safe idempotency and externally observable behavior | Product/API owner |
| D-06 | Decide whether shipment is omitted or represented by a declared stub in M06 | The successful sequence includes shipment kickoff, while the M05 profile documents that the contract lacks a matching `createShipment` operation | Product/API owner |

Until D-03 and D-05 are resolved, implement only behavior already specified by AC-1 through AC-3. Do not silently fill the gaps with guessed API semantics. D-06 can be deferred if the M06 slice is explicitly limited to checkout and payment.

## Stages and tasks

Each task begins with one status checkbox: `- [ ] DONE`. An unchecked box means the task is not done. After the acceptance criteria are verified, check it as `- [x] DONE`. A task is not complete merely because its plan has been reviewed.

### STAGE-01 — Confirm the implementation boundary

- [ ] **DONE** — `DEC-01` — Approve stack versions and repository layout

  - **Result:** A short decision record pins Java 21, Spring Boot and build plugin versions, Node/npm, React and Material UI versions, project generator/archetype, and the backend/frontend directory layout.
  - **Coverage:** Reproducibility prerequisite; M06 README currently expects a Maven project and API/UI build but has no version or layout details.
  - **Dependencies:** None.
  - **Likely files:** `moduli/m06/step-01-implementazione/decisions.md` (new); M06 README files (existing).
  - **Steps:** Compare available generators; select one that creates a Java 21 Spring Boot baseline; record exact versions and generated files; record the chosen commands.
  - **Verification:** Review the decision record; verify generator coordinates and commands against the pinned tool versions. No command is currently defined in this repository.
  - **Acceptance:** Another learner can identify the exact supported JDK, project generator, dependency versions, and target directories without guessing.
  - **Risk/blocker:** Versions can age; keep them centralized and update them deliberately.

- [ ] **DONE** — `DEC-02` — Confirm API slice and unresolved behavior

  - **Result:** A reviewed scope record lists the M05 operations used, request/response examples, retry behavior, and any unresolved decisions that remain excluded.
  - **Coverage:** `spec.md` FR-1–FR-3 and AC-1–AC-3; source OAS and M05 reduced profile.
- **Dependencies:** None; can be reviewed in parallel with DEC-01.
  - **Likely files:** `moduli/m06/step-01-implementazione/decisions.md` (new); possibly a copied M06 OAS slice only if approved.
  - **Steps:** Compare source OAS and M05 profile; identify mismatches and missing exhausted-retry response; explicitly select checkout, payment, and shipment boundaries.
  - **Verification:** Manual contract review; reuse `cd moduli/m05/step-01-contratto && npm run test:playbooks` for the existing M05 baseline.
  - **Acceptance:** Scope has no implicit endpoint or response; open decisions are marked blocked or out of scope.
  - **Risk/blocker:** M05 playbooks use fixed Prism examples and do not exercise backend state or gateway retries.

### STAGE-02 — Generate and verify the backend foundation

- [ ] **DONE** — `PROJ-01` — Generate the Java 21 Spring Boot project

  - **Result:** A minimal backend project generated with the approved Maven archetype/generator and pinned versions.
  - **Coverage:** M06 expected API build; architecture boundaries in M04.
  - **Dependencies:** DEC-01, DEC-02.
  - **Likely files:** New `moduli/m06/step-01-implementazione/backend/` project, Maven wrapper, `pom.xml`, and a brief generation record.
  - **Steps:** Run the approved generator; inspect dependencies and packages; remove unused generated examples; retain wrapper and reproducible configuration.
  - **Verification:** Exact build command to be recorded after generator approval, likely `./mvnw test`; this command is not yet available or verified.
  - **Acceptance:** Clean checkout can compile/test with the documented JDK and wrapper; generated project does not contain secrets or environment-specific paths.
  - **Risk/blocker:** Do not choose a generator or unpinned “latest” dependency implicitly.

- [ ] **DONE** — `API-01` — Map the approved OAS slice to backend resources

  - **Result:** A documented mapping from approved M05 operations and schemas to controller, service, and DTO responsibilities.
  - **Coverage:** OAS operations used by `checkout-api.yaml` and scenarios in `postman-playbooks.yaml`.
  - **Dependencies:** DEC-02, PROJ-01.
  - **Likely files:** New backend API packages and optionally `api-mapping.md` under the M06 step.
  - **Steps:** Inspect existing OAS paths and schemas; define only the approved subset; preserve example field names and status semantics; report gaps instead of adding operations.
  - **Verification:** Compare the mapping with `openapi/checkout-api.yaml`; use a contract validator selected in DEC-01/DEC-02. A backend-specific command is to be defined.
  - **Acceptance:** Every implemented public operation maps to an OAS operation; no undocumented public endpoint is added.
  - **Risk/blocker:** The case-guide contract and reduced M05 profile are not interchangeable; trace any difference before coding.

- [ ] **DONE** — `PAY-01` — Implement the deterministic simulated gateway adapter

  - **Result:** An injectable gateway interface plus local fixture implementation can return configured approval and transient-failure sequences.
  - **Coverage:** `spec.md` AC-1 through AC-3; `req-seq-payment-retry.puml`.
  - **Dependencies:** PROJ-01, DEC-02.
  - **Likely files:** New backend payment gateway interface, fixture adapter, and configuration/test fixtures.
  - **Steps:** Keep external I/O absent; make fixture sequences explicit; record the number and order of authorization calls.
  - **Verification:** Unit tests with first-attempt approval, transient-then-approved, and three transient failures; command to be defined with the selected build.
  - **Acceptance:** Tests can deterministically control outcomes and observe call count; no credentials or real provider dependency exists.
  - **Risk/blocker:** Do not add retries for definitive declines or unknown timeout outcomes before D-05 is decided.

- [ ] **DONE** — `PAY-02` — Implement only the approved retry state transition

  - **Result:** Checkout retries transient authorization failures up to three total attempts, stops on approval, and reports exhausted retries without placing an order.
  - **Coverage:** `spec.md` FR-1–FR-3 and AC-1–AC-3; payment retry sequence diagram.
  - **Dependencies:** API-01, PAY-01, DEC-02.
  - **Likely files:** New payment/checkout application service and unit tests.
  - **Steps:** Keep the retry loop bounded; return the approved `paymentId`; ensure order placement is reachable only after approval; surface an exhausted result using the response agreed in DEC-02.
  - **Verification:** Unit tests assert exactly one call on immediate approval, two calls on transient-then-approved, exactly three on exhaustion, and no order after exhaustion.
  - **Acceptance:** All three acceptance scenarios pass; no fourth call or order on exhausted authorization.
  - **Risk/blocker:** D-1–D-6 remain separate; retrying a gateway call does not establish whole-checkout idempotency.

- [ ] **DONE** — `ORD-01` — Implement the minimal order transition after approval

  - **Result:** The successful checkout path creates an order from the approved payment and returns the contract-defined result.
  - **Coverage:** `req-seq-checkout-success.puml`, `ecomm-requirements.md`, approved order operations in M05.
  - **Dependencies:** API-01, PAY-02, DEC-02.
  - **Likely files:** New order/checkout application service and tests.
  - **Steps:** Create an order only after receiving an approved `paymentId`; keep order state within the scope selected in DEC-04; omit shipment unless D-06 chooses a stub.
  - **Verification:** Integration/unit test asserts approved payment precedes order creation and failed payment creates no order; command to be defined.
  - **Acceptance:** The demonstrated transition matches the approved sequence and does not claim durability or duplicate-checkout protection.
  - **Risk/blocker:** Checkout-wide idempotency remains unresolved and is not inferred from payment idempotency.

### STAGE-03 — Build the React checkout interface

- [ ] **DONE** — `UI-01` — Create the React and Material UI application shell

  - **Result:** A reproducible frontend skeleton using the versions and layout approved in DEC-01.
  - **Coverage:** M06 UI objective; Material UI component research is input, not a runtime dependency unless chosen.
  - **Dependencies:** DEC-01.
  - **Likely files:** New `moduli/m06/step-01-implementazione/frontend/`, package manifest/lockfile, entry point, and start/build scripts.
  - **Steps:** Generate the selected React baseline; add Material UI with pinned versions; render a checkout screen shell with semantic headings, labels, and responsive layout.
  - **Verification:** `npm ci` and `npm run build` only after scripts are defined; neither command currently exists for M06.
  - **Acceptance:** Clean install/build succeeds with documented Node version; no API behavior is simulated in production UI state.
  - **Risk/blocker:** Avoid relying on an MCP at runtime; the MCP is a documentation/design aid, while the app must build from checked-in dependencies.

- [ ] **DONE** — `UI-02` — Render checkout, progress, success, and failure states

  - **Result:** Checkout UI displays amount and confirmation action, pending state, approved result, and exhausted-retry guidance.
  - **Coverage:** Checkout and payment scenarios from the case guide; M06 course UI requirements.
  - **Dependencies:** UI-01, DEC-02, PAY-02.
  - **Likely files:** New React page/components and focused component tests if test tooling is approved.
  - **Steps:** Bind visible states to the approved API response; provide accessible labels and error feedback; prevent duplicate submission while a request is pending without claiming backend idempotency.
  - **Verification:** Component tests and keyboard/browser walkthrough; exact commands/tooling to be selected in DEC-01.
  - **Acceptance:** All user-visible states are reachable with deterministic fixtures; keyboard focus and error text are observable.
  - **Risk/blocker:** UI cannot invent an error response shape before DEC-02.

- [ ] **DONE** — `UI-03` — Connect UI to the local backend

  - **Result:** The confirmation action calls the approved backend endpoint and displays its response.
  - **Coverage:** M05 OAS profile and successful checkout/retry scenarios.
  - **Dependencies:** API-01, PAY-02, ORD-01, UI-02.
  - **Likely files:** New frontend API client/configuration and local-development documentation.
  - **Steps:** Use a configurable local base URL; handle pending, HTTP failure, and valid responses; document how to start backend and frontend together.
  - **Verification:** Browser or end-to-end test against the local fixture backend; exact command to be defined after stack/layout approval.
  - **Acceptance:** One successful and one exhausted-retry flow are demonstrated end-to-end with no external services.
  - **Risk/blocker:** CORS and local port choices are implementation decisions to document, not contract semantics.

### STAGE-04 — Protect the contract and make the demo repeatable

- [ ] **DONE** — `TEST-01` — Add backend acceptance and regression tests

  - **Result:** Automated tests cover AC-1–AC-3 and order sequencing, with clear failure messages.
  - **Coverage:** `spec.md`, `tasks.md`, M05 playbook baseline.
  - **Dependencies:** PAY-02, ORD-01.
  - **Likely files:** New backend unit/integration test packages.
  - **Steps:** Keep gateway fixture deterministic; assert attempt count, stop-on-success, no order on exhaustion, and response schema; preserve tests around decisions explicitly excluded.
  - **Verification:** Approved Maven wrapper test command; compare public request/response fixtures with M05 examples.
  - **Acceptance:** The suite passes from a clean checkout; a deliberate regression in attempt count or order gating fails a test.
  - **Risk/blocker:** Existing Prism playbooks cannot substitute for stateful backend tests.

- [ ] **DONE** — `TEST-02` — Verify UI and API contract together

  - **Result:** A reproducible check shows that frontend requests and backend responses follow the approved OAS examples and scenarios.
  - **Coverage:** M05 `checkout-api.yaml`, playbook scenario map, and the M06 UI flows.
  - **Dependencies:** UI-03, TEST-01, DEC-02.
  - **Likely files:** New integration test or test script; possible OAS validation configuration.
  - **Steps:** Reuse M05 request/response fixtures where appropriate; run the backend and exercise success and exhausted-retry flows; keep Prism mock checks identified as a separate contract baseline.
  - **Verification:** Existing baseline command: `cd moduli/m05/step-01-contratto && npm run test:playbooks`. New M06 command is to be defined after implementation layout is approved.
  - **Acceptance:** The baseline and M06 checks pass independently; documentation states what each one does and does not prove.
  - **Risk/blocker:** The OAS profile currently does not establish backend retry internals or persistence.

- [ ] **DONE** — `DOC-01` — Document setup, execution, evidence, and reset

  - **Result:** M06 README explains prerequisites, generation provenance, exact commands, expected output, test coverage, known limits, and reset behavior.
  - **Coverage:** Course demo reproducibility and safe handoff.
  - **Dependencies:** PROJ-01, UI-03, TEST-01, TEST-02.
  - **Likely files:** `moduli/m06/README.md` and `moduli/m06/step-01-implementazione/README.md` (existing); decision and test records (new).
  - **Steps:** Document clean-clone setup and local-only execution; distinguish generated scaffold from manually reviewed implementation; link plan task codes to evidence.
  - **Verification:** Follow instructions from a clean checkout or isolated clone; check links and commands.
  - **Acceptance:** A learner can reproduce the build and tests without relying on an undocumented local state, secret, or external service.
  - **Risk/blocker:** Do not label the scaffold as executable until the clean-clone procedure succeeds.

## Requirement and scenario traceability

| Requirement/scenario | Planned tasks | Verification |
| --- | --- | --- |
| Java 21, Spring Boot API, React + Material UI | DEC-01, PROJ-01, UI-01 | Decision record; clean backend/frontend build |
| M05 API contract operations and named examples | DEC-02, API-01, TEST-02 | OAS comparison; existing Newman/Prism playbooks; M06 contract integration check |
| Immediate payment approval and checkout continuation (AC-1) | PAY-01, PAY-02, ORD-01, TEST-01, UI-02/UI-03 | Gateway call count is 1; approved `paymentId` precedes order; UI success state |
| Transient error then approval (AC-2) | PAY-01, PAY-02, TEST-01 | Exactly 2 calls; stop after approval; order follows approval |
| Three transient errors and customer notification (AC-3) | DEC-02, PAY-01, PAY-02, TEST-01, UI-02/UI-03 | Exactly 3 calls; no order; agreed failure response and UI guidance |
| Checkout success sequence | API-01, ORD-01, UI-03, TEST-02 | Integration check against approved OAS slice |
| Shipment kickoff in source sequence | DEC-06 | Explicitly include approved stub or document exclusion; no endpoint invented |
| Payment idempotency header | DEC-02, DEC-05, TEST-02 | Contract conformance only; server-side key semantics remain blocked until decided |
| Whole-checkout duplicate prevention | DEC-05 | Not implemented by this plan; separate approved decision and tests required |

## Explicitly out of scope

- Real payment provider, Stripe, capture, reconciliation, credentials, or real transactions.
- Real catalog, login, inventory, fulfillment, carrier integration, or persistence beyond the boundary approved for the demo.
- Guaranteeing one order across repeated checkout submissions or concurrent requests.
- Defining `Idempotency-Key` generation, scope, retention, payload-mismatch handling, or replay semantics without an approved decision.
- Retrying definitive declines, unknown timeout outcomes, or adding backoff/timeouts not specified by the approved scope.
- Treating Prism's fixed examples or M05 playbooks as proof of a stateful service implementation.

## First executable task

After the human decisions, **PROJ-01** is the first implementation task: generate the minimal Java 21 Spring Boot project using the approved, pinned Maven generator and record its reproducible build command. DEC-01 and DEC-02 are review/decision tasks and should be completed first; no coding task is unblocked until the project layout and API slice are agreed.

## Execution roadmap — suggested order and status

The order below respects task dependencies and shows where work can proceed in parallel. Status values mirror the detailed checklists above; all task boxes are currently unchecked, so none is done.

| Order | Stage | Task | Depends on | Status | Execution note |
| ---: | --- | --- | --- | --- | --- |
| 1 | STAGE-01 | `DEC-01` — Approve stack versions and repository layout | — | `[ ] DONE` | Human decision gate |
| 2 | STAGE-01 | `DEC-02` — Confirm API slice and unresolved behavior | — | `[ ] DONE` | Can be reviewed in parallel with DEC-01 |
| 3 | STAGE-02 | `PROJ-01` — Generate Java 21 Spring Boot project | DEC-01, DEC-02 | `[ ] DONE` | First implementation task after decision gates |
| 4 | STAGE-02 | `API-01` — Map approved OAS slice to backend | DEC-02, PROJ-01 | `[ ] DONE` | Establishes the backend contract boundary |
| 5 | STAGE-02 | `PAY-01` — Add deterministic simulated gateway | PROJ-01, DEC-02 | `[ ] DONE` | Can proceed in parallel with API-01 |
| 6 | STAGE-03 | `UI-01` — Create React and Material UI shell | DEC-01 | `[ ] DONE` | Can proceed alongside backend work after stack approval |
| 7 | STAGE-02 | `PAY-02` — Implement approved retry transition | API-01, PAY-01, DEC-02 | `[ ] DONE` | Requires agreed API boundary and gateway fixture |
| 8 | STAGE-02 | `ORD-01` — Create order only after approval | API-01, PAY-02, DEC-02 | `[ ] DONE` | Keep checkout-wide idempotency out of scope |
| 9 | STAGE-03 | `UI-02` — Render checkout and outcome states | UI-01, DEC-02, PAY-02 | `[ ] DONE` | Bind UI to approved response semantics |
| 10 | STAGE-03 | `UI-03` — Connect UI to local backend | API-01, PAY-02, ORD-01, UI-02 | `[ ] DONE` | Local end-to-end flow |
| 11 | STAGE-04 | `TEST-01` — Add backend acceptance/regression tests | PAY-02, ORD-01 | `[ ] DONE` | Can run in parallel with UI work after backend behavior exists |
| 12 | STAGE-04 | `TEST-02` — Verify UI and API contract together | UI-03, TEST-01, DEC-02 | `[ ] DONE` | Distinguish backend tests from Prism mock checks |
| 13 | STAGE-04 | `DOC-01` — Document setup, evidence, and reset | PROJ-01, UI-03, TEST-01, TEST-02 | `[ ] DONE` | Finalize after commands and clean-clone checks are known |
