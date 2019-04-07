path bones.examples;

class RandomExample {
  void main(string[] args) {
    float() randoms = listinit { random(), random(), random() };

    foreach (element : randoms) {
      println (element);
    }
  }
}