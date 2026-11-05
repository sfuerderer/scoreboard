#!/usr/bin/env bash
# Prüft, ob das Fat-JAR die nativen JavaFX-Bibliotheken aller Zielplattformen enthält.
set -euo pipefail

jar_file=$(ls -t target/Anzeigetafel-*.jar | head -n 1)
listing=$(unzip -l "$jar_file")

for ext in dll dylib so; do
    if ! grep -qE "\.$ext\$" <<<"$listing"; then
        echo "::error::$jar_file enthält keine .$ext-Dateien"
        exit 1
    fi
done
echo "$jar_file enthält native Bibliotheken für Windows, macOS und Linux"
