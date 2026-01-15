package ch09collectionsandgenerics;

import java.util.*;
import java.util.function.Predicate;

/**
 * Collections Framework Overview
 *
 * ===== FRAMEWORK HIERARCHY =====
 *
 * Collection (interface)
 *   ├── List (interface) - Ordered, allows duplicates, indexed access
 *   │     ├── ArrayList - Resizable array, fast random access
 *   │     └── LinkedList - Doubly-linked list, fast insertion/deletion
 *   │
 *   ├── Queue (interface) - Ordered for processing, FIFO (First In First Out)
 *   │     └── Deque (interface) - Double-ended queue, FIFO or LIFO
 *   │           └── LinkedList - Also implements Deque
 *   │
 *   └── Set (interface) - No duplicates, no guaranteed order
 *         ├── HashSet - Fast, uses hashCode(), no order
 *         └── TreeSet - Sorted, uses Comparable/Comparator
 *
 * Map (interface) - NOT part of Collection hierarchy!
 *   ├── HashMap - Key-value pairs, fast, no order
 *   └── TreeMap - Sorted by keys
 *
 * ===== KEY TRAITS =====
 *
 * ArrayList:
 * - Fast random access (get by index)
 * - Slow insertion/deletion in middle
 * - Good for frequent reads
 *
 * LinkedList:
 * - Implements both List and Deque
 * - Fast insertion/deletion at ends
 * - Slow random access
 * - Good for frequent adds/removes
 *
 * Queue/Deque:
 * - Queue: Process elements in order (FIFO)
 * - Deque: Add/remove from both ends (can be FIFO or LIFO)
 * - LinkedList is common implementation
 *
 * ===== COLLECTION INTERFACE METHODS (MEMORIZE) =====
 *
 * boolean add(E element)               - Adds element, returns true if collection changed
 * boolean remove(Object obj)           - Removes element, returns true if found and removed
 * boolean isEmpty()                    - Returns true if collection has no elements
 * int size()                           - Returns number of elements
 * void clear()                         - Removes all elements
 * boolean contains(Object obj)         - Returns true if element exists
 * boolean removeIf(Predicate<E> p)     - Removes elements matching predicate, returns true if any removed
 * void forEach(Consumer<E> consumer)   - Performs action for each element
 * boolean equals(Object obj)           - Compares collections for equality
 *
 * ===== DIAMOND OPERATOR RULES =====
 *
 * VALID:
 * - Right side can use diamond: List<String> list = new ArrayList<>();
 * - Type inference from left side
 *
 * INVALID (EXAM TRAPS):
 * - Diamond on left side: List<> list = new ArrayList<String>();  // ERROR
 * - Diamond without type on left: var list = new ArrayList<>();   // Compiles but type is ArrayList<Object>
 * - Diamond on interface: List<String> list = new List<>();       // ERROR - can't instantiate interface
 * - Diamond on both sides without any type: List<> list = new ArrayList<>();  // ERROR
 * - Missing diamond and type on right with var: var list = new ArrayList();  // Raw type (not recommended)
 */
public class CollectionsFramework {

    public static void main(String[] args) {
        demonstrateCollectionMethods();
        demonstrateDiamondOperator();
        demonstrateDiamondOperatorTraps();
    }

    // ===== COLLECTION INTERFACE METHODS =====

    public static void demonstrateCollectionMethods() {
        System.out.println("=== COLLECTION INTERFACE METHODS ===\n");

        List<String> list = new ArrayList<>();

        // boolean add(E element)
        boolean added = list.add("Apple");
        System.out.println("add('Apple'): " + added + " -> " + list);
        list.add("Banana");
        list.add("Cherry");
        System.out.println("After adding more: " + list);

        // int size()
        System.out.println("\nsize(): " + list.size());

        // boolean contains(Object obj)
        System.out.println("contains('Banana'): " + list.contains("Banana"));
        System.out.println("contains('Grape'): " + list.contains("Grape"));

        // boolean isEmpty()
        System.out.println("\nisEmpty(): " + list.isEmpty());

        // boolean remove(Object obj)
        boolean removed = list.remove("Banana");
        System.out.println("\nremove('Banana'): " + removed + " -> " + list);

        // void forEach(Consumer<E> consumer)
        System.out.print("\nforEach: ");
        list.forEach(item -> System.out.print(item + " "));
        System.out.println();

        // Add some more elements
        list.add("Date");
        list.add("Apricot");
        System.out.println("\nBefore removeIf: " + list);

        // boolean removeIf(Predicate<E> p)
        boolean anyRemoved = list.removeIf(s -> s.startsWith("A"));
        System.out.println("removeIf(starts with 'A'): " + anyRemoved + " -> " + list);

        // boolean equals(Object obj)
        List<String> list2 = new ArrayList<>();
        list2.add("Cherry");
        list2.add("Date");
        System.out.println("\nlist.equals(list2): " + list.equals(list2));

        // void clear()
        list.clear();
        System.out.println("\nAfter clear(): " + list);
        System.out.println("isEmpty(): " + list.isEmpty());
        System.out.println("size(): " + list.size());
    }

    // ===== DIAMOND OPERATOR - VALID USAGE =====

    public static void demonstrateDiamondOperator() {
        System.out.println("\n=== DIAMOND OPERATOR - VALID ===\n");

        // VALID: Type on left, diamond on right
        List<String> list1 = new ArrayList<>();
        System.out.println("List<String> list = new ArrayList<>(); - VALID");

        // VALID: Explicit type on both sides
        List<String> list2 = new ArrayList<String>();
        System.out.println("List<String> list = new ArrayList<String>(); - VALID");

        // VALID: var with diamond (infers ArrayList<String> from right side initializer)
        var list3 = new ArrayList<String>();
        System.out.println("var list = new ArrayList<String>(); - VALID (type: ArrayList<String>)");

        // VALID: Nested generics with diamond
        List<List<String>> nestedList = new ArrayList<>();
        System.out.println("List<List<String>> list = new ArrayList<>(); - VALID");

        // VALID: Map with diamond
        Map<String, Integer> map = new HashMap<>();
        System.out.println("Map<String, Integer> map = new HashMap<>(); - VALID");
    }

    // ===== DIAMOND OPERATOR - EXAM TRAPS =====

    public static void demonstrateDiamondOperatorTraps() {
        System.out.println("\n=== DIAMOND OPERATOR - EXAM TRAPS ===\n");

        // INVALID: Diamond on left side
        // List<> list1 = new ArrayList<String>();  // COMPILATION ERROR
        System.out.println("List<> list = new ArrayList<String>(); - ERROR");

        // INVALID: Can't instantiate interface
        // List<String> list2 = new List<>();  // COMPILATION ERROR
        System.out.println("List<String> list = new List<>(); - ERROR (can't instantiate interface)");

        // INVALID: Diamond on both sides without type
        // List<> list3 = new ArrayList<>();  // COMPILATION ERROR
        System.out.println("List<> list = new ArrayList<>(); - ERROR");

        // TRAP: var with diamond on right - infers Object
        var list4 = new ArrayList<>();  // Type is ArrayList<Object>
        list4.add("String");
        list4.add(123);  // Allows anything!
        System.out.println("var list = new ArrayList<>(); - COMPILES but type is ArrayList<Object>");

        // TRAP: Raw types (no generics at all)
        List list5 = new ArrayList();  // Raw type - compiles with warning
        list5.add("String");
        list5.add(123);  // Allows anything!
        System.out.println("List list = new ArrayList(); - COMPILES (raw type, not recommended)");

        // VALID: var with explicit type on right
        var list6 = new ArrayList<String>();
        System.out.println("var list = new ArrayList<String>(); - VALID (type: ArrayList<String>)");

        // INVALID: Type mismatch
        // List<String> list7 = new ArrayList<Integer>();  // COMPILATION ERROR
        System.out.println("List<String> list = new ArrayList<Integer>(); - ERROR (type mismatch)");

        // INVALID: Primitive types in generics
        // List<int> list8 = new ArrayList<>();  // COMPILATION ERROR
        System.out.println("List<int> list = new ArrayList<>(); - ERROR (must use Integer, not int)");
    }

    // ===== FRAMEWORK HIERARCHY DEMONSTRATION =====

    public static void demonstrateHierarchy() {
        System.out.println("\n=== FRAMEWORK HIERARCHY ===\n");

        // List implementations
        List<String> arrayList = new ArrayList<>();
        List<String> linkedList = new LinkedList<>();

        // Queue - LinkedList implements Queue
        Queue<String> queue = new LinkedList<>();

        // Deque - LinkedList implements Deque
        Deque<String> deque = new LinkedList<>();

        // LinkedList can be used as List, Queue, or Deque
        LinkedList<String> multi = new LinkedList<>();
        multi.add("As List");           // List method
        multi.offer("As Queue");        // Queue method
        multi.push("As Deque");         // Deque method

        System.out.println("LinkedList implements List, Queue, and Deque: " + multi);

        // Map does NOT extend Collection
        Map<String, Integer> map = new HashMap<>();
        // map.add("key");  // ERROR - Map has no add() method
        map.put("key", 1);  // Map uses put(), not add()
        System.out.println("\nMap does NOT extend Collection - uses put() instead of add()");
    }
}
