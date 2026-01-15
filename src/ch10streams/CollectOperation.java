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
 *    - supplier: Creates the mutable container (e.g., ArrayList::new)
 *    - accumulator: Adds one element to the container
 *    - combiner: Merges two containers (for parallel streams)
 *
 * 2. <R, A> R collect(Collector<? super T, A, R> collector)
 *
 *    - Uses predefined Collectors from java.util.stream.Collectors
 *    - Much more convenient for common operations!
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
    }

    // =====================================================================
    // minBy(), maxBy()
    // =====================================================================

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
            .collect(Collectors.minBy(Comparator.naturalOrder()));
        System.out.println("minBy(natural): " + first);  // apple

        Optional<String> last = words.stream()
            .collect(Collectors.maxBy(Comparator.naturalOrder()));
        System.out.println("maxBy(natural): " + last);   // pie
    }

    // =====================================================================
    // mapping(), filtering(), flatMapping()
    // =====================================================================

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

    public static void demonstrateGroupingBy() {
        System.out.println("\n=== groupingBy() - VERY IMPORTANT! ===\n");

        List<String> words = List.of("apple", "apricot", "banana", "cherry", "avocado", "blueberry");

        // ----- groupingBy(classifier) -----
        // Groups elements by classifier function
        // Returns Map<K, List<T>> - key is classifier result, value is List of elements

        // Group by first character
        Map<Character, List<String>> byFirstChar = words.stream()
            .collect(Collectors.groupingBy(s -> s.charAt(0)));
        System.out.println("groupingBy(firstChar): " + byFirstChar);
        // {a=[apple, apricot, avocado], b=[banana, blueberry], c=[cherry]}

        // Group by length
        Map<Integer, List<String>> byLength = words.stream()
            .collect(Collectors.groupingBy(String::length));
        System.out.println("groupingBy(length): " + byLength);

        // ----- groupingBy(classifier, downstream) -----
        // Apply another collector to grouped elements

        // Group by first char, count elements in each group
        Map<Character, Long> countByFirstChar = words.stream()
            .collect(Collectors.groupingBy(
                s -> s.charAt(0),          // classifier
                Collectors.counting()      // downstream collector
            ));
        System.out.println("\ngroupingBy + counting: " + countByFirstChar);

        // Group by first char, get Set instead of List
        Map<Character, Set<String>> setByFirstChar = words.stream()
            .collect(Collectors.groupingBy(
                s -> s.charAt(0),
                Collectors.toSet()
            ));
        System.out.println("groupingBy + toSet: " + setByFirstChar);

        // Group by length, join words in each group
        Map<Integer, String> joinedByLength = words.stream()
            .collect(Collectors.groupingBy(
                String::length,
                Collectors.joining(", ")
            ));
        System.out.println("groupingBy + joining: " + joinedByLength);

        // Group by first char, sum lengths
        Map<Character, Integer> sumLengthByFirstChar = words.stream()
            .collect(Collectors.groupingBy(
                s -> s.charAt(0),
                Collectors.summingInt(String::length)
            ));
        System.out.println("groupingBy + summingInt: " + sumLengthByFirstChar);

        // Group by first char, average length
        Map<Character, Double> avgLengthByFirstChar = words.stream()
            .collect(Collectors.groupingBy(
                s -> s.charAt(0),
                Collectors.averagingInt(String::length)
            ));
        System.out.println("groupingBy + averagingInt: " + avgLengthByFirstChar);

        // Group by first char, get longest word
        Map<Character, Optional<String>> longestByFirstChar = words.stream()
            .collect(Collectors.groupingBy(
                s -> s.charAt(0),
                Collectors.maxBy(Comparator.comparingInt(String::length))
            ));
        System.out.println("groupingBy + maxBy: " + longestByFirstChar);

        // ----- groupingBy(classifier, mapFactory, downstream) -----
        // Specify Map implementation

        TreeMap<Character, List<String>> treeMapGroup = words.stream()
            .collect(Collectors.groupingBy(
                s -> s.charAt(0),
                TreeMap::new,              // use TreeMap (sorted keys)
                Collectors.toList()
            ));
        System.out.println("\ngroupingBy to TreeMap: " + treeMapGroup);

        // ----- NESTED GROUPING -----
        System.out.println("\n--- Nested groupingBy ---");

        // Group by first char, then by length
        Map<Character, Map<Integer, List<String>>> nestedGroup = words.stream()
            .collect(Collectors.groupingBy(
                s -> s.charAt(0),
                Collectors.groupingBy(String::length)
            ));
        System.out.println("Nested grouping: " + nestedGroup);

        // ----- mapping() inside groupingBy -----
        System.out.println("\n--- mapping inside groupingBy ---");

        // Group by length, get uppercase versions
        Map<Integer, List<String>> upperByLength = words.stream()
            .collect(Collectors.groupingBy(
                String::length,
                Collectors.mapping(
                    String::toUpperCase,
                    Collectors.toList()
                )
            ));
        System.out.println("groupingBy + mapping: " + upperByLength);

        // Group by first char, get just the lengths
        Map<Character, List<Integer>> lengthsByFirstChar = words.stream()
            .collect(Collectors.groupingBy(
                s -> s.charAt(0),
                Collectors.mapping(
                    String::length,
                    Collectors.toList()
                )
            ));
        System.out.println("groupingBy + mapping(length): " + lengthsByFirstChar);

        // ----- filtering() inside groupingBy -----
        System.out.println("\n--- filtering inside groupingBy ---");

        // Group by first char, only keep words > 6 chars
        Map<Character, List<String>> filteredGroups = words.stream()
            .collect(Collectors.groupingBy(
                s -> s.charAt(0),
                Collectors.filtering(
                    s -> s.length() > 6,
                    Collectors.toList()
                )
            ));
        System.out.println("groupingBy + filtering(>6): " + filteredGroups);
        // Note: Keys still exist even if their list is empty!
    }

    // =====================================================================
    // partitioningBy() - IMPORTANT FOR EXAM!
    // =====================================================================

    public static void demonstratePartitioningBy() {
        System.out.println("\n=== partitioningBy() - IMPORTANT! ===\n");

        // partitioningBy is a SPECIAL CASE of groupingBy
        // - Always produces Map<Boolean, ...>
        // - Always has exactly TWO keys: true and false
        // - Even if no elements match predicate, both keys exist!

        List<String> words = List.of("apple", "pie", "banana", "kiwi", "cherry");

        // ----- partitioningBy(predicate) -----
        // Returns Map<Boolean, List<T>>

        Map<Boolean, List<String>> byLength5 = words.stream()
            .collect(Collectors.partitioningBy(s -> s.length() > 4));
        System.out.println("partitioningBy(length > 4):");
        System.out.println("  true: " + byLength5.get(true));
        System.out.println("  false: " + byLength5.get(false));

        // ----- partitioningBy(predicate, downstream) -----
        // Apply collector to each partition

        // Partition and count
        Map<Boolean, Long> countByLength = words.stream()
            .collect(Collectors.partitioningBy(
                s -> s.length() > 4,
                Collectors.counting()
            ));
        System.out.println("\npartitioningBy + counting: " + countByLength);

        // Partition and join
        Map<Boolean, String> joinedByLength = words.stream()
            .collect(Collectors.partitioningBy(
                s -> s.length() > 4,
                Collectors.joining(", ")
            ));
        System.out.println("partitioningBy + joining: " + joinedByLength);

        // Partition and find max
        Map<Boolean, Optional<String>> maxByLength = words.stream()
            .collect(Collectors.partitioningBy(
                s -> s.length() > 4,
                Collectors.maxBy(Comparator.naturalOrder())
            ));
        System.out.println("partitioningBy + maxBy: " + maxByLength);

        // ----- KEY DIFFERENCE from groupingBy -----
        System.out.println("\n--- partitioningBy vs groupingBy ---");

        // partitioningBy ALWAYS has both true and false keys
        List<String> allShort = List.of("a", "bb", "ccc");

        Map<Boolean, List<String>> partitioned = allShort.stream()
            .collect(Collectors.partitioningBy(s -> s.length() > 10));
        System.out.println("partitioningBy (no matches):");
        System.out.println("  true: " + partitioned.get(true));    // [] - empty but exists!
        System.out.println("  false: " + partitioned.get(false));  // [a, bb, ccc]

        // groupingBy would NOT have a key if no elements map to it
        Map<Boolean, List<String>> grouped = allShort.stream()
            .collect(Collectors.groupingBy(s -> s.length() > 10));
        System.out.println("\ngroupingBy same condition:");
        System.out.println("  contains true key? " + grouped.containsKey(true));   // false!
        System.out.println("  contains false key? " + grouped.containsKey(false)); // true
    }

    // =====================================================================
    // ADVANCED COLLECTORS
    // =====================================================================

    public static void demonstrateAdvancedCollectors() {
        System.out.println("\n=== ADVANCED COLLECTORS ===\n");

        List<String> words = List.of("apple", "banana", "cherry");

        // ----- reducing() -----
        // Collector version of reduce()
        System.out.println("--- reducing() ---");

        // reducing(BinaryOperator) - returns Optional
        Optional<String> concat = words.stream()
            .collect(Collectors.reducing((s1, s2) -> s1 + s2));
        System.out.println("reducing(concat): " + concat);

        // reducing(identity, BinaryOperator) - returns T
        String concatWithIdentity = words.stream()
            .collect(Collectors.reducing("", (s1, s2) -> s1 + s2));
        System.out.println("reducing(\"\", concat): " + concatWithIdentity);

        // reducing(identity, mapper, BinaryOperator) - map then reduce
        Integer totalLength = words.stream()
            .collect(Collectors.reducing(
                0,                         // identity
                String::length,            // mapper
                Integer::sum               // reducer
            ));
        System.out.println("reducing(0, length, sum): " + totalLength);

        // ----- collectingAndThen() -----
        System.out.println("\n--- collectingAndThen() ---");

        // Apply finishing transformation after collecting
        // Useful for making result unmodifiable
        List<String> unmodifiable = words.stream()
            .collect(Collectors.collectingAndThen(
                Collectors.toList(),
                Collections::unmodifiableList
            ));
        System.out.println("collectingAndThen(toList, unmodifiable): " + unmodifiable);

        // Get size after collecting
        Integer size = words.stream()
            .collect(Collectors.collectingAndThen(
                Collectors.toList(),
                List::size
            ));
        System.out.println("collectingAndThen(toList, size): " + size);

        // ----- teeing() (Java 12+) -----
        System.out.println("\n--- teeing() ---");

        // Combines results of two collectors
        // teeing(downstream1, downstream2, merger)

        List<Integer> numbers = List.of(1, 2, 3, 4, 5);

        // Calculate both sum and average in one pass
        String sumAndAvg = numbers.stream()
            .collect(Collectors.teeing(
                Collectors.summingInt(n -> n),     // downstream1: sum
                Collectors.averagingInt(n -> n),  // downstream2: average
                (sum, avg) -> "Sum: " + sum + ", Avg: " + avg  // merger
            ));
        System.out.println("teeing(sum, avg): " + sumAndAvg);

        // Get min and max together
        String minMax = numbers.stream()
            .collect(Collectors.teeing(
                Collectors.minBy(Comparator.naturalOrder()),
                Collectors.maxBy(Comparator.naturalOrder()),
                (min, max) -> "Min: " + min.orElse(-1) + ", Max: " + max.orElse(-1)
            ));
        System.out.println("teeing(min, max): " + minMax);

        // ----- MORE TEEING EXAMPLES -----
        System.out.println("\n--- More teeing() examples ---");

        // Count and collect at the same time
        var countAndList = words.stream()
            .collect(Collectors.teeing(
                Collectors.counting(),
                Collectors.toList(),
                (count, list) -> Map.of("count", count, "items", list)
            ));
        System.out.println("teeing(counting, toList): " + countAndList);

        // Partition-like behavior with teeing
        var shortAndLong = words.stream()
            .collect(Collectors.teeing(
                Collectors.filtering(s -> s.length() <= 5, Collectors.toList()),
                Collectors.filtering(s -> s.length() > 5, Collectors.toList()),
                (shortWords, longWords) -> Map.of("short", shortWords, "long", longWords)
            ));
        System.out.println("teeing(short, long): " + shortAndLong);

        // Get first and last element
        var firstAndLast = words.stream()
            .collect(Collectors.teeing(
                Collectors.reducing((a, b) -> a),  // keeps first
                Collectors.reducing((a, b) -> b),  // keeps last
                (first, last) -> "First: " + first.orElse("") + ", Last: " + last.orElse("")
            ));
        System.out.println("teeing(first, last): " + firstAndLast);

        // Calculate range (max - min)
        Integer range = numbers.stream()
            .collect(Collectors.teeing(
                Collectors.minBy(Comparator.naturalOrder()),
                Collectors.maxBy(Comparator.naturalOrder()),
                (min, max) -> max.orElse(0) - min.orElse(0)
            ));
        System.out.println("teeing to calculate range: " + range);
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
