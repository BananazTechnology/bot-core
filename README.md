# Bananaz Tech Java Core
* Description: An @apache Maven library for all core/base project resources
* Version: (Check main for release or develop for dev)
* Creator: Aaron Renner

### Table of Contents
* [Introduction](#introduction)
* Setup *"How to"*
  * [Install Maven](#install-maven)
  * [Add Github to Settings](#add-github-to-settings)
  * [Local Workspace Offline](#local-workspace-offline)
  
## Introduction

This is a library repository, see the `packages` on the side for instructions on how to add to pom.

## Setup
### Install Maven
NOTE: All java projects in Bananaz Tech come with a Maven wrapper, maven can be excluded from the system by executing the `mvnw` file like:
``` bash
# On Windows
./mvnw clean
# Linux
bash mvnw clean
```

If you would like to have Maven native on your computer please visit [Installing Maven](https://www.baeldung.com/install-maven-on-windows-linux-mac)

### Add Github to Settings
Please ask for help if you do not already have a `~/.m2/settings.xml` file available on your PC
* For login setup please see [Add Github Server](https://stackoverflow.com/questions/58438367/how-to-access-maven-dependecy-from-github-package-registry-beta)

### Local Workspace Offline
Using Maven this project can be installed into the local lookup on your PC by running:
```bash
mvnw clean install
```