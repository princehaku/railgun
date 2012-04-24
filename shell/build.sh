#!/bin/bash
rm railgun -rf
hg clone http://hg.code.sf.net/p/railgun/code railgun || (echo "Please Install hg "; exit 0;)
cd railgun/trunk
sh -c "mvn install:install-file -DgroupId=wltea -DartifactId=IKAnalyzer -Dversion=2012 -Dpackaging=jar -Dfile=lib/IKAnalyzer-2012.jar"
sh -c "mvn install"
cd ../../
rm build.sh
mv -f railgun/shell/deploy.sh ./
echo "+++++++++++++++++++++"
echo "build done"
echo "+++++++++++++++++++++"
read
