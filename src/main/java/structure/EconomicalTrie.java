package structure;

import lombok.Getter;

import java.util.*;

public class EconomicalTrie<V> implements StringMap<V> {
    private static class Node<V> {
        private static final int COLLECTION_INITIAL_CAPACITY = 2;
        private List<V> values = null;
        private Map<Character, Node<V>> children = null;

        public void addValue(V value) {
            if (this.values == null)
                this.values = new ArrayList<>(COLLECTION_INITIAL_CAPACITY);
            this.values.add(value);
        }

        public void addChildNode(char symbol, Node<V> node) {
            if (isLeaf()) this.children = new HashMap<>(COLLECTION_INITIAL_CAPACITY);
            this.children.put(symbol, node);
        }

        public Node<V> getChild(char symbol) {
            if (isLeaf()) return null;
            return this.children.get(symbol);
        }

        public boolean isLeaf() {
            return this.children == null;
        }
    }

    @Getter
    private final Node<V> root = new Node<>();

    @Override
    public List<V> getValuesByPrefix(String prefix) {
        prefix = prefix.toLowerCase();
        Node<V> node = this.root;
        for (char symbol : prefix.toCharArray()) {
            if (node.getChild(symbol) == null) {
                return List.of();
            } else {
                node = node.getChild(symbol);
                assert node != null;
            }
        }
        return getValues(node);
    }

    private List<V> getValues(Node<V> node) {
        List<V> values = new ArrayList<>();
        if (node.values != null)
            values.addAll(node.values);
        if (!node.isLeaf()) {
            node.children
                    .keySet().stream()
                    .sorted(Character::compareTo)
                    .forEach(key -> values.addAll(getValues(node.children.get(key))));
        }
        return values;
    }

    @Override
    public void add(String s, V value) {
        s = s.toLowerCase();
        Node<V> node = this.root;
        for (char symbol : s.toCharArray()) {
            if (node.getChild(symbol) == null) {
                node.addChildNode(symbol, node = new Node<>());
            } else {
                node = node.getChild(symbol);
                assert node != null;
            }
        }
        node.addValue(value);
    }
}
