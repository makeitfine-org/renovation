/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

db.todos.update(
    {_id: 1},
    {
        $set: {
            description: "Supj. Add its support as separate module with functionality or extend backend module.",
            category: "IT",
            image: "/assets/images/img1",
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
            image: "/assets/images/img2",
            price: 2000.0,
            rating: {
                priority: 2
            }
        }
    }
)

db.todos.update(
    {_id: 3},
    {
        $set: {
            description: "Repair some things",
            category: "Other",
            image: "/assets/images/img3",
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
            image: "/assets/images/img4",
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
            image: "/assets/images/img5",
            rating: {
                priority: 0
            }
        }
    }
)
