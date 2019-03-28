path bones.error.runtime;

class runtimeDivByZero {
  void main(string[] args) {
    int impossible = 10 / 0;
    println(impossible);
  }
}