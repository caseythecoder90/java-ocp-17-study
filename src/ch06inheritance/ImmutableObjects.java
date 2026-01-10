package ch06inheritance;

import java.util.ArrayList;
import java.util.List;

/**
 * IMMUTABLE OBJECTS - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * IMMUTABLE OBJECTS
 * ═══════════════════════════════════════════════════════════════════════════
 * Object whose state CANNOT change after construction.
 *
 * Benefits: Thread-safe, cacheable, safe as HashMap keys
 *
 * Examples: String, Integer, LocalDate
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * FIVE RULES FOR IMMUTABILITY
 * ═══════════════════════════════════════════════════════════════════════════
 * 1. PREVENT EXTENSION:
 *    - Mark class as final, OR
 *    - Make all constructors private (use static factory methods)
 *
 *    Private constructor pattern:
 *    private ImmutableClass() { }
 *    public static ImmutableClass of(...) { return new ImmutableClass(); }
 *
 *    Examples: Optional.of(), List.of(), LocalDate.of()
 *
 * 2. MARK ALL INSTANCE VARIABLES PRIVATE AND FINAL:
 *    private final ensures no reassignment, no external access
 *
 * 3. NO SETTER METHODS:
 *    No setXxx() methods!
 *    (OK to have withXxx() that returns NEW instance)
 *
 * 4. DEFENSIVE COPYING FOR MUTABLE OBJECTS:
 *    Constructor: this.list = new ArrayList<>(list);
 *    Getter: return new ArrayList<>(list);
 *
 *    CRITICAL: final reference ≠ immutable object!
 *
 * 5. INITIALIZE ALL FIELDS IN CONSTRUCTOR:
 *    All fields set at construction time
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * DEFENSIVE COPYING - CRITICAL EXAM TOPIC
 * ═══════════════════════════════════════════════════════════════════════════
 * WRONG: this.list = list;           // Stores reference - mutable!
 * RIGHT: this.list = new ArrayList<>(list);  // Copy - immutable!
 *
 * WRONG: return list;                // Returns reference - mutable!
 * RIGHT: return new ArrayList<>(list);  // Copy - immutable!
 */
public class ImmutableObjects {

    public static void main(String[] args) {
        // ────────────────────────────────────────────────────────────────────
        // String is immutable
        // ────────────────────────────────────────────────────────────────────
        String s = "Hello";
        s.toUpperCase();  // Returns NEW string
        System.out.println(s);  // Still "Hello"

        // ────────────────────────────────────────────────────────────────────
        // final class
        // ────────────────────────────────────────────────────────────────────
        FinalImmutable f1 = new FinalImmutable("Test");
        System.out.println(f1.getValue());

        // ────────────────────────────────────────────────────────────────────
        // Private constructor + static factory
        // ────────────────────────────────────────────────────────────────────
        PrivateConstructorImmutable p1 = PrivateConstructorImmutable.of("Factory");
        System.out.println(p1.getValue());

        // ────────────────────────────────────────────────────────────────────
        // Defensive copying - WRONG way
        // ────────────────────────────────────────────────────────────────────
        List<String> list1 = new ArrayList<>();
        list1.add("A");
        NoDefensiveCopy bad = new NoDefensiveCopy(list1);
        System.out.println("Initial: " + bad.getList());

        list1.add("B");  // Modify original
        System.out.println("After modifying original: " + bad.getList());  // Changed!

        bad.getList().add("C");  // Modify returned
        System.out.println("After modifying returned: " + bad.getList());  // Changed!
        System.out.println("→ Not immutable!\n");

        // ────────────────────────────────────────────────────────────────────
        // Defensive copying - CORRECT way
        // ────────────────────────────────────────────────────────────────────
        List<String> list2 = new ArrayList<>();
        list2.add("A");
        WithDefensiveCopy good = new WithDefensiveCopy(list2);
        System.out.println("Initial: " + good.getList());

        list2.add("B");  // Modify original
        System.out.println("After modifying original: " + good.getList());  // Unchanged!

        good.getList().add("C");  // Modify returned
        System.out.println("After modifying returned: " + good.getList());  // Unchanged!
        System.out.println("→ Truly immutable!\n");

        // ────────────────────────────────────────────────────────────────────
        // Complete immutable example
        // ────────────────────────────────────────────────────────────────────
        List<String> hobbies = new ArrayList<>();
        hobbies.add("Reading");
        CompleteImmutable person = new CompleteImmutable("Alice", 30, hobbies);
        System.out.println("Name: " + person.getName());
        System.out.println("Hobbies: " + person.getHobbies());

        hobbies.add("Gaming");  // Modify original
        System.out.println("After modifying original: " + person.getHobbies());  // Unchanged!

        System.out.println("\n✓ All immutability examples work");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// RULE 1: FINAL CLASS
// ═══════════════════════════════════════════════════════════════════════════

final class FinalImmutable {
    private final String value;

    FinalImmutable(String value) {
        this.value = value;
    }

    String getValue() {
        return value;
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// RULE 1: PRIVATE CONSTRUCTOR + STATIC FACTORY
// ═══════════════════════════════════════════════════════════════════════════

class PrivateConstructorImmutable {
    private final String value;

    private PrivateConstructorImmutable(String value) {
        this.value = value;
    }

    static PrivateConstructorImmutable of(String value) {
        return new PrivateConstructorImmutable(value);  // Can call private constructor
    }

    String getValue() {
        return value;
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// RULE 4: DEFENSIVE COPYING - WRONG (not immutable)
// ═══════════════════════════════════════════════════════════════════════════

final class NoDefensiveCopy {
    private final List<String> list;

    NoDefensiveCopy(List<String> list) {
        this.list = list;  // ✗ Stores reference - NOT immutable!
    }

    List<String> getList() {
        return list;  // ✗ Returns reference - NOT immutable!
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// RULE 4: DEFENSIVE COPYING - CORRECT (immutable)
// ═══════════════════════════════════════════════════════════════════════════

final class WithDefensiveCopy {
    private final List<String> list;

    WithDefensiveCopy(List<String> list) {
        this.list = new ArrayList<>(list);  // ✓ Defensive copy
    }

    List<String> getList() {
        return new ArrayList<>(list);  // ✓ Defensive copy
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// COMPLETE IMMUTABLE EXAMPLE - Following ALL 5 rules
// ═══════════════════════════════════════════════════════════════════════════

final class CompleteImmutable {  // 1. final class
    private final String name;    // 2. private final
    private final int age;        // 2. private final
    private final List<String> hobbies;  // 2. private final

    CompleteImmutable(String name, int age, List<String> hobbies) {  // 5. Constructor
        this.name = name;
        this.age = age;
        this.hobbies = new ArrayList<>(hobbies);  // 4. Defensive copy
    }

    String getName() { return name; }
    int getAge() { return age; }

    List<String> getHobbies() {
        return new ArrayList<>(hobbies);  // 4. Defensive copy
    }

    // 3. No setters!
}

// ═══════════════════════════════════════════════════════════════════════════
// EXAM TRAPS
// ═══════════════════════════════════════════════════════════════════════════

// TRAP 1: final reference ≠ immutable object
// final List<String> list = new ArrayList<>();
// list.add("A");  // ✓ Allowed! Object is mutable

// TRAP 2: Not copying mutable field
// class Bad {
//     private final List<String> list;
//     Bad(List<String> list) {
//         this.list = list;  // ✗ No copy!
//     }
// }

// TRAP 3: Returning mutable field directly
// List<String> getList() {
//     return list;  // ✗ Returns reference!
// }

// TRAP 4: Not making class final
// class Bad {  // Not final - subclass could add mutable behavior
//     private final String name;
// }
