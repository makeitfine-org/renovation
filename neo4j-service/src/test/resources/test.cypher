// 1. Unique constraint
//CREATE CONSTRAINT book_title_unique IF NOT EXISTS
//FOR (b:Book)
//REQUIRE b.title IS UNIQUE;

// 2. Create Authors (explicit id will be ignored if @GeneratedValue is used in Java)
CREATE (a1:Author {
id: 1,
firstName:'Igor',
lastName:'Fainiv',
middleName:'Kindratovych',
birthDate:date('1971-11-25')
});

CREATE (a5:Author {
id: 5,
firstName:'Petro',
lastName:'Lomiv',
middleName:'Kindratovych',
birthDate:date('1979-03-02')
});

// 3. Create Books
CREATE (b1:Book {id: 1, title: 'Spring in Action', year: 2021});
CREATE (b2:Book {id: 2, title: 'Neo4j Graph Magic', year: 2022});
CREATE (b3:Book {id: 3, title: 'Effective Java Graphs', year: 2022});
CREATE (b4:Book {id: 4, title: 'Kotlin for Graph Lovers', year: 2024});

// 4. Create relationships
MATCH (a0:Author {id: 1}), (b1:Book {id: 1}), (b2:Book {id: 2})
CREATE (b1)-[:WRITTEN_BY]->(a0),
(b2)-[:WRITTEN_BY]->(a0);

MATCH (a1:Author {id: 5}), (b3:Book {id: 3}), (b4:Book {id: 4})
CREATE (b3)-[:WRITTEN_BY]->(a1),
(b4)-[:WRITTEN_BY]->(a1);

//MATCH (b:Book)-[:WRITTEN_BY]->(a:Author)
//RETURN b.title AS title, b.year AS year, a.id AS authorId;
