/*
 *  Created under not commercial project "Make it fine"
 *
 *  Copyright 2017-2021
 *  @author stingion
 */

db.createUser(
    {
        user: "infouser",
        pwd: "infopassword",
        roles: [
            {role: "readWrite", db: "infodb"},
            {role: "read", db: "reporting"}
        ]
    }
);
