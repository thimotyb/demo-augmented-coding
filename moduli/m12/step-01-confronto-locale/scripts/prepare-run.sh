#!/usr/bin/env bash
set -euo pipefail

if [[ $# -ne 1 || ! "$1" =~ ^[a-zA-Z0-9._-]+$ ]]; then
  echo "Usage: $0 RUN_NAME" >&2
  echo "RUN_NAME may contain letters, digits, dots, underscores, and hyphens." >&2
  exit 2
fi

script_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
step_dir="$(cd -- "$script_dir/.." && pwd)"
destination="$step_dir/runs/$1"

if [[ -e "$destination" ]]; then
  echo "Refusing to overwrite existing run: $destination" >&2
  exit 1
fi

mkdir -p "$step_dir/runs"
cp -R "$step_dir/starter" "$destination"
chmod +x "$destination/check.sh"
echo "Prepared clean run: $destination"
