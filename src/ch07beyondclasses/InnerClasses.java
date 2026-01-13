package ch07beyondclasses;

/**
 * Inner Classes - Non-static nested classes
 *
 * Key characteristics:
 * - Can use any access modifier (public, protected, package, private)
 * - Can extend a class and implement interfaces
 * - Can be marked abstract or final
 * - Can access members of the outer class, including private members
 * - NOT static - requires an instance of the outer class
 * - Two objects need to be created (outer and inner)
 */
public class InnerClasses {

    // ===== BASIC INNER CLASS EXAMPLE =====

    static class Outer {
        private String outerField = "Outer field";
        private static String staticField = "Static field";

        // Inner class with different access modifiers
        public class PublicInner {
            public void display() {
                System.out.println("Public inner class");
                System.out.println("Accessing outer private field: " + outerField);
            }
        }

        protected class ProtectedInner {
            public void display() {
                System.out.println("Protected inner class");
            }
        }

        class PackageInner {  // package-private (default)
            public void display() {
                System.out.println("Package inner class");
            }
        }

        private class PrivateInner {
            public void display() {
                System.out.println("Private inner class");
                System.out.println("Can access: " + outerField);
            }
        }

        // Method to create private inner class (since it's private)
        public void createPrivateInner() {
            PrivateInner pi = new PrivateInner();
            pi.display();
        }
    }

    // ===== INNER CLASS EXTENDING AND IMPLEMENTING =====

    static class Vehicle {
        private String brand = "Generic";

        // Inner class extending another class
        class Car extends java.util.ArrayList<String> {
            public void showBrand() {
                System.out.println("Brand: " + brand);
            }
        }

        // Inner class implementing an interface
        class Engine implements Runnable {
            @Override
            public void run() {
                System.out.println("Engine running for " + brand);
            }
        }
    }

    // ===== ABSTRACT AND FINAL INNER CLASSES =====

    static class Container {
        // Abstract inner class
        abstract class AbstractInner {
            abstract void process();
        }

        // Concrete implementation of abstract inner class
        class ConcreteInner extends AbstractInner {
            @Override
            void process() {
                System.out.println("Processing in concrete inner class");
            }
        }

        // Final inner class (cannot be extended)
        final class FinalInner {
            void display() {
                System.out.println("Final inner class");
            }
        }
    }

    // ===== SPECIAL INSTANTIATION SYNTAX =====

    static class Home {
        private String address = "123 Main St";

        class Room {
            private String type = "Bedroom";

            void enter() {
                System.out.println("Entering " + type + " at " + address);
            }
        }
    }

    // ===== SCOPE AND THIS KEYWORD =====
    // Example showing how to access variables from different scopes

    static class A {
        private int x = 10;

        class B {
            private int x = 20;

            class C {
                private int x = 30;

                public void printAllX() {
                    System.out.println("x in C: " + x);           // 30 - local to C
                    System.out.println("x in C: " + this.x);      // 30 - same as above
                    System.out.println("x in B: " + B.this.x);    // 20 - from B
                    System.out.println("x in A: " + A.this.x);    // 10 - from A
                }
            }
        }
    }

    // ===== DEMONSTRATION =====

    public static void main(String[] args) {
        System.out.println("=== BASIC INNER CLASS ACCESS ===");
        Outer outer = new Outer();
        Outer.PublicInner publicInner = outer.new PublicInner();
        publicInner.display();

        System.out.println("\n=== CREATING PRIVATE INNER CLASS ===");
        outer.createPrivateInner();

        System.out.println("\n=== SPECIAL INSTANTIATION SYNTAX ===");
        // Creating inner class instance in one line
        new Home().new Room().enter();

        // Breaking it down step by step
        Home home = new Home();
        Home.Room room = home.new Room();
        room.enter();

        System.out.println("\n=== INNER CLASS EXTENDING/IMPLEMENTING ===");
        Vehicle vehicle = new Vehicle();
        Vehicle.Car car = vehicle.new Car();
        car.add("Feature1");
        car.showBrand();

        Vehicle.Engine engine = vehicle.new Engine();
        engine.run();

        System.out.println("\n=== ABSTRACT AND FINAL INNER CLASSES ===");
        Container container = new Container();
        Container.ConcreteInner concrete = container.new ConcreteInner();
        concrete.process();

        Container.FinalInner finalInner = container.new FinalInner();
        finalInner.display();

        System.out.println("\n=== SCOPE AND THIS KEYWORD ===");
        A a = new A();
        A.B b = a.new B();
        A.B.C c = b.new C();
        c.printAllX();

        System.out.println("\n=== KEY POINTS ===");
        System.out.println("1. Inner classes are NOT static");
        System.out.println("2. Require an instance of outer class to instantiate");
        System.out.println("3. Can access private members of outer class");
        System.out.println("4. Use ClassName.this.member to specify scope");
        System.out.println("5. Syntax: outerInstance.new InnerClass()");
    }
}
