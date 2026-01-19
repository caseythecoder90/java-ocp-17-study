package ch11exceptionsandlocalization;

import java.io.Closeable;
import java.io.IOException;

/**
 * TRY-WITH-RESOURCES STATEMENT
 * ============================
 *
 * PURPOSE:
 * --------
 * Guarantees that resources are closed as soon as they pass out of scope.
 * Java will attempt to close them within the same method.
 *
 * BEHIND THE SCENES:
 * ------------------
 * The compiler replaces try-with-resources with a try-finally block.
 * This implicit finally block is created and used by the compiler automatically.
 * If you add your own finally block, the IMPLICIT one runs FIRST.
 *
 *
 * ============================================================================
 * KEY INTERFACES
 * ============================================================================
 *
 * AutoCloseable (java.lang):
 * --------------------------
 *   public interface AutoCloseable {
 *       void close() throws Exception;  // Declares checked Exception
 *   }
 *
 *   - Required for try-with-resources
 *   - close() declares Exception (broad checked exception)
 *
 *
 * Closeable (java.io):
 * --------------------
 *   public interface Closeable extends AutoCloseable {
 *       void close() throws IOException;  // Declares IOException (narrower)
 *   }
 *
 *   - Extends AutoCloseable
 *   - close() declares IOException (more specific)
 *   - Important for I/O chapter (Chapter 14)
 *   - All I/O streams implement Closeable
 *
 *
 * EXAM TIP: Both interfaces have close() method, but:
 *   - AutoCloseable.close() throws Exception
 *   - Closeable.close() throws IOException
 *
 *
 * ============================================================================
 * SYNTAX AND RULES
 * ============================================================================
 *
 * BASIC SYNTAX:
 *   try (ResourceType resource = new ResourceType()) {
 *       // use resource
 *   }  // resource.close() called automatically here
 *
 * MULTIPLE RESOURCES:
 *   try (Resource1 r1 = new Resource1();
 *        Resource2 r2 = new Resource2();
 *        Resource3 r3 = new Resource3()) {  // Last semicolon OPTIONAL
 *       // use resources
 *   }  // Closed in REVERSE order: r3, r2, r1
 *
 *
 * RULES SUMMARY:
 * --------------
 * 1. Only classes implementing AutoCloseable can be used
 * 2. Resources are closed in REVERSE order of declaration
 * 3. Resources separated by semicolons; last semicolon is OPTIONAL
 * 4. Each resource must include the DATA TYPE (or var)
 * 5. Resources are in scope ONLY within the try block
 * 6. catch and finally blocks are OPTIONAL
 * 7. Implicit finally runs BEFORE programmer-coded catch/finally
 * 8. Can use var for resource declaration (local scope)
 * 9. Can use pre-declared resources if FINAL or EFFECTIVELY FINAL
 */
public class TryWithResources {

    public static void main(String[] args) {
        demonstrateBasicTryWithResources();
        demonstrateMultipleResources();
        demonstrateCloseOrder();
        demonstratePreDeclaredResources();
        demonstrateSuppressedExceptions();
    }

    // ========================================================================
    // BASIC TRY-WITH-RESOURCES
    // ========================================================================

    public static void demonstrateBasicTryWithResources() {
        System.out.println("=== BASIC TRY-WITH-RESOURCES ===\n");

        // Basic usage - resource automatically closed
        try (MyResource resource = new MyResource("Basic")) {
            resource.use();
        }  // close() called automatically here

        System.out.println();

        // With catch block (optional)
        try (MyResource resource = new MyResource("WithCatch")) {
            resource.use();
        } catch (Exception e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println();

        // With finally block (optional) - implicit finally runs FIRST
        System.out.println("Demonstrating implicit vs explicit finally order:");
        try (MyResource resource = new MyResource("WithFinally")) {
            resource.use();
        } finally {
            System.out.println("  Programmer-defined finally (runs AFTER implicit close)");
        }

        System.out.println();

        // Using var for declaration
        try (var resource = new MyResource("UsingVar")) {
            resource.use();
        }

        System.out.println("\nKey points:");
        System.out.println("  - catch and finally are OPTIONAL");
        System.out.println("  - Implicit finally (close) runs BEFORE your finally");
        System.out.println("  - Can use 'var' for resource type\n");
    }

    // ========================================================================
    // MULTIPLE RESOURCES
    // ========================================================================

    /**
     * Multiple resources are:
     * - Separated by semicolons
     * - Last semicolon is OPTIONAL
     * - Each must include data type
     * - Closed in REVERSE order
     */
    public static void demonstrateMultipleResources() {
        System.out.println("=== MULTIPLE RESOURCES ===\n");

        // Multiple resources - note semicolons
        try (MyResource r1 = new MyResource("First");
             MyResource r2 = new MyResource("Second");
             MyResource r3 = new MyResource("Third")) {  // Last semicolon OPTIONAL
            r1.use();
            r2.use();
            r3.use();
        }

        // INVALID - must include data type for each (commented out)
        // try (MyResource r1 = new MyResource("A"), r2 = new MyResource("B")) { }
        // DOES NOT COMPILE - each resource needs its own type declaration

        System.out.println("\nResources closed in REVERSE order: Third, Second, First\n");
    }

    // ========================================================================
    // CLOSE ORDER DEMONSTRATION
    // ========================================================================

    /**
     * ORDER OF OPERATIONS:
     * 1. Try block executes
     * 2. Resources closed in REVERSE order (implicit finally)
     * 3. Catch block executes (if exception)
     * 4. Programmer-defined finally executes
     */
    public static void demonstrateCloseOrder() {
        System.out.println("=== CLOSE ORDER ===\n");

        System.out.println("Order with exception:");
        try (CloseOrderResource r1 = new CloseOrderResource("R1");
             CloseOrderResource r2 = new CloseOrderResource("R2")) {
            System.out.println("  1. Try block executing");
            throw new RuntimeException("Exception in try");
        } catch (Exception e) {
            System.out.println("  4. Catch block: " + e.getMessage());
        } finally {
            System.out.println("  5. Programmer-defined finally");
        }

        System.out.println("\nOrder summary:");
        System.out.println("  1. Try block");
        System.out.println("  2. Implicit close R2 (reverse order)");
        System.out.println("  3. Implicit close R1 (reverse order)");
        System.out.println("  4. Catch block");
        System.out.println("  5. Programmer-defined finally\n");
    }

    // ========================================================================
    // PRE-DECLARED RESOURCES (FINAL OR EFFECTIVELY FINAL)
    // ========================================================================

    /**
     * RULE: You can use a pre-declared resource in try-with-resources
     *       IF the variable is FINAL or EFFECTIVELY FINAL.
     *
     * Effectively final = not reassigned after initialization
     */
    public static void demonstratePreDeclaredResources() {
        System.out.println("=== PRE-DECLARED RESOURCES ===\n");

        // Effectively final - works!
        MyResource effectivelyFinal = new MyResource("EffectivelyFinal");
        try (effectivelyFinal) {  // No type declaration needed - just variable name
            effectivelyFinal.use();
        }

        System.out.println();

        // Explicitly final - works!
        final MyResource explicitlyFinal = new MyResource("ExplicitlyFinal");
        try (explicitlyFinal) {
            explicitlyFinal.use();
        }

        System.out.println();

        // Multiple pre-declared resources
        MyResource pre1 = new MyResource("Pre1");
        MyResource pre2 = new MyResource("Pre2");
        try (pre1; pre2) {  // Separated by semicolons
            pre1.use();
            pre2.use();
        }

        // INVALID - reassigned variable is NOT effectively final (commented out)
        // MyResource notFinal = new MyResource("A");
        // notFinal = new MyResource("B");  // Reassignment!
        // try (notFinal) { }  // DOES NOT COMPILE - not effectively final

        System.out.println("\nPre-declared resource rules:");
        System.out.println("  - Must be final or effectively final");
        System.out.println("  - No type declaration needed in try()");
        System.out.println("  - Still separated by semicolons\n");
    }

    // ========================================================================
    // SUPPRESSED EXCEPTIONS
    // ========================================================================

    /**
     * SUPPRESSED EXCEPTIONS:
     * ----------------------
     * What happens when BOTH the try block AND close() throw exceptions?
     *
     * ORDER:
     * 1. Try block executes and throws exception (PRIMARY exception)
     * 2. Implicit finally runs, close() throws exception (SUPPRESSED)
     * 3. The primary exception is thrown, with suppressed exceptions attached
     *
     * RULE: The try block exception is the PRIMARY exception.
     *       The close() exceptions are SUPPRESSED and attached to it.
     *
     * To retrieve suppressed exceptions: exception.getSuppressed()
     */
    public static void demonstrateSuppressedExceptions() {
        System.out.println("=== SUPPRESSED EXCEPTIONS ===\n");

        try {
            try (ThrowingResource r1 = new ThrowingResource("R1");
                 ThrowingResource r2 = new ThrowingResource("R2")) {
                System.out.println("Try block about to throw exception...");
                throw new RuntimeException("Primary exception from try block");
            }
        } catch (Exception e) {
            System.out.println("\nPrimary exception: " + e.getMessage());

            // Retrieve suppressed exceptions
            Throwable[] suppressed = e.getSuppressed();
            System.out.println("Number of suppressed exceptions: " + suppressed.length);
            for (Throwable t : suppressed) {
                System.out.println("  Suppressed: " + t.getMessage());
            }
        }

        System.out.println("\nSuppressed exception rules:");
        System.out.println("  - Try block exception is PRIMARY");
        System.out.println("  - close() exceptions are SUPPRESSED");
        System.out.println("  - Use getSuppressed() to retrieve them");
        System.out.println("  - Close exceptions happen in reverse order\n");

        // What if only close() throws? Then that becomes the primary exception
        System.out.println("When only close() throws:");
        try {
            try (ThrowingResource r = new ThrowingResource("OnlyClose")) {
                System.out.println("Try block completes normally");
                // No exception here
            }
        } catch (Exception e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println("Suppressed count: " + e.getSuppressed().length);
        }

        System.out.println();
    }

    // ========================================================================
    // HELPER CLASSES
    // ========================================================================

    /**
     * Basic AutoCloseable implementation
     */
    static class MyResource implements AutoCloseable {
        private String name;

        public MyResource(String name) {
            this.name = name;
            System.out.println("  Opening: " + name);
        }

        public void use() {
            System.out.println("  Using: " + name);
        }

        @Override
        public void close() {  // Can throw Exception (or narrower)
            System.out.println("  Closing: " + name);
        }
    }

    /**
     * Resource that prints close order
     */
    static class CloseOrderResource implements AutoCloseable {
        private String name;

        public CloseOrderResource(String name) {
            this.name = name;
        }

        @Override
        public void close() {
            System.out.println("  2/3. Closing " + name + " (implicit finally)");
        }
    }

    /**
     * Resource that throws exception on close
     */
    static class ThrowingResource implements AutoCloseable {
        private String name;

        public ThrowingResource(String name) {
            this.name = name;
        }

        @Override
        public void close() throws Exception {
            throw new Exception("Close exception from " + name);
        }
    }

    /**
     * Example implementing Closeable (extends AutoCloseable)
     */
    static class MyCloseable implements Closeable {
        @Override
        public void close() throws IOException {  // Must throw IOException or narrower
            System.out.println("Closeable resource closed");
        }
    }

    // ========================================================================
    // EXAM TRAPS SUMMARY
    // ========================================================================

    /**
     * EXAM TRAPS:
     * ===========
     *
     * 1. AUTOCLOSEABLE REQUIRED
     *    try (String s = "test") { }  // DOES NOT COMPILE - String not AutoCloseable
     *
     * 2. MUST INCLUDE TYPE FOR EACH RESOURCE
     *    try (MyResource r1 = new MyResource(), r2 = new MyResource()) { }
     *    // DOES NOT COMPILE - r2 needs type declaration
     *
     * 3. EFFECTIVELY FINAL FOR PRE-DECLARED
     *    MyResource r = new MyResource();
     *    r = new MyResource();  // Reassignment
     *    try (r) { }  // DOES NOT COMPILE - not effectively final
     *
     * 4. RESOURCES OUT OF SCOPE AFTER TRY
     *    try (MyResource r = new MyResource()) {
     *        r.use();
     *    }
     *    r.use();  // DOES NOT COMPILE - r is out of scope
     *
     * 5. CLOSE ORDER IS REVERSE
     *    try (R1; R2; R3) { }  // Closed: R3, R2, R1
     *
     * 6. IMPLICIT FINALLY BEFORE EXPLICIT
     *    try (resource) { }
     *    catch (E e) { }   // Resource already closed here!
     *    finally { }       // Resource already closed here!
     *
     * 7. PRIMARY VS SUPPRESSED EXCEPTIONS
     *    - Try block exception = PRIMARY
     *    - close() exceptions = SUPPRESSED
     *    - getSuppressed() returns Throwable[]
     *
     * 8. INTERFACES TO KNOW
     *    - AutoCloseable: close() throws Exception
     *    - Closeable: close() throws IOException (extends AutoCloseable)
     */
}
