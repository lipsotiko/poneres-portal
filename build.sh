export BUILD_DT=$(date '+%Y-%m-%d')
export DEPLOYMENT_TYPE=develop
cd web-client && npm run generate
cd ..
rm -rf src/main/resources/public
cp -r web-client/.output/public src/main/resources/public
mvn clean package
