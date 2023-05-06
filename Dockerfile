## DOCKER FILE USED TO SERVE JAVADOC ##
FROM --platform=linux/amd64 nginx:1.23.1-alpine
RUN rm -rf /usr/share/nginx/html/*
COPY backend/target/site/apidocs /usr/share/nginx/html
