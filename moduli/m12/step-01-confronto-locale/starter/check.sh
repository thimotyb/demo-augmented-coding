#!/usr/bin/env bash
set -euo pipefail

project_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
cd "$project_dir"

if ! command -v javac >/dev/null 2>&1; then
  echo "ERROR: javac was not found. Install a Java 21 JDK, not only a JRE." >&2
  exit 127
fi

build_dir="${TMPDIR:-/tmp}/m12-payment-retry-$$"
cleanup() {
  rm -rf "$build_dir"
}
trap cleanup EXIT

mkdir -p "$build_dir"
find src/main/java src/test/java -name '*.java' -print0 \
  | xargs -0 javac --release 21 -d "$build_dir"
java -ea -cp "$build_dir" example.payment.TestRunner
