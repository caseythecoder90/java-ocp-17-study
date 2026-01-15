package ch10streams;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Optional Class
 *
 * ===== PURPOSE =====
 *
 * Optional<T> is a container that may or may not contain a non-null value.
 * It provides a cleaner alternative to returning null and helps avoid NullPointerException.
 *
 * Instead of:
 *   String result = getValue();        // might return null
 *   if (result != null) { ... }        // must check for null
 *
 * Use:
 *   Optional<String> result = getValue();
 *   result.ifPresent(s -> ...);        // only executes if value present
 *
 * ===== CREATING OPTIONAL INSTANCES (STATIC METHODS) =====
 *
 * | Method                      | Description                                    |
 * |-----------------------------|------------------------------------------------|
 * | Optional.empty()            | Creates empty Optional                         |
 * | Optional.of(value)          | Creates Optional with non-null value           |
 * |                             | Throws NullPointerException if value is null!  |
 * | Optional.ofNullable(value)  | Creates Optional; empty if value is null       |
 *
 * ===== INSTANCE METHODS (EXAM FOCUS) =====
 *
 * | Method                      | Value Present          | Value Empty                    |
 * |-----------------------------|------------------------|--------------------------------|
 * | get()                       | Returns value          | NoSuchElementException         |
 * | isPresent()                 | Returns true           | Returns false                  |
 * | ifPresent(Consumer)         | Executes Consumer      | Does nothing                   |
 * | orElse(T other)             | Returns value          | Returns other                  |
 * | orElseGet(Supplier)         | Returns value          | Returns Supplier result        |
 * | orElseThrow()               | Returns value          | NoSuchElementException         |
 * | orElseThrow(Supplier)       | Returns value          | Throws Supplier's exception    |
 */
public class OptionalClass {

    public static void main(String[] args) {
        demonstrateCreatingOptionals();
        demonstrateGetAndIsPresent();
        demonstrateIfPresent();
        demonstrateOrElse();
        demonstrateOrElseGet();
        demonstrateOrElseThrow();
    }

    // =====================================================================
    // CREATING OPTIONAL INSTANCES
    // =====================================================================

    public static void demonstrateCreatingOptionals() {
        System.out.println("=== CREATING OPTIONAL INSTANCES ===\n");

        // ----- Optional.empty() -----
        // Signature: static <T> Optional<T> empty()
        // Creates an empty Optional (no value)
        Optional<String> empty = Optional.empty();
        System.out.println("Optional.empty(): " + empty);
        System.out.println("  isPresent: " + empty.isPresent());

        // ----- Optional.of(value) -----
        // Signature: static <T> Optional<T> of(T value)
        // Creates Optional containing the non-null value
        // THROWS NullPointerException if value is null!
        Optional<String> hasValue = Optional.of("Hello");
        System.out.println("\nOptional.of(\"Hello\"): " + hasValue);
        System.out.println("  isPresent: " + hasValue.isPresent());

        // Optional.of(null) throws NullPointerException!
        // Optional<String> willThrow = Optional.of(null);  // NullPointerException!

        // ----- Optional.ofNullable(value) -----
        // Signature: static <T> Optional<T> ofNullable(T value)
        // Creates Optional with value if non-null, or empty Optional if null
        // SAFE for null values - won't throw exception
        String nullValue = null;
        String nonNullValue = "World";

        Optional<String> fromNull = Optional.ofNullable(nullValue);
        Optional<String> fromNonNull = Optional.ofNullable(nonNullValue);

        System.out.println("\nOptional.ofNullable(null): " + fromNull);
        System.out.println("  isPresent: " + fromNull.isPresent());
        System.out.println("Optional.ofNullable(\"World\"): " + fromNonNull);
        System.out.println("  isPresent: " + fromNonNull.isPresent());

        // EXAM TIP: Use ofNullable() when value might be null
        //           Use of() only when you're CERTAIN value is non-null
    }

    // =====================================================================
    // get() and isPresent()
    // =====================================================================

    /**
     * T get()
     * - Value Present: Returns the value
     * - Value Empty:   Throws NoSuchElementException
     *
     * boolean isPresent()
     * - Value Present: Returns true
     * - Value Empty:   Returns false
     */
    public static void demonstrateGetAndIsPresent() {
        System.out.println("\n=== get() and isPresent() ===\n");

        Optional<String> hasValue = Optional.of("Hello");
        Optional<String> empty = Optional.empty();

        // ----- isPresent() -----
        System.out.println("hasValue.isPresent(): " + hasValue.isPresent());  // true
        System.out.println("empty.isPresent(): " + empty.isPresent());        // false

        // ----- get() -----
        // Only call get() after checking isPresent(), or use orElse methods instead
        if (hasValue.isPresent()) {
            String value = hasValue.get();
            System.out.println("\nhasValue.get(): " + value);  // "Hello"
        }

        // get() on empty Optional throws NoSuchElementException
        try {
            String value = empty.get();  // THROWS!
        } catch (java.util.NoSuchElementException e) {
            System.out.println("empty.get(): NoSuchElementException thrown!");
        }

        // EXAM TIP: Avoid using get() directly - prefer orElse, orElseGet, or orElseThrow
        //           If you must use get(), always check isPresent() first
    }

    // =====================================================================
    // ifPresent(Consumer)
    // =====================================================================

    /**
     * void ifPresent(Consumer<? super T> action)
     * - Value Present: Executes the Consumer with the value
     * - Value Empty:   Does nothing (no exception, no action)
     */
    public static void demonstrateIfPresent() {
        System.out.println("\n=== ifPresent(Consumer) ===\n");

        Optional<String> hasValue = Optional.of("Hello");
        Optional<String> empty = Optional.empty();

        // Consumer executes only if value is present
        System.out.print("hasValue.ifPresent(): ");
        hasValue.ifPresent(s -> System.out.println("Value is: " + s));

        System.out.print("empty.ifPresent(): ");
        empty.ifPresent(s -> System.out.println("Value is: " + s));
        System.out.println("(nothing printed - Consumer not executed)");

        // Common use case: perform action only when value exists
        Optional<String> username = Optional.of("john_doe");
        username.ifPresent(name -> System.out.println("Welcome, " + name + "!"));

        // No need for null checks or isPresent() checks
        Optional<String> guest = Optional.empty();
        guest.ifPresent(name -> System.out.println("This won't print"));
    }

    // =====================================================================
    // orElse(T other)
    // =====================================================================

    /**
     * T orElse(T other)
     * - Value Present: Returns the value
     * - Value Empty:   Returns the 'other' value provided
     *
     * NOTE: The 'other' value is ALWAYS evaluated, even if Optional has a value!
     */
    public static void demonstrateOrElse() {
        System.out.println("\n=== orElse(T other) ===\n");

        Optional<String> hasValue = Optional.of("Hello");
        Optional<String> empty = Optional.empty();

        // Returns value if present
        String result1 = hasValue.orElse("Default");
        System.out.println("hasValue.orElse(\"Default\"): " + result1);  // "Hello"

        // Returns 'other' if empty
        String result2 = empty.orElse("Default");
        System.out.println("empty.orElse(\"Default\"): " + result2);     // "Default"

        // IMPORTANT: orElse() always evaluates the default value!
        System.out.println("\n--- orElse() always evaluates default ---");
        String result3 = hasValue.orElse(getExpensiveDefault());
        // "Computing expensive default..." prints even though hasValue has a value!

        // Use orElseGet() if default is expensive to compute
    }

    private static String getExpensiveDefault() {
        System.out.println("  (Computing expensive default...)");
        return "Expensive Default";
    }

    // =====================================================================
    // orElseGet(Supplier)
    // =====================================================================

    /**
     * T orElseGet(Supplier<? extends T> supplier)
     * - Value Present: Returns the value (Supplier NOT called)
     * - Value Empty:   Returns value from Supplier
     *
     * DIFFERENCE FROM orElse():
     * - orElse(T): Default value is ALWAYS evaluated
     * - orElseGet(Supplier): Supplier is ONLY called when Optional is empty
     *
     * Use orElseGet() when default value is expensive to compute!
     */
    public static void demonstrateOrElseGet() {
        System.out.println("\n=== orElseGet(Supplier) ===\n");

        Optional<String> hasValue = Optional.of("Hello");
        Optional<String> empty = Optional.empty();

        // Returns value if present - Supplier NOT called
        String result1 = hasValue.orElseGet(() -> "Default from Supplier");
        System.out.println("hasValue.orElseGet(): " + result1);  // "Hello"

        // Returns Supplier result if empty
        String result2 = empty.orElseGet(() -> "Default from Supplier");
        System.out.println("empty.orElseGet(): " + result2);     // "Default from Supplier"

        // Supplier is lazy - only called when needed
        System.out.println("\n--- orElseGet() is lazy ---");
        String result3 = hasValue.orElseGet(() -> {
            System.out.println("  (This won't print - Supplier not called!)");
            return "Never used";
        });
        System.out.println("Supplier was NOT called because value was present");

        // Use for expensive operations
        Optional<String> config = Optional.empty();
        String value = config.orElseGet(() -> loadFromDatabase());
        System.out.println("\nLoaded config: " + value);
    }

    private static String loadFromDatabase() {
        System.out.println("  (Loading from database...)");
        return "DB_DEFAULT_VALUE";
    }

    // =====================================================================
    // orElseThrow() and orElseThrow(Supplier)
    // =====================================================================

    /**
     * T orElseThrow()
     * - Value Present: Returns the value
     * - Value Empty:   Throws NoSuchElementException
     *
     * T orElseThrow(Supplier<? extends X> exceptionSupplier)
     * - Value Present: Returns the value
     * - Value Empty:   Throws exception created by Supplier
     */
    public static void demonstrateOrElseThrow() {
        System.out.println("\n=== orElseThrow() ===\n");

        Optional<String> hasValue = Optional.of("Hello");
        Optional<String> empty = Optional.empty();

        // ----- orElseThrow() - no args -----
        // Returns value if present
        String result1 = hasValue.orElseThrow();
        System.out.println("hasValue.orElseThrow(): " + result1);  // "Hello"

        // Throws NoSuchElementException if empty
        try {
            String result2 = empty.orElseThrow();
        } catch (java.util.NoSuchElementException e) {
            System.out.println("empty.orElseThrow(): NoSuchElementException thrown!");
        }

        // ----- orElseThrow(Supplier) - custom exception -----
        System.out.println("\n--- orElseThrow(Supplier) ---");

        // Returns value if present - Supplier NOT called
        String result3 = hasValue.orElseThrow(() -> new RuntimeException("Not found"));
        System.out.println("hasValue.orElseThrow(Supplier): " + result3);

        // Throws custom exception if empty
        try {
            String result4 = empty.orElseThrow(
                () -> new IllegalStateException("Value was not present!")
            );
        } catch (IllegalStateException e) {
            System.out.println("empty.orElseThrow(Supplier): " + e.getMessage());
        }

        // Common pattern: throw meaningful exceptions
        Optional<String> userId = Optional.empty();
        try {
            String id = userId.orElseThrow(
                () -> new IllegalArgumentException("User ID is required")
            );
        } catch (IllegalArgumentException e) {
            System.out.println("\nCustom exception: " + e.getMessage());
        }
    }
}

// =====================================================================
// QUICK REFERENCE SUMMARY
// =====================================================================

/**
 * CREATING OPTIONALS:
 *
 * Optional.empty()           -> Empty Optional
 * Optional.of(value)         -> Optional with value (NPE if null!)
 * Optional.ofNullable(value) -> Optional with value or empty if null
 *
 * RETRIEVING VALUES:
 *
 * | Method                | Has Value       | Empty                          |
 * |-----------------------|-----------------|--------------------------------|
 * | get()                 | Returns value   | NoSuchElementException         |
 * | orElse(other)         | Returns value   | Returns other (always eval'd)  |
 * | orElseGet(Supplier)   | Returns value   | Calls Supplier (lazy)          |
 * | orElseThrow()         | Returns value   | NoSuchElementException         |
 * | orElseThrow(Supplier) | Returns value   | Throws Supplier's exception    |
 *
 * CHECKING/CONSUMING:
 *
 * | Method                | Has Value       | Empty                          |
 * |-----------------------|-----------------|--------------------------------|
 * | isPresent()           | true            | false                          |
 * | ifPresent(Consumer)   | Runs Consumer   | Does nothing                   |
 *
 * EXAM TIPS:
 * - Optional.of(null) throws NullPointerException
 * - get() on empty throws NoSuchElementException
 * - orElse() ALWAYS evaluates default; orElseGet() is lazy
 * - Prefer orElseGet() for expensive defaults
 * - Prefer orElseThrow() over get() for clearer intent
 */
