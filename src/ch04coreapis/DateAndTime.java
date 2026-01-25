package ch04coreapis;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * DATE AND TIME API
 * =================
 *
 * Modern Date and Time API (java.time package) introduced in Java 8.
 *
 *
 * ============================================================================
 * KEY CONCEPTS
 * ============================================================================
 *
 * UTC (Coordinated Universal Time)
 * ────────────────────────────────
 * - Universal time standard
 * - Time at longitude 0° (Greenwich, England)
 * - Not affected by time zones or daylight saving time
 * - Also known as GMT (Greenwich Mean Time) - though technically different
 * - Used as basis for time zones: EST = UTC-5, PST = UTC-8, etc.
 *
 *
 * NO PUBLIC CONSTRUCTORS
 * ──────────────────────
 * Date/Time classes do NOT have public constructors!
 * MUST use static factory methods (of(), now(), parse(), etc.)
 *
 * WRONG:
 *   LocalDate date = new LocalDate();  // DOES NOT COMPILE!
 *
 * CORRECT:
 *   LocalDate date = LocalDate.now();  // Factory method
 *
 *
 * IMMUTABILITY
 * ────────────
 * ALL Date/Time classes are IMMUTABLE!
 * - Calling methods returns NEW object
 * - Original object is UNCHANGED
 * - Thread-safe
 *
 * EXAMPLE:
 *   LocalDate date = LocalDate.of(2024, 1, 15);
 *   date.plusDays(1);  // Returns new object, date is UNCHANGED!
 *
 *   LocalDate tomorrow = date.plusDays(1);  // CORRECT - save returned value
 *
 *
 * ============================================================================
 * FOUR MAIN DATE/TIME CLASSES
 * ============================================================================
 *
 * ┌──────────────────┬──────┬──────┬───────────┬──────────────────────────┐
 * │ Class            │ Date │ Time │ Time Zone │ Use Case                 │
 * ├──────────────────┼──────┼──────┼───────────┼──────────────────────────┤
 * │ LocalDate        │ YES  │ NO   │ NO        │ Date only (birthday)     │
 * │ LocalTime        │ NO   │ YES  │ NO        │ Time only (alarm clock)  │
 * │ LocalDateTime    │ YES  │ YES  │ NO        │ Date + Time (no zone)    │
 * │ ZonedDateTime    │ YES  │ YES  │ YES       │ Date + Time + Zone       │
 * └──────────────────┴──────┴──────┴───────────┴──────────────────────────┘
 *
 * MEMORY AID:
 * - "Local" = no time zone
 * - "Zoned" = has time zone
 *
 *
 * ============================================================================
 * LOCALDATE - DATE ONLY
 * ============================================================================
 *
 * Represents a date without time or time zone (year-month-day).
 *
 * FACTORY METHODS:
 *
 * static LocalDate now()
 * ──────────────────────
 * Current date from system clock
 *
 *   LocalDate today = LocalDate.now();  // e.g., 2024-01-15
 *
 *
 * static LocalDate of(int year, int month, int dayOfMonth)
 * ─────────────────────────────────────────────────────────
 * Specific date from year, month, day
 *
 *   LocalDate date = LocalDate.of(2024, 1, 15);  // January 15, 2024
 *
 *
 * static LocalDate of(int year, Month month, int dayOfMonth)
 * ───────────────────────────────────────────────────────────
 * Specific date using Month enum
 *
 *   LocalDate date = LocalDate.of(2024, Month.JANUARY, 15);
 *
 *
 * static LocalDate parse(CharSequence text)
 * ──────────────────────────────────────────
 * Parse from ISO-8601 format (YYYY-MM-DD)
 *
 *   LocalDate date = LocalDate.parse("2024-01-15");
 *
 *
 * MANIPULATION METHODS (LocalDate):
 * ─────────────────────────────────
 *
 * LocalDate plusDays(long days)
 * LocalDate plusWeeks(long weeks)
 * LocalDate plusMonths(long months)
 * LocalDate plusYears(long years)
 *
 * LocalDate minusDays(long days)
 * LocalDate minusWeeks(long weeks)
 * LocalDate minusMonths(long months)
 * LocalDate minusYears(long years)
 *
 * EXAM TRICK - DOES NOT HAVE TIME METHODS:
 *   date.plusHours(1)    // DOES NOT COMPILE! (no hours in date)
 *   date.plusMinutes(30) // DOES NOT COMPILE!
 *   date.plusSeconds(10) // DOES NOT COMPILE!
 *
 *
 * ACCESSOR METHODS (LocalDate):
 * ─────────────────────────────
 *
 * int getYear()
 * Month getMonth()              // Returns Month enum
 * int getMonthValue()           // Returns 1-12
 * int getDayOfMonth()           // Returns 1-31
 * int getDayOfYear()            // Returns 1-365/366
 * DayOfWeek getDayOfWeek()      // Returns DayOfWeek enum
 *
 * EXAM TRICK - NO TIME ACCESSORS:
 *   date.getHour()     // DOES NOT COMPILE!
 *   date.getMinute()   // DOES NOT COMPILE!
 *   date.getSecond()   // DOES NOT COMPILE!
 *
 *
 * ============================================================================
 * LOCALTIME - TIME ONLY
 * ============================================================================
 *
 * Represents a time without date or time zone (hour-minute-second-nano).
 *
 * FACTORY METHODS:
 *
 * static LocalTime now()
 * ──────────────────────
 * Current time from system clock
 *
 *   LocalTime now = LocalTime.now();  // e.g., 14:30:00.123
 *
 *
 * static LocalTime of(int hour, int minute)
 * ──────────────────────────────────────────
 * Specific time with hour and minute
 *
 *   LocalTime time = LocalTime.of(14, 30);  // 2:30 PM
 *
 *
 * static LocalTime of(int hour, int minute, int second)
 * ──────────────────────────────────────────────────────
 * Specific time with hour, minute, second
 *
 *   LocalTime time = LocalTime.of(14, 30, 45);  // 2:30:45 PM
 *
 *
 * static LocalTime of(int hour, int minute, int second, int nanoOfSecond)
 * ────────────────────────────────────────────────────────────────────────
 * Specific time with hour, minute, second, nanosecond
 *
 *   LocalTime time = LocalTime.of(14, 30, 45, 123456789);
 *
 *
 * static LocalTime parse(CharSequence text)
 * ──────────────────────────────────────────
 * Parse from ISO-8601 format (HH:mm:ss)
 *
 *   LocalTime time = LocalTime.parse("14:30:45");
 *
 *
 * MANIPULATION METHODS (LocalTime):
 * ─────────────────────────────────
 *
 * LocalTime plusHours(long hours)
 * LocalTime plusMinutes(long minutes)
 * LocalTime plusSeconds(long seconds)
 * LocalTime plusNanos(long nanos)
 *
 * LocalTime minusHours(long hours)
 * LocalTime minusMinutes(long minutes)
 * LocalTime minusSeconds(long seconds)
 * LocalTime minusNanos(long nanos)
 *
 * EXAM TRICK - DOES NOT HAVE DATE METHODS:
 *   time.plusDays(1)    // DOES NOT COMPILE! (no days in time)
 *   time.plusWeeks(1)   // DOES NOT COMPILE!
 *   time.plusMonths(1)  // DOES NOT COMPILE!
 *   time.plusYears(1)   // DOES NOT COMPILE!
 *
 *
 * ACCESSOR METHODS (LocalTime):
 * ─────────────────────────────
 *
 * int getHour()        // 0-23
 * int getMinute()      // 0-59
 * int getSecond()      // 0-59
 * int getNano()        // 0-999,999,999
 *
 * EXAM TRICK - NO DATE ACCESSORS:
 *   time.getYear()         // DOES NOT COMPILE!
 *   time.getMonth()        // DOES NOT COMPILE!
 *   time.getDayOfMonth()   // DOES NOT COMPILE!
 *
 *
 * ============================================================================
 * LOCALDATETIME - DATE AND TIME
 * ============================================================================
 *
 * Represents a date-time without time zone (combines LocalDate and LocalTime).
 *
 * FACTORY METHODS:
 *
 * static LocalDateTime now()
 * ──────────────────────────
 * Current date-time from system clock
 *
 *   LocalDateTime now = LocalDateTime.now();  // e.g., 2024-01-15T14:30:00
 *
 *
 * static LocalDateTime of(int year, int month, int day, int hour, int minute)
 * ────────────────────────────────────────────────────────────────────────────
 * Specific date-time
 *
 *   LocalDateTime dt = LocalDateTime.of(2024, 1, 15, 14, 30);
 *
 *
 * static LocalDateTime of(int year, Month month, int day, int hour, int minute)
 * ──────────────────────────────────────────────────────────────────────────────
 * With Month enum
 *
 *   LocalDateTime dt = LocalDateTime.of(2024, Month.JANUARY, 15, 14, 30);
 *
 *
 * static LocalDateTime of(int year, int month, int day, int hour, int minute, int second)
 * ─────────────────────────────────────────────────────────────────────────────────────────
 * With seconds
 *
 *   LocalDateTime dt = LocalDateTime.of(2024, 1, 15, 14, 30, 45);
 *
 *
 * static LocalDateTime of(int year, int month, int day, int hour, int minute, int second, int nano)
 * ────────────────────────────────────────────────────────────────────────────────────────────────────
 * With nanoseconds
 *
 *   LocalDateTime dt = LocalDateTime.of(2024, 1, 15, 14, 30, 45, 123456789);
 *
 *
 * static LocalDateTime of(LocalDate date, LocalTime time)
 * ────────────────────────────────────────────────────────
 * Combine LocalDate and LocalTime
 *
 *   LocalDate date = LocalDate.of(2024, 1, 15);
 *   LocalTime time = LocalTime.of(14, 30);
 *   LocalDateTime dt = LocalDateTime.of(date, time);
 *
 *
 * MANIPULATION METHODS (LocalDateTime):
 * ─────────────────────────────────────
 *
 * HAS BOTH DATE AND TIME METHODS:
 *
 * LocalDateTime plusYears(long years)
 * LocalDateTime plusMonths(long months)
 * LocalDateTime plusWeeks(long weeks)
 * LocalDateTime plusDays(long days)
 * LocalDateTime plusHours(long hours)       // Has time methods!
 * LocalDateTime plusMinutes(long minutes)   // Has time methods!
 * LocalDateTime plusSeconds(long seconds)   // Has time methods!
 * LocalDateTime plusNanos(long nanos)       // Has time methods!
 *
 * LocalDateTime minusYears(long years)
 * LocalDateTime minusMonths(long months)
 * LocalDateTime minusWeeks(long weeks)
 * LocalDateTime minusDays(long days)
 * LocalDateTime minusHours(long hours)
 * LocalDateTime minusMinutes(long minutes)
 * LocalDateTime minusSeconds(long seconds)
 * LocalDateTime minusNanos(long nanos)
 *
 *
 * ACCESSOR METHODS (LocalDateTime):
 * ─────────────────────────────────
 *
 * HAS BOTH DATE AND TIME ACCESSORS:
 *
 * // Date accessors
 * int getYear()
 * Month getMonth()
 * int getMonthValue()
 * int getDayOfMonth()
 * int getDayOfYear()
 * DayOfWeek getDayOfWeek()
 *
 * // Time accessors
 * int getHour()
 * int getMinute()
 * int getSecond()
 * int getNano()
 *
 * // Extract parts
 * LocalDate toLocalDate()
 * LocalTime toLocalTime()
 *
 *
 * ============================================================================
 * ZONEDDATETIME - DATE, TIME, AND TIME ZONE
 * ============================================================================
 *
 * Represents a date-time with time zone (most complete date-time class).
 *
 * FACTORY METHODS:
 *
 * static ZonedDateTime now()
 * ──────────────────────────
 * Current date-time with system default time zone
 *
 *   ZonedDateTime now = ZonedDateTime.now();
 *   // e.g., 2024-01-15T14:30:00-05:00[America/New_York]
 *
 *
 * static ZonedDateTime now(ZoneId zone)
 * ──────────────────────────────────────
 * Current date-time in specific time zone
 *
 *   ZonedDateTime tokyo = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
 *
 *
 * static ZonedDateTime of(LocalDateTime dateTime, ZoneId zone)
 * ──────────────────────────────────────────────────────────────
 * From LocalDateTime and ZoneId
 *
 *   LocalDateTime dt = LocalDateTime.of(2024, 1, 15, 14, 30);
 *   ZonedDateTime zdt = ZonedDateTime.of(dt, ZoneId.of("America/New_York"));
 *
 *
 * static ZonedDateTime of(LocalDate date, LocalTime time, ZoneId zone)
 * ──────────────────────────────────────────────────────────────────────
 * From LocalDate, LocalTime, and ZoneId
 *
 *   LocalDate date = LocalDate.of(2024, 1, 15);
 *   LocalTime time = LocalTime.of(14, 30);
 *   ZonedDateTime zdt = ZonedDateTime.of(date, time, ZoneId.of("UTC"));
 *
 *
 * static ZonedDateTime of(int year, int month, int day, int hour, int minute,
 *                         int second, int nano, ZoneId zone)
 * ─────────────────────────────────────────────────────────────────────────
 * From individual fields
 *
 *   ZonedDateTime zdt = ZonedDateTime.of(2024, 1, 15, 14, 30, 0, 0,
 *                                         ZoneId.of("America/New_York"));
 *
 *
 * MANIPULATION METHODS (ZonedDateTime):
 * ─────────────────────────────────────
 *
 * HAS ALL DATE AND TIME METHODS (like LocalDateTime):
 *
 * ZonedDateTime plusYears(long years)
 * ZonedDateTime plusMonths(long months)
 * ZonedDateTime plusWeeks(long weeks)
 * ZonedDateTime plusDays(long days)
 * ZonedDateTime plusHours(long hours)
 * ZonedDateTime plusMinutes(long minutes)
 * ZonedDateTime plusSeconds(long seconds)
 * ZonedDateTime plusNanos(long nanos)
 *
 * (same for minus methods)
 *
 * ZONE METHODS:
 * ZonedDateTime withZoneSameInstant(ZoneId zone)  // Convert to different zone
 * ZonedDateTime withZoneSameLocal(ZoneId zone)    // Same local time, different zone
 *
 *
 * ACCESSOR METHODS (ZonedDateTime):
 * ─────────────────────────────────
 *
 * All LocalDateTime accessors PLUS:
 *
 * ZoneId getZone()
 * ZoneOffset getOffset()
 *
 * // Extract parts
 * LocalDate toLocalDate()
 * LocalTime toLocalTime()
 * LocalDateTime toLocalDateTime()
 *
 *
 * ============================================================================
 * WHICH METHODS WORK ON WHICH CLASSES - CRITICAL FOR EXAM!
 * ============================================================================
 *
 * ┌──────────────────┬───────────┬───────────┬───────────────┬───────────────┐
 * │ Method Type      │ LocalDate │ LocalTime │ LocalDateTime │ ZonedDateTime │
 * ├──────────────────┼───────────┼───────────┼───────────────┼───────────────┤
 * │ plusYears()      │ YES       │ NO ✗      │ YES           │ YES           │
 * │ plusMonths()     │ YES       │ NO ✗      │ YES           │ YES           │
 * │ plusWeeks()      │ YES       │ NO ✗      │ YES           │ YES           │
 * │ plusDays()       │ YES       │ NO ✗      │ YES           │ YES           │
 * ├──────────────────┼───────────┼───────────┼───────────────┼───────────────┤
 * │ plusHours()      │ NO ✗      │ YES       │ YES           │ YES           │
 * │ plusMinutes()    │ NO ✗      │ YES       │ YES           │ YES           │
 * │ plusSeconds()    │ NO ✗      │ YES       │ YES           │ YES           │
 * │ plusNanos()      │ NO ✗      │ YES       │ YES           │ YES           │
 * ├──────────────────┼───────────┼───────────┼───────────────┼───────────────┤
 * │ getYear()        │ YES       │ NO ✗      │ YES           │ YES           │
 * │ getMonth()       │ YES       │ NO ✗      │ YES           │ YES           │
 * │ getDayOfMonth()  │ YES       │ NO ✗      │ YES           │ YES           │
 * ├──────────────────┼───────────┼───────────┼───────────────┼───────────────┤
 * │ getHour()        │ NO ✗      │ YES       │ YES           │ YES           │
 * │ getMinute()      │ NO ✗      │ YES       │ YES           │ YES           │
 * │ getSecond()      │ NO ✗      │ YES       │ YES           │ YES           │
 * └──────────────────┴───────────┴───────────┴───────────────┴───────────────┘
 *
 * MEMORY RULE:
 * - LocalDate: ONLY date methods (years, months, weeks, days)
 * - LocalTime: ONLY time methods (hours, minutes, seconds, nanos)
 * - LocalDateTime: BOTH date AND time methods
 * - ZonedDateTime: BOTH date AND time methods
 *
 * COMMON EXAM TRICKS:
 *   LocalDate date = LocalDate.now();
 *   date.plusHours(1);        // DOES NOT COMPILE! ✗
 *   date.getHour();           // DOES NOT COMPILE! ✗
 *
 *   LocalTime time = LocalTime.now();
 *   time.plusDays(1);         // DOES NOT COMPILE! ✗
 *   time.getYear();           // DOES NOT COMPILE! ✗
 *
 *
 * ============================================================================
 * METHOD CHAINING
 * ============================================================================
 *
 * Since all methods return new immutable objects, can chain calls.
 *
 * EXAMPLE:
 *   LocalDate date = LocalDate.of(2024, 1, 15)
 *                             .plusDays(10)
 *                             .plusMonths(2)
 *                             .minusWeeks(1);
 *   // Result: 2024-03-18
 *
 *   LocalDateTime dt = LocalDateTime.of(2024, 1, 15, 10, 30)
 *                                   .plusDays(5)
 *                                   .plusHours(3)
 *                                   .minusMinutes(15);
 *   // Result: 2024-01-20T13:15
 *
 * IMPORTANT:
 * - Each method in chain returns NEW object
 * - Original object NEVER modified
 * - Must save final result
 *
 * EXAM TRAP:
 *   LocalDate date = LocalDate.of(2024, 1, 15);
 *   date.plusDays(1).plusMonths(1);  // Creates new object but doesn't save!
 *   System.out.println(date);         // Still 2024-01-15 (unchanged!)
 *
 * CORRECT:
 *   LocalDate date = LocalDate.of(2024, 1, 15);
 *   date = date.plusDays(1).plusMonths(1);  // Save result!
 *   System.out.println(date);  // 2024-02-16
 *
 *
 * ============================================================================
 * COMMON METHODS (ALL CLASSES)
 * ============================================================================
 *
 * isBefore(ChronoLocalDate other) / isBefore(LocalTime other) / etc.
 * isAfter(ChronoLocalDate other) / isAfter(LocalTime other) / etc.
 * isEqual(ChronoLocalDate other) / equals(Object obj)
 *
 * EXAMPLE:
 *   LocalDate d1 = LocalDate.of(2024, 1, 15);
 *   LocalDate d2 = LocalDate.of(2024, 1, 20);
 *
 *   d1.isBefore(d2);  // true
 *   d1.isAfter(d2);   // false
 *   d1.isEqual(d2);   // false
 *
 *
 * ============================================================================
 * CODE EXAMPLES
 * ============================================================================
 */
public class DateAndTime {

    /**
     * Demonstrates LocalDate
     */
    public static void demonstrateLocalDate() {
        System.out.println("=== LocalDate (Date Only) ===");

        // Factory methods
        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.of(1990, 5, 15);
        LocalDate independence = LocalDate.of(1776, Month.JULY, 4);

        System.out.println("Today: " + today);
        System.out.println("Birthday: " + birthday);
        System.out.println("Independence Day: " + independence);

        // Manipulation (returns new object!)
        LocalDate tomorrow = today.plusDays(1);
        LocalDate nextMonth = today.plusMonths(1);
        LocalDate nextYear = today.plusYears(1);

        System.out.println("Tomorrow: " + tomorrow);
        System.out.println("Next month: " + nextMonth);

        // Method chaining
        LocalDate future = today.plusDays(10).plusMonths(2).minusWeeks(1);
        System.out.println("Future date: " + future);

        // Accessors
        System.out.println("Year: " + today.getYear());
        System.out.println("Month: " + today.getMonth());
        System.out.println("Day of month: " + today.getDayOfMonth());
        System.out.println("Day of week: " + today.getDayOfWeek());

        // EXAM TRAP - These don't compile:
        // today.plusHours(1);    // NO time methods on LocalDate!
        // today.getHour();       // NO time accessors on LocalDate!

        System.out.println();
    }

    /**
     * Demonstrates LocalTime
     */
    public static void demonstrateLocalTime() {
        System.out.println("=== LocalTime (Time Only) ===");

        // Factory methods
        LocalTime now = LocalTime.now();
        LocalTime lunchTime = LocalTime.of(12, 30);
        LocalTime precise = LocalTime.of(14, 30, 45);
        LocalTime veryPrecise = LocalTime.of(14, 30, 45, 123456789);

        System.out.println("Now: " + now);
        System.out.println("Lunch time: " + lunchTime);
        System.out.println("Precise: " + precise);

        // Manipulation
        LocalTime later = lunchTime.plusHours(2);
        LocalTime earlier = lunchTime.minusMinutes(30);

        System.out.println("2 hours after lunch: " + later);
        System.out.println("30 min before lunch: " + earlier);

        // Method chaining
        LocalTime adjusted = lunchTime.plusHours(3).minusMinutes(15);
        System.out.println("Adjusted: " + adjusted);

        // Accessors
        System.out.println("Hour: " + lunchTime.getHour());
        System.out.println("Minute: " + lunchTime.getMinute());

        // EXAM TRAP - These don't compile:
        // lunchTime.plusDays(1);    // NO date methods on LocalTime!
        // lunchTime.getYear();      // NO date accessors on LocalTime!

        System.out.println();
    }

    /**
     * Demonstrates LocalDateTime
     */
    public static void demonstrateLocalDateTime() {
        System.out.println("=== LocalDateTime (Date + Time) ===");

        // Factory methods
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime meeting = LocalDateTime.of(2024, 1, 15, 14, 30);
        LocalDateTime detailed = LocalDateTime.of(2024, 1, 15, 14, 30, 45);

        // Combine date and time
        LocalDate date = LocalDate.of(2024, 1, 15);
        LocalTime time = LocalTime.of(14, 30);
        LocalDateTime combined = LocalDateTime.of(date, time);

        System.out.println("Now: " + now);
        System.out.println("Meeting: " + meeting);
        System.out.println("Combined: " + combined);

        // HAS BOTH DATE AND TIME METHODS!
        LocalDateTime future = meeting.plusDays(5)      // Date method ✓
                                      .plusHours(2)     // Time method ✓
                                      .minusMinutes(15);  // Time method ✓

        System.out.println("Future: " + future);

        // Accessors - both date and time
        System.out.println("Year: " + meeting.getYear());         // Date accessor ✓
        System.out.println("Month: " + meeting.getMonth());       // Date accessor ✓
        System.out.println("Hour: " + meeting.getHour());         // Time accessor ✓
        System.out.println("Minute: " + meeting.getMinute());     // Time accessor ✓

        // Extract parts
        LocalDate extractedDate = meeting.toLocalDate();
        LocalTime extractedTime = meeting.toLocalTime();
        System.out.println("Extracted date: " + extractedDate);
        System.out.println("Extracted time: " + extractedTime);

        System.out.println();
    }

    /**
     * Demonstrates ZonedDateTime
     */
    public static void demonstrateZonedDateTime() {
        System.out.println("=== ZonedDateTime (Date + Time + Zone) ===");

        // Factory methods
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokyo = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        ZonedDateTime nyc = ZonedDateTime.now(ZoneId.of("America/New_York"));

        System.out.println("Now (system): " + now);
        System.out.println("Tokyo: " + tokyo);
        System.out.println("NYC: " + nyc);

        // From LocalDateTime
        LocalDateTime dt = LocalDateTime.of(2024, 1, 15, 14, 30);
        ZonedDateTime zdt = ZonedDateTime.of(dt, ZoneId.of("America/Los_Angeles"));
        System.out.println("LA meeting: " + zdt);

        // HAS ALL DATE AND TIME METHODS
        ZonedDateTime future = zdt.plusDays(10)
                                  .plusHours(5)
                                  .minusMinutes(30);
        System.out.println("Future: " + future);

        // Change time zone
        ZonedDateTime tokyoTime = zdt.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
        System.out.println("Same instant in Tokyo: " + tokyoTime);

        // Accessors
        System.out.println("Zone: " + zdt.getZone());
        System.out.println("Offset: " + zdt.getOffset());

        System.out.println();
    }

    /**
     * Demonstrates immutability
     */
    public static void demonstrateImmutability() {
        System.out.println("=== Immutability ===");

        LocalDate date = LocalDate.of(2024, 1, 15);
        System.out.println("Original: " + date);

        // This creates NEW object but doesn't save it!
        date.plusDays(1);  // TRAP: Result not saved!
        System.out.println("After plusDays (not saved): " + date);  // Still 2024-01-15

        // Correct - save the result
        date = date.plusDays(1);
        System.out.println("After plusDays (saved): " + date);  // Now 2024-01-16

        System.out.println();
    }

    /**
     * Demonstrates method chaining
     */
    public static void demonstrateMethodChaining() {
        System.out.println("=== Method Chaining ===");

        LocalDate start = LocalDate.of(2024, 1, 1);
        System.out.println("Start: " + start);

        // Chain multiple operations
        LocalDate result = start.plusMonths(3)
                                .plusDays(15)
                                .minusWeeks(2);
        System.out.println("After chaining: " + result);

        // LocalDateTime chaining with both date and time
        LocalDateTime dt = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime adjusted = dt.plusDays(5)
                                   .plusHours(3)
                                   .minusMinutes(30)
                                   .plusMonths(1);
        System.out.println("Adjusted datetime: " + adjusted);

        System.out.println();
    }

    /**
     * Demonstrates comparison methods
     */
    public static void demonstrateComparisons() {
        System.out.println("=== Comparisons ===");

        LocalDate d1 = LocalDate.of(2024, 1, 15);
        LocalDate d2 = LocalDate.of(2024, 1, 20);
        LocalDate d3 = LocalDate.of(2024, 1, 15);

        System.out.println("d1 is before d2: " + d1.isBefore(d2));   // true
        System.out.println("d1 is after d2: " + d1.isAfter(d2));     // false
        System.out.println("d1 is equal d3: " + d1.isEqual(d3));     // true
        System.out.println("d1 equals d3: " + d1.equals(d3));        // true

        System.out.println();
    }

    /**
     * Demonstrates common exam traps
     */
    public static void demonstrateExamTraps() {
        System.out.println("=== Common Exam Traps ===");

        // TRAP 1: Using time methods on LocalDate
        LocalDate date = LocalDate.of(2024, 1, 15);
        // date.plusHours(1);     // DOES NOT COMPILE!
        // date.getHour();        // DOES NOT COMPILE!
        System.out.println("✗ LocalDate cannot use time methods (plusHours, getHour)");

        // TRAP 2: Using date methods on LocalTime
        LocalTime time = LocalTime.of(14, 30);
        // time.plusDays(1);      // DOES NOT COMPILE!
        // time.getYear();        // DOES NOT COMPILE!
        System.out.println("✗ LocalTime cannot use date methods (plusDays, getYear)");

        // TRAP 3: Forgetting immutability
        LocalDate original = LocalDate.of(2024, 1, 15);
        original.plusDays(5);  // Creates new object but doesn't save!
        System.out.println("After plusDays without saving: " + original);  // Still 01-15
        System.out.println("✗ Must save result: date = date.plusDays(5)");

        // TRAP 4: Using constructor
        // LocalDate wrong = new LocalDate();  // DOES NOT COMPILE!
        System.out.println("✗ No public constructors - must use factory methods");

        System.out.println();
    }

    public static void main(String[] args) {
        demonstrateLocalDate();
        demonstrateLocalTime();
        demonstrateLocalDateTime();
        demonstrateZonedDateTime();
        demonstrateImmutability();
        demonstrateMethodChaining();
        demonstrateComparisons();
        demonstrateExamTraps();
    }
}

/*
 * ============================================================================
 * EXAM SUMMARY - DATE AND TIME
 * ============================================================================
 *
 * FOUR MAIN CLASSES:
 * ┌──────────────────┬──────┬──────┬───────────┐
 * │ Class            │ Date │ Time │ Time Zone │
 * ├──────────────────┼──────┼──────┼───────────┤
 * │ LocalDate        │ YES  │ NO   │ NO        │
 * │ LocalTime        │ NO   │ YES  │ NO        │
 * │ LocalDateTime    │ YES  │ YES  │ NO        │
 * │ ZonedDateTime    │ YES  │ YES  │ YES       │
 * └──────────────────┴──────┴──────┴───────────┘
 *
 * KEY CONCEPTS:
 * - UTC = Coordinated Universal Time (time at longitude 0°)
 * - NO public constructors - use factory methods (of(), now(), parse())
 * - ALL classes are IMMUTABLE - methods return new objects
 *
 * FACTORY METHODS:
 * - now() - current date/time
 * - of(...) - specific date/time (multiple overloads)
 * - parse(String) - parse from ISO-8601 format
 *
 * WHICH METHODS WORK WHERE (CRITICAL!):
 * ┌──────────────────┬───────────┬───────────┬───────────────┬───────────────┐
 * │ Method Type      │ LocalDate │ LocalTime │ LocalDateTime │ ZonedDateTime │
 * ├──────────────────┼───────────┼───────────┼───────────────┼───────────────┤
 * │ Date methods     │ YES       │ NO ✗      │ YES           │ YES           │
 * │ (plus/minusYears,│           │           │               │               │
 * │  Months, Days)   │           │           │               │               │
 * ├──────────────────┼───────────┼───────────┼───────────────┼───────────────┤
 * │ Time methods     │ NO ✗      │ YES       │ YES           │ YES           │
 * │ (plus/minusHours,│           │           │               │               │
 * │  Minutes, Secs)  │           │           │               │               │
 * └──────────────────┴───────────┴───────────┴───────────────┴───────────────┘
 *
 * METHOD CHAINING:
 * - Can chain methods because each returns new object
 * - Must save final result (immutability!)
 * - Example: date.plusDays(1).plusMonths(1).minusWeeks(1)
 *
 * COMMON EXAM TRAPS:
 * - LocalDate.plusHours() - DOES NOT COMPILE (no time methods)
 * - LocalTime.plusDays() - DOES NOT COMPILE (no date methods)
 * - new LocalDate() - DOES NOT COMPILE (no public constructor)
 * - date.plusDays(1) without saving - compiles but doesn't change date
 *
 * COMPARISON METHODS:
 * - isBefore(other), isAfter(other), isEqual(other), equals(obj)
 */
