#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
OUT_DIR="$ROOT_DIR/submission"
ZIP_FILE="$OUT_DIR/CS489-Course-Project-HainingSong.zip"

mkdir -p "$OUT_DIR"
rm -f "$ZIP_FILE"

cd "$ROOT_DIR"

mvn -q test

zip -r "$ZIP_FILE" \
  .github \
  docs \
  scripts \
  src \
  Dockerfile \
  README.md \
  SUBMISSION.md \
  docker-compose.yml \
  pom.xml \
  .gitignore \
  -x "*/.DS_Store" \
  -x "*/target/*" \
  -x "*/tmp/*" \
  -x "*/submission/*" \
  -x "*/.idea/*"

echo "Created $ZIP_FILE"
