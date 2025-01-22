#!/bin/bash
PROJECT_ROOT="/home/ubuntu/Back-end/springserver"
JAR_NAME=$PROJECT_ROOT/build/libs/springserver-S
GREEN_PORT=8081
BLUE_PORT=8080
echo "> build 파일명: $JAR_NAME"

echo "> build 파일 복사"
DEPLOY_PATH=/home/ubuntu/app/
cp $BUILD_PATH $DEPLOY_PATH

echo "> 현재 구동중인 Port 확인"
BLUE_PROFILE=$(curl -s http://localhost/profile)
echo "> 현재 프로파일: $BLUE_PROFILE"

# 쉬고 있는 set 찾기: set1이 사용중이면 set2가 쉬고 있고, 반대면 set1이 쉬고 있음
if [ "$BLUE_PROFILE" == "set1" ]; then
  GREEN_PROFILE="set2"
  GREEN_PORT=8081
elif [ "$BLUE_PROFILE" == "set2" ]; then
  GREEN_PROFILE="set1"
  GREEN_PORT=8080
else
  echo "> 일치하는 Profile이 없습니다. Profile: $BLUE_PROFILE"
  echo "> set1을 할당합니다. IDLE_PROFILE: set1"
  GREEN_PROFILE="set1"
  GREEN_PORT=8080
fi

echo "> application.jar 교체"
GREEN_APPLICATION="$GREEN_PROFILE-springserver-0.0.1-SNAPSHOT.jar"
GREEN_APPLICATION_PATH="$DEPLOY_PATH$GREEN_APPLICATION"

ln -Tfs "$DEPLOY_PATH$JAR_NAME" "$GREEN_APPLICATION_PATH"

echo "----------------------------------------------------------------------"
echo "> $GREEN_PROFILE 배포"
nohup java -jar -Duser.timezone=Asia/Seoul -Dspring.profiles.active=$GREEN_PROFILE $GREEN_APPLICATION_PATH >> /home/ubuntu/app/nohup.out 2>&1 &

echo "----------------------------------------------------------------------"

echo "> nginx 포트 스위칭"
echo "set \$service_url http://127.0.0.1:${GREEN_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc
sudo nginx -s reload

echo "> $BLUE_PROFILE 에서 구동중인 애플리케이션 pid 확인"
BLUE_PID=$(pgrep -f "$BLUE_PROFILE-springserver-0.0.1-SNAPSHOT.jar")

if [ -z "$BLUE_PID" ]; then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> 기존 ${BLUE_PROFILE} 서버 중단"
  echo "> kill -15 $BLUE_PID"
  kill -15 "$BLUE_PID"
  sleep 5
fi