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
  }
  
  void roundingTest() {
    float[] floats = arrayinit { 4.5, 6.7, 2.1, 17.8, 45.2, -0.7 };
    int[] toPopulate = int[6];
    int[] expected = arrayinit{ 5, 7, 2, 18, 45, -1 };
    
    toPopulate[0] = call Math.round(floats[0]);
    toPopulate[1] = call Math.round(floats[1]);
    toPopulate[2] = call Math.round(floats[2]);
    toPopulate[3] = call Math.round(floats[3]);
    toPopulate[4] = call Math.round(floats[4]);
    toPopulate[5] = call Math.round(floats[5]);
    
    println(toPopulate[0] == expected[0]);
    println(toPopulate[1] == expected[1]);
    println(toPopulate[2] == expected[2]);
    println(toPopulate[3] == expected[3]);
    println(toPopulate[4] == expected[4]);
    println(toPopulate[5] == expected[5]);
  }  
}

