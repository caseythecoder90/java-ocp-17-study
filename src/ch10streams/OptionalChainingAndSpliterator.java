package ch10streams;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Optional Chaining and Spliterator
 *
 * This file covers two important but sometimes overlooked topics:
 * 1. Chaining Optional operations (map, flatMap, filter, or, stream)
 * 2. Spliterator for traversing and partitioning elements
 *
 * ===== OPTIONAL CHAINING METHODS =====
 *
 * | Method      | Signature                                      | Returns           |
 * |-------------|------------------------------------------------|-------------------|
 * | map         | map(Function<T,U>)                             | Optional<U>       |
 * | flatMap     | flatMap(Function<T,Optional<U>>)               | Optional<U>       |
 * | filter      | filter(Predicate<T>)                           | Optional<T>       |
 * | or          | or(Supplier<Optional<T>>)                      | Optional<T>       |
 * | stream      | stream()                                       | Stream<T> (0 or 1)|
 * | ifPresent   | ifPresent(Consumer<T>)                         | void              |
 * | ifPresentOrElse | ifPresentOrElse(Consumer<T>, Runnable)     | void              |
 *
 * ===== SPLITERATOR METHODS =====
 *
 * | Method            | Signature                        | Returns    | Description              |
 * |-------------------|----------------------------------|------------|--------------------------|
 * | tryAdvance        | tryAdvance(Consumer<T>)          | boolean    | Process ONE element      |
 * | forEachRemaining  | forEachRemaining(Consumer<T>)    | void       | Process ALL remaining    |
 * | trySplit          | trySplit()                       | Spliterator| Split for parallel       |
 * | estimateSize      | estimateSize()                   | long       | Estimated elements left  |
 * | characteristics   | characteristics()                | int        | Bitwise characteristics  |
 */
public class OptionalChainingAndSpliterator {

    public static void main(String[] args) {
        demonstrateOptionalMap();
        demonstrateOptionalFlatMap();
        demonstrateOptionalFilter();
        demonstrateOptionalOr();
        demonstrateOptionalStream();
        demonstrateChainingOptionals();
        demonstrateSpliteratorBasics();
        demonstrateTryAdvance();
        demonstrateForEachRemaining();
        demonstrateTrySplit();
        demonstrateSpliteratorCharacteristics();
    }

    // =====================================================================
    // OPTIONAL.MAP - Transform the value if present
    // =====================================================================

    /**
     * OPTIONAL MAP SIGNATURE:
     *
     * <U> Optional<U> map(Function<? super T, ? extends U> mapper)
     *
     * TYPE PARAMETERS:
     *   - T = current Optional's value type
     *   - U = result type after mapping
     *
     * BEHAVIOR:
     *   - If Optional is EMPTY: returns Optional.empty()
     *   - If Optional has VALUE: applies mapper, wraps result in Optional
     *   - If mapper returns null: returns Optional.empty()
     *
     * ===== KEY INSIGHT =====
     *
     * map() automatically WRAPS the result in Optional!
     *
     * Optional<T>.map(Function<T,U>) -> Optional<U>
     *                 |
     *                 v
     *            returns U, automatically wrapped
     */
    public static void demonstrateOptionalMap() {
        System.out.println("=== OPTIONAL.MAP ===\n");

        Optional<String> name = Optional.of("Alice");
        Optional<String> empty = Optional.empty();

        // EXAMPLE 1: Basic map - String -> Integer
        // TYPE ANALYSIS:
        //   T = String
        //   mapper: String::length is (String) -> Integer
        //   U = Integer
        //   Result: Optional<Integer>
        Optional<Integer> nameLength = name.map(String::length);
        System.out.println("Optional.of(\"Alice\").map(String::length): " + nameLength);
        // Optional[5]

        // EXAMPLE 2: Map on empty Optional
        Optional<Integer> emptyLength = empty.map(String::length);
        System.out.println("Optional.empty().map(String::length): " + emptyLength);
        // Optional.empty - mapper never called!

        // EXAMPLE 3: Chained maps
        // Optional<String> -> Optional<Integer> -> Optional<String>
        Optional<String> lengthDescription = name
            .map(String::length)           // Optional<String> -> Optional<Integer>
            .map(len -> "Length: " + len); // Optional<Integer> -> Optional<String>
        System.out.println("Chained maps: " + lengthDescription);

        // EXAMPLE 4: Map returning null -> empty Optional
        Optional<String> nullResult = name.map(s -> null);
        System.out.println("map returning null: " + nullResult);
        // Optional.empty

        // EXAMPLE 5: Transform to different type
        record Person(String name, int age) {}
        Optional<String> personName = Optional.of("Bob");
        Optional<Person> person = personName.map(n -> new Person(n, 30));
        System.out.println("Map String to Person: " + person);

        System.out.println();
    }

    // =====================================================================
    // OPTIONAL.FLATMAP - Transform when mapper returns Optional
    // =====================================================================

    /**
     * OPTIONAL FLATMAP SIGNATURE:
     *
     * <U> Optional<U> flatMap(Function<? super T, ? extends Optional<? extends U>> mapper)
     *
     * TYPE PARAMETERS:
     *   - T = current Optional's value type
     *   - U = value type inside the Optional returned by mapper
     *
     * BEHAVIOR:
     *   - If Optional is EMPTY: returns Optional.empty()
     *   - If Optional has VALUE: applies mapper, returns the Optional directly
     *   - mapper MUST return Optional (not null!)
     *
     * ===== KEY DIFFERENCE FROM MAP =====
     *
     * map():     mapper returns U      -> wrapped in Optional<U>
     * flatMap(): mapper returns Optional<U> -> returned directly (not double-wrapped!)
     *
     * Use flatMap when your transformation function already returns Optional!
     */
    public static void demonstrateOptionalFlatMap() {
        System.out.println("=== OPTIONAL.FLATMAP ===\n");

        // Helper method that returns Optional
        Function<String, Optional<Integer>> parseIntSafe = s -> {
            try {
                return Optional.of(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        };

        Optional<String> validNumber = Optional.of("42");
        Optional<String> invalidNumber = Optional.of("abc");
        Optional<String> empty = Optional.empty();

        // EXAMPLE 1: flatMap with function returning Optional
        // TYPE ANALYSIS:
        //   T = String
        //   mapper: parseIntSafe returns Optional<Integer>
        //   U = Integer
        //   Result: Optional<Integer> (NOT Optional<Optional<Integer>>!)
        Optional<Integer> parsed1 = validNumber.flatMap(parseIntSafe);
        System.out.println("flatMap \"42\" to Integer: " + parsed1);
        // Optional[42]

        Optional<Integer> parsed2 = invalidNumber.flatMap(parseIntSafe);
        System.out.println("flatMap \"abc\" to Integer: " + parsed2);
        // Optional.empty (parsing failed)

        Optional<Integer> parsed3 = empty.flatMap(parseIntSafe);
        System.out.println("flatMap empty: " + parsed3);
        // Optional.empty

        // EXAMPLE 2: Why flatMap instead of map?
        System.out.println("\n--- map vs flatMap ---");

        // Using map with function returning Optional -> double-wrapped!
        Optional<Optional<Integer>> doubleWrapped = validNumber.map(parseIntSafe);
        System.out.println("map (double-wrapped): " + doubleWrapped);
        // Optional[Optional[42]] - BAD!

        // Using flatMap -> single Optional
        Optional<Integer> singleWrapped = validNumber.flatMap(parseIntSafe);
        System.out.println("flatMap (single): " + singleWrapped);
        // Optional[42] - GOOD!

        // EXAMPLE 3: Chaining flatMaps for nested optionals
        System.out.println("\n--- Chaining flatMaps ---");

        record Address(String city) {}
        record Company(Optional<Address> address) {}
        record Employee(Optional<Company> company) {}

        Employee employee = new Employee(
            Optional.of(new Company(
                Optional.of(new Address("New York"))
            ))
        );

        // Get city from deeply nested structure
        // Each level returns Optional, so use flatMap
        Optional<String> city = Optional.of(employee)
            .flatMap(Employee::company)    // Optional<Employee> -> Optional<Company>
            .flatMap(Company::address)     // Optional<Company> -> Optional<Address>
            .map(Address::city);           // Optional<Address> -> Optional<String>
        System.out.println("Nested flatMap to get city: " + city);

        // With missing intermediate
        Employee noCompany = new Employee(Optional.empty());
        Optional<String> noCity = Optional.of(noCompany)
            .flatMap(Employee::company)
            .flatMap(Company::address)
            .map(Address::city);
        System.out.println("Nested flatMap with missing: " + noCity);
        // Optional.empty - chain short-circuits

        System.out.println();
    }

    // =====================================================================
    // OPTIONAL.FILTER - Keep value only if predicate matches
    // =====================================================================

    /**
     * OPTIONAL FILTER SIGNATURE:
     *
     * Optional<T> filter(Predicate<? super T> predicate)
     *
     * BEHAVIOR:
     *   - If Optional is EMPTY: returns Optional.empty()
     *   - If Optional has VALUE and predicate returns TRUE: returns same Optional
     *   - If Optional has VALUE and predicate returns FALSE: returns Optional.empty()
     *
     * TYPE: Optional<T> -> Optional<T> (same type, possibly empty)
     */
    public static void demonstrateOptionalFilter() {
        System.out.println("=== OPTIONAL.FILTER ===\n");

        Optional<Integer> number = Optional.of(42);
        Optional<Integer> empty = Optional.empty();

        // EXAMPLE 1: Filter passes
        Optional<Integer> evenResult = number.filter(n -> n % 2 == 0);
        System.out.println("filter 42 is even: " + evenResult);
        // Optional[42] - predicate passed

        // EXAMPLE 2: Filter fails
        Optional<Integer> oddResult = number.filter(n -> n % 2 != 0);
        System.out.println("filter 42 is odd: " + oddResult);
        // Optional.empty - predicate failed

        // EXAMPLE 3: Filter on empty
        Optional<Integer> emptyFiltered = empty.filter(n -> n % 2 == 0);
        System.out.println("filter empty: " + emptyFiltered);
        // Optional.empty - was already empty

        // EXAMPLE 4: Chaining filter with map
        Optional<String> name = Optional.of("Alice");

        Optional<String> longName = name
            .filter(s -> s.length() > 3)
            .map(String::toUpperCase);
        System.out.println("filter(length>3).map(toUpperCase): " + longName);

        Optional<String> shortName = Optional.of("Al")
            .filter(s -> s.length() > 3)
            .map(String::toUpperCase);
        System.out.println("\"Al\" filter(length>3).map: " + shortName);
        // Optional.empty - filter stopped the chain

        // EXAMPLE 5: Multiple filters
        Optional<Integer> validAge = Optional.of(25)
            .filter(age -> age >= 0)
            .filter(age -> age <= 120);
        System.out.println("Multiple filters (valid age): " + validAge);

        System.out.println();
    }

    // =====================================================================
    // OPTIONAL.OR - Provide alternative Optional (Java 9+)
    // =====================================================================

    /**
     * OPTIONAL OR SIGNATURE:
     *
     * Optional<T> or(Supplier<? extends Optional<? extends T>> supplier)
     *
     * BEHAVIOR:
     *   - If Optional has VALUE: returns this Optional (supplier not called)
     *   - If Optional is EMPTY: returns Optional from supplier
     *
     * ===== DIFFERENCE FROM orElse/orElseGet =====
     *
     * orElse(T value)           -> returns T (unwrapped value)
     * orElseGet(Supplier<T>)    -> returns T (unwrapped value)
     * or(Supplier<Optional<T>>) -> returns Optional<T> (still wrapped!)
     *
     * Use or() to provide a fallback Optional while staying in Optional context.
     */
    public static void demonstrateOptionalOr() {
        System.out.println("=== OPTIONAL.OR ===\n");

        Optional<String> primary = Optional.of("Primary");
        Optional<String> empty = Optional.empty();
        Supplier<Optional<String>> fallback = () -> Optional.of("Fallback");

        // EXAMPLE 1: or() when value present
        Optional<String> result1 = primary.or(fallback);
        System.out.println("present.or(fallback): " + result1);
        // Optional[Primary] - fallback not used

        // EXAMPLE 2: or() when empty
        Optional<String> result2 = empty.or(fallback);
        System.out.println("empty.or(fallback): " + result2);
        // Optional[Fallback]

        // EXAMPLE 3: Chaining or() calls
        Optional<String> result3 = Optional.<String>empty()
            .or(() -> Optional.empty())
            .or(() -> Optional.of("Third choice"));
        System.out.println("empty.or(empty).or(value): " + result3);
        // Optional[Third choice]

        // EXAMPLE 4: or() vs orElse comparison
        System.out.println("\n--- or() vs orElse/orElseGet ---");

        // orElse returns T (the value)
        String value1 = empty.orElse("default");
        System.out.println("orElse returns T: " + value1);

        // or() returns Optional<T> (still wrapped)
        Optional<String> value2 = empty.or(() -> Optional.of("default"));
        System.out.println("or() returns Optional<T>: " + value2);

        // EXAMPLE 5: Use case - try multiple sources
        System.out.println("\n--- Use case: Try multiple sources ---");

        // Simulate looking up value from multiple sources
        Optional<String> fromCache = Optional.empty();  // cache miss
        Optional<String> fromDb = Optional.empty();     // db miss
        Optional<String> defaultValue = Optional.of("Default Config");

        String config = fromCache
            .or(() -> fromDb)
            .or(() -> defaultValue)
            .orElse("Hardcoded Fallback");
        System.out.println("Config from multiple sources: " + config);

        System.out.println();
    }

    // =====================================================================
    // OPTIONAL.STREAM - Convert to Stream (Java 9+)
    // =====================================================================

    /**
     * OPTIONAL STREAM SIGNATURE:
     *
     * Stream<T> stream()
     *
     * BEHAVIOR:
     *   - If Optional has VALUE: returns Stream with ONE element
     *   - If Optional is EMPTY: returns empty Stream
     *
     * ===== USE CASES =====
     *
     * 1. Integrate Optional into Stream pipelines
     * 2. flatMap a Stream of Optionals to get values
     * 3. Collect present values from List of Optionals
     */
    public static void demonstrateOptionalStream() {
        System.out.println("=== OPTIONAL.STREAM ===\n");

        Optional<String> present = Optional.of("Hello");
        Optional<String> empty = Optional.empty();

        // EXAMPLE 1: Basic stream()
        List<String> presentList = present.stream().toList();
        System.out.println("present.stream().toList(): " + presentList);
        // [Hello]

        List<String> emptyList = empty.stream().toList();
        System.out.println("empty.stream().toList(): " + emptyList);
        // []

        // EXAMPLE 2: Flatten Stream of Optionals
        System.out.println("\n--- Flatten Stream of Optionals ---");

        List<Optional<String>> optionals = List.of(
            Optional.of("Apple"),
            Optional.empty(),
            Optional.of("Banana"),
            Optional.empty(),
            Optional.of("Cherry")
        );

        // Get all present values using flatMap + stream()
        List<String> presentValues = optionals.stream()
            .flatMap(Optional::stream)  // Optional<String> -> Stream<String> (0 or 1)
            .toList();
        System.out.println("flatMap(Optional::stream): " + presentValues);
        // [Apple, Banana, Cherry]

        // EXAMPLE 3: Filter and collect present values
        System.out.println("\n--- Common pattern with Optional.stream() ---");

        record User(String name, Optional<String> email) {}

        List<User> users = List.of(
            new User("Alice", Optional.of("alice@email.com")),
            new User("Bob", Optional.empty()),
            new User("Carol", Optional.of("carol@email.com"))
        );

        // Get all emails (skip users without email)
        List<String> emails = users.stream()
            .map(User::email)               // Stream<Optional<String>>
            .flatMap(Optional::stream)      // Stream<String>
            .toList();
        System.out.println("All emails: " + emails);

        // Alternative without stream(): filter + map
        List<String> emails2 = users.stream()
            .filter(u -> u.email().isPresent())
            .map(u -> u.email().get())
            .toList();
        System.out.println("Alternative approach: " + emails2);

        System.out.println();
    }

    // =====================================================================
    // CHAINING OPTIONALS - Putting it all together
    // =====================================================================

    public static void demonstrateChainingOptionals() {
        System.out.println("=== CHAINING OPTIONALS ===\n");

        record Address(String street, String city, Optional<String> zipCode) {}
        record Person(String name, Optional<Address> address) {}

        Person personWithAddress = new Person("Alice",
            Optional.of(new Address("123 Main St", "Boston", Optional.of("02101"))));

        Person personNoZip = new Person("Bob",
            Optional.of(new Address("456 Oak Ave", "Chicago", Optional.empty())));

        Person personNoAddress = new Person("Carol", Optional.empty());

        // EXAMPLE 1: Complex chain
        System.out.println("--- Complex Optional Chains ---");

        // Get zip code with default
        String zip1 = Optional.of(personWithAddress)
            .flatMap(Person::address)       // Optional<Person> -> Optional<Address>
            .flatMap(Address::zipCode)      // Optional<Address> -> Optional<String>
            .orElse("No Zip");
        System.out.println("Alice's zip: " + zip1);

        String zip2 = Optional.of(personNoZip)
            .flatMap(Person::address)
            .flatMap(Address::zipCode)
            .orElse("No Zip");
        System.out.println("Bob's zip: " + zip2);

        String zip3 = Optional.of(personNoAddress)
            .flatMap(Person::address)
            .flatMap(Address::zipCode)
            .orElse("No Zip");
        System.out.println("Carol's zip: " + zip3);

        // EXAMPLE 2: Chain with filter and map
        System.out.println("\n--- Filter in chain ---");

        // Get city only if it's Boston
        Optional<String> bostonCity = Optional.of(personWithAddress)
            .flatMap(Person::address)
            .map(Address::city)
            .filter(city -> city.equals("Boston"));
        System.out.println("City if Boston: " + bostonCity);

        Optional<String> chicagoCheck = Optional.of(personNoZip)
            .flatMap(Person::address)
            .map(Address::city)
            .filter(city -> city.equals("Boston"));
        System.out.println("Chicago is Boston?: " + chicagoCheck);

        // EXAMPLE 3: Combining or() with chain
        System.out.println("\n--- or() in chain ---");

        Optional<String> cityWithFallback = Optional.of(personNoAddress)
            .flatMap(Person::address)
            .map(Address::city)
            .or(() -> Optional.of("Unknown City"));
        System.out.println("City with fallback: " + cityWithFallback);

        // EXAMPLE 4: ifPresentOrElse for terminal action
        System.out.println("\n--- ifPresentOrElse ---");

        Optional.of(personWithAddress)
            .flatMap(Person::address)
            .map(Address::city)
            .ifPresentOrElse(
                city -> System.out.println("Found city: " + city),
                () -> System.out.println("No city found")
            );

        Optional.of(personNoAddress)
            .flatMap(Person::address)
            .map(Address::city)
            .ifPresentOrElse(
                city -> System.out.println("Found city: " + city),
                () -> System.out.println("No city found")
            );

        System.out.println();
    }

    // =====================================================================
    // SPLITERATOR BASICS
    // =====================================================================

    /**
     * SPLITERATOR OVERVIEW:
     *
     * Spliterator = "Splittable Iterator"
     *
     * Interface for traversing and partitioning elements of a source.
     * Used internally by Streams for parallel processing.
     *
     * ===== KEY METHODS =====
     *
     * boolean tryAdvance(Consumer<? super T> action)
     *   - Process ONE element and advance
     *   - Returns true if element was processed, false if no more elements
     *
     * void forEachRemaining(Consumer<? super T> action)
     *   - Process ALL remaining elements
     *   - Default implementation calls tryAdvance repeatedly
     *
     * Spliterator<T> trySplit()
     *   - Split into two Spliterators for parallel processing
     *   - Returns new Spliterator for portion of elements, or null if can't split
     *   - Original Spliterator covers remaining elements
     *
     * long estimateSize()
     *   - Returns estimated number of elements, or Long.MAX_VALUE if unknown
     *
     * int characteristics()
     *   - Returns bitwise OR of characteristic flags
     *
     * ===== CHARACTERISTICS FLAGS =====
     *
     * ORDERED    - Elements have defined encounter order
     * DISTINCT   - Elements are unique (no duplicates)
     * SORTED     - Elements are sorted
     * SIZED      - estimateSize() returns exact count
     * NONNULL    - Elements are never null
     * IMMUTABLE  - Source cannot be modified
     * CONCURRENT - Source can be safely modified concurrently
     * SUBSIZED   - trySplit() returns SIZED spliterators
     */
    public static void demonstrateSpliteratorBasics() {
        System.out.println("=== SPLITERATOR BASICS ===\n");

        List<String> list = List.of("Apple", "Banana", "Cherry", "Date");

        // Get Spliterator from collection
        Spliterator<String> spliterator = list.spliterator();

        System.out.println("Spliterator from List:");
        System.out.println("  estimateSize(): " + spliterator.estimateSize());
        System.out.println("  characteristics(): " + spliterator.characteristics());

        // Check specific characteristics
        System.out.println("  ORDERED: " + spliterator.hasCharacteristics(Spliterator.ORDERED));
        System.out.println("  SIZED: " + spliterator.hasCharacteristics(Spliterator.SIZED));
        System.out.println("  SORTED: " + spliterator.hasCharacteristics(Spliterator.SORTED));
        System.out.println("  DISTINCT: " + spliterator.hasCharacteristics(Spliterator.DISTINCT));

        System.out.println();
    }

    // =====================================================================
    // TRYADVANCE - Process one element at a time
    // =====================================================================

    /**
     * TRYADVANCE SIGNATURE:
     *
     * boolean tryAdvance(Consumer<? super T> action)
     *
     * BEHAVIOR:
     *   - If element exists: performs action on element, advances, returns TRUE
     *   - If no more elements: returns FALSE
     *
     * USE CASE:
     *   - Process elements one at a time with control over iteration
     *   - Similar to Iterator.hasNext() + next() combined
     */
    public static void demonstrateTryAdvance() {
        System.out.println("=== TRYADVANCE ===\n");

        List<String> list = List.of("One", "Two", "Three", "Four");
        Spliterator<String> spliterator = list.spliterator();

        // EXAMPLE 1: Process one element at a time
        System.out.println("--- Processing one at a time ---");

        System.out.print("First tryAdvance: ");
        boolean hasFirst = spliterator.tryAdvance(s -> System.out.println(s));
        System.out.println("  returned: " + hasFirst);

        System.out.print("Second tryAdvance: ");
        boolean hasSecond = spliterator.tryAdvance(s -> System.out.println(s));
        System.out.println("  returned: " + hasSecond);

        System.out.println("Remaining estimateSize: " + spliterator.estimateSize());

        // EXAMPLE 2: Loop with tryAdvance
        System.out.println("\n--- Loop with tryAdvance ---");

        Spliterator<Integer> numSpliterator = List.of(1, 2, 3, 4, 5).spliterator();

        System.out.print("Elements: ");
        while (numSpliterator.tryAdvance(n -> System.out.print(n + " "))) {
            // Processing happens in the Consumer
        }
        System.out.println();

        // EXAMPLE 3: Process limited number
        System.out.println("\n--- Process only first 2 ---");

        Spliterator<String> fruitSpliterator = List.of("Apple", "Banana", "Cherry", "Date").spliterator();

        int count = 0;
        while (count < 2 && fruitSpliterator.tryAdvance(s -> System.out.println("  " + s))) {
            count++;
        }
        System.out.println("Stopped after " + count + " elements");
        System.out.println("Remaining: " + fruitSpliterator.estimateSize());

        System.out.println();
    }

    // =====================================================================
    // FOREACHREMAINING - Process all remaining elements
    // =====================================================================

    /**
     * FOREACHREMAINING SIGNATURE:
     *
     * void forEachRemaining(Consumer<? super T> action)
     *
     * BEHAVIOR:
     *   - Processes ALL remaining elements
     *   - More efficient than calling tryAdvance in a loop
     *   - After this call, estimateSize() should return 0
     */
    public static void demonstrateForEachRemaining() {
        System.out.println("=== FOREACHREMAINING ===\n");

        // EXAMPLE 1: Process all elements
        System.out.println("--- Process all elements ---");

        Spliterator<String> spliterator = List.of("A", "B", "C", "D").spliterator();

        System.out.println("Before: estimateSize = " + spliterator.estimateSize());
        System.out.print("Elements: ");
        spliterator.forEachRemaining(s -> System.out.print(s + " "));
        System.out.println();
        System.out.println("After: estimateSize = " + spliterator.estimateSize());

        // EXAMPLE 2: Process remaining after some tryAdvance calls
        System.out.println("\n--- Process remaining after tryAdvance ---");

        Spliterator<Integer> numSplit = List.of(1, 2, 3, 4, 5, 6).spliterator();

        // Process first two
        System.out.println("Processing first 2 with tryAdvance:");
        numSplit.tryAdvance(n -> System.out.println("  tryAdvance: " + n));
        numSplit.tryAdvance(n -> System.out.println("  tryAdvance: " + n));

        // Process remaining
        System.out.println("Processing remaining with forEachRemaining:");
        numSplit.forEachRemaining(n -> System.out.println("  forEachRemaining: " + n));

        // EXAMPLE 3: After split
        System.out.println("\n--- forEachRemaining after split ---");

        Spliterator<String> original = List.of("A", "B", "C", "D", "E", "F").spliterator();
        Spliterator<String> split = original.trySplit();

        System.out.println("Split portion:");
        if (split != null) {
            split.forEachRemaining(s -> System.out.print(s + " "));
            System.out.println();
        }

        System.out.println("Original remaining:");
        original.forEachRemaining(s -> System.out.print(s + " "));
        System.out.println();

        System.out.println();
    }

    // =====================================================================
    // TRYSPLIT - Split for parallel processing
    // =====================================================================

    /**
     * TRYSPLIT SIGNATURE:
     *
     * Spliterator<T> trySplit()
     *
     * BEHAVIOR:
     *   - Returns a NEW Spliterator covering a portion of elements
     *   - Original Spliterator covers the REMAINING elements
     *   - Returns NULL if splitting is not possible or worthwhile
     *
     * ===== SPLITTING STRATEGY =====
     *
     * Typically splits approximately in half:
     *   Original: [1, 2, 3, 4, 5, 6, 7, 8]
     *   After trySplit():
     *     - Returned split: [1, 2, 3, 4]
     *     - Original remaining: [5, 6, 7, 8]
     *
     * ===== EXAM TIP =====
     *
     * After trySplit():
     *   - The RETURNED spliterator covers FIRST portion
     *   - The ORIGINAL spliterator covers SECOND (remaining) portion
     */
    public static void demonstrateTrySplit() {
        System.out.println("=== TRYSPLIT ===\n");

        // EXAMPLE 1: Basic split
        System.out.println("--- Basic trySplit ---");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        Spliterator<Integer> original = numbers.spliterator();

        System.out.println("Original size before split: " + original.estimateSize());

        Spliterator<Integer> firstHalf = original.trySplit();

        System.out.println("\nAfter trySplit():");
        if (firstHalf != null) {
            System.out.println("  Returned split size: " + firstHalf.estimateSize());
            System.out.print("  Returned split elements: ");
            firstHalf.forEachRemaining(n -> System.out.print(n + " "));
            System.out.println();
        }

        System.out.println("  Original remaining size: " + original.estimateSize());
        System.out.print("  Original remaining elements: ");
        original.forEachRemaining(n -> System.out.print(n + " "));
        System.out.println();

        // EXAMPLE 2: Multiple splits
        System.out.println("\n--- Multiple splits ---");

        Spliterator<Integer> s1 = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16).spliterator();

        System.out.println("Original size: " + s1.estimateSize());

        Spliterator<Integer> s2 = s1.trySplit();
        System.out.println("After 1st split: s1=" + s1.estimateSize() + ", s2=" + (s2 != null ? s2.estimateSize() : "null"));

        Spliterator<Integer> s3 = s1.trySplit();
        Spliterator<Integer> s4 = s2 != null ? s2.trySplit() : null;

        System.out.println("After 2nd splits:");
        System.out.println("  s1: " + s1.estimateSize());
        System.out.println("  s2: " + (s2 != null ? s2.estimateSize() : "null"));
        System.out.println("  s3: " + (s3 != null ? s3.estimateSize() : "null"));
        System.out.println("  s4: " + (s4 != null ? s4.estimateSize() : "null"));

        // EXAMPLE 3: When trySplit returns null
        System.out.println("\n--- trySplit returns null ---");

        Spliterator<String> tiny = List.of("A").spliterator();
        Spliterator<String> split = tiny.trySplit();
        System.out.println("Splitting single element: " + (split == null ? "null (can't split)" : split));

        // Keep splitting until null
        Spliterator<Integer> shrinking = List.of(1, 2, 3, 4).spliterator();
        int splitCount = 0;
        while (shrinking.trySplit() != null) {
            splitCount++;
        }
        System.out.println("Number of successful splits on [1,2,3,4]: " + splitCount);

        // EXAMPLE 4: Simulating parallel processing
        System.out.println("\n--- Simulating parallel with trySplit ---");

        Spliterator<String> tasks = List.of("Task1", "Task2", "Task3", "Task4", "Task5", "Task6").spliterator();
        Spliterator<String> worker2Tasks = tasks.trySplit();

        System.out.println("Worker 1 processes:");
        if (worker2Tasks != null) {
            worker2Tasks.forEachRemaining(t -> System.out.println("  Worker 1: " + t));
        }

        System.out.println("Worker 2 processes:");
        tasks.forEachRemaining(t -> System.out.println("  Worker 2: " + t));

        System.out.println();
    }

    // =====================================================================
    // SPLITERATOR CHARACTERISTICS
    // =====================================================================

    /**
     * SPLITERATOR CHARACTERISTICS:
     *
     * ORDERED (0x00000010)    - Has defined encounter order
     * DISTINCT (0x00000001)   - Elements are unique
     * SORTED (0x00000004)     - Elements are sorted
     * SIZED (0x00000040)      - Know exact size
     * NONNULL (0x00000100)    - No null elements
     * IMMUTABLE (0x00000400)  - Source cannot change
     * CONCURRENT (0x00001000) - Safe for concurrent modification
     * SUBSIZED (0x00004000)   - Split returns SIZED spliterators
     *
     * Check with: spliterator.hasCharacteristics(Spliterator.ORDERED)
     */
    public static void demonstrateSpliteratorCharacteristics() {
        System.out.println("=== SPLITERATOR CHARACTERISTICS ===\n");

        // Different sources have different characteristics
        System.out.println("--- Different source characteristics ---");

        // ArrayList - ORDERED, SIZED, SUBSIZED
        Spliterator<String> arrayListSplit = new ArrayList<>(List.of("A", "B", "C")).spliterator();
        printCharacteristics("ArrayList", arrayListSplit);

        // HashSet - DISTINCT, SIZED
        Spliterator<String> hashSetSplit = new HashSet<>(List.of("A", "B", "C")).spliterator();
        printCharacteristics("HashSet", hashSetSplit);

        // TreeSet - ORDERED, DISTINCT, SORTED, SIZED
        Spliterator<String> treeSetSplit = new TreeSet<>(List.of("A", "B", "C")).spliterator();
        printCharacteristics("TreeSet", treeSetSplit);

        // Stream.of - ORDERED, SIZED, SUBSIZED, IMMUTABLE
        Spliterator<String> streamSplit = Stream.of("A", "B", "C").spliterator();
        printCharacteristics("Stream.of", streamSplit);

        // IntStream.range - ORDERED, SIZED, SUBSIZED, IMMUTABLE, NONNULL, DISTINCT, SORTED
        Spliterator.OfInt intSplit = IntStream.range(1, 10).spliterator();
        printCharacteristics("IntStream.range", intSplit);

        System.out.println();

        // =====================================================================
        // EXAM PRACTICE
        // =====================================================================
        System.out.println("--- EXAM PRACTICE ---");
        System.out.println();
        System.out.println("Q1: What does tryAdvance return when no elements remain?");
        System.out.println("    Answer: false");
        System.out.println();
        System.out.println("Q2: After trySplit(), which spliterator has the first elements?");
        System.out.println("    Answer: The RETURNED spliterator (not the original)");
        System.out.println();
        System.out.println("Q3: What does trySplit() return if splitting isn't possible?");
        System.out.println("    Answer: null");
        System.out.println();
        System.out.println("Q4: Which method processes ALL remaining elements?");
        System.out.println("    Answer: forEachRemaining(Consumer)");
        System.out.println();
        System.out.println("Q5: Does Optional have map and flatMap?");
        System.out.println("    Answer: YES (unlike primitive optionals!)");
    }

    private static void printCharacteristics(String name, Spliterator<?> split) {
        System.out.println("\n" + name + ":");
        System.out.println("  ORDERED:    " + split.hasCharacteristics(Spliterator.ORDERED));
        System.out.println("  DISTINCT:   " + split.hasCharacteristics(Spliterator.DISTINCT));
        System.out.println("  SORTED:     " + split.hasCharacteristics(Spliterator.SORTED));
        System.out.println("  SIZED:      " + split.hasCharacteristics(Spliterator.SIZED));
        System.out.println("  NONNULL:    " + split.hasCharacteristics(Spliterator.NONNULL));
        System.out.println("  IMMUTABLE:  " + split.hasCharacteristics(Spliterator.IMMUTABLE));
        System.out.println("  SUBSIZED:   " + split.hasCharacteristics(Spliterator.SUBSIZED));
    }
}
