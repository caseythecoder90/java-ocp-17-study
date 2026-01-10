package ch06inheritance;

/**
 * INITIALIZING OBJECTS - Complete Guide for OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CLASS LOADING (INITIALIZATION) - HAPPENS ONCE PER CLASS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Each class is loaded AT MOST ONCE by the JVM.
 *
 * CLASS LOADING RULES (in order):
 * 1. If there is a superclass Y of X, initialize class Y first
 * 2. Process all static variable declarations in the order they appear
 * 3. Process all static initializers in the order they appear
 *
 * Class is loaded when:
 * - First time class is referenced
 * - Creating instance with new
 * - Accessing static member
 * - Using reflection
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * DEFAULT VALUES FOR INSTANCE MEMBERS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Instance members are assigned default values if no value is specified.
 *
 * IMPORTANT: This is ONLY for non-final fields!
 *
 * Default values by type:
 * - boolean     → false
 * - byte/short/int/long → 0
 * - float/double → 0.0
 * - char        → '\u0000' (null character)
 * - Object reference → null
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * FINAL STATIC VARIABLES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * final static variables MUST be explicitly assigned a value exactly once.
 *
 * Can be assigned:
 * 1. Inline (where declared)
 * 2. In a static initializer block
 *
 * CANNOT be assigned:
 * - In instance initializer (wrong context - static vs instance)
 * - In constructor (wrong context - static vs instance)
 *
 * Must be assigned before class initialization completes.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * FINAL INSTANCE VARIABLES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * final instance variables MUST be explicitly assigned a value exactly once.
 *
 * Can be assigned:
 * 1. Inline (where declared)
 * 2. In an instance initializer block
 * 3. In a constructor
 *
 * CRITICAL RULES:
 * - ALL constructors must initialize final instance fields
 * - By the time constructor completes, all final instance variables must be assigned
 * - Unlike local final variables, member final variables MUST be assigned a value
 * - Failure to do so results in compiler error on the line that declares the constructor
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * OBJECT INITIALIZATION - HAPPENS EACH TIME OBJECT IS CREATED
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * OBJECT INITIALIZATION RULES (in order):
 * 1. Initialize class X if it has not been previously initialized
 * 2. If there is a superclass Y of X, initialize the instance of Y first
 * 3. Process all instance variable declarations in the order they appear
 * 4. Process all instance initializers in the order they appear
 * 5. Initialize the constructor, including any overloaded constructors with this()
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * COMPLETE INITIALIZATION ORDER (CLASS + INSTANCE)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * *** MEMORIZE THIS FOR THE EXAM ***
 *
 * FIRST TIME CREATING OBJECT:
 * 1. Load superclass (if exists, recursively)
 * 2. Load current class
 *    a. Static variables (in order)
 *    b. Static initializers (in order)
 * 3. Initialize superclass instance (if exists)
 *    a. Instance variables (in order)
 *    b. Instance initializers (in order)
 *    c. Constructor
 * 4. Initialize current class instance
 *    a. Instance variables (in order)
 *    b. Instance initializers (in order)
 *    c. Constructor
 *
 * SUBSEQUENT OBJECT CREATION:
 * (Steps 1-2 skipped - class already loaded!)
 * 3. Initialize superclass instance
 * 4. Initialize current class instance
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * EXAM TRAPS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * 1. Class loading happens ONCE, instance initialization happens EVERY time
 * 2. Static final fields MUST be initialized (inline or static initializer)
 * 3. Instance final fields MUST be initialized by end of constructor
 * 4. Order matters - variables/initializers execute in declaration order
 * 5. Cannot use instance variables in static context
 * 6. Cannot forward reference variables (use before declaration)
 * 7. Parent initialization ALWAYS happens before child
 */
public class InitializingObjects {

    public static void main(String[] args) {
        System.out.println("=== INITIALIZING OBJECTS ===\n");

        classLoadingBasics();
        defaultValues();
        staticFinalVariables();
        instanceFinalVariables();
        objectInitialization();
        completeInitializationOrder();
        multipleObjectCreation();
        examTraps();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * CLASS LOADING BASICS
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void classLoadingBasics() {
        System.out.println("=== CLASS LOADING BASICS ===");
        System.out.println("*** Each class is loaded AT MOST ONCE by the JVM ***\n");

        System.out.println("CLASS LOADING RULES (in order):");
        System.out.println("1. If there is a superclass Y of X, initialize class Y first");
        System.out.println("2. Process all static variable declarations in order");
        System.out.println("3. Process all static initializers in order\n");

        System.out.println("Class is loaded when:");
        System.out.println("  • First time class is referenced");
        System.out.println("  • Creating instance with new");
        System.out.println("  • Accessing static member");
        System.out.println("  • Using reflection\n");

        System.out.println("--- Demonstration ---");
        System.out.println("Creating ClassLoadingExample instance:\n");
        ClassLoadingExample obj1 = new ClassLoadingExample();

        System.out.println("\n--- Key Observation ---");
        System.out.println("Notice the ORDER:");
        System.out.println("1. Static variable initialized first");
        System.out.println("2. Static initializer executed second");
        System.out.println("3. Instance members initialized after static\n");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * DEFAULT VALUES
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void defaultValues() {
        System.out.println("=== DEFAULT VALUES ===");
        System.out.println("*** IMPORTANT: Only for NON-FINAL fields ***\n");

        System.out.println("Instance members get default values if not initialized:\n");

        DefaultValuesExample obj = new DefaultValuesExample();
        System.out.println("boolean:  " + obj.boolValue);     // false
        System.out.println("byte:     " + obj.byteValue);     // 0
        System.out.println("short:    " + obj.shortValue);    // 0
        System.out.println("int:      " + obj.intValue);      // 0
        System.out.println("long:     " + obj.longValue);     // 0
        System.out.println("float:    " + obj.floatValue);    // 0.0
        System.out.println("double:   " + obj.doubleValue);   // 0.0
        System.out.println("char:     [" + obj.charValue + "]");  // '\u0000'
        System.out.println("String:   " + obj.stringValue);   // null

        System.out.println("\n--- CRITICAL EXAM RULE ---");
        System.out.println("Final fields do NOT get default values!");
        System.out.println("They MUST be explicitly initialized.\n");

        System.out.println("class Bad {");
        System.out.println("    private final int x;  // ✗ DOES NOT COMPILE");
        System.out.println("    // final field not initialized!");
        System.out.println("}");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * STATIC FINAL VARIABLES
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void staticFinalVariables() {
        System.out.println("=== STATIC FINAL VARIABLES ===");
        System.out.println("*** Must be explicitly assigned exactly once ***\n");

        System.out.println("Can be assigned:");
        System.out.println("1. Inline (where declared)");
        System.out.println("2. In static initializer block\n");

        System.out.println("--- Example 1: Inline Initialization ---");
        System.out.println("class Example {");
        System.out.println("    private static final int X = 10;  // ✓ Valid");
        System.out.println("}\n");

        System.out.println("--- Example 2: Static Initializer ---");
        System.out.println("class Example {");
        System.out.println("    private static final int X;");
        System.out.println("    static {");
        System.out.println("        X = 10;  // ✓ Valid");
        System.out.println("    }");
        System.out.println("}\n");

        System.out.println("--- Example 3: CANNOT initialize in instance context ---");
        System.out.println("class Bad {");
        System.out.println("    private static final int X;");
        System.out.println("    {");
        System.out.println("        X = 10;  // ✗ DOES NOT COMPILE - instance initializer!");
        System.out.println("    }");
        System.out.println("}\n");

        System.out.println("class Bad {");
        System.out.println("    private static final int X;");
        System.out.println("    public Bad() {");
        System.out.println("        X = 10;  // ✗ DOES NOT COMPILE - constructor!");
        System.out.println("    }");
        System.out.println("}\n");

        System.out.println("--- Working Example ---");
        System.out.println("StaticFinalExample.INLINE_INIT = " + StaticFinalExample.INLINE_INIT);
        System.out.println("StaticFinalExample.BLOCK_INIT = " + StaticFinalExample.BLOCK_INIT);

        System.out.println("\n*** REMEMBER: static final = inline or static initializer ONLY ***");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * INSTANCE FINAL VARIABLES
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void instanceFinalVariables() {
        System.out.println("=== INSTANCE FINAL VARIABLES ===");
        System.out.println("*** Must be explicitly assigned exactly once ***\n");

        System.out.println("Can be assigned:");
        System.out.println("1. Inline (where declared)");
        System.out.println("2. In instance initializer block");
        System.out.println("3. In constructor\n");

        System.out.println("--- Example 1: Inline Initialization ---");
        System.out.println("class Example {");
        System.out.println("    private final int x = 10;  // ✓ Valid");
        System.out.println("}\n");

        System.out.println("--- Example 2: Instance Initializer ---");
        System.out.println("class Example {");
        System.out.println("    private final int x;");
        System.out.println("    {");
        System.out.println("        x = 10;  // ✓ Valid");
        System.out.println("    }");
        System.out.println("}\n");

        System.out.println("--- Example 3: Constructor ---");
        System.out.println("class Example {");
        System.out.println("    private final int x;");
        System.out.println("    public Example() {");
        System.out.println("        x = 10;  // ✓ Valid");
        System.out.println("    }");
        System.out.println("}\n");

        System.out.println("--- CRITICAL EXAM TRAP ---");
        System.out.println("ALL constructors must initialize final instance fields!\n");

        System.out.println("class Bad {");
        System.out.println("    private final int x;");
        System.out.println("    public Bad() {");
        System.out.println("        x = 10;  // ✓ Initializes x");
        System.out.println("    }");
        System.out.println("    public Bad(int val) {");
        System.out.println("        // ✗ DOES NOT COMPILE - x not initialized!");
        System.out.println("    }");
        System.out.println("}\n");

        System.out.println("--- EXAM DIFFERENCE: Final Instance vs Final Local ---");
        System.out.println("Instance final: MUST be assigned (compiler error if not)");
        System.out.println("Local final:    Can be unassigned (error only if used)\n");

        System.out.println("void method() {");
        System.out.println("    final int x;  // ✓ Valid - local final can be unassigned");
        System.out.println("    // System.out.println(x);  // Would be error");
        System.out.println("}\n");

        System.out.println("class Example {");
        System.out.println("    private final int x;  // ✗ DOES NOT COMPILE");
        System.out.println("    // Must initialize in constructor");
        System.out.println("}");

        System.out.println("\n--- Working Example ---");
        InstanceFinalExample obj = new InstanceFinalExample(100);
        System.out.println("inlineInit = " + obj.getInlineInit());
        System.out.println("blockInit = " + obj.getBlockInit());
        System.out.println("constructorInit = " + obj.getConstructorInit());

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * OBJECT INITIALIZATION
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void objectInitialization() {
        System.out.println("=== OBJECT INITIALIZATION ===");
        System.out.println("*** Happens EVERY time an object is created ***\n");

        System.out.println("OBJECT INITIALIZATION RULES (in order):");
        System.out.println("1. Initialize class X if not previously initialized");
        System.out.println("2. If there is a superclass Y, initialize instance of Y first");
        System.out.println("3. Process all instance variable declarations in order");
        System.out.println("4. Process all instance initializers in order");
        System.out.println("5. Initialize constructor, including this() calls\n");

        System.out.println("--- Demonstration ---");
        ObjectInitializationExample obj = new ObjectInitializationExample();

        System.out.println("\n--- Key Observations ---");
        System.out.println("• Instance variables initialized before constructor");
        System.out.println("• Instance initializer blocks run before constructor");
        System.out.println("• Order of declaration matters!");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * COMPLETE INITIALIZATION ORDER WITH INHERITANCE
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void completeInitializationOrder() {
        System.out.println("=== COMPLETE INITIALIZATION ORDER ===");
        System.out.println("*** CRITICAL EXAM TOPIC - MEMORIZE THIS! ***\n");

        System.out.println("Order when creating FIRST object:");
        System.out.println("1. Parent static variables");
        System.out.println("2. Parent static initializers");
        System.out.println("3. Child static variables");
        System.out.println("4. Child static initializers");
        System.out.println("5. Parent instance variables");
        System.out.println("6. Parent instance initializers");
        System.out.println("7. Parent constructor");
        System.out.println("8. Child instance variables");
        System.out.println("9. Child instance initializers");
        System.out.println("10. Child constructor\n");

        System.out.println("--- Demonstration ---");
        CompleteOrderChild obj1 = new CompleteOrderChild();

        System.out.println("\n\n--- Creating SECOND object (class already loaded) ---");
        CompleteOrderChild obj2 = new CompleteOrderChild();

        System.out.println("\n--- Key Observation ---");
        System.out.println("Static initialization skipped for second object!");
        System.out.println("Class loading happens ONCE, instance initialization EVERY time.");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * MULTIPLE OBJECT CREATION
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void multipleObjectCreation() {
        System.out.println("=== MULTIPLE OBJECT CREATION ===\n");

        System.out.println("Creating first MultipleExample object:");
        MultipleExample obj1 = new MultipleExample(1);

        System.out.println("\nCreating second MultipleExample object:");
        MultipleExample obj2 = new MultipleExample(2);

        System.out.println("\nCreating third MultipleExample object:");
        MultipleExample obj3 = new MultipleExample(3);

        System.out.println("\n--- Observation ---");
        System.out.println("Static initialization ran ONCE (first object)");
        System.out.println("Instance initialization ran THREE TIMES (each object)");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * COMMON EXAM TRAPS
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void examTraps() {
        System.out.println("=== COMMON EXAM TRAPS ===\n");

        System.out.println("--- TRAP 1: Forward Reference ---");
        System.out.println("class Bad {");
        System.out.println("    private int x = y;  // ✗ DOES NOT COMPILE");
        System.out.println("    private int y = 10; // Cannot use y before declaration");
        System.out.println("}\n");

        System.out.println("--- TRAP 2: Using instance in static context ---");
        System.out.println("class Bad {");
        System.out.println("    private int x = 10;");
        System.out.println("    private static int y = x;  // ✗ DOES NOT COMPILE");
        System.out.println("    // Cannot access instance variable in static context");
        System.out.println("}\n");

        System.out.println("--- TRAP 3: Final field not initialized ---");
        System.out.println("class Bad {");
        System.out.println("    private final int x;");
        System.out.println("    public Bad() { }  // ✗ DOES NOT COMPILE");
        System.out.println("    // Error on THIS line (constructor), not field declaration!");
        System.out.println("}\n");

        System.out.println("--- TRAP 4: Initializing static final in constructor ---");
        System.out.println("class Bad {");
        System.out.println("    private static final int X;");
        System.out.println("    public Bad() {");
        System.out.println("        X = 10;  // ✗ DOES NOT COMPILE");
        System.out.println("        // Cannot initialize static in instance context");
        System.out.println("    }");
        System.out.println("}\n");

        System.out.println("--- TRAP 5: Order of initialization matters ---");
        System.out.println("class Tricky {");
        System.out.println("    private int x = getValue();");
        System.out.println("    private int y = 20;");
        System.out.println("    private int getValue() { return y; }");
        System.out.println("}");
        System.out.println("// getValue() is called BEFORE y is initialized!");
        System.out.println("// So x gets 0 (default value), not 20\n");

        TrickyOrder tricky = new TrickyOrder();
        System.out.println("x = " + tricky.getX() + " (expected 20, got 0!)");
        System.out.println("y = " + tricky.getY() + "\n");

        System.out.println("*** EXAM TIP ***");
        System.out.println("Watch for questions testing:");
        System.out.println("• Class loading (ONCE) vs instance initialization (EVERY time)");
        System.out.println("• Parent initialization before child");
        System.out.println("• Static vs instance context");
        System.out.println("• Final field initialization requirements");
        System.out.println("• Order of declaration");

        System.out.println();
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// EXAMPLE CLASSES
// ═══════════════════════════════════════════════════════════════════════════

class ClassLoadingExample {
    static {
        System.out.println("1. Static initializer");
    }

    private static int staticVar = initStatic();

    private static int initStatic() {
        System.out.println("2. Static variable initialization");
        return 100;
    }

    private int instanceVar = initInstance();

    private int initInstance() {
        System.out.println("3. Instance variable initialization");
        return 200;
    }

    {
        System.out.println("4. Instance initializer");
    }

    ClassLoadingExample() {
        System.out.println("5. Constructor");
    }
}

// ───────────────────────────────────────────────────────────────────────────
// DEFAULT VALUES EXAMPLE
// ───────────────────────────────────────────────────────────────────────────

class DefaultValuesExample {
    boolean boolValue;    // false
    byte byteValue;       // 0
    short shortValue;     // 0
    int intValue;         // 0
    long longValue;       // 0
    float floatValue;     // 0.0
    double doubleValue;   // 0.0
    char charValue;       // '\u0000'
    String stringValue;   // null
}

// ───────────────────────────────────────────────────────────────────────────
// STATIC FINAL EXAMPLE
// ───────────────────────────────────────────────────────────────────────────

class StaticFinalExample {
    static final int INLINE_INIT = 100;

    static final int BLOCK_INIT;
    static {
        BLOCK_INIT = 200;
    }

    // Cannot initialize in instance context:
    // static final int BAD;
    // { BAD = 300; }  // DOES NOT COMPILE
}

// ───────────────────────────────────────────────────────────────────────────
// INSTANCE FINAL EXAMPLE
// ───────────────────────────────────────────────────────────────────────────

class InstanceFinalExample {
    private final int inlineInit = 10;

    private final int blockInit;
    {
        blockInit = 20;
    }

    private final int constructorInit;

    InstanceFinalExample(int val) {
        constructorInit = val;
    }

    int getInlineInit() { return inlineInit; }
    int getBlockInit() { return blockInit; }
    int getConstructorInit() { return constructorInit; }
}

// ───────────────────────────────────────────────────────────────────────────
// OBJECT INITIALIZATION EXAMPLE
// ───────────────────────────────────────────────────────────────────────────

class ObjectInitializationExample {
    private int x = initX();

    private int initX() {
        System.out.println("1. Instance variable x initialized");
        return 10;
    }

    private int y = initY();

    private int initY() {
        System.out.println("2. Instance variable y initialized");
        return 20;
    }

    {
        System.out.println("3. Instance initializer block");
    }

    ObjectInitializationExample() {
        System.out.println("4. Constructor");
    }
}

// ───────────────────────────────────────────────────────────────────────────
// COMPLETE ORDER EXAMPLE WITH INHERITANCE
// ───────────────────────────────────────────────────────────────────────────

class CompleteOrderParent {
    static {
        System.out.println("1. Parent static initializer");
    }

    {
        System.out.println("5. Parent instance initializer");
    }

    CompleteOrderParent() {
        System.out.println("6. Parent constructor");
    }
}

class CompleteOrderChild extends CompleteOrderParent {
    static {
        System.out.println("2. Child static initializer");
    }

    {
        System.out.println("7. Child instance initializer");
    }

    CompleteOrderChild() {
        System.out.println("8. Child constructor");
    }
}

// ───────────────────────────────────────────────────────────────────────────
// MULTIPLE OBJECT CREATION EXAMPLE
// ───────────────────────────────────────────────────────────────────────────

class MultipleExample {
    static {
        System.out.println("  [Static: Class loaded]");
    }

    {
        System.out.println("  [Instance: Object initialized]");
    }

    MultipleExample(int id) {
        System.out.println("  [Constructor: Object " + id + " created]");
    }
}

// ───────────────────────────────────────────────────────────────────────────
// TRICKY ORDER EXAMPLE
// ───────────────────────────────────────────────────────────────────────────

class TrickyOrder {
    private int x = getValue();  // Called BEFORE y is initialized
    private int y = 20;

    private int getValue() {
        return y;  // y not initialized yet, returns default value 0
    }

    int getX() { return x; }
    int getY() { return y; }
}
