package ch14io;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

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

    public static void main(String[] args) throws IOException {
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

        System.out.println("\n" + "=".repeat(70));
        System.out.println("All NIO.2 option examples completed!");
        System.out.println("=".repeat(70));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // LinkOption.NOFOLLOW_LINKS
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateLinkOption() throws IOException {
        System.out.println("=== LinkOption ===\n");

        Path tempDir = Files.createTempDirectory("linkOption");
        Path actualFile = tempDir.resolve("actual.txt");
        Path symLink = tempDir.resolve("link.txt");

        try {
            // Create actual file
            Files.writeString(actualFile, "Hello from actual file");

            // Create symbolic link (only works on systems that support it)
            try {
                Files.createSymbolicLink(symLink, actualFile);

                System.out.println("Created: " + actualFile.getFileName());
                System.out.println("Created: " + symLink.getFileName() + " -> " + actualFile.getFileName());

                // Example 1: exists() with symbolic link
                System.out.println("\nFiles.exists(symLink):");
                System.out.println("  Default (follows link): " + Files.exists(symLink));
                System.out.println("  NOFOLLOW_LINKS: " + Files.exists(symLink, LinkOption.NOFOLLOW_LINKS));

                // Delete actual file to show difference
                Files.delete(actualFile);
                System.out.println("\nAfter deleting actual.txt:");
                System.out.println("  Default (follows link): " + Files.exists(symLink) + " (broken link)");
                System.out.println("  NOFOLLOW_LINKS: " + Files.exists(symLink, LinkOption.NOFOLLOW_LINKS) + " (link itself exists)");

                // Detecting broken symbolic links - EXAM TIP!
                boolean isBrokenLink = !Files.exists(symLink) && Files.exists(symLink, LinkOption.NOFOLLOW_LINKS);
                System.out.println("\n  Is broken link? " + isBrokenLink);
                System.out.println("  Explanation: Link file exists, but target is gone");

            } catch (UnsupportedOperationException | java.nio.file.FileSystemException e) {
                System.out.println("Symbolic links require admin privileges on Windows");
                System.out.println("\nConcept demonstration:");
                System.out.println("  If 'link.txt' -> 'actual.txt' (symbolic link):");
                System.out.println("    Files.exists(link) DEFAULT: checks if actual.txt exists");
                System.out.println("    Files.exists(link, NOFOLLOW_LINKS): checks if link itself exists");
                System.out.println("\n  Use case: Detecting if a path is a broken link");
                System.out.println("    !exists(path) && exists(path, NOFOLLOW_LINKS) = broken symlink");
            }
        } finally {
            // Cleanup
            Files.deleteIfExists(symLink);
            Files.deleteIfExists(actualFile);
            Files.deleteIfExists(tempDir);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // StandardCopyOption
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateStandardCopyOption() throws IOException {
        System.out.println("\n=== StandardCopyOption ===\n");

        Path tempDir = Files.createTempDirectory("copyOption");

        try {
            Path source = tempDir.resolve("source.txt");
            Path target = tempDir.resolve("target.txt");

            // Example 1: Copy without REPLACE_EXISTING
            Files.writeString(source, "Original content");
            Files.copy(source, target);
            System.out.println("1. Copy without options: SUCCESS");

            // Example 2: Copy again - should throw exception
            try {
                Files.copy(source, target);
                System.out.println("2. Copy when target exists: (should not see this)");
            } catch (FileAlreadyExistsException e) {
                System.out.println("2. Copy when target exists: FileAlreadyExistsException thrown!");
            }

            // Example 3: REPLACE_EXISTING
            Files.writeString(source, "Updated content");
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("3. REPLACE_EXISTING: " + Files.readString(target));

            // Example 4: COPY_ATTRIBUTES
            Files.delete(target);
            Thread.sleep(2000); // Wait to show time difference
            Files.copy(source, target, StandardCopyOption.COPY_ATTRIBUTES);
            System.out.println("4. COPY_ATTRIBUTES: Last modified times match: " +
                    (Files.getLastModifiedTime(source).equals(Files.getLastModifiedTime(target))));

            // Example 5: ATOMIC_MOVE
            Path moveSource = tempDir.resolve("move-source.txt");
            Path moveTarget = tempDir.resolve("move-target.txt");
            Files.writeString(moveSource, "Move me");
            Files.move(moveSource, moveTarget, StandardCopyOption.ATOMIC_MOVE);
            System.out.println("5. ATOMIC_MOVE: Moved (exists=" + Files.exists(moveTarget) +
                    ", source deleted=" + !Files.exists(moveSource) + ")");

            // Example 6: EXAM TRAP - ATOMIC_MOVE with copy
            try {
                Files.copy(source, tempDir.resolve("test.txt"), StandardCopyOption.ATOMIC_MOVE);
            } catch (UnsupportedOperationException e) {
                System.out.println("6. EXAM TRAP: ATOMIC_MOVE with copy() -> UnsupportedOperationException!");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // Cleanup
            try (Stream<Path> paths = Files.walk(tempDir)) {
                paths.sorted((a, b) -> b.compareTo(a)).forEach(p -> {
                    try { Files.deleteIfExists(p); } catch (IOException e) {}
                });
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // StandardOpenOption
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateStandardOpenOption() throws IOException {
        System.out.println("\n=== StandardOpenOption ===\n");

        Path tempDir = Files.createTempDirectory("openOption");

        try {
            // Example 1: CREATE - creates if doesn't exist, opens if exists
            Path file1 = tempDir.resolve("test1.txt");
            Files.writeString(file1, "First", StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            Files.writeString(file1, "Second", StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("1. CREATE + TRUNCATE_EXISTING: " + Files.readString(file1));

            // Example 2: APPEND
            Path file2 = tempDir.resolve("test2.txt");
            Files.writeString(file2, "Line 1\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(file2, "Line 2\n", StandardOpenOption.APPEND);
            Files.writeString(file2, "Line 3\n", StandardOpenOption.APPEND);
            System.out.println("2. APPEND:");
            System.out.println(Files.readString(file2).trim().replace("\n", ", "));

            // Example 3: CREATE_NEW - fails if exists
            Path file3 = tempDir.resolve("test3.txt");
            Files.writeString(file3, "New file", StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
            try {
                Files.writeString(file3, "Try again", StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
            } catch (FileAlreadyExistsException e) {
                System.out.println("3. CREATE_NEW on existing file: FileAlreadyExistsException!");
            }

            // Example 4: DELETE_ON_CLOSE
            Path file4 = tempDir.resolve("test4.txt");
            try (var out = Files.newOutputStream(file4, StandardOpenOption.CREATE, StandardOpenOption.DELETE_ON_CLOSE)) {
                out.write("Temporary data".getBytes());
                System.out.println("4. DELETE_ON_CLOSE: File exists inside try: " + Files.exists(file4));
            }
            System.out.println("   DELETE_ON_CLOSE: File exists after close: " + Files.exists(file4));

            // Example 5: EXAM TRAP - APPEND and TRUNCATE_EXISTING together
            try {
                Path file5 = tempDir.resolve("test5.txt");
                Files.writeString(file5, "Test",
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND,
                    StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IllegalArgumentException e) {
                System.out.println("5. EXAM TRAP: APPEND + TRUNCATE_EXISTING -> IllegalArgumentException!");
            }

            // Example 6: Default behavior
            Path file6 = tempDir.resolve("test6.txt");
            Files.writeString(file6, "Original");
            Files.writeString(file6, "Default behavior");
            System.out.println("6. Default (no options): " + Files.readString(file6) + " (overwrites)");

        } finally {
            // Cleanup
            try (Stream<Path> paths = Files.walk(tempDir)) {
                paths.sorted((a, b) -> b.compareTo(a)).forEach(p -> {
                    try { Files.deleteIfExists(p); } catch (IOException e) {}
                });
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // FileVisitOption
    // ═══════════════════════════════════════════════════════════════════════════
    private static void demonstrateFileVisitOption() throws IOException {
        System.out.println("\n=== FileVisitOption ===\n");

        Path tempDir = Files.createTempDirectory("visitOption");

        try {
            // Create directory structure
            Path subDir1 = tempDir.resolve("dir1");
            Path subDir2 = tempDir.resolve("dir2");
            Files.createDirectories(subDir1);
            Files.createDirectories(subDir2);

            Files.createFile(tempDir.resolve("root.txt"));
            Files.createFile(subDir1.resolve("file1.txt"));
            Files.createFile(subDir2.resolve("file2.txt"));

            // Example 1: Walk WITHOUT following links (default)
            long countDefault;
            try (Stream<Path> stream = Files.walk(tempDir)) {
                countDefault = stream.filter(Files::isRegularFile).count();
            }
            System.out.println("1. Files.walk(tempDir) - Default (no symlinks): " + countDefault + " files");

            // Example 2: Create symbolic link and walk WITH FOLLOW_LINKS
            try {
                Path link = tempDir.resolve("linkedDir");
                Files.createSymbolicLink(link, subDir1);

                long countWithLinks;
                try (Stream<Path> stream = Files.walk(tempDir, FileVisitOption.FOLLOW_LINKS)) {
                    countWithLinks = stream.filter(Files::isRegularFile).count();
                }
                System.out.println("2. Files.walk(tempDir, FOLLOW_LINKS): " + countWithLinks + " files (includes linked)");

                long countWithoutLinks;
                try (Stream<Path> stream = Files.walk(tempDir)) {
                    countWithoutLinks = stream.filter(Files::isRegularFile).count();
                }
                System.out.println("   Files.walk(tempDir) without option: " + countWithoutLinks + " files (link ignored)");

            } catch (UnsupportedOperationException | java.nio.file.FileSystemException e) {
                System.out.println("2. Symbolic links require admin on Windows");
                System.out.println("   FOLLOW_LINKS would traverse into linked directories");
                System.out.println("   Default ignores symlinks (safer - avoids infinite loops)");
            }

            // Example 3: Files.find with FileVisitOption
            System.out.print("3. Files.find .txt files: ");
            try (Stream<Path> stream = Files.find(tempDir, Integer.MAX_VALUE,
                    (path, attrs) -> path.toString().endsWith(".txt"))) {
                System.out.println(stream.count() + " found");
            }

            // Example 4: Max depth
            System.out.print("4. Files.walk with maxDepth=1 (root only): ");
            try (Stream<Path> stream = Files.walk(tempDir, 1)) {
                System.out.println(stream.filter(Files::isRegularFile).count() + " files");
            }

        } finally {
            // Cleanup
            try (Stream<Path> paths = Files.walk(tempDir)) {
                paths.sorted((a, b) -> b.compareTo(a)).forEach(p -> {
                    try { Files.deleteIfExists(p); } catch (IOException e) {}
                });
            }
        }
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
