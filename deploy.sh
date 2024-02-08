./build.sh
sudo docker build --tag=icare.azurecr.io/icare:latest .
sudo docker push icare.azurecr.io/icare

az webapp config appsettings set --settings \
AUTH0_ISSUER=$AUTH0_ISSUER \
AUTH0_CLIENT_ID=$AUTH0_CLIENT_ID \
AUTH0_CLIENT_SECRET=$AUTH0_CLIENT_SECRET \
MONGODB_URI=$MONGODB_URI \
AWS_REGION=$AWS_REGION \
AWS_ACCESS_KEY=$AWS_ACCESS_KEY \
AWS_ACCESS_SECRET=$AWS_ACCESS_SECRET \
--name i-care-app --resource-group i-care-app_group

az webapp restart --name i-care-app --resource-group i-care-app_group

#    --settings          : Space-separated appsettings in KEY=VALUE format. Use @{file} to load from
#                          a file. See https://go.microsoft.com/fwlink/?linkid=2219923 for more
#                          information on file format and editing app settings in bulk.
