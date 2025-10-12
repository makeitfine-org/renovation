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
##### run buildAll
```bash
gr ba;tnsimple
```
#### Import into ~/.bashrc
```bash
# using: tnot "<message>"
tn() {
local msg="${*:-🔔 Job finished 🔔}"exit
local api="https://api.telegram.org/bot${NOTIFICATION_TELEGRAM_BOT_TOKEN}/sendMessage"
curl -sS -X POST "$api" \
--data "chat_id=${NOTIFICATION_TELEGRAM_CHAT_ID}" \
--data-urlencode "text=$msg"
}

tncut() {
local raw="${*:-🔔 Job finished 🔔}"
# Fast substring (may cut a multi-byte char in UTF-8 edge cases)
local msg="${raw:0:1000}"

# HTML-escape for parse_mode=HTML
local esc
esc=$(printf '%s' "$msg" | sed -e 's/&/\&amp;/g' -e 's/</\&lt;/g' -e 's/>/\&gt;/g')

local api="https://api.telegram.org/bot${NOTIFICATION_TELEGRAM_BOT_TOKEN}/sendMessage"
curl -sS -X POST "$api" \
--data "chat_id=${NOTIFICATION_TELEGRAM_CHAT_ID}" \
--data "parse_mode=HTML" \
--data-urlencode "text=<pre><code>${esc}</code></pre>"
}
```
