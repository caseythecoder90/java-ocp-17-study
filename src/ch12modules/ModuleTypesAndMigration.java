package ch12modules;

/**
 * MODULE TYPES AND MIGRATION STRATEGIES
 * ======================================
 *
 * Three types of modules and how to migrate applications to JPMS.
 *
 *
 * ============================================================================
 * THREE TYPES OF MODULES
 * ============================================================================
 *
 * 1. NAMED MODULE
 * 2. AUTOMATIC MODULE
 * 3. UNNAMED MODULE
 *
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * 1. NAMED MODULE
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * A module that CONTAINS a module-info.java file.
 *
 * CHARACTERISTICS:
 * - Has module-info.java in the root of the JAR (alongside packages)
 * - Appears on the MODULE PATH (not classpath)
 * - Name is defined INSIDE the module-info.java file
 * - Explicitly declares exports and requires
 * - Full control over encapsulation
 *
 * STRUCTURE:
 *   mymodule.jar
 *   ├── module-info.class    <- Defines the module
 *   └── com/
 *       └── example/
 *           └── MyClass.class
 *
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * 2. AUTOMATIC MODULE
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * A regular JAR (no module-info.java) placed on the MODULE PATH.
 * Java AUTOMATICALLY treats it as a module.
 *
 * MEMORY AID: Java "automatically" determines the module name!
 *
 * CHARACTERISTICS:
 * - NO module-info.java file
 * - Placed on MODULE PATH (not classpath)
 * - Java determines the module name automatically
 * - AUTOMATICALLY EXPORTS ALL PACKAGES
 * - Can read from all other modules (named, automatic, unnamed)
 * - Code referencing it treats it AS IF module-info.java exists
 *
 * AUTOMATIC MODULE NAME DETERMINATION:
 *
 *   1. If MANIFEST.MF contains Automatic-Module-Name attribute, use that
 *   2. Otherwise, derive from JAR filename using algorithm:
 *      a. Remove the .jar extension
 *      b. Remove version information from end (digits, dots, extra info)
 *      c. Replace remaining non-letters/non-digits with dots
 *      d. Replace sequences of dots with single dot
 *      e. Remove leading/trailing dots
 *
 *
 * AUTOMATIC MODULE NAME EXAMPLES:
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * SIMPLE EXAMPLE:
 *   commons-lang.jar  →  commons.lang
 *
 * COMPLEX EXAMPLES:
 *
 *   JAR Filename                          Automatic Module Name
 *   ────────────────────────────────────  ─────────────────────────
 *   my-utils-1.0.jar                   →  my.utils
 *   spring-core-5.3.21.jar             →  spring.core
 *   jackson-databind-2.13.3.jar        →  jackson.databind
 *   log4j-api-2.17.2.jar               →  log4j.api
 *   commons-collections4-4.4.jar       →  commons.collections4
 *   guava-31.1-jre.jar                 →  guava
 *   hibernate-core-6.0.0.Final.jar     →  hibernate.core
 *   some_lib--v2.0.1-SNAPSHOT.jar      →  some.lib
 *   my---weird___name-1.2.3.jar        →  my.weird.name
 *
 * ALGORITHM WALKTHROUGH (jackson-databind-2.13.3.jar):
 *   1. Remove .jar extension      → jackson-databind-2.13.3
 *   2. Remove version at end      → jackson-databind
 *   3. Replace non-alphanum       → jackson.databind
 *   4. Collapse multiple dots     → jackson.databind (no change)
 *   5. Remove leading/trailing    → jackson.databind (no change)
 *
 * ALGORITHM WALKTHROUGH (my---weird___name-1.2.3.jar):
 *   1. Remove .jar extension      → my---weird___name-1.2.3
 *   2. Remove version at end      → my---weird___name
 *   3. Replace non-alphanum       → my...weird...name
 *   4. Collapse multiple dots     → my.weird.name
 *   5. Remove leading/trailing    → my.weird.name (no change)
 *
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * 3. UNNAMED MODULE
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * A regular JAR placed on the CLASSPATH.
 * Treated as "old code" - a second-class citizen in the module system.
 *
 * CHARACTERISTICS:
 * - NO module-info.java file
 * - Placed on CLASSPATH (not module path)
 * - Does NOT export any packages to named or automatic modules
 * - CAN READ from any JARs on classpath or module path
 * - Used for legacy/unmigrated code
 *
 *
 * ============================================================================
 * MODULE TYPES COMPARISON TABLE
 * ============================================================================
 *
 * ┌────────────────────────┬──────────────┬──────────────┬──────────────────┐
 * │ QUESTION               │ NAMED        │ AUTOMATIC    │ UNNAMED          │
 * │                        │ MODULE       │ MODULE       │ MODULE           │
 * ├────────────────────────┼──────────────┼──────────────┼──────────────────┤
 * │ Contains               │ YES          │ NO           │ NO               │
 * │ module-info.java?      │              │              │                  │
 * ├────────────────────────┼──────────────┼──────────────┼──────────────────┤
 * │ Where does it          │ Module path  │ Module path  │ Classpath        │
 * │ appear?                │              │              │                  │
 * ├────────────────────────┼──────────────┼──────────────┼──────────────────┤
 * │ Which packages         │ Only those   │ ALL packages │ NO packages      │
 * │ exported to other      │ in exports   │ (automatic)  │ (not visible to  │
 * │ modules?               │ directive    │              │ named/automatic) │
 * ├────────────────────────┼──────────────┼──────────────┼──────────────────┤
 * │ Readable by other      │ YES          │ YES          │ NO               │
 * │ modules on module      │ (if          │              │ (cannot be       │
 * │ path?                  │ exported)    │              │ required)        │
 * ├────────────────────────┼──────────────┼──────────────┼──────────────────┤
 * │ Readable by JARs       │ YES          │ YES          │ YES              │
 * │ on classpath?          │              │              │                  │
 * └────────────────────────┴──────────────┴──────────────┴──────────────────┘
 *
 * KEY POINTS:
 * - Unnamed modules CANNOT be read by named/automatic modules!
 * - Automatic modules export EVERYTHING automatically
 * - Named modules have full control via module-info.java
 *
 *
 * ============================================================================
 * MIGRATION STRATEGIES
 * ============================================================================
 *
 * When migrating a legacy application to modules, you have two strategies:
 *
 * 1. BOTTOM-UP MIGRATION - Start with lowest-level dependencies
 * 2. TOP-DOWN MIGRATION - Start with highest-level application
 *
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * UNDERSTANDING DEPENDENCY ORDER
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * Projects have dependencies. The "lowest level" project is one that has
 * NO dependencies on other projects (a utility library).
 * The "highest level" project is one that DEPENDS ON all others (your app).
 *
 * EXAMPLE DEPENDENCY GRAPH:
 *
 *   my-app              <- HIGHEST LEVEL (depends on everything)
 *     |
 *     +-- my-service    <- MIDDLE LEVEL
 *     |     |
 *     |     +-- my-utils    <- LOWEST LEVEL (no dependencies)
 *     |
 *     +-- my-web
 *           |
 *           +-- my-utils    <- LOWEST LEVEL (no dependencies)
 *
 * ORDER (lowest to highest): my-utils → my-service → my-web → my-app
 * ORDER (highest to lowest): my-app → my-service/my-web → my-utils
 *
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * BOTTOM-UP MIGRATION STRATEGY
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * Start with projects that have NO dependencies, work your way UP.
 * Best when you have control over all JAR files.
 *
 * STEPS:
 *
 * 1. PICK THE LOWEST-LEVEL PROJECT that has not yet been migrated
 *    (The one with NO dependencies on other projects)
 *
 * 2. ADD module-info.java to that project
 *    - Add exports for packages used by higher-level JARs
 *    - Add requires for any modules this depends on
 *
 * 3. MOVE the newly migrated named module from CLASSPATH to MODULE PATH
 *
 * 4. ENSURE unmigrated projects STAY as unnamed modules on CLASSPATH
 *
 * 5. REPEAT with the next lowest-level project until done
 *
 *
 * EXAMPLE (Bottom-Up):
 *
 *   BEFORE: All on classpath (unnamed modules)
 *   ─────────────────────────────────────────
 *   classpath: my-app.jar, my-service.jar, my-web.jar, my-utils.jar
 *
 *   STEP 1: Migrate my-utils (no dependencies)
 *   ─────────────────────────────────────────
 *   module path: my-utils.jar (now named module)
 *   classpath:   my-app.jar, my-service.jar, my-web.jar
 *
 *   STEP 2: Migrate my-service and my-web (depend only on my-utils)
 *   ─────────────────────────────────────────
 *   module path: my-utils.jar, my-service.jar, my-web.jar
 *   classpath:   my-app.jar
 *
 *   STEP 3: Migrate my-app (depends on everything)
 *   ─────────────────────────────────────────
 *   module path: my-utils.jar, my-service.jar, my-web.jar, my-app.jar
 *   classpath:   (empty)
 *
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * TOP-DOWN MIGRATION STRATEGY
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * Start with the HIGHEST-LEVEL project (your application), work DOWN.
 * Useful when you DON'T have control over every JAR file.
 *
 * STEPS:
 *
 * 1. PLACE ALL PROJECTS on the MODULE PATH
 *    (They become automatic modules)
 *
 * 2. PICK THE HIGHEST-LEVEL PROJECT that has not yet been migrated
 *
 * 3. ADD module-info.java to convert automatic module into named module
 *    - Add exports and requires directives
 *    - Use AUTOMATIC MODULE NAMES in requires (most aren't named yet!)
 *
 * 4. REPEAT with the next highest-level project until done
 *
 *
 * EXAMPLE (Top-Down):
 *
 *   STEP 1: Place all on module path (automatic modules)
 *   ─────────────────────────────────────────
 *   module path: my-app.jar*, my-service.jar*, my-web.jar*, my-utils.jar*
 *   (* = automatic module)
 *
 *   STEP 2: Migrate my-app (add module-info.java)
 *   ─────────────────────────────────────────
 *   module path: my-app.jar, my-service.jar*, my-web.jar*, my-utils.jar*
 *   (my-app now named, others still automatic)
 *
 *   my-app module-info.java:
 *     module my.app {
 *         requires my.service;  // automatic module name!
 *         requires my.web;      // automatic module name!
 *     }
 *
 *   STEP 3: Migrate my-service, my-web
 *   ─────────────────────────────────────────
 *   module path: my-app.jar, my-service.jar, my-web.jar, my-utils.jar*
 *
 *   STEP 4: Migrate my-utils
 *   ─────────────────────────────────────────
 *   module path: my-app.jar, my-service.jar, my-web.jar, my-utils.jar
 *   (All now named modules)
 *
 *
 * ============================================================================
 * MIGRATION STRATEGY COMPARISON TABLE
 * ============================================================================
 *
 * ┌────────────────────────────────┬─────────────────────┬────────────────────┐
 * │ PROJECT TYPE                   │ BOTTOM-UP           │ TOP-DOWN           │
 * ├────────────────────────────────┼─────────────────────┼────────────────────┤
 * │ Project that DEPENDS ON        │ Unnamed module      │ Named module       │
 * │ all others (my-app)            │ on CLASSPATH        │ on MODULE PATH     │
 * │ (migrated LAST in bottom-up,   │ (migrated last)     │ (migrated first)   │
 * │ migrated FIRST in top-down)    │                     │                    │
 * ├────────────────────────────────┼─────────────────────┼────────────────────┤
 * │ Project with NO dependencies   │ Named module        │ Automatic module   │
 * │ (my-utils)                     │ on MODULE PATH      │ on MODULE PATH     │
 * │ (migrated FIRST in bottom-up,  │ (migrated first)    │ (migrated last)    │
 * │ migrated LAST in top-down)     │                     │                    │
 * └────────────────────────────────┴─────────────────────┴────────────────────┘
 *
 *
 * WHEN TO USE EACH:
 *
 * BOTTOM-UP:
 * - You control ALL the JARs
 * - Libraries can be properly modularized first
 * - More "correct" approach
 *
 * TOP-DOWN:
 * - You DON'T control all JARs (third-party libraries)
 * - Need to migrate quickly
 * - Can use automatic module names for dependencies
 *
 *
 * ============================================================================
 * EXAM SUMMARY
 * ============================================================================
 *
 * NAMED MODULE:
 * - Has module-info.java
 * - On module path
 * - Exports only declared packages
 *
 * AUTOMATIC MODULE:
 * - NO module-info.java
 * - On module path
 * - Exports ALL packages
 * - Name from Automatic-Module-Name or filename
 *
 * UNNAMED MODULE:
 * - NO module-info.java
 * - On classpath
 * - Exports NOTHING to named/automatic modules
 * - Can read everything (second-class citizen)
 *
 * BOTTOM-UP:
 * - Migrate lowest-level first (no dependencies)
 * - Unmigrated stay on classpath as unnamed modules
 * - Use when you control all JARs
 *
 * TOP-DOWN:
 * - Put everything on module path (automatic modules)
 * - Migrate highest-level first
 * - Use automatic module names in requires
 * - Use when you don't control all JARs
 *
 *
 * AUTOMATIC MODULE NAME ALGORITHM:
 * 1. Check Automatic-Module-Name in MANIFEST.MF
 * 2. Otherwise: remove .jar → remove version → replace non-alphanum with dots
 *    → collapse dots → remove leading/trailing dots
 */
public class ModuleTypesAndMigration {
    // This is a documentation class - see comments above for reference
}
