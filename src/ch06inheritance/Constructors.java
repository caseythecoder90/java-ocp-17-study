package ch06inheritance;

/**
 * CONSTRUCTORS - Complete Guide for OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CONSTRUCTOR FUNDAMENTALS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * A constructor is a special method used to initialize objects.
 *
 * KEY RULES:
 * 1. Same name as the class
 * 2. No return type (not even void)
 * 3. Called when object is created with 'new'
 * 4. If no constructor defined, Java creates default constructor
 * 5. First line must be this() or super() (explicit or implicit)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * DEFAULT CONSTRUCTOR
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Java automatically creates a default constructor IF AND ONLY IF:
 * - No constructors are defined in the class
 *
 * Default constructor characteristics:
 * - No parameters
 * - Calls super() with no arguments
 * - Has same access modifier as class
 * - Created at compile time
 *
 * EXAM TRAP: If you define ANY constructor, default is NOT created!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CONSTRUCTOR OVERLOADING
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Multiple constructors with different parameter lists (signatures)
 * - Same name (class name)
 * - Different number or types of parameters
 * - Cannot differ only by return type (no return type anyway!)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * this() - CALLING OTHER CONSTRUCTORS IN SAME CLASS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Rules:
 * 1. Must be FIRST statement in constructor
 * 2. Cannot use this() and super() in same constructor
 * 3. Used for constructor chaining within same class
 * 4. Can pass different arguments
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * super() - CALLING PARENT CONSTRUCTORS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Rules:
 * 1. Must be FIRST statement in constructor
 * 2. Cannot use this() and super() in same constructor
 * 3. If no this() or super(), compiler inserts super() automatically
 * 4. Can pass arguments to parent constructor
 *
 * CRITICAL EXAM RULE:
 * The first statement of EVERY constructor is either:
 * - Explicit super() call
 * - Explicit this() call
 * - Implicit super() call (inserted by compiler)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * FINAL FIELDS IN CONSTRUCTORS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * final instance variables must be initialized by end of constructor:
 * 1. Inline (where declared)
 * 2. Instance initializer block { }
 * 3. Constructor
 *
 * If using constructors, ALL constructors must initialize final fields!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * ORDER OF INITIALIZATION
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * CRITICAL EXAM TOPIC - Know this order by heart!
 *
 * 1. Static initialization (class level):
 *    a. Static variables (in order declared)
 *    b. Static initializer blocks (in order)
 *
 * 2. Instance initialization (per object):
 *    a. Super class initialization (recursive - parent first!)
 *    b. Instance variables (in order declared)
 *    c. Instance initializer blocks (in order)
 *    d. Constructor body
 *
 * For inheritance:
 * 1. Parent static initialization (if first time)
 * 2. Child static initialization (if first time)
 * 3. Parent instance initialization
 * 4. Parent constructor
 * 5. Child instance initialization
 * 6. Child constructor
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * EXAM TRAPS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * 1. this() or super() must be FIRST line
 * 2. Cannot use both this() and super() in same constructor
 * 3. Default constructor only created if NO user-defined constructors
 * 4. Implicit super() fails if parent has no no-arg constructor
 * 5. final fields must be initialized in ALL constructors
 * 6. Static initialization happens before instance initialization
 * 7. Parent initialization happens before child initialization
 */
public class Constructors {

    public static void main(String[] args) {
        System.out.println("=== CONSTRUCTORS ===\n");

        defaultConstructor();
        constructorOverloading();
        thisKeyword();
        superKeyword();
        finalFields();
        orderOfInitialization();
        commonExamTraps();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * DEFAULT CONSTRUCTOR
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void defaultConstructor() {
        System.out.println("=== DEFAULT CONSTRUCTOR ===\n");

        System.out.println("Java creates default constructor IF AND ONLY IF:");
        System.out.println("  • No constructors are defined\n");

        System.out.println("--- Example 1: Default Constructor Created ✓ ---");
        System.out.println("class Example {");
        System.out.println("    // No constructor defined");
        System.out.println("}");
        System.out.println("\n// Java creates:");
        System.out.println("public Example() {");
        System.out.println("    super();");
        System.out.println("}\n");

        DefaultConstructorExample obj1 = new DefaultConstructorExample();
        System.out.println("DefaultConstructorExample obj = new DefaultConstructorExample();");
        System.out.println("→ ✓ Compiles! Java created default constructor\n");

        System.out.println("--- Example 2: Default Constructor NOT Created ✗ ---");
        System.out.println("class Example {");
        System.out.println("    public Example(int x) {");
        System.out.println("        // User-defined constructor");
        System.out.println("    }");
        System.out.println("}");
        System.out.println("\n// Example e = new Example();  // ✗ DOES NOT COMPILE");
        System.out.println("→ No default constructor created!\n");

        System.out.println("--- Default Constructor Characteristics ---");
        System.out.println("• No parameters");
        System.out.println("• Calls super() with no arguments");
        System.out.println("• Same access modifier as class");
        System.out.println("• Generated at compile time");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * CONSTRUCTOR OVERLOADING
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void constructorOverloading() {
        System.out.println("=== CONSTRUCTOR OVERLOADING ===\n");

        System.out.println("Multiple constructors with different signatures\n");

        System.out.println("--- Example ---");
        System.out.println("class Person {");
        System.out.println("    public Person() { }");
        System.out.println("    public Person(String name) { }");
        System.out.println("    public Person(String name, int age) { }");
        System.out.println("}\n");

        OverloadedConstructor p1 = new OverloadedConstructor();
        OverloadedConstructor p2 = new OverloadedConstructor("Alice");
        OverloadedConstructor p3 = new OverloadedConstructor("Bob", 30);

        System.out.println("Person p1 = new Person();");
        System.out.println("  → Calls no-arg constructor");
        System.out.println("\nPerson p2 = new Person(\"Alice\");");
        System.out.println("  → Calls single-parameter constructor");
        System.out.println("\nPerson p3 = new Person(\"Bob\", 30);");
        System.out.println("  → Calls two-parameter constructor");

        System.out.println("\n--- Overloading Rules ---");
        System.out.println("• Different parameter lists (number or types)");
        System.out.println("• Same class name");
        System.out.println("• Cannot differ only by return type (no return type!)");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * this() - CALLING OTHER CONSTRUCTORS
     * ═══════════════════════════════════════════════════════════════════════
     *
     * *** CRITICAL EXAM RULE ***
     * this() MUST be the FIRST statement in constructor
     */
    private static void thisKeyword() {
        System.out.println("=== this() - CALLING OTHER CONSTRUCTORS ===");
        System.out.println("*** CRITICAL EXAM RULE ***\n");

        System.out.println("RULE: this() must be FIRST statement in constructor\n");

        System.out.println("--- Example: Constructor Chaining ---");
        System.out.println("class Person {");
        System.out.println("    private String name;");
        System.out.println("    private int age;");
        System.out.println();
        System.out.println("    public Person() {");
        System.out.println("        this(\"Unknown\", 0);  // Call other constructor");
        System.out.println("    }");
        System.out.println();
        System.out.println("    public Person(String name) {");
        System.out.println("        this(name, 0);  // Call other constructor");
        System.out.println("    }");
        System.out.println();
        System.out.println("    public Person(String name, int age) {");
        System.out.println("        this.name = name;");
        System.out.println("        this.age = age;");
        System.out.println("    }");
        System.out.println("}\n");

        ThisExample p1 = new ThisExample();
        ThisExample p2 = new ThisExample("Alice");
        ThisExample p3 = new ThisExample("Bob", 30);

        System.out.println("--- EXAM TRAPS ---");
        System.out.println("\n1. this() must be FIRST statement");
        System.out.println("class Bad {");
        System.out.println("    Bad() {");
        System.out.println("        System.out.println(\"Hi\");");
        System.out.println("        this(5);  // ✗ DOES NOT COMPILE - not first!");
        System.out.println("    }");
        System.out.println("    Bad(int x) { }");
        System.out.println("}");

        System.out.println("\n2. Cannot use both this() and super()");
        System.out.println("class Bad extends Parent {");
        System.out.println("    Bad() {");
        System.out.println("        super();  // ✗ DOES NOT COMPILE");
        System.out.println("        this(5);  // Cannot use both!");
        System.out.println("    }");
        System.out.println("    Bad(int x) { }");
        System.out.println("}");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * super() - CALLING PARENT CONSTRUCTORS
     * ═══════════════════════════════════════════════════════════════════════
     *
     * *** MOST IMPORTANT EXAM CONCEPT ***
     * Every constructor has super() or this() as first statement
     * If missing, compiler inserts super() automatically
     */
    private static void superKeyword() {
        System.out.println("=== super() - CALLING PARENT CONSTRUCTORS ===");
        System.out.println("*** MOST IMPORTANT EXAM CONCEPT ***\n");

        System.out.println("CRITICAL RULE:");
        System.out.println("  First statement of EVERY constructor is:");
        System.out.println("  • Explicit super() call, OR");
        System.out.println("  • Explicit this() call, OR");
        System.out.println("  • Implicit super() call (compiler inserts)\n");

        System.out.println("--- Example 1: Explicit super() ---");
        System.out.println("class Parent {");
        System.out.println("    Parent(String name) {");
        System.out.println("        System.out.println(\"Parent: \" + name);");
        System.out.println("    }");
        System.out.println("}");
        System.out.println();
        System.out.println("class Child extends Parent {");
        System.out.println("    Child(String name) {");
        System.out.println("        super(name);  // Must call parent constructor");
        System.out.println("        System.out.println(\"Child: \" + name);");
        System.out.println("    }");
        System.out.println("}\n");

        SuperConstructorExample child1 = new SuperConstructorExample("Test");

        System.out.println("\n--- Example 2: Implicit super() ---");
        System.out.println("class Parent {");
        System.out.println("    Parent() { }  // No-arg constructor");
        System.out.println("}");
        System.out.println();
        System.out.println("class Child extends Parent {");
        System.out.println("    Child() {");
        System.out.println("        // Compiler inserts: super();");
        System.out.println("        System.out.println(\"Child\");");
        System.out.println("    }");
        System.out.println("}\n");

        ImplicitSuperExample child2 = new ImplicitSuperExample();

        System.out.println("\n--- EXAM TRAP: Missing Parent No-Arg Constructor ---");
        System.out.println("class Parent {");
        System.out.println("    Parent(String name) { }  // Only parameterized constructor");
        System.out.println("}");
        System.out.println();
        System.out.println("class Child extends Parent {");
        System.out.println("    Child() {");
        System.out.println("        // Compiler tries: super();");
        System.out.println("        // ✗ DOES NOT COMPILE - Parent has no no-arg constructor!");
        System.out.println("    }");
        System.out.println("}");
        System.out.println("\nSOLUTION: Call super(name) explicitly");

        System.out.println("\n--- super() Rules ---");
        System.out.println("1. Must be FIRST statement");
        System.out.println("2. Cannot use with this() in same constructor");
        System.out.println("3. If omitted, compiler inserts super()");
        System.out.println("4. Parent must have matching constructor");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * FINAL FIELDS IN CONSTRUCTORS
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void finalFields() {
        System.out.println("=== FINAL FIELDS IN CONSTRUCTORS ===\n");

        System.out.println("final fields must be initialized in:");
        System.out.println("  1. Inline (where declared)");
        System.out.println("  2. Instance initializer block { }");
        System.out.println("  3. Constructor\n");

        System.out.println("--- Example: Final Fields in Constructor ---");
        System.out.println("class Example {");
        System.out.println("    private final int a = 10;    // Inline");
        System.out.println("    private final int b;         // In instance initializer");
        System.out.println("    private final int c;         // In constructor");
        System.out.println();
        System.out.println("    {");
        System.out.println("        b = 20;  // Instance initializer");
        System.out.println("    }");
        System.out.println();
        System.out.println("    public Example() {");
        System.out.println("        c = 30;  // Constructor");
        System.out.println("    }");
        System.out.println("}\n");

        FinalFieldsExample obj = new FinalFieldsExample();
        System.out.println("All final fields initialized: a=" + obj.getA()
                         + ", b=" + obj.getB() + ", c=" + obj.getC());

        System.out.println("\n--- Multiple Constructors with Final Fields ---");
        System.out.println("class Example {");
        System.out.println("    private final int x;");
        System.out.println();
        System.out.println("    public Example() {");
        System.out.println("        x = 10;  // Must initialize");
        System.out.println("    }");
        System.out.println();
        System.out.println("    public Example(int val) {");
        System.out.println("        x = val;  // Must initialize in ALL constructors");
        System.out.println("    }");
        System.out.println("}");

        System.out.println("\nRULE: ALL constructors must initialize final fields!");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * ORDER OF INITIALIZATION - CRITICAL EXAM TOPIC
     * ═══════════════════════════════════════════════════════════════════════
     *
     * *** MEMORIZE THIS ORDER ***
     */
    private static void orderOfInitialization() {
        System.out.println("=== ORDER OF INITIALIZATION ===");
        System.out.println("*** CRITICAL EXAM TOPIC - MEMORIZE THIS! ***\n");

        System.out.println("ORDER (single class):");
        System.out.println("  1. Static variables (in order declared)");
        System.out.println("  2. Static initializer blocks (in order)");
        System.out.println("  3. Instance variables (in order declared)");
        System.out.println("  4. Instance initializer blocks (in order)");
        System.out.println("  5. Constructor body\n");

        System.out.println("ORDER (with inheritance):");
        System.out.println("  1. Parent static initialization (if first time)");
        System.out.println("  2. Child static initialization (if first time)");
        System.out.println("  3. Parent instance variables");
        System.out.println("  4. Parent instance initializer blocks");
        System.out.println("  5. Parent constructor body");
        System.out.println("  6. Child instance variables");
        System.out.println("  7. Child instance initializer blocks");
        System.out.println("  8. Child constructor body\n");

        System.out.println("--- Demonstration ---");
        InitializationOrder obj = new InitializationOrder();

        System.out.println("\n*** EXAM TIP ***");
        System.out.println("Remember: Static → Parent → Child");
        System.out.println("Within each: Variables → Blocks → Constructor");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * COMMON EXAM TRAPS
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void commonExamTraps() {
        System.out.println("=== COMMON EXAM TRAPS ===\n");

        System.out.println("--- TRAP 1: this() not first ---");
        System.out.println("class Bad {");
        System.out.println("    Bad() {");
        System.out.println("        System.out.println(\"test\");");
        System.out.println("        this(5);  // ✗ DOES NOT COMPILE");
        System.out.println("    }");
        System.out.println("    Bad(int x) { }");
        System.out.println("}\n");

        System.out.println("--- TRAP 2: Both this() and super() ---");
        System.out.println("class Bad extends Parent {");
        System.out.println("    Bad() {");
        System.out.println("        super();");
        System.out.println("        this(5);  // ✗ DOES NOT COMPILE");
        System.out.println("    }");
        System.out.println("    Bad(int x) { }");
        System.out.println("}\n");

        System.out.println("--- TRAP 3: Missing parent no-arg constructor ---");
        System.out.println("class Parent {");
        System.out.println("    Parent(int x) { }  // Only parameterized");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    Child() { }  // ✗ DOES NOT COMPILE");
        System.out.println("    // Compiler inserts super(), but Parent has no no-arg");
        System.out.println("}\n");

        System.out.println("--- TRAP 4: Final field not initialized ---");
        System.out.println("class Bad {");
        System.out.println("    private final int x;");
        System.out.println("    Bad() { }  // ✗ DOES NOT COMPILE - x not initialized");
        System.out.println("}\n");

        System.out.println("--- TRAP 5: Default constructor not created ---");
        System.out.println("class Parent {");
        System.out.println("    Parent(int x) { }  // User-defined constructor");
        System.out.println("}");
        System.out.println("// Parent p = new Parent();  // ✗ DOES NOT COMPILE");
        System.out.println("// No default constructor created!\n");

        System.out.println("*** REMEMBER ***");
        System.out.println("• this() or super() must be FIRST");
        System.out.println("• Cannot use both this() and super()");
        System.out.println("• Implicit super() requires parent no-arg constructor");
        System.out.println("• All constructors must initialize final fields");
        System.out.println("• Default constructor only if NO user-defined constructors");

        System.out.println();
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// EXAMPLE CLASSES
// ═══════════════════════════════════════════════════════════════════════════

class DefaultConstructorExample {
    // No constructor defined - Java creates default
}

class OverloadedConstructor {
    OverloadedConstructor() {
        System.out.println("No-arg constructor");
    }

    OverloadedConstructor(String name) {
        System.out.println("Constructor with name: " + name);
    }

    OverloadedConstructor(String name, int age) {
        System.out.println("Constructor with name: " + name + ", age: " + age);
    }
}

class ThisExample {
    private String name;
    private int age;

    ThisExample() {
        this("Unknown", 0);
        System.out.println("No-arg constructor called");
    }

    ThisExample(String name) {
        this(name, 0);
        System.out.println("Single-parameter constructor called");
    }

    ThisExample(String name, int age) {
        this.name = name;
        this.age = age;
        System.out.println("Two-parameter constructor called: " + name + ", " + age);
    }
}

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

class ParentWithNoArg {
    ParentWithNoArg() {
        System.out.println("Parent no-arg constructor");
    }
}

class ImplicitSuperExample extends ParentWithNoArg {
    ImplicitSuperExample() {
        // Compiler inserts: super();
        System.out.println("Child constructor");
    }
}

class FinalFieldsExample {
    private final int a = 10;
    private final int b;
    private final int c;

    {
        b = 20;
    }

    FinalFieldsExample() {
        c = 30;
    }

    int getA() { return a; }
    int getB() { return b; }
    int getC() { return c; }
}

// Order of initialization example
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
