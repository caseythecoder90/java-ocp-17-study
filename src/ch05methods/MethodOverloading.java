package ch05methods;

/**
 * METHOD OVERLOADING AND PASSING DATA - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * METHOD SIGNATURE
 * ═══════════════════════════════════════════════════════════════════════════
 * A method signature consists of:
 * 1. Method name
 * 2. Parameter list (types and order)
 *
 * NOT part of signature:
 * - Return type
 * - Access modifiers
 * - Exception list
 * - Parameter names
 *
 * Example signatures:
 * - fly()
 * - fly(int)
 * - fly(int, String)
 * - fly(String, int)          ← Different from above! Order matters
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * METHOD OVERLOADING
 * ═══════════════════════════════════════════════════════════════════════════
 * Method overloading = Multiple methods with the SAME NAME but DIFFERENT
 * parameter lists (different signatures) in the same class.
 *
 * Rules:
 * ✓ Must have different parameter lists (number, types, or order)
 * ✓ Can have different return types
 * ✓ Can have different access modifiers
 * ✓ Can throw different exceptions
 *
 * ✗ Cannot overload by changing ONLY:
 *   - Return type
 *   - Access modifier
 *   - Parameter names
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * JAVA IS PASS-BY-VALUE
 * ═══════════════════════════════════════════════════════════════════════════
 * Java ALWAYS passes by value. This means a COPY of the value is passed.
 *
 * For primitives:   Copy of the actual value (e.g., 5, true)
 * For objects:      Copy of the reference (memory address), NOT the object itself
 *
 * RESULT:
 * - Primitives: Changes to parameter don't affect original
 * - Objects: Changes to object's fields DO affect original
 *            BUT reassigning the parameter doesn't affect original reference
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * PASSING OBJECTS TO METHODS - RULES
 * ═══════════════════════════════════════════════════════════════════════════
 * Objects passed to methods MUST be initialized (not null is okay for passing,
 * but using null will cause NullPointerException).
 *
 * The reference must point to a valid object or be null. Uninitialized
 * variables (declared but not assigned) CANNOT be passed.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * STATIC VS INSTANCE METHOD CALLS
 * ═══════════════════════════════════════════════════════════════════════════
 * Calling STATIC methods:
 * ✓ Static methods can call other static methods directly
 * ✓ Instance methods can call static methods directly
 * → Static methods don't require an instance
 *
 * Calling INSTANCE methods:
 * ✓ Instance methods can call other instance methods directly (same class)
 * ✗ Static methods CANNOT call instance methods directly
 * → Instance methods require an object instance
 *
 * Rule of thumb:
 * - Static = belongs to class → no instance needed
 * - Instance = belongs to object → instance required
 */
public class MethodOverloading {

    // ═══════════════════════════════════════════════════════════════════════
    // METHOD OVERLOADING EXAMPLES
    // ═══════════════════════════════════════════════════════════════════════

    // ───────────────────────────────────────────────────────────────────────
    // Valid Overloading - Different number of parameters
    // ───────────────────────────────────────────────────────────────────────
    public void fly(int distance) {
        System.out.println("Flying " + distance + " miles");
    }

    public void fly(int distance, int speed) {
        System.out.println("Flying " + distance + " miles at " + speed + " mph");
    }

    // ───────────────────────────────────────────────────────────────────────
    // Valid Overloading - Different parameter types
    // ───────────────────────────────────────────────────────────────────────
    public void fly(String destination) {
        System.out.println("Flying to " + destination);
    }

    // ───────────────────────────────────────────────────────────────────────
    // Valid Overloading - Different parameter order
    // ───────────────────────────────────────────────────────────────────────
    public void fly(int distance, String destination) {
        System.out.println("Flying " + distance + " miles to " + destination);
    }

    public void fly(String destination, int distance) {  // Different signature!
        System.out.println("Flying to " + destination + " for " + distance + " miles");
    }

    // ───────────────────────────────────────────────────────────────────────
    // Valid Overloading - Different return types (but must have different params)
    // ───────────────────────────────────────────────────────────────────────
    public int calculate(int a) {
        return a * 2;
    }

    public double calculate(double a) {  // Different parameter type = valid
        return a * 2;
    }

    // ───────────────────────────────────────────────────────────────────────
    // INVALID Overloading - Would NOT compile
    // ───────────────────────────────────────────────────────────────────────
    /*
    public void fly(int miles) {              // ✗ DUPLICATE signature
        System.out.println("Duplicate");
    }

    public int fly(int distance) {            // ✗ Can't change ONLY return type
        return distance;
    }

    public void fly(int dist) {               // ✗ Parameter NAME doesn't matter
        System.out.println("Same signature");  // Same as fly(int distance)
    }
    */

    // ═══════════════════════════════════════════════════════════════════════
    // PASS-BY-VALUE: PRIMITIVES
    // ═══════════════════════════════════════════════════════════════════════
    public static void changePrimitive(int num) {
        num = 100;  // Changes local copy only
        System.out.println("Inside method: " + num);  // 100
    }

    public static void demonstratePrimitivePassing() {
        int value = 5;
        System.out.println("Before: " + value);        // 5
        changePrimitive(value);                        // Passes copy of 5
        System.out.println("After: " + value);         // Still 5 (unchanged!)
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PASS-BY-VALUE: OBJECTS
    // ═══════════════════════════════════════════════════════════════════════
    static class Dog {
        String name;
        Dog(String name) { this.name = name; }
    }

    // ───────────────────────────────────────────────────────────────────────
    // Modifying object's fields - DOES affect original
    // ───────────────────────────────────────────────────────────────────────
    public static void changeDogName(Dog dog) {
        dog.name = "Buddy";  // Modifies the object (reference points to same object)
        System.out.println("Inside method: " + dog.name);  // Buddy
    }

    // ───────────────────────────────────────────────────────────────────────
    // Reassigning parameter - does NOT affect original reference
    // ───────────────────────────────────────────────────────────────────────
    public static void reassignDog(Dog dog) {
        dog = new Dog("New Dog");  // Reassigns LOCAL parameter only
        System.out.println("Inside method: " + dog.name);  // New Dog
    }

    public static void demonstrateObjectPassing() {
        Dog myDog = new Dog("Max");

        System.out.println("Original: " + myDog.name);     // Max

        changeDogName(myDog);                              // Pass reference copy
        System.out.println("After change: " + myDog.name); // Buddy (changed!)

        reassignDog(myDog);                                // Pass reference copy
        System.out.println("After reassign: " + myDog.name); // Still Buddy (unchanged!)
    }

    // ═══════════════════════════════════════════════════════════════════════
    // OBJECT INITIALIZATION RULES
    // ═══════════════════════════════════════════════════════════════════════
    public static void processObject(Dog dog) {
        System.out.println(dog.name);  // Will throw NPE if dog is null
    }

    public static void demonstrateInitialization() {
        Dog dog1 = new Dog("Rex");     // ✓ Initialized
        processObject(dog1);           // ✓ Works

        Dog dog2 = null;               // ✓ Can pass null (but will NPE if used)
        // processObject(dog2);        // ✗ Compiles but throws NullPointerException

        // Dog dog3;                   // ✗ Uninitialized
        // processObject(dog3);        // ✗ DOES NOT COMPILE - variable not initialized
    }

    // ═══════════════════════════════════════════════════════════════════════
    // STATIC VS INSTANCE METHOD CALLS
    // ═══════════════════════════════════════════════════════════════════════
    private int instanceVar = 10;
    private static int staticVar = 20;

    // ───────────────────────────────────────────────────────────────────────
    // Static methods calling other methods
    // ───────────────────────────────────────────────────────────────────────
    public static void staticMethod1() {
        System.out.println("Static method 1");
    }

    public static void staticMethod2() {
        // ✓ Static can call static directly
        staticMethod1();
        System.out.println(staticVar);  // ✓ Can access static variables

        // ✗ Static CANNOT call instance methods directly
        // instanceMethod1();           // DOES NOT COMPILE
        // System.out.println(instanceVar); // DOES NOT COMPILE

        // ✓ Static can call instance methods through an object
        MethodOverloading obj = new MethodOverloading();
        obj.instanceMethod1();          // ✓ Works
        System.out.println(obj.instanceVar);  // ✓ Works
    }

    // ───────────────────────────────────────────────────────────────────────
    // Instance methods calling other methods
    // ───────────────────────────────────────────────────────────────────────
    public void instanceMethod1() {
        System.out.println("Instance method 1");
    }

    public void instanceMethod2() {
        // ✓ Instance can call instance methods directly (same class)
        instanceMethod1();
        System.out.println(instanceVar);  // ✓ Can access instance variables

        // ✓ Instance can call static methods directly
        staticMethod1();
        System.out.println(staticVar);    // ✓ Can access static variables
    }

    // ───────────────────────────────────────────────────────────────────────
    // Summary of calling rules
    // ───────────────────────────────────────────────────────────────────────
    /*
     * FROM              TO                DIRECT CALL?
     * ─────────────────────────────────────────────────────────────────────
     * Static            Static            ✓ YES
     * Static            Instance          ✗ NO (need object)
     * Instance          Static            ✓ YES
     * Instance          Instance          ✓ YES (same class)
     *
     * Why?
     * - Static methods exist independently of any object
     * - Instance methods require an object to operate on
     * - When inside an instance method, "this" object exists
     * - When inside a static method, there is no "this"
     */

    // ═══════════════════════════════════════════════════════════════════════
    // MAIN METHOD - EXAMPLES
    // ═══════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        MethodOverloading demo = new MethodOverloading();

        System.out.println("=== Method Overloading ===");
        demo.fly(100);
        demo.fly(200, 500);
        demo.fly("Hawaii");
        demo.fly(150, "Paris");
        demo.fly("Tokyo", 5000);
        System.out.println(demo.calculate(5));
        System.out.println(demo.calculate(5.5));

        System.out.println("\n=== Pass by Value: Primitives ===");
        demonstratePrimitivePassing();

        System.out.println("\n=== Pass by Value: Objects ===");
        demonstrateObjectPassing();

        System.out.println("\n=== Object Initialization ===");
        demonstrateInitialization();

        System.out.println("\n=== Static vs Instance Calls ===");
        staticMethod2();
        demo.instanceMethod2();

        System.out.println("\n✓ All examples completed successfully");
    }
}
