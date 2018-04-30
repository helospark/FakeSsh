# Fake SSH

## Introduction

A fake SSH service that logs all usernames, passwords and IPs when someone tries to authenticate and rejects all authentication requests.
I made it to investigate brute-force attack on SSH services.

## Depenencies

 - LightDi for DI
 - Bouncycastle for some cryptographic algorithms
 - Slf4j with logback for logging
 - TestNG, Mockito for testing
 - Maven to build and manage dependencies
 - Java 8 

## Build

Maven version 3.0 and above is required. To generate the jar in your target folder execute the following in the main folder of the project:
mvn clean install on the main folder

## Run

Once you have the jar:
java -jar FakeSsh-{version}-SNAPSHOT.jar

## Configuration

You can create an appConfig.properties file next to the jar and override any of these default configs:

- PRIME_DATABASE=moduli
- PRIVATE_KEY_PATH=defaultKey
- MAX_NUMBER_OF_CONNECTION_FROM_SAME_IP=3
- FAKE_SSH_PORT=2222
- BASE_FILE_NAME=sshlog
- LOG_PATH=/tmp
- USER_TIMEOUT=100000

## Result

The logged usernames, passwords and IPS will be in the path given by the LOG_PATH variable (by default "/tmp") named BASE_FILENAME_date.log. It's a regular text file.


*At the moment the development is still inprogress...*
