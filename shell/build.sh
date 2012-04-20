#!/bin/bash
rm railgun -rf
hg clone http://hg.code.sf.net/p/railgun/code railgun || (echo "Please Install hg "; exit 0;)
cd railgun/trunk
mvn install:install-file -DgroupId=wltea -DartifactId=IKAnalyzer -Dversion=2012 -Dpackaging=jar -Dfile=lib/IKAnalyzer-2012.jar && mvn install
echo "安装完成 请使用run.sh运行"