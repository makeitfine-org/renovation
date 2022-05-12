FROM node:lts-alpine

WORKDIR /app

COPY package-docker.json ./package.json

RUN npm install

COPY dist ./dist
COPY ./docker.js ./

EXPOSE 8080
CMD [ "node", "docker.js" ]
