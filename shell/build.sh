#!/bin/bash
rm railgun -rf
git clone git://github.com/princehaku/railgun.git railgun
cd railgun/trunk
sh -c "mvn install:install-file -DgroupId=wltea -DartifactId=IKAnalyzer -Dversion=2012 -Dpackaging=jar -Dfile=lib/IKAnalyzer-2012.jar"
sh -c "mvn install"
cd ../../
mv -f railgun/shell/deploy.sh ./
echo "+++++++++++++++++++++"
echo "build done"
echo "+++++++++++++++++++++"
read
