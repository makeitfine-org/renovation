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
2. assemble project (e.g. `$> gradle buildall`)
3. make:  
`$>docker-compose up`
4. start RenovationApplication.kt on backend module  
  before set env. vars:  
  POSTGRES_PASSWORD=postgres1;  
  POSTGRES_DB_URL=jdbc:postgresql://localhost:5532/postgres?currentSchema=renovation  
  (or config it in .env file)
* To make 2-4 steps automatically with test run:  
`$>gradle all`

###Start application in docker env:
1. assemble project (or run `$>gradle all`)
2. make: `$>docker-compose up`
   or to start in debug mode:
   `$>docker-compose -f docker-compose.yml -f docker-compose-debug.yml up` 
3. open application in browser on port 8180

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
Install heroku:  
https://devcenter.heroku.com/articles/heroku-cli#install-the-heroku-cli  
and java plugin: `heroku plugins:install java`  
1. login heroku:  
   `heroku login`
2. Deploy backend module (with jdk 17 specified) to heroku:  
   `heroku deploy:jar backend/build/libs/backend-0.0.1-SNAPSHOT.jar --jdk 17 --app newk7`
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
`$>gradle clean && gradle :frontend:npmInstall && gradle :frontend:npmBuild 
 && gradle :backend:clean && gradle :backend:compileJava && gradle copyDistToPublic 
 && gradle :backend:build`  
or just  
`$> gradle all`

###Use mockserver
From module gateway run `docker-compose up`  
To add/update/delete requests modify config/initializer.json file  
See: https://github.com/makeitfine-org/renovation/issues/36

###Checkstyle (code check and auto-fix):
For to check:  
`$> gradle ktlintCheck`  
For to autofix:  
`$> gradle ktlintFormat`

###Work with Kubernetes  
1. Install docker-machine:  
   https://github.com/docker/machine/releases
2. Switch between "minikube" cluster (eval $(minikube docker-env)) and local env (eval $(docker-machine env -u))
3. Upload docker image from local machine to minikube cluster:  
   add minikube addon to upload docker images from local env to cluster:
   `$> minikube addons enable registry`  
    Upload image to `minikube` cluster:  
   `$> minikube image load <IMAGE_NAME>`  
4. Run api tests on k8s cluster:  
   `$> gradle k8sApiTest`  
   (in gradle/scripts/k8sApiTest.sh SERVER_URL can be other, so change)  
   With ingress:  
   `$> gradle k8sIngressApiTest`  
5. Upload backend image and redeploy backend service:  
   `$> gradle k8sUploadBackendImage`  
6. Upload info image and redeploy backend service:  
   `$> gradle k8sUploadInfoImage`
7. Create on minikube cluster such dirs:  
   `/mnt/pg/data`, `/mnt/pg/init` (also place `backend/../db-init-scripts/*` folder content here)  
   `/mnt/mongo/data`, `/mnt/mongo/init` (also place `info/../init/*` folder content here)
   7.1 Copy files to minikube: 
       `https://stackoverflow.com/questions/46086303/how-to-transfer-files-between-local-machine-and-minikube`  
8. Deploy/Undeploy all k8s entities scripts:  
   5.1.0 Create `renovation` namespace and make it current:   
   `$> kubectl apply -f aux/k8s/yaml/renovation-namespace.yaml`  
   `$> kubectl config set-context --current --namespace=renovation`
   5.1.1 deploy:  
   `$> sh aux/k8s/scripts/deploy-all.sh`  
   5.2 un-deploy:  
   `$> sh aux/k8s/scripts/delete-all.sh`  
   (all necessary scripts and `kubectl` command can be read from above `sh` scripts)
9. Config k8s (https://kubernetes.io/docs/tasks/access-application-cluster/ingress-minikube/):  
   9.1 Add minikube ip to /etc/hosts (example: `192.168.49.2    mi k8s minikube mii mib`)  
   9.2 Apply ingress yaml 

###Redis
* To evict/remove 'works' keys redis entities call on backend module server URL:  
  `/api/service/redis/work/evict`

###Frontend-info
* Run locally:  
  `$> npm run build`  
  `$> export VUE_APP_BACKEND_API_URL=http://localhost:8180/api`  
  `$> export VUE_APP_INFO_GRAPHQL_URL=http://localhost:9190`  
  `$> node docker.js`  
* Refresh with clearing cache short-cut: `Cntr+Shift+R`
