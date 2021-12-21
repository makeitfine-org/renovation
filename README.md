# renovation
Renovation reporter

# Usage #


Technical configs
-----
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

