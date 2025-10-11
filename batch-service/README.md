### Batch service for working with PostgreSQL database (Spring Batch) ###

#### Build and run test  

##### Run with docker compose
`$>  c batch-service/ && mci && c .. && dcdw && d rmi koresmosto/renovation-batch-service:latest && docker compose -f docker-compose.yml -f docker-compose-debug.yml up renovation-batch-service`  

##### Run with docker compose  
1) `terraform init && terraform apply`
2) check `localhost:8280/startup/about`
