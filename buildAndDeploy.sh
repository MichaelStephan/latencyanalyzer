mvn clean compile assembly:single 
cd target
mkdir build
mkdir build/bin
mkdir build/lib
echo -e "#!/bin/bash\njava -jar ../lib/*.jar" > build/bin/run.sh
cp *.jar build/lib/
cd build
zip app.zip bin/* lib/*
#cf push -f manifest.yaml
