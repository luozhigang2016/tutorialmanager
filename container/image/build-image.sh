#!/bin/bash -e
REGISTRY="myacr" 
REGISTRY_PROJECT="lab2" 
BASEDIR="$(readlink -f $(dirname $0 ))"
JAVA_APP_DIR="$BASEDIR/../../backend"
VUE_APP_DIR="$BASEDIR/../../frontend"
# Build Java App
mvn -f $JAVA_APP_DIR/pom.xml -DskipTests clean package
# Build VUE app
pushd $VUE_APP_DIR
pnpm run build
popd
cp -r $VUE_APP_DIR/dist $JAVA_APP_DIR/target/

#Image VER 
APP="tutorial-manager-be"
VER="latest"
IMAGE_TAG="${APP}:${VER}"
DOCKER_BINDIR="$JAVA_APP_DIR/target"

echo "Image short tag: $IMAGE_TAG"
echo "Image long tag: ${REGISTRY}/${REGISTRY_PROJECT}/${IMAGE_TAG}"
echo "DOCKER bindir from: $DOCKER_BINDIR "

BUILD_OPTS=" -t ${REGISTRY}/${REGISTRY_PROJECT}/${IMAGE_TAG} --build-context bindir=$DOCKER_BINDIR "
docker buildx build  $BUILD_OPTS $BASEDIR

# Tag 
docker tag  ${REGISTRY}/${REGISTRY_PROJECT}/${IMAGE_TAG} ${IMAGE_TAG}

echo ">>>> Output Docker Images <<<<"
docker images | grep "$(echo $IMAGE_TAG | cut -d':' -f1)"

