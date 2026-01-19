package ch12modules;

/**
 * MODULE TOOLS AND COMMANDS
 * ==========================
 *
 * Complete reference for module-related command-line tools:
 * - java (describe, list, resolution)
 * - jar (describe)
 * - jdeps (dependency analysis)
 * - jmod (JMOD files)
 * - jlink (custom runtimes)
 *
 *
 * ============================================================================
 * JAVA COMMAND - MODULE OPTIONS
 * ============================================================================
 *
 * Three module-related options for the java command:
 *
 * 1. DESCRIBE MODULE (-d, --describe-module)
 * 2. LIST MODULES (--list-modules)
 * 3. SHOW MODULE RESOLUTION (--show-module-resolution)
 *
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * 1. DESCRIBE MODULE
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * Shows information about a module: exports, requires, uses, provides.
 *
 * SHORT FORM:  -d <module>
 * LONG FORM:   --describe-module <module>
 *
 * EXAMPLES:
 *
 *   # Describe a JDK module
 *   java -d java.sql
 *   java --describe-module java.sql
 *
 *   # Describe a custom module on module path
 *   java -p mods -d tour.api
 *   java --module-path mods --describe-module tour.api
 *
 *
 * EXAMPLE OUTPUT (java -d java.sql):
 *
 *   java.sql@17
 *   exports java.sql
 *   exports javax.sql
 *   requires java.base mandated
 *   requires java.logging transitive
 *   requires java.transaction.xa transitive
 *   requires java.xml transitive
 *   uses java.sql.Driver
 *
 * OUTPUT BREAKDOWN:
 *   - Module name and version (java.sql@17)
 *   - Exported packages
 *   - Required modules (mandated = automatic, transitive = passed on)
 *   - Services used (uses)
 *   - Services provided (provides)
 *
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * 2. LIST MODULES
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * Lists all observable modules (JDK modules + modules on module path).
 *
 * SYNTAX: --list-modules
 *
 * EXAMPLES:
 *
 *   # List all JDK modules
 *   java --list-modules
 *
 *   # List JDK modules + custom modules on module path
 *   java -p mods --list-modules
 *   java --module-path mods --list-modules
 *
 *
 * EXAMPLE OUTPUT:
 *
 *   java.base@17
 *   java.compiler@17
 *   java.datatransfer@17
 *   java.desktop@17
 *   java.instrument@17
 *   java.logging@17
 *   java.management@17
 *   ...
 *   tour.api              <- Custom module (no version)
 *   tour.app
 *   tour.hiking
 *   tour.wine
 *
 * EXAM TIP: Use this to count total modules available!
 *           java --list-modules | wc -l  (Unix)
 *
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * 3. SHOW MODULE RESOLUTION
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * Shows how modules are resolved when the application starts.
 * Useful for debugging module dependency issues.
 *
 * SYNTAX: --show-module-resolution
 *
 * EXAMPLE:
 *
 *   java --show-module-resolution -p mods -m tour.app/com.tour.app.TourApp
 *
 *
 * EXAMPLE OUTPUT:
 *
 *   root tour.app mods/tour.app.jar
 *     tour.app requires tour.api mods/tour.api.jar
 *       tour.api requires java.base jrt:/java.base
 *     tour.hiking provides com.tour.api.Tour mods/tour.hiking.jar
 *     tour.wine provides com.tour.api.Tour mods/tour.wine.jar
 *
 * OUTPUT SHOWS:
 *   - Root module (what you're running)
 *   - Required modules and where they come from
 *   - Service providers that were discovered
 *
 *
 * ============================================================================
 * JAR COMMAND - DESCRIBE MODULE
 * ============================================================================
 *
 * You can describe a module JAR using the jar command.
 *
 * SYNTAX: jar -d -f <jarfile>
 *         jar --describe-module --file <jarfile>
 *
 * EXAMPLES:
 *
 *   jar -d -f mods/tour.api.jar
 *   jar --describe-module --file mods/tour.api.jar
 *
 *
 * EXAMPLE OUTPUT:
 *
 *   tour.api jar:file:///path/to/mods/tour.api.jar/!module-info.class
 *   exports com.tour.api
 *   requires java.base mandated
 *   uses com.tour.api.Tour
 *
 *
 * ============================================================================
 * JDEPS COMMAND - DEPENDENCY ANALYSIS
 * ============================================================================
 *
 * jdeps analyzes dependencies of Java class files, JARs, or modules.
 * CRITICAL for migrating non-modular code to modules!
 *
 * KEY OPTIONS:
 *
 * --summary (-s)
 *   Shows summary of dependencies at package level
 *
 * --jdk-internals
 *   Finds uses of internal JDK APIs (will break in future Java versions!)
 *
 * --module-path (-p)
 *   Specifies module path for analysis
 *
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * JDEPS --summary
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * SYNTAX: jdeps --summary <jarfile>
 *         jdeps -s <jarfile>
 *
 * EXAMPLE:
 *   jdeps --summary myapp.jar
 *
 * OUTPUT:
 *   myapp.jar -> java.base
 *   myapp.jar -> java.sql
 *   myapp.jar -> java.logging
 *
 * This tells you what modules your non-modular JAR depends on!
 * Useful for writing the 'requires' directives in module-info.java.
 *
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * JDEPS --jdk-internals
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * SYNTAX: jdeps --jdk-internals <jarfile>
 *
 * Finds code that uses internal JDK APIs (like sun.* or com.sun.*)
 * These APIs are encapsulated in Java 9+ and may not be accessible!
 *
 * EXAMPLE:
 *   jdeps --jdk-internals myapp.jar
 *
 * OUTPUT:
 *   myapp.jar -> java.base
 *      com.example.MyClass -> sun.misc.Unsafe
 *
 *   Warning: JDK internal APIs are unsupported and private...
 *
 *   JDK Internal API              Suggested Replacement
 *   ----------------              ---------------------
 *   sun.misc.Unsafe               See http://openjdk.java.net/jeps/260
 *
 * EXAM TIP: Use --jdk-internals to identify migration problems!
 *
 *
 * ============================================================================
 * JMOD COMMAND - JMOD FILES
 * ============================================================================
 *
 * JMOD files are an alternative to JAR files for modules.
 *
 * WHEN TO USE JMOD:
 * - Native libraries (.dll, .so, .dylib)
 * - Configuration files that can't go in a JAR
 * - Legal/license files
 *
 * IMPORTANT: JMOD files are for compile-time and link-time only!
 *            You CANNOT run directly from JMOD files at runtime.
 *            (Use JAR files or jlink for runtime)
 *
 * OPERATIONS:
 *
 *   jmod create   - Create a JMOD file
 *   jmod extract  - Extract contents of JMOD
 *   jmod describe - Print module details
 *   jmod list     - Print names of all entries
 *   jmod hash     - Record hashes of tied modules
 *
 *
 * EXAMPLES:
 *
 *   # Create JMOD
 *   jmod create --class-path out/mymodule mymodule.jmod
 *
 *   # Describe JMOD
 *   jmod describe mymodule.jmod
 *
 *   # List contents
 *   jmod list mymodule.jmod
 *
 *   # Extract
 *   jmod extract mymodule.jmod
 *
 *
 * ============================================================================
 * JLINK COMMAND - CUSTOM JAVA RUNTIMES
 * ============================================================================
 *
 * jlink creates custom Java runtime images with only the modules you need.
 *
 * BENEFITS:
 * - Smaller deployment size
 * - Faster startup
 * - No need to install full JDK on target machine
 *
 * KEY OPTIONS:
 *
 * --module-path (-p)
 *   Where to find modules (JDK modules + custom modules)
 *
 * --add-modules
 *   Which modules to include in the runtime
 *
 * --output
 *   Directory for the generated runtime image
 *
 *
 * EXAMPLE:
 *
 *   jlink --module-path mods:$JAVA_HOME/jmods \
 *         --add-modules tour.app \
 *         --output myruntime
 *
 * This creates a minimal Java runtime with:
 *   - tour.app and its dependencies (tour.api, java.base)
 *   - Only the JDK modules that are required
 *
 * RUN THE CUSTOM RUNTIME:
 *
 *   ./myruntime/bin/java -m tour.app/com.tour.app.TourApp
 *
 *
 * ============================================================================
 * COMMAND REFERENCE TABLE
 * ============================================================================
 *
 * ┌──────────────────────────────┬────────────────────────────────────────────┐
 * │ TASK                         │ COMMAND                                    │
 * ├──────────────────────────────┼────────────────────────────────────────────┤
 * │ Compile nonmodular code      │ javac -cp lib/* -d out src/*.java          │
 * ├──────────────────────────────┼────────────────────────────────────────────┤
 * │ Run nonmodular code          │ java -cp out:lib/* com.example.Main        │
 * ├──────────────────────────────┼────────────────────────────────────────────┤
 * │ Compile module               │ javac -d out --module-source-path src      │
 * │                              │       -m mymodule                          │
 * ├──────────────────────────────┼────────────────────────────────────────────┤
 * │ Run module                   │ java -p out -m mymodule/com.example.Main   │
 * ├──────────────────────────────┼────────────────────────────────────────────┤
 * │ Describe module (java)       │ java -d mymodule                           │
 * │                              │ java --describe-module mymodule            │
 * │                              │ java -p mods -d mymodule                   │
 * ├──────────────────────────────┼────────────────────────────────────────────┤
 * │ Describe module (jar)        │ jar -d -f mymodule.jar                     │
 * │                              │ jar --describe-module --file mymodule.jar  │
 * ├──────────────────────────────┼────────────────────────────────────────────┤
 * │ List available modules       │ java --list-modules                        │
 * │                              │ java -p mods --list-modules                │
 * ├──────────────────────────────┼────────────────────────────────────────────┤
 * │ View dependencies            │ jdeps --summary myapp.jar                  │
 * │                              │ jdeps -s myapp.jar                         │
 * ├──────────────────────────────┼────────────────────────────────────────────┤
 * │ Find JDK internal usage      │ jdeps --jdk-internals myapp.jar            │
 * ├──────────────────────────────┼────────────────────────────────────────────┤
 * │ Show module resolution       │ java --show-module-resolution -p mods      │
 * │                              │       -m mymodule/com.example.Main         │
 * ├──────────────────────────────┼────────────────────────────────────────────┤
 * │ Create JAR                   │ jar -cvf myapp.jar -C out .                │
 * ├──────────────────────────────┼────────────────────────────────────────────┤
 * │ Create module JAR            │ jar -cvf mods/mymodule.jar -C out/mymod .  │
 * ├──────────────────────────────┼────────────────────────────────────────────┤
 * │ Create custom runtime        │ jlink -p mods:$JAVA_HOME/jmods             │
 * │                              │       --add-modules mymodule               │
 * │                              │       --output myruntime                   │
 * └──────────────────────────────┴────────────────────────────────────────────┘
 *
 *
 * ============================================================================
 * JAVAC OPTIONS
 * ============================================================================
 *
 * ┌─────────────────────────────────┬─────────────────────────────────────────┐
 * │ OPTION                          │ DESCRIPTION                             │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ -cp <path>                      │ Classpath (also: -classpath,            │
 * │ -classpath <path>               │ --class-path)                           │
 * │ --class-path <path>             │                                         │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ -d <directory>                  │ Output directory for .class files       │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ -p <path>                       │ Module path (also: --module-path)       │
 * │ --module-path <path>            │                                         │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ --module-source-path <path>     │ Source path for multiple modules        │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ -m <module>                     │ Compile only specified module(s)        │
 * │ --module <module>               │                                         │
 * └─────────────────────────────────┴─────────────────────────────────────────┘
 *
 *
 * ============================================================================
 * JAVA OPTIONS
 * ============================================================================
 *
 * ┌─────────────────────────────────┬─────────────────────────────────────────┐
 * │ OPTION                          │ DESCRIPTION                             │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ -cp <path>                      │ Classpath (also: -classpath,            │
 * │ -classpath <path>               │ --class-path)                           │
 * │ --class-path <path>             │                                         │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ -p <path>                       │ Module path (also: --module-path)       │
 * │ --module-path <path>            │                                         │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ -m <module>[/<class>]           │ Run module (also: --module)             │
 * │ --module <module>[/<class>]     │                                         │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ -d <module>                     │ Describe module (also: --describe-mod)  │
 * │ --describe-module <module>      │                                         │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ --list-modules                  │ List observable modules                 │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ --show-module-resolution        │ Show module resolution at startup       │
 * └─────────────────────────────────┴─────────────────────────────────────────┘
 *
 *
 * ============================================================================
 * JAR OPTIONS
 * ============================================================================
 *
 * ┌─────────────────────────────────┬─────────────────────────────────────────┐
 * │ OPTION                          │ DESCRIPTION                             │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ -c, --create                    │ Create new JAR file                     │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ -v, --verbose                   │ Verbose output                          │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ -f, --file <file>               │ JAR filename                            │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ -C <dir>                        │ Change to directory before adding files │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ -d, --describe-module           │ Describe module in JAR                  │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ -e, --main-class <class>        │ Set main class for executable JAR       │
 * └─────────────────────────────────┴─────────────────────────────────────────┘
 *
 *
 * ============================================================================
 * JDEPS OPTIONS
 * ============================================================================
 *
 * ┌─────────────────────────────────┬─────────────────────────────────────────┐
 * │ OPTION                          │ DESCRIPTION                             │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ -p <path>                       │ Module path                             │
 * │ --module-path <path>            │                                         │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ -s, --summary                   │ Print dependency summary only           │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ --jdk-internals                 │ Find uses of JDK internal APIs          │
 * └─────────────────────────────────┴─────────────────────────────────────────┘
 *
 *
 * ============================================================================
 * JLINK OPTIONS
 * ============================================================================
 *
 * ┌─────────────────────────────────┬─────────────────────────────────────────┐
 * │ OPTION                          │ DESCRIPTION                             │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ -p <path>                       │ Module path (also: --module-path)       │
 * │ --module-path <path>            │                                         │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ --add-modules <modules>         │ Modules to add to runtime image         │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ --output <path>                 │ Output directory for runtime image      │
 * └─────────────────────────────────┴─────────────────────────────────────────┘
 *
 *
 * ============================================================================
 * JMOD OPTIONS
 * ============================================================================
 *
 * ┌─────────────────────────────────┬─────────────────────────────────────────┐
 * │ OPERATION                       │ DESCRIPTION                             │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ jmod create                     │ Create a new JMOD file                  │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ jmod extract                    │ Extract contents of JMOD                │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ jmod describe                   │ Print module details                    │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ jmod list                       │ Print names of all entries              │
 * ├─────────────────────────────────┼─────────────────────────────────────────┤
 * │ jmod hash                       │ Record hashes of tied modules           │
 * └─────────────────────────────────┴─────────────────────────────────────────┘
 *
 * NOTE: JMOD files are for compile-time and link-time only!
 *       Cannot run directly from JMOD at runtime (use JAR or jlink).
 *
 *
 * ============================================================================
 * EXAM SUMMARY - QUICK REFERENCE
 * ============================================================================
 *
 * DESCRIBE MODULE:
 *   java -d java.sql                    <- Short form
 *   java --describe-module java.sql     <- Long form
 *   java -p mods -d mymodule            <- Custom module
 *   jar -d -f mymodule.jar              <- From JAR
 *
 * LIST MODULES:
 *   java --list-modules                 <- All JDK modules
 *   java -p mods --list-modules         <- JDK + custom modules
 *
 * SHOW RESOLUTION:
 *   java --show-module-resolution -p mods -m mymodule/com.Main
 *
 * ANALYZE DEPENDENCIES:
 *   jdeps --summary myapp.jar           <- What modules needed
 *   jdeps --jdk-internals myapp.jar     <- Find internal API usage
 *
 * CREATE RUNTIME:
 *   jlink -p mods:$JAVA_HOME/jmods --add-modules mymod --output myruntime
 *
 *
 * EXAM TRAPS:
 * -----------
 * 1. -d means DIFFERENT things for java vs javac!
 *    java -d = describe module
 *    javac -d = output directory
 *
 * 2. JMOD files cannot be used at runtime (only compile/link time)
 *
 * 3. jdeps --jdk-internals helps identify migration problems
 *
 * 4. jlink requires module path to include $JAVA_HOME/jmods for JDK modules
 */
public class ModuleToolsAndCommands {
    // This is a documentation class - see comments above for reference
}
