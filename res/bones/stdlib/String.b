path bones.stdlib;

class String {

  int indexOf(string pattern, string pattern) {
    if (#pattern > #input || #pattern == 0) {
      return -1;
    }

    for (int i = 0; i < (#input - #pattern) + 1; i++) {
      for (int j = 0; j < #pattern; j++) {
        if (input @ (i + j) != pattern @ j) {
          i++;
          j = -1;
        } else if (j + 1 == #pattern) {
          return i;
        }
      }
    }

    return -1;
  }

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

