#!/usr/bin/env bash
IBO_HOME=/Users/cuiyongxu/workspace/ijson/in-develop-boot

cd $IBO_HOME

git pull

mvn install -Dmaven.test.skip=true

cd $IBO_HOME/in-develop-common
mvn clean install -Dmaven.test.skip=true


cd $IBO_HOME/in-develop-remote
mvn clean install -Dmaven.test.skip=true

cd $IBO_HOME/in-develop-auth
mvn clean install -Dmaven.test.skip=true

cd $IBO_HOME/in-develop-core
mvn clean install -Dmaven.test.skip=true

cd $IBO_HOME/in-develop-web

pid=$(ps -ef | grep "in-develop-web" | grep -v grep | awk '{print $2}')

echo "current in-develop-web pid:" $pid

if [ ! -n "$pid" ];then
    echo "service not started"
else
    kill -9 $pid
fi


DATE=$(date +%Y%m%d%H%M%S)
cd $IBO_HOME/in-develop-web

if [ ! -f "$IBO_HOME/in-develop-web/run.log" ]; then
    if [ ! -d "$IBO_HOME-log/" ]; then
        mkdir $IBO_HOME-log/
    fi
    mv $IBO_HOME/in-develop-web/run.log $IBO_HOME-log/$DATE.log
fi

cd $IBO_HOME/in-develop-web/
nohup mvn spring-boot:run >> run.log 2>&1 &

pid=$(ps -ef | grep "in-develop-web" | grep -v grep | awk '{print $2}')
echo "start in-develop-web success, pid:" $pid
