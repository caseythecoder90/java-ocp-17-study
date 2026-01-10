package ch06inheritance;

/**
 * INHERITANCE BASICS - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * INHERITANCE FUNDAMENTALS
 * ═══════════════════════════════════════════════════════════════════════════
 * Syntax: class Child extends Parent { }
 *
 * KEY RULES:
 * 1. Java supports SINGLE inheritance (one parent only)
 * 2. All classes inherit from java.lang.Object (if no explicit parent)
 * 3. Private members are NOT inherited
 * 4. Constructors are NOT inherited
 * 5. Static methods are NOT overridden (they are hidden)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CLASS MODIFIERS
 * ═══════════════════════════════════════════════════════════════════════════
 * final:       Cannot be extended
 * abstract:    Cannot be instantiated, may have abstract methods
 * sealed:      Restricts which classes can extend it (Java 17+)
 * non-sealed:  Opens inheritance back up (used with sealed)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * METHOD MODIFIERS
 * ═══════════════════════════════════════════════════════════════════════════
 * final:     Cannot be overridden
 * abstract:  No body, must be implemented by subclass
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * SUPER KEYWORD
 * ═══════════════════════════════════════════════════════════════════════════
 * super.field       → Access parent field
 * super.method()    → Call parent method
 * super()           → Call parent constructor (must be first line)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * THIS VS SUPER WITH FIELDS
 * ═══════════════════════════════════════════════════════════════════════════
 * this.field  → Child's field (if exists), otherwise parent's
 * super.field → Always parent's field
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * INHERITING FROM OBJECT
 * ═══════════════════════════════════════════════════════════════════════════
 * Every class inherits: toString(), equals(), hashCode(), clone(), getClass()
 */
public class InheritanceBasics {

    public static void main(String[] args) {
        // ────────────────────────────────────────────────────────────────────
        // Basic inheritance
        // ────────────────────────────────────────────────────────────────────
        Dog dog = new Dog();
        dog.eat();    // Inherited from Animal
        dog.bark();   // Defined in Dog

        // ────────────────────────────────────────────────────────────────────
        // final modifier prevents overriding
        // ────────────────────────────────────────────────────────────────────
        FinalMethodChild fmc = new FinalMethodChild();
        fmc.finalMethod();      // Inherited, cannot override
        fmc.nonFinalMethod();   // Overridden

        // ────────────────────────────────────────────────────────────────────
        // abstract modifier requires subclass implementation
        // ────────────────────────────────────────────────────────────────────
        Circle circle = new Circle(5.0);
        System.out.println("Circle area: " + circle.getArea());

        // ────────────────────────────────────────────────────────────────────
        // super keyword to access parent
        // ────────────────────────────────────────────────────────────────────
        SuperChild child = new SuperChild();
        child.printBoth();  // Calls both child and parent versions

        // ────────────────────────────────────────────────────────────────────
        // this vs super with fields
        // ────────────────────────────────────────────────────────────────────
        FieldChild fc = new FieldChild();
        fc.printFields();

        System.out.println("\n✓ All inheritance examples work");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// BASIC INHERITANCE
// ═══════════════════════════════════════════════════════════════════════════

class Animal {
    public void eat() {
        System.out.println("Animal eating");
    }
}

class Dog extends Animal {
    public void bark() {
        System.out.println("Dog barking");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// FINAL MODIFIER - Cannot override final methods
// ═══════════════════════════════════════════════════════════════════════════

class FinalMethodParent {
    final void finalMethod() {
        System.out.println("Final method (cannot override)");
    }

    void nonFinalMethod() {
        System.out.println("Parent's non-final method");
    }
}

class FinalMethodChild extends FinalMethodParent {
    // Cannot override finalMethod() - it's final!

    @Override
    void nonFinalMethod() {
        System.out.println("Child's overridden method");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// ABSTRACT MODIFIER - Must implement abstract methods
// ═══════════════════════════════════════════════════════════════════════════

abstract class Shape {
    abstract double getArea();  // No body

    void display() {            // Can have concrete methods
        System.out.println("Area: " + getArea());
    }
}

class Circle extends Shape {
    private double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    @Override
    double getArea() {
        return Math.PI * radius * radius;
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// SUPER KEYWORD - Access parent members
// ═══════════════════════════════════════════════════════════════════════════

class Parent {
    void printMessage() {
        System.out.println("Parent message");
    }
}

class SuperChild extends Parent {
    @Override
    void printMessage() {
        System.out.println("Child message");
    }

    void printBoth() {
        this.printMessage();    // Calls child version
        super.printMessage();   // Calls parent version
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// THIS VS SUPER WITH FIELDS - Child takes priority with this
// ═══════════════════════════════════════════════════════════════════════════

class FieldParent {
    String name = "Parent";
    int age = 50;
}

class FieldChild extends FieldParent {
    String name = "Child";  // Shadows parent's name

    void printFields() {
        System.out.println("this.name: " + this.name);      // Child's name
        System.out.println("super.name: " + super.name);    // Parent's name
        System.out.println("this.age: " + this.age);        // Parent's age (no conflict)
    }
}
