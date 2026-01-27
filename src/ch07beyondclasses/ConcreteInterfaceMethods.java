package ch07beyondclasses;

/**
 * CONCRETE INTERFACE METHODS - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * INTERFACE MEMBER TYPES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * MEMBER TYPE          REQUIRED MODIFIERS    IMPLICIT MODIFIERS      HAS VALUE/BODY
 * ──────────────────────────────────────────────────────────────────────────────────
 * Constant variable    final (value)         public, static, final   Yes (value)
 * Abstract method      (none)                public, abstract        No
 * Default method       default               public                  Yes (body)
 * Static method        static                public                  Yes (body)
 * Private method       private               (none)                  Yes (body)
 * Private static       private, static       (none)                  Yes (body)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * DEFAULT METHODS
 * ═══════════════════════════════════════════════════════════════════════════
 * RULES:
 * 1. May ONLY be declared within an interface
 * 2. Must be marked with 'default' keyword and include method body
 * 3. Implicitly public (cannot be private, protected, or package-private)
 * 4. Cannot be marked abstract, final, or static
 * 5. May be optionally overridden by class that implements the interface
 * 6. If class inherits two or more default methods with same signature,
 *    class MUST override the method
 *
 * CALLING HIDDEN DEFAULT METHOD:
 * InterfaceName.super.methodName()
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * STATIC INTERFACE METHODS
 * ═══════════════════════════════════════════════════════════════════════════
 * RULES:
 * 1. Must be marked with 'static' keyword and have method body
 * 2. Static method without access modifier is implicitly public
 * 3. Cannot be marked abstract or final
 * 4. NOT inherited - cannot be accessed in implementing class without
 *    reference to interface name
 *
 * CALLING: InterfaceName.staticMethod()
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * PRIVATE METHODS (Instance and Static)
 * ═══════════════════════════════════════════════════════════════════════════
 * PURPOSE: Reduce code duplication within interface
 *
 * PRIVATE INSTANCE RULES:
 * 1. Must be marked 'private' and include method body
 * 2. May ONLY be called by default and other private non-static methods
 * 3. Cannot be called by static methods
 * 4. Cannot be accessed in implementing class or extending interface
 *
 * PRIVATE STATIC RULES:
 * 1. Must be marked 'private static' and include method body
 * 2. May be called by ANY method within the interface (static or non-static)
 * 3. Cannot be accessed in implementing class or extending interface
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * INTERFACE EXTENSION vs IMPLEMENTATION
 * ═══════════════════════════════════════════════════════════════════════════
 * EXTENDING INTERFACE (interface extends interface):
 * - Inherits abstract methods
 * - Inherits default methods (can override)
 * - Inherits constant variables
 * - Does NOT inherit static methods (not inherited)
 * - Does NOT have access to private methods
 *
 * IMPLEMENTING INTERFACE (class implements interface):
 * - Must implement abstract methods
 * - Inherits default methods (can override)
 * - Inherits constant variables
 * - Does NOT inherit static methods (must use InterfaceName.method())
 * - Does NOT have access to private methods
 */
public class ConcreteInterfaceMethods {

    public static void main(String[] args) {
        System.out.println("=== INTERFACE MEMBER TYPES ===\n");

        // ────────────────────────────────────────────────────────────────────
        // Constant variables and abstract methods
        // ────────────────────────────────────────────────────────────────────
        BasicImpl basic = new BasicImpl();
        System.out.println("Constant: " + BasicInterface.MAX_VALUE);
        System.out.println("Abstract method: " + basic.calculate(10));

        // ────────────────────────────────────────────────────────────────────
        // Default methods
        // ────────────────────────────────────────────────────────────────────
        DefaultImpl def = new DefaultImpl();
        def.defaultMethod();  // Uses interface default
        def.display();        // Overridden in class

        // ────────────────────────────────────────────────────────────────────
        // Static methods (called on interface, not instance)
        // ────────────────────────────────────────────────────────────────────
        StaticInterface.staticMethod();
        // StaticImpl.staticMethod();  // ✗ DOES NOT COMPILE - not inherited!

        // ────────────────────────────────────────────────────────────────────
        // Private methods (internal use only)
        // ────────────────────────────────────────────────────────────────────
        PrivateMethodImpl priv = new PrivateMethodImpl();
        priv.publicMethod();  // Internally uses private methods

        // ────────────────────────────────────────────────────────────────────
        // Default method conflict resolution
        // ────────────────────────────────────────────────────────────────────
        ConflictImpl conflict = new ConflictImpl();
        conflict.conflictingMethod();  // Must override when inherited from multiple

        // ────────────────────────────────────────────────────────────────────
        // Calling hidden default method
        // ────────────────────────────────────────────────────────────────────
        HiddenDefaultImpl hidden = new HiddenDefaultImpl();
        hidden.method();

        System.out.println("\n✓ All interface member types demonstrated");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// CONSTANT VARIABLES AND ABSTRACT METHODS
// ═══════════════════════════════════════════════════════════════════════════
interface BasicInterface {
    // Constant variable - implicitly public static final
    int MAX_VALUE = 100;
    // public static final int MAX_VALUE = 100;  // Same thing

    // Abstract method - implicitly public abstract
    int calculate(int value);
    // public abstract int calculate(int value);  // Same thing
}

class BasicImpl implements BasicInterface {
    @Override
    public int calculate(int value) {  // Must be public
        return value * 2;
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// DEFAULT METHODS
// ═══════════════════════════════════════════════════════════════════════════

interface DefaultInterface {
    // Default method - has body, implicitly public
    default void defaultMethod() {
        System.out.println("Default method from interface");
    }

    public default void display() {
        System.out.println("Interface display");
    }
}

class DefaultImpl implements DefaultInterface {
    // Optionally override default method
    @Override
    public void display() {
        System.out.println("Overridden display in class");
    }

    // defaultMethod() inherited from interface
}

// EXAM TRAPS for default methods:
// default void method();  // ✗ DOES NOT COMPILE - needs body
// default abstract void method() { }  // ✗ DOES NOT COMPILE - cannot be abstract
// default final void method() { }  // ✗ DOES NOT COMPILE - cannot be final
// default static void method() { }  // ✗ DOES NOT COMPILE - cannot be static
// private default void method() { }  // ✗ DOES NOT COMPILE - implicitly public

// ═══════════════════════════════════════════════════════════════════════════
// STATIC METHODS
// ═══════════════════════════════════════════════════════════════════════════

interface StaticInterface {
    // Static method - must have body, implicitly public
    static void staticMethod() {
        System.out.println("Static method from interface");
    }

    static int calculate(int x, int y) {
        return x + y;
    }
}

class StaticImpl implements StaticInterface {
    // Static methods are NOT inherited!
    // Cannot call staticMethod() without interface name

    public void useStatic() {
        StaticInterface.staticMethod();  // ✓ Must use interface name
        // staticMethod();  // ✗ DOES NOT COMPILE - not inherited
    }
}

// EXAM TRAPS for static methods:
// static void method();  // ✗ DOES NOT COMPILE - needs body
// static abstract void method() { }  // ✗ DOES NOT COMPILE - cannot be abstract
// static final void method() { }  // ✗ DOES NOT COMPILE - cannot be final

// ═══════════════════════════════════════════════════════════════════════════
// PRIVATE METHODS (Instance and Static)
// ═══════════════════════════════════════════════════════════════════════════

interface PrivateMethodInterface {
    // Public method that uses private helpers
    default void publicMethod() {
        System.out.println("Public: " + privateHelper());
        System.out.println("Static: " + privateStaticHelper());
    }

    // Private instance method - can only be called by default/private methods
    private String privateHelper() {
        return "Private instance method";
    }

    // Private static method - can be called by ANY method in interface
    private static String privateStaticHelper() {
        return "Private static method";
    }

    // Static method can call private static (but NOT private instance)
    public static void staticPublicMethod() {
        System.out.println(privateStaticHelper());  // ✓ Works
        // System.out.println(privateHelper());  // ✗ DOES NOT COMPILE - static cannot call instance
    }

    static void staticPublicMethod2() {
        System.out.println(privateStaticHelper());
    }
}

class PrivateMethodImpl implements PrivateMethodInterface {
    // Cannot access private methods from interface
    public void test() {
        // privateHelper();  // ✗ DOES NOT COMPILE
        // privateStaticHelper();  // ✗ DOES NOT COMPILE
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// DEFAULT METHOD CONFLICT RESOLUTION
// ═══════════════════════════════════════════════════════════════════════════

interface InterfaceA {
    default void conflictingMethod() {
        System.out.println("InterfaceA");
    }
}

interface InterfaceB {
    default void conflictingMethod() {
        System.out.println("InterfaceB");
    }
}

class ConflictImpl implements InterfaceA, InterfaceB {
    // MUST override when inheriting same default method from multiple interfaces
    @Override
    public void conflictingMethod() {
        System.out.println("ConflictImpl resolves conflict");
        // Can call either parent's version:
        InterfaceA.super.conflictingMethod();
        InterfaceB.super.conflictingMethod();
    }
}

// If we didn't override:
// class Bad implements InterfaceA, InterfaceB {
//     // ✗ DOES NOT COMPILE - conflicting default method
// }

// ═══════════════════════════════════════════════════════════════════════════
// CALLING HIDDEN DEFAULT METHOD - Special Syntax
// ═══════════════════════════════════════════════════════════════════════════

interface HiddenDefaultInterface {
    default void method() {
        System.out.println("Interface default method");
    }
}

class HiddenDefaultImpl implements HiddenDefaultInterface {
    @Override
    public void method() {
        System.out.println("Overridden in class");
        // Call hidden interface default method:
        HiddenDefaultInterface.super.method();  // Special syntax!
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// INTERFACE EXTENSION vs IMPLEMENTATION
// ═══════════════════════════════════════════════════════════════════════════

interface ParentInterface {
    int CONSTANT = 100;                      // Constant
    void abstractMethod();                   // Abstract
    default void defaultMethod() {           // Default
        System.out.println("Parent default");
    }
    static void staticMethod() {             // Static
        System.out.println("Parent static");
    }
    private void privateMethod() {           // Private (not accessible outside)
        System.out.println("Private");
    }
}

// EXTENDING INTERFACE (interface extends interface)
interface ChildInterface extends ParentInterface {
    // Inherits: CONSTANT, abstractMethod(), defaultMethod()
    // Does NOT inherit: staticMethod() (static methods not inherited)
    // Cannot access: privateMethod() (private to ParentInterface)

    @Override
    default void defaultMethod() {  // Can override default methods
        System.out.println("Child default");
        ParentInterface.super.defaultMethod();  // Can call parent's default
        System.out.println(CONSTANT); // Can access the public static final variable.
    }

    // Cannot call ParentInterface.staticMethod() without interface name
}

// IMPLEMENTING INTERFACE (class implements interface)
class ImplementingClass implements ParentInterface {
    @Override
    public void abstractMethod() {  // MUST implement abstract methods
        System.out.println("Implemented");
    }

    // Inherits: CONSTANT, defaultMethod()
    // Does NOT inherit: staticMethod() (must use ParentInterface.staticMethod())
    // Cannot access: privateMethod() (private to interface)

    public void useStatic() {
        ParentInterface.staticMethod();  // ✓ Must use interface name
        // staticMethod();  // ✗ Not inherited
        System.out.println(ParentInterface.CONSTANT);
    }

    public static void main(String[] args) {
        System.out.println(ParentInterface.CONSTANT);
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// COMPLETE EXAMPLE - All member types
// ═══════════════════════════════════════════════════════════════════════════

interface CompleteInterface {
    // 1. Constant variable
    int MAX = 100;

    // 2. Abstract method
    void abstractMethod();

    // 3. Default method
    default void defaultMethod() {
        System.out.println("Default: " + helperMethod());
        System.out.println("Static helper: " + staticHelper());
    }

    // 4. Static method
    static void staticMethod() {
        System.out.println("Static: " + staticHelper());
    }

    // 5. Private instance method (helper for default methods)
    private String helperMethod() {
        return "Private helper";
    }

    // 6. Private static method (helper for any method)
    private static String staticHelper() {
        return "Private static helper";
    }
}

class CompleteImpl implements CompleteInterface {
    @Override
    public void abstractMethod() {
        System.out.println("Abstract implemented");
    }

    // Can use MAX
    // Inherits defaultMethod()
    // Must use CompleteInterface.staticMethod()
    // Cannot access helperMethod() or staticHelper()
}
