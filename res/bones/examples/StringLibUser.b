path bones.examples;

import bones.stdlib.String;

class StringLibUser {
  void main(string[] args) {
    string firstName = call String.substringFromStart("Jordan Bunke", 6);
    string lastName = call String.substringToEnd("Jordan Bunke", 7);
    println("My first name is " + firstName);
    println("and my last name is " + lastName);
    println(call String.indexOf("ord", firstName));
    println(call String.indexOf("test", firstName));
  }
}