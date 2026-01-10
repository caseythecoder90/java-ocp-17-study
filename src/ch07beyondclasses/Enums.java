package ch07beyondclasses;

/**
 * ENUMS - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * ENUM BASICS
 * ═══════════════════════════════════════════════════════════════════════════
 * Special type of class representing fixed set of constants.
 *
 * INITIALIZATION:
 * - Created ONCE by JVM when first referenced
 * - ALL enum values constructed at same time
 * - Later references return already-constructed values (singleton pattern)
 *
 * COMPARISON:
 * - Can use == (recommended) or equals()
 * - Both work because enums are singletons
 *
 * RESTRICTIONS:
 * - Cannot extend an enum (implicitly extends java.lang.Enum)
 * - Cannot be extended (implicitly final)
 *
 * SEMICOLON:
 * - Simple enums (values only) - semicolon optional
 * - Complex enums (with members) - semicolon required after values
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * ENUM METHODS
 * ═══════════════════════════════════════════════════════════════════════════
 * values()      - Returns array of all enum values (in declaration order)
 * name()        - Returns exact name as declared
 * ordinal()     - Returns 0-based position (avoid using in production)
 * valueOf(String) - Returns enum for given name (throws IllegalArgumentException)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * ENUMS IN SWITCH STATEMENTS
 * ═══════════════════════════════════════════════════════════════════════════
 * CRITICAL: Use VALUE, not EnumType.VALUE
 *
 * switch (season) {
 *     case WINTER:    // ✓ Correct
 *     case Season.WINTER:  // ✗ DOES NOT COMPILE
 * }
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * COMPLEX ENUMS
 * ═══════════════════════════════════════════════════════════════════════════
 * Can have constructors, fields, and methods.
 *
 * CONSTRUCTOR RULES:
 * - All enum constructors implicitly private
 * - private modifier optional (but implicit)
 * - Cannot call constructor directly (no new allowed)
 *
 * INITIALIZATION:
 * - First time ANY enum value is referenced, ALL are constructed
 * - Constructor called for each value (without new keyword)
 *
 * MUTABLE FIELDS:
 * - Possible but BAD PRACTICE (breaks singleton guarantee)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * ABSTRACT METHODS IN ENUMS
 * ═══════════════════════════════════════════════════════════════════════════
 * Can declare abstract methods - each value MUST implement.
 *
 * Alternative: Provide default implementation, override for special cases.
 */
public class Enums {

    public static void main(String[] args) {
        System.out.println("=== SIMPLE ENUMS ===\n");

        // ────────────────────────────────────────────────────────────────────
        // Simple enum usage
        // ────────────────────────────────────────────────────────────────────
        Season summer = Season.SUMMER;
        System.out.println("Season: " + summer);

        // ────────────────────────────────────────────────────────────────────
        // Comparison: == and equals() both work
        // ────────────────────────────────────────────────────────────────────
        Season s1 = Season.WINTER;
        Season s2 = Season.WINTER;
        System.out.println("== : " + (s1 == s2));        // true
        System.out.println("equals: " + s1.equals(s2));  // true

        // ────────────────────────────────────────────────────────────────────
        // Enum methods
        // ────────────────────────────────────────────────────────────────────
        System.out.println("\nEnum methods:");
        for (Season s : Season.values()) {  // values()
            System.out.println(s.name() + " - ordinal: " + s.ordinal());
        }

        Season fall = Season.valueOf("FALL");  // valueOf()
        System.out.println("valueOf: " + fall);

        // ────────────────────────────────────────────────────────────────────
        // Enums in switch statements
        // ────────────────────────────────────────────────────────────────────
        printSeason(Season.SPRING);
        printSeason(Season.WINTER);

        System.out.println("\n=== COMPLEX ENUMS ===\n");

        // ────────────────────────────────────────────────────────────────────
        // Complex enum with constructor, fields, methods
        // ────────────────────────────────────────────────────────────────────
        System.out.println("Days in JANUARY: " + Month.JANUARY.getDays());
        System.out.println("Days in FEBRUARY: " + Month.FEBRUARY.getDays());

        // ────────────────────────────────────────────────────────────────────
        // Enum initialization - all constructed on first access
        // ────────────────────────────────────────────────────────────────────
        System.out.println("\nFirst access to TrafficLight:");
        TrafficLight light = TrafficLight.RED;  // All 3 constructed here
        System.out.println("Selected: " + light);

        // ────────────────────────────────────────────────────────────────────
        // Abstract methods in enums
        // ────────────────────────────────────────────────────────────────────
        System.out.println("\nAbstract method example:");
        for (Operation op : Operation.values()) {
            System.out.println("10 " + op + " 5 = " + op.apply(10, 5));
        }

        // ────────────────────────────────────────────────────────────────────
        // Default implementation with override
        // ────────────────────────────────────────────────────────────────────
        System.out.println("\nDefault with override:");
        for (AnimalSealed a : AnimalSealed.values()) {
            System.out.println(a + ": " + a.getSound());
        }

        System.out.println("\n✓ All enum examples work");
    }

    static void printSeason(Season season) {
        // CRITICAL: Use VALUE, not Season.VALUE
        switch (season) {
            case WINTER:  // ✓ Correct
                System.out.println("Cold");
                break;
            case SPRING:
                System.out.println("Flowers");
                break;
            case SUMMER:
                System.out.println("Hot");
                break;
            case FALL:
                System.out.println("Leaves");
                break;
            // case Season.WINTER:  // ✗ DOES NOT COMPILE
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// SIMPLE ENUM - No semicolon needed
// ═══════════════════════════════════════════════════════════════════════════

enum Season {
    WINTER, SPRING, SUMMER, FALL  // No semicolon needed for simple enum
}

// Same as:
// enum Season {
//     WINTER, SPRING, SUMMER, FALL;  // Semicolon optional
// }

// ═══════════════════════════════════════════════════════════════════════════
// COMPLEX ENUM - Constructor, fields, methods
// ═══════════════════════════════════════════════════════════════════════════

enum Month {
    // Semicolon REQUIRED when enum has members
    JANUARY(31),
    FEBRUARY(28),
    MARCH(31),
    APRIL(30),
    MAY(31),
    JUNE(30);  // Semicolon required!

    // Fields (typically private final)
    private final int days;

    // Constructor - implicitly private
    Month(int days) {  // private is implicit
        this.days = days;
    }

    // Explicitly private (same as above)
    // private Month(int days) {
    //     this.days = days;
    // }

    // Methods
    public int getDays() {
        return days;
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// ENUM INITIALIZATION - All constructed on first access
// ═══════════════════════════════════════════════════════════════════════════

enum TrafficLight {
    RED("Stop"),
    YELLOW("Caution"),
    GREEN("Go");

    private final String action;

    TrafficLight(String action) {
        this.action = action;
        System.out.println("Constructing: " + this.name());
    }

    public String getAction() {
        return action;
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// MUTABLE ENUM - Possible but BAD PRACTICE
// ═══════════════════════════════════════════════════════════════════════════

enum BadPractice {
    VALUE;

    private int counter = 0;  // Mutable - BAD!

    public void increment() {
        counter++;  // ✗ Breaks singleton guarantee
    }

    public int getCounter() {
        return counter;
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// ABSTRACT METHODS - Each value MUST implement
// ═══════════════════════════════════════════════════════════════════════════

enum Operation {
    ADD {
        @Override
        public double apply(double x, double y) {
            return x + y;
        }
    },
    SUBTRACT {
        @Override
        public double apply(double x, double y) {
            return x - y;
        }
    },
    MULTIPLY {
        @Override
        public double apply(double x, double y) {
            return x * y;
        }
    },
    DIVIDE {
        @Override
        public double apply(double x, double y) {
            return x / y;
        }
    };

    // Abstract method - each value MUST implement
    public abstract double apply(double x, double y);
}

// ═══════════════════════════════════════════════════════════════════════════
// DEFAULT IMPLEMENTATION WITH OVERRIDE
// ═══════════════════════════════════════════════════════════════════════════

enum Animal {
    DOG,
    CAT,
    BIRD {
        @Override
        public String getSound() {
            return "Chirp";  // Override for special case
        }
    },
    COW;

    // Default implementation (can be overridden)
    public String getSound() {
        return "Generic animal sound";
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// EXAM TRAPS
// ═══════════════════════════════════════════════════════════════════════════

// TRAP 1: Cannot extend enum
// enum Bad extends Season { }  // ✗ DOES NOT COMPILE

// TRAP 2: Cannot create enum instance
// Season s = new Season();  // ✗ DOES NOT COMPILE

// TRAP 3: Using enum type in switch
// switch (season) {
//     case Season.WINTER:  // ✗ DOES NOT COMPILE - use WINTER
// }

// TRAP 4: valueOf with wrong name
// Season s = Season.valueOf("Winter");  // ✗ IllegalArgumentException (case-sensitive)

// TRAP 5: Missing semicolon in complex enum
// enum Bad {
//     VALUE  // ✗ DOES NOT COMPILE - missing semicolon before members
//     private int x;
// }

// TRAP 6: Public/protected constructor
// enum Bad {
//     VALUE;
//     public Bad() { }  // ✗ DOES NOT COMPILE - must be private
// }

// TRAP 7: Not implementing abstract method
// enum Bad {
//     VALUE;  // ✗ DOES NOT COMPILE - must implement apply()
//     public abstract double apply(double x, double y);
// }
