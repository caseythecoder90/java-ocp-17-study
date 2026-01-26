package ch14io;

import java.io.*;

/**
 * SERIALIZATION - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * WHAT IS SERIALIZATION?
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * SERIALIZATION: Process of converting an object into a stream of bytes
 *                so it can be saved to a file or transmitted over a network.
 *
 * DESERIALIZATION: Process of converting a stream of bytes back into an object.
 *
 * Use cases:
 * - Saving object state to disk
 * - Sending objects over a network
 * - Deep copying objects
 * - Caching
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * SERIALIZABLE INTERFACE - Marker Interface
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * To serialize an object using the I/O API, the class must implement:
 *   java.io.Serializable
 *
 * Serializable is a MARKER INTERFACE (no methods to implement).
 * It simply marks a class as "safe to serialize".
 *
 * Example:
 *   public class Person implements Serializable {
 *       private String name;
 *       private int age;
 *   }
 *
 * EXAM TIP: If a class doesn't implement Serializable, attempting to serialize
 *           it throws NotSerializableException at RUNTIME.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * TRANSIENT KEYWORD
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Fields marked with 'transient' are NOT serialized.
 *
 * When deserialized, transient fields revert to their DEFAULT Java values:
 * - Objects: null
 * - int: 0
 * - boolean: false
 * - double: 0.0
 * - etc.
 *
 * Example:
 *   public class User implements Serializable {
 *       private String username;           // Serialized
 *       private transient String password; // NOT serialized (security!)
 *   }
 *
 * Common uses:
 * - Sensitive data (passwords, credit cards)
 * - Derived/calculated fields
 * - Cached data that can be recomputed
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * serialVersionUID
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * private static final long serialVersionUID = 1L;
 *
 * Purpose:
 * - Unique identifier for a Serializable class
 * - Used during deserialization to verify sender and receiver are compatible
 * - If versions don't match: InvalidClassException
 *
 * Best practice:
 * - Always declare serialVersionUID explicitly
 * - If not declared, Java generates one automatically (based on class structure)
 * - Any change to class structure changes auto-generated ID
 *
 * EXAM TIP: Not required, but recommended. If missing, Java auto-generates.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * RULES FOR ENSURING A CLASS IS SERIALIZABLE
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * For a class to be properly serializable:
 *
 * 1. The class MUST be marked Serializable (implements Serializable)
 *
 * 2. EVERY instance member of the class must be one of:
 *    a) Serializable (implements Serializable)
 *    b) Marked transient
 *    c) Have a null value at time of serialization
 *
 * 3. The second rule must be applied RECURSIVELY (all the way down)
 *
 * Example - INVALID:
 *   public class Employee implements Serializable {
 *       private Address address;  // ✗ Address must also be Serializable!
 *   }
 *
 * Example - VALID:
 *   public class Employee implements Serializable {
 *       private Address address;  // ✓ Address implements Serializable
 *   }
 *   public class Address implements Serializable {
 *       private String street;
 *   }
 *
 * Example - VALID (transient):
 *   public class Employee implements Serializable {
 *       private transient Address address;  // ✓ Transient, so not serialized
 *   }
 *
 * EXAM TRAP: If any non-transient instance member is not Serializable,
 *            you get NotSerializableException at RUNTIME!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * ObjectOutputStream AND ObjectInputStream
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * ObjectOutputStream: Used to SERIALIZE objects
 *   void writeObject(Object obj) throws IOException
 *   - Writes object to the stream
 *   - Throws NotSerializableException if object not Serializable
 *
 * ObjectInputStream: Used to DESERIALIZE objects
 *   Object readObject() throws IOException, ClassNotFoundException
 *   - Reads object from the stream
 *   - Returns Object (must cast to correct type)
 *   - Throws ClassNotFoundException if class not found
 *   - Throws EOFException when end of stream reached
 *
 * CRITICAL SIGNATURES FOR EXAM:
 *
 * void writeObject(Object obj) throws IOException
 *   - Parameter: Object obj
 *   - Return type: void
 *   - Exceptions: IOException
 *
 * Object readObject() throws IOException, ClassNotFoundException
 *   - Parameters: none
 *   - Return type: Object (must cast!)
 *   - Exceptions: IOException, ClassNotFoundException
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * READING MULTIPLE OBJECTS - EOFException Pattern
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * IMPORTANT: When calling readObject(), null and -1 do NOT have special meaning!
 * (Unlike read() which returns -1 at EOF)
 *
 * readObject() throws EOFException when end of stream is reached.
 *
 * Pattern: Use infinite loop with try-catch for EOFException
 *
 * while (true) {
 *     try {
 *         Object obj = ois.readObject();
 *         // Process obj
 *     } catch (EOFException e) {
 *         break;  // End of stream
 *     }
 * }
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CRITICAL EXAM TOPIC - HOW DESERIALIZED OBJECTS ARE CREATED
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * This is VERY important and comes up frequently on the exam!
 *
 * When deserializing an object:
 *
 * 1. The CONSTRUCTOR of the serialized class is NOT called
 * 2. Instance initializers are NOT called
 * 3. Java calls the no-arg constructor of the FIRST non-serializable parent class
 *    (Often this is Object's no-arg constructor)
 * 4. Static fields are ignored (not serialized)
 * 5. Transient fields are ignored (not serialized)
 * 6. Values not provided will be given their default Java values
 *
 * HOW DOES JAVA RESTORE THE DATA?
 * - Java uses REFLECTION and internal mechanisms
 * - Bypasses constructors entirely
 * - Directly sets field values in the object
 * - Uses unsafe memory operations (bypassing normal object creation)
 *
 * This is why transient/default values work:
 * - Java creates "empty" object (via parent constructor or Object)
 * - Then directly populates serialized fields using reflection
 * - Transient fields remain at default values (never set)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * TRICKY EXAM TOPIC - STATIC FIELDS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Static fields are NOT serialized (belong to class, not instance).
 *
 * During deserialization, static fields have whatever value was set LAST.
 *
 * Two scenarios:
 *
 * 1. Serialize and deserialize in SAME execution:
 *    - Static field keeps last value set in current JVM
 *    - Appears to be "serialized" but it's not - just current JVM state
 *
 * 2. Serialize in one execution, deserialize in ANOTHER:
 *    - Static field has whatever value is set when class is loaded
 *    - Often the initial value from class declaration
 *
 * EXAM TRAP: Static fields APPEAR to serialize in same execution, but don't!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class Serialization {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // ────────────────────────────────────────────────────────────────────
        // Basic serialization and deserialization
        // ────────────────────────────────────────────────────────────────────
        demonstrateBasicSerialization();

        // ────────────────────────────────────────────────────────────────────
        // Transient fields - not serialized, revert to defaults
        // ────────────────────────────────────────────────────────────────────
        demonstrateTransientFields();

        // ────────────────────────────────────────────────────────────────────
        // Reading multiple objects with EOFException
        // ────────────────────────────────────────────────────────────────────
        demonstrateMultipleObjects();

        // ────────────────────────────────────────────────────────────────────
        // CRITICAL: Constructor not called during deserialization
        // ────────────────────────────────────────────────────────────────────
        demonstrateConstructorNotCalled();

        // ────────────────────────────────────────────────────────────────────
        // How Java actually restores data (bypassing constructor)
        // ────────────────────────────────────────────────────────────────────
        demonstrateDataRestoration();

        // ────────────────────────────────────────────────────────────────────
        // TRICKY: Static field behavior
        // ────────────────────────────────────────────────────────────────────
        demonstrateStaticFieldBehavior();

        // ────────────────────────────────────────────────────────────────────
        // Inheritance with non-serializable parent
        // ────────────────────────────────────────────────────────────────────
        demonstrateInheritance();

        // ────────────────────────────────────────────────────────────────────
        // NotSerializableException example
        // ────────────────────────────────────────────────────────────────────
        demonstrateNotSerializableException();

        System.out.println("\n✓ All serialization examples completed");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Basic Serialization and Deserialization
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateBasicSerialization() throws IOException, ClassNotFoundException {
        System.out.println("=== Basic Serialization ===\n");

        String file = "person.ser";

        // Create and serialize object
        Person person = new Person("Alice", 30);
        System.out.println("Original object: " + person);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(person);
            System.out.println("Object serialized to " + file);
        }

        // Deserialize object
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Person deserializedPerson = (Person) ois.readObject();
            System.out.println("Deserialized object: " + deserializedPerson);
        }

        new File(file).delete();
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Transient Fields
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateTransientFields() throws IOException, ClassNotFoundException {
        System.out.println("=== Transient Fields ===\n");

        String file = "user.ser";

        // Create object with transient field
        User user = new User("john_doe", "secret123", 42);
        System.out.println("Original object:");
        System.out.println("  Username: " + user.username);
        System.out.println("  Password: " + user.password + " (transient)");
        System.out.println("  Login count: " + user.loginCount);

        // Serialize
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(user);
        }

        // Deserialize
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            User deserializedUser = (User) ois.readObject();
            System.out.println("\nDeserialized object:");
            System.out.println("  Username: " + deserializedUser.username);
            System.out.println("  Password: " + deserializedUser.password + " (null - was transient!)");
            System.out.println("  Login count: " + deserializedUser.loginCount + " (0 - was transient!)");
        }

        System.out.println("\nKey point: Transient fields revert to default values:");
        System.out.println("  - Objects → null");
        System.out.println("  - int → 0");
        System.out.println("  - boolean → false");
        System.out.println("  - etc.");

        new File(file).delete();
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Reading Multiple Objects with EOFException
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateMultipleObjects() throws IOException, ClassNotFoundException {
        System.out.println("=== Reading Multiple Objects (EOFException Pattern) ===\n");

        String file = "multiple.ser";

        // Write multiple objects
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(new Person("Alice", 30));
            oos.writeObject(new Person("Bob", 25));
            oos.writeObject(new Person("Charlie", 35));
            System.out.println("Wrote 3 objects to file");
        }

        // Read multiple objects using EOFException pattern
        System.out.println("\nReading objects:");
        System.out.println("IMPORTANT: readObject() does NOT return null or -1 at EOF!");
        System.out.println("           It throws EOFException\n");

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            int count = 1;
            while (true) {  // Infinite loop!
                try {
                    Person p = (Person) ois.readObject();
                    System.out.println("Object " + count++ + ": " + p);
                } catch (EOFException e) {
                    System.out.println("\nEOFException caught - end of stream");
                    break;
                }
            }
        }

        new File(file).delete();
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CRITICAL: Constructor Not Called During Deserialization
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateConstructorNotCalled() throws IOException, ClassNotFoundException {
        System.out.println("=== Constructor NOT Called During Deserialization ===\n");

        String file = "constructor-test.ser";

        System.out.println("Creating object (constructor will print message):");
        ConstructorTest obj = new ConstructorTest("Test", 100);

        System.out.println("\nSerializing object...");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(obj);
        }

        System.out.println("\nDeserializing object (constructor will NOT be called!):");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            ConstructorTest deserialized = (ConstructorTest) ois.readObject();
            System.out.println("Deserialized: " + deserialized);
        }

        System.out.println("\nObservation:");
        System.out.println("  - Constructor message appeared during creation");
        System.out.println("  - Constructor message did NOT appear during deserialization");
        System.out.println("  - Java restored the object WITHOUT calling the constructor!");

        new File(file).delete();
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // How Java Actually Restores Data
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateDataRestoration() throws IOException, ClassNotFoundException {
        System.out.println("=== How Java Restores Data (Bypassing Constructor) ===\n");

        String file = "restoration.ser";

        // Create object with specific values
        DataRestorationExample original = new DataRestorationExample();
        original.name = "Alice";
        original.age = 30;
        original.email = "alice@example.com";
        System.out.println("Original: " + original);

        // Serialize
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(original);
        }

        // Deserialize
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            DataRestorationExample restored = (DataRestorationExample) ois.readObject();
            System.out.println("Restored: " + restored);
        }

        System.out.println("\nHow did Java restore the data?");
        System.out.println("1. Java called Object's no-arg constructor (or first non-serializable parent)");
        System.out.println("2. This creates an 'empty' object with default values");
        System.out.println("3. Java uses REFLECTION to directly set field values");
        System.out.println("4. Bypasses normal field initialization and constructors");
        System.out.println("5. Directly populates serialized fields from byte stream");
        System.out.println("6. Transient fields remain at default values");

        System.out.println("\nThis is why:");
        System.out.println("  - Constructor logic is NOT executed");
        System.out.println("  - Instance initializers are NOT executed");
        System.out.println("  - Transient fields have default values");
        System.out.println("  - But serialized fields have correct values!");

        new File(file).delete();
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TRICKY: Static Field Behavior
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateStaticFieldBehavior() throws IOException, ClassNotFoundException {
        System.out.println("=== TRICKY: Static Field Behavior ===\n");

        String file = "static-test.ser";

        // Set static field
        StaticFieldExample.companyName = "TechCorp";
        System.out.println("Static field before serialization: " + StaticFieldExample.companyName);

        // Create and serialize object
        StaticFieldExample obj1 = new StaticFieldExample("Alice", "Engineer");
        System.out.println("Object: " + obj1);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(obj1);
            System.out.println("\nSerialized object");
        }

        // Change static field AFTER serialization
        StaticFieldExample.companyName = "NewCorp";
        System.out.println("\nChanged static field to: " + StaticFieldExample.companyName);

        // Deserialize
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            StaticFieldExample obj2 = (StaticFieldExample) ois.readObject();
            System.out.println("\nDeserialized object: " + obj2);
            System.out.println("Static field after deserialization: " + StaticFieldExample.companyName);
        }

        System.out.println("\nEXAM TRAP:");
        System.out.println("  - Static field shows 'NewCorp', NOT 'TechCorp'");
        System.out.println("  - Static fields are NOT serialized");
        System.out.println("  - During deserialization, static field has current JVM value");
        System.out.println("  - In same execution: appears to keep last set value");
        System.out.println("  - In different execution: has initial/loaded value");

        new File(file).delete();
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Inheritance with Non-Serializable Parent
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateInheritance() throws IOException, ClassNotFoundException {
        System.out.println("=== Inheritance with Non-Serializable Parent ===\n");

        String file = "inheritance.ser";

        // Create object
        Cat cat = new Cat("Whiskers", "Meow", 3);
        System.out.println("Original: " + cat);

        // Serialize
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(cat);
        }

        // Deserialize
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Cat deserializedCat = (Cat) ois.readObject();
            System.out.println("Deserialized: " + deserializedCat);
        }

        System.out.println("\nKey observations:");
        System.out.println("  - Animal (parent) is NOT Serializable");
        System.out.println("  - Cat (child) IS Serializable");
        System.out.println("  - During deserialization:");
        System.out.println("    1. Java finds first non-serializable parent (Animal)");
        System.out.println("    2. Calls Animal's no-arg constructor");
        System.out.println("    3. Animal's constructor sets name to 'Unknown'");
        System.out.println("    4. Cat's sound field is restored from serialized data");
        System.out.println("  - Result: name is 'Unknown', sound is 'Meow'");

        new File(file).delete();
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // NotSerializableException
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateNotSerializableException() {
        System.out.println("=== NotSerializableException ===\n");

        String file = "not-serializable.ser";

        System.out.println("Attempting to serialize non-Serializable object:");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            NotSerializableClass obj = new NotSerializableClass("test");
            oos.writeObject(obj);  // ✗ Will throw exception
        } catch (NotSerializableException e) {
            System.out.println("Caught NotSerializableException!");
            System.out.println("Class: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        System.out.println("\nKey point: NotSerializableException is thrown at RUNTIME");
        System.out.println("(not a compile-time error)");

        new File(file).delete();
        System.out.println();
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// EXAMPLE CLASSES
// ═══════════════════════════════════════════════════════════════════════════

// Basic Serializable class
class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}

// Class with transient fields
class User implements Serializable {
    private static final long serialVersionUID = 1L;

    String username;               // Serialized
    transient String password;     // NOT serialized (security)
    transient int loginCount;      // NOT serialized (derived data)

    public User(String username, String password, int loginCount) {
        this.username = username;
        this.password = password;
        this.loginCount = loginCount;
    }
}

// Class to demonstrate constructor not called
class ConstructorTest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String data;
    private int value;

    public ConstructorTest(String data, int value) {
        System.out.println("  → Constructor called with data='" + data + "', value=" + value);
        this.data = data;
        this.value = value;
    }

    @Override
    public String toString() {
        return "ConstructorTest{data='" + data + "', value=" + value + "}";
    }
}

// Class to demonstrate data restoration
class DataRestorationExample implements Serializable {
    private static final long serialVersionUID = 1L;

    String name;
    int age;
    transient String email;  // Transient - won't be restored

    // Default constructor - will this be called during deserialization? NO!
    public DataRestorationExample() {
        System.out.println("  → Default constructor called");
        this.name = "DEFAULT";
        this.age = 0;
        this.email = "default@example.com";
    }

    @Override
    public String toString() {
        return "DataRestorationExample{name='" + name + "', age=" + age + ", email='" + email + "'}";
    }
}

// Class with static field
class StaticFieldExample implements Serializable {
    private static final long serialVersionUID = 1L;

    static String companyName;  // Static - NOT serialized
    private String employeeName;
    private String position;

    public StaticFieldExample(String employeeName, String position) {
        this.employeeName = employeeName;
        this.position = position;
    }

    @Override
    public String toString() {
        return "StaticFieldExample{employeeName='" + employeeName +
                "', position='" + position +
                "', companyName='" + companyName + "'}";
    }
}

// Non-serializable parent class
class Animal {
    protected String name;

    // This constructor WILL be called during deserialization of Cat
    public Animal() {
        System.out.println("  → Animal no-arg constructor called");
        this.name = "Unknown";  // This value will be used!
    }

    public Animal(String name) {
        this.name = name;
    }
}

// Serializable child class
class Cat extends Animal implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sound;
    private int lives;

    public Cat(String name, String sound, int lives) {
        super(name);  // This constructor won't be called during deserialization!
        this.sound = sound;
        this.lives = lives;
    }

    @Override
    public String toString() {
        return "Cat{name='" + name + "', sound='" + sound + "', lives=" + lives + "}";
    }
}

// Class that is NOT Serializable
class NotSerializableClass {
    private String data;

    public NotSerializableClass(String data) {
        this.data = data;
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// EXAM SUMMARY
// ═══════════════════════════════════════════════════════════════════════════
//
// KEY SIGNATURES:
//   void writeObject(Object obj) throws IOException
//   Object readObject() throws IOException, ClassNotFoundException
//
// RULES FOR SERIALIZABLE CLASS:
//   1. Class must implement Serializable
//   2. All instance members must be Serializable, transient, or null
//   3. Rule 2 applies recursively
//
// TRANSIENT:
//   - Not serialized
//   - Reverts to default value when deserialized
//
// DESERIALIZATION PROCESS (CRITICAL):
//   1. Constructor of serialized class NOT called
//   2. Instance initializers NOT called
//   3. No-arg constructor of first non-serializable parent IS called
//   4. Java uses reflection to directly set field values
//   5. Static fields NOT serialized (current JVM value used)
//   6. Transient fields NOT serialized (default values)
//
// READING MULTIPLE OBJECTS:
//   - readObject() throws EOFException at end (NOT null or -1)
//   - Use infinite loop with catch EOFException
//
// STATIC FIELDS (TRICKY):
//   - NOT serialized
//   - In same execution: appears to keep last value
//   - In different execution: has class's initial value
//
// ═══════════════════════════════════════════════════════════════════════════
