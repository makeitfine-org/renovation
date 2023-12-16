# renovation
Renovation reporter

# Usage #


Technical configs
-----
### Pre-config env (for start on local db):#
1. Install prostgres db (if not installed) and start it on port: 5432
2. create db:  
   name: postgres  
   schema: renovation  
   username: postgres
   password: postgres  
   (or config env. variables on application.yml datasource:)
3. start RenovationApplication.kt on backend module

### Pre-config env (for start on docker db):
1. Install docker and docker-compose (if not installed)  
2. build project (e.g. `$> gradle buildAll` or `$> gradle ba`) 
3. make:  
`$>docker-compose up`
4. start RenovationApplication.kt on backend module  
  before set env. vars:  
  POSTGRES_PASSWORD=postgres1;  
  POSTGRES_DB_URL=jdbc:postgresql://localhost:5532/postgres?currentSchema=renovation  
  (or config it in .env file)
* To make 2-4 steps automatically with test run:  
`$>gradle all`

### Start application in docker env:
1. assemble project (or run `$>gradle all`)
2. make: `$>docker-compose up`
   or to start in debug mode:
   `$>docker-compose -f docker-compose.yml -f docker-compose-debug.yml up` 
3. open application in browser on port 8180

### Starting work:
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

### Heroku config:
Install heroku:  
https://devcenter.heroku.com/articles/heroku-cli#install-the-heroku-cli  
and java plugin: `heroku plugins:install java`  
1. login heroku:  
   `heroku login`
2. Deploy backend module (with jdk 21 specified) to heroku:  
   `heroku deploy:jar backend/build/libs/backend-0.0.1-SNAPSHOT.jar --jdk 21 --app newk7`
3. See heroku logs:  
   `heroku logs --tail --app aqueous-woodland-63249`
4. See heroku configs:  
   `heroku config`
5. app dashboard:  
   https://dashboard.heroku.com/apps/aqueous-woodland-63249
6. app url:  
   https://aqueous-woodland-63249.herokuapp.com/  

### Start frontend locally
1. run backend for api to work
2. set env. var `VUE_APP_BACKEND_API_URL` in frontend to backend api
   (e.g. if backend port is 8090: `export VUE_APP_BACKEND_API_URL=http://localhost:8090/api`)

### Integrate frontend prod build to backend:
1. On frontend run:  
`$>npm run build`
2. copy `dist/*` files to `backend/main/resources/public/*`
3. Run backend: `http://localhost:<port>/index.html`

See also:  
https://devcenter.heroku.com/articles/deploying-executable-jar-files  
https://devcenter.heroku.com/articles/deploying-spring-boot-apps-to-heroku
https://github.com/heroku/heroku-cli-deploy

### Usefull
1. build backend (skip any kind of test) + run jar (from backend module):  
`$>grcb -x test -x integrationTest -x e2eTest && java -jar build/libs/backend-0.0.1-SNAPSHOT.jar`
2. build project and copy dist to backend static resources:  
`$>gradle clean && gradle :frontend:npmInstall && gradle :frontend:npmBuild 
 && gradle :backend:clean && gradle :backend:compileJava && gradle copyDistToPublic 
 && gradle :backend:build`  
or just  
`$> gradle all`
3. backup db (mysql):
   `$> docker exec renovation-mysql /usr/bin/mysqldump -u root --password=... r2 > backup.sql`

### Use mockserver
From module mockapi run `docker-compose renovation-mockapi up`  
To add/update/delete requests modify config/initializer.json file  
See: https://github.com/makeitfine-org/renovation/issues/36  
Example: `curl http://localhost:1080/api/v1/name?surname=Mosto`

### Checkstyle (code check and auto-fix):
For to check:  
`$> gradle ktlintCheck`  
For to autofix:  
`$> gradle ktlintFormat`

### Work with Kubernetes  
1. Install docker-machine:  
   https://github.com/docker/machine/releases
2. Switch between "minikube" cluster (eval $(minikube docker-env)) and local env (eval $(docker-machine env -u))
3. Upload docker image from local machine to minikube cluster:  
   add minikube addon to upload docker images from local env to cluster:
   `$> minikube addons enable registry`  
    Upload image to `minikube` cluster:  
   `$> minikube image load <IMAGE_NAME>`  
   Install helm:  
   https://helm.sh/docs/intro/install/  
4. Run api tests on k8s cluster:  
   `$> gradle k8sApiTest`  
   (in gradle/scripts/k8sApiTest.sh BACKEND_SERVER_URL can be other, so change)  
   With ingress:  
   `$> gradle k8sIngressApiTest`
4.1 Instead of 5 - 9 belove steps execute script:   
   `$> sh aux/minikube/mn-cluster-create.sh`
5. Upload backend image and redeploy backend service:  
   `$> gradle k8sUploadBackendImage`  
6. Upload info image and redeploy backend service:  
   `$> gradle k8sUploadInfoImage`
7. Create on minikube cluster such dirs:  
   `/mnt/pg/data`, `/mnt/pg/init` (also place `backend/../db-init-scripts/*` folder content here)  
   `/mnt/mongo/data`, `/mnt/mongo/init` (also place `info/../init/*` folder content here)  

   `/mnt/pg-ha/init` (place `backend/../db-init-scripts/*` folder content here)  
   `/mnt/pg-ha/data-0` (make `sudo chown -R 1001:1001 data-0`),  
   `/mnt/pg-ha/data-1` (make `sudo chown -R 1001:1001 data-1`),  
   `/mnt/pg-ha/data-2` (make `sudo chown -R 1001:1001 data-2`),
   
   Or just copy `create-mnt-content.sh` to minikube `/mnt` and execute to create folders  
   7.1 Copy files to minikube: 
       `https://stackoverflow.com/questions/46086303/how-to-transfer-files-between-local-machine-and-minikube`  
   7.2 if to create multinode cluster create folders on each node:  
       `https://minikube.sigs.k8s.io/docs/tutorials/multi_node/`  
       ssh access to one of the multi-nodes (`mn` is name): `mi ssh -p m -n mn-m02`  
       upload images to multi-nodes: `mi image load <image> -p mn`  
8. Deploy/Undeploy all k8s entities scripts:  
   5.1.0 Create `renovation` namespace and make it current:   
   `$> kubectl apply -f aux/k8s/yaml/renovation-namespace.yaml`  
   `$> kubectl config set-context --current --namespace=renovation`
   5.1.1 deploy:  
   `$> sh aux/k8s/scripts/deploy-all.sh`  
   5.2 un-deploy:  
   `$> sh aux/k8s/scripts/delete-all.sh`  
   (all necessary scripts and `kubectl` command can be read from above `sh` scripts)  
   8.1: work with postgres-ha:  
      https://devopscube.com/deploy-postgresql-statefulset/  
   8.2 Connect to `postgrs-ha-sts` pods:  
   `$>kubectl exec -it pg-client -n renovation -- /bin/bash`  
   `$>PGPASSWORD=postgres1 psql -h pgpool-svc -p 5432 -U postgres`  
   or  
   `$>PGPASSWORD=postgres1 psql -h postgres-ha-sts-1.postgres-ha-headless-svc.renovation.svc.cluster.local -p 5432 -U postgres`  
9. Config k8s (https://kubernetes.io/docs/tasks/access-application-cluster/ingress-minikube/):  
    9.1 Add minikube ip to /etc/hosts (example: `192.168.49.2    mi k8s minikube mii mib`)  
        (For multinode server add minikube ip to /etc/hosts (example: `192.168.49.2    mmi mk8s mminikube mmii mmib`)  
    9.2 `$>minikube addon ingress enable`  
    9.3 Apply ingress yaml  
10. Run bash for pod:  
    `$>kubectl exec -it <pod name> -- /bin/sh`  
11. Run helm grafana single-node on multi-cluster specify:  
    (https://tanzu.vmware.com/developer/guides/observability-prometheus-grafana-p1/)  
    execute command line commands:  
    `$>helm install prometheus bitnami/kube-prometheus`  
    (https://artifacthub.io/packages/helm/bitnami/grafana  
     https://tanzu.vmware.com/developer/guides/spring-prometheus/)  
    `$>helm install grafana  bitnami/grafana --set grafana.nodeSelector."<cluser>"=<label>`:  
    For example:  
    `helm install grafana bitnami/grafana --set grafana.nodeSelector."kubernetes\.io/hostname"=mn` 
      
    To config grafana:  
    > on create PROMETHEUS datasource use url from  
    `k describe service -n monitoring prometheus-kube-prometheus-prometheus`    
    Or access to prometheus (nodePort: 31909) and grafana (nodePort:31300)    
12. Open `minikube service`:  
   `$>minikube -p mn service backend --namespace renovation`
13. Test minikube with junit: 
   `$>gradle :api-test:minikube --tests "renovation.backend.api.test.minikube.MinikubeApiTest"`  
### Redis
* To evict/remove 'works' keys redis entities call on backend module server URL:  
  `/api/service/redis/work/evict`

### Frontend-info
* Run locally:  
  `$> npm run build`  
  `$> export VUE_APP_BACKEND_API_URL=http://localhost:8180/api`  
  `$> export VUE_APP_INFO_GRAPHQL_URL=http://localhost:9190`  
  `$> node docker.js`  
* Refresh with clearing cache short-cut: `Cntr+Shift+R`

### Run api test (local):
1) Run backend module locally
2) set env. var: `ACCESS_TOKENS_LOCALHOST=true` and `BACKEND_SERVER_URL=http://localhost:8080`
3) Run gateway locally bootTestRun (from root project):  
`$> ./gradle :gateway:bootTestRun`

### Access tokens from docker renovation network keycloak service:  
* http://localhost:8281/insecure/token/grant/password  
* http://localhost:8281/insecure/token/grant/client  

### Run modules:
#### mockapi:  
1) $>`docker-compose up renovation-mockup`
#### gateway:
1) $>`docker-compose up`  
2) Run GatewayApplication locally (See `SecurityConfig, ExposeApiConfig` for)  
application.yml change:
```
...
    port: ${MONGO_PORT:27117}
...
```
#### backend:
1) $>`docker-compose up`
2.1)  
   - Run RenovationApplication locally (config db params in app*.yml)
    See `application-secured-test.yml` permissions  
   application.yml change:
```
...
    url: "${POSTGRES_DB_URL:jdbc:postgresql://localhost:5532/postgres?currentSchema=renovation}"
...
    password: "${POSTGRES_PASSWORD:postgres1}"
...
    port: ${REDIS_PORT:6479}
...

```
   - Run subj. with csrf:  
SecurityConfig.kt change:
```
.csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
//                .disable() //todo: enable
```
Postman:  
2.2) Execute `Get`: `http://localhost:8080/api/work` with `Bearer Token` and `Test` tab:
```
let xsrfCookie = pm.cookies.get("XSRF-TOKEN")
pm.environment.set('xsrf-token', xsrfCookie)
```
2.2) Execute `Post`: `http://localhost:8080/api/work` with `Bearer Token` and `Test` tab:
```
let xsrfCookie = pm.cookies.get("XSRF-TOKEN")
pm.environment.set('xsrf-token', xsrfCookie)
```
and add `header`: `X-XSRF-TOKEN`=`{{xsrf-token}}`
#### api-test
Execute test `WorkControllerApiTest` locally in Idea:  
set `env. var`: `ACCESS_TOKENS_LOCALHOST=true`  
set `env. var`: `BACKEND_SERVER_URL=http://localhost:8080`

### Codestyle foramtter:
Enable editorconfig plugin in idea and enable editor config support for .editorconfig in root to work

- Code checkstayle check: `gradle ktlintCheck` && `gradle detekt`
- Code checkstayle format: `gradle ktlintFormat`

### Performance:
- https://httpd.apache.org/docs/2.4/programs/ab.html  
`$> ab -n 1600 -c 40 localhost:8080/async_result`

## Reverse proxy nginx for to redirect docker keycloak via local machine:  
- install and start nginx service  
- see: /etc/nginx/conf.d/renovation-keycloak.conf
```
server {
    listen      8080;
    server_name renovation-keycloak;

    location / {
	proxy_redirect      off;
	proxy_set_header    X-Real-IP $remote_addr;
        proxy_set_header    X-Forwarded-For $proxy_add_x_forwarded_for;
	proxy_set_header    Host $http_host;
	proxy_pass 	    http://127.0.0.1:18080/;
    }
}

```
(might be to add to /etc/hosts/ file such record: `#127.0.0.1	renovation-keycloak`)*

## nginx useful:

### for minikube

/etc/host:
```
#127.0.0.1       r l
192.168.49.2    mi k8s minikube mmii mmib
#192.168.58.2   mmi mk8s mminikube mmii mmib
192.168.55.3    mmi mk8s mminikube mmii mmib 
192.168.55.3    www.my web1.my web2.my webx.my cat.my myapps
```

load balancing (/etc/nginx/conf.d/milb.conf):
```
upstream backend {
#        least_conn;
        server 192.168.55.2:30080 weight=3;
        server 192.168.55.3:30082;
    }

    server {
        listen      80;
        server_name milb.com;

        location / {
	        proxy_redirect      off;
	        proxy_set_header    X-Real-IP $remote_addr;
	        proxy_set_header    X-Forwarded-For $proxy_add_x_forwarded_for;
	        proxy_set_header    Host $http_host;
		proxy_pass http://backend;
	}
}
```
forwarding (/etc/nginx/conf.d/micro.conf):
```
server {
    listen 8099;

    server_name micro.com;

    location / {
        proxy_pass http://192.168.55.4:30080;
    }
}
```

## Modules

### Temp
1) Run:  
`$> docker compose up renovation-vault-prepopulate`  
`$> ./gradlew :temp:bootRun`  
2) Token:  
`$> http POST :8080/token --auth test:test -v`  
3) Copy JWT token:  
4) http :8080/about 'Authorization: Bearer <JWT_TOKEN_HERE>'  
