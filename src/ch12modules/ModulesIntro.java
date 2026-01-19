package ch12modules;

/**
 * JAVA PLATFORM MODULE SYSTEM (JPMS)
 * ===================================
 *
 * Introduced in Java 9, JPMS provides a way to organize Java code into modules.
 *
 *
 * ============================================================================
 * WHAT IS A MODULE?
 * ============================================================================
 *
 * A MODULE is a group of one or more RELATED PACKAGES plus a special file
 * called module-info.java.
 *
 * The main purpose of a module is to provide groups of related packages that
 * offer developers a particular set of functionality.
 *
 * Module structure:
 *
 *   mymodule/
 *   ├── module-info.java          <- Module declaration (REQUIRED, in root)
 *   └── com/
 *       └── example/
 *           ├── api/
 *           │   └── MyService.java
 *           └── internal/
 *               └── Helper.java   <- Can be hidden from other modules!
 *
 *
 * ============================================================================
 * WHAT JPMS PROVIDES
 * ============================================================================
 *
 * 1. A FORMAT FOR MODULE JAR FILES
 *    - JAR files can now contain module-info.class
 *    - Defines what the module exports and requires
 *
 * 2. PARTITIONING OF THE JDK INTO MODULES
 *    - JDK itself is modularized (java.base, java.sql, java.logging, etc.)
 *    - You can see them with: java --list-modules
 *
 * 3. ADDITIONAL COMMAND-LINE OPTIONS FOR JAVA TOOLS
 *    - --module-path (-p), --module (-m), --add-modules, etc.
 *
 *
 * ============================================================================
 * BENEFITS OF MODULES
 * ============================================================================
 *
 * 1. BETTER ACCESS CONTROL
 *    - Packages can be internal to a module (not exported)
 *    - Even public classes are hidden if package is not exported
 *    - Stronger encapsulation than just public/private
 *
 * 2. CLEARER DEPENDENCY MANAGEMENT
 *    - Module explicitly declares what it requires
 *    - Dependencies are checked at compile time AND runtime
 *    - No more "JAR hell" - missing dependencies caught early
 *    - Circular dependencies are detected and prevented
 *
 * 3. CUSTOM JAVA BUILDS
 *    - Create minimal Java runtime with only needed modules
 *    - Use jlink to create custom runtime images
 *    - Don't need entire JDK for simple applications
 *
 * 4. IMPROVED SECURITY
 *    - Smaller attack surface (fewer modules = fewer vulnerabilities)
 *    - Can omit modules with security-sensitive APIs
 *    - Internal APIs are truly hidden
 *
 * 5. IMPROVED PERFORMANCE
 *    - Smaller Java package = lower memory footprint
 *    - Faster startup (less to load)
 *    - JVM can optimize based on module boundaries
 *
 * 6. UNIQUE PACKAGE ENFORCEMENT
 *    - Each package can only come from ONE module
 *    - No split packages (same package in multiple JARs)
 *    - Eliminates confusion about which class is being loaded
 *
 *
 * ============================================================================
 * MODULE DECLARATION FILE (module-info.java)
 * ============================================================================
 *
 * RULES (EXAM IMPORTANT!):
 * ------------------------
 *
 * 1. LOCATION: Must be in the ROOT directory of your module
 *    - NOT in a package
 *    - Regular Java files should be in packages
 *
 *    CORRECT:                    WRONG:
 *    mymodule/                   mymodule/
 *    ├── module-info.java        └── com/
 *    └── com/                        └── example/
 *        └── example/                    └── module-info.java  <- WRONG!
 *            └── Main.java
 *
 * 2. KEYWORD: Uses 'module' keyword (not class, interface, enum, or record)
 *
 *    module mymodule {
 *        // directives here
 *    }
 *
 * 3. MODULE NAMING RULES (same as package naming):
 *    - Must be a valid Java identifier
 *    - Can contain periods (like packages): com.example.mymodule
 *    - Cannot be a reserved word
 *    - By convention, lowercase with periods
 *    - Often matches the root package name
 *    - Cannot start with a digit
 *    - Can contain letters, digits, underscores, but not hyphens
 *
 *    VALID:   mymodule, com.example.api, my_module, module123
 *    INVALID: 123module, my-module, class, public
 *
 *
 * ============================================================================
 * MODULE DECLARATION SYNTAX
 * ============================================================================
 *
 *   module com.example.mymodule {
 *       requires java.sql;                    // Dependency
 *       requires transitive java.logging;    // Transitive dependency
 *
 *       exports com.example.api;             // Export to all
 *       exports com.example.spi to           // Export to specific modules
 *           com.example.impl,
 *           com.example.test;
 *
 *       opens com.example.model;             // Open for reflection
 *       opens com.example.internal to        // Open to specific modules
 *           com.fasterxml.jackson;
 *
 *       uses com.example.spi.Service;        // Service consumer
 *       provides com.example.spi.Service     // Service provider
 *           with com.example.impl.ServiceImpl;
 *   }
 *
 *
 * ============================================================================
 * MODULE DIRECTIVES (EXAM IMPORTANT!)
 * ============================================================================
 *
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ DIRECTIVE              │ DESCRIPTION                                    │
 * ├─────────────────────────────────────────────────────────────────────────┤
 * │ exports                │ Makes package available to other modules       │
 * │ exports ... to        │ Makes package available to specific modules    │
 * │ requires              │ Declares dependency on another module          │
 * │ requires transitive   │ Dependency is passed to modules that require   │
 * │                       │ this module                                    │
 * │ opens                 │ Allows reflection access to package            │
 * │ opens ... to          │ Allows reflection access to specific modules   │
 * │ open (module keyword) │ Opens ALL packages for reflection              │
 * │ uses                  │ Declares this module consumes a service        │
 * │ provides ... with     │ Declares this module provides a service impl   │
 * └─────────────────────────────────────────────────────────────────────────┘
 *
 *
 * ============================================================================
 * EXPORTS DIRECTIVE
 * ============================================================================
 *
 * Makes a package AVAILABLE to other modules.
 *
 * SYNTAX:
 *   exports packageName;
 *   exports packageName to module1, module2;
 *
 * WHAT IS EXPORTED:
 * - All PUBLIC classes, interfaces, enums, and records in the package
 * - All PUBLIC and PROTECTED fields and methods in those types
 *
 * WHAT IS NOT EXPORTED:
 * - Package-private (default) classes
 * - Private members
 * - Packages not listed in exports
 *
 * EXAMPLES:
 *
 *   // Export to ALL modules
 *   exports com.example.api;
 *
 *   // Export to SPECIFIC modules only (qualified export)
 *   exports com.example.internal to
 *       com.example.impl,
 *       com.example.test;
 *
 *
 * ============================================================================
 * REQUIRES DIRECTIVE
 * ============================================================================
 *
 * Declares a DEPENDENCY on another module.
 *
 * SYNTAX:
 *   requires moduleName;
 *   requires transitive moduleName;
 *
 * EXAMPLES:
 *
 *   module myapp {
 *       requires java.sql;        // Depend on java.sql module
 *       requires java.logging;    // Depend on java.logging module
 *   }
 *
 * NOTE: Every module implicitly requires java.base (no need to declare it)
 *
 *
 * ============================================================================
 * REQUIRES TRANSITIVE (EXAM IMPORTANT!)
 * ============================================================================
 *
 * When you use 'requires transitive', any module that requires YOUR module
 * will ALSO automatically depend on the transitive module.
 *
 * EXAMPLE:
 *
 *   module com.example.api {
 *       requires transitive java.sql;  // Transitive dependency
 *   }
 *
 *   module com.example.app {
 *       requires com.example.api;      // Automatically gets java.sql too!
 *   }
 *
 * WHY USE IT?
 * - When your API exposes types from another module
 * - Consumers of your module need access to those types
 *
 * EXAM TRAP: You CANNOT mix requires and requires transitive for same module!
 *
 *   module mymodule {
 *       requires java.sql;
 *       requires transitive java.sql;  // COMPILATION ERROR!
 *   }
 *
 *
 * ============================================================================
 * OPENS DIRECTIVE (REFLECTION)
 * ============================================================================
 *
 * Allows REFLECTION access to a package at runtime.
 *
 * Even if a package is not exported, you can open it for reflection.
 * This is needed for frameworks like Jackson, Hibernate, Spring that use
 * reflection to access private fields.
 *
 * SYNTAX:
 *   opens packageName;                    // Open to all modules
 *   opens packageName to module1, module2; // Open to specific modules
 *
 * EXAMPLES:
 *
 *   module myapp {
 *       // Package not exported (compile-time), but open for reflection (runtime)
 *       opens com.example.model;
 *
 *       // Open only to Jackson for JSON serialization
 *       opens com.example.dto to com.fasterxml.jackson.databind;
 *   }
 *
 * DIFFERENCE: exports vs opens
 *   exports = compile-time access (can use types directly)
 *   opens   = runtime reflection access (setAccessible works)
 *
 *
 * ============================================================================
 * OPEN MODULE (EXAM IMPORTANT!)
 * ============================================================================
 *
 * You can declare an entire module as OPEN, which opens ALL packages
 * for reflection.
 *
 * SYNTAX:
 *   open module mymodule {
 *       // directives
 *   }
 *
 * EXAM TRAP: You CANNOT mix 'open module' with 'opens' directive!
 *
 *   open module mymodule {
 *       opens com.example.model;  // COMPILATION ERROR!
 *   }
 *
 * If module is already open, individual opens directives are redundant
 * and cause a compilation error.
 *
 *
 * ============================================================================
 * USES AND PROVIDES (SERVICE LOADER)
 * ============================================================================
 *
 * For Service Provider Interface (SPI) pattern.
 *
 * USES - Declares that this module CONSUMES a service
 *   uses com.example.spi.MyService;
 *
 * PROVIDES - Declares that this module PROVIDES a service implementation
 *   provides com.example.spi.MyService
 *       with com.example.impl.MyServiceImpl;
 *
 * EXAMPLE:
 *
 *   // Service interface module
 *   module com.example.spi {
 *       exports com.example.spi;
 *   }
 *
 *   // Service consumer module
 *   module com.example.app {
 *       requires com.example.spi;
 *       uses com.example.spi.MyService;
 *   }
 *
 *   // Service provider module
 *   module com.example.impl {
 *       requires com.example.spi;
 *       provides com.example.spi.MyService
 *           with com.example.impl.MyServiceImpl;
 *   }
 *
 *
 * ============================================================================
 * ACCESS CONTROL WITH MODULES (EXAM TABLE!)
 * ============================================================================
 *
 * ┌─────────────┬────────────────────────────┬────────────────────────────────┐
 * │ ACCESS      │ WITHIN MODULE CODE         │ OUTSIDE MODULE                 │
 * │ LEVEL       │                            │                                │
 * ├─────────────┼────────────────────────────┼────────────────────────────────┤
 * │ private     │ Available only within      │ No access                      │
 * │             │ the class                  │                                │
 * ├─────────────┼────────────────────────────┼────────────────────────────────┤
 * │ package     │ Available only within      │ No access                      │
 * │ (default)   │ the package                │                                │
 * ├─────────────┼────────────────────────────┼────────────────────────────────┤
 * │ protected   │ Available only within      │ Accessible to SUBCLASSES only  │
 * │             │ package or to subclasses   │ IF the package is EXPORTED     │
 * ├─────────────┼────────────────────────────┼────────────────────────────────┤
 * │ public      │ Available to ALL classes   │ Accessible only IF the         │
 * │             │ in the module              │ package is EXPORTED            │
 * └─────────────┴────────────────────────────┴────────────────────────────────┘
 *
 * KEY INSIGHT:
 * - public doesn't mean accessible anymore!
 * - A public class in a non-exported package is INVISIBLE outside the module
 * - Modules add a layer of access control ABOVE the traditional access modifiers
 *
 *
 * ============================================================================
 * EXAM SUMMARY - MODULE DIRECTIVES
 * ============================================================================
 *
 * exports pkg                  - Package available to all modules
 * exports pkg to mod1, mod2    - Package available to specific modules
 *
 * requires mod                 - Depend on module
 * requires transitive mod      - Depend + pass dependency to consumers
 *                               CANNOT MIX with requires for same module!
 *
 * opens pkg                    - Allow reflection on package
 * opens pkg to mod1, mod2      - Allow reflection to specific modules
 *
 * open module                  - Open ALL packages for reflection
 *                               CANNOT MIX with opens directives!
 *
 * uses ServiceType             - Consume a service
 * provides ServiceType with Impl - Provide service implementation
 */
public class ModulesIntro {
    // This is a documentation class - no executable code needed
    // See Javadoc comments above for module system reference
}
