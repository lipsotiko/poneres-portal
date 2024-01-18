cd icare_client && npm run generate
cd ..
rm -rf src/main/resources/public
cp -r icare_client/.output/public src/main/resources/public
mvn clean package
sudo docker build --tag=icare.azurecr.io/icare:latest .
sudo docker push icare.azurecr.io/icare
