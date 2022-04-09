/*
 *  Created under not commercial project "Make it fine"
 *
 *  Copyright 2017-2021
 *  @author stingion
 */

db.createCollection("description");
db.description.createIndex({"key": 1}, {"unique": true});
db.description.insert({"key": "author", "details": "koresmosto@gmail.com"});
db.description.insert({"key": "purpose", "details": "Social Network for workers"});
db.description.insert({"key": "stage", "details": "Development stage"});
