### Authorize mongo db ###

`$> mongo -u infouser -p --authenticationDatabase infodb`

###Build and run test
`$> dcdw && dc build && dcu && gr clean build`  
(docker-compose up && docker-compose build && docker-compose up && gradle clean build)

### Run application
1. Run `docker-compose`
2. Run InfoApplication
3. Check: `http://l:9090/api/v1/info`