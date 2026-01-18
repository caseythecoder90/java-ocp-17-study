package ch10streams;

import java.util.*;
import java.util.stream.*;

import static java.util.stream.Collectors.toSet;

/**
 * Intermediate Stream Operations
 *
 * Intermediate operations return a NEW Stream and are LAZY - they don't execute
 * until a terminal operation is called. They can be chained together.
 *
 * ===== KEY CHARACTERISTICS =====
 *
 * 1. LAZY EVALUATION: Nothing happens until terminal operation
 * 2. RETURN STREAM: Each intermediate op returns a new Stream
 * 3. CHAINABLE: stream.filter().map().sorted().limit()
 * 4. STATELESS vs STATEFUL:
 *    - Stateless: filter, map, flatMap, peek (process each element independently)
 *    - Stateful: distinct, sorted, limit, skip (need to know about other elements)
 *
 * ===== OPERATIONS COVERED =====
 *
 * | Operation      | Signature                                    | Returns      |
 * |----------------|----------------------------------------------|--------------|
 * | filter         | filter(Predicate<T>)                         | Stream<T>    |
 * | map            | map(Function<T,R>)                           | Stream<R>    |
 * | flatMap        | flatMap(Function<T,Stream<R>>)               | Stream<R>    |
 * | distinct       | distinct()                                   | Stream<T>    |
 * | sorted         | sorted() / sorted(Comparator<T>)             | Stream<T>    |
 * | limit          | limit(long)                                  | Stream<T>    |
 * | skip           | skip(long)                                   | Stream<T>    |
 * | peek           | peek(Consumer<T>)                            | Stream<T>    |
 * | concat         | Stream.concat(Stream<T>, Stream<T>)          | Stream<T>    |
 *
 * ===== MEMORY AID: RETURN TYPE =====
 *
 * Most intermediate operations: Stream<T> -> Stream<T> (same type)
 * Exceptions:
 *   - map:     Stream<T> -> Stream<R>  (type can change!)
 *   - flatMap: Stream<T> -> Stream<R>  (type can change + flattens)
 */
public class IntermediateOperations {

    public static void main(String[] args) {
        demonstrateFilter();
        demonstrateMap();
        demonstrateDistinct();
        demonstrateLimitSkip();
        demonstrateFlatMap();
        demonstrateConcat();
        demonstrateSorted();
        demonstratePeek();
        demonstrateChainingOperations();
    }

    // =====================================================================
    // FILTER - Keep elements matching predicate
    // =====================================================================

    /**
     * FILTER SIGNATURE:
     *
     * Stream<T> filter(Predicate<? super T> predicate)
     *
     * TYPE PARAMETERS:
     *   - T = stream element type (SAME before and after)
     *
     * PARAMETER:
     *   - predicate: Predicate<T> - test to apply to each element
     *               (T) -> boolean
     *               Elements where predicate returns TRUE are kept
     *
     * RETURNS: Stream<T> (same type as input)
     *
     * ===== TYPE FLOW =====
     *
     * Stream<T> --filter(predicate)--> Stream<T>
     *              |
     *              v
     *         keeps elements where predicate(element) == true
     *
     * ===== EXAM TIP =====
     *
     * filter does NOT change the element type!
     * Stream<String>.filter(...) -> Stream<String>
     */
    public static void demonstrateFilter() {
        System.out.println("=== FILTER ===\n");

        List<String> words = List.of("apple", "banana", "cherry", "apricot", "blueberry");

        // EXAMPLE 1: Basic filter
        // TYPE ANALYSIS:
        //   T = String
        //   predicate: (String) -> boolean
        //   Result: Stream<String>
        List<String> startsWithA = words.stream()
            .filter(s -> s.startsWith("a"))  // predicate: (String) -> boolean
            .toList();
        System.out.println("filter(startsWith 'a'): " + startsWithA);
        // [apple, apricot]

        // EXAMPLE 2: Filter by length
        // predicate: s -> s.length() > 5
        // Returns true for: banana(6), cherry(6), apricot(7), blueberry(9)
        List<String> longWords = words.stream()
            .filter(s -> s.length() > 5)
            .toList();
        System.out.println("filter(length > 5): " + longWords);

        // EXAMPLE 3: Multiple conditions in predicate
        // predicate: s -> s.length() > 5 && s.startsWith("b")
        List<String> longBWords = words.stream()
            .filter(s -> s.length() > 5 && s.startsWith("b"))
            .toList();
        System.out.println("filter(length>5 AND startsWith 'b'): " + longBWords);

        // EXAMPLE 4: Chained filters (equivalent to AND)
        // Same as above but with two filter calls
        List<String> chainedFilter = words.stream()
            .filter(s -> s.length() > 5)
            .filter(s -> s.startsWith("b"))
            .toList();
        System.out.println("chained filters: " + chainedFilter);

        // EXAMPLE 5: Filter with method reference
        List<String> nonEmpty = words.stream()
            .filter(s -> !s.isEmpty())  // or use Predicate.not(String::isEmpty)
            .toList();
        System.out.println("filter non-empty: " + nonEmpty);

        // EXAMPLE 6: Filter with numbers
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> evens = numbers.stream()
            .filter(n -> n % 2 == 0)  // predicate: (Integer) -> boolean
            .toList();
        System.out.println("filter even numbers: " + evens);

        System.out.println();
    }

    // =====================================================================
    // MAP - Transform each element
    // =====================================================================

    /**
     * MAP SIGNATURE:
     *
     * <R> Stream<R> map(Function<? super T, ? extends R> mapper)
     *
     * TYPE PARAMETERS:
     *   - T = input element type
     *   - R = output element type (CAN BE DIFFERENT!)
     *
     * PARAMETER:
     *   - mapper: Function<T, R> - transforms each element
     *            (T) -> R
     *
     * RETURNS: Stream<R> (can be different type from input!)
     *
     * ===== TYPE FLOW =====
     *
     * Stream<T> --map(mapper)--> Stream<R>
     *              |
     *              v
     *         applies mapper to each element
     *         element of type T becomes element of type R
     *
     * ===== KEY DIFFERENCE FROM FILTER =====
     *
     * filter: Stream<T> -> Stream<T>  (same type, fewer elements)
     * map:    Stream<T> -> Stream<R>  (can change type, same count)
     *
     * ===== EXAM TIP =====
     *
     * map ALWAYS produces same NUMBER of elements (1-to-1 mapping)
     * but the TYPE can change!
     */
    public static void demonstrateMap() {
        System.out.println("=== MAP ===\n");

        List<String> words = List.of("apple", "banana", "cherry");

        // EXAMPLE 1: String -> Integer (different types!)
        // TYPE ANALYSIS:
        //   T = String
        //   R = Integer
        //   mapper: String::length is (String) -> Integer
        //   Result: Stream<Integer>
        List<Integer> lengths = words.stream()
            .map(String::length)  // mapper: (String) -> Integer
            .toList();
        System.out.println("map(String::length): " + lengths);
        System.out.println("  T = String, R = Integer");
        // [5, 6, 6]

        // EXAMPLE 2: String -> String (same type, transformed)
        // TYPE ANALYSIS:
        //   T = String, R = String
        //   mapper: String::toUpperCase is (String) -> String
        List<String> upper = words.stream()
            .map(String::toUpperCase)
            .toList();
        System.out.println("map(toUpperCase): " + upper);
        System.out.println("  T = String, R = String");

        // EXAMPLE 3: String -> Character (first char)
        // TYPE ANALYSIS:
        //   T = String
        //   R = Character
        //   mapper: s -> s.charAt(0) is (String) -> char (boxed to Character)
        List<Character> firstChars = words.stream()
            .map(s -> s.charAt(0))
            .toList();
        System.out.println("map(first char): " + firstChars);
        System.out.println("  T = String, R = Character");

        // EXAMPLE 4: Integer -> Integer
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        List<Integer> doubled = numbers.stream()
            .map(n -> n * 2)  // mapper: (Integer) -> Integer
            .toList();
        System.out.println("map(n * 2): " + doubled);

        // EXAMPLE 5: Integer -> String
        // TYPE ANALYSIS:
        //   T = Integer
        //   R = String
        List<String> numberStrings = numbers.stream()
            .map(n -> "Number: " + n)  // mapper: (Integer) -> String
            .toList();
        System.out.println("map(Integer -> String): " + numberStrings);
        System.out.println("  T = Integer, R = String");

        // EXAMPLE 6: Chained maps
        // Stream<String> -> Stream<Integer> -> Stream<String>
        List<String> lengthDescriptions = words.stream()
            .map(String::length)       // String -> Integer
            .map(len -> "Length: " + len)  // Integer -> String
            .toList();
        System.out.println("chained maps: " + lengthDescriptions);

        // EXAMPLE 7: Map with record/object
        record Person(String name, int age) {}
        List<Person> people = List.of(
            new Person("Alice", 30),
            new Person("Bob", 25)
        );

        // Person -> String (extracting property)
        List<String> names = people.stream()
            .map(Person::name)  // mapper: (Person) -> String
            .toList();
        System.out.println("map(Person::name): " + names);
        System.out.println("  T = Person, R = String");

        System.out.println();
    }

    // =====================================================================
    // DISTINCT - Remove duplicates
    // =====================================================================

    /**
     * DISTINCT SIGNATURE:
     *
     * Stream<T> distinct()
     *
     * TYPE: Stream<T> -> Stream<T> (same type)
     *
     * BEHAVIOR:
     *   - Removes duplicate elements
     *   - Uses equals() to determine duplicates
     *   - STATEFUL operation (must track seen elements)
     *   - Preserves encounter order (first occurrence kept)
     *
     * ===== IMPORTANT =====
     *
     * For custom objects, equals() and hashCode() MUST be properly implemented!
     * Records automatically implement equals/hashCode based on components.
     */
    public static void demonstrateDistinct() {
        System.out.println("=== DISTINCT ===\n");

        // EXAMPLE 1: Distinct with primitives
        List<Integer> numbers = List.of(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);
        List<Integer> unique = numbers.stream()
            .distinct()
            .toList();
        System.out.println("distinct integers: " + unique);
        // [1, 2, 3, 4]

        // EXAMPLE 2: Distinct with strings
        List<String> words = List.of("apple", "banana", "apple", "cherry", "banana");
        List<String> uniqueWords = words.stream()
            .distinct()
            .toList();
        System.out.println("distinct strings: " + uniqueWords);
        // [apple, banana, cherry] - first occurrence kept

        // EXAMPLE 3: Distinct preserves order
        List<Integer> ordered = List.of(3, 1, 2, 1, 3, 2);
        List<Integer> uniqueOrdered = ordered.stream()
            .distinct()
            .toList();
        System.out.println("distinct preserves first occurrence order: " + uniqueOrdered);
        // [3, 1, 2] - not sorted, but order of first occurrence

        // EXAMPLE 4: Distinct with custom objects (using record)
        record Product(String name, double price) {}
        List<Product> products = List.of(
            new Product("Apple", 1.00),
            new Product("Banana", 0.50),
            new Product("Apple", 1.00),  // duplicate
            new Product("Apple", 1.50)   // NOT duplicate (different price)
        );
        List<Product> uniqueProducts = products.stream()
            .distinct()
            .toList();
        System.out.println("distinct products: " + uniqueProducts);
        // Records use all components for equals()

        // EXAMPLE 5: Combining distinct with other operations
        List<String> mixed = List.of("APPLE", "apple", "Apple", "BANANA", "banana");
        List<String> uniqueLower = mixed.stream()
            .map(String::toLowerCase)  // normalize first
            .distinct()                 // then remove duplicates
            .toList();
        System.out.println("map(toLowerCase) then distinct: " + uniqueLower);
        // [apple, banana]

        System.out.println();
    }

    // =====================================================================
    // LIMIT and SKIP - Truncate stream
    // =====================================================================

    /**
     * LIMIT SIGNATURE:
     *
     * Stream<T> limit(long maxSize)
     *
     * TYPE: Stream<T> -> Stream<T> (same type)
     *
     * BEHAVIOR:
     *   - Returns stream with at most maxSize elements
     *   - SHORT-CIRCUITING: stops processing once limit reached
     *   - STATEFUL operation
     *
     * ===== SKIP SIGNATURE =====
     *
     * Stream<T> skip(long n)
     *
     * TYPE: Stream<T> -> Stream<T> (same type)
     *
     * BEHAVIOR:
     *   - Discards first n elements
     *   - Returns remaining elements
     *   - STATEFUL operation
     *
     * ===== COMBINING LIMIT AND SKIP =====
     *
     * skip(n).limit(m) = skip first n, then take next m
     * Useful for pagination!
     *
     * Page 1: skip(0).limit(10)   -> elements 0-9
     * Page 2: skip(10).limit(10)  -> elements 10-19
     * Page 3: skip(20).limit(10)  -> elements 20-29
     */
    public static void demonstrateLimitSkip() {
        System.out.println("=== LIMIT and SKIP ===\n");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // EXAMPLE 1: Basic limit
        List<Integer> firstThree = numbers.stream()
            .limit(3)
            .toList();
        System.out.println("limit(3): " + firstThree);
        // [1, 2, 3]

        // EXAMPLE 2: Basic skip
        List<Integer> skipThree = numbers.stream()
            .skip(3)
            .toList();
        System.out.println("skip(3): " + skipThree);
        // [4, 5, 6, 7, 8, 9, 10]

        // EXAMPLE 3: Skip then limit (pagination)
        List<Integer> page2 = numbers.stream()
            .skip(3)   // skip first 3
            .limit(3)  // take next 3
            .toList();
        System.out.println("skip(3).limit(3) [page 2]: " + page2);
        // [4, 5, 6]

        // EXAMPLE 4: Limit larger than stream size
        List<Integer> limitTooMuch = numbers.stream()
            .limit(100)  // only 10 elements exist
            .toList();
        System.out.println("limit(100) on 10 elements: " + limitTooMuch);
        // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10] - just returns all

        // EXAMPLE 5: Skip more than stream size
        List<Integer> skipTooMuch = numbers.stream()
            .skip(100)
            .toList();
        System.out.println("skip(100) on 10 elements: " + skipTooMuch);
        // [] - empty list

        // EXAMPLE 6: Limit with infinite stream
        System.out.println("\nWith infinite stream:");
        List<Integer> firstFive = Stream.iterate(1, n -> n + 1)  // infinite: 1, 2, 3, ...
            .limit(5)  // MUST limit infinite stream!
            .toList();
        System.out.println("infinite.limit(5): " + firstFive);
        // [1, 2, 3, 4, 5]

        // EXAMPLE 7: Skip with infinite stream
        List<Integer> skipThenLimit = Stream.iterate(1, n -> n + 1)
            .skip(10)   // skip first 10
            .limit(5)   // take next 5
            .toList();
        System.out.println("infinite.skip(10).limit(5): " + skipThenLimit);
        // [11, 12, 13, 14, 15]

        // EXAMPLE 8: Order matters! limit then skip vs skip then limit
        System.out.println("\nOrder matters:");
        List<Integer> limitThenSkip = numbers.stream()
            .limit(5)   // [1,2,3,4,5]
            .skip(2)    // [3,4,5]
            .toList();
        System.out.println("limit(5).skip(2): " + limitThenSkip);

        List<Integer> skipThenLimitDiff = numbers.stream()
            .skip(2)    // [3,4,5,6,7,8,9,10]
            .limit(5)   // [3,4,5,6,7]
            .toList();
        System.out.println("skip(2).limit(5): " + skipThenLimitDiff);

        System.out.println();
    }

    // =====================================================================
    // FLATMAP - Flatten nested structures (IMPORTANT!)
    // =====================================================================

    /**
     * FLATMAP SIGNATURE:
     *
     * <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper)
     *
     * TYPE PARAMETERS:
     *   - T = input element type
     *   - R = output element type (in the flattened stream)
     *
     * PARAMETER:
     *   - mapper: Function<T, Stream<R>> - transforms each element into a STREAM
     *            (T) -> Stream<R>
     *
     * RETURNS: Stream<R> - all streams merged/flattened into one
     *
     * ===== HOW FLATMAP WORKS =====
     *
     * 1. Apply mapper to each element, getting a Stream for each
     * 2. FLATTEN all those streams into a single stream
     *
     * Stream<T>     mapper      Stream<Stream<R>>    flatten     Stream<R>
     * [A, B, C] --> each T --> [[a1,a2], [b1], [c1,c2,c3]] --> [a1,a2,b1,c1,c2,c3]
     *               becomes
     *               Stream<R>
     *
     * ===== KEY INSIGHT =====
     *
     * map:     1 element -> 1 element  (1:1)
     * flatMap: 1 element -> 0, 1, or many elements (1:N)
     *
     * ===== COMMON USE CASES =====
     *
     * 1. List of Lists -> flat List
     * 2. Object with collection property -> all items from all objects
     * 3. String -> characters
     * 4. Optional handling (flatMap with Optional)
     *
     * ===== EXAM TIP =====
     *
     * The mapper function MUST return a Stream!
     * If you have a Collection, use .stream() on it:
     *   flatMap(list -> list.stream())  // List<T> -> Stream<T>
     *   flatMap(Collection::stream)     // method reference version
     */
    public static void demonstrateFlatMap() {
        System.out.println("=== FLATMAP (IMPORTANT!) ===\n");

        // =====================================================================
        // EXAMPLE 1: List of Lists -> Flat List
        // =====================================================================
        System.out.println("--- Example 1: List<List<T>> -> List<T> ---");

        List<List<Integer>> listOfLists = List.of(
            List.of(1, 2, 3),
            List.of(4, 5),
            List.of(6, 7, 8, 9)
        );

        // WITHOUT flatMap - would get Stream<List<Integer>>
        // WITH flatMap - get Stream<Integer>

        // TYPE ANALYSIS:
        //   T = List<Integer> (element of outer stream)
        //   mapper: list -> list.stream() returns Stream<Integer>
        //   R = Integer
        //   Result: Stream<Integer> (flattened)
        //
        // STEP BY STEP:
        //   Element [1,2,3]  -> mapper -> Stream.of(1,2,3)
        //   Element [4,5]    -> mapper -> Stream.of(4,5)
        //   Element [6,7,8,9]-> mapper -> Stream.of(6,7,8,9)
        //   Flatten all -> Stream.of(1,2,3,4,5,6,7,8,9)
        List<Integer> flattened = listOfLists.stream()
            .flatMap(list -> list.stream())  // or Collection::stream
            .toList();
        System.out.println("Input: " + listOfLists);
        System.out.println("flatMap(list -> list.stream()): " + flattened);
        System.out.println("  T = List<Integer>");
        System.out.println("  mapper returns: Stream<Integer>");
        System.out.println("  R = Integer");

        // Method reference version
        List<Integer> flattenedRef = listOfLists.stream()
            .flatMap(Collection::stream)
            .toList();
        System.out.println("flatMap(Collection::stream): " + flattenedRef);

        // =====================================================================
        // EXAMPLE 2: String -> Characters
        // =====================================================================
        System.out.println("\n--- Example 2: String -> Characters ---");

        List<String> words = List.of("Hi", "Bye");

        // TYPE ANALYSIS:
        //   T = String
        //   mapper: s -> s.chars()... returns Stream<Character>
        //   R = Character
        //
        // "Hi"  -> ['H', 'i']
        // "Bye" -> ['B', 'y', 'e']
        // Flattened -> ['H', 'i', 'B', 'y', 'e']
        List<Character> allChars = words.stream()
            .flatMap(s -> s.chars().mapToObj(c -> (char) c))
            .toList();
        System.out.println("Words: " + words);
        System.out.println("flatMap to chars: " + allChars);

        "Casey".chars()
                .mapToObj(i -> String.valueOf(i))
                .sorted(Comparator.reverseOrder())
                .forEach(System.out::println);

        // Alternative: split to single-char strings
        List<String> charStrings = words.stream()
            .flatMap(s -> Arrays.stream(s.split("")))
            .toList();
        System.out.println("flatMap split to strings: " + charStrings);

        // =====================================================================
        // EXAMPLE 3: Object with Collection Property
        // =====================================================================
        System.out.println("\n--- Example 3: Object with Collection Property ---");

        record Order(String customer, List<String> items) {}

        List<Order> orders = List.of(
            new Order("Alice", List.of("Apple", "Banana")),
            new Order("Bob", List.of("Cherry", "Date", "Elderberry")),
            new Order("Carol", List.of("Fig"))
        );

        // Get ALL items from ALL orders (flattened)
        // TYPE ANALYSIS:
        //   T = Order
        //   mapper: order -> order.items().stream() returns Stream<String>
        //   R = String
        List<String> allItems = orders.stream()
            .flatMap(order -> order.items().stream())
            .toList();
        System.out.println("Orders: " + orders);
        System.out.println("flatMap to all items: " + allItems);
        System.out.println("  T = Order");
        System.out.println("  mapper: Order -> Stream<String>");
        System.out.println("  R = String");

        // =====================================================================
        // EXAMPLE 4: flatMap vs map - The Difference
        // =====================================================================
        System.out.println("\n--- Example 4: flatMap vs map (KEY DIFFERENCE!) ---");

        List<List<String>> nested = List.of(
            List.of("a", "b"),
            List.of("c", "d", "e")
        );

        // Using MAP: get Stream<Stream<String>> - still nested!
        List<Stream<String>> mapResult = nested.stream()
            .map(list -> list.stream())  // returns Stream<Stream<String>>
            .toList();
        System.out.println("map(list -> list.stream()): Stream of Streams!");
        System.out.println("  Result type: List<Stream<String>>");

        // Using FLATMAP: get Stream<String> - flattened!
        List<String> flatMapResult = nested.stream()
            .flatMap(list -> list.stream())  // returns Stream<String>
            .toList();
        System.out.println("flatMap(list -> list.stream()): " + flatMapResult);
        System.out.println("  Result type: List<String>");

        // =====================================================================
        // EXAMPLE 5: flatMap with Empty Streams (Filtering effect)
        // =====================================================================
        System.out.println("\n--- Example 5: flatMap with Empty Streams ---");

        // flatMap can FILTER by returning empty stream!
        List<String> wordsWithNulls = Arrays.asList("hello", null, "world", null, "!");

        // Return empty stream for nulls -> effectively filters them out
        // TYPE ANALYSIS:
        //   T = String (nullable)
        //   mapper returns: Stream.empty() for null, Stream.of(s) for non-null
        //   R = String
        List<String> nonNulls = wordsWithNulls.stream()
            .flatMap(s -> s == null ? Stream.empty() : Stream.of(s))
            .toList();
        System.out.println("Input with nulls: " + wordsWithNulls);
        System.out.println("flatMap filtering nulls: " + nonNulls);

        // =====================================================================
        // EXAMPLE 6: flatMap to Expand Elements (1 -> Many)
        // =====================================================================
        System.out.println("\n--- Example 6: Expand 1 element to many ---");

        List<Integer> nums = List.of(1, 2, 3);

        // Each number n becomes [n, n*10, n*100]
        // TYPE ANALYSIS:
        //   T = Integer
        //   mapper: n -> Stream.of(n, n*10, n*100) returns Stream<Integer>
        //   R = Integer
        //
        // 1 -> [1, 10, 100]
        // 2 -> [2, 20, 200]
        // 3 -> [3, 30, 300]
        // Flattened -> [1, 10, 100, 2, 20, 200, 3, 30, 300]
        List<Integer> expanded = nums.stream()
            .flatMap(n -> Stream.of(n, n * 10, n * 100))
            .toList();
        System.out.println("Input: " + nums);
        System.out.println("flatMap(n -> [n, n*10, n*100]): " + expanded);

        // =====================================================================
        // EXAMPLE 7: Nested flatMap
        // =====================================================================
        System.out.println("\n--- Example 7: Nested flatMap (3D -> 1D) ---");

        List<List<List<Integer>>> threeD = List.of(
            List.of(List.of(1, 2), List.of(3)),
            List.of(List.of(4, 5, 6))
        );

        // Flatten 3D to 1D with two flatMaps
        // First flatMap: List<List<List<Int>>> -> Stream<List<Int>>
        // Second flatMap: Stream<List<Int>> -> Stream<Int>
        List<Integer> oneD = threeD.stream()
            .flatMap(Collection::stream)   // 3D -> 2D
            .flatMap(Collection::stream)   // 2D -> 1D
            .toList();
        System.out.println("3D input: " + threeD);
        System.out.println("double flatMap to 1D: " + oneD);

        // =====================================================================
        // EXAMPLE 8: flatMap with Optional (handling absent values)
        // =====================================================================
        System.out.println("\n--- Example 8: flatMap with Optional ---");

        record Employee(String name, Optional<String> department) {}

        List<Employee> employees = List.of(
            new Employee("Alice", Optional.of("Engineering")),
            new Employee("Bob", Optional.empty()),  // no department
            new Employee("Carol", Optional.of("Marketing"))
        );

        // Get all department names (skip employees without department)
        // TYPE ANALYSIS:
        //   T = Employee
        //   mapper: emp.department().stream() -> Stream<String> (0 or 1 element)
        //   R = String
        List<String> departments = employees.stream()
            .flatMap(emp -> emp.department().stream())  // Optional.stream()!
            .toList();
        System.out.println("Employees: " + employees);
        System.out.println("flatMap departments (skips empty): " + departments);
        System.out.println("  Optional.empty().stream() -> empty Stream (filtered out)");

        // =====================================================================
        // EXAMPLE 9: Common Exam Pattern - Words to Letters
        // =====================================================================
        System.out.println("\n--- Example 9: Common Exam Pattern ---");

        List<String> sentence = List.of("the", "quick", "brown", "fox");

        // Get unique letters from all words
        Set<String> uniqueLetters = sentence.stream()
            .flatMap(word -> Arrays.stream(word.split("")))
            .collect(toSet());
        System.out.println("Sentence: " + sentence);
        System.out.println("Unique letters: " + uniqueLetters);

        // =====================================================================
        // EXAM PRACTICE
        // =====================================================================
        System.out.println("\n--- FLATMAP EXAM PRACTICE ---");
        System.out.println();
        System.out.println("Q1: What does flatMap(s -> Stream.empty()) do?");
        System.out.println("    Answer: Removes ALL elements (empty result)");
        System.out.println();
        System.out.println("Q2: What's the type of this expression?");
        System.out.println("    List<List<String>> lists;");
        System.out.println("    lists.stream().flatMap(l -> l.stream())");
        System.out.println("    Answer: Stream<String>");
        System.out.println();
        System.out.println("Q3: map vs flatMap:");
        System.out.println("    stream.map(x -> getList(x))     -> Stream<List<T>>");
        System.out.println("    stream.flatMap(x -> getList(x).stream()) -> Stream<T>");
        System.out.println();
        System.out.println("Q4: What's wrong with this?");
        System.out.println("    stream.flatMap(x -> x.getItems())  // getItems returns List");
        System.out.println("    Answer: Must return Stream! Use x.getItems().stream()");

        System.out.println();
    }

    // =====================================================================
    // STREAM.CONCAT - Combine two streams
    // =====================================================================

    /**
     * CONCAT SIGNATURE:
     *
     * static <T> Stream<T> Stream.concat(Stream<? extends T> a, Stream<? extends T> b)
     *
     * TYPE: Stream<T> + Stream<T> -> Stream<T>
     *
     * BEHAVIOR:
     *   - Creates lazily concatenated stream
     *   - Elements of first stream, then elements of second stream
     *   - Both input streams are consumed (don't reuse!)
     *
     * ===== IMPORTANT =====
     *
     * This is a STATIC method on Stream class!
     * Stream.concat(stream1, stream2)  // NOT stream1.concat(stream2)
     *
     * For more than 2 streams, use:
     *   Stream.of(s1, s2, s3).flatMap(Function.identity())
     */
    public static void demonstrateConcat() {
        System.out.println("=== STREAM.CONCAT ===\n");

        // EXAMPLE 1: Basic concat
        Stream<Integer> first = Stream.of(1, 2, 3);
        Stream<Integer> second = Stream.of(4, 5, 6);

        // TYPE ANALYSIS:
        //   T = Integer
        //   Both streams must be Stream<Integer> (or subtype)
        List<Integer> combined = Stream.concat(first, second)
            .toList();
        System.out.println("concat([1,2,3], [4,5,6]): " + combined);
        // [1, 2, 3, 4, 5, 6]

        // EXAMPLE 2: Concat with different sources
        List<String> list1 = List.of("a", "b");
        List<String> list2 = List.of("c", "d", "e");

        List<String> concatLists = Stream.concat(list1.stream(), list2.stream())
            .toList();
        System.out.println("concat two lists: " + concatLists);

        // EXAMPLE 3: Concat with operations
        List<Integer> processedConcat = Stream.concat(
                Stream.of(1, 2, 3),
                Stream.of(4, 5, 6)
            )
            .filter(n -> n % 2 == 0)  // can chain operations
            .map(n -> n * 10)
            .toList();
        System.out.println("concat then filter/map: " + processedConcat);

        // EXAMPLE 4: Concatenating more than 2 streams
        Stream<String> s1 = Stream.of("a", "b");
        Stream<String> s2 = Stream.of("c", "d");
        Stream<String> s3 = Stream.of("e", "f");

        // Method 1: Nested concat (messy)
        List<String> nested = Stream.concat(Stream.concat(s1, s2), s3).toList();
        System.out.println("nested concat: " + nested);

        // Method 2: flatMap (cleaner for many streams)
        List<String> flatMapConcat = Stream.of(
                Stream.of("a", "b"),
                Stream.of("c", "d"),
                Stream.of("e", "f")
            )
            .flatMap(s -> s)  // or Function.identity()
            .toList();
        System.out.println("flatMap concat (cleaner): " + flatMapConcat);

        // EXAMPLE 5: Concat preserves order
        List<Integer> ordered = Stream.concat(
                Stream.of(3, 1, 4),
                Stream.of(1, 5, 9)
            ).toList();
        System.out.println("concat preserves order: " + ordered);
        // [3, 1, 4, 1, 5, 9] - not sorted, just concatenated

        System.out.println();
    }

    // =====================================================================
    // SORTED - Sort elements
    // =====================================================================

    /**
     * SORTED SIGNATURES:
     *
     * 1. Stream<T> sorted()
     *    - Uses NATURAL ordering
     *    - T must implement Comparable<T>
     *    - Throws ClassCastException if T is not Comparable
     *
     * 2. Stream<T> sorted(Comparator<? super T> comparator)
     *    - Uses provided Comparator
     *    - T does NOT need to be Comparable
     *
     * TYPE: Stream<T> -> Stream<T> (same type)
     *
     * BEHAVIOR:
     *   - STATEFUL operation (must see all elements to sort)
     *   - Returns new stream with elements in sorted order
     *
     * ===== EXAM TIP =====
     *
     * sorted() with no args requires Comparable!
     * String, Integer, etc. are Comparable
     * Custom objects need Comparable OR use sorted(Comparator)
     */
    public static void demonstrateSorted() {
        System.out.println("=== SORTED ===\n");

        // =====================================================================
        // SIGNATURE 1: sorted() - Natural Order
        // =====================================================================
        System.out.println("--- sorted() - Natural Order ---");

        // EXAMPLE 1: Sort integers (natural order = ascending)
        List<Integer> numbers = List.of(5, 2, 8, 1, 9, 3);
        List<Integer> sortedNumbers = numbers.stream()
            .sorted()  // uses Integer's natural ordering (ascending)
            .toList();
        System.out.println("sorted() integers: " + sortedNumbers);
        // [1, 2, 3, 5, 8, 9]

        // EXAMPLE 2: Sort strings (natural order = alphabetical)
        List<String> words = List.of("banana", "apple", "cherry", "date");
        List<String> sortedWords = words.stream()
            .sorted()  // uses String's natural ordering (alphabetical)
            .toList();
        System.out.println("sorted() strings: " + sortedWords);
        // [apple, banana, cherry, date]

        // EXAMPLE 3: What happens with non-Comparable?
        // record Person(String name) {} // NOT Comparable
        // people.stream().sorted() -> ClassCastException at runtime!

        // =====================================================================
        // SIGNATURE 2: sorted(Comparator) - Custom Order
        // =====================================================================
        System.out.println("\n--- sorted(Comparator) - Custom Order ---");

        // EXAMPLE 4: Reverse order
        List<Integer> reversed = numbers.stream()
            .sorted(Comparator.reverseOrder())
            .toList();
        System.out.println("sorted(reverseOrder): " + reversed);
        // [9, 8, 5, 3, 2, 1]

        // EXAMPLE 5: Sort by property
        record Person(String name, int age) {}
        List<Person> people = List.of(
            new Person("Charlie", 35),
            new Person("Alice", 30),
            new Person("Bob", 25)
        );

        // Sort by age
        List<Person> byAge = people.stream()
            .sorted(Comparator.comparingInt(Person::age))
            .toList();
        System.out.println("sorted by age: " + byAge);
        // Bob(25), Alice(30), Charlie(35)

        // Sort by name
        List<Person> byName = people.stream()
            .sorted(Comparator.comparing(Person::name))
            .toList();
        System.out.println("sorted by name: " + byName);
        // Alice, Bob, Charlie

        // EXAMPLE 6: Sort by multiple criteria
        List<Person> people2 = List.of(
            new Person("Alice", 30),
            new Person("Bob", 30),
            new Person("Charlie", 25)
        );

        // Sort by age, then by name
        List<Person> multiSort = people2.stream()
            .sorted(Comparator
                .comparingInt(Person::age)
                .thenComparing(Person::name))
            .toList();
        System.out.println("sorted by age then name: " + multiSort);
        // Charlie(25), Alice(30), Bob(30)

        // EXAMPLE 7: Sort strings by length
        List<String> byLength = words.stream()
            .sorted(Comparator.comparingInt(String::length))
            .toList();
        System.out.println("sorted by length: " + byLength);

        // EXAMPLE 8: Reverse with Comparator
        List<String> reverseAlpha = words.stream()
            .sorted(Comparator.comparing(String::toString).reversed())
            .toList();
        System.out.println("sorted reversed: " + reverseAlpha);

        // EXAMPLE 9: Null-safe sorting
        List<String> withNull = new ArrayList<>(Arrays.asList("banana", null, "apple"));
        List<String> nullsFirst = withNull.stream()
            .sorted(Comparator.nullsFirst(Comparator.naturalOrder()))
            .toList();
        System.out.println("nullsFirst: " + nullsFirst);

        List<String> nullsLast = withNull.stream()
            .sorted(Comparator.nullsLast(Comparator.naturalOrder()))
            .toList();
        System.out.println("nullsLast: " + nullsLast);

        System.out.println();
    }

    // =====================================================================
    // PEEK - Debug/observe elements without modification
    // =====================================================================

    /**
     * PEEK SIGNATURE:
     *
     * Stream<T> peek(Consumer<? super T> action)
     *
     * TYPE: Stream<T> -> Stream<T> (same type, same elements)
     *
     * PARAMETER:
     *   - action: Consumer<T> - action to perform on each element
     *            (T) -> void
     *
     * BEHAVIOR:
     *   - Performs action on each element as it flows through
     *   - Does NOT modify the stream (elements pass through unchanged)
     *   - Primarily for DEBUGGING
     *
     * ===== WARNING =====
     *
     * peek() is mainly for debugging. Don't use it for:
     *   - Side effects in production code
     *   - Modifying elements (use map instead)
     *
     * peek() may not execute if stream short-circuits!
     */
    public static void demonstratePeek() {
        System.out.println("=== PEEK ===\n");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5);

        // EXAMPLE 1: Basic peek for debugging
        System.out.println("--- Debugging with peek ---");
        List<Integer> result = numbers.stream()
            .peek(n -> System.out.println("  Before filter: " + n))
            .filter(n -> n % 2 == 0)
            .peek(n -> System.out.println("  After filter: " + n))
            .map(n -> n * 10)
            .peek(n -> System.out.println("  After map: " + n))
            .toList();
        System.out.println("Result: " + result);

        // EXAMPLE 2: peek doesn't modify stream
        System.out.println("\n--- peek passes elements unchanged ---");
        List<String> words = List.of("hello", "world");
        List<String> peeked = words.stream()
            .peek(s -> s.toUpperCase())  // this does nothing useful!
            .toList();
        System.out.println("peek(toUpperCase): " + peeked);
        System.out.println("  Elements unchanged! Use map() to transform.");

        // EXAMPLE 3: peek with short-circuit (may not execute)
        System.out.println("\n--- peek with short-circuit ---");
        List<Integer> nums = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Optional<Integer> firstEven = nums.stream()
            .peek(n -> System.out.println("  Processing: " + n))
            .filter(n -> n % 2 == 0)
            .findFirst();  // short-circuits after finding first even
        System.out.println("findFirst even: " + firstEven);
        System.out.println("  Notice: only processed 1, 2 (stopped at first match)");

        // EXAMPLE 4: Multiple peeks for pipeline debugging
        System.out.println("\n--- Pipeline debugging ---");
        List.of("apple", "banana", "cherry").stream()
            .peek(s -> System.out.println("Original: " + s))
            .map(String::toUpperCase)
            .peek(s -> System.out.println("Upper: " + s))
            .filter(s -> s.startsWith("B"))
            .peek(s -> System.out.println("Filtered: " + s))
            .forEach(s -> System.out.println("Final: " + s));

        System.out.println();
    }

    // =====================================================================
    // CHAINING OPERATIONS - Putting it all together
    // =====================================================================

    public static void demonstrateChainingOperations() {
        System.out.println("=== CHAINING INTERMEDIATE OPERATIONS ===\n");

        record Employee(String name, String dept, int salary) {}

        List<Employee> employees = List.of(
            new Employee("Alice", "Engineering", 80000),
            new Employee("Bob", "Marketing", 60000),
            new Employee("Charlie", "Engineering", 90000),
            new Employee("Diana", "Marketing", 70000),
            new Employee("Eve", "Engineering", 85000),
            new Employee("Frank", "HR", 50000)
        );

        // Complex pipeline combining multiple operations
        // TYPE FLOW:
        //   Stream<Employee> -> filter -> Stream<Employee>
        //                    -> map -> Stream<Integer>
        //                    -> sorted -> Stream<Integer>
        //                    -> distinct -> Stream<Integer>
        //                    -> limit -> Stream<Integer>
        //                    -> toList -> List<Integer>
        List<Integer> result = employees.stream()
            .filter(e -> e.salary() > 55000)           // keep salary > 55k
            .map(Employee::salary)                     // extract salary
            .sorted(Comparator.reverseOrder())         // sort descending
            .distinct()                                // remove duplicates
            .limit(3)                                  // top 3
            .toList();

        System.out.println("Top 3 distinct salaries > 55k (descending):");
        System.out.println(result);

        // Another example: Get unique departments, sorted
        List<String> departments = employees.stream()
            .map(Employee::dept)      // Employee -> String
            .distinct()               // unique departments
            .sorted()                 // alphabetical
            .toList();
        System.out.println("\nUnique departments sorted: " + departments);

        // Combining flatMap with other operations
        List<List<Integer>> nested = List.of(
            List.of(3, 1, 4),
            List.of(1, 5, 9),
            List.of(2, 6, 5)
        );

        List<Integer> processed = nested.stream()
            .flatMap(Collection::stream)  // flatten
            .distinct()                   // unique
            .sorted()                     // sort
            .limit(5)                     // first 5
            .toList();
        System.out.println("\nFlattened, distinct, sorted, limited: " + processed);
    }
}
