path bones.examples;

class Casting {
  void main(string[] args) {
    call floatToInt();
    call stringToBool();
    call intToChar();
    // call castingError();
  }

  void floatToInt() {
    float someF = 5.5;

    print("Integer value of ");
    print(someF);
    print(": ");
    println((int) someF);
  }

  void stringToBool() {
    string someS = "true";
    string someOtherS = "false";

    if ((bool) someS) {
      println("Expected");
    } else {
      println("ERROR");
    }

    if (!(bool) someOtherS) {
      println("Expected");
    } else {
      println("ERROR");
    }
  }

  void intToChar() {
    int someI = 65;
    print("The first letter of the alphabet is ");
    println((char) someI);
  }

  void castingError() {
    string invalid = "looky here";

    bool someB = (bool) invalid;
  }
}