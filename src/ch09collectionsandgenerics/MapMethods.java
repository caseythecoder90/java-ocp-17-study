package ch09collectionsandgenerics;

import java.util.*;
import java.util.function.BiFunction;

/**
 * Map Interface - HashMap, TreeMap, LinkedHashMap
 *
 * ===== MAP IMPLEMENTATIONS =====
 *
 * HashMap:
 * - Key-value pairs
 * - No guaranteed order
 * - Uses hashCode() for keys
 * - Fast operations (O(1))
 * - Allows one null key and null values
 *
 * TreeMap:
 * - Key-value pairs
 * - SORTED by keys (natural order or Comparator)
 * - Uses compareTo() or Comparator for keys
 * - Slower operations (O(log n))
 * - Does NOT allow null keys (throws NullPointerException)
 * - Allows null values
 *
 * LinkedHashMap:
 * - Key-value pairs
 * - Maintains INSERTION ORDER
 * - Uses hashCode() for keys
 * - Slightly slower than HashMap
 * - Allows one null key and null values
 *
 * ===== FACTORY METHODS =====
 *
 * Map.of(k1, v1, k2, v2, ...):
 * - IMMUTABLE map (up to 10 key-value pairs)
 * - Does NOT allow null keys or values
 * - Does NOT allow duplicate keys
 *
 * Map.ofEntries(entry1, entry2, ...):
 * - IMMUTABLE map (any number of entries)
 * - Use Map.entry(key, value) to create entries
 * - Does NOT allow null keys or values
 *
 * Map.copyOf(map):
 * - IMMUTABLE copy
 * - Does NOT allow null keys or values
 *
 * ===== MAP METHODS (MEMORIZE SIGNATURES AND BEHAVIOR) =====
 *
 * void clear()                                      - Removes all entries
 * boolean containsKey(Object key)                   - Returns true if key exists
 * boolean containsValue(Object value)               - Returns true if value exists
 * Set<Map.Entry<K,V>> entrySet()                    - Returns set view of entries
 * void forEach(BiConsumer<K,V> action)              - Performs action for each entry
 * V get(Object key)                                 - Returns value for key, null if not found
 * V getOrDefault(Object key, V defaultValue)        - Returns value or default if not found
 * boolean isEmpty()                                 - Returns true if empty
 * Set<K> keySet()                                   - Returns set view of keys
 * V put(K key, V value)                             - Adds/updates entry, returns old value or null
 * V putIfAbsent(K key, V value)                     - Adds only if key not present, returns existing value or null
 * V remove(Object key)                              - Removes entry, returns value or null
 * boolean remove(Object key, Object value)          - Removes only if key maps to value
 * V replace(K key, V value)                         - Replaces value if key exists, returns old value or null
 * boolean replace(K key, V oldValue, V newValue)    - Replaces only if key maps to oldValue
 * void replaceAll(BiFunction<K,V,V> function)       - Replaces all values using function
 * int size()                                        - Returns number of entries
 * Collection<V> values()                            - Returns collection view of values
 *
 * ===== ADVANCED METHODS (CRITICAL FOR EXAM) =====
 *
 * V merge(K key, V value, BiFunction<V,V,V> remappingFunction):
 * - If key absent or null: puts value
 * - If key present: applies function to (oldValue, newValue), puts result
 * - If function returns null: removes key
 *
 * V compute(K key, BiFunction<K,V,V> remappingFunction):
 * - Applies function to (key, oldValue), puts result
 * - If function returns null: removes key (if present)
 *
 * V computeIfAbsent(K key, Function<K,V> mappingFunction):
 * - If key absent or null: applies function to key, puts result
 * - If key present: returns existing value
 *
 * V computeIfPresent(K key, BiFunction<K,V,V> remappingFunction):
 * - If key present and value not null: applies function, puts result
 * - If function returns null: removes key
 *
 * ===== SORTED VS UNSORTED =====
 * HashMap: NOT sorted, uses hashCode()
 * TreeMap: SORTED by keys, uses compareTo() or Comparator
 * LinkedHashMap: INSERTION ORDER, uses hashCode()
 */
public class MapMethods {

    public static void main(String[] args) {
        demonstrateBasicMethods();
        demonstrateHashMapVsTreeMapVsLinkedHashMap();
        demonstrateFactoryMethods();
        demonstrateMerge();
        demonstrateCompute();
        demonstrateComputeIfAbsent();
        demonstrateComputeIfPresent();
    }

    // ===== BASIC MAP METHODS =====

    public static void demonstrateBasicMethods() {
        System.out.println("=== BASIC MAP METHODS ===\n");

        Map<String, Integer> map = new HashMap<>();

        // V put(K key, V value) - returns old value or null
        System.out.println("put('Alice', 25): " + map.put("Alice", 25));
        System.out.println("put('Bob', 30): " + map.put("Bob", 30));
        System.out.println("put('Alice', 26): " + map.put("Alice", 26) + " (returns old value)");
        System.out.println("Map: " + map);

        // int size()
        System.out.println("\nsize(): " + map.size());

        // boolean isEmpty()
        System.out.println("isEmpty(): " + map.isEmpty());

        // V get(Object key) - returns null if not found
        System.out.println("\nget('Alice'): " + map.get("Alice"));
        System.out.println("get('Charlie'): " + map.get("Charlie") + " (not found)");

        // V getOrDefault(Object key, V defaultValue)
        System.out.println("getOrDefault('Charlie', 0): " + map.getOrDefault("Charlie", 0));

        // boolean containsKey(Object key)
        System.out.println("\ncontainsKey('Bob'): " + map.containsKey("Bob"));
        System.out.println("containsKey('Charlie'): " + map.containsKey("Charlie"));

        // boolean containsValue(Object value)
        System.out.println("\ncontainsValue(26): " + map.containsValue(26));
        System.out.println("containsValue(100): " + map.containsValue(100));

        // V putIfAbsent(K key, V value) - returns existing value or null
        System.out.println("\nputIfAbsent('Bob', 35): " + map.putIfAbsent("Bob", 35) + " (key exists)");
        System.out.println("putIfAbsent('Charlie', 28): " + map.putIfAbsent("Charlie", 28) + " (key absent)");
        System.out.println("Map: " + map);

        // V replace(K key, V value) - returns old value or null
        System.out.println("\nreplace('Bob', 31): " + map.replace("Bob", 31));
        System.out.println("replace('David', 40): " + map.replace("David", 40) + " (key doesn't exist)");
        System.out.println("Map: " + map);

        // boolean replace(K key, V oldValue, V newValue)
        System.out.println("\nreplace('Bob', 31, 32): " + map.replace("Bob", 31, 32) + " (matches)");
        System.out.println("replace('Bob', 50, 33): " + map.replace("Bob", 50, 33) + " (doesn't match)");
        System.out.println("Map: " + map);

        // V remove(Object key) - returns value or null
        System.out.println("\nremove('Charlie'): " + map.remove("Charlie"));
        System.out.println("Map: " + map);

        // boolean remove(Object key, Object value)
        map.put("Eve", 29);
        System.out.println("remove('Eve', 29): " + map.remove("Eve", 29) + " (matches)");
        System.out.println("remove('Bob', 50): " + map.remove("Bob", 50) + " (doesn't match)");
        System.out.println("Map: " + map);

        // Set<K> keySet()
        System.out.println("\nkeySet(): " + map.keySet());

        // Collection<V> values()
        System.out.println("values(): " + map.values());

        // Set<Map.Entry<K,V>> entrySet()
        System.out.println("entrySet(): " + map.entrySet());

        // void forEach(BiConsumer<K,V> action)
        System.out.print("forEach: ");
        map.forEach((k, v) -> System.out.print(k + "=" + v + " "));
        System.out.println();

        // void replaceAll(BiFunction<K,V,V> function)
        map.replaceAll((k, v) -> v + 1);
        System.out.println("\nAfter replaceAll(v -> v + 1): " + map);

        // void clear()
        map.clear();
        System.out.println("\nAfter clear(): " + map);
        System.out.println("isEmpty(): " + map.isEmpty());
    }

    // ===== HashMap vs TreeMap vs LinkedHashMap =====

    public static void demonstrateHashMapVsTreeMapVsLinkedHashMap() {
        System.out.println("\n=== HashMap vs TreeMap vs LinkedHashMap ===\n");

        // HashMap - no guaranteed order
        Map<String, Integer> hashMap = new HashMap<>();
        hashMap.put("Charlie", 3);
        hashMap.put("Alice", 1);
        hashMap.put("Bob", 2);
        System.out.println("HashMap (no order): " + hashMap);
        System.out.println("Uses hashCode() for keys");

        // TreeMap - sorted by keys
        Map<String, Integer> treeMap = new TreeMap<>();
        treeMap.put("Charlie", 3);
        treeMap.put("Alice", 1);
        treeMap.put("Bob", 2);
        System.out.println("\nTreeMap (sorted by keys): " + treeMap);
        System.out.println("Uses compareTo() for keys");

        // LinkedHashMap - insertion order
        Map<String, Integer> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("Charlie", 3);
        linkedHashMap.put("Alice", 1);
        linkedHashMap.put("Bob", 2);
        System.out.println("\nLinkedHashMap (insertion order): " + linkedHashMap);
        System.out.println("Uses hashCode() for keys");

        // Null key handling
        hashMap.put(null, 99);
        System.out.println("\nHashMap allows null key: " + hashMap);

        linkedHashMap.put(null, 99);
        System.out.println("LinkedHashMap allows null key: " + linkedHashMap);

        try {
            treeMap.put(null, 99);
        } catch (NullPointerException e) {
            System.out.println("TreeMap null key: NullPointerException ✗");
        }
    }

    // ===== FACTORY METHODS =====

    public static void demonstrateFactoryMethods() {
        System.out.println("\n=== FACTORY METHODS ===\n");

        // Map.of() - up to 10 key-value pairs
        Map<String, Integer> map1 = Map.of("A", 1, "B", 2, "C", 3);
        System.out.println("Map.of: " + map1);

        // Map.ofEntries() - any number of entries
        Map<String, Integer> map2 = Map.ofEntries(
            Map.entry("A", 1),
            Map.entry("B", 2),
            Map.entry("C", 3)
        );
        System.out.println("Map.ofEntries: " + map2);

        // Map.copyOf() - immutable copy
        Map<String, Integer> original = new HashMap<>();
        original.put("X", 10);
        original.put("Y", 20);
        Map<String, Integer> copy = Map.copyOf(original);
        System.out.println("\nMap.copyOf: " + copy);

        // All are IMMUTABLE
        try {
            map1.put("D", 4);
        } catch (UnsupportedOperationException e) {
            System.out.println("\nMap.of - put(): UnsupportedOperationException ✗");
        }

        try {
            map2.remove("A");
        } catch (UnsupportedOperationException e) {
            System.out.println("Map.ofEntries - remove(): UnsupportedOperationException ✗");
        }

        try {
            copy.clear();
        } catch (UnsupportedOperationException e) {
            System.out.println("Map.copyOf - clear(): UnsupportedOperationException ✗");
        }

        // No null keys or values
        try {
            Map<String, Integer> nullMap = Map.of("A", null);
        } catch (NullPointerException e) {
            System.out.println("\nMap.of with null value: NullPointerException ✗");
        }

        try {
            Map<String, Integer> nullMap = Map.of(null, 1);
        } catch (NullPointerException e) {
            System.out.println("Map.of with null key: NullPointerException ✗");
        }
    }

    // ===== MERGE METHOD (VERY IMPORTANT) =====

    public static void demonstrateMerge() {
        System.out.println("\n=== MERGE METHOD ===\n");

        Map<String, Integer> map = new HashMap<>();
        map.put("Alice", 10);
        map.put("Bob", 20);

        System.out.println("Initial: " + map);

        // merge(K key, V value, BiFunction<V,V,V> remappingFunction)
        // If key absent: puts value
        map.merge("Charlie", 5, (oldVal, newVal) -> oldVal + newVal);
        System.out.println("\nmerge('Charlie', 5, sum): " + map + " (key absent, puts 5)");

        // If key present: applies function to (oldValue, newValue)
        map.merge("Alice", 5, (oldVal, newVal) -> oldVal + newVal);
        System.out.println("merge('Alice', 5, sum): " + map + " (10 + 5 = 15)");

        // If function returns null: removes key
        map.merge("Bob", 0, (oldVal, newVal) -> null);
        System.out.println("merge('Bob', 0, returns null): " + map + " (Bob removed)");

        // Common use case: counting occurrences
        Map<String, Integer> counts = new HashMap<>();
        String[] words = {"apple", "banana", "apple", "cherry", "banana", "apple"};

        for (String word : words) {
            counts.merge(word, 1, (oldCount, one) -> oldCount + one);
            // Equivalent to: counts.merge(word, 1, Integer::sum);
        }
        System.out.println("\nWord counts using merge: " + counts);
    }

    // ===== COMPUTE METHOD =====

    public static void demonstrateCompute() {
        System.out.println("\n=== COMPUTE METHOD ===\n");

        Map<String, Integer> map = new HashMap<>();
        map.put("Alice", 10);
        map.put("Bob", 20);

        System.out.println("Initial: " + map);

        // compute(K key, BiFunction<K,V,V> remappingFunction)
        // Applies function to (key, oldValue)
        map.compute("Alice", (k, v) -> v == null ? 1 : v * 2);
        System.out.println("\ncompute('Alice', v * 2): " + map + " (10 * 2 = 20)");

        // Works with absent keys (oldValue is null)
        map.compute("Charlie", (k, v) -> v == null ? 1 : v * 2);
        System.out.println("compute('Charlie', ...): " + map + " (key absent, puts 1)");

        // If function returns null: removes key
        map.compute("Bob", (k, v) -> null);
        System.out.println("compute('Bob', returns null): " + map + " (Bob removed)");
    }

    // ===== COMPUTE IF ABSENT =====

    public static void demonstrateComputeIfAbsent() {
        System.out.println("\n=== COMPUTE IF ABSENT ===\n");

        Map<String, Integer> map = new HashMap<>();
        map.put("Alice", 10);

        System.out.println("Initial: " + map);

        // computeIfAbsent(K key, Function<K,V> mappingFunction)
        // If key absent: applies function to key, puts result
        Integer result1 = map.computeIfAbsent("Bob", k -> k.length());
        System.out.println("\ncomputeIfAbsent('Bob', k.length()): " + result1);
        System.out.println("Map: " + map + " (Bob added with length 3)");

        // If key present: returns existing value, doesn't call function
        Integer result2 = map.computeIfAbsent("Alice", k -> 999);
        System.out.println("\ncomputeIfAbsent('Alice', 999): " + result2);
        System.out.println("Map: " + map + " (Alice unchanged, returns 10)");

        // Common use case: initialize complex values
        Map<String, List<String>> multiMap = new HashMap<>();
        multiMap.computeIfAbsent("fruits", k -> new ArrayList<>()).add("apple");
        multiMap.computeIfAbsent("fruits", k -> new ArrayList<>()).add("banana");
        multiMap.computeIfAbsent("vegetables", k -> new ArrayList<>()).add("carrot");
        System.out.println("\nMulti-map using computeIfAbsent: " + multiMap);
    }

    // ===== COMPUTE IF PRESENT =====

    public static void demonstrateComputeIfPresent() {
        System.out.println("\n=== COMPUTE IF PRESENT ===\n");

        Map<String, Integer> map = new HashMap<>();
        map.put("Alice", 10);
        map.put("Bob", 20);

        System.out.println("Initial: " + map);

        // computeIfPresent(K key, BiFunction<K,V,V> remappingFunction)
        // If key present: applies function to (key, value)
        map.computeIfPresent("Alice", (k, v) -> v * 2);
        System.out.println("\ncomputeIfPresent('Alice', v * 2): " + map + " (10 * 2 = 20)");

        // If key absent: does nothing
        map.computeIfPresent("Charlie", (k, v) -> 999);
        System.out.println("computeIfPresent('Charlie', 999): " + map + " (no change)");

        // If function returns null: removes key
        map.computeIfPresent("Bob", (k, v) -> null);
        System.out.println("computeIfPresent('Bob', null): " + map + " (Bob removed)");
    }
}
