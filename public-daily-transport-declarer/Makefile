SHELL := /bin/bash

b: build-maven
build-maven:
	mvn clean install -Pjar
create-native: build-maven
	mvn clean install -Pnative
	cp target/public-daily-transport-declarer .
create-native-no-fallback: create-native
install-locally: create-native-no-fallback
	sudo cp public-daily-transport-declarer /usr/local/bin/
create-native-old: build-maven
	$(JAVA_HOME)/bin/native-image -jar target/public-daily-transport-declarer.jar
create-native-no-fallback-old:
	$(JAVA_HOME)/bin/native-image -jar target/public-daily-transport-declarer.jar --no-fallback
just-install-locally:
	sudo cp public-daily-transport-declarer /usr/local/bin/
remove-locally:
	sudo rm /usr/local/bin/public-daily-transport-declarer
jar: build-maven
check-jar:
	if ! test -f target/public-daily-transport-declarer.jar; then make jar; fi
build-and-generate-last-report: jar generate-last-report
generate-last-report: check-jar
	java -jar target/public-daily-transport-declarer.jar -i reports
