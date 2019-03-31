package execution;

import java.util.ArrayList;
import java.util.List;

public class BonesList<T> extends ArrayList<T> {
  public static <T> BonesList<T> fromList(List<T> from) {
    BonesList<T> bonesList = new BonesList<>();
    bonesList.addAll(from);
    return bonesList;
  }

  public T at(int index) {
    return get(index);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof BonesList)) return false;

    BonesList other = (BonesList) o;

    if (size() != other.size()) return false;

    for (int i = 0; i < size(); i++) {
      if (!(get(i).equals(other.get(i)))) return false;
    }
    return true;
  }
}
