package ch13concurrency;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

/**
 * PARALLEL STREAMS
 * ================
 *
 * Using streams for parallel processing and concurrent reductions.
 *
 *
 * ============================================================================
 * PARALLEL DECOMPOSITION
 * ============================================================================
 *
 * Breaking a task into smaller pieces that can be processed concurrently.
 *
 * CREATING PARALLEL STREAMS:
 *
 * Method 1: parallel() on existing stream
 *   Stream<String> parallelStream = list.stream().parallel();
 *
 * Method 2: parallelStream() on collection
 *   Stream<String> parallelStream = list.parallelStream();
 *
 * Method 3: parallel() on Stream of primitives
 *   IntStream parallelStream = IntStream.range(1, 100).parallel();
 *
 *
 * CHECKING IF PARALLEL:
 *   boolean isParallel = stream.isParallel();
 *
 *
 * CONVERTING BACK TO SEQUENTIAL:
 *   Stream<String> sequential = parallelStream.sequential();
 *
 *
 * HOW PARALLEL DECOMPOSITION WORKS:
 * - Stream is divided into multiple chunks
 * - Each chunk processed by different thread
 * - Results combined at the end
 * - Uses Fork/Join framework internally
 * - Number of threads based on available processors
 *
 * EXAMPLE:
 *   List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
 *
 *   // Sequential
 *   numbers.stream()
 *          .map(n -> n * 2)
 *          .forEach(System.out::println);
 *
 *   // Parallel - operations done concurrently
 *   numbers.parallelStream()
 *          .map(n -> n * 2)
 *          .forEach(System.out::println);  // Order not guaranteed!
 *
 *
 * WHEN TO USE PARALLEL STREAMS:
 * - Large datasets
 * - CPU-intensive operations
 * - Operations are independent (no shared state)
 * - Order doesn't matter (or use forEachOrdered)
 *
 * WHEN NOT TO USE:
 * - Small datasets (overhead > benefit)
 * - I/O operations (threads block waiting)
 * - Operations modify shared state (race conditions)
 * - Stateful operations (limit, skip can be expensive)
 *
 *
 * ============================================================================
 * PARALLEL REDUCTIONS
 * ============================================================================
 *
 * Combining stream elements into a single result using parallel processing.
 *
 * REDUCTION OPERATIONS:
 * - reduce()
 * - collect()
 * - count(), min(), max(), sum(), average()
 *
 *
 * reduce() WITH PARALLEL STREAMS:
 * ═══════════════════════════════
 *
 * SIGNATURE (3-argument version for parallel):
 *   <U> U reduce(
 *       U identity,
 *       BiFunction<U, ? super T, U> accumulator,
 *       BinaryOperator<U> combiner
 *   )
 *
 * - identity: Initial value and value when stream empty
 * - accumulator: Combines element with partial result
 * - combiner: Combines partial results from different threads
 *
 * EXAMPLE:
 *   List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
 *
 *   int sum = numbers.parallelStream()
 *                    .reduce(
 *                        0,                    // identity
 *                        (a, b) -> a + b,      // accumulator
 *                        (a, b) -> a + b       // combiner
 *                    );
 *
 * HOW IT WORKS IN PARALLEL:
 * 1. Stream split into chunks: [1,2] [3,4] [5]
 * 2. Each chunk reduced with identity and accumulator:
 *    Thread 1: 0 + 1 + 2 = 3
 *    Thread 2: 0 + 3 + 4 = 7
 *    Thread 3: 0 + 5 = 5
 * 3. Partial results combined with combiner:
 *    3 + 7 + 5 = 15
 *
 *
 * REQUIREMENTS FOR COMBINER:
 * - combiner.apply(identity, i) must equal i
 * - Combiner must be associative: (a op b) op c = a op (b op c)
 * - Combiner must be compatible with accumulator
 *
 *
 * ============================================================================
 * COLLECT WITH PARALLEL STREAMS
 * ============================================================================
 *
 * Mutable reduction - accumulating into a mutable container.
 *
 * REGULAR collect() (3-argument version):
 *   <R> R collect(
 *       Supplier<R> supplier,
 *       BiConsumer<R, ? super T> accumulator,
 *       BiConsumer<R, R> combiner
 *   )
 *
 * - supplier: Creates new result container
 * - accumulator: Adds element to container
 * - combiner: Merges two containers
 *
 * EXAMPLE:
 *   List<String> result = stream.parallel()
 *       .collect(
 *           () -> new ArrayList<>(),        // supplier
 *           (list, item) -> list.add(item), // accumulator
 *           (list1, list2) -> list1.addAll(list2)  // combiner
 *       );
 *
 * Or with method references:
 *   List<String> result = stream.parallel()
 *       .collect(ArrayList::new, List::add, List::addAll);
 *
 *
 * ============================================================================
 * PARALLEL REDUCTION WITH COLLECT() - CRITICAL REQUIREMENTS!
 * ============================================================================
 *
 * For parallel reduction using collect(Collector), THREE requirements must be met:
 *
 * REQUIREMENT 1: Stream is PARALLEL
 * ──────────────────────────────────
 * Must use parallelStream() or parallel()
 *
 *   stream.parallel().collect(collector)  // ✓
 *   stream.collect(collector)             // ✗ (sequential)
 *
 *
 * REQUIREMENT 2: Collector has Characteristics.CONCURRENT
 * ────────────────────────────────────────────────────────
 * The collector must be marked as concurrent
 *
 * Collectors with CONCURRENT characteristic:
 * - Collectors.toConcurrentMap()
 * - Collectors.groupingByConcurrent()
 *
 * Regular collectors are NOT concurrent:
 * - Collectors.toList()       // NOT concurrent
 * - Collectors.toSet()        // NOT concurrent
 * - Collectors.toMap()        // NOT concurrent
 * - Collectors.groupingBy()   // NOT concurrent
 *
 *
 * REQUIREMENT 3: Stream is UNORDERED or Collector has Characteristics.UNORDERED
 * ──────────────────────────────────────────────────────────────────────────────
 * Either the stream must be unordered OR the collector must have UNORDERED characteristic
 *
 * Making stream unordered:
 *   stream.parallel().unordered().collect(collector)
 *
 * Collectors with UNORDERED characteristic:
 * - Collectors.toConcurrentMap()       // UNORDERED and CONCURRENT
 * - Collectors.groupingByConcurrent()  // UNORDERED and CONCURRENT
 *
 *
 * ALL THREE REQUIREMENTS SUMMARY:
 * ═══════════════════════════════
 *
 * For optimal parallel collection performance:
 *
 * 1. stream.parallel() or stream.parallelStream()
 * 2. Collector has Characteristics.CONCURRENT
 * 3. Stream is unordered OR collector has Characteristics.UNORDERED
 *
 * If ANY requirement is not met, the collect operation may:
 * - Fall back to sequential processing
 * - Use synchronized access (slower)
 * - Not achieve parallel benefits
 *
 *
 * EXAMPLE - All requirements met:
 *   Map<String, List<Integer>> result = numbers.parallelStream()  // ✓ parallel
 *       .collect(Collectors.groupingByConcurrent(              // ✓ CONCURRENT
 *           n -> n % 2 == 0 ? "even" : "odd"
 *       ));
 *   // groupingByConcurrent is also UNORDERED ✓
 *
 *
 * ============================================================================
 * COLLECTORS.TOCONCURRENTMAP()
 * ============================================================================
 *
 * Creates a ConcurrentHashMap with parallel stream support.
 *
 * CHARACTERISTICS:
 * - CONCURRENT
 * - UNORDERED
 *
 * SIGNATURES:
 *
 * 1. Basic version:
 *   static <T,K,U> Collector<T,?,ConcurrentMap<K,U>> toConcurrentMap(
 *       Function<? super T, ? extends K> keyMapper,
 *       Function<? super T, ? extends U> valueMapper
 *   )
 *
 * 2. With merge function (for duplicate keys):
 *   static <T,K,U> Collector<T,?,ConcurrentMap<K,U>> toConcurrentMap(
 *       Function<? super T, ? extends K> keyMapper,
 *       Function<? super T, ? extends U> valueMapper,
 *       BinaryOperator<U> mergeFunction
 *   )
 *
 * 3. With merge function and map supplier:
 *   static <T,K,U,M extends ConcurrentMap<K,U>> Collector<T,?,M> toConcurrentMap(
 *       Function<? super T, ? extends K> keyMapper,
 *       Function<? super T, ? extends U> valueMapper,
 *       BinaryOperator<U> mergeFunction,
 *       Supplier<M> mapSupplier
 *   )
 *
 *
 * EXAMPLE:
 *   List<String> words = Arrays.asList("apple", "banana", "cherry");
 *
 *   ConcurrentMap<String, Integer> lengthMap = words.parallelStream()
 *       .collect(Collectors.toConcurrentMap(
 *           word -> word,           // key: word itself
 *           word -> word.length()   // value: word length
 *       ));
 *   // Result: {apple=5, banana=6, cherry=6}
 *
 *
 * HANDLING DUPLICATE KEYS:
 *   List<String> words = Arrays.asList("apple", "apricot", "banana");
 *
 *   ConcurrentMap<Character, String> firstLetterMap = words.parallelStream()
 *       .collect(Collectors.toConcurrentMap(
 *           word -> word.charAt(0),         // key: first letter
 *           word -> word,                   // value: word
 *           (word1, word2) -> word1 + "," + word2  // merge duplicates
 *       ));
 *   // Result: {a=apple,apricot, b=banana}
 *
 *
 * ============================================================================
 * COLLECTORS.GROUPINGBYCONCURRENT()
 * ============================================================================
 *
 * Groups elements by classifier function into a ConcurrentMap.
 *
 * CHARACTERISTICS:
 * - CONCURRENT
 * - UNORDERED
 *
 * SIGNATURES:
 *
 * 1. Basic version (groups into Lists):
 *   static <T,K> Collector<T,?,ConcurrentMap<K,List<T>>> groupingByConcurrent(
 *       Function<? super T, ? extends K> classifier
 *   )
 *
 * 2. With downstream collector:
 *   static <T,K,A,D> Collector<T,?,ConcurrentMap<K,D>> groupingByConcurrent(
 *       Function<? super T, ? extends K> classifier,
 *       Collector<? super T, A, D> downstream
 *   )
 *
 * 3. With map supplier and downstream:
 *   static <T,K,A,D,M extends ConcurrentMap<K,D>> Collector<T,?,M> groupingByConcurrent(
 *       Function<? super T, ? extends K> classifier,
 *       Supplier<M> mapFactory,
 *       Collector<? super T, A, D> downstream
 *   )
 *
 *
 * EXAMPLE - Basic grouping:
 *   List<String> words = Arrays.asList("apple", "apricot", "banana", "blueberry");
 *
 *   ConcurrentMap<Character, List<String>> grouped = words.parallelStream()
 *       .collect(Collectors.groupingByConcurrent(
 *           word -> word.charAt(0)  // Group by first letter
 *       ));
 *   // Result: {a=[apple, apricot], b=[banana, blueberry]}
 *
 *
 * EXAMPLE - With downstream collector:
 *   ConcurrentMap<Character, Long> counts = words.parallelStream()
 *       .collect(Collectors.groupingByConcurrent(
 *           word -> word.charAt(0),     // Group by first letter
 *           Collectors.counting()       // Count items in each group
 *       ));
 *   // Result: {a=2, b=2}
 *
 *
 * ============================================================================
 * COMPARISON: CONCURRENT vs REGULAR COLLECTORS
 * ============================================================================
 *
 * ┌──────────────────────────┬─────────────┬──────────────┬────────────┐
 * │ Collector                │ Concurrent? │ Unordered?   │ Best For   │
 * ├──────────────────────────┼─────────────┼──────────────┼────────────┤
 * │ toList()                 │ NO          │ NO           │ Sequential │
 * │ toSet()                  │ NO          │ YES          │ Sequential │
 * │ toMap()                  │ NO          │ NO           │ Sequential │
 * │ groupingBy()             │ NO          │ NO           │ Sequential │
 * ├──────────────────────────┼─────────────┼──────────────┼────────────┤
 * │ toConcurrentMap()        │ YES         │ YES          │ Parallel   │
 * │ groupingByConcurrent()   │ YES         │ YES          │ Parallel   │
 * └──────────────────────────┴─────────────┴──────────────┴────────────┘
 *
 *
 * ============================================================================
 * PERFORMANCE CONSIDERATIONS
 * ============================================================================
 *
 * WHEN PARALLEL IS FASTER:
 * - Large datasets (thousands+ elements)
 * - CPU-intensive operations (complex calculations)
 * - Independent operations (no shared state)
 *
 * WHEN SEQUENTIAL IS FASTER:
 * - Small datasets (overhead > benefit)
 * - I/O bound operations
 * - Operations with locks/synchronization
 * - Stateful intermediate operations (distinct, sorted, limit)
 *
 * OVERHEAD OF PARALLEL STREAMS:
 * - Splitting the source
 * - Thread management
 * - Combining results
 *
 * RULE OF THUMB:
 * Test with your data! Parallel isn't always faster.
 *
 *
 * ============================================================================
 * CODE EXAMPLES
 * ============================================================================
 */
public class ParallelStreams {

    /**
     * Demonstrates creating parallel streams
     */
    public static void demonstrateParallelCreation() {
        System.out.println("=== Creating Parallel Streams ===");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        // Method 1: parallel() on stream
        Stream<Integer> parallel1 = numbers.stream().parallel();
        System.out.println("Method 1 is parallel: " + parallel1.isParallel());

        // Method 2: parallelStream() on collection
        Stream<Integer> parallel2 = numbers.parallelStream();
        System.out.println("Method 2 is parallel: " + parallel2.isParallel());

        // Convert back to sequential
        Stream<Integer> sequential = parallel2.sequential();
        System.out.println("After sequential(): " + sequential.isParallel());

        System.out.println();
    }

    /**
     * Demonstrates parallel decomposition
     */
    public static void demonstrateParallelDecomposition() {
        System.out.println("=== Parallel Decomposition ===");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

        System.out.println("Sequential forEach (ordered):");
        numbers.stream()
               .forEach(n -> System.out.print(n + " "));

        System.out.println("\n\nParallel forEach (may be unordered):");
        numbers.parallelStream()
               .forEach(n -> System.out.print(n + " "));

        System.out.println("\n\nParallel forEachOrdered (maintains order):");
        numbers.parallelStream()
               .forEachOrdered(n -> System.out.print(n + " "));

        System.out.println("\n");
    }

    /**
     * Demonstrates parallel reduction with reduce()
     */
    public static void demonstrateParallelReduce() {
        System.out.println("=== Parallel Reduction with reduce() ===");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        // Sum using parallel reduce
        int sum = numbers.parallelStream()
                         .reduce(
                             0,                    // identity
                             (a, b) -> a + b,      // accumulator
                             (a, b) -> a + b       // combiner (for parallel)
                         );

        System.out.println("Sum: " + sum);  // 15

        // Product using parallel reduce
        int product = numbers.parallelStream()
                             .reduce(
                                 1,
                                 (a, b) -> a * b,
                                 (a, b) -> a * b
                             );

        System.out.println("Product: " + product);  // 120
        System.out.println();
    }

    /**
     * Demonstrates parallel collect with 3-argument version
     */
    public static void demonstrateParallelCollect() {
        System.out.println("=== Parallel Collect (3-argument) ===");

        List<String> words = Arrays.asList("apple", "banana", "cherry");

        List<String> result = words.parallelStream()
            .collect(
                ArrayList::new,           // supplier
                List::add,                // accumulator
                List::addAll              // combiner
            );

        System.out.println("Collected: " + result);
        System.out.println();
    }

    /**
     * Demonstrates toConcurrentMap
     */
    public static void demonstrateToConcurrentMap() {
        System.out.println("=== toConcurrentMap ===");

        List<String> words = Arrays.asList("apple", "banana", "cherry", "date");

        // Map word to length
        ConcurrentMap<String, Integer> lengthMap = words.parallelStream()
            .collect(Collectors.toConcurrentMap(
                word -> word,
                word -> word.length()
            ));

        System.out.println("Word lengths: " + lengthMap);

        // Handle duplicate keys (first letter)
        words = Arrays.asList("apple", "apricot", "banana", "blueberry");

        ConcurrentMap<Character, String> firstLetterMap = words.parallelStream()
            .collect(Collectors.toConcurrentMap(
                word -> word.charAt(0),
                word -> word,
                (w1, w2) -> w1 + ", " + w2  // Merge function
            ));

        System.out.println("Grouped by first letter: " + firstLetterMap);
        System.out.println();
    }

    /**
     * Demonstrates groupingByConcurrent
     */
    public static void demonstrateGroupingByConcurrent() {
        System.out.println("=== groupingByConcurrent ===");

        List<String> words = Arrays.asList("apple", "apricot", "banana",
                                           "blueberry", "cherry", "coconut");

        // Basic grouping
        ConcurrentMap<Character, List<String>> grouped = words.parallelStream()
            .collect(Collectors.groupingByConcurrent(
                word -> word.charAt(0)
            ));

        System.out.println("Grouped by first letter:");
        grouped.forEach((key, value) ->
            System.out.println("  " + key + ": " + value)
        );

        // With downstream collector (counting)
        ConcurrentMap<Character, Long> counts = words.parallelStream()
            .collect(Collectors.groupingByConcurrent(
                word -> word.charAt(0),
                Collectors.counting()
            ));

        System.out.println("\nCounts by first letter: " + counts);
        System.out.println();
    }

    /**
     * Demonstrates concurrent vs regular collectors
     */
    public static void demonstrateConcurrentVsRegular() {
        System.out.println("=== Concurrent vs Regular Collectors ===");

        List<Integer> numbers = IntStream.rangeClosed(1, 100)
                                        .boxed()
                                        .collect(Collectors.toList());

        // Regular toMap (not concurrent)
        long start = System.currentTimeMillis();
        Map<Integer, Integer> regularMap = numbers.parallelStream()
            .collect(Collectors.toMap(
                n -> n,
                n -> n * n
            ));
        long regularTime = System.currentTimeMillis() - start;
        System.out.println("Regular toMap time: " + regularTime + "ms");

        // Concurrent toConcurrentMap
        start = System.currentTimeMillis();
        ConcurrentMap<Integer, Integer> concurrentMap = numbers.parallelStream()
            .collect(Collectors.toConcurrentMap(
                n -> n,
                n -> n * n
            ));
        long concurrentTime = System.currentTimeMillis() - start;
        System.out.println("Concurrent toConcurrentMap time: " + concurrentTime + "ms");

        System.out.println("Both maps same size: " +
            (regularMap.size() == concurrentMap.size()));
        System.out.println();
    }

    /**
     * Demonstrates the three requirements for parallel collection
     */
    public static void demonstrateParallelCollectionRequirements() {
        System.out.println("=== Parallel Collection Requirements ===");

        List<String> words = Arrays.asList("apple", "banana", "cherry");

        // Requirement 1: Stream is parallel ✓
        // Requirement 2: Collector has CONCURRENT ✓
        // Requirement 3: Collector has UNORDERED ✓
        ConcurrentMap<Character, List<String>> result = words.parallelStream()
            .collect(Collectors.groupingByConcurrent(
                word -> word.charAt(0)
            ));

        System.out.println("All requirements met:");
        System.out.println("  1. parallelStream() - parallel ✓");
        System.out.println("  2. groupingByConcurrent - CONCURRENT ✓");
        System.out.println("  3. groupingByConcurrent - UNORDERED ✓");
        System.out.println("Result: " + result);
        System.out.println();
    }

    /**
     * Performance comparison: sequential vs parallel
     */
    public static void demonstratePerformanceComparison() {
        System.out.println("=== Performance: Sequential vs Parallel ===");

        // Large dataset
        List<Integer> largeList = IntStream.rangeClosed(1, 1000000)
                                          .boxed()
                                          .collect(Collectors.toList());

        // Sequential
        long start = System.currentTimeMillis();
        long sequentialSum = largeList.stream()
                                      .mapToLong(n -> n)
                                      .sum();
        long sequentialTime = System.currentTimeMillis() - start;

        // Parallel
        start = System.currentTimeMillis();
        long parallelSum = largeList.parallelStream()
                                    .mapToLong(n -> n)
                                    .sum();
        long parallelTime = System.currentTimeMillis() - start;

        System.out.println("Sequential time: " + sequentialTime + "ms");
        System.out.println("Parallel time: " + parallelTime + "ms");
        System.out.println("Both sums equal: " + (sequentialSum == parallelSum));
        System.out.println("Speedup: " + (double)sequentialTime / parallelTime + "x");
        System.out.println();
    }

    public static void main(String[] args) {
        demonstrateParallelCreation();
        demonstrateParallelDecomposition();
        demonstrateParallelReduce();
        demonstrateParallelCollect();
        demonstrateToConcurrentMap();
        demonstrateGroupingByConcurrent();
        demonstrateConcurrentVsRegular();
        demonstrateParallelCollectionRequirements();
        demonstratePerformanceComparison();
    }
}

/*
 * ============================================================================
 * EXAM SUMMARY - PARALLEL STREAMS
 * ============================================================================
 *
 * CREATING PARALLEL STREAMS:
 * - stream.parallel()
 * - collection.parallelStream()
 * - Check: stream.isParallel()
 *
 * PARALLEL DECOMPOSITION:
 * - Stream split into chunks
 * - Processed by different threads
 * - Results combined at end
 *
 * PARALLEL REDUCTION:
 * - reduce(identity, accumulator, combiner)
 * - combiner combines partial results from threads
 *
 * THREE REQUIREMENTS FOR PARALLEL COLLECT():
 * 1. Stream is PARALLEL (parallelStream() or parallel())
 * 2. Collector has Characteristics.CONCURRENT
 * 3. Stream is UNORDERED or collector has Characteristics.UNORDERED
 *
 * CONCURRENT COLLECTORS (both CONCURRENT and UNORDERED):
 * - Collectors.toConcurrentMap()
 * - Collectors.groupingByConcurrent()
 *
 * NON-CONCURRENT COLLECTORS:
 * - toList(), toSet(), toMap(), groupingBy()
 * - Can still use with parallel streams but won't get full benefit
 *
 * PERFORMANCE:
 * - Parallel faster for: large data, CPU-intensive, independent operations
 * - Sequential faster for: small data, I/O operations, shared state
 * - Always test with your data!
 *
 * ORDER:
 * - forEach() may be unordered with parallel
 * - forEachOrdered() maintains order (but slower)
 */
