### Influx service for working with influx database ###

### Build and run test
`$>  c influx-service/ && mci && c .. && dcdw && d rmi koresmosto/renovation-influx-service:latest && docker compose -f docker-compose.yml -f docker-compose-debug.yml up renovation-influx-service`  
