#!/bin/bash
BASE="src/test/java/app/test/"
PILF='s:\$/(.+)\.(\S+):\2/\1.java:'
FLIP='s:(acceptance|integration)/(.+).java:$/\2.\1:'
find "$BASE" -type f | grep -v 'generic' | sed -E "$FLIP" | sort | sed -E "$PILF" | while read SRC; do
    START=$(grep -nm1 '^//' $SRC | cut -d: -f1)
    END=$(wc -l $SRC | cut -d\  -f1)
    echo "$SRC $START $END"
done | column -t | sed -E '/integration/G'