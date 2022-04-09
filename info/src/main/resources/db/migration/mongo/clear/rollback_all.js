/*
 *  Created under not commercial project "Make it fine"
 *
 *  Copyright 2017-2021
 *  @author stingion
 */

db.dropUser("infouser");

db = db.getSiblingDB("infodb");
db.dropDatabase();
