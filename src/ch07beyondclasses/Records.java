package ch07beyondclasses;

import java.util.ArrayList;

/**
 * ENCAPSULATION AND RECORDS - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * ENCAPSULATION
 * ═══════════════════════════════════════════════════════════════════════════
 * Technique of protecting data by:
 * - Making instance variables private
 * - Providing public getter/setter methods
 * - Controlling access and validation
 *
 * Benefits: Data hiding, validation, flexibility to change implementation
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * RECORDS - Immutable Data Carriers
 * ═══════════════════════════════════════════════════════════════════════════
 * record keyword: Declares immutable data carrier
 *
 * KEY FEATURES:
 * - Implicitly final (cannot be extended)
 * - Automatic constructor with fields in declaration order
 * - Accessor methods: fieldName() not getFieldName()
 * - Auto-generates: equals(), hashCode(), toString()
 * - All fields are inherently final
 * - Cannot be extended
 *
 * INTERFACES:
 * - Can implement regular or sealed interfaces
 * - Must implement all abstract methods
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * RECORD CONSTRUCTORS
 * ═══════════════════════════════════════════════════════════════════════════
 * LONG CONSTRUCTOR:
 * - The constructor compiler normally generates
 * - Can declare explicitly for validation
 * - MUST set every field
 *
 * record Person(String name, int age) {
 *     public Person(String name, int age) {  // Long constructor
 *         if (age < 0) throw new IllegalArgumentException();
 *         this.name = name;   // Must set every field
 *         this.age = age;
 *     }
 * }
 *
 * COMPACT CONSTRUCTOR (Java 16+):
 * - Special type for records
 * - Takes NO parameters
 * - Implicitly sets all fields (long constructor called at end)
 * - Apply transformations BEFORE object created
 * - Can modify PARAMETERS, NOT fields
 * - Compact constructor access level cannot be more restrictive than the record access level ('public')
 *
 * record Person(String name, int age) {
 *     public Person {  // Compact constructor
 *         if (age < 0) age = 0;  // Modifying parameter
 *         // this.age = 0;  // ✗ Cannot modify fields
 *     }  // Long constructor implicitly called
 * }
 *
 * OVERLOADED CONSTRUCTORS:
 * - Defeats purpose but allowed
 * - First line MUST be explicit this() call
 * - Can only transform data on first line
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * WHAT'S ALLOWED IN RECORDS
 * ═══════════════════════════════════════════════════════════════════════════
 * ✓ Instance methods
 * ✓ Override provided methods (equals, hashCode, toString, accessors)
 * ✓ Nested classes, interfaces, annotations, enums, records
 * ✓ Static fields and methods
 * ✓ Other data types
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * WHAT'S NOT ALLOWED IN RECORDS
 * ═══════════════════════════════════════════════════════════════════════════
 * ✗ Instance fields outside record declaration (even private)
 * ✗ Instance initializers
 * ✗ Extending records
 */
public class Records {

    public static void main(String[] args) {
        System.out.println("=== ENCAPSULATION ===\n");

        // ────────────────────────────────────────────────────────────────────
        // Encapsulation with traditional class
        // ────────────────────────────────────────────────────────────────────
        EncapsulatedPerson person = new EncapsulatedPerson("Alice", 30);
        System.out.println("Name: " + person.getName());
        System.out.println("Age: " + person.getAge());

        person.setAge(31);
        System.out.println("New age: " + person.getAge());

        // person.setAge(-5);  // Throws IllegalArgumentException

        System.out.println("\n=== BASIC RECORDS ===\n");

        // ────────────────────────────────────────────────────────────────────
        // Simple record - automatic constructor
        // ────────────────────────────────────────────────────────────────────
        Point p1 = new Point(10, 20);
        System.out.println("Point: " + p1);
        System.out.println("x: " + p1.x());  // Accessor: x() not getX()
        System.out.println("y: " + p1.y());

        // ────────────────────────────────────────────────────────────────────
        // Auto-generated equals and hashCode
        // ────────────────────────────────────────────────────────────────────
        Point p2 = new Point(10, 20);
        System.out.println("p1.equals(p2): " + p1.equals(p2));  // true
        System.out.println("p1 == p2: " + (p1 == p2));          // false

        // ────────────────────────────────────────────────────────────────────
        // Records with interfaces
        // ────────────────────────────────────────────────────────────────────
        SerializableRecord userRec = new UserRecord("Bob", 25);
        System.out.println("\nSerialized: " + userRec.serialize());

        System.out.println("\n=== RECORD CONSTRUCTORS ===\n");

        // ────────────────────────────────────────────────────────────────────
        // Long constructor with validation
        // ────────────────────────────────────────────────────────────────────
        ValidatedPerson v1 = new ValidatedPerson("Charlie", 40);
        System.out.println("Validated: " + v1);

        try {
            new ValidatedPerson("Dave", -10);  // Throws exception
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        // ────────────────────────────────────────────────────────────────────
        // Compact constructor with transformation
        // ────────────────────────────────────────────────────────────────────
        CompactPerson c1 = new CompactPerson("  eve  ", -5);
        System.out.println("\nCompact: " + c1);  // Name trimmed, age normalized

        CompactPerson c2 = new CompactPerson("Frank", 50);
        System.out.println("Compact: " + c2);

        // ────────────────────────────────────────────────────────────────────
        // Overloaded constructors
        // ────────────────────────────────────────────────────────────────────
        OverloadedPerson o1 = new OverloadedPerson("Grace");  // Uses overload
        System.out.println("\nOverloaded: " + o1);

        OverloadedPerson o2 = new OverloadedPerson("Henry", 35);
        System.out.println("Overloaded: " + o2);

        System.out.println("\n=== RECORD FEATURES ===\n");

        // ────────────────────────────────────────────────────────────────────
        // Instance methods and overriding
        // ────────────────────────────────────────────────────────────────────
        Employee emp = new Employee("Ivy", 50000);
        System.out.println("Employee: " + emp);
        System.out.println("Formatted: " + emp.format());
        System.out.println("Salary accessor: " + emp.salary());

        // ────────────────────────────────────────────────────────────────────
        // Static fields and methods
        // ────────────────────────────────────────────────────────────────────
        ProductRecord p = new ProductRecord("Widget", 19.99);
        System.out.println("\nProduct: " + p);
        System.out.println("Tax rate: " + ProductRecord.getTaxRate());

        // ────────────────────────────────────────────────────────────────────
        // Nested types in records
        // ────────────────────────────────────────────────────────────────────
        Container c = new Container("Data");
        Container.NestedRecord nested = new Container.NestedRecord(100);
        System.out.println("\nNested: " + nested);

        System.out.println("\n✓ All record examples work");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// ENCAPSULATION - Traditional approach
// ═══════════════════════════════════════════════════════════════════════════

class EncapsulatedPerson {
    private String name;  // Private fields
    private int age;

    public EncapsulatedPerson(String name, int age) {
        this.name = name;
        setAge(age);  // Use setter for validation
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    // Setter with validation
    public void setAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
        this.age = age;
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// BASIC RECORD - Minimal syntax
// ═══════════════════════════════════════════════════════════════════════════

record Point(int x, int y) {
    // Compiler generates:
    // - Constructor: public Point(int x, int y)
    // - Accessors: public int x(), public int y()
    // - equals(), hashCode(), toString()
}

// Same as writing:
// final class Point {
//     private final int x;
//     private final int y;
//
//     public Point(int x, int y) {
//         this.x = x;
//         this.y = y;
//     }
//
//     public int x() { return x; }
//     public int y() { return y; }
//
//     @Override
//     public boolean equals(Object o) { ... }
//     @Override
//     public int hashCode() { ... }
//     @Override
//     public String toString() { ... }
// }

// ═══════════════════════════════════════════════════════════════════════════
// RECORDS WITH INTERFACES
// ═══════════════════════════════════════════════════════════════════════════

interface SerializableRecord {
    String serialize();
}

record UserRecord(String name, int age) implements SerializableRecord {
    @Override
    public String serialize() {
        return name + "," + age;
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// LONG CONSTRUCTOR - Explicit validation
// ═══════════════════════════════════════════════════════════════════════════

record ValidatedPerson(String name, int age) {
    // Long constructor - must set EVERY field
    public ValidatedPerson(String name, int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        // MUST set every field
        this.name = name;
        this.age = age;
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// COMPACT CONSTRUCTOR - Transformations
// ═══════════════════════════════════════════════════════════════════════════

record CompactPerson(String name, int age) {
    // Compact constructor - no parameters!
    public CompactPerson {
        // Can modify PARAMETERS (not fields)
        name = name.trim();           // ✓ Modifying parameter
        if (age < 0) age = 0;         // ✓ Modifying parameter

        // this.name = name.trim();   // ✗ DOES NOT COMPILE - cannot modify fields
        // this.age = 0;              // ✗ DOES NOT COMPILE - cannot modify fields
    }
    // Long constructor implicitly called here with modified parameters
}

// ═══════════════════════════════════════════════════════════════════════════
// OVERLOADED CONSTRUCTORS
// ═══════════════════════════════════════════════════════════════════════════

record OverloadedPerson(String name, int age) {
    // Overloaded constructor - first line MUST call this()
    public OverloadedPerson(String name) {
        this(name, 0);  // ✓ First line calls another constructor
    }

    // Another overload
    public OverloadedPerson() {
        this("Unknown", 0);  // ✓ First line calls another constructor
    }

    // WRONG:
    // public OverloadedPerson(String name) {
    //     name = name.trim();  // ✗ Can only transform on first line
    //     this(name, 0);
    // }
}

// ═══════════════════════════════════════════════════════════════════════════
// INSTANCE METHODS AND OVERRIDING
// ═══════════════════════════════════════════════════════════════════════════

record Employee(String name, double salary) {
    // Instance method
    public String format() {
        return name + ": $" + salary;
    }

    // Override toString
    @Override
    public String toString() {
        return "Employee[name=" + name + ", salary=$" + salary + "]";
    }

    // Can override accessor
    @Override
    public double salary() {
        return Math.round(salary * 100.0) / 100.0;  // Round to 2 decimals
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// STATIC FIELDS AND METHODS
// ═══════════════════════════════════════════════════════════════════════════

record ProductRecord(String name, double price) {
    // Static field
    private static final double TAX_RATE = 0.08;

    // Static method
    public static double getTaxRate() {
        return TAX_RATE;
    }

    // Instance method using static field
    public double totalPrice() {
        return price * (1 + TAX_RATE);
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// NESTED TYPES IN RECORDS
// ═══════════════════════════════════════════════════════════════════════════

record Container(String data) {
    // Nested record
    record NestedRecord(int value) { }

    // Nested class
    static class NestedClass { }

    // Nested interface
    interface NestedInterface { }

    // Nested enum
    enum NestedEnum { A, B }
}

// ═══════════════════════════════════════════════════════════════════════════
// EXAM TRAPS
// ═══════════════════════════════════════════════════════════════════════════

// TRAP 1: Cannot extend records
// record Parent(String name) { }
// record Child(String name, int age) extends Parent { }  // ✗ DOES NOT COMPILE

// TRAP 2: Cannot add instance fields
// record Bad(String name) {
//     private int age;  // ✗ DOES NOT COMPILE - no instance fields outside declaration
// }

// TRAP 3: Instance initializers not allowed
// record Bad(String name) {
//     { System.out.println("Init"); }  // ✗ DOES NOT COMPILE
// }

// TRAP 4: Modifying fields in compact constructor
// record Bad(String name) {
//     public Bad {
//         this.name = name.trim();  // ✗ DOES NOT COMPILE - modify parameter, not field
//     }
// }

// TRAP 5: Overloaded constructor without this()
// record Bad(String name) {
//     public Bad() {
//         name = "Default";  // ✗ DOES NOT COMPILE - must call this() first
//     }
// }

// TRAP 6: Long constructor not setting all fields
// record Bad(String name, int age) {
//     public Bad(String name, int age) {
//         this.name = name;  // ✗ DOES NOT COMPILE - must set age too
//     }
// }

// TRAP 7: Wrong accessor name
// record Point(int x, int y) { }
// Point p = new Point(1, 2);
// p.getX();  // ✗ DOES NOT COMPILE - use x() not getX()

// TRAP 8: Trying to make record non-final
// public non-final record Bad(String name) { }  // ✗ DOES NOT COMPILE - records are implicitly final
