# 🎓 Designing and implementing a Doctoral Admission and Training System 🎓

[![License](https://img.shields.io/github/license/Tu-Varna-2019/phd-portal-server)](https://www.gnu.org/licenses/gpl-3.0.en.html)
[![Dependabot Updates](https://github.com/Tu-Varna-2019/phd-portal-server/actions/workflows/dependabot/dependabot-updates/badge.svg)](https://github.com/Tu-Varna-2019/phd-portal-server/actions/workflows/dependabot/dependabot-updates)
[![Create and publish a Docker image](https://github.com/Tu-Varna-2019/phd-portal-server/actions/workflows/docker-publish.yaml/badge.svg)](https://github.com/Tu-Varna-2019/phd-portal-server/actions/workflows/docker-publish.yaml)
[![Security scanning](https://github.com/Tu-Varna-2019/phd-portal-server/actions/workflows/security-scan.yaml/badge.svg)](https://github.com/Tu-Varna-2019/phd-portal-server/actions/workflows/security-scan.yaml)
[![Test](https://github.com/Tu-Varna-2019/phd-portal-server/actions/workflows/test.yaml/badge.svg)](https://github.com/Tu-Varna-2019/phd-portal-server/actions/workflows/test.yaml)
[![GitHub release](https://img.shields.io/github/v/release/Tu-Varna-2019/phd-portal-server)](#)
[![GitHub release date](https://img.shields.io/github/release-date/Tu-Varna-2019/phd-portal-server)](#)
[![itHub last commit](https://img.shields.io/github/last-commit/Tu-Varna-2019/phd-portal-server)](#)

## 🚀 About

The project should develop an engineering solution for digitizing an information system for doctoral students and candidates in order to be used by higher education institutions, which would integrate into the systematic process for the management and management of the so -called "digital university".

## 🎉 Getting started

### ✅ Prerequisites

In order to run the app, make sure you have installed the following dependencies

| Program           | URL      |
| :------------- | :----------: |
| Java         | [Java](https://www.java.com/en/download/) |
| Quarkus       | [Quarkus](https://quarkus.io/) |
| AWS Vault [Optional]          | [AWS Vault ](https://github.com/99designs/aws-vault) |
| PostgreSQL    | [PostgreSQL](https://www.postgresql.org/) |
| ElasticSearch       | [ElasticSearch](https://www.elastic.co/elasticsearch) |

> If you are using Nix or NixOS you can install them in flake.nix via *devenv*

### 🌱 Setup

1. Make sure to have elastic search instance up and running
   - you can also execute `make services` to run it via *devenv*

2. Add your secrets in `.env` file.
   - credentials from the postgresql database
   - credentials from smtp to send emails
   - credentials (access key and secret key) on AWS for storing media assets

### 🏗️ Build

```sh
quarkus build
# Or
make build

# Bulid a docker image
docker build -t IliyanKostov9/phd-server:1.0.0 .
# Or
make docker-build
```

### 🏃 Run

```sh
quarkus dev -Dquarkus.profile=dev
# Or
make run
```


##  🧑‍💻 Commands

|Command|Description|
|:-|:-|
|make help|Show available commands with their description|
|make help-server|Show available commands for quarkus with their description|
|make sys-update-|Build your system configuration|
|make all|Run all commands at once|
|make run|Run the server|
|make services|Run elastic search via *devenv*|
|make stop|Stop elastic search via *devenv*|
|make test|Run tests|
|make build|Build the server|
|make clean|Uninstall the deps and bulid again|
|make install|Install the deps |
|make elastic-attach|Attach to the Elastic search TUI via *devenv*|


#### 📚 Docs
- [ Java ]( https://docs.oracle.com/en/java/  )
- [ Quarkus ](  https://quarkus.io/guides/# )
- [ PostreSQL ]( https://www.postgresql.org/docs/ )
- [ Docker ]( https://docs.docker.com/ )
- [ Nix ]( https://nix.dev/manual/nix/2.18/)
- [ AWS ]( https://docs.aws.amazon.com/)
- [ Elastic Search ]( https://www.elastic.co/)

### 📃 License
This product is licensed under [GNU General Public License](https://www.gnu.org/licenses/gpl-3.0.en.html)
