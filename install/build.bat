@echo off
rmdir /S /Q railgun
git clone git://github.com/princehaku/railgun.git railgun
cd railgun/trunk
cmd /c "mvn install:install-file -DgroupId=wltea -DartifactId=IKAnalyzer -Dversion=2012 -Dpackaging=jar -Dfile=lib/IKAnalyzer-2012.jar"
cmd /c "mvn install"
cd ../../
move railgun\\shell\\deploy.bat .\\deploy.bat
echo "+++++++++++++++++++++"
echo "build done"
echo "+++++++++++++++++++++"
pause