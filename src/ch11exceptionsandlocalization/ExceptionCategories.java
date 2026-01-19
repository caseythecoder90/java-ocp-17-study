package ch11exceptionsandlocalization;

/**
 * EXCEPTION CATEGORIES AND HIERARCHY
 * ==================================
 *
 * EXCEPTION HIERARCHY (java.lang package):
 * ----------------------------------------
 *
 *                      Throwable (CHECKED)
 *                      /              \
 *                     /                \
 *              Exception (CHECKED)     Error (UNCHECKED)
 *                    |                      |
 *                    |                      +-- ExceptionInInitializerError
 *            RuntimeException (UNCHECKED)   +-- StackOverflowError
 *                    |                      +-- NoClassDefFoundError
 *                    +-- ArithmeticException
 *                    +-- ArrayIndexOutOfBoundsException
 *                    +-- ClassCastException
 *                    +-- NullPointerException
 *                    +-- IllegalArgumentException
 *                              |
 *                              +-- NumberFormatException
 *
 * CHECKED EXCEPTIONS (must handle or declare):
 * --------------------------------------------
 *     Exception
 *         |
 *         +-- IOException
 *         |       |
 *         |       +-- FileNotFoundException
 *         |       +-- NotSerializableException
 *         |
 *         +-- ParseException
 *         +-- SQLException
 *
 *
 * ============================================================================
 * CHECKED vs UNCHECKED EXCEPTIONS
 * ============================================================================
 *
 * CHECKED EXCEPTIONS:
 * - Throwable and Exception (and their subclasses, EXCEPT RuntimeException)
 * - MUST be handled or declared by application code (Handle or Declare Rule)
 * - Compiler enforces this rule
 * - Examples: IOException, SQLException, ParseException, FileNotFoundException
 *
 * UNCHECKED EXCEPTIONS:
 * - RuntimeException and all its subclasses
 * - Error and all its subclasses
 * - Do NOT have to be handled or declared
 * - Compiler does not enforce handling
 * - Examples: NullPointerException, ArithmeticException, ArrayIndexOutOfBoundsException
 *
 *
 * ============================================================================
 * throw vs throws KEYWORDS
 * ============================================================================
 *
 * throw - Used to THROW an exception object
 *         Appears INSIDE a method body
 *         Followed by an exception OBJECT (instance)
 *
 *         Example: throw new IllegalArgumentException("Invalid value");
 *
 * throws - Used to DECLARE that a method might throw an exception
 *          Appears in the METHOD SIGNATURE (after parameters)
 *          Followed by exception CLASS NAMES (can list multiple, comma-separated)
 *
 *          Example: public void readFile() throws IOException, SQLException { }
 *
 *
 * ============================================================================
 * HANDLE OR DECLARE RULE (for checked exceptions)
 * ============================================================================
 *
 * When code throws a checked exception, you must either:
 *
 * 1. HANDLE it - wrap in try-catch block
 *    try {
 *        readFile();
 *    } catch (IOException e) {
 *        // handle the exception
 *    }
 *
 * 2. DECLARE it - add throws clause to method signature
 *    public void myMethod() throws IOException {
 *        readFile();  // caller must now handle or declare
 *    }
 *
 * IMPORTANT: Even just DECLARING a checked exception (without actually throwing it)
 * is enough for the compiler to require the caller to handle or declare it!
 *
 *
 * ============================================================================
 * UNREACHABLE CODE - COMPILATION ERROR
 * ============================================================================
 *
 * EXAM TRAP: Unreachable code causes a COMPILATION ERROR
 *
 * Scenario 1: Catching a checked exception that cannot be thrown
 * ---------------------------------------------------------------
 * public void doSomething() {
 *     try {
 *         System.out.println("Hello");  // This code CANNOT throw IOException
 *     } catch (IOException e) {         // DOES NOT COMPILE - unreachable catch block
 *         // unreachable
 *     }
 * }
 *
 * NOTE: This rule only applies to CHECKED exceptions.
 *       You CAN catch RuntimeException even if code doesn't explicitly throw it
 *       (because any code could potentially throw an unchecked exception).
 *
 * Scenario 2: Code after throw statement
 * --------------------------------------
 * public void test() {
 *     throw new RuntimeException();
 *     System.out.println("Hello");  // DOES NOT COMPILE - unreachable
 * }
 *
 *
 * ============================================================================
 * OVERRIDING METHODS WITH EXCEPTIONS
 * ============================================================================
 *
 * RULE: An overriding method may NOT declare any NEW or BROADER checked
 *       exceptions than the method it inherits.
 *
 * The overriding method CAN:
 * - Declare the SAME checked exceptions
 * - Declare FEWER checked exceptions
 * - Declare a SUBCLASS of a declared checked exception
 * - Declare NO checked exceptions at all
 * - Declare any UNCHECKED exceptions (RuntimeException, Error)
 *
 * The overriding method CANNOT:
 * - Declare a NEW checked exception not in parent
 * - Declare a BROADER (superclass) checked exception
 *
 * Example:
 *     class Parent {
 *         void read() throws IOException { }
 *     }
 *
 *     class Child extends Parent {
 *         void read() throws FileNotFoundException { }  // OK - narrower (subclass)
 *         // void read() throws Exception { }          // DOES NOT COMPILE - broader
 *         // void read() throws SQLException { }       // DOES NOT COMPILE - new exception
 *     }
 */
public class ExceptionCategories {

    public static void main(String[] args) {
        demonstrateRuntimeExceptions();
        demonstrateCheckedExceptions();
        demonstrateErrors();
    }

    // ========================================================================
    // RUNTIME EXCEPTIONS (UNCHECKED) - Can be thrown by JVM or Programmer
    // ========================================================================

    /**
     * RuntimeException subclasses - UNCHECKED
     * These do NOT have to be handled or declared.
     * Can be thrown by JVM or by programmer code.
     */
    public static void demonstrateRuntimeExceptions() {
        System.out.println("=== RUNTIME EXCEPTIONS (UNCHECKED) ===\n");

        // ---------------------------------------------------------------------
        // ArithmeticException
        // - Thrown by: JVM
        // - Cause: Division by zero (integer division)
        // ---------------------------------------------------------------------
        System.out.println("1. ArithmeticException - thrown by JVM for division by 0");
        try {
            int result = 10 / 0;  // JVM throws ArithmeticException
        } catch (ArithmeticException e) {
            System.out.println("   Caught: " + e.getMessage());
        }

        // ---------------------------------------------------------------------
        // ArrayIndexOutOfBoundsException
        // - Thrown by: JVM
        // - Cause: Accessing an invalid array index
        // ---------------------------------------------------------------------
        System.out.println("\n2. ArrayIndexOutOfBoundsException - thrown by JVM for invalid array index");
        try {
            int[] arr = {1, 2, 3};
            int value = arr[5];  // JVM throws ArrayIndexOutOfBoundsException
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("   Caught: " + e.getMessage());
        }

        // ---------------------------------------------------------------------
        // ClassCastException
        // - Thrown by: JVM
        // - Cause: Attempting an impossible cast
        // ---------------------------------------------------------------------
        System.out.println("\n3. ClassCastException - thrown by JVM for impossible casts");
        try {
            Object obj = "Hello";
            Integer num = (Integer) obj;  // JVM throws ClassCastException
        } catch (ClassCastException e) {
            System.out.println("   Caught: " + e.getMessage());
        }

        // ---------------------------------------------------------------------
        // NullPointerException
        // - Thrown by: JVM
        // - Cause: Calling instance method/field on a null reference
        //
        // EXAM TIP: Compile with -g:vars flag to see variable names in
        //           NullPointerException messages!
        //           Example: javac -g:vars MyClass.java
        //           This helps identify WHICH variable was null.
        // ---------------------------------------------------------------------
        System.out.println("\n4. NullPointerException - thrown by JVM when accessing null reference");
        System.out.println("   EXAM TIP: Compile with -g:vars flag to see variable names in NPE messages");
        try {
            String str = null;
            int len = str.length();  // JVM throws NullPointerException
        } catch (NullPointerException e) {
            System.out.println("   Caught: " + e.getMessage());
        }

        // ---------------------------------------------------------------------
        // IllegalArgumentExceptionI
        // - Thrown by: PROGRAMMER
        // - Cause: Method receives an argument that violates its contract
        // - Common use: Validating method parameters
        // ---------------------------------------------------------------------
        System.out.println("\n5. IllegalArgumentException - thrown by PROGRAMMER for invalid arguments");
        try {
            setAge(-5);  // Programmer throws IllegalArgumentException
        } catch (IllegalArgumentException e) {
            System.out.println("   Caught: " + e.getMessage());
        }

        // ---------------------------------------------------------------------
        // NumberFormatException
        // - Thrown by: JAVA (parsing methods)
        // - Cause: Invalid string passed to number parsing method
        // - NOTE: This is a SUBCLASS of IllegalArgumentException
        // ---------------------------------------------------------------------
        System.out.println("\n6. NumberFormatException - thrown by Java for invalid number strings");
        System.out.println("   (Subclass of IllegalArgumentException)");
        try {
            int num = Integer.parseInt("abc");  // Java throws NumberFormatException
        } catch (NumberFormatException e) {
            System.out.println("   Caught: " + e.getMessage());
        }

        System.out.println();
    }

    // Helper method demonstrating IllegalArgumentException
    public static void setAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative: " + age);
        }
    }

    // ========================================================================
    // CHECKED EXCEPTIONS - Must be handled or declared
    // ========================================================================

    /**
     * CHECKED EXCEPTIONS - Must handle or declare
     *
     * IOException subclasses:
     * - FileNotFoundException
     * - NotSerializableException
     *
     * Other checked exceptions:
     * - ParseException
     * - SQLException
     */
    public static void demonstrateCheckedExceptions() {
        System.out.println("=== CHECKED EXCEPTIONS ===\n");

        System.out.println("IOException and its subclasses:");
        System.out.println("  - IOException (parent)");
        System.out.println("      |-- FileNotFoundException");
        System.out.println("      |-- NotSerializableException");
        System.out.println();
        System.out.println("Other checked exceptions:");
        System.out.println("  - ParseException");
        System.out.println("  - SQLException");
        System.out.println();

        // These would require try-catch or throws declaration:
        // FileInputStream fis = new FileInputStream("file.txt");  // throws FileNotFoundException
        // Connection conn = DriverManager.getConnection(url);     // throws SQLException
        // Date date = new SimpleDateFormat("yyyy").parse("abc");  // throws ParseException
    }

    // ========================================================================
    // ERROR CLASSES - Unchecked, typically thrown by JVM
    // ========================================================================

    /**
     * ERROR CLASSES - Unchecked
     * Usually indicate serious problems that applications should not try to catch.
     *
     * ExceptionInInitializerError - Thrown when static initializer throws exception
     * StackOverflowError - Thrown when call stack overflows (usually infinite recursion)
     * NoClassDefFoundError - Thrown when JVM cannot find a class definition
     */
    public static void demonstrateErrors() {
        System.out.println("=== ERROR CLASSES (UNCHECKED) ===\n");

        // ---------------------------------------------------------------------
        // ExceptionInInitializerError
        // - Thrown by: JVM
        // - Cause: Static initializer block throws an exception
        // ---------------------------------------------------------------------
        System.out.println("1. ExceptionInInitializerError");
        System.out.println("   Thrown by JVM when static initializer throws an exception");
        System.out.println("   Example:");
        System.out.println("   class BadInit {");
        System.out.println("       static { int x = 1/0; }  // Causes ExceptionInInitializerError");
        System.out.println("   }");

        // ---------------------------------------------------------------------
        // StackOverflowError
        // - Thrown by: JVM
        // - Cause: Call stack exceeds memory limit (usually infinite recursion)
        // ---------------------------------------------------------------------
        System.out.println("\n2. StackOverflowError");
        System.out.println("   Thrown by JVM when call stack overflows");
        System.out.println("   Example:");
        System.out.println("   void infinite() { infinite(); }  // Causes StackOverflowError");

        // ---------------------------------------------------------------------
        // NoClassDefFoundError
        // - Thrown by: JVM
        // - Cause: JVM cannot find a class that was available at compile time
        //          but is missing at runtime
        // ---------------------------------------------------------------------
        System.out.println("\n3. NoClassDefFoundError");
        System.out.println("   Thrown by JVM when a class was available at compile time");
        System.out.println("   but cannot be found at runtime");
        System.out.println();
    }

    // ========================================================================
    // EXCEPTION SUMMARY TABLE
    // ========================================================================

    /**
     * QUICK REFERENCE TABLE
     * =====================
     *
     * +----------------------------------+----------+--------------+
     * | Exception                        | Checked? | Thrown By    |
     * +----------------------------------+----------+--------------+
     * | ArithmeticException              | No       | JVM          |
     * | ArrayIndexOutOfBoundsException   | No       | JVM          |
     * | ClassCastException               | No       | JVM          |
     * | NullPointerException             | No       | JVM          |
     * | IllegalArgumentException         | No       | Programmer   |
     * | NumberFormatException            | No       | Java         |
     * +----------------------------------+----------+--------------+
     * | FileNotFoundException            | Yes      | Programmer   |
     * | IOException                      | Yes      | Programmer   |
     * | NotSerializableException         | Yes      | Java         |
     * | ParseException                   | Yes      | Java         |
     * | SQLException                     | Yes      | Programmer   |
     * +----------------------------------+----------+--------------+
     * | ExceptionInInitializerError      | No       | JVM          |
     * | StackOverflowError               | No       | JVM          |
     * | NoClassDefFoundError             | No       | JVM          |
     * +----------------------------------+----------+--------------+
     *
     *
     * EXAM TIPS:
     * ----------
     * 1. RuntimeException and its subclasses are UNCHECKED
     * 2. Error and its subclasses are UNCHECKED
     * 3. Everything else under Throwable/Exception is CHECKED
     * 4. NumberFormatException extends IllegalArgumentException
     * 5. FileNotFoundException and NotSerializableException extend IOException
     * 6. Compile with -g:vars to see variable names in NullPointerException
     * 7. Unreachable catch blocks for checked exceptions = COMPILATION ERROR
     * 8. Overriding methods cannot declare new or broader checked exceptions
     */
}
