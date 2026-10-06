# M12 — Compare local coding agents

This lab connects Claude Code and OpenCode to Ollama, then asks multiple local
models to solve the same Java 21 task. Run every candidate from a fresh copy of
the fixture and keep the prompt unchanged.

## 1. Architecture

```text
Claude Code ── Anthropic Messages API ─┐
                                      ├── Ollama ── local model
OpenCode ──── OpenAI-compatible API ──┘
```

The agent decides which files and tools to use. The model proposes the decisions.
The API adapter translates the messages. A successful connection therefore does
not guarantee successful tool use or good code.

## 2. Prerequisites

- Java 21 (`java` and `javac`)
- Git
- Ollama 0.30 or newer
- Claude Code 2.x for the first demonstration
- OpenCode for the open-source TUI demonstration
- enough memory for the selected model; do not run all models concurrently

Check the environment:

```bash
java -version
javac -version
git --version
ollama --version
claude --version
opencode --version
```

Install each tool from its official instructions. Pull the baseline model and
verify the local server:

```bash
ollama pull qwen3.5:9b
ollama list
curl http://localhost:11434/api/version
curl http://localhost:11434/api/tags
```

If `curl` cannot connect, start `ollama serve` in another terminal. Desktop and
system-service installations may already have it running.

## 3. Configure a large context window

Coding agents send the model a system prompt, tool definitions, conversation
history, file contents, and tool results. Ollama defaults to only 4K on GPUs with
less than 24 GiB of VRAM, which is too small for a realistic agent loop. This lab
uses **64K (65536 tokens)**. Use 32K as a fallback if 64K causes CPU offload or an
out-of-memory error.

There are two separate settings and they must agree:

- Ollama must allocate the real runtime context;
- OpenCode must know that model's input and output limits.

Changing only OpenCode does not increase Ollama's allocation. Changing only
Ollama leaves OpenCode with incorrect token-budget information.

### 3.1 Configure Ollama started manually

Stop any existing Ollama server, then launch it from a dedicated terminal:

```bash
OLLAMA_CONTEXT_LENGTH=65536 ollama serve
```

Do not start this second server when Ollama is already managed by systemd or the
desktop application. Configure the existing service instead.

### 3.2 Configure the systemd service on Linux

First identify the active unit:

```bash
systemctl is-active ollama
systemctl --user is-active ollama
```

For a system-wide unit that reports `active`, create a drop-in rather than editing
the vendor unit:

```bash
sudo mkdir -p /etc/systemd/system/ollama.service.d
printf '%s\n' \
  '[Service]' \
  'Environment="OLLAMA_CONTEXT_LENGTH=65536"' \
  | sudo tee /etc/systemd/system/ollama.service.d/context.conf >/dev/null

sudo systemctl daemon-reload
sudo systemctl restart ollama
```

For an active user unit, use `systemctl --user edit ollama` and add the same two
drop-in lines, then run:

```bash
systemctl --user daemon-reload
systemctl --user restart ollama
```

### 3.3 Tell OpenCode the same limits

The supplied [`opencode.json.example`](opencode.json.example) contains this model
configuration:

```json
"qwen3.5:9b": {
  "name": "Qwen 3.5 9B (local)",
  "limit": {
    "context": 65536,
    "output": 16384
  },
  "options": {
    "reasoningEffort": "none"
  }
}
```

`limit.context` tells OpenCode the total context available. `limit.output`
reserves enough room for code and tool calls; it is part of the 64K total, not an
additional allocation. `reasoningEffort: "none"` avoids an interoperability issue
observed with `qwen3.5:9b`, where a tool loop can finish with reasoning tokens but
no visible final text. It is model-specific and should not automatically be
copied to models whose reasoning API has been verified with OpenCode.

### 3.4 Verify the effective allocation

OpenCode configuration alone is not proof. Force Ollama to load the model, then
inspect the running process:

```bash
ollama run qwen3.5:9b "Reply with exactly CONTEXT_OK"
ollama ps
```

The `CONTEXT` column must show `65536`. Also inspect `PROCESSOR`: `100% GPU` is
ideal, while a CPU/GPU split means some layers were offloaded and agent turns will
be slower. On NVIDIA systems, check memory pressure with:

```bash
nvidia-smi --query-gpu=name,memory.total,memory.used,memory.free \
  --format=csv,noheader
```

If the model does not fit, change both `OLLAMA_CONTEXT_LENGTH` and
`limit.context` to `32768`, restart Ollama, restart OpenCode, and verify again.
Ollama's current guidance recommends at least 64K for agents and coding tools;
larger windows consume more memory
([context-length documentation](https://docs.ollama.com/context-length)).

## 4. Connect Claude Code to the local model

The simplest current Ollama integration is:

```bash
ollama launch claude --model qwen3.5:9b
```

To show what the launcher configures, use the equivalent manual setup:

```bash
export ANTHROPIC_AUTH_TOKEN=ollama
export ANTHROPIC_API_KEY=""
export ANTHROPIC_BASE_URL=http://localhost:11434
export CLAUDE_CODE_MAX_CONTEXT_TOKENS=65536

claude --model qwen3.5:9b
```

Inside Claude Code, run `/status` and confirm that the base URL is local and the
selected model is `qwen3.5:9b`. Then ask:

```text
Read the repository without changing files. Summarize the Java version, the task,
and the command that verifies the solution. Stop after the summary.
```

Claude Code may issue requests for auxiliary model names. If the installed
version ignores `--model` for one of those requests, pin its model aliases too:

```bash
export ANTHROPIC_DEFAULT_OPUS_MODEL=qwen3.5:9b
export ANTHROPIC_DEFAULT_SONNET_MODEL=qwen3.5:9b
export ANTHROPIC_DEFAULT_HAIKU_MODEL=qwen3.5:9b
claude --model qwen3.5:9b
```

Do not put these exports in a shell profile during the workshop: they would also
affect later Claude Code sessions. Close the shell or `unset` the variables when
finished.

Claude Code may warn that a local model name is unrecognized. This concerns its
context-window heuristics, not endpoint connectivity. Set
`CLAUDE_CODE_MAX_CONTEXT_TOKENS` to the context actually configured in Ollama;
do not claim a larger value than the local runtime can allocate. The first call
may also be slow while Ollama loads the model: inspect `ollama ps` before treating
a timeout as an API incompatibility.

Ollama documents the Claude Code launcher and manual variables in its
[Claude Code integration](https://docs.ollama.com/integrations/claude-code) and
documents the supported subset and known gaps in
[Anthropic API compatibility](https://docs.ollama.com/api/anthropic-compatibility).
Anthropic's own gateway documentation states that it does not support non-Claude
models behind Claude Code; treat this route as third-party compatibility, not an
Anthropic-supported deployment ([gateway documentation](https://code.claude.com/docs/en/llm-gateway)).

## 5. Use the open-source OpenCode TUI

OpenCode is provider-neutral and is the recommended interface for the model
comparison. Install it with one of the commands in the
[official installation guide](https://opencode.ai/docs/).

The **repository root** is the top-level directory of this cloned project, not the
`moduli/m12/step-01-confronto-locale` directory. It is the directory that contains
the top-level `README.md`, `caso-guida/`, `moduli/`, and `scripts/`. In the example
environment used to prepare this workshop, its absolute path is:

```text
/home/thimoty/git/demo-augmented-coding
```

Move to that directory, copy the supplied configuration there, and start OpenCode
from the same location:

```bash
cd /home/thimoty/git/demo-augmented-coding
cp moduli/m12/step-01-confronto-locale/opencode.json.example opencode.json
opencode
```

Adapt the first command if the repository was cloned elsewhere. After the copy,
the relevant paths are:

```text
demo-augmented-coding/                 # repository root and OpenCode working directory
├── opencode.json                      # active project-local OpenCode configuration
├── README.md
├── caso-guida/
├── moduli/
└── scripts/
```

You can confirm that you are in the correct directory before launching OpenCode:

```bash
pwd
test -f README.md && test -d moduli/m12 && echo "Repository root found"
```

Starting OpenCode here gives it access to the complete teaching repository. Later,
when comparing models, change to the specific disposable directory under
`moduli/m12/step-01-confronto-locale/runs/` before starting a new agent session if
you want that session to focus on one benchmark run. The working directory is not
a security sandbox, so continue to review every requested file operation.

Inside the TUI:

1. run `/models` and select `ollama/qwen3.5:9b`;
2. use **Plan** mode for inspection and **Build** mode for implementation;
3. review every requested command and diff before approving it;
4. use `/undo` if the agent takes the task in the wrong direction.

The configuration uses Ollama's OpenAI-compatible `/v1` endpoint and disables
sharing. It deliberately lists only the workshop models. OpenCode documents this
custom-provider shape in its [provider guide](https://opencode.ai/docs/providers/).

Remove the project-local configuration after the lesson if it is not wanted:

```bash
rm opencode.json
```

## 6. Understand the Java task

The fixture has no build-tool or network dependency. `PaymentRetryService` must:

- reject a blank payment token or idempotency key before calling the gateway;
- make at most three authorization attempts;
- retry only `TransientPaymentException`;
- wait 100 ms and then 200 ms between failed attempts;
- reuse the same idempotency key on every attempt;
- return immediately after authorization succeeds;
- propagate a permanent decline immediately;
- preserve thread interruption and stop retrying if backoff is interrupted;
- preserve the final transient exception as the cause when retries are exhausted.

Prepare an isolated working copy and observe the expected initial failure:

```bash
cd moduli/m12/step-01-confronto-locale
./scripts/prepare-run.sh qwen35
cd runs/qwen35
./check.sh
```

The initial check must fail. That proves the exercise has not already been solved.

## 7. Fixed benchmark prompt

Use exactly the contents of [`prompts/benchmark.md`](prompts/benchmark.md). Paste
it into the agent, or run it non-interactively after entering a prepared run:

```bash
claude -p "$(cat ../../prompts/benchmark.md)"
```

For the live lesson, interactive mode is preferable because participants can see
file reads, command choices, permission requests, and recovery from errors. Do
not help one model but not the others. If a model asks a blocking question, give
the same short answer to every model and record it.

Two additional prompts are provided for teaching prompt quality:

- [`prompts/vague.md`](prompts/vague.md) shows why “implement retry” is ambiguous;
- [`prompts/review.md`](prompts/review.md) asks for review after the benchmark,
  without changing the original implementation score.

## 8. Compare two or three models fairly

Suggested candidates already small enough for a workstation:

```bash
ollama pull qwen3.5:9b
ollama pull qwen3-coder
ollama pull gemma4:12b
```

Create one run per model:

```bash
./scripts/prepare-run.sh qwen35
./scripts/prepare-run.sh qwen3-coder
./scripts/prepare-run.sh gemma4
```

In each run, select only the assigned model, submit the fixed benchmark prompt,
and stop when the agent says it is done or after 12 minutes. Then execute:

```bash
./check.sh
/usr/bin/time -v ./check.sh
ollama ps
```

Save the final response as `agent-final.md` and the following metadata in
`run-notes.md`: model tag, model digest from `ollama list`, quantization, context,
agent and Ollama versions, hardware, elapsed time, interventions, and test result.
Generated `runs/` directories are ignored by Git.

## 9. Score the result

Use [`results/scorecard.md`](results/scorecard.md). The code has priority over the
agent's explanation.

| Dimension | Points | What to inspect |
| --- | ---: | --- |
| Correctness | 0–4 | All checks pass; exact retry and exception behavior |
| Scope discipline | 0–2 | No weakened tests, unrelated rewrites, or new dependencies |
| Java quality | 0–2 | Clear control flow, useful names, no unnecessary abstraction |
| Verification | 0–1 | Agent runs the check and reacts to failures |
| Explanation | 0–1 | Final answer states changes, evidence, and limitations |

Disqualify a run from the numeric ranking if it edits `TestRunner.java` or
`check.sh`, because it has changed the measurement. Keep it in the discussion:
attempting to weaken evaluation is itself an important agent result.

## 10. Example outcomes and code commentary

These are **review examples**, not measured claims about a model. Replace them
with the class's actual runs before publishing a model ranking.

### Weak result

```java
for (int attempt = 0; attempt < 3; attempt++) {
    try {
        return gateway.authorize(amount, paymentToken, UUID.randomUUID().toString());
    } catch (Exception ignored) {
        Thread.sleep(100);
    }
}
throw new RuntimeException("Payment failed");
```

Commentary: the loop looks plausible but breaks idempotency on every attempt,
retries permanent declines, swallows diagnostic information, sleeps after the
last failure, uses a fixed delay, and mishandles interruption. Compilation alone
would not make this acceptable.

### Better result

```java
TransientPaymentException lastFailure = null;
for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
    try {
        return gateway.authorize(amount, paymentToken, idempotencyKey);
    } catch (TransientPaymentException failure) {
        lastFailure = failure;
        if (attempt < MAX_ATTEMPTS) {
            sleepBeforeRetry(attempt);
        }
    }
}
throw new PaymentRetriesExhaustedException(MAX_ATTEMPTS, lastFailure);
```

Commentary: this version catches only the retryable failure, preserves the stable
key and last cause, and avoids a sleep after the final attempt. The reviewer must
still inspect `sleepBeforeRetry` for 100/200 ms delays and correct interruption.

## 11. Discussion prompts

- Did the better result come from the model, the agent loop, or both?
- Which model inspected tests before editing?
- Which failures were reasoning failures versus tool/API compatibility failures?
- Did a larger or code-specialized model justify its latency and memory use?
- Would the result be safe without deterministic tests?
- Which source files or prompts left the machine, if any?

## Reset

Delete a single disposable run and recreate it:

```bash
rm -rf runs/qwen35
./scripts/prepare-run.sh qwen35
```

Only delete a named directory under `runs/`; never run the reset from an
unverified path. The immutable fixture in `starter/` remains the source of truth.
