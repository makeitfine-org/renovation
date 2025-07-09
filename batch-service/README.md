### Batch service for working with neo4j database ###

### Build and run test
`$>  c batch-service/ && mci && c .. && dcdw && d rmi koresmosto/renovation-batch-service:latest && docker compose -f docker-compose.yml -f docker-compose-debug.yml up renovation-batch-service`  
