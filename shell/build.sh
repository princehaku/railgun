#!/bin/bash
rm railgun -rf
hg clone http://hg.code.sf.net/p/railgun/code railgun
cd railgun/trunk
mvn install:install-file -DgroupId=wltea -DartifactId=IKAnalyzer -Dversion=2012 -Dpackaging=jar -Dfile=lib/IKAnalyzer-2012.jar
mvn install
