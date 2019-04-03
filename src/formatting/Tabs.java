package formatting;

public class Tabs {
  public static String tabLines(String s) {
    StringBuilder res = new StringBuilder();
    String[] lines = s.split("\n");

    for (String line : lines) {
      line = "\t" + line + "\n";
      res.append(line);
    }

    return res.toString();
  }
}
