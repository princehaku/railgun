@echo off
cd railgun
hg pull
hg update
cd trunk
cmd /c "mvn install"
echo "+++++++++++++++++++++"
echo "deploy done"
echo "+++++++++++++++++++++"
pause