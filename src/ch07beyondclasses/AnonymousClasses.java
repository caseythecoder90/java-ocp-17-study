package ch07beyondclasses;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Anonymous Classes
 *
 * Key characteristics:
 * - Specialized form of local class that does NOT have a name
 * - Declared and instantiated in one statement using:
 *   new keyword + type name with parentheses + set of braces
 * - MUST extend an existing class OR implement an existing interface
 * - Cannot have explicit constructors (no name to call constructor)
 * - Can access final and effectively final local variables
 * - Can access members of enclosing class
 * - Syntax: new TypeName() { body }
 */
public class AnonymousClasses {

    // ===== ANONYMOUS CLASS IMPLEMENTING AN INTERFACE =====

    interface Greeting {
        void greet(String name);
    }

    public static void demonstrateInterfaceImplementation() {
        // Anonymous class implementing Greeting interface
        Greeting greeting = new Greeting() {
            @Override
            public void greet(String name) {
                System.out.println("Hello, " + name + "!");
            }
        };

        greeting.greet("Alice");

        // Another anonymous class with different implementation
        Greeting formalGreeting = new Greeting() {
            @Override
            public void greet(String name) {
                System.out.println("Good day, " + name + ". How do you do?");
            }
        };

        formalGreeting.greet("Bob");

        // Compare with lambda expressions
        Greeting greetingLambda = name -> System.out.println("Hello there, " + name);
    }

    // ===== ANONYMOUS CLASS IMPLEMENTING RUNNABLE =====

    public static void demonstrateRunnable() {
        // Anonymous class implementing Runnable
        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println("Task is running in anonymous class");
            }
        };

        task.run();

        // Inline usage - common pattern
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread running with anonymous Runnable");
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // ===== ANONYMOUS CLASS EXTENDING A CONCRETE CLASS =====

    static class Animal {
        protected String name;

        public Animal(String name) {
            this.name = name;
        }

        public void makeSound() {
            System.out.println("Some generic animal sound");
        }

        public void eat() {
            System.out.println(name + " is eating");
        }
    }

    public static void demonstrateExtendingClass() {
        // Anonymous class extending Animal with no-arg constructor
        // Note: Must call super() implicitly or explicitly
        Animal dog = new Animal("Buddy") {
            @Override
            public void makeSound() {
                System.out.println(name + " says: Woof! Woof!");
            }
        };

        dog.makeSound();
        dog.eat();

        // Another anonymous class extending Animal
        Animal cat = new Animal("Whiskers") {
            @Override
            public void makeSound() {
                System.out.println(name + " says: Meow!");
            }

            // Can add new methods, but can only call them within this block
            public void purr() {
                System.out.println(name + " is purring");
            }

            // Calling the new method within the anonymous class
            public void interact() {
                makeSound();
                purr();  // Can call here
            }
        };

        cat.makeSound();
        cat.eat();
        // cat.purr();  // COMPILATION ERROR - method not in Animal type

        // But we can call interact which uses purr internally
        Animal interactiveCat = new Animal("Fluffy") {
            {
                // Instance initializer block
                makeSound();
            }

            @Override
            public void makeSound() {
                System.out.println(name + " says: Meow softly");
            }
        };
    }

    // ===== ANONYMOUS CLASS EXTENDING AN ABSTRACT CLASS =====

    abstract static class Vehicle {
        protected String brand;
        protected int year;

        public Vehicle(String brand, int year) {
            this.brand = brand;
            this.year = year;
        }

        // Abstract method - must be implemented
        public abstract void start();

        // Concrete method - can be overridden
        public void stop() {
            System.out.println(brand + " is stopping");
        }

        // Concrete method
        public void displayInfo() {
            System.out.println("Brand: " + brand + ", Year: " + year);
        }
    }

    public static void demonstrateExtendingAbstractClass() {
        // Anonymous class extending abstract Vehicle
        Vehicle car = new Vehicle("Toyota", 2023) {
            @Override
            public void start() {
                System.out.println(brand + " car is starting with key");
            }

            @Override
            public void stop() {
                System.out.println(brand + " car is stopping smoothly");
            }
        };

        car.displayInfo();
        car.start();
        car.stop();

        // Another anonymous class with different implementation
        Vehicle motorcycle = new Vehicle("Harley", 2022) {
            @Override
            public void start() {
                System.out.println(brand + " motorcycle is roaring to life!");
            }
            // Inherits default stop() method
        };

        motorcycle.displayInfo();
        motorcycle.start();
        motorcycle.stop();
    }

    // ===== ANONYMOUS CLASS WITH ABSTRACT CLASS AND MULTIPLE METHODS =====

    abstract static class Shape {
        protected String color;

        public Shape(String color) {
            this.color = color;
        }

        public abstract double calculateArea();
        public abstract double calculatePerimeter();

        public void display() {
            System.out.println("Color: " + color);
            System.out.println("Area: " + calculateArea());
            System.out.println("Perimeter: " + calculatePerimeter());
        }
    }

    public static void demonstrateComplexAbstractClass() {
        // Anonymous class implementing all abstract methods
        Shape circle = new Shape("Red") {
            private double radius = 5.0;

            @Override
            public double calculateArea() {
                return Math.PI * radius * radius;
            }

            @Override
            public double calculatePerimeter() {
                return 2 * Math.PI * radius;
            }
        };

        circle.display();

        Shape rectangle = new Shape("Blue") {
            private double length = 10.0;
            private double width = 5.0;

            @Override
            public double calculateArea() {
                return length * width;
            }

            @Override
            public double calculatePerimeter() {
                return 2 * (length + width);
            }
        };

        rectangle.display();
    }

    // ===== ACCESSING FINAL AND EFFECTIVELY FINAL VARIABLES =====

    public static void demonstrateVariableAccess() {
        final String prefix = "Mr. ";
        String suffix = " Esq.";  // effectively final
        int counter = 100;        // effectively final

        Greeting formalGreeting = new Greeting() {
            @Override
            public void greet(String name) {
                // Can access final and effectively final variables
                System.out.println(prefix + name + suffix);
                System.out.println("Counter: " + counter);
            }
        };

        formalGreeting.greet("Johnson");

        // Cannot modify effectively final variables after anonymous class uses them
        // suffix = " Jr.";  // Would cause compilation error
        // counter++;        // Would cause compilation error
    }

    // ===== COMMON USAGE PATTERNS =====

    public static void demonstrateCommonPatterns() {
        List<String> names = new ArrayList<>();
        names.add("Charlie");
        names.add("Alice");
        names.add("Bob");

        // Anonymous class for Comparator
        names.sort(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.length() - s2.length();  // Sort by length
            }
        });

        System.out.println("Sorted by length: " + names);

        // Anonymous class passed directly as argument
        names.sort(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s2.compareTo(s1);  // Reverse alphabetical
            }
        });

        System.out.println("Reverse alphabetical: " + names);
    }

    // ===== EXAM TRAPS =====

    interface Calculator {
        int calculate(int a, int b);
    }

    static class ExamTraps {
        public static void demonstrateTraps() {
            // VALID - implementing interface
            Calculator add = new Calculator() {
                @Override
                public int calculate(int a, int b) {
                    return a + b;
                }
            };

            System.out.println("5 + 3 = " + add.calculate(5, 3));

            // VALID - can have instance initializer
            Calculator multiply = new Calculator() {
                {
                    System.out.println("Multiply calculator initialized");
                }

                @Override
                public int calculate(int a, int b) {
                    return a * b;
                }
            };

            System.out.println("5 * 3 = " + multiply.calculate(5, 3));

            // INVALID - cannot define constructor (no name!)
            /*
            Calculator invalid = new Calculator() {
                public Calculator() {  // COMPILATION ERROR!
                    System.out.println("Constructor");
                }

                @Override
                public int calculate(int a, int b) {
                    return a + b;
                }
            };
            */

            // VALID - can define additional methods but cannot call them
            Animal special = new Animal("Special") {
                public void specialMethod() {
                    System.out.println("Special method");
                }

                @Override
                public void makeSound() {
                    specialMethod();  // Can call within anonymous class
                    System.out.println("Special sound");
                }
            };

            special.makeSound();
            // special.specialMethod();  // COMPILATION ERROR - not in Animal type
        }
    }

    // ===== DEMONSTRATION =====

    public static void main(String[] args) {
        System.out.println("=== IMPLEMENTING INTERFACE ===");
        demonstrateInterfaceImplementation();

        System.out.println("\n=== IMPLEMENTING RUNNABLE ===");
        demonstrateRunnable();

        System.out.println("\n=== EXTENDING CONCRETE CLASS ===");
        demonstrateExtendingClass();

        System.out.println("\n=== EXTENDING ABSTRACT CLASS ===");
        demonstrateExtendingAbstractClass();

        System.out.println("\n=== COMPLEX ABSTRACT CLASS ===");
        demonstrateComplexAbstractClass();

        System.out.println("\n=== VARIABLE ACCESS ===");
        demonstrateVariableAccess();

        System.out.println("\n=== COMMON PATTERNS ===");
        demonstrateCommonPatterns();

        System.out.println("\n=== EXAM TRAPS ===");
        ExamTraps.demonstrateTraps();

        System.out.println("\n=== KEY EXAM POINTS ===");
        System.out.println("✓ Anonymous classes have NO name");
        System.out.println("✓ Declared and instantiated in ONE statement");
        System.out.println("✓ Syntax: new TypeName(args) { body }");
        System.out.println("✓ MUST extend a class OR implement an interface");
        System.out.println("✗ CANNOT have explicit constructors (no name!)");
        System.out.println("✓ CAN have instance initializer blocks");
        System.out.println("✓ Can access final and effectively final variables");
        System.out.println("✓ Can define new methods but cannot call them externally");
        System.out.println("✓ Common with interfaces: Runnable, Comparator, etc.");
    }
}
