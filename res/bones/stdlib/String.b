path bones.stdlib;

class String {
  
  string substringFromStart(string input, int stopAt) {
    return call substring(input, 0, stopAt);
  }
  
  string substringToEnd(string input, int startFrom) {
    return call substring(input, startFrom, #input);
  }
  
  string substring(string input, int startFrom, int stopAt) {
    string res = "";
    
    for (int i = startFrom; i < stopAt; i++) {
      res += (string) (input @ i);
    }
    
    return res;
  }
  
}

