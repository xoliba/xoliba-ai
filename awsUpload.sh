#!/bin/bash

eval $(aws ecr get-login --no-include-email --region us-east-2)
docker build -t xoliba-ai-aws .
docker tag xoliba-ai-aws:latest $AWS_ACCOUNT_ID.dkr.ecr.us-east-2.amazonaws.com/xoliba-docker-repo:ai
docker push $AWS_ACCOUNT_ID.dkr.ecr.us-east-2.amazonaws.com/xoliba-docker-repo:ai
