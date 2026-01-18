package ch10streams;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Primitive Streams: IntStream, LongStream, DoubleStream
 *
 * Primitive streams avoid boxing/unboxing overhead and provide additional
 * numeric operations not available on Stream<T>.
 *
 * ===== THREE PRIMITIVE STREAM TYPES =====
 *
 * | Stream Type   | Primitive | Also Used For          |
 * |---------------|-----------|------------------------|
 * | IntStream     | int       | short, byte, char      |
 * | LongStream    | long      | (just long)            |
 * | DoubleStream  | double    | float                  |
 *
 * Note: There is NO FloatStream, ShortStream, ByteStream, or CharStream!
 *
 * ===== WHY USE PRIMITIVE STREAMS? =====
 *
 * 1. Performance: No boxing/unboxing (int vs Integer)
 * 2. Special methods: sum(), average(), max(), min(), summaryStatistics()
 * 3. Range generation: range(), rangeClosed()
 *
 * ===== KEY DIFFERENCES FROM Stream<T> =====
 *
 * | Operation         | Stream<T>           | Primitive Stream        |
 * |-------------------|---------------------|-------------------------|
 * | sum()             | NOT available       | int/long/double sum()   |
 * | average()         | NOT available       | OptionalDouble          |
 * | max()             | Optional<T>         | OptionalInt/Long/Double |
 * | min()             | Optional<T>         | OptionalInt/Long/Double |
 * | range()           | NOT available       | IntStream, LongStream   |
 * | boxed()           | NOT needed          | Stream<Integer/Long/Double> |
 * | reduce()          | BinaryOperator<T>   | IntBinaryOperator, etc. |
 * | map()             | Function<T,R>       | IntUnaryOperator, etc.  |
 *
 * ===== MAPPING BETWEEN STREAM TYPES - MASTER TABLE =====
 *
 * SOURCE STREAM -> map method -> TARGET STREAM
 *
 * | Source         | Method           | Function Parameter          | Target         |
 * |----------------|------------------|----------------------------|----------------|
 * | Stream<T>      | map              | Function<T,R>              | Stream<R>      |
 * | Stream<T>      | mapToInt         | ToIntFunction<T>           | IntStream      |
 * | Stream<T>      | mapToLong        | ToLongFunction<T>          | LongStream     |
 * | Stream<T>      | mapToDouble      | ToDoubleFunction<T>        | DoubleStream   |
 * |----------------|------------------|----------------------------|----------------|
 * | IntStream      | map              | IntUnaryOperator           | IntStream      |
 * | IntStream      | mapToObj         | IntFunction<R>             | Stream<R>      |
 * | IntStream      | mapToLong        | IntToLongFunction          | LongStream     |
 * | IntStream      | mapToDouble      | IntToDoubleFunction        | DoubleStream   |
 * |----------------|------------------|----------------------------|----------------|
 * | LongStream     | map              | LongUnaryOperator          | LongStream     |
 * | LongStream     | mapToObj         | LongFunction<R>            | Stream<R>      |
 * | LongStream     | mapToInt         | LongToIntFunction          | IntStream      |
 * | LongStream     | mapToDouble      | LongToDoubleFunction       | DoubleStream   |
 * |----------------|------------------|----------------------------|----------------|
 * | DoubleStream   | map              | DoubleUnaryOperator        | DoubleStream   |
 * | DoubleStream   | mapToObj         | DoubleFunction<R>          | Stream<R>      |
 * | DoubleStream   | mapToInt         | DoubleToIntFunction        | IntStream      |
 * | DoubleStream   | mapToLong        | DoubleToLongFunction       | LongStream     |
 *
 * ===== FUNCTION PARAMETER TYPES - QUICK REFERENCE =====
 *
 * Object Stream Functions:
 *   Function<T,R>        (T) -> R           Stream<T> -> Stream<R>
 *   ToIntFunction<T>     (T) -> int         Stream<T> -> IntStream
 *   ToLongFunction<T>    (T) -> long        Stream<T> -> LongStream
 *   ToDoubleFunction<T>  (T) -> double      Stream<T> -> DoubleStream
 *
 * IntStream Functions:
 *   IntUnaryOperator     (int) -> int       IntStream -> IntStream
 *   IntFunction<R>       (int) -> R         IntStream -> Stream<R>
 *   IntToLongFunction    (int) -> long      IntStream -> LongStream
 *   IntToDoubleFunction  (int) -> double    IntStream -> DoubleStream
 *
 * LongStream Functions:
 *   LongUnaryOperator    (long) -> long     LongStream -> LongStream
 *   LongFunction<R>      (long) -> R        LongStream -> Stream<R>
 *   LongToIntFunction    (long) -> int      LongStream -> IntStream
 *   LongToDoubleFunction (long) -> double   LongStream -> DoubleStream
 *
 * DoubleStream Functions:
 *   DoubleUnaryOperator    (double) -> double  DoubleStream -> DoubleStream
 *   DoubleFunction<R>      (double) -> R       DoubleStream -> Stream<R>
 *   DoubleToIntFunction    (double) -> int     DoubleStream -> IntStream
 *   DoubleToLongFunction   (double) -> long    DoubleStream -> LongStream
 *
 * ===== OPTIONAL TYPES FOR PRIMITIVE STREAMS =====
 *
 * | Primitive Stream | Optional Type    | Get Method    | orElse param |
 * |------------------|------------------|---------------|--------------|
 * | IntStream        | OptionalInt      | getAsInt()    | int          |
 * | LongStream       | OptionalLong     | getAsLong()   | long         |
 * | DoubleStream     | OptionalDouble   | getAsDouble() | double       |
 *
 * Note: Regular Optional<T> uses get(), primitive optionals use getAsXxx()!
 */
public class PrimitiveStreams {

    public static void main(String[] args) {
        demonstrateCreatingPrimitiveStreams();
        demonstrateRangeAndRangeClosed();
        demonstrateSumAverageMinMax();
        demonstrateSummaryStatistics();
        demonstrateBoxedAndConversions();
        demonstrateMappingBetweenStreams();
        demonstrateFlatMapToXxx();
        demonstratePrimitiveOptionals();
        demonstrateCommonPatterns();
    }

    // =====================================================================
    // CREATING PRIMITIVE STREAMS
    // =====================================================================

    /**
     * CREATING PRIMITIVE STREAMS:
     *
     * IntStream:
     *   IntStream.of(int... values)
     *   IntStream.range(int startInclusive, int endExclusive)
     *   IntStream.rangeClosed(int startInclusive, int endInclusive)
     *   IntStream.generate(IntSupplier s)
     *   IntStream.iterate(int seed, IntUnaryOperator f)
     *   IntStream.iterate(int seed, IntPredicate hasNext, IntUnaryOperator next)
     *   Arrays.stream(int[] array)
     *   String.chars()  -> IntStream
     *
     * LongStream:
     *   LongStream.of(long... values)
     *   LongStream.range(long startInclusive, long endExclusive)
     *   LongStream.rangeClosed(long startInclusive, long endInclusive)
     *   LongStream.generate(LongSupplier s)
     *   LongStream.iterate(long seed, LongUnaryOperator f)
     *   LongStream.iterate(long seed, LongPredicate hasNext, LongUnaryOperator next)
     *   Arrays.stream(long[] array)
     *
     * DoubleStream:
     *   DoubleStream.of(double... values)
     *   DoubleStream.generate(DoubleSupplier s)
     *   DoubleStream.iterate(double seed, DoubleUnaryOperator f)
     *   DoubleStream.iterate(double seed, DoublePredicate hasNext, DoubleUnaryOperator next);
     *   Arrays.stream(double[] array)
     *   Note: NO range() or rangeClosed() for DoubleStream!
     */
    public static void demonstrateCreatingPrimitiveStreams() {
        System.out.println("=== CREATING PRIMITIVE STREAMS ===\n");

        // ===== IntStream Creation =====
        System.out.println("--- IntStream Creation ---");

        // IntStream.of(int... values)
        IntStream intStream1 = IntStream.of(1, 2, 3, 4, 5);
        System.out.println("IntStream.of(1,2,3,4,5): " + Arrays.toString(intStream1.toArray()));

        // From int array
        int[] intArray = {10, 20, 30};
        IntStream intStream2 = Arrays.stream(intArray);
        System.out.println("Arrays.stream(int[]): " + Arrays.toString(intStream2.toArray()));

        // From String (each char as int)
        IntStream charStream = "Hello".chars();
        System.out.println("\"Hello\".chars(): " + Arrays.toString(charStream.toArray()));
        // [72, 101, 108, 108, 111] - ASCII values

        // IntStream.generate (infinite!)
        IntStream generated = IntStream.generate(() -> (int)(Math.random() * 100));
        System.out.println("IntStream.generate (first 5): " +
            Arrays.toString(generated.limit(5).toArray()));

        // IntStream.iterate
        IntStream iterated = IntStream.iterate(1, n -> n * 2);
        System.out.println("IntStream.iterate(1, n->n*2) (first 5): " +
            Arrays.toString(iterated.limit(5).toArray()));
        // [1, 2, 4, 8, 16]

        IntStream iteratedTwo = IntStream.iterate(1, n -> n < 100, n -> n + 1);

        // ===== LongStream Creation =====
        System.out.println("\n--- LongStream Creation ---");

        LongStream longStream1 = LongStream.of(100L, 200L, 300L);
        System.out.println("LongStream.of: " + Arrays.toString(longStream1.toArray()));

        long[] longArray = {1000L, 2000L, 3000L};
        LongStream longStream2 = Arrays.stream(longArray);
        System.out.println("Arrays.stream(long[]): " + Arrays.toString(longStream2.toArray()));

        // ===== DoubleStream Creation =====
        System.out.println("\n--- DoubleStream Creation ---");

        DoubleStream doubleStream1 = DoubleStream.of(1.1, 2.2, 3.3);
        System.out.println("DoubleStream.of: " + Arrays.toString(doubleStream1.toArray()));

        double[] doubleArray = {10.5, 20.5, 30.5};
        DoubleStream doubleStream2 = Arrays.stream(doubleArray);
        System.out.println("Arrays.stream(double[]): " + Arrays.toString(doubleStream2.toArray()));

        System.out.println();
    }

    // =====================================================================
    // RANGE AND RANGECLOSED (IntStream and LongStream only!)
    // =====================================================================

    /**
     * RANGE SIGNATURES:
     *
     * IntStream:
     *   static IntStream range(int startInclusive, int endExclusive)
     *   static IntStream rangeClosed(int startInclusive, int endInclusive)
     *
     * LongStream:
     *   static LongStream range(long startInclusive, long endExclusive)
     *   static LongStream rangeClosed(long startInclusive, long endInclusive)
     *
     * DoubleStream: NO range() or rangeClosed() methods!
     *
     * ===== MEMORY AID =====
     *
     * range(1, 5)       -> 1, 2, 3, 4       (excludes end)
     * rangeClosed(1, 5) -> 1, 2, 3, 4, 5   (includes end)
     *
     * Think: "Closed" means the interval is closed on both ends [1, 5]
     */
    public static void demonstrateRangeAndRangeClosed() {
        System.out.println("=== RANGE AND RANGECLOSED ===\n");

        // ===== IntStream.range vs rangeClosed =====
        System.out.println("--- IntStream range vs rangeClosed ---");

        // range: startInclusive to endExclusive
        IntStream range1to5 = IntStream.range(1, 5);
        System.out.println("IntStream.range(1, 5): " + Arrays.toString(range1to5.toArray()));
        // [1, 2, 3, 4] - excludes 5!

        // rangeClosed: startInclusive to endInclusive
        IntStream rangeClosed1to5 = IntStream.rangeClosed(1, 5);
        System.out.println("IntStream.rangeClosed(1, 5): " + Arrays.toString(rangeClosed1to5.toArray()));
        // [1, 2, 3, 4, 5] - includes 5!

        // Common use: counting from 0 to n-1 (array indices)
        IntStream indices = IntStream.range(0, 5);
        System.out.println("range(0, 5) for indices: " + Arrays.toString(indices.toArray()));
        // [0, 1, 2, 3, 4]

        // Common use: counting from 1 to n (1-based)
        IntStream oneToN = IntStream.rangeClosed(1, 5);
        System.out.println("rangeClosed(1, 5) for 1-based: " + Arrays.toString(oneToN.toArray()));
        // [1, 2, 3, 4, 5]

        // ===== LongStream.range vs rangeClosed =====
        System.out.println("\n--- LongStream range vs rangeClosed ---");

        LongStream longRange = LongStream.range(1, 5);
        System.out.println("LongStream.range(1, 5): " + Arrays.toString(longRange.toArray()));
        // [1, 2, 3, 4]

        LongStream longRangeClosed = LongStream.rangeClosed(1, 5);
        System.out.println("LongStream.rangeClosed(1, 5): " + Arrays.toString(longRangeClosed.toArray()));
        // [1, 2, 3, 4, 5]

        // ===== DoubleStream has NO range! =====
        System.out.println("\n--- DoubleStream ---");
        System.out.println("DoubleStream has NO range() or rangeClosed() methods!");
        System.out.println("For double ranges, use IntStream.range().mapToDouble()");

        // Workaround for double range
        DoubleStream doubleRange = IntStream.rangeClosed(1, 5)
            .mapToDouble(i -> i * 0.5);
        System.out.println("IntStream.rangeClosed(1,5).mapToDouble(i -> i*0.5): " +
            Arrays.toString(doubleRange.toArray()));
        // [0.5, 1.0, 1.5, 2.0, 2.5]

        // ===== Edge cases =====
        System.out.println("\n--- Edge Cases ---");

        // Empty range (start >= end)
        IntStream emptyRange = IntStream.range(5, 5);
        System.out.println("range(5, 5) [start=end]: " + Arrays.toString(emptyRange.toArray()));
        // [] empty

        // Single element with rangeClosed
        IntStream singleElement = IntStream.rangeClosed(5, 5);
        System.out.println("rangeClosed(5, 5) [start=end]: " + Arrays.toString(singleElement.toArray()));
        // [5] single element

        // Negative range (start > end) -> empty
        IntStream negativeRange = IntStream.range(5, 1);
        System.out.println("range(5, 1) [start>end]: " + Arrays.toString(negativeRange.toArray()));
        // [] empty

        System.out.println();
    }

    // =====================================================================
    // SUM, AVERAGE, MIN, MAX
    // =====================================================================

    /**
     * NUMERIC TERMINAL OPERATIONS:
     *
     * ===== SUM =====
     * int sum()     - IntStream
     * long sum()    - LongStream
     * double sum()  - DoubleStream
     * Returns: primitive value (NOT Optional!)
     * Empty stream: returns 0
     *
     * ===== AVERAGE =====
     * OptionalDouble average() - ALL THREE TYPES
     * Returns: OptionalDouble (always, even for IntStream!)
     * Empty stream: returns OptionalDouble.empty()
     * Note: Average is ALWAYS double, never int or long
     *
     * ===== MIN =====
     * OptionalInt min()     - IntStream
     * OptionalLong min()    - LongStream
     * OptionalDouble min()  - DoubleStream
     * Returns: Optional of same primitive type
     * Empty stream: returns empty Optional
     *
     * ===== MAX =====
     * OptionalInt max()     - IntStream
     * OptionalLong max()    - LongStream
     * OptionalDouble max()  - DoubleStream
     * Returns: Optional of same primitive type
     * Empty stream: returns empty Optional
     *
     * ===== EXAM TIP =====
     *
     * sum() returns PRIMITIVE (0 if empty)
     * average(), min(), max() return OPTIONAL (empty if empty stream)
     * average() ALWAYS returns OptionalDouble (even for IntStream!)
     */
    public static void demonstrateSumAverageMinMax() {
        System.out.println("=== SUM, AVERAGE, MIN, MAX ===\n");

        // ===== IntStream =====
        System.out.println("--- IntStream ---");
        IntStream intStream = IntStream.of(3, 1, 4, 1, 5, 9, 2, 6);

        // Note: Stream can only be consumed once!
        int[] intArray = IntStream.of(3, 1, 4, 1, 5, 9, 2, 6).toArray();

        // sum() -> int
        int sum = IntStream.of(intArray).sum();
        System.out.println("IntStream sum(): " + sum + " (type: int)");
        // 31

        // average() -> OptionalDouble (NOT OptionalInt!)
        OptionalDouble avg = IntStream.of(intArray).average();
        System.out.println("IntStream average(): " + avg + " (type: OptionalDouble)");
        System.out.println("  avg.getAsDouble(): " + avg.getAsDouble());

        // min() -> OptionalInt
        OptionalInt min = IntStream.of(intArray).min();
        System.out.println("IntStream min(): " + min + " (type: OptionalInt)");
        System.out.println("  min.getAsInt(): " + min.getAsInt());

        // max() -> OptionalInt
        OptionalInt max = IntStream.of(intArray).max();
        System.out.println("IntStream max(): " + max + " (type: OptionalInt)");
        System.out.println("  max.getAsInt(): " + max.getAsInt());

        // ===== LongStream =====
        System.out.println("\n--- LongStream ---");
        long[] longArray = {100L, 200L, 300L, 400L, 500L};

        long longSum = LongStream.of(longArray).sum();
        System.out.println("LongStream sum(): " + longSum + " (type: long)");

        OptionalDouble longAvg = LongStream.of(longArray).average();
        System.out.println("LongStream average(): " + longAvg + " (type: OptionalDouble)");

        OptionalLong longMin = LongStream.of(longArray).min();
        System.out.println("LongStream min(): " + longMin + " (type: OptionalLong)");

        OptionalLong longMax = LongStream.of(longArray).max();
        System.out.println("LongStream max(): " + longMax + " (type: OptionalLong)");

        // ===== DoubleStream =====
        System.out.println("\n--- DoubleStream ---");
        double[] doubleArray = {1.5, 2.5, 3.5, 4.5};

        double doubleSum = DoubleStream.of(doubleArray).sum();
        System.out.println("DoubleStream sum(): " + doubleSum + " (type: double)");

        OptionalDouble doubleAvg = DoubleStream.of(doubleArray).average();
        System.out.println("DoubleStream average(): " + doubleAvg + " (type: OptionalDouble)");

        OptionalDouble doubleMin = DoubleStream.of(doubleArray).min();
        System.out.println("DoubleStream min(): " + doubleMin + " (type: OptionalDouble)");

        OptionalDouble doubleMax = DoubleStream.of(doubleArray).max();
        System.out.println("DoubleStream max(): " + doubleMax + " (type: OptionalDouble)");

        // ===== Empty Stream Behavior =====
        System.out.println("\n--- Empty Stream Behavior ---");

        int emptySum = IntStream.empty().sum();
        System.out.println("Empty IntStream.sum(): " + emptySum + " (returns 0, not Optional!)");

        OptionalDouble emptyAvg = IntStream.empty().average();
        System.out.println("Empty IntStream.average(): " + emptyAvg + " (returns empty Optional)");
        System.out.println("  emptyAvg.isPresent(): " + emptyAvg.isPresent());
        System.out.println("  emptyAvg.orElse(-1): " + emptyAvg.orElse(-1));

        OptionalInt emptyMin = IntStream.empty().min();
        System.out.println("Empty IntStream.min(): " + emptyMin);

        OptionalInt emptyMax = IntStream.empty().max();
        System.out.println("Empty IntStream.max(): " + emptyMax);

        System.out.println();
    }

    // =====================================================================
    // SUMMARY STATISTICS
    // =====================================================================

    /**
     * SUMMARY STATISTICS SIGNATURES:
     *
     * IntStream:    IntSummaryStatistics summaryStatistics()
     * LongStream:   LongSummaryStatistics summaryStatistics()
     * DoubleStream: DoubleSummaryStatistics summaryStatistics()
     *
     * All statistics classes provide:
     *   - getCount()   -> long
     *   - getSum()     -> int/long/double (matching type)
     *   - getMin()     -> int/long/double (matching type)
     *   - getMax()     -> int/long/double (matching type)
     *   - getAverage() -> double (always double!)
     *
     * ===== WHY USE SUMMARY STATISTICS? =====
     *
     * Calculate ALL statistics in ONE pass through the stream!
     * Otherwise, you'd need to create stream 5 times for each stat.
     *
     * ===== EMPTY STREAM BEHAVIOR =====
     *
     * count = 0, sum = 0, min = MAX_VALUE, max = MIN_VALUE, average = 0.0
     */
    public static void demonstrateSummaryStatistics() {
        System.out.println("=== SUMMARY STATISTICS ===\n");

        // ===== IntSummaryStatistics =====
        System.out.println("--- IntSummaryStatistics ---");

        IntSummaryStatistics intStats = IntStream.of(3, 1, 4, 1, 5, 9, 2, 6)
            .summaryStatistics();

        System.out.println("IntSummaryStatistics for [3,1,4,1,5,9,2,6]:");
        System.out.println("  getCount():   " + intStats.getCount() + " (type: long)");
        System.out.println("  getSum():     " + intStats.getSum() + " (type: long)");
        System.out.println("  getMin():     " + intStats.getMin() + " (type: int)");
        System.out.println("  getMax():     " + intStats.getMax() + " (type: int)");
        System.out.println("  getAverage(): " + intStats.getAverage() + " (type: double)");

        // ===== LongSummaryStatistics =====
        System.out.println("\n--- LongSummaryStatistics ---");

        LongSummaryStatistics longStats = LongStream.of(100L, 200L, 300L, 400L)
            .summaryStatistics();

        System.out.println("LongSummaryStatistics for [100,200,300,400]:");
        System.out.println("  getCount():   " + longStats.getCount());
        System.out.println("  getSum():     " + longStats.getSum() + " (type: long)");
        System.out.println("  getMin():     " + longStats.getMin() + " (type: long)");
        System.out.println("  getMax():     " + longStats.getMax() + " (type: long)");
        System.out.println("  getAverage(): " + longStats.getAverage() + " (type: double)");

        // ===== DoubleSummaryStatistics =====
        System.out.println("\n--- DoubleSummaryStatistics ---");

        DoubleSummaryStatistics doubleStats = DoubleStream.of(1.5, 2.5, 3.5)
            .summaryStatistics();

        System.out.println("DoubleSummaryStatistics for [1.5, 2.5, 3.5]:");
        System.out.println("  getCount():   " + doubleStats.getCount());
        System.out.println("  getSum():     " + doubleStats.getSum() + " (type: double)");
        System.out.println("  getMin():     " + doubleStats.getMin() + " (type: double)");
        System.out.println("  getMax():     " + doubleStats.getMax() + " (type: double)");
        System.out.println("  getAverage(): " + doubleStats.getAverage() + " (type: double)");

        // ===== Empty Stream Statistics =====
        System.out.println("\n--- Empty Stream Statistics ---");

        IntSummaryStatistics emptyStats = IntStream.empty().summaryStatistics();
        System.out.println("Empty IntSummaryStatistics:");
        System.out.println("  getCount():   " + emptyStats.getCount());
        System.out.println("  getSum():     " + emptyStats.getSum());
        System.out.println("  getMin():     " + emptyStats.getMin() + " (Integer.MAX_VALUE!)");
        System.out.println("  getMax():     " + emptyStats.getMax() + " (Integer.MIN_VALUE!)");
        System.out.println("  getAverage(): " + emptyStats.getAverage() + " (0.0)");

        // ===== Combining Statistics =====
        System.out.println("\n--- Combining Statistics ---");

        IntSummaryStatistics stats1 = IntStream.of(1, 2, 3).summaryStatistics();
        IntSummaryStatistics stats2 = IntStream.of(4, 5, 6).summaryStatistics();

        stats1.combine(stats2);  // Modifies stats1!
        System.out.println("Combined stats for [1,2,3] + [4,5,6]:");
        System.out.println("  count: " + stats1.getCount() + ", sum: " + stats1.getSum());

        System.out.println();
    }

    // =====================================================================
    // BOXED AND CONVERSIONS
    // =====================================================================

    /**
     * BOXED AND CONVERSION METHODS:
     *
     * ===== boxed() - Primitive Stream to Object Stream =====
     *
     * IntStream.boxed()    -> Stream<Integer>
     * LongStream.boxed()   -> Stream<Long>
     * DoubleStream.boxed() -> Stream<Double>
     *
     * ===== mapToObj() - Primitive to Object with transformation =====
     *
     * IntStream.mapToObj(IntFunction<R>)       -> Stream<R>
     * LongStream.mapToObj(LongFunction<R>)     -> Stream<R>
     * DoubleStream.mapToObj(DoubleFunction<R>) -> Stream<R>
     *
     * ===== Object Stream to Primitive Stream =====
     *
     * Stream<T>.mapToInt(ToIntFunction<T>)       -> IntStream
     * Stream<T>.mapToLong(ToLongFunction<T>)     -> LongStream
     * Stream<T>.mapToDouble(ToDoubleFunction<T>) -> DoubleStream
     *
     * ===== WHEN TO USE BOXED? =====
     *
     * - Need to collect to List/Set (Collectors don't work on primitive streams)
     * - Need to use Stream<T> methods not available on primitive streams
     * - Need to store in collection that requires objects
     */
    public static void demonstrateBoxedAndConversions() {
        System.out.println("=== BOXED AND CONVERSIONS ===\n");

        // ===== boxed() =====
        System.out.println("--- boxed() - Primitive to Object Stream ---");

        // IntStream -> Stream<Integer>
        Stream<Integer> boxedInts = IntStream.of(1, 2, 3).boxed();
        List<Integer> intList = boxedInts.toList();
        System.out.println("IntStream.boxed().toList(): " + intList);
        System.out.println("  IntStream -> Stream<Integer> -> List<Integer>");

        // LongStream -> Stream<Long>
        Stream<Long> boxedLongs = LongStream.of(100L, 200L).boxed();
        List<Long> longList = boxedLongs.toList();
        System.out.println("LongStream.boxed().toList(): " + longList);

        // DoubleStream -> Stream<Double>
        Stream<Double> boxedDoubles = DoubleStream.of(1.5, 2.5).boxed();
        List<Double> doubleList = boxedDoubles.toList();
        System.out.println("DoubleStream.boxed().toList(): " + doubleList);

        // ===== mapToObj() - Transform to Object =====
        System.out.println("\n--- mapToObj() - Transform primitive to object ---");

        // IntStream -> Stream<String>
        // TYPE ANALYSIS:
        //   IntFunction<String>: (int) -> String
        Stream<String> intToString = IntStream.rangeClosed(1, 5)
            .mapToObj(n -> "Number: " + n);  // IntFunction<String>
        System.out.println("IntStream.mapToObj(n -> \"Number: \" + n): " + intToString.toList());
        System.out.println("  Parameter: IntFunction<R> where (int) -> R");

        // LongStream -> Stream<String>
        Stream<String> longToString = LongStream.of(100L, 200L)
            .mapToObj(n -> n + "L");  // LongFunction<String>
        System.out.println("LongStream.mapToObj: " + longToString.toList());

        // DoubleStream -> Stream<String>
        Stream<String> doubleToString = DoubleStream.of(1.5, 2.5)
            .mapToObj(d -> String.format("%.1f", d));  // DoubleFunction<String>
        System.out.println("DoubleStream.mapToObj: " + doubleToString.toList());

        // ===== Object Stream to Primitive Stream =====
        System.out.println("\n--- Object Stream to Primitive Stream ---");

        List<String> words = List.of("apple", "banana", "cherry");

        // Stream<String> -> IntStream
        // TYPE ANALYSIS:
        //   ToIntFunction<String>: (String) -> int
        IntStream lengths = words.stream()
            .mapToInt(String::length);  // ToIntFunction<String>
        System.out.println("Stream<String>.mapToInt(String::length): " +
            Arrays.toString(lengths.toArray()));
        System.out.println("  Parameter: ToIntFunction<T> where (T) -> int");

        // Stream<String> -> LongStream
        LongStream longLengths = words.stream()
            .mapToLong(String::length);  // ToLongFunction<String>
        System.out.println("Stream<String>.mapToLong: " + Arrays.toString(longLengths.toArray()));

        // Stream<String> -> DoubleStream
        DoubleStream doubleLengths = words.stream()
            .mapToDouble(s -> s.length() * 1.5);  // ToDoubleFunction<String>
        System.out.println("Stream<String>.mapToDouble: " + Arrays.toString(doubleLengths.toArray()));

        // ===== Common Pattern: Collect Primitive Stream to List =====
        System.out.println("\n--- Collecting to List (requires boxed) ---");

        // WRONG: This won't compile!
        // List<Integer> list = IntStream.range(1, 5).collect(Collectors.toList());

        // CORRECT: Use boxed() first
        List<Integer> list = IntStream.range(1, 5)
            .boxed()
            .collect(Collectors.toList());
        System.out.println("IntStream.boxed().collect(toList()): " + list);

        // Alternative: Use toList() on boxed stream
        List<Integer> list2 = IntStream.range(1, 5)
            .boxed()
            .toList();
        System.out.println("IntStream.boxed().toList(): " + list2);

        System.out.println();
    }

    // =====================================================================
    // MAPPING BETWEEN STREAM TYPES
    // =====================================================================

    /**
     * MAPPING BETWEEN STREAM TYPES - DETAILED ANALYSIS
     *
     * ===== FROM STREAM<T> =====
     *
     * map(Function<T,R>)           -> Stream<R>        (T) -> R
     * mapToInt(ToIntFunction<T>)   -> IntStream        (T) -> int
     * mapToLong(ToLongFunction<T>) -> LongStream       (T) -> long
     * mapToDouble(ToDoubleFunction<T>) -> DoubleStream (T) -> double
     *
     * ===== FROM INTSTREAM =====
     *
     * map(IntUnaryOperator)           -> IntStream     (int) -> int
     * mapToObj(IntFunction<R>)        -> Stream<R>     (int) -> R
     * mapToLong(IntToLongFunction)    -> LongStream    (int) -> long
     * mapToDouble(IntToDoubleFunction)-> DoubleStream  (int) -> double
     *
     * ===== FROM LONGSTREAM =====
     *
     * map(LongUnaryOperator)            -> LongStream   (long) -> long
     * mapToObj(LongFunction<R>)         -> Stream<R>    (long) -> R
     * mapToInt(LongToIntFunction)       -> IntStream    (long) -> int
     * mapToDouble(LongToDoubleFunction) -> DoubleStream (long) -> double
     *
     * ===== FROM DOUBLESTREAM =====
     *
     * map(DoubleUnaryOperator)        -> DoubleStream (double) -> double
     * mapToObj(DoubleFunction<R>)     -> Stream<R>    (double) -> R
     * mapToInt(DoubleToIntFunction)   -> IntStream    (double) -> int
     * mapToLong(DoubleToLongFunction) -> LongStream   (double) -> long
     */
    public static void demonstrateMappingBetweenStreams() {
        System.out.println("=== MAPPING BETWEEN STREAM TYPES ===\n");

        // =====================================================================
        // From Stream<T> to Primitive Streams
        // =====================================================================
        System.out.println("--- Stream<T> to Primitive Streams ---");

        List<String> words = List.of("apple", "banana", "cherry");

        // Stream<String> -> IntStream using ToIntFunction<String>
        // ToIntFunction<String>: (String) -> int
        System.out.println("\nStream<String> -> IntStream:");
        System.out.println("  mapToInt(ToIntFunction<T>)");
        System.out.println("  ToIntFunction<String>: (String) -> int");
        int[] intResult = words.stream()
            .mapToInt(s -> s.length())  // ToIntFunction<String>
            .toArray();
        System.out.println("  words.mapToInt(String::length): " + Arrays.toString(intResult));

        // Stream<String> -> LongStream using ToLongFunction<String>
        System.out.println("\nStream<String> -> LongStream:");
        System.out.println("  mapToLong(ToLongFunction<T>)");
        System.out.println("  ToLongFunction<String>: (String) -> long");
        long[] longResult = words.stream()
            .mapToLong(s -> (long) s.length())  // ToLongFunction<String>
            .toArray();
        System.out.println("  words.mapToLong: " + Arrays.toString(longResult));

        // Stream<String> -> DoubleStream using ToDoubleFunction<String>
        System.out.println("\nStream<String> -> DoubleStream:");
        System.out.println("  mapToDouble(ToDoubleFunction<T>)");
        System.out.println("  ToDoubleFunction<String>: (String) -> double");
        double[] doubleResult = words.stream()
            .mapToDouble(s -> s.length() * 1.5)  // ToDoubleFunction<String>
            .toArray();
        System.out.println("  words.mapToDouble: " + Arrays.toString(doubleResult));

        // =====================================================================
        // IntStream to other streams
        // =====================================================================
        System.out.println("\n--- IntStream to Other Streams ---");

        int[] nums = {1, 2, 3, 4, 5};

        // IntStream -> IntStream using IntUnaryOperator
        // IntUnaryOperator: (int) -> int
        System.out.println("\nIntStream -> IntStream:");
        System.out.println("  map(IntUnaryOperator)");
        System.out.println("  IntUnaryOperator: (int) -> int");
        int[] doubled = IntStream.of(nums)
            .map(n -> n * 2)  // IntUnaryOperator
            .toArray();
        System.out.println("  intStream.map(n -> n*2): " + Arrays.toString(doubled));

        // IntStream -> Stream<R> using IntFunction<R>
        // IntFunction<String>: (int) -> String
        System.out.println("\nIntStream -> Stream<R>:");
        System.out.println("  mapToObj(IntFunction<R>)");
        System.out.println("  IntFunction<String>: (int) -> String");
        List<String> intToStrings = IntStream.of(nums)
            .mapToObj(n -> "Value: " + n)  // IntFunction<String>
            .toList();
        System.out.println("  intStream.mapToObj: " + intToStrings);

        // IntStream -> LongStream using IntToLongFunction
        // IntToLongFunction: (int) -> long
        System.out.println("\nIntStream -> LongStream:");
        System.out.println("  mapToLong(IntToLongFunction)");
        System.out.println("  IntToLongFunction: (int) -> long");
        long[] intToLong = IntStream.of(nums)
            .mapToLong(n -> n * 1000L)  // IntToLongFunction
            .toArray();
        System.out.println("  intStream.mapToLong: " + Arrays.toString(intToLong));

        // IntStream -> DoubleStream using IntToDoubleFunction
        // IntToDoubleFunction: (int) -> double
        System.out.println("\nIntStream -> DoubleStream:");
        System.out.println("  mapToDouble(IntToDoubleFunction)");
        System.out.println("  IntToDoubleFunction: (int) -> double");
        double[] intToDouble = IntStream.of(nums)
            .mapToDouble(n -> n / 2.0)  // IntToDoubleFunction
            .toArray();
        System.out.println("  intStream.mapToDouble: " + Arrays.toString(intToDouble));

        // =====================================================================
        // LongStream to other streams
        // =====================================================================
        System.out.println("\n--- LongStream to Other Streams ---");

        long[] longs = {100L, 200L, 300L};

        // LongStream -> LongStream using LongUnaryOperator
        System.out.println("\nLongStream -> LongStream:");
        System.out.println("  map(LongUnaryOperator): (long) -> long");
        long[] longDoubled = LongStream.of(longs)
            .map(n -> n * 2)  // LongUnaryOperator
            .toArray();
        System.out.println("  longStream.map: " + Arrays.toString(longDoubled));

        // LongStream -> Stream<R> using LongFunction<R>
        System.out.println("\nLongStream -> Stream<R>:");
        System.out.println("  mapToObj(LongFunction<R>): (long) -> R");
        List<String> longToStrings = LongStream.of(longs)
            .mapToObj(n -> n + "L")  // LongFunction<String>
            .toList();
        System.out.println("  longStream.mapToObj: " + longToStrings);

        // LongStream -> IntStream using LongToIntFunction
        System.out.println("\nLongStream -> IntStream:");
        System.out.println("  mapToInt(LongToIntFunction): (long) -> int");
        int[] longToInt = LongStream.of(longs)
            .mapToInt(n -> (int) n)  // LongToIntFunction
            .toArray();
        System.out.println("  longStream.mapToInt: " + Arrays.toString(longToInt));

        // LongStream -> DoubleStream using LongToDoubleFunction
        System.out.println("\nLongStream -> DoubleStream:");
        System.out.println("  mapToDouble(LongToDoubleFunction): (long) -> double");
        double[] longToDouble = LongStream.of(longs)
            .mapToDouble(n -> n / 100.0)  // LongToDoubleFunction
            .toArray();
        System.out.println("  longStream.mapToDouble: " + Arrays.toString(longToDouble));

        // =====================================================================
        // DoubleStream to other streams
        // =====================================================================
        System.out.println("\n--- DoubleStream to Other Streams ---");

        double[] doubles = {1.5, 2.5, 3.5};

        // DoubleStream -> DoubleStream using DoubleUnaryOperator
        System.out.println("\nDoubleStream -> DoubleStream:");
        System.out.println("  map(DoubleUnaryOperator): (double) -> double");
        double[] doubleDoubled = DoubleStream.of(doubles)
            .map(d -> d * 2)  // DoubleUnaryOperator
            .toArray();
        System.out.println("  doubleStream.map: " + Arrays.toString(doubleDoubled));

        // DoubleStream -> Stream<R> using DoubleFunction<R>
        System.out.println("\nDoubleStream -> Stream<R>:");
        System.out.println("  mapToObj(DoubleFunction<R>): (double) -> R");
        List<String> doubleToStrings = DoubleStream.of(doubles)
            .mapToObj(d -> String.format("%.1f", d))  // DoubleFunction<String>
            .toList();
        System.out.println("  doubleStream.mapToObj: " + doubleToStrings);

        // DoubleStream -> IntStream using DoubleToIntFunction
        System.out.println("\nDoubleStream -> IntStream:");
        System.out.println("  mapToInt(DoubleToIntFunction): (double) -> int");
        int[] doubleToInt = DoubleStream.of(doubles)
            .mapToInt(d -> (int) d)  // DoubleToIntFunction
            .toArray();
        System.out.println("  doubleStream.mapToInt: " + Arrays.toString(doubleToInt));

        // DoubleStream -> LongStream using DoubleToLongFunction
        System.out.println("\nDoubleStream -> LongStream:");
        System.out.println("  mapToLong(DoubleToLongFunction): (double) -> long");
        long[] doubleToLong = DoubleStream.of(doubles)
            .mapToLong(d -> (long) d)  // DoubleToLongFunction
            .toArray();
        System.out.println("  doubleStream.mapToLong: " + Arrays.toString(doubleToLong));

        System.out.println();
    }

    // =====================================================================
    // FLATMAP TO PRIMITIVE STREAMS
    // =====================================================================

    /**
     * FLATMAP TO PRIMITIVE STREAMS:
     *
     * From Stream<T>:
     *   flatMapToInt(Function<T, IntStream>)       -> IntStream
     *   flatMapToLong(Function<T, LongStream>)     -> LongStream
     *   flatMapToDouble(Function<T, DoubleStream>) -> DoubleStream
     *
     * From IntStream:
     *   flatMap(IntFunction<IntStream>)            -> IntStream
     *
     * From LongStream:
     *   flatMap(LongFunction<LongStream>)          -> LongStream
     *
     * From DoubleStream:
     *   flatMap(DoubleFunction<DoubleStream>)      -> DoubleStream
     *
     * Note: Primitive streams don't have flatMapToObj!
     *       Use mapToObj + flatMap on the resulting Stream instead.
     */
    public static void demonstrateFlatMapToXxx() {
        System.out.println("=== FLATMAP TO PRIMITIVE STREAMS ===\n");

        // ===== Stream<T>.flatMapToInt =====
        System.out.println("--- Stream<T>.flatMapToInt ---");

        List<List<Integer>> nested = List.of(
            List.of(1, 2, 3),
            List.of(4, 5),
            List.of(6, 7, 8, 9)
        );

        // TYPE ANALYSIS:
        //   T = List<Integer>
        //   Function<List<Integer>, IntStream>: (List<Integer>) -> IntStream
        int[] flattened = nested.stream()
            .flatMapToInt(list -> list.stream().mapToInt(i -> i))
            .toArray();
        System.out.println("flatMapToInt on List<List<Integer>>: " + Arrays.toString(flattened));
        System.out.println("  Function<T, IntStream>: (List<Integer>) -> IntStream");

        // ===== Stream<T>.flatMapToLong =====
        System.out.println("\n--- Stream<T>.flatMapToLong ---");

        List<long[]> longArrays = List.of(
            new long[]{100L, 200L},
            new long[]{300L, 400L, 500L}
        );

        long[] flatLongs = longArrays.stream()
            .flatMapToLong(arr -> LongStream.of(arr))
            .toArray();
        System.out.println("flatMapToLong: " + Arrays.toString(flatLongs));

        // ===== Stream<T>.flatMapToDouble =====
        System.out.println("\n--- Stream<T>.flatMapToDouble ---");

        List<double[]> doubleArrays = List.of(
            new double[]{1.1, 2.2},
            new double[]{3.3, 4.4, 5.5}
        );

        double[] flatDoubles = doubleArrays.stream()
            .flatMapToDouble(arr -> DoubleStream.of(arr))
            .toArray();
        System.out.println("flatMapToDouble: " + Arrays.toString(flatDoubles));

        // ===== IntStream.flatMap =====
        System.out.println("\n--- IntStream.flatMap ---");

        // Each int n becomes the stream [n, n*10, n*100]
        // IntFunction<IntStream>: (int) -> IntStream
        int[] expanded = IntStream.of(1, 2, 3)
            .flatMap(n -> IntStream.of(n, n * 10, n * 100))  // IntFunction<IntStream>
            .toArray();
        System.out.println("IntStream.flatMap(n -> [n, n*10, n*100]): " + Arrays.toString(expanded));
        System.out.println("  IntFunction<IntStream>: (int) -> IntStream");

        // ===== LongStream.flatMap =====
        System.out.println("\n--- LongStream.flatMap ---");

        long[] expandedLongs = LongStream.of(1L, 2L)
            .flatMap(n -> LongStream.of(n, n * 1000))  // LongFunction<LongStream>
            .toArray();
        System.out.println("LongStream.flatMap: " + Arrays.toString(expandedLongs));

        // ===== DoubleStream.flatMap =====
        System.out.println("\n--- DoubleStream.flatMap ---");

        double[] expandedDoubles = DoubleStream.of(1.0, 2.0)
            .flatMap(d -> DoubleStream.of(d, d + 0.5))  // DoubleFunction<DoubleStream>
            .toArray();
        System.out.println("DoubleStream.flatMap: " + Arrays.toString(expandedDoubles));

        // ===== Common Pattern: String chars to IntStream =====
        System.out.println("\n--- Common Pattern: Words to char codes ---");

        List<String> words = List.of("Hi", "Bye");

        int[] allCharCodes = words.stream()
            .flatMapToInt(String::chars)  // String.chars() returns IntStream
            .toArray();
        System.out.println("words.flatMapToInt(String::chars): " + Arrays.toString(allCharCodes));
        // [72, 105, 66, 121, 101] - ASCII values

        System.out.println();
    }

    // =====================================================================
    // PRIMITIVE OPTIONALS
    // =====================================================================

    /**
     * PRIMITIVE OPTIONAL TYPES:
     *
     * | Primitive Stream | Optional Type    | Get Method      | ifPresent param     |
     * |------------------|------------------|-----------------|---------------------|
     * | IntStream        | OptionalInt      | getAsInt()      | IntConsumer         |
     * | LongStream       | OptionalLong     | getAsLong()     | LongConsumer        |
     * | DoubleStream     | OptionalDouble   | getAsDouble()   | DoubleConsumer      |
     *
     * ===== COMMON METHODS =====
     *
     * All three (OptionalInt, OptionalLong, OptionalDouble) have:
     *   - isPresent()                    -> boolean
     *   - isEmpty()                      -> boolean (Java 11+)
     *   - getAsXxx()                     -> primitive value (throws if empty!)
     *   - orElse(primitive defaultValue) -> primitive value
     *   - orElseGet(XxxSupplier)         -> primitive value
     *   - orElseThrow()                  -> primitive value or throws NoSuchElementException
     *   - orElseThrow(Supplier<Exception>) -> primitive value or throws custom exception
     *   - ifPresent(XxxConsumer)         -> void
     *   - ifPresentOrElse(XxxConsumer, Runnable) -> void (Java 9+)
     *
     * ===== NOT AVAILABLE ON PRIMITIVE OPTIONALS =====
     *
     * - map(), flatMap(), filter() - NOT available!
     * - or() - NOT available!
     * - stream() - NOT available (use orElse with empty stream instead)
     *
     * For these operations, convert to Optional<T> using:
     *   optionalInt.stream().boxed().findFirst() -> Optional<Integer>
     *
     * ===== EXAM TIP =====
     *
     * OptionalInt.getAsInt()     NOT get()!
     * OptionalLong.getAsLong()   NOT get()!
     * OptionalDouble.getAsDouble() NOT get()!
     *
     * Optional<Integer>.get()    NOT getAsInt()!
     */
    public static void demonstratePrimitiveOptionals() {
        System.out.println("=== PRIMITIVE OPTIONALS ===\n");

        // =====================================================================
        // OptionalInt
        // =====================================================================
        System.out.println("--- OptionalInt ---");

        OptionalInt optInt = IntStream.of(3, 1, 4, 1, 5).max();
        System.out.println("IntStream.max(): " + optInt);

        // getAsInt() - throws NoSuchElementException if empty!
        System.out.println("  getAsInt(): " + optInt.getAsInt());

        // isPresent() / isEmpty()
        System.out.println("  isPresent(): " + optInt.isPresent());
        System.out.println("  isEmpty(): " + optInt.isEmpty());

        // orElse(int)
        System.out.println("  orElse(-1): " + optInt.orElse(-1));

        // orElseGet(IntSupplier)
        System.out.println("  orElseGet(() -> -1): " + optInt.orElseGet(() -> -1));

        // orElseThrow()
        System.out.println("  orElseThrow(): " + optInt.orElseThrow());

        // ifPresent(IntConsumer)
        System.out.print("  ifPresent(System.out::println): ");
        optInt.ifPresent(System.out::println);

        // ifPresentOrElse(IntConsumer, Runnable)
        System.out.print("  ifPresentOrElse: ");
        optInt.ifPresentOrElse(
            val -> System.out.println("Value: " + val),
            () -> System.out.println("Empty!")
        );

        // Empty OptionalInt
        System.out.println("\nEmpty OptionalInt:");
        OptionalInt emptyInt = IntStream.empty().max();
        System.out.println("  IntStream.empty().max(): " + emptyInt);
        System.out.println("  isPresent(): " + emptyInt.isPresent());
        System.out.println("  orElse(-1): " + emptyInt.orElse(-1));
        // emptyInt.getAsInt() would throw NoSuchElementException!

        // =====================================================================
        // OptionalLong
        // =====================================================================
        System.out.println("\n--- OptionalLong ---");

        OptionalLong optLong = LongStream.of(100L, 200L, 300L).min();
        System.out.println("LongStream.min(): " + optLong);
        System.out.println("  getAsLong(): " + optLong.getAsLong());
        System.out.println("  orElse(0L): " + optLong.orElse(0L));

        // =====================================================================
        // OptionalDouble
        // =====================================================================
        System.out.println("\n--- OptionalDouble ---");

        OptionalDouble optDouble = DoubleStream.of(1.5, 2.5, 3.5).average();
        System.out.println("DoubleStream.average(): " + optDouble);
        System.out.println("  getAsDouble(): " + optDouble.getAsDouble());
        System.out.println("  orElse(0.0): " + optDouble.orElse(0.0));

        // average() always returns OptionalDouble (even for IntStream!)
        OptionalDouble intAvg = IntStream.of(1, 2, 3, 4).average();
        System.out.println("\nIntStream.average(): " + intAvg + " (type: OptionalDouble!)");

        // =====================================================================
        // Creating Primitive Optionals Directly
        // =====================================================================
        System.out.println("\n--- Creating Primitive Optionals ---");

        OptionalInt created = OptionalInt.of(42);
        System.out.println("OptionalInt.of(42): " + created);

        OptionalInt empty = OptionalInt.empty();
        System.out.println("OptionalInt.empty(): " + empty);

        OptionalLong createdLong = OptionalLong.of(100L);
        System.out.println("OptionalLong.of(100L): " + createdLong);

        OptionalDouble createdDouble = OptionalDouble.of(3.14);
        System.out.println("OptionalDouble.of(3.14): " + createdDouble);

        // =====================================================================
        // Converting Between Optional Types
        // =====================================================================
        System.out.println("\n--- Converting Optional Types ---");

        // OptionalInt -> Optional<Integer>
        OptionalInt primitiveOpt = OptionalInt.of(42);
        Optional<Integer> boxedOpt = primitiveOpt.isPresent()
            ? Optional.of(primitiveOpt.getAsInt())
            : Optional.empty();
        System.out.println("OptionalInt -> Optional<Integer>: " + boxedOpt);

        // Optional<Integer> -> OptionalInt
        Optional<Integer> objectOpt = Optional.of(100);
        OptionalInt primitiveFromObject = objectOpt
            .map(i -> OptionalInt.of(i))
            .orElse(OptionalInt.empty());
        System.out.println("Optional<Integer> -> OptionalInt: " + primitiveFromObject);

        // =====================================================================
        // EXAM PRACTICE
        // =====================================================================
        System.out.println("\n--- EXAM PRACTICE: Primitive Optionals ---");
        System.out.println();
        System.out.println("Q1: What method to get value from OptionalInt?");
        System.out.println("    Answer: getAsInt() (NOT get()!)");
        System.out.println();
        System.out.println("Q2: What's the return type of IntStream.average()?");
        System.out.println("    Answer: OptionalDouble (NOT OptionalInt!)");
        System.out.println();
        System.out.println("Q3: What's the return type of IntStream.sum()?");
        System.out.println("    Answer: int (NOT OptionalInt - returns 0 if empty)");
        System.out.println();
        System.out.println("Q4: Does OptionalInt have map() method?");
        System.out.println("    Answer: NO! Primitive optionals don't have map/flatMap/filter");
        System.out.println();
        System.out.println("Q5: What does IntStream.empty().max().orElse(-1) return?");
        System.out.println("    Answer: -1 (empty stream -> empty optional -> orElse value)");

        System.out.println();
    }

    // =====================================================================
    // COMMON PATTERNS
    // =====================================================================

    public static void demonstrateCommonPatterns() {
        System.out.println("=== COMMON PATTERNS ===\n");

        // Pattern 1: Sum of object property
        System.out.println("--- Pattern 1: Sum of object property ---");
        record Product(String name, int price) {}
        List<Product> products = List.of(
            new Product("Apple", 100),
            new Product("Banana", 50),
            new Product("Cherry", 150)
        );

        int totalPrice = products.stream()
            .mapToInt(Product::price)  // Stream<Product> -> IntStream
            .sum();
        System.out.println("Total price: " + totalPrice);

        // Pattern 2: Average with default
        System.out.println("\n--- Pattern 2: Average with default ---");
        double avgPrice = products.stream()
            .mapToInt(Product::price)
            .average()
            .orElse(0.0);  // default if empty
        System.out.println("Average price: " + avgPrice);

        // Pattern 3: Count in range
        System.out.println("\n--- Pattern 3: Count elements in range ---");
        long count = IntStream.rangeClosed(1, 100)
            .filter(n -> n % 7 == 0)  // divisible by 7
            .count();
        System.out.println("Numbers 1-100 divisible by 7: " + count);

        // Pattern 4: Generate sequence and collect
        System.out.println("\n--- Pattern 4: Generate and collect ---");
        List<Integer> squares = IntStream.rangeClosed(1, 5)
            .map(n -> n * n)
            .boxed()
            .toList();
        System.out.println("Squares 1-5: " + squares);

        // Pattern 5: Statistics in one pass
        System.out.println("\n--- Pattern 5: All statistics in one pass ---");
        IntSummaryStatistics stats = products.stream()
            .mapToInt(Product::price)
            .summaryStatistics();
        System.out.println("Price stats: count=" + stats.getCount() +
            ", sum=" + stats.getSum() +
            ", min=" + stats.getMin() +
            ", max=" + stats.getMax() +
            ", avg=" + stats.getAverage());

        // Pattern 6: Reduce with primitive
        System.out.println("\n--- Pattern 6: Reduce on primitive stream ---");
        int product = IntStream.rangeClosed(1, 5)
            .reduce(1, (a, b) -> a * b);  // 1 * 2 * 3 * 4 * 5 = 120
        System.out.println("Product of 1-5 (factorial): " + product);

        // Pattern 7: Finding with primitive
        System.out.println("\n--- Pattern 7: Finding in primitive stream ---");
        OptionalInt firstEven = IntStream.of(1, 3, 5, 4, 7, 8)
            .filter(n -> n % 2 == 0)
            .findFirst();
        System.out.println("First even: " + firstEven);

        // Pattern 8: Parallel primitive streams
        System.out.println("\n--- Pattern 8: Parallel sum ---");
        long largeSum = LongStream.rangeClosed(1, 1_000_000)
            .parallel()
            .sum();
        System.out.println("Sum of 1 to 1,000,000: " + largeSum);

        System.out.println();

        // =====================================================================
        // FINAL EXAM REFERENCE TABLE
        // =====================================================================
        System.out.println("===== FINAL QUICK REFERENCE =====\n");

        System.out.println("STREAM CREATION:");
        System.out.println("  IntStream:    of(), range(), rangeClosed(), generate(), iterate()");
        System.out.println("  LongStream:   of(), range(), rangeClosed(), generate(), iterate()");
        System.out.println("  DoubleStream: of(), generate(), iterate() (NO range!)");
        System.out.println();

        System.out.println("TERMINAL OPERATIONS:");
        System.out.println("  sum()              -> primitive (0 if empty)");
        System.out.println("  average()          -> OptionalDouble (ALWAYS Double!)");
        System.out.println("  min()              -> OptionalXxx");
        System.out.println("  max()              -> OptionalXxx");
        System.out.println("  summaryStatistics()-> XxxSummaryStatistics");
        System.out.println();

        System.out.println("CONVERSIONS:");
        System.out.println("  boxed()            -> Stream<Integer/Long/Double>");
        System.out.println("  mapToObj(fn)       -> Stream<R>");
        System.out.println("  mapToInt/Long/Double -> other primitive stream");
        System.out.println();

        System.out.println("OPTIONALS:");
        System.out.println("  OptionalInt.getAsInt()       (NOT get()!)");
        System.out.println("  OptionalLong.getAsLong()     (NOT get()!)");
        System.out.println("  OptionalDouble.getAsDouble() (NOT get()!)");
        System.out.println("  NO map/flatMap/filter on primitive optionals!");
    }
}
