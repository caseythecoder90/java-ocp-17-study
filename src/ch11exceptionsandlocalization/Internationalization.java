package ch11exceptionsandlocalization;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * INTERNATIONALIZATION AND LOCALIZATION
 * =====================================
 *
 * DEFINITIONS:
 * ------------
 * Internationalization (i18n):
 *   The process of designing your program so it can be adapted to different
 *   languages and regions WITHOUT code changes. It's about making your app
 *   "world-ready" by externalizing text and using locale-aware APIs.
 *   (i18n = i + 18 letters + n)
 *
 * Localization (l10n):
 *   The process of adapting your internationalized program for a SPECIFIC
 *   locale by adding locale-specific resources (translations, formats, etc.).
 *   (l10n = l + 10 letters + n)
 *
 *
 * ============================================================================
 * LOCALE CLASS
 * ============================================================================
 *
 * FORMAT:
 * -------
 *   language_COUNTRY
 *   - language: lowercase two-letter code (required)
 *   - COUNTRY: UPPERCASE two-letter code (optional)
 *
 *   Examples:
 *     en       - English (no country)
 *     en_US    - English, United States
 *     en_GB    - English, Great Britain
 *     fr       - French (no country)
 *     fr_FR    - French, France
 *     fr_CA    - French, Canada
 *
 *
 * THREE WAYS TO CREATE A LOCALE:
 * ------------------------------
 *
 * 1. BUILT-IN CONSTANTS:
 *    Locale locale = Locale.US;
 *    Locale locale = Locale.FRENCH;
 *    Locale locale = Locale.GERMANY;
 *
 * 2. CONSTRUCTORS:
 *    Locale locale = new Locale("en");           // Language only
 *    Locale locale = new Locale("en", "US");     // Language + Country
 *
 * 3. BUILDER PATTERN:
 *    Locale locale = new Locale.Builder()
 *        .setLanguage("en")
 *        .setRegion("US")
 *        .build();
 *
 *    NOTE: Builder requires 'new' keyword unlike typical builder patterns!
 *          new Locale.Builder().setX().setY().build()
 */
public class Internationalization {

    public static void main(String[] args) throws ParseException {
        demonstrateLocaleCreation();
        demonstrateNumberFormat();
        demonstrateCurrencyFormat();
        demonstratePercentFormat();
        demonstrateCompactNumberFormat();
        demonstrateParseMethod();
    }

    // ========================================================================
    // LOCALE CREATION
    // ========================================================================

    public static void demonstrateLocaleCreation() {
        System.out.println("=== LOCALE CREATION ===\n");

        // Method 1: Built-in constants
        System.out.println("1. Built-in Constants:");
        Locale us = Locale.US;
        Locale french = Locale.FRENCH;       // Language only
        Locale germany = Locale.GERMANY;     // Country (includes language)
        Locale canada = Locale.CANADA;
        Locale canadaFrench = Locale.CANADA_FRENCH;

        System.out.println("   Locale.US:            " + us);
        System.out.println("   Locale.FRENCH:        " + french);
        System.out.println("   Locale.GERMANY:       " + germany);
        System.out.println("   Locale.CANADA:        " + canada);
        System.out.println("   Locale.CANADA_FRENCH: " + canadaFrench);

        System.out.println();

        // Method 2: Constructors
        System.out.println("2. Constructors:");
        Locale langOnly = new Locale("es");           // Spanish, no country
        Locale langAndCountry = new Locale("es", "MX"); // Spanish, Mexico

        System.out.println("   new Locale(\"es\"):        " + langOnly);
        System.out.println("   new Locale(\"es\", \"MX\"): " + langAndCountry);

        System.out.println();

        // Method 3: Builder pattern (note: requires 'new' keyword!)
        System.out.println("3. Builder Pattern:");
        Locale builderLocale = new Locale.Builder()
                .setLanguage("de")
                .setRegion("AT")    // Austria
                .build();

        System.out.println("   new Locale.Builder()");
        System.out.println("       .setLanguage(\"de\")");
        System.out.println("       .setRegion(\"AT\")");
        System.out.println("       .build() = " + builderLocale);

        System.out.println();

        // Get default locale
        System.out.println("Default Locale: " + Locale.getDefault());

        System.out.println();
        System.out.println("LOCALE FORMAT: language_COUNTRY");
        System.out.println("  - language: lowercase (required)");
        System.out.println("  - COUNTRY: UPPERCASE (optional)\n");
    }

    // ========================================================================
    // NUMBER FORMAT
    // ========================================================================

    /**
     * NumberFormat FACTORY METHODS:
     * -----------------------------
     * getInstance()              - General-purpose number formatter
     * getNumberInstance()        - Same as getInstance()
     * getCurrencyInstance()      - Currency formatting
     * getPercentInstance()       - Percentage formatting
     * getIntegerInstance()       - Integer formatting (rounds)
     * getCompactNumberInstance() - Compact format (K, M, B, T)
     *
     * All methods have overloaded versions:
     *   getXxxInstance()         - Uses default locale
     *   getXxxInstance(Locale)   - Uses specified locale
     *
     * KEY METHODS:
     *   format(number) - Number to String
     *   parse(string)  - String to Number (can throw ParseException)
     */
    public static void demonstrateNumberFormat() {
        System.out.println("=== NUMBER FORMAT ===\n");

        double number = 1234567.89;

        // getInstance() / getNumberInstance() - general purpose
        System.out.println("getNumberInstance() - formats numbers with locale separators:");
        System.out.println("  US:      " + NumberFormat.getNumberInstance(Locale.US).format(number));
        System.out.println("  Germany: " + NumberFormat.getNumberInstance(Locale.GERMANY).format(number));
        System.out.println("  France:  " + NumberFormat.getNumberInstance(Locale.FRANCE).format(number));

        System.out.println();

        // getIntegerInstance() - rounds to integer
        System.out.println("getIntegerInstance() - rounds to nearest integer:");
        System.out.println("  US: " + NumberFormat.getIntegerInstance(Locale.US).format(number));

        System.out.println();
    }

    // ========================================================================
    // CURRENCY FORMAT
    // ========================================================================

    public static void demonstrateCurrencyFormat() {
        System.out.println("=== CURRENCY FORMAT ===\n");

        double amount = 1234.56;

        System.out.println("getCurrencyInstance() - formats with currency symbol:");
        System.out.println("  US:      " + NumberFormat.getCurrencyInstance(Locale.US).format(amount));
        System.out.println("  UK:      " + NumberFormat.getCurrencyInstance(Locale.UK).format(amount));
        System.out.println("  Germany: " + NumberFormat.getCurrencyInstance(Locale.GERMANY).format(amount));
        System.out.println("  Japan:   " + NumberFormat.getCurrencyInstance(Locale.JAPAN).format(amount));
        System.out.println("  France:  " + NumberFormat.getCurrencyInstance(Locale.FRANCE).format(amount));

        System.out.println();
    }

    // ========================================================================
    // PERCENT FORMAT
    // ========================================================================

    public static void demonstratePercentFormat() {
        System.out.println("=== PERCENT FORMAT ===\n");

        double decimal = 0.75;

        System.out.println("getPercentInstance() - formats as percentage:");
        System.out.println("  Value: " + decimal);
        System.out.println("  US:      " + NumberFormat.getPercentInstance(Locale.US).format(decimal));
        System.out.println("  Germany: " + NumberFormat.getPercentInstance(Locale.GERMANY).format(decimal));

        System.out.println();
    }

    // ========================================================================
    // COMPACT NUMBER FORMAT (New to Java 17 - expect exam question!)
    // ========================================================================

    /**
     * COMPACT NUMBER FORMAT
     * ---------------------
     * Designed for LIMITED PRINT SPACE (UI labels, charts, etc.)
     * Locale-specific output that varies by location.
     *
     * NumberFormat.Style enum:
     *   SHORT - Uses symbols (K, M, B, T)     - DEFAULT
     *   LONG  - Uses words (thousand, million, billion, trillion)
     *
     * FORMATTING RULES:
     * 1. Determines highest range (thousand, million, billion, trillion)
     * 2. Returns up to FIRST 3 DIGITS, rounding last digit as needed
     * 3. Prints identifier:
     *    - SHORT: symbol (K, M, B, T)
     *    - LONG: space + word (thousand, million, etc.)
     *
     * EXAMPLES (US Locale, SHORT):
     *   1,000      -> 1K
     *   12,345     -> 12K
     *   123,456    -> 123K
     *   1,234,567  -> 1M
     *   7,654,321  -> 8M (rounds!)
     */
    public static void demonstrateCompactNumberFormat() {
        System.out.println("=== COMPACT NUMBER FORMAT (Java 17) ===\n");

        // Create formatters with SHORT and LONG styles
        NumberFormat shortFormat = NumberFormat.getCompactNumberInstance(
                Locale.US, NumberFormat.Style.SHORT);
        NumberFormat longFormat = NumberFormat.getCompactNumberInstance(
                Locale.US, NumberFormat.Style.LONG);

        // Test values
        long[] values = {999, 1_000, 12_345, 123_456, 999_999,
                         1_234_567, 7_654_321, 1_000_000_000L, 1_500_000_000_000L};

        System.out.println("Style.SHORT vs Style.LONG (US Locale):");
        System.out.println("  Number          | SHORT    | LONG");
        System.out.println("  ----------------|----------|------------------");
        for (long value : values) {
            System.out.printf("  %,15d | %-8s | %s%n",
                    value, shortFormat.format(value), longFormat.format(value));
        }

        System.out.println();

        // Demonstrate 3-digit rule with rounding
        System.out.println("3-DIGIT RULE with rounding:");
        System.out.println("  1,234,567 -> " + shortFormat.format(1_234_567) + " (first 3 digits: 1.23, rounds to 1M)");
        System.out.println("  7,654,321 -> " + shortFormat.format(7_654_321) + " (first 3 digits: 7.65, rounds to 8M)");
        System.out.println("  1,567,890 -> " + shortFormat.format(1_567_890) + " (first 3 digits: 1.57, rounds to 2M)");

        System.out.println();

        // Locale-specific output
        System.out.println("Locale-specific output (1,000,000):");
        long million = 1_000_000;
        System.out.println("  US (SHORT):      " +
                NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT).format(million));
        System.out.println("  US (LONG):       " +
                NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.LONG).format(million));
        System.out.println("  Germany (SHORT): " +
                NumberFormat.getCompactNumberInstance(Locale.GERMANY, NumberFormat.Style.SHORT).format(million));
        System.out.println("  Germany (LONG):  " +
                NumberFormat.getCompactNumberInstance(Locale.GERMANY, NumberFormat.Style.LONG).format(million));

        System.out.println();
        System.out.println("EXAM TIP: CompactNumberFormat is new to Java 17 - expect a question!");
        System.out.println("  - Style.SHORT = symbol (K, M, B, T) - DEFAULT");
        System.out.println("  - Style.LONG = space + word");
        System.out.println("  - Returns up to 3 digits, rounds as needed\n");
    }

    // ========================================================================
    // PARSE METHOD
    // ========================================================================

    /**
     * parse() method:
     * - Converts String to Number
     * - STRIPS OUT characters (currency symbols, percent signs, etc.)
     * - Can throw ParseException (checked!)
     */
    public static void demonstrateParseMethod() throws ParseException {
        System.out.println("=== PARSE METHOD ===\n");

        // format() - Number to String
        System.out.println("format() - Number to String:");
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        String formatted = nf.format(1234.56);
        System.out.println("  1234.56 -> \"" + formatted + "\"");

        // parse() - String to Number (strips characters!)
        System.out.println("\nparse() - String to Number (strips characters):");

        // Parse regular number
        Number parsed = nf.parse("1,234.56");
        System.out.println("  \"1,234.56\" -> " + parsed);

        // Parse currency - strips currency symbol!
        NumberFormat cf = NumberFormat.getCurrencyInstance(Locale.US);
        Number parsedCurrency = cf.parse("$1,234.56");
        System.out.println("  \"$1,234.56\" -> " + parsedCurrency + " (currency symbol stripped)");

        // Parse percentage - strips percent and divides!
        NumberFormat pf = NumberFormat.getPercentInstance(Locale.US);
        Number parsedPercent = pf.parse("75%");
        System.out.println("  \"75%\" -> " + parsedPercent + " (percent stripped, value divided by 100)");

        // Parse compact number
        NumberFormat compact = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        Number parsedCompact = compact.parse("5M");
        System.out.println("  \"5M\" -> " + parsedCompact + " (compact symbol stripped, value expanded)");

        System.out.println();
        System.out.println("parse() rules:");
        System.out.println("  - Strips formatting characters ($ , % K M B T)");
        System.out.println("  - Returns Number object");
        System.out.println("  - Throws ParseException (checked!)\n");
    }

    // ========================================================================
    // EXAM SUMMARY
    // ========================================================================

    /**
     * EXAM SUMMARY:
     * =============
     *
     * LOCALE:
     *   Format: language_COUNTRY (language lowercase, COUNTRY uppercase)
     *   Country is optional
     *
     *   Three ways to create:
     *   1. Constants: Locale.US, Locale.FRENCH, etc.
     *   2. Constructor: new Locale("en") or new Locale("en", "US")
     *   3. Builder: new Locale.Builder().setLanguage("en").setRegion("US").build()
     *      NOTE: Builder requires 'new' keyword!
     *
     * NUMBERFORMAT FACTORY METHODS:
     *   getInstance() / getNumberInstance() - General numbers
     *   getCurrencyInstance()               - Currency ($, €, etc.)
     *   getPercentInstance()                - Percentages (%)
     *   getIntegerInstance()                - Integers (rounds)
     *   getCompactNumberInstance()          - Compact (K, M, B, T)
     *
     *   All have overloads: getXxxInstance() and getXxxInstance(Locale)
     *
     * COMPACT NUMBER FORMAT (Java 17):
     *   - Style.SHORT = symbols (K, M, B, T) - DEFAULT
     *   - Style.LONG = words (thousand, million, etc.)
     *   - Up to 3 digits, rounds last digit
     *   - Locale-specific output
     *
     * KEY METHODS:
     *   format(number) - Number -> String
     *   parse(string)  - String -> Number (throws ParseException!)
     *   parse() strips formatting characters
     */
}
