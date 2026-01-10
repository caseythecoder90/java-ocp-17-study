package ch06inheritance;

/**
 * INITIALIZING OBJECTS - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CLASS LOADING (happens ONCE per class)
 * ═══════════════════════════════════════════════════════════════════════════
 * 1. If superclass Y of X exists, initialize class Y first
 * 2. Process all static variable declarations in order
 * 3. Process all static initializers in order
 *
 * Class loaded when:
 * - First time referenced, new instance created, static member accessed
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * DEFAULT VALUES (non-final fields only)
 * ═══════════════════════════════════════════════════════════════════════════
 * boolean → false      |  byte/short/int/long → 0
 * float/double → 0.0   |  char → '\u0000'  |  Object → null
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * FINAL STATIC VARIABLES
 * ═══════════════════════════════════════════════════════════════════════════
 * Must initialize: inline OR in static initializer (NOT instance initializer!)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * FINAL INSTANCE VARIABLES
 * ═══════════════════════════════════════════════════════════════════════════
 * Must initialize: inline, instance initializer, OR constructor
 * ALL constructors must initialize final instance fields!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * OBJECT INITIALIZATION (happens EACH time object created)
 * ═══════════════════════════════════════════════════════════════════════════
 * 1. Initialize class X if not previously initialized
 * 2. If superclass Y exists, initialize instance of Y first
 * 3. Process all instance variable declarations in order
 * 4. Process all instance initializers in order
 * 5. Initialize constructor (including this() calls)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * COMPLETE ORDER (first time creating object)
 * ═══════════════════════════════════════════════════════════════════════════
 * 1. Parent static variables
 * 2. Parent static initializers
 * 3. Child static variables
 * 4. Child static initializers
 * 5. Parent instance variables
 * 6. Parent instance initializers
 * 7. Parent constructor
 * 8. Child instance variables
 * 9. Child instance initializers
 * 10. Child constructor
 *
 * Subsequent objects: Skip steps 1-4 (class already loaded)
 */
public class InitializingObjects {

    public static void main(String[] args) {
        System.out.println("=== First object creation ===");
        CompleteOrder obj1 = new CompleteOrder();

        System.out.println("\n=== Second object (class already loaded) ===");
        CompleteOrder obj2 = new CompleteOrder();

        // Static initialization only happens once!
        System.out.println("\n=== Multiple objects ===");
        MultipleExample m1 = new MultipleExample(1);
        MultipleExample m2 = new MultipleExample(2);
        MultipleExample m3 = new MultipleExample(3);
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// FINAL STATIC vs FINAL INSTANCE
// ═══════════════════════════════════════════════════════════════════════════

class FinalFields {
    // final static: inline or static initializer
    static final int STATIC_INLINE = 100;
    static final int STATIC_BLOCK;
    static {
        STATIC_BLOCK = 200;
    }

    // final instance: inline, instance initializer, or constructor
    private final int instanceInline = 10;
    private final int instanceBlock;
    private final int instanceConstructor;

    {
        instanceBlock = 20;
    }

    FinalFields() {
        instanceConstructor = 30;
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// COMPLETE INITIALIZATION ORDER
// ═══════════════════════════════════════════════════════════════════════════

class CompleteOrderParent {
    static { System.out.println("1. Parent static block"); }
    { System.out.println("5. Parent instance block"); }
    CompleteOrderParent() { System.out.println("6. Parent constructor"); }
}

class CompleteOrder extends CompleteOrderParent {
    static { System.out.println("2. Child static block"); }
    { System.out.println("7. Child instance block"); }
    CompleteOrder() { System.out.println("8. Child constructor"); }
}

// ═══════════════════════════════════════════════════════════════════════════
// MULTIPLE OBJECT CREATION - Static only once
// ═══════════════════════════════════════════════════════════════════════════

class MultipleExample {
    static { System.out.println("  [Static: Class loaded]"); }
    { System.out.println("  [Instance: Object initialized]"); }

    MultipleExample(int id) {
        System.out.println("  [Constructor: Object " + id + "]");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// EXAM TRAP: Order matters!
// ═══════════════════════════════════════════════════════════════════════════

class TrickyOrder {
    private int x = getValue();  // Called BEFORE y is initialized
    private int y = 20;

    private int getValue() {
        return y;  // y not initialized yet, returns default 0
    }
    // Result: x = 0, y = 20 (not x = 20 as might be expected!)
}
