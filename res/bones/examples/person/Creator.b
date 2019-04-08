path bones.examples.person;

import bones.examples.person.Person;
import bones.examples.person.Date;

class Creator {
  void main(string[] args) {
    Person jordan = new Person(new Date(16, 4, 1999), "Jordan", "Bunke");
    Person chelsea = new Person(new Date(31, 1, 2003), "Chelsea", "Bunke");

    println(jordan);
    println(chelsea);
  }
}