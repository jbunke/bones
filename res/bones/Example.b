path root;

class Example {
  int a;

  void main(string[] p_args) {
    int answer = call sumOfThree(6, 7, 8);
    println(10 / 3);
    println(10 / 3.0);
    println(10 / 0);
    print("Answer: ");
    println(answer);
  }

  int sumOfThree(int x, int y, int z) {
    return x + y + z;
  }
}