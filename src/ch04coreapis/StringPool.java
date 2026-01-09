package ch04coreapis;

/**
 * STRING POOL - Complete Guide for OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * WHAT IS THE STRING POOL?
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * The String Pool (also called String Constant Pool or String Intern Pool) is
 * a special memory region in the JVM that stores UNIQUE string literals.
 *
 * PURPOSE:
 *   - Saves memory by reusing string literals
 *   - Improves performance (strings are compared by reference)
 *   - Part of the heap memory
 *
 * LOCATION:
 *   - Java 7+: Part of the heap (formerly PermGen in Java 6)
 *   - Subject to garbage collection
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * HOW STRING LITERALS ARE POOLED
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * When you create a string literal, the JVM:
 * 1. Checks if the string already exists in the pool
 * 2. If YES: Returns reference to the existing string
 * 3. If NO: Adds the string to the pool and returns reference
 *
 * EXAMPLE:
 *   String s1 = "Java";      // Creates "Java" in pool
 *   String s2 = "Java";      // Reuses "Java" from pool
 *   System.out.println(s1 == s2);  // true - SAME OBJECT
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * STRING LITERAL vs NEW STRING
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * LITERAL (goes to pool):
 *   String s1 = "Java";
 *
 * NEW (creates new object, NOT in pool):
 *   String s2 = new String("Java");
 *
 * RESULT:
 *   s1 == s2  // false - DIFFERENT OBJECTS
 *   s1.equals(s2)  // true - SAME CONTENT
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * THE intern() METHOD
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * String.intern() manually adds a string to the pool (or retrieves existing one).
 *
 * BEHAVIOR:
 *   - If string exists in pool: Returns reference to pooled string
 *   - If string NOT in pool: Adds it to pool and returns reference
 *
 * USE CASE:
 *   - Convert non-pooled strings to pooled strings
 *   - Useful when you have many duplicate strings created at runtime
 *
 * SYNTAX:
 *   String s = new String("Java").intern();
 *   // s now references the pooled "Java" string
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * EXAM TIPS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * 1. String literals ALWAYS use the pool
 * 2. new String() NEVER uses the pool (unless you call intern())
 * 3. Use == to compare references (same object)
 * 4. Use equals() to compare content (same value)
 * 5. intern() returns a reference to the pooled string
 * 6. Concatenation with + at compile-time is pooled
 * 7. Concatenation with + at runtime creates new objects
 */
public class StringPool {

    public static void main(String[] args) {
        System.out.println("=== STRING POOL PRACTICE ===\n");

        example01_BasicStringPool();
        example02_LiteralVsNew();
        example03_InternMethod();
        example04_CompileTimeVsRuntime();
        example05_ConcatenationPooling();
        example06_InternWithNew();
        example07_PracticalUseCase();
        example08_CommonExamTraps();
    }

    private static void example01_BasicStringPool() {
        System.out.println("--- Example 1: Basic String Pool ---");

        // Both literals reference the SAME object in the pool
        String s1 = "Java";
        String s2 = "Java";

        System.out.println("s1: " + s1);
        System.out.println("s2: " + s2);
        System.out.println("s1 == s2: " + (s1 == s2));           // true - same reference
        System.out.println("s1.equals(s2): " + s1.equals(s2));   // true - same content

        // Different literals create different objects
        String s3 = "Python";
        System.out.println("s1 == s3: " + (s1 == s3) + "\n");    // false - different strings
    }

    private static void example02_LiteralVsNew() {
        System.out.println("--- Example 2: Literal vs new String() ---");

        String s1 = "Java";              // Pooled
        String s2 = new String("Java");  // NOT pooled - creates new object

        System.out.println("s1: " + s1);
        System.out.println("s2: " + s2);
        System.out.println("s1 == s2: " + (s1 == s2));           // false - different objects!
        System.out.println("s1.equals(s2): " + s1.equals(s2));   // true - same content

        // new String() creates a NEW object even if literal exists
        String s3 = new String("Java");
        System.out.println("s2 == s3: " + (s2 == s3) + "\n");    // false - different objects
    }

    private static void example03_InternMethod() {
        System.out.println("--- Example 3: intern() Method ---");

        // Create non-pooled string
        String s1 = new String("Hello");
        String s2 = "Hello";  // Pooled

        System.out.println("Before intern():");
        System.out.println("s1 == s2: " + (s1 == s2));  // false - different objects

        // intern() returns reference to pooled string
        String s3 = s1.intern();
        System.out.println("\nAfter intern():");
        System.out.println("s3 == s2: " + (s3 == s2));  // true - both reference pool!
        System.out.println("s1 == s3: " + (s1 == s3));  // false - s1 still non-pooled

        // Calling intern() on already pooled string
        String s4 = "World";
        String s5 = s4.intern();
        System.out.println("s4 == s5: " + (s4 == s5) + "\n");  // true - already pooled
    }

    private static void example04_CompileTimeVsRuntime() {
        System.out.println("--- Example 4: Compile-Time vs Runtime ---");

        // Compile-time concatenation - POOLED
        String s1 = "Java" + "17";      // Compiled to "Java17"
        String s2 = "Java17";
        System.out.println("Compile-time concat:");
        System.out.println("s1 == s2: " + (s1 == s2));  // true - both pooled

        // Runtime concatenation - NOT POOLED
        String part1 = "Java";
        String s3 = part1 + "17";       // Concatenated at runtime
        System.out.println("\nRuntime concat:");
        System.out.println("s2 == s3: " + (s2 == s3));  // false - s3 not pooled

        // But content is the same
        System.out.println("s2.equals(s3): " + s2.equals(s3) + "\n");  // true
    }

    private static void example05_ConcatenationPooling() {
        System.out.println("--- Example 5: Concatenation and Pooling ---");

        // final variables are compile-time constants
        final String CONST1 = "Hello";
        final String CONST2 = "World";
        String s1 = CONST1 + CONST2;  // Compile-time - POOLED
        String s2 = "HelloWorld";

        System.out.println("final concatenation:");
        System.out.println("s1 == s2: " + (s1 == s2));  // true - compile-time optimization

        // Non-final variables - runtime concatenation
        String var1 = "Hello";
        String var2 = "World";
        String s3 = var1 + var2;  // Runtime - NOT POOLED

        System.out.println("\nNon-final concatenation:");
        System.out.println("s2 == s3: " + (s2 == s3) + "\n");  // false
    }

    private static void example06_InternWithNew() {
        System.out.println("--- Example 6: intern() with new String() ---");

        // Without intern() - creates new object
        String s1 = new String("Test");
        String s2 = new String("Test");
        System.out.println("Without intern():");
        System.out.println("s1 == s2: " + (s1 == s2));  // false

        // With intern() - uses pool
        String s3 = new String("Test").intern();
        String s4 = new String("Test").intern();
        System.out.println("\nWith intern():");
        System.out.println("s3 == s4: " + (s3 == s4));  // true - both from pool

        // intern() returns pooled version
        String s5 = "Test";
        System.out.println("s3 == s5: " + (s3 == s5) + "\n");  // true
    }

    private static void example07_PracticalUseCase() {
        System.out.println("--- Example 7: Practical Use Case for intern() ---");

        // Scenario: Reading many duplicate strings from external source
        // (simulating database/file reads)
        String[] userInputs = {
            new String("ACTIVE"),
            new String("INACTIVE"),
            new String("ACTIVE"),
            new String("PENDING"),
            new String("ACTIVE"),
            new String("INACTIVE")
        };

        System.out.println("Without intern() - many duplicate objects:");
        int uniqueRefs = countUniqueReferences(userInputs);
        System.out.println("Unique references: " + uniqueRefs);  // 6 - all different

        // With intern() - reuse pooled strings
        String[] internedInputs = new String[userInputs.length];
        for (int i = 0; i < userInputs.length; i++) {
            internedInputs[i] = userInputs[i].intern();
        }

        System.out.println("\nWith intern() - shared references:");
        uniqueRefs = countUniqueReferences(internedInputs);
        System.out.println("Unique references: " + uniqueRefs + "\n");  // 3 - reused
    }

    private static int countUniqueReferences(String[] strings) {
        int unique = 0;
        for (int i = 0; i < strings.length; i++) {
            boolean isDuplicate = false;
            for (int j = 0; j < i; j++) {
                if (strings[i] == strings[j]) {  // Reference comparison
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate) unique++;
        }
        return unique;
    }

    private static void example08_CommonExamTraps() {
        System.out.println("--- Example 8: Common Exam Traps ---\n");

        System.out.println("TRAP 1: new String() does NOT use pool");
        String s1 = "Java";
        String s2 = new String("Java");
        System.out.println("  s1 == s2: " + (s1 == s2));  // false!

        System.out.println("\nTRAP 2: intern() RETURNS the pooled reference");
        String s3 = new String("Test");
        s3.intern();  // Calling intern() but NOT using the return value!
        String s4 = "Test";
        System.out.println("  s3 == s4: " + (s3 == s4));  // false - must assign return value!

        String s5 = new String("Test2").intern();  // Correct - using return value
        String s6 = "Test2";
        System.out.println("  s5 == s6: " + (s5 == s6));  // true

        System.out.println("\nTRAP 3: Runtime concatenation doesn't pool");
        String a = "Hello";
        String b = a + " World";  // Runtime concatenation
        String c = "Hello World";
        System.out.println("  b == c: " + (b == c));  // false

        System.out.println("\nTRAP 4: But compile-time concatenation DOES pool");
        String d = "Hello" + " World";  // Compile-time optimization
        String e = "Hello World";
        System.out.println("  d == e: " + (d == e));  // true

        System.out.println("\nTRAP 5: == checks references, not content");
        String f = new String("Java");
        String g = new String("Java");
        System.out.println("  f == g: " + (f == g));        // false - different objects
        System.out.println("  f.equals(g): " + f.equals(g));  // true - same content

        System.out.println("\nTRAP 6: Empty string literal is pooled");
        String h = "";
        String i = "";
        System.out.println("  h == i: " + (h == i));  // true - both reference pool

        System.out.println("\nTRAP 7: StringBuilder/StringBuffer results are NOT pooled");
        StringBuilder sb = new StringBuilder("Java");
        String j = sb.toString();  // Creates new String - NOT pooled
        String k = "Java";         // Pooled
        System.out.println("  j == k: " + (j == k));  // false
        String m = j.intern();     // Now pooled
        System.out.println("  m == k: " + (m == k));  // true
    }
}
