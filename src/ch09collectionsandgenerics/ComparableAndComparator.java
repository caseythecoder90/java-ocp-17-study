package ch09collectionsandgenerics;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

/**
 * Comparable vs Comparator
 *
 * ===== COMPARABLE =====
 * Package: java.lang (automatically imported)
 * Interface: Comparable<T>
 * Method: int compareTo(T o)
 * Parameters: 1 (the object to compare to)
 * Implementation: Class itself implements the interface
 * Lambda: NO - must implement at class level
 * Purpose: Defines natural ordering for a class
 *
 * compareTo() RETURN VALUE RULES:
 * - ZERO (0): this object equals the parameter object
 * - NEGATIVE (<0): this object is less than the parameter (comes before)
 * - POSITIVE (>0): this object is greater than the parameter (comes after)
 *
 * Common pattern: return this.field - other.field (for numbers)
 * String example: return this.name.compareTo(other.name)
 *
 * ===== COMPARATOR =====
 * Package: java.util (must import)
 * Interface: Comparator<T>
 * Method: int compare(T o1, T o2)
 * Parameters: 2 (both objects to compare)
 * Implementation: Separate class or lambda
 * Lambda: YES - commonly used with lambda
 * Purpose: Defines custom ordering separate from class
 *
 * compare() RETURN VALUE RULES:
 * - ZERO (0): o1 equals o2
 * - NEGATIVE (<0): o1 is less than o2 (o1 comes before o2)
 * - POSITIVE (>0): o1 is greater than o2 (o1 comes after o2)
 *
 * ===== KEY DIFFERENCES =====
 * Comparable: ONE way to compare (natural order), IN the class
 * Comparator: MANY ways to compare, OUTSIDE the class
 *
 * ===== COMPARATOR STATIC METHODS (for building comparators) =====
 * Comparator.comparing(Function)              - Extract Comparable field
 * Comparator.comparingDouble(ToDoubleFunction) - Extract double field
 * Comparator.comparingInt(ToIntFunction)      - Extract int field
 * Comparator.comparingLong(ToLongFunction)    - Extract long field
 * Comparator.naturalOrder()                   - Natural ordering (requires Comparable)
 * Comparator.reverseOrder()                   - Reverse natural ordering
 *
 * ===== COMPARATOR INSTANCE METHODS (for chaining) =====
 * reversed()                  - Reverses the comparator
 * thenComparing(Function)     - Secondary sort by Comparable field
 * thenComparingDouble(ToDoubleFunction) - Secondary sort by double field
 * thenComparingInt(ToIntFunction)       - Secondary sort by int field
 * thenComparingLong(ToLongFunction)     - Secondary sort by long field
 */
public class ComparableAndComparator {

    // ===== COMPARABLE EXAMPLE =====

    static class Person implements Comparable<Person> {
        private final String name;
        private final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        // MUST implement compareTo when implementing Comparable
        // Signature: int compareTo(T o)
        @Override
        public int compareTo(Person other) {
            // Natural ordering: by name
            // Returns: negative if this < other, 0 if equal, positive if this > other
            return this.name.compareTo(other.name);
        }

        public String getName() { return name; }
        public int getAge() { return age; }

        @Override
        public String toString() {
            return name + " (age " + age + ")";
        }
    }

    public static void demonstrateComparable() {
        System.out.println("=== COMPARABLE ===");

        List<Person> people = new ArrayList<>();
        people.add(new Person("Charlie", 30));
        people.add(new Person("Alice", 25));
        people.add(new Person("Bob", 35));

        // Uses natural ordering (compareTo method)
        Collections.sort(people);

        System.out.println("Sorted by natural order (name): " + people);
    }

    // ===== COMPARATOR EXAMPLES =====

    static class Animal {
        private String name;
        private int age;
        private double weight;

        public Animal(String name, int age, double weight) {
            this.name = name;
            this.age = age;
            this.weight = weight;
        }

        public String getName() { return name; }
        public int getAge() { return age; }
        public double getWeight() { return weight; }

        @Override
        public String toString() {
            return name + " (age " + age + ", weight " + weight + ")";
        }
    }

    public static void demonstrateComparator() {
        System.out.println("\n=== COMPARATOR ===");

        List<Animal> animals = new ArrayList<>();
        animals.add(new Animal("Dog", 5, 25.5));
        animals.add(new Animal("Cat", 3, 8.2));
        animals.add(new Animal("Horse", 10, 450.0));

        // Method 1: Lambda (most common)
        // Signature: int compare(T o1, T o2)
        Comparator<Animal> byName = (a1, a2) -> a1.getName().compareTo(a2.getName());
        Collections.sort(animals, byName);
        System.out.println("Sorted by name (lambda): " + animals);

        // Method 2: Using Comparator.comparing() - cleaner
        Comparator<Animal> byAge = Comparator.comparing(Animal::getAge);
        Collections.sort(animals, byAge);
        System.out.println("Sorted by age (comparing): " + animals);
    }

    // ===== COMPARATOR STATIC METHODS =====

    public static void demonstrateComparatorStaticMethods() {
        System.out.println("\n=== COMPARATOR STATIC METHODS ===");

        List<Animal> animals = new ArrayList<>();
        animals.add(new Animal("Dog", 5, 25.5));
        animals.add(new Animal("Cat", 3, 8.2));
        animals.add(new Animal("Horse", 10, 450.0));

        // comparing() - extracts Comparable field (String, Integer, etc.)
        Function<Animal, String> function = Animal::getName;
        Comparator<Animal> byName = Comparator.comparing(function);
        animals.sort(byName);
        System.out.println("comparing(getName): " + animals);

        // comparingInt() - extracts int primitive
        Comparator<Animal> byAge = Comparator.comparingInt(Animal::getAge);
        Collections.sort(animals, byAge);
        System.out.println("comparingInt(getAge): " + animals);

        // comparingDouble() - extracts double primitive
        ToDoubleFunction<Animal> toDoubleFunction = Animal::getWeight;
        Comparator<Animal> byWeight = Comparator.comparingDouble(toDoubleFunction);
        Collections.sort(animals, byWeight);
        System.out.println("comparingDouble(getWeight): " + animals);

        // comparingLong() - extracts long primitive (if you had a long field)
        // Comparator<Animal> byId = Comparator.comparingLong(Animal::getId);

        // naturalOrder() - uses Comparable (requires class implements Comparable)
        List<String> strings = Arrays.asList("Charlie", "Alice", "Bob");
        Collections.sort(strings, Comparator.naturalOrder());
        System.out.println("naturalOrder() on strings: " + strings);

        // reverseOrder() - reverse of natural order
        Collections.sort(strings, Comparator.reverseOrder());
        System.out.println("reverseOrder() on strings: " + strings);
    }

    // ===== COMPARATOR INSTANCE METHODS (CHAINING) =====

    public static void demonstrateComparatorInstanceMethods() {
        System.out.println("\n=== COMPARATOR INSTANCE METHODS ===");

        List<Animal> animals = new ArrayList<>();
        animals.add(new Animal("Dog", 5, 25.5));
        animals.add(new Animal("Cat", 5, 8.2));    // Same age as Dog
        animals.add(new Animal("Horse", 10, 450.0));
        animals.add(new Animal("Bird", 3, 0.5));

        // reversed() - reverses the comparator
        Comparator<Animal> byAgeReversed = Comparator.comparingInt(Animal::getAge).reversed();
        Collections.sort(animals, byAgeReversed);
        System.out.println("reversed() - age descending: " + animals);

        // thenComparing() - secondary sort by Comparable field
        // Primary: age, Secondary: name
        Comparator<Animal> byAgeThenName = Comparator.comparingInt(Animal::getAge)
                                                      .thenComparing(Animal::getName);
        Collections.sort(animals, byAgeThenName);
        System.out.println("thenComparing() - age then name: " + animals);

        // thenComparingInt() - secondary sort by int field
        // Primary: name, Secondary: age
        Comparator<Animal> byNameThenAge = Comparator.comparing(Animal::getName)
                                                      .thenComparingInt(Animal::getAge);
        Collections.sort(animals, byNameThenAge);
        System.out.println("thenComparingInt() - name then age: " + animals);

        // thenComparingDouble() - secondary sort by double field
        // Primary: age, Secondary: weight
        Comparator<Animal> byAgeThenWeight = Comparator.comparingInt(Animal::getAge)
                                                        .thenComparingDouble(Animal::getWeight);
        Collections.sort(animals, byAgeThenWeight);
        System.out.println("thenComparingDouble() - age then weight: " + animals);

        // thenComparingLong() - secondary sort by long field (if you had one)
        // Comparator<Animal> example = Comparator.comparingInt(Animal::getAge)
        //                                        .thenComparingLong(Animal::getId);

        // Multiple chaining - age DESC, then name ASC, then weight ASC
        Comparator<Animal> complex = Comparator.comparingInt(Animal::getAge).reversed()
                                               .thenComparing(Animal::getName)
                                               .thenComparingDouble(Animal::getWeight);
        Collections.sort(animals, complex);
        System.out.println("Complex: age DESC, name ASC, weight ASC: " + animals);
    }

    // ===== COMPARISON TABLE =====

    public static void printComparisonTable() {
        System.out.println("\n=== COMPARABLE vs COMPARATOR ===");
        System.out.println("┌─────────────────────────┬──────────────────┬──────────────────┐");
        System.out.println("│ Feature                 │ Comparable       │ Comparator       │");
        System.out.println("├─────────────────────────┼──────────────────┼──────────────────┤");
        System.out.println("│ Package                 │ java.lang        │ java.util        │");
        System.out.println("│ Must implement in class │ YES              │ NO               │");
        System.out.println("│ Method name             │ compareTo        │ compare          │");
        System.out.println("│ Number of parameters    │ 1                │ 2                │");
        System.out.println("│ Common to use lambda    │ NO               │ YES              │");
        System.out.println("│ Purpose                 │ Natural order    │ Custom order     │");
        System.out.println("└─────────────────────────┴──────────────────┴──────────────────┘");
    }

    // ===== EXAM TIPS =====

    public static void demonstrateExamTips() {
        System.out.println("\n=== EXAM TIPS ===");

        // Tip 1: Return values - remember the sign
        System.out.println("Return value rules:");
        System.out.println("  0 = equal");
        System.out.println("  <0 = first is less (comes before)");
        System.out.println("  >0 = first is greater (comes after)");

        // Tip 2: Comparable is in java.lang (no import needed)
        // Comparator is in java.util (must import)

        // Tip 3: compareTo takes 1 param, compare takes 2 params

        // Tip 4: Common mistake - reversing the logic
        List<Integer> nums = Arrays.asList(3, 1, 2);
        Collections.sort(nums, (a, b) -> a - b);  // Ascending
        System.out.println("Ascending: " + nums);

        Collections.sort(nums, (a, b) -> b - a);  // Descending
        System.out.println("Descending: " + nums);

        // Tip 5: Chaining comparators
        // thenComparing only applies when first comparison is equal
    }

    // ===== MAIN DEMONSTRATION =====

    public static void main(String[] args) {
        demonstrateComparable();
        demonstrateComparator();
        demonstrateComparatorStaticMethods();
        demonstrateComparatorInstanceMethods();
        printComparisonTable();
        demonstrateExamTips();
    }
}
