##### run buildAll (todo: remake, not actual)
```shell
timeout 7m ./gradlew buildAll 2>&1; BUILD_EXIT_CODE=$?; if [ $BUILD_EXIT_CODE -eq 124 ]; then curl -X POST "https://api.telegram.org/bot$NOTIFICATION_TELEGRAM_BOT_TOKEN/sendMessage" -H "Content-Type: application/json" -d "{\"chat_id\": \"$NOTIFICATION_TELEGRAM_CHAT_ID\", \"text\": \"⌛TIMEOUT⌛\"}"; elif [ $BUILD_EXIT_CODE -eq 0 ]; then curl -X POST "https://api.telegram.org/bot$NOTIFICATION_TELEGRAM_BOT_TOKEN/sendMessage" -H "Content-Type: application/json" -d "{\"chat_id\": \"$NOTIFICATION_TELEGRAM_CHAT_ID\", \"text\": \"✅BUILD SUCCESSFUL✅\"}"; else curl -X POST "https://api.telegram.org/bot$NOTIFICATION_TELEGRAM_BOT_TOKEN/sendMessage" -H "Content-Type: application/json" -d "{\"chat_id\": \"$NOTIFICATION_TELEGRAM_CHAT_ID\", \"text\": \"❌BUILD FAILED❌\"}"; fi
```

```shell
gr ba;tnsimple
```

```shell
gr ba;tn <message>
```

```shell
Change "componentTest" to "integrationTest" in @event-service/ tests to "componentTest"

and then run `./gradlew :event-service:integrationTest` and if build successful exec in terminal exec `tn "Succces"` otherwise `tn "Failed"`
```

```shell
remove 'event-service' modules and its references in the project and  after exec alias a>tg 'AI job Finished'

During execution:
-  if any question to me or if I need to approve any command, for example "rm"  or any other just exec alias a>tg with message of short description of the command you whant me to approve before asking for approval
and then proceed with execution you planned about to solve the task
```
