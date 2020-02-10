#!/bin/bash
# Michael O'Brien

usage() {
cat <<EOF
Usage: $0 [PARAMs]
example
sudo ./build.sh -c "add(2,4)
-u                  : Display usage
-e [exp]            : expression
-v verify
EOF
}

build() {
BUILD_ID=10001
BUILD_DIR=builds
mkdir ../../$BUILD_DIR
TARGET_DIR=../../$BUILD_DIR/$BUILD_ID
mkdir $TARGET_DIR
CONTAINER_IMAGE=calculator-cli
MVN_VERSION=0.0.1

cp ../../target/*.jar $TARGET_DIR
cp DockerFile $TARGET_DIR
cp startService.sh $TARGET_DIR
cd $TARGET_DIR
docker build --no-cache --build-arg build-id=$BUILD_ID -t obrienlabs/$CONTAINER_IMAGE -f DockerFile .
#docker tag $CONTAINER_IMAGE:latest $CONTAINER_IMAGE:latest
docker tag obrienlabs/$CONTAINER_IMAGE obrienlabs/$CONTAINER_IMAGE:$MVN_VERSION
# dockerhub
docker push obrienlabs/$CONTAINER_IMAGE:$MVN_VERSION
# locally
docker stop $CONTAINER_IMAGE
docker rm $CONTAINER_IMAGE
echo "starting: $CONTAINER_IMAGE"
docker run --name $CONTAINER_IMAGE \
    -d -p 8888:8080 \
    -e os.environment.configuration.dir=/ \
    -e os.environment.ecosystem=sbx \
    obrienlabs/$CONTAINER_IMAGE:$MVN_VERSION

docker logs calculator-cli

cd ../../src/docker

}

EXPR=

while getopts ":e:u:v" PARAM; do
  case $PARAM in
    u)
      usage
      exit 1
      ;;
    e)
      EXPR=${OPTARG}
      ;;
    ?)
      usage
      exit
      ;;
    esac
done

build $EXPR


