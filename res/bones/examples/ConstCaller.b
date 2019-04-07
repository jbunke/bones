path bones.examples;

import bones.examples.WithConst;

class ConstCaller {
  WithConst instance;

  void main(string[] args) {
    instance = new WithConst(20, "Jordan");
    WithConst inst2 = new WithConst(16, "Chelsea");
    println(instance);
    println(inst2);
  }
}