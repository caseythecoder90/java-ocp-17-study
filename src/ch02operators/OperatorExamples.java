package ch02operators;

/**
 * OPERATOR EXAMPLES - Complete Reference for OCP Java 17 Exam
 *
 * This file demonstrates ALL Java operators with detailed examples.
 * Please Pray Over Code, My Addict Shifts Really Equal Logic And exclusive ORs Convince Coders To Always Remember
 * post unary
 * pre unary
 * other unary
 * cast
 * multiplication/division/modulus
 * addition subtraction
 * shifts
 * relational
 * equal to not equal to
 * logical and logical exclusive or , logical inclusive or
 */
public class OperatorExamples {

    public static void main(String[] args) {
        System.out.println("=== COMPREHENSIVE OPERATOR EXAMPLES ===\n");

        unaryOperators();
        arithmeticOperators();
        assignmentOperators();
        compoundAssignmentOperators();
        incrementDecrementOperators();
        relationalOperators();
        logicalOperators();
        conditionalOperators();
        bitwiseOperators();
        shiftOperators();
        ternaryOperator();
        instanceofOperator();
        demonstrateInstanceOfInDepth();
        stringConcatenation();
        numericPromotion();
    }

    /**
     * UNARY OPERATORS
     * Operate on a single operand
     */
    private static void unaryOperators() {
        System.out.println("=== UNARY OPERATORS ===");

        // Unary plus (+) - indicates positive value (rarely used)
        int a = +5;
        System.out.println("Unary plus: +5 = " + a);

        // Unary minus (-) - negates value
        int b = -10;
        System.out.println("Unary minus: -10 = " + b);
        int c = -(-5);
        System.out.println("Double negative: -(-5) = " + c);

        // Logical NOT (!) - inverts boolean value
        boolean flag = true;
        System.out.println("Logical NOT: !true = " + !flag);
        System.out.println("Double NOT: !!true = " + !!flag);

        // Bitwise complement (~) - inverts all bits
        // To find bitwise complement of a number, multiply it by negative one
        // and then subtract one
        int d = 5;  // Binary: 0000 0101
        int e = ~d; // Binary: 1111 1010 = -6
        System.out.println("Bitwise complement: ~5 = " + e);

        System.out.println();
    }

    /**
     * ARITHMETIC OPERATORS
     * Basic mathematical operations
     */
    private static void arithmeticOperators() {
        System.out.println("=== ARITHMETIC OPERATORS ===");

        int a = 10, b = 3;

        // Addition (+)
        System.out.println("Addition: 10 + 3 = " + (a + b));

        // Subtraction (-)
        System.out.println("Subtraction: 10 - 3 = " + (a - b));

        // Multiplication (*)
        System.out.println("Multiplication: 10 * 3 = " + (a * b));

        // Division (/)
        System.out.println("Division: 10 / 3 = " + (a / b) + " (integer division)");
        System.out.println("Division: 10.0 / 3 = " + (10.0 / 3) + " (floating-point)");

        // Modulus (%) - remainder after division
        System.out.println("Modulus: 10 % 3 = " + (a % b));
        System.out.println("Modulus: 15 % 4 = " + (15 % 4));

        // EXAM TRAP: Division by zero
        // int bad = 10 / 0;  // Throws ArithmeticException at runtime
        // double okay = 10.0 / 0;  // Returns Infinity (no exception!)
        System.out.println("Division by zero (double): 10.0 / 0 = " + (10.0 / 0));
        System.out.println("Modulus by zero (double): 10.0 % 0 = " + (10.0 % 0)); // NaN

        System.out.println();
    }

    /**
     * ASSIGNMENT OPERATOR
     * Assigns value to a variable
     */
    private static void assignmentOperators() {
        System.out.println("=== ASSIGNMENT OPERATOR (=) ===");

        int x = 10;
        System.out.println("Simple assignment: x = 10, x is " + x);

        // Multiple assignment (right-to-left associativity)
        int a, b, c;
        a = b = c = 5;
        System.out.println("Multiple assignment: a = b = c = 5");
        System.out.println("  a = " + a + ", b = " + b + ", c = " + c);

        // Assignment returns a value
        int y;
        int z = (y = 20) + 5;  // y = 20, then z = 20 + 5
        System.out.println("Assignment returns value: z = (y = 20) + 5");
        System.out.println("  y = " + y + ", z = " + z);

        System.out.println();
    }

    /**
     * COMPOUND ASSIGNMENT OPERATORS
     * Combine operation with assignment
     */
    private static void compoundAssignmentOperators() {
        System.out.println("=== COMPOUND ASSIGNMENT OPERATORS ===");

        int x = 10;

        // Addition assignment (+=)
        x += 5;  // Same as: x = x + 5
        System.out.println("x += 5: " + x);

        // Subtraction assignment (-=)
        x -= 3;  // Same as: x = x - 3
        System.out.println("x -= 3: " + x);

        // Multiplication assignment (*=)
        x *= 2;  // Same as: x = x * 2
        System.out.println("x *= 2: " + x);

        // Division assignment (/=)
        x /= 4;  // Same as: x = x / 4
        System.out.println("x /= 4: " + x);

        // Modulus assignment (%=)
        x %= 5;  // Same as: x = x % 5
        System.out.println("x %= 5: " + x);

        // IMPORTANT: Compound operators include implicit cast!
        long y = 10;
        int z = 5;
        // z = z + y;  // DOES NOT COMPILE - requires explicit cast
        z += y;        // COMPILES - implicit cast included!
        System.out.println("\nCompound assignment includes implicit cast:");
        System.out.println("  z += y (where y is long) compiles and z = " + z);

        System.out.println();
    }

    /**
     * INCREMENT (++) AND DECREMENT (--) OPERATORS
     * CRITICAL for OCP exam!
     */
    private static void incrementDecrementOperators() {
        System.out.println("=== INCREMENT/DECREMENT OPERATORS ===");

        // Pre-increment (++x) - increment first, then use value
        int x = 5;
        int a = ++x;
        System.out.println("Pre-increment: int a = ++x where x=5");
        System.out.println("  a = " + a + ", x = " + x);

        // Post-increment (x++) - use value first, then increment
        x = 5;
        int b = x++;
        System.out.println("\nPost-increment: int b = x++ where x=5");
        System.out.println("  b = " + b + ", x = " + x);

        // Pre-decrement (--x)
        x = 5;
        int c = --x;
        System.out.println("\nPre-decrement: int c = --x where x=5");
        System.out.println("  c = " + c + ", x = " + x);

        // Post-decrement (x--)
        x = 5;
        int d = x--;
        System.out.println("\nPost-decrement: int d = x-- where x=5");
        System.out.println("  d = " + d + ", x = " + x);

        // EXAM TRAP: Multiple increments in same expression
        x = 5;
        int result = x++ + x++ + x++;
        System.out.println("\nEXAM TRAP: x++ + x++ + x++ where x=5");
        System.out.println("  Result: " + result + " (5 + 6 + 7 = 18)");
        System.out.println("  x is now: " + x);

        System.out.println();
    }

    /**
     * RELATIONAL OPERATORS
     * Compare two values, return boolean
     */
    private static void relationalOperators() {
        System.out.println("=== RELATIONAL OPERATORS ===");

        int a = 10, b = 20;

        // Less than (<)
        System.out.println("10 < 20: " + (a < b));

        // Greater than (>)
        System.out.println("10 > 20: " + (a > b));

        // Less than or equal to (<=)
        System.out.println("10 <= 10: " + (10 <= 10));

        // Greater than or equal to (>=)
        System.out.println("20 >= 10: " + (b >= a));

        // EXAM NOTE: Can only compare numeric types
        // boolean bad = "hello" < "world";  // DOES NOT COMPILE
        // boolean bad2 = true < false;       // DOES NOT COMPILE

        System.out.println();
    }

    /**
     * EQUALITY OPERATORS
     * Check if two values are equal or not
     */
    private static void equalityOperators() {
        System.out.println("=== EQUALITY OPERATORS ===");

        // Equal to (==)
        System.out.println("10 == 10: " + (10 == 10));
        System.out.println("10 == 20: " + (10 == 20));

        // Not equal to (!=)
        System.out.println("10 != 20: " + (10 != 20));
        System.out.println("10 != 10: " + (10 != 10));

        // For objects, == compares references, not values
        String s1 = new String("hello");
        String s2 = new String("hello");
        System.out.println("\nObject comparison:");
        System.out.println("s1 == s2: " + (s1 == s2) + " (different objects)");
        System.out.println("s1.equals(s2): " + s1.equals(s2) + " (same content)");

        // String pool
        String s3 = "hello";
        String s4 = "hello";
        System.out.println("s3 == s4 (string literals): " + (s3 == s4) + " (same object in pool)");

        System.out.println();
    }

    /**
     * LOGICAL OPERATORS
     * Operate on boolean values
     */
    private static void logicalOperators() {
        System.out.println("=== LOGICAL OPERATORS ===");

        // Logical AND (&) - evaluates both sides (NO short-circuit)
        System.out.println("true & true: " + (true & true));
        System.out.println("true & false: " + (true & false));
        int x = 5;
        boolean result1 = (false & (++x > 0));  // ++x STILL executes!
        System.out.println("false & (++x > 0): " + result1 + ", x = " + x + " (incremented!)");

        // Logical OR (|) - evaluates both sides (NO short-circuit)
        System.out.println("\ntrue | false: " + (true | false));
        System.out.println("false | false: " + (false | false));

        // Logical XOR (^) - true if operands are different
        System.out.println("\ntrue ^ true: " + (true ^ true) + " (same = false)");
        System.out.println("true ^ false: " + (true ^ false) + " (different = true)");
        System.out.println("false ^ false: " + (false ^ false) + " (same = false)");

        System.out.println();
    }

    /**
     * CONDITIONAL OPERATORS (Short-circuit)
     * Only evaluate right side if necessary
     */
    private static void conditionalOperators() {
        System.out.println("=== CONDITIONAL OPERATORS (Short-circuit) ===");

        // Conditional AND (&&) - short-circuits if left is false
        System.out.println("true && true: " + (true && true));
        System.out.println("false && true: " + (false && true));

        int x = 5;
        boolean result1 = false && (++x > 0);  // ++x NOT executed (short-circuit)
        System.out.println("false && (++x > 0): " + result1 + ", x = " + x + " (NOT incremented)");

        // Conditional OR (||) - short-circuits if left is true
        System.out.println("\ntrue || false: " + (true || false));
        System.out.println("false || false: " + (false || false));

        x = 5;
        boolean result2 = true || (++x > 0);  // ++x NOT executed (short-circuit)
        System.out.println("true || (++x > 0): " + result2 + ", x = " + x + " (NOT incremented)");

        // EXAM TIP: Know the difference between & and &&, | and ||
        System.out.println("\nDifference between & and &&:");
        System.out.println("  & always evaluates both sides");
        System.out.println("  && short-circuits (stops if left is false)");

        System.out.println();
    }

    /**
     * BITWISE OPERATORS
     * Operate on individual bits
     */
    private static void bitwiseOperators() {
        System.out.println("=== BITWISE OPERATORS ===");

        int a = 5;   // Binary: 0101
        int b = 3;   // Binary: 0011

        // Bitwise AND (&) - 1 if both bits are 1
        int and = a & b;  // 0101 & 0011 = 0001 = 1
        System.out.println("5 & 3 = " + and + " (binary: 0101 & 0011 = 0001)");

        // Bitwise OR (|) - 1 if either bit is 1
        int or = a | b;   // 0101 | 0011 = 0111 = 7
        System.out.println("5 | 3 = " + or + " (binary: 0101 | 0011 = 0111)");

        // Bitwise XOR (^) - 1 if bits are different
        int xor = a ^ b;  // 0101 ^ 0011 = 0110 = 6
        System.out.println("5 ^ 3 = " + xor + " (binary: 0101 ^ 0011 = 0110)");

        // Bitwise complement (~) - inverts all bits
        int complement = ~5;  // ~0101 = 1010 (in two's complement = -6)
        System.out.println("~5 = " + complement);

        System.out.println();
    }

    /**
     * SHIFT OPERATORS
     * Shift bits left or right
     */
    private static void shiftOperators() {
        System.out.println("=== SHIFT OPERATORS ===");

        int x = 8;  // Binary: 1000

        // Left shift (<<) - shift bits left, fill with zeros
        int leftShift = x << 2;  // 1000 << 2 = 100000 = 32
        System.out.println("8 << 2 = " + leftShift + " (multiply by 2^2 = 4)");

        // Right shift (>>) - shift bits right, fill with sign bit
        int rightShift = x >> 2;  // 1000 >> 2 = 10 = 2
        System.out.println("8 >> 2 = " + rightShift + " (divide by 2^2 = 4)");

        // Unsigned right shift (>>>) - shift right, fill with zeros
        int negativeShift = -8 >>> 2;
        System.out.println("-8 >>> 2 = " + negativeShift + " (fills with zeros, not sign)");

        // EXAM TIP: Shift operators can be tricky with negative numbers
        System.out.println("\n-8 >> 2 (signed): " + (-8 >> 2) + " (preserves sign)");
        System.out.println("-8 >>> 2 (unsigned): " + (-8 >>> 2) + " (fills with 0)");

        System.out.println();
    }

    /**
     * TERNARY OPERATOR
     * The only ternary operator in Java: ? :
     */
    private static void ternaryOperator() {
        System.out.println("=== TERNARY OPERATOR (? :) ===");

        int x = 10;
        String result1 = (x > 5) ? "Greater" : "Smaller";
        System.out.println("(10 > 5) ? \"Greater\" : \"Smaller\" = " + result1);

        int result2 = (x < 5) ? 100 : 200;
        System.out.println("(10 < 5) ? 100 : 200 = " + result2);

        // Nested ternary (avoid in practice, but know for exam)
        int a = 15;
        String result3 = (a > 20) ? "Large" : (a > 10) ? "Medium" : "Small";
        System.out.println("Nested ternary for a=15: " + result3);

        // EXAM TRAP: Both expressions must be compatible types
        // int bad = true ? "Hello" : 42;  // DOES NOT COMPILE - incompatible types

        // EXAM TRAP: Both branches are evaluated for type
        int y = 1;
        // int bad2 = (y > 0) ? y++ : "error";  // DOES NOT COMPILE
        int good = (y > 0) ? y++ : -1;  // Both int, compiles
        System.out.println("Compatible types required: " + good);

        System.out.println();
    }

    /**
     * INSTANCEOF OPERATOR
     * Tests if object is instance of a class
     * It checks the actual runtime object type and NOT the reference type
     */
    private static void instanceofOperator() {
        System.out.println("=== INSTANCEOF OPERATOR ===");

        String str = "hello";
        Object obj = "world";
        String nullStr = null;

        System.out.println("\"hello\" instanceof String: " + (str instanceof String));
        System.out.println("\"world\" instanceof Object: " + (obj instanceof Object));
        System.out.println("\"world\" instanceof String: " + (obj instanceof String));

        // null is not an instance of anything
        System.out.println("null instanceof String: " + (nullStr instanceof String));

        // EXAM TIP: Pattern matching instanceof (Java 16+)
        if (obj instanceof String s) {
            System.out.println("Pattern matching: length = " + s.length());
        }

        System.out.println();
    }

    public static void demonstrateInstanceOfInDepth() {
        System.out.println("=".repeat(70));
        System.out.println("INSTANCEOF CHECKS THE ACTUAL OBJECT, NOT THE REFERENCE TYPE");
        System.out.println("=".repeat(70) + "\n");

        // Example 1: Reference type vs Actual object type
        System.out.println("--- Example 1: Basic instanceof ---");
        String str = "hello";           // Reference: String, Object: String
        Object obj = "world";            // Reference: Object, Object: String

        System.out.println("String str = \"hello\";");
        System.out.println("Object obj = \"world\";");
        System.out.println();

        System.out.println("str instanceof String:  " + (str instanceof String));  // true
        System.out.println("obj instanceof String:  " + (obj instanceof String));  // true ← KEY!
        System.out.println("obj instanceof Object:  " + (obj instanceof Object));  // true

        System.out.println("\nWhy? Because obj POINTS TO a String object in memory!");
        System.out.println("The reference type (Object) doesn't matter for instanceof.");

        // Example 2: Class hierarchy
        System.out.println("\n" + "=".repeat(70));
        System.out.println("--- Example 2: Class Hierarchy ---");
        System.out.println("=".repeat(70) + "\n");

        Animal animal = new Dog();      // Reference: Animal, Object: Dog

        System.out.println("Animal animal = new Dog();");
        System.out.println();
        System.out.println("animal instanceof Dog:     " + (animal instanceof Dog));     // true
        System.out.println("animal instanceof Animal:  " + (animal instanceof Animal));  // true
        System.out.println("animal instanceof Object:  " + (animal instanceof Object));  // true

        System.out.println("\nThe actual object is a Dog, so instanceof Dog returns true!");

        // Example 3: Demonstrating with multiple references
        System.out.println("\n" + "=".repeat(70));
        System.out.println("--- Example 3: Same Object, Different Reference Types ---");
        System.out.println("=".repeat(70) + "\n");

        Dog actualDog = new Dog();
        Animal animalRef = actualDog;
        Object objectRef = actualDog;

        System.out.println("Dog actualDog = new Dog();");
        System.out.println("Animal animalRef = actualDog;");
        System.out.println("Object objectRef = actualDog;");
        System.out.println("\nAll three references point to THE SAME Dog object!");
        System.out.println();

        System.out.println("Reference Type | instanceof Dog | instanceof Animal | instanceof Object");
        System.out.println("---------------|----------------|-------------------|------------------");
        System.out.printf("%-14s | %-14s | %-17s | %s%n",
                "Dog", (actualDog instanceof Dog), (actualDog instanceof Animal), (actualDog instanceof Object));
        System.out.printf("%-14s | %-14s | %-17s | %s%n",
                "Animal", (animalRef instanceof Dog), (animalRef instanceof Animal), (animalRef instanceof Object));
        System.out.printf("%-14s | %-14s | %-17s | %s%n",
                "Object", (objectRef instanceof Dog), (objectRef instanceof Animal), (objectRef instanceof Object));

        System.out.println("\nAll return the SAME results because they point to the same Dog object!");

        // Example 4: What about compile-time checks?
        System.out.println("\n" + "=".repeat(70));
        System.out.println("--- Example 4: Compile-Time vs Runtime Checks ---");
        System.out.println("=".repeat(70) + "\n");

        Object obj2 = "test";

        // Runtime check - works!
        if (obj2 instanceof String) {
            System.out.println("✓ Runtime: obj2 is a String");
        }

        // But you can't do this at compile-time without casting:
        // int length = obj2.length();  // ← COMPILE ERROR!

        // You need to cast:
        int length = ((String) obj2).length();
        System.out.println("  After casting: length = " + length);

        // Or use pattern matching (Java 16+):
        if (obj2 instanceof String s) {
            System.out.println("  Pattern matching: length = " + s.length());
        }

        // Example 5: Null check
        System.out.println("\n" + "=".repeat(70));
        System.out.println("--- Example 5: null is not an instance of anything ---");
        System.out.println("=".repeat(70) + "\n");

        String nullStr = null;
        Object nullObj = null;

        System.out.println("null instanceof String:  " + (nullStr instanceof String));  // false
        System.out.println("null instanceof Object:  " + (nullObj instanceof Object));  // false
        System.out.println("\nnull is NEVER an instance of any type!");

        // Example 6: Illegal instanceof at compile time
        System.out.println("\n" + "=".repeat(70));
        System.out.println("--- Example 6: When instanceof Won't Compile ---");
        System.out.println("=".repeat(70) + "\n");

        String str2 = "test";
        // str2 instanceof Integer  // ← WON'T COMPILE!
        // Why? String and Integer have NO relationship
        // The compiler knows this is impossible

        System.out.println("String str2 = \"test\";");
        System.out.println("str2 instanceof Integer  // ← Compilation ERROR!");
        System.out.println("\nWhy? String and Integer are unrelated types.");
        System.out.println("The compiler knows a String can NEVER be an Integer.");

        // But this compiles (runtime check):
        Object obj3 = "test";
        System.out.println("\nObject obj3 = \"test\";");
        System.out.println("obj3 instanceof Integer: " + (obj3 instanceof Integer));  // false
        System.out.println("\nThis compiles because obj3 COULD point to an Integer.");

        // Summary
        System.out.println("\n" + "=".repeat(70));
        System.out.println("KEY TAKEAWAYS");
        System.out.println("=".repeat(70));
        System.out.println("✓ instanceof checks the ACTUAL OBJECT TYPE at runtime");
        System.out.println("✓ NOT the reference variable's declared type");
        System.out.println("✓ Works up the inheritance hierarchy (Dog is an Animal)");
        System.out.println("✓ null instanceof AnyType is always false");
        System.out.println("✓ Compiler prevents impossible checks (String instanceof Integer)");
        System.out.println("✓ Use pattern matching to avoid casting: if (obj instanceof String s)");
    }

    static class Animal {}
    static class Dog extends Animal {}

    /**
     * STRING CONCATENATION (+)
     * Special behavior with String
     */
    private static void stringConcatenation() {
        System.out.println("=== STRING CONCATENATION ===");

        String s1 = "Hello" + " " + "World";
        System.out.println("String + String: " + s1);

        String s2 = "Number: " + 42;
        System.out.println("String + int: " + s2);

        String s3 = 1 + 2 + " is three";
        System.out.println("int + int + String: " + s3 + " (left-to-right)");

        String s4 = "Three is " + 1 + 2;
        System.out.println("String + int + int: " + s4 + " (becomes string concatenation)");

        String s5 = "Result: " + (1 + 2);
        System.out.println("String + (int + int): " + s5);

        System.out.println();
    }

    /**
     * NUMERIC PROMOTION
     * How Java promotes types in expressions
     */
    private static void numericPromotion() {
        System.out.println("=== NUMERIC PROMOTION ===");

        // Rule 1: byte, short, char promoted to int
        byte b = 5;
        short s = 10;
        // byte result1 = b + s;  // DOES NOT COMPILE - result is int
        int result1 = b + s;      // Correct
        System.out.println("byte + short = int: " + result1);

        // Rule 2: If one operand is long, result is long
        long l = 100L;
        int i = 50;
        long result2 = l + i;
        System.out.println("long + int = long: " + result2);

        // Rule 3: If one operand is double, result is double
        double d = 3.14;
        int x = 2;
        double result3 = d * x;
        System.out.println("double * int = double: " + result3);

        // Rule 4: If one operand is float (and no double), result is float
        float f = 2.5f;
        int y = 4;
        float result4 = f + y;
        System.out.println("float + int = float: " + result4);

        // EXAM TRAP: Compound assignment has implicit cast
        int z = 10;
        // z = z * 3.5;  // DOES NOT COMPILE - double to int
        z *= 3.5;        // Compiles - implicit cast to int
        System.out.println("\nCompound assignment implicit cast: z *= 3.5 = " + z);

        System.out.println();
    }

    /**
     * OPERATOR SUMMARY
     */
    public static void printOperatorSummary() {
        System.out.println("""

                ╔════════════════════════════════════════════════════════════════╗
                ║                    OPERATOR SUMMARY                            ║
                ╠════════════════════════════════════════════════════════════════╣
                ║ UNARY:        +  -  !  ~  ++  --                               ║
                ║ ARITHMETIC:   +  -  *  /  %                                    ║
                ║ SHIFT:        <<  >>  >>>                                      ║
                ║ RELATIONAL:   <  >  <=  >=  instanceof                         ║
                ║ EQUALITY:     ==  !=                                           ║
                ║ BITWISE:      &  ^  |                                          ║
                ║ LOGICAL:      &&  ||                                           ║
                ║ TERNARY:      ? :                                              ║
                ║ ASSIGNMENT:   =  +=  -=  *=  /=  %=  &=  ^=  |=  <<=  >>=  >>>= ║
                ╚════════════════════════════════════════════════════════════════╝
                """);
    }
}
