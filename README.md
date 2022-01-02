# renovation
Renovation reporter

# Usage #


Technical configs
-----
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
