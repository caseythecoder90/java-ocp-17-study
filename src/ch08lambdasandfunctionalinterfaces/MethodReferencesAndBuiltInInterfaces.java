package ch08lambdasandfunctionalinterfaces;

import java.util.function.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Method References and Built-In Functional Interfaces
 *
 * METHOD REFERENCE FORMATS:
 * 1. Static methods:              ClassName::staticMethod
 * 2. Instance method on object:   instance::instanceMethod
 * 3. Instance method on parameter: ClassName::instanceMethod
 * 4. Constructors:                ClassName::new
 *
 * BUILT-IN FUNCTIONAL INTERFACES (MEMORIZE):
 *
 * Supplier<T>              T get()                         No input, returns T
 * Consumer<T>              void accept(T t)                Takes T, no return
 * BiConsumer<T, U>         void accept(T t, U u)           Takes T and U, no return
 * Predicate<T>             boolean test(T t)               Takes T, returns boolean
 * BiPredicate<T, U>        boolean test(T t, U u)          Takes T and U, returns boolean
 * Function<T, R>           R apply(T t)                    Takes T, returns R
 * BiFunction<T, U, R>      R apply(T t, U u)               Takes T and U, returns R
 * UnaryOperator<T>         T apply(T t)                    Takes T, returns T (extends Function<T, T>)
 * BinaryOperator<T>        T apply(T t1, T t2)             Takes two T, returns T (extends BiFunction<T, T, T>)
 *
 * CONVENIENCE METHODS:
 * Consumer:  andThen()     - chains consumers
 * Function:  andThen()     - chains functions (apply first function, then second)
 * Function:  compose()     - chains functions (apply second function, then first)
 * Predicate: negate()      - reverses boolean result
 * Predicate: and()         - logical AND of two predicates
 * Predicate: or()          - logical OR of two predicates
 */
public class MethodReferencesAndBuiltInInterfaces {

    private int num = 10;

    // ===== METHOD REFERENCE FORMAT 1: STATIC METHODS =====

    public static int staticAdd(int a, int b) {
        return a + b;
    }

    public static void demonstrateStaticMethodReferences() {
        // Method reference to static method
        BinaryOperator<Integer> methodRef = MethodReferencesAndBuiltInInterfaces::staticAdd;

        // Equivalent lambda
        BinaryOperator<Integer> lambda = (a, b) -> MethodReferencesAndBuiltInInterfaces.staticAdd(a, b);

        // More examples
        Consumer<String> printRef = System.out::println;              // Method reference
        Consumer<String> printLambda = s -> System.out.println(s);    // Equivalent lambda

        Function<String, Integer> parseRef = Integer::parseInt;       // Method reference
        Function<String, Integer> parseLambda = s -> Integer.parseInt(s);  // Equivalent lambda

        System.out.println("Static method ref: " + methodRef.apply(5, 3));
    }

    // ===== METHOD REFERENCE FORMAT 2: INSTANCE METHOD ON PARTICULAR OBJECT =====

    public static void demonstrateInstanceMethodOnObject() {
        String prefix = "Hello, ";

        // Method reference on specific object instance
        Predicate<String> startsWithRef = prefix::startsWith;

        // Equivalent lambda
        Predicate<String> startsWithLambda = s -> prefix.startsWith(s);

        // More examples
        StringBuilder sb = new StringBuilder("Test");
        Consumer<String> appendRef = sb::append;                  // Method reference
        Consumer<String> appendLambda = s -> sb.append(s);        // Equivalent lambda

        List<String> list = new ArrayList<>();
        Consumer<String> addRef = list::add;                      // Method reference
        Consumer<String> addLambda = s -> list.add(s);            // Equivalent lambda

        System.out.println("Instance on object: " + startsWithRef.test("Hel"));
    }

    // ===== METHOD REFERENCE FORMAT 3: INSTANCE METHOD ON PARAMETER =====
    // TRICKY: Parameter becomes the instance on which method is called

    public static void demonstrateInstanceMethodOnParameter() {
        /*
         * KEY CONCEPT: First parameter becomes the instance
         * ClassName::instanceMethod means: (instance, params...) -> instance.instanceMethod(params...)
         */

        // Example 1: No parameters - isEmpty()
        Predicate<String> isEmptyRef = String::isEmpty;              // Method reference
        Predicate<String> isEmptyLambda = s -> s.isEmpty();          // Equivalent lambda
        // The String parameter becomes the instance, isEmpty() takes no additional params

        // Example 2: One parameter - concat()
        BiFunction<String, String, String> concatRef = String::concat;     // Method reference
        BiFunction<String, String, String> concatLambda = (s1, s2) -> s1.concat(s2);  // Equivalent lambda
        // First param (s1) is the instance, second param (s2) is the method parameter

        // Example 3: One parameter - startsWith()
        BiPredicate<String, String> startsWithRef = String::startsWith;    // Method reference
        BiPredicate<String, String> startsWithLambda = (s, prefix) -> s.startsWith(prefix);  // Equivalent lambda

        // Example 4: Comparing strings
        BiPredicate<String, String> equalsRef = String::equals;           // Method reference
        BiPredicate<String, String> equalsLambda = (s1, s2) -> s1.equals(s2);  // Equivalent lambda

        System.out.println("isEmpty on 'hello': " + isEmptyRef.test("hello"));
        System.out.println("concat 'Hello' + ' World': " + concatRef.apply("Hello", " World"));
        System.out.println("'Java' starts with 'Ja': " + startsWithRef.test("Java", "Ja"));
    }

    // ===== METHOD REFERENCE FORMAT 4: CONSTRUCTORS =====

    // Helper class for constructor reference example
    static class Person {
        String name;
        int age;

        Person() {
            this.name = "Unknown";
            this.age = 0;
        }

        Person(String name) {
            this(name, 0);
        }

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return name + " (" + age + ")";
        }
    }

    public static void demonstrateConstructorReferences() {
        // Constructor with no args
        Supplier<StringBuilder> newSbRef = StringBuilder::new;       // Method reference
        Supplier<StringBuilder> newSbLambda = () -> new StringBuilder();  // Equivalent lambda

        // Constructor with one arg
        Function<String, StringBuilder> sbWithStringRef = StringBuilder::new;  // Method reference
        Function<String, StringBuilder> sbWithStringLambda = s -> new StringBuilder(s);  // Equivalent lambda

        // Constructor with two args - using custom class
        BiFunction<String, Integer, Person> personRef = Person::new;  // Method reference
        BiFunction<String, Integer, Person> personLambda = (name, age) -> new Person(name, age);  // Equivalent lambda
        System.out.println("Two-arg constructor: " + personRef.apply("Alice", 30));

        // Array constructor
        Function<Integer, String[]> arrayRef = String[]::new;        // Method reference
        Function<Integer, String[]> arrayLambda = size -> new String[size];  // Equivalent lambda

        BiFunction<Person, Person, List<Person>> createListOfPeople = List::of;

        Person p1 = personRef.apply("Casey", 35);
        Person p2 = personRef.apply("Yasmim", 29);

        List<Person> people = createListOfPeople.apply(p1, p2);
        System.out.println("BiFunction<Person, Person, List<Person> : List::of ==> " + people);

        System.out.println("Constructor ref: " + newSbRef.get().append("test"));
    }

    // ===== BUILT-IN FUNCTIONAL INTERFACES =====

    public static void demonstrateBuiltInInterfaces() {
        System.out.println("\n=== BUILT-IN FUNCTIONAL INTERFACES ===");

        // Supplier<T>: () -> T
        Supplier<String> supplier = () -> "Hello";
        System.out.println("Supplier: " + supplier.get());

        // Consumer<T>: T -> void
        Consumer<String> consumer = s -> System.out.println("Consumer: " + s);
        consumer.accept("test");

        // BiConsumer<T, U>: (T, U) -> void
        BiConsumer<String, Integer> biConsumer = (s, i) -> System.out.println("BiConsumer: " + s + " " + i);
        biConsumer.accept("value", 42);

        // Predicate<T>: T -> boolean
        Predicate<String> predicate = s -> s.length() > 3;
        System.out.println("Predicate 'hello' > 3: " + predicate.test("hello"));

        // BiPredicate<T, U>: (T, U) -> boolean
        BiPredicate<String, Integer> biPredicate = (s, i) -> s.length() == i;
        System.out.println("BiPredicate 'test' length == 4: " + biPredicate.test("test", 4));

        // Function<T, R>: T -> R
        Function<String, Integer> function = s -> s.length();
        System.out.println("Function length of 'hello': " + function.apply("hello"));

        // BiFunction<T, U, R>: (T, U) -> R
        BiFunction<String, String, Integer> biFunction = (s1, s2) -> s1.length() + s2.length();
        System.out.println("BiFunction total length: " + biFunction.apply("hello", "world"));

        // UnaryOperator<T>: T -> T (same input and output type)
        UnaryOperator<String> unaryOp = s -> s.toUpperCase();
        System.out.println("UnaryOperator: " + unaryOp.apply("hello"));

        // BinaryOperator<T>: (T, T) -> T (two inputs same type, output same type)
        BinaryOperator<Integer> binaryOp = Integer::sum;
        System.out.println("BinaryOperator: " + binaryOp.apply(5, 3));
    }

    // ===== CONVENIENCE METHODS =====

    public static void demonstrateConvenienceMethods() {
        System.out.println("\n=== CONVENIENCE METHODS ===");

        // CONSUMER: andThen() - chains consumers, executes in order
        Consumer<String> c1 = s -> System.out.print("First: " + s);
        Consumer<String> c2 = s -> System.out.println(", Second: " + s);
        Consumer<String> chained = c1.andThen(c2);  // c1 first, then c2
        chained.accept("test");

        // FUNCTION: andThen() - chains functions, output of first becomes input of second
        Function<String, String> f1 = s -> {
            s = s.toLowerCase();
            System.out.println(s);
            return s;
        };
        Function<String, Integer> f2 = s -> {
            System.out.println("Getting length of string");
            return s.length();
        };
        Function<String, Integer> andThenChain = f1.andThen(f2);  // f1 first, then f2
        // Equivalent to: s -> f2.apply(f1.apply(s))
        // Equivalent to: s -> s.toLowerCase().length()
        System.out.println("andThen: " + andThenChain.apply("HELLO"));

        // FUNCTION: compose() - chains functions, applies argument function first
        Function<String, String> f3 = s -> s.toUpperCase();
        Function<String, Integer> f4 = s -> s.length();
        Function<String, Integer> composeChain = f4.compose(f3);  // f3 first, then f4
        // Equivalent to: s -> f4.apply(f3.apply(s))
        // Equivalent to: s -> s.toUpperCase().length()
        System.out.println("compose: " + composeChain.apply("hello"));

        /*
         * andThen vs compose:
         * f1.andThen(f2):  apply f1, then f2    ->  f2(f1(x))
         * f1.compose(f2):  apply f2, then f1    ->  f1(f2(x))
         */

        // PREDICATE: negate() - reverses the result
        Predicate<String> isEmpty = String::isEmpty;
        Predicate<String> isNotEmpty = isEmpty.negate();  // Reverses result
        System.out.println("isEmpty 'hello': " + isEmpty.test("hello"));
        System.out.println("isNotEmpty 'hello': " + isNotEmpty.test("hello"));

        // PREDICATE: and() - logical AND
        Predicate<String> startsWithJ = s -> s.startsWith("J");
        Predicate<String> endsWithA = s -> s.endsWith("a");
        Predicate<String> combined = startsWithJ.and(endsWithA);  // Both must be true
        System.out.println("'Java' starts with J AND ends with a: " + combined.test("Java"));

        // PREDICATE: or() - logical OR
        Predicate<String> startsWithP = s -> s.startsWith("P");
        Predicate<String> eitherOr = startsWithJ.or(startsWithP);  // Either can be true
        System.out.println("'Python' starts with J OR P: " + eitherOr.test("Python"));
    }

    // ===== MAIN DEMONSTRATION =====

    public static void main(String[] args) {
        demonstrateStaticMethodReferences();
        demonstrateInstanceMethodOnObject();
        demonstrateInstanceMethodOnParameter();
        demonstrateConstructorReferences();
        demonstrateBuiltInInterfaces();
        demonstrateConvenienceMethods();
    }
}
