path bones.examples;

class Example2 {
  void main(string[] args) {
    bool done = false;
    int x = 0;
    while (!done) {
      println("How old are you?");
      x = read();
      print("Are you sure that you are ");
      print(x);
      println(" years old?");
      println("Y / N");
      string response = read();
      if (response == "Y") {
        done = true;
      }
    }
    print("Can't wait until you turn ");
    print(x + 1);
    println("!");
  }
}