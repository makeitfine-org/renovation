### Event service for working with kafka/kafka streams ###

### Build and run test
`$>  rm -rf /tmp/kafka-streams/* && c event-service/ && mci && c .. 
&& dcdw && d rmi koresmosto/renovation-event-service:latest 
&& docker compose -f docker-compose.yml -f docker-compose-debug.yml up renovation-event-service renovation-kafka-ui`  

### Work with stream in event-service
Clear test streams locally:  
$ rm -rf /tmp/kafka-streams/*  
