/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

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
