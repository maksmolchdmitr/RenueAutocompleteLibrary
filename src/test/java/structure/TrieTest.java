package structure;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrieTest {
    @Test
    void addTrieSimpleTest() {
        StringMap<String> stringMap = new Trie<>();
        stringMap.add("car", "car");
        stringMap.add("cat", "cat");
        stringMap.add("cow", "cow");
        assertEquals(List.of("car", "cat", "cow"), stringMap.getValuesByPrefix("c"));
    }

    @Test
    void addTrieSortedTest() {
        StringMap<String> stringMap = new Trie<>();
        List<String> words = new ArrayList<>(List.of("cat", "dog", "fish", "cow", "wolf", "man", "woman", "eggs", "fox", "zoo"));
        words.sort(String::compareTo);
        words.forEach(w -> stringMap.add(w, w));
        assertEquals(words, stringMap.getValuesByPrefix(""));
    }

}