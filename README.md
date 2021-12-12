# renovation
Renovation reporter

----

## Local installation ##
1. Install githooks in from folder `aux/githooks`
   (set git comment sign):
`$> git config --local core.commentChar ';'`
2. Postgres port "5432' should be, or change env. var POSTGRES_PORT in .env file  
`$> docker-compose up`
## Using ##
1. Deploy to heroku backend dir (main should be used):  
`$> git subtree push --prefix backend heroku main`  
or if it's fast-forwad push:  
`$> git push --no-verify heroku `git subtree split --prefix backend main`:main --force`
