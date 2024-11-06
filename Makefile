###############
# TARGETS
###############

.PHONY: run
run:
	quarkus dev

.PHONY: jar
jar:
	quarkus build

.PHONY: build-native
build-native:
	quarkus build --native

.PHONY: run-native-container
run-native-container:
	quarkus build --native -Dquarkus.native.container-build=true
	quarkus ext add io.quarkus:quarkus-hibernate-orm

.PHONY: add-ext
add-ext:
	quarkus ext add io.quarkus:quarkus-jdbc-postgresql
