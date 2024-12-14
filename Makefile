###############
# TARGETS
###############

.PHONY: all clean build run test
all: run build test

run:
	quarkus dev

test:
	quarkus test --verbose

build:
	quarkus build

clean:
	echo "clean"

.PHONY: help
help:
	quarkus --help
