package ch12modules;

/**
 * JAR FILES - IN-DEPTH GUIDE
 * ==========================
 *
 * JAR = Java ARchive
 *
 * A JAR file is a compressed archive (ZIP format) that bundles Java class files,
 * metadata, and resources into a single distributable file.
 *
 *
 * ============================================================================
 * WHY JAR FILES?
 * ============================================================================
 *
 * 1. DISTRIBUTION - Package entire application/library into single file
 * 2. COMPRESSION - Reduces file size for faster downloads
 * 3. VERSIONING - Manifest can include version information
 * 4. SECURITY - Can be digitally signed
 * 5. CLASSPATH - Easier to manage dependencies (one file vs many directories)
 * 6. EXECUTION - Can be made executable with main class specification
 *
 *
 * ============================================================================
 * JAR FILE STRUCTURE
 * ============================================================================
 *
 * myapp.jar
 * ├── META-INF/
 * │   └── MANIFEST.MF          <- Metadata file (always present)
 * ├── com/
 * │   └── example/
 * │       ├── Main.class       <- Compiled class files
 * │       └── Utils.class
 * └── resources/
 *     └── config.properties    <- Resource files
 *
 *
 * ============================================================================
 * MANIFEST FILE (META-INF/MANIFEST.MF)
 * ============================================================================
 *
 * The manifest is a special metadata file containing key-value pairs.
 *
 * COMMON MANIFEST ATTRIBUTES:
 *
 *   Manifest-Version: 1.0
 *   Created-By: 17.0.1 (Oracle Corporation)
 *   Main-Class: com.example.Main        <- For executable JARs
 *   Class-Path: lib/dependency.jar      <- Runtime dependencies
 *
 * RULES:
 * - Each line is a key: value pair
 * - Must end with a newline character
 * - Lines cannot exceed 72 characters (use continuation)
 * - Main-Class specifies fully qualified class name (no .class extension)
 *
 *
 * ============================================================================
 * JAR COMMAND OPTIONS (EXAM IMPORTANT!)
 * ============================================================================
 *
 * CREATING A JAR:
 *
 *   jar -cvf myapp.jar -C classes .
 *
 * OPTIONS:
 * ---------
 *   -c, --create       Create a new JAR file
 *   -v, --verbose      Print files being added (shows what's happening)
 *   -f, --file         Specify the JAR filename (REQUIRED for most operations)
 *   -C DIR             Change to directory before including files
 *                      (files are added relative to this directory)
 *   -e, --main-class   Specify main class for executable JAR
 *   -m, --manifest     Include manifest from specified file
 *   -t, --list         List table of contents
 *   -x, --extract      Extract files from JAR
 *   -u, --update       Update existing JAR
 *
 *
 * EXAM TIP: Options can be combined!
 *   jar -cvf  = create, verbose, file
 *   jar cvf   = same (dash is optional for single-letter options)
 *
 *
 * ============================================================================
 * JAR COMMAND EXAMPLES
 * ============================================================================
 *
 * 1. CREATE A JAR FROM CLASS FILES:
 *    ---------------------------------
 *    jar -cvf myapp.jar -C out .
 *
 *    Breakdown:
 *    -c         = create
 *    -v         = verbose (show files being added)
 *    -f myapp.jar = output filename
 *    -C out     = change to 'out' directory first
 *    .          = include all files from current dir (which is now 'out')
 *
 *
 * 2. CREATE EXECUTABLE JAR:
 *    ------------------------
 *    jar -cvfe myapp.jar com.example.Main -C out .
 *
 *    -e com.example.Main = specify main class
 *    Now you can run: java -jar myapp.jar
 *
 *
 * 3. CREATE WITH CUSTOM MANIFEST:
 *    ------------------------------
 *    jar -cvfm myapp.jar manifest.txt -C out .
 *
 *    -m manifest.txt = include custom manifest
 *
 *
 * 4. LIST CONTENTS OF JAR:
 *    -----------------------
 *    jar -tf myapp.jar
 *
 *    -t = list table of contents
 *    -f = specify file
 *
 *    Output:
 *    META-INF/
 *    META-INF/MANIFEST.MF
 *    com/example/Main.class
 *
 *
 * 5. EXTRACT JAR:
 *    -------------
 *    jar -xvf myapp.jar
 *
 *    -x = extract
 *    Extracts all files to current directory
 *
 *
 * 6. EXTRACT SPECIFIC FILE:
 *    ------------------------
 *    jar -xvf myapp.jar com/example/Main.class
 *
 *
 * ============================================================================
 * THE -C OPTION (EXAM IMPORTANT!)
 * ============================================================================
 *
 * -C changes the directory BEFORE adding files.
 *
 * WITHOUT -C:
 *   jar -cvf myapp.jar out/com/example/Main.class
 *   Result: JAR contains out/com/example/Main.class (wrong path!)
 *
 * WITH -C:
 *   jar -cvf myapp.jar -C out .
 *   Result: JAR contains com/example/Main.class (correct path!)
 *
 * The -C option is crucial because:
 * - Class files must be in correct package structure inside JAR
 * - Without it, the build directory path becomes part of the JAR structure
 *
 *
 * ============================================================================
 * RUNNING JAR FILES
 * ============================================================================
 *
 * 1. EXECUTABLE JAR (has Main-Class in manifest):
 *    ---------------------------------------------
 *    java -jar myapp.jar
 *
 *    IMPORTANT: When using -jar, the classpath is IGNORED!
 *    The JAR's Class-Path manifest attribute is used instead.
 *
 *
 * 2. NON-EXECUTABLE JAR (no Main-Class):
 *    ------------------------------------
 *    java -cp myapp.jar com.example.Main
 *
 *    -cp adds JAR to classpath
 *    Must specify main class explicitly
 *
 *
 * 3. MULTIPLE JARS ON CLASSPATH:
 *    ----------------------------
 *    Windows: java -cp "lib\a.jar;lib\b.jar;." com.example.Main
 *    Unix:    java -cp "lib/a.jar:lib/b.jar:." com.example.Main
 *
 *    Note: ; on Windows, : on Unix/Mac
 *    Note: . includes current directory
 *
 *
 * 4. WILDCARD FOR ALL JARS:
 *    -----------------------
 *    java -cp "lib/*" com.example.Main
 *
 *    Includes all JARs in lib directory
 *    Does NOT include subdirectories
 *    Does NOT include .class files in lib
 *
 *
 * ============================================================================
 * JAR vs MODULE JAR
 * ============================================================================
 *
 * REGULAR JAR:
 * - Contains .class files in package structure
 * - No module-info.class
 * - Used on classpath (-cp)
 * - All public types are accessible
 *
 * MODULE JAR:
 * - Contains module-info.class in root
 * - Used on module path (--module-path or -p)
 * - Only exported packages are accessible
 * - Has explicit dependencies (requires)
 *
 * A JAR can be BOTH:
 * - If placed on classpath: treated as regular JAR (module-info ignored)
 * - If placed on module path: treated as module
 *
 *
 * ============================================================================
 * CREATING A MODULE JAR
 * ============================================================================
 *
 * 1. Compile with module:
 *    javac -d out --module-source-path src -m mymodule
 *
 * 2. Create module JAR:
 *    jar -cvf mods/mymodule.jar -C out/mymodule .
 *
 * 3. Run module:
 *    java --module-path mods -m mymodule/com.example.Main
 *
 *
 * ============================================================================
 * EXAM TIPS
 * ============================================================================
 *
 * 1. -f is almost always required (specifies JAR filename)
 *
 * 2. -C changes directory BEFORE adding files
 *    jar -cvf app.jar -C out .    <- Correct
 *    jar -cvf app.jar out/.       <- Wrong (includes 'out' in path)
 *
 * 3. Order matters for some options:
 *    jar -cvf file.jar ...        <- -f must be followed by filename
 *    jar -cvfe file.jar Main ...  <- -e must be followed by class name
 *
 * 4. java -jar IGNORES -cp option
 *    java -cp lib.jar -jar app.jar  <- lib.jar is IGNORED!
 *
 * 5. Classpath separator:
 *    Windows: semicolon ;
 *    Unix/Mac: colon :
 *
 * 6. Wildcard * only matches JARs, not .class files
 *
 * 7. . (period) means current directory in classpath
 */
public class JarFiles {
    // This is a documentation class - no executable code needed
    // See Javadoc comments above for JAR file reference
}
