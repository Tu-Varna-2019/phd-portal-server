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

.PHONY: help
help:
	quarkus --help

.PHONY: elastic-attach
elastic-attach:
	process-compose attach
