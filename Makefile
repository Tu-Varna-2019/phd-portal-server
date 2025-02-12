###############
# TARGETS
###############

.PHONY: all clean build run test install
all: run build test

run:
	devenv up -D
	quarkus dev -Dquarkus.profile=dev

stop:
	process-compose down

test:
	quarkus test --verbose

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
