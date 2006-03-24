#!/bin/sh
cd src && javac -Xlint:unchecked -d ../classes `find . -name *.java` && cd ../classes && java sim/Simulator 
