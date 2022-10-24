#!/bin/bash

mkdir -p bin

if mvn clean package;
then
    printf "Maven build successful\n"
else
    printf "Maven build failed\n"
    exit 1
fi

tar xzf server/target/tpe2-g8-server-1.0-SNAPSHOT-bin.tar.gz -C bin/
tar xzf client/target/tpe2-g8-client-1.0-SNAPSHOT-bin.tar.gz -C bin/

chmod u+x bin/tpe2-g8-client-1.0-SNAPSHOT/run-*
chmod u+x bin/tpe2-g8-server-1.0-SNAPSHOT/run-*
