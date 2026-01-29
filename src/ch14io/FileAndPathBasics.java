package ch14io;

import java.io.*;
import java.nio.file.*;

/**
 * FILE AND PATH BASICS - I/O FUNDAMENTALS
 * ========================================
 *
 * Understanding files, directories, paths, and the APIs to work with them.
 *
 *
 * ============================================================================
 * BASIC TERMINOLOGY
 * ============================================================================
 *
 * FILE
 * ────
 * A record within a file system that stores data.
 * Contains actual content (text, binary data, etc.)
 * Examples: document.txt, photo.jpg, program.exe
 *
 *
 * DIRECTORY (FOLDER)
 * ──────────────────
 * A location that can contain files and other directories.
 * Organizes files in a hierarchical structure.
 * On Unix/Linux: directory
 * On Windows: folder (same concept)
 *
 *
 * PATH
 * ────
 * A string representation of a file or directory location.
 * Example Unix: /home/user/file.txt
 * Example Windows: C:\Users\\user\file.txt
 *
 *
 * ROOT DIRECTORY
 * ──────────────
 * The topmost directory in a file system hierarchy.
 * Unix/Linux: / (single root)
 * Windows: C:\, D:\, etc. (multiple roots, one per drive)
 *
 * Examples:
 *   /home/user/file.txt    → root is /
 *   C:\Users\\user\file.txt → root is C:\
 *
 *
 * ABSOLUTE PATH
 * ─────────────
 * Full path from the root directory to a file/directory.
 * Always starts from root.
 * Unix: /home/user/documents/file.txt
 * Windows: C:\Users\\user\documents\file.txt
 *
 * CHARACTERISTICS:
 * - Unambiguous - always refers to the same location
 * - Starts with root (/ on Unix, C:\ on Windows)
 * - Does NOT depend on current working directory
 *
 *
 * RELATIVE PATH
 * ─────────────
 * Path relative to the current working directory.
 * Does NOT start from root.
 *
 * Examples (if current directory is /home/user):
 *   documents/file.txt      → refers to /home/user/documents/file.txt
 *   ../other/file.txt       → refers to /home/other/file.txt
 *
 * CHARACTERISTICS:
 * - Depends on current working directory
 * - Shorter and more portable
 * - Can use . and .. for navigation
 *
 *
 * CURRENT DIRECTORY: . (single dot)
 * ──────────────────────────────────
 * Represents the current directory.
 *
 * Examples:
 *   ./file.txt              → file.txt in current directory
 *   .                       → the current directory itself
 *
 * NOTE: Often optional
 *   ./file.txt  same as  file.txt
 *
 *
 * PARENT DIRECTORY: .. (double dot)
 * ──────────────────────────────────
 * Represents the parent (containing) directory.
 *
 * Examples (if current directory is /home/user/documents):
 *   ..                      → /home/user
 *   ../..                   → /home
 *   ../../other/file.txt    → /home/other/file.txt
 *
 *
 * SYMBOLIC LINK (SYMLINK)
 * ───────────────────────
 * A special file that points to another file or directory.
 * Like a shortcut or alias.
 *
 * CHARACTERISTICS:
 * - Points to a target file/directory
 * - If target is deleted, symlink becomes "broken"
 * - Can cross file system boundaries
 * - Can create circular references
 *
 * EXAM NOTE: Some NIO.2 methods have LinkOption parameter to control
 * whether to follow symbolic links or not.
 *
 *
 * ============================================================================
 * TWO I/O APIS IN JAVA
 * ============================================================================
 *
 * LEGACY I/O (java.io.File)
 * ─────────────────────────
 * - Older API (since Java 1.0)
 * - Uses File class
 * - Less feature-rich
 * - Poor error handling (returns false instead of exceptions)
 * - Limited support for file attributes and symbolic links
 *
 *
 * NIO.2 (java.nio.file)
 * ─────────────────────
 * - Modern API (since Java 7)
 * - Uses Path, Paths, Files classes
 * - Feature-rich (attributes, symbolic links, watching, etc.)
 * - Better error handling (throws IOException with details)
 * - Recommended for new code
 *
 * EXAM: Must know BOTH APIs!
 *
 *
 * ============================================================================
 * CREATING FILE OBJECTS (LEGACY I/O)
 * ============================================================================
 *
 * The File class has THREE constructors:
 *
 *
 * Constructor 1: File(String pathname)
 * ────────────────────────────────────
 * Creates File from string path.
 *
 *   File file1 = new File("/home/user/file.txt");
 *   File file2 = new File("relative/path/file.txt");
 *
 *
 * Constructor 2: File(String parent, String child)
 * ─────────────────────────────────────────────────
 * Creates File from parent directory path and child name.
 *
 *   File file = new File("/home/user", "file.txt");
 *   // Result: /home/user/file.txt
 *
 *
 * Constructor 3: File(File parent, String child)
 * ───────────────────────────────────────────────
 * Creates File from parent File object and child name.
 *
 *   File parent = new File("/home/user");
 *   File file = new File(parent, "file.txt");
 *   // Result: /home/user/file.txt
 *
 *
 * IMPORTANT: Creating File object does NOT create actual file!
 * File object is just a reference to a path (may or may not exist).
 *
 *
 * ============================================================================
 * CREATING PATH OBJECTS (NIO.2)
 * ============================================================================
 *
 * Path is an INTERFACE - cannot instantiate directly.
 * Must use STATIC FACTORY METHODS.
 *
 *
 * Method 1: Path.of(...) (Java 11+)
 * ──────────────────────────────────
 * PREFERRED modern method.
 *
 * SIGNATURES (multiple overloads):
 *   static Path of(String first, String... more)
 *   static Path of(URI uri)
 *
 * EXAMPLES:
 *   Path path1 = Path.of("/home/user/file.txt");
 *   Path path2 = Path.of("/home", "user", "file.txt");  // Joined with separator
 *   Path path3 = Path.of("relative/file.txt");
 *
 *
 * Method 2: Paths.get(...) (Java 7+)
 * ───────────────────────────────────
 * Older factory method (still widely used).
 *
 * SIGNATURES (multiple overloads):
 *   static Path get(String first, String... more)
 *   static Path get(URI uri)
 *
 * EXAMPLES:
 *   Path path1 = Paths.get("/home/user/file.txt");
 *   Path path2 = Paths.get("/home", "user", "file.txt");
 *   Path path3 = Paths.get("relative/file.txt");
 *
 * NOTE: Path.of() and Paths.get() are EQUIVALENT!
 *   Path.of(...) calls Paths.get(...) internally
 *
 *
 * Method 3: FileSystems.getDefault().getPath(...)
 * ────────────────────────────────────────────────
 * Get Path from FileSystem object.
 *
 * SIGNATURE:
 *   Path getPath(String first, String... more)
 *
 * EXAMPLE:
 *   FileSystem fs = FileSystems.getDefault();
 *   Path path = fs.getPath("/home/user/file.txt");
 *
 * USE CASE: Useful when working with different file systems
 * (e.g., ZIP file system, remote file system)
 *
 *
 * CONVERTING BETWEEN FILE AND PATH:
 * ──────────────────────────────────
 *   File file = new File("/home/user/file.txt");
 *   Path path = file.toPath();        // File → Path
 *
 *   Path path2 = Path.of("/home/user/file.txt");
 *   File file2 = path2.toFile();      // Path → File
 *
 *
 * ============================================================================
 * COMMON FILE METHODS (LEGACY I/O) - KNOW THESE!
 * ============================================================================
 *
 * Getting Information:
 *
 * String getName()
 * ────────────────
 * Returns the name of the file or directory (last element of path).
 * Does NOT throw IOException.
 *
 *   File file = new File("/home/user/file.txt");
 *   String name = file.getName();  // "file.txt"
 *
 *
 * String getParent()
 * ──────────────────
 * Returns parent directory path as String, or null if no parent.
 * Does NOT throw IOException.
 *
 *   File file = new File("/home/user/file.txt");
 *   String parent = file.getParent();  // "/home/user"
 *
 *   File file2 = new File("file.txt");
 *   String parent2 = file2.getParent();  // null (no parent in relative path)
 *
 *
 * boolean isAbsolute()
 * ────────────────────
 * Returns true if path is absolute.
 * Does NOT throw IOException.
 *
 *   File file1 = new File("/home/user/file.txt");
 *   file1.isAbsolute();  // true
 *
 *   File file2 = new File("relative/file.txt");
 *   file2.isAbsolute();  // false
 *
 *
 * String getAbsolutePath()
 * ────────────────────────
 * Returns absolute path as String.
 * Does NOT throw IOException.
 * Converts relative path to absolute using current working directory.
 *
 *   File file = new File("file.txt");
 *   String abs = file.getAbsolutePath();  // e.g., "/current/directory/file.txt"
 *
 *
 * boolean exists()
 * ────────────────
 * Returns true if file/directory exists.
 * Does NOT throw IOException.
 * Returns false if doesn't exist OR if there's an error.
 *
 *   File file = new File("/home/user/file.txt");
 *   if (file.exists()) {
 *       System.out.println("File exists");
 *   }
 *
 *
 * boolean isDirectory()
 * ─────────────────────
 * Returns true if path exists AND is a directory.
 * Does NOT throw IOException.
 * Returns false if doesn't exist, is a file, or error occurs.
 *
 *   File dir = new File("/home/user");
 *   if (dir.isDirectory()) {
 *       System.out.println("Is a directory");
 *   }
 *
 *
 * boolean isFile()
 * ────────────────
 * Returns true if path exists AND is a regular file.
 * Does NOT throw IOException.
 * Returns false if doesn't exist, is a directory, or error occurs.
 *
 *   File file = new File("/home/user/file.txt");
 *   if (file.isFile()) {
 *       System.out.println("Is a file");
 *   }
 *
 *
 * long length()
 * ─────────────
 * Returns size of file in bytes.
 * Does NOT throw IOException.
 * Returns 0 if file doesn't exist or is a directory.
 *
 *   File file = new File("/home/user/file.txt");
 *   long size = file.length();  // Size in bytes
 *
 *
 * long lastModified()
 * ───────────────────
 * Returns last modified time in milliseconds since epoch (Jan 1, 1970).
 * Does NOT throw IOException.
 * Returns 0 if file doesn't exist or error occurs.
 *
 *   File file = new File("/home/user/file.txt");
 *   long modified = file.lastModified();
 *
 *
 * File[] listFiles()
 * ──────────────────
 * Returns array of File objects in directory, or null if not a directory.
 * Does NOT throw IOException.
 * Returns null if path doesn't exist, is not a directory, or error occurs.
 *
 *   File dir = new File("/home/user");
 *   File[] files = dir.listFiles();
 *   if (files != null) {
 *       for (File f : files) {
 *           System.out.println(f.getName());
 *       }
 *   }
 *
 *
 * boolean delete()
 * ────────────────
 * Deletes file or empty directory.
 * Does NOT throw IOException.
 * Returns true if deleted, false otherwise.
 * Cannot delete non-empty directories.
 *
 *   File file = new File("/home/user/file.txt");
 *   boolean deleted = file.delete();
 *
 *
 * boolean mkdir()
 * ───────────────
 * Creates directory (parent must exist).
 * Does NOT throw IOException.
 * Returns true if created, false otherwise.
 *
 *   File dir = new File("/home/user/newdir");
 *   boolean created = dir.mkdir();
 *
 *
 * boolean mkdirs()
 * ────────────────
 * Creates directory and all necessary parent directories.
 * Does NOT throw IOException.
 * Returns true if created, false otherwise.
 *
 *   File dir = new File("/home/user/a/b/c");
 *   boolean created = dir.mkdirs();  // Creates a, b, and c
 *
 *
 * boolean renameTo(File dest)
 * ───────────────────────────
 * Renames/moves file to destination.
 * Does NOT throw IOException.
 * Returns true if successful, false otherwise.
 *
 *   File source = new File("/home/user/old.txt");
 *   File dest = new File("/home/user/new.txt");
 *   boolean renamed = source.renameTo(dest);
 *
 *
 * ============================================================================
 * COMMON PATH/FILES METHODS (NIO.2) - KNOW THESE!
 * ============================================================================
 *
 * Path Methods (operate on Path object):
 *
 * Path getFileName()
 * ──────────────────
 * Returns the name of the file or directory (last element).
 * Returns Path object, not String.
 * Does NOT throw IOException.
 *
 *   Path path = Path.of("/home/user/file.txt");
 *   Path name = path.getFileName();  // file.txt (as Path)
 *   String nameStr = name.toString();  // "file.txt" (as String)
 *
 *
 * Path getParent()
 * ────────────────
 * Returns parent path, or null if no parent.
 * Does NOT throw IOException.
 *
 *   Path path = Path.of("/home/user/file.txt");
 *   Path parent = path.getParent();  // /home/user
 *
 *
 * boolean isAbsolute()
 * ────────────────────
 * Returns true if path is absolute.
 * Does NOT throw IOException.
 *
 *   Path path1 = Path.of("/home/user/file.txt");
 *   path1.isAbsolute();  // true
 *
 *   Path path2 = Path.of("relative/file.txt");
 *   path2.isAbsolute();  // false
 *
 *
 * Path toAbsolutePath()
 * ─────────────────────
 * Returns absolute path.
 * Does NOT throw IOException.
 *
 *   Path path = Path.of("file.txt");
 *   Path abs = path.toAbsolutePath();  // e.g., /current/directory/file.txt
 *
 *
 * Files Methods (static methods in Files class):
 *
 * CRITICAL RULE:
 * ══════════════
 * If a NIO.2 method declares IOException, it usually requires
 * the paths it operates on to EXIST.
 *
 *
 * boolean exists(Path path, LinkOption... options)
 * ─────────────────────────────────────────────────
 * Returns true if path exists.
 * Does NOT throw IOException (no exception = doesn't require existence).
 *
 *   Path path = Path.of("/home/user/file.txt");
 *   if (Files.exists(path)) {
 *       System.out.println("Exists");
 *   }
 *
 * LinkOption parameter:
 *   Files.exists(path, LinkOption.NOFOLLOW_LINKS)  // Don't follow symlinks
 *
 *
 * boolean isDirectory(Path path, LinkOption... options)
 * ──────────────────────────────────────────────────────
 * Returns true if path exists AND is a directory.
 * Does NOT throw IOException.
 *
 *   Path path = Path.of("/home/user");
 *   if (Files.isDirectory(path)) {
 *       System.out.println("Is directory");
 *   }
 *
 *
 * boolean isRegularFile(Path path, LinkOption... options)
 * ────────────────────────────────────────────────────────
 * Returns true if path exists AND is a regular file.
 * Does NOT throw IOException.
 *
 *   Path path = Path.of("/home/user/file.txt");
 *   if (Files.isRegularFile(path)) {
 *       System.out.println("Is regular file");
 *   }
 *
 *
 * long size(Path path) throws IOException
 * ────────────────────────────────────────
 * Returns size of file in bytes.
 * THROWS IOException.
 * Requires path to exist (throws if doesn't exist).
 *
 *   Path path = Path.of("/home/user/file.txt");
 *   try {
 *       long size = Files.size(path);
 *   } catch (IOException e) {
 *       // File doesn't exist or error reading
 *   }
 *
 *
 * FileTime getLastModifiedTime(Path path, LinkOption... options) throws IOException
 * ──────────────────────────────────────────────────────────────────────────────────
 * Returns last modified time.
 * THROWS IOException.
 * Requires path to exist.
 *
 *   Path path = Path.of("/home/user/file.txt");
 *   FileTime time = Files.getLastModifiedTime(path);
 *
 *
 * void delete(Path path) throws IOException
 * ──────────────────────────────────────────
 * Deletes file or empty directory.
 * THROWS IOException.
 * Throws if path doesn't exist or directory not empty.
 *
 *   Path path = Path.of("/home/user/file.txt");
 *   Files.delete(path);  // Throws if doesn't exist
 *
 *
 * boolean deleteIfExists(Path path) throws IOException
 * ─────────────────────────────────────────────────────
 * Deletes file if it exists.
 * THROWS IOException (but NOT if file doesn't exist).
 * Returns true if deleted, false if didn't exist.
 *
 *   Path path = Path.of("/home/user/file.txt");
 *   boolean deleted = Files.deleteIfExists(path);  // No exception if doesn't exist
 *
 *
 * Stream<Path> list(Path dir) throws IOException
 * ───────────────────────────────────────────────
 * Returns Stream of entries in directory (one level).
 * THROWS IOException.
 * Requires directory to exist.
 *
 *   Path dir = Path.of("/home/user");
 *   try (Stream<Path> stream = Files.list(dir)) {
 *       stream.forEach(System.out::println);
 *   }
 *
 * IMPORTANT: Must close Stream (use try-with-resources).
 *
 *
 * Path createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException
 * ─────────────────────────────────────────────────────────────────────────────
 * Creates directory (parent must exist).
 * THROWS IOException.
 * Throws if parent doesn't exist or directory already exists.
 *
 *   Path dir = Path.of("/home/user/newdir");
 *   Files.createDirectory(dir);
 *
 *
 * Path createDirectories(Path dir, FileAttribute<?>... attrs) throws IOException
 * ───────────────────────────────────────────────────────────────────────────────
 * Creates directory and all necessary parent directories.
 * THROWS IOException.
 * Does NOT throw if directory already exists (idempotent).
 *
 *   Path dir = Path.of("/home/user/a/b/c");
 *   Files.createDirectories(dir);  // Creates a, b, and c if needed
 *
 *
 * Path move(Path source, Path target, CopyOption... options) throws IOException
 * ──────────────────────────────────────────────────────────────────────────────
 * Moves or renames file/directory.
 * THROWS IOException.
 * Requires source to exist.
 *
 *   Path source = Path.of("/home/user/old.txt");
 *   Path target = Path.of("/home/user/new.txt");
 *   Files.move(source, target);
 *
 *
 * ============================================================================
 * COMPARISON: FILE (I/O) vs PATH/FILES (NIO.2)
 * ============================================================================
 *
 * ┌─────────────────────┬──────────────────────┬─────────────────────────────┐
 * │ Operation           │ File (I/O)           │ Path/Files (NIO.2)          │
 * ├─────────────────────┼──────────────────────┼─────────────────────────────┤
 * │ Get name            │ getName()            │ getFileName()               │
 * │                     │ (returns String)     │ (returns Path)              │
 * │                     │ (no exception)       │ (no exception)              │
 * ├─────────────────────┼──────────────────────┼─────────────────────────────┤
 * │ Get parent          │ getParent()          │ getParent()                 │
 * │                     │ (returns String)     │ (returns Path)              │
 * │                     │ (no exception)       │ (no exception)              │
 * ├─────────────────────┼──────────────────────┼─────────────────────────────┤
 * │ Check if absolute   │ isAbsolute()         │ isAbsolute()                │
 * │                     │ (no exception)       │ (no exception)              │
 * ├─────────────────────┼──────────────────────┼─────────────────────────────┤
 * │ Get absolute path   │ getAbsolutePath()    │ toAbsolutePath()            │
 * │                     │ (returns String)     │ (returns Path)              │
 * │                     │ (no exception)       │ (no exception)              │
 * ├─────────────────────┼──────────────────────┼─────────────────────────────┤
 * │ Check existence     │ exists()             │ exists(Path)                │
 * │                     │ (no exception)       │ (no exception)              │
 * ├─────────────────────┼──────────────────────┼─────────────────────────────┤
 * │ Check if directory  │ isDirectory()        │ isDirectory(Path)           │
 * │                     │ (no exception)       │ (no exception)              │
 * ├─────────────────────┼──────────────────────┼─────────────────────────────┤
 * │ Check if file       │ isFile()             │ isRegularFile(Path)         │
 * │                     │ (no exception)       │ (no exception)              │
 * ├─────────────────────┼──────────────────────┼─────────────────────────────┤
 * │ Get file size       │ length()             │ size(Path)                  │
 * │                     │ (no exception)       │ (THROWS IOException)        │
 * ├─────────────────────┼──────────────────────┼─────────────────────────────┤
 * │ Last modified       │ lastModified()       │ getLastModifiedTime(Path)   │
 * │                     │ (no exception)       │ (THROWS IOException)        │
 * ├─────────────────────┼──────────────────────┼─────────────────────────────┤
 * │ Delete              │ delete()             │ delete(Path)                │
 * │                     │ (returns boolean)    │ (THROWS IOException)        │
 * │                     │                      │ deleteIfExists(Path)        │
 * │                     │                      │ (THROWS IOException)        │
 * ├─────────────────────┼──────────────────────┼─────────────────────────────┤
 * │ List directory      │ listFiles()          │ list(Path)                  │
 * │                     │ (returns array)      │ (returns Stream)            │
 * │                     │ (no exception)       │ (THROWS IOException)        │
 * ├─────────────────────┼──────────────────────┼─────────────────────────────┤
 * │ Create directory    │ mkdir()              │ createDirectory(Path)       │
 * │                     │ (returns boolean)    │ (THROWS IOException)        │
 * ├─────────────────────┼──────────────────────┼─────────────────────────────┤
 * │ Create directories  │ mkdirs()             │ createDirectories(Path)     │
 * │                     │ (returns boolean)    │ (THROWS IOException)        │
 * ├─────────────────────┼──────────────────────┼─────────────────────────────┤
 * │ Move/Rename         │ renameTo(File)       │ move(Path, Path)            │
 * │                     │ (returns boolean)    │ (THROWS IOException)        │
 * └─────────────────────┴──────────────────────┴─────────────────────────────┘
 *
 * KEY DIFFERENCES:
 * - File methods return String, Path methods return Path (getName/getFileName, getParent, getAbsolutePath/toAbsolutePath)
 * - File methods return false on error, Files methods throw IOException
 * - Files methods provide better error information
 * - Files methods support LinkOption for symbolic link handling
 * - Files.list() returns Stream (must close), File.listFiles() returns array
 * - Path methods: getFileName(), getParent(), isAbsolute(), toAbsolutePath()
 * - Files methods: exists(Path), isDirectory(Path), size(Path), etc.
 *
 *
 * ============================================================================
 * EXCEPTION HANDLING RULE (CRITICAL!)
 * ============================================================================
 *
 * RULE: If a NIO.2 method declares IOException, it usually requires
 *       the paths it operates on to EXIST.
 *
 * EXCEPTIONS TO THE RULE:
 * - exists() - doesn't throw, doesn't require existence (obviously)
 * - isDirectory() - doesn't throw, doesn't require existence
 * - isRegularFile() - doesn't throw, doesn't require existence
 * - deleteIfExists() - throws IOException but NOT if file doesn't exist
 * - createDirectories() - throws IOException but NOT if directory exists
 *
 * EXAMPLES:
 *
 * Files.size(path)              // Throws if path doesn't exist ✓
 * Files.delete(path)            // Throws if path doesn't exist ✓
 * Files.getLastModifiedTime(p)  // Throws if path doesn't exist ✓
 * Files.list(path)              // Throws if path doesn't exist ✓
 * Files.createDirectory(path)   // Throws if parent doesn't exist ✓
 * Files.move(source, target)    // Throws if source doesn't exist ✓
 *
 * Files.exists(path)            // Doesn't throw ✓
 * Files.deleteIfExists(path)    // Doesn't throw if doesn't exist ✓
 *
 *
 * ============================================================================
 * CODE EXAMPLES
 * ============================================================================
 */
public class FileAndPathBasics {

    /**
     * Demonstrates creating File objects (3 constructors)
     */
    public static void demonstrateFileCreation() {
        System.out.println("=== Creating File Objects ===");

        // Constructor 1: File(String pathname)
        File file1 = new File("/home/user/file.txt");
        System.out.println("File 1: " + file1);

        // Constructor 2: File(String parent, String child)
        File file2 = new File("/home/user", "file.txt");
        System.out.println("File 2: " + file2);

        // Constructor 3: File(File parent, String child)
        File parent = new File("/home/user");
        File file3 = new File(parent, "file.txt");
        System.out.println("File 3: " + file3);

        System.out.println("NOTE: Creating File object doesn't create actual file!");
        System.out.println();
    }

    /**
     * Demonstrates creating Path objects
     */
    public static void demonstratePathCreation() {
        System.out.println("=== Creating Path Objects ===");

        // Method 1: Path.of() (Java 11+)
        Path path1 = Path.of("/home/user/file.txt");
        System.out.println("Path.of: " + path1);

        // Method 2: Paths.get()
        Path path2 = Paths.get("/home/user/file.txt");
        System.out.println("Paths.get: " + path2);

        // Method 3: FileSystems
        FileSystem fs = FileSystems.getDefault();
        Path path3 = fs.getPath("/home/user/file.txt");
        System.out.println("FileSystems: " + path3);

        // With multiple arguments (joined with separator)
        Path path4 = Path.of("/home", "user", "file.txt");
        System.out.println("Multiple args: " + path4);

        System.out.println();
    }

    /**
     * Demonstrates File to Path conversion
     */
    public static void demonstrateFilePathConversion() {
        System.out.println("=== File ↔ Path Conversion ===");

        File file = new File("/home/user/file.txt");
        Path path = file.toPath();
        System.out.println("File → Path: " + path);

        Path path2 = Path.of("/home/user/file.txt");
        File file2 = path2.toFile();
        System.out.println("Path → File: " + file2);

        System.out.println();
    }

    /**
     * Demonstrates common File methods
     */
    public static void demonstrateFileMethods() {
        System.out.println("=== File Methods ===");

        File file = new File("test.txt");

        System.out.println("getName(): " + file.getName());
        System.out.println("getParent(): " + file.getParent());
        System.out.println("isAbsolute(): " + file.isAbsolute());
        System.out.println("getAbsolutePath(): " + file.getAbsolutePath());
        System.out.println("exists(): " + file.exists());
        System.out.println("isDirectory(): " + file.isDirectory());
        System.out.println("isFile(): " + file.isFile());
        System.out.println("length(): " + file.length());
        System.out.println("lastModified(): " + file.lastModified());

        System.out.println("\nNOTE: None of these methods throw IOException!");
        System.out.println();
    }

    /**
     * Demonstrates common Path/Files methods
     */
    public static void demonstratePathFilesMethods() {
        System.out.println("=== Path/Files Methods ===");

        Path path = Path.of("test.txt");

        // Path methods (no exceptions)
        System.out.println("getFileName(): " + path.getFileName());
        System.out.println("getParent(): " + path.getParent());
        System.out.println("isAbsolute(): " + path.isAbsolute());
        System.out.println("toAbsolutePath(): " + path.toAbsolutePath());

        // Files methods (some throw IOException)
        System.out.println("exists(): " + Files.exists(path));
        System.out.println("isDirectory(): " + Files.isDirectory(path));
        System.out.println("isRegularFile(): " + Files.isRegularFile(path));

        // These throw IOException if file doesn't exist
        try {
            System.out.println("size(): " + Files.size(path)); // NoSuchFileException
        } catch (IOException e) {
            System.out.println("size() threw IOException: " + e.getMessage());
        }

        try {
            System.out.println("getLastModifiedTime(): " + Files.getLastModifiedTime(path));
        } catch (IOException e) {
            System.out.println("getLastModifiedTime() threw IOException: " + e.getMessage());
        }

        System.out.println();
    }

    /**
     * Demonstrates exception handling differences
     */
    public static void demonstrateExceptionHandling() {
        System.out.println("=== Exception Handling: File vs Files ===");

        File file = new File("nonexistent.txt");
        Path path = Path.of("nonexistent.txt");

        // File.length() - returns 0, no exception
        System.out.println("File.length() for nonexistent: " + file.length());

        // Files.size() - throws IOException
        try {
            Files.size(path);
        } catch (IOException e) {
            System.out.println("Files.size() threw: " + e.getClass().getSimpleName());
        }

        // File.delete() - returns false, no exception
        boolean deleted = file.delete();
        System.out.println("File.delete() returned: " + deleted);

        // Files.delete() - throws IOException
        try {
            Files.delete(path);
        } catch (IOException e) {
            System.out.println("Files.delete() threw: " + e.getClass().getSimpleName());
        }

        // Files.deleteIfExists() - doesn't throw if doesn't exist
        try {
            boolean exists = Files.deleteIfExists(path);
            System.out.println("Files.deleteIfExists() returned: " + exists);
        } catch (IOException e) {
            System.out.println("Files.deleteIfExists() threw: " + e.getMessage());
        }

        System.out.println();
    }

    public static void main(String[] args) {
        demonstrateFileCreation();
        demonstratePathCreation();
        demonstrateFilePathConversion();
        demonstrateFileMethods();
        demonstratePathFilesMethods();
        demonstrateExceptionHandling();
    }
}

/*
 * ============================================================================
 * EXAM SUMMARY - FILE AND PATH BASICS
 * ============================================================================
 *
 * TERMINOLOGY:
 * - File: record storing data
 * - Directory: location containing files/directories
 * - Path: string representation of location
 * - Root: topmost directory (/ on Unix, C:\ on Windows)
 * - Absolute path: from root (unambiguous)
 * - Relative path: from current directory (context-dependent)
 * - . (dot): current directory
 * - .. (double dot): parent directory
 * - Symbolic link: pointer to another file/directory
 *
 * FILE CREATION (3 constructors):
 * - File(String pathname)
 * - File(String parent, String child)
 * - File(File parent, String child)
 *
 * PATH CREATION (static factory methods):
 * - Path.of(String, String...)        [Java 11+, preferred]
 * - Paths.get(String, String...)      [Java 7+]
 * - FileSystems.getDefault().getPath(String, String...)
 * - Path.of() and Paths.get() are equivalent
 *
 * CONVERSION:
 * - file.toPath() → File to Path
 * - path.toFile() → Path to File
 *
 * FILE METHODS (no IOException):
 * - getName(), getParent(), isAbsolute(), getAbsolutePath()
 * - exists(), isDirectory(), isFile()
 * - length(), lastModified(), listFiles()
 * - delete(), mkdir(), mkdirs(), renameTo(File)
 *
 * PATH/FILES METHODS:
 * No IOException:
 * - path.getFileName(), getParent(), isAbsolute(), toAbsolutePath()
 * - Files.exists(path), isDirectory(path), isRegularFile(path)
 *
 * THROWS IOException:
 * - Files.size(path) - requires existence
 * - Files.getLastModifiedTime(path) - requires existence
 * - Files.delete(path) - requires existence
 * - Files.deleteIfExists(path) - doesn't throw if doesn't exist
 * - Files.list(path) - requires existence, returns Stream (must close)
 * - Files.createDirectory(path) - parent must exist
 * - Files.createDirectories(path) - creates parents, ok if exists
 * - Files.move(source, target) - source must exist
 *
 * CRITICAL RULE:
 * If NIO.2 method declares IOException, it usually requires paths to EXIST.
 * Exceptions: exists(), isDirectory(), isRegularFile(), deleteIfExists(), createDirectories()
 *
 * KEY DIFFERENCES:
 * - File methods return false on error
 * - Files methods throw IOException with details
 * - Files.list() returns Stream (must close)
 * - File.listFiles() returns array
 */
