#!/bin/bash
cd railgun
hg pull
hg update
cd trunk
sh -c "mvn install"
echo "+++++++++++++++++++++"
echo "deploy done"
echo "+++++++++++++++++++++"
read
