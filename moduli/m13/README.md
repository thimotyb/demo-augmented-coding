# M13 — Local models and final comparison

**Status: runnable workshop.**

This module compares local coding agents on the same Java 21 task. It answers three
separate questions:

1. Can Claude Code use an LLM served by Ollama instead of Anthropic's API?
2. What does the same workflow look like in an open-source TUI?
3. How do two or three local models differ when the prompt, code, checks, and time
   budget are held constant?

The reference setup uses [Ollama](https://ollama.com/) as the local model server,
[Claude Code](https://code.claude.com/docs/en/overview) as the familiar agent, and
[OpenCode](https://opencode.ai/docs/) as the open-source TUI. The coding task is a
Java 21 payment-authorization retry from the shared [guided case](../../caso-guida/README.md).

| Step | Input | Output |
| --- | --- | --- |
| [Local agent comparison](step-01-confronto-locale/README.md) | Clean Java 21 fixture and one fixed prompt | Tested implementation, transcript, and scoring matrix |

## Learning outcomes

At the end of the module, participants can:

- route Claude Code to Ollama's Anthropic-compatible local endpoint;
- configure OpenCode against Ollama's OpenAI-compatible endpoint;
- distinguish the coding agent from the model that powers it;
- run a fair, repeatable comparison rather than judging a single impressive answer;
- review generated Java for behavior, design, tests, and operational safety.

## Recommended live-demo sequence

Allow 50–65 minutes:

| Time | Activity |
| ---: | --- |
| 10 min | Architecture and local endpoint smoke test |
| 10 min | Claude Code connected to `qwen3.5:9b` |
| 10 min | The same repository and model in OpenCode |
| 20 min | Blind comparison of two or three models |
| 10 min | Test results, code review, and discussion |

Start with `qwen3.5:9b`. For the comparison, the supplied examples use
`qwen3.5:9b`, `qwen3-coder:latest`, and `gemma4:12b`; substitute models that fit
the available RAM/VRAM, but record the exact tags and settings.

## Important limitation

Claude Code is the agent UI, not the local model. Ollama implements only a subset
of the Anthropic Messages API, and Anthropic does not support routing Claude Code
to non-Claude models. Basic file editing and tool calls can work, but behavior may
change across Claude Code, Ollama, and model versions. OpenCode is the primary
provider-neutral path for the repeatable comparison; the Claude Code path is a
useful interoperability demonstration.
