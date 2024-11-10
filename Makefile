###############
# TARGETS
###############

.PHONY: run
run:
	quarkus dev

.PHONY: jar
jar:
	quarkus build
