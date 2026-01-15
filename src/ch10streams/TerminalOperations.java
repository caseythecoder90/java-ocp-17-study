package ch10streams;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Terminal Operations
 *
 * Terminal operations produce a result from a stream pipeline.
 * After a terminal operation, the stream is consumed and cannot be reused.
 *
 * NOTE: You can perform a terminal operation without any intermediate operations!
 *       Source + Terminal = valid pipeline (intermediate is optional)
 *
 * ===== REDUCTIONS =====
 *
 * A reduction is a special type of terminal operation where all contents of
 * the stream are combined into a single primitive or Object.
 *
 * ===== TERMINAL OPERATIONS SUMMARY =====
 *
 * | Method        | Return Type      | Infinite Stream | Reduction? |
 * |---------------|------------------|-----------------|------------|
 * | count()       | long             | Never terminates| Yes        |
 * | min()         | Optional<T>      | Never terminates| Yes        |
 * | max()         | Optional<T>      | Never terminates| Yes        |
 * | findAny()     | Optional<T>      | Terminates      | No         |
 * | findFirst()   | Optional<T>      | Terminates      | No         |
 * | allMatch()    | boolean          | Sometimes*      | No         |
 * | anyMatch()    | boolean          | Sometimes*      | No         |
 * | noneMatch()   | boolean          | Sometimes*      | No         |
 * | forEach()     | void             | Never terminates| No         |
 * | reduce()      | varies           | Never terminates| Yes        |
 * | collect()     | varies           | Never terminates| Yes        |
 *
 * * Match methods terminate early if result can be determined (short-circuit)
 *
 * ===== reduce() and collect() =====
 *
 * These are covered in detail in separate files:
 * - ReduceOperation.java
 * - CollectOperation.java
 */
public class TerminalOperations {

    public static void main(String[] args) {
        demonstrateCount();
        demonstrateMinMax();
        demonstrateFindAnyAndFindFirst();
        demonstrateMatching();
        demonstrateForEach();
    }

    // =====================================================================
    // count()
    // =====================================================================

    /**
     * long count()
     *
     * - Determines the number of elements in a FINITE stream
     * - Returns: long (the count)
     * - Infinite stream: NEVER TERMINATES (hangs forever)
     * - Reduction: YES (combines all elements into single value)
     */
    public static void demonstrateCount() {
        System.out.println("=== count() ===\n");

        // Signature: long count()

        List<String> list = List.of("a", "b", "c", "d", "e");

        long count = list.stream().count();
        System.out.println("list.stream().count(): " + count);

        // Empty stream
        long emptyCount = Stream.empty().count();
        System.out.println("Stream.empty().count(): " + emptyCount);

        // With filter
        long filteredCount = list.stream()
            .filter(s -> s.compareTo("c") > 0)
            .count();
        System.out.println("Filtered (> 'c'): " + filteredCount);

        // WARNING: Never terminates on infinite stream!
        // Stream.generate(() -> "x").count();  // HANGS FOREVER!
    }

    // =====================================================================
    // min() and max()
    // =====================================================================

    /**
     * Optional<T> min(Comparator<? super T> comparator)
     * Optional<T> max(Comparator<? super T> comparator)
     *
     * - Find smallest/largest value according to the Comparator
     * - Returns: Optional<T> (empty if stream is empty)
     * - Infinite stream: NEVER TERMINATES (can't be sure smaller/larger isn't coming)
     * - Reduction: YES (combines all elements to find single min/max)
     */
    public static void demonstrateMinMax() {
        System.out.println("\n=== min() and max() ===\n");

        // Signatures:
        // Optional<T> min(Comparator<? super T> comparator)
        // Optional<T> max(Comparator<? super T> comparator)

        List<Integer> numbers = List.of(5, 3, 8, 1, 9, 2);

        // Find minimum
        Optional<Integer> min = numbers.stream()
            .min(Comparator.naturalOrder());
        System.out.println("min(naturalOrder): " + min.orElse(-1));

        // Find maximum
        Optional<Integer> max = numbers.stream()
            .max(Comparator.naturalOrder());
        System.out.println("max(naturalOrder): " + max.orElse(-1));

        // With custom comparator - find shortest string
        List<String> words = List.of("apple", "pie", "banana", "kiwi");

        Optional<String> shortest = words.stream()
            .min(Comparator.comparingInt(String::length));
        System.out.println("\nShortest word: " + shortest.orElse("none"));

        Optional<String> longest = words.stream()
            .max(Comparator.comparingInt(String::length));
        System.out.println("Longest word: " + longest.orElse("none"));

        // Empty stream returns empty Optional
        Optional<Integer> emptyMin = Stream.<Integer>empty()
            .min(Comparator.naturalOrder());
        System.out.println("\nEmpty stream min: " + emptyMin);  // Optional.empty

        // WARNING: Never terminates on infinite stream!
        // Stream.generate(() -> 1).min(Comparator.naturalOrder());  // HANGS!
    }

    // =====================================================================
    // findAny() and findFirst()
    // =====================================================================

    /**
     * Optional<T> findAny()
     * Optional<T> findFirst()
     *
     * - findAny():   Returns ANY element (not guaranteed to be first!)
     * - findFirst(): Returns the FIRST element (always first if stream has order)
     * - Returns: Optional<T> (empty if stream is empty)
     * - Infinite stream: TERMINATES (returns as soon as element found)
     * - Reduction: NO (may return without processing all elements)
     *
     * NOT reductions because they sometimes return without processing all elements.
     * They return a value based on the stream but do not reduce stream into single value.
     */
    public static void demonstrateFindAnyAndFindFirst() {
        System.out.println("\n=== findAny() and findFirst() ===\n");

        // Signatures:
        // Optional<T> findAny()
        // Optional<T> findFirst()

        List<String> list = List.of("alpha", "beta", "gamma", "delta");

        // ----- findFirst() -----
        // Always returns the first element if stream is not empty
        // Maintains encounter order
        Optional<String> first = list.stream().findFirst();
        System.out.println("findFirst(): " + first.orElse("none"));

        // Does findFirst() always return first element if stream is not empty?
        // YES - findFirst() respects encounter order and returns the first element
        Optional<String> firstFiltered = list.stream()
            .filter(s -> s.length() == 5)
            .findFirst();
        System.out.println("findFirst() with filter (length 5): " + firstFiltered.orElse("none"));

        // ----- findAny() -----
        // Returns any element - may not be the first!
        // With sequential stream, often returns first (but not guaranteed)
        Optional<String> any = list.stream().findAny();
        System.out.println("\nfindAny() sequential: " + any.orElse("none"));

        // IMPORTANT: With parallel streams, findAny() may return different elements!
        // This is because multiple threads process the stream simultaneously
        System.out.println("\nfindAny() with parallel stream (5 runs):");
        for (int i = 0; i < 5; i++) {
            Optional<String> parallelAny = list.parallelStream().findAny();
            System.out.println("  Run " + (i+1) + ": " + parallelAny.orElse("none"));
        }
        System.out.println("  (Results may vary - not always first element!)");

        // Empty stream
        Optional<String> emptyFirst = Stream.<String>empty().findFirst();
        Optional<String> emptyAny = Stream.<String>empty().findAny();
        System.out.println("\nEmpty stream findFirst(): " + emptyFirst);
        System.out.println("Empty stream findAny(): " + emptyAny);

        // These TERMINATE on infinite streams (return immediately when element found)
        Optional<Integer> infiniteFirst = Stream.iterate(1, n -> n + 1).findFirst();
        System.out.println("\nInfinite stream findFirst(): " + infiniteFirst.orElse(-1));

        Optional<Integer> infiniteAny = Stream.generate(() -> 42).findAny();
        System.out.println("Infinite stream findAny(): " + infiniteAny.orElse(-1));
    }

    // =====================================================================
    // allMatch(), anyMatch(), noneMatch()
    // =====================================================================

    /**
     * boolean allMatch(Predicate<? super T> predicate)
     * boolean anyMatch(Predicate<? super T> predicate)
     * boolean noneMatch(Predicate<? super T> predicate)
     *
     * - allMatch():  Returns true if ALL elements match the predicate
     * - anyMatch():  Returns true if ANY element matches the predicate
     * - noneMatch(): Returns true if NO elements match the predicate
     * - Returns: boolean
     * - Infinite stream: MAY OR MAY NOT TERMINATE (short-circuit if possible)
     * - Reduction: NO (short-circuit operations)
     *
     * Short-circuit behavior:
     * - allMatch():  Terminates on first FALSE (returns false immediately)
     * - anyMatch():  Terminates on first TRUE (returns true immediately)
     * - noneMatch(): Terminates on first TRUE (returns false immediately)
     */
    public static void demonstrateMatching() {
        System.out.println("\n=== allMatch(), anyMatch(), noneMatch() ===\n");

        // Signatures:
        // boolean allMatch(Predicate<? super T> predicate)
        // boolean anyMatch(Predicate<? super T> predicate)
        // boolean noneMatch(Predicate<? super T> predicate)

        List<Integer> numbers = List.of(2, 4, 6, 8, 10);

        // ----- allMatch() -----
        // Returns true only if ALL elements match
        boolean allEven = numbers.stream().allMatch(n -> n % 2 == 0);
        System.out.println("allMatch(even): " + allEven);  // true

        boolean allGreaterThan5 = numbers.stream().allMatch(n -> n > 5);
        System.out.println("allMatch(> 5): " + allGreaterThan5);  // false

        // ----- anyMatch() -----
        // Returns true if at least ONE element matches
        boolean anyGreaterThan5 = numbers.stream().anyMatch(n -> n > 5);
        System.out.println("\nanyMatch(> 5): " + anyGreaterThan5);  // true

        boolean anyNegative = numbers.stream().anyMatch(n -> n < 0);
        System.out.println("anyMatch(negative): " + anyNegative);  // false

        // ----- noneMatch() -----
        // Returns true if NO elements match
        boolean noneNegative = numbers.stream().noneMatch(n -> n < 0);
        System.out.println("\nnoneMatch(negative): " + noneNegative);  // true

        boolean noneEven = numbers.stream().noneMatch(n -> n % 2 == 0);
        System.out.println("noneMatch(even): " + noneEven);  // false

        // ----- Empty stream behavior -----
        System.out.println("\nEmpty stream behavior:");
        System.out.println("  allMatch(): " + Stream.empty().allMatch(x -> false));  // true!
        System.out.println("  anyMatch(): " + Stream.empty().anyMatch(x -> true));   // false
        System.out.println("  noneMatch(): " + Stream.empty().noneMatch(x -> true)); // true

        // ----- Infinite stream behavior -----
        System.out.println("\nInfinite stream behavior:");

        // anyMatch terminates when finds a match
        boolean infiniteAny = Stream.iterate(1, n -> n + 1)
            .anyMatch(n -> n > 5);
        System.out.println("  anyMatch(> 5) on infinite: " + infiniteAny + " (terminates!)");

        // allMatch terminates when finds non-match
        boolean infiniteAll = Stream.iterate(1, n -> n + 1)
            .allMatch(n -> n < 10);
        System.out.println("  allMatch(< 10) on infinite: " + infiniteAll + " (terminates!)");

        // noneMatch terminates when finds a match
        boolean infiniteNone = Stream.iterate(1, n -> n + 1)
            .noneMatch(n -> n > 5);
        System.out.println("  noneMatch(> 5) on infinite: " + infiniteNone + " (terminates!)");

        // WARNING: These would hang if condition is never met/always met:
        // Stream.iterate(1, n -> n + 1).anyMatch(n -> n < 0);   // HANGS - never finds negative
        // Stream.iterate(1, n -> n + 1).allMatch(n -> n > 0);   // HANGS - all are positive
        // Stream.iterate(1, n -> n + 1).noneMatch(n -> n > 0);  // HANGS - all are positive
    }

    // =====================================================================
    // forEach()
    // =====================================================================

    /**
     * void forEach(Consumer<? super T> action)
     *
     * - Performs an action for each element of the stream
     * - Returns: void (no return value!)
     * - Infinite stream: NEVER TERMINATES
     * - Reduction: NO (does not produce a single result)
     *
     * NOTE: forEach() does not guarantee order with parallel streams!
     * Use forEachOrdered() if order matters with parallel streams.
     */
    public static void demonstrateForEach() {
        System.out.println("\n=== forEach() ===\n");

        // Signature: void forEach(Consumer<? super T> action)

        List<String> list = List.of("one", "two", "three");

        // Basic forEach
        System.out.println("forEach with sequential stream:");
        list.stream().forEach(s -> System.out.println("  " + s));

        // Method reference
        System.out.println("\nforEach with method reference:");
        list.stream().forEach(System.out::println);

        // WARNING: Order not guaranteed with parallel stream!
        System.out.println("\nforEach with parallel stream (order may vary):");
        list.parallelStream().forEach(s -> System.out.println("  " + s));

        // Use forEachOrdered() for guaranteed order with parallel
        System.out.println("\nforEachOrdered with parallel stream (order preserved):");
        list.parallelStream().forEachOrdered(s -> System.out.println("  " + s));

        // Returns void - cannot chain or assign result
        // String result = list.stream().forEach(System.out::println);  // ERROR!

        // WARNING: Never terminates on infinite stream!
        // Stream.generate(() -> "x").forEach(System.out::println);  // HANGS!
    }
}

// =====================================================================
// QUICK REFERENCE
// =====================================================================

/**
 * TERMINAL OPERATIONS SIGNATURES:
 *
 * long count()
 * Optional<T> min(Comparator<? super T> comparator)
 * Optional<T> max(Comparator<? super T> comparator)
 * Optional<T> findAny()
 * Optional<T> findFirst()
 * boolean allMatch(Predicate<? super T> predicate)
 * boolean anyMatch(Predicate<? super T> predicate)
 * boolean noneMatch(Predicate<? super T> predicate)
 * void forEach(Consumer<? super T> action)
 *
 * REDUCTIONS (combine all elements into single value):
 * - count(), min(), max(), reduce(), collect()
 *
 * NOT REDUCTIONS (may short-circuit / not process all):
 * - findAny(), findFirst(), allMatch(), anyMatch(), noneMatch(), forEach()
 *
 * TERMINATE ON INFINITE STREAMS:
 * - findAny(), findFirst() - always terminate
 * - allMatch(), anyMatch(), noneMatch() - terminate if can short-circuit
 *
 * NEVER TERMINATE ON INFINITE STREAMS:
 * - count(), min(), max(), forEach(), reduce(), collect()
 *
 * EMPTY STREAM GOTCHAS:
 * - allMatch() on empty returns TRUE (vacuous truth)
 * - anyMatch() on empty returns FALSE
 * - noneMatch() on empty returns TRUE
 *
 * findAny() vs findFirst():
 * - findFirst(): Always returns first element (respects order)
 * - findAny(): May return any element (faster with parallel streams)
 *
 * reduce() and collect() are covered in separate files:
 * - ReduceOperation.java
 * - CollectOperation.java
 */
