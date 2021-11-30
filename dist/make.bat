@echo off

mvn -f ../pom.xml clean install

copy ..\target\shliffen-0.0.1-SNAPSHOT.jar ..\dist\FileStorageApp.jar