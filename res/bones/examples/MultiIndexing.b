path bones.examples;

class MultiIndexing {
  int[][] multidim = arrayinit { arrayinit { 1, 2, 3 }, arrayinit { 4, 5, 6 } };
  
  void main (string[] args) {
    int index = multidim[1][2];
    println(6 == index);
  }
}

