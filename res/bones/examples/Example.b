path bones.examples;

class Example {
  int a;

  void main(string[] p_args) {
    int answer = call sumOfThree(6, 7, 8);
    println(10 / 3);
    println(10 / 3.0);
    println("");
    print("Answer: ");
    println(answer);
    call printLongerLetterByLetter("Jordan", "Chelsea");
    call printLongerLetterByLetter("Timothy", "Jordan");
  }

  int sumOfThree(int x, int y, int z) {
    int b = -46;
    bool c = true;
    // comment in front: should resolve b + c;
    return x + y + z; // comment behind - should still work
  }

  void printLongerLetterByLetter(string first, string second) {
    if (#first > #second) {
      foreach (letter : first) {
        print(letter);
        print(" ");
      }
      println("");
    } else {
      foreach (letter : second) {
        print(letter);
        print(" ");
      }
      println("");
    }
  }
}