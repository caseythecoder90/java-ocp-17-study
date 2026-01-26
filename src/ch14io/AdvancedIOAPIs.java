package ch14io;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.time.Instant;
import java.util.stream.Stream;

/**
 * ADVANCED I/O APIs - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * STREAM MANIPULATION METHODS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Some InputStream and Reader implementations support marking a position
 * and resetting back to that position.
 *
 * METHODS:
 *
 * boolean markSupported()
 *   - Returns true if mark() and reset() are supported
 *   - Returns false otherwise
 *   - Check BEFORE attempting to mark
 *
 * void mark(int readAheadLimit)
 *   - Marks current position in stream
 *   - readAheadLimit: number of bytes/chars that can be read before mark invalid
 *   - Not all streams support this (check markSupported() first)
 *   - BufferedInputStream, BufferedReader support mark
 *   - FileInputStream, FileReader do NOT support mark
 *
 * void reset() throws IOException
 *   - Returns stream position to last marked position
 *   - Throws IOException if mark not set or invalidated
 *
 * long skip(long n) throws IOException
 *   - Skips over and discards n bytes/chars
 *   - Returns actual number skipped (may be less than n)
 *   - Used to skip unwanted data efficiently
 *   - Works on all InputStream/Reader implementations
 *
 * EXAM TIP: Only BUFFERED streams support mark/reset!
 * EXAM TIP: skip() returns long (actual number skipped), mark() returns void!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * FILE ATTRIBUTES - CHECKING ACCESSIBILITY
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Files class provides static methods to check file attributes:
 *
 * boolean isDirectory(Path path, LinkOption... options)
 *   - Returns true if path is a directory
 *   - Default: follows symbolic links
 *
 * boolean isSymbolicLink(Path path)
 *   - Returns true if path is a symbolic link
 *   - Does NOT follow the link (checks link itself)
 *   - NO LinkOption parameter (always checks link, not target)
 *
 * boolean isRegularFile(Path path, LinkOption... options)
 *   - Returns true if path is a regular file (not directory or link)
 *   - Default: follows symbolic links
 *
 * boolean isHidden(Path path) throws IOException
 *   - Returns true if file is hidden
 *   - Throws IOException (unlike other is* methods!)
 *   - Platform-dependent (. prefix on Unix, attribute on Windows)
 *
 * boolean isReadable(Path path)
 *   - Returns true if file can be read by current user
 *
 * boolean isWritable(Path path)
 *   - Returns true if file can be written by current user
 *
 * boolean isExecutable(Path path)
 *   - Returns true if file can be executed by current user
 *
 * EXAM TRAP: isHidden() throws IOException, others don't!
 * EXAM TRAP: isSymbolicLink() has NO LinkOption parameter!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * FILE ATTRIBUTE INTERFACES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * BasicFileAttributes (interface)
 *   - Basic attributes available on all file systems
 *   - Methods: size(), creationTime(), lastModifiedTime(), lastAccessTime(),
 *              isDirectory(), isRegularFile(), isSymbolicLink(), isOther()
 *
 * DosFileAttributes (interface, extends BasicFileAttributes)
 *   - DOS/Windows-specific attributes
 *   - Additional methods: isReadOnly(), isHidden(), isArchive(), isSystem()
 *
 * PosixFileAttributes (interface, extends BasicFileAttributes)
 *   - POSIX/Unix-specific attributes
 *   - Additional methods: owner(), group(), permissions()
 *
 * READING ATTRIBUTES:
 *
 * <A extends BasicFileAttributes> A readAttributes(
 *     Path path,
 *     Class<A> type,
 *     LinkOption... options
 * ) throws IOException
 *   - Reads file attributes in bulk (single I/O operation)
 *   - More efficient than multiple individual calls
 *   - Returns attribute object
 *
 * Example:
 *   BasicFileAttributes attrs = Files.readAttributes(
 *       path,
 *       BasicFileAttributes.class
 *   );
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * MODIFYING FILE ATTRIBUTES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Path setLastModifiedTime(Path path, FileTime time) throws IOException
 *   - Sets last modified time
 *   - Returns the path
 *
 * BasicFileAttributeView getFileAttributeView(
 *     Path path,
 *     Class<V> type,
 *     LinkOption... options
 * )
 *   - Returns view for updating attributes
 *   - Can set multiple attributes at once
 *
 * BasicFileAttributeView.setTimes(
 *     FileTime lastModifiedTime,
 *     FileTime lastAccessTime,
 *     FileTime createTime
 * ) throws IOException
 *   - Sets all three times in single operation
 *   - Pass null to keep current value
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * TRAVERSING A DIRECTORY TREE
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * NIO.2 uses DEPTH-FIRST SEARCH when traversing directories.
 *
 * Depth-first means:
 * 1. Visit directory
 * 2. Visit ALL contents of directory (recursively)
 * 3. Then visit sibling directories
 *
 * Example structure:        Traversal order:
 *   /root                   1. /root
 *     /a                    2. /root/a
 *       file1.txt           3. /root/a/file1.txt
 *       file2.txt           4. /root/a/file2.txt
 *     /b                    5. /root/b
 *       file3.txt           6. /root/b/file3.txt
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * WALK METHODS
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Stream<Path> walk(Path start, FileVisitOption... options)
 *   throws IOException
 *   - Traverses directory tree starting at 'start'
 *   - Default: Visits ALL levels (no depth limit)
 *   - Returns Stream<Path> of all visited paths
 *   - LAZY loading (paths visited as stream consumed)
 *   - MUST close Stream (try-with-resources)
 *   - Default: does NOT follow symbolic links
 *
 * Stream<Path> walk(Path start, int maxDepth, FileVisitOption... options)
 *   throws IOException
 *   - Same as above but limits depth
 *   - maxDepth: maximum number of directory levels to visit
 *   - maxDepth = 0: only start path
 *   - maxDepth = 1: start path + direct children
 *   - maxDepth = Integer.MAX_VALUE: all levels
 *
 * FileVisitOption.FOLLOW_LINKS:
 *   - Follow symbolic links
 *   - WARNING: Can cause infinite loops if circular links exist
 *   - Without this option: safer, won't follow links
 *
 * EXAM TIP: walk() returns Stream<Path> which MUST be closed!
 * EXAM TIP: Default does NOT follow symbolic links (safety)
 * EXAM TIP: maxDepth = 0 includes only start path, maxDepth = 1 includes children
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * FIND METHOD - CRITICAL EXAM TOPIC
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Stream<Path> find(
 *     Path start,
 *     int maxDepth,
 *     BiPredicate<Path, BasicFileAttributes> matcher,
 *     FileVisitOption... options
 * ) throws IOException
 *
 * Parameters:
 *   - start: Starting directory
 *   - maxDepth: Maximum depth to search
 *   - matcher: BiPredicate that tests Path AND BasicFileAttributes
 *   - options: FileVisitOption.FOLLOW_LINKS (optional)
 *
 * Returns: Stream<Path> of paths matching the predicate
 *
 * THE BiPredicate:
 *   BiPredicate<Path, BasicFileAttributes> matcher
 *   - Takes TWO parameters: Path and BasicFileAttributes
 *   - Returns boolean (true = include in results)
 *   - Lambda syntax: (path, attrs) -> condition
 *
 * CRITICAL EXAM TRAPS WITH find():
 *
 * 1. BiPredicate takes TWO parameters (Path, BasicFileAttributes)
 *    ✓ (path, attrs) -> path.toString().endsWith(".txt")
 *    ✗ path -> path.toString().endsWith(".txt")  // DOES NOT COMPILE!
 *
 * 2. Must use BOTH parameters or explicitly ignore one
 *    ✓ (path, attrs) -> attrs.isRegularFile()
 *    ✓ (path, attrs) -> path.toString().contains("test")
 *    ✗ (path) -> path.toString().contains("test")  // DOES NOT COMPILE!
 *
 * 3. Returns Stream<Path>, not Stream<BasicFileAttributes>
 *    ✓ Stream<Path> results = Files.find(...)
 *    ✗ Stream<BasicFileAttributes> results = Files.find(...)  // Wrong type!
 *
 * 4. Stream MUST be closed (try-with-resources)
 *    ✓ try (Stream<Path> stream = Files.find(...)) { }
 *    ✗ Stream<Path> stream = Files.find(...); stream.forEach(...)  // Resource leak!
 *
 * 5. Stream operations that don't compile
 *    ✓ .filter((path, attrs) -> ...)  // In find() BiPredicate
 *    ✗ .filter(path -> ...)  // After find(), Stream<Path> uses Predicate<Path>
 *    ✓ stream.filter(path -> ...)  // Correct for Stream<Path>
 *
 * EXAM LOVES TESTING: Mixing up lambda parameters in find() vs Stream operations!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class AdvancedIOAPIs {

    public static void main(String[] args) throws IOException {
        // ────────────────────────────────────────────────────────────────────
        // Stream manipulation: mark, reset, skip
        // ────────────────────────────────────────────────────────────────────
        demonstrateStreamManipulation();

        // ────────────────────────────────────────────────────────────────────
        // File attributes: checking accessibility
        // ────────────────────────────────────────────────────────────────────
        demonstrateFileAccessibility();

        // ────────────────────────────────────────────────────────────────────
        // Reading file attributes (BasicFileAttributes, etc.)
        // ────────────────────────────────────────────────────────────────────
        demonstrateReadingAttributes();

        // ────────────────────────────────────────────────────────────────────
        // Modifying file attributes (setTimes)
        // ────────────────────────────────────────────────────────────────────
        demonstrateModifyingAttributes();

        // ────────────────────────────────────────────────────────────────────
        // Traversing directory tree: walk()
        // ────────────────────────────────────────────────────────────────────
        demonstrateWalk();

        // ────────────────────────────────────────────────────────────────────
        // CRITICAL: find() method with BiPredicate and Stream pipelines
        // ────────────────────────────────────────────────────────────────────
        demonstrateFind();

        // ────────────────────────────────────────────────────────────────────
        // EXAM TRAPS: Common compilation errors with find()
        // ────────────────────────────────────────────────────────────────────
        demonstrateFindExamTraps();

        System.out.println("\n✓ All advanced I/O API examples completed");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Stream Manipulation: mark, reset, skip
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateStreamManipulation() throws IOException {
        System.out.println("=== Stream Manipulation: mark, reset, skip ===\n");

        // Create test file
        String testFile = "stream-manipulation.txt";
        Files.writeString(Path.of(testFile), "ABCDEFGHIJKLMNOPQRSTUVWXYZ");

        // ────────────────────────────────────────────────────────────────
        // BufferedInputStream supports mark/reset
        // ────────────────────────────────────────────────────────────────
        System.out.println("1. mark() and reset() with BufferedInputStream:");
        try (BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(testFile))) {

            // Check if mark is supported
            System.out.println("   markSupported(): " + bis.markSupported());  // true

            // Read first 3 bytes
            System.out.println("   Read: " + (char) bis.read());  // A
            System.out.println("   Read: " + (char) bis.read());  // B
            System.out.println("   Read: " + (char) bis.read());  // C

            // Mark current position (can read 10 more bytes before mark invalid)
            bis.mark(10);
            System.out.println("   Marked position");

            // Read next 3 bytes
            System.out.println("   Read: " + (char) bis.read());  // D
            System.out.println("   Read: " + (char) bis.read());  // E
            System.out.println("   Read: " + (char) bis.read());  // F

            // Reset back to mark
            bis.reset();
            System.out.println("   Reset to mark");

            // Read again (starts from D again)
            System.out.println("   Read: " + (char) bis.read());  // D
            System.out.println("   Read: " + (char) bis.read());  // E
        }

        // ────────────────────────────────────────────────────────────────
        // FileInputStream does NOT support mark/reset
        // ────────────────────────────────────────────────────────────────
        System.out.println("\n2. FileInputStream does NOT support mark:");
        try (FileInputStream fis = new FileInputStream(testFile)) {
            System.out.println("   markSupported(): " + fis.markSupported());  // false
        }

        // ────────────────────────────────────────────────────────────────
        // skip() - works on all streams
        // ────────────────────────────────────────────────────────────────
        System.out.println("\n3. skip() method:");
        try (FileInputStream fis = new FileInputStream(testFile)) {
            System.out.println("   Read: " + (char) fis.read());  // A
            long skipped = fis.skip(5);  // Skip B, C, D, E, F
            System.out.println("   Skipped " + skipped + " bytes");
            System.out.println("   Read: " + (char) fis.read());  // G
        }

        Files.deleteIfExists(Path.of(testFile));

        System.out.println("\nKey points:");
        System.out.println("  - Only BUFFERED streams support mark/reset");
        System.out.println("  - Check markSupported() before using mark/reset");
        System.out.println("  - skip() works on ALL streams");
        System.out.println("  - skip() returns long (actual bytes skipped)");
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // File Attributes: Checking Accessibility
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateFileAccessibility() throws IOException {
        System.out.println("=== File Attributes: Checking Accessibility ===\n");

        // Create test files
        Path regularFile = Path.of("test-file.txt");
        Path directory = Path.of("test-dir");

        Files.writeString(regularFile, "Test content");
        Files.createDirectories(directory);

        System.out.println("Checking: " + regularFile);
        System.out.println("  isRegularFile():  " + Files.isRegularFile(regularFile));
        System.out.println("  isDirectory():    " + Files.isDirectory(regularFile));
        System.out.println("  isSymbolicLink(): " + Files.isSymbolicLink(regularFile));
        System.out.println("  isReadable():     " + Files.isReadable(regularFile));
        System.out.println("  isWritable():     " + Files.isWritable(regularFile));
        System.out.println("  isExecutable():   " + Files.isExecutable(regularFile));
        System.out.println("  isHidden():       " + Files.isHidden(regularFile));

        System.out.println("\nChecking: " + directory);
        System.out.println("  isRegularFile():  " + Files.isRegularFile(directory));
        System.out.println("  isDirectory():    " + Files.isDirectory(directory));
        System.out.println("  isSymbolicLink(): " + Files.isSymbolicLink(directory));

        // Clean up
        Files.deleteIfExists(regularFile);
        Files.deleteIfExists(directory);

        System.out.println("\nEXAM TRAPS:");
        System.out.println("  - isHidden() throws IOException (others don't!)");
        System.out.println("  - isSymbolicLink() has NO LinkOption parameter");
        System.out.println("  - Others have LinkOption... parameter");
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Reading File Attributes
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateReadingAttributes() throws IOException {
        System.out.println("=== Reading File Attributes ===\n");

        Path file = Path.of("attributes-test.txt");
        Files.writeString(file, "Test content for attributes");

        // Read BasicFileAttributes
        System.out.println("BasicFileAttributes:");
        BasicFileAttributes attrs = Files.readAttributes(
                file,
                BasicFileAttributes.class
        );

        System.out.println("  Size:              " + attrs.size() + " bytes");
        System.out.println("  Creation time:     " + attrs.creationTime());
        System.out.println("  Last modified:     " + attrs.lastModifiedTime());
        System.out.println("  Last access time:  " + attrs.lastAccessTime());
        System.out.println("  Is directory:      " + attrs.isDirectory());
        System.out.println("  Is regular file:   " + attrs.isRegularFile());
        System.out.println("  Is symbolic link:  " + attrs.isSymbolicLink());
        System.out.println("  Is other:          " + attrs.isOther());

        Files.deleteIfExists(file);

        System.out.println("\nKey points:");
        System.out.println("  - readAttributes() reads ALL attributes in single I/O operation");
        System.out.println("  - More efficient than multiple individual calls");
        System.out.println("  - Returns BasicFileAttributes object");
        System.out.println("  - Platform-specific: DosFileAttributes, PosixFileAttributes");
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Modifying File Attributes
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateModifyingAttributes() throws IOException {
        System.out.println("=== Modifying File Attributes ===\n");

        Path file = Path.of("modify-attributes.txt");
        Files.writeString(file, "Test");

        // Read current times
        BasicFileAttributes before = Files.readAttributes(
                file,
                BasicFileAttributes.class
        );
        System.out.println("Before modification:");
        System.out.println("  Last modified: " + before.lastModifiedTime());
        System.out.println("  Last access:   " + before.lastAccessTime());
        System.out.println("  Creation time: " + before.creationTime());

        // Method 1: setLastModifiedTime()
        FileTime newModifiedTime = FileTime.from(Instant.now().minusSeconds(3600));
        Files.setLastModifiedTime(file, newModifiedTime);
        System.out.println("\nSet last modified time to 1 hour ago");

        // Method 2: setTimes() using BasicFileAttributeView
        BasicFileAttributeView view = Files.getFileAttributeView(
                file,
                BasicFileAttributeView.class
        );

        FileTime newAccessTime = FileTime.from(Instant.now().minusSeconds(7200));
        view.setTimes(
                null,            // lastModifiedTime (null = keep current)
                newAccessTime,   // lastAccessTime
                null             // createTime (null = keep current)
        );
        System.out.println("Set last access time to 2 hours ago");

        // Read after modification
        BasicFileAttributes after = Files.readAttributes(
                file,
                BasicFileAttributes.class
        );
        System.out.println("\nAfter modification:");
        System.out.println("  Last modified: " + after.lastModifiedTime());
        System.out.println("  Last access:   " + after.lastAccessTime());
        System.out.println("  Creation time: " + after.creationTime());

        Files.deleteIfExists(file);

        System.out.println("\nKey methods:");
        System.out.println("  Files.setLastModifiedTime(Path, FileTime)");
        System.out.println("  Files.getFileAttributeView(Path, Class<V>)");
        System.out.println("  BasicFileAttributeView.setTimes(modified, access, create)");
        System.out.println("    (pass null to keep current value)");
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Traversing Directory Tree: walk()
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateWalk() throws IOException {
        System.out.println("=== Traversing Directory Tree: walk() ===\n");

        // Create test directory structure
        Path root = Path.of("walk-test");
        Files.createDirectories(root.resolve("a"));
        Files.createDirectories(root.resolve("b/c"));
        Files.writeString(root.resolve("file1.txt"), "1");
        Files.writeString(root.resolve("a/file2.txt"), "2");
        Files.writeString(root.resolve("b/file3.txt"), "3");
        Files.writeString(root.resolve("b/c/file4.txt"), "4");

        System.out.println("Directory structure:");
        System.out.println("  walk-test/");
        System.out.println("    file1.txt");
        System.out.println("    a/");
        System.out.println("      file2.txt");
        System.out.println("    b/");
        System.out.println("      file3.txt");
        System.out.println("      c/");
        System.out.println("        file4.txt");

        // walk() with no depth limit
        System.out.println("\n1. walk(Path) - no depth limit:");
        try (Stream<Path> stream = Files.walk(root)) {
            stream.forEach(path -> System.out.println("  " + path));
        }

        // walk() with maxDepth
        System.out.println("\n2. walk(Path, maxDepth) - maxDepth = 1:");
        try (Stream<Path> stream = Files.walk(root, 1)) {
            stream.forEach(path -> System.out.println("  " + path));
        }

        System.out.println("\n3. walk(Path, maxDepth) - maxDepth = 2:");
        try (Stream<Path> stream = Files.walk(root, 2)) {
            stream.forEach(path -> System.out.println("  " + path));
        }

        // Using Stream operations
        System.out.println("\n4. Finding .txt files using walk():");
        try (Stream<Path> stream = Files.walk(root)) {
            stream.filter(path -> path.toString().endsWith(".txt"))
                  .forEach(path -> System.out.println("  " + path));
        }

        // Clean up
        deleteRecursive(root);

        System.out.println("\nKey points:");
        System.out.println("  - walk() uses DEPTH-FIRST search");
        System.out.println("  - Returns Stream<Path> (must close!)");
        System.out.println("  - Default: does NOT follow symbolic links");
        System.out.println("  - maxDepth = 0: only start path");
        System.out.println("  - maxDepth = 1: start + direct children");
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CRITICAL: find() method with BiPredicate
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateFind() throws IOException {
        System.out.println("=== CRITICAL: find() Method with BiPredicate ===\n");

        // Create test directory
        Path root = Path.of("find-test");
        Files.createDirectories(root.resolve("dir1"));
        Files.writeString(root.resolve("test.txt"), "1");
        Files.writeString(root.resolve("data.csv"), "2");
        Files.writeString(root.resolve("dir1/test.txt"), "3");
        Files.writeString(root.resolve("dir1/readme.md"), "4");

        System.out.println("Directory structure:");
        System.out.println("  find-test/");
        System.out.println("    test.txt");
        System.out.println("    data.csv");
        System.out.println("    dir1/");
        System.out.println("      test.txt");
        System.out.println("      readme.md");

        // Example 1: Find all .txt files
        System.out.println("\n1. Find all .txt files:");
        try (Stream<Path> stream = Files.find(
                root,
                Integer.MAX_VALUE,
                (path, attrs) -> path.toString().endsWith(".txt")  // BiPredicate!
        )) {
            stream.forEach(path -> System.out.println("  " + path));
        }

        // Example 2: Find all regular files
        System.out.println("\n2. Find all regular files:");
        try (Stream<Path> stream = Files.find(
                root,
                Integer.MAX_VALUE,
                (path, attrs) -> attrs.isRegularFile()  // Using BasicFileAttributes!
        )) {
            stream.forEach(path -> System.out.println("  " + path));
        }

        // Example 3: Find files larger than 0 bytes
        System.out.println("\n3. Find files larger than 0 bytes:");
        try (Stream<Path> stream = Files.find(
                root,
                Integer.MAX_VALUE,
                (path, attrs) -> attrs.isRegularFile() && attrs.size() > 0
        )) {
            stream.forEach(path -> System.out.println("  " + path));
        }

        // Example 4: Find with maxDepth = 1
        System.out.println("\n4. Find .txt files with maxDepth = 1:");
        try (Stream<Path> stream = Files.find(
                root,
                1,  // Only direct children
                (path, attrs) -> path.toString().endsWith(".txt")
        )) {
            stream.forEach(path -> System.out.println("  " + path));
        }

        // Example 5: Complex Stream pipeline
        System.out.println("\n5. Complex Stream pipeline:");
        try (Stream<Path> stream = Files.find(
                root,
                Integer.MAX_VALUE,
                (path, attrs) -> attrs.isRegularFile()
        )) {
            long count = stream
                    .filter(path -> path.toString().endsWith(".txt"))  // Predicate<Path>
                    .map(Path::getFileName)
                    .peek(name -> System.out.println("  Found: " + name))
                    .count();
            System.out.println("  Total: " + count);
        }

        // Clean up
        deleteRecursive(root);

        System.out.println("\nKey signature:");
        System.out.println("  Stream<Path> find(");
        System.out.println("      Path start,");
        System.out.println("      int maxDepth,");
        System.out.println("      BiPredicate<Path, BasicFileAttributes> matcher,");
        System.out.println("      FileVisitOption... options");
        System.out.println("  )");
        System.out.println();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // EXAM TRAPS: Common compilation errors with find()
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateFindExamTraps() {
        System.out.println("=== EXAM TRAPS: find() Compilation Errors ===\n");

        System.out.println("EXAM LOVES TO TEST: BiPredicate parameter confusion!");
        System.out.println();

        System.out.println("✓ VALID - BiPredicate with two parameters:");
        System.out.println("  Files.find(path, 10, (p, a) -> p.toString().endsWith(\".txt\"))");
        System.out.println();

        System.out.println("✗ DOES NOT COMPILE - Only one parameter:");
        System.out.println("  Files.find(path, 10, p -> p.toString().endsWith(\".txt\"))");
        System.out.println("  // BiPredicate requires TWO parameters!");
        System.out.println();

        System.out.println("✓ VALID - Using both parameters:");
        System.out.println("  Files.find(path, 10, (p, a) -> a.isRegularFile())");
        System.out.println();

        System.out.println("✓ VALID - Using path only (but must accept both):");
        System.out.println("  Files.find(path, 10, (p, a) -> p.getFileName().toString().equals(\"test\"))");
        System.out.println();

        System.out.println("✗ DOES NOT COMPILE - Wrong lambda signature:");
        System.out.println("  Files.find(path, 10, path -> Files.isRegularFile(path))");
        System.out.println("  // Must accept TWO parameters!");
        System.out.println();

        System.out.println("✓ VALID - Stream operations AFTER find():");
        System.out.println("  try (Stream<Path> s = Files.find(path, 10, (p, a) -> true)) {");
        System.out.println("      s.filter(p -> p.toString().contains(\"test\"))  // Predicate<Path>");
        System.out.println("       .forEach(System.out::println);");
        System.out.println("  }");
        System.out.println();

        System.out.println("✗ DOES NOT COMPILE - Wrong return type:");
        System.out.println("  Stream<BasicFileAttributes> s = Files.find(...);");
        System.out.println("  // Returns Stream<Path>, not Stream<BasicFileAttributes>!");
        System.out.println();

        System.out.println("✗ RESOURCE LEAK - Stream not closed:");
        System.out.println("  Stream<Path> s = Files.find(...);");
        System.out.println("  s.forEach(...);  // Should use try-with-resources!");
        System.out.println();

        System.out.println("KEY EXAM TIPS:");
        System.out.println("  1. BiPredicate ALWAYS takes TWO parameters");
        System.out.println("  2. Parameters are (Path, BasicFileAttributes)");
        System.out.println("  3. Returns Stream<Path>, not Stream<BasicFileAttributes>");
        System.out.println("  4. Stream MUST be closed (try-with-resources)");
        System.out.println("  5. After find(), Stream operations use Predicate<Path>");
        System.out.println("  6. In find(), lambda must accept BOTH parameters");
        System.out.println();

        System.out.println("COMMON EXAM TRICK:");
        System.out.println("  Show code mixing find() BiPredicate with Stream filter() Predicate");
        System.out.println("  Example that DOES NOT COMPILE:");
        System.out.println("    Files.find(path, 10, p -> true)  // ✗ Wrong parameter count");
        System.out.println("         .filter((p, a) -> ...)      // ✗ filter() takes Predicate<Path>");
        System.out.println();
    }

    // Helper method to delete directory recursively
    private static void deleteRecursive(Path path) throws IOException {
        if (Files.exists(path)) {
            try (Stream<Path> stream = Files.walk(path)) {
                stream.sorted((a, b) -> b.compareTo(a))  // Delete children before parents
                      .forEach(p -> {
                          try {
                              Files.delete(p);
                          } catch (IOException e) {
                              // Ignore
                          }
                      });
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// QUICK REFERENCE - METHOD SIGNATURES
// ═══════════════════════════════════════════════════════════════════════════
//
// STREAM MANIPULATION:
//   boolean markSupported()
//   void mark(int readAheadLimit)
//   void reset() throws IOException
//   long skip(long n) throws IOException
//
// FILE ACCESSIBILITY:
//   boolean isDirectory(Path, LinkOption...)
//   boolean isSymbolicLink(Path)                    // NO LinkOption!
//   boolean isRegularFile(Path, LinkOption...)
//   boolean isHidden(Path) throws IOException       // Throws IOException!
//   boolean isReadable(Path)
//   boolean isWritable(Path)
//   boolean isExecutable(Path)
//
// READING ATTRIBUTES:
//   <A extends BasicFileAttributes> A readAttributes(
//       Path path,
//       Class<A> type,
//       LinkOption... options
//   ) throws IOException
//
// MODIFYING ATTRIBUTES:
//   Path setLastModifiedTime(Path, FileTime) throws IOException
//   BasicFileAttributeView getFileAttributeView(Path, Class<V>, LinkOption...)
//   void setTimes(FileTime modified, FileTime access, FileTime create)
//
// TRAVERSING DIRECTORIES:
//   Stream<Path> walk(Path start, FileVisitOption...) throws IOException
//   Stream<Path> walk(Path start, int maxDepth, FileVisitOption...) throws IOException
//
//   Stream<Path> find(
//       Path start,
//       int maxDepth,
//       BiPredicate<Path, BasicFileAttributes> matcher,
//       FileVisitOption... options
//   ) throws IOException
//
// ═══════════════════════════════════════════════════════════════════════════
