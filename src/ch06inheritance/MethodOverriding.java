package ch06inheritance;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * METHOD OVERRIDING - Complete Guide for OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * METHOD OVERRIDING DEFINITION
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Overriding in Java is when a subclass declares a new implementation for an
 * inherited method with the same signature and compatible return type.
 *
 * You can still access the parent version using super.method()
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * FOUR KEY RULES FOR OVERRIDING - MEMORIZE THESE!
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * 1. SIGNATURE RULE:
 *    The method in the child class must have the SAME SIGNATURE as the method
 *    in the parent class (same name, same parameter types, same order).
 *
 * 2. ACCESS MODIFIER RULE:
 *    The method in the child class must be AT LEAST AS ACCESSIBLE as the
 *    method in the parent class.
 *    Order: private < package-private < protected < public
 *    Can make MORE accessible, but NOT less accessible.
 *
 * 3. EXCEPTION RULE:
 *    The method in the child class may NOT declare a checked exception that is
 *    new or broader than the class of any exception declared in the parent
 *    class method.
 *    - Can declare fewer exceptions
 *    - Can declare narrower (subclass) exceptions
 *    - Can declare unchecked exceptions (RuntimeException)
 *    - CANNOT declare new checked exceptions
 *    - CANNOT declare broader checked exceptions
 *
 * 4. RETURN TYPE RULE (COVARIANT RETURN TYPES):
 *    If the method returns a value, it must be the same or a subtype of the
 *    method in the parent class.
 *
 *    COVARIANCE TEST:
 *    Given an inherited return type A and an overriding return type B,
 *    can you assign an instance of B to a reference variable for A without a cast?
 *    If so, they are covariant.
 *
 *    Example: B b = new B();
 *             A a = b;  // No cast needed? → Covariant ✓
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * REDECLARING PRIVATE METHODS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Private methods are NOT inherited, therefore they CANNOT be overridden.
 * If you declare a method with the same signature in the child class, it's
 * a NEW method, not an override.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * HIDING STATIC METHODS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Static methods are HIDDEN, not overridden.
 *
 * Rules for hiding static methods:
 * 1. Must have same signature
 * 2. Must be at least as accessible
 * 3. Must follow exception rules
 * 4. Must follow return type rules
 * 5. BOTH must be static (if parent is static, child must be static)
 *
 * Key difference: The version that runs depends on the REFERENCE TYPE,
 * not the object type (unlike instance method overriding).
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * HIDDEN VARIABLES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Variables (fields) are HIDDEN, not overridden.
 * Each class has its own copy of the field.
 * The version accessed depends on the REFERENCE TYPE, not object type.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * FINAL METHODS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Methods marked as final CANNOT be overridden.
 * Attempting to override a final method results in a compiler error.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * EXAM TRAPS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * 1. Reducing access modifier (public → protected) ✗
 * 2. Adding new checked exception ✗
 * 3. Changing return type to unrelated type ✗
 * 4. Overriding static method with instance method ✗
 * 5. Overriding instance method with static method ✗
 * 6. Overriding final method ✗
 * 7. Overriding private method (it's not overriding!) ✗
 */
public class MethodOverriding {

    public static void main(String[] args) {
        System.out.println("=== METHOD OVERRIDING ===\n");

        basicOverriding();
        signatureRule();
        accessModifierRule();
        exceptionRule();
        returnTypeRule();
        covariantReturnTypes();
        covariantTest();
        redeclaringPrivateMethods();
        hidingStaticMethods();
        hiddenVariables();
        finalMethods();
        examTraps();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * BASIC OVERRIDING
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void basicOverriding() {
        System.out.println("=== BASIC OVERRIDING ===\n");

        System.out.println("Overriding: Subclass declares new implementation for");
        System.out.println("inherited method with same signature.\n");

        BasicParent parent = new BasicParent();
        parent.display();

        System.out.println();

        BasicChild child = new BasicChild();
        child.display();        // Overridden version
        child.callParent();     // Access parent version with super

        System.out.println("\n--- Polymorphism ---");
        BasicParent poly = new BasicChild();
        poly.display();  // Calls child version (polymorphism!)

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * RULE 1: SIGNATURE RULE
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void signatureRule() {
        System.out.println("=== RULE 1: SIGNATURE RULE ===");
        System.out.println("*** Child method must have SAME SIGNATURE as parent ***\n");

        System.out.println("Signature = method name + parameter types + order\n");

        System.out.println("--- Valid Override ✓ ---");
        System.out.println("class Parent {");
        System.out.println("    void method(int x) { }");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    void method(int x) { }  // ✓ Same signature");
        System.out.println("}\n");

        System.out.println("--- NOT Override (Overload) ---");
        System.out.println("class Parent {");
        System.out.println("    void method(int x) { }");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    void method(long x) { }  // Different parameter type");
        System.out.println("    // This is OVERLOADING, not overriding!");
        System.out.println("}\n");

        System.out.println("--- Invalid Override ✗ ---");
        System.out.println("class Parent {");
        System.out.println("    void method(int x) { }");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    int method(int x) { }  // ✗ DOES NOT COMPILE");
        System.out.println("    // Different return type with same signature!");
        System.out.println("}");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * RULE 2: ACCESS MODIFIER RULE
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void accessModifierRule() {
        System.out.println("=== RULE 2: ACCESS MODIFIER RULE ===");
        System.out.println("*** Child method must be AT LEAST AS ACCESSIBLE ***\n");

        System.out.println("Access modifier order (least to most accessible):");
        System.out.println("  private < package-private < protected < public\n");

        System.out.println("Rule: Can make MORE accessible, NOT less accessible\n");

        System.out.println("--- Valid Examples ✓ ---");
        System.out.println("Parent: protected → Child: protected  ✓ Same");
        System.out.println("Parent: protected → Child: public     ✓ More accessible");
        System.out.println("Parent: package   → Child: protected  ✓ More accessible");
        System.out.println("Parent: package   → Child: public     ✓ More accessible\n");

        System.out.println("--- Invalid Examples ✗ ---");
        System.out.println("Parent: public    → Child: protected  ✗ Less accessible");
        System.out.println("Parent: public    → Child: package    ✗ Less accessible");
        System.out.println("Parent: protected → Child: package    ✗ Less accessible");
        System.out.println("Parent: protected → Child: private    ✗ Less accessible\n");

        System.out.println("--- Code Example ✗ ---");
        System.out.println("class Parent {");
        System.out.println("    public void method() { }");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    protected void method() { }  // ✗ DOES NOT COMPILE");
        System.out.println("    // Cannot reduce from public to protected");
        System.out.println("}");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * RULE 3: EXCEPTION RULE - CRITICAL EXAM TOPIC
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void exceptionRule() {
        System.out.println("=== RULE 3: EXCEPTION RULE ===");
        System.out.println("*** CRITICAL EXAM TOPIC - VERY TRICKY ***\n");

        System.out.println("Rule: Child method CANNOT declare new or broader CHECKED exceptions\n");

        System.out.println("Can do:");
        System.out.println("  ✓ Declare fewer exceptions");
        System.out.println("  ✓ Declare narrower (subclass) exceptions");
        System.out.println("  ✓ Declare unchecked exceptions (any time)");
        System.out.println("  ✓ Declare no exceptions\n");

        System.out.println("Cannot do:");
        System.out.println("  ✗ Declare new checked exception");
        System.out.println("  ✗ Declare broader (superclass) checked exception\n");

        System.out.println("--- Example 1: Fewer Exceptions ✓ ---");
        System.out.println("class Parent {");
        System.out.println("    void method() throws IOException { }");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    void method() { }  // ✓ No exception - OK!");
        System.out.println("}\n");

        System.out.println("--- Example 2: Narrower Exception ✓ ---");
        System.out.println("class Parent {");
        System.out.println("    void method() throws IOException { }");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    void method() throws FileNotFoundException { }");
        System.out.println("    // ✓ FileNotFoundException is subclass of IOException");
        System.out.println("}\n");

        System.out.println("--- Example 3: Broader Exception ✗ ---");
        System.out.println("class Parent {");
        System.out.println("    void method() throws FileNotFoundException { }");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    void method() throws IOException { }");
        System.out.println("    // ✗ DOES NOT COMPILE - IOException is broader!");
        System.out.println("}\n");

        System.out.println("--- Example 4: New Checked Exception ✗ ---");
        System.out.println("class Parent {");
        System.out.println("    void method() throws IOException { }");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    void method() throws IOException, SQLException { }");
        System.out.println("    // ✗ DOES NOT COMPILE - SQLException is new!");
        System.out.println("}\n");

        System.out.println("--- Example 5: Unchecked Exceptions Always OK ✓ ---");
        System.out.println("class Parent {");
        System.out.println("    void method() { }");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    void method() throws NullPointerException { }");
        System.out.println("    // ✓ Unchecked exceptions can be added anytime");
        System.out.println("}\n");

        System.out.println("--- Working Example ---");
        ExceptionChild child = new ExceptionChild();
        try {
            child.fewerExceptions();
            child.narrowerException();
            child.uncheckedOK();
        } catch (Exception e) {
            // Handle
        }

        System.out.println("*** EXAM TIP ***");
        System.out.println("Remember: Exception hierarchy matters!");
        System.out.println("  Exception");
        System.out.println("    ├─ IOException (checked)");
        System.out.println("    │   └─ FileNotFoundException (checked, narrower)");
        System.out.println("    └─ RuntimeException (unchecked)");
        System.out.println("         └─ NullPointerException (unchecked)");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * RULE 4: RETURN TYPE RULE (COVARIANT RETURN TYPES)
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void returnTypeRule() {
        System.out.println("=== RULE 4: RETURN TYPE RULE ===");
        System.out.println("*** Return type must be same or subtype (covariant) ***\n");

        System.out.println("Rule: Child method return type must be:");
        System.out.println("  • Same as parent return type, OR");
        System.out.println("  • Subtype (subclass) of parent return type\n");

        System.out.println("--- Example 1: Same Return Type ✓ ---");
        System.out.println("class Parent {");
        System.out.println("    String method() { return \"Parent\"; }");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    String method() { return \"Child\"; }  // ✓ Same type");
        System.out.println("}\n");

        System.out.println("--- Example 2: Covariant Return Type ✓ ---");
        System.out.println("class Parent {");
        System.out.println("    CharSequence method() { return \"Parent\"; }");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    String method() { return \"Child\"; }");
        System.out.println("    // ✓ String is subtype of CharSequence");
        System.out.println("}\n");

        System.out.println("--- Example 3: Invalid Return Type ✗ ---");
        System.out.println("class Parent {");
        System.out.println("    String method() { return \"Parent\"; }");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    CharSequence method() { return \"Child\"; }");
        System.out.println("    // ✗ DOES NOT COMPILE - CharSequence is supertype!");
        System.out.println("}");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * COVARIANT RETURN TYPES - DETAILED EXAMPLES
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void covariantReturnTypes() {
        System.out.println("=== COVARIANT RETURN TYPES ===");
        System.out.println("*** TRICKY EXAM TOPIC ***\n");

        System.out.println("Covariant means child return type is subtype of parent return type\n");

        System.out.println("--- Example 1: Object → String ✓ ---");
        System.out.println("class Parent {");
        System.out.println("    Object getValue() { return new Object(); }");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    String getValue() { return \"Hello\"; }");
        System.out.println("    // ✓ String is subclass of Object");
        System.out.println("}\n");

        System.out.println("--- Example 2: Number → Integer ✓ ---");
        System.out.println("class Parent {");
        System.out.println("    Number getValue() { return 10; }");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    Integer getValue() { return 20; }");
        System.out.println("    // ✓ Integer is subclass of Number");
        System.out.println("}\n");

        System.out.println("--- Example 3: CharSequence → String ✓ ---");
        System.out.println("class Parent {");
        System.out.println("    CharSequence getValue() { return \"Parent\"; }");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    String getValue() { return \"Child\"; }");
        System.out.println("    // ✓ String implements CharSequence");
        System.out.println("}\n");

        System.out.println("--- Working Example ---");
        CovariantChild child = new CovariantChild();
        System.out.println("Child returns String: " + child.getValue());

        CovariantParent parent = new CovariantChild();
        System.out.println("Parent reference, child object: " + parent.getValue());

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * COVARIANCE TEST
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void covariantTest() {
        System.out.println("=== COVARIANCE TEST ===");
        System.out.println("*** USE THIS TO DETERMINE IF TYPES ARE COVARIANT ***\n");

        System.out.println("COVARIANCE TEST:");
        System.out.println("Given inherited return type A and overriding return type B,");
        System.out.println("can you assign instance of B to reference variable for A");
        System.out.println("WITHOUT a cast?\n");

        System.out.println("Test: B b = new B();");
        System.out.println("      A a = b;  // No cast needed?\n");

        System.out.println("If YES → Covariant ✓ (override is valid)");
        System.out.println("If NO  → Not covariant ✗ (override is invalid)\n");

        System.out.println("--- Test 1: String → CharSequence ---");
        System.out.println("String s = \"test\";");
        System.out.println("CharSequence cs = s;  // ✓ No cast needed");
        System.out.println("→ Covariant! Child can return String if parent returns CharSequence\n");

        System.out.println("--- Test 2: Integer → Number ---");
        System.out.println("Integer i = 10;");
        System.out.println("Number n = i;  // ✓ No cast needed");
        System.out.println("→ Covariant! Child can return Integer if parent returns Number\n");

        System.out.println("--- Test 3: CharSequence → String (BACKWARDS!) ---");
        System.out.println("CharSequence cs = \"test\";");
        System.out.println("String s = cs;  // ✗ DOES NOT COMPILE - cast needed!");
        System.out.println("→ NOT Covariant! Child CANNOT return CharSequence if parent returns String\n");

        System.out.println("--- Test 4: Number → Integer (BACKWARDS!) ---");
        System.out.println("Number n = 10;");
        System.out.println("Integer i = n;  // ✗ DOES NOT COMPILE - cast needed!");
        System.out.println("→ NOT Covariant! Child CANNOT return Number if parent returns Integer\n");

        System.out.println("*** EXAM TIP ***");
        System.out.println("The covariance test is your best friend!");
        System.out.println("If unsure, mentally test the assignment.");
        System.out.println("Child return type → Parent return type (no cast?) = Valid override");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * REDECLARING PRIVATE METHODS
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void redeclaringPrivateMethods() {
        System.out.println("=== REDECLARING PRIVATE METHODS ===");
        System.out.println("*** Private methods are NOT inherited ***\n");

        System.out.println("Key concept: You CANNOT override private methods");
        System.out.println("because they are NOT inherited!\n");

        System.out.println("If child declares method with same signature,");
        System.out.println("it's a NEW method, not an override.\n");

        System.out.println("--- Example ---");
        System.out.println("class Parent {");
        System.out.println("    private void method() {");
        System.out.println("        System.out.println(\"Parent\");");
        System.out.println("    }");
        System.out.println("}");
        System.out.println();
        System.out.println("class Child extends Parent {");
        System.out.println("    private void method() {  // Not an override!");
        System.out.println("        System.out.println(\"Child\");");
        System.out.println("    }");
        System.out.println("    // This is a NEW method, unrelated to parent");
        System.out.println("}\n");

        System.out.println("--- Demonstration ---");
        PrivateMethodChild child = new PrivateMethodChild();
        child.callMethod();  // Calls parent's private method

        System.out.println("\n--- Why This Matters ---");
        System.out.println("Parent reference → calls parent's private method");
        System.out.println("Child has its own method, but it's not an override");
        System.out.println("No polymorphism with private methods!");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * HIDING STATIC METHODS
     * ═══════════════════════════════════════════════════════════════════════
     */
    @SuppressWarnings("static")
    private static void hidingStaticMethods() {
        System.out.println("=== HIDING STATIC METHODS ===");
        System.out.println("*** Static methods are HIDDEN, not overridden ***\n");

        System.out.println("Key difference from overriding:");
        System.out.println("  • Instance methods: Version depends on OBJECT type");
        System.out.println("  • Static methods: Version depends on REFERENCE type\n");

        System.out.println("Rules for hiding:");
        System.out.println("  1. Both must be static (or compilation error)");
        System.out.println("  2. Must have same signature");
        System.out.println("  3. Must be at least as accessible");
        System.out.println("  4. Must follow exception rules");
        System.out.println("  5. Must follow return type rules\n");

        System.out.println("--- Valid Hiding ✓ ---");
        System.out.println("class Parent {");
        System.out.println("    static void method() { System.out.println(\"Parent\"); }");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    static void method() { System.out.println(\"Child\"); }");
        System.out.println("    // ✓ Valid hiding");
        System.out.println("}\n");

        System.out.println("--- Invalid: static → instance ✗ ---");
        System.out.println("class Parent {");
        System.out.println("    static void method() { }");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    void method() { }  // ✗ DOES NOT COMPILE");
        System.out.println("    // Cannot override static with instance");
        System.out.println("}\n");

        System.out.println("--- Invalid: instance → static ✗ ---");
        System.out.println("class Parent {");
        System.out.println("    void method() { }");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    static void method() { }  // ✗ DOES NOT COMPILE");
        System.out.println("    // Cannot hide instance with static");
        System.out.println("}\n");

        System.out.println("--- Demonstration: Reference Type Matters ---");
        StaticHidingParent.method();
        StaticHidingChild.method();

        System.out.println("\nReference type determines which version:");
        StaticHidingParent ref1 = new StaticHidingParent();
        StaticHidingChild ref2 = new StaticHidingChild();
        StaticHidingParent ref3 = new StaticHidingChild();

        ref1.method();  // Parent (reference is Parent)
        ref2.method();  // Child (reference is Child)
        ref3.method();  // Parent! (reference is Parent, even though object is Child)

        System.out.println("\n*** CRITICAL DIFFERENCE ***");
        System.out.println("Static methods: Reference type determines version");
        System.out.println("Instance methods: Object type determines version (polymorphism)");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * HIDDEN VARIABLES
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void hiddenVariables() {
        System.out.println("=== HIDDEN VARIABLES ===");
        System.out.println("*** Variables are HIDDEN, not overridden ***\n");

        System.out.println("Key concept: Each class has its own copy of the variable");
        System.out.println("Version accessed depends on REFERENCE TYPE, not object type\n");

        System.out.println("--- Example ---");
        System.out.println("class Parent {");
        System.out.println("    String name = \"Parent\";");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    String name = \"Child\";  // Hides parent's name");
        System.out.println("}\n");

        System.out.println("--- Demonstration ---");
        HiddenVariableParent parent = new HiddenVariableParent();
        HiddenVariableChild child = new HiddenVariableChild();
        HiddenVariableParent poly = new HiddenVariableChild();

        System.out.println("parent.name = " + parent.name);  // Parent
        System.out.println("child.name = " + child.name);    // Child
        System.out.println("poly.name = " + poly.name);      // Parent! (reference type)

        System.out.println("\n--- Compare with Method Overriding ---");
        System.out.println("parent.display() → " + parent.display());
        System.out.println("child.display() → " + child.display());
        System.out.println("poly.display() → " + poly.display());  // Child! (object type)

        System.out.println("\n*** KEY INSIGHT ***");
        System.out.println("Variables: Reference type determines version (hiding)");
        System.out.println("Methods: Object type determines version (overriding)");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * FINAL METHODS
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void finalMethods() {
        System.out.println("=== FINAL METHODS ===");
        System.out.println("*** final methods CANNOT be overridden ***\n");

        System.out.println("Rule: If parent method is final, child cannot override it\n");

        System.out.println("--- Example ✗ ---");
        System.out.println("class Parent {");
        System.out.println("    final void method() { }");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    void method() { }  // ✗ DOES NOT COMPILE");
        System.out.println("    // Cannot override final method");
        System.out.println("}\n");

        System.out.println("--- Why Use final? ---");
        System.out.println("  • Security - prevent behavior changes");
        System.out.println("  • Performance - enable compiler optimizations");
        System.out.println("  • Design - signal method should not be changed\n");

        System.out.println("--- Working Example ---");
        OverrideFinalMethodChild child = new OverrideFinalMethodChild();
        child.finalMethod();      // Inherited, cannot override
        child.overridableMethod(); // Overridden version

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * COMMON EXAM TRAPS
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void examTraps() {
        System.out.println("=== COMMON EXAM TRAPS ===\n");

        System.out.println("--- TRAP 1: Reducing Access Modifier ✗ ---");
        System.out.println("class Parent { public void method() { } }");
        System.out.println("class Child extends Parent {");
        System.out.println("    protected void method() { }  // ✗ DOES NOT COMPILE");
        System.out.println("}\n");

        System.out.println("--- TRAP 2: Adding Checked Exception ✗ ---");
        System.out.println("class Parent { void method() { } }");
        System.out.println("class Child extends Parent {");
        System.out.println("    void method() throws IOException { }  // ✗ DOES NOT COMPILE");
        System.out.println("}\n");

        System.out.println("--- TRAP 3: Incompatible Return Type ✗ ---");
        System.out.println("class Parent { String method() { return \"\"; } }");
        System.out.println("class Child extends Parent {");
        System.out.println("    CharSequence method() { return \"\"; }  // ✗ DOES NOT COMPILE");
        System.out.println("    // CharSequence is supertype of String!");
        System.out.println("}\n");

        System.out.println("--- TRAP 4: static vs instance Mismatch ✗ ---");
        System.out.println("class Parent { static void method() { } }");
        System.out.println("class Child extends Parent {");
        System.out.println("    void method() { }  // ✗ DOES NOT COMPILE");
        System.out.println("}\n");

        System.out.println("--- TRAP 5: Overriding final Method ✗ ---");
        System.out.println("class Parent { final void method() { } }");
        System.out.println("class Child extends Parent {");
        System.out.println("    void method() { }  // ✗ DOES NOT COMPILE");
        System.out.println("}\n");

        System.out.println("--- TRAP 6: Thinking You Can Override private ✗ ---");
        System.out.println("class Parent { private void method() { } }");
        System.out.println("class Child extends Parent {");
        System.out.println("    public void method() { }  // Not an override!");
        System.out.println("    // private methods are not inherited");
        System.out.println("}\n");

        System.out.println("*** EXAM CHECKLIST ***");
        System.out.println("When evaluating override validity, check:");
        System.out.println("1. ✓ Same signature?");
        System.out.println("2. ✓ Access modifier at least as accessible?");
        System.out.println("3. ✓ Exceptions narrower or same?");
        System.out.println("4. ✓ Return type covariant? (use assignment test!)");
        System.out.println("5. ✓ Not final?");
        System.out.println("6. ✓ Not static mismatch?");
        System.out.println("7. ✓ Not private?");

        System.out.println();
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// EXAMPLE CLASSES
// ═══════════════════════════════════════════════════════════════════════════

class BasicParent {
    void display() {
        System.out.println("Parent display()");
    }
}

class BasicChild extends BasicParent {
    @Override
    void display() {
        System.out.println("Child display() - overridden!");
    }

    void callParent() {
        System.out.print("Calling parent with super: ");
        super.display();
    }
}

// ───────────────────────────────────────────────────────────────────────────
// EXCEPTION EXAMPLES
// ───────────────────────────────────────────────────────────────────────────

class ExceptionParent {
    void fewerExceptions() throws IOException {
        System.out.println("Parent throws IOException");
    }

    void narrowerException() throws IOException {
        System.out.println("Parent throws IOException");
    }

    void uncheckedOK() {
        System.out.println("Parent throws nothing");
    }
}

class ExceptionChild extends ExceptionParent {
    @Override
    void fewerExceptions() {
        // ✓ No exception - fewer than parent
        System.out.println("Child throws nothing");
    }

    @Override
    void narrowerException() throws FileNotFoundException {
        // ✓ FileNotFoundException is narrower than IOException
        System.out.println("Child throws FileNotFoundException");
    }

    @Override
    void uncheckedOK() throws NullPointerException {
        // ✓ Unchecked exceptions can be added anytime
        System.out.println("Child throws NullPointerException");
    }
}

// ───────────────────────────────────────────────────────────────────────────
// COVARIANT RETURN TYPE EXAMPLES
// ───────────────────────────────────────────────────────────────────────────

class CovariantParent {
    CharSequence getValue() {
        return "Parent";
    }
}

class CovariantChild extends CovariantParent {
    @Override
    String getValue() {  // ✓ String is subtype of CharSequence
        return "Child";
    }
}

// ───────────────────────────────────────────────────────────────────────────
// PRIVATE METHOD EXAMPLE
// ───────────────────────────────────────────────────────────────────────────

class PrivateMethodParent {
    private void method() {
        System.out.println("Parent's private method");
    }

    public void callMethod() {
        System.out.print("Parent calling: ");
        method();
    }
}

class PrivateMethodChild extends PrivateMethodParent {
    // This is NOT an override! It's a new method.
    public void method() {
        System.out.println("Child's method (not an override!)");
    }
}

// ───────────────────────────────────────────────────────────────────────────
// STATIC METHOD HIDING EXAMPLE
// ───────────────────────────────────────────────────────────────────────────

class StaticHidingParent {
    static void method() {
        System.out.println("Parent static method");
    }
}

class StaticHidingChild extends StaticHidingParent {
    static void method() {  // Hides parent's static method
        System.out.println("Child static method");
    }
}

// ───────────────────────────────────────────────────────────────────────────
// HIDDEN VARIABLE EXAMPLE
// ───────────────────────────────────────────────────────────────────────────

class HiddenVariableParent {
    String name = "Parent";

    String display() {
        return "Parent method: " + name;
    }
}

class HiddenVariableChild extends HiddenVariableParent {
    String name = "Child";  // Hides parent's name

    @Override
    String display() {
        return "Child method: " + name;
    }
}

// ───────────────────────────────────────────────────────────────────────────
// FINAL METHOD EXAMPLE
// ───────────────────────────────────────────────────────────────────────────

class OverrideFinalMethodParent {
    final void finalMethod() {
        System.out.println("Final method - cannot be overridden");
    }

    void overridableMethod() {
        System.out.println("Parent's overridable method");
    }
}

class OverrideFinalMethodChild extends OverrideFinalMethodParent {
    // Cannot override finalMethod() - it's final!

    @Override
    void overridableMethod() {
        System.out.println("Child's overridden method");
    }
}
