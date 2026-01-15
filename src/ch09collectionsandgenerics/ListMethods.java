package ch09collectionsandgenerics;

import java.util.*;

/**
 * List Interface - Factory Methods and Constructors
 *
 * ===== FACTORY METHODS - MUTABILITY RULES (CRITICAL FOR EXAM) =====
 *
 * Arrays.asList(varargs):
 * - Returns FIXED-SIZE list (backed by array)
 * - CAN replace elements: list.set(index, element) ✓
 * - CANNOT add elements: list.add(element) ✗ throws UnsupportedOperationException
 * - CANNOT remove elements: list.remove(index) ✗ throws UnsupportedOperationException
 *
 * List.of(varargs):
 * - Returns IMMUTABLE list
 * - CANNOT replace: list.set(index, element) ✗ throws UnsupportedOperationException
 * - CANNOT add: list.add(element) ✗ throws UnsupportedOperationException
 * - CANNOT remove: list.remove(index) ✗ throws UnsupportedOperationException
 * - Does NOT allow null elements
 *
 * List.copyOf(Collection):
 * - Returns IMMUTABLE copy
 * - CANNOT replace: list.set(index, element) ✗ throws UnsupportedOperationException
 * - CANNOT add: list.add(element) ✗ throws UnsupportedOperationException
 * - CANNOT remove: list.remove(index) ✗ throws UnsupportedOperationException
 * - Does NOT allow null elements
 *
 * ===== CONSTRUCTORS =====
 *
 * ArrayList:
 * - ArrayList() - Creates empty list with default capacity (10)
 * - ArrayList(int capacity) - Creates empty list with specified initial capacity
 * - ArrayList(Collection c) - Creates list from another collection
 *
 * LinkedList:
 * - LinkedList() - Creates empty list
 * - LinkedList(Collection c) - Creates list from another collection
 * - Note: LinkedList does NOT have capacity constructor
 *
 * ===== SUMMARY TABLE =====
 * Method           | Mutable? | Can Add? | Can Replace? | Can Remove? | Allows null?
 * -----------------|----------|----------|--------------|-------------|-------------
 * Arrays.asList    | Fixed    | NO       | YES          | NO          | YES
 * List.of          | No       | NO       | NO           | NO          | NO
 * List.copyOf      | No       | NO       | NO           | NO          | NO
 * new ArrayList    | Yes      | YES      | YES          | YES         | YES
 * new LinkedList   | Yes      | YES      | YES          | YES         | YES
 */
public class ListMethods {

    public static void main(String[] args) {
        demonstrateArraysAsList();
        demonstrateListOf();
        demonstrateListCopyOf();
        demonstrateConstructors();
        demonstrateComparison();
    }

    // ===== Arrays.asList - FIXED SIZE =====

    public static void demonstrateArraysAsList() {
        System.out.println("=== Arrays.asList - FIXED SIZE ===\n");

        List<String> list = Arrays.asList("A", "B", "C");
        System.out.println("Created: " + list);

        // CAN replace elements
        list.set(1, "Z");
        System.out.println("After set(1, 'Z'): " + list + " ✓");

        // CANNOT add elements
        try {
            list.add("D");
        } catch (UnsupportedOperationException e) {
            System.out.println("add('D'): UnsupportedOperationException ✗");
        }

        // CANNOT remove elements
        try {
            list.remove(0);
        } catch (UnsupportedOperationException e) {
            System.out.println("remove(0): UnsupportedOperationException ✗");
        }

        // ALLOWS null
        list.set(0, null);
        System.out.println("After set(0, null): " + list + " ✓");
    }

    // ===== List.of - IMMUTABLE =====

    public static void demonstrateListOf() {
        System.out.println("\n=== List.of - IMMUTABLE ===\n");

        List<String> list = List.of("A", "B", "C");
        System.out.println("Created: " + list);

        // CANNOT replace elements
        try {
            list.set(1, "Z");
        } catch (UnsupportedOperationException e) {
            System.out.println("set(1, 'Z'): UnsupportedOperationException ✗");
        }

        // CANNOT add elements
        try {
            list.add("D");
        } catch (UnsupportedOperationException e) {
            System.out.println("add('D'): UnsupportedOperationException ✗");
        }

        // CANNOT remove elements
        try {
            list.remove(0);
        } catch (UnsupportedOperationException e) {
            System.out.println("remove(0): UnsupportedOperationException ✗");
        }

        // Does NOT allow null
        try {
            List<String> nullList = List.of("A", null, "C");
        } catch (NullPointerException e) {
            System.out.println("List.of with null: NullPointerException ✗");
        }
    }

    // ===== List.copyOf - IMMUTABLE COPY =====

    public static void demonstrateListCopyOf() {
        System.out.println("\n=== List.copyOf - IMMUTABLE COPY ===\n");

        List<String> original = new ArrayList<>();
        original.add("A");
        original.add("B");
        original.add("C");

        List<String> copy = List.copyOf(original);
        System.out.println("Original: " + original);
        System.out.println("Copy: " + copy);

        // Modifying original doesn't affect copy
        original.add("D");
        System.out.println("After adding to original: " + original);
        System.out.println("Copy unchanged: " + copy);

        // CANNOT modify copy
        try {
            copy.set(0, "Z");
        } catch (UnsupportedOperationException e) {
            System.out.println("copy.set(0, 'Z'): UnsupportedOperationException ✗");
        }

        try {
            copy.add("E");
        } catch (UnsupportedOperationException e) {
            System.out.println("copy.add('E'): UnsupportedOperationException ✗");
        }

        try {
            copy.remove(0);
        } catch (UnsupportedOperationException e) {
            System.out.println("copy.remove(0): UnsupportedOperationException ✗");
        }
    }

    // ===== CONSTRUCTORS =====

    public static void demonstrateConstructors() {
        System.out.println("\n=== CONSTRUCTORS ===\n");

        // ArrayList() - empty with default capacity
        List<String> list1 = new ArrayList<>();
        System.out.println("ArrayList(): " + list1 + " (default capacity 10)");

        // ArrayList(int capacity) - empty with specified capacity
        List<String> list2 = new ArrayList<>(100);
        System.out.println("ArrayList(100): " + list2 + " (initial capacity 100)");

        // ArrayList(Collection) - from another collection
        List<String> source = Arrays.asList("A", "B", "C");
        List<String> list3 = new ArrayList<>(source);
        System.out.println("ArrayList(collection): " + list3);

        // LinkedList() - empty
        List<String> list4 = new LinkedList<>();
        System.out.println("\nLinkedList(): " + list4);

        // LinkedList(Collection) - from another collection
        List<String> list5 = new LinkedList<>(source);
        System.out.println("LinkedList(collection): " + list5);

        // Note: LinkedList does NOT have capacity constructor
        // List<String> invalid = new LinkedList<>(100);  // COMPILATION ERROR

        // All constructors create MUTABLE lists
        list3.add("D");
        list3.set(0, "Z");
        list3.remove(1);
        System.out.println("\nArrayList after modifications: " + list3 + " ✓");

        list5.add("D");
        list5.set(0, "Z");
        list5.remove(1);
        System.out.println("LinkedList after modifications: " + list5 + " ✓");
    }

    // ===== COMPARISON OF ALL METHODS =====

    public static void demonstrateComparison() {
        System.out.println("\n=== COMPARISON TABLE ===\n");

        System.out.println("┌─────────────────┬──────────┬──────────┬──────────────┬────────────┬─────────────┐");
        System.out.println("│ Method          │ Mutable? │ Can Add? │ Can Replace? │ Can Remove?│ Allows null?│");
        System.out.println("├─────────────────┼──────────┼──────────┼──────────────┼────────────┼─────────────┤");
        System.out.println("│ Arrays.asList   │ Fixed    │ NO       │ YES          │ NO         │ YES         │");
        System.out.println("│ List.of         │ No       │ NO       │ NO           │ NO         │ NO          │");
        System.out.println("│ List.copyOf     │ No       │ NO       │ NO           │ NO         │ NO          │");
        System.out.println("│ new ArrayList   │ Yes      │ YES      │ YES          │ YES        │ YES         │");
        System.out.println("│ new LinkedList  │ Yes      │ YES      │ YES          │ YES        │ YES         │");
        System.out.println("└─────────────────┴──────────┴──────────┴──────────────┴────────────┴─────────────┘");

        System.out.println("\nKEY EXAM TRAPS:");
        System.out.println("- Arrays.asList: FIXED size (can replace, can't add/remove)");
        System.out.println("- List.of/copyOf: IMMUTABLE (can't do anything)");
        System.out.println("- List.of/copyOf: NO null elements");
        System.out.println("- ArrayList: HAS capacity constructor");
        System.out.println("- LinkedList: NO capacity constructor");

        List<String> arr = new ArrayList<>();
        arr.add("1");
        arr.add("2");
        arr.replaceAll((str) -> str + "aaa");
        System.out.println(arr);
    }
}
