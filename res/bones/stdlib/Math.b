path bones.stdlib;

class Math {
  
  int floor(float num) {
    if (num < 0.0) {
      return ((int) num) - 1;
    }
    
    return (int) num;
  }
  
  int ceiling(float num) {
    if (num < 0.0) {
      return ((int) num);
    }

    return ((int) num) + 1;
  }
  
  int round(float num) {
    int cl = call ceiling(num);
    int fl = call floor(num);
    
    float upDiff = ((float) cl) - num;
    float downDiff = num - ((float) fl);
    
    if (downDiff < upDiff) {
      return fl;
    } else {
      return cl;
    }
  }
  
  int abs(int num) {
    if (num < 0) {
      return -num;
    }
    return num;
  }
  
  int max(int a, int b) {
    if (a > b) {
      return a;
    }
    return b;
  }
  
  int min(int a, int b) {
    if (a < b) {
      return a;
    }
    return b;
  }
  
  int listmax(int() nums) {
    int res;
    
    if (#res > 0) {
      res = nums(0);
    } else {
      return 0;
    }
    
    foreach (num : nums) {
      if (num > res) {
        res = num;
      }
    }
    
    return res;
  }
  
  int arraymax(int[] nums) {
    int res;
    
    if (#res > 0) {
      res = nums[0];
    } else {
      return 0;
    }
    
    foreach (num : nums) {
      if (num > res) {
        res = num;
      }
    }
    
    return res;
  }
  
  int listmin(int() nums) {
    int res;
    
    if (#res > 0) {
      res = nums(0);
    } else {
      return 0;
    }
    
    foreach (num : nums) {
      if (num < res) {
        res = num;
      }
    }
    
    return res;
  }
  
  int arraymin(int[] nums) {
    int res;
    
    if (#res > 0) {
      res = nums[0];
    } else {
      return 0;
    }
    
    foreach (num : nums) {
      if (num < res) {
        res = num;
      }
    }
    
    return res;
  }
}

