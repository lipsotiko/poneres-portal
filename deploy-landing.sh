BUCKET="poneres-portal-landing"

aws s3 sync ./web-landing "s3://$BUCKET"  --delete

echo "http://$BUCKET.s3-website.us-east-1.amazonaws.com"
