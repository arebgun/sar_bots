#!/bin/sh
cd src && javac -d ../classes `find . -name *.java` && cd ../classes && java sim/Simulator 
