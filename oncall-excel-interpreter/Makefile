SHELL := /bin/bash

b: build-maven
build-maven:
	mvn clean install -Pjar
create-native: build-maven
	mvn clean install -Pnative
	cp target/oncall-excel-interpreter .
create-native-no-fallback: create-native
install-locally: create-native-no-fallback
	sudo cp oncall-excel-interpreter /usr/local/bin/
create-native-old: build-maven
	$(JAVA_HOME)/bin/native-image -jar target/oncall-excel-interpreter.jar
create-native-no-fallback-old:
	$(JAVA_HOME)/bin/native-image -jar target/oncall-excel-interpreter.jar --no-fallback
just-install-locally:
	sudo cp oncall-excel-interpreter /usr/local/bin/
remove-locally:
	sudo rm /usr/local/bin/oncall-excel-interpreter
jar: build-maven
check-jar:
	if ! test -f target/oncall-excel-interpreter.jar; then make jar; fi
build-and-generate-last-report: jar generate-last-report
generate-last-report: check-jar
	java -jar target/oncall-excel-interpreter.jar -i reports
