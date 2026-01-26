package ch14io;

import java.io.*;

/**
 * INTERACTING WITH USERS - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * SYSTEM STREAMS - System.out, System.err, System.in
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Java provides three standard I/O streams for user interaction:
 *
 * System.out - PrintStream for standard output (normal messages)
 * System.err - PrintStream for error output (error messages)
 * System.in  - InputStream for standard input (reading user input)
 *
 * ┌─────────────┬─────────────┬───────────────────────────────────────────┐
 * │ Stream      │ Type        │ Purpose                                   │
 * ├─────────────┼─────────────┼───────────────────────────────────────────┤
 * │ System.out  │ PrintStream │ Standard output (normal messages)         │
 * │ System.err  │ PrintStream │ Error output (errors/warnings)            │
 * │ System.in   │ InputStream │ Standard input (read user input)          │
 * └─────────────┴─────────────┴───────────────────────────────────────────┘
 *
 * KEY CHARACTERISTICS:
 * - Static objects (System.out, System.err, System.in)
 * - Shared by ENTIRE application (all threads)
 * - Created and opened automatically by the JVM
 * - Available throughout program lifetime
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * SYSTEM STREAMS - CLOSING BEHAVIOR (EXAM TRAP)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * System streams CAN be used in try-with-resources or closed manually:
 *
 *   try (PrintStream out = System.out) {
 *       // Valid syntax, but DON'T DO THIS!
 *   }
 *
 * However, CLOSING SYSTEM STREAMS IS NOT RECOMMENDED!
 *
 * Why?
 * - Closing makes them PERMANENTLY UNAVAILABLE for all threads
 * - Cannot be reopened
 * - Affects entire application, not just current thread
 * - Other parts of code may still need them
 *
 * EXAM TIP: Closing System.out/err/in makes them unavailable for remainder
 *           of program execution for ALL threads!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * EXCEPTION HANDLING - PrintStream vs InputStream
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * PrintStream (System.out, System.err):
 * - Methods do NOT throw checked exceptions
 * - Errors fail SILENTLY
 * - Use checkError() to detect errors
 *
 * Example:
 *   System.out.println("Hello");  // No checked exception
 *
 * InputStream (System.in):
 * - Methods DO throw IOException
 * - Operating on closed stream throws exception
 * - Errors are NOT silent
 *
 * Example:
 *   int b = System.in.read();  // throws IOException
 *
 * EXAM TRAP: PrintStream fails silently, InputStream throws exceptions!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * READING USER INPUT WITH System.in
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * System.in is an InputStream (reads bytes, not characters).
 *
 * Common pattern: Wrap with BufferedReader for readLine() method
 *
 * BufferedReader reader = new BufferedReader(
 *                           new InputStreamReader(System.in));
 * String line = reader.readLine();
 *
 * Why wrap?
 * - InputStream.read() reads one BYTE at a time (inefficient)
 * - InputStreamReader converts bytes to characters
 * - BufferedReader provides readLine() method (read entire line)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CONSOLE CLASS - java.io.Console
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Console is specifically designed to handle user interactions.
 *
 * KEY CHARACTERISTICS:
 * - SINGLETON pattern
 * - Accessible ONLY from factory method: System.console()
 * - Only ONE instance created by JVM
 * - NO PUBLIC CONSTRUCTOR
 * - May return null if no console available (IDEs, non-interactive)
 *
 * How to obtain:
 *   Console console = System.console();
 *   if (console == null) {
 *       // No console available
 *   }
 *
 * EXAM TIP: Console has NO constructor! Use System.console() factory method!
 * EXAM TIP: System.console() can return null!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CONSOLE METHODS - READING INPUT
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * String readLine()
 *   - Reads a line of text from console
 *   - Returns String (including spaces, until newline)
 *   - Returns null if end of stream reached
 *
 * String readLine(String fmt, Object... args)
 *   - Displays formatted prompt, then reads line
 *   - Format string uses printf syntax
 *   - Returns String
 *
 * char[] readPassword()
 *   - Reads password from console
 *   - Does NOT echo characters to screen (security!)
 *   - Returns char[] (NOT String!)
 *   - Returns null if end of stream reached
 *
 * char[] readPassword(String fmt, Object... args)
 *   - Displays formatted prompt, then reads password
 *   - Does NOT echo characters to screen
 *   - Returns char[]
 *
 * CRITICAL EXAM DIFFERENCES: readLine() vs readPassword()
 * ┌──────────────────┬─────────────┬────────────┬──────────────────────┐
 * │ Method           │ Return Type │ Echo?      │ Use Case             │
 * ├──────────────────┼─────────────┼────────────┼──────────────────────┤
 * │ readLine()       │ String      │ Yes (shown)│ Normal input         │
 * │ readPassword()   │ char[]      │ No (hidden)│ Sensitive data       │
 * └──────────────────┴─────────────┴────────────┴──────────────────────┘
 *
 * WHY char[] INSTEAD OF String?
 * - Strings are immutable and stored in String pool
 * - Cannot be "cleared" from memory (security risk)
 * - char[] can be zeroed out after use (cleared from memory)
 * - Better for sensitive data like passwords
 *
 * Example:
 *   char[] password = console.readPassword();
 *   // Use password...
 *   Arrays.fill(password, ' ');  // Clear from memory
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CONSOLE METHODS - OUTPUT
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Console format(String fmt, Object... args)
 *   - Writes formatted string to console
 *   - Uses printf syntax
 *   - Returns Console (for method chaining)
 *
 * PrintWriter writer()
 *   - Returns PrintWriter associated with console
 *   - Can be used for more complex output
 *
 * Reader reader()
 *   - Returns Reader associated with console
 *   - Can be used for more complex input
 *
 * Console printf(String format, Object... args)
 *   - Same as format()
 *   - Returns Console (for method chaining)
 *
 * EXAM TIP: format() and printf() return Console for method chaining!
 *
 * Example:
 *   console.format("Hello %s", name)
 *          .format("Age: %d", age)
 *          .format("Welcome!%n");
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * METHOD CHAINING (FLUENT API)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Many Console methods return the Console instance, allowing chaining:
 *
 *   console.format("Enter name: ")
 *          .flush();
 *
 *   String name = console.readLine();
 *
 *   console.format("Hello %s", name)
 *          .format("%n")
 *          .format("Welcome!%n");
 *
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class InteractingWithUsers {

    public static void main(String[] args) {
        // ────────────────────────────────────────────────────────────────────
        // System.out and System.err
        // ────────────────────────────────────────────────────────────────────
        demonstrateSystemOutErr();

        // ────────────────────────────────────────────────────────────────────
        // Reading input with System.in (wrapped)
        // ────────────────────────────────────────────────────────────────────
        // demonstrateSystemIn();  // Commented out - requires user input

        // ────────────────────────────────────────────────────────────────────
        // Closing System streams (NOT recommended)
        // ────────────────────────────────────────────────────────────────────
        demonstrateClosingSystemStreams();

        // ────────────────────────────────────────────────────────────────────
        // Console class overview
        // ────────────────────────────────────────────────────────────────────
        demonstrateConsoleBasics();

        // ────────────────────────────────────────────────────────────────────
        // Console reading methods (interactive - commented out)
        // ────────────────────────────────────────────────────────────────────
        // demonstrateConsoleReading();  // Requires interactive console

        // ────────────────────────────────────────────────────────────────────
        // readPassword behavior
        // ────────────────────────────────────────────────────────────────────
        demonstrateReadPassword();

        // ────────────────────────────────────────────────────────────────────
        // Console output methods
        // ────────────────────────────────────────────────────────────────────
        demonstrateConsoleOutput();

        System.out.println("\n✓ All user interaction examples completed");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // System.out and System.err
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateSystemOutErr() {
        System.out.println("=== System.out and System.err ===\n");

        System.out.println("System.out characteristics:");
        System.out.println("  - Type: PrintStream");
        System.out.println("  - Purpose: Standard output (normal messages)");
        System.out.println("  - Shared: By entire application (all threads)");
        System.out.println("  - Created: Automatically by JVM");

        System.err.println("\nSystem.err characteristics:");
        System.err.println("  - Type: PrintStream");
        System.err.println("  - Purpose: Error output (errors/warnings)");
        System.err.println("  - Often displayed in different color");

        System.out.println("\nKey point: PrintStream methods don't throw checked exceptions");
        System.out.println("           They fail SILENTLY if errors occur");
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Reading input with System.in
    // ═══════════════════════════════════════════════════════════════════════════
    @SuppressWarnings("unused")
    private static void demonstrateSystemIn() {
        System.out.println("=== Reading with System.in ===\n");

        System.out.println("Common pattern: Wrap System.in with BufferedReader");
        System.out.println("BufferedReader reader = new BufferedReader(");
        System.out.println("                          new InputStreamReader(System.in));");
        System.out.println();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Enter your name: ");
            String name = reader.readLine();
            System.out.println("Hello, " + name + "!");

            System.out.print("Enter your age: ");
            String ageStr = reader.readLine();
            int age = Integer.parseInt(ageStr);
            System.out.println("You are " + age + " years old.");

        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }

        System.out.println("\nKey points:");
        System.out.println("  - System.in is InputStream (reads bytes)");
        System.out.println("  - InputStreamReader converts bytes to characters");
        System.out.println("  - BufferedReader provides readLine() method");
        System.out.println("  - readLine() throws IOException (checked exception!)");
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Closing System streams (NOT recommended)
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateClosingSystemStreams() {
        System.out.println("=== Closing System Streams (NOT RECOMMENDED) ===\n");

        System.out.println("System streams can be closed, but SHOULD NOT be:");
        System.out.println();

        // Create a copy reference to demonstrate
        PrintStream out = System.out;
        System.out.println("Before closing: System.out works");

        // DON'T DO THIS IN REAL CODE!
        // out.close();
        // After closing, System.out would be unavailable for ALL threads!

        System.out.println("\nWhy not to close System streams:");
        System.out.println("  1. Makes them PERMANENTLY unavailable");
        System.out.println("  2. Affects ALL threads, not just current thread");
        System.out.println("  3. Cannot be reopened");
        System.out.println("  4. Other code may still need them");
        System.out.println("  5. JVM manages their lifecycle");

        System.out.println("\nEXAM TIP:");
        System.out.println("  - Closing System streams is valid syntax");
        System.out.println("  - But makes them unavailable for rest of program");
        System.out.println("  - Don't close them in practice!");

        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Console basics
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateConsoleBasics() {
        System.out.println("=== Console Class Basics ===\n");

        System.out.println("How to obtain Console:");
        System.out.println("  Console console = System.console();");
        System.out.println();

        Console console = System.console();

        if (console == null) {
            System.out.println("Console is NULL");
            System.out.println("This happens when:");
            System.out.println("  - Running in IDE (like IntelliJ, Eclipse)");
            System.out.println("  - Running with redirected I/O");
            System.out.println("  - Running as background process");
            System.out.println("  - No interactive console available");
            System.out.println();
            System.out.println("EXAM TIP: Always check if console is null!");
        } else {
            System.out.println("Console is available: " + console.getClass().getName());
        }

        System.out.println("\nKey characteristics:");
        System.out.println("  - SINGLETON pattern (only one instance)");
        System.out.println("  - NO public constructor");
        System.out.println("  - Factory method: System.console()");
        System.out.println("  - Can return null if no console available");
        System.out.println("  - Specifically designed for user interactions");

        System.out.println("\nMethods:");
        System.out.println("  Reading:");
        System.out.println("    String readLine()");
        System.out.println("    String readLine(String fmt, Object... args)");
        System.out.println("    char[] readPassword()");
        System.out.println("    char[] readPassword(String fmt, Object... args)");
        System.out.println();
        System.out.println("  Writing:");
        System.out.println("    Console format(String fmt, Object... args)");
        System.out.println("    Console printf(String format, Object... args)");
        System.out.println("    PrintWriter writer()");
        System.out.println("    Reader reader()");
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Console reading methods (requires interactive console)
    // ═══════════════════════════════════════════════════════════════════════════
    @SuppressWarnings("unused")
    private static void demonstrateConsoleReading() {
        System.out.println("=== Console Reading Methods ===\n");

        Console console = System.console();
        if (console == null) {
            System.out.println("Console not available (running in IDE)");
            return;
        }

        // readLine() - no prompt
        String name = console.readLine();
        System.out.println("You entered: " + name);

        // readLine(String fmt, Object... args) - with formatted prompt
        int age = Integer.parseInt(console.readLine("Enter your age: "));
        console.format("You are %d years old.%n", age);

        // readPassword() - no prompt, no echo
        char[] password = console.readPassword();
        console.format("Password length: %d%n", password.length);

        // readPassword(String fmt, Object... args) - with prompt, no echo
        char[] confirmPassword = console.readPassword("Confirm password: ");

        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // readPassword behavior
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateReadPassword() {
        System.out.println("=== readPassword() Behavior ===\n");

        System.out.println("readPassword() characteristics:");
        System.out.println("  1. Does NOT echo characters to screen");
        System.out.println("     (User types, but nothing appears)");
        System.out.println();
        System.out.println("  2. Returns char[] (NOT String!)");
        System.out.println();
        System.out.println("  3. Can be zeroed out after use (security)");
        System.out.println();

        System.out.println("Why char[] instead of String?");
        System.out.println("  - Strings are immutable");
        System.out.println("  - Strings stored in String pool");
        System.out.println("  - Cannot be cleared from memory");
        System.out.println("  - Security risk for passwords");
        System.out.println();
        System.out.println("  - char[] is mutable");
        System.out.println("  - Can be zeroed out: Arrays.fill(password, ' ')");
        System.out.println("  - Cleared from memory immediately");
        System.out.println();

        System.out.println("Example usage:");
        System.out.println("  char[] password = console.readPassword(\"Password: \");");
        System.out.println("  // Use password for authentication");
        System.out.println("  Arrays.fill(password, ' ');  // Clear from memory");
        System.out.println();

        System.out.println("EXAM COMPARISON:");
        System.out.println("┌──────────────────┬─────────────┬────────────┬──────────────┐");
        System.out.println("│ Method           │ Return Type │ Echo?      │ Use Case     │");
        System.out.println("├──────────────────┼─────────────┼────────────┼──────────────┤");
        System.out.println("│ readLine()       │ String      │ Yes (shown)│ Normal input │");
        System.out.println("│ readPassword()   │ char[]      │ No (hidden)│ Passwords    │");
        System.out.println("└──────────────────┴─────────────┴────────────┴──────────────┘");

        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Console output methods
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateConsoleOutput() {
        System.out.println("=== Console Output Methods ===\n");

        Console console = System.console();
        if (console == null) {
            System.out.println("Console not available - showing method signatures:\n");
            showConsoleOutputMethodSignatures();
            return;
        }

        // format() - returns Console for chaining
        console.format("Hello, %s!%n", "World");
        console.format("Number: %d%n", 42);

        // Method chaining
        console.format("First line%n")
               .format("Second line%n")
               .format("Third line%n");

        // printf() - same as format()
        console.printf("Printf: %s = %d%n", "answer", 42);

        // writer() - get PrintWriter
        PrintWriter writer = console.writer();
        writer.println("Using PrintWriter from console");

        // reader() - get Reader
        Reader reader = console.reader();
        System.out.println("Reader available: " + (reader != null));

        System.out.println();
    }

    private static void showConsoleOutputMethodSignatures() {
        System.out.println("Console format(String fmt, Object... args)");
        System.out.println("  - Writes formatted string");
        System.out.println("  - Uses printf syntax");
        System.out.println("  - Returns Console (for method chaining)");
        System.out.println();

        System.out.println("Console printf(String format, Object... args)");
        System.out.println("  - Same as format()");
        System.out.println("  - Returns Console (for method chaining)");
        System.out.println();

        System.out.println("PrintWriter writer()");
        System.out.println("  - Returns PrintWriter associated with console");
        System.out.println();

        System.out.println("Reader reader()");
        System.out.println("  - Returns Reader associated with console");
        System.out.println();

        System.out.println("Example method chaining:");
        System.out.println("  console.format(\"Hello %s\", name)");
        System.out.println("         .format(\"Age: %d\", age)");
        System.out.println("         .format(\"Welcome!%n\");");
        System.out.println();
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// COMPARISON: System.in vs Console
// ═══════════════════════════════════════════════════════════════════════════
//
// ┌─────────────────────────┬──────────────────────┬────────────────────────┐
// │ Feature                 │ System.in            │ Console                │
// ├─────────────────────────┼──────────────────────┼────────────────────────┤
// │ Type                    │ InputStream          │ Console (singleton)    │
// │ Obtaining               │ System.in (field)    │ System.console() (may  │
// │                         │                      │ return null)           │
// │ Reading line            │ Wrap with            │ readLine() built-in    │
// │                         │ BufferedReader       │                        │
// │ Reading password        │ Not supported        │ readPassword() (no     │
// │                         │                      │ echo, returns char[])  │
// │ Formatted prompts       │ Not supported        │ Supported (printf      │
// │                         │                      │ syntax)                │
// │ Availability            │ Always available     │ May be null (IDEs,     │
// │                         │                      │ non-interactive)       │
// │ Thread-safe             │ Yes                  │ Yes                    │
// └─────────────────────────┴──────────────────────┴────────────────────────┘
//
// ═══════════════════════════════════════════════════════════════════════════
// EXAM SUMMARY
// ═══════════════════════════════════════════════════════════════════════════
//
// SYSTEM STREAMS:
//   - System.out, System.err: PrintStream (output)
//   - System.in: InputStream (input)
//   - Static, shared by entire application
//   - Created automatically by JVM
//   - Closing NOT recommended (makes unavailable for all threads)
//   - PrintStream methods don't throw checked exceptions (fail silently)
//   - InputStream methods DO throw IOException
//
// SYSTEM.IN PATTERN:
//   BufferedReader reader = new BufferedReader(
//                             new InputStreamReader(System.in));
//   String line = reader.readLine();
//
// CONSOLE CLASS:
//   - Singleton (System.console() factory method, NO constructor)
//   - Can return null (check before use!)
//   - Designed for user interactions
//
// CONSOLE METHODS:
//   String readLine()                           Read line, echo to screen
//   String readLine(String fmt, Object... args) Read with formatted prompt
//   char[] readPassword()                       Read password, NO echo
//   char[] readPassword(String fmt, ...)        Read password with prompt
//   Console format(String fmt, ...)             Formatted output, returns Console
//   Console printf(String format, ...)          Same as format()
//   PrintWriter writer()                        Get PrintWriter
//   Reader reader()                             Get Reader
//
// CRITICAL DIFFERENCES:
//   readLine()     → String, echoed, normal input
//   readPassword() → char[], NOT echoed, secure input
//
// WHY char[] FOR PASSWORD?
//   - Strings immutable, can't be cleared
//   - char[] can be zeroed out (security)
//
// ═══════════════════════════════════════════════════════════════════════════
