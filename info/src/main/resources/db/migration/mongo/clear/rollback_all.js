/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2024
 */

db.dropUser("infouser");

db = db.getSiblingDB("infodb");
db.dropDatabase();
