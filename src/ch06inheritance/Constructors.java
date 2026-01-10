package ch06inheritance;

/**
 * CONSTRUCTORS - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CONSTRUCTOR FUNDAMENTALS
 * ══════════════════════════════════════════════════════════════════════════
 * 1. Same name as class, no return type
 * 2. Called with 'new' keyword
 * 3. If no constructor defined, Java creates default (no-arg, calls super())
 * 4. First line must be this() or super() (explicit or implicit)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * DEFAULT CONSTRUCTOR
 * ═══════════════════════════════════════════════════════════════════════════
 * Created IF AND ONLY IF no constructors are defined.
 * - No parameters
 * - Calls super()
 * - Same access as class
 *
 * EXAM TRAP: If you define ANY constructor, default is NOT created!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * this() - CALLING OTHER CONSTRUCTORS
 * ═══════════════════════════════════════════════════════════════════════════
 * Must be FIRST statement in constructor.
 * Cannot use this() and super() in same constructor.
 * Used for constructor chaining within same class.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * super() - CALLING PARENT CONSTRUCTORS
 * ═══════════════════════════════════════════════════════════════════════════
 * Must be FIRST statement in constructor.
 * If no this() or super(), compiler inserts super() automatically.
 *
 * CRITICAL: If parent has no no-arg constructor, child MUST explicitly
 * call super(args).
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * FINAL FIELDS IN CONSTRUCTORS
 * ═══════════════════════════════════════════════════════════════════════════
 * final instance variables must be initialized:
 * 1. Inline (where declared)
 * 2. Instance initializer block { }
 * 3. Constructor
 *
 * ALL constructors must initialize final fields!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * ORDER OF INITIALIZATION (with inheritance)
 * ═══════════════════════════════════════════════════════════════════════════
 * 1. Parent static initialization (if first time)
 * 2. Child static initialization (if first time)
 * 3. Parent instance variables
 * 4. Parent instance initializer blocks
 * 5. Parent constructor
 * 6. Child instance variables
 * 7. Child instance initializer blocks
 * 8. Child constructor
 */
public class Constructors {

    public static void main(String[] args) {
        // ────────────────────────────────────────────────────────────────────
        // Default constructor (created automatically)
        // ────────────────────────────────────────────────────────────────────
        DefaultConstructorExample d1 = new DefaultConstructorExample();

        // ────────────────────────────────────────────────────────────────────
        // Constructor overloading
        // ────────────────────────────────────────────────────────────────────
        OverloadedConstructor p1 = new OverloadedConstructor();
        OverloadedConstructor p2 = new OverloadedConstructor("Alice");
        OverloadedConstructor p3 = new OverloadedConstructor("Bob", 30);

        // ────────────────────────────────────────────────────────────────────
        // this() - calling other constructors
        // ────────────────────────────────────────────────────────────────────
        ThisExample t1 = new ThisExample();
        ThisExample t2 = new ThisExample("Alice");

        // ────────────────────────────────────────────────────────────────────
        // super() - calling parent constructor
        // ────────────────────────────────────────────────────────────────────
        SuperConstructorExample s1 = new SuperConstructorExample("Test");

        // ────────────────────────────────────────────────────────────────────
        // Final fields in constructors
        // ────────────────────────────────────────────────────────────────────
        FinalFieldsExample f1 = new FinalFieldsExample();
        System.out.println("Final fields: " + f1.getA() + ", " + f1.getB() + ", " + f1.getC());

        // ────────────────────────────────────────────────────────────────────
        // Order of initialization
        // ────────────────────────────────────────────────────────────────────
        System.out.println("\nFirst object creation:");
        InitializationOrder obj1 = new InitializationOrder();

        System.out.println("\nSecond object creation (class already loaded):");
        InitializationOrder obj2 = new InitializationOrder();
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// DEFAULT CONSTRUCTOR
// ═══════════════════════════════════════════════════════════════════════════

class DefaultConstructorExample {
    // No constructor defined - Java creates default
}

// ═══════════════════════════════════════════════════════════════════════════
// CONSTRUCTOR OVERLOADING
// ═══════════════════════════════════════════════════════════════════════════

class OverloadedConstructor {
    OverloadedConstructor() {
        System.out.println("No-arg constructor");
    }

    OverloadedConstructor(String name) {
        System.out.println("Constructor: " + name);
    }

    OverloadedConstructor(String name, int age) {
        System.out.println("Constructor: " + name + ", " + age);
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// this() - Constructor chaining
// ═══════════════════════════════════════════════════════════════════════════

class ThisExample {
    private String name;
    private int age;

    ThisExample() {
        this("Unknown", 0);  // Must be first line
        System.out.println("No-arg called");
    }

    ThisExample(String name) {
        this(name, 0);  // Must be first line
        System.out.println("Single-arg called");
    }

    ThisExample(String name, int age) {
        this.name = name;
        this.age = age;
        System.out.println("Two-arg called: " + name + ", " + age);
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// super() - Calling parent constructor
// ═══════════════════════════════════════════════════════════════════════════

class ParentWithConstructor {
    ParentWithConstructor(String name) {
        System.out.println("Parent constructor: " + name);
    }
}

class SuperConstructorExample extends ParentWithConstructor {
    SuperConstructorExample(String name) {
        super(name);  // Must call parent constructor
        System.out.println("Child constructor: " + name);
    }
}

// EXAM TRAP: Missing parent no-arg constructor
// class Parent {
//     Parent(int x) { }  // Only parameterized constructor
// }
// class Child extends Parent {
//     Child() { }  // ✗ DOES NOT COMPILE
//     // Compiler tries to insert super(), but Parent has no no-arg constructor
// }

// ═══════════════════════════════════════════════════════════════════════════
// FINAL FIELDS - Must initialize in ALL constructors
// ═══════════════════════════════════════════════════════════════════════════

class FinalFieldsExample {
    private final int a = 10;    // Inline
    private final int b;         // Instance initializer
    private final int c;         // Constructor

    {
        b = 20;  // Instance initializer
    }

    FinalFieldsExample() {
        c = 30;  // Must initialize in constructor
    }

    int getA() { return a; }
    int getB() { return b; }
    int getC() { return c; }
}

// ═══════════════════════════════════════════════════════════════════════════
// ORDER OF INITIALIZATION
// ═══════════════════════════════════════════════════════════════════════════

class InitializationParent {
    static { System.out.println("1. Parent static block"); }
    { System.out.println("3. Parent instance block"); }
    InitializationParent() { System.out.println("4. Parent constructor"); }
}

class InitializationOrder extends InitializationParent {
    static { System.out.println("2. Child static block"); }
    { System.out.println("5. Child instance block"); }
    InitializationOrder() { System.out.println("6. Child constructor"); }
}
