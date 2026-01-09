package ch04coreapis;

/**
 * STRINGBUILDER - Complete Guide for OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * KEY DIFFERENCE: String vs StringBuilder
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * String:
 *   - IMMUTABLE - cannot change once created
 *   - Every modification creates a NEW String object
 *   - Example: s = s + "x" creates new String, old one garbage collected
 *
 * StringBuilder:
 *   - MUTABLE - changes its own state
 *   - Returns reference to ITSELF for method chaining
 *   - More efficient for multiple modifications
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CRITICAL CONCEPT: Returns Reference to Itself
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Most StringBuilder methods return a reference to the SAME StringBuilder object.
 * This enables METHOD CHAINING:
 *
 *   StringBuilder sb = new StringBuilder("Hello");
 *   sb.append(" World").append("!").reverse();
 *   // sb now contains "!dlroW olleH"
 *
 * EXAM TRAP: Some methods DON'T return StringBuilder:
 *   - substring() returns a NEW String (does NOT modify StringBuilder)
 *   - charAt() returns a char
 *   - length() returns an int
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * METHOD REFERENCE GUIDE
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * METHODS THAT RETURN StringBuilder (can chain):
 *   - append(...)      - adds to end
 *   - insert(...)      - adds at position
 *   - delete(...)      - removes range
 *   - deleteCharAt()   - removes one char
 *   - replace(...)     - replaces range
 *   - reverse()        - reverses entire sequence
 *
 * METHODS THAT DON'T RETURN StringBuilder (cannot chain):
 *   - substring()      - returns String (does NOT modify)
 *   - charAt()         - returns char
 *   - length()         - returns int
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * APPEND METHOD - OVERLOADED FOR ALL TYPES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * append() has versions for ALL primitive types and objects:
 *   - append(String)
 *   - append(int)
 *   - append(long)
 *   - append(float)
 *   - append(double)
 *   - append(boolean)
 *   - append(char)
 *   - append(char[])
 *   - append(Object)     // calls toString()
 *
 * ALWAYS returns reference to the same StringBuilder!
 */
public class StringBuilders {

    public static void main(String[] args) {
        System.out.println("=== STRINGBUILDER PRACTICE ===\n");

        example01_MutabilityAndChaining();
        example02_LengthAndCharAt();
        example03_SubstringTrap();
        example04_AppendOverloaded();
        example05_InsertMethod();
        example06_DeleteMethods();
        example07_ReplaceMethod();
        example08_ReverseMethod();
        example09_MethodChaining();
        example10_CommonExamTraps();
    }

    private static void example01_MutabilityAndChaining() {
        System.out.println("--- Example 1: Mutability and Chaining ---");

        // StringBuilder CHANGES ITS OWN STATE
        StringBuilder sb = new StringBuilder("Hello");
        System.out.println("Original: " + sb);  // Hello
        System.out.println("Identity: " + System.identityHashCode(sb));

        sb.append(" World");
        System.out.println("After append: " + sb);  // Hello World
        System.out.println("Identity: " + System.identityHashCode(sb));
        System.out.println("SAME OBJECT - state changed!\n");

        // Compare to String - creates NEW objects
        String s = "Hello";
        System.out.println("String identity: " + System.identityHashCode(s));
        s = s + " World";
        System.out.println("After concat identity: " + System.identityHashCode(s));
        System.out.println("DIFFERENT OBJECT - immutable!\n");
    }

    private static void example02_LengthAndCharAt() {
        System.out.println("--- Example 2: length() and charAt() ---");

        StringBuilder sb = new StringBuilder("Java");
        System.out.println("StringBuilder: " + sb);
        System.out.println("Length: " + sb.length());  // 4
        System.out.println("charAt(0): " + sb.charAt(0));  // J
        System.out.println("charAt(3): " + sb.charAt(3));  // a

        // EXAM TRAP: length() returns int, NOT StringBuilder
        // sb = sb.length();  // DOES NOT COMPILE - can't assign int to StringBuilder

        System.out.println();
    }

    private static void example03_SubstringTrap() {
        System.out.println("--- Example 3: substring() - CRITICAL EXAM TRAP ---");

        StringBuilder sb = new StringBuilder("Programming");
        System.out.println("Original: " + sb);

        // substring() returns a NEW String
        String result = sb.substring(0, 7);
        System.out.println("substring(0, 7): " + result);  // Program

        // CRITICAL: StringBuilder is NOT modified!
        System.out.println("StringBuilder after substring: " + sb);  // Programming

        // EXAM TRAP: substring() returns String, not StringBuilder
        // StringBuilder sb2 = sb.substring(0, 5);  // DOES NOT COMPILE!

        // EXAM TRAP: Can't chain after substring
        // sb.substring(0, 5).append("x");  // DOES NOT COMPILE - String has no append()

        System.out.println();
    }

    private static void example04_AppendOverloaded() {
        System.out.println("--- Example 4: append() - Overloaded for All Types ---");

        StringBuilder sb = new StringBuilder();

        // append() accepts ALL primitive types
        sb.append("String: ").append("Hello");  // String
        sb.append("\n");
        sb.append("int: ").append(42);          // int
        sb.append("\n");
        sb.append("long: ").append(1000L);      // long
        sb.append("\n");
        sb.append("double: ").append(3.14);     // double
        sb.append("\n");
        sb.append("boolean: ").append(true);    // boolean
        sb.append("\n");
        sb.append("char: ").append('X');        // char

        System.out.println(sb);

        // append(Object) calls toString()
        StringBuilder sb2 = new StringBuilder();
        sb2.append(new Integer(100));  // calls Integer.toString()
        System.out.println("Object append: " + sb2 + "\n");
    }

    private static void example05_InsertMethod() {
        System.out.println("--- Example 5: insert() Method ---");

        StringBuilder sb = new StringBuilder("Java");
        System.out.println("Original: " + sb);

        // insert(index, value) - adds at specified position
        sb.insert(4, " Programming");
        System.out.println("After insert(4, \" Programming\"): " + sb);

        // Insert at beginning
        sb.insert(0, "Learning ");
        System.out.println("After insert(0, \"Learning \"): " + sb);

        // insert() is also overloaded for all types
        StringBuilder sb2 = new StringBuilder("Score: ");
        sb2.insert(7, 100);  // insert int
        System.out.println("Insert int: " + sb2);

        // RETURNS StringBuilder - can chain
        StringBuilder sb3 = new StringBuilder("ab");
        sb3.insert(1, "X").insert(2, "Y");
        System.out.println("Chained inserts: " + sb3 + "\n");  // aXYb
    }

    private static void example06_DeleteMethods() {
        System.out.println("--- Example 6: delete() and deleteCharAt() ---");

        // delete(start, end) - removes characters from start (inclusive) to end (exclusive)
        StringBuilder sb = new StringBuilder("Hello World");
        System.out.println("Original: " + sb);

        sb.delete(5, 11);  // removes " World"
        System.out.println("After delete(5, 11): " + sb);  // Hello

        // deleteCharAt(index) - removes single character
        StringBuilder sb2 = new StringBuilder("Java!");
        System.out.println("\nOriginal: " + sb2);

        sb2.deleteCharAt(4);  // removes '!'
        System.out.println("After deleteCharAt(4): " + sb2);  // Java

        // RETURNS StringBuilder - can chain
        StringBuilder sb3 = new StringBuilder("abcdef");
        sb3.delete(1, 3).deleteCharAt(1);  // delete "bc", then delete 'd'
        System.out.println("Chained deletes: " + sb3 + "\n");  // aef
    }

    private static void example07_ReplaceMethod() {
        System.out.println("--- Example 7: replace() Method ---");

        // replace(start, end, newString) - replaces characters in range
        StringBuilder sb = new StringBuilder("Java is hard");
        System.out.println("Original: " + sb);

        sb.replace(8, 12, "fun");  // replace "hard" with "fun"
        System.out.println("After replace(8, 12, \"fun\"): " + sb);

        // Replacement can be different length
        StringBuilder sb2 = new StringBuilder("I like X");
        sb2.replace(7, 8, "Java Programming");
        System.out.println("Replace with longer: " + sb2);

        // RETURNS StringBuilder - can chain
        StringBuilder sb3 = new StringBuilder("aaabbbccc");
        sb3.replace(0, 3, "X").replace(1, 4, "Y");
        System.out.println("Chained replaces: " + sb3 + "\n");
    }

    private static void example08_ReverseMethod() {
        System.out.println("--- Example 8: reverse() Method ---");

        StringBuilder sb = new StringBuilder("Java");
        System.out.println("Original: " + sb);

        sb.reverse();
        System.out.println("After reverse(): " + sb);  // avaJ

        // RETURNS StringBuilder - can chain
        StringBuilder sb2 = new StringBuilder("Hello");
        sb2.append(" World").reverse();
        System.out.println("Append then reverse: " + sb2 + "\n");  // dlroW olleH
    }

    private static void example09_MethodChaining() {
        System.out.println("--- Example 9: Method Chaining ---");

        // Most methods return StringBuilder reference - can chain
        StringBuilder sb = new StringBuilder("Start");
        sb.append(" Middle")
          .append(" End")
          .insert(0, ">> ")
          .append(" <<")
          .reverse();

        System.out.println("After chaining: " + sb);

        // EXAM TRAP: substring() breaks the chain
        StringBuilder sb2 = new StringBuilder("Hello World");
        // sb2.substring(0, 5).append("!");  // DOES NOT COMPILE!
        // substring() returns String, which has no append() method

        // If you need to continue, create new StringBuilder
        String temp = sb2.substring(0, 5);
        StringBuilder sb3 = new StringBuilder(temp).append("!");
        System.out.println("After substring workaround: " + sb3 + "\n");
    }

    private static void example10_CommonExamTraps() {
        System.out.println("--- Example 10: Common Exam Traps ---\n");

        System.out.println("TRAP 1: substring() does NOT modify StringBuilder");
        StringBuilder sb1 = new StringBuilder("Java");
        sb1.substring(0, 2);
        System.out.println("  After substring: " + sb1);  // Still "Java"!

        System.out.println("\nTRAP 2: substring() returns String, not StringBuilder");
        StringBuilder sb2 = new StringBuilder("Hello");
        // StringBuilder sb3 = sb2.substring(0, 3);  // DOES NOT COMPILE!
        String s = sb2.substring(0, 3);  // OK - returns String
        System.out.println("  substring result: " + s);

        System.out.println("\nTRAP 3: Can't chain after methods that don't return StringBuilder");
        StringBuilder sb4 = new StringBuilder("Test");
        // sb4.charAt(0).append("x");     // DOES NOT COMPILE - charAt returns char
        // sb4.length().append("x");      // DOES NOT COMPILE - length returns int
        // sb4.substring(0, 2).append("x"); // DOES NOT COMPILE - substring returns String
        System.out.println("  These methods break the chain!");

        System.out.println("\nTRAP 4: delete() vs deleteCharAt() parameters");
        StringBuilder sb5 = new StringBuilder("abcdef");
        sb5.delete(1, 3);        // delete range [1, 3) -> "adef"
        sb5.deleteCharAt(1);     // delete char at index 1 -> "aef"
        System.out.println("  After delete operations: " + sb5);

        System.out.println("\nTRAP 5: insert() shifts characters, doesn't replace");
        StringBuilder sb6 = new StringBuilder("ac");
        sb6.insert(1, "b");  // Inserts, doesn't replace -> "abc"
        System.out.println("  After insert: " + sb6);

        System.out.println("\nTRAP 6: All mutation methods return reference to same object");
        StringBuilder sb7 = new StringBuilder("X");
        StringBuilder sb8 = sb7.append("Y");
        System.out.println("  sb7 == sb8: " + (sb7 == sb8));  // true - same object!
        System.out.println("  sb7: " + sb7 + ", sb8: " + sb8);  // Both "XY"
    }
}
