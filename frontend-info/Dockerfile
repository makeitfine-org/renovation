FROM node:16.14.0-alpine

WORKDIR /app

COPY package-docker.json ./package.json
COPY KeycloakService.js ./KeycloakService.js

RUN npm install

COPY dist ./dist
COPY ./docker.js ./
COPY ./about.json ./

EXPOSE 8080
CMD [ "node", "docker.js" ]
