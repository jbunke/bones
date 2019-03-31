path bones.examples;

class Collections {
  int[] a;
  char() b = listinit {'J', 'o', 'r', 'd', 'a', 'n'};

  void main(string[] args) {
    call assignToA();
    call assignAElementWise(4, 3, 2, 1);
    call printA();
    call printMyNameFromB();
  }

  void assignToA() {
    a = int[4];
  }

  void assignAElementWise(int first, int second, int third, int fourth) {
    a[0] = first;
    a[1] = second;
    a[2] = third;
    a[3] = fourth;
  }

  void printA() {
    foreach (elem : a) {
      println(elem);
    }
  }

  void printMyNameFromB() {
    foreach (letter : b) {
      print(letter);
    }
    println("");
  }
}