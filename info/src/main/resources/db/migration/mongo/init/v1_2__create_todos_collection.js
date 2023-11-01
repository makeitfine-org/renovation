/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

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
