#!/bin/bash
cd railgun
hg pull
hg update
cd trunk
mvn install
