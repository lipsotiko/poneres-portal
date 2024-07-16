# iCare

An application management system to help patients procure insulin for free.
Populates PDF forms with patient data, and captures / applies handwritten signatures.

## Dependencies

* Docker
* Mongo DB
* Java v21
* Node

## Backend
* `./mongo-start.sh`
* `mvn spring-boot:run -Dspring-boot.run.profiles=develop`

## Frontend
* `cd icare_client`
* `npm run dev`


## Deployment
`.deploy.sh`

## Environment Variables
* AUTH0_ISSUER
* AUTH0_CLIENT_ID
* AUTH0_CLIENT_SECRET
* MONGODB_URI
* AWS_REGION
* AWS_ACCESS_KEY
* AWS_ACCESS_SECRET
