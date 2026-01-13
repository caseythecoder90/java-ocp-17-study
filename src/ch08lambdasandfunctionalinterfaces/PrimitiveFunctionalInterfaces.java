package ch08lambdasandfunctionalinterfaces;

import java.util.EnumMap;
import java.util.Enumeration;
import java.util.function.*;

/**
 * Primitive Functional Interfaces - MEMORIZE FOR EXAM
 *
 * IMPORTANT: Not all combinations exist! Know which DON'T exist to avoid trick questions.
 *
 * ===== SUPPLIERS =====
 * BooleanSupplier          boolean getAsBoolean()
 * IntSupplier              int getAsInt()
 * LongSupplier             long getAsLong()
 * DoubleSupplier           double getAsDouble()
 *
 * ===== CONSUMERS =====
 * IntConsumer              void accept(int value)
 * LongConsumer             void accept(long value)
 * DoubleConsumer           void accept(double value)
 * NOTE: No BooleanConsumer!
 *
 * ===== PREDICATES =====
 * IntPredicate             boolean test(int value)
 * LongPredicate            boolean test(long value)
 * DoublePredicate          boolean test(double value)
 * NOTE: No BooleanPredicate!
 *
 * ===== FUNCTIONS (Primitive -> Object) =====
 * IntFunction<R>           R apply(int value)
 * LongFunction<R>          R apply(long value)
 * DoubleFunction<R>        R apply(double value)
 *
 * ===== UNARY OPERATORS (Same type in and out) =====
 * IntUnaryOperator         int applyAsInt(int operand)
 * LongUnaryOperator        long applyAsLong(long operand)
 * DoubleUnaryOperator      double applyAsDouble(double operand)
 *
 * ===== BINARY OPERATORS (Same type in and out) =====
 * IntBinaryOperator        int applyAsInt(int left, int right)
 * LongBinaryOperator       long applyAsLong(long left, long right)
 * DoubleBinaryOperator     double applyAsDouble(double left, double right)
 *
 * ===== TO-PRIMITIVE FUNCTIONS (Object -> Primitive) =====
 * ToIntFunction<T>         int applyAsInt(T value)
 * ToLongFunction<T>        long applyAsLong(T value)
 * ToDoubleFunction<T>      double applyAsDouble(T value)
 *
 * ===== TO-PRIMITIVE BI-FUNCTIONS (Object, Object -> Primitive) =====
 * ToIntBiFunction<T, U>    int applyAsInt(T t, U u)
 * ToLongBiFunction<T, U>   long applyAsLong(T t, U u)
 * ToDoubleBiFunction<T, U> double applyAsDouble(T t, U u)
 *
 * ===== PRIMITIVE-TO-PRIMITIVE FUNCTIONS =====
 * DoubleToIntFunction      int applyAsInt(double value)
 * DoubleToLongFunction     long applyAsLong(double value)
 * IntToDoubleFunction      double applyAsDouble(int value)
 * IntToLongFunction        long applyAsLong(int value)
 * LongToDoubleFunction     double applyAsDouble(long value)
 * LongToIntFunction        int applyAsInt(long value)
 * NOTE: No conversions to/from boolean! No same-to-same (e.g., IntToIntFunction doesn't exist - use IntUnaryOperator)
 *
 * ===== OBJECT-PRIMITIVE CONSUMERS =====
 * ObjIntConsumer<T>        void accept(T t, int value)
 * ObjLongConsumer<T>       void accept(T t, long value)
 * ObjDoubleConsumer<T>     void accept(T t, double value)
 * NOTE: No ObjBooleanConsumer!
 *
 * EXAM TRAPS - THESE DON'T EXIST:
 * - BooleanConsumer, BooleanPredicate, BooleanFunction
 * - Any boolean conversions (BooleanToIntFunction, IntToBooleanFunction, etc.)
 * - Same-to-same primitive functions (IntToIntFunction - use IntUnaryOperator instead)
 * - FloatSupplier, FloatConsumer, etc. (only int, long, double, boolean)
 */
public class PrimitiveFunctionalInterfaces {

    public static void main(String[] args) {

        // ===== SUPPLIERS =====
        System.out.println("=== SUPPLIERS ===");

        BooleanSupplier boolSupplier = () -> true;
        System.out.println("BooleanSupplier: " + boolSupplier.getAsBoolean());

        IntSupplier intSupplier = () -> 42;
        System.out.println("IntSupplier: " + intSupplier.getAsInt());

        LongSupplier longSupplier = () -> 100L;
        System.out.println("LongSupplier: " + longSupplier.getAsLong());

        DoubleSupplier doubleSupplier = () -> 3.14;
        System.out.println("DoubleSupplier: " + doubleSupplier.getAsDouble());

        // ===== CONSUMERS =====
        System.out.println("\n=== CONSUMERS ===");

        IntConsumer intConsumer = i -> System.out.println("IntConsumer: " + i);
        intConsumer.accept(10);

        LongConsumer longConsumer = l -> System.out.println("LongConsumer: " + l);
        longConsumer.accept(20L);

        DoubleConsumer doubleConsumer = d -> System.out.println("DoubleConsumer: " + d);
        doubleConsumer.accept(5.5);

        // NOTE: BooleanConsumer does NOT exist!

        // ===== PREDICATES =====
        System.out.println("\n=== PREDICATES ===");

        IntPredicate intPredicate = i -> i > 5;
        System.out.println("IntPredicate (10 > 5): " + intPredicate.test(10));

        LongPredicate longPredicate = l -> l < 100;
        System.out.println("LongPredicate (50 < 100): " + longPredicate.test(50L));

        DoublePredicate doublePredicate = d -> d == 0.0;
        System.out.println("DoublePredicate (1.5 == 0.0): " + doublePredicate.test(1.5));

        // NOTE: BooleanPredicate does NOT exist!

        // ===== FUNCTIONS (Primitive -> Object) =====
        System.out.println("\n=== FUNCTIONS (Primitive -> Object) ===");

        IntFunction<String> intFunction = i -> "Number: " + i;
        System.out.println("IntFunction: " + intFunction.apply(7));

        LongFunction<String> longFunction = l -> "Long: " + l;
        System.out.println("LongFunction: " + longFunction.apply(99L));

        DoubleFunction<String> doubleFunction = d -> "Double: " + d;
        System.out.println("DoubleFunction: " + doubleFunction.apply(2.5));

        // ===== UNARY OPERATORS =====
        System.out.println("\n=== UNARY OPERATORS (Same type in/out) ===");

        IntUnaryOperator intUnary = i -> i * 2;
        System.out.println("IntUnaryOperator (5 * 2): " + intUnary.applyAsInt(5));

        LongUnaryOperator longUnary = l -> l + 10;
        System.out.println("LongUnaryOperator (20 + 10): " + longUnary.applyAsLong(20L));

        DoubleUnaryOperator doubleUnary = d -> d / 2;
        System.out.println("DoubleUnaryOperator (10.0 / 2): " + doubleUnary.applyAsDouble(10.0));

        // ===== BINARY OPERATORS =====
        System.out.println("\n=== BINARY OPERATORS (Same type in/out) ===");

        IntBinaryOperator intBinary = (a, b) -> a + b;
        System.out.println("IntBinaryOperator (3 + 7): " + intBinary.applyAsInt(3, 7));

        LongBinaryOperator longBinary = (a, b) -> a * b;
        System.out.println("LongBinaryOperator (4 * 5): " + longBinary.applyAsLong(4L, 5L));

        DoubleBinaryOperator doubleBinary = (a, b) -> a - b;
        System.out.println("DoubleBinaryOperator (10.5 - 3.5): " + doubleBinary.applyAsDouble(10.5, 3.5));

        // ===== TO-PRIMITIVE FUNCTIONS (Object -> Primitive) =====
        System.out.println("\n=== TO-PRIMITIVE FUNCTIONS (Object -> Primitive) ===");

        ToIntFunction<String> toInt = s -> s.length();
        System.out.println("ToIntFunction (length of 'hello'): " + toInt.applyAsInt("hello"));

        ToLongFunction<String> toLong = s -> (long) s.length();
        System.out.println("ToLongFunction (length of 'world'): " + toLong.applyAsLong("world"));

        ToDoubleFunction<String> toDouble = s -> s.length() * 1.5;
        System.out.println("ToDoubleFunction (length * 1.5): " + toDouble.applyAsDouble("test"));

        // ===== TO-PRIMITIVE BI-FUNCTIONS (Object, Object -> Primitive) =====
        System.out.println("\n=== TO-PRIMITIVE BI-FUNCTIONS (Object, Object -> Primitive) ===");

        ToIntBiFunction<String, String> toIntBi = (s1, s2) -> s1.length() + s2.length();
        System.out.println("ToIntBiFunction (total length): " + toIntBi.applyAsInt("hello", "world"));

        ToLongBiFunction<String, String> toLongBi = (s1, s2) -> (long) (s1.length() * s2.length());
        System.out.println("ToLongBiFunction (length product): " + toLongBi.applyAsLong("hi", "bye"));

        ToDoubleBiFunction<Integer, Integer> toDoubleBi = (a, b) -> (a + b) / 2.0;
        System.out.println("ToDoubleBiFunction (average): " + toDoubleBi.applyAsDouble(10, 20));

        // ===== PRIMITIVE-TO-PRIMITIVE FUNCTIONS =====
        System.out.println("\n=== PRIMITIVE-TO-PRIMITIVE FUNCTIONS ===");

        DoubleToIntFunction doubleToInt = d -> (int) d;
        System.out.println("DoubleToIntFunction (5.7 -> int): " + doubleToInt.applyAsInt(5.7));

        DoubleToLongFunction doubleToLong = d -> (long) d;
        System.out.println("DoubleToLongFunction (9.9 -> long): " + doubleToLong.applyAsLong(9.9));

        IntToDoubleFunction intToDouble = i -> i * 1.5;
        System.out.println("IntToDoubleFunction (4 * 1.5): " + intToDouble.applyAsDouble(4));

        IntToLongFunction intToLong = i -> (long) i;
        System.out.println("IntToLongFunction (100 -> long): " + intToLong.applyAsLong(100));

        LongToDoubleFunction longToDouble = l -> l / 2.0;
        System.out.println("LongToDoubleFunction (10 / 2.0): " + longToDouble.applyAsDouble(10L));

        LongToIntFunction longToInt = l -> (int) l;
        System.out.println("LongToIntFunction (500 -> int): " + longToInt.applyAsInt(500L));

        // NOTE: IntToIntFunction does NOT exist - use IntUnaryOperator
        // NOTE: No boolean conversions exist!

        // ===== OBJECT-PRIMITIVE CONSUMERS =====
        System.out.println("\n=== OBJECT-PRIMITIVE CONSUMERS ===");

        ObjIntConsumer<String> objIntConsumer = (s, i) ->
            System.out.println("ObjIntConsumer: " + s + " = " + i);
        objIntConsumer.accept("Count", 42);

        ObjLongConsumer<String> objLongConsumer = (s, l) ->
            System.out.println("ObjLongConsumer: " + s + " = " + l);
        objLongConsumer.accept("ID", 123456L);

        ObjDoubleConsumer<String> objDoubleConsumer = (s, d) ->
            System.out.println("ObjDoubleConsumer: " + s + " = " + d);
        objDoubleConsumer.accept("Price", 19.99);

        // NOTE: ObjBooleanConsumer does NOT exist!

        // ===== EXAM TRAPS - COMMON MISTAKES =====
        System.out.println("\n=== EXAM TRAPS ===");
        System.out.println("These DO NOT exist:");
        System.out.println("- BooleanConsumer, BooleanPredicate, BooleanFunction");
        System.out.println("- ObjBooleanConsumer");
        System.out.println("- FloatSupplier, ByteConsumer, etc. (only int, long, double, boolean)");
        System.out.println("- IntToIntFunction (use IntUnaryOperator)");
        System.out.println("- BooleanToIntFunction, IntToBooleanFunction (no boolean conversions)");
    }
}
