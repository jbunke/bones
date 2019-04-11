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
    int years = now.year - birthday.year;

    if (now.month < birthday.month ||
      (now.month == birthday.month && now.day < birthday.day)) {
      years--;
    }

    return years;
  }

  string name() {
    return givenName + " " + surname;
  }

  string birthdate() {
    string res = "";

    if (birthday.month == 1) {
      res += "January ";
    } else if (birthday.month == 2) {
      res += "February ";
    } else if (birthday.month == 3) {
      res += "March ";
    } else if (birthday.month == 4) {
      res += "April ";
    } else if (birthday.month == 5) {
      res += "May ";
    } else if (birthday.month == 6) {
      res += "June ";
    } else if (birthday.month == 7) {
      res += "July ";
    } else if (birthday.month == 8) {
      res += "August ";
    } else if (birthday.month == 9) {
      res += "September ";
    } else if (birthday.month == 10) {
      res += "October ";
    } else if (birthday.month == 11) {
      res += "November ";
    } else if (birthday.month == 12) {
      res += "December ";
    }

    res += (string) birthday.day + ", " + (string) birthday.year;

    return res;
  }

  string summary() {
    return call name() + ", born on " + call birthdate();
  }
}