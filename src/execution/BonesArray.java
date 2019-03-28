package execution;

import java.util.ArrayList;

public class BonesArray<T> extends ArrayList<T> {
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
