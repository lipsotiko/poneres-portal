./build.sh

az acr login --name poneres
docker build --tag=poneres.azurecr.io/poneres-portal:latest .
docker push poneres.azurecr.io/poneres-portal

az webapp config appsettings set --settings \
AUTH0_ISSUER=$AUTH0_ISSUER \
AUTH0_CLIENT_ID=$AUTH0_CLIENT_ID \
AUTH0_CLIENT_SECRET=$AUTH0_CLIENT_SECRET \
AUTH0_ADMIN_CLIENT_ID=$AUTH0_ADMIN_CLIENT_ID \
AUTH0_ADMIN_CLIENT_SECRET=$AUTH0_ADMIN_CLIENT_SECRET \
MONGODB_URI=$MONGODB_URI \
AWS_REGION=$AWS_REGION \
AWS_ACCESS_KEY=$AWS_ACCESS_KEY \
AWS_ACCESS_SECRET=$AWS_ACCESS_SECRET \
SENDGRID_API_KEY=$SENDGRID_API_KEY \
STRIPE_PRIVATE_KEY=$STRIPE_PRIVATE_KEY \
SIGNWELL_API_KEY=$SIGNWELL_API_KEY \
--name poneres-portal --resource-group poneres

az webapp restart --name poneres-portal --resource-group poneres
