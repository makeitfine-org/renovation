CREATE (:Author {
  id: 1,
  firstName: 'Igor',
  lastName: 'Fainiv',
  middleName: 'Kindratovych',
  birthDate: date('1971-11-25')
});

CREATE (:Author {
  id: 5,
  firstName: 'Petro',
  lastName: 'Lomiv',
  middleName: 'Kindratovych',
  birthDate: date('1979-03-11')
});

CREATE CONSTRAINT ON (b:Book) ASSERT b.title IS UNIQUE
