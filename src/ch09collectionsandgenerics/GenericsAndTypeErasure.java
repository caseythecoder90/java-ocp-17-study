package ch09collectionsandgenerics;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Generics and Type Erasure
 *
 * ===== TYPE ERASURE =====
 *
 * Type erasure is how Java implements generics for backward compatibility with pre-generics code.
 * At compile time, the compiler:
 * 1. Replaces all generic type parameters with Object (or their upper bound)
 * 2. Inserts type casts where necessary
 * 3. Generates bridge methods to preserve polymorphism
 *
 * Example of what compiler does:
 *   BEFORE (your code):        AFTER (compiled bytecode):
 *   List<String> list;         List list;
 *   list.add("hello");         list.add("hello");
 *   String s = list.get(0);    String s = (String) list.get(0);  // Cast inserted!
 *
 * ===== WHY TYPE ERASURE MATTERS FOR THE EXAM =====
 *
 * 1. Cannot overload methods that differ only in generic parameter type
 *    - List<String> and List<Integer> both become List after erasure
 *
 * 2. Cannot override generic methods by changing only the generic parameter type
 *    - Same reason: after erasure, signatures are identical
 *
 * 3. Covariance rules for overridden methods:
 *    - Return type: CAN be covariant (subtype allowed)
 *    - Generic parameter type: MUST match exactly
 *
 * ===== EXAM TIP =====
 * Apply type erasure mentally to generic questions:
 * Replace all generic types with Object, then check if methods would conflict.
 */
public class GenericsAndTypeErasure {

    public static void main(String[] args) {
        demonstrateGenericClass();
        demonstrateTypeErasure();
    }

    // ===== CREATING GENERIC CLASSES =====

    public static void demonstrateGenericClass() {
        System.out.println("=== CREATING GENERIC CLASSES ===\n");

        // Using our generic Box class with different types
        Box<String> stringBox = new Box<>();
        stringBox.setContents("Hello Generics");
        String s = stringBox.getContents();  // No cast needed - compiler handles it
        System.out.println("String box: " + s);

        Box<Integer> intBox = new Box<>();
        intBox.setContents(42);
        Integer i = intBox.getContents();  // No cast needed
        System.out.println("Integer box: " + i);

        // Multiple type parameters
        Pair<String, Integer> pair = new Pair<>("Age", 25);
        System.out.println("Pair: " + pair.getFirst() + " = " + pair.getSecond());
    }

    // ===== TYPE ERASURE DEMONSTRATION =====

    public static void demonstrateTypeErasure() {
        System.out.println("\n=== TYPE ERASURE IN ACTION ===\n");

        // What you write:
        List<String> strings = new ArrayList<>();
        strings.add("hello");
        String s = strings.get(0);

        // What the compiler generates (conceptually):
        // List strings = new ArrayList();      // Generic type removed
        // strings.add("hello");
        // String s = (String) strings.get(0);  // Cast inserted

        System.out.println("Your code: String s = list.get(0);");
        System.out.println("After erasure: String s = (String) list.get(0);");

        // This is why you can't do: new T() or new T[] - type is erased!
        System.out.println("\nCannot create: new T() or new T[] because T is erased to Object");
    }
}

// ===== GENERIC CLASS WITH SINGLE TYPE PARAMETER =====

// T is a type parameter - replaced with Object after type erasure
class Box<T> {
    private T contents;

    public T getContents() {
        return contents;
    }

    public void setContents(T contents) {
        this.contents = contents;
    }

    // After type erasure, this class becomes:
    // class Box {
    //     private Object contents;
    //     public Object getContents() { return contents; }
    //     public void setContents(Object contents) { this.contents = contents; }
    // }
}

// ===== GENERIC CLASS WITH MULTIPLE TYPE PARAMETERS =====

class Pair<K, V> {
    private K first;
    private V second;

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public K getFirst() { return first; }
    public V getSecond() { return second; }
}

// ===== CANNOT OVERLOAD METHODS - TYPE ERASURE CONFLICT =====

class TypeErasureOverloadProblem {

    // This method compiles fine
    protected void chew(List<Object> input) {
        System.out.println("Chewing objects");
    }

    // DOES NOT COMPILE! After type erasure, both methods have signature: chew(List)
    // protected void chew(List<Double> input) {
    //     System.out.println("Chewing doubles");
    // }

    // Why it fails - apply type erasure:
    //   chew(List<Object> input)  ->  chew(List input)
    //   chew(List<Double> input)  ->  chew(List input)   <- DUPLICATE!

    // Same problem with any generic type:
    // void process(List<String> strings) {}
    // void process(List<Integer> integers) {}  // DOES NOT COMPILE - same erasure
}

// ===== CANNOT OVERRIDE BY CHANGING GENERIC PARAMETER TYPE =====

class Parent {
    // Parent method with List<Object>
    protected void consume(List<Object> data) {
        System.out.println("Parent consuming");
    }

    // Generic return type
    public List<Object> getItems() {
        return new ArrayList<>();
    }
}

class Child extends Parent {

    // DOES NOT COMPILE! Cannot override by changing List<Object> to List<String>
    // @Override
    // protected void consume(List<String> data) {  // ERROR: not an override, but erasure conflict
    //     System.out.println("Child consuming");
    // }

    // Why it fails:
    // Parent: consume(List<Object>) -> consume(List) after erasure
    // Child:  consume(List<String>) -> consume(List) after erasure
    // These have same erasure but different generic types = ERROR

    // VALID: You can override with the EXACT same generic type
    @Override
    protected void consume(List<Object> data) {
        System.out.println("Child consuming - same generic type OK");
    }
}

// ===== COVARIANT RETURN TYPES WITH GENERICS =====

/**
 * COVARIANCE RULES FOR OVERRIDING:
 *
 * Return type:           CAN be covariant (subtype allowed)
 * Generic parameter:     MUST match exactly
 *
 * Covariance applies to the RETURN TYPE, not the generic parameter type!
 */

class CovariantParent {
    // Returns List<CharSequence>
    public List<CharSequence> getSequences() {
        return new ArrayList<>();
    }

    // Returns CharSequence
    public CharSequence getText() {
        return "parent";
    }
}

class CovariantChild extends CovariantParent {

    // VALID: Return type ArrayList is subtype of List (covariant return)
    // BUT: Generic parameter CharSequence must match exactly
    @Override
    public ArrayList<CharSequence> getSequences() {  // ArrayList<CharSequence> is valid
        return new ArrayList<>();
    }

    // DOES NOT COMPILE: Generic parameter type changed (CharSequence -> String)
    // Even though String IS-A CharSequence, generic parameters must match exactly!
    // @Override
    // public List<String> getSequences() {  // ERROR! Generic param must be CharSequence
    //     return new ArrayList<>();
    // }

    // VALID: String is subtype of CharSequence (covariant return type)
    @Override
    public String getText() {
        return "child";
    }
}

// ===== MORE COVARIANCE EXAMPLES =====

class AnimalShelter {
    public List<Animal> getAnimals() {
        return new ArrayList<>();
    }
}

class DogShelter extends AnimalShelter {

    // VALID: ArrayList is subtype of List (covariant return)
    // Generic parameter Animal stays the same (required)
    @Override
    public ArrayList<Animal> getAnimals() {
        return new ArrayList<>();
    }

    // DOES NOT COMPILE: Cannot change generic parameter from Animal to Dog
    // @Override
    // public List<Dog> getAnimals() {  // ERROR! Must be List<Animal>
    //     return new ArrayList<>();
    // }

    // Remember:
    // - List -> ArrayList: VALID (covariant return type)
    // - <Animal> -> <Dog>: INVALID (generic param must match exactly)
}

class Animal {}
class Dog extends Animal {}

// ===== EXAM PRACTICE: APPLY TYPE ERASURE =====

/**
 * EXAM TIP: When you see generic method overloading/overriding questions,
 * mentally apply type erasure to determine if code compiles.
 *
 * Step 1: Replace all generic types with Object
 * Step 2: Check if any method signatures become identical
 * Step 3: If identical after erasure -> DOES NOT COMPILE
 *
 * Examples:
 *
 * void foo(List<String> x) {}
 * void foo(List<Integer> x) {}
 * After erasure: void foo(List x) {} and void foo(List x) {} -> CONFLICT!
 *
 * void bar(Map<String, Integer> x) {}
 * void bar(Map<Integer, String> x) {}
 * After erasure: void bar(Map x) {} and void bar(Map x) {} -> CONFLICT!
 *
 * void baz(List<String> x) {}
 * void baz(Set<String> x) {}
 * After erasure: void baz(List x) {} and void baz(Set x) {} -> OK! Different types
 */
class ErasureExamPractice {

    // These compile - different raw types after erasure
    void process(List<String> list) {}
    void process(Set<String> set) {}     // OK: List vs Set after erasure

    // These would NOT compile - same raw type after erasure
    // void handle(List<String> list) {}
    // void handle(List<Integer> list) {}  // ERROR: both become handle(List)

    // These would NOT compile - same raw type after erasure
    // void compute(Map<String, Integer> map) {}
    // void compute(Map<Integer, String> map) {}  // ERROR: both become compute(Map)
}

// =====================================================================
// IMPLEMENTING GENERIC INTERFACES - THREE APPROACHES
// =====================================================================

/**
 * Three ways a class can implement a generic interface:
 * 1. Specify concrete type (fill in the generic)
 * 2. Pass through the generic (implementing class is also generic)
 * 3. Use raw type (remove the generic) - NOT RECOMMENDED
 */

interface Container<T> {
    void store(T item);
    T retrieve();
}

// APPROACH 1: Specify concrete type - "fill in" the generic
// The implementing class is NOT generic - it locks in String
class StringContainer implements Container<String> {
    private String item;

    @Override
    public void store(String item) { this.item = item; }

    @Override
    public String retrieve() { return item; }
}

// APPROACH 2: Pass through the generic - implementing class is also generic
// Most flexible - caller decides the type
class GenericContainer<T> implements Container<T> {
    private T item;

    @Override
    public void store(T item) { this.item = item; }

    @Override
    public T retrieve() { return item; }
}

// APPROACH 3: Raw type - NOT RECOMMENDED, generates warnings
// Loses all type safety! Compiles but dangerous
@SuppressWarnings("rawtypes")
class RawContainer implements Container {  // No <T> - raw type
    private Object item;

    @Override
    public void store(Object item) { this.item = item; }

    @Override
    public Object retrieve() { return item; }
}

// =====================================================================
// THINGS YOU CANNOT DO WITH GENERIC TYPES
// =====================================================================

/**
 * RESTRICTIONS ON GENERIC TYPE PARAMETERS:
 *
 * 1. Cannot call constructor:        new T()           - Type unknown at runtime
 * 2. Cannot create array:            new T[10]         - Type unknown at runtime
 * 3. Cannot use instanceof:          obj instanceof T  - Type erased at runtime
 * 4. Cannot use primitive types:     Box<int>          - Must use wrapper: Box<Integer>
 * 5. Cannot create static variable:  static T field    - T belongs to instance, not class
 */

class GenericRestrictions<T> {
    private T instance;

    // CANNOT call constructor - new T()
    // public void createInstance() {
    //     instance = new T();  // ERROR: Type parameter T cannot be instantiated
    // }

    // CANNOT create array of generic type - new T[]
    // public T[] createArray() {
    //     return new T[10];  // ERROR: Cannot create generic array
    // }

    // CANNOT use instanceof with generic type
    // public boolean checkType(Object obj) {
    //     return obj instanceof T;  // ERROR: Cannot perform instanceof with type parameter
    // }

    // CANNOT use primitive as type parameter
    // Box<int> intBox;     // ERROR: Must use Box<Integer>
    // Box<double> dblBox;  // ERROR: Must use Box<Double>

    // CANNOT have static variable of generic type
    // static T staticField;  // ERROR: Cannot make static reference to non-static type T

    // WHY? T is defined per-instance. Static belongs to the class, not instances.
    // GenericRestrictions<String> and GenericRestrictions<Integer> share same static fields!

    // VALID: Static methods CAN declare their own generic type (see Generic Methods below)
    static <U> U staticMethod(U param) { return param; }
}

// =====================================================================
// GENERIC METHODS
// =====================================================================

/**
 * GENERIC METHODS:
 *
 * - Declare type parameter BEFORE the return type: <T> T methodName(T param)
 * - Useful for static methods (can't use class-level generics)
 * - Also allowed on non-static methods
 * - Method's generic type is INDEPENDENT of class's generic type
 *
 * SYNTAX: [modifiers] <TypeParam> ReturnType methodName(parameters)
 *
 * IMPORTANT: If method declares its own <T>, it shadows any class-level T!
 */

class GenericMethodExamples<T> {

    // Non-generic method using CLASS's type parameter T
    // No <T> before return type - uses class's T
    public T classGenericMethod(T param) {
        return param;
    }

    // Generic method declaring its OWN type parameter <U>
    // <U> appears before return type
    public <U> U methodGenericMethod(U param) {
        return param;
    }

    // STATIC method MUST declare its own generic - can't use class's T
    // Static methods aren't part of an instance, so no access to instance's T
    public static <T> T staticGenericMethod(T param) {
        return param;
    }

    // Method generic is INDEPENDENT of class generic
    // This <T> is a NEW type parameter, shadows class's T!
    public <T> T shadowingMethod(T param) {
        // This T is the METHOD's T, not the class's T
        return param;
    }

    // Multiple type parameters on a method
    public <K, V> V multipleTypeParams(K key, V value) {
        System.out.println("Key: " + key);
        return value;
    }
}

// Demonstration of generic method usage
class GenericMethodDemo {
    public static void main(String[] args) {

        // CALLING STATIC GENERIC METHODS

        // Normal invocation - type inferred from argument
        String s = GenericMethodExamples.staticGenericMethod("hello");
        Integer i = GenericMethodExamples.staticGenericMethod(42);

        // EXPLICIT TYPE SPECIFICATION - strange looking but valid!
        // Type goes BEFORE method name: ClassName.<Type>methodName()
        String s2 = GenericMethodExamples.<String>staticGenericMethod("hello");
        Integer i2 = GenericMethodExamples.<Integer>staticGenericMethod(42);

        // CALLING INSTANCE GENERIC METHODS
        GenericMethodExamples<String> instance = new GenericMethodExamples<>();

        // Uses class's T (String)
        String result1 = instance.classGenericMethod("test");

        // Uses method's own U - can be anything, independent of class's String
        Integer result2 = instance.methodGenericMethod(100);  // U is Integer
        Double result3 = instance.methodGenericMethod(3.14);  // U is Double

        // Explicit type for instance methods - type goes after dot, before method name
        Integer result4 = instance.<Integer>methodGenericMethod(100);
    }
}

// =====================================================================
// TRICKY EXAMPLE: CLASS GENERIC VS METHOD GENERIC
// =====================================================================

class TrickyGenerics<T> {
    private T classField;

    // This method uses the CLASS's T
    public void setField(T value) {
        this.classField = value;
    }

    // This method declares its OWN T - shadows class T!
    // The <T> before void makes this a generic method with its own T
    public <T> void trickyMethod(T param) {
        // param is method's T, classField is class's T
        // They are DIFFERENT types!
        System.out.println("Param type: " + param.getClass().getSimpleName());

        // This would NOT compile - method's T is different from class's T:
        // classField = param;  // ERROR: incompatible types
    }

    // EXAM TIP: Look for <T> before return type
    // - Has <T>: Method declares its own generic (independent of class)
    // - No <T>: Method uses class's generic type
}

class TrickyDemo {
    public static void main(String[] args) {
        TrickyGenerics<String> obj = new TrickyGenerics<>();

        obj.setField("Hello");           // Uses class's T (String)
        obj.trickyMethod(123);           // Uses method's T (Integer) - independent!
        obj.trickyMethod(3.14);          // Uses method's T (Double)
        obj.<Boolean>trickyMethod(true); // Explicit: method's T is Boolean
    }
}

// =====================================================================
// GENERIC RECORDS
// =====================================================================

// Records can have generic type parameters - works just like classes
record GenericRecord<T>(T value, String label) {
    // Compact constructor with validation
    public GenericRecord {
        if (label == null) throw new IllegalArgumentException("Label required");
    }
}

// Record with multiple type parameters
record GenericPairRecord<K, V>(K key, V value) {}

class RecordGenericDemo {
    public static void main(String[] args) {
        // Using generic records
        GenericRecord<Integer> intRecord = new GenericRecord<>(42, "Answer");
        GenericRecord<String> strRecord = new GenericRecord<>("Hello", "Greeting");

        System.out.println(intRecord.value());  // 42 - no cast needed
        System.out.println(strRecord.value());  // Hello

        GenericPairRecord<String, Double> pair = new GenericPairRecord<>("Pi", 3.14159);
        System.out.println(pair.key() + " = " + pair.value());
    }
}
