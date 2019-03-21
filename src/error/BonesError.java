package error;

public class BonesError {
  private final Category category;
  private final String message;

  BonesError(Category category, String message) {
    this.category = category;
    this.message = message;
  }

  public enum Category {
    SYNTAX, SEMANTIC, RUNTIME
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof BonesError) {
      BonesError other = (BonesError) obj;
      return message.equals(other.message) && category == other.category;
    }
    return false;
  }

  @Override
  public String toString() {
    return "Bones " + category.toString() + " Error: " + message;
  }
}
