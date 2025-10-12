##### run buildAll
```bash
timeout 15m ./gradlew buildAll 2>&1;\
 BUILD_EXIT_CODE=$?;\
 export NOTIFICATION_TELEGRAM_BOT_TOKEN=$(grep "NOTIFICATION_TELEGRAM_BOT_TOKEN" ~/.bashrc | cut -d'=' -f2 | tr -d '"');\
 export NOTIFICATION_TELEGRAM_CHAT_ID=$(grep "NOTIFICATION_TELEGRAM_CHAT_ID" ~/.bashrc | cut -d'=' -f2 | tr -d '"');\
 if [ $BUILD_EXIT_CODE -eq 124 ];\
 then curl -X POST "https://api.telegram.org/bot$NOTIFICATION_TELEGRAM_BOT_TOKEN/sendMessage"\
 -H "Content-Type: application/json" -d "{\"chat_id\": \"$NOTIFICATION_TELEGRAM_CHAT_ID\", \"text\": \"⌛TIMEOUT⌛\"}";\
 elif [ $BUILD_EXIT_CODE -eq 0 ];\
 then curl -X POST "https://api.telegram.org/bot$NOTIFICATION_TELEGRAM_BOT_TOKEN/sendMessage"\
 -H "Content-Type: application/json" -d "{\"chat_id\": \"$NOTIFICATION_TELEGRAM_CHAT_ID\", \"text\": \"✅BUILD SUCCESSFUL✅\"}";\
 else curl -X POST "https://api.telegram.org/bot$NOTIFICATION_TELEGRAM_BOT_TOKEN/sendMessage"\
 -H "Content-Type: application/json" -d "{\"chat_id\": \"$NOTIFICATION_TELEGRAM_CHAT_ID\", \"text\": \"❌BUILD FAILED❌\"}"; fi
```
