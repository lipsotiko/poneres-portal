export BUILD_DT=$(date '+%Y-%m-%d')
export DEPLOYMENT_TYPE=production
cd icare_client && npm run generate
cd ..
rm -rf src/main/resources/public
cp -r icare_client/.output/public src/main/resources/public
mvn clean package
