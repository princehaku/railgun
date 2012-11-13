@echo off
echo "+++++++++++++++++++++++"
echo "   building starting   "
echo "makesure mvn executable"
echo "+++++++++++++++++++++++"
rmdir /Q /S dist
mkdir dist\lib\
cd ../railgun_core/
cmd /c "mvn install:install-file -DgroupId=wltea -DartifactId=IKAnalyzer -Dversion=2012 -Dpackaging=jar -Dfile=lib/IKAnalyzer-2012.jar"
cmd /c "mvn clean install"
copy /Y /D target\lib\*.jar %~dp0\dist\lib\
copy /Y /D target\*.jar %~dp0\dist\
echo "+++++++++++++++++++++++"
echo "build railgun core done"
echo "+++++++++++++++++++++++"
cd ../demo_vsearch/
cmd /c "mvn clean install"
xcopy /S /Y /D src\main\resources %~dp0\dist\src\main\resources\
del %~dp0\dist\src\main\resources\filters\*.class
copy /Y /D src\main\java\system.propertie %~dp0\dist\
copy /Y /D target\*.jar %~dp0\dist\
cd %~dp0\dist\
echo "+++++++++++++++++++++++"
echo "   build vsearch done  "
echo "+++++++++++++++++++++++"
echo @echo off> run.bat
echo %JAVA_HOME%/bin/java -classpath ./lib/*;./* net.techest.vsearch.App>> run.bat
echo "+++++++++++++++++++++++"
echo "   using dist\run.bat  "
echo "+++++++++++++++++++++++"
pause
