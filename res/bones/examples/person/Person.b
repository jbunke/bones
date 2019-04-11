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

  int age(Date now) {
    return 0;
  }

  string name() {
    return givenName + " " + surname;
  }

  string birthdate() {
    string res = "TODO";
  }
}