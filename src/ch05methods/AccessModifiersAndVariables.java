package ch05methods;

/**
 * ACCESS MODIFIERS AND VARIABLE SPECIFIERS
 * Complete Guide for OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * ACCESS MODIFIERS (for classes, methods, constructors, fields)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * From MOST restrictive to LEAST restrictive:
 *
 * MODIFIER          SAME CLASS    SAME PACKAGE    SUBCLASS    EVERYWHERE
 * ────────────────────────────────────────────────────────────────────────────
 * private              ✓              ✗              ✗            ✗
 * (package-private)    ✓              ✓              ✗            ✗
 * protected            ✓              ✓              ✓            ✗
 * public               ✓              ✓              ✓            ✓
 * ────────────────────────────────────────────────────────────────────────────
 *
 * CRITICAL EXAM KNOWLEDGE:
 *
 * package-private (default):
 * - No keyword! Just omit the access modifier
 * - Accessible within same package only
 * - NOT accessible to subclasses in different packages!
 *
 * protected:
 * - Gives ALL package access PLUS subclass access
 * - Accessible in same package (even by non-subclasses!)
 * - Accessible to subclasses in different packages
 * - EXAM TRAP: protected > package-private (more permissive!)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * VARIABLE SPECIFIERS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * LOCAL VARIABLES (inside methods):
 * - Can ONLY use: final
 * - Cannot use: static, volatile, transient
 * - Can be effectively final (never reassigned after initialization)
 *
 * INSTANCE VARIABLES (fields in a class):
 * - Can use: final, volatile, transient
 * - Can combine with access modifiers
 * - final instance variables MUST be initialized:
 *   • Inline (where declared)
 *   • In constructor
 *   • In instance initializer block
 *
 * STATIC VARIABLES (class variables):
 * - Can use: final, volatile, transient
 * - final static variables MUST be initialized:
 *   • Inline (where declared)
 *   • In static initializer block
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * EFFECTIVELY FINAL
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * A variable is "effectively final" if:
 * - It is not declared with final keyword
 * - It is never reassigned after initialization
 *
 * WHY IT MATTERS:
 * - Lambda expressions can only access effectively final variables
 * - Anonymous inner classes can only access effectively final variables
 *
 * HOW TO TEST:
 * - Add the "final" keyword to the variable
 * - If it still compiles → it was effectively final
 * - If it doesn't compile → it was NOT effectively final
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * EXAM TRAPS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * 1. protected is MORE permissive than package-private!
 * 2. Package-private classes can access protected members in same package
 * 3. final instance variables must be assigned by end of constructor
 * 4. Local variables can ONLY have final (no other modifiers)
 * 5. Effectively final doesn't need the final keyword
 * 6. Reassigning in any code path makes variable NOT effectively final
 */
public class AccessModifiersAndVariables {

    // Instance variable examples (will be used in demonstrations)
    private int privateField = 1;
    int packagePrivateField = 2;           // No modifier = package-private
    protected int protectedField = 3;
    public int publicField = 4;

    // Final instance variables - must be initialized
    private final int finalInline = 10;    // Initialized inline

    private final int finalInConstructor;  // Initialized in constructor

    private final int finalInInitializer;  // Initialized in instance initializer

    // Instance initializer block
    {
        finalInInitializer = 30;
    }

    // Constructor
    public AccessModifiersAndVariables() {
        finalInConstructor = 20;
        // All final instance variables MUST be assigned by here!
    }

    public static void main(String[] args) {
        System.out.println("=== ACCESS MODIFIERS AND VARIABLE SPECIFIERS ===\n");

        accessModifierBasics();
        packagePrivateVsProtected();
        protectedSubclassReferenceTrap();
        staticVsInstanceContext();
        autoboxingAndCasting();
        localVariableSpecifiers();
        instanceVariableSpecifiers();
        finalInstanceVariables();
        effectivelyFinalBasics();
        effectivelyFinalTesting();
        effectivelyFinalWithLambdas();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * ACCESS MODIFIER BASICS
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void accessModifierBasics() {
        System.out.println("=== ACCESS MODIFIER BASICS ===\n");

        System.out.println("Access Levels (from most to least restrictive):");
        System.out.println("  1. private         - same class only");
        System.out.println("  2. package-private - same package only (NO keyword!)");
        System.out.println("  3. protected       - same package + subclasses");
        System.out.println("  4. public          - everywhere");

        System.out.println("\n--- Same Class Access ---");
        AccessModifiersAndVariables obj = new AccessModifiersAndVariables();
        System.out.println("In same class, can access ALL modifiers:");
        System.out.println("  private:         " + obj.privateField);
        System.out.println("  package-private: " + obj.packagePrivateField);
        System.out.println("  protected:       " + obj.protectedField);
        System.out.println("  public:          " + obj.publicField);

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * PACKAGE-PRIVATE VS PROTECTED - CRITICAL EXAM CONCEPT
     * ═══════════════════════════════════════════════════════════════════════
     *
     * *** MOST IMPORTANT ***
     *
     * Many students think protected is more restrictive than package-private.
     * THIS IS WRONG!
     *
     * TRUTH:
     * - protected = package access + subclass access
     * - protected is MORE permissive than package-private
     *
     * EXAMPLE SCENARIO:
     *
     * package animals;
     * public class Animal {
     *     protected void eat() { }      // protected
     *     void sleep() { }              // package-private
     * }
     *
     * package animals;
     * public class Zoo {  // NOT a subclass, but SAME package
     *     void test() {
     *         Animal a = new Animal();
     *         a.eat();    // ✓ Works! protected gives package access
     *         a.sleep();  // ✓ Works! package-private gives package access
     *     }
     * }
     *
     * package zoo;  // DIFFERENT package
     * import animals.Animal;
     * public class Zookeeper extends Animal {  // IS a subclass, DIFFERENT package
     *     void test() {
     *         eat();      // ✓ Works! protected gives subclass access
     *         sleep();    // ✗ ERROR! package-private does NOT give subclass access
     *     }
     * }
     *
     * KEY INSIGHT:
     * - protected members accessible to ANY class in same package
     * - package-private members accessible to ANY class in same package
     * - But protected ALSO accessible to subclasses in different packages
     * - package-private NOT accessible to subclasses in different packages
     */
    private static void packagePrivateVsProtected() {
        System.out.println("=== PACKAGE-PRIVATE VS PROTECTED ===");
        System.out.println("*** CRITICAL EXAM CONCEPT ***\n");

        System.out.println("Common Misconception:");
        System.out.println("  ✗ \"protected is more restrictive than package-private\"");
        System.out.println("\nTRUTH:");
        System.out.println("  ✓ protected = package access + subclass access");
        System.out.println("  ✓ protected is MORE PERMISSIVE than package-private\n");

        System.out.println("SCENARIO 1: Same Package, Non-Subclass");
        System.out.println("────────────────────────────────────────────────");
        System.out.println("class Animal {");
        System.out.println("    protected void eat() { }");
        System.out.println("    void sleep() { }  // package-private");
        System.out.println("}");
        System.out.println("\nclass Zoo {  // NOT a subclass, SAME package");
        System.out.println("    void test() {");
        System.out.println("        Animal a = new Animal();");
        System.out.println("        a.eat();    // ✓ Works! protected");
        System.out.println("        a.sleep();  // ✓ Works! package-private");
        System.out.println("    }");
        System.out.println("}");

        System.out.println("\nSCENARIO 2: Different Package, Subclass");
        System.out.println("────────────────────────────────────────────────");
        System.out.println("package zoo;  // DIFFERENT package");
        System.out.println("import animals.Animal;");
        System.out.println("\nclass Zookeeper extends Animal {");
        System.out.println("    void test() {");
        System.out.println("        eat();      // ✓ Works! protected + subclass");
        System.out.println("        sleep();    // ✗ ERROR! package-private");
        System.out.println("    }");
        System.out.println("}");

        System.out.println("\n*** KEY INSIGHT ***");
        System.out.println("• protected > package-private (more access)");
        System.out.println("• protected = package access + subclass access");
        System.out.println("• Classes in same package can access protected (even non-subclasses!)");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * PROTECTED SUBCLASS REFERENCE TRAP - VERY TRICKY EXAM TOPIC!
     * ═══════════════════════════════════════════════════════════════════════
     *
     * *** CRITICAL EXAM TRAP ***
     *
     * When a subclass is in a DIFFERENT package, protected access works, but
     * there's a subtle restriction many students miss!
     *
     * WHAT WORKS (Subclass in different package):
     * 1. Direct access to inherited protected members ✓
     * 2. Access through instance of CHILD class ✓
     * 3. Access through instance of child's SUBCLASSES ✓
     *
     * WHAT DOESN'T WORK:
     * 4. Access through instance of PARENT class ✗
     *
     * WHY?
     * Protected access for subclasses in different packages is limited to
     * the inheritance hierarchy. You can only access protected members through
     * YOUR class type (or subclasses), not through arbitrary parent instances.
     *
     * EXAMPLE:
     *
     * package animals;
     * public class Animal {
     *     protected void eat() { }
     *     protected int age = 5;
     * }
     *
     * package zoo;  // DIFFERENT package
     * import animals.Animal;
     * public class Lion extends Animal {
     *     void test() {
     *         // Scenario 1: Direct access (inherited) ✓
     *         eat();              // ✓ Works! Inherited protected method
     *         System.out.println(age);  // ✓ Works! Inherited protected field
     *
     *         // Scenario 2: Through instance of CHILD class ✓
     *         Lion lion = new Lion();
     *         lion.eat();         // ✓ Works! Lion is the child
     *         System.out.println(lion.age);  // ✓ Works!
     *
     *         // Scenario 3: Through instance of PARENT class ✗
     *         Animal animal = new Animal();
     *         animal.eat();       // ✗ DOES NOT COMPILE!
     *         System.out.println(animal.age);  // ✗ DOES NOT COMPILE!
     *
     *         // Scenario 4: Parent reference to child instance ✗
     *         Animal animalRef = new Lion();
     *         animalRef.eat();    // ✗ DOES NOT COMPILE!
     *         System.out.println(animalRef.age);  // ✗ DOES NOT COMPILE!
     *     }
     * }
     *
     * EXPLANATION:
     * The compiler checks the REFERENCE TYPE, not the object type!
     * - Lion reference → can access protected (it's your class)
     * - Animal reference → cannot access protected (it's parent class)
     *
     * This prevents you from accessing protected members of unrelated instances
     * of the parent class in different packages.
     */
    private static void protectedSubclassReferenceTrap() {
        System.out.println("=== PROTECTED SUBCLASS REFERENCE TRAP ===");
        System.out.println("*** VERY TRICKY EXAM TOPIC! ***\n");

        System.out.println("SCENARIO: Subclass in DIFFERENT package accessing protected members\n");

        System.out.println("Parent class (package: animals):");
        System.out.println("────────────────────────────────────────────────");
        System.out.println("package animals;");
        System.out.println("public class Animal {");
        System.out.println("    protected void eat() { }");
        System.out.println("    protected int age = 5;");
        System.out.println("}");

        System.out.println("\nChild class (package: zoo) - DIFFERENT package:");
        System.out.println("────────────────────────────────────────────────");
        System.out.println("package zoo;");
        System.out.println("import animals.Animal;");
        System.out.println("\npublic class Lion extends Animal {");
        System.out.println("    void test() {");

        System.out.println("\n        // Scenario 1: Direct access (inherited) ✓");
        System.out.println("        eat();                    // ✓ COMPILES");
        System.out.println("        System.out.println(age);  // ✓ COMPILES");
        System.out.println("        → Works! Inherited protected members");

        System.out.println("\n        // Scenario 2: Through CHILD instance ✓");
        System.out.println("        Lion lion = new Lion();");
        System.out.println("        lion.eat();               // ✓ COMPILES");
        System.out.println("        System.out.println(lion.age);  // ✓ COMPILES");
        System.out.println("        → Works! Using child class reference");

        System.out.println("\n        // Scenario 3: Through PARENT instance ✗");
        System.out.println("        Animal animal = new Animal();");
        System.out.println("        animal.eat();             // ✗ DOES NOT COMPILE!");
        System.out.println("        System.out.println(animal.age);  // ✗ DOES NOT COMPILE!");
        System.out.println("        → Fails! Cannot use parent class reference");

        System.out.println("\n        // Scenario 4: PARENT reference to child instance ✗");
        System.out.println("        Animal animalRef = new Lion();");
        System.out.println("        animalRef.eat();          // ✗ DOES NOT COMPILE!");
        System.out.println("        System.out.println(animalRef.age);  // ✗ DOES NOT COMPILE!");
        System.out.println("        → Fails! Reference type is Animal (parent)");

        System.out.println("    }");
        System.out.println("}");

        System.out.println("\n*** KEY RULES ***");
        System.out.println("1. Compiler checks REFERENCE TYPE, not object type");
        System.out.println("2. Child reference → Can access protected ✓");
        System.out.println("3. Parent reference → Cannot access protected ✗");
        System.out.println("4. Even if object is actually a child instance!");

        System.out.println("\n*** WHY THIS RESTRICTION? ***");
        System.out.println("Prevents subclass from accessing protected members of");
        System.out.println("unrelated parent instances in different packages.");
        System.out.println("\nExample:");
        System.out.println("  Lion shouldn't access protected members of");
        System.out.println("  an arbitrary Animal instance that might be a");
        System.out.println("  different subclass (Tiger, Bear, etc.)");

        System.out.println("\n*** EXAM TIP ***");
        System.out.println("Look for code like this:");
        System.out.println("  class Child extends Parent {");
        System.out.println("      void method() {");
        System.out.println("          Parent p = new Parent();");
        System.out.println("          p.protectedMember();  // ✗ Does this compile?");
        System.out.println("      }");
        System.out.println("  }");
        System.out.println("\nAnswer: NO! (if Child is in different package)");

        System.out.println();
    }

    // Demo fields for static vs instance context
    private int instanceVariable = 100;
    private static int staticVariable = 200;

    private void instanceMethod() {
        System.out.println("Instance method called");
    }

    private static void staticMethod() {
        System.out.println("Static method called");
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * STATIC VS INSTANCE CONTEXT - VERY COMMON EXAM TRAP!
     * ═══════════════════════════════════════════════════════════════════════
     *
     * *** CRITICAL EXAM CONCEPT ***
     *
     * The exam LOVES to test this concept!
     *
     * KEY RULES:
     *
     * STATIC METHODS:
     * - Cannot directly access instance variables ✗
     * - Cannot directly call instance methods ✗
     * - Cannot use "this" keyword ✗
     * - CAN access static variables ✓
     * - CAN call static methods ✓
     * - MUST create an instance to access instance members ✓
     *
     * INSTANCE METHODS:
     * - CAN access instance variables ✓
     * - CAN call instance methods ✓
     * - CAN use "this" keyword ✓
     * - CAN also access static variables ✓
     * - CAN also call static methods ✓
     *
     * WHY?
     * Static methods belong to the CLASS, not to any instance.
     * There is no "this" instance in a static context.
     * To access instance members, you need an actual instance!
     *
     * EXAM PATTERN:
     * Look for static methods trying to access instance members directly.
     * This is a compilation error!
     */
    private static void staticVsInstanceContext() {
        System.out.println("=== STATIC VS INSTANCE CONTEXT ===");
        System.out.println("*** VERY COMMON EXAM TRAP! ***\n");

        System.out.println("KEY RULE: Static methods need an instance to access instance members!\n");

        // ═══════════════════════════════════════════════════════════════════
        System.out.println("=== WHAT STATIC METHODS CAN ACCESS ===\n");

        // ✓ Can access static variables
        System.out.println("--- Static Variable Access ✓ ---");
        System.out.println("In static method:");
        System.out.println("  System.out.println(staticVariable);");
        System.out.println("  → " + staticVariable + " (Works!)");

        // ✓ Can call static methods
        System.out.println("\n--- Static Method Call ✓ ---");
        System.out.println("In static method:");
        System.out.println("  staticMethod();");
        System.out.print("  → ");
        staticMethod();

        // ✗ CANNOT access instance variables directly
        System.out.println("\n--- Instance Variable Access ✗ ---");
        System.out.println("In static method:");
        System.out.println("  System.out.println(instanceVariable);");
        System.out.println("  → ✗ DOES NOT COMPILE!");
        System.out.println("  Error: non-static variable cannot be referenced from a static context");

        // ✗ CANNOT call instance methods directly
        System.out.println("\n--- Instance Method Call ✗ ---");
        System.out.println("In static method:");
        System.out.println("  instanceMethod();");
        System.out.println("  → ✗ DOES NOT COMPILE!");
        System.out.println("  Error: non-static method cannot be referenced from a static context");

        // ✗ CANNOT use "this"
        System.out.println("\n--- Using 'this' Keyword ✗ ---");
        System.out.println("In static method:");
        System.out.println("  this.instanceVariable = 10;");
        System.out.println("  → ✗ DOES NOT COMPILE!");
        System.out.println("  Error: non-static variable this cannot be referenced from a static context");

        // ═══════════════════════════════════════════════════════════════════
        System.out.println("\n=== HOW STATIC METHODS ACCESS INSTANCE MEMBERS ===\n");
        System.out.println("SOLUTION: Create an instance of the class!\n");

        // ✓ Create instance, then access instance variable
        System.out.println("--- Access Instance Variable ✓ ---");
        AccessModifiersAndVariables obj = new AccessModifiersAndVariables();
        System.out.println("In static method:");
        System.out.println("  AccessModifiersAndVariables obj = new AccessModifiersAndVariables();");
        System.out.println("  System.out.println(obj.instanceVariable);");
        System.out.println("  → " + obj.instanceVariable + " (Works!)");

        // ✓ Create instance, then call instance method
        System.out.println("\n--- Call Instance Method ✓ ---");
        System.out.println("In static method:");
        System.out.println("  AccessModifiersAndVariables obj = new AccessModifiersAndVariables();");
        System.out.println("  obj.instanceMethod();");
        System.out.print("  → ");
        obj.instanceMethod();

        // ═══════════════════════════════════════════════════════════════════
        System.out.println("\n=== INSTANCE METHODS CAN ACCESS EVERYTHING ===\n");
        System.out.println("Instance methods have access to:");
        System.out.println("  ✓ Instance variables (directly)");
        System.out.println("  ✓ Instance methods (directly)");
        System.out.println("  ✓ Static variables (directly)");
        System.out.println("  ✓ Static methods (directly)");
        System.out.println("  ✓ 'this' keyword");

        // ═══════════════════════════════════════════════════════════════════
        System.out.println("\n=== EXAM EXAMPLES ===\n");

        System.out.println("--- Example 1: DOES NOT COMPILE ✗ ---");
        System.out.println("class Example {");
        System.out.println("    private int count = 0;");
        System.out.println();
        System.out.println("    public static void main(String[] args) {");
        System.out.println("        count++;  // ✗ ERROR! static method accessing instance variable");
        System.out.println("    }");
        System.out.println("}");

        System.out.println("\n--- Example 2: COMPILES ✓ ---");
        System.out.println("class Example {");
        System.out.println("    private int count = 0;");
        System.out.println();
        System.out.println("    public static void main(String[] args) {");
        System.out.println("        Example e = new Example();");
        System.out.println("        e.count++;  // ✓ Works! Using instance to access");
        System.out.println("    }");
        System.out.println("}");

        System.out.println("\n--- Example 3: DOES NOT COMPILE ✗ ---");
        System.out.println("class Example {");
        System.out.println("    private void print() {");
        System.out.println("        System.out.println(\"Hello\");");
        System.out.println("    }");
        System.out.println();
        System.out.println("    public static void main(String[] args) {");
        System.out.println("        print();  // ✗ ERROR! static method calling instance method");
        System.out.println("    }");
        System.out.println("}");

        System.out.println("\n--- Example 4: COMPILES ✓ ---");
        System.out.println("class Example {");
        System.out.println("    private void print() {");
        System.out.println("        System.out.println(\"Hello\");");
        System.out.println("    }");
        System.out.println();
        System.out.println("    public static void main(String[] args) {");
        System.out.println("        Example e = new Example();");
        System.out.println("        e.print();  // ✓ Works! Using instance to call method");
        System.out.println("    }");
        System.out.println("}");

        System.out.println("\n--- Example 5: Tricky - DOES NOT COMPILE ✗ ---");
        System.out.println("class Example {");
        System.out.println("    private int x = 10;");
        System.out.println();
        System.out.println("    public static void test() {");
        System.out.println("        System.out.println(this.x);  // ✗ ERROR! 'this' in static method");
        System.out.println("    }");
        System.out.println("}");

        // ═══════════════════════════════════════════════════════════════════
        System.out.println("\n*** MEMORY AID ***");
        System.out.println("Think: 'STATIC = CLASS, INSTANCE = OBJECT'");
        System.out.println();
        System.out.println("Static context:");
        System.out.println("  • Belongs to the CLASS");
        System.out.println("  • No 'this' reference");
        System.out.println("  • Need an instance to access instance members");
        System.out.println();
        System.out.println("Instance context:");
        System.out.println("  • Belongs to an OBJECT");
        System.out.println("  • Has 'this' reference");
        System.out.println("  • Can access everything");

        System.out.println("\n*** EXAM TIP ***");
        System.out.println("Common exam pattern:");
        System.out.println("  1. Show a static method (often main)");
        System.out.println("  2. Try to access instance variable/method directly");
        System.out.println("  3. Ask: Does this compile?");
        System.out.println("  4. Answer: NO! Need an instance!");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * AUTOBOXING/UNBOXING AND CASTING - TRICKY EXAM TOPIC!
     * ═══════════════════════════════════════════════════════════════════════
     *
     * *** CRITICAL RULES ***
     *
     * Java will do ONE automatic conversion, not TWO!
     *
     * GENERAL RULE:
     * - Java will autobox OR cast (one step) ✓
     * - Java will NOT autobox AND cast (two steps) ✗
     *
     * EXCEPTION:
     * - Java WILL unbox and then cast (one special case) ✓
     * - Java will NOT cast and then autobox ✗
     *
     * ═══════════════════════════════════════════════════════════════════════
     * AUTOBOXING/UNBOXING BASICS
     * ═══════════════════════════════════════════════════════════════════════
     *
     * AUTOBOXING (primitive → wrapper):
     * - int → Integer
     * - long → Long
     * - double → Double
     * - etc.
     *
     * UNBOXING (wrapper → primitive):
     * - Integer → int
     * - Long → long
     * - Double → double
     * - etc.
     *
     * ═══════════════════════════════════════════════════════════════════════
     * WIDENING PRIMITIVE CONVERSIONS (implicit casting)
     * ═══════════════════════════════════════════════════════════════════════
     *
     * byte → short → int → long → float → double
     * char → int → long → float → double
     *
     * Java does this automatically (widening)
     */
    private static void autoboxingAndCasting() {
        System.out.println("=== AUTOBOXING/UNBOXING AND CASTING ===");
        System.out.println("*** TRICKY EXAM TOPIC! ***\n");

        System.out.println("GOLDEN RULE:");
        System.out.println("  Java will do ONE conversion automatically");
        System.out.println("  Java will NOT do TWO conversions automatically\n");

        // ═══════════════════════════════════════════════════════════════════
        System.out.println("=== SCENARIO 1: AUTOBOX ONLY (ONE STEP) ✓ ===\n");

        // int to Integer - autobox ✓
        System.out.println("--- int → Integer (autobox) ✓ ---");
        int primitiveInt = 5;
        Integer wrapperInt = primitiveInt;  // Autobox
        System.out.println("int primitiveInt = 5;");
        System.out.println("Integer wrapperInt = primitiveInt;");
        System.out.println("→ ✓ COMPILES (autobox)");
        System.out.println("Result: " + wrapperInt);

        // long to Long - autobox ✓
        System.out.println("\n--- long → Long (autobox) ✓ ---");
        long primitiveLong = 100L;
        Long wrapperLong = primitiveLong;  // Autobox
        System.out.println("long primitiveLong = 100L;");
        System.out.println("Long wrapperLong = primitiveLong;");
        System.out.println("→ ✓ COMPILES (autobox)");
        System.out.println("Result: " + wrapperLong);

        // ═══════════════════════════════════════════════════════════════════
        System.out.println("\n=== SCENARIO 2: UNBOX ONLY (ONE STEP) ✓ ===\n");

        // Integer to int - unbox ✓
        System.out.println("--- Integer → int (unbox) ✓ ---");
        Integer wrapperInt2 = 10;
        int primitiveInt2 = wrapperInt2;  // Unbox
        System.out.println("Integer wrapperInt2 = 10;");
        System.out.println("int primitiveInt2 = wrapperInt2;");
        System.out.println("→ ✓ COMPILES (unbox)");
        System.out.println("Result: " + primitiveInt2);

        // ═══════════════════════════════════════════════════════════════════
        System.out.println("\n=== SCENARIO 3: CAST ONLY (ONE STEP) ✓ ===\n");

        // int to long - widen ✓
        System.out.println("--- int → long (widen/cast) ✓ ---");
        int smallInt = 5;
        long bigLong = smallInt;  // Implicit cast (widening)
        System.out.println("int smallInt = 5;");
        System.out.println("long bigLong = smallInt;");
        System.out.println("→ ✓ COMPILES (implicit widening)");
        System.out.println("Result: " + bigLong);

        // int to double - widen ✓
        System.out.println("\n--- int → double (widen/cast) ✓ ---");
        int intVal = 42;
        double doubleVal = intVal;  // Implicit cast (widening)
        System.out.println("int intVal = 42;");
        System.out.println("double doubleVal = intVal;");
        System.out.println("→ ✓ COMPILES (implicit widening)");
        System.out.println("Result: " + doubleVal);

        // ═══════════════════════════════════════════════════════════════════
        System.out.println("\n=== SCENARIO 4: AUTOBOX THEN CAST (TWO STEPS) ✗ ===\n");

        // int to Long - would need autobox to Integer, then cast to Long ✗
        System.out.println("--- int → Long (autobox + cast) ✗ ---");
        int primitiveInt3 = 5;
        // Long wrapperLong2 = primitiveInt3;  // DOES NOT COMPILE!
        System.out.println("int primitiveInt3 = 5;");
        System.out.println("Long wrapperLong2 = primitiveInt3;");
        System.out.println("→ ✗ DOES NOT COMPILE!");
        System.out.println("Why: Would need to autobox (int→Integer) then cast (Integer→Long)");
        System.out.println("Solution: Long wrapperLong2 = (long) primitiveInt3; // cast then autobox");

        // int to Double - would need autobox to Integer, then cast to Double ✗
        System.out.println("\n--- int → Double (autobox + cast) ✗ ---");
        int primitiveInt4 = 10;
        // Double wrapperDouble = primitiveInt4;  // DOES NOT COMPILE!
        System.out.println("int primitiveInt4 = 10;");
        System.out.println("Double wrapperDouble = primitiveInt4;");
        System.out.println("→ ✗ DOES NOT COMPILE!");
        System.out.println("Why: Would need to autobox (int→Integer) then cast (Integer→Double)");
        System.out.println("Solution: Double wrapperDouble = (double) primitiveInt4; // cast then autobox");

        // ═══════════════════════════════════════════════════════════════════
        System.out.println("\n=== SCENARIO 5: UNBOX THEN CAST (SPECIAL CASE) ✓ ===\n");

        // Integer to long - unbox to int, then widen to long ✓
        System.out.println("--- Integer → long (unbox + widen) ✓ ---");
        Integer wrapperInt3 = 5;
        long primitiveLong2 = wrapperInt3;  // Unbox to int, then widen to long
        System.out.println("Integer wrapperInt3 = 5;");
        System.out.println("long primitiveLong2 = wrapperInt3;");
        System.out.println("→ ✓ COMPILES!");
        System.out.println("How: Unbox (Integer→int) then widen (int→long)");
        System.out.println("Result: " + primitiveLong2);

        // Integer to double - unbox to int, then widen to double ✓
        System.out.println("\n--- Integer → double (unbox + widen) ✓ ---");
        Integer wrapperInt4 = 42;
        double primitiveDouble = wrapperInt4;  // Unbox to int, then widen to double
        System.out.println("Integer wrapperInt4 = 42;");
        System.out.println("double primitiveDouble = wrapperInt4;");
        System.out.println("→ ✓ COMPILES!");
        System.out.println("How: Unbox (Integer→int) then widen (int→double)");
        System.out.println("Result: " + primitiveDouble);

        // Short to int - unbox to short, then widen to int ✓
        System.out.println("\n--- Short → int (unbox + widen) ✓ ---");
        Short wrapperShort = 100;
        int primitiveInt5 = wrapperShort;  // Unbox to short, then widen to int
        System.out.println("Short wrapperShort = 100;");
        System.out.println("int primitiveInt5 = wrapperShort;");
        System.out.println("→ ✓ COMPILES!");
        System.out.println("How: Unbox (Short→short) then widen (short→int)");
        System.out.println("Result: " + primitiveInt5);

        // ═══════════════════════════════════════════════════════════════════
        System.out.println("\n=== SCENARIO 6: CAST THEN AUTOBOX ✗ ===\n");

        // Integer to Long - would need cast then autobox ✗
        System.out.println("--- Integer → Long (cast + autobox) ✗ ---");
        Integer wrapperInt5 = 5;
        // Long wrapperLong3 = wrapperInt5;  // DOES NOT COMPILE!
        System.out.println("Integer wrapperInt5 = 5;");
        System.out.println("Long wrapperLong3 = wrapperInt5;");
        System.out.println("→ ✗ DOES NOT COMPILE!");
        System.out.println("Why: Integer and Long are unrelated types");
        System.out.println("Solution: Long wrapperLong3 = wrapperInt5.longValue(); // explicit conversion");

        // ═══════════════════════════════════════════════════════════════════
        System.out.println("\n=== SUMMARY TABLE ===\n");

        System.out.println("CONVERSION                      STEPS     COMPILES?");
        System.out.println("────────────────────────────────────────────────────");
        System.out.println("int → Integer                   1 (box)      ✓");
        System.out.println("Integer → int                   1 (unbox)    ✓");
        System.out.println("int → long                      1 (widen)    ✓");
        System.out.println("int → Long                      2 (widen+box) ✓ cast first!");
        System.out.println("int → Double                    2 (box+cast)  ✗");
        System.out.println("Integer → long                  2 (unbox+widen) ✓ SPECIAL!");
        System.out.println("Integer → Long                  2 (cast+box)  ✗");
        System.out.println("Short → int                     2 (unbox+widen) ✓ SPECIAL!");

        System.out.println("\n*** KEY TAKEAWAYS ***");
        System.out.println("1. ONE conversion is automatic ✓");
        System.out.println("2. TWO conversions usually fail ✗");
        System.out.println("3. EXCEPTION: Unbox + Widen works ✓");
        System.out.println("   (Integer→long, Short→int, etc.)");
        System.out.println("4. Autobox + Cast does NOT work ✗");
        System.out.println("   (int→Long needs explicit cast first)");

        System.out.println("\n*** EXAM TIPS ***");
        System.out.println("• int → Long: Cast first → (long)intValue then autobox ✓");
        System.out.println("• Integer → long: Unbox then widen automatically ✓");
        System.out.println("• Integer → Long: No automatic conversion ✗");
        System.out.println("• Look for wrapper to different primitive type!");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * LOCAL VARIABLE SPECIFIERS
     * ═══════════════════════════════════════════════════════════════════════
     *
     * LOCAL VARIABLES (inside methods):
     * - Can ONLY use: final
     * - Cannot use: static, volatile, transient, access modifiers
     */
    private static void localVariableSpecifiers() {
        System.out.println("=== LOCAL VARIABLE SPECIFIERS ===");
        System.out.println("Local variables (inside methods) can ONLY use: final\n");

        // Valid - no modifier
        int x = 10;
        System.out.println("int x = 10;                        ✓ Valid");

        // Valid - final
        final int y = 20;
        System.out.println("final int y = 20;                  ✓ Valid");

        // Invalid examples (commented out because they don't compile)
        System.out.println("\n--- INVALID (Does Not Compile) ---");
        System.out.println("static int z = 30;                 ✗ DOES NOT COMPILE");
        System.out.println("private int a = 40;                ✗ DOES NOT COMPILE");
        System.out.println("volatile int b = 50;               ✗ DOES NOT COMPILE");
        System.out.println("transient int c = 60;              ✗ DOES NOT COMPILE");

        // Final means cannot reassign
        System.out.println("\n--- final Prevents Reassignment ---");
        // y = 30;  // DOES NOT COMPILE - y is final
        System.out.println("final int y = 20;");
        System.out.println("y = 30;  // ✗ DOES NOT COMPILE (y is final)");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * INSTANCE VARIABLE SPECIFIERS
     * ═══════════════════════════════════════════════════════════════════════
     *
     * INSTANCE VARIABLES (fields):
     * - Can use: final, volatile, transient
     * - Can combine with access modifiers
     */
    private static void instanceVariableSpecifiers() {
        System.out.println("=== INSTANCE VARIABLE SPECIFIERS ===");
        System.out.println("Instance variables can use: final, volatile, transient\n");

        System.out.println("Examples:");
        System.out.println("  private final int x = 10;        ✓ Valid");
        System.out.println("  public volatile boolean flag;    ✓ Valid");
        System.out.println("  protected transient String temp; ✓ Valid");
        System.out.println("  private final transient int y;   ✓ Valid (can combine)");

        System.out.println("\n--- Specifier Meanings ---");
        System.out.println("final:");
        System.out.println("  • Cannot be reassigned after initialization");
        System.out.println("  • Must be initialized inline, in constructor, or in initializer");
        System.out.println("\nvolatile:");
        System.out.println("  • Used for multithreading");
        System.out.println("  • Ensures visibility across threads");
        System.out.println("\ntransient:");
        System.out.println("  • Used for serialization");
        System.out.println("  • Field is NOT serialized");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * FINAL INSTANCE VARIABLES - CRITICAL EXAM TOPIC
     * ═══════════════════════════════════════════════════════════════════════
     *
     * *** VERY IMPORTANT ***
     *
     * final instance variables MUST be assigned by end of constructor!
     *
     * THREE PLACES TO INITIALIZE:
     * 1. Inline (where declared)
     * 2. Instance initializer block { }
     * 3. Constructor
     *
     * EXAM TRAP:
     * - Can mix and match (some inline, some in constructor)
     * - MUST be assigned in ALL constructors (if multiple)
     * - Cannot leave unassigned - compilation error!
     */
    private static void finalInstanceVariables() {
        System.out.println("=== FINAL INSTANCE VARIABLES ===");
        System.out.println("*** CRITICAL EXAM TOPIC ***\n");

        System.out.println("final instance variables MUST be assigned in:");
        System.out.println("  1. Inline (where declared)");
        System.out.println("  2. Instance initializer block { }");
        System.out.println("  3. Constructor");

        System.out.println("\n--- Example Class ---");
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
        System.out.println("        // All final fields MUST be assigned by here!");
        System.out.println("    }");
        System.out.println("}");

        System.out.println("\n--- EXAM TRAPS ---");
        System.out.println("\n1. Multiple Constructors");
        System.out.println("class Example {");
        System.out.println("    private final int x;");
        System.out.println();
        System.out.println("    public Example() {");
        System.out.println("        x = 10;  // ✓ Assigned");
        System.out.println("    }");
        System.out.println();
        System.out.println("    public Example(int val) {");
        System.out.println("        x = val;  // ✓ MUST assign in ALL constructors");
        System.out.println("    }");
        System.out.println("}");

        System.out.println("\n2. Unassigned final - DOES NOT COMPILE");
        System.out.println("class Bad {");
        System.out.println("    private final int x;  // ✗ Never assigned!");
        System.out.println();
        System.out.println("    public Bad() {");
        System.out.println("        // ✗ ERROR: x not initialized");
        System.out.println("    }");
        System.out.println("}");

        System.out.println("\n3. Cannot Reassign final");
        System.out.println("class Bad {");
        System.out.println("    private final int x = 10;");
        System.out.println();
        System.out.println("    public Bad() {");
        System.out.println("        x = 20;  // ✗ ERROR: already assigned!");
        System.out.println("    }");
        System.out.println("}");

        // Demonstrate our class
        System.out.println("\n--- This Class Example ---");
        AccessModifiersAndVariables obj = new AccessModifiersAndVariables();
        System.out.println("finalInline:       " + obj.finalInline);       // 10
        System.out.println("finalInConstructor: " + obj.finalInConstructor); // 20
        System.out.println("finalInInitializer: " + obj.finalInInitializer); // 30

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * EFFECTIVELY FINAL - CRITICAL EXAM CONCEPT
     * ═══════════════════════════════════════════════════════════════════════
     *
     * A variable is "effectively final" if:
     * - It is not declared with final keyword
     * - It is never reassigned after initialization
     *
     * WHY IT MATTERS:
     * - Lambda expressions can only access effectively final variables
     * - Anonymous inner classes can only access effectively final variables
     * - Introduced in Java 8
     */
    private static void effectivelyFinalBasics() {
        System.out.println("=== EFFECTIVELY FINAL BASICS ===");
        System.out.println("*** CRITICAL EXAM CONCEPT ***\n");

        System.out.println("Definition:");
        System.out.println("  A variable is 'effectively final' if:");
        System.out.println("  • NOT declared with 'final' keyword");
        System.out.println("  • Never reassigned after initialization");

        System.out.println("\n--- Examples ---");

        // Effectively final - never reassigned
        int x = 10;
        System.out.println("\nint x = 10;");
        System.out.println("// x is never reassigned");
        System.out.println("→ x IS effectively final ✓");

        // NOT effectively final - reassigned
        int y = 20;
        y = 30;
        System.out.println("\nint y = 20;");
        System.out.println("y = 30;");
        System.out.println("→ y is NOT effectively final ✗");

        // Effectively final - initialized later but never reassigned
        int z;
        z = 40;
        System.out.println("\nint z;");
        System.out.println("z = 40;");
        System.out.println("// z is never reassigned after this");
        System.out.println("→ z IS effectively final ✓");

        // NOT effectively final - reassigned in conditional
        int a = 50;
        if (true) {
            a = 60;
        }
        System.out.println("\nint a = 50;");
        System.out.println("if (true) { a = 60; }");
        System.out.println("→ a is NOT effectively final ✗");

        System.out.println("\n*** Key Point ***");
        System.out.println("ANY reassignment (even in conditional) makes variable NOT effectively final");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * TESTING EFFECTIVELY FINAL
     * ═══════════════════════════════════════════════════════════════════════
     *
     * *** EXAM TIP ***
     *
     * HOW TO TEST if a variable is effectively final:
     * 1. Add the "final" keyword to the variable declaration
     * 2. If it still compiles → it WAS effectively final
     * 3. If it doesn't compile → it was NOT effectively final
     */
    private static void effectivelyFinalTesting() {
        System.out.println("=== TESTING EFFECTIVELY FINAL ===");
        System.out.println("*** EXAM TIP ***\n");

        System.out.println("HOW TO TEST:");
        System.out.println("  1. Add 'final' keyword to variable");
        System.out.println("  2. If still compiles → was effectively final ✓");
        System.out.println("  3. If doesn't compile → was NOT effectively final ✗");

        System.out.println("\n--- Example 1: Is 'x' effectively final? ---");
        System.out.println("int x = 10;");
        System.out.println("System.out.println(x);");
        System.out.println("\nTEST: Add 'final':");
        System.out.println("final int x = 10;");
        System.out.println("System.out.println(x);");
        System.out.println("→ Still compiles! x WAS effectively final ✓");

        final int x = 10;
        System.out.println("Value of x: " + x);

        System.out.println("\n--- Example 2: Is 'y' effectively final? ---");
        System.out.println("int y = 20;");
        System.out.println("y = 30;");
        System.out.println("\nTEST: Add 'final':");
        System.out.println("final int y = 20;");
        System.out.println("y = 30;  // ✗ ERROR: cannot assign to final variable");
        System.out.println("→ Doesn't compile! y was NOT effectively final ✗");

        System.out.println("\n--- Example 3: Tricky Case ---");
        System.out.println("int z;");
        System.out.println("z = 40;");
        System.out.println("System.out.println(z);");
        System.out.println("\nTEST: Add 'final':");
        System.out.println("final int z;");
        System.out.println("z = 40;  // ✓ First assignment is OK");
        System.out.println("System.out.println(z);");
        System.out.println("→ Still compiles! z WAS effectively final ✓");

        final int z;
        z = 40;
        System.out.println("Value of z: " + z);

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * EFFECTIVELY FINAL WITH LAMBDAS
     * ═══════════════════════════════════════════════════════════════════════
     *
     * *** WHY EFFECTIVELY FINAL MATTERS ***
     *
     * Lambdas and anonymous inner classes can ONLY access:
     * - final variables
     * - effectively final variables
     *
     * EXAM TRAP: Common question pattern!
     */
    private static void effectivelyFinalWithLambdas() {
        System.out.println("=== EFFECTIVELY FINAL WITH LAMBDAS ===");
        System.out.println("*** WHY IT MATTERS ***\n");

        System.out.println("Lambdas can ONLY access:");
        System.out.println("  • final variables");
        System.out.println("  • effectively final variables");

        // Example 1: Effectively final - works
        System.out.println("\n--- Example 1: Effectively Final ✓ ---");
        int x = 10;
        Runnable r1 = () -> System.out.println("x = " + x);
        System.out.println("int x = 10;");
        System.out.println("Runnable r = () -> System.out.println(x);");
        System.out.println("→ Compiles! x is effectively final");
        r1.run();

        // Example 2: NOT effectively final - doesn't compile
        System.out.println("\n--- Example 2: NOT Effectively Final ✗ ---");
        int y = 20;
        y = 30;  // Reassignment makes it NOT effectively final
        // Runnable r2 = () -> System.out.println(y);  // DOES NOT COMPILE
        System.out.println("int y = 20;");
        System.out.println("y = 30;");
        System.out.println("Runnable r = () -> System.out.println(y);");
        System.out.println("→ DOES NOT COMPILE! y is not effectively final");

        // Example 3: Modified in lambda - doesn't compile
        System.out.println("\n--- Example 3: Modified in Lambda ✗ ---");
        int z = 40;
        // Runnable r3 = () -> z = 50;  // DOES NOT COMPILE
        System.out.println("int z = 40;");
        System.out.println("Runnable r = () -> z = 50;");
        System.out.println("→ DOES NOT COMPILE! Cannot modify in lambda");

        // Example 4: final keyword - works
        System.out.println("\n--- Example 4: Explicitly final ✓ ---");
        final int w = 50;
        Runnable r4 = () -> System.out.println("w = " + w);
        System.out.println("final int w = 50;");
        System.out.println("Runnable r = () -> System.out.println(w);");
        System.out.println("→ Compiles! w is final");
        r4.run();

        // EXAM TIP
        System.out.println("\n*** EXAM TIP ***");
        System.out.println("Common exam pattern:");
        System.out.println("  1. Show variable with lambda");
        System.out.println("  2. Variable is reassigned before or after lambda");
        System.out.println("  3. Ask: Does it compile?");
        System.out.println("  4. Answer: NO - variable not effectively final!");

        System.out.println("\n*** TRICKY EXAM EXAMPLE ***");
        System.out.println("int num = 1;");
        System.out.println("Runnable r = () -> System.out.println(num);");
        System.out.println("num = 2;  // Reassigned AFTER lambda creation");
        System.out.println("\nDoes this compile?");
        System.out.println("Answer: NO! The reassignment makes 'num' not effectively final,");
        System.out.println("even though it happens AFTER the lambda is created.");

        System.out.println();
    }
}

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * ADDITIONAL EXAMPLE CLASSES (for reference)
 * ═══════════════════════════════════════════════════════════════════════════
 */

// Package-private class (no access modifier)
class PackagePrivateExample {
    void method() {
        System.out.println("Package-private class");
    }
}

// Class demonstrating final fields
class FinalFieldsExample {
    // All three valid ways to initialize final fields
    private final int a = 10;              // Inline
    private final int b;                   // In instance initializer
    private final int c;                   // In constructor

    // Instance initializer
    {
        b = 20;
    }

    // Constructor
    public FinalFieldsExample() {
        c = 30;
    }

    // If you have multiple constructors, ALL must initialize final fields
    public FinalFieldsExample(int value) {
        c = value;  // Must initialize in ALL constructors
    }
}

// Class demonstrating what DOESN'T compile
class CompilationErrorExamples {
    // Uncomment to see compilation errors:

    // private final int x;  // ERROR: not initialized

    void method() {
        // static int y = 10;      // ERROR: local variables can't be static
        // private int z = 20;     // ERROR: local variables can't have access modifiers
        // volatile int a = 30;    // ERROR: local variables can't be volatile
        // transient int b = 40;   // ERROR: local variables can't be transient
    }
}
