package ch14io;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * PATH API METHODS - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * PATH INTERFACE - java.nio.file.Path
 * ═══════════════════════════════════════════════════════════════════════════
 * Represents a file or directory path in the file system.
 * Path objects are IMMUTABLE - all methods return new Path objects.
 *
 * Path is an INTERFACE, not a class.
 * Created using static factory methods: Path.of(), Paths.get(), FileSystems.getDefault().getPath()
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * PATH STRUCTURE AND INDICES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Example path: /home/user/documents/file.txt
 *
 * Visual breakdown:
 *   /home/user/documents/file.txt
 *   │ └┬─┘ └┬─┘ └───┬────┘ └──┬──┘
 *   │  0    1       2         3      ← getName(index) indices
 *   │
 *   └─ Root (not included in name count or indices)
 *
 * getNameCount() = 4 (does NOT include root)
 * getName(0) = "home"
 * getName(1) = "user"
 * getName(2) = "documents"
 * getName(3) = "file.txt"
 * getFileName() = "file.txt" (same as last name element)
 * getParent() = /home/user/documents
 * getRoot() = /
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CRITICAL EXAM RULES
 * ═══════════════════════════════════════════════════════════════════════════
 * 1. Root is NEVER included in name count or getName() indices
 * 2. getName() indices start at 0 from the first element AFTER root
 * 3. getFileName() returns the LAST element (same as getName(getNameCount()-1))
 * 4. getParent() returns everything EXCEPT the last element
 * 5. Relative paths have NO root (getRoot() returns null)
 * 6. resolve() with ABSOLUTE path argument RETURNS the argument (ignores caller) (think concatenation)
 * 7. relativize() requires BOTH paths to be absolute OR BOTH relative
 * 8. normalize() removes redundant "." and ".." but does NOT resolve symbolic links
 * 9. toRealPath() REQUIRES path to exist, resolves symbolic links, returns absolute path
 * 10. All Path methods return NEW Path objects (immutable)
 * 11. subpath and getName() throw IllegalArgumentException if given an invalid path
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * PATH METHOD SIGNATURES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * String toString()                                    Returns string representation
 * Path getName(int index)                              Returns name element at index
 * int getNameCount()                                   Returns number of name elements
 * Path subpath(int beginIndex, int endIndex)           Returns subsequence of names
 * Path getFileName()                                   Returns last element
 * Path getParent()                                     Returns parent path (null if none)
 * Path getRoot()                                       Returns root (null if relative)
 * Path resolve(Path other)                             Joins paths
 * Path resolve(String other)                           Joins paths
 * Path relativize(Path other)                          Creates relative path from this to other
 * Path normalize()                                     Removes redundant . and ..
 * Path toRealPath(LinkOption... options) throws IOException    Resolves to actual path
 *
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class PathMethods {

    public static void main(String[] args) throws IOException {
        // ────────────────────────────────────────────────────────────────────
        // toString() - String representation
        // ────────────────────────────────────────────────────────────────────
        demonstrateToString();

        // ────────────────────────────────────────────────────────────────────
        // getName(int index) - Get name element at index
        // ────────────────────────────────────────────────────────────────────
        demonstrateGetName();

        // ────────────────────────────────────────────────────────────────────
        // getNameCount() - Number of name elements
        // ────────────────────────────────────────────────────────────────────
        demonstrateGetNameCount();

        // ────────────────────────────────────────────────────────────────────
        // subpath(int beginIndex, int endIndex) - Subsequence of names
        // ────────────────────────────────────────────────────────────────────
        demonstrateSubpath();

        // ────────────────────────────────────────────────────────────────────
        // getFileName() - Last element
        // ────────────────────────────────────────────────────────────────────
        demonstrateGetFileName();

        // ────────────────────────────────────────────────────────────────────
        // getParent() - Parent path
        // ────────────────────────────────────────────────────────────────────
        demonstrateGetParent();

        // ────────────────────────────────────────────────────────────────────
        // getRoot() - Root component
        // ────────────────────────────────────────────────────────────────────
        demonstrateGetRoot();

        // ────────────────────────────────────────────────────────────────────
        // resolve(Path) and resolve(String) - Join paths
        // ────────────────────────────────────────────────────────────────────
        demonstrateResolve();

        // ────────────────────────────────────────────────────────────────────
        // relativize(Path) - Create relative path
        // ────────────────────────────────────────────────────────────────────
        demonstrateRelativize();

        // ────────────────────────────────────────────────────────────────────
        // normalize() - Remove redundant elements
        // ────────────────────────────────────────────────────────────────────
        demonstrateNormalize();

        // ────────────────────────────────────────────────────────────────────
        // toRealPath(LinkOption...) - Resolve to actual path
        // ────────────────────────────────────────────────────────────────────
        demonstrateToRealPath();

        System.out.println("\n✓ All Path method examples completed");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // toString() - Returns string representation
    // ═══════════════════════════════════════════════════════════════════════════
    //
    // Returns the string representation of the path.
    // This is the same as the original string used to create the Path.
    //
    private static void demonstrateToString() {
        System.out.println("\n=== toString() ===");

        Path p1 = Path.of("/home/user/file.txt");
        System.out.println("Path: " + p1.toString());  // /home/user/file.txt

        Path p2 = Path.of("documents/file.txt");
        System.out.println("Path: " + p2.toString());  // documents/file.txt

        // toString() is called implicitly when printing
        System.out.println("Path: " + p1);  // Same as p1.toString()
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // getName(int index) - Get name element at index
    // ═══════════════════════════════════════════════════════════════════════════
    //
    // Returns the name element at the specified index.
    // Indices start at 0 for the FIRST element (not the root).
    // Root is NEVER included in indices.
    //
    // EXAM TRAP: Remember root is NOT included in getName() indices!
    //
    private static void demonstrateGetName() {
        System.out.println("\n=== getName(int index) ===");

        // Absolute path: /home/user/documents/file.txt
        //                 └─0─┘ └─1┘ └────2────┘ └──3──┘
        Path absolute = Path.of("/home/user/documents/file.txt");
        System.out.println("Path: " + absolute);
        System.out.println("getName(0): " + absolute.getName(0));  // home
        System.out.println("getName(1): " + absolute.getName(1));  // user
        System.out.println("getName(2): " + absolute.getName(2));  // documents
        System.out.println("getName(3): " + absolute.getName(3));  // file.txt

        // Relative path: documents/file.txt
        //                └────0───┘ └──1──┘
        Path relative = Path.of("documents/file.txt");
        System.out.println("\nPath: " + relative);
        System.out.println("getName(0): " + relative.getName(0));  // documents
        System.out.println("getName(1): " + relative.getName(1));  // file.txt

        // EXAM TRAP: IllegalArgumentException if index >= getNameCount()
//         System.out.println(relative.getName(2));  // ✗ IllegalArgumentException

        // Edge case: Single element path
        Path single = Path.of("file.txt");
        System.out.println("\nPath: " + single);
        System.out.println("getName(0): " + single.getName(0));  // file.txt
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // getNameCount() - Number of name elements
    // ═══════════════════════════════════════════════════════════════════════════
    //
    // Returns the number of name elements in the path.
    // Root is NOT included in the count.
    //
    private static void demonstrateGetNameCount() {
        System.out.println("\n=== getNameCount() ===");

        Path p1 = Path.of("/home/user/documents/file.txt");
        System.out.println("Path: " + p1);
        System.out.println("Name count: " + p1.getNameCount());  // 4 (root not counted)

        Path p2 = Path.of("documents/file.txt");
        System.out.println("\nPath: " + p2);
        System.out.println("Name count: " + p2.getNameCount());  // 2

        Path p3 = Path.of("/file.txt");
        System.out.println("\nPath: " + p3);
        System.out.println("Name count: " + p3.getNameCount());  // 1

        // Edge case: Root only
        Path root = Path.of("/");
        System.out.println("\nPath: " + root);
        System.out.println("Name count: " + root.getNameCount());  // 0 (only root, no names)
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // subpath(int beginIndex, int endIndex) - Subsequence of names
    // ═══════════════════════════════════════════════════════════════════════════
    //
    // Returns a subsequence of name elements.
    // beginIndex is INCLUSIVE, endIndex is EXCLUSIVE (like substring).
    // Does NOT include the root.
    // Returned path is ALWAYS relative (no root).
    //
    // EXAM TRAP: endIndex is exclusive, not inclusive! Can give length of path from getNameCount because
    // end index is exclusive. Reference below examples.
    // EXAM TRAP: Returned path is ALWAYS relative, even if original was absolute!
    //
    private static void demonstrateSubpath() {
        System.out.println("\n=== subpath(int beginIndex, int endIndex) ===");

        // Path: /home/user/documents/file.txt
        //        └─0─┘ └─1┘ └────2────┘ └──3──┘
        Path path = Path.of("/home/user/documents/file.txt");
        System.out.println("Original path: " + path);

        System.out.println("subpath(0, 1): " + path.subpath(0, 1));  // home (relative!)
        System.out.println("subpath(0, 2): " + path.subpath(0, 2));  // home/user
        System.out.println("subpath(1, 3): " + path.subpath(1, 3));  // user/documents
        System.out.println("subpath(2, 4): " + path.subpath(2, 4));  // documents/file.txt

        // EXAM TRAP: Result is ALWAYS relative (no leading /)
        Path sub = path.subpath(0, 2);
        System.out.println("\nIs subpath absolute? " + sub.isAbsolute());  // false

        // EXAM TRAP: IllegalArgumentException if indices are invalid
        // path.subpath(0, 0);    // ✗ IllegalArgumentException (empty range)
        // path.subpath(2, 1);    // ✗ IllegalArgumentException (begin > end)
        // path.subpath(0, 5);    // ✗ IllegalArgumentException (end > count)
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // getFileName() - Last element
    // ═══════════════════════════════════════════════════════════════════════════
    //
    // Returns the name element that is FARTHEST from the root.
    // Equivalent to getName(getNameCount() - 1).
    // Returns null if path is empty or represents root only.
    //
    private static void demonstrateGetFileName() {
        System.out.println("\n=== getFileName() ===");

        Path p1 = Path.of("/home/user/documents/file.txt");
        System.out.println("Path: " + p1);
        System.out.println("File name: " + p1.getFileName());  // file.txt

        Path p2 = Path.of("documents/file.txt");
        System.out.println("\nPath: " + p2);
        System.out.println("File name: " + p2.getFileName());  // file.txt

        // Works even if "file name" is actually a directory
        Path p3 = Path.of("/home/user/documents");
        System.out.println("\nPath: " + p3);
        System.out.println("File name: " + p3.getFileName());  // documents

        // Edge case: Root only returns null
        Path root = Path.of("/");
        System.out.println("\nPath: " + root);
        System.out.println("File name: " + root.getFileName());  // null

        // Edge case: Single element
        Path single = Path.of("file.txt");
        System.out.println("\nPath: " + single);
        System.out.println("File name: " + single.getFileName());  // file.txt
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // getParent() - Parent path
    // ═══════════════════════════════════════════════════════════════════════════
    //
    // Returns the parent path, or null if there is no parent.
    // Parent is everything EXCEPT the last element.
    // For absolute paths, includes the root.
    // For relative paths, does not include root (there is none).
    //
    // EXAM TRAP: Returns null if no parent exists!
    //
    private static void demonstrateGetParent() {
        System.out.println("\n=== getParent() ===");

        Path p1 = Path.of("/home/.././user/documents/file.txt");
        System.out.println("Path: " + p1);
        System.out.println("Parent: " + p1.getParent());  // /home/.././user/documents

        Path p2 = Path.of("documents/file.txt");
        System.out.println("\nPath: " + p2);
        System.out.println("Parent: " + p2.getParent());  // documents

        // Walking up the tree
        Path p3 = Path.of("/home/user/file.txt");
        System.out.println("\nPath: " + p3);
        System.out.println("Parent: " + p3.getParent());          // /home/user
        System.out.println("Grandparent: " + p3.getParent().getParent());  // /home
        System.out.println("Great-grandparent: " + p3.getParent().getParent().getParent());  // /

        // EXAM TRAP: Root's parent is null
        Path root = Path.of("/");
        System.out.println("\nPath: " + root);
        System.out.println("Parent: " + root.getParent());  // null

        // EXAM TRAP: Single element relative path has null parent
        Path single = Path.of("file.txt");
        System.out.println("\nPath: " + single);
        System.out.println("Parent: " + single.getParent());  // null
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // getRoot() - Root component
    // ═══════════════════════════════════════════════════════════════════════════
    //
    // Returns the root component of the path.
    // Returns null for relative paths.
    //
    // On Unix/Linux: "/" (single slash)
    // On Windows: "C:\", "D:\", etc.
    //
    private static void demonstrateGetRoot() {
        System.out.println("\n=== getRoot() ===");

        Path absolute = Path.of("/home/user/file.txt");
        System.out.println("Path: " + absolute);
        System.out.println("Root: " + absolute.getRoot());  // /
        System.out.println("Is absolute? " + absolute.isAbsolute());  // true

        Path relative = Path.of("documents/file.txt");
        System.out.println("\nPath: " + relative);
        System.out.println("Root: " + relative.getRoot());  // null
        System.out.println("Is absolute? " + relative.isAbsolute());  // false

        // Root only
        Path root = Path.of("/");
        System.out.println("\nPath: " + root);
        System.out.println("Root: " + root.getRoot());  // /
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // resolve(Path) and resolve(String) - Join paths
    // ═══════════════════════════════════════════════════════════════════════════
    // Think concatenation when you see this method. resolve() does not clean up path symbols
    // The object on which the resolve() method is invoked becomes the basis for the new Path
    // object, with the input argument being appended onto the Path.
    //
    // If the input parameter is an absolute path, the output wil be that absolute path. **
    //
    // Joins this path with another path.
    // Behavior depends on whether argument is absolute or relative:
    //
    // 1. If argument is ABSOLUTE: returns the argument (ignores caller)
    // 2. If argument is RELATIVE: appends argument to caller
    //
    // EXAM TRAP: Absolute argument causes caller to be IGNORED!
    // EXAM TRAP: Empty path argument returns caller unchanged!
    //
    private static void demonstrateResolve() {
        System.out.println("\n=== resolve(Path) and resolve(String) ===");

        // Case 1: Absolute path resolves relative path (common case)
        Path base = Path.of("/home/user");
        Path relative = Path.of("documents/file.txt");
        System.out.println("Base: " + base);
        System.out.println("Relative: " + relative);
        System.out.println("Resolved: " + base.resolve(relative));  // /home/user/documents/file.txt

        // Case 2: Relative path resolves relative path
        Path rel1 = Path.of("documents");
        Path rel2 = Path.of("file.txt");
        System.out.println("\nPath 1: " + rel1);
        System.out.println("Path 2: " + rel2);
        System.out.println("Resolved: " + rel1.resolve(rel2));  // documents/file.txt

        // EXAM TRAP: Absolute argument REPLACES caller (ignores it)
        Path abs1 = Path.of("/home/user");
        Path abs2 = Path.of("/etc/config.txt");
        System.out.println("\nPath 1: " + abs1);
        System.out.println("Path 2 (absolute): " + abs2);
        System.out.println("Resolved: " + abs1.resolve(abs2));  // /etc/config.txt (caller ignored!)

        // String overload works the same way
        System.out.println("\nResolve with String:");
        System.out.println(base.resolve("documents/file.txt"));  // /home/user/documents/file.txt

        // Edge case: Empty path returns caller unchanged
        System.out.println("\nResolve empty path:");
        System.out.println(base.resolve(""));  // /home/user

        // Practical example: Building file paths
        Path directory = Path.of("/var/log");
        Path logFile = directory.resolve("application.log");
        System.out.println("\nDirectory: " + directory);
        System.out.println("Log file: " + logFile);  // /var/log/application.log
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // relativize(Path) - Create relative path
    // ═══════════════════════════════════════════════════════════════════════════
    //
    // Constructs a relative path from this path to the given path.
    // Answer the question: "How do I get from THIS path to OTHER path?"
    //
    // CRITICAL RULES:
    // 1. BOTH paths must be absolute OR BOTH must be relative
    // 2. Throws IllegalArgumentException if one is absolute and one is relative
    // 3. Result is ALWAYS a relative path
    // 4. Uses ".." to go up directories
    //
    // EXAM TRAP: Cannot mix absolute and relative paths!
    // EXAM TRAP: Order matters! p1.relativize(p2) != p2.relativize(p1)
    //
    private static void demonstrateRelativize() {
        System.out.println("\n=== relativize(Path) ===");

        // Case 1: Both absolute paths
        Path abs1 = Path.of("/home/user/documents");
        Path abs2 = Path.of("/home/user/pictures/photo.jpg");
        System.out.println("From: " + abs1);
        System.out.println("To: " + abs2);
        System.out.println("Relative path: " + abs1.relativize(abs2));  // ../pictures/photo.jpg

        // Verify: If we resolve the result with the start path, we get the target
        System.out.println("Verification: " + abs1.resolve(abs1.relativize(abs2)));  // /home/user/pictures/photo.jpg

        // Case 2: Both relative paths
        Path rel1 = Path.of("documents");
        Path rel2 = Path.of("pictures/photo.jpg");
        System.out.println("\nFrom: " + rel1);
        System.out.println("To: " + rel2);
        System.out.println("Relative path: " + rel1.relativize(rel2));  // ../pictures/photo.jpg

        // Case 3: Same directory
        Path same1 = Path.of("/home/user/file1.txt");
        Path same2 = Path.of("/home/user/file2.txt");
        System.out.println("\nFrom: " + same1);
        System.out.println("To: " + same2);
        System.out.println("Relative path: " + same1.relativize(same2));  // ../file2.txt

        // Case 4: Going up multiple levels
        Path deep = Path.of("/home/user/documents/work/project");
        Path high = Path.of("/home/user/pictures");
        System.out.println("\nFrom: " + deep);
        System.out.println("To: " + high);
        System.out.println("Relative path: " + deep.relativize(high));  // ../../../pictures

        // EXAM TRAP: Order matters!
        System.out.println("\nReversed:");
        System.out.println("From: " + high);
        System.out.println("To: " + deep);
        System.out.println("Relative path: " + high.relativize(deep));  // ../documents/work/project

        // EXAM TRAP: Cannot mix absolute and relative
        // Path abs = Path.of("/home/user");
        // Path rel = Path.of("documents");
        // abs.relativize(rel);  // ✗ IllegalArgumentException

        // Case 5: Same path
        Path p = Path.of("/home/user");
        System.out.println("\nSame path relativize:");
        System.out.println(p.relativize(p));  // (empty path)
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // normalize() - Remove redundant elements
    // ═══════════════════════════════════════════════════════════════════════════
    //
    // Removes redundant "." and ".." elements from the path.
    // "." means current directory
    // ".." means parent directory
    //
    // Does NOT access the file system.
    // Does NOT resolve symbolic links.
    // Does NOT verify the path exists.
    //
    // EXAM TRAP: normalize() does NOT resolve symbolic links!
    // EXAM TRAP: Does NOT require path to exist!
    //
    private static void demonstrateNormalize() {
        System.out.println("\n=== normalize() ===");

        // Remove current directory references (.)
        Path p1 = Path.of("/home/./user/./file.txt");
        System.out.println("Original: " + p1);
        System.out.println("Normalized: " + p1.normalize());  // /home/user/file.txt

        // Remove parent directory references (..)
        Path p2 = Path.of("/home/user/../admin/file.txt");
        System.out.println("\nOriginal: " + p2);
        System.out.println("Normalized: " + p2.normalize());  // /home/admin/file.txt

        // Complex example
        Path p3 = Path.of("/home/user/documents/../pictures/./photo.jpg");
        System.out.println("\nOriginal: " + p3);
        System.out.println("Normalized: " + p3.normalize());  // /home/user/pictures/photo.jpg

        // Multiple parent references
        Path p4 = Path.of("/home/user/docs/../../admin/file.txt");
        System.out.println("\nOriginal: " + p4);
        System.out.println("Normalized: " + p4.normalize());  // /home/admin/file.txt

        // Relative path normalization
        Path p5 = Path.of("documents/./work/../pictures/photo.jpg");
        System.out.println("\nOriginal: " + p5);
        System.out.println("Normalized: " + p5.normalize());  // documents/pictures/photo.jpg

        // EXAM TRAP: Does NOT require path to exist
        Path nonExistent = Path.of("/fake/path/../other/file.txt");
        System.out.println("\nNon-existent path: " + nonExistent);
        System.out.println("Normalized: " + nonExistent.normalize());  // /fake/other/file.txt (no exception!)

        // Edge case: Too many .. at the start (relative path)
        Path tooMany = Path.of("../../../file.txt");
        System.out.println("\nOriginal: " + tooMany);
        System.out.println("Normalized: " + tooMany.normalize());  // ../../../file.txt (unchanged)

        // normalize() returns a new Path (immutable)
        Path original = Path.of("/home/./user");
        Path normalized = original.normalize();
        System.out.println("\nOriginal unchanged: " + original);  // /home/./user
        System.out.println("Normalized result: " + normalized);   // /home/user
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // toRealPath(LinkOption...) - Resolve to actual path
    // ═══════════════════════════════════════════════════════════════════════════
    //
    // Returns the REAL path by:
    // 1. Making path absolute (if relative)
    // 2. Resolving symbolic links (unless NOFOLLOW_LINKS specified)
    // 3. Normalizing the path (removing . and ..)
    //
    // REQUIRES the path to EXIST (throws IOException if not).
    //
    // LinkOption.NOFOLLOW_LINKS: Don't follow symbolic links
    //
    // EXAM TRAP: Requires path to exist! (throws IOException)
    // EXAM TRAP: Follows symbolic links by default!
    //
    private static void demonstrateToRealPath() {
        System.out.println("\n=== toRealPath(LinkOption...) ===");

        try {
            // Create a test file that exists
            Path testFile = Path.of("test-file.txt");
            java.nio.file.Files.createFile(testFile);

            // Convert relative path to real absolute path
            Path real = testFile.toRealPath();
            System.out.println("Original: " + testFile);
            System.out.println("Real path: " + real);
            System.out.println("Is absolute? " + real.isAbsolute());  // true

            // Clean up
            java.nio.file.Files.deleteIfExists(testFile);

        } catch (IOException e) {
            System.out.println("Could not create test file: " + e.getMessage());
        }

        // Example with LinkOption.NOFOLLOW_LINKS
        System.out.println("\nWith NOFOLLOW_LINKS:");
        try {
            Path current = Path.of(".");
            Path realWithFollow = current.toRealPath();
            Path realNoFollow = current.toRealPath(LinkOption.NOFOLLOW_LINKS);
            System.out.println("Following links: " + realWithFollow);
            System.out.println("Not following links: " + realNoFollow);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // EXAM TRAP: Throws IOException if path does not exist
        System.out.println("\nNon-existent path:");
        try {
            Path nonExistent = Path.of("/this/does/not/exist.txt");
            Path real = nonExistent.toRealPath();  // ✗ Throws IOException
            System.out.println("Real path: " + real);
        } catch (IOException e) {
            System.out.println("IOException: " + e.getClass().getSimpleName());  // NoSuchFileException
        }

        // Comparison: normalize() vs toRealPath()
        System.out.println("\nnormalize() vs toRealPath():");
        Path withDots = Path.of(".");
        System.out.println("normalize(): " + withDots.normalize());  // . (no change)
        try {
            System.out.println("toRealPath(): " + withDots.toRealPath());  // Full absolute path
        } catch (IOException e) {
            System.out.println("toRealPath() failed: " + e.getMessage());
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// EXAM TRAPS SUMMARY
// ═══════════════════════════════════════════════════════════════════════════
//
// 1. getName() indices start at 0 AFTER root (root not included)
// 2. getNameCount() does NOT include root
// 3. subpath() returns ALWAYS relative path (no root)
// 4. subpath() endIndex is EXCLUSIVE
// 5. getParent() returns null if no parent
// 6. getRoot() returns null for relative paths
// 7. resolve() with absolute argument RETURNS the argument (ignores caller)
// 8. relativize() requires BOTH absolute OR BOTH relative
// 9. relativize() order matters: p1.relativize(p2) != p2.relativize(p1)
// 10. normalize() does NOT resolve symbolic links
// 11. normalize() does NOT require path to exist
// 12. toRealPath() REQUIRES path to exist (throws IOException)
// 13. toRealPath() follows symbolic links by default
// 14. All Path methods return NEW Path objects (immutable)
