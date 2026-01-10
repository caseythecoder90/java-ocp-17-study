package ch07beyondclasses;

/**
 * DEFINING INTERFACES - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * INTERFACE DEFINITION
 * ═══════════════════════════════════════════════════════════════════════════
 * Syntax: [access] interface InterfaceName { }
 *
 * IMPLICIT MODIFIERS:
 * - Interfaces are implicitly abstract
 * - Interface variables are implicitly public static final
 * - Abstract methods are implicitly public abstract
 * - Methods without private modifier are implicitly public
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * INTERFACE RULES
 * ═══════════════════════════════════════════════════════════════════════════
 * 1. Cannot be marked final (interfaces are meant to be implemented)
 * 2. May be marked abstract (but it's redundant - implicit)
 * 3. Can have zero or more methods
 * 4. Cannot be instantiated directly
 * 5. All top-level interfaces are implicitly public or package-private
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * VARIABLE RULES
 * ═══════════════════════════════════════════════════════════════════════════
 * All interface variables are implicitly: public static final
 * Must be initialized when declared (they're constants)
 */
public class DefiningInterfaceExample {

    public static void main(String[] args) {
        // ────────────────────────────────────────────────────────────────────
        // Interface constants
        // ────────────────────────────────────────────────────────────────────
        System.out.println("Constant: " + DefiningExample.MAX_VALUE);

        // ────────────────────────────────────────────────────────────────────
        // Implementing interface
        // ────────────────────────────────────────────────────────────────────
        DefiningImpl impl = new DefiningImpl();
        System.out.println("Speed: " + impl.getSpeed(5));

        // ────────────────────────────────────────────────────────────────────
        // Empty interface is valid
        // ────────────────────────────────────────────────────────────────────
        EmptyImpl empty = new EmptyImpl();
        System.out.println("Empty interface: valid");

        System.out.println("\n✓ Interface definition examples work");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// BASIC INTERFACE WITH IMPLICIT MODIFIERS
// ═══════════════════════════════════════════════════════════════════════════

// 'abstract' is implicit (can omit)
interface DefiningExample {
    // All three modifiers are implicit:
    int MAX_VALUE = 100;
    // public static final int MAX_VALUE = 100;  // Same thing

    // public abstract are implicit:
    Float getSpeed(int age);
    // public abstract Float getSpeed(int age);  // Same thing
}

class DefiningImpl implements DefiningExample {
    @Override
    public Float getSpeed(int age) {  // Must be public
        return age * 2.0f;
    }

    public void useConstant() {
        System.out.println(MAX_VALUE);  // Can access constant
        System.out.println(DefiningExample.MAX_VALUE);  // Or use interface name
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// EMPTY INTERFACE (valid)
// ═══════════════════════════════════════════════════════════════════════════

interface EmptyInterface {
    // No methods - perfectly valid (marker interface)
}

class EmptyImpl implements EmptyInterface {
    // No methods to implement
}

// ═══════════════════════════════════════════════════════════════════════════
// EXAM TRAPS
// ═══════════════════════════════════════════════════════════════════════════

// TRAP 1: final interface
// final interface Bad { }  // ✗ DOES NOT COMPILE - cannot be final

// TRAP 2: Uninitialized constant
// interface Bad {
//     int CONSTANT;  // ✗ DOES NOT COMPILE - must initialize
// }

// TRAP 3: Cannot instantiate interface
// interface Example { }
// Example e = new Example();  // ✗ DOES NOT COMPILE

// TRAP 4: Method must be public in implementation
// interface Example {
//     void method();  // Implicitly public
// }
// class Bad implements Example {
//     void method() { }  // ✗ DOES NOT COMPILE - must be public
// }
