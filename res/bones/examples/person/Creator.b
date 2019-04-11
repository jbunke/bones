path bones.examples.person;

import bones.examples.person.Person;
import bones.examples.person.Date;

class Creator {
  void main(string[] args) {
    Person jordan = new Person(new Date(16, 4, 1999), "Jordan", "Bunke");
    Person chelsea = new Person(new Date(31, 1, 2003), "Chelsea", "Bunke");

    Date today = new Date(11, 4, 2019);

    Person() people = listinit { jordan, chelsea,
                                  new Person(new Date(14, 6, 1968), "Priscilla", "Bunke"),
                                  new Person(new Date(14, 10, 1965), "Olaf", "Bunke") };

    foreach (person : people) {
      println(call person.summary());
      println(call person.age(today));
      println("");
    }
  }
}