#!/usr/bin/env bash
#
# Created under not commercial project "Renovation"
#
# Copyright 2021-2025
#

### Configs for HA Postgress DB
sudo mkdir /mnt/mongo
sudo mkdir /mnt/mongo/data
sudo mkdir /mnt/mongo/init

sudo touch /mnt/mongo/init/v1_0__create_introuser.js
sudo touch /mnt/mongo/init/v1_1__create_details_collection.js

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

### Add content for postgress (ha and simple)
cat <<EOT >> /mnt/pg/init/init.sql
create schema renovation;

EOT

### Add content for postgress-ha only
cat <<EOT >> /mnt/pg-ha/init/init.sql
create schema renovation;

EOT

### Content for Mongo
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

cat <<EOT >> /mnt/mongo/init/v1_1__create_details_collection.js
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

cat <<EOT >> /mnt/mongo/init/v1_2__create_todos_collection.js
db.createCollection("todos");

db.todos.insert({
    // "_id": ObjectId('11571572b11c114c70d2c101'),
    _id: 1,
    title: "Add Casandra support",
    completed: false,
    date: new Date("2023-10-18T14:10:30Z")
});

db.todos.insert({
    // "_id": ObjectId('12571572a12c114c70a2c101'),
    _id: 2,
    title: "Buy sport products",
    completed: true,
    date: new Date("2022-03-18T13:10:00Z")
});

db.todos.insert({
    // "_id": ObjectId('c2571572a12b114c70a2c212'),
    _id: 3,
    title: "Improve home staff",
    completed: false,
    date: new Date("2023-06-11T10:10:00Z")
});

db.todos.insert({
    // "_id": ObjectId('a3371572a12c114c70a2c222'),
    _id: 4,
    title: "Add kubernates yamls",
    completed: false,
    date: new Date("2023-08-05T13:10:00Z")
});

db.todos.insert({
    // "_id": ObjectId('a2571572333c114c70a2c222'),
    _id: 5,
    title: "Add Postgress configs",
    completed: true,
    date: new Date("2022-03-10T20:15:00Z")
});

EOT

cat <<EOT >> /mnt/mongo/init/v1_3__extend_todos_collection.js
db.todos.update(
    {_id: 1},
    {
        $set: {
            description: "Subj. Add its support as separate module with functionality or extend backend module.",
            category: "IT",
            image: "/assets/images/img1.jpg",
            price: 0.0,
            rating: {
                priority: 5
            }
        }
    }
)

db.todos.update(
    {_id: 2},
    {
        $set: {
            description: "Buy protein, creatine, glutamine",
            category: "Other",
            image: "/assets/images/img2.jpg",
            price: 2000.0
        }
    }
)

db.todos.update(
    {_id: 3},
    {
        $set: {
            description: "Repair some things",
            category: "Other",
            price: 500.50,
            rating: {
                priority: 3
            }
        }
    }
)

db.todos.update(
    {_id: 4},
    {
        $set: {
            description: "Improve kubernetes yaml and helm configs in project",
            category: "IT",
            image: "/assets/images/img4.jpg",
            rating: {
                priority: 4
            }
        }
    }
)

db.todos.update(
    {_id: 5},
    {
        $set: {
            category: "IT",
            image: "/assets/images/img5.jpg",
            rating: {
                priority: 0
            }
        }
    }
)

EOT
