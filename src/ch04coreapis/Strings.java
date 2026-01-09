package ch04coreapis;

/**
 * STRINGS - Complete Guide for OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * STRING CONCATENATION RULES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * 1. Evaluated LEFT-TO-RIGHT
 * 2. If either operand is a String, result is String concatenation
 * 3. If both operands are numeric, result is addition
 * 4. null concatenated with String becomes "null"
 *
 * Examples:
 *   1 + 2 + "c"        → "3c" (1+2=3, then 3+"c"="3c")
 *   "c" + 1 + 2        → "c12" ("c"+1="c1", then "c1"+2="c12")
 *   "c" + (1 + 2)      → "c3" (parentheses force 1+2=3 first)
 *   "a" + null         → "anull"
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * IMPORTANT: String is IMMUTABLE
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * - All String methods return a NEW String
 * - Original String is NEVER modified
 * - Must assign result to use it!
 *
 * Common mistake:
 *   String s = "hello";
 *   s.toUpperCase();     // Does nothing! String not modified
 *   System.out.println(s); // Still "hello"
 *
 * Correct:
 *   String s = "hello";
 *   s = s.toUpperCase();   // Assign the result
 *   System.out.println(s); // "HELLO"
 */
public class Strings {

    public static void main(String[] args) {
        System.out.println("=== STRING METHODS PRACTICE ===\n");

        concatenationRules();
        lengthAndCharAt();
        indexOfMethods();
        substringMethods();
        caseAdjustment();
        equalsMethod();
        searchingSubstrings();
        replacingValues();
        removingWhitespace();
        indentationMethods();
        translateEscapes();
        emptyAndBlankChecks();
        formattingValues();
    }

    /**
     * STRING CONCATENATION RULES
     */
    private static void concatenationRules() {
        System.out.println("=== String Concatenation Rules ===");

        // Rule 1: Left-to-right evaluation
        System.out.println("1 + 2 + \"c\" = " + (1 + 2 + "c"));        // "3c"
        System.out.println("\"c\" + 1 + 2 = " + ("c" + 1 + 2));        // "c12"
        System.out.println("\"c\" + (1 + 2) = " + ("c" + (1 + 2)));    // "c3"

        // Rule 2: null becomes "null"
        String nullStr = null;
        System.out.println("\"a\" + null = " + ("a" + nullStr));       // "anull"

        // Rule 3: += works with strings
        String s = "Hello";
        s += " World";
        System.out.println("s += \" World\" = " + s);                  // "Hello World"

        System.out.println();
    }

    /**
     * length() and charAt(int)
     *
     * length() - returns number of characters (int)
     * charAt(int index) - returns character at index (0-based)
     *   - Throws StringIndexOutOfBoundsException if index invalid
     */
    private static void lengthAndCharAt() {
        System.out.println("=== length() and charAt() ===");

        String s = "Hello";
        System.out.println("String: \"" + s + "\"");
        System.out.println("length() = " + s.length());                // 5

        System.out.println("charAt(0) = " + s.charAt(0));              // 'H'
        System.out.println("charAt(4) = " + s.charAt(4));              // 'o'
        // System.out.println(s.charAt(5));  // StringIndexOutOfBoundsException!

        // EXAM TIP: length is a METHOD for String (not a property like arrays)
        String str = "Test";
        System.out.println("str.length() = " + str.length());          // Correct
        // System.out.println(str.length);  // DOES NOT COMPILE

        System.out.println();
    }

    /**
     * indexOf() - Find position of substring
     *
     * Overloaded versions:
     * - indexOf(char ch)                    - first occurrence
     * - indexOf(char ch, int fromIndex)     - first occurrence from index
     * - indexOf(String str)                 - first occurrence of string
     * - indexOf(String str, int fromIndex)  - first occurrence from index
     *
     * Returns -1 if not found
     * Returns 0-based index if found
     */
    private static void indexOfMethods() {
        System.out.println("=== indexOf() Methods ===");

        String s = "animals";
        System.out.println("String: \"" + s + "\"");

        // indexOf(char)
        System.out.println("indexOf('a') = " + s.indexOf('a'));        // 0
        System.out.println("indexOf('n') = " + s.indexOf('n'));        // 1

        // indexOf(char, fromIndex)
        System.out.println("indexOf('a', 1) = " + s.indexOf('a', 1));  // 4 (skips first 'a')

        // indexOf(String)
        System.out.println("indexOf(\"mal\") = " + s.indexOf("mal"));  // 3

        // indexOf(String, fromIndex)
        System.out.println("indexOf(\"al\", 5) = " + s.indexOf("al", 5)); // -1 (not found)

        // Not found returns -1
        System.out.println("indexOf('x') = " + s.indexOf('x'));        // -1

        System.out.println();
    }

    /**
     * substring() - Extract portion of string
     *
     * Two versions:
     * - substring(int beginIndex)              - from beginIndex to end
     * - substring(int beginIndex, int endIndex) - from beginIndex to endIndex (exclusive)
     *
     * IMPORTANT: endIndex is EXCLUSIVE (not included)
     * Throws StringIndexOutOfBoundsException if indices invalid
     */
    private static void substringMethods() {
        System.out.println("=== substring() Methods ===");

        String s = "animals";
        System.out.println("String: \"" + s + "\"");

        // substring(beginIndex) - to end
        System.out.println("substring(3) = \"" + s.substring(3) + "\"");      // "mals"

        // substring(beginIndex, endIndex) - endIndex is EXCLUSIVE
        System.out.println("substring(3, 4) = \"" + s.substring(3, 4) + "\"");  // "m"
        System.out.println("substring(3, 7) = \"" + s.substring(3, 7) + "\"");  // "mals"

        // Edge cases
        System.out.println("substring(0, 3) = \"" + s.substring(0, 3) + "\"");  // "ani"
        System.out.println("substring(3, 3) = \"" + s.substring(3, 3) + "\"");  // "" (empty)
        System.out.println("substring(7) = \"" + s.substring(7) + "\"");        // "" (empty)

        // EXAM TRAP: endIndex is length, not last index
        System.out.println("substring(0, 7) = \"" + s.substring(0, 7) + "\"");  // "animals" (all)
        // System.out.println(s.substring(0, 8)); // StringIndexOutOfBoundsException!

        System.out.println();
    }

    /**
     * Case Adjustment
     *
     * toUpperCase() - converts to uppercase
     * toLowerCase() - converts to lowercase
     *
     * Both return NEW string (original unchanged)
     */
    private static void caseAdjustment() {
        System.out.println("=== Case Adjustment ===");

        String s = "Hello World";
        System.out.println("Original: \"" + s + "\"");
        System.out.println("toUpperCase() = \"" + s.toUpperCase() + "\"");    // "HELLO WORLD"
        System.out.println("toLowerCase() = \"" + s.toLowerCase() + "\"");    // "hello world"

        // EXAM TRAP: Original string unchanged!
        System.out.println("Original still: \"" + s + "\"");                  // "Hello World"

        System.out.println();
    }

    /**
     * equals() and equalsIgnoreCase()
     *
     * equals(Object obj) - checks value equality (case-sensitive)
     * equalsIgnoreCase(String str) - checks value equality (case-insensitive)
     *
     * IMPORTANT: == checks reference equality, NOT value!
     */
    private static void equalsMethod() {
        System.out.println("=== equals() Methods ===");

        String s1 = "Hello";
        String s2 = "Hello";
        String s3 = new String("Hello");
        String s4 = "hello";

        // equals() - value comparison
        System.out.println("s1.equals(s2) = " + s1.equals(s2));              // true
        System.out.println("s1.equals(s3) = " + s1.equals(s3));              // true
        System.out.println("s1.equals(s4) = " + s1.equals(s4));              // false

        // equalsIgnoreCase() - case-insensitive
        System.out.println("s1.equalsIgnoreCase(s4) = " + s1.equalsIgnoreCase(s4)); // true

        // == checks reference (not recommended for strings)
        System.out.println("s1 == s2 = " + (s1 == s2));                      // true (string pool)
        System.out.println("s1 == s3 = " + (s1 == s3));                      // false (different object)

        System.out.println();
    }

    /**
     * Searching for Substrings
     *
     * startsWith(String prefix) - checks if starts with prefix
     * startsWith(String prefix, int offset) - checks from offset
     * endsWith(String suffix) - checks if ends with suffix
     * contains(CharSequence s) - checks if contains substring
     */
    private static void searchingSubstrings() {
        System.out.println("=== Searching for Substrings ===");

        String s = "animals";
        System.out.println("String: \"" + s + "\"");

        // startsWith()
        System.out.println("startsWith(\"an\") = " + s.startsWith("an"));    // true
        System.out.println("startsWith(\"mal\") = " + s.startsWith("mal"));  // false

        // startsWith(prefix, offset)
        System.out.println("startsWith(\"mal\", 3) = " + s.startsWith("mal", 3)); // true

        // endsWith()
        System.out.println("endsWith(\"als\") = " + s.endsWith("als"));      // true
        System.out.println("endsWith(\"ani\") = " + s.endsWith("ani"));      // false

        // contains()
        System.out.println("contains(\"nim\") = " + s.contains("nim"));      // true
        System.out.println("contains(\"xyz\") = " + s.contains("xyz"));      // false

        System.out.println();
    }

    /**
     * Replacing Values
     *
     * replace(char oldChar, char newChar) - replace all occurrences of char
     * replace(CharSequence target, CharSequence replacement) - replace all occurrences
     * replaceAll(String regex, String replacement) - replace using regex
     * replaceFirst(String regex, String replacement) - replace first match
     */
    private static void replacingValues() {
        System.out.println("=== Replacing Values ===");

        String s = "abcabc";
        System.out.println("Original: \"" + s + "\"");

        // replace(char, char) - all occurrences
        System.out.println("replace('a', 'A') = \"" + s.replace('a', 'A') + "\""); // "AbcAbc"

        // replace(CharSequence, CharSequence) - all occurrences
        System.out.println("replace(\"ab\", \"XY\") = \"" + s.replace("ab", "XY") + "\""); // "XYcXYc"

        // replaceAll(regex, replacement)
        String s2 = "a1b2c3";
        System.out.println("\nOriginal: \"" + s2 + "\"");
        System.out.println("replaceAll(\"\\\\d\", \"X\") = \"" + s2.replaceAll("\\d", "X") + "\""); // "aXbXcX"

        // replaceFirst(regex, replacement)
        System.out.println("replaceFirst(\"\\\\d\", \"X\") = \"" + s2.replaceFirst("\\d", "X") + "\""); // "aXb2c3"

        System.out.println();
    }

    /**
     * Removing Whitespace
     *
     * IMPORTANT DIFFERENCES:
     *
     * trim() - removes leading/trailing whitespace (spaces, tabs, \n, etc. up to Unicode \u0020)
     * strip() - removes leading/trailing whitespace (Unicode-aware, includes \u0020 and beyond)
     * stripLeading() - removes only leading whitespace
     * stripTrailing() - removes only trailing whitespace
     *
     * EXAM TIP: strip() is more comprehensive than trim()
     */
    private static void removingWhitespace() {
        System.out.println("=== Removing Whitespace ===");

        String s = "  abc  ";
        System.out.println("Original: [" + s + "] (length=" + s.length() + ")");

        // trim() - legacy method
        System.out.println("trim(): [" + s.trim() + "] (length=" + s.trim().length() + ")");

        // strip() - modern, Unicode-aware
        System.out.println("strip(): [" + s.strip() + "] (length=" + s.strip().length() + ")");

        // stripLeading() - only leading
        System.out.println("stripLeading(): [" + s.stripLeading() + "]");

        // stripTrailing() - only trailing
        System.out.println("stripTrailing(): [" + s.stripTrailing() + "]");

        // Difference between trim() and strip() with Unicode
        String unicode = " \u2000 abc \u2000 ";  // \u2000 is a Unicode space
        System.out.println("\nWith Unicode space (\\u2000):");
        System.out.println("trim(): [" + unicode.trim() + "]");     // Doesn't remove \u2000
        System.out.println("strip(): [" + unicode.strip() + "]");   // Removes \u2000

        System.out.println();
    }

    /**
     * INDENTATION METHODS (Java 12+)
     * VERY TRICKY FOR EXAM!
     *
     * ═══════════════════════════════════════════════════════════════════════
     * indent(int n)
     * ═══════════════════════════════════════════════════════════════════════
     *
     * 1. Normalizes line terminators to \n
     * 2. Adds or removes n spaces from each line
     *    - Positive n: adds n spaces to beginning of each line
     *    - Negative n: removes up to n spaces from beginning of each line
     * 3. Adds trailing \n if string doesn't end with one
     *
     * ═══════════════════════════════════════════════════════════════════════
     * stripIndent()
     * ═══════════════════════════════════════════════════════════════════════
     *
     * 1. Removes incidental whitespace (like text blocks)
     * 2. Finds minimum number of leading spaces across all lines
     * 3. Removes that many spaces from each line
     * 4. Normalizes line terminators to \n
     * 5. Adds trailing \n if missing
     */
    private static void indentationMethods() {
        System.out.println("=== Indentation Methods ===");

        // indent() - adds spaces
        System.out.println("--- indent() ---");
        String s1 = "Hello\nWorld";
        System.out.println("Original: [" + s1.replace("\n", "\\n") + "]");

        String indented = s1.indent(3);
        System.out.println("indent(3): [" + indented.replace("\n", "\\n") + "]");
        System.out.println("Result:");
        System.out.println(indented);
        // Result: "   Hello\n   World\n" (added 3 spaces + trailing \n)

        // indent() - removes spaces
        String s2 = "   Hello\n   World";
        System.out.println("\nOriginal: [" + s2.replace("\n", "\\n") + "]");

        String unindented = s2.indent(-2);
        System.out.println("indent(-2): [" + unindented.replace("\n", "\\n") + "]");
        System.out.println("Result:");
        System.out.println(unindented);
        // Result: " Hello\n World\n" (removed 2 spaces + added trailing \n)

        // indent() normalizes line terminators
        String s3 = "Hello\r\nWorld";  // Windows line ending
        System.out.println("\nOriginal with \\r\\n: [" + s3.replace("\r\n", "\\r\\n") + "]");
        String normalized = s3.indent(0);
        System.out.println("indent(0): [" + normalized.replace("\n", "\\n") + "]");
        // Result: "Hello\nWorld\n" (normalized to \n + added trailing \n)

        // stripIndent() - removes incidental whitespace
        System.out.println("\n--- stripIndent() ---");
        String s4 = "   Hello\n     World\n   !";
        System.out.println("Original: [" + s4.replace("\n", "\\n") + "]");

        String stripped = s4.stripIndent();
        System.out.println("stripIndent(): [" + stripped.replace("\n", "\\n") + "]");
        System.out.println("Result:");
        System.out.println(stripped);
        // Finds minimum indent (3 spaces), removes it from all lines
        // Result: "Hello\n  World\n!\n" (removed 3 spaces from each + trailing \n)

        // stripIndent() - already has trailing newline
        String s5 = "   Hello\n   World\n";
        System.out.println("\nOriginal with trailing \\n: [" + s5.replace("\n", "\\n") + "]");
        String stripped2 = s5.stripIndent();
        System.out.println("stripIndent(): [" + stripped2.replace("\n", "\\n") + "]");
        // Already has \n, so it stays: "Hello\nWorld\n"

        System.out.println();
    }

    /**
     * translateEscapes() (Java 15+)
     * CONFUSING FOR MANY!
     *
     * Converts escape sequences in String to actual characters
     * Useful when you have a String containing literal escape sequences
     *
     * Works on: \t, \n, \r, \\, \", \', \b, \f, and octal escapes
     *
     * Example: "Hello\\nWorld" becomes "Hello\nWorld" (actual newline)
     */
    private static void translateEscapes() {
        System.out.println("=== translateEscapes() ===");

        // String with literal escape sequences
        String s1 = "Hello\\nWorld";
        System.out.println("Original: \"" + s1 + "\"");
        System.out.println("  (literal backslash-n, not a newline)");

        String translated = s1.translateEscapes();
        System.out.println("\ntranslateEscapes(): \"" + translated.replace("\n", "\\n") + "\"");
        System.out.println("  (now actual newline)");
        System.out.println("Result:");
        System.out.println(translated);

        // Multiple escape sequences
        String s2 = "Line1\\nLine2\\tTabbed\\\\Backslash";
        System.out.println("\nOriginal: \"" + s2 + "\"");
        String translated2 = s2.translateEscapes();
        System.out.println("translateEscapes():");
        System.out.println(translated2);

        // EXAM TIP: Only works on literal escape sequences in the string
        String s3 = "Hello\nWorld";  // Already a real newline
        System.out.println("\nWith real newline: [" + s3.replace("\n", "\\n") + "]");
        System.out.println("translateEscapes(): [" + s3.translateEscapes().replace("\n", "\\n") + "]");

        System.out.println();
    }

    /**
     * Empty and Blank Checks
     *
     * isEmpty() - true if length is 0
     * isBlank() - true if empty OR contains only whitespace (Java 11+)
     */
    private static void emptyAndBlankChecks() {
        System.out.println("=== Empty and Blank Checks ===");

        String s1 = "";
        String s2 = "   ";
        String s3 = "abc";

        System.out.println("String: \"\" (empty)");
        System.out.println("  isEmpty() = " + s1.isEmpty());    // true
        System.out.println("  isBlank() = " + s1.isBlank());    // true

        System.out.println("\nString: \"   \" (spaces)");
        System.out.println("  isEmpty() = " + s2.isEmpty());    // false
        System.out.println("  isBlank() = " + s2.isBlank());    // true

        System.out.println("\nString: \"abc\"");
        System.out.println("  isEmpty() = " + s3.isEmpty());    // false
        System.out.println("  isBlank() = " + s3.isBlank());    // false

        System.out.println();
    }

    /**
     * FORMATTING VALUES
     * VERY IMPORTANT FOR EXAM!
     *
     * ═══════════════════════════════════════════════════════════════════════
     * COMMON FORMAT SYMBOLS
     * ═══════════════════════════════════════════════════════════════════════
     *
     * %s - String
     * %d - decimal integer (byte, short, int, long)
     * %f - floating point (float, double)
     * %n - platform-specific line separator (use this, not \n!)
     * %% - literal percent sign
     *
     * ═══════════════════════════════════════════════════════════════════════
     * FORMAT FLAGS AND SPECIFIERS
     * ═══════════════════════════════════════════════════════════════════════
     *
     * General format: %[flags][width][.precision]conversion
     *
     * WIDTH:
     *   %5d    - minimum 5 characters, right-aligned, space-padded
     *   %10s   - minimum 10 characters
     *   %3.1f  - minimum 3 characters total
     *
     * DEFAULT: No minimum width (uses only what's needed)
     *
     * FLAGS:
     *   %-5d   - left-align (default is right-align)
     *   %05d   - pad with zeros instead of spaces
     *   %+d    - show sign for positive numbers too
     *   %,d    - use locale-specific grouping separators
     *
     * PRECISION:
     *   %.2f   - 2 digits after decimal point
     *   %.0f   - no digits after decimal (rounds)
     *   %5.2f  - 5 characters total, 2 after decimal
     *
     * ═══════════════════════════════════════════════════════════════════════
     * TWO WAYS TO FORMAT
     * ═══════════════════════════════════════════════════════════════════════
     *
     * 1. String.format(format, args...)     - static method, returns String
     * 2. string.formatted(args...)          - instance method (Java 15+)
     */
    private static void formattingValues() {
        System.out.println("=== Formatting Values ===");

        // Basic format symbols
        System.out.println("--- Basic Format Symbols ---");
        String s1 = String.format("Name: %s, Age: %d", "Alice", 25);
        System.out.println(s1);

        String s2 = String.format("Pi: %f", 3.14159);
        System.out.println(s2);  // Pi: 3.141590 (default 6 decimal places)

        String s3 = String.format("100%% complete");
        System.out.println(s3);

        // Width - specifying total length
        System.out.println("\n--- Width (Total Length) ---");
        System.out.println(String.format("[%5d]", 42));       // [   42] (5 chars, right-aligned)
        System.out.println(String.format("[%5d]", 12345));    // [12345] (exactly 5)
        System.out.println(String.format("[%5d]", 123456));   // [123456] (exceeds width)

        System.out.println(String.format("[%10s]", "Hi"));    // [        Hi]

        // Default width (no width specified)
        System.out.println("\n--- Default Width (None) ---");
        System.out.println(String.format("[%d]", 42));        // [42]
        System.out.println(String.format("[%s]", "Hi"));      // [Hi]

        // Precision - digits after decimal
        System.out.println("\n--- Precision (Decimal Places) ---");
        System.out.println(String.format("%.2f", 3.14159));   // 3.14
        System.out.println(String.format("%.0f", 3.14159));   // 3 (rounds)
        System.out.println(String.format("%.4f", 3.1));       // 3.1000

        // Width + Precision
        System.out.println("\n--- Width + Precision ---");
        System.out.println(String.format("[%7.2f]", 3.14));   // [   3.14]
        System.out.println(String.format("[%7.2f]", 123.4));  // [ 123.40]

        // Flags - left align
        System.out.println("\n--- Left-Align Flag (-) ---");
        System.out.println(String.format("[%-5d]", 42));      // [42   ]
        System.out.println(String.format("[%-10s]", "Hi"));   // [Hi        ]

        // Flags - zero padding
        System.out.println("\n--- Zero Padding Flag (0) ---");
        System.out.println(String.format("[%05d]", 42));      // [00042]
        System.out.println(String.format("[%08.2f]", 3.14));  // [00003.14]

        // Flags - show positive sign
        System.out.println("\n--- Show Sign Flag (+) ---");
        System.out.println(String.format("[%+d]", 42));       // [+42]
        System.out.println(String.format("[%+d]", -42));      // [-42]

        // Flags - grouping separator
        System.out.println("\n--- Grouping Flag (,) ---");
        System.out.println(String.format("[%,d]", 1000000));  // [1,000,000]
        System.out.println(String.format("[%,d]", 1234));     // [1,234]

        // Combining flags
        System.out.println("\n--- Combining Flags ---");
        System.out.println(String.format("[%+,10d]", 1234));  // [    +1,234]
        System.out.println(String.format("[%-+,10d]", 1234)); // [+1,234    ]

        // formatted() method (Java 15+)
        System.out.println("\n--- formatted() Method ---");
        String template = "Name: %s, Score: %.1f";
        String result = template.formatted("Bob", 95.5);
        System.out.println(result);

        System.out.println();
    }
}
