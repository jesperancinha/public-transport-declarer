create-test-pdf:
	touch text.txt && echo "Im a big big cat" >> text.txt && convert text.txt -page Letter sample.pdf
create-test-pdf2:
	echo "Im a big big cat" | groff -T pdf > sample.pdf
b: build-maven
build-maven:
	mvn clean install -Pjar
create-native: build-maven
	mvn clean install -Pnative
	cp target/pdf-native-test .
