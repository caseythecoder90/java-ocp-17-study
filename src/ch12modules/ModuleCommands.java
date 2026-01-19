package ch12modules;

/**
 * MODULE COMMANDS AND OPTIONS
 * ============================
 *
 * This file covers all the command-line options you need to know for the exam:
 * - javac (compilation)
 * - java (execution)
 * - jar (packaging)
 * - Module-specific options
 *
 *
 * ============================================================================
 * CLASSPATH BASICS (REVIEW)
 * ============================================================================
 *
 * The CLASSPATH tells Java where to find .class files and JARs.
 *
 * THREE WAYS TO SPECIFY CLASSPATH:
 * ---------------------------------
 *
 * 1. -cp option (short form):
 *    java -cp classes com.example.Main
 *
 * 2. --class-path option (long form):
 *    java --class-path classes com.example.Main
 *
 * 3. -classpath option (medium form):
 *    java -classpath classes com.example.Main
 *
 * All three are equivalent!
 *
 *
 * CLASSPATH SYNTAX:
 * -----------------
 *
 * PERIOD (.) = current working directory
 *    java -cp . com.example.Main
 *
 * SEPARATOR (platform-specific):
 *    Windows:  semicolon ;
 *    Unix/Mac: colon :
 *
 * EXAMPLES:
 *    Windows:  java -cp "classes;lib\\util.jar;." com.example.Main
 *    Unix:     java -cp "classes:lib/util.jar:." com.example.Main
 *
 * WILDCARD (*):
 *    java -cp "lib/*" com.example.Main
 *    - Matches all JARs in lib directory
 *    - Does NOT match subdirectories
 *    - Does NOT match .class files
 *
 *
 * ============================================================================
 * JAVAC OPTIONS (COMPILATION)
 * ============================================================================
 *
 * BASIC SYNTAX:
 *   javac [options] source-files
 *
 *
 * KEY OPTIONS:
 * ------------
 *
 * -d <directory>
 *   Specifies where to place generated .class files
 *   Creates package directories automatically
 *
 *   javac -d out src/com/example/Main.java
 *   Result: out/com/example/Main.class
 *
 *
 * -cp, --class-path, -classpath <path>
 *   Specifies where to find user class files and JARs
 *
 *   javac -cp lib/util.jar -d out src/com/example/Main.java
 *
 *
 * --module-path, -p <path>
 *   Specifies where to find application modules
 *   (Used when compiling code that depends on modules)
 *
 *   javac -p mods -d out src/com/example/Main.java
 *
 *
 * --module-source-path <path>
 *   Specifies where to find module source files
 *   Used when compiling multiple modules at once
 *
 *   javac --module-source-path src -d out -m mymodule
 *
 *
 * -m, --module <module>
 *   Compile only the specified module(s)
 *
 *   javac --module-source-path src -d out -m mymodule,othermodule
 *
 *
 * ============================================================================
 * JAVA OPTIONS (EXECUTION)
 * ============================================================================
 *
 * BASIC SYNTAX (non-module):
 *   java [options] mainclass [args]
 *   java [options] -jar jarfile [args]
 *
 * BASIC SYNTAX (module):
 *   java [options] -m module[/mainclass] [args]
 *   java [options] --module module[/mainclass] [args]
 *
 *
 * KEY OPTIONS:
 * ------------
 *
 * -cp, --class-path, -classpath <path>
 *   Specifies where to find user class files
 *
 *   java -cp out com.example.Main
 *   java -cp "out;lib/*" com.example.Main    (Windows)
 *   java -cp "out:lib/*" com.example.Main    (Unix)
 *
 *
 * -jar <jarfile>
 *   Execute JAR file (must have Main-Class in manifest)
 *
 *   java -jar myapp.jar
 *
 *   IMPORTANT: -jar IGNORES the classpath!
 *   java -cp lib.jar -jar myapp.jar  <- lib.jar is IGNORED!
 *
 *
 * --module-path, -p <path>
 *   Specifies where to find application modules
 *
 *   java -p mods -m mymodule/com.example.Main
 *   java --module-path mods --module mymodule/com.example.Main
 *
 *
 * -m, --module <module>[/<mainclass>]
 *   Specifies the module (and optionally main class) to run
 *
 *   java -p mods -m mymodule                    <- Main class in module-info
 *   java -p mods -m mymodule/com.example.Main   <- Explicit main class
 *
 *
 * ============================================================================
 * RUNNING MODULES (EXAM IMPORTANT!)
 * ============================================================================
 *
 * SYNTAX:
 *   java --module-path <path> --module <module>/<package>.<MainClass>
 *   java -p <path> -m <module>/<package>.<MainClass>
 *
 * COMPONENTS:
 *   --module-path (-p) : Directory containing module JARs or exploded modules
 *   --module (-m)      : Module name followed by main class
 *   /                  : Separator between module name and main class
 *
 *
 * EXAMPLES:
 *
 * 1. Run module with explicit main class:
 *    java -p mods -m mymodule/com.example.Main
 *
 *    Breakdown:
 *    -p mods                = Module path is 'mods' directory
 *    -m mymodule            = Module name
 *    /com.example.Main      = Fully qualified main class
 *
 *
 * 2. Run module with main class from module-info (using Main-Class attribute):
 *    java -p mods -m mymodule
 *
 *
 * 3. Run with multiple module paths:
 *    java -p "mods;libs" -m mymodule/com.example.Main   (Windows)
 *    java -p "mods:libs" -m mymodule/com.example.Main   (Unix)
 *
 *
 * 4. Run from exploded modules (not JARs):
 *    java -p out -m mymodule/com.example.Main
 *
 *
 * ============================================================================
 * JAR OPTIONS (PACKAGING)
 * ============================================================================
 *
 * BASIC SYNTAX:
 *   jar [options] [jar-file] [manifest-file] [entry-point] [-C dir] files
 *
 *
 * KEY OPTIONS:
 * ------------
 *
 * -c, --create
 *   Create a new JAR file
 *
 * -v, --verbose
 *   Generate verbose output (shows files being processed)
 *
 * -f, --file <filename>
 *   Specify the JAR filename
 *
 * -C <directory>
 *   Change to directory before adding files
 *   CRITICAL: Use this to avoid including build directory in paths!
 *
 * -e, --main-class <classname>
 *   Specify the main class for executable JAR
 *
 * -m, --manifest <filename>
 *   Include manifest from specified file
 *
 * -t, --list
 *   List table of contents
 *
 * -x, --extract
 *   Extract files from JAR
 *
 *
 * COMMON COMBINATIONS:
 * --------------------
 *
 * Create JAR:
 *   jar -cvf myapp.jar -C out .
 *   jar --create --verbose --file myapp.jar -C out .
 *
 * Create executable JAR:
 *   jar -cvfe myapp.jar com.example.Main -C out .
 *
 * Create module JAR:
 *   jar -cvf mods/mymodule.jar -C out/mymodule .
 *
 * List contents:
 *   jar -tf myapp.jar
 *
 * Extract:
 *   jar -xvf myapp.jar
 *
 *
 * ============================================================================
 * COMPLETE MODULE WORKFLOW
 * ============================================================================
 *
 * PROJECT STRUCTURE:
 *
 *   myproject/
 *   ├── src/
 *   │   └── mymodule/
 *   │       ├── module-info.java
 *   │       └── com/
 *   │           └── example/
 *   │               └── Main.java
 *   ├── out/          <- Compiled classes go here
 *   └── mods/         <- Module JARs go here
 *
 *
 * STEP 1: Compile the module
 *   javac -d out --module-source-path src -m mymodule
 *
 *   Result:
 *   out/
 *   └── mymodule/
 *       ├── module-info.class
 *       └── com/example/Main.class
 *
 *
 * STEP 2: Create module JAR
 *   jar -cvf mods/mymodule.jar -C out/mymodule .
 *
 *   Result:
 *   mods/
 *   └── mymodule.jar
 *
 *
 * STEP 3: Run the module
 *   java -p mods -m mymodule/com.example.Main
 *
 *
 * ============================================================================
 * ADDITIONAL MODULE OPTIONS
 * ============================================================================
 *
 * --add-modules <module>(,<module>)*
 *   Add modules to the default set (root modules)
 *
 *   java --add-modules java.logging -p mods -m mymodule/com.example.Main
 *
 *
 * --add-exports <module>/<package>=<target-module>(,<target-module>)*
 *   Export a package from one module to another at runtime
 *   (Bypasses module boundaries - use sparingly!)
 *
 *   java --add-exports java.base/sun.security.ssl=mymodule -p mods -m mymodule
 *
 *
 * --add-reads <module>=<target-module>(,<target-module>)*
 *   Add a module read edge at runtime
 *
 *
 * --add-opens <module>/<package>=<target-module>(,<target-module>)*
 *   Open a package for deep reflection at runtime
 *
 *
 * --describe-module, -d <module>
 *   Describe a module (show its exports, requires, etc.)
 *
 *   java -p mods -d mymodule
 *
 *
 * --list-modules
 *   List all observable modules (JDK + module path)
 *
 *   java --list-modules
 *   java -p mods --list-modules
 *
 *
 * --show-module-resolution
 *   Show module resolution output during startup
 *
 *
 * ============================================================================
 * EXAM COMMAND SUMMARY
 * ============================================================================
 *
 * CLASSPATH OPTIONS (all equivalent):
 *   -cp, -classpath, --class-path
 *   Separator: ; (Windows) or : (Unix)
 *   . = current directory
 *   * = all JARs in directory
 *
 * JAVAC:
 *   javac -d <output-dir> <source-files>
 *   javac -d out --module-source-path src -m mymodule
 *
 * JAVA (non-module):
 *   java -cp <classpath> <mainclass>
 *   java -jar <jarfile>                    <- Ignores -cp!
 *
 * JAVA (module):
 *   java -p <module-path> -m <module>/<mainclass>
 *   java --module-path <path> --module <module>/<class>
 *
 * JAR:
 *   jar -cvf <jarfile> -C <dir> .          <- Create
 *   jar -cvfe <jarfile> <mainclass> -C <dir> .  <- Executable
 *   jar -tf <jarfile>                      <- List
 *   jar -xvf <jarfile>                     <- Extract
 *
 * MODULE OPTIONS:
 *   -p, --module-path     : Where to find modules
 *   -m, --module          : Which module to run
 *   -d, --describe-module : Describe a module
 *   --list-modules        : List all modules
 */
public class ModuleCommands {
    // This is a documentation class - no executable code needed
    // See Javadoc comments above for command reference
}
