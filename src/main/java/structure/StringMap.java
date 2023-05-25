package structure;

import java.util.List;

public interface StringMap<V>{
    List<V> getValuesByPrefix(String prefix);
    void add(String s, V value);
}
