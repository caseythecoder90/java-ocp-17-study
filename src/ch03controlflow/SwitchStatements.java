package ch03controlflow;

/**
 * SWITCH STATEMENTS - Complete Guide for OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * RULE 1: Break Statements are OPTIONAL
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * - You can omit break statements
 * - Without break, execution "falls through" to the next case
 * - This is called "fall-through" behavior
 * - Fall-through is INTENTIONAL in some cases, ACCIDENTAL in others (watch for bugs!)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * RULE 2: Default Branch is OPTIONAL
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * - The default case is NOT required
 * - If no case matches and no default, the switch does nothing
 * - Default can appear ANYWHERE in the switch (but conventionally at the end)
 * - Only ONE default per switch
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * RULE 3: Fall-Through Behavior
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * If you match a case and DON'T have a break:
 * - Execution continues to the NEXT case (even if it doesn't match!)
 * - It will execute ALL subsequent cases until it hits a break or the end
 * - This includes the DEFAULT case if it's encountered
 *
 * Example:
 *   case 1:
 *       System.out.println("One");
 *       // NO BREAK - falls through!
 *   case 2:
 *       System.out.println("Two");
 *       break;
 *
 * If input is 1, output is:
 *   One
 *   Two
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * RULE 4: Allowed Data Types
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ALLOWED types for switch variable:
 * - int and Integer
 * - byte and Byte
 * - short and Short
 * - char and Character
 * - String (since Java 7)
 * - enum values
 * - var (if it resolves to one of the above types)
 *
 * NOT ALLOWED:
 * - long, Long
 * - float, Float
 * - double, Double
 * - boolean, Boolean
 * - Arrays
 * - Objects (except String and wrapper types listed above)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * RULE 5: Case Values Must Be Compile-Time Constants
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Case values MUST be compile-time constants:
 *
 * ALLOWED:
 * - Literals: case 1:  case 'A':  case "hello":
 * - Enum constants: case RED:
 * - Final constant variables: final int MAX = 10;  case MAX:
 *
 * NOT ALLOWED:
 * - Variables: int x = 5; case x:  // DOES NOT COMPILE
 * - Non-final variables: case myVariable:  // DOES NOT COMPILE
 * - Method calls: case getValue():  // DOES NOT COMPILE
 * - Expressions with variables: case x + 1:  // DOES NOT COMPILE
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * RULE 6: Data Type Matching
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * The data type of case values MUST match the switch variable type:
 *
 * VALID:
 *   int x = 5;
 *   switch(x) {
 *       case 1:   // int literal matches int variable
 *       case 2:
 *   }
 *
 * INVALID:
 *   int x = 5;
 *   switch(x) {
 *       case "hello":  // DOES NOT COMPILE - String doesn't match int
 *   }
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * RULE 7: Case Values Must Be Unique
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * You CANNOT have duplicate case values:
 *
 * INVALID:
 *   switch(x) {
 *       case 1:
 *           System.out.println("One");
 *           break;
 *       case 1:  // DOES NOT COMPILE - duplicate!
 *           System.out.println("Also one");
 *           break;
 *   }
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * EXAM TIPS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * 1. Watch for missing break statements - fall-through is a common exam trap!
 * 2. Remember: long, float, double, boolean are NOT allowed
 * 3. Case values must be compile-time constants (final or literals)
 * 4. Default can appear anywhere, not just at the end
 * 5. Null values in switch will throw NullPointerException at runtime
 * 6. Switch variable can be null (compiles), but throws exception when evaluated
 */
public class SwitchStatements {

    // Enum for examples
    enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    enum Season {
        WINTER, SPRING, SUMMER, FALL
    }

    public static void main(String[] args) {
        System.out.println("=== SWITCH STATEMENTS PRACTICE ===\n");

        example01_BasicSwitch();
        example02_FallThrough();
        example03_DefaultCase();
        example04_AllowedTypes();
        example05_CompileTimeConstants();
        example06_DataTypeMatching();
        example07_CommonTraps();
        example08_PracticeQuestions();
    }

    /**
     * Example 1: Basic Switch Statement
     */
    private static void example01_BasicSwitch() {
        System.out.println("--- Example 1: Basic Switch ---");

        int day = 3;
        switch (day) {
            case 1:
                System.out.println("Monday");
                break;
            case 2:
                System.out.println("Tuesday");
                break;
            case 3:
                System.out.println("Wednesday");
                break;
            case 4:
                System.out.println("Thursday");
                break;
            case 5:
                System.out.println("Friday");
                break;
            default:
                System.out.println("Weekend");
                break;
        }
        System.out.println();
    }

    /**
     * Example 2: Fall-Through Behavior
     * VERY IMPORTANT FOR EXAM!
     */
    private static void example02_FallThrough() {
        System.out.println("--- Example 2: Fall-Through Behavior ---");

        // Example 2a: Intentional fall-through (no break)
        int month = 2;
        System.out.println("Month " + month + " has:");
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                System.out.println("  31 days");
                break;
            case 4: case 6: case 9: case 11:
                System.out.println("  30 days");
                break;
            case 2:
                System.out.println("  28 or 29 days");
                break;
            default:
                System.out.println("  Invalid month");
        }

        // Example 2b: Fall-through without break (EXAM TRAP!)
        System.out.println("\nFall-through example (no breaks):");
        int x = 1;
        switch (x) {
            case 1:
                System.out.println("  Case 1");
                // NO BREAK - falls through!
            case 2:
                System.out.println("  Case 2");
                // NO BREAK - falls through!
            case 3:
                System.out.println("  Case 3");
                // NO BREAK - falls through!
            default:
                System.out.println("  Default");
        }
        System.out.println("Output: All four print statements execute!\n");

        // Example 2c: Partial fall-through
        System.out.println("Partial fall-through example:");
        int y = 2;
        switch (y) {
            case 1:
                System.out.println("  One");
                break;
            case 2:
                System.out.println("  Two");
                // NO BREAK - falls through!
            case 3:
                System.out.println("  Three");
                break;
            case 4:
                System.out.println("  Four");
                break;
        }
        System.out.println("Output: 'Two' and 'Three' both printed!\n");
        System.out.println();
    }

    /**
     * Example 3: Default Case
     * Default is optional and can appear anywhere
     */
    private static void example03_DefaultCase() {
        System.out.println("--- Example 3: Default Case ---");

        // Example 3a: No default (no match = no output)
        int x = 99;
        System.out.println("No default, x = 99:");
        switch (x) {
            case 1:
                System.out.println("  One");
                break;
            case 2:
                System.out.println("  Two");
                break;
        }
        System.out.println("  (nothing printed)\n");

        // Example 3b: Default at the end (conventional)
        System.out.println("Default at end, x = 99:");
        switch (x) {
            case 1:
                System.out.println("  One");
                break;
            case 2:
                System.out.println("  Two");
                break;
            default:
                System.out.println("  Other");
                break;
        }

        // Example 3c: Default in the middle (allowed but unusual)
        System.out.println("\nDefault in middle, x = 99:");
        switch (x) {
            case 1:
                System.out.println("  One");
                break;
            default:
                System.out.println("  Other");
                break;
            case 2:
                System.out.println("  Two");
                break;
        }

        // Example 3d: Default with fall-through
        System.out.println("\nDefault with fall-through, x = 99:");
        switch (x) {
            case 1:
                System.out.println("  One");
                break;
            default:
                System.out.println("  Default");
                // NO BREAK - falls through!
            case 2:
                System.out.println("  Two");
                break;
        }
        System.out.println("Output: Both 'Default' and 'Two' printed!\n");
        System.out.println();
    }

    /**
     * Example 4: All Allowed Data Types
     */
    private static void example04_AllowedTypes() {
        System.out.println("--- Example 4: Allowed Data Types ---");

        // int
        int intValue = 1;
        switch (intValue) {
            case 1:
                System.out.println("int: One");
                break;
        }

        // Integer (wrapper)
        Integer integerValue = 2;
        switch (integerValue) {
            case 2:
                System.out.println("Integer: Two");
                break;
        }

        // byte
        byte byteValue = 3;
        switch (byteValue) {
            case 3:
                System.out.println("byte: Three");
                break;
        }

        // Byte (wrapper)
        Byte byteWrapper = 4;
        switch (byteWrapper) {
            case 4:
                System.out.println("Byte: Four");
                break;
        }

        // short
        short shortValue = 5;
        switch (shortValue) {
            case 5:
                System.out.println("short: Five");
                break;
        }

        // Short (wrapper)
        Short shortWrapper = 6;
        switch (shortWrapper) {
            case 6:
                System.out.println("Short: Six");
                break;
        }

        // char
        char charValue = 'A';
        switch (charValue) {
            case 'A':
                System.out.println("char: A");
                break;
            case 'B':
                System.out.println("char: B");
                break;
        }

        // Character (wrapper)
        Character charWrapper = 'Z';
        switch (charWrapper) {
            case 'Z':
                System.out.println("Character: Z");
                break;
        }

        // String
        String stringValue = "hello";
        switch (stringValue) {
            case "hello":
                System.out.println("String: hello");
                break;
            case "world":
                System.out.println("String: world");
                break;
        }

        // enum
        Day today = Day.FRIDAY;
        switch (today) {
            case MONDAY:
                System.out.println("enum: Start of week");
                break;
            case FRIDAY:
                System.out.println("enum: TGIF!");
                break;
            case SATURDAY:
            case SUNDAY:
                System.out.println("enum: Weekend!");
                break;
        }

        // var (resolves to int)
        var varValue = 10;
        switch (varValue) {
            case 10:
                System.out.println("var: Ten");
                break;
        }

        // THESE DO NOT COMPILE:
        // long longValue = 100L;
        // switch(longValue) { }  // DOES NOT COMPILE

        // double doubleValue = 3.14;
        // switch(doubleValue) { }  // DOES NOT COMPILE

        // boolean boolValue = true;
        // switch(boolValue) { }  // DOES NOT COMPILE

        System.out.println();
    }

    /**
     * Example 5: Compile-Time Constants
     * Case values MUST be compile-time constants
     */
    private static void example05_CompileTimeConstants() {
        System.out.println("--- Example 5: Compile-Time Constants ---");

        int value = 2;

        // VALID: Literal values
        switch (value) {
            case 1:  // Literal - OK
                System.out.println("One");
                break;
            case 2:  // Literal - OK
                System.out.println("Two");
                break;
        }

        // VALID: Final constant variable
        final int MAX = 10;
        final int MIN = 1;
        switch (value) {
            case MIN:  // Final constant - OK
                System.out.println("Minimum");
                break;
            case MAX:  // Final constant - OK
                System.out.println("Maximum");
                break;
        }

        // VALID: Enum constants
        Day day = Day.MONDAY;
        switch (day) {
            case MONDAY:    // Enum constant - OK
                System.out.println("Monday");
                break;
            case TUESDAY:   // Enum constant - OK
                System.out.println("Tuesday");
                break;
        }

        // INVALID EXAMPLES (commented out - would not compile):

        // Non-final variable
        // int x = 5;
        // switch(value) {
        //     case x:  // DOES NOT COMPILE - not final
        //         break;
        // }

        // Final but not initialized with constant
        // final int y = getValue();  // method call
        // switch(value) {
        //     case y:  // DOES NOT COMPILE - not compile-time constant
        //         break;
        // }

        // Expression with variables
        // int z = 5;
        // switch(value) {
        //     case z + 1:  // DOES NOT COMPILE - not constant
        //         break;
        // }

        System.out.println();
    }

    /**
     * Example 6: Data Type Matching
     * Case values must match switch variable type
     */
    private static void example06_DataTypeMatching() {
        System.out.println("--- Example 6: Data Type Matching ---");

        // VALID: int with int literals
        int intVal = 5;
        switch (intVal) {
            case 1:  // int literal matches int
            case 5:
                System.out.println("Valid: int with int literal");
                break;
        }

        // VALID: String with String literals
        String strVal = "test";
        switch (strVal) {
            case "test":  // String literal matches String
            case "hello":
                System.out.println("Valid: String with String literal");
                break;
        }

        // VALID: char with char literals
        char charVal = 'A';
        switch (charVal) {
            case 'A':  // char literal matches char
            case 'B':
                System.out.println("Valid: char with char literal");
                break;
        }

        // INVALID EXAMPLES (would not compile):

        // int with String case
        // switch(intVal) {
        //     case "hello":  // DOES NOT COMPILE - type mismatch
        //         break;
        // }

        // String with int case
        // switch(strVal) {
        //     case 1:  // DOES NOT COMPILE - type mismatch
        //         break;
        // }

        // char with int case (tricky - char is numeric but requires exact type)
        // switch(charVal) {
        //     case 65:  // DOES NOT COMPILE - int literal, not char
        //         break;
        // }
        // Note: char can be promoted to int, but not in case labels!

        System.out.println();
    }

    /**
     * Example 7: Common Exam Traps
     */
    private static void example07_CommonTraps() {
        System.out.println("--- Example 7: Common Exam Traps ---");

        // TRAP 1: Null switch value
        System.out.println("TRAP 1: Null switch value");
        String text = null;
        try {
            switch (text) {  // Compiles, but throws NullPointerException at runtime!
                case "hello":
                    System.out.println("Hello");
                    break;
            }
        } catch (NullPointerException e) {
            System.out.println("  NullPointerException thrown!\n");
        }

        // TRAP 2: Fall-through to default
        System.out.println("TRAP 2: Fall-through includes default");
        int x = 1;
        switch (x) {
            case 1:
                System.out.println("  One");
                // NO BREAK - falls through to default!
            default:
                System.out.println("  Default (executed even though x matched case 1!)");
                break;
        }

        // TRAP 3: Empty switch (valid but useless)
        System.out.println("\nTRAP 3: Empty switch (compiles)");
        int y = 5;
        switch (y) {
            // No cases at all - compiles but does nothing
        }
        System.out.println("  Empty switch does nothing\n");

        // TRAP 4: Unreachable code after case without break
        System.out.println("TRAP 4: Be careful with unreachable code");
        int z = 1;
        switch (z) {
            case 1:
                System.out.println("  One");
                // If you put code here after falling through, be careful!
            case 2:
                System.out.println("  Two (executes when z=1 due to fall-through)");
                break;
        }

        // TRAP 5: Case values must be unique
        System.out.println("\nTRAP 5: Duplicate case values");
        System.out.println("  This does NOT compile:");
        System.out.println("  switch(x) {");
        System.out.println("      case 1: break;");
        System.out.println("      case 1: break;  // ERROR: duplicate");
        System.out.println("  }");

        System.out.println();
    }

    /**
     * Example 8: Practice Questions
     */
    private static void example08_PracticeQuestions() {
        System.out.println("--- Example 8: Practice Questions ---");

        System.out.println("Question 1: What is the output?");
        System.out.println("int x = 2;");
        System.out.println("switch(x) {");
        System.out.println("    case 1: System.out.print(\"A\");");
        System.out.println("    case 2: System.out.print(\"B\");");
        System.out.println("    case 3: System.out.print(\"C\");");
        System.out.println("}");
        System.out.print("Answer: ");
        int x1 = 2;
        switch (x1) {
            case 1: System.out.print("A");
            case 2: System.out.print("B");
            case 3: System.out.print("C");
        }
        System.out.println(" (falls through to case 3)\n");

        System.out.println("Question 2: What is the output?");
        System.out.println("int x = 1;");
        System.out.println("switch(x) {");
        System.out.println("    case 1: System.out.print(\"A\"); break;");
        System.out.println("    default: System.out.print(\"B\");");
        System.out.println("    case 2: System.out.print(\"C\"); break;");
        System.out.println("}");
        System.out.print("Answer: ");
        int x2 = 1;
        switch (x2) {
            case 1: System.out.print("A"); break;
            default: System.out.print("B");
            case 2: System.out.print("C"); break;
        }
        System.out.println(" (matches case 1, prints A, then breaks)\n");

        System.out.println("Question 3: What is the output?");
        System.out.println("int x = 5;");
        System.out.println("switch(x) {");
        System.out.println("    case 1: System.out.print(\"A\"); break;");
        System.out.println("    default: System.out.print(\"B\");");
        System.out.println("    case 2: System.out.print(\"C\"); break;");
        System.out.println("}");
        System.out.print("Answer: ");
        int x3 = 5;
        switch (x3) {
            case 1: System.out.print("A"); break;
            default: System.out.print("B");
            case 2: System.out.print("C"); break;
        }
        System.out.println(" (matches default, no break, falls to case 2)\n");

        System.out.println("Question 4: Does this compile?");
        System.out.println("long x = 10L;");
        System.out.println("switch(x) { case 1: break; }");
        System.out.println("Answer: NO - long is not an allowed type\n");

        System.out.println("Question 5: Does this compile?");
        System.out.println("int x = 5;");
        System.out.println("int y = 10;");
        System.out.println("switch(x) { case y: break; }");
        System.out.println("Answer: NO - y is not a compile-time constant (not final)\n");

        System.out.println("Question 6: Does this compile?");
        System.out.println("final int MAX = 100;");
        System.out.println("int x = 50;");
        System.out.println("switch(x) { case MAX: break; }");
        System.out.println("Answer: YES - MAX is a final compile-time constant\n");
    }

    /**
     * Helper method for examples (not called)
     */
    private static int getValue() {
        return 5;
    }
}
