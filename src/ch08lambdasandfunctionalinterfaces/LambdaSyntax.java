package ch08lambdasandfunctionalinterfaces;

/**
 * Lambda Syntax and Functional Interface Rules
 *
 * KEY RULES TO MEMORIZE:
 * 1. Functional interface = exactly ONE abstract method (SAM - Single Abstract Method)
 * 2. Can have unlimited default methods
 * 3. Can have unlimited static methods
 * 4. Can override Object methods (equals, hashCode, toString) - DON'T count as abstract
 * 5. @FunctionalInterface annotation is optional but recommended
 *
 * LAMBDA SYNTAX VARIATIONS:
 * - Full: (Type p1, Type p2) -> { statements; return value; }
 * - Inferred types: (p1, p2) -> { statements; return value; }
 * - Single expression: (p1, p2) -> expression
 * - Single param (no parens): p -> expression
 * - Single param (with parens): (p) -> expression
 * - Single param with type: (Type p) -> expression
 * - No params: () -> expression
 * - var (Java 11+): (var p1, var p2) -> expression
 *
 * VAR IN LAMBDA PARAMETERS (Java 11+):
 * ✓ Can use var: (var a, var b) -> a + b
 * ✓ Allows annotations: (@NonNull var a, var b) -> a + b
 * ✓ Must use parens: (var a) -> a * 2  (even for single param)
 * ✗ Cannot mix with types: (var a, int b) -> a + b  // ERROR
 * ✗ Cannot mix with inferred: (var a, b) -> a + b  // ERROR
 * ✗ All-or-nothing rule: If one param uses var, ALL must use var
 *
 * LAMBDA PARAMETER NAMING (IMPORTANT EXAM TRAP!):
 * ✗ CANNOT reuse local variable names: int x = 5; ... (x) -> ...  // ERROR
 * ✗ CANNOT reuse method parameter names: void m(int x) { ... (x) -> ... }  // ERROR
 * ✓ CAN shadow instance variables: this.x exists, (x) -> ... is OK
 * ✓ CAN shadow static variables: static int x exists, (x) -> ... is OK
 *
 * KEY DIFFERENCE: Anonymous classes CAN shadow local vars, lambdas CANNOT!
 *
 * EXAM TRAPS:
 * - CANNOT mix typed and untyped: (int a, b) -> a + b  // ERROR
 * - CANNOT mix var with types: (var a, int b) -> a + b  // ERROR
 * - CANNOT mix var with inferred: (var a, b) -> a + b  // ERROR
 * - var requires parens: var a -> a + 1  // ERROR (even single param)
 * - Return requires braces: (a, b) -> return a + b;  // ERROR
 * - Multiple params need parens: a, b -> a + b  // ERROR
 *
 * VARIABLE ACCESS RULES IN LAMBDA BODY:
 * ✓ Instance variables - ALLOWED (can read and modify)
 * ✓ Static variables - ALLOWED (can read and modify)
 * ✓ Effectively final local variables - ALLOWED (can read only)
 * ✓ Effectively final method parameters - ALLOWED (can read only)
 * ✓ Lambda parameters - ALLOWED (can read and modify)
 * ✗ Non-effectively-final local/method variables - NOT ALLOWED
 *
 * Effectively final = variable is never reassigned after initialization
 */
public class LambdaSyntax {

    // ===== VALID FUNCTIONAL INTERFACES =====

    @FunctionalInterface
    interface Calculator {
        int calculate(int a, int b);  // One abstract method
    }

    @FunctionalInterface
    interface Processor {
        void process(String data);  // One abstract method

        private void helper() {} // private methods allowed and can be used by interface methods only
        default void preProcess(String data) { helper(); }  // Default methods allowed
        default void postProcess(String data) { }

        static void info() { }  // Static methods allowed
    }

    // Overriding Object methods doesn't violate single abstract method rule
    @FunctionalInterface
    interface CustomComparator<T> {
        int compare(T o1, T o2);  // The ONE abstract method

        // These DON'T count - they override Object methods
        boolean equals(Object obj);
        int hashCode();
        String toString();

        default CustomComparator<T> reversed() {  // Default method - still valid
            return (o1, o2) -> compare(o2, o1);
        }
    }

    // ===== INVALID FUNCTIONAL INTERFACES =====

    // @FunctionalInterface  // ERROR - no abstract methods
    // interface NoAbstractMethod {
    //     default void method() { }
    // }

    // @FunctionalInterface  // ERROR - two abstract methods
    // interface TwoAbstractMethods {
    //     void method1();
    //     void method2();
    // }

    // ===== VARIABLE ACCESS IN LAMBDAS =====

    private static int staticVar = 100;        // Static variable
    private int instanceVar = 200;             // Instance variable

    public void demonstrateVariableAccess() {
        final int finalLocal = 10;              // Final local variable
        int effectivelyFinal = 20;              // Effectively final (never reassigned)
        int notEffectivelyFinal = 30;           // NOT effectively final

        notEffectivelyFinal = 40;  // Reassigned - breaks effectively final

        @FunctionalInterface
        interface Action {
            void execute();
        }

        // VALID: Access instance variable (can read and modify)
        Action a1 = () -> {
            System.out.println(instanceVar);  // Read
            instanceVar = 300;                // Modify - OK
        };

        // VALID: Access static variable (can read and modify)
        Action a2 = () -> {
            System.out.println(staticVar);    // Read
            staticVar = 150;                  // Modify - OK
        };

        // VALID: Access final local variable (read only)
        Action a3 = () -> {
            System.out.println(finalLocal);   // Read - OK
            // finalLocal = 15;               // Modify - ERROR (final)
        };

        // VALID: Access effectively final local variable (read only)
        Action a4 = () -> {
            System.out.println(effectivelyFinal);  // Read - OK
            // effectivelyFinal = 25;              // Modify - ERROR (would break effectively final)
        };

        // INVALID: Cannot access non-effectively-final variable
        // Action a5 = () -> {
        //     System.out.println(notEffectivelyFinal);  // ERROR - not effectively final
        // };

        // After lambda uses it, cannot reassign effectively final variable
        // effectivelyFinal = 25;  // ERROR - would break lambda above

        @FunctionalInterface
        interface Calculator {
            int calc(int x);
        }

        // VALID: Lambda parameters can be read and modified
        Calculator c1 = (x) -> {
            x = x * 2;  // Modify lambda parameter - OK
            return x;
        };

        // Method parameter access
        demonstrateMethodParamAccess(50);
    }

    public void demonstrateMethodParamAccess(int methodParam) {
        // methodParam is effectively final (not reassigned)

        @FunctionalInterface
        interface Action {
            void execute();
        }

        // VALID: Access effectively final method parameter
        Action a = () -> System.out.println(methodParam);

        // INVALID: Reassigning would break effectively final
        // methodParam = 100;  // ERROR - lambda uses it

        a.execute();
    }

    // ===== LAMBDA SYNTAX EXAMPLES =====

    public static void main(String[] args) {
        // Demonstrate variable access rules
        LambdaSyntax demo = new LambdaSyntax();
        demo.demonstrateVariableAccess();

        System.out.println("\n=== LAMBDA SYNTAX EXAMPLES ===");

        // 1. Full syntax with types and braces
        Calculator c1 = (int a, int b) -> { return a + b; };

        // 2. Omit types (type inference)
        Calculator c2 = (a, b) -> { return a + b; };

        // 3. Single expression - no braces or return keyword
        Calculator c3 = (a, b) -> a + b;

        // 4. Multiple statements - braces and return required
        Calculator c4 = (a, b) -> {
            int sum = a + b;
            return sum;
        };

        // 5. Single parameter - can omit parentheses
        @FunctionalInterface
        interface Transformer {
            int transform(int x);
        }
        Transformer t1 = x -> x * 2;           // No parens
        Transformer t2 = (x) -> x * 2;         // With parens - also valid
        Transformer t3 = (int x) -> x * 2;     // With type - parens required

        // 6. No parameters - empty parentheses required
        @FunctionalInterface
        interface Supplier {
            String get();
        }
        Supplier s1 = () -> "Hello";
        Supplier s2 = () -> { return "Hello"; };

        // 7. void return type - no return statement
        Processor p1 = data -> System.out.println(data);
        Processor p2 = data -> { System.out.println(data); };  // Braces optional

        // Test examples
        System.out.println(c3.calculate(5, 3));
        System.out.println(t1.transform(10));
        System.out.println(s1.get());

        // ===== EXAM TRAPS =====

        // INVALID - cannot mix typed and untyped parameters
        // Calculator invalid1 = (int a, b) -> a + b;  // ERROR

        // INVALID - return keyword requires braces
        // Calculator invalid2 = (a, b) -> return a + b;  // ERROR

        // INVALID - multiple parameters require parentheses
        // Calculator invalid3 = a, b -> a + b;  // ERROR

        // INVALID - single parameter with type requires parentheses
        // Transformer invalid4 = int x -> x * 2;  // ERROR

        // VALID - all parameters typed or all inferred
        Calculator valid1 = (int a, int b) -> a + b;  // All typed
        Calculator valid2 = (a, b) -> a + b;          // All inferred

        // ===== VAR IN LAMBDA PARAMETERS (Java 11+) =====

        System.out.println("\n=== VAR IN LAMBDA PARAMETERS ===");

        // VALID - using var for all parameters
        Calculator varCalc = (var a, var b) -> a + b;
        System.out.println("(var a, var b) -> a + b: " + varCalc.calculate(10, 5));

        // VALID - var with single parameter (parens required!)
        Transformer varTransform = (var x) -> x * 2;
        System.out.println("(var x) -> x * 2: " + varTransform.transform(7));

        // Main benefit: var allows annotations
        // Calculator annotated = (@NonNull var a, @NonNull var b) -> a + b;

        // INVALID EXAMPLES (commented out):
        // Transformer noParens = var x -> x * 2;        // ERROR - parens required with var
        // Calculator mixed1 = (var a, int b) -> a + b;  // ERROR - cannot mix var with types
        // Calculator mixed2 = (var a, b) -> a + b;      // ERROR - cannot mix var with inferred

        // ===== LAMBDA PARAMETER NAMING (SHADOWING) =====

        System.out.println("\n=== LAMBDA PARAMETER NAMING ===");

        // INVALID - cannot reuse local variable names
        int localVar = 100;
        // Transformer error1 = (localVar) -> localVar * 2;  // ERROR - cannot shadow local var

        // VALID - CAN shadow instance variables
        Transformer shadowInstance = (instanceVar) -> instanceVar * 2;
        System.out.println("Shadowing instance var: " + shadowInstance.transform(5));

        // VALID - CAN shadow static variables
        Transformer shadowStatic = (staticVar) -> staticVar * 2;
        System.out.println("Shadowing static var: " + shadowStatic.transform(5));

        // Key difference: Anonymous classes CAN shadow local variables, lambdas CANNOT
        System.out.println("\nNote: Anonymous classes CAN shadow local vars, lambdas CANNOT");
        Transformer anonymousClass = new Transformer() {
            @Override
            public int transform(int localVar) {  // OK in anonymous class!
                return localVar * 2;
            }
        };
        System.out.println("Anonymous class shadowing local: " + anonymousClass.transform(7));
    }
}
