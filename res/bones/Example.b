path root;

class Example {
  int a;

  int recFib(int n) {
    if (n < 0) {
      return -1;
    }

    if (n == 0 || n == 1) {
      return n;
    }

    return call recFib(n - 1) + call recFib(n - 2);
  }

  void main(string[] p_args) {
    a = call recFib(20);
  }
}