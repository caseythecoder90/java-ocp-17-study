package ch14io;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;

/**
 * READING AND WRITING FILES - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * FUNDAMENTAL METHODS - read() and write()
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * The most important methods for I/O streams are read() and write().
 *
 * InputStream and Reader:  int read()
 * OutputStream and Writer: void write(int b)
 *
 * WHY INT INSTEAD OF BYTE?
 * - byte data type has range of 256 values (0-255 for unsigned, -128 to 127 for signed)
 * - Need an EXTRA value to indicate END OF STREAM
 * - Solution: Use larger data type (int) so -1 can indicate end of stream
 * - read() returns -1 when end of stream is reached
 * - write() uses int for consistency, but only writes the lower 8 bits
 *
 * EXAM TIP: read() returns -1 at end of stream, NOT 0 or null!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * OVERLOADED read() and write() METHODS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Reading/writing multiple bytes at a time improves performance:
 *
 * InputStream / OutputStream:
 *   int read()                              Read single byte, return -1 if EOF
 *   int read(byte[] b)                      Read into array, return # bytes read
 *   int read(byte[] b, int offset, int len) Read into array from offset
 *   void write(int b)                       Write single byte
 *   void write(byte[] b)                    Write entire array
 *   void write(byte[] b, int offset, int len) Write portion of array
 *
 * Reader / Writer:
 *   int read()                              Read single char, return -1 if EOF
 *   int read(char[] c)                      Read into array, return # chars read
 *   int read(char[] c, int offset, int len) Read into array from offset
 *   void write(int c)                       Write single char
 *   void write(char[] c)                    Write entire array
 *   void write(char[] c, int offset, int len) Write portion of array
 *   void write(String str)                  Write string (Writer only!)
 *
 * OFFSET AND LENGTH:
 * - offset: Starting position in the ARRAY (not the stream!)
 * - length: Number of bytes/chars to read/write
 * - The stream itself maintains its own position marker
 *
 * EXAM TIP: offset applies to the ARRAY, not the stream position!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * STREAM POSITION MARKER
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Q: Why is offset often 0 when reading/writing?
 * A: Because the STREAM has an internal position marker that moves forward!
 *
 * Example:
 *   InputStream in = ...;
 *   byte[] buffer = new byte[1024];
 *
 *   // First read: reads bytes 0-1023 from stream into buffer[0-1023]
 *   int bytesRead = in.read(buffer, 0, 1024);
 *
 *   // Second read: reads bytes 1024-2047 from stream into buffer[0-1023]
 *   bytesRead = in.read(buffer, 0, 1024);  // offset still 0!
 *
 * The offset 0 means "start at position 0 in the BUFFER".
 * The stream's internal marker tracks where we are in the FILE.
 * Each read/write moves the stream's marker forward automatically.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * flush() - FORCING DATA TO BE WRITTEN
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * void flush() throws IOException
 *
 * When data is written to an OutputStream or Writer, the OS does NOT guarantee
 * that the data will make it to the file system IMMEDIATELY.
 *
 * flush() requests that all accumulated data be written immediately to disk.
 *
 * Why use flush():
 * - Reduces data loss if application terminates unexpectedly
 * - Ensures other processes can see the data
 *
 * Cost:
 * - flush() is an EXPENSIVE operation
 * - Forces synchronous I/O (waits for disk)
 * - Use sparingly for critical data only
 *
 * Note: close() automatically calls flush(), so no need to flush before close.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * BUFFEREDREADER AND BUFFEREDWRITER - CONVENIENCE METHODS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * BufferedReader:
 *   String readLine() throws IOException
 *   - Reads an entire line of text
 *   - Returns null when end of stream reached
 *   - Line does NOT include line terminator (\n, \r\n)
 *
 * BufferedWriter:
 *   void write(String str) throws IOException
 *   - Writes a string
 *
 *   void newLine() throws IOException
 *   - Writes platform-specific line separator
 *   - Use this instead of \n for portability
 *
 * EXAM TIP: readLine() returns null at EOF, read() returns -1!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * PRINTSTREAM AND PRINTWRITER - THE CONFUSING ONES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * PrintStream:  High-level BYTE output stream (extends OutputStream)
 * PrintWriter:  High-level CHARACTER output stream (extends Writer)
 *
 * Both provide:
 *   print(...)      - Print without newline
 *   println(...)    - Print with newline
 *   printf(...)     - Formatted printing
 *   format(...)     - Same as printf
 *
 * Key differences from other streams:
 * 1. Methods do NOT throw IOException (suppress exceptions)
 * 2. Check error status with checkError() method
 * 3. Automatically flush on println() calls
 *
 * Common uses:
 *   System.out  - PrintStream (byte stream)
 *   System.err  - PrintStream (byte stream)
 *   PrintWriter - For writing formatted text to files
 *
 * EXAM TIP: PrintStream/PrintWriter methods don't throw checked exceptions!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * NIO.2 FILES CLASS - SIMPLIFIED API
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * The Files class provides static methods that simplify reading/writing:
 * - No need for manual stream creation
 * - No need for manual buffering
 * - Handles resource cleanup automatically
 * - More concise and less error-prone
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * NIO.2 METHODS - READING FILES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * byte[] readAllBytes(Path path)
 *   throws IOException
 *   - Reads ALL bytes from file into byte array
 *   - Entire file loaded into memory
 *   - Use for SMALL files only
 *
 * String readString(Path path)
 *   throws IOException
 *   - Reads entire file into String (using UTF-8)
 *   - Entire file loaded into memory
 *   - Use for SMALL text files only
 *
 * List<String> readAllLines(Path path)
 *   throws IOException
 *   - Reads ALL lines into List<String>
 *   - Entire file loaded into memory AT ONCE
 *   - Returns List, NOT Stream
 *   - Use for SMALL files only
 *
 * Stream<String> lines(Path path)
 *   throws IOException
 *   - Returns Stream<String> of lines
 *   - LAZY LOADING - reads lines as needed
 *   - Does NOT load entire file into memory
 *   - Use for LARGE files
 *   - Must close the Stream (use try-with-resources)
 *
 * BufferedReader newBufferedReader(Path path, OpenOption... options)
 *   throws IOException
 *   - Creates BufferedReader for the file
 *   - Handles wrapping internally
 *   - Use for custom line-by-line processing
 *
 * CRITICAL EXAM DIFFERENCE: readAllLines() vs lines()
 * ┌─────────────────┬─────────────┬─────────────┬────────────────────────┐
 * │ Method          │ Return Type │ Loading     │ Best For               │
 * ├─────────────────┼─────────────┼─────────────┼────────────────────────┤
 * │ readAllLines()  │ List<String>│ Eager (all) │ Small files            │
 * │ lines()         │Stream<String>│ Lazy (on-demand)│ Large files      │
 * └─────────────────┴─────────────┴─────────────┴────────────────────────┘
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * NIO.2 METHODS - WRITING FILES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Path write(Path path, byte[] bytes, OpenOption... options)
 *   throws IOException
 *   - Writes byte array to file
 *   - Default: CREATE, TRUNCATE_EXISTING, WRITE
 *
 * Path writeString(Path path, String str, OpenOption... options)
 *   throws IOException
 *   - Writes string to file (using UTF-8)
 *   - Default: CREATE, TRUNCATE_EXISTING, WRITE
 *
 * Path write(Path path, Iterable<String> lines, OpenOption... options)
 *   throws IOException
 *   - Writes lines to file (adds line separators)
 *   - Takes List<String>, Collection<String>, etc.
 *   - Default: CREATE, TRUNCATE_EXISTING, WRITE
 *
 * BufferedWriter newBufferedWriter(Path path, OpenOption... options)
 *   throws IOException
 *   - Creates BufferedWriter for the file
 *   - Handles wrapping internally
 *   - Default: CREATE, TRUNCATE_EXISTING, WRITE
 *   - Use for custom writing
 *
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class ReadingWritingFiles {

    public static void main(String[] args) throws IOException {
        // ────────────────────────────────────────────────────────────────────
        // Demonstrate basic read() and write() with single bytes
        // ────────────────────────────────────────────────────────────────────
        demonstrateBasicReadWrite();

        // ────────────────────────────────────────────────────────────────────
        // Demonstrate buffered reading and writing
        // ────────────────────────────────────────────────────────────────────
        demonstrateBufferedReadWrite();

        // ────────────────────────────────────────────────────────────────────
        // Demonstrate copyStream with buffer
        // ────────────────────────────────────────────────────────────────────
        demonstrateCopyStream();

        // ────────────────────────────────────────────────────────────────────
        // Demonstrate flush()
        // ────────────────────────────────────────────────────────────────────
        demonstrateFlush();

        // ────────────────────────────────────────────────────────────────────
        // Demonstrate BufferedReader.readLine() and BufferedWriter.newLine()
        // ────────────────────────────────────────────────────────────────────
        demonstrateBufferedReaderWriter();

        // ────────────────────────────────────────────────────────────────────
        // Demonstrate PrintStream and PrintWriter
        // ────────────────────────────────────────────────────────────────────
        demonstratePrintStreamWriter();

        // ────────────────────────────────────────────────────────────────────
        // Demonstrate NIO.2 Files methods for reading
        // ────────────────────────────────────────────────────────────────────
        demonstrateFilesReading();

        // ────────────────────────────────────────────────────────────────────
        // Demonstrate NIO.2 Files methods for writing
        // ────────────────────────────────────────────────────────────────────
        demonstrateFilesWriting();

        // ────────────────────────────────────────────────────────────────────
        // CRITICAL: Demonstrate readAllLines() vs lines()
        // ────────────────────────────────────────────────────────────────────
        demonstrateReadAllLinesVsLines();

        System.out.println("\n✓ All reading and writing examples completed");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Basic read() and write() - Single byte at a time
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateBasicReadWrite() throws IOException {
        System.out.println("=== Basic read() and write() ===\n");

        String sourceFile = "source.txt";
        String destFile = "dest.txt";

        // Write some data
        try (FileOutputStream out = new FileOutputStream(sourceFile)) {
            out.write(72);   // 'H'
            out.write(101);  // 'e'
            out.write(108);  // 'l'
            out.write(108);  // 'l'
            out.write(111);  // 'o'
            System.out.println("Wrote 5 bytes to " + sourceFile);
        }

        // Read and copy data one byte at a time
        try (FileInputStream in = new FileInputStream(sourceFile);
             FileOutputStream out = new FileOutputStream(destFile)) {

            System.out.println("\nReading and copying one byte at a time:");
            int byteData;
            int count = 0;
            while ((byteData = in.read()) != -1) {  // read() returns -1 at EOF
                System.out.printf("  Read byte: %d (char: '%c')%n", byteData, (char) byteData);
                out.write(byteData);
                count++;
            }
            System.out.println("Copied " + count + " bytes");
        }

        // Verify
        System.out.println("\nVerification:");
        try (FileInputStream in = new FileInputStream(destFile)) {
            int b;
            while ((b = in.read()) != -1) {
                System.out.print((char) b);
            }
            System.out.println();
        }

        // Clean up
        new File(sourceFile).delete();
        new File(destFile).delete();

        System.out.println("\nKey points:");
        System.out.println("  - read() returns int, not byte (to accommodate -1 for EOF)");
        System.out.println("  - write() takes int, but only writes lower 8 bits");
        System.out.println("  - Reading one byte at a time is SLOW for large files");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Buffered reading and writing - Multiple bytes at a time
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateBufferedReadWrite() throws IOException {
        System.out.println("\n=== Buffered read() and write() ===\n");

        String sourceFile = "source-buffered.txt";
        String destFile = "dest-buffered.txt";

        // Create source file
        try (FileWriter fw = new FileWriter(sourceFile)) {
            fw.write("This is a test file with some content.\n");
            fw.write("It has multiple lines.\n");
            fw.write("We will copy it using buffered I/O.\n");
        }

        // Copy using byte array buffer
        System.out.println("Copying file using byte array buffer:");
        try (FileInputStream in = new FileInputStream(sourceFile);
             FileOutputStream out = new FileOutputStream(destFile)) {

            byte[] buffer = new byte[1024];  // 1 KB buffer
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                System.out.println("  Read " + bytesRead + " bytes");
                out.write(buffer, 0, bytesRead);  // Write only bytes actually read
            }
        }

        System.out.println("\nCopied successfully using buffer");

        // Clean up
        new File(sourceFile).delete();
        new File(destFile).delete();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // copyStream with buffer - Classic pattern
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateCopyStream() throws IOException {
        System.out.println("\n=== copyStream with Buffer ===\n");

        String sourceFile = "copy-source.txt";
        String destFile = "copy-dest.txt";

        // Create source file
        Files.writeString(Path.of(sourceFile), "Sample content for copying");

        // Copy using reusable method
        try (FileInputStream in = new FileInputStream(sourceFile);
             FileOutputStream out = new FileOutputStream(destFile)) {
            copyStream(in, out);
        }

        System.out.println("File copied successfully");
        System.out.println("\nKey points:");
        System.out.println("  - Buffer size typically 1024 or 8192 bytes");
        System.out.println("  - offset = 0 (start at beginning of ARRAY)");
        System.out.println("  - length = bytesRead (write only what was read)");
        System.out.println("  - Stream position tracked automatically by stream itself");

        // Clean up
        new File(sourceFile).delete();
        new File(destFile).delete();
    }

    // Reusable copy method
    private static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;

        // offset = 0: Start at position 0 in BUFFER
        // Stream's internal marker tracks position in FILE
        while ((bytesRead = in.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // flush() - Forcing data to be written
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateFlush() throws IOException {
        System.out.println("\n=== flush() ===\n");

        String file = "flush-test.txt";

        System.out.println("Writing with explicit flush:");
        try (FileWriter fw = new FileWriter(file)) {
            fw.write("Line 1\n");
            fw.flush();  // Force write to disk
            System.out.println("  Wrote Line 1 and flushed");

            fw.write("Line 2\n");
            fw.flush();  // Force write to disk
            System.out.println("  Wrote Line 2 and flushed");

            fw.write("Line 3\n");
            // No flush here - will be flushed on close()
            System.out.println("  Wrote Line 3 (will flush on close)");
        }

        System.out.println("\nWhy use flush():");
        System.out.println("  - Reduces data loss if application crashes");
        System.out.println("  - Ensures other processes can see the data");
        System.out.println("  - Costly operation - use sparingly");
        System.out.println("  - close() automatically flushes, so usually not needed");

        // Clean up
        new File(file).delete();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // BufferedReader.readLine() and BufferedWriter.newLine()
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateBufferedReaderWriter() throws IOException {
        System.out.println("\n=== BufferedReader.readLine() and BufferedWriter ===\n");

        String file = "buffered-lines.txt";

        // Write lines using BufferedWriter
        System.out.println("Writing lines with BufferedWriter:");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Line 1");
            writer.newLine();  // Platform-specific line separator
            writer.write("Line 2");
            writer.newLine();
            writer.write("Line 3");
            writer.newLine();
            System.out.println("  Wrote 3 lines");
        }

        // Read lines using BufferedReader
        System.out.println("\nReading lines with BufferedReader:");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {  // Returns null at EOF
                System.out.println("  Line " + lineNum++ + ": " + line);
            }
        }

        System.out.println("\nKey points:");
        System.out.println("  - readLine() returns null at EOF (not -1!)");
        System.out.println("  - readLine() does NOT include line terminator");
        System.out.println("  - newLine() writes platform-specific separator");

        // Clean up
        new File(file).delete();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PrintStream and PrintWriter
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstratePrintStreamWriter() throws IOException {
        System.out.println("\n=== PrintStream and PrintWriter ===\n");

        String file = "print-test.txt";

        System.out.println("PrintWriter example:");
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.print("Hello");       // No newline
            writer.println(" World");    // With newline
            writer.printf("Number: %d%n", 42);  // Formatted
            writer.format("Pi: %.2f%n", Math.PI);  // Same as printf

            // Check for errors (since PrintWriter doesn't throw exceptions)
            if (writer.checkError()) {
                System.out.println("  Error occurred during writing");
            } else {
                System.out.println("  Write successful (no errors)");
            }
        }

        // Read back
        System.out.println("\nContent written:");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("  " + line);
            }
        }

        System.out.println("\nKey points:");
        System.out.println("  - PrintStream: for byte streams (e.g., System.out)");
        System.out.println("  - PrintWriter: for character streams");
        System.out.println("  - print() and println() don't throw IOException");
        System.out.println("  - Use checkError() to detect errors");
        System.out.println("  - Automatically flush on println()");

        // Clean up
        new File(file).delete();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // NIO.2 Files methods - Reading
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateFilesReading() throws IOException {
        System.out.println("\n=== NIO.2 Files Reading Methods ===\n");

        String file = "nio2-read-test.txt";
        Path path = Path.of(file);

        // Create test file
        Files.writeString(path, "Line 1\nLine 2\nLine 3\n");

        // 1. readAllBytes()
        System.out.println("1. Files.readAllBytes():");
        byte[] bytes = Files.readAllBytes(path);
        System.out.println("   Read " + bytes.length + " bytes");

        // 2. readString()
        System.out.println("\n2. Files.readString():");
        String content = Files.readString(path);
        System.out.println("   Content: " + content.replace("\n", "\\n"));

        // 3. readAllLines()
        System.out.println("\n3. Files.readAllLines():");
        List<String> lines = Files.readAllLines(path);
        System.out.println("   Returns: List<String>");
        System.out.println("   Size: " + lines.size());
        lines.forEach(line -> System.out.println("   " + line));

        // 4. lines() - returns Stream
        System.out.println("\n4. Files.lines():");
        System.out.println("   Returns: Stream<String>");
        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(line -> System.out.println("   " + line));
        }

        // 5. newBufferedReader()
        System.out.println("\n5. Files.newBufferedReader():");
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            System.out.println("   Returns: BufferedReader");
            System.out.println("   First line: " + reader.readLine());
        }

        // Clean up
        Files.deleteIfExists(path);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // NIO.2 Files methods - Writing
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateFilesWriting() throws IOException {
        System.out.println("\n=== NIO.2 Files Writing Methods ===\n");

        // 1. writeString()
        System.out.println("1. Files.writeString():");
        Path file1 = Path.of("write-string.txt");
        Files.writeString(file1, "Hello, World!");
        System.out.println("   Wrote string to " + file1);
        Files.deleteIfExists(file1);

        // 2. write(byte[])
        System.out.println("\n2. Files.write(Path, byte[]):");
        Path file2 = Path.of("write-bytes.txt");
        Files.write(file2, "Hello, Bytes!".getBytes());
        System.out.println("   Wrote bytes to " + file2);
        Files.deleteIfExists(file2);

        // 3. write(Iterable<String>)
        System.out.println("\n3. Files.write(Path, Iterable<String>):");
        Path file3 = Path.of("write-lines.txt");
        List<String> lines = List.of("Line 1", "Line 2", "Line 3");
        Files.write(file3, lines);
        System.out.println("   Wrote lines to " + file3);
        System.out.println("   Automatically adds line separators");
        Files.deleteIfExists(file3);

        // 4. newBufferedWriter()
        System.out.println("\n4. Files.newBufferedWriter():");
        Path file4 = Path.of("buffered-writer.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(file4)) {
            writer.write("Line 1");
            writer.newLine();
            writer.write("Line 2");
            writer.newLine();
        }
        System.out.println("   Wrote lines using BufferedWriter to " + file4);
        Files.deleteIfExists(file4);

        // OpenOption examples
        System.out.println("\n5. With StandardOpenOption:");
        Path file5 = Path.of("append-test.txt");
        Files.writeString(file5, "First line\n");
        Files.writeString(file5, "Second line\n", StandardOpenOption.APPEND);
        System.out.println("   Content:");
        Files.readAllLines(file5).forEach(line -> System.out.println("   " + line));
        Files.deleteIfExists(file5);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CRITICAL EXAM TOPIC: readAllLines() vs lines()
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateReadAllLinesVsLines() throws IOException {
        System.out.println("\n=== CRITICAL: readAllLines() vs lines() ===\n");

        Path file = Path.of("compare-test.txt");
        Files.writeString(file, "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\n");

        // ────────────────────────────────────────────────────────────────
        // readAllLines() - EAGER loading
        // ────────────────────────────────────────────────────────────────
        System.out.println("1. Files.readAllLines():");
        System.out.println("   Return type: List<String>");
        System.out.println("   Loading: EAGER (loads entire file into memory)");
        System.out.println("   Memory: ALL lines stored in List");
        System.out.println("   Best for: SMALL files");
        System.out.println();

        List<String> linesList = Files.readAllLines(file);
        System.out.println("   Result type: " + linesList.getClass().getSimpleName());
        System.out.println("   Number of lines: " + linesList.size());
        System.out.println("   Can access by index: " + linesList.get(0));
        System.out.println("   Can iterate multiple times: Yes");

        // ────────────────────────────────────────────────────────────────
        // lines() - LAZY loading
        // ────────────────────────────────────────────────────────────────
        System.out.println("\n2. Files.lines():");
        System.out.println("   Return type: Stream<String>");
        System.out.println("   Loading: LAZY (reads lines on-demand)");
        System.out.println("   Memory: Only current line in memory");
        System.out.println("   Best for: LARGE files");
        System.out.println();

        try (Stream<String> linesStream = Files.lines(file)) {
            System.out.println("   Result type: Stream<String>");
            System.out.println("   Must use try-with-resources: Yes");
            System.out.println("   Can iterate multiple times: No (consumed after use)");
            System.out.println("   Can use Stream operations:");
            long count = linesStream.filter(line -> line.contains("3")).count();
            System.out.println("   Lines containing '3': " + count);
        }

        // Comparison table
        System.out.println("\n┌─────────────────────┬────────────────────┬────────────────────┐");
        System.out.println("│ Feature             │ readAllLines()     │ lines()            │");
        System.out.println("├─────────────────────┼────────────────────┼────────────────────┤");
        System.out.println("│ Return Type         │ List<String>       │ Stream<String>     │");
        System.out.println("│ Loading             │ Eager (all at once)│ Lazy (on-demand)   │");
        System.out.println("│ Memory Usage        │ High (all lines)   │ Low (one line)     │");
        System.out.println("│ Best For            │ Small files        │ Large files        │");
        System.out.println("│ Random Access       │ Yes (by index)     │ No                 │");
        System.out.println("│ Reusable            │ Yes                │ No (consumed)      │");
        System.out.println("│ Needs try-resources │ No                 │ Yes (must close)   │");
        System.out.println("│ Stream operations   │ No (List)          │ Yes (filter, map)  │");
        System.out.println("└─────────────────────┴────────────────────┴────────────────────┘");

        System.out.println("\nEXAM TIP:");
        System.out.println("  - readAllLines() → List → all in memory → small files");
        System.out.println("  - lines() → Stream → lazy loading → large files");
        System.out.println("  - lines() MUST be closed (try-with-resources)");

        // Clean up
        Files.deleteIfExists(file);
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// COMPLETE METHOD REFERENCE TABLE
// ═══════════════════════════════════════════════════════════════════════════
//
// JAVA.IO STREAM METHODS:
// ┌─────────────────────┬───────────────────────────────────────────────────┐
// │ Class               │ Method Signature                                  │
// ├─────────────────────┼───────────────────────────────────────────────────┤
// │ InputStream         │ int read() throws IOException                     │
// │                     │ int read(byte[] b) throws IOException             │
// │                     │ int read(byte[] b, int off, int len)              │
// ├─────────────────────┼───────────────────────────────────────────────────┤
// │ OutputStream        │ void write(int b) throws IOException              │
// │                     │ void write(byte[] b) throws IOException           │
// │                     │ void write(byte[] b, int off, int len)            │
// │                     │ void flush() throws IOException                   │
// │                     │ void close() throws IOException                   │
// ├─────────────────────┼───────────────────────────────────────────────────┤
// │ Reader              │ int read() throws IOException                     │
// │                     │ int read(char[] c) throws IOException             │
// │                     │ int read(char[] c, int off, int len)              │
// ├─────────────────────┼───────────────────────────────────────────────────┤
// │ Writer              │ void write(int c) throws IOException              │
// │                     │ void write(char[] c) throws IOException           │
// │                     │ void write(char[] c, int off, int len)            │
// │                     │ void write(String str) throws IOException         │
// │                     │ void flush() throws IOException                   │
// │                     │ void close() throws IOException                   │
// ├─────────────────────┼───────────────────────────────────────────────────┤
// │ BufferedReader      │ String readLine() throws IOException              │
// ├─────────────────────┼───────────────────────────────────────────────────┤
// │ BufferedWriter      │ void write(String str) throws IOException         │
// │                     │ void newLine() throws IOException                 │
// ├─────────────────────┼───────────────────────────────────────────────────┤
// │ PrintStream/Writer  │ void print(...)                                   │
// │                     │ void println(...)                                 │
// │                     │ void printf(String format, Object... args)        │
// │                     │ boolean checkError()                              │
// └─────────────────────┴───────────────────────────────────────────────────┘
//
// NIO.2 FILES CLASS METHODS:
// ┌─────────────────────────────────────────────────────────────────────────┐
// │ Method Signature                                                        │
// ├─────────────────────────────────────────────────────────────────────────┤
// │ byte[] readAllBytes(Path path) throws IOException                      │
// │ String readString(Path path) throws IOException                        │
// │ List<String> readAllLines(Path path) throws IOException                │
// │ Stream<String> lines(Path path) throws IOException                     │
// │ BufferedReader newBufferedReader(Path path) throws IOException         │
// ├─────────────────────────────────────────────────────────────────────────┤
// │ Path write(Path path, byte[] bytes, OpenOption...) throws IOException  │
// │ Path writeString(Path path, String str, OpenOption...)                 │
// │ Path write(Path path, Iterable<String> lines, OpenOption...)           │
// │ BufferedWriter newBufferedWriter(Path path, OpenOption...)             │
// └─────────────────────────────────────────────────────────────────────────┘
//
// ═══════════════════════════════════════════════════════════════════════════
