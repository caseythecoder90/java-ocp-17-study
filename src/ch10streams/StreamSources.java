package ch10streams;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Stream;

/**
 * Stream Pipeline Concepts and Sources
 *
 * ===== WHAT IS A STREAM? =====
 *
 * A stream is a sequence of data elements that can be processed with operations.
 * Think of it like an assembly line in a factory:
 * - Source: Raw materials enter the assembly line
 * - Intermediate operations: Workers transform/filter items as they pass
 * - Terminal operation: Finished products come off the line
 *
 * KEY INSIGHT: Items flow through one at a time, not all at once!
 *
 * ===== FINITE VS INFINITE STREAMS =====
 *
 * Finite Stream:   Has a defined number of elements (e.g., from a List)
 * Infinite Stream: Generates elements forever (e.g., Stream.generate())
 *                  Must use a limiting operation (limit(), findFirst(), etc.)
 *                  or it will run forever!
 *
 * ===== LAZY EVALUATION =====
 *
 * Streams use LAZY EVALUATION:
 * - Intermediate operations are NOT executed when called
 * - They are only executed when a TERMINAL operation is invoked
 * - This allows for optimization (short-circuiting, fusion)
 *
 * Example:
 *   stream.filter(x -> ...).map(x -> ...).findFirst();
 *   // filter and map don't run until findFirst() is called
 *   // Also, once findFirst() finds one element, it stops processing
 *
 * ===== STREAM PIPELINE PARTS =====
 *
 * 1. SOURCE:       Where the stream comes from (collection, array, generator)
 * 2. INTERMEDIATE: Transform stream into another stream (filter, map, sorted)
 * 3. TERMINAL:     Produces a result or side-effect (collect, forEach, count)
 *
 * ===== PIPELINE CHARACTERISTICS TABLE (MEMORIZE!) =====
 *
 * | Scenario                      | Source | Intermediate | Terminal |
 * |-------------------------------|--------|--------------|----------|
 * | Required in pipeline?         | Yes    | No           | Yes      |
 * | Can exist multiple times?     | No     | Yes          | No       |
 * | Return type is Stream<T>?     | Yes    | Yes          | No       |
 * | Executed upon method call?    | Yes    | No (lazy!)   | Yes      |
 * | Stream valid after call?      | Yes    | Yes          | No       |
 *
 * EXAM TIPS from this table:
 * - Source + Terminal = minimum valid pipeline (no intermediate required)
 * - Only intermediate operations can appear multiple times
 * - Intermediate operations return Stream<T>, terminal operations don't
 * - Intermediate operations are LAZY (not executed until terminal)
 * - Stream is INVALID after terminal operation (cannot be reused!)
 *
 * ===== STREAMS CAN ONLY BE USED ONCE =====
 *
 * After a terminal operation completes, the stream is "consumed" and cannot be reused.
 *
 *   Stream<String> s = Stream.of("a", "b", "c");
 *   s.forEach(System.out::println);  // Terminal operation
 *   s.forEach(System.out::println);  // IllegalStateException! Stream already consumed
 *
 * If you need to process the same data multiple times, create a new stream each time.
 *
 * ===== 7 STREAM SOURCE CREATION METHODS =====
 *
 * | #  | Method                                            | Finite/Infinite |
 * |----|---------------------------------------------------|-----------------|
 * | 1  | Stream.empty()                                    | FINITE          |
 * | 2  | Stream.of(varargs)                                | FINITE          |
 * | 3  | collection.stream()                               | FINITE          |
 * | 4  | collection.parallelStream()                       | FINITE          |
 * | 5  | Stream.generate(Supplier)                         | INFINITE        |
 * | 6  | Stream.iterate(seed, UnaryOperator)               | INFINITE        |
 * | 7  | Stream.iterate(seed, Predicate, UnaryOperator)    | FINITE*         |
 *
 * * #7 can be INFINITE if the Predicate never returns false!
 */
public class StreamSources {

    public static void main(String[] args) {
        demonstrateFiniteSources();
        demonstrateInfiniteSources();
        demonstrateStreamOnlyUsedOnce();
    }

    // =====================================================================
    // FINITE STREAM SOURCES (1-4, 7)
    // =====================================================================

    public static void demonstrateFiniteSources() {
        System.out.println("=== FINITE STREAM SOURCES ===\n");

        // -----------------------------------------------------------------
        // 1. Stream.empty()
        // -----------------------------------------------------------------
        // Signature: static <T> Stream<T> empty()
        // Creates a stream with no elements
        // FINITE: 0 elements
        Stream<String> empty = Stream.empty();
        System.out.println("1. Stream.empty()");
        System.out.println("   Count: " + empty.count());

        // count() : return mapToLong(e -> 1L).sum();

        // -----------------------------------------------------------------
        // 2. Stream.of(varargs)
        // -----------------------------------------------------------------
        // Signature: static <T> Stream<T> of(T... values)
        // Creates a stream from provided values
        // FINITE: number of elements equals number of arguments
        Stream<String> ofStream = Stream.of("a", "b", "c");
        System.out.println("\n2. Stream.of(\"a\", \"b\", \"c\")");
        System.out.print("   Elements: ");
        ofStream.forEach(s -> System.out.print(s + " "));
        System.out.println();

        // Can also pass an array
        String[] arr = {"x", "y", "z"};
        Stream<String> fromArray = Stream.of(arr);
        System.out.println("\n   Stream.of(array):");
        System.out.print("   Elements: ");
        fromArray.forEach(s -> System.out.print(s + " "));
        System.out.println();

        // -----------------------------------------------------------------
        // 3. collection.stream()
        // -----------------------------------------------------------------
        // Signature: default Stream<E> stream()  (on Collection interface)
        // Creates a sequential stream from a Collection
        // FINITE: number of elements in the collection
        List<String> list = List.of("one", "two", "three");
        Stream<String> listStream = list.stream();
        System.out.println("\n3. list.stream()");
        System.out.print("   Elements: ");
        listStream.forEach(s -> System.out.print(s + " "));
        System.out.println();

        // -----------------------------------------------------------------
        // 4. collection.parallelStream()
        // -----------------------------------------------------------------
        // Signature: default Stream<E> parallelStream()  (on Collection interface)
        // Creates a parallel stream from a Collection
        // FINITE: number of elements in the collection
        // NOTE: Elements may be processed in any order!
        Stream<String> parallelStream = list.parallelStream();
        System.out.println("\n4. list.parallelStream()");
        System.out.print("   Elements (order may vary): ");
        parallelStream.forEach(s -> System.out.print(s + " "));
        System.out.println();

        // -----------------------------------------------------------------
        // 7. Stream.iterate(seed, Predicate, UnaryOperator) - Java 9+
        // -----------------------------------------------------------------
        // Signature: static <T> Stream<T> iterate(T seed, Predicate<T> hasNext, UnaryOperator<T> next)
        // Creates stream similar to a for loop - FINITE only if predicate eventually returns false!
        // Think: for(T i = seed; hasNext.test(i); i = next.apply(i))
        //
        // WARNING: Can be INFINITE if predicate never returns false!
        //   Stream.iterate(1, n -> n > 0, n -> n + 1)  // INFINITE! n > 0 always true
        //   Stream.iterate(1, n -> true, n -> n + 1)   // INFINITE! predicate always true
        Stream<Integer> iterateFinite = Stream.iterate(1, n -> n <= 5, n -> n + 1);
        System.out.println("\n7. Stream.iterate(1, n -> n <= 5, n -> n + 1)");
        System.out.print("   Elements: ");
        iterateFinite.forEach(n -> System.out.print(n + " "));
        System.out.println();
        System.out.println("   Like: for(int n = 1; n <= 5; n++)");
        System.out.println("   WARNING: Can be INFINITE if predicate never returns false!");
    }

    // =====================================================================
    // INFINITE STREAM SOURCES (5-6)
    // =====================================================================

    public static void demonstrateInfiniteSources() {
        System.out.println("\n=== INFINITE STREAM SOURCES ===\n");
        System.out.println("WARNING: Must use limit() or short-circuit operation!\n");

        // -----------------------------------------------------------------
        // 5. Stream.generate(Supplier)
        // -----------------------------------------------------------------
        // Signature: static <T> Stream<T> generate(Supplier<T> s)
        // Creates an infinite stream where each element comes from the Supplier
        // INFINITE: Supplier is called forever unless limited!
        // Each element is independent (no relation to previous)

        System.out.println("5. Stream.generate(Supplier)");

        // Generate constant values
        Stream<String> hellos = Stream.generate(() -> "Hello");
        System.out.print("   generate(() -> \"Hello\").limit(3): ");
        hellos.limit(3).forEach(s -> System.out.print(s + " "));
        System.out.println();

        // Generate random values
        Stream<Double> randoms = Stream.generate(Math::random);
        System.out.print("   generate(Math::random).limit(3): ");
        randoms.limit(3).forEach(d -> System.out.printf("%.2f ", d));
        System.out.println();

        // -----------------------------------------------------------------
        // 6. Stream.iterate(seed, UnaryOperator)
        // -----------------------------------------------------------------
        // Signature: static <T> Stream<T> iterate(T seed, UnaryOperator<T> f)
        // Creates an infinite stream where each element depends on the previous
        // INFINITE: keeps applying function forever unless limited!
        // seed = first element, f(seed) = second, f(f(seed)) = third, etc.

        System.out.println("\n6. Stream.iterate(seed, UnaryOperator)");

        // Count up: 1, 2, 3, 4, 5...
        Stream<Integer> counting = Stream.iterate(1, n -> n + 1);
        System.out.print("   iterate(1, n -> n + 1).limit(5): ");
        counting.limit(5).forEach(n -> System.out.print(n + " "));
        System.out.println();

        // Powers of 2: 1, 2, 4, 8, 16...
        Stream<Integer> powers = Stream.iterate(1, n -> n * 2);
        System.out.print("   iterate(1, n -> n * 2).limit(5): ");
        powers.limit(5).forEach(n -> System.out.print(n + " "));
        System.out.println();

        // Odd numbers: 1, 3, 5, 7, 9...
        Stream<Integer> odds = Stream.iterate(1, n -> n + 2);
        System.out.print("   iterate(1, n -> n + 2).limit(5): ");
        odds.limit(5).forEach(n -> System.out.print(n + " "));
        System.out.println();
    }

    // =====================================================================
    // STREAMS CAN ONLY BE USED ONCE
    // =====================================================================

    public static void demonstrateStreamOnlyUsedOnce() {
        System.out.println("\n=== STREAMS CAN ONLY BE USED ONCE ===\n");

        Stream<String> stream = Stream.of("a", "b", "c");

        // First terminal operation - OK
        long count = stream.count();
        System.out.println("First use - count: " + count);

        // Second use - IllegalStateException!
        try {
            stream.forEach(System.out::println);  // THROWS!
        } catch (IllegalStateException e) {
            System.out.println("Second use - IllegalStateException!");
            System.out.println("  Message: " + e.getMessage());
        }

        // SOLUTION: Create new stream each time from the source
        System.out.println("\nSolution - create new stream each time:");
        List<String> source = List.of("a", "b", "c");
        System.out.println("  Count: " + source.stream().count());
        System.out.println("  First: " + source.stream().findFirst().get());
    }
}

// =====================================================================
// QUICK REFERENCE - 7 STREAM SOURCE METHODS
// =====================================================================

/**
 * FINITE SOURCES:
 *
 * 1. Stream.empty()
 *    - Creates empty stream (0 elements)
 *
 * 2. Stream.of(T... values)
 *    - Creates stream from varargs or array
 *
 * 3. collection.stream()
 *    - Sequential stream from any Collection
 *
 * 4. collection.parallelStream()
 *    - Parallel stream from any Collection
 *    - Elements processed in parallel, order not guaranteed
 *
 * 7. Stream.iterate(seed, Predicate, UnaryOperator)  [Java 9+]
 *    - Like a for loop - stops when Predicate returns false
 *    - CAN BE INFINITE if Predicate never returns false!
 *
 * INFINITE SOURCES (require limit() or short-circuit!):
 *
 * 5. Stream.generate(Supplier)
 *    - Each element from Supplier (independent)
 *    - Use for: constants, random values
 *
 * 6. Stream.iterate(seed, UnaryOperator)
 *    - Each element derived from previous
 *    - Use for: sequences, counting, mathematical series
 *
 * EXAM TIP: Know which are finite vs infinite!
 * - generate() and 2-arg iterate() are always INFINITE
 * - 3-arg iterate() is FINITE only if Predicate eventually returns false
 *   - iterate(1, n -> n <= 5, n -> n + 1)  // FINITE: stops at 5
 *   - iterate(1, n -> n > 0, n -> n + 1)   // INFINITE: predicate always true!
 */
