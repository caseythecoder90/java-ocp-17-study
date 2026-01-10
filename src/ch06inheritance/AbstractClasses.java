package ch06inheritance;

/**
 * ABSTRACT CLASSES - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * ABSTRACT CLASSES
 * ═══════════════════════════════════════════════════════════════════════════
 * Declared with 'abstract' modifier, CANNOT be instantiated.
 *
 * Purpose: Prevent direct instantiation while providing common functionality.
 * Use when you want subclasses to inherit properties but fill in implementation details.
 *
 * Can contain:
 * - Abstract methods (no body, must be overridden)
 * - Concrete methods (with body)
 * - Variables, constructors, static members
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * ABSTRACT METHODS
 * ═══════════════════════════════════════════════════════════════════════════
 * Declared with 'abstract' modifier, NO body.
 * Forces subclasses to implement.
 *
 * Syntax: abstract void methodName();
 *
 * Purpose: Polymorphism - guarantee method exists without specifying
 * implementation in abstract class.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * KEY RULES
 * ═══════════════════════════════════════════════════════════════════════════
 * 1. Only INSTANCE methods can be abstract (static = hidden, not overridden)
 * 2. Methods CANNOT be both abstract and private (private = not inherited)
 * 3. Abstract methods can ONLY be in abstract classes
 * 4. Concrete (non-abstract) class MUST implement ALL inherited abstract methods
 * 5. Abstract classes CAN have constructors
 * 6. Cannot be both abstract and final (contradiction!)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CONSTRUCTORS IN ABSTRACT CLASSES
 * ═══════════════════════════════════════════════════════════════════════════
 * Abstract classes CAN have constructors.
 * Called when concrete subclass is instantiated (via super()).
 * Used to initialize inherited fields and common setup.
 */
public class AbstractClasses {

    public static void main(String[] args) {
        // ────────────────────────────────────────────────────────────────────
        // Cannot instantiate abstract class
        // ────────────────────────────────────────────────────────────────────
        // Shape shape = new Shape();  // ✗ DOES NOT COMPILE

        // ────────────────────────────────────────────────────────────────────
        // Concrete subclass implements abstract methods
        // ────────────────────────────────────────────────────────────────────
        ConcreteCircle circle = new ConcreteCircle(5.0);
        System.out.println("Circle area: " + circle.getArea());
        circle.display();  // Inherited concrete method

        // ────────────────────────────────────────────────────────────────────
        // Polymorphism with abstract class
        // ────────────────────────────────────────────────────────────────────
        AbstractShape rectangle = new ConcreteRectangle(4.0, 6.0);
        System.out.println("Rectangle area: " + rectangle.getArea());

        // ────────────────────────────────────────────────────────────────────
        // Abstract class with constructor
        // ────────────────────────────────────────────────────────────────────
        AbstractAnimal dog = new ConcreteDog("Buddy");
        dog.makeSound();

        // ────────────────────────────────────────────────────────────────────
        // Abstract class with mix of abstract and concrete
        // ────────────────────────────────────────────────────────────────────
        MixedAbstract cat = new ConcreteCat("Whiskers");
        cat.makeSound();  // Implemented in concrete class
        cat.sleep();      // Inherited concrete method

        System.out.println("\n✓ All abstract examples work");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// BASIC ABSTRACT CLASS - Must implement all abstract methods
// ═══════════════════════════════════════════════════════════════════════════

abstract class AbstractShape {
    abstract double getArea();  // No body
    abstract double getPerimeter();

    void display() {  // Can have concrete methods
        System.out.println("Area: " + getArea());
    }
}

class ConcreteCircle extends AbstractShape {
    private double radius;

    ConcreteCircle(double radius) {
        this.radius = radius;
    }

    @Override
    double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    double getPerimeter() {
        return 2 * Math.PI * radius;
    }
}

class ConcreteRectangle extends AbstractShape {
    private double length, width;

    ConcreteRectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    @Override
    double getArea() {
        return length * width;
    }

    @Override
    double getPerimeter() {
        return 2 * (length + width);
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// CONSTRUCTOR IN ABSTRACT CLASS
// ═══════════════════════════════════════════════════════════════════════════

abstract class AbstractAnimal {
    private String name;

    AbstractAnimal(String name) {
        this.name = name;
        System.out.println("AbstractAnimal constructor: " + name);
    }

    abstract void makeSound();

    String getName() { return name; }
}

class ConcreteDog extends AbstractAnimal {
    ConcreteDog(String name) {
        super(name);  // Calls abstract class constructor
    }

    @Override
    void makeSound() {
        System.out.println(getName() + " says: Woof!");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// MIXED ABSTRACT AND CONCRETE MEMBERS
// ═══════════════════════════════════════════════════════════════════════════

abstract class MixedAbstract {
    abstract void makeSound();  // Abstract

    void sleep() {  // Concrete
        System.out.println("Sleeping...");
    }

    static void info() {  // Static
        System.out.println("Animals need food");
    }
}

class ConcreteCat extends MixedAbstract {
    private String name;

    ConcreteCat(String name) {
        this.name = name;
    }

    @Override
    void makeSound() {
        System.out.println(name + " says: Meow!");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// MULTILEVEL INHERITANCE - First concrete class implements ALL
// ═══════════════════════════════════════════════════════════════════════════

abstract class MultiAnimal {
    abstract void eat();
    abstract void sleep();
}

abstract class MultiMammal extends MultiAnimal {
    abstract void giveBirth();
    // eat() and sleep() still abstract
}

class MultiDog extends MultiMammal {
    @Override
    void eat() { System.out.println("Eating"); }

    @Override
    void sleep() { System.out.println("Sleeping"); }

    @Override
    void giveBirth() { System.out.println("Giving birth"); }
    // Must implement ALL three abstract methods
}

// ═══════════════════════════════════════════════════════════════════════════
// EXAM TRAPS
// ═══════════════════════════════════════════════════════════════════════════

// TRAP 1: Instantiating abstract class
// abstract class Bad { }
// Bad b = new Bad();  // ✗ DOES NOT COMPILE

// TRAP 2: Abstract method with body
// abstract class Bad {
//     abstract void method() { }  // ✗ DOES NOT COMPILE
// }

// TRAP 3: Abstract method in concrete class
// class Bad {
//     abstract void method();  // ✗ DOES NOT COMPILE
// }

// TRAP 4: abstract static method
// abstract class Bad {
//     abstract static void method();  // ✗ DOES NOT COMPILE
//     // Static methods hidden, not overridden
// }

// TRAP 5: abstract private method
// abstract class Bad {
//     abstract private void method();  // ✗ DOES NOT COMPILE
//     // Private methods not inherited
// }

// TRAP 6: abstract final
// abstract final class Bad { }  // ✗ DOES NOT COMPILE
// abstract class Bad {
//     abstract final void method();  // ✗ DOES NOT COMPILE
// }

// TRAP 7: Not implementing all abstract methods
// abstract class Parent {
//     abstract void method1();
//     abstract void method2();
// }
// class Child extends Parent {
//     void method1() { }
//     // ✗ DOES NOT COMPILE - method2() not implemented
// }
