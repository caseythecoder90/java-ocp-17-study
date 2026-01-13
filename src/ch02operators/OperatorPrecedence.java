package ch02operators;

/// OPERATOR PRECEDENCE - Complete Guide for OCP Java 17 Exam
/// ═══════════════════════════════════════════════════════════════════════════
/// THREE TYPES OF OPERATORS
/// ═══════════════════════════════════════════════════════════════════════════
/// 1. UNARY OPERATORS (operate on ONE operand)
///    - Post-unary: expr++, expr--
///    - Pre-unary: ++expr, --expr
///    - Other unary: +, -, !
///    - Cast: (type)
/// 2. BINARY OPERATORS (operate on TWO operands)
///    - Arithmetic: *, /, %, +, -
///    - Shift: <<, >>, >>>
///    - Relational: <, >, <=, >=, instanceof
///    - Equality: ==, !=
///    - Logical: &, ^, |, &&, ||
///    - Assignment: =, +=, -=, *=, /=, %=, &=, ^=, |=, <<=, >>=, >>>=
/// 3. TERNARY OPERATOR (operates on THREE operands)
///    - Conditional: ? :
///    - Example: result = (condition) ? valueIfTrue : valueIfFalse
///
/// ═══════════════════════════════════════════════════════════════════════════
///
/// OPERATOR PRECEDENCE TABLE (Highest to Lowest)
///
/// ═══════════════════════════════════════════════════════════════════════════
///
/// Priority | Operator Type              | Operators          | Associativity
///
/// ---------|----------------------------|--------------------|--------------
///
///    1     | Post-unary                 | expr++  expr--     | Left-to-right
///
///    2     | Pre-unary                  | ++expr  --expr     | Right-to-left
///
///    3     | Other unary                | +  -  !            | Right-to-left
///
///    4     | Cast                       | (type)             | Right-to-left
///
///    5     | Multiplication/Division    | *  /  %            | Left-to-right
///
///    6     | Addition/Subtraction       | +  -               | Left-to-right
///
///    7     | Shift operators            | <<  >>  >>>        | Left-to-right
///
///    8     | Relational                 | <  >  <=  >=       | Left-to-right
///
///          |                            | instanceof         |
///
///    9     | Equal to/Not equal to      | ==  !=             | Left-to-right
///
///   10     | Logical AND                | &                  | Left-to-right
///
///   11     | Logical exclusive OR       | ^                  | Left-to-right
///
///   12     | Logical inclusive OR       | |                  | Left-to-right
///
///   13     | Conditional AND            | &&                 | Left-to-right
///
///   14     | Conditional OR             | ||                 | Left-to-right
///
///   15     | Ternary                    | ? :                | Right-to-left
///
///   16     | Assignment                 | =  +=  -=  *=  /=  | Right-to-left
///
///          |                            | %=  &=  ^=  |=     |
///
///          |                            | <<=  >>=  >>>=     |
///
///   17     | Arrow operator             | ->                 | Right-to-left
///
/// ═══════════════════════════════════════════════════════════════════════════
/// MEMORY TRICK: "Please Pray Over Code, My Shifts Really Equal Logic
///                And eXclusive ORs Convince Coders To Always Remember"
/// ═══════════════════════════════════════════════════════════════════════════
/// P - Post-unary
///
/// P - Pre-unary
///
/// O - Other unary
///
/// C - Cast
///
/// M - Multiplication/Division/Modulus
///
///
/// S - Shift
///
/// R - Relational
///
/// E - Equal/Not Equal
///
/// L - Logical AND (&)
///
/// A - (covered above)
///
/// X - eXclusive OR (^)
///
/// O - inclusive OR (|)
///
/// C - Conditional AND (&&)
///
/// C - Conditional OR (||)
///
/// T - Ternary
///
/// A - Assignment
///
/// R - (aRrow)
/// ═══════════════════════════════════════════════════════════════════════════
/// KEY EXAM POINTS
/// ═══════════════════════════════════════════════════════════════════════════
/// 1. Post-increment/decrement (x++) has HIGHER precedence than pre-increment (++x)
/// 2. Unary operators have HIGHER precedence than binary operators
/// 3. Multiplication/Division/Modulus come BEFORE Addition/Subtraction
/// 4. Relational operators (<, >, <=, >=) come BEFORE equality (==, !=)
/// 5. Logical & comes BEFORE ^, which comes BEFORE |
/// 6. Conditional && comes BEFORE ||
/// 7. Assignment has very LOW precedence (almost last)
/// 8. When precedence is equal, use associativity (left-to-right or right-to-left)
/// ═══════════════════════════════════════════════════════════════════════════
/// ASSOCIATIVITY RULES
/// ═══════════════════════════════════════════════════════════════════════════
/// LEFT-TO-RIGHT: Most operators
///   Example: a - b - c  →  (a - b) - c
/// RIGHT-TO-LEFT: Assignment and unary operators
///   Example: a = b = c  →  a = (b = c)
///   Example: -~x  →  -(~x)
public class OperatorPrecedence {

    public static void main(String[] args) {
        System.out.println("=== OPERATOR PRECEDENCE PRACTICE ===\n");

        // Examples demonstrating precedence
        example01_PostVsPreUnary();
        example02_UnaryVsBinary();
        example03_ArithmeticPrecedence();
        example04_RelationalVsEquality();
        example05_LogicalPrecedence();
        example06_ConditionalPrecedence();
        example07_AssignmentPrecedence();
        example08_ComplexExpressions();

        // Practice questions
        System.out.println("\n" + "=".repeat(70));
        practiceQuestions();
    }

    /**
     * Example 1: Post-unary vs Pre-unary
     * Post-unary has HIGHER precedence
     */
    private static void example01_PostVsPreUnary() {
        System.out.println("--- Example 1: Post-unary vs Pre-unary ---");

        int x = 5;
        int result = x++ * 2;  // Post-unary first: (x++) * 2 = 5 * 2 = 10, then x becomes 6
        System.out.println("x++ * 2 where x=5:");
        System.out.println("  Result: " + result + " (uses original value 5)");
        System.out.println("  x is now: " + x);

        x = 5;
        result = ++x * 2;      // Pre-unary: (++x) * 2 = 6 * 2 = 12
        System.out.println("\n++x * 2 where x=5:");
        System.out.println("  Result: " + result + " (uses incremented value 6)");
        System.out.println("  x is now: " + x);
        System.out.println();
    }

    /**
     * Example 2: Unary operators have higher precedence than binary
     */
    private static void example02_UnaryVsBinary() {
        System.out.println("--- Example 2: Unary vs Binary ---");

        int a = -3 + 2;  // Unary - applied first: (-3) + 2 = -1
        System.out.println("-3 + 2 = " + a);

        int b = 10;
        int c = -b * 2;  // Unary - first: (-b) * 2 = -10 * 2 = -20
        System.out.println("-b * 2 where b=10: " + c);

        boolean flag = !true && false;  // ! first: (!true) && false = false && false = false
        System.out.println("!true && false = " + flag);
        System.out.println();
    }

    /**
     * Example 3: Arithmetic operator precedence
     * * / % before + -
     */
    private static void example03_ArithmeticPrecedence() {
        System.out.println("--- Example 3: Arithmetic Precedence ---");

        int result1 = 2 + 3 * 4;      // * first: 2 + (3 * 4) = 2 + 12 = 14
        System.out.println("2 + 3 * 4 = " + result1);

        int result2 = 10 - 6 / 2;     // / first: 10 - (6 / 2) = 10 - 3 = 7
        System.out.println("10 - 6 / 2 = " + result2);

        int result3 = 20 / 4 * 2;     // Same precedence, left-to-right: (20 / 4) * 2 = 5 * 2 = 10
        System.out.println("20 / 4 * 2 = " + result3 + " (left-to-right)");

        int result4 = 10 % 3 + 2;     // % first: (10 % 3) + 2 = 1 + 2 = 3
        System.out.println("10 % 3 + 2 = " + result4);
        System.out.println();
    }

    /**
     * Example 4: Relational before Equality
     * <, >, <=, >= evaluated before ==, !=
     */
    private static void example04_RelationalVsEquality() {
        System.out.println("--- Example 4: Relational vs Equality ---");

        boolean b1 = 5 > 3 == true;   // > first: (5 > 3) == true = true == true = true
        System.out.println("5 > 3 == true: " + b1);

        boolean b2 = 10 < 20 != false; // < first: (10 < 20) != false = true != false = true
        System.out.println("10 < 20 != false: " + b2);

        // Common exam trap!
        // boolean b3 = 5 < 10 < 15;  // DOES NOT COMPILE! (5 < 10) returns boolean, can't compare boolean < 15
        System.out.println();
    }

    /**
     * Example 5: Logical operator precedence
     * & before ^ before |
     */
    private static void example05_LogicalPrecedence() {
        System.out.println("--- Example 5: Logical Precedence ---");

        boolean result1 = true | false & false;  // & first: true | (false & false) = true | false = true
        System.out.println("true | false & false = " + result1);

        boolean result2 = true & false | true;   // & first: (true & false) | true = false | true = true
        System.out.println("true & false | true = " + result2);

        boolean result3 = true ^ false & true;   // & first: true ^ (false & true) = true ^ false = true
        System.out.println("true ^ false & true = " + result3);
        System.out.println();
    }

    /**
     * Example 6: Conditional operator precedence
     * && before ||
     */
    private static void example06_ConditionalPrecedence() {
        System.out.println("--- Example 6: Conditional Precedence ---");

        boolean result1 = false || true && false;  // && first: false || (true && false) = false || false = false
        System.out.println("false || true && false = " + result1);

        boolean result2 = true || false && false;  // && first: true || (true && false) = true || false = true
        System.out.println("true || false && false = " + result2);

        // Short-circuit evaluation
        int x = 5;
        boolean result3 = false && (++x > 0);  // && short-circuits, ++x never executes
        System.out.println("false && (++x > 0): " + result3 + ", x = " + x + " (not incremented)");
        System.out.println();
    }

    /**
     * Example 7: Assignment has low precedence
     */
    private static void example07_AssignmentPrecedence() {
        System.out.println("--- Example 7: Assignment Precedence ---");

        int x = 2 + 3;  // + first: x = (2 + 3) = x = 5
        System.out.println("x = 2 + 3: " + x);

        int y = 10;
        y += 5 * 2;     // * first: y += (5 * 2) = y = y + 10 = 20
        System.out.println("y += 5 * 2 where y=10: " + y);

        // Right-to-left associativity for assignment
        int a, b, c;
        a = b = c = 100;  // Right-to-left: a = (b = (c = 100))
        System.out.println("a = b = c = 100: a=" + a + ", b=" + b + ", c=" + c);
        System.out.println();
    }

    /**
     * Example 8: Complex expressions
     */
    private static void example08_ComplexExpressions() {
        System.out.println("--- Example 8: Complex Expressions ---");

        int result1 = 2 + 3 * 4 - 5;
        // Step 1: 3 * 4 = 12
        // Step 2: 2 + 12 = 14 (left-to-right)
        // Step 3: 14 - 5 = 9
        System.out.println("2 + 3 * 4 - 5 = " + result1);

        int x = 5;
        int result2 = ++x * 2 + x--;
        // Step 1: ++x = 6 (x is now 6)
        // Step 2: 6 * 2 = 12
        // Step 3: 12 + 6 = 18 (post-decrement uses 6)
        // Step 4: x becomes 5
        System.out.println("++x * 2 + x-- where x=5: " + result2 + ", x is now: " + x);

        boolean b = 5 > 3 && 10 < 20 || false;
        // Step 1: 5 > 3 = true
        // Step 2: 10 < 20 = true
        // Step 3: true && true = true
        // Step 4: true || false = true
        System.out.println("5 > 3 && 10 < 20 || false = " + b);

        int ternary = 10 > 5 ? 2 * 3 : 4 + 5;
        // Step 1: 10 > 5 = true
        // Step 2: Since true, evaluate 2 * 3 = 6
        System.out.println("10 > 5 ? 2 * 3 : 4 + 5 = " + ternary);
        System.out.println();
    }

    /**
     * PRACTICE QUESTIONS - Try to solve these mentally!
     */
    private static void practiceQuestions() {
        System.out.println("=== PRACTICE QUESTIONS ===\n");

        System.out.println("Try to evaluate these expressions mentally, then check the answer!\n");

        // Question 1
        System.out.println("Question 1: What is the value?");
        System.out.println("  int x = 5;");
        System.out.println("  int result = x++ + ++x;");
        int x1 = 5;
        int result1 = x1++ + ++x1;
        System.out.println("  Answer: " + result1 + " (5 + 7 = 12, x becomes 7)");
        System.out.println("  Explanation: x++ uses 5, then x becomes 6, ++x makes it 7 and uses 7\n");

        // Question 2
        System.out.println("Question 2: What is the value?");
        System.out.println("  3 + 4 * 5 - 2");
        int result2 = 3 + 4 * 5 - 2;
        System.out.println("  Answer: " + result2);
        System.out.println("  Explanation: 4*5=20, then 3+20=23, then 23-2=21\n");

        // Question 3
        System.out.println("Question 3: What is the value?");
        System.out.println("  10 - 3 - 2");
        int result3 = 10 - 3 - 2;
        System.out.println("  Answer: " + result3);
        System.out.println("  Explanation: Left-to-right: (10-3)-2 = 7-2 = 5\n");

        // Question 4
        System.out.println("Question 4: What is the value?");
        System.out.println("  true || false && false");
        boolean result4 = true || false && false;
        System.out.println("  Answer: " + result4);
        System.out.println("  Explanation: && before ||: true || (false && false) = true || false = true\n");

        // Question 5
        System.out.println("Question 5: What is the value?");
        System.out.println("  int y = 10;");
        System.out.println("  y += y++ + ++y;");
        int y5 = 10;
        y5 += y5++ + ++y5;
        System.out.println("  Answer: " + y5);
        System.out.println("  Explanation: y5++ uses 10 (y5 becomes 11), ++y5 makes it 12 and uses 12");
        System.out.println("               y5 += (10 + 12) → y5 = 12 + 22 = 34\n");

        // Question 6
        System.out.println("Question 6: What is the value?");
        System.out.println("  5 > 3 == true && 10 < 20");
        boolean result6 = 5 > 3 == true && 10 < 20;
        System.out.println("  Answer: " + result6);
        System.out.println("  Explanation: 5>3=true, true==true=true, 10<20=true, true&&true=true\n");

        // Question 7
        System.out.println("Question 7: What is the value?");
        System.out.println("  16 / 4 / 2");
        int result7 = 16 / 4 / 2;
        System.out.println("  Answer: " + result7);
        System.out.println("  Explanation: Left-to-right: (16/4)/2 = 4/2 = 2\n");

        // Question 8
        System.out.println("Question 8: What is the value?");
        System.out.println("  !false && true || false");
        boolean result8 = !false && true || false;
        System.out.println("  Answer: " + result8);
        System.out.println("  Explanation: !false=true, true&&true=true, true||false=true\n");

        // Question 9
        System.out.println("Question 9: What is the value of x?");
        System.out.println("  int x = 5;");
        System.out.println("  x = x++ + x;");
        int x9 = 5;
        x9 = x9++ + x9;
        System.out.println("  Answer: " + x9);
        System.out.println("  Explanation: x9++ uses 5, then x9 becomes 6, so 5+6=11\n");

        // Question 10
        System.out.println("Question 10: What is the value?");
        System.out.println("  2 * 3 > 5 ? 10 : 20");
        int result10 = 2 * 3 > 5 ? 10 : 20;
        System.out.println("  Answer: " + result10);
        System.out.println("  Explanation: 2*3=6, 6>5=true, so return 10\n");
    }

    /**
     * QUICK REFERENCE SUMMARY
     */
    public static void printQuickReference() {
        System.out.println("""

                ╔════════════════════════════════════════════════════════════════╗
                ║            OPERATOR PRECEDENCE QUICK REFERENCE                 ║
                ╠════════════════════════════════════════════════════════════════╣
                ║  1. Post-unary        x++  x--                  (highest)      ║
                ║  2. Pre-unary         ++x  --x                                 ║
                ║  3. Other unary       +  -  ! ~                                  ║
                ║  4. Cast              (type)                                   ║
                ║  5. Multiplicative    *  /  %                                  ║
                ║  6. Additive          +  -                                     ║
                ║  7. Shift             <<  >>  >>>                              ║
                ║  8. Relational        <  >  <=  >=  instanceof                 ║
                ║  9. Equality          ==  !=                                   ║
                ║ 10. Logical AND       &                                        ║
                ║ 11. Logical XOR       ^                                        ║
                ║ 12. Logical OR        |                                        ║
                ║ 13. Conditional AND   &&                                       ║
                ║ 14. Conditional OR    ||                                       ║
                ║ 15. Ternary           ? :                                      ║
                ║ 16. Assignment        =  +=  -=  *=  etc.       (lowest)       ║
                ╚════════════════════════════════════════════════════════════════╝

                REMEMBER: When in doubt, use parentheses to make your intent clear!
                """);
    }
}
