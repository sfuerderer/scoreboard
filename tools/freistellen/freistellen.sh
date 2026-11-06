#!/usr/bin/env bash
# Stellt Spielerfotos für die Anzeigetafel frei (nur macOS 14+).
#
#   tools/freistellen/freistellen.sh "<Ordner mit Fotos>" "<Ausgabeordner>"
#
# Kompiliert beim ersten Aufruf (und nach Änderungen) freistellen.swift nach target/freistellen.
set -euo pipefail

if [[ "$(uname)" != "Darwin" ]]; then
    echo "Freistellen gibt es nur auf macOS (nutzt die Motiv-Erkennung von macOS)." >&2
    exit 1
fi
if [[ $# -ne 2 ]]; then
    echo "Aufruf: $0 \"<Foto oder Ordner>\" \"<Ausgabeordner>\"" >&2
    exit 2
fi

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
repo_dir="$(cd "$script_dir/../.." && pwd)"
source_file="$script_dir/freistellen.swift"
binary="$repo_dir/target/freistellen"

if [[ ! -x "$binary" || "$source_file" -nt "$binary" ]]; then
    command -v swiftc >/dev/null || { echo "swiftc fehlt: xcode-select --install" >&2; exit 1; }
    mkdir -p "$repo_dir/target"
    echo "Kompiliere Freistell-Werkzeug …"
    swiftc -O "$source_file" -o "$binary"
fi

"$binary" "$1" "$2"
