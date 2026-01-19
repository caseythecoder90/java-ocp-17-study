package ch11exceptionsandlocalization;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * FORMATTING VALUES
 * =================
 *
 * Topics covered:
 * - NumberFormat interface and DecimalFormat
 * - DateTimeFormatter predefined formats
 * - DateTimeFormatter custom formats
 * - Symbol compatibility with date/time types
 *
 *
 * ============================================================================
 * NUMBER FORMATTING - DecimalFormat
 * ============================================================================
 *
 * DecimalFormat implements NumberFormat interface.
 *
 * KEY SYMBOLS:
 * ------------
 *   #  - Omit position if no digit exists for it
 *   0  - Put 0 in position if no digit exists for it
 *
 * EXAMPLES:
 *   Pattern     | Value     | Result
 *   ------------|-----------|--------
 *   ###.##      | 1.2       | 1.2
 *   000.00      | 1.2       | 001.20
 *   #,###.#     | 12345.6   | 12,345.6
 *   $#,###.00   | 1234.5    | $1,234.50
 *
 *
 * ============================================================================
 * DATE/TIME FORMATTING - DateTimeFormatter
 * ============================================================================
 *
 * PREDEFINED FORMATTERS (ISO formats):
 * ------------------------------------
 *   ISO_LOCAL_DATE       - yyyy-MM-dd           (e.g., 2024-01-15)
 *   ISO_LOCAL_TIME       - HH:mm:ss             (e.g., 14:30:45)
 *   ISO_LOCAL_DATE_TIME  - yyyy-MM-ddTHH:mm:ss  (e.g., 2024-01-15T14:30:45)
 *
 *
 * CUSTOM FORMAT SYMBOLS:
 * ----------------------
 *   Symbol | Meaning              | Example
 *   -------|----------------------|---------
 *   y      | Year                 | 2024
 *   M      | Month                | 1 or 01 or Jan or January
 *   d      | Day of month         | 15
 *   h      | Hour (12-hour, 1-12) | 2
 *   H      | Hour (24-hour, 0-23) | 14
 *   m      | Minute               | 30
 *   s      | Second               | 45
 *   S      | Fraction of second   | 123
 *   a      | AM/PM                | PM
 *   z      | Time zone name       | EST, PST
 *   Z      | Time zone offset     | -0500
 *
 *
 * SYMBOL COMPATIBILITY:
 * ---------------------
 *                    | LocalDate | LocalTime | LocalDateTime | ZonedDateTime
 *   -----------------|-----------|-----------|---------------|---------------
 *   y (year)         |    YES    |    NO     |     YES       |     YES
 *   M (month)        |    YES    |    NO     |     YES       |     YES
 *   d (day)          |    YES    |    NO     |     YES       |     YES
 *   h (hour 12)      |    NO     |    YES    |     YES       |     YES
 *   H (hour 24)      |    NO     |    YES    |     YES       |     YES
 *   m (minute)       |    NO     |    YES    |     YES       |     YES
 *   s (second)       |    NO     |    YES    |     YES       |     YES
 *   S (fraction)     |    NO     |    YES    |     YES       |     YES
 *   a (AM/PM)        |    NO     |    YES    |     YES       |     YES
 *   z (zone name)    |    NO     |    NO     |     NO        |     YES
 *   Z (zone offset)  |    NO     |    NO     |     NO        |     YES
 *
 * EXAM TIP: Using incompatible symbol throws DateTimeException at runtime!
 *
 *
 * TWO WAYS TO FORMAT:
 * -------------------
 *   1. dateTime.format(formatter)   - Call format() on the date/time object
 *   2. formatter.format(dateTime)   - Call format() on the formatter
 *
 *   Both produce the same result!
 */
public class FormattingValues {

    public static void main(String[] args) {
        demonstrateDecimalFormat();
        demonstratePredefinedFormatters();
        demonstrateCustomFormatters();
        demonstrateSymbolCompatibility();
        demonstrateEscapingText();
        demonstrateTwoFormatMethods();
    }

    // ========================================================================
    // DECIMAL FORMAT
    // ========================================================================

    public static void demonstrateDecimalFormat() {
        System.out.println("=== DECIMAL FORMAT ===\n");

        double value1 = 1.2;
        double value2 = 12345.678;
        double value3 = 0.5;

        // # symbol - omit position if no digit exists
        System.out.println("# symbol - omits position if no digit:");
        DecimalFormat hashFormat = new DecimalFormat("###.##");
        System.out.println("  Pattern ###.## with 1.2:       " + hashFormat.format(value1));
        System.out.println("  Pattern ###.## with 12345.678: " + hashFormat.format(value2));
        System.out.println("  Pattern ###.## with 0.5:       " + hashFormat.format(value3));

        System.out.println();

        // 0 symbol - put 0 if no digit exists
        System.out.println("0 symbol - puts 0 if no digit:");
        DecimalFormat zeroFormat = new DecimalFormat("000.00");
        System.out.println("  Pattern 000.00 with 1.2:       " + zeroFormat.format(value1));
        System.out.println("  Pattern 000.00 with 12345.678: " + zeroFormat.format(value2));
        System.out.println("  Pattern 000.00 with 0.5:       " + zeroFormat.format(value3));

        System.out.println();

        // Combined examples
        System.out.println("Combined patterns:");
        System.out.println("  $#,###.00 with 1234.5:  " + new DecimalFormat("$#,###.00").format(1234.5));
        System.out.println("  #,###.#   with 12345.6: " + new DecimalFormat("#,###.#").format(12345.6));
        System.out.println("  ##%       with 0.75:    " + new DecimalFormat("##%").format(0.75));

        System.out.println();
        System.out.println("KEY DIFFERENCE:");
        System.out.println("  # = Omit if no digit");
        System.out.println("  0 = Show 0 if no digit\n");
    }

    // ========================================================================
    // PREDEFINED FORMATTERS (ISO)
    // ========================================================================

    public static void demonstratePredefinedFormatters() {
        System.out.println("=== PREDEFINED ISO FORMATTERS ===\n");

        LocalDate date = LocalDate.of(2024, 1, 15);
        LocalTime time = LocalTime.of(14, 30, 45);
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        // ISO_LOCAL_DATE - for LocalDate
        System.out.println("ISO_LOCAL_DATE (yyyy-MM-dd):");
        System.out.println("  " + date.format(DateTimeFormatter.ISO_LOCAL_DATE));

        // ISO_LOCAL_TIME - for LocalTime
        System.out.println("\nISO_LOCAL_TIME (HH:mm:ss):");
        System.out.println("  " + time.format(DateTimeFormatter.ISO_LOCAL_TIME));

        // ISO_LOCAL_DATE_TIME - for LocalDateTime
        System.out.println("\nISO_LOCAL_DATE_TIME (yyyy-MM-ddTHH:mm:ss):");
        System.out.println("  " + dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        System.out.println();
    }

    // ========================================================================
    // CUSTOM FORMATTERS
    // ========================================================================

    public static void demonstrateCustomFormatters() {
        System.out.println("=== CUSTOM DATE/TIME FORMATTERS ===\n");

        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 14, 30, 45);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.of("America/New_York"));

        // Year, Month, Day patterns
        System.out.println("Date patterns (y, M, d):");
        System.out.println("  yyyy-MM-dd:    " + dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        System.out.println("  yy/M/d:        " + dateTime.format(DateTimeFormatter.ofPattern("yy/M/d")));
        System.out.println("  MMMM d, yyyy:  " + dateTime.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));
        System.out.println("  MMM dd yyyy:   " + dateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy")));

        System.out.println();

        // Time patterns
        System.out.println("Time patterns (h, H, m, s, S, a):");
        System.out.println("  HH:mm:ss:      " + dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        System.out.println("  h:mm a:        " + dateTime.format(DateTimeFormatter.ofPattern("h:mm a")));
        System.out.println("  hh:mm:ss a:    " + dateTime.format(DateTimeFormatter.ofPattern("hh:mm:ss a")));

        System.out.println();

        // Time zone patterns (z, Z) - requires ZonedDateTime
        System.out.println("Time zone patterns (z, Z) - requires ZonedDateTime:");
        System.out.println("  z (zone name):   " + zonedDateTime.format(DateTimeFormatter.ofPattern("z")));
        System.out.println("  Z (zone offset): " + zonedDateTime.format(DateTimeFormatter.ofPattern("Z")));
        System.out.println("  Full with zone:  " + zonedDateTime.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")));

        System.out.println();

        // Pattern symbol meanings
        System.out.println("Symbol repetition affects output:");
        System.out.println("  M    = " + dateTime.format(DateTimeFormatter.ofPattern("M")));      // 1
        System.out.println("  MM   = " + dateTime.format(DateTimeFormatter.ofPattern("MM")));     // 01
        System.out.println("  MMM  = " + dateTime.format(DateTimeFormatter.ofPattern("MMM")));    // Jan
        System.out.println("  MMMM = " + dateTime.format(DateTimeFormatter.ofPattern("MMMM")));   // January

        System.out.println();
    }

    // ========================================================================
    // SYMBOL COMPATIBILITY
    // ========================================================================

    /**
     * IMPORTANT: Using incompatible symbols throws DateTimeException!
     */
    public static void demonstrateSymbolCompatibility() {
        System.out.println("=== SYMBOL COMPATIBILITY ===\n");

        LocalDate date = LocalDate.of(2024, 1, 15);
        LocalTime time = LocalTime.of(14, 30, 45);
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.of("America/New_York"));

        System.out.println("Compatibility table:");
        System.out.println("                    | LocalDate | LocalTime | LocalDateTime | ZonedDateTime");
        System.out.println("  ------------------|-----------|-----------|---------------|---------------");
        System.out.println("  y,M,d (date)      |    YES    |    NO     |     YES       |     YES");
        System.out.println("  h,H,m,s,S,a (time)|    NO     |    YES    |     YES       |     YES");
        System.out.println("  z,Z (zone)        |    NO     |    NO     |     NO        |     YES");
        System.out.println();

        // Demonstrate incompatible type exception
        System.out.println("Incompatible type throws DateTimeException:");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        try {
            date.format(timeFormatter);  // LocalDate cannot use time symbols!
        } catch (Exception e) {
            System.out.println("  LocalDate with HH:mm:ss: " + e.getClass().getSimpleName());
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            time.format(dateFormatter);  // LocalTime cannot use date symbols!
        } catch (Exception e) {
            System.out.println("  LocalTime with yyyy-MM-dd: " + e.getClass().getSimpleName());
        }

        DateTimeFormatter zoneFormatter = DateTimeFormatter.ofPattern("z");
        try {
            dateTime.format(zoneFormatter);  // LocalDateTime cannot use zone symbols!
        } catch (Exception e) {
            System.out.println("  LocalDateTime with z: " + e.getClass().getSimpleName());
        }

        System.out.println();
        System.out.println("EXAM TIP: Mismatched symbols = DateTimeException at runtime!\n");
    }

    // ========================================================================
    // ESCAPING TEXT WITH SINGLE QUOTES
    // ========================================================================

    /**
     * Use single quotes ' to insert literal text in format patterns.
     * To include a literal single quote, use two single quotes ''.
     */
    public static void demonstrateEscapingText() {
        System.out.println("=== ESCAPING TEXT WITH SINGLE QUOTES ===\n");

        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 14, 30, 0);

        // Inserting text with single quotes
        System.out.println("Using single quotes to insert text:");

        String pattern1 = "'Date:' yyyy-MM-dd";
        System.out.println("  Pattern: " + pattern1);
        System.out.println("  Result:  " + dateTime.format(DateTimeFormatter.ofPattern(pattern1)));

        String pattern2 = "MMMM d, yyyy 'at' h:mm a";
        System.out.println("\n  Pattern: " + pattern2);
        System.out.println("  Result:  " + dateTime.format(DateTimeFormatter.ofPattern(pattern2)));

        String pattern3 = "'Today is' EEEE";
        System.out.println("\n  Pattern: " + pattern3);
        System.out.println("  Result:  " + dateTime.format(DateTimeFormatter.ofPattern(pattern3)));

        // Literal single quote with ''
        String pattern4 = "h 'o''clock' a";
        System.out.println("\n  Pattern: " + pattern4 + " (two single quotes for literal ')");
        System.out.println("  Result:  " + dateTime.format(DateTimeFormatter.ofPattern(pattern4)));

        System.out.println();
        System.out.println("Rules:");
        System.out.println("  - Wrap literal text in single quotes: 'text'");
        System.out.println("  - For literal single quote, use two: ''\n");
    }

    // ========================================================================
    // TWO WAYS TO FORMAT
    // ========================================================================

    /**
     * Both methods produce the same result:
     *   1. dateTime.format(formatter)
     *   2. formatter.format(dateTime)
     */
    public static void demonstrateTwoFormatMethods() {
        System.out.println("=== TWO WAYS TO FORMAT ===\n");

        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 14, 30, 45);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Method 1: Call format() on the date/time object
        String result1 = dateTime.format(formatter);
        System.out.println("Method 1: dateTime.format(formatter)");
        System.out.println("  Result: " + result1);

        // Method 2: Call format() on the formatter
        String result2 = formatter.format(dateTime);
        System.out.println("\nMethod 2: formatter.format(dateTime)");
        System.out.println("  Result: " + result2);

        System.out.println("\nBoth methods produce the same result: " + result1.equals(result2));
        System.out.println();
    }

    // ========================================================================
    // EXAM SUMMARY
    // ========================================================================

    /**
     * EXAM SUMMARY:
     * =============
     *
     * DECIMALFORMAT:
     *   # = Omit position if no digit
     *   0 = Put 0 if no digit
     *
     * PREDEFINED FORMATTERS:
     *   ISO_LOCAL_DATE      - LocalDate       (yyyy-MM-dd)
     *   ISO_LOCAL_TIME      - LocalTime       (HH:mm:ss)
     *   ISO_LOCAL_DATE_TIME - LocalDateTime   (yyyy-MM-ddTHH:mm:ss)
     *
     * CUSTOM FORMAT SYMBOLS:
     *   y = year, M = month, d = day
     *   h = hour (12), H = hour (24), m = minute, s = second
     *   S = fraction of second, a = AM/PM
     *   z = zone name, Z = zone offset
     *
     * SYMBOL COMPATIBILITY:
     *   - Date symbols (y,M,d): LocalDate, LocalDateTime, ZonedDateTime
     *   - Time symbols (h,m,s,a): LocalTime, LocalDateTime, ZonedDateTime
     *   - Zone symbols (z,Z): ZonedDateTime ONLY
     *   - Mismatch = DateTimeException at RUNTIME
     *
     * ESCAPING TEXT:
     *   - Use single quotes: 'literal text'
     *   - Literal quote: ''
     *
     * TWO FORMAT METHODS:
     *   - dateTime.format(formatter)
     *   - formatter.format(dateTime)
     */
}
