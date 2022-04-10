/*
 *  Created under not commercial project "Make it fine"
 *
 *  Copyright 2017-2021
 *  @author stingion
 */

db.createCollection("details");
// db.description.createIndex({"key": 1}, {"unique": true});
db.details.insert({"name": "Tom", surname: "Travolta", "age": 27, "gender": "M"});
db.details.insert({"name": "Sam", surname: "Berbik", "age": 45, "gender": "M"});
db.details.insert({"name": "Alfred", surname: "Berbik", "age": 33, "gender": "M"});
db.details.insert({"name": "Alfred", surname: "Hatton", "age": 33, "gender": "M"});
db.details.insert({"name": "Kate", surname: "Hatton", "age": 33, "gender": "W"});
db.details.insert({"name": "Maestro", surname: "Rave", "age": 27, "gender": "M"});
db.details.insert({"name": "Kventin", surname: "Toddo", "age": 54, "gender": "M"});
db.details.insert({"name": "El", surname: "Jey", "age": 18, "gender": "M"});
db.details.insert({"name": "Anny", surname: "Bally", "age": 18, "gender": "W"});
