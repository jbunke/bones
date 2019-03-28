path bones.error.runtime;

class runtimeNullPointer {
  void main(string[] args) {
    int a;

    int b = 6 + a;
  }
}