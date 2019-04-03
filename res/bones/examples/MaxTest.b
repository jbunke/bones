path bones.examples;

class MaxTest {
  int[] arr1 = arrayinit { 17, 214, -56, 56, 127 } ;
  int[] arr2 = int[8];
  int[] arr3 = arrayinit { 6, 5, 7, 4, 2, 8, 9, 0, 1, 6 } ;

  void main(string[] args) {
    println(call maximum(arr1));
    println(call maximum(arr2));
    println(call maximum(arr3));
  }

  int max(int a, int b) {
    if (a > b) {
      return a;
    }
    return b;
  }

  int maximum(int[] array) {
    int largest;

    if (#array > 0) {
      largest = array[0];
    } else {
      largest = 0;
    }

    foreach (entry : array) {
      largest = call max(largest, entry);
    }

    return largest;
  }
}