FROM alpine:3.22
ARG DOCKER_USER=quarkus
RUN addgroup -s ${DOCKER_USER} && adduser -S ${DOCKER_USER} -G ${DOCKER_USER}


FROM gradle:jdk21-corretto AS build
USER ${DOCKER_USER}

COPY --chown=${DOCKER_USER}:${DOCKER_USER} gradlew /phd-portal-server/gradlew
COPY --chown=${DOCKER_USER}:${DOCKER_USER} gradle /phd-portal-server/gradle
COPY --chown=${DOCKER_USER}:${DOCKER_USER} build.gradle /phd-portal-server/
COPY --chown=${DOCKER_USER}:${DOCKER_USER} settings.gradle /phd-portal-server/
COPY --chown=${DOCKER_USER}:${DOCKER_USER} gradle.properties /phd-portal-server/

WORKDIR /phd-portal-server

COPY src /phd-portal-server/src
RUN /phd-portal-server/gradlew clean build -x test -Dquarkus.profile=prod -Dquarkus.package.type=uber-jar

LABEL org.opencontainers.image.source=https://github.com/Tu-Varna-2019/phd-portal-server \
	version="1.0.0-RELEASE" \
	description="Masters thesis for developing REST API backend server" \
	author="Iliyan Kostov" \
	env="prod"


FROM eclipse-temurin:21-jre-noble

WORKDIR /server/
COPY --from=build /phd-portal-server/build/*.jar /server/app.jar

EXPOSE 8080
CMD ["java", "-Dquarkus.profile=prod", "-jar", "/server/app.jar"]
