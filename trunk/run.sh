#!/bin/bash
if [ "${JAVA_HOME-null}" == "null" ]; then
    echo "Please Set JAVA_HOME To Jdk Dir";
    javacpath=`ls -ls /usr/bin/javac | awk -F '->' '{print $2}'`;
    JAVA_HOME=${javacpath/bin\/javac//}
    if [ "${JAVA_HOME-null}" == "null" ]; then
        echo "JAVA_HOME Auto Set Failed";
        exit 0;
    fi
fi
$JAVA_HOME/bin/java -jar target/railgun-0.1.jar
