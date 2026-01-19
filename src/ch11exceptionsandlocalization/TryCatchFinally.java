package ch11exceptionsandlocalization;

/**
 * TRY-CATCH-FINALLY BLOCKS
 * ========================
 *
 * BASIC RULES:
 * ------------
 * 1. Try block BRACES are REQUIRED (even for single statement)
 * 2. Normal try (not try-with-resources) REQUIRES a catch OR finally block
 * 3. Catch blocks are checked in ORDER they appear
 * 4. Subclass exceptions must come BEFORE superclass exceptions
 * 5. Finally block ALWAYS runs (regardless of exception)
 * 6. If both catch and finally present, catch must come BEFORE finally
 *
 *
 * VALID TRY STRUCTURES:
 * ---------------------
 *   try { } catch (Exception e) { }                    // try + catch
 *   try { } finally { }                                // try + finally (no catch required!)
 *   try { } catch (Exception e) { } finally { }       // try + catch + finally
 *   try { } catch (A a) { } catch (B b) { }           // try + multiple catches
 *   try { } catch (A | B e) { }                       // try + multi-catch
 *
 *
 * INVALID TRY STRUCTURES:
 * -----------------------
 *   try { }                          // DOES NOT COMPILE - needs catch or finally
 *   try { } catch Exception e { }    // DOES NOT COMPILE - braces required around type
 *   try catch (Exception e) { }      // DOES NOT COMPILE - braces required for try
 */
public class TryCatchFinally {

    public static void main(String[] args) {
        demonstrateTryBraces();
        demonstrateCatchOrder();
        demonstrateCatchScope();
        demonstrateMultiCatch();
        demonstrateFinally();
        demonstrateFinallyWithReturn();
    }

    // ========================================================================
    // TRY BLOCK - BRACES REQUIRED
    // ========================================================================

    public static void demonstrateTryBraces() {
        System.out.println("=== TRY BLOCK - BRACES REQUIRED ===\n");

        // VALID - braces present
        try {
            System.out.println("Single statement still needs braces");
        } catch (Exception e) {
            // handle
        }

        // INVALID examples (commented out):
        // try                              // DOES NOT COMPILE
        //     System.out.println("Hi");
        // catch (Exception e) { }

        // try { }                          // DOES NOT COMPILE - needs catch or finally

        System.out.println("Try block always requires braces { }");
        System.out.println("Normal try requires catch OR finally block\n");
    }

    // ========================================================================
    // CATCH BLOCK ORDER - SUBCLASS BEFORE SUPERCLASS
    // ========================================================================

    /**
     * CATCH BLOCK ORDER RULES:
     * ------------------------
     * Java checks catch blocks in the ORDER they appear.
     * If a superclass catch comes before a subclass catch, the subclass
     * catch is UNREACHABLE and causes a COMPILATION ERROR.
     *
     * The error appears on the LINE with the UNREACHABLE catch block.
     *
     * Remember the hierarchy:
     *   Exception
     *       |-- IOException
     *       |       |-- FileNotFoundException
     *       |
     *       |-- RuntimeException
     *               |-- IllegalArgumentException
     *                       |-- NumberFormatException
     */
    public static void demonstrateCatchOrder() {
        System.out.println("=== CATCH BLOCK ORDER ===\n");

        // CORRECT ORDER - subclass before superclass
        try {
            // Method that could throw FileNotFoundException OR other IOException
            readFile();
        } catch (java.io.FileNotFoundException e) {    // More specific FIRST
            System.out.println("Caught FileNotFoundException");
        } catch (java.io.IOException e) {              // Less specific SECOND
            System.out.println("Caught IOException");
        } catch (Exception e) {                        // Most general LAST
            System.out.println("Caught Exception");
        }

        // INCORRECT ORDER - would not compile (commented out)
        // try {
        //     throw new java.io.FileNotFoundException("test");
        // } catch (java.io.IOException e) {              // Catches all IOExceptions
        //     System.out.println("Caught IOException");
        // } catch (java.io.FileNotFoundException e) {    // DOES NOT COMPILE
        //     // Unreachable - FileNotFoundException is already caught above!
        //     // Compilation error appears on THIS line
        // }

        // INCORRECT - Exception catches everything
        // try {
        //     // some code
        // } catch (Exception e) {                        // Catches ALL exceptions
        //     System.out.println("Caught Exception");
        // } catch (RuntimeException e) {                 // DOES NOT COMPILE
        //     // Unreachable - RuntimeException is subclass of Exception
        // }

        System.out.println("\nRULE: Subclass exceptions must come BEFORE superclass exceptions");
        System.out.println("ERROR: Appears on the line with the unreachable catch block\n");

        // Unrelated exceptions can be in any order
        try {
            int x = 1;
        } catch (ArithmeticException e) {
            // OK
        } catch (ArrayIndexOutOfBoundsException e) {
            // OK - not related to ArithmeticException, order doesn't matter
        }

        System.out.println("Unrelated exceptions can be in any order\n");
    }

    // ========================================================================
    // CATCH BLOCK SCOPE
    // ========================================================================

    /**
     * Exception variable is ONLY in scope within its catch block.
     * Cannot access it outside that specific catch block.
     */
    public static void demonstrateCatchScope() {
        System.out.println("=== CATCH BLOCK SCOPE ===\n");

        try {
            throw new RuntimeException("Test");
        } catch (RuntimeException e) {
            System.out.println("Inside catch: " + e.getMessage());  // e is in scope here
        }
        // System.out.println(e);  // DOES NOT COMPILE - e is out of scope

        // Each catch has its own scope
        try {
            throw new Exception("Test");
        } catch (RuntimeException e) {
            System.out.println("RuntimeException: " + e);  // This 'e' is different
        } catch (Exception e) {
            System.out.println("Exception: " + e);         // This 'e' is also different
        }

        System.out.println("Exception variable only in scope within its catch block\n");
    }

    // ========================================================================
    // MULTI-CATCH BLOCKS
    // ========================================================================

    /**
     * MULTI-CATCH SYNTAX:
     * -------------------
     *   catch (ExceptionA | ExceptionB | ExceptionC e) { }
     *
     * RULES:
     * 1. Use pipe | character to separate exception types
     * 2. Variable name appears at the END, only ONCE
     * 3. Exception types CANNOT be related (no subclass/superclass)
     * 4. Reduces code duplication when handling multiple exceptions the same way
     *
     * IMPORTANT: The exception variable in multi-catch is EFFECTIVELY FINAL
     *            You cannot reassign it!
     */
    public static void demonstrateMultiCatch() {
        System.out.println("=== MULTI-CATCH BLOCKS ===\n");

        // VALID multi-catch - unrelated exceptions
        try {
            if (Math.random() > 0.5) {
                throw new ArithmeticException("Math error");
            } else {
                throw new ArrayIndexOutOfBoundsException("Index error");
            }
        } catch (ArithmeticException | ArrayIndexOutOfBoundsException e) {
            // Single variable 'e' at the end
            System.out.println("Caught: " + e.getClass().getSimpleName());
        }

        // INVALID - variable name must be at end, only once (commented out)
        // catch (ArithmeticException e | ArrayIndexOutOfBoundsException e) { }  // DOES NOT COMPILE
        // catch (ArithmeticException e | ArrayIndexOutOfBoundsException) { }    // DOES NOT COMPILE

        // INVALID - redundant types / related exceptions (commented out)
        // catch (FileNotFoundException | IOException e) { }  // DOES NOT COMPILE
        // FileNotFoundException is subclass of IOException - redundant!

        // catch (Exception | RuntimeException e) { }  // DOES NOT COMPILE
        // RuntimeException is subclass of Exception - redundant!

        System.out.println("\nMulti-catch rules:");
        System.out.println("  - Use | (pipe) to separate types");
        System.out.println("  - Variable name at END, only ONCE");
        System.out.println("  - Types CANNOT be related (no subclass/superclass)");
        System.out.println("  - Variable is effectively final (cannot reassign)\n");

        // Multi-catch variable is effectively final
        try {
            throw new RuntimeException();
        } catch (RuntimeException e) {
            e = new RuntimeException();  // OK - single catch, can reassign
        }

        try {
            throw new IllegalStateException();
        } catch (ArithmeticException | IllegalStateException e) {
            // e = new RuntimeException();  // DOES NOT COMPILE - effectively final!
            System.out.println("Multi-catch variable is effectively final");
        }

        System.out.println();
    }

    // ========================================================================
    // FINALLY BLOCK
    // ========================================================================

    /**
     * FINALLY BLOCK RULES:
     * --------------------
     * 1. ALWAYS runs, regardless of whether exception occurs
     * 2. Runs even if catch block throws an exception
     * 3. Runs even if try or catch has a return statement
     * 4. Catch block is NOT required if finally is present
     * 5. If both catch and finally present, catch must come BEFORE finally
     */
    public static void demonstrateFinally() {
        System.out.println("=== FINALLY BLOCK ===\n");

        // Finally runs when no exception
        System.out.println("Case 1: No exception");
        try {
            System.out.println("  try block");
        } catch (Exception e) {
            System.out.println("  catch block");
        } finally {
            System.out.println("  finally block - ALWAYS RUNS");
        }

        // Finally runs when exception is caught
        System.out.println("\nCase 2: Exception caught");
        try {
            throw new RuntimeException("test");
        } catch (RuntimeException e) {
            System.out.println("  catch block - caught exception");
        } finally {
            System.out.println("  finally block - ALWAYS RUNS");
        }

        // Finally runs even without catch block
        System.out.println("\nCase 3: Try + Finally (no catch)");
        try {
            System.out.println("  try block");
        } finally {
            System.out.println("  finally block - catch is optional!");
        }

        // INVALID - finally must come after catch (commented out)
        // try { }
        // finally { }
        // catch (Exception e) { }  // DOES NOT COMPILE - finally must be last

        System.out.println("\nFinally rules:");
        System.out.println("  - Always runs (even with return statements)");
        System.out.println("  - Catch is optional if finally is present");
        System.out.println("  - Order must be: try -> catch(es) -> finally\n");
    }

    // ========================================================================
    // FINALLY WITH RETURN STATEMENTS
    // ========================================================================

    /**
     * EXAM TRAP: Finally runs even when return is in try or catch!
     * If finally also has a return, it OVERRIDES the try/catch return value.
     */
    public static void demonstrateFinallyWithReturn() {
        System.out.println("=== FINALLY WITH RETURN ===\n");

        System.out.println("getValue() returns: " + getValue());
        System.out.println("(finally's return overrides try's return)\n");

        System.out.println("getValueWithException() returns: " + getValueWithException());
        System.out.println("(finally's return overrides catch's return)\n");
    }

    public static int getValue() {
        try {
            return 1;  // This return is scheduled
        } finally {
            return 2;  // But finally runs and overrides with 2!
        }
    }

    public static int getValueWithException() {
        try {
            throw new RuntimeException();
        } catch (RuntimeException e) {
            return 1;  // This return is scheduled
        } finally {
            return 2;  // But finally runs and overrides with 2!
        }
    }

    // Helper method that declares IOException
    private static void readFile() throws java.io.IOException {
        throw new java.io.FileNotFoundException("file.txt");
    }

    // ========================================================================
    // SUMMARY AND EXAM TRAPS
    // ========================================================================

    /**
     * EXAM TRAPS SUMMARY:
     * ===================
     *
     * 1. BRACES REQUIRED
     *    try System.out.println("Hi");  // DOES NOT COMPILE
     *
     * 2. CATCH OR FINALLY REQUIRED (for normal try)
     *    try { }  // DOES NOT COMPILE
     *
     * 3. CATCH ORDER - SUBCLASS FIRST
     *    catch (IOException e) { }
     *    catch (FileNotFoundException e) { }  // DOES NOT COMPILE - unreachable!
     *    // Error appears on the FileNotFoundException line
     *
     * 4. MULTI-CATCH - NO RELATED TYPES
     *    catch (IOException | FileNotFoundException e) { }  // DOES NOT COMPILE
     *
     * 5. MULTI-CATCH VARIABLE IS EFFECTIVELY FINAL
     *    catch (A | B e) { e = new A(); }  // DOES NOT COMPILE
     *
     * 6. FINALLY OVERRIDES RETURN
     *    try { return 1; } finally { return 2; }  // Returns 2!
     *
     * 7. ORDER MUST BE: try -> catch(es) -> finally
     *    try { } finally { } catch (E e) { }  // DOES NOT COMPILE
     */
}
