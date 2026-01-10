package ch06inheritance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * IMMUTABLE OBJECTS - Complete Guide for OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * WHAT ARE IMMUTABLE OBJECTS?
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * An immutable object is an object whose state CANNOT be changed after it is
 * constructed.
 *
 * Examples of immutable classes in Java:
 * - String
 * - All wrapper classes (Integer, Long, Double, etc.)
 * - BigInteger, BigDecimal
 * - LocalDate, LocalTime, LocalDateTime
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * WHY USE IMMUTABLE OBJECTS?
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Benefits:
 * 1. Thread-safe (no synchronization needed)
 * 2. Safe to share and reuse
 * 3. Can be cached
 * 4. Simpler to understand and debug
 * 5. Safe to use as keys in HashMap or elements in HashSet
 * 6. Prevent accidental modification
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * FIVE RULES FOR CREATING IMMUTABLE OBJECTS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * 1. PREVENT EXTENSION:
 *    Mark the class as final OR make all constructors private
 *
 *    Why? Prevents subclasses from adding mutable behavior.
 *
 *    PRIVATE CONSTRUCTOR EXPLAINED:
 *    When all constructors are private, you use STATIC FACTORY METHODS
 *    to create instances.
 *
 *    Examples:
 *    - Optional.of(value)         // private constructor, static factory
 *    - List.of(1, 2, 3)           // private constructor, static factory
 *    - Collections.emptyList()    // private constructor, static factory
 *
 *    Pattern:
 *    class Immutable {
 *        private Immutable() { }  // Private constructor
 *
 *        public static Immutable of(String value) {  // Static factory
 *            return new Immutable();  // Can call private constructor from within class
 *        }
 *    }
 *
 * 2. MARK ALL INSTANCE VARIABLES PRIVATE AND FINAL:
 *    - private: Cannot be accessed directly from outside
 *    - final: Cannot be reassigned after initialization
 *
 *    Why? Prevents direct modification and reassignment.
 *
 * 3. DON'T DEFINE ANY SETTER METHODS:
 *    No setXxx() methods!
 *
 *    Why? Setters allow modification of state.
 *
 * 4. DON'T ALLOW REFERENCED MUTABLE OBJECTS TO BE MODIFIED:
 *    Use defensive copying for mutable objects.
 *
 *    If a field references a mutable object (like ArrayList):
 *    - Make a copy when accepting it in constructor
 *    - Make a copy when returning it from getter
 *
 *    Why? Even though the reference is final, the object it points to
 *    could be modified externally.
 *
 * 5. USE CONSTRUCTOR TO SET ALL PROPERTIES:
 *    Initialize all fields in constructor, making copies if needed.
 *
 *    Why? Ensures object is fully initialized at construction time.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * DEFENSIVE COPYING
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Critical for mutable fields!
 *
 * WRONG (not immutable):
 * class Bad {
 *     private final List<String> list;
 *     public Bad(List<String> list) {
 *         this.list = list;  // Stores reference - caller can modify!
 *     }
 *     public List<String> getList() {
 *         return list;  // Returns reference - caller can modify!
 *     }
 * }
 *
 * RIGHT (immutable):
 * class Good {
 *     private final List<String> list;
 *     public Good(List<String> list) {
 *         this.list = new ArrayList<>(list);  // Defensive copy!
 *     }
 *     public List<String> getList() {
 *         return new ArrayList<>(list);  // Defensive copy!
 *     }
 * }
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * EXAM TRAPS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * 1. final reference doesn't make the object immutable
 * 2. Forgetting defensive copies for mutable fields
 * 3. Having a setter method (even if it returns new instance)
 * 4. Not making class final (subclass could add mutable behavior)
 * 5. Making fields public (even if final)
 * 6. Returning mutable object without copying
 */
public class ImmutableObjects {

    public static void main(String[] args) {
        System.out.println("=== IMMUTABLE OBJECTS ===\n");

        whatIsImmutable();
        whyImmutable();
        rule1PreventExtension();
        rule1PrivateConstructor();
        rule2PrivateFinalFields();
        rule3NoSetters();
        rule4DefensiveCopying();
        rule5ConstructorInitialization();
        completeImmutableExample();
        commonMistakes();
        examTraps();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * WHAT IS IMMUTABLE?
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void whatIsImmutable() {
        System.out.println("=== WHAT IS IMMUTABLE? ===\n");

        System.out.println("Immutable = Object whose state CANNOT change after construction\n");

        System.out.println("--- Example: String is Immutable ---");
        String s = "Hello";
        System.out.println("Original: " + s);
        s.toUpperCase();  // Returns NEW string
        System.out.println("After toUpperCase(): " + s);  // Still "Hello"!
        s = s.toUpperCase();  // Must reassign to change reference
        System.out.println("After reassignment: " + s);

        System.out.println("\n--- Common Immutable Classes in Java ---");
        System.out.println("  • String");
        System.out.println("  • Integer, Long, Double (all wrapper classes)");
        System.out.println("  • BigInteger, BigDecimal");
        System.out.println("  • LocalDate, LocalTime, LocalDateTime");
        System.out.println("  • Many classes from Collections (via List.of(), etc.)");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * WHY IMMUTABLE?
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void whyImmutable() {
        System.out.println("=== WHY USE IMMUTABLE OBJECTS? ===\n");

        System.out.println("Benefits:");
        System.out.println("1. Thread-safe (no synchronization needed)");
        System.out.println("2. Safe to share and reuse");
        System.out.println("3. Can be cached (String pool!)");
        System.out.println("4. Simpler to understand and debug");
        System.out.println("5. Safe to use as keys in HashMap");
        System.out.println("6. Safe to use as elements in HashSet");
        System.out.println("7. Prevent accidental modification\n");

        System.out.println("--- Example: String Caching ---");
        String s1 = "Hello";
        String s2 = "Hello";
        System.out.println("s1 == s2: " + (s1 == s2));  // true - same object!
        System.out.println("→ Because String is immutable, JVM can reuse same instance\n");

        System.out.println("--- Example: Safe as HashMap Key ---");
        System.out.println("// Immutable objects don't change hashCode");
        System.out.println("Map<String, Integer> map = new HashMap<>();");
        System.out.println("String key = \"test\";");
        System.out.println("map.put(key, 100);  // Safe - key won't change");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * RULE 1: PREVENT EXTENSION (final class)
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void rule1PreventExtension() {
        System.out.println("=== RULE 1: PREVENT EXTENSION ===");
        System.out.println("*** Mark class as final ***\n");

        System.out.println("Why? Prevent subclasses from adding mutable behavior.\n");

        System.out.println("--- Example ✓ ---");
        System.out.println("public final class ImmutablePerson {");
        System.out.println("    private final String name;");
        System.out.println("    public ImmutablePerson(String name) {");
        System.out.println("        this.name = name;");
        System.out.println("    }");
        System.out.println("}\n");

        System.out.println("--- Why This Matters ---");
        System.out.println("class MutablePerson extends ImmutablePerson {  // If not final");
        System.out.println("    private String nickname;");
        System.out.println("    public void setNickname(String n) { nickname = n; }");
        System.out.println("    // Subclass added mutable behavior!");
        System.out.println("}\n");

        System.out.println("--- Demonstration ---");
        FinalImmutable obj = new FinalImmutable("Test");
        System.out.println("Created: " + obj.getValue());
        // Cannot extend FinalImmutable - it's final!

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * RULE 1: PREVENT EXTENSION (private constructor)
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void rule1PrivateConstructor() {
        System.out.println("=== RULE 1: PREVENT EXTENSION (Alternative) ===");
        System.out.println("*** Make all constructors private + use static factory ***\n");

        System.out.println("QUESTION: How do you create object if constructor is private?\n");
        System.out.println("ANSWER: Use STATIC FACTORY METHODS!\n");

        System.out.println("Static methods CAN call private constructors");
        System.out.println("(they're in the same class).\n");

        System.out.println("--- Example ---");
        System.out.println("public class ImmutablePerson {");
        System.out.println("    private final String name;");
        System.out.println("    ");
        System.out.println("    private ImmutablePerson(String name) {  // Private!");
        System.out.println("        this.name = name;");
        System.out.println("    }");
        System.out.println("    ");
        System.out.println("    public static ImmutablePerson of(String name) {");
        System.out.println("        return new ImmutablePerson(name);  // ✓ Can call!");
        System.out.println("    }");
        System.out.println("}\n");

        System.out.println("Usage:");
        System.out.println("// ImmutablePerson p = new ImmutablePerson(\"Bob\");  // ✗ DOES NOT COMPILE");
        System.out.println("ImmutablePerson p = ImmutablePerson.of(\"Bob\");  // ✓ Works!\n");

        System.out.println("--- Real Java Examples ---");
        System.out.println("Optional.of(value)              // private constructor");
        System.out.println("List.of(1, 2, 3)                // private constructor");
        System.out.println("Collections.emptyList()         // private constructor");
        System.out.println("LocalDate.of(2024, 1, 1)        // private constructor\n");

        System.out.println("--- Demonstration ---");
        PrivateConstructorImmutable obj = PrivateConstructorImmutable.of("Factory Created");
        System.out.println("Created via factory: " + obj.getValue());
        // Cannot call: new PrivateConstructorImmutable("test") - private!
        // Cannot extend: constructor is private!

        System.out.println("\n--- Why Use Private Constructor Instead of final? ---");
        System.out.println("  • More control over instance creation");
        System.out.println("  • Can cache instances (like Integer.valueOf())");
        System.out.println("  • Can return subtype (more flexible)");
        System.out.println("  • Can enforce validation in factory method");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * RULE 2: PRIVATE AND FINAL FIELDS
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void rule2PrivateFinalFields() {
        System.out.println("=== RULE 2: PRIVATE AND FINAL FIELDS ===\n");

        System.out.println("All instance variables should be:");
        System.out.println("  • private - Cannot be accessed directly");
        System.out.println("  • final - Cannot be reassigned\n");

        System.out.println("--- Correct ✓ ---");
        System.out.println("public final class Person {");
        System.out.println("    private final String name;");
        System.out.println("    private final int age;");
        System.out.println("}\n");

        System.out.println("--- Wrong: public field ✗ ---");
        System.out.println("public final class Person {");
        System.out.println("    public final String name;  // ✗ public allows direct access");
        System.out.println("}\n");
        System.out.println("Problem: Person p = new Person(\"Bob\");");
        System.out.println("         p.name = \"Alice\";  // Won't compile (final)");
        System.out.println("         // But exposes internal representation\n");

        System.out.println("--- Wrong: not final ✗ ---");
        System.out.println("public final class Person {");
        System.out.println("    private String name;  // ✗ not final, can be reassigned");
        System.out.println("}\n");
        System.out.println("Problem: Even though private, methods in same class");
        System.out.println("         could reassign the field.\n");

        System.out.println("--- Demonstration ---");
        PrivateFinalFields obj = new PrivateFinalFields("Alice", 30);
        System.out.println("Name: " + obj.getName());
        System.out.println("Age: " + obj.getAge());
        // Cannot modify: fields are private and final

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * RULE 3: NO SETTERS
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void rule3NoSetters() {
        System.out.println("=== RULE 3: NO SETTERS ===\n");

        System.out.println("Rule: Do NOT provide setter methods (setXxx)\n");

        System.out.println("Why? Setters allow modification of state.\n");

        System.out.println("--- Wrong: Has Setter ✗ ---");
        System.out.println("public final class Person {");
        System.out.println("    private final String name;");
        System.out.println("    ");
        System.out.println("    public void setName(String name) {  // ✗ SETTER!");
        System.out.println("        this.name = name;  // Won't compile (final), but still bad design");
        System.out.println("    }");
        System.out.println("}\n");

        System.out.println("--- Correct: Getter Only ✓ ---");
        System.out.println("public final class Person {");
        System.out.println("    private final String name;");
        System.out.println("    ");
        System.out.println("    public Person(String name) {  // Set in constructor");
        System.out.println("        this.name = name;");
        System.out.println("    }");
        System.out.println("    ");
        System.out.println("    public String getName() {  // ✓ Getter only");
        System.out.println("        return name;");
        System.out.println("    }");
        System.out.println("}\n");

        System.out.println("--- What About \"Setters\" That Return New Instance? ---");
        System.out.println("This is OK! It's not really a setter.\n");

        System.out.println("Example from String:");
        System.out.println("String s1 = \"hello\";");
        System.out.println("String s2 = s1.toUpperCase();  // Returns NEW String");
        System.out.println("// s1 is unchanged, s2 is new instance\n");

        System.out.println("public final class Person {");
        System.out.println("    private final String name;");
        System.out.println("    ");
        System.out.println("    public Person withName(String newName) {  // ✓ OK!");
        System.out.println("        return new Person(newName);  // Returns NEW instance");
        System.out.println("    }");
        System.out.println("}");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * RULE 4: DEFENSIVE COPYING
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void rule4DefensiveCopying() {
        System.out.println("=== RULE 4: DEFENSIVE COPYING ===");
        System.out.println("*** CRITICAL EXAM TOPIC ***\n");

        System.out.println("Rule: Don't allow referenced mutable objects to be modified\n");

        System.out.println("IMPORTANT: final reference ≠ immutable object!\n");

        System.out.println("--- Wrong: No Defensive Copy ✗ ---");
        System.out.println("public final class Bad {");
        System.out.println("    private final List<String> list;");
        System.out.println("    ");
        System.out.println("    public Bad(List<String> list) {");
        System.out.println("        this.list = list;  // ✗ Stores reference!");
        System.out.println("    }");
        System.out.println("    ");
        System.out.println("    public List<String> getList() {");
        System.out.println("        return list;  // ✗ Returns reference!");
        System.out.println("    }");
        System.out.println("}\n");

        System.out.println("Problem:");
        System.out.println("List<String> original = new ArrayList<>();");
        System.out.println("original.add(\"A\");");
        System.out.println("Bad bad = new Bad(original);");
        System.out.println("original.add(\"B\");  // Modifies object's internal state!");
        System.out.println("// bad.getList() now contains [\"A\", \"B\"]\n");

        System.out.println("--- Correct: Defensive Copies ✓ ---");
        System.out.println("public final class Good {");
        System.out.println("    private final List<String> list;");
        System.out.println("    ");
        System.out.println("    public Good(List<String> list) {");
        System.out.println("        this.list = new ArrayList<>(list);  // ✓ Copy!");
        System.out.println("    }");
        System.out.println("    ");
        System.out.println("    public List<String> getList() {");
        System.out.println("        return new ArrayList<>(list);  // ✓ Copy!");
        System.out.println("    }");
        System.out.println("}\n");

        System.out.println("--- Demonstration: WRONG Way ---");
        List<String> original = new ArrayList<>();
        original.add("A");
        NoDefensiveCopy bad = new NoDefensiveCopy(original);
        System.out.println("Initial: " + bad.getList());

        original.add("B");  // Modify original
        System.out.println("After modifying original: " + bad.getList());  // Changed!

        bad.getList().add("C");  // Modify returned list
        System.out.println("After modifying returned list: " + bad.getList());  // Changed!
        System.out.println("→ Not immutable!\n");

        System.out.println("--- Demonstration: CORRECT Way ---");
        List<String> original2 = new ArrayList<>();
        original2.add("A");
        WithDefensiveCopy good = new WithDefensiveCopy(original2);
        System.out.println("Initial: " + good.getList());

        original2.add("B");  // Modify original
        System.out.println("After modifying original: " + good.getList());  // Unchanged!

        good.getList().add("C");  // Modify returned list
        System.out.println("After modifying returned list: " + good.getList());  // Unchanged!
        System.out.println("→ Truly immutable!\n");

        System.out.println("--- Alternative: Unmodifiable Collections ✓ ---");
        System.out.println("public List<String> getList() {");
        System.out.println("    return Collections.unmodifiableList(list);");
        System.out.println("}");
        System.out.println("// Returns view that throws exception on modification");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * RULE 5: CONSTRUCTOR INITIALIZATION
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void rule5ConstructorInitialization() {
        System.out.println("=== RULE 5: CONSTRUCTOR INITIALIZATION ===\n");

        System.out.println("Rule: Use constructor to set all properties,");
        System.out.println("      making copies if needed\n");

        System.out.println("Why? Ensures object is fully initialized at construction time.\n");

        System.out.println("--- Example ✓ ---");
        System.out.println("public final class Person {");
        System.out.println("    private final String name;");
        System.out.println("    private final int age;");
        System.out.println("    private final List<String> hobbies;");
        System.out.println("    ");
        System.out.println("    public Person(String name, int age, List<String> hobbies) {");
        System.out.println("        this.name = name;");
        System.out.println("        this.age = age;");
        System.out.println("        this.hobbies = new ArrayList<>(hobbies);  // Copy!");
        System.out.println("    }");
        System.out.println("}\n");

        System.out.println("--- Key Points ---");
        System.out.println("• All fields initialized in constructor");
        System.out.println("• Mutable objects are copied (defensive copy)");
        System.out.println("• After construction, object is complete and immutable");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * COMPLETE IMMUTABLE EXAMPLE
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void completeImmutableExample() {
        System.out.println("=== COMPLETE IMMUTABLE EXAMPLE ===");
        System.out.println("*** Following ALL 5 Rules ***\n");

        System.out.println("public final class ImmutablePerson {  // 1. final class");
        System.out.println("    private final String name;         // 2. private final");
        System.out.println("    private final int age;             // 2. private final");
        System.out.println("    private final List<String> hobbies; // 2. private final");
        System.out.println("    ");
        System.out.println("    public ImmutablePerson(String name, int age, List<String> hobbies) {");
        System.out.println("        this.name = name;");
        System.out.println("        this.age = age;");
        System.out.println("        this.hobbies = new ArrayList<>(hobbies);  // 4. defensive copy");
        System.out.println("    }");
        System.out.println("    ");
        System.out.println("    public String getName() { return name; }");
        System.out.println("    public int getAge() { return age; }");
        System.out.println("    ");
        System.out.println("    public List<String> getHobbies() {");
        System.out.println("        return new ArrayList<>(hobbies);  // 4. defensive copy");
        System.out.println("    }");
        System.out.println("    ");
        System.out.println("    // 3. No setters!");
        System.out.println("}\n");

        System.out.println("--- Demonstration ---");
        List<String> hobbies = new ArrayList<>();
        hobbies.add("Reading");
        hobbies.add("Coding");

        CompleteImmutable person = new CompleteImmutable("Alice", 30, hobbies);
        System.out.println("Name: " + person.getName());
        System.out.println("Age: " + person.getAge());
        System.out.println("Hobbies: " + person.getHobbies());

        System.out.println("\nTrying to modify...");
        hobbies.add("Gaming");  // Modify original list
        System.out.println("Hobbies after modifying original: " + person.getHobbies());

        person.getHobbies().add("Swimming");  // Modify returned list
        System.out.println("Hobbies after modifying returned: " + person.getHobbies());

        System.out.println("\n→ Object is truly immutable!");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * COMMON MISTAKES
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void commonMistakes() {
        System.out.println("=== COMMON MISTAKES ===\n");

        System.out.println("--- MISTAKE 1: final reference != immutable ✗ ---");
        System.out.println("final List<String> list = new ArrayList<>();");
        System.out.println("list.add(\"A\");  // ✓ Allowed!");
        System.out.println("// final means reference can't change, not object\n");

        System.out.println("--- MISTAKE 2: Not copying mutable fields ✗ ---");
        System.out.println("private final List<String> list;");
        System.out.println("public Bad(List<String> list) {");
        System.out.println("    this.list = list;  // ✗ No copy!");
        System.out.println("}\n");

        System.out.println("--- MISTAKE 3: Returning mutable field directly ✗ ---");
        System.out.println("public List<String> getList() {");
        System.out.println("    return list;  // ✗ Returns reference!");
        System.out.println("}\n");

        System.out.println("--- MISTAKE 4: Not making class final ✗ ---");
        System.out.println("public class Bad {  // Not final!");
        System.out.println("    private final String name;");
        System.out.println("}");
        System.out.println("// Subclass could add mutable behavior\n");

        System.out.println("--- MISTAKE 5: Having setter methods ✗ ---");
        System.out.println("public void setName(String name) {  // ✗ Setter!");
        System.out.println("    this.name = name;");
        System.out.println("}");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * EXAM TRAPS
     * ═══════════════════════════════════════════════════════════════════════
     */
    private static void examTraps() {
        System.out.println("=== EXAM TRAPS ===\n");

        System.out.println("--- TRAP 1: final field with mutable object ---");
        System.out.println("class Example {");
        System.out.println("    private final List<String> list = new ArrayList<>();");
        System.out.println("    public void add(String s) {");
        System.out.println("        list.add(s);  // ✓ Allowed! Object is mutable");
        System.out.println("    }");
        System.out.println("}");
        System.out.println("→ Not immutable! Reference is final, but object is mutable\n");

        System.out.println("--- TRAP 2: Thinking Collections.unmodifiableList makes it immutable ---");
        System.out.println("List<String> original = new ArrayList<>();");
        System.out.println("original.add(\"A\");");
        System.out.println("List<String> unmod = Collections.unmodifiableList(original);");
        System.out.println("original.add(\"B\");  // ✓ Allowed!");
        System.out.println("// unmod now shows [\"A\", \"B\"] - it's a VIEW, not a copy\n");

        System.out.println("--- TRAP 3: Not copying in constructor ---");
        System.out.println("public final class Bad {");
        System.out.println("    private final StringBuilder sb;");
        System.out.println("    public Bad(StringBuilder sb) {");
        System.out.println("        this.sb = sb;  // ✗ No copy!");
        System.out.println("    }");
        System.out.println("}");
        System.out.println("// Caller can still modify the StringBuilder\n");

        System.out.println("--- TRAP 4: Exposing mutable component ---");
        System.out.println("public final class Bad {");
        System.out.println("    private final Date date;");
        System.out.println("    public Date getDate() {");
        System.out.println("        return date;  // ✗ Date is mutable!");
        System.out.println("    }");
        System.out.println("}");
        System.out.println("// Should return: new Date(date.getTime())\n");

        System.out.println("*** EXAM CHECKLIST FOR IMMUTABLE OBJECTS ***");
        System.out.println("1. ✓ Class is final (or constructors are private)");
        System.out.println("2. ✓ All fields are private and final");
        System.out.println("3. ✓ No setter methods");
        System.out.println("4. ✓ Defensive copies for mutable objects (in and out)");
        System.out.println("5. ✓ Constructor initializes all fields");
        System.out.println("6. ✓ No methods that modify state");
        System.out.println("7. ✓ Methods that \"change\" state return new instance");

        System.out.println();
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// EXAMPLE CLASSES
// ═══════════════════════════════════════════════════════════════════════════

// ───────────────────────────────────────────────────────────────────────────
// RULE 1: FINAL CLASS
// ───────────────────────────────────────────────────────────────────────────

final class FinalImmutable {
    private final String value;

    FinalImmutable(String value) {
        this.value = value;
    }

    String getValue() {
        return value;
    }
}

// ───────────────────────────────────────────────────────────────────────────
// RULE 1: PRIVATE CONSTRUCTOR
// ───────────────────────────────────────────────────────────────────────────

class PrivateConstructorImmutable {
    private final String value;

    private PrivateConstructorImmutable(String value) {
        this.value = value;
    }

    static PrivateConstructorImmutable of(String value) {
        return new PrivateConstructorImmutable(value);
    }

    String getValue() {
        return value;
    }
}

// ───────────────────────────────────────────────────────────────────────────
// RULE 2: PRIVATE FINAL FIELDS
// ───────────────────────────────────────────────────────────────────────────

final class PrivateFinalFields {
    private final String name;
    private final int age;

    PrivateFinalFields(String name, int age) {
        this.name = name;
        this.age = age;
    }

    String getName() { return name; }
    int getAge() { return age; }
}

// ───────────────────────────────────────────────────────────────────────────
// RULE 4: DEFENSIVE COPYING
// ───────────────────────────────────────────────────────────────────────────

final class NoDefensiveCopy {
    private final List<String> list;

    NoDefensiveCopy(List<String> list) {
        this.list = list;  // ✗ No copy - stores reference
    }

    List<String> getList() {
        return list;  // ✗ No copy - returns reference
    }
}

final class WithDefensiveCopy {
    private final List<String> list;

    WithDefensiveCopy(List<String> list) {
        this.list = new ArrayList<>(list);  // ✓ Defensive copy
    }

    List<String> getList() {
        return new ArrayList<>(list);  // ✓ Defensive copy
    }
}

// ───────────────────────────────────────────────────────────────────────────
// COMPLETE IMMUTABLE EXAMPLE
// ───────────────────────────────────────────────────────────────────────────

final class CompleteImmutable {
    private final String name;
    private final int age;
    private final List<String> hobbies;

    CompleteImmutable(String name, int age, List<String> hobbies) {
        this.name = name;
        this.age = age;
        this.hobbies = new ArrayList<>(hobbies);  // Defensive copy
    }

    String getName() { return name; }
    int getAge() { return age; }

    List<String> getHobbies() {
        return new ArrayList<>(hobbies);  // Defensive copy
    }

    // No setters!
}
