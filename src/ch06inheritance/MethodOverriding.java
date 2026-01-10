package ch06inheritance;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * METHOD OVERRIDING - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * METHOD OVERRIDING
 * ═══════════════════════════════════════════════════════════════════════════
 * Subclass declares new implementation for inherited method with same
 * signature and compatible return type.
 * Access parent version: super.method()
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * FOUR KEY RULES FOR OVERRIDING
 * ═══════════════════════════════════════════════════════════════════════════
 * 1. SIGNATURE: Same method name, parameter types, and order
 *
 * 2. ACCESS MODIFIER: Must be AT LEAST as accessible
 *    Order: private < package < protected < public
 *
 * 3. EXCEPTIONS: Cannot declare new or broader CHECKED exceptions
 *    ✓ Fewer exceptions, narrower exceptions, unchecked exceptions
 *    ✗ New checked exceptions, broader checked exceptions
 *
 * 4. RETURN TYPE: Same or subtype (covariant)
 *    COVARIANCE TEST: Can you assign B to A without cast?
 *    B b = new B(); A a = b; // No cast? → Covariant ✓
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * COVARIANT RETURN TYPES
 * ═══════════════════════════════════════════════════════════════════════════
 * Child can return subtype of parent's return type.
 *
 * Examples:
 * Parent: Object     → Child: String       ✓
 * Parent: Number     → Child: Integer      ✓
 * Parent: CharSequence → Child: String     ✓
 * Parent: String     → Child: CharSequence ✗ (wrong direction!)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * SPECIAL CASES
 * ═══════════════════════════════════════════════════════════════════════════
 * PRIVATE methods:   NOT inherited, cannot override
 * STATIC methods:    HIDDEN (not overridden), reference type determines version
 * FINAL methods:     CANNOT be overridden
 * VARIABLES:         HIDDEN (not overridden), reference type determines version
 */
public class MethodOverriding {

    public static void main(String[] args) {
        // ────────────────────────────────────────────────────────────────────
        // Basic overriding with polymorphism
        // ────────────────────────────────────────────────────────────────────
        OverrideParent parent = new OverrideChild();
        parent.display();  // Calls Child version (polymorphism!)

        // ────────────────────────────────────────────────────────────────────
        // Covariant return types
        // ────────────────────────────────────────────────────────────────────
        CovariantChild cc = new CovariantChild();
        String result = cc.getValue();  // Returns String (subtype of CharSequence)
        System.out.println("Covariant: " + result);

        // ────────────────────────────────────────────────────────────────────
        // Exception rules
        // ────────────────────────────────────────────────────────────────────
        ExceptionChild ec = new ExceptionChild();
        try {
            ec.fewerExceptions();
            ec.narrowerException();
        } catch (Exception e) { }

        // ────────────────────────────────────────────────────────────────────
        // Static method hiding (NOT overriding!)
        // ────────────────────────────────────────────────────────────────────
        StaticParent.method();  // Parent
        StaticChild.method();   // Child

        StaticParent ref = new StaticChild();
        ref.method();  // Parent! (reference type, not object type)

        // ────────────────────────────────────────────────────────────────────
        // Variable hiding
        // ────────────────────────────────────────────────────────────────────
        HiddenVarParent vp = new HiddenVarChild();
        System.out.println("Variable: " + vp.name);  // Parent (reference type)
        System.out.println("Method: " + vp.display());  // Child (object type)

        System.out.println("\n✓ All overriding examples work");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// BASIC OVERRIDING
// ═══════════════════════════════════════════════════════════════════════════

class OverrideParent {
    void display() {
        System.out.println("Parent");
    }
}

class OverrideChild extends OverrideParent {
    @Override
    void display() {
        System.out.println("Child (overridden)");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// ACCESS MODIFIER RULE - Must be at least as accessible
// ═══════════════════════════════════════════════════════════════════════════

class AccessParent {
    protected void method() { }
}

class AccessChild extends AccessParent {
    @Override
    public void method() { }  // ✓ More accessible (protected → public)

    // protected void method() { }  // ✓ Same accessibility
    // void method() { }            // ✗ Less accessible (protected → package)
}

// ═══════════════════════════════════════════════════════════════════════════
// EXCEPTION RULE - Cannot declare new or broader checked exceptions
// ═══════════════════════════════════════════════════════════════════════════

class ExceptionParent {
    void fewerExceptions() throws IOException { }
    void narrowerException() throws IOException { }
    void uncheckedOK() { }
}

class ExceptionChild extends ExceptionParent {
    @Override
    void fewerExceptions() { }  // ✓ Fewer exceptions

    @Override
    void narrowerException() throws FileNotFoundException { }  // ✓ Narrower

    @Override
    void uncheckedOK() throws NullPointerException { }  // ✓ Unchecked always OK

    // ✗ void method() throws IOException, SQLException { }  // New checked exception
    // ✗ void narrowerException() throws Exception { }  // Broader exception
}

// ═══════════════════════════════════════════════════════════════════════════
// COVARIANT RETURN TYPES - Child can return subtype
// ═══════════════════════════════════════════════════════════════════════════

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

    // Test: String s = "test"; CharSequence cs = s; ✓ No cast → Covariant!
}

// ═══════════════════════════════════════════════════════════════════════════
// STATIC METHOD HIDING - NOT overriding!
// ═══════════════════════════════════════════════════════════════════════════

class StaticParent {
    static void method() {
        System.out.println("Static Parent");
    }
}

class StaticChild extends StaticParent {
    static void method() {  // Hides, doesn't override
        System.out.println("Static Child");
    }
}

// CRITICAL: static → instance or instance → static DOES NOT COMPILE
// class Bad extends StaticParent {
//     void method() { }  // ✗ DOES NOT COMPILE
// }

// ═══════════════════════════════════════════════════════════════════════════
// VARIABLE HIDING - Reference type determines version
// ═══════════════════════════════════════════════════════════════════════════

class HiddenVarParent {
    String name = "Parent";

    String display() {
        return "Parent method";
    }
}

class HiddenVarChild extends HiddenVarParent {
    String name = "Child";  // Hides parent's name

    @Override
    String display() {
        return "Child method";
    }
}

// KEY DIFFERENCE:
// Variables: Reference type determines version (hiding)
// Methods: Object type determines version (overriding)

// ═══════════════════════════════════════════════════════════════════════════
// FINAL METHODS - Cannot be overridden
// ═══════════════════════════════════════════════════════════════════════════

class FinalParent {
    final void finalMethod() {
        System.out.println("Cannot override");
    }

    void normalMethod() {
        System.out.println("Can override");
    }
}

class FinalChild extends FinalParent {
    // void finalMethod() { }  // ✗ DOES NOT COMPILE

    @Override
    void normalMethod() {
        System.out.println("Overridden");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// PRIVATE METHODS - NOT inherited, cannot override
// ═══════════════════════════════════════════════════════════════════════════

class PrivateParent {
    private void method() {
        System.out.println("Parent private");
    }

    public void callMethod() {
        method();  // Calls parent's private method
    }
}

class PrivateChild extends PrivateParent {
    // This is NOT an override! It's a new method
    public void method() {
        System.out.println("Child public");
    }
}
