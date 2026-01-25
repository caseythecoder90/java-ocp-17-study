package ch14io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;

/**
 * FILES OPERATIONS - OCP Java 17 Exam
 * Creating, Moving, Copying, Deleting Files and Directories
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * FILES CLASS - java.nio.file.Files
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Static utility class for file and directory operations.
 * All methods are STATIC.
 * Most methods throw IOException.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * METHOD SIGNATURES - CREATING FILES AND DIRECTORIES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Path createDirectory(Path dir, FileAttribute<?>... attrs)
 *   throws IOException
 *   - Creates a SINGLE directory
 *   - Throws FileAlreadyExistsException if directory already exists
 *   - Throws IOException if parent directory doesn't exist
 *   - Returns the created directory path
 *
 * Path createDirectories(Path dir, FileAttribute<?>... attrs)
 *   throws IOException
 *   - Creates directory AND any necessary parent directories
 *   - Does NOT throw exception if directory already exists
 *   - Like "mkdir -p" in Unix
 *   - Returns the directory path
 *
 * Path createFile(Path path, FileAttribute<?>... attrs)
 *   throws IOException
 *   - Creates an empty file
 *   - Throws FileAlreadyExistsException if file already exists
 *   - Returns the created file path
 *
 * EXAM TRAP: createDirectory() vs createDirectories()
 * - createDirectory()  -> Fails if parent doesn't exist, fails if exists
 * - createDirectories() -> Creates parents, doesn't fail if exists
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * METHOD SIGNATURES - COPYING FILES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * There are THREE overloaded copy() methods:
 *
 * 1. Path copy(Path source, Path target, CopyOption... options)
 *    throws IOException
 *    - Copies file or directory from source to target
 *    - If source is directory, creates empty directory (doesn't copy contents)
 *    - Throws FileAlreadyExistsException if target exists (unless REPLACE_EXISTING)
 *    - Returns the target path
 *
 * 2. long copy(InputStream in, Path target, CopyOption... options)
 *    throws IOException
 *    - Copies all bytes from InputStream to file
 *    - Returns number of bytes copied
 *    - Closes the InputStream when done
 *
 * 3. long copy(Path source, OutputStream out)
 *    throws IOException
 *    - Copies all bytes from file to OutputStream
 *    - Returns number of bytes copied
 *    - Does NOT close the OutputStream
 *    - NO CopyOption parameter on this overload!
 *
 * EXAM TRAP: Third overload has NO CopyOption parameter!
 * EXAM TRAP: Copying directory only creates empty directory, not contents!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * METHOD SIGNATURES - MOVING FILES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Path move(Path source, Path target, CopyOption... options)
 *   throws IOException
 *   - Moves or renames file or directory
 *   - Can move across file systems (becomes copy + delete)
 *   - Throws FileAlreadyExistsException if target exists (unless REPLACE_EXISTING)
 *   - Throws DirectoryNotEmptyException if replacing non-empty directory
 *   - Returns the target path
 *
 * ATOMIC MOVES:
 * - Use StandardCopyOption.ATOMIC_MOVE
 * - Move is atomic (all-or-nothing, no partial state visible)
 * - Throws AtomicMoveNotSupportedException if not supported
 * - Usually only works within same file system
 *
 * EXAM TRAP: ATOMIC_MOVE only works with move(), NOT copy()!
 * EXAM TRAP: Moving directory moves entire tree, copying directory does NOT!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * METHOD SIGNATURES - DELETING FILES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * void delete(Path path)
 *   throws IOException
 *   - Deletes file or EMPTY directory
 *   - Throws NoSuchFileException if path doesn't exist
 *   - Throws DirectoryNotEmptyException if directory is not empty
 *   - Throws IOException on other failures
 *
 * boolean deleteIfExists(Path path)
 *   throws IOException
 *   - Deletes file or EMPTY directory if it exists
 *   - Returns true if deleted, false if didn't exist
 *   - Does NOT throw exception if path doesn't exist
 *   - Still throws DirectoryNotEmptyException if directory not empty
 *
 * EXAM TRAP: Both methods FAIL on non-empty directories!
 * EXAM TRAP: delete() throws exception if doesn't exist, deleteIfExists() doesn't!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * METHOD SIGNATURES - COMPARING FILES
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * boolean isSameFile(Path path1, Path path2)
 *   throws IOException
 *   - Checks if two paths locate the SAME file
 *   - Resolves symbolic links
 *   - Returns true if same file (even if paths are different)
 *   - Throws IOException if cannot determine
 *
 * long mismatch(Path path1, Path path2)
 *   throws IOException
 *   - Compares contents of two files byte-by-byte
 *   - Returns position of first mismatch (0-based)
 *   - Returns -1L if files are identical
 *   - Returns -1L if both files are empty
 *   - Throws IOException if cannot read files
 *
 * EXAM TRAP: isSameFile() checks if SAME file (identity), not if equal contents!
 * EXAM TRAP: mismatch() returns -1L if identical, position (>= 0) if different!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class FilesOperations {

    public static void main(String[] args) {
        // ────────────────────────────────────────────────────────────────────
        // Creating directories - createDirectory() vs createDirectories()
        // ────────────────────────────────────────────────────────────────────
        demonstrateCreateDirectory();

        // ────────────────────────────────────────────────────────────────────
        // Creating files - createFile()
        // ────────────────────────────────────────────────────────────────────
        demonstrateCreateFile();

        // ────────────────────────────────────────────────────────────────────
        // Copying files - copy() three overloads
        // ────────────────────────────────────────────────────────────────────
        demonstrateCopy();

        // ────────────────────────────────────────────────────────────────────
        // Moving files - move() and atomic moves
        // ────────────────────────────────────────────────────────────────────
        demonstrateMove();

        // ────────────────────────────────────────────────────────────────────
        // Deleting files - delete() vs deleteIfExists()
        // ────────────────────────────────────────────────────────────────────
        demonstrateDelete();

        // ────────────────────────────────────────────────────────────────────
        // Comparing files - isSameFile() and mismatch()
        // ────────────────────────────────────────────────────────────────────
        demonstrateCompare();

        System.out.println("\n✓ All Files operation examples completed");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // createDirectory() vs createDirectories()
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateCreateDirectory() {
        System.out.println("=== createDirectory() vs createDirectories() ===\n");

        try {
            // Clean up from previous runs
            cleanup();

            // ────────────────────────────────────────────────────────────────
            // createDirectory() - Creates SINGLE directory only
            // ────────────────────────────────────────────────────────────────
            System.out.println("1. createDirectory() - single directory:");

            Path singleDir = Path.of("test-dir");
            Files.createDirectory(singleDir);
            System.out.println("   Created: " + singleDir);

            // EXAM TRAP: createDirectory() fails if already exists
            try {
                Files.createDirectory(singleDir);  // ✗ Fails!
            } catch (FileAlreadyExistsException e) {
                System.out.println("   FileAlreadyExistsException: directory already exists");
            }

            // EXAM TRAP: createDirectory() fails if parent doesn't exist
            System.out.println("\n2. createDirectory() with missing parent:");
            try {
                Path deepDir = Path.of("parent/child/grandchild");
                Files.createDirectory(deepDir);  // ✗ Fails!
            } catch (IOException e) {
                System.out.println("   IOException: parent directories don't exist");
            }

            // ────────────────────────────────────────────────────────────────
            // createDirectories() - Creates ALL necessary parent directories
            // ────────────────────────────────────────────────────────────────
            System.out.println("\n3. createDirectories() - creates parents:");

            Path deepDir = Path.of("parent/child/grandchild");
            Files.createDirectories(deepDir);
            System.out.println("   Created: " + deepDir + " (and all parents)");

            // Does NOT fail if already exists
            Files.createDirectories(deepDir);  // ✓ No exception
            System.out.println("   Called again: no exception (already exists)");

            // Clean up
            cleanup();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Summary
        System.out.println("\n┌─────────────────────┬─────────────────┬───────────────────┐");
        System.out.println("│ Method              │ Parent missing? │ Already exists?   │");
        System.out.println("├─────────────────────┼─────────────────┼───────────────────┤");
        System.out.println("│ createDirectory()   │ ✗ Throws        │ ✗ Throws          │");
        System.out.println("│ createDirectories() │ ✓ Creates       │ ✓ No exception    │");
        System.out.println("└─────────────────────┴─────────────────┴───────────────────┘");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // createFile()
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateCreateFile() {
        System.out.println("\n=== createFile() ===\n");

        try {
            cleanup();

            // Create an empty file
            Path file = Path.of("test-file.txt");
            Files.createFile(file);
            System.out.println("Created empty file: " + file);
            System.out.println("File size: " + Files.size(file) + " bytes");

            // EXAM TRAP: Fails if file already exists
            try {
                Files.createFile(file);  // ✗ Fails!
            } catch (FileAlreadyExistsException e) {
                System.out.println("FileAlreadyExistsException: file already exists");
            }

            cleanup();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // copy() - THREE overloads
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateCopy() {
        System.out.println("\n=== copy() - Three Overloads ===\n");

        try {
            cleanup();

            // ────────────────────────────────────────────────────────────────
            // Overload 1: copy(Path, Path, CopyOption...)
            // ────────────────────────────────────────────────────────────────
            System.out.println("1. copy(Path source, Path target, CopyOption...)");

            // Create source file
            Path source = Path.of("source.txt");
            Files.writeString(source, "Hello, World!");

            // Copy to target
            Path target = Path.of("target.txt");
            Files.copy(source, target);
            System.out.println("   Copied: " + source + " -> " + target);

            // EXAM TRAP: Fails if target exists (unless REPLACE_EXISTING)
            try {
                Files.copy(source, target);  // ✗ Fails!
            } catch (FileAlreadyExistsException e) {
                System.out.println("   FileAlreadyExistsException: target already exists");
            }

            // Use REPLACE_EXISTING option
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("   With REPLACE_EXISTING: success");

            // Copy with COPY_ATTRIBUTES
            Path target2 = Path.of("target2.txt");
            Files.copy(source, target2,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.COPY_ATTRIBUTES);
            System.out.println("   With COPY_ATTRIBUTES: copied timestamps too");

            // EXAM TRAP: Copying directory creates EMPTY directory
            System.out.println("\n   Copying directories:");
            Path srcDir = Path.of("src-dir");
            Files.createDirectory(srcDir);
            Files.writeString(srcDir.resolve("file.txt"), "Content");

            Path targetDir = Path.of("target-dir");
            Files.copy(srcDir, targetDir);
            System.out.println("   Copied directory: " + srcDir + " -> " + targetDir);
            System.out.println("   Is target empty? " + (Files.list(targetDir).count() == 0));
            System.out.println("   WARNING: Directory contents NOT copied!");

            // ────────────────────────────────────────────────────────────────
            // Overload 2: copy(InputStream, Path, CopyOption...)
            // ────────────────────────────────────────────────────────────────
            System.out.println("\n2. copy(InputStream in, Path target, CopyOption...)");

            Path streamTarget = Path.of("from-stream.txt");
            try (InputStream in = Files.newInputStream(source)) {
                long bytes = Files.copy(in, streamTarget, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("   Copied " + bytes + " bytes from InputStream to " + streamTarget);
            }

            // ────────────────────────────────────────────────────────────────
            // Overload 3: copy(Path, OutputStream) - NO CopyOption!
            // ────────────────────────────────────────────────────────────────
            System.out.println("\n3. copy(Path source, OutputStream out)");
            System.out.println("   EXAM TRAP: NO CopyOption parameter on this overload!");

            Path streamSource = Path.of("source.txt");
            try (OutputStream out = Files.newOutputStream(Path.of("to-stream.txt"))) {
                long bytes = Files.copy(streamSource, out);
                System.out.println("   Copied " + bytes + " bytes from " + streamSource + " to OutputStream");
            }

            cleanup();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        // Summary
        System.out.println("\n┌────────────────────────────────────────┬─────────────┬──────────┐");
        System.out.println("│ Method Signature                       │ CopyOption? │ Returns  │");
        System.out.println("├────────────────────────────────────────┼─────────────┼──────────┤");
        System.out.println("│ copy(Path, Path, CopyOption...)        │ ✓ Yes       │ Path     │");
        System.out.println("│ copy(InputStream, Path, CopyOption...) │ ✓ Yes       │ long     │");
        System.out.println("│ copy(Path, OutputStream)               │ ✗ NO!       │ long     │");
        System.out.println("└────────────────────────────────────────┴─────────────┴──────────┘");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // move() - Moving and renaming files
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateMove() {
        System.out.println("\n=== move() ===\n");

        try {
            cleanup();

            // ────────────────────────────────────────────────────────────────
            // Basic move/rename
            // ────────────────────────────────────────────────────────────────
            System.out.println("1. Basic move/rename:");

            Path source = Path.of("move-source.txt");
            Files.writeString(source, "Moving this file");

            Path target = Path.of("move-target.txt");
            Files.move(source, target);
            System.out.println("   Moved: " + source + " -> " + target);
            System.out.println("   Source exists? " + Files.exists(source));  // false
            System.out.println("   Target exists? " + Files.exists(target));  // true

            // EXAM TRAP: Fails if target exists (unless REPLACE_EXISTING)
            Path source2 = Path.of("move-source2.txt");
            Files.writeString(source2, "Another file");
            try {
                Files.move(source2, target);  // ✗ Fails!
            } catch (FileAlreadyExistsException e) {
                System.out.println("   FileAlreadyExistsException: target already exists");
            }

            // Use REPLACE_EXISTING
            Files.move(source2, target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("   With REPLACE_EXISTING: success");

            // ────────────────────────────────────────────────────────────────
            // Moving directories
            // ────────────────────────────────────────────────────────────────
            System.out.println("\n2. Moving directories:");

            Path srcDir = Path.of("src-dir-move");
            Files.createDirectory(srcDir);
            Files.writeString(srcDir.resolve("file.txt"), "Content");

            Path targetDir = Path.of("target-dir-move");
            Files.move(srcDir, targetDir);
            System.out.println("   Moved directory: " + srcDir + " -> " + targetDir);
            System.out.println("   Contents moved? " + Files.exists(targetDir.resolve("file.txt")));
            System.out.println("   NOTE: Moving directory moves ENTIRE tree!");

            // ────────────────────────────────────────────────────────────────
            // Atomic move
            // ────────────────────────────────────────────────────────────────
            System.out.println("\n3. Atomic move:");

            Path atomicSource = Path.of("atomic-source.txt");
            Files.writeString(atomicSource, "Atomic move");

            Path atomicTarget = Path.of("atomic-target.txt");
            try {
                Files.move(atomicSource, atomicTarget, StandardCopyOption.ATOMIC_MOVE);
                System.out.println("   Atomic move successful");
                System.out.println("   Guarantees: All-or-nothing, no partial state visible");
            } catch (AtomicMoveNotSupportedException e) {
                System.out.println("   AtomicMoveNotSupportedException: not supported on this file system");
            }

            // EXAM TRAP: ATOMIC_MOVE only with move(), not copy()
            System.out.println("\n   EXAM TRAP:");
            System.out.println("   ATOMIC_MOVE only works with Files.move()");
            System.out.println("   Using with Files.copy() throws UnsupportedOperationException");

            cleanup();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Summary
        System.out.println("\n┌──────────────┬────────────────────────────────────────────────┐");
        System.out.println("│ Operation    │ Behavior                                       │");
        System.out.println("├──────────────┼────────────────────────────────────────────────┤");
        System.out.println("│ move(file)   │ Moves file, source no longer exists            │");
        System.out.println("│ move(dir)    │ Moves ENTIRE directory tree                    │");
        System.out.println("│ copy(file)   │ Copies file, source still exists               │");
        System.out.println("│ copy(dir)    │ Creates EMPTY directory (no contents!)         │");
        System.out.println("└──────────────┴────────────────────────────────────────────────┘");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // delete() vs deleteIfExists()
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateDelete() {
        System.out.println("\n=== delete() vs deleteIfExists() ===\n");

        try {
            cleanup();

            // ────────────────────────────────────────────────────────────────
            // delete() - Throws exception if doesn't exist
            // ────────────────────────────────────────────────────────────────
            System.out.println("1. delete():");

            Path file1 = Path.of("delete-me.txt");
            Files.writeString(file1, "Delete this");
            Files.delete(file1);
            System.out.println("   Deleted: " + file1);

            // EXAM TRAP: Throws exception if doesn't exist
            try {
                Files.delete(file1);  // ✗ Fails! Already deleted
            } catch (NoSuchFileException e) {
                System.out.println("   NoSuchFileException: file doesn't exist");
            }

            // ────────────────────────────────────────────────────────────────
            // deleteIfExists() - Returns boolean, no exception if missing
            // ────────────────────────────────────────────────────────────────
            System.out.println("\n2. deleteIfExists():");

            Path file2 = Path.of("delete-if-exists.txt");
            Files.writeString(file2, "Delete this");

            boolean deleted1 = Files.deleteIfExists(file2);
            System.out.println("   First call: " + deleted1);  // true

            boolean deleted2 = Files.deleteIfExists(file2);
            System.out.println("   Second call: " + deleted2);  // false (no exception!)

            // ────────────────────────────────────────────────────────────────
            // EXAM TRAP: Both fail on non-empty directories
            // ────────────────────────────────────────────────────────────────
            System.out.println("\n3. Deleting directories:");

            // Empty directory: success
            Path emptyDir = Path.of("empty-dir");
            Files.createDirectory(emptyDir);
            Files.delete(emptyDir);
            System.out.println("   Empty directory: deleted successfully");

            // Non-empty directory: fails
            Path nonEmptyDir = Path.of("non-empty-dir");
            Files.createDirectory(nonEmptyDir);
            Files.writeString(nonEmptyDir.resolve("file.txt"), "Content");

            try {
                Files.delete(nonEmptyDir);  // ✗ Fails!
            } catch (DirectoryNotEmptyException e) {
                System.out.println("   DirectoryNotEmptyException: directory not empty");
            }

            // deleteIfExists() also fails on non-empty directory
            try {
                Files.deleteIfExists(nonEmptyDir);  // ✗ Still fails!
            } catch (DirectoryNotEmptyException e) {
                System.out.println("   deleteIfExists() ALSO throws DirectoryNotEmptyException");
            }

            cleanup();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Summary
        System.out.println("\n┌────────────────────┬──────────────────┬──────────────────────┐");
        System.out.println("│ Method             │ Doesn't exist?   │ Non-empty directory? │");
        System.out.println("├────────────────────┼──────────────────┼──────────────────────┤");
        System.out.println("│ delete()           │ ✗ Throws         │ ✗ Throws             │");
        System.out.println("│ deleteIfExists()   │ ✓ Returns false  │ ✗ Throws             │");
        System.out.println("└────────────────────┴──────────────────┴──────────────────────┘");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // isSameFile() and mismatch()
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateCompare() {
        System.out.println("\n=== isSameFile() and mismatch() ===\n");

        try {
            cleanup();

            // ────────────────────────────────────────────────────────────────
            // isSameFile() - Check if paths refer to same file
            // ────────────────────────────────────────────────────────────────
            System.out.println("1. isSameFile():");

            Path file1 = Path.of("same-file.txt");
            Files.writeString(file1, "Content");

            Path file2 = Path.of("same-file.txt");  // Same path
            System.out.println("   Same path: " + Files.isSameFile(file1, file2));  // true

            Path file3 = Path.of("./same-file.txt");  // Different string, same file
            System.out.println("   Different path string, same file: " + Files.isSameFile(file1, file3));  // true

            Path different = Path.of("different-file.txt");
            Files.writeString(different, "Content");  // Same content, different file
            System.out.println("   Different file, same content: " + Files.isSameFile(file1, different));  // false

            System.out.println("\n   EXAM TRAP: isSameFile() checks IDENTITY, not content equality!");

            // ────────────────────────────────────────────────────────────────
            // mismatch() - Compare file contents byte-by-byte
            // ────────────────────────────────────────────────────────────────
            System.out.println("\n2. mismatch():");

            Path fileA = Path.of("file-a.txt");
            Path fileB = Path.of("file-b.txt");
            Path fileC = Path.of("file-c.txt");

            Files.writeString(fileA, "Hello World");
            Files.writeString(fileB, "Hello World");  // Identical
            Files.writeString(fileC, "Hello There");  // Different at position 6

            long pos1 = Files.mismatch(fileA, fileB);
            System.out.println("   Identical files: " + pos1);  // -1L

            long pos2 = Files.mismatch(fileA, fileC);
            System.out.println("   Different at position: " + pos2);  // 6

            // Different lengths
            Path shortFile = Path.of("short.txt");
            Path longFile = Path.of("long.txt");
            Files.writeString(shortFile, "Hello");
            Files.writeString(longFile, "Hello World");

            long pos3 = Files.mismatch(shortFile, longFile);
            System.out.println("   Different lengths, mismatch at: " + pos3);  // 5 (end of shorter file)

            // Empty files
            Path empty1 = Path.of("empty1.txt");
            Path empty2 = Path.of("empty2.txt");
            Files.createFile(empty1);
            Files.createFile(empty2);

            long pos4 = Files.mismatch(empty1, empty2);
            System.out.println("   Both empty: " + pos4);  // -1L

            cleanup();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Summary
        System.out.println("\n┌──────────────────┬──────────────────────────────────────────┐");
        System.out.println("│ Method           │ Purpose                                  │");
        System.out.println("├──────────────────┼──────────────────────────────────────────┤");
        System.out.println("│ isSameFile()     │ Check if paths refer to SAME file        │");
        System.out.println("│                  │ (identity, not content equality)         │");
        System.out.println("├──────────────────┼──────────────────────────────────────────┤");
        System.out.println("│ mismatch()       │ Find first byte difference in contents   │");
        System.out.println("│                  │ Returns -1L if identical                 │");
        System.out.println("│                  │ Returns position (>=0) if different      │");
        System.out.println("└──────────────────┴──────────────────────────────────────────┘");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Helper: Clean up test files
    // ═══════════════════════════════════════════════════════════════════════════
    private static void cleanup() throws IOException {
        deleteIfExistsRecursive(Path.of("test-dir"));
        deleteIfExistsRecursive(Path.of("parent"));
        deleteIfExistsRecursive(Path.of("test-file.txt"));
        deleteIfExistsRecursive(Path.of("source.txt"));
        deleteIfExistsRecursive(Path.of("target.txt"));
        deleteIfExistsRecursive(Path.of("target2.txt"));
        deleteIfExistsRecursive(Path.of("src-dir"));
        deleteIfExistsRecursive(Path.of("target-dir"));
        deleteIfExistsRecursive(Path.of("from-stream.txt"));
        deleteIfExistsRecursive(Path.of("to-stream.txt"));
        deleteIfExistsRecursive(Path.of("move-source.txt"));
        deleteIfExistsRecursive(Path.of("move-target.txt"));
        deleteIfExistsRecursive(Path.of("move-source2.txt"));
        deleteIfExistsRecursive(Path.of("src-dir-move"));
        deleteIfExistsRecursive(Path.of("target-dir-move"));
        deleteIfExistsRecursive(Path.of("atomic-source.txt"));
        deleteIfExistsRecursive(Path.of("atomic-target.txt"));
        deleteIfExistsRecursive(Path.of("delete-me.txt"));
        deleteIfExistsRecursive(Path.of("delete-if-exists.txt"));
        deleteIfExistsRecursive(Path.of("empty-dir"));
        deleteIfExistsRecursive(Path.of("non-empty-dir"));
        deleteIfExistsRecursive(Path.of("same-file.txt"));
        deleteIfExistsRecursive(Path.of("different-file.txt"));
        deleteIfExistsRecursive(Path.of("file-a.txt"));
        deleteIfExistsRecursive(Path.of("file-b.txt"));
        deleteIfExistsRecursive(Path.of("file-c.txt"));
        deleteIfExistsRecursive(Path.of("short.txt"));
        deleteIfExistsRecursive(Path.of("long.txt"));
        deleteIfExistsRecursive(Path.of("empty1.txt"));
        deleteIfExistsRecursive(Path.of("empty2.txt"));
    }

    private static void deleteIfExistsRecursive(Path path) throws IOException {
        if (Files.exists(path)) {
            if (Files.isDirectory(path)) {
                // Delete contents first
                try (var stream = Files.list(path)) {
                    stream.forEach(p -> {
                        try {
                            deleteIfExistsRecursive(p);
                        } catch (IOException e) {
                            // Ignore
                        }
                    });
                }
            }
            Files.deleteIfExists(path);
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// QUICK REFERENCE - EXCEPTION BEHAVIOR
// ═══════════════════════════════════════════════════════════════════════════
//
// ┌─────────────────────┬──────────────────────┬─────────────────────────────┐
// │ Method              │ If doesn't exist     │ If already exists           │
// ├─────────────────────┼──────────────────────┼─────────────────────────────┤
// │ createDirectory()   │ Creates it           │ FileAlreadyExistsException  │
// │ createDirectories() │ Creates it + parents │ No exception                │
// │ createFile()        │ Creates it           │ FileAlreadyExistsException  │
// │ copy()              │ IOException          │ FileAlreadyExistsException* │
// │ move()              │ IOException          │ FileAlreadyExistsException* │
// │ delete()            │ NoSuchFileException  │ N/A                         │
// │ deleteIfExists()    │ Returns false        │ N/A                         │
// └─────────────────────┴──────────────────────┴─────────────────────────────┘
// * Unless REPLACE_EXISTING option is used
//
// ═══════════════════════════════════════════════════════════════════════════
