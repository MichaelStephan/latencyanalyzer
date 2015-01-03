#!/bin/bash
cd ..
mvn clean compile assembly:single 
cd target
rm -rf build
mkdir build
mkdir build/bin
mkdir build/lib
cp ../deploy/run.sh build/bin/
#cp ../src/main/resources/logback.xml build/lib/
chmod u+x build/bin/run.sh
cp *.jar build/lib/
cd build
zip app.zip bin/* lib/*
