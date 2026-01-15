package ch10streams;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

/**
 * The reduce() Operation
 *
 * reduce() combines a stream into a single value.
 * - Infinite stream: NEVER TERMINATES
 * - Reduction: YES (by definition!)
 *
 * ===== THREE SIGNATURES =====
 *
 * 1. T reduce(T identity, BinaryOperator<T> accumulator)
 *    - Has identity (starting value)
 *    - Returns T directly (never null, uses identity if empty)
 *
 * 2. Optional<T> reduce(BinaryOperator<T> accumulator)
 *    - No identity
 *    - Returns Optional<T> (empty if stream is empty)
 *
 * 3. <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner)
 *    - Different types: stream elements (T) vs result (U)
 *    - Used when accumulating into a different type
 *    - Combiner merges parallel results
 *
 * ===== SIGNATURE COMPARISON =====
 *
 * | Signature | Identity | Return Type  | Same Types? | Use Case                    |
 * |-----------|----------|--------------|-------------|-----------------------------|
 * | 1         | Yes      | T            | Yes         | Simple reduction, same type |
 * | 2         | No       | Optional<T>  | Yes         | When no good identity value |
 * | 3         | Yes      | U            | No (T -> U) | Different result type       |
 *
 * ===== HOW REDUCE WORKS =====
 *
 * Think: Start with identity, then repeatedly combine with each element
 *
 * Example: reduce(0, (a, b) -> a + b) on [1, 2, 3]
 *   Step 1: 0 + 1 = 1
 *   Step 2: 1 + 2 = 3
 *   Step 3: 3 + 3 = 6
 *   Result: 6
 */
public class ReduceOperation {

    public static void main(String[] args) {
        demonstrateWithoutFunctional();
        demonstrateSignature1();
        demonstrateSignature2();
        demonstrateSignature3();
        examPractice();
    }

    // =====================================================================
    // REDUCTION WITHOUT FUNCTIONAL PROGRAMMING (comparison)
    // =====================================================================

    public static void demonstrateWithoutFunctional() {
        System.out.println("=== REDUCTION: IMPERATIVE VS FUNCTIONAL ===\n");

        List<String> letters = List.of("w", "o", "l", "f");

        // IMPERATIVE approach - manual concatenation
        String result = "";
        for (String letter : letters) {
            result = result + letter;
        }
        System.out.println("Imperative: " + result);

        // FUNCTIONAL approach - reduce()
        String functional = letters.stream()
            .reduce("", (s1, s2) -> s1 + s2);
        System.out.println("Functional: " + functional);

        // With method reference
        String withMethodRef = letters.stream()
            .reduce("", String::concat);
        System.out.println("Method ref: " + withMethodRef);
    }

    // =====================================================================
    // SIGNATURE 1: T reduce(T identity, BinaryOperator<T> accumulator)
    // =====================================================================

    /**
     * T reduce(T identity, BinaryOperator<T> accumulator)
     *
     * - identity: Starting value / default value if stream is empty
     * - accumulator: Combines current result with current element
     * - Returns: T (the accumulated result, or identity if empty)
     *
     * BOTH parameters and return are same type T!
     *
     * BinaryOperator<T> is (T, T) -> T
     *   - First T: accumulated result so far
     *   - Second T: current stream element
     *   - Returns T: new accumulated result
     */
    public static void demonstrateSignature1() {
        System.out.println("\n=== SIGNATURE 1: T reduce(T identity, BinaryOperator<T>) ===\n");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5);

        // Sum with identity 0
        // identity = 0, accumulator = (a, b) -> a + b
        Integer sum = numbers.stream()
            .reduce(0, (a, b) -> a + b);
        System.out.println("Sum (identity=0): " + sum);  // 15

        // Product with identity 1
        Integer product = numbers.stream()
            .reduce(1, (a, b) -> a * b);
        System.out.println("Product (identity=1): " + product);  // 120

        // String concatenation
        List<String> words = List.of("Hello", " ", "World");
        String concat = words.stream()
            .reduce("", String::concat);
        System.out.println("Concat (identity=\"\"): " + concat);

        // EMPTY STREAM returns identity
        Integer emptySum = Stream.<Integer>empty()
            .reduce(0, (a, b) -> a + b);
        System.out.println("\nEmpty stream with identity 0: " + emptySum);  // 0

        // Find max (less common, usually use max() instead)
        Integer max = numbers.stream()
            .reduce(Integer.MIN_VALUE, Integer::max);
        System.out.println("Max: " + max);

        // HOW IT WORKS step by step:
        System.out.println("\nStep by step for [1,2,3].reduce(0, +):");
        System.out.println("  Start: identity = 0");
        System.out.println("  Step 1: 0 + 1 = 1");
        System.out.println("  Step 2: 1 + 2 = 3");
        System.out.println("  Step 3: 3 + 3 = 6");
        System.out.println("  Result: 6");
    }

    // =====================================================================
    // SIGNATURE 2: Optional<T> reduce(BinaryOperator<T> accumulator)
    // =====================================================================

    /**
     * Optional<T> reduce(BinaryOperator<T> accumulator)
     *
     * - NO identity value
     * - Returns Optional<T> because stream might be empty
     *
     * Behavior:
     * - Empty stream: Returns Optional.empty()
     * - One element: Returns Optional of that element (accumulator not called!)
     * - Multiple elements: Applies accumulator, returns Optional of result
     */
    public static void demonstrateSignature2() {
        System.out.println("\n=== SIGNATURE 2: Optional<T> reduce(BinaryOperator<T>) ===\n");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5);

        // Sum without identity - returns Optional
        Optional<Integer> sum = numbers.stream()
            .reduce((a, b) -> a + b);
        System.out.println("Sum: " + sum.orElse(0));

        // Empty stream - returns Optional.empty()
        Optional<Integer> emptySum = Stream.<Integer>empty()
            .reduce((a, b) -> a + b);
        System.out.println("Empty stream: " + emptySum);  // Optional.empty

        // Single element - returns that element (accumulator NOT called)
        Optional<Integer> single = Stream.of(42)
            .reduce((a, b) -> a + b);
        System.out.println("Single element: " + single);  // Optional[42]

        // Multiple elements
        Optional<String> concat = Stream.of("a", "b", "c")
            .reduce((s1, s2) -> s1 + s2);
        System.out.println("Concatenated: " + concat.orElse(""));  // abc

        // WHEN TO USE: When there's no sensible identity value
        // Example: Finding minimum without knowing the type's min value
        Optional<Integer> min = numbers.stream()
            .reduce(Integer::min);
        System.out.println("\nMin without identity: " + min);
    }

    // =====================================================================
    // SIGNATURE 3: <U> U reduce(U identity, BiFunction, BinaryOperator)
    // =====================================================================

    /**
     * <U> U reduce(U identity,
     *              BiFunction<U, ? super T, U> accumulator,
     *              BinaryOperator<U> combiner)
     *
     * Used when stream type (T) differs from result type (U)
     *
     * TYPE BREAKDOWN:
     * - T = Stream element type
     * - U = Result type (different from T!)
     *
     * Parameters:
     * - identity (U): Starting value of result type
     * - accumulator BiFunction<U, T, U>: Combines result (U) with element (T) -> U
     *     First param (U): accumulated result so far
     *     Second param (T): current stream element
     *     Returns (U): new accumulated result
     * - combiner BinaryOperator<U>: Combines two U results (for parallel streams)
     *     (U, U) -> U
     *
     * ===== MEMORY AID FOR TYPES =====
     *
     * Stream<T> ---reduce---> U
     *
     * identity:    U          (what type is the result? that's U)
     * accumulator: (U, T) -> U (take result + element, produce new result)
     * combiner:    (U, U) -> U (combine two results - same type!)
     */
    public static void demonstrateSignature3() {
        System.out.println("\n=== SIGNATURE 3: <U> U reduce(U, BiFunction, BinaryOperator) ===\n");

        // EXAMPLE: Sum the LENGTH of strings (Stream<String> -> Integer)
        // T = String (stream element type)
        // U = Integer (result type)

        Stream<String> stream = Stream.of("w", "o", "l", "f");

        int length = stream.reduce(
            0,                              // identity: U (Integer) - start at 0
            (i, s) -> i + s.length(),       // accumulator: (U, T) -> U = (Integer, String) -> Integer
            (a, b) -> a + b                 // combiner: (U, U) -> U = (Integer, Integer) -> Integer
        );

        System.out.println("Total length of [w,o,l,f]: " + length);  // 4

        // STEP BY STEP:
        System.out.println("\nStep by step:");
        System.out.println("  Stream type T = String");
        System.out.println("  Result type U = Integer");
        System.out.println("  identity = 0 (Integer)");
        System.out.println("  accumulator (i, s) -> i + s.length()");
        System.out.println("    i is Integer (accumulated result)");
        System.out.println("    s is String (stream element)");
        System.out.println("  combiner (a, b) -> a + b");
        System.out.println("    Both a and b are Integer (combines parallel results)");

        // ANOTHER EXAMPLE: Count characters in list of strings
        List<String> words = List.of("Hello", "World", "!");

        int totalChars = words.stream().reduce(
            0,                              // U = Integer
            (count, word) -> count + word.length(),  // (Integer, String) -> Integer
            Integer::sum                    // (Integer, Integer) -> Integer
        );
        System.out.println("\nTotal chars in [Hello, World, !]: " + totalChars);  // 11

        // EXAMPLE: Build a StringBuilder from integers
        // T = Integer, U = StringBuilder
        List<Integer> nums = List.of(1, 2, 3);

        StringBuilder sb = nums.stream().reduce(
            new StringBuilder(),            // identity: StringBuilder
            (builder, num) -> builder.append(num),  // (StringBuilder, Integer) -> StringBuilder
            (b1, b2) -> b1.append(b2)       // (StringBuilder, StringBuilder) -> StringBuilder
        );
        System.out.println("\nStringBuilder from [1,2,3]: " + sb);  // 123
    }

    // =====================================================================
    // EXAM PRACTICE - TYPE ANALYSIS
    // =====================================================================

    public static void examPractice() {
        System.out.println("\n=== EXAM PRACTICE: ANALYZING TYPES ===\n");

        // TECHNIQUE: Look at identity to determine U, look at stream to determine T

        // EXAMPLE 1
        System.out.println("EXAMPLE 1:");
        System.out.println("  Stream<String> s = Stream.of(\"a\", \"bb\", \"ccc\");");
        System.out.println("  int x = s.reduce(0, (n, str) -> n + str.length(), Integer::sum);");
        System.out.println("\n  Analysis:");
        System.out.println("  - Stream<String> means T = String");
        System.out.println("  - identity is 0 (int), so U = Integer");
        System.out.println("  - accumulator: (n, str) -> n + str.length()");
        System.out.println("    - n is U (Integer) - the accumulated result");
        System.out.println("    - str is T (String) - the stream element");
        System.out.println("  - combiner: Integer::sum is (Integer, Integer) -> Integer");
        System.out.println("  - Result: 6 (1 + 2 + 3)");

        int ex1 = Stream.of("a", "bb", "ccc")
            .reduce(0, (n, str) -> n + str.length(), Integer::sum);
        System.out.println("  Actual result: " + ex1);

        // EXAMPLE 2
        System.out.println("\nEXAMPLE 2:");
        System.out.println("  Stream<Integer> s = Stream.of(1, 2, 3);");
        System.out.println("  String x = s.reduce(\"\", (str, n) -> str + n, String::concat);");
        System.out.println("\n  Analysis:");
        System.out.println("  - Stream<Integer> means T = Integer");
        System.out.println("  - identity is \"\" (String), so U = String");
        System.out.println("  - accumulator: (str, n) -> str + n");
        System.out.println("    - str is U (String) - the accumulated result");
        System.out.println("    - n is T (Integer) - the stream element");
        System.out.println("  - combiner: String::concat is (String, String) -> String");
        System.out.println("  - Result: \"123\"");

        String ex2 = Stream.of(1, 2, 3)
            .reduce("", (str, n) -> str + n, String::concat);
        System.out.println("  Actual result: " + ex2);

        // EXAMPLE 3 - DOES THIS COMPILE?
        System.out.println("\nEXAMPLE 3: Does this compile?");
        System.out.println("  Stream.of(\"a\", \"b\").reduce(0, (s1, s2) -> s1 + s2, Integer::sum);");
        System.out.println("\n  Analysis:");
        System.out.println("  - Stream<String> means T = String");
        System.out.println("  - identity is 0, so U = Integer");
        System.out.println("  - accumulator: (s1, s2) -> s1 + s2");
        System.out.println("    - s1 should be U (Integer)");
        System.out.println("    - s2 should be T (String)");
        System.out.println("    - s1 + s2 would be String concat, but we need Integer!");
        System.out.println("  - DOES NOT COMPILE! accumulator types don't match");

        // Correct version:
        // int result = Stream.of("a", "b").reduce(0, (i, s) -> i + s.length(), Integer::sum);

        // EXAMPLE 4 - TRICKY ONE
        System.out.println("\nEXAMPLE 4: Tricky type inference");
        System.out.println("  var x = Stream.of(1.0, 2.0).reduce(0, (a, b) -> a + b, Integer::sum);");
        System.out.println("\n  Analysis:");
        System.out.println("  - Stream<Double> means T = Double");
        System.out.println("  - identity is 0 (int literal), so U = Integer");
        System.out.println("  - accumulator: (a, b) -> a + b");
        System.out.println("    - a is U (Integer)");
        System.out.println("    - b is T (Double)");
        System.out.println("    - Integer + Double = Double (but we need Integer!)");
        System.out.println("  - DOES NOT COMPILE! accumulator return type is wrong");

        // Correct version would need explicit cast:
        // int result = Stream.of(1.0, 2.0).reduce(0, (a, b) -> (int)(a + b), Integer::sum);
    }
}

// =====================================================================
// QUICK REFERENCE
// =====================================================================

/**
 * REDUCE SIGNATURES:
 *
 * 1. T reduce(T identity, BinaryOperator<T> accumulator)
 *    - Same type throughout
 *    - Returns T directly
 *
 * 2. Optional<T> reduce(BinaryOperator<T> accumulator)
 *    - No identity
 *    - Returns Optional<T>
 *
 * 3. <U> U reduce(U identity, BiFunction<U,? super T,U> accumulator, BinaryOperator<U> combiner)
 *    - Different types: T (stream) -> U (result)
 *    - identity: U
 *    - accumulator: (U, T) -> U
 *    - combiner: (U, U) -> U
 *
 * ===== TYPE ANALYSIS TECHNIQUE =====
 *
 * 1. What is T? Look at Stream<T>
 * 2. What is U? Look at identity type
 * 3. Accumulator first param is U (result so far)
 * 4. Accumulator second param is T (stream element)
 * 5. Accumulator returns U
 * 6. Combiner takes two U's and returns U
 *
 * ===== EXAM TRAPS =====
 *
 * - Wrong accumulator types: (T, T) instead of (U, T)
 * - Wrong combiner types: must be (U, U) -> U
 * - Accumulator must return U, not T
 * - Identity must be type U
 * - Don't confuse which param is the accumulated result vs stream element
 *
 * ===== COMMON PATTERNS =====
 *
 * Sum:       reduce(0, (a, b) -> a + b) or reduce(0, Integer::sum)
 * Product:   reduce(1, (a, b) -> a * b)
 * Concat:    reduce("", String::concat) or reduce("", (a, b) -> a + b)
 * Max:       reduce(Integer.MIN_VALUE, Integer::max)
 * Min:       reduce(Integer.MAX_VALUE, Integer::min)
 * Length sum: reduce(0, (n, s) -> n + s.length(), Integer::sum)
 */
