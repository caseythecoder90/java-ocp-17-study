package ch10streams;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * The collect() Operation and Collectors
 *
 * collect() is a special type of reduction called a MUTABLE REDUCTION.
 * More efficient than regular reduce() because it uses the SAME mutable object
 * while accumulating (like StringBuilder or ArrayList) instead of creating
 * new immutable objects each time.
 *
 * ===== TWO SIGNATURES =====
 *
 * 1. <R> R collect(Supplier<R> supplier,
 *                  BiConsumer<R, ? super T> accumulator,
 *                  BiConsumer<R, R> combiner)
 *
 * 2. <R, A> R collect(Collector<? super T, A, R> collector)
 *
 * ===== SIGNATURE 1: DETAILED TYPE BREAKDOWN =====
 *
 * <R> R collect(Supplier<R> supplier,
 *               BiConsumer<R, ? super T> accumulator,
 *               BiConsumer<R, R> combiner)
 *
 * TYPE PARAMETERS:
 * - T = Stream element type (what's IN the stream)
 * - R = Result container type (what you're collecting INTO)
 *
 * PARAMETER BREAKDOWN:
 *
 * supplier: Supplier<R>              () -> R
 *   - Creates a NEW empty mutable container
 *   - Called once (or once per thread in parallel)
 *   - Example: ArrayList::new creates () -> new ArrayList<>()
 *
 * accumulator: BiConsumer<R, ? super T>    (R, T) -> void
 *   - First param (R): the CONTAINER (result so far)
 *   - Second param (T): the ELEMENT from stream
 *   - ADDS element TO container (mutates container!)
 *   - Example: ArrayList::add becomes (list, elem) -> list.add(elem)
 *
 * combiner: BiConsumer<R, R>         (R, R) -> void
 *   - BOTH params are containers (R)
 *   - Merges second container INTO first (for parallel streams)
 *   - Example: ArrayList::addAll becomes (list1, list2) -> list1.addAll(list2)
 *
 * ===== MEMORY AID FOR 3-ARG COLLECT =====
 *
 * Stream<T> ---collect---> R
 *
 * supplier:    () -> R           "Create empty container"
 * accumulator: (R, T) -> void    "Add element to container"
 *                 ^  ^
 *                 |  |
 *           container element
 * combiner:    (R, R) -> void    "Merge two containers"
 *
 * ===== SIGNATURE 2: COLLECTOR VERSION =====
 *
 * <R, A> R collect(Collector<? super T, A, R> collector)
 *
 * TYPE PARAMETERS:
 * - T = Stream element type
 * - A = Accumulator type (internal, often hidden with ?)
 * - R = Result type
 *
 * Uses predefined Collectors from java.util.stream.Collectors - much more
 * convenient for common operations!
 *
 * ===== COMPARISON: REDUCE VS COLLECT =====
 *
 * | Aspect         | reduce()                      | collect()                    |
 * |----------------|-------------------------------|------------------------------|
 * | Container      | Immutable (creates new each)  | Mutable (reuses same)        |
 * | Performance    | Creates many objects          | More efficient               |
 * | accumulator    | BiFunction (U, T) -> U        | BiConsumer (R, T) -> void    |
 * | Return type    | Returns new value             | Mutates container in place   |
 * | Use case       | Primitives, immutable results | Collections, StringBuilder   |
 *
 * ===== EXAM TIP: ACCUMULATOR PARAMETER ORDER =====
 *
 * ALWAYS: (container, element) - container comes FIRST!
 *
 * - collect:  BiConsumer<R, T>   (container, element) -> void
 * - reduce:   BiFunction<U, T, U> (result, element) -> newResult
 *
 * The container/result is always the FIRST parameter, stream element is SECOND.
 */
public class CollectOperation {

    public static void main(String[] args) {
        demonstrateBasicCollect();
        demonstrateToListSetCollection();
        demonstrateJoining();
        demonstrateCountingSummingAveraging();
        demonstrateMinByMaxBy();
        demonstrateMappingFiltering();
        demonstrateToMap();
        demonstrateGroupingBy();
        demonstratePartitioningBy();
        demonstrateAdvancedCollectors();
    }

    // =====================================================================
    // BASIC COLLECT WITH SUPPLIER, ACCUMULATOR, COMBINER
    // =====================================================================

    public static void demonstrateBasicCollect() {
        System.out.println("=== BASIC COLLECT (3-arg signature) ===\n");

        // Signature: <R> R collect(Supplier<R> supplier,
        //                         BiConsumer<R, ? super T> accumulator,
        //                         BiConsumer<R, R> combiner)

        Stream<String> stream = Stream.of("w", "o", "l", "f");

        // Collect into a TreeSet
        TreeSet<String> set = stream.collect(
            TreeSet::new,              // supplier: create the container
            TreeSet::add,              // accumulator: add element to container
            TreeSet::addAll            // combiner: merge two containers (parallel)
        );
        System.out.println("Collected to TreeSet: " + set);

        Stream<String> meExample = Stream.of("AWS", "Java", "Go");
        TreeSet<String> mySet = meExample.collect(
                () -> new TreeSet<String>(),
                (treeSet, str) -> treeSet.add(str),
                (treeSet1, treeSet2) -> treeSet1.addAll(treeSet2)
        );

        // above shows what is actually happening even tho we should use method references in practice

        // Collect into StringBuilder
        StringBuilder sb = Stream.of("a", "b", "c").collect(
            StringBuilder::new,        // supplier
            StringBuilder::append,     // accumulator
            StringBuilder::append      // combiner
        );
        System.out.println("Collected to StringBuilder: " + sb);

        // Collect into ArrayList
        ArrayList<String> list = Stream.of("x", "y", "z").collect(
            ArrayList::new,            // supplier: () -> new ArrayList<>()
            ArrayList::add,            // accumulator: (list, elem) -> list.add(elem)
            ArrayList::addAll          // combiner: (list1, list2) -> list1.addAll(list2)
        );
        System.out.println("Collected to ArrayList: " + list);

        // WHY COMBINER? For parallel streams, each thread has its own container
        // Combiner merges them at the end
    }

    // =====================================================================
    // toList(), toSet(), toCollection()
    // =====================================================================

    /**
     * COLLECTION COLLECTORS - SIGNATURES:
     *
     * 1. static <T> Collector<T,?,List<T>> toList()
     *    - T = stream element type
     *    - Returns: Collector that accumulates into a List<T>
     *    - No guarantees on List type, mutability, or thread-safety
     *
     * 2. static <T> Collector<T,?,Set<T>> toSet()
     *    - T = stream element type
     *    - Returns: Collector that accumulates into a Set<T>
     *    - Removes duplicates (uses equals/hashCode)
     *
     * 3. static <T> Collector<T,?,List<T>> toUnmodifiableList()
     *    - T = stream element type
     *    - Returns: Collector that accumulates into an UNMODIFIABLE List<T>
     *    - Throws NullPointerException if any element is null
     *
     * 4. static <T> Collector<T,?,Set<T>> toUnmodifiableSet()
     *    - T = stream element type
     *    - Returns: Collector that accumulates into an UNMODIFIABLE Set<T>
     *    - Throws NullPointerException if any element is null
     *
     * 5. static <T,C extends Collection<T>> Collector<T,?,C> toCollection(Supplier<C> collectionFactory)
     *    - T = stream element type
     *    - C = specific Collection type you want (TreeSet, LinkedList, etc.)
     *    - collectionFactory: Supplier<C> - creates empty collection, e.g., TreeSet::new
     *    - Returns: Collector that accumulates into your chosen Collection type
     *
     * 6. Stream.toList() (Java 16+) - NOT a Collector, but a terminal operation
     *    - Returns UNMODIFIABLE List<T>
     *    - Shorthand for .collect(Collectors.toUnmodifiableList())
     *
     * ===== TYPE PARAMETER MEMORY AID =====
     *
     * Collector<T, ?, R>
     *   T = what goes IN (stream element type)
     *   ? = internal accumulator (hidden)
     *   R = what comes OUT (result type)
     *
     * toList():       Collector<T, ?, List<T>>     Stream<String> -> List<String>
     * toSet():        Collector<T, ?, Set<T>>      Stream<String> -> Set<String>
     * toCollection(): Collector<T, ?, C>           Stream<String> -> TreeSet<String>
     */
    public static void demonstrateToListSetCollection() {
        System.out.println("\n=== toList(), toSet(), toCollection() ===\n");

        List<String> source = List.of("a", "b", "c", "a", "b");

        // ----- Collectors.toList() -----
        // Returns a Collector that accumulates into a new List
        // No guarantees on type, mutability, or thread-safety
        List<String> list = source.stream()
            .collect(Collectors.toList());
        System.out.println("toList(): " + list);

        // ----- Collectors.toSet() -----
        // Returns a Collector that accumulates into a new Set (removes duplicates)
        Set<String> set = source.stream()
            .collect(Collectors.toSet());
        System.out.println("toSet(): " + set);

        // ----- Collectors.toUnmodifiableList() -----
        // Returns unmodifiable List (Java 10+)
        List<String> unmodList = source.stream()
            .collect(Collectors.toUnmodifiableList());
        System.out.println("toUnmodifiableList(): " + unmodList);
        // unmodList.add("x");  // UnsupportedOperationException!

        // ----- Collectors.toUnmodifiableSet() -----
        Set<String> unmodSet = source.stream()
            .collect(Collectors.toUnmodifiableSet());
        System.out.println("toUnmodifiableSet(): " + unmodSet);

        // ----- Collectors.toCollection(Supplier) -----
        // Collect into specific collection type
        TreeSet<String> treeSet = source.stream()
            .collect(Collectors.toCollection(TreeSet::new));
        System.out.println("toCollection(TreeSet::new): " + treeSet);

        LinkedList<String> linkedList = source.stream()
            .collect(Collectors.toCollection(LinkedList::new));
        System.out.println("toCollection(LinkedList::new): " + linkedList);

        // ----- Stream.toList() (Java 16+) -----
        // Shorthand that returns unmodifiable list
        List<String> streamToList = source.stream().toList();
        System.out.println("stream().toList(): " + streamToList);
    }

    // =====================================================================
    // joining()
    // =====================================================================

    /**
     * JOINING COLLECTORS - SIGNATURES:
     *
     * 1. static Collector<CharSequence,?,String> joining()
     *    - Input: CharSequence (String, StringBuilder, etc.)
     *    - Returns: Collector that concatenates all elements into a single String
     *    - No delimiter between elements
     *    - Example: ["a","b","c"] -> "abc"
     *
     * 2. static Collector<CharSequence,?,String> joining(CharSequence delimiter)
     *    - Input: CharSequence
     *    - delimiter: inserted BETWEEN elements (not at start/end)
     *    - Returns: Collector that joins with delimiter
     *    - Example: joining(", ") on ["a","b","c"] -> "a, b, c"
     *
     * 3. static Collector<CharSequence,?,String> joining(CharSequence delimiter,
     *                                                     CharSequence prefix,
     *                                                     CharSequence suffix)
     *    - Input: CharSequence
     *    - delimiter: inserted between elements
     *    - prefix: added at the START of result
     *    - suffix: added at the END of result
     *    - Returns: Collector that joins with delimiter and wraps with prefix/suffix
     *    - Example: joining(", ", "[", "]") on ["a","b","c"] -> "[a, b, c]"
     *
     * ===== IMPORTANT =====
     *
     * joining() ONLY works with CharSequence streams (String, StringBuilder, etc.)
     * For non-String streams, you must MAP to String first:
     *
     *   Stream.of(1, 2, 3)
     *       .map(String::valueOf)      // Integer -> String
     *       .collect(joining("-"));    // "1-2-3"
     *
     * ===== TYPE BREAKDOWN =====
     *
     * Collector<CharSequence, ?, String>
     *           ^              ^  ^
     *           |              |  |
     *      input type    hidden  result type
     *   (what stream has)      (always String)
     */
    public static void demonstrateJoining() {
        System.out.println("\n=== joining() ===\n");

        List<String> words = List.of("Hello", "World", "Java");

        // ----- joining() - no delimiter -----
        String noDelim = words.stream()
            .collect(Collectors.joining());
        System.out.println("joining(): " + noDelim);  // HelloWorldJava

        // ----- joining(delimiter) -----
        String withDelim = words.stream()
            .collect(Collectors.joining(", "));
        System.out.println("joining(\", \"): " + withDelim);  // Hello, World, Java

        // ----- joining(delimiter, prefix, suffix) -----
        String withPrefixSuffix = words.stream()
            .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("joining(\", \", \"[\", \"]\"): " + withPrefixSuffix);  // [Hello, World, Java]

        // NOTE: Works with CharSequence (String, StringBuilder, etc.)
        // Must map non-strings first!
        String numbers = Stream.of(1, 2, 3)
            .map(String::valueOf)
            .collect(Collectors.joining("-"));
        System.out.println("Numbers joined: " + numbers);  // 1-2-3
    }

    // =====================================================================
    // counting(), summingXxx(), averagingXxx(), summarizingXxx()
    // =====================================================================

    /**
     * NUMERIC COLLECTORS - SIGNATURES:
     *
     * ===== COUNTING =====
     *
     * static <T> Collector<T,?,Long> counting()
     *   - T = any stream element type (elements are just counted, not used)
     *   - Returns: Collector that counts elements
     *   - Result type: Long (always Long, not int!)
     *   - Example: Stream.of("a","b","c").collect(counting()) -> 3L
     *
     * ===== SUMMING =====
     *
     * static <T> Collector<T,?,Integer> summingInt(ToIntFunction<? super T> mapper)
     *   - T = stream element type
     *   - mapper: ToIntFunction<T> - extracts int value from each element
     *            (T) -> int
     *   - Returns: Collector that sums the int values
     *   - Result type: Integer
     *   - Example: summingInt(String::length) on ["ab","cde"] -> 5
     *
     * static <T> Collector<T,?,Long> summingLong(ToLongFunction<? super T> mapper)
     *   - Same as summingInt but mapper returns long, result is Long
     *
     * static <T> Collector<T,?,Double> summingDouble(ToDoubleFunction<? super T> mapper)
     *   - Same as summingInt but mapper returns double, result is Double
     *
     * ===== AVERAGING =====
     *
     * static <T> Collector<T,?,Double> averagingInt(ToIntFunction<? super T> mapper)
     *   - T = stream element type
     *   - mapper: ToIntFunction<T> - extracts int value from each element
     *   - Returns: Collector that calculates average
     *   - Result type: ALWAYS Double (even for averagingInt!)
     *   - Empty stream returns 0.0
     *   - Example: averagingInt(String::length) on ["ab","cdef"] -> 3.0
     *
     * static <T> Collector<T,?,Double> averagingLong(ToLongFunction<? super T> mapper)
     *   - Same pattern, mapper returns long, result is Double
     *
     * static <T> Collector<T,?,Double> averagingDouble(ToDoubleFunction<? super T> mapper)
     *   - Same pattern, mapper returns double, result is Double
     *
     * ===== SUMMARIZING =====
     *
     * static <T> Collector<T,?,IntSummaryStatistics> summarizingInt(ToIntFunction<? super T> mapper)
     *   - T = stream element type
     *   - mapper: ToIntFunction<T> - extracts int value from each element
     *   - Returns: IntSummaryStatistics with count, sum, min, max, average
     *   - Methods: getCount(), getSum(), getMin(), getMax(), getAverage()
     *
     * static <T> Collector<T,?,LongSummaryStatistics> summarizingLong(ToLongFunction<? super T> mapper)
     * static <T> Collector<T,?,DoubleSummaryStatistics> summarizingDouble(ToDoubleFunction<? super T> mapper)
     *
     * ===== MAPPER FUNCTION TYPES =====
     *
     * ToIntFunction<T>:    (T) -> int       T t -> int
     * ToLongFunction<T>:   (T) -> long      T t -> long
     * ToDoubleFunction<T>: (T) -> double    T t -> double
     *
     * Common mappers:
     *   String::length        (String) -> int
     *   s -> s.length()       (String) -> int
     *   n -> n                (Integer) -> int (unboxing)
     *   s -> s.length() * 1.5 (String) -> double
     *
     * ===== EXAM TIP =====
     *
     * Return types to memorize:
     * - counting()      -> Long (not int!)
     * - summingInt()    -> Integer
     * - summingLong()   -> Long
     * - summingDouble() -> Double
     * - averaging___()  -> ALWAYS Double (even averagingInt!)
     */
    public static void demonstrateCountingSummingAveraging() {
        System.out.println("\n=== counting(), summing, averaging, summarizing ===\n");

        List<String> words = List.of("apple", "banana", "cherry", "date");

        // ----- counting() -----
        Long count = words.stream()
            .collect(Collectors.counting());
        System.out.println("counting(): " + count);

        // ----- summingInt/Long/Double -----
        // Sum of applying function to each element
        Integer totalLength = words.stream()
            .collect(Collectors.summingInt(String::length));
        System.out.println("summingInt(length): " + totalLength);

        Long totalLengthLong = words.stream()
            .collect(Collectors.summingLong(String::length));
        System.out.println("summingLong(length): " + totalLengthLong);

        Double totalLengthDouble = words.stream()
            .collect(Collectors.summingDouble(String::length));
        System.out.println("summingDouble(length): " + totalLengthDouble);

        // ----- averagingInt/Long/Double -----
        // Average of applying function to each element (always returns Double!)
        Double avgLength = words.stream()
            .collect(Collectors.averagingInt(String::length));
        System.out.println("\naveragingInt(length): " + avgLength);

        Double avgLengthDouble = words.stream()
            .collect(Collectors.averagingDouble(s -> s.length() * 1.5));
        System.out.println("averagingDouble(length*1.5): " + avgLengthDouble);

        // ----- summarizingInt/Long/Double -----
        // Returns summary statistics (count, sum, min, max, average)
        IntSummaryStatistics stats = words.stream()
            .collect(Collectors.summarizingInt(String::length));
        System.out.println("\nsummarizingInt(length):");
        System.out.println("  count: " + stats.getCount());
        System.out.println("  sum: " + stats.getSum());
        System.out.println("  min: " + stats.getMin());
        System.out.println("  max: " + stats.getMax());
        System.out.println("  average: " + stats.getAverage());

        DoubleSummaryStatistics doubleSummaryStatistics = words.stream()
                .collect(Collectors.summarizingDouble(String::length));

        // My example

        record Person(String name, int age) {

            public static Person of(String name, int age) {
                return new Person(name, age);
            }

        }

        List<Person> people = new ArrayList<>(List.of(
                Person.of("Casey", 35),
                Person.of("Yasmim", 29)));

        IntSummaryStatistics summary = people.stream()
                .collect(Collectors.summarizingInt(Person::age));
    }

    // =====================================================================
    // minBy(), maxBy()
    // =====================================================================

    /**
     * MIN/MAX COLLECTORS - SIGNATURES:
     *
     * static <T> Collector<T,?,Optional<T>> minBy(Comparator<? super T> comparator)
     *   - T = stream element type
     *   - comparator: Comparator<T> - defines ordering to find minimum
     *   - Returns: Collector that finds minimum element according to comparator
     *   - Result type: Optional<T> (empty if stream is empty!)
     *
     * static <T> Collector<T,?,Optional<T>> maxBy(Comparator<? super T> comparator)
     *   - T = stream element type
     *   - comparator: Comparator<T> - defines ordering to find maximum
     *   - Returns: Collector that finds maximum element according to comparator
     *   - Result type: Optional<T> (empty if stream is empty!)
     *
     * ===== TYPE BREAKDOWN =====
     *
     * Collector<T, ?, Optional<T>>
     *           ^  ^  ^
     *           |  |  |
     *     input |  |  result (Optional because stream might be empty)
     *         hidden
     *
     * ===== COMMON COMPARATORS =====
     *
     * Comparator.naturalOrder()           - natural ordering (Comparable)
     * Comparator.reverseOrder()           - reverse natural ordering
     * Comparator.comparingInt(mapper)     - compare by int property
     * Comparator.comparingLong(mapper)    - compare by long property
     * Comparator.comparingDouble(mapper)  - compare by double property
     * Comparator.comparing(mapper)        - compare by Comparable property
     *
     * ===== EXAMPLES =====
     *
     * minBy(Comparator.naturalOrder())              - smallest by natural order
     * maxBy(Comparator.comparingInt(String::length)) - longest string
     * minBy(Comparator.comparing(Person::getAge))    - youngest person
     *
     * ===== IMPORTANT: TYPE WITNESS FOR naturalOrder() =====
     *
     * When using Comparator.naturalOrder() in complex generic contexts
     * (like teeing or groupingBy), you may need explicit type:
     *
     *   Comparator.<String>naturalOrder()   // explicit type
     *   Comparator.<Integer>naturalOrder()
     *
     * This helps the compiler resolve generic type inference.
     */
    public static void demonstrateMinByMaxBy() {
        System.out.println("\n=== minBy(), maxBy() ===\n");

        List<String> words = List.of("apple", "pie", "banana", "kiwi");

        // ----- minBy(Comparator) -----
        // Returns Optional<T> - empty if stream is empty
        Optional<String> shortest = words.stream()
            .collect(Collectors.minBy(Comparator.comparingInt(String::length)));
        System.out.println("minBy(length): " + shortest);

        // ----- maxBy(Comparator) -----
        Optional<String> longest = words.stream()
            .collect(Collectors.maxBy(Comparator.comparingInt(String::length)));
        System.out.println("maxBy(length): " + longest);

        // Alphabetically
        Optional<String> first = words.stream()
            .collect(Collectors.minBy(Comparator.<String>naturalOrder()));
        System.out.println("minBy(natural): " + first);  // apple

        Optional<String> last = words.stream()
            .collect(Collectors.maxBy(Comparator.<String>naturalOrder()));
        System.out.println("maxBy(natural): " + last);   // pie
    }

    // =====================================================================
    // mapping(), filtering(), flatMapping()
    // =====================================================================

    /// ADAPTING COLLECTORS - SIGNATURES:
    /// These collectors WRAP another collector (downstream) and transform elements
    /// before passing them to the downstream collector. Most useful inside groupingBy!
    /// ===== MAPPING =====
    /// static <T,U,A,R> Collector<T,?,R> mapping(Function<? super T,? extends U> mapper,
    ///                                            Collector<? super U,A,R> downstream)
    /// TYPE PARAMETERS:
    ///   - T = input element type (what the stream has)
    ///   - U = mapped element type (what mapper produces)
    ///   - A = accumulator type of downstream (hidden)
    ///   - R = result type of downstream
    /// PARAMETER BREAKDOWN:
    ///   - mapper: Function<T, U> - transforms each element before collecting
    ///            (T) -> U
    ///   - downstream: Collector<U, A, R> - collects the MAPPED elements
    /// Flow: Stream<T> --mapper--> Stream<U> --downstream--> R
    /// Example: mapping(String::length, toList())
    ///   - T = String (input)
    ///   - U = Integer (after mapping)
    ///   - R = List<Integer> (result)
    ///   - Stream<String> -> map to Integer -> collect to List<Integer>
    /// ===== FILTERING =====
    /// static <T,A,R> Collector<T,?,R> filtering(Predicate<? super T> predicate,
    ///                                            Collector<? super T,A,R> downstream)
    /// TYPE PARAMETERS:
    ///   - T = element type (same before and after filtering)
    ///   - A = accumulator type of downstream (hidden)
    ///   - R = result type of downstream
    /// PARAMETER BREAKDOWN:
    ///   - predicate: Predicate<T> - determines which elements pass through
    ///               (T) -> boolean
    ///   - downstream: Collector<T, A, R> - collects elements that pass the predicate
    /// Flow: Stream<T> --filter--> Stream<T> --downstream--> R
    /// Example: filtering(s -> s.length() > 5, toList())
    ///   - Only elements where length > 5 are collected
    /// ===== FLAT MAPPING =====
    /// static <T,U,A,R> Collector<T,?,R> flatMapping(Function<? super T,? extends Stream<? extends U>> mapper,
    ///                                                Collector<? super U,A,R> downstream)
    /// TYPE PARAMETERS:
    ///   - T = input element type
    ///   - U = element type INSIDE the streams produced by mapper
    ///   - A = accumulator type of downstream (hidden)
    ///   - R = result type of downstream
    /// PARAMETER BREAKDOWN:
    ///   - mapper: Function<T, Stream<U>> - transforms each element into a STREAM
    ///            (T) -> Stream<U>
    ///   - downstream: Collector<U, A, R> - collects all elements from all streams
    /// Flow: Stream<T> --mapper--> Stream<Stream<U>> --flatten--> Stream<U> --downstream--> R
    /// Example: flatMapping(List::stream, toList())
    ///   - T = List<String>
    ///   - U = String
    ///   - Each List becomes a Stream<String>, all flattened into one collection
    /// ===== WHY USE THESE? =====
    /// Most useful INSIDE groupingBy/partitioningBy to transform grouped values:
    ///   groupingBy(classifier, mapping(transformer, toList()))
    ///   groupingBy(classifier, filtering(predicate, toList()))
    /// Without these, you'd have to post-process the grouped results.
    public static void demonstrateMappingFiltering() {
        System.out.println("\n=== mapping(), filtering(), flatMapping() ===\n");

        List<String> words = List.of("apple", "banana", "cherry", "apricot");

        // ----- mapping(Function, downstream) -----
        // Applies function before collecting
        // Useful inside groupingBy to transform grouped values
        List<Integer> lengths = words.stream()
            .collect(Collectors.mapping(
                String::length,           // mapper
                Collectors.toList()       // downstream collector
            ));
        System.out.println("mapping(length, toList): " + lengths);

        // ----- filtering(Predicate, downstream) -----
        // Filters before collecting
        // Useful inside groupingBy to filter grouped values
        List<String> longWords = words.stream()
            .collect(Collectors.filtering(
                s -> s.length() > 5,       // predicate
                Collectors.toList()        // downstream
            ));
        System.out.println("filtering(length>5, toList): " + longWords);

        // ----- flatMapping(Function, downstream) -----
        // Flat maps before collecting
        List<List<String>> nested = List.of(
            List.of("a", "b"),
            List.of("c", "d", "e")
        );
        List<String> flattened = nested.stream()
            .collect(Collectors.flatMapping(
                List::stream,              // Function that returns Stream
                Collectors.toList()
            ));
        System.out.println("flatMapping: " + flattened);

        // These are most useful INSIDE groupingBy - see groupingBy section
    }

    // =====================================================================
    // toMap() - IMPORTANT FOR EXAM!
    // =====================================================================

    /**
     * toMap() COLLECTORS - SIGNATURES:
     *
     * ===== SIGNATURE 1: Basic (2-arg) =====
     *
     * static <T,K,U> Collector<T,?,Map<K,U>> toMap(Function<? super T,? extends K> keyMapper,
     *                                               Function<? super T,? extends U> valueMapper)
     *
     * TYPE PARAMETERS:
     *   - T = stream element type
     *   - K = key type in resulting map
     *   - U = value type in resulting map
     *
     * PARAMETER BREAKDOWN:
     *   - keyMapper: Function<T, K> - extracts key from each element
     *               (T) -> K
     *   - valueMapper: Function<T, U> - extracts value from each element
     *                 (T) -> U
     *
     * THROWS: IllegalStateException if duplicate keys!
     *
     * Example: toMap(s -> s, String::length)
     *   - T = String, K = String, U = Integer
     *   - "apple" -> key="apple", value=5
     *
     * ===== SIGNATURE 2: With Merge Function (3-arg) =====
     *
     * static <T,K,U> Collector<T,?,Map<K,U>> toMap(Function<? super T,? extends K> keyMapper,
     *                                               Function<? super T,? extends U> valueMapper,
     *                                               BinaryOperator<U> mergeFunction)
     *
     * ADDITIONAL PARAMETER:
     *   - mergeFunction: BinaryOperator<U> - resolves duplicate keys
     *                   (U existingValue, U newValue) -> U resultValue
     *
     * Common merge functions:
     *   (v1, v2) -> v1     // keep FIRST (existing) value
     *   (v1, v2) -> v2     // keep LAST (new) value
     *   (v1, v2) -> v1+v2  // combine values
     *
     * ===== SIGNATURE 3: With Map Supplier (4-arg) =====
     *
     * static <T,K,U,M extends Map<K,U>> Collector<T,?,M> toMap(
     *     Function<? super T,? extends K> keyMapper,
     *     Function<? super T,? extends U> valueMapper,
     *     BinaryOperator<U> mergeFunction,
     *     Supplier<M> mapFactory)
     *
     * ADDITIONAL PARAMETER:
     *   - mapFactory: Supplier<M> - creates the specific Map implementation
     *                () -> M
     *
     * Example: toMap(keyFn, valueFn, mergeFn, TreeMap::new)
     *   - Result will be a TreeMap (sorted keys)
     *
     * ===== SIGNATURE 4: Unmodifiable =====
     *
     * static <T,K,U> Collector<T,?,Map<K,U>> toUnmodifiableMap(
     *     Function<? super T,? extends K> keyMapper,
     *     Function<? super T,? extends U> valueMapper)
     *
     * static <T,K,U> Collector<T,?,Map<K,U>> toUnmodifiableMap(
     *     Function<? super T,? extends K> keyMapper,
     *     Function<? super T,? extends U> valueMapper,
     *     BinaryOperator<U> mergeFunction)
     *
     * Returns unmodifiable Map. Throws on null keys/values and duplicate keys
     * (unless merge function provided).
     *
     * ===== MEMORY AID =====
     *
     * Stream<T> --toMap--> Map<K, U>
     *
     * keyMapper:   (T) -> K     "How to get key from element"
     * valueMapper: (T) -> U     "How to get value from element"
     * mergeFunction: (U, U) -> U "What to do when keys collide"
     *                 ^  ^
     *                 |  |
     *            existing new
     *
     * ===== EXAM TRAP =====
     *
     * 2-arg toMap THROWS on duplicate keys!
     * Always ask: "Can my keyMapper produce duplicate keys?"
     * If yes, use 3-arg version with merge function.
     *
     * Function.identity() is useful for "element itself as value":
     *   toMap(String::toUpperCase, Function.identity())
     */
    public static void demonstrateToMap() {
        System.out.println("\n=== toMap() - IMPORTANT! ===\n");

        List<String> words = List.of("apple", "banana", "cherry");

        // ----- toMap(keyMapper, valueMapper) -----
        // Creates Map where key and value come from functions applied to each element
        // THROWS IllegalStateException if duplicate keys!

        Map<String, Integer> wordToLength = words.stream()
            .collect(Collectors.toMap(
                s -> s,                    // keyMapper: element becomes key
                String::length             // valueMapper: length becomes value
            ));
        System.out.println("toMap(word, length): " + wordToLength);

        // Key is first char, value is the word
        Map<Character, String> firstCharToWord = words.stream()
            .collect(Collectors.toMap(
                s -> s.charAt(0),          // keyMapper
                s -> s                     // valueMapper
            ));
        System.out.println("toMap(firstChar, word): " + firstCharToWord);

        // ----- DUPLICATE KEY PROBLEM -----
        System.out.println("\n--- Handling duplicate keys ---");
        List<String> wordsWithDupes = List.of("apple", "apricot", "banana");

        // This would throw IllegalStateException - both 'a' words have same first char!
        // Map<Character, String> bad = wordsWithDupes.stream()
        //     .collect(Collectors.toMap(s -> s.charAt(0), s -> s));

        // ----- toMap(keyMapper, valueMapper, mergeFunction) -----
        // mergeFunction handles duplicate keys
        Map<Character, String> merged = wordsWithDupes.stream()
            .collect(Collectors.toMap(
                s -> s.charAt(0),          // keyMapper
                s -> s,                    // valueMapper
                (v1, v2) -> v1 + "," + v2  // mergeFunction: what to do with duplicates
            ));
        System.out.println("toMap with merge (concat): " + merged);

        // Keep first value on duplicate
        Map<Character, String> keepFirst = wordsWithDupes.stream()
            .collect(Collectors.toMap(
                s -> s.charAt(0),
                s -> s,
                (v1, v2) -> v1             // keep first
            ));
        System.out.println("toMap with merge (keepFirst): " + keepFirst);

        // Keep last value on duplicate
        Map<Character, String> keepLast = wordsWithDupes.stream()
            .collect(Collectors.toMap(
                s -> s.charAt(0),
                s -> s,
                (v1, v2) -> v2             // keep last
            ));
        System.out.println("toMap with merge (keepLast): " + keepLast);

        // ----- toMap(keyMapper, valueMapper, mergeFunction, mapSupplier) -----
        // Specify the Map implementation
        TreeMap<Character, String> treeMap = wordsWithDupes.stream()
            .collect(Collectors.toMap(
                s -> s.charAt(0),
                s -> s,
                (v1, v2) -> v1,
                TreeMap::new               // mapSupplier
            ));
        System.out.println("toMap to TreeMap: " + treeMap);

        // ----- toUnmodifiableMap -----
        Map<String, Integer> unmodifiable = words.stream()
            .collect(Collectors.toUnmodifiableMap(
                s -> s,
                String::length
            ));
        System.out.println("\ntoUnmodifiableMap: " + unmodifiable);

        // ----- EXAM TIP: Function.identity() -----
        // Use when element itself is the value
        Map<String, String> identityMap = words.stream()
            .collect(Collectors.toMap(
                s -> s.toUpperCase(),      // key
                Function.identity()        // value = element itself
            ));
        System.out.println("Using Function.identity(): " + identityMap);
    }

    // =====================================================================
    // groupingBy() - VERY IMPORTANT FOR EXAM!
    // =====================================================================

    /**
     * groupingBy() COLLECTORS - SIGNATURES:
     *
     * ===== SIGNATURE 1: Basic (1-arg) =====
     *
     * static <T,K> Collector<T,?,Map<K,List<T>>> groupingBy(Function<? super T,? extends K> classifier)
     *
     * TYPE PARAMETERS:
     *   - T = stream element type
     *   - K = key type (what classifier returns)
     *
     * PARAMETER:
     *   - classifier: Function<T, K> - determines which group each element belongs to
     *                (T) -> K
     *
     * RESULT: Map<K, List<T>>
     *   - Key = classifier result
     *   - Value = List of elements that produced that key
     *
     * Example: groupingBy(String::length)
     *   - T = String, K = Integer
     *   - Groups strings by their length
     *   - Result: Map<Integer, List<String>>
     *
     * ===== SIGNATURE 2: With Downstream Collector (2-arg) =====
     *
     * static <T,K,A,D> Collector<T,?,Map<K,D>> groupingBy(
     *     Function<? super T,? extends K> classifier,
     *     Collector<? super T,A,D> downstream)
     *
     * TYPE PARAMETERS:
     *   - T = stream element type
     *   - K = key type (from classifier)
     *   - A = accumulator type of downstream (hidden)
     *   - D = result type of downstream (what each group becomes)
     *
     * PARAMETERS:
     *   - classifier: Function<T, K> - groups elements
     *   - downstream: Collector<T, A, D> - what to do with each group
     *
     * RESULT: Map<K, D>
     *   - Key = classifier result
     *   - Value = result of downstream collector for that group
     *
     * Example: groupingBy(String::length, counting())
     *   - T = String, K = Integer, D = Long
     *   - Groups by length, counts each group
     *   - Result: Map<Integer, Long>
     *
     * ===== SIGNATURE 3: With Map Factory (3-arg) =====
     *
     * static <T,K,D,A,M extends Map<K,D>> Collector<T,?,M> groupingBy(
     *     Function<? super T,? extends K> classifier,
     *     Supplier<M> mapFactory,
     *     Collector<? super T,A,D> downstream)
     *
     * ADDITIONAL PARAMETER:
     *   - mapFactory: Supplier<M> - creates specific Map implementation
     *                () -> M
     *
     * NOTE: mapFactory is the SECOND parameter (not third!)
     *
     * Example: groupingBy(classifier, TreeMap::new, toList())
     *   - Result will be a TreeMap (sorted keys)
     *
     * ===== DOWNSTREAM COLLECTOR EXAMPLES =====
     *
     * groupingBy(classifier, counting())           -> Map<K, Long>
     * groupingBy(classifier, summingInt(fn))       -> Map<K, Integer>
     * groupingBy(classifier, averagingInt(fn))     -> Map<K, Double>
     * groupingBy(classifier, toSet())              -> Map<K, Set<T>>
     * groupingBy(classifier, joining(", "))        -> Map<K, String>
     * groupingBy(classifier, maxBy(comparator))    -> Map<K, Optional<T>>
     * groupingBy(classifier, mapping(fn, toList()))-> Map<K, List<U>>
     * groupingBy(classifier, filtering(pred, toList())) -> Map<K, List<T>>
     * groupingBy(classifier, groupingBy(classifier2)) -> Map<K, Map<K2, List<T>>> (nested!)
     *
     * ===== MEMORY AID =====
     *
     * Stream<T> --groupingBy--> Map<K, D>
     *
     * classifier: (T) -> K        "What key does this element belong to?"
     * downstream: Collector<T, ?, D>  "What to do with elements in each group"
     *
     * Default downstream (1-arg version) is toList()
     *
     * ===== KEY BEHAVIOR =====
     *
     * Keys only exist if at least one element maps to them!
     * Unlike partitioningBy, groupingBy does NOT guarantee all possible keys exist.
     */
    public static void demonstrateGroupingBy() {
        System.out.println("\n=== groupingBy() - VERY IMPORTANT! ===\n");

        List<String> words = List.of("apple", "apricot", "banana", "cherry", "avocado", "blueberry");

        // =====================================================================
        // SIGNATURE 1: groupingBy(classifier)
        // Returns: Map<K, List<T>>
        // =====================================================================

        // EXAMPLE 1: Group by first character
        // TYPE ANALYSIS:
        //   Stream<String> -> groupingBy -> Map<Character, List<String>>
        //   T = String (stream element)
        //   K = Character (classifier returns char -> boxed to Character)
        //   Result = Map<K, List<T>> = Map<Character, List<String>>
        //
        // classifier: s -> s.charAt(0)
        //   Input: String
        //   Output: char (boxed to Character) = the KEY
        Map<Character, List<String>> byFirstChar = words.stream()
            .collect(Collectors.groupingBy(s -> s.charAt(0)));
        System.out.println("groupingBy(firstChar): " + byFirstChar);
        // {a=[apple, apricot, avocado], b=[banana, blueberry], c=[cherry]}

        // EXAMPLE 2: Group by length
        // TYPE ANALYSIS:
        //   T = String
        //   K = Integer (String::length returns int -> boxed to Integer)
        //   Result = Map<Integer, List<String>>
        Map<Integer, List<String>> byLength = words.stream()
            .collect(Collectors.groupingBy(String::length));
        System.out.println("groupingBy(length): " + byLength);

        // =====================================================================
        // SIGNATURE 2: groupingBy(classifier, downstream)
        // Returns: Map<K, D>  where D = downstream result type
        // =====================================================================

        // EXAMPLE 3: Group and COUNT
        // TYPE ANALYSIS:
        //   T = String
        //   K = Character (from classifier)
        //   D = Long (counting() returns Collector<T,?,Long>)
        //   Result = Map<Character, Long>
        //
        // HOW IT WORKS:
        //   1. Elements grouped by first char: {a=[apple,apricot,avocado], b=[...], c=[...]}
        //   2. Each group passed to counting() collector
        //   3. counting() returns count of elements in that group
        Map<Character, Long> countByFirstChar = words.stream()
            .collect(Collectors.groupingBy(
                s -> s.charAt(0),          // classifier: (String) -> Character
                Collectors.counting()      // downstream: Collector<String,?,Long>
            ));
        System.out.println("\ngroupingBy + counting: " + countByFirstChar);
        // {a=3, b=2, c=1}

        // EXAMPLE 4: Group and collect to SET (instead of default List)
        // TYPE ANALYSIS:
        //   T = String, K = Character
        //   D = Set<String> (toSet() returns Collector<T,?,Set<T>>)
        //   Result = Map<Character, Set<String>>
        Map<Character, Set<String>> setByFirstChar = words.stream()
            .collect(Collectors.groupingBy(
                s -> s.charAt(0),
                Collectors.toSet()
            ));
        System.out.println("groupingBy + toSet: " + setByFirstChar);

        // EXAMPLE 5: Group and JOIN strings
        // TYPE ANALYSIS:
        //   T = String, K = Integer (length)
        //   D = String (joining() returns Collector<CharSequence,?,String>)
        //   Result = Map<Integer, String>
        Map<Integer, String> joinedByLength = words.stream()
            .collect(Collectors.groupingBy(
                String::length,
                Collectors.joining(", ", "{{", "}}")
            ));
        System.out.println("groupingBy + joining: " + joinedByLength);
        //  {5={{apple}}, 6={{banana, cherry}}, 7={{apricot, avocado}}, 9={{blueberry}}}

        // EXAMPLE 6: Group and SUM
        // TYPE ANALYSIS:
        //   T = String, K = Character
        //   D = Integer (summingInt returns Collector<T,?,Integer>)
        //   summingInt parameter: ToIntFunction<String> = String::length
        //   Result = Map<Character, Integer>
        //
        // For group 'a': apple(5) + apricot(7) + avocado(7) = 19
        Map<Character, Integer> sumLengthByFirstChar = words.stream()
            .collect(Collectors.groupingBy(
                s -> s.charAt(0),
                Collectors.summingInt(String::length)
            ));
        System.out.println("groupingBy + summingInt: " + sumLengthByFirstChar);

        // EXAMPLE 7: Group and AVERAGE
        // TYPE ANALYSIS:
        //   T = String, K = Character
        //   D = Double (averagingInt ALWAYS returns Double!)
        //   Result = Map<Character, Double>
        Map<Character, Double> avgLengthByFirstChar = words.stream()
            .collect(Collectors.groupingBy(
                s -> s.charAt(0),
                Collectors.averagingInt(String::length)
            ));
        System.out.println("groupingBy + averagingInt: " + avgLengthByFirstChar);

        // EXAMPLE 8: Group and find MAX
        // TYPE ANALYSIS:
        //   T = String, K = Character
        //   D = Optional<String> (maxBy returns Collector<T,?,Optional<T>>)
        //   Result = Map<Character, Optional<String>>
        //
        // Note: Result is Optional because a group COULD be empty
        Map<Character, Optional<String>> longestByFirstChar = words.stream()
            .collect(Collectors.groupingBy(
                s -> s.charAt(0),
                Collectors.maxBy(Comparator.comparingInt(String::length))
            ));
        System.out.println("groupingBy + maxBy: " + longestByFirstChar);

        // =====================================================================
        // SIGNATURE 3: groupingBy(classifier, mapFactory, downstream)
        // Returns: M extends Map<K, D>
        // NOTE: mapFactory is SECOND parameter!
        // =====================================================================

        // EXAMPLE 9: Use TreeMap for sorted keys
        // TYPE ANALYSIS:
        //   T = String, K = Character
        //   M = TreeMap<Character, List<String>>
        //   D = List<String>
        TreeMap<Character, List<String>> treeMapGroup = words.stream()
            .collect(Collectors.groupingBy(
                s -> s.charAt(0),          // classifier
                TreeMap::new,              // mapFactory: () -> TreeMap
                Collectors.toList()        // downstream
            ));
        System.out.println("\ngroupingBy to TreeMap: " + treeMapGroup);
        // Keys are sorted: {a=[...], b=[...], c=[...]}

        // =====================================================================
        // NESTED GROUPING - groupingBy inside groupingBy
        // =====================================================================
        System.out.println("\n--- Nested groupingBy ---");

        // EXAMPLE 10: Two-level grouping
        // TYPE ANALYSIS:
        //   Outer: groupingBy(s -> s.charAt(0), innerCollector)
        //     T = String, K1 = Character
        //   Inner: groupingBy(String::length)
        //     T = String, K2 = Integer, D = List<String>
        //   Inner result: Map<Integer, List<String>>
        //   Outer result: Map<Character, Map<Integer, List<String>>>
        //
        // STEP BY STEP:
        //   1. Group by first char: a=[apple,apricot,avocado], b=[banana,blueberry], c=[cherry]
        //   2. For each group, group again by length:
        //      a -> {5=[apple], 7=[apricot,avocado]}
        //      b -> {6=[banana], 9=[blueberry]}
        //      c -> {6=[cherry]}
        Map<Character, Map<Integer, List<String>>> nestedGroup = words.stream()
            .collect(Collectors.groupingBy(
                s -> s.charAt(0),                      // outer classifier
                Collectors.groupingBy(String::length) // inner groupingBy as downstream
            ));
        System.out.println("Nested grouping: " + nestedGroup);

        // =====================================================================
        // mapping() INSIDE groupingBy - transform elements within groups
        // =====================================================================
        System.out.println("\n--- mapping inside groupingBy ---");

        // EXAMPLE 11: Transform grouped elements
        // TYPE ANALYSIS:
        //   groupingBy: T = String, K = Integer (length)
        //   mapping: transforms String -> String (toUpperCase)
        //   downstream of mapping: toList() -> List<String>
        //   Result: Map<Integer, List<String>>
        //
        // Flow: group by length -> within each group, map to uppercase -> collect to list
        Map<Integer, List<String>> upperByLength = words.stream()
            .collect(Collectors.groupingBy(
                String::length,
                Collectors.mapping(
                    String::toUpperCase,       // mapper: (String) -> String
                    Collectors.toList()        // downstream of mapping
                )
            ));
        System.out.println("groupingBy + mapping: " + upperByLength);

        // EXAMPLE 12: Extract different type from grouped elements
        // TYPE ANALYSIS:
        //   groupingBy: T = String, K = Character
        //   mapping: transforms String -> Integer (length)
        //   downstream of mapping: toList() -> List<Integer>
        //   Result: Map<Character, List<Integer>>
        //
        // Instead of Map<Char, List<String>>, we get Map<Char, List<Integer>>
        Map<Character, List<Integer>> lengthsByFirstChar = words.stream()
            .collect(Collectors.groupingBy(
                s -> s.charAt(0),
                Collectors.mapping(
                    String::length,            // mapper: (String) -> Integer
                    Collectors.toList()
                )
            ));
        System.out.println("groupingBy + mapping(length): " + lengthsByFirstChar);
        // {a=[5, 7, 7], b=[6, 9], c=[6]}

        // =====================================================================
        // filtering() INSIDE groupingBy - filter within groups
        // =====================================================================
        System.out.println("\n--- filtering inside groupingBy ---");

        // EXAMPLE 13: Filter elements within each group
        // TYPE ANALYSIS:
        //   groupingBy: T = String, K = Character
        //   filtering: keeps only strings where length > 6
        //   downstream of filtering: toList() -> List<String>
        //   Result: Map<Character, List<String>>
        //
        // IMPORTANT: Keys still exist even if their filtered list is empty!
        // This is different from filtering BEFORE grouping.
        Map<Character, List<String>> filteredGroups = words.stream()
            .collect(Collectors.groupingBy(
                s -> s.charAt(0),
                Collectors.filtering(
                    s -> s.length() > 6,       // predicate
                    Collectors.toList()
                )
            ));
        System.out.println("groupingBy + filtering(>6): " + filteredGroups);
        // {a=[apricot, avocado], b=[blueberry], c=[]}  <- 'c' key exists but empty!

        // =====================================================================
        // EXAM PRACTICE: TYPE ANALYSIS
        // =====================================================================
        System.out.println("\n--- EXAM PRACTICE: Analyzing groupingBy types ---");
        System.out.println("Given: List<String> words");
        System.out.println();
        System.out.println("Q1: words.stream().collect(groupingBy(String::length))");
        System.out.println("    classifier: String::length returns Integer");
        System.out.println("    Result: Map<Integer, List<String>>");
        System.out.println();
        System.out.println("Q2: words.stream().collect(groupingBy(s->s.charAt(0), counting()))");
        System.out.println("    classifier returns: Character");
        System.out.println("    downstream counting() returns: Long");
        System.out.println("    Result: Map<Character, Long>");
        System.out.println();
        System.out.println("Q3: words.stream().collect(groupingBy(String::length, ");
        System.out.println("        mapping(s->s.charAt(0), toSet())))");
        System.out.println("    classifier returns: Integer");
        System.out.println("    mapping transforms String->Character, collects to Set");
        System.out.println("    Result: Map<Integer, Set<Character>>");
    }

    // =====================================================================
    // partitioningBy() - IMPORTANT FOR EXAM!
    // =====================================================================

    /**
     * partitioningBy() COLLECTORS - SIGNATURES:
     *
     * partitioningBy is a SPECIAL CASE of groupingBy where the classifier
     * is a Predicate (returns boolean). Always results in exactly TWO groups.
     *
     * ===== SIGNATURE 1: Basic (1-arg) =====
     *
     * static <T> Collector<T,?,Map<Boolean,List<T>>> partitioningBy(Predicate<? super T> predicate)
     *
     * TYPE PARAMETERS:
     *   - T = stream element type
     *
     * PARAMETER:
     *   - predicate: Predicate<T> - test that determines partition
     *               (T) -> boolean
     *
     * RESULT: Map<Boolean, List<T>>
     *   - Key true = List of elements where predicate returned true
     *   - Key false = List of elements where predicate returned false
     *   - BOTH KEYS ALWAYS EXIST (even if empty!)
     *
     * Example: partitioningBy(s -> s.length() > 5)
     *   - T = String
     *   - Result: Map<Boolean, List<String>>
     *   - true -> ["banana", "cherry"], false -> ["apple", "pie"]
     *
     * ===== SIGNATURE 2: With Downstream Collector (2-arg) =====
     *
     * static <T,D,A> Collector<T,?,Map<Boolean,D>> partitioningBy(
     *     Predicate<? super T> predicate,
     *     Collector<? super T,A,D> downstream)
     *
     * TYPE PARAMETERS:
     *   - T = stream element type
     *   - A = accumulator type of downstream (hidden)
     *   - D = result type of downstream (what each partition becomes)
     *
     * PARAMETERS:
     *   - predicate: Predicate<T> - determines partition
     *   - downstream: Collector<T, A, D> - what to do with each partition
     *
     * RESULT: Map<Boolean, D>
     *   - Key = true/false
     *   - Value = result of downstream collector for that partition
     *
     * Example: partitioningBy(s -> s.length() > 5, counting())
     *   - T = String, D = Long
     *   - Result: Map<Boolean, Long>
     *   - {true=2, false=3}
     *
     * ===== KEY DIFFERENCE FROM groupingBy =====
     *
     * | Aspect         | groupingBy              | partitioningBy           |
     * |----------------|-------------------------|--------------------------|
     * | Classifier     | Function<T, K>          | Predicate<T> (boolean)   |
     * | Key type       | Any type K              | Always Boolean           |
     * | Number of keys | Variable (0 to many)    | Always exactly 2         |
     * | Missing groups | Key doesn't exist       | Key exists with empty list|
     *
     * ===== MEMORY AID =====
     *
     * Stream<T> --partitioningBy--> Map<Boolean, D>
     *
     * predicate:  (T) -> boolean   "Does this element pass the test?"
     * downstream: Collector<T, ?, D>  "What to do with each partition"
     *
     * Default downstream (1-arg version) is toList()
     *
     * ===== EXAM TRAP =====
     *
     * partitioningBy ALWAYS has both true AND false keys!
     *
     * List<String> allShort = List.of("a", "b");
     * Map<Boolean, List<String>> result = allShort.stream()
     *     .collect(partitioningBy(s -> s.length() > 10));
     *
     * result.get(true)  -> [] (empty list, but key EXISTS)
     * result.get(false) -> ["a", "b"]
     *
     * With groupingBy, the true key would NOT exist at all!
     */
    public static void demonstratePartitioningBy() {
        System.out.println("\n=== partitioningBy() - IMPORTANT! ===\n");

        List<String> words = List.of("apple", "pie", "banana", "kiwi", "cherry");

        // =====================================================================
        // SIGNATURE 1: partitioningBy(predicate)
        // Returns: Map<Boolean, List<T>>
        // ALWAYS has exactly TWO keys: true and false
        // =====================================================================

        // EXAMPLE 1: Basic partition
        // TYPE ANALYSIS:
        //   Stream<String> -> partitioningBy -> Map<Boolean, List<String>>
        //   T = String
        //   predicate: (String) -> boolean
        //   Result = Map<Boolean, List<T>> = Map<Boolean, List<String>>
        //
        // HOW IT WORKS:
        //   predicate: s -> s.length() > 4
        //   "apple"(5)  -> true
        //   "pie"(3)    -> false
        //   "banana"(6) -> true
        //   "kiwi"(4)   -> false
        //   "cherry"(6) -> true
        //   Result: {true=[apple,banana,cherry], false=[pie,kiwi]}
        Map<Boolean, List<String>> byLength5 = words.stream()
            .collect(Collectors.partitioningBy(s -> s.length() > 4));
        System.out.println("partitioningBy(length > 4):");
        System.out.println("  true: " + byLength5.get(true));   // [apple, banana, cherry]
        System.out.println("  false: " + byLength5.get(false)); // [pie, kiwi]

        // =====================================================================
        // SIGNATURE 2: partitioningBy(predicate, downstream)
        // Returns: Map<Boolean, D>  where D = downstream result type
        // =====================================================================

        // EXAMPLE 2: Partition and COUNT
        // TYPE ANALYSIS:
        //   T = String
        //   predicate: (String) -> boolean
        //   downstream: counting() returns Collector<T,?,Long>
        //   D = Long
        //   Result = Map<Boolean, Long>
        Map<Boolean, Long> countByLength = words.stream()
            .collect(Collectors.partitioningBy(
                s -> s.length() > 4,       // predicate: (String) -> boolean
                Collectors.counting()      // downstream: Collector<String,?,Long>
            ));
        System.out.println("\npartitioningBy + counting: " + countByLength);
        // {false=2, true=3}

        // EXAMPLE 3: Partition and JOIN
        // TYPE ANALYSIS:
        //   T = String
        //   D = String (joining() returns Collector<CharSequence,?,String>)
        //   Result = Map<Boolean, String>
        Map<Boolean, String> joinedByLength = words.stream()
            .collect(Collectors.partitioningBy(
                s -> s.length() > 4,
                Collectors.joining(", ")
            ));
        System.out.println("partitioningBy + joining: " + joinedByLength);
        // {false=pie, kiwi, true=apple, banana, cherry}

        // EXAMPLE 4: Partition and find MAX
        // TYPE ANALYSIS:
        //   T = String
        //   D = Optional<String> (maxBy returns Collector<T,?,Optional<T>>)
        //   Result = Map<Boolean, Optional<String>>
        Map<Boolean, Optional<String>> maxByLength = words.stream()
            .collect(Collectors.partitioningBy(
                s -> s.length() > 4,
                Collectors.maxBy(Comparator.<String>naturalOrder())
            ));
        System.out.println("partitioningBy + maxBy: " + maxByLength);
        // {false=Optional[pie], true=Optional[cherry]}

        // EXAMPLE 5: Partition and SUM lengths
        // TYPE ANALYSIS:
        //   T = String
        //   D = Integer (summingInt returns Collector<T,?,Integer>)
        //   Result = Map<Boolean, Integer>
        Map<Boolean, Integer> sumByPartition = words.stream()
            .collect(Collectors.partitioningBy(
                s -> s.length() > 4,
                Collectors.summingInt(String::length)
            ));
        System.out.println("partitioningBy + summingInt: " + sumByPartition);

        // =====================================================================
        // KEY DIFFERENCE: partitioningBy vs groupingBy
        // =====================================================================
        System.out.println("\n--- partitioningBy vs groupingBy: CRITICAL DIFFERENCE ---");

        List<String> allShort = List.of("a", "bb", "ccc");

        // PARTITIONING: ALWAYS has BOTH true and false keys!
        // Even when NO elements match the predicate
        // TYPE ANALYSIS:
        //   predicate: s.length() > 10 (none match!)
        //   Result still has BOTH keys
        Map<Boolean, List<String>> partitioned = allShort.stream()
            .collect(Collectors.partitioningBy(s -> s.length() > 10));
        System.out.println("partitioningBy (no elements match predicate):");
        System.out.println("  partitioned.get(true): " + partitioned.get(true));   // [] empty but EXISTS
        System.out.println("  partitioned.get(false): " + partitioned.get(false)); // [a, bb, ccc]
        System.out.println("  partitioned.containsKey(true): " + partitioned.containsKey(true)); // TRUE!

        // GROUPING BY BOOLEAN: Key does NOT exist if no elements map to it
        Map<Boolean, List<String>> grouped = allShort.stream()
            .collect(Collectors.groupingBy(s -> s.length() > 10));
        System.out.println("\ngroupingBy with same boolean expression:");
        System.out.println("  grouped.containsKey(true): " + grouped.containsKey(true));   // FALSE!
        System.out.println("  grouped.containsKey(false): " + grouped.containsKey(false)); // true
        System.out.println("  grouped.get(true): " + grouped.get(true)); // null - key doesn't exist!

        // =====================================================================
        // EXAM PRACTICE: TYPE ANALYSIS
        // =====================================================================
        System.out.println("\n--- EXAM PRACTICE: Analyzing partitioningBy types ---");
        System.out.println("Given: List<String> words");
        System.out.println();
        System.out.println("Q1: words.stream().collect(partitioningBy(s -> s.isEmpty()))");
        System.out.println("    predicate: returns boolean");
        System.out.println("    Result: Map<Boolean, List<String>>");
        System.out.println("    GUARANTEED: result.containsKey(true) && result.containsKey(false)");
        System.out.println();
        System.out.println("Q2: words.stream().collect(partitioningBy(s -> s.length()>3, counting()))");
        System.out.println("    predicate: returns boolean");
        System.out.println("    downstream counting() returns: Long");
        System.out.println("    Result: Map<Boolean, Long>");
        System.out.println();
        System.out.println("Q3: What's the difference between these?");
        System.out.println("    A: stream.collect(partitioningBy(pred))");
        System.out.println("    B: stream.collect(groupingBy(pred))");
        System.out.println("    Answer: A ALWAYS has true/false keys, B may be missing keys!");
    }

    // =====================================================================
    // ADVANCED COLLECTORS
    // =====================================================================

    /**
     * ADVANCED COLLECTORS - SIGNATURES:
     *
     * ===== REDUCING (Collector version of reduce()) =====
     *
     * 1. static <T> Collector<T,?,Optional<T>> reducing(BinaryOperator<T> op)
     *    - T = element type (same throughout)
     *    - op: BinaryOperator<T> - combines two elements: (T, T) -> T
     *    - Returns Optional<T> because stream might be empty
     *    - Example: reducing((s1, s2) -> s1 + s2) on strings
     *
     * 2. static <T> Collector<T,?,T> reducing(T identity, BinaryOperator<T> op)
     *    - T = element type
     *    - identity: starting value (returned if stream empty)
     *    - op: BinaryOperator<T> - combines elements
     *    - Returns T directly (not Optional)
     *    - Example: reducing("", String::concat)
     *
     * 3. static <T,U> Collector<T,?,U> reducing(U identity,
     *                                           Function<? super T,? extends U> mapper,
     *                                           BinaryOperator<U> op)
     *    - T = stream element type
     *    - U = result type (can differ from T!)
     *    - identity: starting value of type U
     *    - mapper: Function<T, U> - transforms element before reducing
     *    - op: BinaryOperator<U> - combines mapped values
     *    - Example: reducing(0, String::length, Integer::sum)
     *             Stream<String> -> map to Integer -> reduce to single Integer
     *
     * TYPE COMPARISON WITH Stream.reduce():
     *
     * | Collectors.reducing()           | Stream.reduce()                  |
     * |---------------------------------|----------------------------------|
     * | reducing(op)                    | reduce(op) -> Optional<T>        |
     * | reducing(identity, op)          | reduce(identity, op) -> T        |
     * | reducing(identity, mapper, op)  | reduce(identity, acc, combiner)  |
     *
     * ===== COLLECTING AND THEN =====
     *
     * static <T,A,R,RR> Collector<T,A,RR> collectingAndThen(
     *     Collector<T,A,R> downstream,
     *     Function<R,RR> finisher)
     *
     * TYPE PARAMETERS:
     *   - T = stream element type
     *   - A = accumulator type of downstream (hidden)
     *   - R = result type of downstream
     *   - RR = final result type after finisher
     *
     * PARAMETERS:
     *   - downstream: Collector<T, A, R> - the initial collector
     *   - finisher: Function<R, RR> - transforms the result
     *              (R) -> RR
     *
     * Flow: Stream<T> --downstream--> R --finisher--> RR
     *
     * Examples:
     *   collectingAndThen(toList(), Collections::unmodifiableList)
     *     - R = List<T>, RR = List<T> (but unmodifiable)
     *
     *   collectingAndThen(toList(), List::size)
     *     - R = List<T>, RR = Integer
     *
     *   collectingAndThen(maxBy(comparator), Optional::get)
     *     - R = Optional<T>, RR = T (careful: throws if empty!)
     *
     * ===== TEEING (Java 12+) =====
     *
     * static <T,R1,R2,R> Collector<T,?,R> teeing(
     *     Collector<? super T,?,R1> downstream1,
     *     Collector<? super T,?,R2> downstream2,
     *     BiFunction<? super R1,? super R2,R> merger)
     *
     * TYPE PARAMETERS:
     *   - T = stream element type
     *   - R1 = result type of first collector
     *   - R2 = result type of second collector
     *   - R = final merged result type
     *
     * PARAMETERS:
     *   - downstream1: Collector<T, ?, R1> - first collector
     *   - downstream2: Collector<T, ?, R2> - second collector
     *   - merger: BiFunction<R1, R2, R> - combines both results
     *            (R1, R2) -> R
     *
     * Flow:
     *              +--> downstream1 --> R1 --+
     * Stream<T> --+                           +--> merger --> R
     *              +--> downstream2 --> R2 --+
     *
     * Both collectors process the SAME stream elements!
     *
     * Examples:
     *   teeing(summingInt(n->n), counting(), (sum, count) -> sum/count)
     *     - R1 = Integer, R2 = Long, R = calculated average
     *
     *   teeing(minBy(cmp), maxBy(cmp), (min, max) -> new Range(min, max))
     *     - R1 = Optional<T>, R2 = Optional<T>, R = Range
     *
     * ===== MEMORY AID FOR TEEING =====
     *
     * "Tee" like a T-junction in plumbing - one stream splits into two collectors
     *
     * teeing(collector1, collector2, merger)
     *        ^           ^           ^
     *        |           |           |
     *   produces R1  produces R2  (R1, R2) -> R
     */
    public static void demonstrateAdvancedCollectors() {
        System.out.println("\n=== ADVANCED COLLECTORS ===\n");

        List<String> words = List.of("apple", "banana", "cherry");

        // =====================================================================
        // REDUCING - Collector version of Stream.reduce()
        // =====================================================================
        System.out.println("--- reducing() ---");

        // SIGNATURE 1: reducing(BinaryOperator<T> op)
        // TYPE ANALYSIS:
        //   T = String
        //   op: BinaryOperator<String> = (String, String) -> String
        //   Result: Optional<String> (Optional because stream might be empty)
        Optional<String> concat = words.stream()
            .collect(Collectors.reducing((s1, s2) -> s1 + s2));
        System.out.println("reducing(concat): " + concat);

        // SIGNATURE 2: reducing(T identity, BinaryOperator<T> op)
        // TYPE ANALYSIS:
        //   T = String
        //   identity = "" (same type as T)
        //   op: BinaryOperator<String>
        //   Result: String (not Optional - identity used if empty)
        String concatWithIdentity = words.stream()
            .collect(Collectors.reducing("", (s1, s2) -> s1 + s2));
        System.out.println("reducing(\"\", concat): " + concatWithIdentity);

        // SIGNATURE 3: reducing(U identity, Function<T,U> mapper, BinaryOperator<U> op)
        // TYPE ANALYSIS:
        //   T = String (stream element)
        //   U = Integer (result type - DIFFERENT from T!)
        //   identity = 0 (type U)
        //   mapper: Function<String, Integer> = String::length
        //   op: BinaryOperator<Integer> = Integer::sum
        //   Result: Integer
        //
        // Flow: Stream<String> --map to Integer--> reduce Integers
        Integer totalLength = words.stream()
            .collect(Collectors.reducing(
                0,                         // identity: U (Integer)
                String::length,            // mapper: (T) -> U = (String) -> Integer
                Integer::sum               // op: (U, U) -> U = (Integer, Integer) -> Integer
            ));
        System.out.println("reducing(0, length, sum): " + totalLength);

        // =====================================================================
        // COLLECTING AND THEN - Post-process the collection result
        // =====================================================================
        System.out.println("\n--- collectingAndThen() ---");

        // SIGNATURE: collectingAndThen(Collector<T,A,R> downstream, Function<R,RR> finisher)
        // TYPE ANALYSIS:
        //   T = String (stream element)
        //   R = List<String> (downstream result)
        //   RR = List<String> (final result, but unmodifiable)
        //   finisher: Function<List<String>, List<String>>
        //
        // Flow: Stream<String> --toList()--> List<String> --unmodifiable--> List<String>
        List<String> unmodifiable = words.stream()
            .collect(Collectors.collectingAndThen(
                Collectors.toList(),           // downstream: Collector<String,?,List<String>>
                Collections::unmodifiableList  // finisher: (List<String>) -> List<String>
            ));
        System.out.println("collectingAndThen(toList, unmodifiable): " + unmodifiable);

        // EXAMPLE 2: Transform result type
        // TYPE ANALYSIS:
        //   R = List<String>
        //   RR = Integer (completely different type!)
        //   finisher: List::size is (List<String>) -> Integer
        Integer size = words.stream()
            .collect(Collectors.collectingAndThen(
                Collectors.toList(),
                List::size
            ));
        System.out.println("collectingAndThen(toList, size): " + size);

        // =====================================================================
        // TEEING (Java 12+) - Run TWO collectors and merge results
        // THIS IS FREQUENTLY TESTED ON EXAMS!
        // =====================================================================
        System.out.println("\n--- teeing() - IMPORTANT FOR EXAM! ---");

        /*
         * TEEING SIGNATURE:
         * static <T,R1,R2,R> Collector<T,?,R> teeing(
         *     Collector<? super T,?,R1> downstream1,
         *     Collector<? super T,?,R2> downstream2,
         *     BiFunction<? super R1,? super R2,R> merger)
         *
         * HOW IT WORKS:
         *                    +---> downstream1 ---> R1 ---+
         * Stream<T> elements |                            |---> merger(R1, R2) ---> R
         *    (same to both)  +---> downstream2 ---> R2 ---+
         *
         * Both collectors receive ALL the SAME elements from the stream.
         * After both finish, merger combines their results into final result R.
         */

        List<Integer> numbers = List.of(1, 2, 3, 4, 5);

        // ===== TEEING EXAMPLE 1: Sum and Average in one pass =====
        // TYPE ANALYSIS:
        //   T = Integer (stream element)
        //   downstream1: summingInt(n -> n) returns Collector<Integer,?,Integer>
        //     R1 = Integer (sum)
        //   downstream2: averagingInt(n -> n) returns Collector<Integer,?,Double>
        //     R2 = Double (average - ALWAYS Double!)
        //   merger: (Integer, Double) -> String
        //     R = String
        //
        // STEP BY STEP:
        //   Stream [1,2,3,4,5] flows to BOTH collectors
        //   downstream1 (summingInt): 1+2+3+4+5 = 15 (Integer)
        //   downstream2 (averagingInt): (1+2+3+4+5)/5 = 3.0 (Double)
        //   merger receives: (15, 3.0) -> "Sum: 15, Avg: 3.0"
        String sumAndAvg = numbers.stream()
            .collect(Collectors.teeing(
                Collectors.summingInt(n -> n),     // downstream1: Collector<Integer,?,Integer>
                Collectors.averagingInt(n -> n),   // downstream2: Collector<Integer,?,Double>
                (sum, avg) -> "Sum: " + sum + ", Avg: " + avg  // merger: (Integer, Double) -> String
            ));
        System.out.println("teeing(sum, avg): " + sumAndAvg);
        System.out.println("  R1 (sum) type: Integer = 15");
        System.out.println("  R2 (avg) type: Double = 3.0");
        System.out.println("  R (merged) type: String");

        // ===== TEEING EXAMPLE 2: Min and Max together =====
        // TYPE ANALYSIS:
        //   T = Integer
        //   downstream1: minBy(comparator) returns Collector<Integer,?,Optional<Integer>>
        //     R1 = Optional<Integer>
        //   downstream2: maxBy(comparator) returns Collector<Integer,?,Optional<Integer>>
        //     R2 = Optional<Integer>
        //   merger: (Optional<Integer>, Optional<Integer>) -> String
        //     R = String
        //
        // WHY Optional? Because minBy/maxBy return Optional in case stream is empty!
        String minMax = numbers.stream()
            .collect(Collectors.teeing(
                Collectors.minBy(Comparator.<Integer>naturalOrder()),  // R1 = Optional<Integer>
                Collectors.maxBy(Comparator.<Integer>naturalOrder()),  // R2 = Optional<Integer>
                (min, max) -> "Min: " + min.orElse(-1) + ", Max: " + max.orElse(-1)
            ));
        System.out.println("\nteeing(min, max): " + minMax);
        System.out.println("  R1 type: Optional<Integer>");
        System.out.println("  R2 type: Optional<Integer>");
        System.out.println("  merger params: (Optional<Integer>, Optional<Integer>)");

        // ===== TEEING EXAMPLE 3: Count and Collect simultaneously =====
        // TYPE ANALYSIS:
        //   T = String
        //   downstream1: counting() returns Collector<String,?,Long>
        //     R1 = Long
        //   downstream2: toList() returns Collector<String,?,List<String>>
        //     R2 = List<String>
        //   merger: (Long, List<String>) -> Map<String, Object>
        //     R = Map<String, Object>
        System.out.println("\n--- More teeing() examples with type analysis ---");

        var countAndList = words.stream()
            .collect(Collectors.teeing(
                Collectors.counting(),       // R1 = Long
                Collectors.toList(),         // R2 = List<String>
                (count, list) -> Map.of("count", count, "items", list)
            ));
        System.out.println("teeing(counting, toList): " + countAndList);
        System.out.println("  R1 = Long (from counting())");
        System.out.println("  R2 = List<String> (from toList())");

        // ===== TEEING EXAMPLE 4: Partition-like behavior =====
        // TYPE ANALYSIS:
        //   T = String
        //   downstream1: filtering(pred, toList()) returns Collector<String,?,List<String>>
        //     R1 = List<String> (short words)
        //   downstream2: filtering(pred, toList()) returns Collector<String,?,List<String>>
        //     R2 = List<String> (long words)
        //   merger: (List<String>, List<String>) -> Map<String, List<String>>
        var shortAndLong = words.stream()
            .collect(Collectors.teeing(
                Collectors.filtering(s -> s.length() <= 5, Collectors.toList()),  // R1
                Collectors.filtering(s -> s.length() > 5, Collectors.toList()),   // R2
                (shortWords, longWords) -> Map.of("short", shortWords, "long", longWords)
            ));
        System.out.println("\nteeing for partition-like behavior: " + shortAndLong);

        // ===== TEEING EXAMPLE 5: First and Last element =====
        // TYPE ANALYSIS:
        //   reducing((a,b) -> a) keeps first element by always returning first arg
        //   reducing((a,b) -> b) keeps last element by always returning second arg
        //   Both return Optional<String> (1-arg reducing returns Optional!)
        //   R1 = Optional<String>, R2 = Optional<String>
        var firstAndLast = words.stream()
            .collect(Collectors.teeing(
                Collectors.reducing((a, b) -> a),  // R1 = Optional<String> (first)
                Collectors.reducing((a, b) -> b),  // R2 = Optional<String> (last)
                (first, last) -> "First: " + first.orElse("") + ", Last: " + last.orElse("")
            ));
        System.out.println("\nteeing(first, last): " + firstAndLast);

        // ===== TEEING EXAMPLE 6: Calculate range (max - min) =====
        // TYPE ANALYSIS:
        //   R1 = Optional<Integer> (from minBy)
        //   R2 = Optional<Integer> (from maxBy)
        //   merger does arithmetic: max.orElse(0) - min.orElse(0)
        //   R = Integer
        Integer range = numbers.stream()
            .collect(Collectors.teeing(
                Collectors.minBy(Comparator.<Integer>naturalOrder()),  // R1 = Optional<Integer>
                Collectors.maxBy(Comparator.<Integer>naturalOrder()),  // R2 = Optional<Integer>
                (min, max) -> max.orElse(0) - min.orElse(0)           // R = Integer
            ));
        System.out.println("\nteeing to calculate range: " + range);

        // =====================================================================
        // TEEING EXAM PRACTICE
        // =====================================================================
        System.out.println("\n--- TEEING EXAM PRACTICE ---");
        System.out.println();
        System.out.println("Q1: What are the types in this teeing?");
        System.out.println("    stream.collect(teeing(counting(), toList(), (c,l) -> c + \": \" + l))");
        System.out.println("    Answer:");
        System.out.println("      R1 = Long (counting always returns Long)");
        System.out.println("      R2 = List<T>");
        System.out.println("      R = String (c + \": \" + l is string concatenation)");
        System.out.println();
        System.out.println("Q2: What are the types here?");
        System.out.println("    Stream.of(1,2,3).collect(teeing(");
        System.out.println("        summingInt(n->n), maxBy(naturalOrder()), (s,m) -> s + m.get()))");
        System.out.println("    Answer:");
        System.out.println("      T = Integer");
        System.out.println("      R1 = Integer (summingInt returns Integer)");
        System.out.println("      R2 = Optional<Integer> (maxBy returns Optional)");
        System.out.println("      merger: (Integer, Optional<Integer>) -> Integer");
        System.out.println("      R = Integer");
        System.out.println();
        System.out.println("Q3: Does this compile?");
        System.out.println("    teeing(toList(), toSet(), (list, set) -> list.size() + set.size())");
        System.out.println("    Answer: YES!");
        System.out.println("      R1 = List<T>, R2 = Set<T>, R = Integer (int + int)");
        System.out.println();
        System.out.println("Q4: What's wrong with this?");
        System.out.println("    teeing(counting(), counting(), (a, b) -> a + b)");
        System.out.println("    Answer: Nothing wrong - just counts same elements twice!");
        System.out.println("      R1 = Long, R2 = Long, R = Long");
        System.out.println("      Both downstream1 and downstream2 can be the same collector.");
    }
}

// =====================================================================
// QUICK REFERENCE - COLLECTORS
// =====================================================================

/**
 * ===== BASIC COLLECTORS =====
 *
 * toList()                              -> List<T>
 * toSet()                               -> Set<T>
 * toCollection(Supplier)                -> C extends Collection<T>
 * toUnmodifiableList()                  -> List<T> (immutable)
 * toUnmodifiableSet()                   -> Set<T> (immutable)
 *
 * ===== STRING COLLECTORS =====
 *
 * joining()                             -> String (concatenate)
 * joining(delimiter)                    -> String (with delimiter)
 * joining(delim, prefix, suffix)        -> String (with prefix/suffix)
 *
 * ===== NUMERIC COLLECTORS =====
 *
 * counting()                            -> Long
 * summingInt/Long/Double(mapper)        -> Integer/Long/Double
 * averagingInt/Long/Double(mapper)      -> Double (always!)
 * summarizingInt/Long/Double(mapper)    -> XxxSummaryStatistics
 * maxBy(Comparator)                     -> Optional<T>
 * minBy(Comparator)                     -> Optional<T>
 *
 * ===== MAP COLLECTORS =====
 *
 * toMap(keyMapper, valueMapper)                          -> Map<K,V>
 * toMap(keyMapper, valueMapper, mergeFunction)           -> Map<K,V>
 * toMap(keyMapper, valueMapper, mergeFunction, supplier) -> M extends Map<K,V>
 * toUnmodifiableMap(...)                                 -> Map<K,V> (immutable)
 *
 * EXAM TIP: toMap throws IllegalStateException on duplicate keys!
 *           Use 3-arg version with merge function to handle duplicates.
 *
 * ===== GROUPING COLLECTORS =====
 *
 * groupingBy(classifier)                                 -> Map<K, List<T>>
 * groupingBy(classifier, downstream)                     -> Map<K, D>
 * groupingBy(classifier, mapFactory, downstream)         -> M extends Map<K, D>
 *
 * partitioningBy(predicate)                              -> Map<Boolean, List<T>>
 * partitioningBy(predicate, downstream)                  -> Map<Boolean, D>
 *
 * KEY DIFFERENCE:
 * - groupingBy: Keys only exist if elements map to them
 * - partitioningBy: ALWAYS has both true and false keys (even if empty)
 *
 * ===== ADAPTING COLLECTORS =====
 *
 * mapping(mapper, downstream)           -> adapt downstream to accept mapped type
 * filtering(predicate, downstream)      -> filter before collecting
 * flatMapping(mapper, downstream)       -> flat map before collecting
 * collectingAndThen(collector, finisher)-> apply transformation after collecting
 * reducing(...)                         -> collector version of reduce()
 *
 * ===== TEEING (Java 12+) =====
 *
 * teeing(downstream1, downstream2, merger) -> R
 *
 * Runs TWO collectors in parallel, then merges results.
 * Signature: teeing(Collector<T,?,R1>, Collector<T,?,R2>, BiFunction<R1,R2,R>)
 *
 * Common uses:
 * - Get multiple aggregations in one pass (sum + avg, min + max)
 * - Count and collect simultaneously
 * - Calculate range (max - min)
 *
 * ===== EXAM TIPS =====
 *
 * 1. toMap() with duplicate keys:
 *    - 2-arg throws IllegalStateException
 *    - 3-arg needs merge function: (v1, v2) -> v1 (keep first) or v2 (keep last)
 *
 * 2. groupingBy vs partitioningBy:
 *    - groupingBy: any classifier function, keys may not exist
 *    - partitioningBy: predicate only, ALWAYS has true/false keys
 *
 * 3. downstream collectors in groupingBy/partitioningBy:
 *    - counting(), summingInt(), averagingInt() - aggregate groups
 *    - mapping() - transform before aggregating
 *    - filtering() - filter within groups (keeps empty groups!)
 *    - toSet(), toList() - change collection type
 *    - maxBy(), minBy() - find extremes in groups
 *    - joining() - concatenate strings in groups
 *    - groupingBy() - nested grouping!
 *
 * 4. Common patterns:
 *    - Group and count: groupingBy(classifier, counting())
 *    - Group and sum: groupingBy(classifier, summingInt(mapper))
 *    - Group and join: groupingBy(classifier, joining(", "))
 *    - Partition and count: partitioningBy(pred, counting())
 *
 * 5. teeing() signature:
 *    - First two args are Collectors
 *    - Third arg is BiFunction that merges results
 *    - Type of each collector result must match BiFunction params
 */
