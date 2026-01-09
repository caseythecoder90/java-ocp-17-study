package ch03controlflow;

/**
 * SWITCH EXPRESSIONS - Complete Guide for OCP Java 17 Exam
 * (Introduced in Java 14)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * WHAT ARE SWITCH EXPRESSIONS?
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * - A COMPACT form of switch statement that can RETURN a value
 * - Uses arrow syntax (->) instead of colon (:)
 * - NO fall-through behavior (unlike switch statements)
 * - Can be used on the right side of an assignment
 * - Must end with semicolon when returning a value
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * BOOK RULE 1: All Branches Must Return Compatible Data Type
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * All branches that don't throw an exception must return a CONSISTENT data type
 * (if the switch expression returns a value)
 *
 * VALID:
 *   int x = switch(y) {
 *       case 1 -> 10;      // returns int
 *       case 2 -> 20;      // returns int
 *       default -> 30;     // returns int
 *   };
 *
 * INVALID:
 *   var x = switch(y) {
 *       case 1 -> "hello";  // returns String
 *       case 2 -> 42;       // returns int - DOES NOT COMPILE!
 *   };
 *
 * Exception branches don't need to return a value:
 *   int x = switch(y) {
 *       case 1 -> 10;
 *       case 2 -> throw new IllegalArgumentException();  // OK - throws
 *       default -> 30;
 *   };
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * BOOK RULE 2: yield Required for Block Branches That Return Value
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * If the switch expression returns a value, every branch that ISN'T an
 * expression MUST yield a value.
 *
 * Two types of branches:
 *
 * TYPE 1: EXPRESSION (single value, no braces)
 *   case X -> expression;
 *   - NO yield needed
 *
 * TYPE 2: BLOCK (code block with braces)
 *   case X -> { statements; yield value; }
 *   - REQUIRES yield statement
 *
 * VALID:
 *   int x = switch(y) {
 *       case 1 -> {
 *           System.out.println("One");
 *           yield 10;  // Required!
 *       }
 *       case 2 -> 20;  // Expression - no yield needed
 *       default -> 30;
 *   };
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * BOOK RULE 3: default Required Unless All Cases Covered or No Value Returned
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * A default branch is REQUIRED unless:
 * 1. All possible cases are covered (e.g., all enum values), OR
 * 2. The switch expression does NOT return a value
 *
 * REQUIRED default:
 *   int x = switch(y) {
 *       case 1 -> 10;
 *       case 2 -> 20;
 *       // DOES NOT COMPILE - missing default!
 *   };
 *
 * NOT required (exhaustive enum):
 *   enum Day { MON, TUE, WED }
 *   int x = switch(day) {
 *       case MON -> 1;
 *       case TUE -> 2;
 *       case WED -> 3;  // All values covered!
 *   };
 *
 * NOT required (no return value):
 *   switch(x) {
 *       case 1 -> System.out.println("One");
 *       case 2 -> System.out.println("Two");
 *   }
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * EXAM TIPS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * 1. Arrow (->) = switch expression, Colon (:) = switch statement
 * 2. yield ONLY for block branches in switch expressions
 * 3. default required unless exhaustive or no return value
 * 4. All branches must return compatible types
 * 5. Semicolon required when returning a value
 * 6. NO fall-through (unlike switch statements)
 * 7. Cannot mix -> and : syntax
 * 8. default can appear anywhere
 */
public class SwitchExpressions {

    enum Day { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }
    enum Season { WINTER, SPRING, SUMMER, FALL }
    enum TrafficLight { RED, YELLOW, GREEN }

    public static void main(String[] args) {
        System.out.println("=== SWITCH EXPRESSIONS PRACTICE ===\n");

        example01_BasicSwitchExpression();
        example02_ExpressionVsBlock();
        example03_YieldStatement();
        example04_DefaultRequirement();
        example05_DataTypeConsistency();
        example06_EnumExhaustiveness();
        example07_NoReturnValue();
        example08_CommonTraps();
        example09_PracticeQuestions();
    }

    private static void example01_BasicSwitchExpression() {
        System.out.println("--- Example 1: Basic Switch Expression ---");

        int dayNumber = 3;
        String dayName = switch (dayNumber) {
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6, 7 -> "Weekend";
            default -> "Invalid";
        };  // Semicolon required!

        System.out.println("Day " + dayNumber + " is: " + dayName + "\n");
    }

    private static void example02_ExpressionVsBlock() {
        System.out.println("--- Example 2: Expression vs Block Branches ---");

        // Expression branch - no yield needed
        String result1 = switch (1) {
            case 1 -> "One";
            default -> "Other";
        };
        System.out.println("Expression: " + result1);

        // Block branch - yield required
        String result2 = switch (2) {
            case 1 -> {
                System.out.println("  Processing...");
                yield "One";  // Required!
            }
            case 2 -> {
                yield "Two";  // Required!
            }
            default -> "Other";
        };
        System.out.println("Block: " + result2 + "\n");
    }

    private static void example03_YieldStatement() {
        System.out.println("--- Example 3: yield Statement (BOOK RULE 2) ---");

        int score = 85;
        String grade = switch (score / 10) {
            case 10, 9 -> {
                System.out.println("  Excellent!");
                yield "A";
            }
            case 8 -> { yield "B"; }
            case 7 -> { yield "C"; }
            default -> { yield "F"; }
        };
        System.out.println("Score " + score + " = Grade " + grade + "\n");
    }

    private static void example04_DefaultRequirement() {
        System.out.println("--- Example 4: default Requirement (BOOK RULE 3) ---");

        // Required - not exhaustive
        String result1 = switch (5) {
            case 1 -> "One";
            case 2 -> "Two";
            default -> "Other";  // REQUIRED!
        };
        System.out.println("With default: " + result1);

        // Not required - exhaustive enum
        TrafficLight light = TrafficLight.RED;
        String action = switch (light) {
            case RED -> "Stop";
            case YELLOW -> "Caution";
            case GREEN -> "Go";  // All values covered!
        };
        System.out.println("Exhaustive enum: " + action + "\n");
    }

    private static void example05_DataTypeConsistency() {
        System.out.println("--- Example 5: Data Type Consistency (BOOK RULE 1) ---");

        // All branches return same type - OK
        int result1 = switch (2) {
            case 1 -> 10;
            case 2 -> 20;
            default -> 30;
        };
        System.out.println("All int: " + result1);

        // Exception branch doesn't need to return value
        int result2 = switch (1) {
            case 1 -> 10;
            case 2 -> throw new IllegalArgumentException();
            default -> 30;
        };
        System.out.println("With exception: " + result2 + "\n");
    }

    private static void example06_EnumExhaustiveness() {
        System.out.println("--- Example 6: Enum Exhaustiveness ---");

        Season season = Season.SUMMER;
        String activity = switch (season) {
            case WINTER -> "Skiing";
            case SPRING -> "Gardening";
            case SUMMER -> "Swimming";
            case FALL -> "Hiking";  // Exhaustive - no default needed!
        };
        System.out.println(season + ": " + activity + "\n");
    }

    private static void example07_NoReturnValue() {
        System.out.println("--- Example 7: No Return Value ---");

        // Not returning a value - default NOT required
        switch (5) {
            case 1 -> System.out.println("One");
            case 2 -> System.out.println("Two");
            // No default needed!
        }

        // Block without yield - OK when not returning value
        switch (1) {
            case 1 -> {
                System.out.println("Processing...");
                // No yield needed!
            }
            default -> System.out.println("Other");
        }
        System.out.println();
    }

    private static void example08_CommonTraps() {
        System.out.println("--- Example 8: Common Exam Traps ---\n");

        System.out.println("TRAP 1: Missing semicolon");
        System.out.println("  int x = switch(y) { ... }  // Missing ; - ERROR!\n");

        System.out.println("TRAP 2: Missing yield in block");
        System.out.println("  case 1 -> { System.out.println(); } // Missing yield - ERROR!\n");

        System.out.println("TRAP 3: Using return instead of yield");
        System.out.println("  case 1 -> { return 10; } // Use yield, not return - ERROR!\n");

        System.out.println("TRAP 4: Missing default when not exhaustive");
        System.out.println("  int x = switch(y) { case 1->10; case 2->20; } // ERROR!\n");

        System.out.println("TRAP 5: Mixing arrow and colon syntax");
        System.out.println("  Cannot mix -> and : in same switch - ERROR!\n");

        System.out.println("TRAP 6: Incompatible return types");
        System.out.println("  case 1 -> \"One\"; case 2 -> 2; // String vs int - ERROR!\n");
    }

    private static void example09_PracticeQuestions() {
        System.out.println("--- Example 9: Practice Questions ---\n");

        System.out.println("Q1: Does this compile?");
        System.out.println("int x = switch(y) { case 1 -> 10; case 2 -> 20; };");
        System.out.println("Answer: NO - missing default\n");

        System.out.println("Q2: Does this compile?");
        System.out.println("int x = switch(y) { case 1 -> { System.out.println(); } default -> 0; };");
        System.out.println("Answer: NO - case 1 block missing yield\n");

        System.out.println("Q3: Does this compile?");
        System.out.println("String x = switch(y) { case 1 -> \"One\"; case 2 -> 2; default -> \"X\"; };");
        System.out.println("Answer: NO - incompatible types (String vs int)\n");

        System.out.println("Q4: Does this compile?");
        System.out.println("enum Color { RED, BLUE } Color c = RED;");
        System.out.println("String x = switch(c) { case RED -> \"R\"; case BLUE -> \"B\"; };");
        System.out.println("Answer: YES - exhaustive enum\n");

        System.out.println("Q5: Does this compile?");
        System.out.println("switch(x) { case 1 -> print(\"One\"); case 2 -> print(\"Two\"); }");
        System.out.println("Answer: YES - no return value, default not required\n");
    }
}
