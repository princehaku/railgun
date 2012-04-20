#!/bin/bash
if [ $JAVA_HOME eq "" ] ;
 echo "Please Set JAVA_HOME To Jdk Dir"
 javacpath = `ls -ls /usr/bin/javac | awk -F '->' '{print $2}'`
if
"$JAVA_HOME" -jar target/railgun-0.1.jar
