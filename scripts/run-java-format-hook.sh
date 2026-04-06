#!/usr/bin/env bash
set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$repo_root"

if ! command -v mvn >/dev/null 2>&1 && [[ -s "$HOME/.sdkman/bin/sdkman-init.sh" ]]; then
  # shellcheck source=/dev/null
  set +u
  source "$HOME/.sdkman/bin/sdkman-init.sh"
  set -u
fi

if ! command -v mvn >/dev/null 2>&1; then
  echo "mvn not found. Install Maven or make it available in PATH before running pre-commit." >&2
  exit 1
fi

formatter_file="$repo_root/.vscode/eclipse-java-google-style.xml"

if [[ ! -f "$formatter_file" ]]; then
  echo "Formatter XML is missing at $formatter_file." >&2
  exit 1
fi

exec mvn -q -f CW3/pom.xml \
  -Dspotless.formatter.file="$formatter_file" \
  spotless:apply
