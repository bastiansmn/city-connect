#!/bin/bash

if [ -z "$1" ]
then
  echo "Spécifiez un namespace pour créer les secrets"
  exit 1
fi

kubectl create secret generic registry-creds \
    --from-file=.dockerconfigjson=/root/.docker/config.json \
    --type=kubernetes.io/dockerconfigjson \
    -n=$1
