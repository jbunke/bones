path bones.error.semantic;

class semanticUndeclaredVariable {
  void main(string[] args) {
    int a = b + 10;
  }
}