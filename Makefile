###############
# TARGETS
###############

.PHONY: all clean build run test
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
	echo "clean"

.PHONY: help
help:
	quarkus --help
