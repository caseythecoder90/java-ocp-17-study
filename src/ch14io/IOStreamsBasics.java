package ch14io;

import java.io.*;

/**
 * I/O STREAMS BASICS - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CONCEPTUAL UNDERSTANDING - WHAT IS AN I/O STREAM?
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * An I/O stream is a list of elements presented SEQUENTIALLY.
 *
 * Think of it like water flowing through a pipe:
 * - Data flows in ONE DIRECTION (input OR output, not both)
 * - Data is accessed SEQUENTIALLY (one piece at a time)
 * - Once you read/write data, you can't go back (unless you close and reopen)
 *
 * Each type of I/O stream segments data into "waves" or "blocks":
 * - BYTE STREAMS: Segment data into individual bytes (8 bits)
 * - CHARACTER STREAMS: Segment data into characters (16 bits, Unicode)
 *
 * Contents of a file may be accessed or written via an I/O stream.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * TWO SETS OF I/O STREAM CLASSES - java.io API
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * The java.io API defines TWO sets of I/O stream classes:
 *
 * 1. BYTE STREAMS - For reading/writing BINARY data (images, videos, executables)
 *    - Class names end in: InputStream or OutputStream
 *    - Process data as raw bytes
 *    - Examples: FileInputStream, FileOutputStream, BufferedInputStream
 *
 * 2. CHARACTER STREAMS - For reading/writing TEXT data (text files, strings)
 *    - Class names end in: Reader or Writer
 *    - Process data as characters (handles character encoding)
 *    - Examples: FileReader, FileWriter, BufferedReader
 *
 * ┌──────────────────┬─────────────────┬──────────────────────────────────┐
 * │ Stream Type      │ Data Type       │ Class Name Pattern               │
 * ├──────────────────┼─────────────────┼──────────────────────────────────┤
 * │ Byte Streams     │ Binary (bytes)  │ *InputStream / *OutputStream     │
 * │ Character Streams│ Text (chars)    │ *Reader / *Writer                │
 * └──────────────────┴─────────────────┴──────────────────────────────────┘
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * INPUT vs OUTPUT - COMPLEMENTARY CLASSES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Most I/O stream classes have a COMPLEMENTARY class for the opposite operation:
 *
 * INPUT: Reading data FROM a source
 * OUTPUT: Writing data TO a destination
 *
 * Complementary Pairs:
 * ┌────────────────────────────┬────────────────────────────┐
 * │ Input (Reading)            │ Output (Writing)           │
 * ├────────────────────────────┼────────────────────────────┤
 * │ FileInputStream            │ FileOutputStream           │
 * │ FileReader                 │ FileWriter                 │
 * │ BufferedInputStream        │ BufferedOutputStream       │
 * │ BufferedReader             │ BufferedWriter             │
 * │ ObjectInputStream          │ ObjectOutputStream         │
 * │ DataInputStream            │ DataOutputStream           │
 * │ PrintStream (System.out)   │ N/A (special case)         │
 * │ N/A (special case)         │ PrintWriter                │
 * └────────────────────────────┴────────────────────────────┘
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * FOUR ABSTRACT BASE CLASSES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * All I/O stream classes in java.io extend from ONE of these four abstract classes:
 *
 * ┌────────────────┬─────────────┬─────────────────────────────────────────┐
 * │ Abstract Class │ Stream Type │ Purpose                                 │
 * ├────────────────┼─────────────┼─────────────────────────────────────────┤
 * │ InputStream    │ Byte        │ Read binary data (input)                │
 * │ OutputStream   │ Byte        │ Write binary data (output)              │
 * │ Reader         │ Character   │ Read text data (input)                  │
 * │ Writer         │ Character   │ Write text data (output)                │
 * └────────────────┴─────────────┴─────────────────────────────────────────┘
 *
 * These four classes define the fundamental methods:
 * - InputStream:  int read()
 * - OutputStream: void write(int b)
 * - Reader:       int read()
 * - Writer:       void write(int c)
 *
 * EXAM TIP: All I/O stream classes are in java.io package (NOT java.nio.file)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * LOW-LEVEL vs HIGH-LEVEL STREAMS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * LOW-LEVEL STREAMS:
 * - Connect DIRECTLY to the data source (file, array, String)
 * - Process RAW data in a direct and unfiltered manner
 * - Example: FileInputStream reads file data ONE BYTE AT A TIME
 * - Can be slow and inefficient for large files
 *
 * HIGH-LEVEL STREAMS:
 * - Built ON TOP OF another stream (wrapping)
 * - Provide BUFFERING, PERFORMANCE ENHANCEMENTS, or CONVENIENCE METHODS
 * - Example: BufferedInputStream buffers data in memory, reducing I/O operations
 * - May add new methods like readLine() (BufferedReader)
 *
 * ┌────────────────────────────┬──────────────────────────────────────────┐
 * │ Low-Level                  │ High-Level                               │
 * ├────────────────────────────┼──────────────────────────────────────────┤
 * │ FileInputStream            │ BufferedInputStream                      │
 * │ FileOutputStream           │ BufferedOutputStream                     │
 * │ FileReader                 │ BufferedReader                           │
 * │ FileWriter                 │ BufferedWriter                           │
 * │ ByteArrayInputStream       │ DataInputStream                          │
 * │ ByteArrayOutputStream      │ DataOutputStream                         │
 * │ StringReader               │ PrintStream                              │
 * │ StringWriter               │ PrintWriter                              │
 * │                            │ ObjectInputStream                        │
 * │                            │ ObjectOutputStream                       │
 * └────────────────────────────┴──────────────────────────────────────────┘
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * WRAPPING - THE CONFUSING PART
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * WRAPPING is the process by which an instance is passed to the constructor
 * of another class, and operations on the resulting instance are filtered
 * and applied to the original instance.
 *
 * Example: Creating a BufferedReader to read a file efficiently
 *
 *   // The confusing way (manual wrapping):
 *   FileReader fileReader = new FileReader("file.txt");            // Low-level
 *   BufferedReader bufferedReader = new BufferedReader(fileReader); // High-level wraps low-level
 *
 * Why this is confusing:
 * - You create 2 objects just to read a file efficiently
 * - You have to remember which streams wrap which
 * - Easy to mix incompatible types (exam trap!)
 *
 * NIO.2 Improvement (YES, they solved this!):
 *   // The clean way (NIO.2 handles wrapping internally):
 *   BufferedReader reader = Files.newBufferedReader(Path.of("file.txt"));
 *
 * The NIO.2 API handles the wrapping internally, so you don't have to
 * create multiple objects or understand the low-level/high-level distinction.
 *
 * However, for the OCP exam, you MUST understand the old java.io way!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * WHY WRAPPING? (Constructor Design)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * High-level stream constructors take a reference to the ABSTRACT class:
 *
 * public BufferedReader(Reader in)  // Takes Reader, not FileReader specifically
 *
 * This allows flexibility:
 * - BufferedReader can wrap ANY Reader subclass
 * - FileReader, StringReader, InputStreamReader, etc.
 * - This is polymorphism in action
 *
 * Example:
 *   BufferedReader br1 = new BufferedReader(new FileReader("file.txt"));
 *   BufferedReader br2 = new BufferedReader(new StringReader("text"));
 *   BufferedReader br3 = new BufferedReader(new InputStreamReader(System.in));
 *
 * All valid because FileReader, StringReader, and InputStreamReader extend Reader.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * EXAM TRAP - MIXING INCOMPATIBLE STREAM CLASSES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * The exam likes to mix incompatible I/O stream classes.
 * You MUST recognize when streams are NOT compatible!
 *
 * RULE: You can ONLY wrap streams with the SAME BASE CLASS
 *
 * ✓ VALID combinations:
 *   new BufferedInputStream(new FileInputStream("file"))     // Both InputStream
 *   new BufferedReader(new FileReader("file"))               // Both Reader
 *   new ObjectInputStream(new FileInputStream("file"))       // Both InputStream
 *   new PrintWriter(new FileWriter("file"))                  // Both Writer
 *
 * ✗ INVALID combinations (DOES NOT COMPILE):
 *   new BufferedInputStream(new FileReader("file"))          // InputStream ← Reader ✗
 *   new BufferedReader(new FileInputStream("file"))          // Reader ← InputStream ✗
 *   new ObjectOutputStream(new FileWriter("file"))           // OutputStream ← Writer ✗
 *   new PrintWriter(new FileInputStream("file"))             // Writer ← InputStream ✗
 *
 * Remember the hierarchy:
 *   InputStream  ←  FileInputStream, BufferedInputStream, ObjectInputStream
 *   OutputStream ←  FileOutputStream, BufferedOutputStream, ObjectOutputStream
 *   Reader       ←  FileReader, BufferedReader, StringReader
 *   Writer       ←  FileWriter, BufferedWriter, PrintWriter
 *
 * EXAM TIP: If you see different endings (InputStream + Reader), it won't compile!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class IOStreamsBasics {

    public static void main(String[] args) throws IOException {
        // ────────────────────────────────────────────────────────────────────
        // Demonstrate byte streams vs character streams
        // ────────────────────────────────────────────────────────────────────
        demonstrateByteVsCharacterStreams();

        // ────────────────────────────────────────────────────────────────────
        // Demonstrate low-level vs high-level streams
        // ────────────────────────────────────────────────────────────────────
        demonstrateLowLevelVsHighLevel();

        // ────────────────────────────────────────────────────────────────────
        // Demonstrate wrapping
        // ────────────────────────────────────────────────────────────────────
        demonstrateWrapping();

        // ────────────────────────────────────────────────────────────────────
        // Demonstrate VALID vs INVALID stream combinations (exam traps)
        // ────────────────────────────────────────────────────────────────────
        demonstrateValidVsInvalidCombinations();

        System.out.println("\n✓ All I/O Stream basics examples completed");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Byte Streams vs Character Streams
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateByteVsCharacterStreams() throws IOException {
        System.out.println("=== Byte Streams vs Character Streams ===\n");

        // Create a test file
        String testFile = "test-stream.txt";
        try (FileWriter fw = new FileWriter(testFile)) {
            fw.write("Hello, World!");
        }

        // ────────────────────────────────────────────────────────────────
        // BYTE STREAM - Reads ONE BYTE at a time
        // ────────────────────────────────────────────────────────────────
        System.out.println("1. Byte Stream (FileInputStream):");
        System.out.println("   Reading bytes one at a time:");
        try (FileInputStream fis = new FileInputStream(testFile)) {
            int byteData;
            int count = 0;
            while ((byteData = fis.read()) != -1 && count < 5) {
                System.out.printf("   Byte %d: %d (char: '%c')%n", count++, byteData, (char) byteData);
            }
            System.out.println("   ...");
        }

        // ────────────────────────────────────────────────────────────────
        // CHARACTER STREAM - Reads ONE CHARACTER at a time
        // ────────────────────────────────────────────────────────────────
        System.out.println("\n2. Character Stream (FileReader):");
        System.out.println("   Reading characters one at a time:");
        try (FileReader fr = new FileReader(testFile)) {
            int charData;
            int count = 0;
            while ((charData = fr.read()) != -1 && count < 5) {
                System.out.printf("   Char %d: '%c'%n", count++, (char) charData);
            }
            System.out.println("   ...");
        }

        // Clean up
        new File(testFile).delete();

        System.out.println("\nKey differences:");
        System.out.println("  - Byte streams work with raw bytes (0-255)");
        System.out.println("  - Character streams work with Unicode characters");
        System.out.println("  - Use byte streams for binary data (images, videos)");
        System.out.println("  - Use character streams for text data");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Low-Level vs High-Level Streams
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateLowLevelVsHighLevel() throws IOException {
        System.out.println("\n=== Low-Level vs High-Level Streams ===\n");

        String testFile = "test-buffered.txt";
        try (FileWriter fw = new FileWriter(testFile)) {
            for (int i = 0; i < 1000; i++) {
                fw.write("Line " + i + "\n");
            }
        }

        // ────────────────────────────────────────────────────────────────
        // LOW-LEVEL: FileReader (no buffering, slow)
        // ────────────────────────────────────────────────────────────────
        System.out.println("1. Low-Level Stream (FileReader):");
        System.out.println("   - Connects directly to file");
        System.out.println("   - Reads ONE CHARACTER at a time");
        System.out.println("   - Each read() call is a system call (slow)");
        System.out.println("   - No convenience methods");

        long start = System.nanoTime();
        try (FileReader fr = new FileReader(testFile)) {
            int count = 0;
            while (fr.read() != -1) {
                count++;
            }
        }
        long elapsed = System.nanoTime() - start;
        System.out.println("   Time: " + elapsed / 1_000_000.0 + " ms");

        // ────────────────────────────────────────────────────────────────
        // HIGH-LEVEL: BufferedReader (buffering, fast)
        // ────────────────────────────────────────────────────────────────
        System.out.println("\n2. High-Level Stream (BufferedReader):");
        System.out.println("   - Wraps another Reader (low-level)");
        System.out.println("   - Buffers data in memory (reads chunks at a time)");
        System.out.println("   - Fewer system calls (faster)");
        System.out.println("   - Adds convenience methods like readLine()");

        start = System.nanoTime();
        try (BufferedReader br = new BufferedReader(new FileReader(testFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // readLine() is a convenience method from BufferedReader
            }
        }
        elapsed = System.nanoTime() - start;
        System.out.println("   Time: " + elapsed / 1_000_000.0 + " ms");

        System.out.println("\n   BufferedReader is typically MUCH faster!");

        // Clean up
        new File(testFile).delete();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Wrapping - Building High-Level on Low-Level
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateWrapping() throws IOException {
        System.out.println("\n=== Wrapping (High-Level on Low-Level) ===\n");

        String testFile = "test-wrapping.txt";
        try (FileWriter fw = new FileWriter(testFile)) {
            fw.write("Line 1\nLine 2\nLine 3\n");
        }

        System.out.println("Creating a BufferedReader to read a file:");
        System.out.println();
        System.out.println("Step 1: Create low-level stream (connects to file)");
        System.out.println("  FileReader fileReader = new FileReader(\"file.txt\");");
        System.out.println();
        System.out.println("Step 2: Wrap with high-level stream (adds buffering)");
        System.out.println("  BufferedReader bufferedReader = new BufferedReader(fileReader);");
        System.out.println();
        System.out.println("Result: You have 2 objects:");
        System.out.println("  1. FileReader (low-level) - connected to file");
        System.out.println("  2. BufferedReader (high-level) - wraps FileReader");
        System.out.println();
        System.out.println("When you call bufferedReader.readLine():");
        System.out.println("  BufferedReader → calls read() on FileReader → reads from file");

        // Demonstrate actual usage
        System.out.println("\nActual code:");
        try (FileReader fileReader = new FileReader(testFile);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            System.out.println("Reading lines using bufferedReader.readLine():");
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println("  " + line);
            }
        }

        // NIO.2 comparison
        System.out.println("\nNIO.2 Alternative (simpler, no manual wrapping):");
        System.out.println("  BufferedReader reader = Files.newBufferedReader(Path.of(\"file.txt\"));");
        System.out.println("  (Wrapping handled internally!)");

        // Clean up
        new File(testFile).delete();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Valid vs Invalid Stream Combinations (EXAM TRAPS)
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateValidVsInvalidCombinations() {
        System.out.println("\n=== Valid vs Invalid Stream Combinations ===\n");

        System.out.println("RULE: High-level stream constructor must match base class");
        System.out.println();

        System.out.println("✓ VALID: BufferedInputStream(InputStream)");
        System.out.println("  new BufferedInputStream(new FileInputStream(\"file\"))");
        System.out.println("  → Both are InputStream ✓");
        System.out.println();

        System.out.println("✓ VALID: BufferedReader(Reader)");
        System.out.println("  new BufferedReader(new FileReader(\"file\"))");
        System.out.println("  → Both are Reader ✓");
        System.out.println();

        System.out.println("✓ VALID: ObjectInputStream(InputStream)");
        System.out.println("  new ObjectInputStream(new FileInputStream(\"file\"))");
        System.out.println("  → Both are InputStream ✓");
        System.out.println();

        System.out.println("✓ VALID: PrintWriter(Writer)");
        System.out.println("  new PrintWriter(new FileWriter(\"file\"))");
        System.out.println("  → Both are Writer ✓");
        System.out.println();

        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println();

        System.out.println("✗ INVALID: BufferedInputStream(Reader)");
        System.out.println("  new BufferedInputStream(new FileReader(\"file\"))");
        System.out.println("  → InputStream ← Reader ✗ DOES NOT COMPILE");
        System.out.println();

        System.out.println("✗ INVALID: BufferedReader(InputStream)");
        System.out.println("  new BufferedReader(new FileInputStream(\"file\"))");
        System.out.println("  → Reader ← InputStream ✗ DOES NOT COMPILE");
        System.out.println();

        System.out.println("✗ INVALID: ObjectOutputStream(Writer)");
        System.out.println("  new ObjectOutputStream(new FileWriter(\"file\"))");
        System.out.println("  → OutputStream ← Writer ✗ DOES NOT COMPILE");
        System.out.println();

        System.out.println("✗ INVALID: PrintWriter(InputStream)");
        System.out.println("  new PrintWriter(new FileInputStream(\"file\"))");
        System.out.println("  → Writer ← InputStream ✗ DOES NOT COMPILE");
        System.out.println();

        System.out.println("EXAM TIP: Check the class name endings!");
        System.out.println("  - InputStream + InputStream = ✓");
        System.out.println("  - Reader + Reader = ✓");
        System.out.println("  - InputStream + Reader = ✗");
        System.out.println("  - Any mix of different types = ✗");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// COMPLETE STREAM CLASS REFERENCE
// ═══════════════════════════════════════════════════════════════════════════
//
// ┌─────────────────────────┬──────────┬──────────┬────────────────────────┐
// │ Class Name              │ Base     │ Level    │ Description            │
// ├─────────────────────────┼──────────┼──────────┼────────────────────────┤
// │ BYTE INPUT STREAMS (InputStream)                                        │
// ├─────────────────────────┼──────────┼──────────┼────────────────────────┤
// │ FileInputStream         │ Input    │ Low      │ Reads file as bytes    │
// │ ByteArrayInputStream    │ Input    │ Low      │ Reads byte array       │
// │ BufferedInputStream     │ Input    │ High     │ Buffered byte reading  │
// │ ObjectInputStream       │ Input    │ High     │ Deserialize objects    │
// │ DataInputStream         │ Input    │ High     │ Read primitive types   │
// ├─────────────────────────┼──────────┼──────────┼────────────────────────┤
// │ BYTE OUTPUT STREAMS (OutputStream)                                      │
// ├─────────────────────────┼──────────┼──────────┼────────────────────────┤
// │ FileOutputStream        │ Output   │ Low      │ Writes file as bytes   │
// │ ByteArrayOutputStream   │ Output   │ Low      │ Writes byte array      │
// │ BufferedOutputStream    │ Output   │ High     │ Buffered byte writing  │
// │ ObjectOutputStream      │ Output   │ High     │ Serialize objects      │
// │ DataOutputStream        │ Output   │ High     │ Write primitive types  │
// │ PrintStream             │ Output   │ High     │ Print formatted output │
// ├─────────────────────────┼──────────┼──────────┼────────────────────────┤
// │ CHARACTER INPUT STREAMS (Reader)                                        │
// ├─────────────────────────┼──────────┼──────────┼────────────────────────┤
// │ FileReader              │ Reader   │ Low      │ Reads file as chars    │
// │ StringReader            │ Reader   │ Low      │ Reads String           │
// │ CharArrayReader         │ Reader   │ Low      │ Reads char array       │
// │ BufferedReader          │ Reader   │ High     │ Buffered char reading  │
// │ InputStreamReader       │ Reader   │ High     │ Byte→char bridge       │
// ├─────────────────────────┼──────────┼──────────┼────────────────────────┤
// │ CHARACTER OUTPUT STREAMS (Writer)                                       │
// ├─────────────────────────┼──────────┼──────────┼────────────────────────┤
// │ FileWriter              │ Writer   │ Low      │ Writes file as chars   │
// │ StringWriter            │ Writer   │ Low      │ Writes to String       │
// │ CharArrayWriter         │ Writer   │ Low      │ Writes char array      │
// │ BufferedWriter          │ Writer   │ High     │ Buffered char writing  │
// │ PrintWriter             │ Writer   │ High     │ Print formatted output │
// │ OutputStreamWriter      │ Writer   │ High     │ Char→byte bridge       │
// └─────────────────────────┴──────────┴──────────┴────────────────────────┘
//
// ═══════════════════════════════════════════════════════════════════════════
