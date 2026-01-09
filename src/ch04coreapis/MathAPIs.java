package ch04coreapis;

/**
 * MATH APIs - Common Methods Guide for OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CRITICAL: Know the SIGNATURES and RETURN TYPES! (Common Exam Trap)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * All Math methods are STATIC - call with Math.methodName()
 * No need to import - Math is in java.lang package
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * COMPLETE SIGNATURES TABLE
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * METHOD: min()
 * ────────────────────────────────────────────────────────────────────────────
 * static int min(int a, int b)                 → returns int
 * static long min(long a, long b)              → returns long
 * static float min(float a, float b)           → returns float
 * static double min(double a, double b)        → returns double
 *
 * METHOD: max()
 * ────────────────────────────────────────────────────────────────────────────
 * static int max(int a, int b)                 → returns int
 * static long max(long a, long b)              → returns long
 * static float max(float a, float b)           → returns float
 * static double max(double a, double b)        → returns double
 *
 * METHOD: round() *** MOST TESTED ***
 * ────────────────────────────────────────────────────────────────────────────
 * static int round(float a)                    → returns int    ← MEMORIZE!
 * static long round(double a)                  → returns long   ← MEMORIZE!
 *
 * METHOD: ceil()
 * ────────────────────────────────────────────────────────────────────────────
 * static double ceil(double a)                 → returns double ← NOT int!
 *
 * METHOD: floor()
 * ────────────────────────────────────────────────────────────────────────────
 * static double floor(double a)                → returns double ← NOT int!
 *
 * METHOD: pow()
 * ────────────────────────────────────────────────────────────────────────────
 * static double pow(double base, double exp)   → returns double
 *
 * METHOD: sqrt()
 * ────────────────────────────────────────────────────────────────────────────
 * static double sqrt(double a)                 → returns double
 *
 * METHOD: abs()
 * ────────────────────────────────────────────────────────────────────────────
 * static int abs(int a)                        → returns int
 * static long abs(long a)                      → returns long
 * static float abs(float a)                    → returns float
 * static double abs(double a)                  → returns double
 *
 * METHOD: random()
 * ────────────────────────────────────────────────────────────────────────────
 * static double random()                       → returns double (range [0.0, 1.0))
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * MOST COMMON EXAM TRAPS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * 1. round(float) returns INT, not float!
 * 2. round(double) returns LONG, not int or double!
 * 3. ceil() and floor() return DOUBLE, not int!
 * 4. ceil() and floor() take DOUBLE parameter only (no overloads)
 * 5. pow() takes TWO doubles and returns double
 * 6. min/max/abs are overloaded - return type matches parameter type
 * 7. random() takes NO parameters and returns double
 */
public class MathAPIs {

    public static void main(String[] args) {
        System.out.println("=== MATH APIs PRACTICE ===\n");
        minMaxMethods();
        roundMethod();
        ceilMethod();
        floorMethod();
        powMethod();
        sqrtMethod();
        absMethods();
        randomMethod();
        compilationTraps();
        signatureQuiz();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * Math.min() and Math.max()
     * ═══════════════════════════════════════════════════════════════════════
     *
     * ALL SIGNATURES:
     *
     * static int min(int a, int b)
     * static long min(long a, long b)
     * static float min(float a, float b)
     * static double min(double a, double b)
     *
     * static int max(int a, int b)
     * static long max(long a, long b)
     * static float max(float a, float b)
     * static double max(double a, double b)
     *
     * RULE: Return type ALWAYS matches parameter type!
     *
     * EXAM TIP:
     * - If you pass int, you get int back
     * - If you pass long, you get long back
     * - If you pass float, you get float back
     * - If you pass double, you get double back
     */
    private static void minMaxMethods() {
        System.out.println("=== Math.min() and Math.max() ===");
        System.out.println("SIGNATURES:");
        System.out.println("  static int min(int a, int b)");
        System.out.println("  static long min(long a, long b)");
        System.out.println("  static float min(float a, float b)");
        System.out.println("  static double min(double a, double b)");
        System.out.println("  (same for max)\n");
        System.out.println("RULE: Return type = Parameter type\n");

        // int versions - returns int
        int minInt = Math.min(5, 10);
        int maxInt = Math.max(5, 10);
        System.out.println("Math.min(5, 10) = " + minInt + " (int → int)");
        System.out.println("Math.max(5, 10) = " + maxInt + " (int → int)");

        // long versions - returns long
        long minLong = Math.min(5L, 10L);
        long maxLong = Math.max(5L, 10L);
        System.out.println("\nMath.min(5L, 10L) = " + minLong + " (long → long)");
        System.out.println("Math.max(5L, 10L) = " + maxLong + " (long → long)");

        // float versions - returns float
        float minFloat = Math.min(5.5f, 10.5f);
        float maxFloat = Math.max(5.5f, 10.5f);
        System.out.println("\nMath.min(5.5f, 10.5f) = " + minFloat + " (float → float)");
        System.out.println("Math.max(5.5f, 10.5f) = " + maxFloat + " (float → float)");

        // double versions - returns double
        double minDouble = Math.min(5.5, 10.5);
        double maxDouble = Math.max(5.5, 10.5);
        System.out.println("\nMath.min(5.5, 10.5) = " + minDouble + " (double → double)");
        System.out.println("Math.max(5.5, 10.5) = " + maxDouble + " (double → double)");

        // Works with negative numbers
        System.out.println("\n--- With Negative Numbers ---");
        System.out.println("Math.min(-5, 10) = " + Math.min(-5, 10));      // -5
        System.out.println("Math.max(-5, 10) = " + Math.max(-5, 10));      // 10
        System.out.println("Math.min(-5, -10) = " + Math.min(-5, -10));    // -10
        System.out.println("Math.max(-5, -10) = " + Math.max(-5, -10));    // -5

        // EXAM TRAP: Parameter type mismatch
        System.out.println("\n--- Type Compatibility ---");
        int result1 = Math.min(5, 10);              // ✓ OK: int, int → int
        long result2 = Math.min(5L, 10L);           // ✓ OK: long, long → long
        // int wrong = Math.min(5L, 10L);           // ✗ ERROR: long, long → long (not int)
        int correct = (int) Math.min(5L, 10L);      // ✓ OK: with cast
        System.out.println("int result1 = Math.min(5, 10);        ✓ Compiles");
        System.out.println("long result2 = Math.min(5L, 10L);     ✓ Compiles");
        System.out.println("int wrong = Math.min(5L, 10L);        ✗ DOES NOT COMPILE");
        System.out.println("int correct = (int) Math.min(5L, 10L); ✓ Compiles");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * Math.round() - MOST TESTED METHOD ON EXAM!
     * ═══════════════════════════════════════════════════════════════════════
     *
     * ALL SIGNATURES (ONLY 2!):
     *
     * static int round(float a)      ← Takes FLOAT, returns INT
     * static long round(double a)    ← Takes DOUBLE, returns LONG
     *
     * *** CRITICAL EXAM KNOWLEDGE ***
     * - round(float)  → returns INT (not float!)
     * - round(double) → returns LONG (not double or int!)
     *
     * Behavior:
     * - Rounds to nearest whole number
     * - .5 rounds UP (away from zero for positive)
     * - Negative .5 rounds toward zero: -2.5 → -2
     *
     * EXAM TRAP:
     * Many students think round() returns same type as input!
     * This is WRONG! float → int, double → long
     */
    private static void roundMethod() {
        System.out.println("=== Math.round() - MOST TESTED! ===");
        System.out.println("SIGNATURES:");
        System.out.println("  static int round(float a)    ← float → int");
        System.out.println("  static long round(double a)  ← double → long");
        System.out.println("\n*** MEMORIZE: round(float)→int, round(double)→long ***\n");

        // round(float) returns INT
        System.out.println("--- round(float a) → int ---");
        int r1 = Math.round(3.4f);
        int r2 = Math.round(3.5f);
        int r3 = Math.round(3.9f);
        System.out.println("Math.round(3.4f) = " + r1 + " (type: int)");  // 3
        System.out.println("Math.round(3.5f) = " + r2 + " (type: int)");  // 4 (.5 rounds up)
        System.out.println("Math.round(3.9f) = " + r3 + " (type: int)");  // 4

        // round(double) returns LONG
        System.out.println("\n--- round(double a) → long ---");
        long r4 = Math.round(3.4);
        long r5 = Math.round(3.5);
        long r6 = Math.round(3.9);
        System.out.println("Math.round(3.4) = " + r4 + " (type: long)");  // 3
        System.out.println("Math.round(3.5) = " + r5 + " (type: long)");  // 4 (.5 rounds up)
        System.out.println("Math.round(3.9) = " + r6 + " (type: long)");  // 4

        // .5 rounding behavior
        System.out.println("\n--- .5 Rounding Rules ---");
        System.out.println("Positive .5 → rounds UP (away from zero):");
        System.out.println("  Math.round(0.5f) = " + Math.round(0.5f));   // 1
        System.out.println("  Math.round(2.5f) = " + Math.round(2.5f));   // 3
        System.out.println("\nNegative .5 → rounds toward zero:");
        System.out.println("  Math.round(-0.5f) = " + Math.round(-0.5f)); // 0
        System.out.println("  Math.round(-2.5f) = " + Math.round(-2.5f)); // -2 (toward zero)
        System.out.println("  Math.round(-2.6f) = " + Math.round(-2.6f)); // -3 (away from zero)

        // EXAM COMPILATION TRAP
        System.out.println("\n--- CRITICAL Type Compatibility Traps ---");
        int intVar = Math.round(3.5f);             // ✓ OK: round(float) → int
        long longVar = Math.round(3.5);            // ✓ OK: round(double) → long
        // int wrongVar = Math.round(3.5);         // ✗ ERROR: round(double) → long (not int!)
        int correctVar = (int) Math.round(3.5);    // ✓ OK: with cast
        // float wrongVar2 = Math.round(3.5f);     // ✗ ERROR: round(float) → int (not float!)

        System.out.println("int intVar = Math.round(3.5f);         ✓ Compiles (float→int)");
        System.out.println("long longVar = Math.round(3.5);        ✓ Compiles (double→long)");
        System.out.println("int wrongVar = Math.round(3.5);        ✗ DOES NOT COMPILE");
        System.out.println("  → round(double) returns long, not int!");
        System.out.println("int correctVar = (int) Math.round(3.5); ✓ Compiles (with cast)");
        System.out.println("float wrongVar2 = Math.round(3.5f);    ✗ DOES NOT COMPILE");
        System.out.println("  → round(float) returns int, not float!");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * Math.ceil() - Ceiling (round UP)
     * ═══════════════════════════════════════════════════════════════════════
     *
     * SIGNATURE (ONLY 1!):
     *
     * static double ceil(double a)   ← Takes DOUBLE, returns DOUBLE
     *
     * *** CRITICAL EXAM KNOWLEDGE ***
     * - NO overloads! Only one signature!
     * - Takes double parameter
     * - Returns DOUBLE (NOT int!)
     *
     * Behavior:
     * - Returns smallest double value that is ≥ argument
     * - Always rounds UP toward positive infinity
     * - 3.1 → 4.0, 3.9 → 4.0, 4.0 → 4.0
     * - Negative: -3.1 → -3.0 (up toward zero)
     *
     * EXAM TRAP: Returns DOUBLE, not int! Many students expect int.
     */
    private static void ceilMethod() {
        System.out.println("=== Math.ceil() ===");
        System.out.println("SIGNATURE:");
        System.out.println("  static double ceil(double a)  ← double → double (NOT int!)");
        System.out.println("\n*** MEMORIZE: ceil() returns DOUBLE, not int! ***\n");

        // Returns double, not int!
        System.out.println("--- ceil() returns double ---");
        double c1 = Math.ceil(3.1);
        double c2 = Math.ceil(3.5);
        double c3 = Math.ceil(3.9);
        System.out.println("Math.ceil(3.1) = " + c1 + " (type: double)");  // 4.0
        System.out.println("Math.ceil(3.5) = " + c2 + " (type: double)");  // 4.0
        System.out.println("Math.ceil(3.9) = " + c3 + " (type: double)");  // 4.0

        // Already whole number - still returns double
        System.out.println("\n--- Already Whole Number ---");
        System.out.println("Math.ceil(5.0) = " + Math.ceil(5.0));          // 5.0 (still double)
        System.out.println("Math.ceil(5) = " + Math.ceil(5));              // 5.0 (int promoted to double)

        // Negative numbers - rounds UP (toward zero)
        System.out.println("\n--- Negative Numbers (rounds UP/toward zero) ---");
        System.out.println("Math.ceil(-3.1) = " + Math.ceil(-3.1));        // -3.0 (up toward zero)
        System.out.println("Math.ceil(-3.5) = " + Math.ceil(-3.5));        // -3.0 (up toward zero)
        System.out.println("Math.ceil(-3.9) = " + Math.ceil(-3.9));        // -3.0 (up toward zero)
        System.out.println("Math.ceil(-4.0) = " + Math.ceil(-4.0));        // -4.0 (already whole)

        // EXAM COMPILATION TRAP
        System.out.println("\n--- CRITICAL Type Compatibility Traps ---");
        double doubleVar = Math.ceil(3.5);         // ✓ OK: returns double
        // int intVar = Math.ceil(3.5);            // ✗ ERROR: ceil returns double!
        int correctVar = (int) Math.ceil(3.5);     // ✓ OK: with cast

        System.out.println("double doubleVar = Math.ceil(3.5);     ✓ Compiles");
        System.out.println("int intVar = Math.ceil(3.5);           ✗ DOES NOT COMPILE");
        System.out.println("  → ceil() returns double, not int!");
        System.out.println("int correctVar = (int) Math.ceil(3.5); ✓ Compiles (with cast)");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * Math.floor() - Floor (round DOWN)
     * ═══════════════════════════════════════════════════════════════════════
     *
     * SIGNATURE (ONLY 1!):
     *
     * static double floor(double a)  ← Takes DOUBLE, returns DOUBLE
     *
     * *** CRITICAL EXAM KNOWLEDGE ***
     * - NO overloads! Only one signature!
     * - Takes double parameter
     * - Returns DOUBLE (NOT int!)
     *
     * Behavior:
     * - Returns largest double value that is ≤ argument
     * - Always rounds DOWN toward negative infinity
     * - 3.1 → 3.0, 3.9 → 3.0, 3.0 → 3.0
     * - Negative: -3.1 → -4.0 (down away from zero)
     *
     * EXAM TRAP: Returns DOUBLE, not int! Many students expect int.
     */
    private static void floorMethod() {
        System.out.println("=== Math.floor() ===");
        System.out.println("SIGNATURE:");
        System.out.println("  static double floor(double a)  ← double → double (NOT int!)");
        System.out.println("\n*** MEMORIZE: floor() returns DOUBLE, not int! ***\n");

        // Returns double, not int!
        System.out.println("--- floor() returns double ---");
        double f1 = Math.floor(3.1);
        double f2 = Math.floor(3.5);
        double f3 = Math.floor(3.9);
        System.out.println("Math.floor(3.1) = " + f1 + " (type: double)");  // 3.0
        System.out.println("Math.floor(3.5) = " + f2 + " (type: double)");  // 3.0
        System.out.println("Math.floor(3.9) = " + f3 + " (type: double)");  // 3.0

        // Already whole number - still returns double
        System.out.println("\n--- Already Whole Number ---");
        System.out.println("Math.floor(5.0) = " + Math.floor(5.0));        // 5.0 (still double)
        System.out.println("Math.floor(5) = " + Math.floor(5));            // 5.0 (int promoted to double)

        // Negative numbers - rounds DOWN (away from zero)
        System.out.println("\n--- Negative Numbers (rounds DOWN/away from zero) ---");
        System.out.println("Math.floor(-3.1) = " + Math.floor(-3.1));      // -4.0 (down away from zero)
        System.out.println("Math.floor(-3.5) = " + Math.floor(-3.5));      // -4.0 (down away from zero)
        System.out.println("Math.floor(-3.9) = " + Math.floor(-3.9));      // -4.0 (down away from zero)
        System.out.println("Math.floor(-3.0) = " + Math.floor(-3.0));      // -3.0 (already whole)

        // EXAM COMPILATION TRAP
        System.out.println("\n--- CRITICAL Type Compatibility Traps ---");
        double doubleVar = Math.floor(3.5);        // ✓ OK: returns double
        // int intVar = Math.floor(3.5);           // ✗ ERROR: floor returns double!
        int correctVar = (int) Math.floor(3.5);    // ✓ OK: with cast

        System.out.println("double doubleVar = Math.floor(3.5);     ✓ Compiles");
        System.out.println("int intVar = Math.floor(3.5);           ✗ DOES NOT COMPILE");
        System.out.println("  → floor() returns double, not int!");
        System.out.println("int correctVar = (int) Math.floor(3.5); ✓ Compiles (with cast)");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * Math.pow() - Power (exponentiation)
     * ═══════════════════════════════════════════════════════════════════════
     *
     * SIGNATURE (ONLY 1!):
     *
     * static double pow(double base, double exponent)  ← Takes 2 DOUBLES, returns DOUBLE
     *
     * *** CRITICAL EXAM KNOWLEDGE ***
     * - NO overloads! Only one signature!
     * - Takes TWO double parameters (base and exponent)
     * - Returns DOUBLE
     *
     * Behavior:
     * - Returns base^exponent
     * - pow(2, 3) = 8.0 (2³)
     * - pow(2, 0) = 1.0 (anything^0 = 1)
     * - pow(2, -1) = 0.5 (negative exponent = 1/base^exp)
     *
     * EXAM TRAPS:
     * - Both parameters must be double (or will be promoted)
     * - Returns double (not int)
     */
    private static void powMethod() {
        System.out.println("=== Math.pow() ===");
        System.out.println("SIGNATURE:");
        System.out.println("  static double pow(double base, double exponent)");
        System.out.println("  ← Takes 2 doubles, returns double\n");

        // Basic usage
        System.out.println("--- Basic Powers ---");
        System.out.println("Math.pow(2, 3) = " + Math.pow(2, 3));          // 8.0 (2^3)
        System.out.println("Math.pow(5, 2) = " + Math.pow(5, 2));          // 25.0 (5^2)
        System.out.println("Math.pow(10, 3) = " + Math.pow(10, 3));        // 1000.0 (10^3)

        // Fractional exponents
        System.out.println("\n--- Fractional Exponents ---");
        System.out.println("Math.pow(4, 0.5) = " + Math.pow(4, 0.5));      // 2.0 (square root)
        System.out.println("Math.pow(8, 1.0/3.0) = " + Math.pow(8, 1.0/3.0)); // 2.0 (cube root)

        // Special cases
        System.out.println("\n--- Special Cases ---");
        System.out.println("Math.pow(2, 0) = " + Math.pow(2, 0));          // 1.0 (anything^0 = 1)
        System.out.println("Math.pow(2, 1) = " + Math.pow(2, 1));          // 2.0 (anything^1 = itself)
        System.out.println("Math.pow(2, -1) = " + Math.pow(2, -1));        // 0.5 (2^-1 = 1/2)
        System.out.println("Math.pow(2, -2) = " + Math.pow(2, -2));        // 0.25 (2^-2 = 1/4)

        // Negative base
        System.out.println("\n--- Negative Base ---");
        System.out.println("Math.pow(-2, 3) = " + Math.pow(-2, 3));        // -8.0
        System.out.println("Math.pow(-2, 2) = " + Math.pow(-2, 2));        // 4.0

        // Type compatibility
        System.out.println("\n--- Type Compatibility ---");
        double result = Math.pow(2, 3);            // ✓ OK: returns double
        // int intResult = Math.pow(2, 3);         // ✗ ERROR: pow returns double!
        int correctResult = (int) Math.pow(2, 3);  // ✓ OK: with cast

        System.out.println("double result = Math.pow(2, 3);        ✓ Compiles");
        System.out.println("int intResult = Math.pow(2, 3);        ✗ DOES NOT COMPILE");
        System.out.println("  → pow() returns double, not int!");
        System.out.println("int correctResult = (int) Math.pow(2, 3); ✓ Compiles (with cast)");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * Math.sqrt() - Square Root
     * ═══════════════════════════════════════════════════════════════════════
     *
     * SIGNATURE (ONLY 1!):
     *
     * static double sqrt(double a)   ← Takes DOUBLE, returns DOUBLE
     *
     * Behavior:
     * - Returns positive square root
     * - Returns NaN for negative numbers
     * - Returns double
     */
    private static void sqrtMethod() {
        System.out.println("=== Math.sqrt() ===");
        System.out.println("SIGNATURE:");
        System.out.println("  static double sqrt(double a)  ← double → double\n");

        // Basic usage
        System.out.println("--- Basic Square Roots ---");
        System.out.println("Math.sqrt(4) = " + Math.sqrt(4));              // 2.0
        System.out.println("Math.sqrt(9) = " + Math.sqrt(9));              // 3.0
        System.out.println("Math.sqrt(16) = " + Math.sqrt(16));            // 4.0
        System.out.println("Math.sqrt(2) = " + Math.sqrt(2));              // 1.414...

        // Special cases
        System.out.println("\n--- Special Cases ---");
        System.out.println("Math.sqrt(0) = " + Math.sqrt(0));              // 0.0
        System.out.println("Math.sqrt(1) = " + Math.sqrt(1));              // 1.0
        System.out.println("Math.sqrt(-1) = " + Math.sqrt(-1));            // NaN (not a number)

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * Math.abs() - Absolute Value
     * ═══════════════════════════════════════════════════════════════════════
     *
     * ALL SIGNATURES:
     *
     * static int abs(int a)          ← int → int
     * static long abs(long a)        ← long → long
     * static float abs(float a)      ← float → float
     * static double abs(double a)    ← double → double
     *
     * RULE: Return type ALWAYS matches parameter type!
     *
     * Behavior:
     * - Returns absolute value (distance from zero)
     * - Positive numbers stay positive
     * - Negative numbers become positive
     */
    private static void absMethods() {
        System.out.println("=== Math.abs() ===");
        System.out.println("SIGNATURES:");
        System.out.println("  static int abs(int a)");
        System.out.println("  static long abs(long a)");
        System.out.println("  static float abs(float a)");
        System.out.println("  static double abs(double a)");
        System.out.println("\nRULE: Return type = Parameter type\n");

        // int version
        System.out.println("--- abs(int) → int ---");
        System.out.println("Math.abs(5) = " + Math.abs(5));                // 5
        System.out.println("Math.abs(-5) = " + Math.abs(-5));              // 5
        System.out.println("Math.abs(0) = " + Math.abs(0));                // 0

        // long version
        System.out.println("\n--- abs(long) → long ---");
        System.out.println("Math.abs(5L) = " + Math.abs(5L));              // 5
        System.out.println("Math.abs(-5L) = " + Math.abs(-5L));            // 5

        // float version
        System.out.println("\n--- abs(float) → float ---");
        System.out.println("Math.abs(5.5f) = " + Math.abs(5.5f));          // 5.5
        System.out.println("Math.abs(-5.5f) = " + Math.abs(-5.5f));        // 5.5

        // double version
        System.out.println("\n--- abs(double) → double ---");
        System.out.println("Math.abs(5.5) = " + Math.abs(5.5));            // 5.5
        System.out.println("Math.abs(-5.5) = " + Math.abs(-5.5));          // 5.5

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * Math.random() - Random Number
     * ═══════════════════════════════════════════════════════════════════════
     *
     * SIGNATURE (ONLY 1!):
     *
     * static double random()         ← NO parameters, returns DOUBLE
     *
     * *** CRITICAL EXAM KNOWLEDGE ***
     * - NO parameters! (common mistake)
     * - Returns double
     * - Range: [0.0, 1.0) - includes 0.0, excludes 1.0
     *
     * Common pattern for random int in range [0, n):
     *   (int) (Math.random() * n)
     */
    private static void randomMethod() {
        System.out.println("=== Math.random() ===");
        System.out.println("SIGNATURE:");
        System.out.println("  static double random()  ← NO parameters! Returns [0.0, 1.0)\n");

        // Basic usage - generates different values each time
        System.out.println("--- Random Doubles [0.0, 1.0) ---");
        System.out.println("Math.random() = " + Math.random());
        System.out.println("Math.random() = " + Math.random());
        System.out.println("Math.random() = " + Math.random());

        // Common pattern - random int in range
        System.out.println("\n--- Random int in range [0, 10) ---");
        int rand1 = (int) (Math.random() * 10);
        int rand2 = (int) (Math.random() * 10);
        int rand3 = (int) (Math.random() * 10);
        System.out.println("(int) (Math.random() * 10) = " + rand1);
        System.out.println("(int) (Math.random() * 10) = " + rand2);
        System.out.println("(int) (Math.random() * 10) = " + rand3);

        // Random int in range [min, max]
        System.out.println("\n--- Random int in range [5, 10] ---");
        int min = 5;
        int max = 10;
        int range = max - min + 1;
        int rand4 = (int) (Math.random() * range) + min;
        int rand5 = (int) (Math.random() * range) + min;
        System.out.println("Random in [5, 10]: " + rand4);
        System.out.println("Random in [5, 10]: " + rand5);

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * COMPILATION TRAPS - Test Your Knowledge!
     * ═══════════════════════════════════════════════════════════════════════
     *
     * These are common mistakes that appear on the exam.
     * Study these carefully!
     */
    private static void compilationTraps() {
        System.out.println("=== COMPILATION TRAPS - Study These! ===\n");

        System.out.println("--- TRAP 1: round() return types ---");
        int r1 = Math.round(3.5f);              // ✓ Compiles (round(float) → int)
        long r2 = Math.round(3.5);              // ✓ Compiles (round(double) → long)
        // int r3 = Math.round(3.5);            // ✗ DOES NOT COMPILE (round(double) → long, not int)
        int r4 = (int) Math.round(3.5);         // ✓ Compiles with cast
        System.out.println("  int r1 = Math.round(3.5f);        ✓ Compiles");
        System.out.println("  long r2 = Math.round(3.5);        ✓ Compiles");
        System.out.println("  int r3 = Math.round(3.5);         ✗ ERROR: returns long");
        System.out.println("  int r4 = (int) Math.round(3.5);   ✓ Compiles\n");

        System.out.println("--- TRAP 2: ceil() and floor() return double ---");
        double c1 = Math.ceil(3.5);             // ✓ Compiles (returns double)
        // int c2 = Math.ceil(3.5);             // ✗ DOES NOT COMPILE (returns double, not int)
        int c3 = (int) Math.ceil(3.5);          // ✓ Compiles with cast
        double f1 = Math.floor(3.5);            // ✓ Compiles (returns double)
        // int f2 = Math.floor(3.5);            // ✗ DOES NOT COMPILE (returns double, not int)
        int f3 = (int) Math.floor(3.5);         // ✓ Compiles with cast
        System.out.println("  double c1 = Math.ceil(3.5);       ✓ Compiles");
        System.out.println("  int c2 = Math.ceil(3.5);          ✗ ERROR: returns double");
        System.out.println("  int c3 = (int) Math.ceil(3.5);    ✓ Compiles");
        System.out.println("  double f1 = Math.floor(3.5);      ✓ Compiles");
        System.out.println("  int f2 = Math.floor(3.5);         ✗ ERROR: returns double");
        System.out.println("  int f3 = (int) Math.floor(3.5);   ✓ Compiles\n");

        System.out.println("--- TRAP 3: pow() returns double ---");
        double p1 = Math.pow(2, 3);             // ✓ Compiles (returns double)
        // int p2 = Math.pow(2, 3);             // ✗ DOES NOT COMPILE (returns double, not int)
        int p3 = (int) Math.pow(2, 3);          // ✓ Compiles with cast
        System.out.println("  double p1 = Math.pow(2, 3);       ✓ Compiles");
        System.out.println("  int p2 = Math.pow(2, 3);          ✗ ERROR: returns double");
        System.out.println("  int p3 = (int) Math.pow(2, 3);    ✓ Compiles\n");

        System.out.println("--- TRAP 4: min/max return type matches parameter type ---");
        int m1 = Math.min(5, 10);               // ✓ Compiles (int params → int return)
        long m2 = Math.max(5L, 10L);            // ✓ Compiles (long params → long return)
        // int m3 = Math.max(5L, 10L);          // ✗ DOES NOT COMPILE (long params → long return, not int)
        int m4 = (int) Math.max(5L, 10L);       // ✓ Compiles with cast
        System.out.println("  int m1 = Math.min(5, 10);         ✓ Compiles");
        System.out.println("  long m2 = Math.max(5L, 10L);      ✓ Compiles");
        System.out.println("  int m3 = Math.max(5L, 10L);       ✗ ERROR: returns long");
        System.out.println("  int m4 = (int) Math.max(5L, 10L); ✓ Compiles\n");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * SIGNATURE QUIZ - Test Yourself!
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void signatureQuiz() {
        System.out.println("=== SIGNATURE QUIZ ===\n");
        System.out.println("Cover the answers and try to recall the signatures!\n");

        System.out.println("Question 1: What does round(float) return?");
        System.out.println("Answer: int\n");

        System.out.println("Question 2: What does round(double) return?");
        System.out.println("Answer: long\n");

        System.out.println("Question 3: What does ceil(double) return?");
        System.out.println("Answer: double (NOT int!)\n");

        System.out.println("Question 4: What does floor(double) return?");
        System.out.println("Answer: double (NOT int!)\n");

        System.out.println("Question 5: What parameters does pow() take?");
        System.out.println("Answer: two doubles (double base, double exponent)\n");

        System.out.println("Question 6: What does pow() return?");
        System.out.println("Answer: double\n");

        System.out.println("Question 7: What does min(int, int) return?");
        System.out.println("Answer: int (return type matches parameter type)\n");

        System.out.println("Question 8: What does max(long, long) return?");
        System.out.println("Answer: long (return type matches parameter type)\n");

        System.out.println("Question 9: How many parameters does random() take?");
        System.out.println("Answer: ZERO (no parameters!)\n");

        System.out.println("Question 10: What does random() return?");
        System.out.println("Answer: double (range [0.0, 1.0))\n");

        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("SUMMARY CHEAT SHEET:");
        System.out.println("  round(float) → int");
        System.out.println("  round(double) → long");
        System.out.println("  ceil(double) → double");
        System.out.println("  floor(double) → double");
        System.out.println("  pow(double, double) → double");
        System.out.println("  sqrt(double) → double");
        System.out.println("  min/max/abs → return type = parameter type");
        System.out.println("  random() → double [0.0, 1.0)");
        System.out.println("═══════════════════════════════════════════════════════════");
    }
}
