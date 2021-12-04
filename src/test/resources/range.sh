#!/bin/bash
BASE='src/test/java/app/test/'
ESAB='code/test/'

PILF='s:\$/(.+)\.(\S+):\2/\1.java:'
FLIP='s:(acceptance|integration)/(.+).java:$/\2.\1:'

find "$BASE" -type f | grep -v 'generic' | sed -E "$FLIP" | sort | sed -E "$PILF" | while read SRC; do
    sed "s:^$BASE:$ESAB:" <<< "$SRC"
    grep -n '^public' $SRC | tail -1 | cut -d: -f1
    grep -n '^}' $SRC | head -1 | cut -d: -f1
done