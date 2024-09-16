SHELL := /bin/bash
GITHUB_RUN_ID ?=123

b: build-maven
build:
	mvn clean install
build-maven:
	mvn clean install -DskipTests
build-maven-jar:
	mvn clean install -Pjar
test:
	mvn test
test-maven:
	mvn test
local: no-test
	mkdir -p bin
no-test:
	mvn clean install -DskipTests
docker:
	docker-compose down -v
	docker-compose rm -svf
	docker-compose up -d --build --remove-orphans
docker-databases: stop local
coverage:
	mvn clean install jacoco:prepare-agent package jacoco:report
	mvn omni-coveragereporter:report
build-images:
build-docker: stop no-test build-npm
	docker-compose up -d --build --remove-orphans
show:
	docker ps -a  --format '{{.ID}} - {{.Names}} - {{.Status}}'
docker-delete-idle:
	docker ps --format '{{.ID}}' -q --filter="name=biscaje_"| xargs -I {} docker rm {}
docker-delete: stop
	docker ps -a --format '{{.ID}}' -q --filter="name=biscaje_"| xargs -I {}  docker stop {}
	docker ps -a --format '{{.ID}}' -q --filter="name=biscaje_"| xargs -I {}  docker rm {}
docker-cleanup: docker-delete
	docker images -q | xargs docker rmi
docker-clean:
	docker-compose down -v
	docker-compose rm -svf
docker-clean-build-start: docker-clean b docker
docker-delete-apps: stop
prune-all: docker-delete
	docker network prune
	docker system prune --all
	docker builder prune
	docker system prune --all --volumes
stop:
	docker-compose down --remove-orphans
install:
	nvm install --lts
	nvm use --lts
	brew tap kong/deck
	brew install deck
locust-welcome-start:
	cd locust/welcome && locust --host=localhost
dcup-light:
	docker-compose -p ${GITHUB_RUN_ID} up -d postgres
dcd:
	docker-compose -p ${GITHUB_RUN_ID} down
create-native:
	cd public-transport-declarer && make create-native
install-locally:
	cd public-transport-declarer && make install-locally
remove-locally:
	cd public-transport-declarer && make remove-locally
deps-plugins-update:
	curl -sL https://raw.githubusercontent.com/jesperancinha/project-signer/master/pluginUpdatesOne.sh | bash -s -- $(PARAMS)
deps-java-update:
	curl -sL https://raw.githubusercontent.com/jesperancinha/project-signer/master/javaUpdatesOne.sh | bash
deps-node-update:
	curl -sL https://raw.githubusercontent.com/jesperancinha/project-signer/master/nodeUpdatesOne.sh | bash
deps-quick-update:  deps-plugins-update deps-java-update deps-node-update
accept-prs:
	curl -sL https://raw.githubusercontent.com/jesperancinha/project-signer/master/acceptPR.sh | bash

