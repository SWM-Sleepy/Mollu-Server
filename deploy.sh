#! /bin/bash

REPOSITORY=/home/ubuntu/mollu/zip

echo "> 현재 구동 중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -fl server-0.0.1-SNAPSHOT.jar | awk '{print $1}')

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  sudo kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 환경변수 불러오기"
source ~/.bash_profile

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

if [ "$DEPLOYMENT_GROUP_NAME" == "github-code-deploy-group" ]; then
  PROFILE=dev
else
  PROFILE=prod
fi

nohup java -jar -Duser.timezone=Asia/Seoul -Dspring.profiles.active=$PROFILE $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &