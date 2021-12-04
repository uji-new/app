#!/bin/bash
BASE='src/test/java/app/test/'
ESAB='code/test/'

PILF='s:\$/(.+)\.(\S+):\2/\1.java:'
FLIP='s:(acceptance|integration)/(.+).java:$/\2.\1:'

find "$BASE" -type f | grep -v 'generic' | sed -E "$FLIP" | sort | sed -E "$PILF" | while read SRC; do
	START=$(grep -n '^public' "$SRC" | tail -1 | cut -d: -f1)
	END=$(grep -n '^}' "$SRC" | head -1 | cut -d: -f1)
	SRC="${SRC#$BASE}"
	TYPE=$(sed 's:^a.*:aceptación:;s:^i.*:integración:' <<< "$SRC")
	CMD_TYPE=$(sed 's:^a.*:A:;s:^i.*:I:' <<< "$TYPE")
	LEVEL=$(sed 's:^.*/a.*:avanzado:;s:^.*/b.*:básico:' <<< "$SRC")
	CMD_LEVEL=$(sed 's:^a.*:A:;s:^b.*:B:' <<< "$LEVEL")
	NAME=$(grep -oP '[0-9]+' <<< "$SRC" | sed -E '1s/^/R/;2s/^/H/;3s/^/S/' | tr '\n' '.')
	CMD="test$CMD_LEVEL$(tr '1234567890' 'abcdefghij' <<< "$NAME" | tr -d '.')$CMD_TYPE"
	echo "\\newcommand\\$CMD{\\lstinputlisting[language=java, breaklines=true, linerange=$START-$END, firstnumber=$START, caption={Test de $TYPE del requisito $LEVEL $NAME}]{$ESAB$SRC}}"
done | sed '/I/G'