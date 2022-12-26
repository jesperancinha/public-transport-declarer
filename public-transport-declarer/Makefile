b: build-maven
build-maven:
	mvn clean install -Pjar
create-native: build-maven
	$(JAVA_HOME)/bin/native-image -jar target/public-transport-declarer.jar
create-native-no-fallback:
	$(JAVA_HOME)/bin/native-image -jar target/public-transport-declarer.jar --no-fallback
install-locally: build-maven create-native-no-fallback
	sudo cp public-transport-declarer /usr/local/bin/
remove-locally:
	sudo rm /usr/local/bin/public-transport-declarer
