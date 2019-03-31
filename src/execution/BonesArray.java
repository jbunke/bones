package execution;

import java.util.ArrayList;
import java.util.List;

public class BonesArray<T> extends ArrayList<T> {
  public static <T> BonesArray<T> fromList(List<T> from) {
    BonesArray<T> bonesArray = new BonesArray<>();
    bonesArray.addAll(from);
    return bonesArray;
  }

  public T at(int index) {
    return get(index);
  }

  @Override
  public boolean add(T t) {
    return false;
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
