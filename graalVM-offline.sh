#!/usr/bin/env sh

echo -e '\033[92mChanging to the first available SDK MAN GraalVM version.\033[0m'
echo -e '\033[91mRemember to run this command like this: . ./graalVM.sh\033[0m'
sdk use java $(sdk list java | grep "\-grl" |  cut -d'|' -f6- | cut -d' ' -f2- |  cut -d' ' -f2-)
