#!/usr/bin/env bash
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2022
#

sudo mkdir /mnt/mongo
sudo mkdir /mnt/mongo/data
sudo mkdir /mnt/mongo/init

sudo touch /mnt/mongo/init/v1_0__create_introuser.js
sudo touch /mnt/mongo/init/v1_1__create_info_collection.js

sudo mkdir /mnt/pg
sudo mkdir /mnt/pg/data
sudo mkdir /mnt/pg/init

sudo touch /mnt/pg/init/init.sql

sudo mkdir /mnt/pg-ha
sudo mkdir /mnt/pg-ha/data-0
sudo mkdir /mnt/pg-ha/data-1
sudo mkdir /mnt/pg-ha/data-2

sudo chown -R 1001:1001 /mnt/pg-ha/data-0
sudo chown -R 1001:1001 /mnt/pg-ha/data-1
sudo chown -R 1001:1001 /mnt/pg-ha/data-2

sudo mkdir /mnt/pg-ha/init

sudo touch /mnt/pg-ha/init/init.sql

### Add content
cat <<EOT >> /mnt/pg/init/init.sql
create schema renovation;

EOT

cat <<EOT >> /mnt/pg-ha/init/init.sql
create schema renovation;

EOT

cat <<EOT >> /mnt/mongo/init/v1_0__create_introuser.js
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

EOT

cat <<EOT >> /mnt/mongo/init/v1_1__create_info_collection.js
db.createCollection("details");
db.details.insert({
    "_id": ObjectId('62571572b85c114c70d2c101'),
    "name": "Tom",
    surname: "Travolta",
    "age": 27,
    "gender": "Male",
    "detailsEmails": [
        {"email": "tt27@email.one", "emailStatus": "Active"}
    ],
    "additionInfos": [
        {"nickName": "Doro One", "phoneNumber": "+389229222123"},
        {"nickName": "Doro Two", "phoneNumber": "+389229222323"},
    ]
});
db.details.insert({
    "_id": ObjectId('62571572b85c114c70d2c102'),
    "name": "Sam",
    surname: "Berbik",
    "age": 45,
    "gender": "Male"
});
db.details.insert({
    "_id": ObjectId('62571572b85c114c70d2c103'),
    "name": "Alfred",
    surname: "Berbik",
    "age": 33,
    "gender": "Male"
});
db.details.insert({
    "_id": ObjectId('62571572b85c114c70d2c104'),
    "name": "Alfred",
    surname: "Hatton",
    "age": 33,
    "gender": "Male"
});
db.details.insert({
    "_id": ObjectId('62571572b85c114c70d2c105'),
    "name": "Kate",
    surname: "Hatton",
    "age": 33,
    "gender": "Female",
    "detailsEmails": [
        {"email": "kh33@email.com", "emailStatus": "Active"},
        {"email": "kh33_other@email.two", "emailStatus": "Inactive"}
    ]
});
db.details.insert({
    "_id": ObjectId('62571572b85c114c70d2c106'),
    "name": "Maestro",
    surname: "Rave",
    "age": 27,
    "gender": "Male"
});
db.details.insert({
    "_id": ObjectId("62571572b85c114c70d2c107"),
    "name": "Kventin",
    surname: "Toddo",
    "age": 54,
    "gender": "Male",
    "detailsEmails": [{"email": "kt54_other@email.two", "emailStatus": "Closed"}]
});
db.details.insert({
    "_id": ObjectId('62571572b85c114c70d2c108'),
    "name": "El",
    surname: "Jey",
    "age": 18,
    "gender": "Male",
    "additionInfos": [
        {"nickName": "quacky", "phoneNumber": "+389229229123"}
    ]
});
db.details.insert({
    "_id": ObjectId('62571572b85c114c70d2c109'),
    "name": "Anny",
    surname: "Bally",
    "age": 18,
    "gender": "Female"
});

EOT
