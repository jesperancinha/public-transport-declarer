SHELL := /bin/bash

b: build-maven
build-maven:
	mvn clean install -Pjar
create-native: build-maven
	mvn clean install -Pnative
	cp target/public-transport-declarer .
create-native-no-fallback: create-native
install-locally: create-native-no-fallback
	sudo cp public-transport-declarer /usr/local/bin/
create-native-old: build-maven
	$(JAVA_HOME)/bin/native-image -jar target/public-transport-declarer.jar
create-native-no-fallback-old:
	$(JAVA_HOME)/bin/native-image -jar target/public-transport-declarer.jar --no-fallback
just-install-locally:
	sudo cp public-transport-declarer /usr/local/bin/
remove-locally:
	sudo rm /usr/local/bin/public-transport-declarer
jar: build-maven
build-and-generate-last-via-csv: jar generate-last-via-csv
check-jar:
	if ! test -f target/public-transport-declarer.jar; then make jar; fi
generate-last-via-csv: check-jar
	java -jar target/public-transport-declarer.jar -o $(shell ls -l transact*.csv | tail -n1 | cut -d" " -f10) -g 10 -d report.csv -l Arnhem,Velp,Schipol,Eindhoven,Amstelveenseweg,Ede,Wageningen,Amsterdam,Zoetermeer,Hertogenbosch,Airport,Leidsche -r routes.txt
generate-last-via-csv-2: check-jar
	java -jar target/public-transport-declarer.jar -o $(shell ls -l transact*.csv | tail -n1 | cut -d" " -f9) -g 10 -d report.csv -l Arnhem,Velp,Schipol,Eindhoven,Amstelveenseweg,Ede,Wageningen,Amsterdam,Zoetermeer,Hertogenbosch,Airport,Leidsche -r routes.txt
build-and-generate-last-report: jar generate-last-report
generate-last-report: check-jar
	./generateReport.sh
