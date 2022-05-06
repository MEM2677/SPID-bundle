#!/bin/bash
CURRENT_DIR=`pwd`

while getopts d:v: flag
do
    case "${flag}" in
        d) COMPANY_NAME=${OPTARG};;
        v) VERSION=${OPTARG};;
    esac
done
echo "***"
echo "Dockerhub company name: $COMPANY_NAME";
echo "Version requested: $VERSION";
echo "current directory: $CURRENT_DIR";
echo "***"

cd $CURRENT_DIR/installer
mvn clean package
mv target/spid-provider-installer.jar ../docker/spid-provider-installer.jar

cd $CURRENT_DIR/docker
docker build --no-cache -t spid-installer:$VERSION .
IMAGE_ID=`docker images --format="{{.Repository}} {{.ID}}" | grep ^spid-installer | head -1 | cut -d' ' -f2`
echo "***"
echo "Tagging image ID $IMAGE_ID..."
echo "***"
docker tag $IMAGE_ID docker.io/$COMPANY_NAME/spid-provider-installer:$VERSION
docker push $COMPANY_NAME/spid-provider-installer:$VERSION
