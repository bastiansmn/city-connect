#!/bin/bash

kubectl delete secret registry-creds -n city-connect
kubectl create secret generic registry-creds \
    --from-file=.dockerconfigjson=/root/.docker/config.json \
    --type=kubernetes.io/dockerconfigjson \
    -n=city-connect
