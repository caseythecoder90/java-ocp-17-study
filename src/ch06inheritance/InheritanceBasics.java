package ch06inheritance;

/**
 * INHERITANCE BASICS - Complete Guide for OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * INHERITANCE FUNDAMENTALS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Inheritance allows a class to inherit members (fields and methods) from
 * another class using the "extends" keyword.
 *
 * Syntax: class Child extends Parent { }
 *
 * KEY RULES:
 * 1. Java supports SINGLE inheritance (one parent class only)
 * 2. A class can implement multiple interfaces
 * 3. All classes inherit from java.lang.Object (directly or indirectly)
 * 4. Private members are NOT inherited
 * 5. Constructors are NOT inherited
 * 6. Static methods are NOT overridden (they are hidden)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CLASS MODIFIERS FOR INHERITANCE
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * FINAL CLASS:
 * - Cannot be extended
 * - No subclasses allowed
 * - Syntax: final class ClassName { }
 * - Example: String, Integer, Math are final classes
 *
 * ABSTRACT CLASS:
 * - Cannot be instantiated
 * - Can have abstract methods (no body)
 * - Can have concrete methods (with body)
 * - Subclasses must implement abstract methods or be abstract themselves
 * - Syntax: abstract class ClassName { }
 *
 * SEALED CLASS (Java 17+):
 * - Restricts which classes can extend it
 * - Must specify permitted subclasses
 * - Syntax: sealed class Parent permits Child1, Child2 { }
 *
 * NON-SEALED CLASS:
 * - Used with sealed classes
 * - Opens inheritance back up (any class can extend it)
 * - Syntax: non-sealed class Child extends SealedParent { }
 *
 * STATIC (nested classes):
 * - Static nested classes can be inherited
 * - Don't have access to outer class instance
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * METHOD MODIFIERS FOR INHERITANCE
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * FINAL METHOD:
 * - Cannot be overridden by subclasses
 * - Syntax: final void method() { }
 *
 * ABSTRACT METHOD:
 * - No method body (only in abstract classes or interfaces)
 * - Subclasses MUST implement it
 * - Syntax: abstract void method();
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * INHERITING FROM OBJECT
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Every class in Java inherits from java.lang.Object (if no explicit parent)
 *
 * Key methods inherited from Object:
 * - equals(Object obj)
 * - hashCode()
 * - toString()
 * - clone()
 * - finalize()
 * - getClass()
 * - notify(), notifyAll(), wait()
 *
 * These can be overridden in your class!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * SUPER KEYWORD
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Used to access parent class members from child class
 *
 * Uses:
 * 1. super.field - access parent field
 * 2. super.method() - call parent method
 * 3. super() - call parent constructor (must be first line)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * THIS KEYWORD WITH INHERITANCE
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Can also access parent class members, but:
 * - If child has same-named member, child version is used (child takes priority)
 * - Use super to explicitly access parent version
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * EXAM TRAPS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * 1. Cannot extend a final class
 * 2. Cannot override a final method
 * 3. Abstract classes can have constructors (even though can't be instantiated)
 * 4. Private methods are not inherited (cannot override)
 * 5. Static methods are hidden, not overridden
 * 6. Must use super() or this() as first line of constructor
 */
public class InheritanceBasics {

    public static void main(String[] args) {
        System.out.println("=== INHERITANCE BASICS ===\n");

        basicInheritance();
        finalModifier();
        abstractModifier();
        superKeyword();
        thisVsSuperWithFields();
        inheritingFromObject();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * BASIC INHERITANCE
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void basicInheritance() {
        System.out.println("=== BASIC INHERITANCE ===\n");

        System.out.println("Syntax: class Child extends Parent { }\n");

        Animal animal = new Animal();
        animal.eat();
        animal.sleep();

        System.out.println();

        Dog dog = new Dog();
        dog.eat();    // Inherited from Animal
        dog.sleep();  // Inherited from Animal
        dog.bark();   // Defined in Dog

        System.out.println("\n--- Key Points ---");
        System.out.println("• Dog inherits eat() and sleep() from Animal");
        System.out.println("• Dog adds its own bark() method");
        System.out.println("• Dog 'is-a' Animal (inheritance relationship)");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * FINAL MODIFIER - CRITICAL EXAM TOPIC
     * ═══════════════════════════════════════════════════════════════════════
     *
     * final class - Cannot be extended
     * final method - Cannot be overridden
     * final field - Cannot be reassigned
     */
    private static void finalModifier() {
        System.out.println("=== FINAL MODIFIER ===");
        System.out.println("*** CRITICAL EXAM TOPIC ***\n");

        System.out.println("--- final Class ---");
        System.out.println("final class FinalClass { }");
        System.out.println("class Child extends FinalClass { }  // ✗ DOES NOT COMPILE");
        System.out.println("→ Cannot extend a final class\n");

        System.out.println("Examples of final classes in Java:");
        System.out.println("  • String");
        System.out.println("  • Integer, Double, Long (wrapper classes)");
        System.out.println("  • Math");
        System.out.println("  → Cannot create subclasses of these!\n");

        System.out.println("--- final Method ---");
        System.out.println("class Parent {");
        System.out.println("    final void method() { }");
        System.out.println("}");
        System.out.println("\nclass Child extends Parent {");
        System.out.println("    void method() { }  // ✗ DOES NOT COMPILE");
        System.out.println("}");
        System.out.println("→ Cannot override a final method\n");

        // Demonstrate final method
        FinalMethodExample parent = new FinalMethodExample();
        parent.finalMethod();
        parent.nonFinalMethod();

        System.out.println();
        FinalMethodChild child = new FinalMethodChild();
        child.finalMethod();      // Inherited, cannot override
        child.nonFinalMethod();   // Overridden in child

        System.out.println("\n--- Why Use final? ---");
        System.out.println("• Security - prevent subclass from changing behavior");
        System.out.println("• Performance - compiler optimizations");
        System.out.println("• Design - communicate that class/method shouldn't be extended");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * ABSTRACT MODIFIER - VERY IMPORTANT EXAM TOPIC
     * ═══════════════════════════════════════════════════════════════════════
     *
     * abstract class - Cannot be instantiated, can have abstract methods
     * abstract method - No body, must be implemented by subclass
     */
    private static void abstractModifier() {
        System.out.println("=== ABSTRACT MODIFIER ===");
        System.out.println("*** VERY IMPORTANT EXAM TOPIC ***\n");

        System.out.println("--- abstract Class ---");
        System.out.println("abstract class Shape {");
        System.out.println("    abstract double getArea();  // No body");
        System.out.println("    void display() {            // Can have concrete methods");
        System.out.println("        System.out.println(\"Area: \" + getArea());");
        System.out.println("    }");
        System.out.println("}");

        System.out.println("\n// Shape shape = new Shape();  // ✗ DOES NOT COMPILE");
        System.out.println("→ Cannot instantiate abstract class\n");

        System.out.println("--- Concrete Subclass ---");
        System.out.println("class Circle extends Shape {");
        System.out.println("    private double radius;");
        System.out.println("    double getArea() {  // MUST implement abstract method");
        System.out.println("        return Math.PI * radius * radius;");
        System.out.println("    }");
        System.out.println("}");

        System.out.println("\nCircle circle = new Circle();  // ✓ COMPILES");
        System.out.println("→ Concrete class implements abstract method\n");

        // Demonstrate abstract class
        Circle circle = new Circle(5.0);
        circle.display();

        System.out.println("\n--- Abstract Rules ---");
        System.out.println("1. Abstract class can have 0 or more abstract methods");
        System.out.println("2. Abstract class CAN have concrete methods");
        System.out.println("3. Abstract class CAN have constructors");
        System.out.println("4. Subclass MUST implement all abstract methods OR be abstract");
        System.out.println("5. Cannot be both abstract and final (contradiction!)");

        System.out.println("\n--- EXAM TRAP ---");
        System.out.println("abstract final class Bad { }  // ✗ DOES NOT COMPILE");
        System.out.println("→ Cannot be both abstract (must extend) and final (cannot extend)");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * SUPER KEYWORD
     * ═══════════════════════════════════════════════════════════════════════
     *
     * Used to access parent class members
     */
    private static void superKeyword() {
        System.out.println("=== SUPER KEYWORD ===\n");

        System.out.println("Uses:");
        System.out.println("1. super.field - access parent field");
        System.out.println("2. super.method() - call parent method");
        System.out.println("3. super() - call parent constructor\n");

        SuperExample child = new SuperExample();
        child.printBoth();

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * THIS VS SUPER WITH FIELDS
     * ═══════════════════════════════════════════════════════════════════════
     *
     * CRITICAL EXAM CONCEPT:
     * - this accesses current class members (child if in child)
     * - this CAN access parent members if no name conflict
     * - If child and parent have same-named field, child takes priority with this
     * - Use super to explicitly access parent version
     */
    private static void thisVsSuperWithFields() {
        System.out.println("=== THIS VS SUPER WITH FIELDS ===");
        System.out.println("*** CRITICAL EXAM CONCEPT ***\n");

        System.out.println("RULE: this defaults to child if names overlap\n");

        FieldExample child = new FieldExample();
        child.printFields();

        System.out.println("\n--- Key Insight ---");
        System.out.println("• this.name → Child's name (child takes priority)");
        System.out.println("• super.name → Parent's name (explicit parent access)");
        System.out.println("• this.age → Parent's age (no conflict, accesses parent)");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * INHERITING FROM OBJECT
     * ═══════════════════════════════════════════════════════════════════════
     *
     * Every class inherits from java.lang.Object
     */
    private static void inheritingFromObject() {
        System.out.println("=== INHERITING FROM OBJECT ===\n");

        System.out.println("Every class inherits from java.lang.Object\n");

        System.out.println("Implicit inheritance:");
        System.out.println("  class MyClass { }");
        System.out.println("  // Same as:");
        System.out.println("  class MyClass extends Object { }\n");

        System.out.println("Key methods inherited from Object:");
        System.out.println("  • toString()    - string representation");
        System.out.println("  • equals()      - equality comparison");
        System.out.println("  • hashCode()    - hash code for collections");
        System.out.println("  • clone()       - create copy");
        System.out.println("  • getClass()    - get Class object");
        System.out.println("  • finalize()    - called before garbage collection (deprecated)");

        System.out.println("\n--- Demonstration ---");
        ObjectExample obj = new ObjectExample("Test");
        System.out.println("toString(): " + obj.toString());
        System.out.println("getClass(): " + obj.getClass());
        System.out.println("hashCode(): " + obj.hashCode());

        ObjectExample obj2 = new ObjectExample("Test");
        System.out.println("equals(same content): " + obj.equals(obj2));

        System.out.println("\n--- Inheritance Chain ---");
        System.out.println("Dog → Animal → Object");
        System.out.println("Every class eventually inherits from Object!");

        System.out.println();
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// EXAMPLE CLASSES
// ═══════════════════════════════════════════════════════════════════════════

class Animal {
    public void eat() {
        System.out.println("Animal is eating");
    }

    public void sleep() {
        System.out.println("Animal is sleeping");
    }
}

class Dog extends Animal {
    public void bark() {
        System.out.println("Dog is barking");
    }
}

// ───────────────────────────────────────────────────────────────────────────
// FINAL EXAMPLES
// ───────────────────────────────────────────────────────────────────────────

class FinalMethodExample {
    final void finalMethod() {
        System.out.println("This is a final method (cannot be overridden)");
    }

    void nonFinalMethod() {
        System.out.println("This is a non-final method (can be overridden)");
    }
}

class FinalMethodChild extends FinalMethodExample {
    // Cannot override finalMethod() - it's final!

    @Override
    void nonFinalMethod() {
        System.out.println("Child overrode the non-final method");
    }
}

// Example: Cannot extend final class
// final class FinalClass { }
// class CannotExtend extends FinalClass { }  // DOES NOT COMPILE

// ───────────────────────────────────────────────────────────────────────────
// ABSTRACT EXAMPLES
// ───────────────────────────────────────────────────────────────────────────

abstract class Shape {
    abstract double getArea();

    void display() {
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

// ───────────────────────────────────────────────────────────────────────────
// SUPER KEYWORD EXAMPLES
// ───────────────────────────────────────────────────────────────────────────

class Parent {
    void printMessage() {
        System.out.println("Message from Parent");
    }
}

class SuperExample extends Parent {
    @Override
    void printMessage() {
        System.out.println("Message from Child");
    }

    void printBoth() {
        this.printMessage();    // Calls child version
        super.printMessage();   // Calls parent version
    }
}

// ───────────────────────────────────────────────────────────────────────────
// THIS VS SUPER WITH FIELDS
// ───────────────────────────────────────────────────────────────────────────

class ParentWithFields {
    String name = "Parent Name";
    int age = 50;
}

class FieldExample extends ParentWithFields {
    String name = "Child Name";  // Shadows parent's name

    void printFields() {
        System.out.println("this.name: " + this.name);      // Child's name
        System.out.println("super.name: " + super.name);    // Parent's name
        System.out.println("this.age: " + this.age);        // Parent's age (no conflict)
    }
}

// ───────────────────────────────────────────────────────────────────────────
// INHERITING FROM OBJECT
// ───────────────────────────────────────────────────────────────────────────

class ObjectExample {
    private String value;

    ObjectExample(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ObjectExample[value=" + value + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ObjectExample)) return false;
        ObjectExample other = (ObjectExample) obj;
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
