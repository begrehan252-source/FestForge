#!/usr/bin/env sh
set -eu
DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
VER=8.7
BASE="${GRADLE_USER_HOME:-$HOME/.gradle}/festforge-gradle"
DIST="$BASE/gradle-$VER"
if [ ! -x "$DIST/bin/gradle" ]; then
  mkdir -p "$BASE"
  ZIP="$BASE/gradle-$VER-bin.zip"
  URL="https://services.gradle.org/distributions/gradle-$VER-bin.zip"
  if command -v curl >/dev/null 2>&1; then curl -fL "$URL" -o "$ZIP"; else wget -O "$ZIP" "$URL"; fi
  rm -rf "$DIST"
  unzip -q "$ZIP" -d "$BASE"
fi
exec "$DIST/bin/gradle" -p "$DIR" "$@"
