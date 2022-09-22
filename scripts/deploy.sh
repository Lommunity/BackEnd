#!/bin/bash

echo ">> 배포 시작 <<"
cd BackEnd/

echo ">> Git pull <<"
sudo git pull origin main

echo ">> Gradle build <<"
sudo ./gradlew clean build -x test

echo ">> 구동중인 애플리케이션 확인 <<"
PID=$(pgrep -f Lommunity-0.0.1-SNAPSHOT.jar)

if [ -z "$PID" ]; then
    echo ">> 구동중인 애플리케이션이 존재하지 않음 <<"
else
  echo ">> 구동중인 애플리케이션이 존재, 해당 애플리케이션을 종료 합니다. <<"
  sudo kill -15 $PID
  sleep 5
fi

echo ">> 새 애플리케이션 배포 <<"
sudo nohup java -jar build/libs/Lommunity-0.0.1-SNAPSHOT.jar 2>&1 &