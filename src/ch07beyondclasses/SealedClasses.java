package ch07beyondclasses;

/**
 * SEALED CLASSES - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * SEALED CLASSES BASICS
 * ═══════════════════════════════════════════════════════════════════════════
 * sealed: Indicates class or interface may ONLY be extended/implemented by
 *         named classes or interfaces
 *
 * permits: Lists the classes and interfaces allowed to extend/implement
 *
 * non-sealed: Applied to subclass, indicating it CAN be extended by
 *             unspecified classes (opens the hierarchy back up)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * SEALED CLASS RULES
 * ═══════════════════════════════════════════════════════════════════════════
 * 1. Sealed class and direct subclasses must be in SAME PACKAGE
 *
 * 2. All permitted subclasses MUST directly extend the sealed class
 *
 * 3. Every permitted subclass MUST have exactly one of these modifiers:
 *    - final      (cannot be extended further)
 *    - sealed     (can only be extended by its permitted subclasses)
 *    - non-sealed (can be extended by any class)
 *
 * 4. Sealed subclasses are held to SAME RULES as their parents
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * OMITTING PERMITS CLAUSE - CRITICAL EXAM TOPIC
 * ═══════════════════════════════════════════════════════════════════════════
 * Can OMIT permits clause when ALL subclasses are in the SAME FILE.
 *
 * sealed class Parent { }           // No permits needed
 * final class Child extends Parent { }  // In same file
 *
 * IMPORTANT: This only works when:
 * - All permitted subclasses are in the same source file
 * - Compiler can infer the permitted subclasses
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * NESTED SUBCLASSES - Special Syntax
 * ═══════════════════════════════════════════════════════════════════════════
 * Use dot notation: ParentClass.NestedClass
 *
 * sealed class Parent permits Parent.Nested { }
 * class Parent {
 *     final static class Nested extends Parent { }
 * }
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * SEALED INTERFACES
 * ═══════════════════════════════════════════════════════════════════════════
 * permits list can include:
 * - Classes that IMPLEMENT the interface
 * - Interfaces that EXTEND the interface
 *
 * sealed interface Shape permits Circle, Rectangle, Polygon { }
 * final class Circle implements Shape { }
 * non-sealed interface Polygon extends Shape { }
 */
public class SealedClasses {

    public static void main(String[] args) {
        System.out.println("=== SEALED CLASSES ===\n");

        // ────────────────────────────────────────────────────────────────────
        // Basic sealed class with permits
        // ────────────────────────────────────────────────────────────────────
        Cat cat = new Cat();
        Dog dog = new Dog();
        System.out.println("Cat sound: " + cat.makeSound());
        System.out.println("Dog sound: " + dog.makeSound());

        // ────────────────────────────────────────────────────────────────────
        // Sealed subclass (further restricted)
        // ────────────────────────────────────────────────────────────────────
        BengalCat bengal = new BengalCat();
        System.out.println("Bengal: " + bengal.makeSound());

        // ────────────────────────────────────────────────────────────────────
        // Non-sealed subclass (can be extended by anyone)
        // ────────────────────────────────────────────────────────────────────
        GoldenRetriever golden = new GoldenRetriever();
        System.out.println("Golden Retriever: " + golden.makeSound());

        // ────────────────────────────────────────────────────────────────────
        // Sealed class with omitted permits (same file)
        // ────────────────────────────────────────────────────────────────────
        Circle circle = new Circle();
        Rectangle rect = new Rectangle();
        System.out.println("\nCircle area: " + circle.area());
        System.out.println("Rectangle area: " + rect.area());

        // ────────────────────────────────────────────────────────────────────
        // Nested subclass with dot notation
        // ────────────────────────────────────────────────────────────────────
        Vehicle.Car car = new Vehicle.Car();
        Vehicle.Truck truck = new Vehicle.Truck();
        System.out.println("\nCar wheels: " + car.getWheels());
        System.out.println("Truck wheels: " + truck.getWheels());

        // ────────────────────────────────────────────────────────────────────
        // Sealed interface
        // ────────────────────────────────────────────────────────────────────
        Jsonable json = new User("Alice");
        Xmlable xml = new Product("Widget");
        System.out.println("\n" + json.toJson());
        System.out.println(xml.toXml());

        System.out.println("\n✓ All sealed class examples work");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// BASIC SEALED CLASS - With permits clause
// ═══════════════════════════════════════════════════════════════════════════

sealed class Animal permits Cat, Dog {
    String makeSound() {
        return "Generic animal sound";
    }
}

// Permitted subclass 1: sealed (further restricted)
sealed class Cat extends Animal permits BengalCat {
    @Override
    String makeSound() {
        return "Meow";
    }
}

// Permitted subclass 2: non-sealed (can be extended by anyone)
non-sealed class Dog extends Animal {
    @Override
    String makeSound() {
        return "Woof";
    }
}

// Cat is sealed, so only BengalCat can extend it
final class BengalCat extends Cat {
    @Override
    String makeSound() {
        return "Bengal meow";
    }
}

// Dog is non-sealed, so ANYONE can extend it
class GoldenRetriever extends Dog {
    @Override
    String makeSound() {
        return "Happy woof!";
    }
}

// EXAM TRAP: Must use one of three modifiers
// class BadCat extends Cat { }  // ✗ DOES NOT COMPILE - must be sealed, non-sealed, or final

// ═══════════════════════════════════════════════════════════════════════════
// OMITTING PERMITS CLAUSE - Same file
// ═══════════════════════════════════════════════════════════════════════════

// CRITICAL: Can omit permits when all subclasses in SAME FILE
sealed class Shape {  // No permits clause!
    double area() {
        return 0.0;
    }
}

// Compiler infers Circle and Rectangle are permitted
final class Circle extends Shape {
    @Override
    double area() {
        return 3.14 * 5 * 5;
    }
}

final class Rectangle extends Shape {
    @Override
    double area() {
        return 10.0 * 5.0;
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// NESTED SUBCLASSES - Dot notation in permits
// ═══════════════════════════════════════════════════════════════════════════

sealed class Vehicle permits Vehicle.Car, Vehicle.Truck {
    int getWheels() {
        return 0;
    }

    // Nested permitted subclass 1
    final static class Car extends Vehicle {
        @Override
        int getWheels() {
            return 4;
        }
    }

    // Nested permitted subclass 2
    final static class Truck extends Vehicle {
        @Override
        int getWheels() {
            return 18;
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// SEALED INTERFACE - permits can include classes AND interfaces
// ═══════════════════════════════════════════════════════════════════════════

// permits includes both a class (User) and an interface (Xmlable)
sealed interface Serializable permits User, Xmlable {
    String serialize();
}

// Class implementing sealed interface (must be final, sealed, or non-sealed)
final class User implements Serializable, Jsonable {
    private String name;

    User(String name) {
        this.name = name;
    }

    @Override
    public String serialize() {
        return toJson();
    }

    @Override
    public String toJson() {
        return "{\"name\":\"" + name + "\"}";
    }
}

// Interface extending sealed interface (must be final, sealed, or non-sealed)
// CRITICAL: Interfaces cannot be final, so must be sealed or non-sealed
non-sealed interface Xmlable extends Serializable {
    default String toXml() {
        return "<data>" + serialize() + "</data>";
    }
}

// Can implement Xmlable (it's non-sealed)
final class Product implements Xmlable {
    private String name;

    Product(String name) {
        this.name = name;
    }

    @Override
    public String serialize() {
        return name;
    }
}

// Helper interface
interface Jsonable {
    String toJson();
}

// ═══════════════════════════════════════════════════════════════════════════
// SEALED INTERFACE WITH MULTIPLE TYPES
// ═══════════════════════════════════════════════════════════════════════════

sealed interface Payment permits CreditCard, PayPalPayment, PaymentMethod {
    void process();
}

// Class implementing sealed interface
final class CreditCard implements Payment {
    @Override
    public void process() {
        System.out.println("Processing credit card");
    }
}

// Another class implementing sealed interface
final class PayPalPayment implements Payment {
    @Override
    public void process() {
        System.out.println("Processing PayPal");
    }
}

// Interface extending sealed interface
sealed interface PaymentMethod extends Payment permits BankTransfer {
    // Can add more methods
}

final class BankTransfer implements PaymentMethod {
    @Override
    public void process() {
        System.out.println("Processing bank transfer");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// EXAM TRAPS
// ═══════════════════════════════════════════════════════════════════════════

// TRAP 1: Subclass not in permits list
// sealed class Parent permits Child1 { }
// final class Child1 extends Parent { }
// final class Child2 extends Parent { }  // ✗ DOES NOT COMPILE - not in permits

// TRAP 2: Subclass missing required modifier
// sealed class Parent permits Child { }
// class Child extends Parent { }  // ✗ DOES NOT COMPILE - must be sealed, non-sealed, or final

// TRAP 3: Sealed class and subclass in different packages
// package a;
// public sealed class Parent permits b.Child { }  // ✗ DOES NOT COMPILE - different packages
//
// package b;
// public final class Child extends a.Parent { }

// TRAP 4: Interface marked final
// sealed interface Parent permits Child { }
// final interface Child extends Parent { }  // ✗ DOES NOT COMPILE - interfaces cannot be final

// TRAP 5: Forgetting dot notation for nested class
// sealed class Parent permits Nested { }  // ✗ DOES NOT COMPILE - should be Parent.Nested
// class Parent {
//     final static class Nested extends Parent { }
// }

// TRAP 6: Using permits when subclasses not in same file/package
// sealed class Parent permits Child { }  // ✗ If Child is in different file/package
