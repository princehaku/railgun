@echo off
if "%JAVA_HOME%" == "" (echo "Please Set JAVA_HOME To Jdk Dir" && pause && exit;)
"%JAVA_HOME%/bin/java" -jar target/railgun-0.2.jar
