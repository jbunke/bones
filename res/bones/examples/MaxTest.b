path bones.examples;

class MaxTest {
  void main(string[] args) {
    call maxTest(0, 1);
    call maxTest(12, 6);
    call maxTest(2532, 33252);
    call maxTest(657, 456);
  }

  int min(int a, int b) {
    if (a < b) {
      return a;
    }
    return b;
  }

  int max(int a, int b) {
    if (a > b) {
      return a;
    }
    return b;
  }

  void maxTest(int a, int b) {
    print("Largest of ");
    print(a);
    print(" and ");
    print(b);
    print(" is: ");
    println(call max(a, b));
  }
}