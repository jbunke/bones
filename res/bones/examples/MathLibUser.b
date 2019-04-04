path bones.examples;

import bones.stdlib.Math;

class MathLibUser {
  void main(string[] args) {
    call floorTest();
    call roundingTest();
  }
  
  void floorTest() {
    int floorOfNegative = call Math.floor(-5.7);
    int expected = -6;
    println(floorOfNegative == expected);
    println("DONE FLOOR TEST");
  }
  
  void roundingTest() {
    float[] floats = arrayinit { 4.5, 6.7, 2.1, 17.8, 45.2, -0.7 };
    int[] toPopulate = int[6];
    int[] expected = arrayinit{ 5, 7, 2, 18, 45, -1 };
    
    for (int i = 0; i < #toPopulate; i++;) {
      toPopulate[i] = call Math.round(floats[i]);
      println(toPopulate[i] == expected[i]);
    }
    println("DONE ROUNDING TEST");
  }  
}

