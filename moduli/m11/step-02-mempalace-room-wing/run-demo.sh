#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
RUN_DIR="$(mktemp -d "${TMPDIR:-/tmp}/mempalace-m11.XXXXXX")"
PALACE="$RUN_DIR/palace"
CORPUS="$RUN_DIR/corpus"

mkdir -p "$CORPUS"
cp -R "$SCRIPT_DIR/corpus/." "$CORPUS/"

echo "Demo MemPalace M11 — CLI v3.0.0 testato; dati temporanei in: $RUN_DIR"
echo
printf '\n' | mempalace --palace "$PALACE" init "$CORPUS"
mempalace --palace "$PALACE" mine "$CORPUS" --wing augmented-coding --agent codex-m11-demo
mempalace --palace "$PALACE" status

mempalace --palace "$PALACE" search \
  "Quali decisioni sul retry bloccano il task PAY-02?" \
  --wing augmented-coding --room team_memory --results 2

mempalace --palace "$PALACE" search \
  "Quando conviene passare dal modello locale a quello remoto e cosa trasferire?" \
  --wing augmented-coding --room cost_control --results 2

mempalace --palace "$PALACE" search \
  "Quale decisione sul retry è bloccante?" \
  --wing augmented-coding --room cost_control --results 1

printf '\nPalace temporanea conservata per ispezione: %s\n' "$PALACE"
