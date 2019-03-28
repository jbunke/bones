path root;

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
    return x + y + z;
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