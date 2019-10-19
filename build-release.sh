#!/bin/bash
set -ex
mvn clean install -DskipTests

VERSION=$(mvn org.apache.maven.plugins:maven-help-plugin:3.1.0:evaluate -Dexpression=project.version -q -DforceStdout)

DIR=$(pwd)
TMPDIR="/tmp/mvn-repo/$(uuidgen)"
mkdir -p $TMPDIR
cd $TMPDIR

git clone git@github.com:amccurry/simple-web-application.git
cd $TMPDIR/simple-web-application
git checkout repository
git pull origin repository

mvn install:install-file \
 -DgroupId=swa \
 -DartifactId=swa-app \
 -Dversion=${VERSION} \
 -Dfile=${DIR}/swa-spi/target/swa-app-${VERSION}.jar \
 -Dpackaging=jar \
 -DgeneratePom=false \
 -DpomFile=${DIR}/swa-app/pom.xml \
 -DlocalRepositoryPath=. \
 -DcreateChecksum=true

mvn install:install-file \
 -DgroupId=swa \
 -DartifactId=swa-spi \
 -Dversion=${VERSION} \
 -Dfile=${DIR}/swa-spi/target/swa-spi-${VERSION}.jar \
 -Dpackaging=jar \
 -DgeneratePom=false \
 -DpomFile=${DIR}/swa-spi/pom.xml \
 -DlocalRepositoryPath=. \
 -DcreateChecksum=true

git add swa
git commit -a -m "Repo release."
git push origin repository

rm -rf $TMPDIR
