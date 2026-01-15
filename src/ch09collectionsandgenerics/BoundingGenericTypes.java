package ch09collectionsandgenerics;

import java.util.ArrayList;
import java.util.List;

/**
 * Bounding Generic Types - Wildcards
 *
 * ===== WILDCARD GENERIC TYPE =====
 *
 * A wildcard (?) represents an UNKNOWN generic type.
 *
 * THREE WAYS TO USE WILDCARDS:
 *
 * 1. Unbounded:     ?                - Any type is OK
 * 2. Upper Bounded: ? extends Type   - Type or any subtype
 * 3. Lower Bounded: ? super Type     - Type or any supertype
 *
 * ===== QUICK REFERENCE =====
 *
 * | Wildcard          | Can READ as | Can ADD    | Use Case                    |
 * |-------------------|-------------|------------|-----------------------------|
 * | ?                 | Object      | Nothing*   | Read-only, any type         |
 * | ? extends Type    | Type        | Nothing*   | Read-only, need subtype     |
 * | ? super Type      | Object      | Type       | Write-only, need supertype  |
 *
 * * Technically can add null, and can remove elements, but exam won't test this
 *
 * ===== MEMORY AID: PECS =====
 * Producer Extends, Consumer Super
 * - If you GET/READ from it (produces data): use extends
 * - If you PUT/WRITE to it (consumes data): use super
 */
public class BoundingGenericTypes {

    public static void main(String[] args) {
        demonstrateUnboundedWildcard();
        demonstrateUpperBoundedWildcard();
        demonstrateLowerBoundedWildcard();
        demonstratePassingGenericArguments();
    }

    // =====================================================================
    // UNBOUNDED WILDCARD: ?
    // =====================================================================

    /**
     * Unbounded wildcard (?) means "any type is OK"
     * Use when you don't care about the type, just want to iterate/read
     */
    public static void demonstrateUnboundedWildcard() {
        System.out.println("=== UNBOUNDED WILDCARD: ? ===\n");

        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");

        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);

        // Both work because printList accepts List<?> - any type
        printList(strings);
        printList(integers);
    }

    // List<?> accepts any List regardless of generic type
    // Read elements as Object since we don't know the actual type
    public static void printList(List<?> list) {
        for (Object element : list) {  // Must use Object - type unknown
            System.out.println("Element: " + element);
        }

        // CANNOT ADD to List<?> - type is unknown!
        // list.add("test");     // ERROR: can't add String - might be List<Integer>
        // list.add(123);        // ERROR: can't add Integer - might be List<String>
        // list.add(new Object()); // ERROR: can't add anything (except null)
    }

    // =====================================================================
    // UPPER BOUNDED WILDCARD: ? extends Type
    // =====================================================================

    /**
     * Upper bounded wildcard (? extends Type) means "Type or any subtype"
     *
     * PROBLEM IT SOLVES:
     * Generic types must match exactly - this is ILLEGAL:
     *     ArrayList<Number> list = new ArrayList<Integer>();  // ERROR!
     *
     * Even though Integer IS-A Number, ArrayList<Integer> IS-NOT-A ArrayList<Number>
     * Generics are INVARIANT, not covariant like arrays.
     *
     * SOLUTION - Use wildcard:
     *     List<? extends Number> list = new ArrayList<Integer>();  // OK!
     */
    public static void demonstrateUpperBoundedWildcard() {
        System.out.println("\n=== UPPER BOUNDED WILDCARD: ? extends Type ===\n");

        // ILLEGAL - generic types must match exactly
        // ArrayList<Number> list = new ArrayList<Integer>();  // ERROR!

        // LEGAL - wildcard allows subtypes
        List<? extends Number> numberList = new ArrayList<Integer>();

        // Can also assign List<Double>, List<Long>, etc.
        numberList = new ArrayList<Double>();
        numberList = new ArrayList<Number>();

        // Create actual lists with data
        List<Integer> integers = List.of(1, 2, 3);
        List<Double> doubles = List.of(1.1, 2.2, 3.3);

        // Both work - Integer and Double extend Number
        System.out.println("Sum of integers: " + sumNumbers(integers));
        System.out.println("Sum of doubles: " + sumNumbers(doubles));

        demonstrateUpperBoundImmutability();
    }

    // Accepts List of Number or any subtype (Integer, Double, Long, etc.)
    public static double sumNumbers(List<? extends Number> numbers) {
        double sum = 0;
        for (Number n : numbers) {  // Can read as Number (the upper bound)
            sum += n.doubleValue();
        }
        return sum;
    }

    // ===== WHY UPPER BOUNDED LISTS ARE IMMUTABLE =====

    public static void demonstrateUpperBoundImmutability() {
        System.out.println("\nWhy ? extends is immutable:");

        List<? extends Bird> birds = new ArrayList<Sparrow>();

        // CAN read as Bird (the upper bound)
        // Bird b = birds.get(0);  // Would work if list had elements

        // CANNOT add anything!
        // birds.add(new Bird());     // ERROR!
        // birds.add(new Sparrow());  // ERROR!

        // WHY? Java doesn't know the actual type:
        // - If birds is really List<Sparrow>, adding Bird would be wrong
        // - If birds is really List<Bird>, adding Sparrow might be OK
        // - Java can't tell at compile time, so it forbids ALL adds

        System.out.println("List<? extends Bird> - cannot add Bird or Sparrow");
        System.out.println("Java doesn't know if it's List<Bird> or List<Sparrow>");
    }

    // =====================================================================
    // LOWER BOUNDED WILDCARD: ? super Type
    // =====================================================================

    /**
     * Lower bounded wildcard (? super Type) means "Type or any supertype"
     *
     * USE CASE: When you need to ADD elements to a collection
     *
     * PROBLEM: We want a method that adds "quack" to both:
     *   - List<String>
     *   - List<Object>
     *
     * Let's see why other approaches fail...
     */
    public static void demonstrateLowerBoundedWildcard() {
        System.out.println("\n=== LOWER BOUNDED WILDCARD: ? super Type ===\n");

        List<String> strings = new ArrayList<>();
        List<Object> objects = new ArrayList<>();

        // APPROACH 1: List<?> - FAILS
        // addQuackUnbounded(strings);  // Would compile...
        // But inside the method, we CAN'T add because type is unknown

        // APPROACH 2: List<? extends Object> - FAILS
        // Same problem - list is immutable

        // APPROACH 3: List<Object> - FAILS
        // addQuackObject(objects);  // Works
        // addQuackObject(strings);  // ERROR! List<String> is not List<Object>

        // APPROACH 4: List<? super String> - WORKS!
        addQuack(strings);  // OK: String is exactly String
        addQuack(objects);  // OK: Object is supertype of String

        System.out.println("strings: " + strings);
        System.out.println("objects: " + objects);

        demonstrateLowerBoundSafety();
    }

    // FAILS: Can't add to List<?> - type unknown
    public static void addQuackUnbounded(List<?> list) {
        // list.add("quack");  // ERROR: can't add String to unknown type
    }

    // FAILS: Can't add to List<? extends Object> - immutable
    public static void addQuackExtends(List<? extends Object> list) {
        // list.add("quack");  // ERROR: upper bounded = immutable
    }

    // FAILS: Can't pass List<String> to List<Object> parameter
    public static void addQuackObject(List<Object> list) {
        list.add("quack");  // Works inside method...
        // But can't call with List<String>!
    }

    // WORKS: List<? super String> accepts String or any supertype
    public static void addQuack(List<? super String> list) {
        list.add("quack");  // SAFE! Guaranteed to accept String
    }

    // ===== WHY LOWER BOUNDED IS SAFE FOR ADDING =====

    public static void demonstrateLowerBoundSafety() {
        System.out.println("\nWhy ? super String is safe for adding String:");

        // List<? super String> could be:
        // - List<String>   -> can add String? YES
        // - List<Object>   -> can add String? YES (String IS-A Object)
        // - List<CharSequence> -> can add String? YES (String IS-A CharSequence)

        // No matter which supertype, adding String is ALWAYS safe!

        List<? super String> list1 = new ArrayList<String>();
        List<? super String> list2 = new ArrayList<Object>();
        List<? super String> list3 = new ArrayList<CharSequence>();

        list1.add("safe");  // OK
        list2.add("safe");  // OK
        list3.add("safe");  // OK

        // BUT reading is limited to Object (the ultimate supertype)
        Object obj = list1.get(0);  // Can only read as Object
        // String s = list1.get(0);  // ERROR: might not be String

        System.out.println("Adding String to List<? super String> is always safe");
        System.out.println("But reading returns Object (most general type)");
    }

    // =====================================================================
    // PASSING GENERIC ARGUMENTS - EXAMPLES
    // =====================================================================

    public static void demonstratePassingGenericArguments() {
        System.out.println("\n=== PASSING GENERIC ARGUMENTS ===\n");

        // Create lists of different types
        List<Integer> integers = new ArrayList<>(List.of(1, 2, 3));
        List<Double> doubles = new ArrayList<>(List.of(1.5, 2.5));
        List<Number> numbers = new ArrayList<>(List.of(10, 20.5));
        List<Object> objects = new ArrayList<>(List.of("a", 1, 2.0));
        List<String> strings = new ArrayList<>(List.of("hello", "world"));

        // ===== UNBOUNDED: ? =====
        System.out.println("--- Unbounded ? ---");
        // Accepts ANY list
        countElements(integers);  // OK
        countElements(doubles);   // OK
        countElements(strings);   // OK
        countElements(objects);   // OK

        // ===== UPPER BOUNDED: ? extends Number =====
        System.out.println("\n--- Upper Bounded ? extends Number ---");
        // Accepts Number or subtypes (Integer, Double, etc.)
        printNumbers(integers);  // OK: Integer extends Number
        printNumbers(doubles);   // OK: Double extends Number
        printNumbers(numbers);   // OK: Number is Number
        // printNumbers(objects);  // ERROR: Object is NOT a Number
        // printNumbers(strings);  // ERROR: String is NOT a Number

        // ===== LOWER BOUNDED: ? super Integer =====
        System.out.println("\n--- Lower Bounded ? super Integer ---");
        // Accepts Integer or supertypes (Number, Object)
        addInteger(integers);  // OK: Integer is Integer
        addInteger(numbers);   // OK: Number is supertype of Integer
        addInteger(objects);   // OK: Object is supertype of Integer
        // addInteger(doubles);  // ERROR: Double is NOT supertype of Integer
        // addInteger(strings);  // ERROR: String is NOT supertype of Integer

        System.out.println("\nintegers after addInteger: " + integers);
        System.out.println("numbers after addInteger: " + numbers);
        System.out.println("objects after addInteger: " + objects);
    }

    // Unbounded - accepts any list, read-only
    public static void countElements(List<?> list) {
        System.out.println("Count: " + list.size());
    }

    // Upper bounded - accepts Number or subtypes, read-only
    public static void printNumbers(List<? extends Number> list) {
        for (Number n : list) {
            System.out.print(n + " ");
        }
        System.out.println();
    }

    // Lower bounded - accepts Integer or supertypes, can add Integer
    public static void addInteger(List<? super Integer> list) {
        list.add(99);  // Safe: all supertypes of Integer accept Integer
    }
}

// =====================================================================
// HELPER CLASSES FOR EXAMPLES
// =====================================================================

class Bird {
    String name = "Bird";
}

class Sparrow extends Bird {
    String name = "Sparrow";
}

// =====================================================================
// COMPILATION EXAMPLES - WHAT WORKS AND WHAT DOESN'T
// =====================================================================

/**
 * EXAM PRACTICE: Which method calls compile?
 */
class WildcardExamples {

    // Methods with different wildcard bounds
    static void unbounded(List<?> list) {}
    static void upperBounded(List<? extends Number> list) {}
    static void lowerBounded(List<? super Integer> list) {}
    static void exactType(List<Number> list) {}

    public static void main(String[] args) {
        List<Integer> integers = new ArrayList<>();
        List<Double> doubles = new ArrayList<>();
        List<Number> numbers = new ArrayList<>();
        List<Object> objects = new ArrayList<>();
        List<String> strings = new ArrayList<>();

        // ===== unbounded(List<?>) - accepts ANYTHING =====
        unbounded(integers);  // OK
        unbounded(doubles);   // OK
        unbounded(numbers);   // OK
        unbounded(objects);   // OK
        unbounded(strings);   // OK

        // ===== upperBounded(List<? extends Number>) - Number or subtype =====
        upperBounded(integers);  // OK: Integer extends Number
        upperBounded(doubles);   // OK: Double extends Number
        upperBounded(numbers);   // OK: Number is Number
        // upperBounded(objects);  // ERROR: Object does NOT extend Number
        // upperBounded(strings);  // ERROR: String does NOT extend Number

        // ===== lowerBounded(List<? super Integer>) - Integer or supertype =====
        lowerBounded(integers);  // OK: Integer is Integer
        // lowerBounded(doubles);  // ERROR: Double is NOT supertype of Integer
        lowerBounded(numbers);   // OK: Number is supertype of Integer
        lowerBounded(objects);   // OK: Object is supertype of Integer
        // lowerBounded(strings);  // ERROR: String is NOT supertype of Integer

        // ===== exactType(List<Number>) - ONLY Number, not subtypes! =====
        // exactType(integers);  // ERROR: List<Integer> is NOT List<Number>
        // exactType(doubles);   // ERROR: List<Double> is NOT List<Number>
        exactType(numbers);      // OK: exact match
        // exactType(objects);   // ERROR: List<Object> is NOT List<Number>
        // exactType(strings);   // ERROR: List<String> is NOT List<Number>
    }
}

// =====================================================================
// SUMMARY: WHAT CAN YOU DO WITH EACH WILDCARD?
// =====================================================================

/**
 * UNBOUNDED: List<?>
 * - CAN pass: Any List<T>
 * - CAN read: As Object
 * - CANNOT add: Anything (except null)
 * - USE: When you only need to read and don't care about type
 *
 * UPPER BOUNDED: List<? extends Type>
 * - CAN pass: List<Type> or List<Subtype>
 * - CAN read: As Type (the bound)
 * - CANNOT add: Anything (except null)
 * - USE: When you need to READ and need specific type methods
 *
 * LOWER BOUNDED: List<? super Type>
 * - CAN pass: List<Type> or List<Supertype>
 * - CAN read: As Object only
 * - CAN add: Type and subtypes
 * - USE: When you need to WRITE/ADD elements
 *
 * EXACT TYPE: List<Type>
 * - CAN pass: Only List<Type> (exact match)
 * - CAN read: As Type
 * - CAN add: Type
 * - USE: When you need both read and write with specific type
 */
class WildcardSummary {

    static void unboundedExample(List<?> list) {
        Object o = list.get(0);  // Read as Object
        // list.add("x");        // ERROR: cannot add
    }

    static void upperExample(List<? extends Number> list) {
        Number n = list.get(0);  // Read as Number (the bound)
        // list.add(1);          // ERROR: cannot add
    }

    static void lowerExample(List<? super Integer> list) {
        Object o = list.get(0);  // Read as Object only
        list.add(1);             // OK: can add Integer
        list.add(100);           // OK: can add Integer
    }

    static void exactExample(List<Integer> list) {
        Integer i = list.get(0); // Read as Integer
        list.add(1);             // OK: can add Integer
    }
}

// =====================================================================
// WHERE WILDCARDS CAN AND CANNOT BE USED
// =====================================================================

/**
 * WILDCARDS IN RETURN TYPES AND DECLARATIONS
 *
 * ? can appear INSIDE a parameterized type (as part of it)
 * ? CANNOT be the entire type by itself
 *
 * VALID - Wildcard inside parameterized type:
 *   - Return type:    List<?> getList()
 *   - Parameter:      void process(List<? extends Number> list)
 *   - Variable:       List<? super String> list = ...
 *
 * INVALID - Wildcard alone or in class declaration:
 *   - Return type:    ? getItem()           // ERROR: ? is not a type
 *   - Class generic:  class Box<?> {}       // ERROR: need named type parameter
 *   - Class generic:  class Box<? extends Number> {}  // ERROR
 *
 * For class/method type parameters, use a NAME with optional bound:
 *   - class Box<T> {}                  // Unbounded type parameter
 *   - class Box<T extends Number> {}   // Bounded type parameter
 *   - <T> T getItem()                  // Method type parameter
 *   - <T extends Number> T getNum()    // Bounded method type parameter
 */
class WildcardPlacement {

    // VALID: Wildcard in return type (inside parameterized type)
    public List<?> getAnyList() {
        return new ArrayList<String>();
    }

    public List<? extends Number> getNumberList() {
        return new ArrayList<Integer>();
    }

    public List<? super String> getStringConsumer() {
        return new ArrayList<Object>();
    }

    // INVALID: Wildcard as the entire return type
    // public ? getUnknown() { }  // ERROR: '?' is not a valid type

    // INVALID: Wildcard in class type parameter declaration
    // class BadBox<?> { }                    // ERROR
    // class BadBox<? extends Number> { }     // ERROR

    // VALID: Named type parameter with bound (use this instead!)
    // class GoodBox<T> { }
    // class GoodBox<T extends Number> { }
}

/**
 * EXAM TIP: Wildcard vs Type Parameter
 *
 * Use WILDCARD (?) when:
 *   - Declaring variables: List<?> list
 *   - Method parameters: void foo(List<? extends Number> list)
 *   - Return types: List<?> getList()
 *
 * Use TYPE PARAMETER (T, E, K, V, etc.) when:
 *   - Declaring a generic class: class Box<T> {}
 *   - Declaring a generic interface: interface Container<E> {}
 *   - Declaring a generic method: <T> T identity(T item)
 *
 * KEY DIFFERENCE:
 *   - Type parameter (T): You can reference it multiple times
 *       <T> void copy(List<T> src, List<T> dest)  // Same T in both
 *   - Wildcard (?): Each ? is independent
 *       void copy(List<?> src, List<?> dest)  // Could be different types!
 */
