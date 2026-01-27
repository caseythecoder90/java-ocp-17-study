package ch07beyondclasses;

/**
 * INNER INTERFACES (NESTED INTERFACES) - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CRITICAL RULE: ALL INNER INTERFACES ARE IMPLICITLY STATIC
 * ═══════════════════════════════════════════════════════════════════════════
 * Unlike inner classes, ALL interfaces declared inside another type
 * (class or interface) are AUTOMATICALLY STATIC.
 *
 * You CANNOT have a non-static inner interface!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * KEY RULES FOR INNER INTERFACES
 * ═══════════════════════════════════════════════════════════════════════════
 * 1. ALWAYS implicitly static (whether you write "static" or not)
 * 2. Can be declared in classes, interfaces, or enums
 * 3. Can have any access modifier: public, protected, package-private, private
 * 4. Do NOT require instance of outer class to use
 * 5. Can be accessed using: OuterClass.InnerInterface syntax
 * 6. Members are implicitly public static final (like top-level interfaces)
 * 7. Methods are implicitly public abstract (like top-level interfaces)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * WHY ARE THEY STATIC?
 * ═══════════════════════════════════════════════════════════════════════════
 * Interfaces define contracts - they don't have instance state.
 * It wouldn't make sense for an interface to be tied to an instance
 * of the outer class, so Java makes ALL nested interfaces static.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * ACCESS MODIFIERS FOR INNER INTERFACES
 * ═══════════════════════════════════════════════════════════════════════════
 * In a CLASS:
 * - public          ✓ Accessible everywhere
 * - protected       ✓ Accessible in subclasses and same package
 * - package-private ✓ Accessible in same package only
 * - private         ✓ Accessible only in outer class
 *
 * In an INTERFACE:
 * - public          ✓ Explicitly public (redundant, already default)
 * - (default)       ✓ Implicitly public
 * - protected       ✗ NOT allowed
 * - private         ✗ NOT allowed (private methods allowed, but not private nested interfaces)
 */

// ═══════════════════════════════════════════════════════════════════════════
// EXAMPLE 1: INNER INTERFACE IN A CLASS
// ═══════════════════════════════════════════════════════════════════════════
class OuterClass {

    // ───────────────────────────────────────────────────────────────────────
    // Public inner interface - accessible anywhere
    // ───────────────────────────────────────────────────────────────────────
    public interface PublicInnerInterface {
        void publicMethod();
        // Implicitly: public abstract void publicMethod();
    }

    // ───────────────────────────────────────────────────────────────────────
    // Private inner interface - only accessible within OuterClass
    // ───────────────────────────────────────────────────────────────────────
    private interface PrivateInnerInterface {
        void privateMethod();
    }

    // ───────────────────────────────────────────────────────────────────────
    // Protected inner interface - accessible in subclasses and same package
    // ───────────────────────────────────────────────────────────────────────
    protected interface ProtectedInnerInterface {
        void protectedMethod();
    }

    // ───────────────────────────────────────────────────────────────────────
    // Package-private inner interface - accessible in same package only
    // ───────────────────────────────────────────────────────────────────────
    interface PackagePrivateInnerInterface {
        void packageMethod();
    }

    // ───────────────────────────────────────────────────────────────────────
    // You CAN write "static" but it's redundant (already implicitly static)
    // ───────────────────────────────────────────────────────────────────────
    public static interface ExplicitlyStatic {
        void method();
    }
    // This is IDENTICAL to PublicInnerInterface above

    // ───────────────────────────────────────────────────────────────────────
    // Using private inner interface within outer class
    // ───────────────────────────────────────────────────────────────────────
    private class ImplementsPrivate implements PrivateInnerInterface {
        @Override
        public void privateMethod() {
            System.out.println("Implementing private interface");
        }
    }

    public void demonstratePrivateInterface() {
        PrivateInnerInterface obj = new ImplementsPrivate();
        obj.privateMethod();
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// USING PUBLIC INNER INTERFACE FROM OUTSIDE
// ═══════════════════════════════════════════════════════════════════════════
class ImplementsPublicInner implements OuterClass.PublicInnerInterface {
    @Override
    public void publicMethod() {
        System.out.println("Implementing public inner interface");
    }
    // Note: No instance of OuterClass needed! It's static!
}

// ═══════════════════════════════════════════════════════════════════════════
// EXAMPLE 2: INNER INTERFACE IN AN INTERFACE
// ═══════════════════════════════════════════════════════════════════════════
interface OuterInterface {

    // ───────────────────────────────────────────────────────────────────────
    // Inner interface in an interface - implicitly public and static
    // ───────────────────────────────────────────────────────────────────────
    interface InnerInterface {
        void innerMethod();
    }

    // Same as writing:
    // public static interface InnerInterface { ... }

    // ───────────────────────────────────────────────────────────────────────
    // Another nested interface - also implicitly public and static
    // ───────────────────────────────────────────────────────────────────────
    interface AnotherInner {
        String getMessage();
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// USING NESTED INTERFACE FROM OUTER INTERFACE
// ═══════════════════════════════════════════════════════════════════════════
class ImplementsNestedInterface implements OuterInterface.InnerInterface {
    @Override
    public void innerMethod() {
        System.out.println("Implementing nested interface");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// EXAMPLE 3: REAL-WORLD USE CASE - MAP.ENTRY
// ═══════════════════════════════════════════════════════════════════════════
// Java's Map interface has a famous inner interface: Map.Entry
// This is a perfect example of an inner interface in action!

/*
 * public interface Map<K, V> {
 *     interface Entry<K, V> {      // Inner interface (implicitly static)
 *         K getKey();
 *         V getValue();
 *         V setValue(V value);
 *     }
 * }
 *
 * Usage:
 * Map.Entry<String, Integer> entry = ...
 * entry.getKey();
 */

// ═══════════════════════════════════════════════════════════════════════════
// EXAMPLE 4: MULTILEVEL NESTING
// ═══════════════════════════════════════════════════════════════════════════
class Level1 {
    interface Level2 {           // Implicitly static
        interface Level3 {       // Implicitly static
            void deepMethod();
        }
    }
}

class ImplementsLevel3 implements Level1.Level2.Level3 {
    @Override
    public void deepMethod() {
        System.out.println("Deep nesting works!");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// EXAMPLE 5: INNER INTERFACE WITH CONSTANTS
// ═══════════════════════════════════════════════════════════════════════════
class Configuration {
    interface Constants {
        // All implicitly: public static final
        int MAX_SIZE = 100;
        String DEFAULT_NAME = "Unknown";
        double PI = 3.14159;
    }

    public void useConstants() {
        System.out.println("Max size: " + Constants.MAX_SIZE);
        // Can access without instance of Configuration!
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// EXAMPLE 6: COMMON EXAM TRAPS
// ═══════════════════════════════════════════════════════════════════════════
class ExamTraps {

    // ───────────────────────────────────────────────────────────────────────
    // TRAP 1: Thinking you need an instance of the outer class
    // ───────────────────────────────────────────────────────────────────────
    public interface MyInterface {
        void doSomething();
    }

    // WRONG thinking:
    // ExamTraps outer = new ExamTraps();
    // ExamTraps.MyInterface inner = outer.new MyInterface() {...};  // ✗ WRONG!

    // CORRECT: No instance needed (it's static!)
    // ExamTraps.MyInterface inner = new ExamTraps.MyInterface() {...};  // ✓
}

// ═══════════════════════════════════════════════════════════════════════════
// COMPARISON: INNER INTERFACE vs INNER CLASS
// ═══════════════════════════════════════════════════════════════════════════
class ComparisonExample {

    // Inner interface - ALWAYS static (implicitly)
    interface InnerInterface {
        void method();
    }

    // Inner class - NOT static by default
    class InnerClass {  // Non-static inner class
        void method() {
            // Can access outer class instance members
        }
    }

    // Static nested class - explicitly static
    static class StaticNestedClass {
        void method() {
            // Cannot access outer class instance members directly
        }
    }

    /*
     * KEY DIFFERENCE:
     *
     * InnerInterface:     ALWAYS static, no instance of ComparisonExample needed
     * InnerClass:         Non-static, REQUIRES instance of ComparisonExample
     * StaticNestedClass:  Explicitly static, no instance needed
     */
}

// ═══════════════════════════════════════════════════════════════════════════
// DEMONSTRATION CLASS
// ═══════════════════════════════════════════════════════════════════════════
public class InnerInterfaces {

    // ───────────────────────────────────────────────────────────────────────
    // Example: Callback pattern using inner interface
    // ───────────────────────────────────────────────────────────────────────
    public interface Callback {
        void onComplete(String result);
        void onError(String error);
    }

    public void performOperation(Callback callback) {
        try {
            // Simulate some operation
            String result = "Operation successful";
            callback.onComplete(result);
        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // MAIN METHOD - EXAMPLES
    // ═══════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        System.out.println("=== Inner Interfaces Demo ===\n");

        // ───────────────────────────────────────────────────────────────────
        // 1. Using public inner interface (no outer instance needed!)
        // ───────────────────────────────────────────────────────────────────
        OuterClass.PublicInnerInterface obj1 = new ImplementsPublicInner();
        obj1.publicMethod();

        // ───────────────────────────────────────────────────────────────────
        // 2. Using private inner interface (from within outer class)
        // ───────────────────────────────────────────────────────────────────
        OuterClass outer = new OuterClass();
        outer.demonstratePrivateInterface();

        // ───────────────────────────────────────────────────────────────────
        // 3. Using nested interface from another interface
        // ───────────────────────────────────────────────────────────────────
        OuterInterface.InnerInterface obj2 = new ImplementsNestedInterface();
        obj2.innerMethod();

        // ───────────────────────────────────────────────────────────────────
        // 4. Using deeply nested interface
        // ───────────────────────────────────────────────────────────────────
        Level1.Level2.Level3 obj3 = new ImplementsLevel3();
        obj3.deepMethod();

        // ───────────────────────────────────────────────────────────────────
        // 5. Accessing constants from inner interface
        // ───────────────────────────────────────────────────────────────────
        System.out.println("Max size: " + Configuration.Constants.MAX_SIZE);
        System.out.println("Default name: " + Configuration.Constants.DEFAULT_NAME);

        // ───────────────────────────────────────────────────────────────────
        // 6. Using callback pattern
        // ───────────────────────────────────────────────────────────────────
        InnerInterfaces demo = new InnerInterfaces();
        demo.performOperation(new Callback() {
            @Override
            public void onComplete(String result) {
                System.out.println("Success: " + result);
            }

            @Override
            public void onError(String error) {
                System.out.println("Error: " + error);
            }
        });

        // ───────────────────────────────────────────────────────────────────
        // 7. Demonstrating that inner interface is static
        // ───────────────────────────────────────────────────────────────────
        // No instance of InnerInterfaces needed to use Callback!
        Callback callback = new Callback() {
            @Override
            public void onComplete(String result) {
                System.out.println("Callback without outer instance: " + result);
            }

            @Override
            public void onError(String error) {
                System.out.println("Error: " + error);
            }
        };
        callback.onComplete("This works!");

        System.out.println("\n✓ All inner interface examples completed");

        // ───────────────────────────────────────────────────────────────────
        // REMEMBER FOR EXAM:
        // ───────────────────────────────────────────────────────────────────
        System.out.println("\n=== KEY EXAM POINTS ===");
        System.out.println("1. ALL inner interfaces are IMPLICITLY STATIC");
        System.out.println("2. No instance of outer class needed to use them");
        System.out.println("3. Can have any access modifier in a class");
        System.out.println("4. In an interface: implicitly public only");
        System.out.println("5. Access with: OuterType.InnerInterface");
    }
}
