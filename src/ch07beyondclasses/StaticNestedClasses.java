package ch07beyondclasses;

/**
 * Static Nested Classes
 *
 * Key characteristics:
 * - Marked with static keyword and defined at member level
 * - CAN be instantiated without an instance of the enclosing class
 * - CANNOT access instance variables or methods of outer class
 * - CAN access static variables and methods of outer class (including private)
 * - Creates a namespace - must use enclosing class name to refer to it
 * - Can be marked private, protected, public, or package-private
 * - Enclosing class can access fields and methods of static nested class
 * - Can extend classes and implement interfaces
 * - Can be marked abstract or final
 */
public class StaticNestedClasses {

    // ===== BASIC STATIC NESTED CLASS =====

    static class Enclosing {
        private static String staticField = "Static field in Enclosing";
        private String instanceField = "Instance field in Enclosing";
        private static int counter = 0;

        // Public static nested class
        public static class PublicNested {
            public void display() {
                // CAN access static members of outer class
                System.out.println("Accessing: " + staticField);
                System.out.println("Counter: " + counter);

                // CANNOT access instance members (compilation error if uncommented)
                // System.out.println(instanceField);  // ERROR!
            }

            public void increment() {
                counter++;  // Can modify static fields of outer class
            }
        }

        // Private static nested class
        private static class PrivateNested {
            private String data = "Private nested data";

            void show() {
                System.out.println("Private nested class accessing: " + staticField);
            }
        }

        // Protected static nested class
        protected static class ProtectedNested {
            void display() {
                System.out.println("Protected nested class");
            }
        }

        // Package-private (default) static nested class
        static class PackageNested {
            void display() {
                System.out.println("Package nested class");
            }
        }

        // Outer class accessing static nested class members
        public void accessNestedClass() {
            PrivateNested pn = new PrivateNested();
            System.out.println("Outer accessing nested: " + pn.data);
            pn.show();
        }
    }

    // ===== NAMESPACE EXAMPLE =====

    static class Outer1 {
        static class Nested {
            static String name = "Outer1.Nested";
        }
    }

    static class Outer2 {
        static class Nested {
            static String name = "Outer2.Nested";
        }
    }

    // ===== EXTENDING AND IMPLEMENTING =====

    static class Company {
        private static String companyName = "TechCorp";

        static class Employee extends java.util.ArrayList<String> implements Comparable<Employee> {
            private String name;

            public Employee(String name) {
                this.name = name;
            }

            public void showCompany() {
                System.out.println(name + " works at " + companyName);
            }

            @Override
            public int compareTo(Employee other) {
                return this.name.compareTo(other.name);
            }
        }
    }

    // ===== ABSTRACT AND FINAL STATIC NESTED CLASSES =====

    static class Factory {
        // Abstract static nested class
        abstract static class AbstractProduct {
            abstract void manufacture();
        }

        // Concrete implementation
        static class ConcreteProduct extends AbstractProduct {
            @Override
            void manufacture() {
                System.out.println("Manufacturing product");
            }
        }

        // Final static nested class (cannot be extended)
        final static class FinalProduct {
            void produce() {
                System.out.println("Producing final product");
            }
        }
    }

    // ===== ACCESSING PRIVATE STATIC MEMBERS =====

    static class Database {
        private static String connectionString = "jdbc:mysql://localhost:3306/db";
        private static int maxConnections = 10;

        static class ConnectionPool {
            public void connect() {
                // Can access private static members of outer class
                System.out.println("Connecting to: " + connectionString);
                System.out.println("Max connections: " + maxConnections);
            }
        }
    }

    // ===== EXAM TRAPS =====

    static class ExamTrap {
        private int instanceVar = 5;
        private static int staticVar = 10;

        static class Nested {
            public void test() {
                // This works - accessing static member
                System.out.println("Static var: " + staticVar);

                // This would NOT compile - cannot access instance member
                // System.out.println(instanceVar);  // COMPILATION ERROR!

                // This also would NOT work - no outer instance reference
                // System.out.println(ExamTrap.this.instanceVar);  // ERROR!
            }
        }
    }

    // ===== DEMONSTRATION =====

    public static void main(String[] args) {
        System.out.println("=== INSTANTIATION WITHOUT OUTER INSTANCE ===");
        // No need for an instance of Enclosing
        Enclosing.PublicNested nested = new Enclosing.PublicNested();
        nested.display();
        nested.increment();
        nested.display();

        System.out.println("\n=== NAMESPACE DEMONSTRATION ===");
        // Must use enclosing class name
        System.out.println(Outer1.Nested.name);
        System.out.println(Outer2.Nested.name);
        // Creates separate namespaces for classes with same name

        System.out.println("\n=== OUTER ACCESSING NESTED MEMBERS ===");
        Enclosing enc = new Enclosing();
        enc.accessNestedClass();

        System.out.println("\n=== EXTENDING AND IMPLEMENTING ===");
        Company.Employee emp = new Company.Employee("Alice");
        emp.add("Skill: Java");
        emp.add("Skill: Python");
        emp.showCompany();

        System.out.println("\n=== ABSTRACT AND FINAL NESTED CLASSES ===");
        Factory.ConcreteProduct product = new Factory.ConcreteProduct();
        product.manufacture();

        Factory.FinalProduct finalProduct = new Factory.FinalProduct();
        finalProduct.produce();

        System.out.println("\n=== ACCESSING PRIVATE STATIC MEMBERS ===");
        Database.ConnectionPool pool = new Database.ConnectionPool();
        pool.connect();

        System.out.println("\n=== EXAM TRAPS ===");
        ExamTrap.Nested trapNested = new ExamTrap.Nested();
        trapNested.test();

        System.out.println("\n=== KEY EXAM POINTS ===");
        System.out.println("✓ Static nested class does NOT need outer instance");
        System.out.println("✓ CAN access static members of outer (including private)");
        System.out.println("✗ CANNOT access instance members of outer");
        System.out.println("✓ Syntax: OuterClass.NestedClass obj = new OuterClass.NestedClass();");
        System.out.println("✓ Creates namespace - prevents naming conflicts");
        System.out.println("✓ Outer class CAN access private members of nested class");
        System.out.println("✓ Can have any access modifier (public/protected/package/private)");
    }
}
