/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2024
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
