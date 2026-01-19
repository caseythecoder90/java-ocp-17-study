package ch11exceptionsandlocalization;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * RESOURCE BUNDLES - WORKING EXAMPLES
 * ====================================
 *
 * This file uses actual .properties files in the same package:
 *   Zoo.properties        - Default (fallback)
 *   Zoo_en.properties     - English
 *   Zoo_en_US.properties  - English, United States
 *   Zoo_en_GB.properties  - English, Great Britain
 *   Zoo_fr.properties     - French
 *   Zoo_fr_FR.properties  - French, France
 *
 *
 * FILE NAMING: bundleName_language_COUNTRY.properties
 *
 * HIERARCHY (most specific to least):
 *   Zoo_fr_FR -> Zoo_fr -> Zoo
 *   Zoo_en_US -> Zoo_en -> Zoo
 *
 *
 * TWO PROCESSES:
 * 1. BUNDLE SELECTION - Which bundle to use as starting point
 * 2. VALUE SELECTION - Which bundle(s) to get each key from
 *
 * BUNDLE SELECTION ORDER (requested fr_FR, default en_US):
 *   1. Zoo_fr_FR  (requested: lang + country)
 *   2. Zoo_fr     (requested: lang only)
 *   3. Zoo_en_US  (default: lang + country)
 *   4. Zoo_en     (default: lang only)
 *   5. Zoo        (default bundle)
 *
 * VALUE SELECTION:
 *   Once bundle selected, values come from that hierarchy ONLY.
 *   If Zoo_fr found, en hierarchy is ABANDONED for values!
 */
public class ResourceBundles {

    // Base name for our resource bundle (matches Zoo*.properties files)
    private static final String BUNDLE_NAME = "ch11exceptionsandlocalization.Zoo";

    public static void main(String[] args) {
        demonstrateBasicUsage();
        demonstrateKeySetAndAllKeys();
        demonstrateBundleHierarchy();
        demonstrateValueInheritance();
        demonstrateMessageFormat();
        practiceQuestions();
    }

    // ========================================================================
    // BASIC USAGE
    // ========================================================================

    public static void demonstrateBasicUsage() {
        System.out.println("=== BASIC RESOURCEBUNDLE USAGE ===\n");

        // Get bundle for default locale
        ResourceBundle defaultBundle = ResourceBundle.getBundle(BUNDLE_NAME);
        System.out.println("Default locale: " + Locale.getDefault());
        System.out.println("Bundle for default locale:");
        System.out.println("  name = " + defaultBundle.getString("name"));
        System.out.println("  greeting = " + defaultBundle.getString("greeting"));

        System.out.println();

        // Get bundle for specific locale
        ResourceBundle frenchBundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.FRENCH);
        System.out.println("Bundle for Locale.FRENCH (fr):");
        System.out.println("  name = " + frenchBundle.getString("name"));
        System.out.println("  greeting = " + frenchBundle.getString("greeting"));

        System.out.println();

        // Get bundle for French France
        ResourceBundle frFRBundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.FRANCE);
        System.out.println("Bundle for Locale.FRANCE (fr_FR):");
        System.out.println("  name = " + frFRBundle.getString("name"));
        System.out.println("  greeting = " + frFRBundle.getString("greeting"));

        System.out.println();
    }

    // ========================================================================
    // KEYSET - GET ALL AVAILABLE KEYS
    // ========================================================================

    public static void demonstrateKeySetAndAllKeys() {
        System.out.println("=== KEYSET METHOD ===\n");

        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.US);

        System.out.println("All keys available for en_US bundle:");
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            System.out.println("  " + key + " = " + bundle.getString(key));
        }

        System.out.println();
        System.out.println("Note: keySet() returns keys from the ENTIRE hierarchy,");
        System.out.println("not just the selected bundle file.\n");
    }

    // ========================================================================
    // BUNDLE HIERARCHY DEMONSTRATION
    // ========================================================================

    public static void demonstrateBundleHierarchy() {
        System.out.println("=== BUNDLE HIERARCHY ===\n");

        // Show what's in each properties file
        System.out.println("Contents of our .properties files:");
        System.out.println();
        System.out.println("Zoo.properties (default):");
        System.out.println("  name=Default Zoo");
        System.out.println("  greeting=Welcome");
        System.out.println("  open=The zoo is open");
        System.out.println("  close=The zoo is closed");
        System.out.println("  animal=animal");
        System.out.println();
        System.out.println("Zoo_en.properties:");
        System.out.println("  name=English Zoo");
        System.out.println("  greeting=Hello");
        System.out.println("  open=We are open");
        System.out.println("  animal=animal");
        System.out.println("  favorite=Our favorite animal is the lion");
        System.out.println();
        System.out.println("Zoo_en_US.properties:");
        System.out.println("  name=American Zoo");
        System.out.println("  greeting=Hey there");
        System.out.println();
        System.out.println("Zoo_fr.properties:");
        System.out.println("  name=Zoo Francais");
        System.out.println("  greeting=Bonjour");
        System.out.println("  open=Nous sommes ouverts");
        System.out.println();
        System.out.println("Zoo_fr_FR.properties:");
        System.out.println("  greeting=Salut");
        System.out.println();
    }

    // ========================================================================
    // VALUE INHERITANCE - THE TRICKY PART
    // ========================================================================

    public static void demonstrateValueInheritance() {
        System.out.println("=== VALUE INHERITANCE (TRICKY!) ===\n");

        // en_US locale
        System.out.println("REQUEST: en_US");
        System.out.println("HIERARCHY: Zoo_en_US -> Zoo_en -> Zoo");
        ResourceBundle enUS = ResourceBundle.getBundle(BUNDLE_NAME, Locale.US);
        System.out.println("  name     = " + enUS.getString("name") + "  <- from Zoo_en_US");
        System.out.println("  greeting = " + enUS.getString("greeting") + "  <- from Zoo_en_US");
        System.out.println("  open     = " + enUS.getString("open") + "  <- from Zoo_en (inherited)");
        System.out.println("  close    = " + enUS.getString("close") + "  <- from Zoo (inherited)");
        System.out.println("  favorite = " + enUS.getString("favorite") + "  <- from Zoo_en");
        System.out.println();

        // en_GB locale
        System.out.println("REQUEST: en_GB");
        System.out.println("HIERARCHY: Zoo_en_GB -> Zoo_en -> Zoo");
        ResourceBundle enGB = ResourceBundle.getBundle(BUNDLE_NAME, new Locale("en", "GB"));
        System.out.println("  name     = " + enGB.getString("name") + "  <- from Zoo_en_GB");
        System.out.println("  greeting = " + enGB.getString("greeting") + "  <- from Zoo_en_GB");
        System.out.println("  open     = " + enGB.getString("open") + "  <- from Zoo_en (inherited)");
        System.out.println("  close    = " + enGB.getString("close") + "  <- from Zoo (inherited)");
        System.out.println();

        // fr_FR locale - THIS IS THE TRICKY ONE
        System.out.println("REQUEST: fr_FR");
        System.out.println("HIERARCHY: Zoo_fr_FR -> Zoo_fr -> Zoo");
        System.out.println("NOTE: Zoo_en is NOT in this hierarchy!");
        ResourceBundle frFR = ResourceBundle.getBundle(BUNDLE_NAME, Locale.FRANCE);
        System.out.println("  name     = " + frFR.getString("name") + "  <- from Zoo_fr (inherited)");
        System.out.println("  greeting = " + frFR.getString("greeting") + "  <- from Zoo_fr_FR");
        System.out.println("  open     = " + frFR.getString("open") + "  <- from Zoo_fr");
        System.out.println("  close    = " + frFR.getString("close") + "  <- from Zoo (inherited)");

        // Try to get "favorite" - it's only in Zoo_en!
        try {
            frFR.getString("favorite");
        } catch (MissingResourceException e) {
            System.out.println("  favorite = MissingResourceException! (only in Zoo_en, not in fr hierarchy)");
        }
        System.out.println();

        // fr locale (no country)
        System.out.println("REQUEST: fr (no country)");
        System.out.println("HIERARCHY: Zoo_fr -> Zoo");
        ResourceBundle fr = ResourceBundle.getBundle(BUNDLE_NAME, Locale.FRENCH);
        System.out.println("  name     = " + fr.getString("name") + "  <- from Zoo_fr");
        System.out.println("  greeting = " + fr.getString("greeting") + "  <- from Zoo_fr");
        System.out.println("  close    = " + fr.getString("close") + "  <- from Zoo (inherited)");
        System.out.println();

        // Spanish locale - no Zoo_es exists, falls back
        System.out.println("REQUEST: es_MX (no Spanish bundle exists!)");
        Locale spanishMexico = new Locale("es", "MX");
        ResourceBundle spanish = ResourceBundle.getBundle(BUNDLE_NAME, spanishMexico);
        System.out.println("  Bundle selected based on default locale hierarchy");
        System.out.println("  name     = " + spanish.getString("name"));
        System.out.println("  greeting = " + spanish.getString("greeting"));
        System.out.println();
    }

    // ========================================================================
    // MESSAGE FORMAT WITH RESOURCE BUNDLES
    // ========================================================================

    public static void demonstrateMessageFormat() {
        System.out.println("=== MESSAGE FORMAT ===\n");

        ResourceBundle enUS = ResourceBundle.getBundle(BUNDLE_NAME, Locale.US);
        ResourceBundle frFR = ResourceBundle.getBundle(BUNDLE_NAME, Locale.FRANCE);

        // Get pattern and format with values
        String ticketPatternUS = enUS.getString("ticket.price");
        String ticketPatternFR = frFR.getString("ticket.price");

        System.out.println("ticket.price patterns:");
        System.out.println("  en_US: " + ticketPatternUS);
        System.out.println("  fr_FR: " + ticketPatternFR);
        System.out.println();

        String resultUS = MessageFormat.format(ticketPatternUS, 25);
        String resultFR = MessageFormat.format(ticketPatternFR, 20);

        System.out.println("After MessageFormat.format() with price:");
        System.out.println("  en_US: " + resultUS);
        System.out.println("  fr_FR: " + resultFR);
        System.out.println();

        // visitor.count example
        String visitorPattern = enUS.getString("visitor.count");
        System.out.println("visitor.count = " + visitorPattern);
        System.out.println("  Formatted: " + MessageFormat.format(visitorPattern, 1500));
        System.out.println();
    }

    // ========================================================================
    // PRACTICE QUESTIONS
    // ========================================================================

    public static void practiceQuestions() {
        System.out.println("=== PRACTICE QUESTIONS ===\n");

        // Question 1
        System.out.println("QUESTION 1:");
        System.out.println("Given these bundles exist: Zoo.properties, Zoo_en.properties, Zoo_fr.properties");
        System.out.println("Default locale is en_US. What does this code print?");
        System.out.println();
        System.out.println("  Locale locale = new Locale(\"fr\");");
        System.out.println("  ResourceBundle rb = ResourceBundle.getBundle(\"Zoo\", locale);");
        System.out.println("  System.out.println(rb.getString(\"name\"));");
        System.out.println();

        ResourceBundle q1 = ResourceBundle.getBundle(BUNDLE_NAME, new Locale("fr"));
        System.out.println("ANSWER: " + q1.getString("name"));
        System.out.println("WHY: Zoo_fr is selected. 'name' is in Zoo_fr, so we get 'Zoo Francais'");
        System.out.println();

        // Question 2
        System.out.println("─".repeat(60));
        System.out.println("\nQUESTION 2:");
        System.out.println("Same bundles. What does this print?");
        System.out.println();
        System.out.println("  Locale locale = new Locale(\"fr\");");
        System.out.println("  ResourceBundle rb = ResourceBundle.getBundle(\"Zoo\", locale);");
        System.out.println("  System.out.println(rb.getString(\"close\"));");
        System.out.println();

        System.out.println("ANSWER: " + q1.getString("close"));
        System.out.println("WHY: Zoo_fr is selected, but 'close' is NOT in Zoo_fr.");
        System.out.println("     Java looks up hierarchy: Zoo_fr -> Zoo");
        System.out.println("     Found in Zoo.properties!");
        System.out.println();

        // Question 3
        System.out.println("─".repeat(60));
        System.out.println("\nQUESTION 3:");
        System.out.println("Same bundles. 'favorite' key only exists in Zoo_en.properties.");
        System.out.println("What happens with this code?");
        System.out.println();
        System.out.println("  Locale locale = new Locale(\"fr\");");
        System.out.println("  ResourceBundle rb = ResourceBundle.getBundle(\"Zoo\", locale);");
        System.out.println("  System.out.println(rb.getString(\"favorite\"));");
        System.out.println();

        try {
            q1.getString("favorite");
            System.out.println("ANSWER: Found it");
        } catch (MissingResourceException e) {
            System.out.println("ANSWER: MissingResourceException!");
        }
        System.out.println("WHY: Zoo_fr is selected. Hierarchy is Zoo_fr -> Zoo.");
        System.out.println("     'favorite' is ONLY in Zoo_en, which is NOT in this hierarchy!");
        System.out.println("     Even though en is the default locale, once fr is found,");
        System.out.println("     the en hierarchy is ABANDONED for value lookups.");
        System.out.println();

        // Question 4
        System.out.println("─".repeat(60));
        System.out.println("\nQUESTION 4:");
        System.out.println("Request locale fr_FR. These bundles exist:");
        System.out.println("  Zoo.properties, Zoo_fr.properties, Zoo_fr_FR.properties");
        System.out.println("Zoo_fr_FR has: greeting=Salut");
        System.out.println("Zoo_fr has: name=Zoo Francais, greeting=Bonjour");
        System.out.println("What is rb.getString(\"greeting\")?");
        System.out.println();

        ResourceBundle q4 = ResourceBundle.getBundle(BUNDLE_NAME, Locale.FRANCE);
        System.out.println("ANSWER: " + q4.getString("greeting"));
        System.out.println("WHY: Zoo_fr_FR is selected (most specific match).");
        System.out.println("     'greeting' IS in Zoo_fr_FR, so we get 'Salut' not 'Bonjour'.");
        System.out.println();

        // Question 5
        System.out.println("─".repeat(60));
        System.out.println("\nQUESTION 5:");
        System.out.println("Same as Q4, but what is rb.getString(\"name\")?");
        System.out.println();

        System.out.println("ANSWER: " + q4.getString("name"));
        System.out.println("WHY: Zoo_fr_FR is selected, but 'name' is NOT in Zoo_fr_FR.");
        System.out.println("     Look up hierarchy: Zoo_fr_FR -> Zoo_fr -> Zoo");
        System.out.println("     Found in Zoo_fr!");
        System.out.println();

        // Question 6
        System.out.println("─".repeat(60));
        System.out.println("\nQUESTION 6:");
        System.out.println("Locale requested: de_DE (German). No German bundles exist.");
        System.out.println("Default locale: en_US. What bundle is selected?");
        System.out.println();
        System.out.println("Search order:");
        System.out.println("  1. Zoo_de_DE - not found");
        System.out.println("  2. Zoo_de - not found");
        System.out.println("  3. Zoo_en_US - FOUND!");
        System.out.println();

        ResourceBundle q6 = ResourceBundle.getBundle(BUNDLE_NAME, Locale.GERMANY);
        System.out.println("ANSWER: name = " + q6.getString("name"));
        System.out.println("WHY: No German bundles, so falls back to default locale (en_US).");
        System.out.println();

        // Question 7
        System.out.println("─".repeat(60));
        System.out.println("\nQUESTION 7: EXAM TRAP");
        System.out.println("Given only: Zoo.properties, Zoo_fr.properties");
        System.out.println("Request: fr_CA. Default: en_US");
        System.out.println("What bundle is selected and why?");
        System.out.println();
        System.out.println("Search order:");
        System.out.println("  1. Zoo_fr_CA - not found");
        System.out.println("  2. Zoo_fr - FOUND!");
        System.out.println();
        System.out.println("ANSWER: Zoo_fr is selected.");
        System.out.println("WHY: Even though fr_CA doesn't exist, Zoo_fr matches the language.");
        System.out.println("     Java doesn't fall back to en_US because a French bundle exists!");
        System.out.println();

        ResourceBundle q7 = ResourceBundle.getBundle(BUNDLE_NAME, new Locale("fr", "CA"));
        System.out.println("Proof - name = " + q7.getString("name") + " (from Zoo_fr hierarchy)");
        System.out.println();
    }

    // ========================================================================
    // PROPERTIES CLASS EXAMPLE
    // ========================================================================

    public static void demonstratePropertiesClass() {
        System.out.println("=== PROPERTIES CLASS ===\n");

        Properties props = new Properties();
        props.setProperty("db.url", "jdbc:mysql://localhost:3306/mydb");
        props.setProperty("db.user", "admin");

        System.out.println("getProperty(\"db.url\"): " + props.getProperty("db.url"));
        System.out.println("getProperty(\"db.pass\"): " + props.getProperty("db.pass"));
        System.out.println("getProperty(\"db.pass\", \"secret\"): " + props.getProperty("db.pass", "secret"));
        System.out.println();
    }
}
