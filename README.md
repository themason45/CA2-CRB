# CA2-CRB

Written by: **Sam Mason**
Email: **sm1091@exeter.ac.uk**
Reference: **CA2-CRB**

## Specification

The `CA2_v3.pdf` file defines the specification of the project.

## Requirements

This application has been written in Java, compiled and tested using:
- openjdk version "15.0.2" 2021-01-19
- OpenJDK Runtime Environment (build 15.0.2+7-27)
- OpenJDK 64-Bit Server VM (build 15.0.2+7-27, mixed mode, sharing)

Use `java -version` to view your configuration

## Building the app

Before running the app, it will need to be built for your platform.

From the root directory run the following command to build the app
```
javac -d out/production/CA2-CRB src/*.java src/menus/*.java src/models/*.java src/support/*.java
```

## Running the app

From the root directory run the app using the following command:
```
java -classpath out/production/CA2-CRB BookingApp
```
