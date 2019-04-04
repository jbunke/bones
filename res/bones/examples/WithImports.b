path bones.examples;

import bones.examples.Example;
import bones.examples.Example2;

class WithImports {
  void main(string[] args) {
    Example e;

    int sum = call Example.sumOfThree(4, 4, 4);
    println(sum);

    println("EXECUTED");
  }
}
