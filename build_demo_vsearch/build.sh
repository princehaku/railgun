#!/bin/sh
echo "+++++++++++++++++++++++"
echo "   building starting   "
echo "makesure mvn executable"
echo "+++++++++++++++++++++++"
basepath=$(pwd)
rm dist -rf
mkdir -p dist/lib/
cd ../railgun_core/
sh -c "mvn install:install-file -DgroupId=wltea -DartifactId=IKAnalyzer -Dversion=2012 -Dpackaging=jar -Dfile=lib/IKAnalyzer-2012.jar"
sh -c "mvn clean install"
cp target/lib/*.jar $basepath/dist/lib/
cp target/*.jar $basepath/dist/
echo "+++++++++++++++++++++++"
echo "build railgun core done"
echo "+++++++++++++++++++++++"
cd ../demo_vsearch/
sh -c "mvn clean install"
mkdir -p ${basepath}/dist/src/main/resources/
cp src/main/resources ${basepath}/dist/src/main/resources/ -R
rm $basepath/dist/src/main/resources/filters/*.class
cp src/main/java/system.propertie $basepath/dist/
cp target/*.jar $basepath/dist/
cd $basepath/dist/
echo "+++++++++++++++++++++++"
echo "   build vsearch done  "
echo "+++++++++++++++++++++++"
echo "#!/bin/sh"> run.sh
echo "${JAVA_HOME}/bin/java -Dfile.encoding=UTF-8 -classpath ./lib/*;./* net.techest.vsearch.App">> run.sh
echo "+++++++++++++++++++++++"
echo "   using dist\run.sh  "
echo "+++++++++++++++++++++++"
