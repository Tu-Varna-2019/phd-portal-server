###############
# TARGETS
###############

.PHONY: all clean build services run test install
all: services run build test

run:
	quarkus dev -Dquarkus.profile=dev

services:
	devenv up

stop:
	process-compose down

test:
	quarkus test

build:
	quarkus build

clean:
	./gradlew --stop
	./gradlew clean
	./gradlew build --refresh-dependencies

install:
	gradle build

.PHONY: help-server
help:
	quarkus --help

.PHONY: help
help:  ## help target to show available commands with information
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) |  awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'


.PHONY: elastic-attach
elastic-attach:
	process-compose attach

.PHONY: docker-build
docker-build:
docker build -t IliyanKostov9/phd-server:1.0.0 .
