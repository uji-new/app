#!/bin/bash
"${0%/*}/cmd_source.sh" | sed -E 's:.*(\\test\w+).*:\1:'