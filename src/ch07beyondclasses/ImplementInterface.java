package ch07beyondclasses;

/**
 * IMPLEMENTING INTERFACES - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * IMPLEMENTING INTERFACES
 * ═══════════════════════════════════════════════════════════════════════════
 * Syntax: class ClassName implements Interface1, Interface2 { }
 *
 * RULES:
 * 1. Concrete class MUST implement ALL inherited abstract methods
 * 2. Implementation methods must be public
 * 3. Can implement multiple interfaces (unlike class extension)
 * 4. Covariant return types allowed
 * 5. Signature must match interface method exactly
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * INTERFACE EXTENSION
 * ═══════════════════════════════════════════════════════════════════════════
 * Syntax: interface Child extends Parent1, Parent2 { }
 *
 * Interfaces can extend multiple interfaces (unlike classes)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * ABSTRACT CLASSES AND INTERFACES
 * ═══════════════════════════════════════════════════════════════════════════
 * Abstract classes can implement interfaces without implementing all methods.
 * Only concrete classes must implement all inherited abstract methods.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * COMPATIBLE METHOD DECLARATIONS
 * ═══════════════════════════════════════════════════════════════════════════
 * Java supports inheriting two abstract methods if they have compatible
 * declarations (one method can properly override both).
 */
public class ImplementInterface {

    public static void main(String[] args) {
        // ────────────────────────────────────────────────────────────────────
        // Implementing multiple interfaces
        // ────────────────────────────────────────────────────────────────────
        MultiImpl multi = new MultiImpl();
        System.out.println("Speed: " + multi.getSpeed(5));
        System.out.println("Color: " + multi.getColor());

        // ────────────────────────────────────────────────────────────────────
        // Covariant return types
        // ────────────────────────────────────────────────────────────────────
        System.out.println("Covariant return: Float instead of Number");

        // ────────────────────────────────────────────────────────────────────
        // Polymorphism with interfaces
        // ────────────────────────────────────────────────────────────────────
        SpeedInterface speedRef = multi;
        System.out.println("Via interface ref: " + speedRef.getSpeed(10));

        // ────────────────────────────────────────────────────────────────────
        // Abstract class implementing interface
        // ────────────────────────────────────────────────────────────────────
        ConcreteImpl concrete = new ConcreteImpl();
        concrete.method1();
        concrete.method2();

        System.out.println("\n✓ Interface implementation examples work");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// IMPLEMENTING MULTIPLE INTERFACES
// ═══════════════════════════════════════════════════════════════════════════

interface SpeedInterface {
    Number getSpeed(int age);  // Returns Number
}

interface ColorInterface {
    CharSequence getColor();  // Returns CharSequence
}

class MultiImpl implements SpeedInterface, ColorInterface {
    // Covariant return type - Float is subtype of Number
    @Override
    public Float getSpeed(int age) {
        return age * 2.0f;
    }

    // Covariant return type - String is subtype of CharSequence
    @Override
    public String getColor() {
        return "Blue";
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// INTERFACE EXTENDING MULTIPLE INTERFACES
// ═══════════════════════════════════════════════════════════════════════════

interface ExtendedInterface extends SpeedInterface, ColorInterface {
    // Inherits getSpeed() and getColor()
    // Can add more methods
    void extraMethod();
}

class ExtendedImpl implements ExtendedInterface {
    @Override
    public Number getSpeed(int age) {
        return age * 2;
    }

    @Override
    public CharSequence getColor() {
        return "Red";
    }

    @Override
    public void extraMethod() {
        System.out.println("Extra");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// ABSTRACT CLASS IMPLEMENTING INTERFACE
// ═══════════════════════════════════════════════════════════════════════════

interface ExampleInterface {
    void method1();
    void method2();
}

// Abstract class doesn't need to implement all methods
abstract class AbstractImpl implements ExampleInterface {
    @Override
    public void method1() {
        System.out.println("Method1 implemented");
    }
    // method2() still abstract - concrete subclass must implement
}

// Concrete class must implement ALL remaining abstract methods
class ConcreteImpl extends AbstractImpl {
    @Override
    public void method2() {
        System.out.println("Method2 implemented");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// COMPATIBLE METHOD DECLARATIONS
// ═══════════════════════════════════════════════════════════════════════════

interface NumberInterface {
    Number getValue();
}

interface IntegerInterface {
    Integer getValue();  // More specific than Number
}

// One method can satisfy both - they're compatible
class CompatibleImpl implements NumberInterface, IntegerInterface {
    @Override
    public Integer getValue() {  // Satisfies both interfaces
        return 100;
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// EXAM TRAPS
// ═══════════════════════════════════════════════════════════════════════════

// TRAP 1: Implementation must be public
// interface Example {
//     void method();  // Implicitly public
// }
// class Bad implements Example {
//     void method() { }  // ✗ DOES NOT COMPILE - must be public
// }

// TRAP 2: Concrete class must implement ALL methods
// interface Example {
//     void method1();
//     void method2();
// }
// class Bad implements Example {
//     public void method1() { }
//     // ✗ DOES NOT COMPILE - method2() not implemented
// }

// TRAP 3: Incompatible return types
// interface InterfaceX {
//     String getValue();
// }
// interface InterfaceY {
//     Integer getValue();  // Incompatible with String
// }
// class Bad implements InterfaceX, InterfaceY {
//     // ✗ DOES NOT COMPILE - cannot satisfy both
// }
