package ch06inheritance;

/**
 * ABSTRACT CLASSES - Complete Guide for OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * WHAT ARE ABSTRACT CLASSES?
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Abstract classes are declared with the abstract modifier and CANNOT be
 * instantiated directly.
 *
 * Purpose: Use when you don't want a class to be directly instantiated.
 *
 * Abstract classes can contain:
 * - Abstract methods (no body, must be overridden)
 * - Concrete methods (with body)
 * - Variables (static and instance)
 * - Constructors
 * - Static methods
 * - All other class members
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * ABSTRACT METHODS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * An abstract method is a method declared with the abstract modifier that
 * does NOT define a body.
 *
 * Syntax: abstract void methodName();
 *
 * Purpose: Forces subclasses to override the method.
 *
 * WHY USE ABSTRACT METHODS?
 * Polymorphism! We can guarantee that some version of the method will be
 * available on an instance without having to specify what that version is
 * in the abstract parent class.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * KEY RULES FOR ABSTRACT CLASSES AND METHODS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * 1. Only INSTANCE methods can be marked abstract within a class
 *    (cannot have abstract static methods)
 *    Why? Static methods can only be HIDDEN, not overridden.
 *    Abstract requires overriding.
 *
 * 2. Methods CANNOT be both abstract and private
 *    Why? Private methods are not inherited.
 *    Abstract requires subclass to override (inherit).
 *
 * 3. An abstract method can ONLY be declared in an abstract class
 *    (regular class cannot have abstract method)
 *
 * 4. A non-abstract (concrete) class that extends an abstract class MUST
 *    implement ALL inherited abstract methods
 *
 * 5. Overriding an abstract method follows ALL normal rules for method
 *    overriding (access modifiers, exceptions, return types, etc.)
 *
 * 6. Abstract classes can have constructors (even though cannot be instantiated)
 *
 * 7. Abstract classes can be initialized, but ONLY as part of the instantiation
 *    of a non-abstract subclass
 *
 * 8. Classes and methods CANNOT be both abstract and final
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * WHEN TO USE ABSTRACT CLASSES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Most commonly used when you want another class to inherit some properties
 * of a particular class, but you want the SUBCLASS to fill in some of the
 * implementation details.
 *
 * Example: Shape class with abstract getArea() method
 * - All shapes have an area
 * - Each specific shape calculates area differently
 * - We don't want to instantiate generic "Shape"
 * - We want to instantiate Circle, Rectangle, etc.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * ABSTRACT VS CONCRETE CLASSES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * CONCRETE CLASS:
 * - Non-abstract class
 * - Can be instantiated
 * - Must implement all inherited abstract methods
 *
 * ABSTRACT CLASS:
 * - Declared with abstract modifier
 * - Cannot be instantiated directly
 * - May have abstract methods (or may have none)
 * - May have concrete methods
 *
 * IMPORTANT: The first concrete subclass that extends an abstract class is
 * required to implement ALL inherited abstract methods (including from any
 * interfaces it is implementing).
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CONSTRUCTORS IN ABSTRACT CLASSES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Abstract classes CAN have constructors even though they cannot be
 * instantiated directly.
 *
 * When a concrete subclass is instantiated, any constructor in the abstract
 * class is invoked following the normal constructor and instantiation rules
 * (super() is called first).
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * ABSTRACT AND FINAL - CONTRADICTION!
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Classes and methods CANNOT be both abstract and final.
 *
 * Why?
 * - abstract = must be extended/overridden
 * - final = cannot be extended/overridden
 * - These are contradictory!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * EXAM TRAPS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * 1. Cannot instantiate abstract class directly
 * 2. Abstract method cannot have a body
 * 3. Concrete class must implement ALL abstract methods
 * 4. Cannot be both abstract and final
 * 5. Cannot have abstract static methods (static = hidden, not overridden)
 * 6. Cannot have abstract private methods (private = not inherited)
 * 7. Abstract method must be in abstract class
 * 8. Abstract class CAN have concrete methods
 * 9. Abstract class CAN have constructors
 * 10. Abstract class does NOT need to have any abstract methods
 */
public class AbstractClasses {

    public static void main(String[] args) {
        System.out.println("=== ABSTRACT CLASSES ===\n");

        basicAbstractClass();
        abstractMethods();
        concreteImplementation();
        polymorphismWithAbstract();
        constructorsInAbstract();
        abstractWithConcreteMembers();
        multilevelInheritance();
        partialImplementation();
        abstractAndFinal();
        examTraps();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * BASIC ABSTRACT CLASS
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void basicAbstractClass() {
        System.out.println("=== BASIC ABSTRACT CLASS ===");
        System.out.println("*** Abstract classes CANNOT be instantiated ***\n");

        System.out.println("--- Abstract Class Declaration ---");
        System.out.println("abstract class Animal {");
        System.out.println("    abstract void makeSound();  // No body");
        System.out.println("}\n");

        System.out.println("--- Attempting to Instantiate ✗ ---");
        System.out.println("Animal animal = new Animal();  // ✗ DOES NOT COMPILE");
        System.out.println("→ Cannot instantiate abstract class\n");

        System.out.println("--- Valid: Instantiate Concrete Subclass ✓ ---");
        System.out.println("class Dog extends Animal {");
        System.out.println("    void makeSound() { System.out.println(\"Woof\"); }");
        System.out.println("}");
        System.out.println("\nAnimal dog = new Dog();  // ✓ Valid");
        System.out.println("dog.makeSound();  // Calls Dog's implementation\n");

        System.out.println("--- Why Use Abstract Classes? ---");
        System.out.println("• Prevent direct instantiation of base class");
        System.out.println("• Force subclasses to implement specific methods");
        System.out.println("• Share common code among subclasses");
        System.out.println("• Enable polymorphism with guaranteed methods");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * ABSTRACT METHODS
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void abstractMethods() {
        System.out.println("=== ABSTRACT METHODS ===");
        System.out.println("*** Methods with no body that must be overridden ***\n");

        System.out.println("Syntax: abstract returnType methodName();\n");

        System.out.println("--- Valid Abstract Method ✓ ---");
        System.out.println("abstract class Shape {");
        System.out.println("    abstract double getArea();  // No body, ends with ;");
        System.out.println("}\n");

        System.out.println("--- Invalid: Abstract Method with Body ✗ ---");
        System.out.println("abstract class Bad {");
        System.out.println("    abstract void method() { }  // ✗ DOES NOT COMPILE");
        System.out.println("    // Abstract methods cannot have a body");
        System.out.println("}\n");

        System.out.println("--- Invalid: Abstract Method in Concrete Class ✗ ---");
        System.out.println("class Bad {  // Not abstract");
        System.out.println("    abstract void method();  // ✗ DOES NOT COMPILE");
        System.out.println("    // Abstract method must be in abstract class");
        System.out.println("}\n");

        System.out.println("--- Invalid: Abstract Static Method ✗ ---");
        System.out.println("abstract class Bad {");
        System.out.println("    abstract static void method();  // ✗ DOES NOT COMPILE");
        System.out.println("    // Only INSTANCE methods can be abstract");
        System.out.println("    // Static methods can only be HIDDEN, not overridden");
        System.out.println("}\n");

        System.out.println("--- Invalid: Abstract Private Method ✗ ---");
        System.out.println("abstract class Bad {");
        System.out.println("    abstract private void method();  // ✗ DOES NOT COMPILE");
        System.out.println("    // Private methods are NOT inherited");
        System.out.println("    // Cannot override something that's not inherited");
        System.out.println("}\n");

        System.out.println("--- Why Abstract Methods? ---");
        System.out.println("Polymorphism! Guarantee method exists without specifying");
        System.out.println("the implementation in the abstract class.\n");

        System.out.println("Example:");
        System.out.println("List<Shape> shapes = Arrays.asList(");
        System.out.println("    new Circle(5), new Rectangle(4, 6)");
        System.out.println(");");
        System.out.println("for (Shape shape : shapes) {");
        System.out.println("    shape.getArea();  // Guaranteed to exist!");
        System.out.println("}");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * CONCRETE IMPLEMENTATION
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void concreteImplementation() {
        System.out.println("=== CONCRETE IMPLEMENTATION ===");
        System.out.println("*** First concrete class must implement ALL abstract methods ***\n");

        System.out.println("Rule: Non-abstract (concrete) class extending abstract class");
        System.out.println("      must implement ALL inherited abstract methods.\n");

        System.out.println("--- Abstract Class ---");
        System.out.println("abstract class Shape {");
        System.out.println("    abstract double getArea();");
        System.out.println("    abstract double getPerimeter();");
        System.out.println("}\n");

        System.out.println("--- Invalid: Missing Implementation ✗ ---");
        System.out.println("class Circle extends Shape {");
        System.out.println("    double getArea() { return 0; }");
        System.out.println("    // ✗ DOES NOT COMPILE - getPerimeter() not implemented");
        System.out.println("}\n");

        System.out.println("--- Valid: All Methods Implemented ✓ ---");
        System.out.println("class Circle extends Shape {");
        System.out.println("    double getArea() { return 0; }");
        System.out.println("    double getPerimeter() { return 0; }");
        System.out.println("    // ✓ All abstract methods implemented");
        System.out.println("}\n");

        System.out.println("--- Demonstration ---");
        ConcreteCircle circle = new ConcreteCircle(5.0);
        System.out.println("Circle area: " + circle.getArea());
        System.out.println("Circle perimeter: " + circle.getPerimeter());

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * POLYMORPHISM WITH ABSTRACT CLASSES
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void polymorphismWithAbstract() {
        System.out.println("=== POLYMORPHISM WITH ABSTRACT CLASSES ===");
        System.out.println("*** The power of abstract classes! ***\n");

        System.out.println("We can use abstract class as reference type:\n");

        AbstractShape circle = new ConcreteCircle(5.0);
        AbstractShape rectangle = new ConcreteRectangle(4.0, 6.0);

        System.out.println("Circle area: " + circle.getArea());
        System.out.println("Circle perimeter: " + circle.getPerimeter());
        System.out.println();
        System.out.println("Rectangle area: " + rectangle.getArea());
        System.out.println("Rectangle perimeter: " + rectangle.getPerimeter());

        System.out.println("\n--- Key Insight ---");
        System.out.println("We can call getArea() and getPerimeter() on AbstractShape");
        System.out.println("reference without knowing the specific shape type!");
        System.out.println("This is polymorphism in action.");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * CONSTRUCTORS IN ABSTRACT CLASSES
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void constructorsInAbstract() {
        System.out.println("=== CONSTRUCTORS IN ABSTRACT CLASSES ===");
        System.out.println("*** Abstract classes CAN have constructors! ***\n");

        System.out.println("Even though you cannot instantiate an abstract class,");
        System.out.println("it can have constructors that are called when a concrete");
        System.out.println("subclass is instantiated.\n");

        System.out.println("--- Example ---");
        System.out.println("abstract class Animal {");
        System.out.println("    private String name;");
        System.out.println("    ");
        System.out.println("    public Animal(String name) {");
        System.out.println("        this.name = name;");
        System.out.println("        System.out.println(\"Animal constructor: \" + name);");
        System.out.println("    }");
        System.out.println("}");
        System.out.println();
        System.out.println("class Dog extends Animal {");
        System.out.println("    public Dog(String name) {");
        System.out.println("        super(name);  // Calls abstract class constructor");
        System.out.println("        System.out.println(\"Dog constructor\");");
        System.out.println("    }");
        System.out.println("}\n");

        System.out.println("--- Demonstration ---");
        AbstractAnimal dog = new ConcreteDog("Buddy");

        System.out.println("\n--- Why This Matters ---");
        System.out.println("Abstract class constructors allow initialization of");
        System.out.println("inherited fields and execution of common setup code.");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * ABSTRACT CLASSES WITH CONCRETE MEMBERS
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void abstractWithConcreteMembers() {
        System.out.println("=== ABSTRACT CLASSES WITH CONCRETE MEMBERS ===");
        System.out.println("*** Abstract classes can have concrete methods! ***\n");

        System.out.println("Abstract classes can contain:");
        System.out.println("  • Abstract methods (no body)");
        System.out.println("  • Concrete methods (with body)");
        System.out.println("  • Instance variables");
        System.out.println("  • Static variables");
        System.out.println("  • Static methods");
        System.out.println("  • Constructors");
        System.out.println("  • Instance initializers");
        System.out.println("  • Static initializers\n");

        System.out.println("--- Example ---");
        System.out.println("abstract class Animal {");
        System.out.println("    private String name;  // Instance variable");
        System.out.println("    ");
        System.out.println("    abstract void makeSound();  // Abstract method");
        System.out.println("    ");
        System.out.println("    void sleep() {  // Concrete method");
        System.out.println("        System.out.println(name + \" is sleeping\");");
        System.out.println("    }");
        System.out.println("    ");
        System.out.println("    static void info() {  // Static method");
        System.out.println("        System.out.println(\"All animals need food\");");
        System.out.println("    }");
        System.out.println("}\n");

        System.out.println("--- Demonstration ---");
        MixedAbstractAnimal cat = new ConcreteCat("Whiskers");
        cat.makeSound();    // Abstract method, implemented in subclass
        cat.sleep();        // Concrete method, inherited from abstract class
        cat.eat();          // Concrete method, inherited from abstract class
        MixedAbstractAnimal.info();  // Static method

        System.out.println("\n--- Key Point ---");
        System.out.println("Abstract class does NOT need to have abstract methods!");
        System.out.println("An abstract class with no abstract methods is valid");
        System.out.println("(just prevents instantiation).");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * MULTILEVEL INHERITANCE
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void multilevelInheritance() {
        System.out.println("=== MULTILEVEL INHERITANCE ===");
        System.out.println("*** Abstract → Abstract → Concrete ***\n");

        System.out.println("You can have multiple levels of abstract classes:\n");

        System.out.println("abstract class Animal {");
        System.out.println("    abstract void eat();");
        System.out.println("}");
        System.out.println();
        System.out.println("abstract class Mammal extends Animal {");
        System.out.println("    abstract void giveBirth();");
        System.out.println("    // eat() still not implemented");
        System.out.println("}");
        System.out.println();
        System.out.println("class Dog extends Mammal {");
        System.out.println("    void eat() { }        // Must implement");
        System.out.println("    void giveBirth() { }  // Must implement");
        System.out.println("}\n");

        System.out.println("--- Key Rule ---");
        System.out.println("First CONCRETE class must implement ALL abstract methods");
        System.out.println("from the entire inheritance chain.\n");

        System.out.println("--- Invalid Example ✗ ---");
        System.out.println("class Dog extends Mammal {");
        System.out.println("    void eat() { }");
        System.out.println("    // ✗ DOES NOT COMPILE - giveBirth() not implemented");
        System.out.println("}");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * PARTIAL IMPLEMENTATION
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void partialImplementation() {
        System.out.println("=== PARTIAL IMPLEMENTATION ===");
        System.out.println("*** Abstract class can implement some methods ***\n");

        System.out.println("Abstract class in the middle can implement SOME abstract");
        System.out.println("methods and leave others for concrete subclass:\n");

        System.out.println("abstract class Animal {");
        System.out.println("    abstract void eat();");
        System.out.println("    abstract void sleep();");
        System.out.println("    abstract void makeSound();");
        System.out.println("}");
        System.out.println();
        System.out.println("abstract class Mammal extends Animal {");
        System.out.println("    void sleep() {  // Implements one method");
        System.out.println("        System.out.println(\"Sleeping...\");");
        System.out.println("    }");
        System.out.println("    // eat() and makeSound() still abstract");
        System.out.println("}");
        System.out.println();
        System.out.println("class Dog extends Mammal {");
        System.out.println("    void eat() { }        // Must implement");
        System.out.println("    void makeSound() { }  // Must implement");
        System.out.println("    // sleep() already implemented by Mammal");
        System.out.println("}\n");

        System.out.println("--- Demonstration ---");
        PartialAnimal bird = new ConcreteBird();
        bird.eat();
        bird.sleep();      // Inherited from PartialMammal
        bird.makeSound();

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * ABSTRACT AND FINAL - CONTRADICTION
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void abstractAndFinal() {
        System.out.println("=== ABSTRACT AND FINAL ===");
        System.out.println("*** CANNOT be both abstract and final! ***\n");

        System.out.println("Why the contradiction?\n");

        System.out.println("abstract = Must be extended/overridden");
        System.out.println("final = Cannot be extended/overridden\n");

        System.out.println("These are mutually exclusive!\n");

        System.out.println("--- Invalid: abstract final class ✗ ---");
        System.out.println("abstract final class Bad { }  // ✗ DOES NOT COMPILE");
        System.out.println("// Cannot be both abstract (must extend) and final (cannot extend)\n");

        System.out.println("--- Invalid: abstract final method ✗ ---");
        System.out.println("abstract class Bad {");
        System.out.println("    abstract final void method();  // ✗ DOES NOT COMPILE");
        System.out.println("    // Cannot be both abstract (must override) and final (cannot override)");
        System.out.println("}\n");

        System.out.println("--- Valid: abstract class with final method ✓ ---");
        System.out.println("abstract class Good {");
        System.out.println("    abstract void abstractMethod();  // Must override");
        System.out.println("    final void finalMethod() { }     // Cannot override");
        System.out.println("    // ✓ The CLASS is abstract, but one METHOD is final");
        System.out.println("}");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * COMMON EXAM TRAPS
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void examTraps() {
        System.out.println("=== COMMON EXAM TRAPS ===\n");

        System.out.println("--- TRAP 1: Instantiating Abstract Class ✗ ---");
        System.out.println("abstract class Animal { }");
        System.out.println("Animal a = new Animal();  // ✗ DOES NOT COMPILE\n");

        System.out.println("--- TRAP 2: Abstract Method with Body ✗ ---");
        System.out.println("abstract class Bad {");
        System.out.println("    abstract void method() { }  // ✗ DOES NOT COMPILE");
        System.out.println("}\n");

        System.out.println("--- TRAP 3: Abstract Method in Concrete Class ✗ ---");
        System.out.println("class Bad {");
        System.out.println("    abstract void method();  // ✗ DOES NOT COMPILE");
        System.out.println("}\n");

        System.out.println("--- TRAP 4: Not Implementing All Abstract Methods ✗ ---");
        System.out.println("abstract class Animal {");
        System.out.println("    abstract void eat();");
        System.out.println("    abstract void sleep();");
        System.out.println("}");
        System.out.println("class Dog extends Animal {");
        System.out.println("    void eat() { }");
        System.out.println("    // ✗ DOES NOT COMPILE - sleep() not implemented");
        System.out.println("}\n");

        System.out.println("--- TRAP 5: abstract static method ✗ ---");
        System.out.println("abstract class Bad {");
        System.out.println("    abstract static void method();  // ✗ DOES NOT COMPILE");
        System.out.println("    // Static methods can only be hidden, not overridden");
        System.out.println("}\n");

        System.out.println("--- TRAP 5b: abstract private method ✗ ---");
        System.out.println("abstract class Bad {");
        System.out.println("    abstract private void method();  // ✗ DOES NOT COMPILE");
        System.out.println("    // Private methods are not inherited");
        System.out.println("}\n");

        System.out.println("--- TRAP 6: abstract final ✗ ---");
        System.out.println("abstract final class Bad { }  // ✗ DOES NOT COMPILE\n");

        System.out.println("abstract class Bad {");
        System.out.println("    abstract final void method();  // ✗ DOES NOT COMPILE");
        System.out.println("}\n");

        System.out.println("--- TRAP 7: Thinking Abstract Class Needs Abstract Methods ✗ ---");
        System.out.println("abstract class Valid {  // ✓ Valid!");
        System.out.println("    void concreteMethod() { }");
        System.out.println("    // No abstract methods - still valid!");
        System.out.println("}\n");

        System.out.println("--- TRAP 8: Forgetting Override Rules Apply ---");
        System.out.println("abstract class Parent {");
        System.out.println("    abstract void method() throws IOException;");
        System.out.println("}");
        System.out.println("class Child extends Parent {");
        System.out.println("    void method() throws Exception { }  // ✗ DOES NOT COMPILE");
        System.out.println("    // Cannot throw broader exception when overriding");
        System.out.println("}\n");

        System.out.println("*** EXAM CHECKLIST FOR ABSTRACT CLASSES ***");
        System.out.println("1. ✓ Cannot instantiate abstract class");
        System.out.println("2. ✓ Abstract methods have no body");
        System.out.println("3. ✓ Abstract methods only in abstract classes");
        System.out.println("4. ✓ Concrete class must implement ALL abstract methods");
        System.out.println("5. ✓ Cannot be both abstract and final");
        System.out.println("6. ✓ Cannot have abstract static methods (static = hidden, not overridden)");
        System.out.println("7. ✓ Cannot have abstract private methods (private = not inherited)");
        System.out.println("8. ✓ Abstract classes CAN have constructors");
        System.out.println("9. ✓ Abstract classes CAN have concrete methods");
        System.out.println("10. ✓ Override rules apply when implementing abstract methods");

        System.out.println();
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// EXAMPLE CLASSES
// ═══════════════════════════════════════════════════════════════════════════

abstract class AbstractShape {
    abstract double getArea();
    abstract double getPerimeter();
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
    private double length;
    private double width;

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

// ───────────────────────────────────────────────────────────────────────────
// CONSTRUCTOR EXAMPLE
// ───────────────────────────────────────────────────────────────────────────

abstract class AbstractAnimal {
    private String name;

    AbstractAnimal(String name) {
        this.name = name;
        System.out.println("AbstractAnimal constructor: " + name);
    }

    abstract void makeSound();

    String getName() {
        return name;
    }
}

class ConcreteDog extends AbstractAnimal {
    ConcreteDog(String name) {
        super(name);
        System.out.println("ConcreteDog constructor");
    }

    @Override
    void makeSound() {
        System.out.println(getName() + " says: Woof!");
    }
}

// ───────────────────────────────────────────────────────────────────────────
// MIXED ABSTRACT AND CONCRETE MEMBERS
// ───────────────────────────────────────────────────────────────────────────

abstract class MixedAbstractAnimal {
    private String name;

    MixedAbstractAnimal(String name) {
        this.name = name;
    }

    abstract void makeSound();  // Abstract method

    void sleep() {  // Concrete method
        System.out.println(name + " is sleeping");
    }

    void eat() {  // Concrete method
        System.out.println(name + " is eating");
    }

    static void info() {  // Static method
        System.out.println("All animals need food and water");
    }
}

class ConcreteCat extends MixedAbstractAnimal {
    ConcreteCat(String name) {
        super(name);
    }

    @Override
    void makeSound() {
        System.out.println("Meow!");
    }
}

// ───────────────────────────────────────────────────────────────────────────
// PARTIAL IMPLEMENTATION EXAMPLE
// ───────────────────────────────────────────────────────────────────────────

abstract class PartialAnimal {
    abstract void eat();
    abstract void sleep();
    abstract void makeSound();
}

abstract class PartialMammal extends PartialAnimal {
    @Override
    void sleep() {  // Implements one abstract method
        System.out.println("Sleeping 8 hours...");
    }
    // eat() and makeSound() still abstract
}

class ConcreteBird extends PartialAnimal {
    @Override
    void eat() {
        System.out.println("Eating seeds");
    }

    @Override
    void sleep() {
        System.out.println("Sleeping on branch");
    }

    @Override
    void makeSound() {
        System.out.println("Chirp chirp!");
    }
}
