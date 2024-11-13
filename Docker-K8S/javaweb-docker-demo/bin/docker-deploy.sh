#!/bin/bash

## 第一个参数：up|start|stop|restart|rm
COMMAND=$1
## app 名称
APP_NAME=$2

## 如果没有给 app 名称，默认就叫 app
if [ -z $APP_NAME ]; then
  APP_NAME="javaweb-docker-demo"
fi

## 暴露的端口
EXPOSE_PORT=8808
## 项目/组织
NAMESPACE=wolfcode
## 版本号
TAG=1.0-SNAPSHOT

## 仓库地址
REGISTRY_SERVER=192.168.113.121:5000

## 用户名
USERNAME=admin
## 密码
PASSWORD=wolfcode

## 镜像名称
IMAGE_NAME="$REGISTRY_SERVER/$NAMESPACE/$APP_NAME:$TAG"

## 使用说明，用来提示输入参数
function usage() {
	echo "Usage: sh docker-deploy.sh [up|start|stop|restart|rm]"
	exit 1
}

## 登录仓库
function login() {
  echo "docker login -u $USERNAME --password-stdin $REGISTRY_SERVER"
  echo "$PASSWORD" | docker login -u $USERNAME --password-stdin $REGISTRY_SERVER
}

## 启动容器
function start() {
  # 检查容器是否存在
  CONTAINER_NAME=$(docker ps | grep "$APP_NAME" | awk '{print $NF}')
  # 存在就不启动了
  if [ -n "$CONTAINER_NAME" ]; then
    echo "container $CONTAINER_NAME aready started..."
    exit 1
  fi
  # 镜像如果不存在需要先登录
  IMAGE=$(docker images | grep "$APP_NAME" | awk '{print $3}')
  if [ -z "$IMAGE" ]; then
    login
  fi
  # 容器不存在就启动
  echo "starting container $APP_NAME..."
  docker run -d --restart=always --name $APP_NAME -p $EXPOSE_PORT:8080 $IMAGE_NAME
  echo "container $APP_NAME started..."
}

## 停止容器
function stop() {
  # 检查容器是否存在
  CONTAINER_NAME=$(docker ps | grep "$APP_NAME" | awk '{print $NF}')

  # 不存在就不需要停止
  if [ -z "$CONTAINER_NAME" ]; then
    echo "container $CONTAINER_NAME not running..."
    exit 1
  fi

  # 存在就停止容器
  echo "stoping container $APP_NAME..."
  docker stop $CONTAINER_NAME
  echo "container $APP_NAME stopted..."
}

## 重启容器
function restart() {
  # 先停止
  stop
  # 再启动
  start
}

## 删除容器、镜像
function rm() {
  # 获取容器名称
  CONTAINER_NAME=$(docker ps | grep "$APP_NAME" | awk '{print $NF}')
  if [ -n "$CONTAINER_NAME" ]; then
    # 停止容器
    stop
    # 删除容器
    echo "removing container $APP_NAME..."
    docker rm $CONTAINER_NAME
    echo "container $APP_NAME removed..."
  fi

  # 获取镜像 id
  IMAGE=$(docker images | grep "$APP_NAME" | awk '{print $3}')
  if [ -n "$IMAGE" ]; then
    # 删除镜像
    echo "removing image $IMAGE..."
    docker rmi $IMAGE
    echo "image $IMAGE removed..."
  fi
}

# 重新拉取镜像并启动容器
function up() {
  # 删除旧的镜像与容器
  rm
  # 拉取新的镜像并启动容器
  start
}

# 根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$COMMAND" in
"up")
	up
;;
"start")
	start
;;
"stop")
	stop
;;
"restart")
	restart
;;
"rm")
	rm
;;
*)
	usage
;;
esac
