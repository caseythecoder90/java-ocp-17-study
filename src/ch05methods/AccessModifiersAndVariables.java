package ch05methods;

/**
 * ACCESS MODIFIERS AND VARIABLE SPECIFIERS - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * ACCESS MODIFIERS (Least to Most Permissive)
 * ═══════════════════════════════════════════════════════════════════════════
 * private          → Same class only
 * (package)        → Same package only (NO keyword - just omit modifier)
 * protected        → Same package + subclasses (more permissive than package!)
 * public           → Everywhere
 *
 * EXAM TRAP: protected > package-private!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * VARIABLE SPECIFIERS
 * ═══════════════════════════════════════════════════════════════════════════
 * LOCAL variables:     final only
 * INSTANCE variables:  final, volatile, transient
 * STATIC variables:    final, volatile, transient
 *
 * final instance: Must init inline, in constructor, or instance initializer
 * final static: Must init inline or in static initializer
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * EFFECTIVELY FINAL
 * ═══════════════════════════════════════════════════════════════════════════
 * Not declared final but never reassigned after initialization.
 * Required for lambdas and anonymous inner classes.
 *
 * Test: Add "final" keyword. If compiles → was effectively final.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * PROTECTED ACCESS TRAP
 * ═══════════════════════════════════════════════════════════════════════════
 * Child class in different package CAN access protected members:
 * - Directly: this.protectedMethod()  ✓
 * - Through child reference: child.protectedMethod()  ✓
 * - Through parent reference: parent.protectedMethod()  ✗ DOES NOT COMPILE
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * STATIC vs INSTANCE CONTEXT
 * ═══════════════════════════════════════════════════════════════════════════
 * static method CANNOT access instance members directly (no "this").
 * Need instance: ClassName obj = new ClassName(); obj.instanceMethod();
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * AUTOBOXING vs CASTING
 * ═══════════════════════════════════════════════════════════════════════════
 * Java will do ONE conversion automatically:
 * - Widen primitive: int → long  ✓
 * - Autobox: int → Integer  ✓
 * - Unbox: Integer → int  ✓
 *
 * Will NOT do both (except unbox + widen):
 * - Integer → Long  ✗ (would need unbox Integer→int, then widen int→long, then box long→Long)
 * - int → Long  ✗ (would need widen int→long, then box long→Long)
 *
 * EXCEPTION: Unbox + widen works!
 * - Integer → long  ✓ (unbox to int, widen to long)
 */
public class AccessModifiersAndVariables {

    // ═══════════════════════════════════════════════════════════════════════
    // FINAL INSTANCE VARIABLES - Must initialize in constructor
    // ═══════════════════════════════════════════════════════════════════════
    private final int finalInline = 10;
    private final int finalInConstructor;
    private final int finalInInitializer;

    { finalInInitializer = 20; }  // Instance initializer

    public AccessModifiersAndVariables() {
        finalInConstructor = 30;  // ALL constructors must init final fields
    }

    // ═══════════════════════════════════════════════════════════════════════
    // EFFECTIVELY FINAL
    // ═══════════════════════════════════════════════════════════════════════
    public void effectivelyFinalExample() {
        int x = 10;           // Not declared final
        // x = 20;            // ✗ If uncommented, NOT effectively final

        // Lambda can access x because it's effectively final
        Runnable r = () -> System.out.println(x);

        // Test: Add "final" to x. Still compiles? → Was effectively final!
        // final int x = 10;  ✓ Compiles, so x is effectively final
    }

    // ═══════════════════════════════════════════════════════════════════════
    // STATIC vs INSTANCE CONTEXT
    // ═══════════════════════════════════════════════════════════════════════
    private int instanceVar = 10;

    public static void staticMethodExample() {
        // System.out.println(instanceVar);  // ✗ DOES NOT COMPILE
        // Static method cannot access instance variable directly

        // Need an instance:
        AccessModifiersAndVariables obj = new AccessModifiersAndVariables();
        System.out.println(obj.instanceVar);  // ✓ Works
    }

    // ═══════════════════════════════════════════════════════════════════════
    // AUTOBOXING / UNBOXING / CASTING
    // ═══════════════════════════════════════════════════════════════════════
    public void autoboxingExamples() {
        // ────────────────────────────────────────────────────────────────────
        // ONE conversion: ✓ Works
        // ────────────────────────────────────────────────────────────────────
        int primitiveInt = 5;
        long primitiveLong = primitiveInt;      // ✓ Widen
        Integer wrapperInt = primitiveInt;      // ✓ Autobox
        int unboxed = wrapperInt;               // ✓ Unbox

        // ────────────────────────────────────────────────────────────────────
        // TWO conversions: ✗ Does NOT work (except unbox + widen)
        // ────────────────────────────────────────────────────────────────────
        // Long wrapperLong = primitiveInt;     // ✗ Would need widen + box
        // Integer boxed = primitiveLong;       // ✗ Would need narrow + box

        // ────────────────────────────────────────────────────────────────────
        // EXCEPTION: Unbox + Widen works!
        // ────────────────────────────────────────────────────────────────────
        Integer wrapper = 5;
        long widened = wrapper;                  // ✓ Unbox then widen

        // ────────────────────────────────────────────────────────────────────
        // But Widen + Box does NOT work
        // ────────────────────────────────────────────────────────────────────
        int primitive = 5;
        // Long boxed = primitive;               // ✗ Would need widen + box
    }

    public static void main(String[] args) {
        AccessModifiersAndVariables obj = new AccessModifiersAndVariables();
        obj.effectivelyFinalExample();
        staticMethodExample();
        obj.autoboxingExamples();
        System.out.println("✓ All examples compiled successfully");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// PROTECTED ACCESS EXAMPLES (different package simulation)
// ═══════════════════════════════════════════════════════════════════════════

class ParentInSamePackage {
    protected void protectedMethod() {
        System.out.println("Protected method");
    }
}

class ChildInSamePackage extends ParentInSamePackage {
    public void demonstrateProtectedAccess() {
        // In same package - can access protected through ANY reference
        this.protectedMethod();                           // ✓ Works

        ChildInSamePackage child = new ChildInSamePackage();
        child.protectedMethod();                          // ✓ Works

        ParentInSamePackage parent = new ParentInSamePackage();
        parent.protectedMethod();                         // ✓ Works (same package!)
    }
}

// In DIFFERENT package, child can ONLY access through child reference:
// ParentInSamePackage parent = new ParentInSamePackage();
// parent.protectedMethod();  // ✗ DOES NOT COMPILE (different package, parent reference)
//
// ChildInSamePackage child = new ChildInSamePackage();
// child.protectedMethod();   // ✓ Works (child reference)
// this.protectedMethod();    // ✓ Works (direct access)
