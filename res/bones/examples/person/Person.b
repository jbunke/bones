path bones.examples.person;

import bones.examples.person.Date;

class Person {
  Date birthday;
  string givenName;
  string surname;

  constructor(Date setBirthday, string setGivenName, string setSurname) {
    birthday = setBirthday;
    givenName = setGivenName;
    surname = setSurname;
  }
}