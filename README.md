# renovation
Renovation reporter

# Usage #


Technical configs
-----
###Pre-config env (for start on local db):
1. Install prostgres db (if not installed) and start it on port: 5432
2. create db:  
   name: postgres  
   schema: renovation  
   username: postgres
   password: postgres  
   (or config env. variables on application.yml datasource:)
3. start RenovationApplication.kt on backend module

###Pre-config env (for start on docker db):
1. Install docker and docker-compose (if not installed)  
2. assemble project (e.g. `$> gradle all`)
3. make:  
`$>docker-compose up`
4. start RenovationApplication.kt on backend module  
  before set env. vars:  
  POSTGRES_PASSWORD=postgres1;  
  POSTGRES_DB_URL=jdbc:postgresql://localhost:5532/postgres?currentSchema=renovation  
  (or config it in .env file)

###Start application in docker env:
1. assemble project (or run `$>gradle all`)
2. make: `$>docker-compose up`
   or to start in debug mode:
   `$>docker-compose -f docker-compose.yml -f docker-compose-debug.yml up` 
3. open application in browser on port 8085

###Starting work:
1. First after cloning repo config githook for project:  
   Run gradle task from base module `renovation`:  
   `$> gradle installGitHooks`

2. To init project execute:  
   `$>gradle all`
3. start `backend`:  
   `$>gradle bootRun`
4. start `frontend`:
   `$>npm install && npm start`
5. start mid with live reload pages (instant refresh in browser):  
   `$>DEBUG=mid:* node app.js`
6. Debug frontend:  
   start server in debug mode:  
   `$>node --inspect ./node_modules/@vue/cli-service/bin/vue-cli-service.js serve`
   connect from Intellij idea: create debug/run config:   
   "debug javascript" > insert in url: "ws://127.0.0.1:9229/b727369a-1135-480a-9111-ad90f3c40a52" > run it in debug mode

###Heroku config:
1. login heroku:  
   `heroku login`
2. Deploy backend module (with jdk 17 specified) to heroku:  
   `heroku deploy:jar backend/build/libs/backend-0.0.1-SNAPSHOT.jar --jdk 17 --app aqueous-woodland-63249`
3. See heroku logs:  
   `heroku logs --tail --app aqueous-woodland-63249`
4. See heroku configs:  
   `heroku config`
5. app dashboard:  
   https://dashboard.heroku.com/apps/aqueous-woodland-63249
6. app url:  
   https://aqueous-woodland-63249.herokuapp.com/  

###Start frontend locally
1. run backend for api to work
2. set env. var `VUE_APP_BACKEND_API_URL` in frontend to backend api
   (e.g. if backend port is 8090: `export VUE_APP_BACKEND_API_URL=http://localhost:8090/api`)

###Integrate frontend prod build to backend:
1. On frontend run:  
`$>npm run build`
2. copy `dist/*` files to `backend/main/resources/public/*`
3. Run backend: `http://localhost:<port>/index.html`

See also:  
https://devcenter.heroku.com/articles/deploying-executable-jar-files  
https://devcenter.heroku.com/articles/deploying-spring-boot-apps-to-heroku
https://github.com/heroku/heroku-cli-deploy

###Usefull
1. build backend (skip any kind of test) + run jar (from backend module):  
`$>grcb -x test -x healthCheck -x smokeTest -x intTest -x functionalTest && java -jar build/libs/backend-0.0.1-SNAPSHOT.jar`
2. build project and copy dist to backend static resources:  
`$>gradle clean && gradle :frontend:npmInstall --build-cache && gradle :frontend:npmBuild --build-cache 
 && gradle :backend:clean --build-cache && gradle :backend:compileJava --build-cache && gradle copyDistToPublic 
 && gradle :backend:build --build-cache`  
or just  
`$> gradle all`
