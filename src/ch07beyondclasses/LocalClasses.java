package ch07beyondclasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Local Classes
 *
 * Key characteristics:
 * - Nested classes defined within a method, constructor, or initializer
 * - Declaration doesn't exist until the method is invoked
 * - Goes out of scope when the method returns
 * - Can only create instances from within the method
 * - Instances CAN be returned from the method (like local variables)
 * - Do NOT have access modifiers (no public, private, protected)
 * - CAN be declared final or abstract
 * - Have access to all fields and methods of enclosing class (in instance methods)
 * - Can access final and effectively final local variables
 * - In static methods, can only access static members of outer class
 */
public class LocalClasses {

    // ===== BASIC LOCAL CLASS IN METHOD =====

    static class Calculator {
        private int baseValue = 100;

        public void calculate(int multiplier) {
            final int bonus = 10;  // final local variable
            int result = 0;        // effectively final (never reassigned)

            // Local class defined inside method
            class LocalCalculator {
                void compute() {
                    // Can access instance field of outer class
                    System.out.println("Base value: " + baseValue);

                    // Can access method parameters (effectively final)
                    System.out.println("Multiplier: " + multiplier);

                    // Can access final local variables
                    System.out.println("Bonus: " + bonus);

                    // Can access effectively final local variables
                    System.out.println("Result: " + result);
                }
            }

            // Can only instantiate within the method
            LocalCalculator calc = new LocalCalculator();
            calc.compute();

            // CANNOT instantiate outside this method - compilation error
        }
    }

    // ===== RETURNING LOCAL CLASS INSTANCE =====

    static class AnimalFactory {
        interface Animal {
            void makeSound();
        }

        public Animal createDog() {
            String sound = "Woof";  // effectively final

            // Local class implementing interface
            class Dog implements Animal {
                @Override
                public void makeSound() {
                    System.out.println(sound);
                }
            }

            // Return instance of local class
            return new Dog();
        }

        public List<Animal> createAnimals() {
            // Local class in method
            class Cat implements Animal {
                @Override
                public void makeSound() {
                    System.out.println("Meow");
                }
            }

            List<Animal> animals = new ArrayList<>();
            animals.add(new Cat());
            animals.add(new Cat());
            return animals;  // Returning instances created in method
        }
    }

    // ===== FINAL AND ABSTRACT LOCAL CLASSES =====

    static class Shape {
        public void processShapes() {
            // Abstract local class
            abstract class AbstractShape {
                abstract void draw();
            }

            // Concrete implementation of abstract local class
            class Circle extends AbstractShape {
                @Override
                void draw() {
                    System.out.println("Drawing circle");
                }
            }

            // Final local class (cannot be extended within this method)
            final class Square extends AbstractShape {
                @Override
                void draw() {
                    System.out.println("Drawing square");
                }
            }

            // Cannot extend Square because it's final
            // class ExtendedSquare extends Square { }  // ERROR!

            AbstractShape circle = new Circle();
            AbstractShape square = new Square();
            circle.draw();
            square.draw();
        }
    }

    // ===== NO ACCESS MODIFIERS =====

    static class AccessModifierExample {
        public void demonstrateAccessModifiers() {
            // Local classes CANNOT have access modifiers
            // public class PublicLocal { }      // COMPILATION ERROR!
            // private class PrivateLocal { }    // COMPILATION ERROR!
            // protected class ProtectedLocal { } // COMPILATION ERROR!

            // Only valid options: no modifier, final, or abstract
            class ValidLocal { }
            final class FinalLocal { }
            abstract class AbstractLocal { }
        }
    }

    // ===== FINAL AND EFFECTIVELY FINAL VARIABLES =====

    static class VariableAccess {
        public void demonstrateFinalVariables() {
            final int finalVar = 10;
            int effectivelyFinal = 20;
            int notEffectivelyFinal = 30;

            notEffectivelyFinal = 40;  // Reassigned - NOT effectively final

            class LocalClass {
                void display() {
                    // Can access final variable
                    System.out.println("Final: " + finalVar);

                    // Can access effectively final variable
                    System.out.println("Effectively final: " + effectivelyFinal);

                    // CANNOT access non-effectively-final variable
                    // System.out.println(notEffectivelyFinal);  // ERROR!
                }
            }

            LocalClass lc = new LocalClass();
            lc.display();

            // Cannot modify effectivelyFinal after local class uses it
            // effectivelyFinal = 25;  // Would make above code not compile
        }
    }

    // ===== LOCAL CLASS IN STATIC METHOD =====

    static class StaticMethodExample {
        private static int staticField = 100;
        private int instanceField = 200;

        public static void staticMethod() {
            int localVar = 50;

            class LocalInStatic {
                void display() {
                    // CAN access static fields of outer class
                    System.out.println("Static field: " + staticField);

                    // CAN access local variables (effectively final)
                    System.out.println("Local var: " + localVar);

                    // CANNOT access instance fields (no instance context)
                    // System.out.println(instanceField);  // ERROR!
                }
            }

            LocalInStatic local = new LocalInStatic();
            local.display();
        }

        public void instanceMethod() {
            int localVar = 50;

            class LocalInInstance {
                void display() {
                    // CAN access static fields
                    System.out.println("Static field: " + staticField);

                    // CAN access instance fields
                    System.out.println("Instance field: " + instanceField);

                    // CAN access local variables
                    System.out.println("Local var: " + localVar);
                }
            }

            LocalInInstance local = new LocalInInstance();
            local.display();
        }
    }

    // ===== LOCAL CLASS IN CONSTRUCTOR =====

    static class ConstructorExample {
        private String name;

        public ConstructorExample(String inputName) {
            final String prefix = "Mr. ";

            // Local class in constructor
            class NameFormatter {
                String format() {
                    return prefix + inputName;
                }
            }

            NameFormatter formatter = new NameFormatter();
            this.name = formatter.format();
        }

        public String getName() {
            return name;
        }
    }

    // ===== LOCAL CLASS IN INITIALIZER =====

    static class InitializerExample {
        private List<String> items;

        // Instance initializer block
        {
            class ItemBuilder {
                List<String> build() {
                    List<String> list = new ArrayList<>();
                    list.add("Item 1");
                    list.add("Item 2");
                    return list;
                }
            }

            ItemBuilder builder = new ItemBuilder();
            items = builder.build();
        }

        public void displayItems() {
            System.out.println("Items: " + items);
        }
    }

    // ===== EXAM TRAPS =====

    static class ExamTraps {
        public Runnable createRunnable() {
            int count = 0;  // effectively final

            class Counter implements Runnable {
                @Override
                public void run() {
                    // Can read effectively final variable
                    System.out.println("Count: " + count);

                    // CANNOT modify it
                    // count++;  // COMPILATION ERROR!
                }
            }

            // This would break effectively final
            // count++;  // Would cause compilation error above

            return new Counter();
        }

        public void scopeDemo() {
            class LocalClass {
                void method() {
                    System.out.println("Local class method");
                }
            }

            LocalClass lc = new LocalClass();
            lc.method();
        }

        public void cannotAccessAfterMethod() {
            scopeDemo();

            // LocalClass is out of scope here
            // LocalClass lc = new LocalClass();  // COMPILATION ERROR!
        }
    }

    // ===== DEMONSTRATION =====

    public static void main(String[] args) {
        System.out.println("=== BASIC LOCAL CLASS ===");
        Calculator calc = new Calculator();
        calc.calculate(5);

        System.out.println("\n=== RETURNING LOCAL CLASS INSTANCES ===");
        AnimalFactory factory = new AnimalFactory();
        AnimalFactory.Animal dog = factory.createDog();
        dog.makeSound();

        List<AnimalFactory.Animal> animals = factory.createAnimals();
        animals.forEach(AnimalFactory.Animal::makeSound);

        System.out.println("\n=== FINAL AND ABSTRACT LOCAL CLASSES ===");
        Shape shape = new Shape();
        shape.processShapes();

        System.out.println("\n=== FINAL AND EFFECTIVELY FINAL ===");
        VariableAccess va = new VariableAccess();
        va.demonstrateFinalVariables();

        System.out.println("\n=== LOCAL CLASS IN STATIC METHOD ===");
        StaticMethodExample.staticMethod();

        System.out.println("\n=== LOCAL CLASS IN INSTANCE METHOD ===");
        StaticMethodExample sme = new StaticMethodExample();
        sme.instanceMethod();

        System.out.println("\n=== LOCAL CLASS IN CONSTRUCTOR ===");
        ConstructorExample ce = new ConstructorExample("Smith");
        System.out.println("Name: " + ce.getName());

        System.out.println("\n=== LOCAL CLASS IN INITIALIZER ===");
        InitializerExample ie = new InitializerExample();
        ie.displayItems();

        System.out.println("\n=== EXAM TRAPS ===");
        ExamTraps traps = new ExamTraps();
        Runnable r = traps.createRunnable();
        r.run();

        System.out.println("\n=== KEY EXAM POINTS ===");
        System.out.println("✓ Local classes defined in method/constructor/initializer");
        System.out.println("✓ Only exist during method execution");
        System.out.println("✓ Instances CAN be returned from method");
        System.out.println("✗ CANNOT have access modifiers (public/private/protected)");
        System.out.println("✓ CAN be final or abstract");
        System.out.println("✓ Access all outer class members (in instance methods)");
        System.out.println("✓ Access only static members (in static methods)");
        System.out.println("✓ Can access final and effectively final local variables");
        System.out.println("✗ CANNOT access variables that are reassigned");
    }
}
