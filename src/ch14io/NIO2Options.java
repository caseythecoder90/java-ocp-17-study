package ch14io;

import java.nio.file.*;

/**
 * NIO.2 OPTIONAL PARAMETERS (ENUMS) - OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * NIO.2 OPTIONAL PARAMETERS - OVERVIEW
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Many NIO.2 methods accept optional enum parameters (varargs) to customize behavior:
 *
 * 1. LinkOption          - How to handle symbolic links
 * 2. StandardCopyOption  - Options for copy operations
 * 3. StandardOpenOption  - Options for opening files
 * 4. FileVisitOption     - Options for directory tree traversal
 *
 * These are passed as VARARGS (...) so you can:
 * - Pass zero arguments (use defaults)
 * - Pass one or more enum values
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * 1. LinkOption - How to Handle Symbolic Links
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Enum: java.nio.file.LinkOption
 * Implements: OpenOption, CopyOption
 *
 * Values:
 * ┌───────────────────┬─────────────────────────────────────────────────────┐
 * │ Enum Value        │ Description                                         │
 * ├───────────────────┼─────────────────────────────────────────────────────┤
 * │ NOFOLLOW_LINKS    │ Do NOT follow symbolic links                        │
 * │                   │ Treat symbolic link as the link itself, not target  │
 * └───────────────────┴─────────────────────────────────────────────────────┘
 *
 * Used in methods:
 * - Files.exists(Path, LinkOption...)
 * - Files.isDirectory(Path, LinkOption...)
 * - Files.isRegularFile(Path, LinkOption...)
 * - Files.copy(Path, Path, CopyOption...)  [LinkOption implements CopyOption]
 * - Files.move(Path, Path, CopyOption...)  [LinkOption implements CopyOption]
 * - Path.toRealPath(LinkOption...)
 * - Files.readAttributes(Path, Class<A>, LinkOption...)
 *
 * DEFAULT BEHAVIOR (no option):
 * - Follows symbolic links (resolves to target)
 *
 * EXAM TIP: Only ONE value! Either specify NOFOLLOW_LINKS or don't.
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * 2. StandardCopyOption - Options for Copy Operations
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Enum: java.nio.file.StandardCopyOption
 * Implements: CopyOption
 *
 * Values:
 * ┌──────────────────────┬──────────────────────────────────────────────────┐
 * │ Enum Value           │ Description                                      │
 * ├──────────────────────┼──────────────────────────────────────────────────┤
 * │ REPLACE_EXISTING     │ Replace target file if it already exists         │
 * │                      │ Without this: throws FileAlreadyExistsException  │
 * ├──────────────────────┼──────────────────────────────────────────────────┤
 * │ COPY_ATTRIBUTES      │ Copy file attributes (timestamps, permissions)   │
 * │                      │ to the new file                                  │
 * ├──────────────────────┼──────────────────────────────────────────────────┤
 * │ ATOMIC_MOVE          │ Move must be atomic (all-or-nothing)             │
 * │                      │ Only for Files.move(), not Files.copy()          │
 * │                      │ Throws AtomicMoveNotSupportedException if not    │
 * │                      │ supported on this file system                    │
 * └──────────────────────┴──────────────────────────────────────────────────┘
 *
 * Used in methods:
 * - Files.copy(Path, Path, CopyOption...)
 * - Files.copy(InputStream, Path, CopyOption...)
 * - Files.copy(Path, OutputStream)  // No options on this overload
 * - Files.move(Path, Path, CopyOption...)
 *
 * DEFAULT BEHAVIOR (no options):
 * - Throws FileAlreadyExistsException if target exists
 * - Does NOT copy attributes
 * - Move is NOT atomic
 *
 * EXAM TRAP: ATOMIC_MOVE only works with Files.move(), NOT Files.copy()!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * 3. StandardOpenOption - Options for Opening Files
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Enum: java.nio.file.StandardOpenOption
 * Implements: OpenOption
 *
 * Values:
 * ┌──────────────────┬────────────────────────────────────────────────────┐
 * │ Enum Value       │ Description                                        │
 * ├──────────────────┼────────────────────────────────────────────────────┤
 * │ READ             │ Open for read access                               │
 * ├──────────────────┼────────────────────────────────────────────────────┤
 * │ WRITE            │ Open for write access                              │
 * ├──────────────────┼────────────────────────────────────────────────────┤
 * │ APPEND           │ Append to end of file (if opened for write)        │
 * │                  │ Implies WRITE                                      │
 * ├──────────────────┼────────────────────────────────────────────────────┤
 * │ TRUNCATE_EXISTING│ Truncate file to 0 bytes (if opened for write)     │
 * │                  │ Implies WRITE                                      │
 * ├──────────────────┼────────────────────────────────────────────────────┤
 * │ CREATE           │ Create new file if it doesn't exist                │
 * │                  │ If file exists, open it (use with TRUNCATE_EXISTING│
 * │                  │ or APPEND to control behavior)                     │
 * ├──────────────────┼────────────────────────────────────────────────────┤
 * │ CREATE_NEW       │ Create new file, FAIL if it already exists         │
 * │                  │ Throws FileAlreadyExistsException if file exists   │
 * ├──────────────────┼────────────────────────────────────────────────────┤
 * │ DELETE_ON_CLOSE  │ Delete file when stream is closed                  │
 * │                  │ Useful for temporary files                         │
 * ├──────────────────┼────────────────────────────────────────────────────┤
 * │ SPARSE           │ Hint that file will be sparse (mostly empty)       │
 * ├──────────────────┼────────────────────────────────────────────────────┤
 * │ SYNC             │ Synchronize file content AND metadata to storage   │
 * │                  │ Every update is immediately written to disk        │
 * ├──────────────────┼────────────────────────────────────────────────────┤
 * │ DSYNC            │ Synchronize file content to storage (not metadata) │
 * │                  │ Every update is immediately written to disk        │
 * └──────────────────┴────────────────────────────────────────────────────┘
 *
 * Used in methods:
 * - Files.newBufferedReader(Path, OpenOption...)
 * - Files.newBufferedWriter(Path, OpenOption...)
 * - Files.newInputStream(Path, OpenOption...)
 * - Files.newOutputStream(Path, OpenOption...)
 * - Files.newByteChannel(Path, OpenOption...)
 * - Files.write(Path, byte[], OpenOption...)
 * - Files.writeString(Path, String, OpenOption...)
 *
 * DEFAULT BEHAVIOR (varies by method):
 * - newInputStream(): READ only
 * - newOutputStream(): CREATE, TRUNCATE_EXISTING, WRITE
 * - newBufferedReader(): READ only
 * - newBufferedWriter(): CREATE, TRUNCATE_EXISTING, WRITE
 *
 * COMMON COMBINATIONS:
 * - READ                           (read existing file)
 * - WRITE, CREATE, TRUNCATE_EXISTING   (overwrite or create file)
 * - WRITE, CREATE, APPEND              (append to file)
 * - WRITE, CREATE_NEW                  (create new, fail if exists)
 *
 * EXAM TRAP: APPEND and TRUNCATE_EXISTING are mutually exclusive!
 * EXAM TRAP: CREATE_NEW fails if file exists, CREATE does not!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * 4. FileVisitOption - Options for Directory Tree Traversal
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Enum: java.nio.file.FileVisitOption
 *
 * Values:
 * ┌──────────────────┬────────────────────────────────────────────────────┐
 * │ Enum Value       │ Description                                        │
 * ├──────────────────┼────────────────────────────────────────────────────┤
 * │ FOLLOW_LINKS     │ Follow symbolic links when traversing directories  │
 * │                  │ Default: do NOT follow symbolic links              │
 * │                  │ WARNING: Can cause infinite loops if links create  │
 * │                  │ cycles in directory structure                      │
 * └──────────────────┴────────────────────────────────────────────────────┘
 *
 * Used in methods:
 * - Files.walk(Path, FileVisitOption...)
 * - Files.walk(Path, int maxDepth, FileVisitOption...)
 * - Files.find(Path, int maxDepth, BiPredicate, FileVisitOption...)
 * - Files.walkFileTree(Path, Set<FileVisitOption>, int maxDepth, FileVisitor)
 *
 * DEFAULT BEHAVIOR (no option):
 * - Does NOT follow symbolic links (safer, avoids cycles)
 *
 * EXAM TIP: Only ONE value! Either FOLLOW_LINKS or don't.
 * EXAM TRAP: FOLLOW_LINKS can cause infinite loops with circular links!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class NIO2Options {

    public static void main(String[] args) {
        // ────────────────────────────────────────────────────────────────────
        // LinkOption examples
        // ────────────────────────────────────────────────────────────────────
        demonstrateLinkOption();

        // ────────────────────────────────────────────────────────────────────
        // StandardCopyOption examples
        // ────────────────────────────────────────────────────────────────────
        demonstrateStandardCopyOption();

        // ────────────────────────────────────────────────────────────────────
        // StandardOpenOption examples
        // ────────────────────────────────────────────────────────────────────
        demonstrateStandardOpenOption();

        // ────────────────────────────────────────────────────────────────────
        // FileVisitOption examples
        // ────────────────────────────────────────────────────────────────────
        demonstrateFileVisitOption();

        System.out.println("\n✓ All NIO.2 option examples completed");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // LinkOption.NOFOLLOW_LINKS
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateLinkOption() {
        System.out.println("=== LinkOption ===\n");

        Path path = Path.of(".");

        // Example 1: exists() with LinkOption
        System.out.println("Files.exists(path):");
        System.out.println("  Following links: " + Files.exists(path));
        System.out.println("  Not following links: " + Files.exists(path, LinkOption.NOFOLLOW_LINKS));

        // Example 2: isDirectory() with LinkOption
        System.out.println("\nFiles.isDirectory(path):");
        System.out.println("  Following links: " + Files.isDirectory(path));
        System.out.println("  Not following links: " + Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS));

        // Use case: Detecting symbolic links
        System.out.println("\nDetecting symbolic links:");
        System.out.println("If Files.isSymbolicLink(path) returns true, then:");
        System.out.println("  - exists(path) follows link to target");
        System.out.println("  - exists(path, NOFOLLOW_LINKS) checks link itself");

        // Common method signatures:
        System.out.println("\nCommon methods accepting LinkOption:");
        System.out.println("  Files.exists(Path, LinkOption...)");
        System.out.println("  Files.isDirectory(Path, LinkOption...)");
        System.out.println("  Files.isRegularFile(Path, LinkOption...)");
        System.out.println("  Path.toRealPath(LinkOption...)");
        System.out.println("  Files.readAttributes(Path, Class<A>, LinkOption...)");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // StandardCopyOption
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateStandardCopyOption() {
        System.out.println("\n=== StandardCopyOption ===\n");

        // All three enum values
        System.out.println("Enum values:");
        System.out.println("1. REPLACE_EXISTING   - Replace target if exists");
        System.out.println("2. COPY_ATTRIBUTES    - Copy file attributes");
        System.out.println("3. ATOMIC_MOVE        - Atomic move (move only!)");

        // Example: Copy with options
        System.out.println("\nExample usage:");
        System.out.println("Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);");
        System.out.println("Files.copy(source, target, StandardCopyOption.COPY_ATTRIBUTES);");
        System.out.println("Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING,");
        System.out.println("                           StandardCopyOption.COPY_ATTRIBUTES);");

        // Example: Move with atomic
        System.out.println("\nAtomic move:");
        System.out.println("Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);");

        // EXAM TRAP
        System.out.println("\nEXAM TRAP:");
        System.out.println("  ATOMIC_MOVE only works with Files.move()");
        System.out.println("  Using it with Files.copy() -> UnsupportedOperationException");

        // Default behavior
        System.out.println("\nDefault behavior (no options):");
        System.out.println("  - Throws FileAlreadyExistsException if target exists");
        System.out.println("  - Does NOT copy attributes");
        System.out.println("  - Move is NOT atomic");

        // Combining with LinkOption
        System.out.println("\nCombining with LinkOption:");
        System.out.println("Files.copy(source, target,");
        System.out.println("           StandardCopyOption.REPLACE_EXISTING,");
        System.out.println("           LinkOption.NOFOLLOW_LINKS);");
        System.out.println("  (Both implement CopyOption interface)");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // StandardOpenOption
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateStandardOpenOption() {
        System.out.println("\n=== StandardOpenOption ===\n");

        // All enum values
        System.out.println("Enum values:");
        System.out.println(" 1. READ              - Open for read access");
        System.out.println(" 2. WRITE             - Open for write access");
        System.out.println(" 3. APPEND            - Append to end (implies WRITE)");
        System.out.println(" 4. TRUNCATE_EXISTING - Truncate to 0 bytes (implies WRITE)");
        System.out.println(" 5. CREATE            - Create if doesn't exist");
        System.out.println(" 6. CREATE_NEW        - Create new, fail if exists");
        System.out.println(" 7. DELETE_ON_CLOSE   - Delete when closed");
        System.out.println(" 8. SPARSE            - File will be sparse");
        System.out.println(" 9. SYNC              - Sync content AND metadata");
        System.out.println("10. DSYNC             - Sync content only");

        // Common combinations
        System.out.println("\nCommon combinations:");
        System.out.println("\n1. Read existing file:");
        System.out.println("   Files.newInputStream(path, StandardOpenOption.READ);");
        System.out.println("   // READ is default for input streams");

        System.out.println("\n2. Overwrite or create file:");
        System.out.println("   Files.newOutputStream(path,");
        System.out.println("       StandardOpenOption.WRITE,");
        System.out.println("       StandardOpenOption.CREATE,");
        System.out.println("       StandardOpenOption.TRUNCATE_EXISTING);");
        System.out.println("   // These are defaults for output streams");

        System.out.println("\n3. Append to file:");
        System.out.println("   Files.newOutputStream(path,");
        System.out.println("       StandardOpenOption.WRITE,");
        System.out.println("       StandardOpenOption.CREATE,");
        System.out.println("       StandardOpenOption.APPEND);");

        System.out.println("\n4. Create new file (fail if exists):");
        System.out.println("   Files.newOutputStream(path,");
        System.out.println("       StandardOpenOption.WRITE,");
        System.out.println("       StandardOpenOption.CREATE_NEW);");

        System.out.println("\n5. Temporary file (deleted on close):");
        System.out.println("   Files.newOutputStream(path,");
        System.out.println("       StandardOpenOption.CREATE,");
        System.out.println("       StandardOpenOption.DELETE_ON_CLOSE);");

        // EXAM TRAPS
        System.out.println("\nEXAM TRAPS:");
        System.out.println("  1. APPEND and TRUNCATE_EXISTING are mutually exclusive!");
        System.out.println("  2. CREATE_NEW throws FileAlreadyExistsException if file exists");
        System.out.println("  3. CREATE does NOT throw exception if file exists");
        System.out.println("  4. APPEND implies WRITE (don't need to specify both)");
        System.out.println("  5. TRUNCATE_EXISTING implies WRITE");

        // Used in methods
        System.out.println("\nUsed in methods:");
        System.out.println("  Files.newBufferedReader(Path, OpenOption...)");
        System.out.println("  Files.newBufferedWriter(Path, OpenOption...)");
        System.out.println("  Files.newInputStream(Path, OpenOption...)");
        System.out.println("  Files.newOutputStream(Path, OpenOption...)");
        System.out.println("  Files.write(Path, byte[], OpenOption...)");
        System.out.println("  Files.writeString(Path, String, OpenOption...)");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // FileVisitOption
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateFileVisitOption() {
        System.out.println("\n=== FileVisitOption ===\n");

        // Only one value
        System.out.println("Enum value:");
        System.out.println("  FOLLOW_LINKS - Follow symbolic links when traversing");

        System.out.println("\nDefault behavior:");
        System.out.println("  Does NOT follow symbolic links (safer, avoids cycles)");

        // Example usage
        System.out.println("\nExample usage:");
        System.out.println("\n1. Walk directory tree WITHOUT following links (default):");
        System.out.println("   Files.walk(startPath)");

        System.out.println("\n2. Walk directory tree FOLLOWING links:");
        System.out.println("   Files.walk(startPath, FileVisitOption.FOLLOW_LINKS)");

        System.out.println("\n3. Walk with max depth:");
        System.out.println("   Files.walk(startPath, 3, FileVisitOption.FOLLOW_LINKS)");

        System.out.println("\n4. Find files:");
        System.out.println("   Files.find(startPath, Integer.MAX_VALUE,");
        System.out.println("              (path, attrs) -> path.toString().endsWith(\".txt\"),");
        System.out.println("              FileVisitOption.FOLLOW_LINKS)");

        // EXAM TRAPS
        System.out.println("\nEXAM TRAPS:");
        System.out.println("  1. FOLLOW_LINKS can cause INFINITE LOOPS with circular links!");
        System.out.println("  2. FileNotFoundException if symbolic link target doesn't exist");
        System.out.println("  3. Default is NOT to follow links (for safety)");

        // Used in methods
        System.out.println("\nUsed in methods:");
        System.out.println("  Files.walk(Path, FileVisitOption...)");
        System.out.println("  Files.walk(Path, int maxDepth, FileVisitOption...)");
        System.out.println("  Files.find(Path, int maxDepth, BiPredicate, FileVisitOption...)");
        System.out.println("  Files.walkFileTree(Path, Set<FileVisitOption>, int, FileVisitor)");
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// QUICK REFERENCE TABLE - ALL ENUMS
// ═══════════════════════════════════════════════════════════════════════════
//
// LinkOption:
// ┌───────────────────┬──────────────────────────────────┐
// │ NOFOLLOW_LINKS    │ Don't follow symbolic links      │
// └───────────────────┴──────────────────────────────────┘
//
// StandardCopyOption:
// ┌───────────────────┬──────────────────────────────────────────┐
// │ REPLACE_EXISTING  │ Replace target if exists                 │
// │ COPY_ATTRIBUTES   │ Copy file attributes                     │
// │ ATOMIC_MOVE       │ Atomic move (Files.move() only!)         │
// └───────────────────┴──────────────────────────────────────────┘
//
// StandardOpenOption:
// ┌──────────────────┬─────────────────────────────────────────────┐
// │ READ             │ Open for read                               │
// │ WRITE            │ Open for write                              │
// │ APPEND           │ Append to end (implies WRITE)               │
// │ TRUNCATE_EXISTING│ Truncate to 0 bytes (implies WRITE)         │
// │ CREATE           │ Create if doesn't exist                     │
// │ CREATE_NEW       │ Create new, fail if exists                  │
// │ DELETE_ON_CLOSE  │ Delete when closed                          │
// │ SPARSE           │ File will be sparse                         │
// │ SYNC             │ Sync content AND metadata                   │
// │ DSYNC            │ Sync content only                           │
// └──────────────────┴─────────────────────────────────────────────┘
//
// FileVisitOption:
// ┌───────────────────┬──────────────────────────────────────────┐
// │ FOLLOW_LINKS      │ Follow symbolic links (careful: cycles!) │
// └───────────────────┴──────────────────────────────────────────┘
//
// ═══════════════════════════════════════════════════════════════════════════
