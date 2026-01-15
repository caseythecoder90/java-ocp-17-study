package ch09collectionsandgenerics;

import java.util.*;

/**
 * Set Interface - HashSet and TreeSet
 *
 * ===== SET IMPLEMENTATIONS =====
 *
 * HashSet:
 * - No duplicates
 * - No guaranteed order
 * - Uses hashCode() and equals() to check duplicates
 * - Fast operations (O(1) for add, remove, contains)
 * - Allows one null element
 *
 * TreeSet:
 * - No duplicates
 * - SORTED order (natural order or Comparator)
 * - Uses compareTo() (Comparable) or Comparator
 * - Slower operations (O(log n) for add, remove, contains)
 * - Does NOT allow null (throws NullPointerException)
 *
 * ===== FACTORY METHODS =====
 *
 * Set.of(varargs):
 * - Returns IMMUTABLE set
 * - CANNOT add elements
 * - CANNOT remove elements
 * - Does NOT allow null elements
 * - Does NOT allow duplicate elements (throws IllegalArgumentException)
 *
 * Set.copyOf(Collection):
 * - Returns IMMUTABLE copy
 * - CANNOT add elements
 * - CANNOT remove elements
 * - Does NOT allow null elements
 *
 * ===== KEY METHODS (MEMORIZE SIGNATURES) =====
 *
 * boolean add(E element)        - Adds element, returns false if duplicate
 * boolean contains(Object obj)  - Returns true if element exists
 * boolean remove(Object obj)    - Removes element, returns true if found
 * int size()                    - Returns number of elements
 * void clear()                  - Removes all elements
 * boolean isEmpty()             - Returns true if empty
 *
 * ===== SORTED VS UNSORTED =====
 * HashSet: NOT sorted, uses hashCode()
 * TreeSet: SORTED, uses compareTo() or Comparator
 */
public class SetMethods {

    public static void main(String[] args) {
        demonstrateHashSet();
        demonstrateTreeSet();
        demonstrateSetOf();
        demonstrateSetCopyOf();
        demonstrateHashCodeVsCompareTo();
    }

    // ===== HashSet - Uses hashCode() =====

    public static void demonstrateHashSet() {
        System.out.println("=== HashSet - Uses hashCode() ===\n");

        Set<String> set = new HashSet<>();

        // boolean add(E element) - returns false if duplicate
        System.out.println("add('Apple'): " + set.add("Apple"));
        System.out.println("add('Banana'): " + set.add("Banana"));
        System.out.println("add('Apple'): " + set.add("Apple") + " (duplicate)");
        System.out.println("Set: " + set);

        // No guaranteed order
        set.add("Cherry");
        set.add("Date");
        System.out.println("After more adds: " + set + " (no guaranteed order)");

        // boolean contains(Object obj)
        System.out.println("\ncontains('Banana'): " + set.contains("Banana"));
        System.out.println("contains('Grape'): " + set.contains("Grape"));

        // boolean remove(Object obj)
        System.out.println("\nremove('Banana'): " + set.remove("Banana"));
        System.out.println("After remove: " + set);

        // Allows null
        set.add(null);
        System.out.println("After add(null): " + set + " (allows null)");

        // size() and isEmpty()
        System.out.println("\nsize(): " + set.size());
        System.out.println("isEmpty(): " + set.isEmpty());

        // clear()
        set.clear();
        System.out.println("After clear(): " + set);
        System.out.println("isEmpty(): " + set.isEmpty());
    }

    // ===== TreeSet - Uses compareTo() or Comparator =====

    public static void demonstrateTreeSet() {
        System.out.println("\n=== TreeSet - Uses compareTo() (SORTED) ===\n");

        Set<String> set = new TreeSet<>();

        // Elements are sorted
        set.add("Banana");
        set.add("Apple");
        set.add("Date");
        set.add("Cherry");
        System.out.println("TreeSet (sorted): " + set);

        // No duplicates
        System.out.println("\nadd('Apple'): " + set.add("Apple") + " (duplicate)");
        System.out.println("Set unchanged: " + set);

        // boolean contains(Object obj)
        System.out.println("\ncontains('Cherry'): " + set.contains("Cherry"));

        // boolean remove(Object obj)
        set.remove("Date");
        System.out.println("After remove('Date'): " + set);

        // Does NOT allow null
        try {
            set.add(null);
        } catch (NullPointerException e) {
            System.out.println("\nadd(null): NullPointerException (TreeSet doesn't allow null)");
        }

        // TreeSet with numbers - natural order
        Set<Integer> numbers = new TreeSet<>();
        numbers.add(5);
        numbers.add(1);
        numbers.add(3);
        numbers.add(2);
        System.out.println("\nTreeSet<Integer>: " + numbers + " (sorted)");

        // TreeSet with Comparator - reverse order
        Set<String> reversed = new TreeSet<>(Comparator.reverseOrder());
        reversed.add("A");
        reversed.add("C");
        reversed.add("B");
        System.out.println("TreeSet with Comparator.reverseOrder(): " + reversed);
    }

    // ===== Set.of - IMMUTABLE =====

    public static void demonstrateSetOf() {
        System.out.println("\n=== Set.of - IMMUTABLE ===\n");

        Set<String> set = Set.of("A", "B", "C");
        System.out.println("Created: " + set);

        // CANNOT add
        try {
            set.add("D");
        } catch (UnsupportedOperationException e) {
            System.out.println("add('D'): UnsupportedOperationException ✗");
        }

        // CANNOT remove
        try {
            set.remove("A");
        } catch (UnsupportedOperationException e) {
            System.out.println("remove('A'): UnsupportedOperationException ✗");
        }

        // Does NOT allow null
        try {
            Set<String> nullSet = Set.of("A", null, "C");
        } catch (NullPointerException e) {
            System.out.println("Set.of with null: NullPointerException ✗");
        }

        // Does NOT allow duplicates at creation
        try {
            Set<String> dupSet = Set.of("A", "B", "A");
        } catch (IllegalArgumentException e) {
            System.out.println("Set.of with duplicates: IllegalArgumentException ✗");
        }
    }

    // ===== Set.copyOf - IMMUTABLE COPY =====

    public static void demonstrateSetCopyOf() {
        System.out.println("\n=== Set.copyOf - IMMUTABLE COPY ===\n");

        Set<String> original = new HashSet<>();
        original.add("A");
        original.add("B");
        original.add("C");

        Set<String> copy = Set.copyOf(original);
        System.out.println("Original: " + original);
        System.out.println("Copy: " + copy);

        // Modifying original doesn't affect copy
        original.add("D");
        System.out.println("\nAfter adding to original: " + original);
        System.out.println("Copy unchanged: " + copy);

        // CANNOT modify copy
        try {
            copy.add("E");
        } catch (UnsupportedOperationException e) {
            System.out.println("copy.add('E'): UnsupportedOperationException ✗");
        }

        try {
            copy.remove("A");
        } catch (UnsupportedOperationException e) {
            System.out.println("copy.remove('A'): UnsupportedOperationException ✗");
        }
    }

    // ===== hashCode() vs compareTo() =====

    static class Person {
        String name;
        int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Person)) return false;
            Person person = (Person) o;
            return Objects.equals(name, person.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return name + "(" + age + ")";
        }
    }

    static class Student implements Comparable<Student> {
        String name;
        int grade;

        Student(String name, int grade) {
            this.name = name;
            this.grade = grade;
        }

        @Override
        public int compareTo(Student other) {
            return this.name.compareTo(other.name);
        }

        @Override
        public String toString() {
            return name + "(" + grade + ")";
        }
    }

    public static void demonstrateHashCodeVsCompareTo() {
        System.out.println("\n=== hashCode() vs compareTo() ===\n");

        // HashSet uses hashCode() and equals()
        Set<Person> hashSet = new HashSet<>();
        hashSet.add(new Person("Alice", 25));
        hashSet.add(new Person("Bob", 30));
        hashSet.add(new Person("Alice", 35));  // Duplicate name, same hashCode
        System.out.println("HashSet (uses hashCode): " + hashSet);
        System.out.println("Size: " + hashSet.size() + " (Alice appears once - duplicates removed)");

        // TreeSet uses compareTo() or Comparator
        Set<Student> treeSet = new TreeSet<>();
        treeSet.add(new Student("Charlie", 90));
        treeSet.add(new Student("Alice", 85));
        treeSet.add(new Student("Bob", 95));
        System.out.println("\nTreeSet (uses compareTo): " + treeSet);
        System.out.println("Sorted by name (natural order)");

        System.out.println("\nKEY POINTS:");
        System.out.println("- HashSet: calls hashCode() and equals()");
        System.out.println("- TreeSet: calls compareTo() or uses Comparator");
        System.out.println("- HashSet: no order guarantee");
        System.out.println("- TreeSet: sorted order");
    }
}
