package ch07beyondclasses;

/**
 * Polymorphism
 *
 * Key Principles:
 * - Polymorphism is the property of an object to take on many forms
 * - A Java object may be accessed using:
 *   1. A reference with the same type as the object
 *   2. A reference that is a superclass of the object
 *   3. A reference that defines an interface the object implements or inherits
 *
 * - A cast is NOT required when assigning to a supertype or interface
 * - Any object can be assigned to an Object reference
 * - Changing reference type changes which methods/fields are accessible
 * - Can use casting to access methods/fields not available on reference type
 *
 * CRITICAL DISTINCTION:
 * - Type of OBJECT determines which properties exist in memory
 * - Type of REFERENCE determines which methods/variables are accessible to the program
 */
public class Polymorphism {

    // ===== CLASS HIERARCHY =====

    interface Swimmable {
        void swim();
    }

    interface Flyable {
        void fly();
    }

    static class Animal {
        protected String name;

        public Animal(String name) {
            this.name = name;
        }

        public void eat() {
            System.out.println(name + " is eating");
        }

        public void sleep() {
            System.out.println(name + " is sleeping");
        }
    }

    static class Mammal extends Animal {
        private int gestationPeriod;

        public Mammal(String name, int gestationPeriod) {
            super(name);
            this.gestationPeriod = gestationPeriod;
        }

        public void nurse() {
            System.out.println(name + " is nursing young");
        }

        public void regulateTemperature() {
            System.out.println(name + " maintains constant body temperature");
        }
    }

    static class Dog extends Mammal implements Swimmable {
        private String breed;

        public Dog(String name, String breed) {
            super(name, 63);  // 63 days gestation
            this.breed = breed;
        }

        public void bark() {
            System.out.println(name + " says: Woof! Woof!");
        }

        @Override
        public void swim() {
            System.out.println(name + " is swimming (dog paddle)");
        }

        public String getBreed() {
            return breed;
        }

        @Override
        public void eat() {
            System.out.println(name + " is eating dog food");
        }
    }

    static class Duck extends Animal implements Swimmable, Flyable {
        public Duck(String name) {
            super(name);
        }

        public void quack() {
            System.out.println(name + " says: Quack! Quack!");
        }

        @Override
        public void swim() {
            System.out.println(name + " is swimming gracefully");
        }

        @Override
        public void fly() {
            System.out.println(name + " is flying south");
        }
    }

    // ===== ONE OBJECT, MANY REFERENCE TYPES =====

    public static void demonstratePolymorphism() {
        System.out.println("=== CREATING ONE DOG OBJECT ===");

        // Creating ONE object in memory
        Dog actualDog = new Dog("Buddy", "Golden Retriever");

        System.out.println("\n=== SAME OBJECT, DIFFERENT REFERENCES ===");

        // Reference 1: Same type as object
        Dog dogRef = actualDog;
        System.out.println("\n--- Dog reference ---");
        dogRef.bark();              // Dog method
        dogRef.swim();              // Swimmable method
        dogRef.nurse();             // Mammal method
        dogRef.eat();               // Animal method (overridden)
        System.out.println("Breed: " + dogRef.getBreed());

        // Reference 2: Superclass (Mammal) reference - NO CAST NEEDED
        Mammal mammalRef = actualDog;  // Automatic upcast
        System.out.println("\n--- Mammal reference ---");
        mammalRef.nurse();          // Mammal method - OK
        mammalRef.eat();            // Animal method - OK (still calls Dog's version!)
        mammalRef.regulateTemperature();  // Mammal method - OK
        // mammalRef.bark();        // COMPILATION ERROR - not in Mammal
        // mammalRef.swim();        // COMPILATION ERROR - not in Mammal
        // mammalRef.getBreed();    // COMPILATION ERROR - not in Mammal

        // Reference 3: Superclass (Animal) reference - NO CAST NEEDED
        Animal animalRef = actualDog;  // Automatic upcast
        System.out.println("\n--- Animal reference ---");
        animalRef.eat();            // Animal method - OK (still calls Dog's version!)
        animalRef.sleep();          // Animal method - OK
        // animalRef.nurse();       // COMPILATION ERROR - not in Animal
        // animalRef.bark();        // COMPILATION ERROR - not in Animal
        // animalRef.swim();        // COMPILATION ERROR - not in Animal

        // Reference 4: Interface (Swimmable) reference - NO CAST NEEDED
        Swimmable swimmableRef = actualDog;  // Automatic upcast
        System.out.println("\n--- Swimmable interface reference ---");
        swimmableRef.swim();        // Swimmable method - OK
        // swimmableRef.eat();      // COMPILATION ERROR - not in Swimmable
        // swimmableRef.bark();     // COMPILATION ERROR - not in Swimmable

        // Reference 5: Object reference - ANY OBJECT CAN BE ASSIGNED
        Object objectRef = actualDog;  // Automatic upcast
        System.out.println("\n--- Object reference ---");
        System.out.println("toString: " + objectRef.toString());
        System.out.println("hashCode: " + objectRef.hashCode());
        // objectRef.eat();         // COMPILATION ERROR - not in Object
        // objectRef.bark();        // COMPILATION ERROR - not in Object

        System.out.println("\n=== KEY POINT ===");
        System.out.println("All 5 references point to the SAME object in memory!");
        System.out.println("dogRef == mammalRef: " + (dogRef == mammalRef));
        System.out.println("mammalRef == animalRef: " + (mammalRef == animalRef));
        System.out.println("animalRef == objectRef: " + (animalRef == objectRef));
    }

    // ===== ACCESSING METHODS WITH CASTING =====

    public static void demonstrateCasting() {
        System.out.println("=== CASTING TO ACCESS METHODS ===");

        Dog dog = new Dog("Max", "Labrador");

        // Assign to Animal reference
        Animal animal = dog;

        System.out.println("\n--- Without cast ---");
        animal.eat();
        // animal.bark();  // COMPILATION ERROR

        System.out.println("\n--- With cast ---");
        ((Dog) animal).bark();  // Cast allows access to Dog methods
        ((Dog) animal).swim();
        System.out.println("Breed: " + ((Dog) animal).getBreed());

        // Can also cast to intermediate types
        ((Mammal) animal).nurse();

        // Can cast to interface
        ((Swimmable) animal).swim();
    }

    // ===== CASTING RULES WITH OBJECTS =====

    public static void demonstrateCastingRules() {
        System.out.println("=== CASTING RULES ===");

        /*
         * CASTING RULES:
         *
         * 1. IMPLICIT CAST (Subtype to Supertype) - NO explicit cast needed
         *    - Also called "upcasting" or "widening"
         *    - Going from specific type to more general type
         *    - Always safe - compiler allows it automatically
         *    - Examples: Dog -> Mammal -> Animal -> Object
         *
         * 2. EXPLICIT CAST (Supertype to Subtype) - REQUIRES explicit cast
         *    - Also called "downcasting" or "narrowing"
         *    - Going from general type to more specific type
         *    - May fail at runtime - must be explicitly requested
         *    - Syntax: (TargetType) reference
         *
         * 3. RUNTIME VALIDATION
         *    - Invalid cast throws ClassCastException at runtime
         *    - Use instanceof to check before casting
         *
         * 4. COMPILER CHECKS
         *    - Compiler disallows casts to completely unrelated types
         *    - Example: Cannot cast Dog to Duck (no inheritance relationship)
         */

        System.out.println("\n--- 1. IMPLICIT CAST: Subtype to Supertype (NO explicit cast) ---");
        Dog dog = new Dog("Buddy", "Golden Retriever");

        // Implicit cast from Dog to Mammal (subtype to supertype)
        Mammal mammal = dog;  // No cast needed - automatic
        System.out.println("Dog -> Mammal: " + mammal.name);

        // Implicit cast from Dog to Animal (subtype to supertype)
        Animal animal = dog;  // No cast needed - automatic
        System.out.println("Dog -> Animal: " + animal.name);

        // Implicit cast from Dog to Object (subtype to supertype)
        Object obj = dog;  // No cast needed - automatic
        System.out.println("Dog -> Object: " + obj.toString());

        // Implicit cast to interface
        Swimmable swimmer = dog;  // No cast needed - automatic
        System.out.println("Dog -> Swimmable: OK");

        System.out.println("\n--- 2. EXPLICIT CAST: Supertype to Subtype (REQUIRES explicit cast) ---");

        // Create a Dog and store it in Animal reference
        Animal animalRef = new Dog("Max", "Poodle");

        // EXPLICIT cast required from Animal to Dog (supertype to subtype)
        Dog dogRef = (Dog) animalRef;  // Explicit cast required
        System.out.println("Animal -> Dog (explicit): " + dogRef.getBreed());
        dogRef.bark();

        // Can also cast from Object back down
        Object objectRef = new Dog("Charlie", "Beagle");
        Dog anotherDog = (Dog) objectRef;  // Explicit cast required
        System.out.println("Object -> Dog (explicit): " + anotherDog.getBreed());

        System.out.println("\n--- 3. RUNTIME VALIDATION: ClassCastException ---");

        // This compiles but FAILS at runtime
        Animal actualDuck = new Duck("Daffy");

        try {
            // Compiler allows this (Duck and Dog are both Animals)
            // But runtime knows actualDuck is a Duck, not a Dog!
            Dog impossibleDog = (Dog) actualDuck;  // ClassCastException!
            impossibleDog.bark();
        } catch (ClassCastException e) {
            System.out.println("ERROR: Cannot cast Duck to Dog - " + e.getMessage());
        }

        // SAFE WAY: Use instanceof before casting
        if (actualDuck instanceof Dog) {
            Dog safeDog = (Dog) actualDuck;
            safeDog.bark();
        } else {
            System.out.println("actualDuck is NOT a Dog, it's a " + actualDuck.getClass().getSimpleName());
        }

        System.out.println("\n--- 4. COMPILER CHECKS: Disallows Unrelated Types ---");

        // These will NOT compile - compiler knows they're unrelated
        Dog myDog = new Dog("Rover", "Lab");

        // COMPILATION ERROR: Dog and Duck are unrelated (both extend Animal, but no inheritance)
        // Duck impossibleDuck = (Duck) myDog;  // ERROR!

        // COMPILATION ERROR: Dog and String are completely unrelated
        // String impossibleString = (String) myDog;  // ERROR!

        System.out.println("Compiler prevents casts to unrelated types");

        System.out.println("\n--- PRACTICAL EXAMPLE: Polymorphic Collection ---");

        Animal[] animals = new Animal[3];
        animals[0] = new Dog("Fido", "Terrier");
        animals[1] = new Duck("Donald");
        animals[2] = new Mammal("Whale", 365);

        for (Animal a : animals) {
            System.out.println("\nProcessing: " + a.name);

            // Check type before casting (SAFE)
            if (a instanceof Dog) {
                Dog d = (Dog) a;  // Explicit cast needed
                d.bark();
                System.out.println("Breed: " + d.getBreed());
            } else if (a instanceof Duck) {
                Duck duck = (Duck) a;  // Explicit cast needed
                duck.quack();
            } else if (a instanceof Mammal) {
                Mammal m = (Mammal) a;  // Explicit cast needed
                m.nurse();
            }
        }

        System.out.println("\n--- KEY RULES SUMMARY ---");
        System.out.println("Subtype -> Supertype: IMPLICIT (automatic, no cast)");
        System.out.println("Supertype -> Subtype: EXPLICIT (requires (Type) cast)");
        System.out.println("Invalid cast at runtime: ClassCastException");
        System.out.println("Unrelated types: Compiler error (won't compile)");
        System.out.println("Best practice: Use instanceof before casting");
    }

    // ===== OBJECT TYPE VS REFERENCE TYPE =====

    public static void demonstrateObjectVsReference() {
        System.out.println("=== OBJECT TYPE VS REFERENCE TYPE ===");

        // Create a Dog object
        Dog dog = new Dog("Charlie", "Beagle");

        System.out.println("\n--- Object Type (what exists in memory) ---");
        System.out.println("The object in memory is a Dog");
        System.out.println("It has all Dog properties: name, breed, gestationPeriod");
        System.out.println("It has all Dog methods: bark(), swim(), nurse(), eat(), etc.");
        System.out.println("Actual class: " + dog.getClass().getName());

        // Assign to Animal reference
        Animal animalRef = dog;

        System.out.println("\n--- Reference Type (what's accessible) ---");
        System.out.println("Reference type: Animal");
        System.out.println("Can access: eat(), sleep() [Animal methods]");
        System.out.println("Cannot access: bark(), swim(), nurse(), getBreed()");
        System.out.println("But the object is STILL a Dog!");
        System.out.println("Actual class via reference: " + animalRef.getClass().getName());

        System.out.println("\n--- Method Calls Use Object Type ---");
        animalRef.eat();  // Calls Dog's eat(), not Animal's eat()
    }

    // ===== POLYMORPHISM WITH ARRAYS =====

    public static void demonstratePolymorphicArray() {
        System.out.println("=== POLYMORPHIC ARRAY ===");

        // Array of Animal references holding different object types
        Animal[] animals = new Animal[3];
        animals[0] = new Dog("Buddy", "Poodle");
        animals[1] = new Duck("Donald");
        animals[2] = new Mammal("Whale", 365);

        System.out.println("\n--- Calling methods on polymorphic array ---");
        for (Animal animal : animals) {
            System.out.println("\nAnimal: " + animal.name);
            animal.eat();  // Each calls their own version

            // Check actual type and cast if needed
            if (animal instanceof Dog) {
                ((Dog) animal).bark();
            } else if (animal instanceof Duck) {
                ((Duck) animal).quack();
            } else if (animal instanceof Mammal) {
                ((Mammal) animal).nurse();
            }
        }
    }

    // ===== POLYMORPHISM WITH INTERFACES =====

    public static void demonstrateInterfacePolymorphism() {
        System.out.println("=== INTERFACE POLYMORPHISM ===");

        // One Dog object, multiple interface references
        Dog dog = new Dog("Rover", "Retriever");

        System.out.println("\n--- Swimmable interface reference ---");
        Swimmable swimmer = dog;  // NO CAST NEEDED
        swimmer.swim();
        // swimmer.bark();  // ERROR - not in Swimmable

        System.out.println("\n--- But it's still a Dog! ---");
        System.out.println("Actual type: " + swimmer.getClass().getName());
        ((Dog) swimmer).bark();  // Cast to access Dog methods

        System.out.println("\n--- Array of Swimmables ---");
        Swimmable[] swimmers = new Swimmable[2];
        swimmers[0] = new Dog("Swimmer1", "Lab");
        swimmers[1] = new Duck("Swimmer2");

        for (Swimmable s : swimmers) {
            s.swim();  // Each has their own implementation
        }
    }

    // ===== EXAM TRAPS =====

    public static void demonstrateExamTraps() {
        System.out.println("=== EXAM TRAPS ===");

        Dog dog = new Dog("Trap", "Collie");

        System.out.println("\n--- Trap 1: Reference type determines accessibility ---");
        Animal animal = dog;
        // String breed = animal.getBreed();  // COMPILATION ERROR
        String breed = ((Dog) animal).getBreed();  // Need cast
        System.out.println("Breed: " + breed);

        System.out.println("\n--- Trap 2: Object type determines method called ---");
        Animal a = new Dog("Override", "Terrier");
        a.eat();  // Calls Dog's eat(), not Animal's eat()

        System.out.println("\n--- Trap 3: All objects can be Object reference ---");
        Object obj = dog;
        // obj.eat();  // COMPILATION ERROR - not in Object class
        ((Animal) obj).eat();  // Cast to access

        System.out.println("\n--- Trap 4: Interface references ---");
        Swimmable s = new Dog("Doggy", "Spaniel");
        // s.eat();  // COMPILATION ERROR - not in Swimmable
        ((Dog) s).eat();  // Cast to Dog
        ((Animal) s).eat();  // Or cast to Animal
    }

    // ===== DEMONSTRATION =====

    public static void main(String[] args) {
        demonstratePolymorphism();

        System.out.println("\n" + "=".repeat(60) + "\n");
        demonstrateCasting();

        System.out.println("\n" + "=".repeat(60) + "\n");
        demonstrateCastingRules();

        System.out.println("\n" + "=".repeat(60) + "\n");
        demonstrateObjectVsReference();

        System.out.println("\n" + "=".repeat(60) + "\n");
        demonstratePolymorphicArray();

        System.out.println("\n" + "=".repeat(60) + "\n");
        demonstrateInterfacePolymorphism();

        System.out.println("\n" + "=".repeat(60) + "\n");
        demonstrateExamTraps();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("KEY EXAM POINTS");
        System.out.println("=".repeat(60));
        System.out.println("✓ Polymorphism = object taking many forms");
        System.out.println("✓ Object can be referenced as: same type, superclass, interface");
        System.out.println("✓ NO cast needed when assigning to supertype/interface");
        System.out.println("✓ ANY object can be assigned to Object reference");
        System.out.println("✓ OBJECT type = what exists in memory (all properties)");
        System.out.println("✓ REFERENCE type = what's accessible in code");
        System.out.println("✓ Cast to access methods not on reference type");
        System.out.println("✓ Method calls use OBJECT's implementation (not reference)");
        System.out.println("\n--- CASTING RULES ---");
        System.out.println("✓ Subtype to Supertype: IMPLICIT cast (automatic)");
        System.out.println("✓ Supertype to Subtype: EXPLICIT cast required");
        System.out.println("✗ Invalid cast at runtime: ClassCastException thrown");
        System.out.println("✗ Unrelated types: Compiler error (won't compile)");
        System.out.println("✓ Use instanceof before casting to avoid exceptions");
    }
}
