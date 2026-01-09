package ch04coreapis;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.UnsupportedTemporalTypeException;

/**
 * DATE/TIME: Period, Duration, and DAYLIGHT SAVINGS TIME
 * Complete Guide for OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * CRITICAL: Period vs Duration
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * PERIOD - Date-based (years, months, days)
 * ────────────────────────────────────────────────────────────────────────────
 * - Works with: LocalDate, LocalDateTime, ZonedDateTime
 * - Does NOT work with: LocalTime (throws UnsupportedTemporalTypeException)
 * - Units: years, months, days
 * - Format: P[years]Y[months]M[days]D
 * - Example: P1Y2M3D = 1 year, 2 months, 3 days
 *
 * DURATION - Time-based (hours, minutes, seconds, nanos)
 * ────────────────────────────────────────────────────────────────────────────
 * - Works with: LocalTime, LocalDateTime, ZonedDateTime
 * - Does NOT work with: LocalDate (throws UnsupportedTemporalTypeException)
 * - Units: hours, minutes, seconds, nanoseconds
 * - Format: PT[hours]H[minutes]M[seconds]S
 * - Example: PT1H30M = 1 hour, 30 minutes
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * EXAM TRAPS SUMMARY
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * 1. Period with LocalTime → UnsupportedTemporalTypeException
 * 2. Duration with LocalDate → UnsupportedTemporalTypeException
 * 3. Period.ofWeeks() exists but converts to days (no weeks field!)
 * 4. Period and Duration are IMMUTABLE - plus() returns NEW object
 * 5. DST Spring Forward: 2:30 AM doesn't exist → becomes 3:30 AM
 * 6. DST Fall Back: 1:30 AM exists TWICE → uses offset to disambiguate
 * 7. Negative periods/durations are allowed
 * 8. Cannot chain Period.of...() methods (only last one counts!)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * DAYLIGHT SAVINGS TIME (DST) - CRITICAL EXAM TOPIC!
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * In the US (America/New_York, America/Los_Angeles, etc.):
 *
 * SPRING FORWARD - Lose 1 hour (2nd Sunday in March)
 * ────────────────────────────────────────────────────────────────────────────
 * - Clocks jump from 2:00 AM → 3:00 AM
 * - Times 2:00-2:59 AM DO NOT EXIST on this day
 * - If you create 2:30 AM, Java adjusts to 3:30 AM
 * - Offset changes: EST (UTC-5) → EDT (UTC-4)
 *
 * FALL BACK - Gain 1 hour (1st Sunday in November)
 * ────────────────────────────────────────────────────────────────────────────
 * - Clocks jump from 2:00 AM → 1:00 AM
 * - Times 1:00-1:59 AM OCCUR TWICE on this day
 * - First occurrence: EDT (UTC-4)
 * - Second occurrence: EST (UTC-5)
 * - Java uses offset to distinguish which one
 *
 * 2024 DST Dates (for testing):
 * - Spring Forward: March 10, 2024 at 2:00 AM
 * - Fall Back: November 3, 2024 at 2:00 AM
 */
public class DateTimePeriodDuration {

    public static void main(String[] args) {
        System.out.println("=== PERIOD, DURATION, AND DST GUIDE ===\n");

        // Period examples
        periodBasics();
        periodCreation();
        periodWithDates();
        periodInvalidUsage();
        periodImmutability();
        periodChaining();

        // Duration examples
        durationBasics();
        durationCreation();
        durationWithTimes();
        durationInvalidUsage();

        // Instant examples
        instantBasics();
        instantCreation();
        instantWithPeriodAndDuration();
        instantConversions();
        instantComparisons();

        // DST examples - THE MOST IMPORTANT SECTION!
        dstSpringForwardBasics();
        dstSpringForwardInvalidTime();
        dstSpringForwardAddingTime();
        dstFallBackBasics();
        dstFallBackAmbiguousTime();
        dstFallBackAddingTime();
        dstPeriodVsDuration();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * PERIOD BASICS
     * ═══════════════════════════════════════════════════════════════════════
     *
     * Period represents date-based amounts (years, months, days)
     * Used with LocalDate, LocalDateTime, ZonedDateTime
     *
     * Key signatures:
     * - static Period of(int years, int months, int days)
     * - static Period ofYears(int years)
     * - static Period ofMonths(int months)
     * - static Period ofWeeks(int weeks)    ← converts to days!
     * - static Period ofDays(int days)
     * - static Period between(LocalDate start, LocalDate end)
     */
    private static void periodBasics() {
        System.out.println("=== PERIOD BASICS ===");
        System.out.println("Period = Date-based (years, months, days)");
        System.out.println("Works with: LocalDate, LocalDateTime, ZonedDateTime\n");

        // Creating periods
        Period p1 = Period.of(1, 2, 3);           // 1 year, 2 months, 3 days
        Period p2 = Period.ofYears(1);            // 1 year
        Period p3 = Period.ofMonths(6);           // 6 months
        Period p4 = Period.ofWeeks(2);            // 14 days (converts to days!)
        Period p5 = Period.ofDays(10);            // 10 days

        System.out.println("Period.of(1, 2, 3) = " + p1);        // P1Y2M3D
        System.out.println("Period.ofYears(1) = " + p2);         // P1Y
        System.out.println("Period.ofMonths(6) = " + p3);        // P6M
        System.out.println("Period.ofWeeks(2) = " + p4);         // P14D (not P2W!)
        System.out.println("Period.ofDays(10) = " + p5);         // P10D

        // Accessing components
        System.out.println("\n--- Period Components ---");
        Period p = Period.of(1, 2, 3);
        System.out.println("Period: " + p);
        System.out.println("  getYears() = " + p.getYears());    // 1
        System.out.println("  getMonths() = " + p.getMonths());  // 2
        System.out.println("  getDays() = " + p.getDays());      // 3

        System.out.println();
    }

    /**
     * PERIOD CREATION VARIATIONS
     *
     * EXAM TRAP: Negative values are allowed!
     */
    private static void periodCreation() {
        System.out.println("=== PERIOD CREATION VARIATIONS ===");

        // Positive period
        Period p1 = Period.of(1, 2, 3);
        System.out.println("Period.of(1, 2, 3) = " + p1);

        // Negative period - perfectly valid!
        Period p2 = Period.of(-1, -2, -3);
        System.out.println("Period.of(-1, -2, -3) = " + p2);     // P-1Y-2M-3D

        // Mixed signs - also valid!
        Period p3 = Period.of(1, -2, 3);
        System.out.println("Period.of(1, -2, 3) = " + p3);       // P1Y-2M3D

        // Zero period
        Period p4 = Period.of(0, 0, 0);
        System.out.println("Period.of(0, 0, 0) = " + p4);        // P0D

        // between() method - calculates difference
        System.out.println("\n--- Period.between() ---");
        LocalDate start = LocalDate.of(2024, 1, 15);
        LocalDate end = LocalDate.of(2024, 6, 20);
        Period between = Period.between(start, end);
        System.out.println("Between " + start + " and " + end + ": " + between);

        // between() with reversed dates - negative!
        Period reversed = Period.between(end, start);
        System.out.println("Between " + end + " and " + start + ": " + reversed);

        System.out.println();
    }

    /**
     * USING PERIOD WITH DATES
     *
     * Period works with:
     * - LocalDate.plus(Period)
     * - LocalDate.minus(Period)
     * - LocalDateTime.plus(Period)
     * - ZonedDateTime.plus(Period)
     */
    private static void periodWithDates() {
        System.out.println("=== USING PERIOD WITH DATES ===");

        LocalDate date = LocalDate.of(2024, 1, 15);
        System.out.println("Starting date: " + date);

        // Adding period
        Period p = Period.of(1, 2, 3);
        LocalDate result = date.plus(p);
        System.out.println("Add " + p + ": " + result);          // 2025-03-18

        // Subtracting period
        LocalDate result2 = date.minus(p);
        System.out.println("Subtract " + p + ": " + result2);    // 2022-11-12

        // Individual additions
        System.out.println("\n--- Individual Additions ---");
        System.out.println("Original: " + date);
        System.out.println("plusYears(1): " + date.plusYears(1));
        System.out.println("plusMonths(2): " + date.plusMonths(2));
        System.out.println("plusDays(3): " + date.plusDays(3));
        System.out.println("plusWeeks(2): " + date.plusWeeks(2));

        System.out.println();
    }

    /**
     * PERIOD INVALID USAGE - EXAM TRAP!
     *
     * *** CRITICAL ***
     * Period with LocalTime throws UnsupportedTemporalTypeException!
     */
    private static void periodInvalidUsage() {
        System.out.println("=== PERIOD INVALID USAGE - EXAM TRAP! ===");
        System.out.println("*** Period DOES NOT work with LocalTime! ***\n");

        LocalTime time = LocalTime.of(10, 30);
        Period p = Period.ofDays(1);

        System.out.println("LocalTime: " + time);
        System.out.println("Period: " + p);
        System.out.println("\nTrying time.plus(period)...");

        try {
            LocalTime result = time.plus(p);
            System.out.println("Result: " + result);  // This line won't execute
        } catch (UnsupportedTemporalTypeException e) {
            System.out.println("✗ EXCEPTION: " + e.getClass().getSimpleName());
            System.out.println("  Message: Unsupported unit: " + e.getMessage());
            System.out.println("\n  → Period cannot be added to LocalTime!");
            System.out.println("  → Use Duration instead for time-based operations");
        }

        System.out.println();
    }

    /**
     * PERIOD IMMUTABILITY - EXAM TRAP!
     *
     * *** CRITICAL ***
     * Period is IMMUTABLE - methods return NEW Period object!
     */
    private static void periodImmutability() {
        System.out.println("=== PERIOD IMMUTABILITY - EXAM TRAP! ===");
        System.out.println("*** Period is IMMUTABLE! ***\n");

        Period p = Period.ofDays(5);
        System.out.println("Original period: " + p);

        // This doesn't modify p - returns new Period!
        p.plusDays(3);
        System.out.println("After p.plusDays(3) without assignment: " + p);  // Still P5D!

        // Must assign to see change
        Period p2 = p.plusDays(3);
        System.out.println("p2 = p.plusDays(3): " + p2);                     // P8D

        System.out.println("\n*** Remember: Must assign result to use it! ***");
        System.out.println();
    }

    /**
     * PERIOD CHAINING - MAJOR EXAM TRAP!
     *
     * *** CRITICAL ***
     * Cannot chain Period.of...() methods!
     * Only the LAST call counts!
     */
    private static void periodChaining() {
        System.out.println("=== PERIOD CHAINING - MAJOR EXAM TRAP! ===");
        System.out.println("*** Cannot chain Period.of...() methods! ***\n");

        // WRONG - only ofDays(3) counts!
        Period wrong = Period.ofYears(1).ofMonths(2).ofDays(3);
        System.out.println("Period.ofYears(1).ofMonths(2).ofDays(3) = " + wrong);
        System.out.println("  ✗ Result: " + wrong + " (only days counted!)");
        System.out.println("  → ofYears(1) creates P1Y, then we CALL ofDays(3) which returns P3D");

        // CORRECT - use Period.of()
        Period correct = Period.of(1, 2, 3);
        System.out.println("\nPeriod.of(1, 2, 3) = " + correct);
        System.out.println("  ✓ Result: " + correct + " (all components!)");

        // CAN chain plus() methods (they return new Period)
        System.out.println("\n--- CAN chain plus() methods ---");
        Period p = Period.ofYears(1).plusMonths(2).plusDays(3);
        System.out.println("Period.ofYears(1).plusMonths(2).plusDays(3) = " + p);
        System.out.println("  ✓ Result: " + p);

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * DURATION BASICS
     * ═══════════════════════════════════════════════════════════════════════
     *
     * Duration represents time-based amounts (hours, minutes, seconds, nanos)
     * Used with LocalTime, LocalDateTime, ZonedDateTime
     *
     * Key signatures:
     * - static Duration ofDays(long days)
     * - static Duration ofHours(long hours)
     * - static Duration ofMinutes(long minutes)
     * - static Duration ofSeconds(long seconds)
     * - static Duration ofMillis(long millis)
     * - static Duration ofNanos(long nanos)
     * - static Duration of(long amount, TemporalUnit unit)
     * - static Duration between(Temporal start, Temporal end)
     */
    private static void durationBasics() {
        System.out.println("=== DURATION BASICS ===");
        System.out.println("Duration = Time-based (hours, minutes, seconds, nanos)");
        System.out.println("Works with: LocalTime, LocalDateTime, ZonedDateTime\n");

        // Creating durations
        Duration d1 = Duration.ofDays(1);
        Duration d2 = Duration.ofHours(2);
        Duration d3 = Duration.ofMinutes(30);
        Duration d4 = Duration.ofSeconds(45);

        System.out.println("Duration.ofDays(1) = " + d1);         // PT24H
        System.out.println("Duration.ofHours(2) = " + d2);        // PT2H
        System.out.println("Duration.ofMinutes(30) = " + d3);     // PT30M
        System.out.println("Duration.ofSeconds(45) = " + d4);     // PT45S

        // Combining - use of() method
        Duration d5 = Duration.ofHours(1).plusMinutes(30);
        System.out.println("\nDuration.ofHours(1).plusMinutes(30) = " + d5); // PT1H30M

        // Accessing components
        System.out.println("\n--- Duration Methods ---");
        Duration d = Duration.ofHours(2).plusMinutes(30).plusSeconds(45);
        System.out.println("Duration: " + d);
        System.out.println("  toHours() = " + d.toHours());           // 2 (total hours)
        System.out.println("  toMinutes() = " + d.toMinutes());       // 150 (total minutes)
        System.out.println("  toSeconds() = " + d.toSeconds());       // 9045 (total seconds)
        System.out.println("  getSeconds() = " + d.getSeconds());     // 9045 (total seconds)
        System.out.println("  getNano() = " + d.getNano());           // 0 (nano component)

        System.out.println();
    }

    /**
     * DURATION CREATION VARIATIONS
     */
    private static void durationCreation() {
        System.out.println("=== DURATION CREATION VARIATIONS ===");

        // Various units
        Duration d1 = Duration.ofDays(1);
        Duration d2 = Duration.ofHours(24);
        System.out.println("Duration.ofDays(1) = " + d1);
        System.out.println("Duration.ofHours(24) = " + d2);
        System.out.println("Are they equal? " + d1.equals(d2));  // true

        // Negative duration
        Duration d3 = Duration.ofHours(-2);
        System.out.println("\nDuration.ofHours(-2) = " + d3);    // PT-2H

        // between() method
        System.out.println("\n--- Duration.between() ---");
        LocalTime start = LocalTime.of(10, 30);
        LocalTime end = LocalTime.of(14, 45);
        Duration between = Duration.between(start, end);
        System.out.println("Between " + start + " and " + end + ": " + between);

        System.out.println();
    }

    /**
     * USING DURATION WITH TIMES
     */
    private static void durationWithTimes() {
        System.out.println("=== USING DURATION WITH TIMES ===");

        LocalTime time = LocalTime.of(10, 30);
        System.out.println("Starting time: " + time);

        // Adding duration
        Duration d = Duration.ofHours(2).plusMinutes(30);
        LocalTime result = time.plus(d);
        System.out.println("Add " + d + ": " + result);           // 13:00

        // Subtracting duration
        LocalTime result2 = time.minus(d);
        System.out.println("Subtract " + d + ": " + result2);     // 08:00

        System.out.println();
    }

    /**
     * DURATION INVALID USAGE - EXAM TRAP!
     *
     * *** CRITICAL ***
     * Duration with LocalDate throws UnsupportedTemporalTypeException!
     */
    private static void durationInvalidUsage() {
        System.out.println("=== DURATION INVALID USAGE - EXAM TRAP! ===");
        System.out.println("*** Duration DOES NOT work with LocalDate! ***\n");

        LocalDate date = LocalDate.of(2024, 1, 15);
        Duration d = Duration.ofHours(24);

        System.out.println("LocalDate: " + date);
        System.out.println("Duration: " + d);
        System.out.println("\nTrying date.plus(duration)...");

        try {
            LocalDate result = date.plus(d);
            System.out.println("Result: " + result);  // This line won't execute
        } catch (UnsupportedTemporalTypeException e) {
            System.out.println("✗ EXCEPTION: " + e.getClass().getSimpleName());
            System.out.println("  Message: Unsupported unit: " + e.getMessage());
            System.out.println("\n  → Duration cannot be added to LocalDate!");
            System.out.println("  → Use Period instead for date-based operations");
        }

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * INSTANT BASICS
     * ═══════════════════════════════════════════════════════════════════════
     *
     * Instant represents a point in time on the timeline (epoch-based)
     *
     * KEY CHARACTERISTICS:
     * - Represents a specific moment in time (timestamp)
     * - Always in UTC (no timezone information)
     * - Based on Unix epoch: January 1, 1970, 00:00:00 UTC
     * - Stored as seconds + nanoseconds since epoch
     * - Cannot access date/time components directly (no getYear(), getMonth(), etc.)
     * - Must convert to ZonedDateTime to access components
     *
     * KEY SIGNATURES:
     * - static Instant now()
     * - static Instant ofEpochSecond(long epochSecond)
     * - static Instant ofEpochMilli(long epochMilli)
     * - static Instant parse(CharSequence text)
     * - long getEpochSecond()
     * - int getNano()
     *
     * EXAM TRAPS:
     * - Cannot use Period with Instant (throws exception)
     * - CAN use Duration with Instant
     * - No direct access to date/time fields
     * - Always UTC, no timezone
     */
    private static void instantBasics() {
        System.out.println("=== INSTANT BASICS ===");
        System.out.println("Instant = Point in time (timestamp in UTC)");
        System.out.println("Based on Unix epoch: 1970-01-01T00:00:00Z\n");

        // Creating Instant
        Instant now = Instant.now();
        System.out.println("Instant.now() = " + now);
        System.out.println("  Format: <date>T<time>Z (Z means UTC)");

        // Getting epoch values
        System.out.println("\n--- Epoch Values ---");
        System.out.println("getEpochSecond() = " + now.getEpochSecond());
        System.out.println("  (seconds since 1970-01-01 00:00:00 UTC)");
        System.out.println("getNano() = " + now.getNano());
        System.out.println("  (nanosecond component, 0-999,999,999)");

        // Cannot access date/time components directly
        System.out.println("\n--- EXAM TRAP: No Direct Field Access ---");
        System.out.println("Instant has NO methods like:");
        System.out.println("  × getYear()");
        System.out.println("  × getMonth()");
        System.out.println("  × getDayOfMonth()");
        System.out.println("  × getHour()");
        System.out.println("Must convert to ZonedDateTime to access these!");

        System.out.println();
    }

    /**
     * INSTANT CREATION METHODS
     *
     * Various ways to create Instant objects
     */
    private static void instantCreation() {
        System.out.println("=== INSTANT CREATION METHODS ===");

        // now() - current moment
        Instant now = Instant.now();
        System.out.println("Instant.now() = " + now);

        // ofEpochSecond() - from epoch seconds
        System.out.println("\n--- ofEpochSecond() ---");
        Instant epoch = Instant.ofEpochSecond(0);
        System.out.println("Instant.ofEpochSecond(0) = " + epoch);
        System.out.println("  → 1970-01-01T00:00:00Z (Unix epoch)");

        Instant later = Instant.ofEpochSecond(1000000000);
        System.out.println("Instant.ofEpochSecond(1000000000) = " + later);

        Instant past = Instant.ofEpochSecond(-3600);
        System.out.println("Instant.ofEpochSecond(-3600) = " + past);
        System.out.println("  → 1 hour before epoch (negative values allowed)");

        // ofEpochMilli() - from epoch milliseconds
        System.out.println("\n--- ofEpochMilli() ---");
        Instant millis = Instant.ofEpochMilli(1000);
        System.out.println("Instant.ofEpochMilli(1000) = " + millis);
        System.out.println("  → 1 second after epoch (1000 ms = 1 sec)");

        // parse() - from ISO-8601 string
        System.out.println("\n--- parse() ---");
        Instant parsed = Instant.parse("2024-01-15T10:30:00Z");
        System.out.println("Instant.parse(\"2024-01-15T10:30:00Z\") = " + parsed);

        // EXAM TRAP: Must end with Z (UTC)
        System.out.println("\n--- EXAM TRAP: Must use UTC format ---");
        System.out.println("Valid:   \"2024-01-15T10:30:00Z\" (ends with Z)");
        System.out.println("Invalid: \"2024-01-15T10:30:00\" (no Z)");
        System.out.println("Invalid: \"2024-01-15T10:30:00-05:00\" (has offset)");
        System.out.println("  → parse() requires UTC format with Z");

        System.out.println();
    }

    /**
     * INSTANT WITH PERIOD AND DURATION - CRITICAL EXAM TRAP!
     *
     * *** MOST IMPORTANT ***
     * - Period does NOT work with Instant (throws exception)
     * - Duration DOES work with Instant
     *
     * WHY?
     * Period = date-based (years, months, days)
     * Instant = precise point in time (no concept of calendar days)
     * Duration = time-based (seconds, nanos) - works fine!
     */
    private static void instantWithPeriodAndDuration() {
        System.out.println("=== INSTANT WITH PERIOD AND DURATION ===");
        System.out.println("*** CRITICAL EXAM TRAP ***\n");

        Instant instant = Instant.parse("2024-01-15T10:30:00Z");
        System.out.println("Starting instant: " + instant);

        // Duration works fine!
        System.out.println("\n--- Duration WORKS with Instant ✓ ---");
        Duration duration = Duration.ofHours(2);
        Instant result1 = instant.plus(duration);
        System.out.println("instant.plus(Duration.ofHours(2)) = " + result1);

        Instant result2 = instant.minus(Duration.ofMinutes(30));
        System.out.println("instant.minus(Duration.ofMinutes(30)) = " + result2);

        // Can also use specific methods
        Instant result3 = instant.plusSeconds(3600);
        System.out.println("instant.plusSeconds(3600) = " + result3);

        // Period does NOT work! (for month/year based periods)
        System.out.println("\n--- Period with Months/Years DOES NOT work with Instant ✗ ---");

        // Note: Period.ofDays() works because it converts to hours
        System.out.println("SPECIAL NOTE: Period.ofDays() actually works (converts to Duration)");
        Instant dayLater = instant.plus(Period.ofDays(1));
        System.out.println("instant.plus(Period.ofDays(1)) = " + dayLater + " ✓");

        // But Period with months or years fails!
        System.out.println("\nBUT Period.ofMonths() or Period.ofYears() throws exception:");
        Period period = Period.ofMonths(1);
        System.out.println("Trying instant.plus(Period.ofMonths(1))...");

        try {
            Instant result = instant.plus(period);
            System.out.println("Result: " + result);  // This line won't execute
        } catch (UnsupportedTemporalTypeException e) {
            System.out.println("✗ EXCEPTION: " + e.getClass().getSimpleName());
            System.out.println("  Message: " + e.getMessage());
            System.out.println("\n  → Period with months/years cannot be added to Instant!");
            System.out.println("  → Instant is time-based, Period is date-based");
            System.out.println("  → Use Duration instead, or convert to ZonedDateTime first");
        }

        // EXAM TIP: Why Period doesn't work
        System.out.println("\n--- WHY Period Doesn't Work ---");
        System.out.println("Explanation:");
        System.out.println("  • Instant = precise moment (epoch seconds)");
        System.out.println("  • Period = calendar-based (1 month = 28-31 days)");
        System.out.println("  • Calendar concepts don't apply to Instant");
        System.out.println("  • Must convert to ZonedDateTime to use Period");

        System.out.println();
    }

    /**
     * INSTANT CONVERSIONS - VERY COMMON EXAM TOPIC
     *
     * Converting between Instant and other date/time types
     *
     * KEY CONVERSIONS:
     * - Instant → ZonedDateTime (need timezone)
     * - ZonedDateTime → Instant
     * - Instant → LocalDateTime (need timezone)
     * - Cannot convert directly: Instant ↔ LocalDate or LocalTime
     */
    private static void instantConversions() {
        System.out.println("=== INSTANT CONVERSIONS ===");
        System.out.println("*** VERY COMMON EXAM QUESTIONS ***\n");

        Instant instant = Instant.parse("2024-01-15T15:30:00Z");
        System.out.println("Starting instant: " + instant);
        System.out.println("  (This is UTC time)\n");

        // Instant → ZonedDateTime (most common)
        System.out.println("--- Instant → ZonedDateTime ---");
        ZoneId nyZone = ZoneId.of("America/New_York");
        ZonedDateTime zdt = instant.atZone(nyZone);
        System.out.println("instant.atZone(America/New_York) = " + zdt);
        System.out.println("  → " + zdt.toLocalDateTime() + " (New York time)");
        System.out.println("  → Note: 15:30 UTC = 10:30 EST (offset -05:00)");

        ZoneId tokyoZone = ZoneId.of("Asia/Tokyo");
        ZonedDateTime zdtTokyo = instant.atZone(tokyoZone);
        System.out.println("\ninstant.atZone(Asia/Tokyo) = " + zdtTokyo);
        System.out.println("  → " + zdtTokyo.toLocalDateTime() + " (Tokyo time)");
        System.out.println("  → Note: 15:30 UTC = 00:30 JST next day (offset +09:00)");

        // ZonedDateTime → Instant
        System.out.println("\n--- ZonedDateTime → Instant ---");
        ZonedDateTime now = ZonedDateTime.now();
        Instant fromZdt = now.toInstant();
        System.out.println("ZonedDateTime: " + now);
        System.out.println("toInstant():   " + fromZdt);
        System.out.println("  → Converts to UTC");

        // Instant → LocalDateTime (need timezone)
        System.out.println("\n--- Instant → LocalDateTime ---");
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, nyZone);
        System.out.println("LocalDateTime.ofInstant(instant, America/New_York) = " + ldt);
        System.out.println("  → Gets local date/time for that timezone");
        System.out.println("  → NOTE: LocalDateTime has no timezone info!");

        // Cannot convert directly to LocalDate or LocalTime
        System.out.println("\n--- EXAM TRAP: Cannot Convert Directly ---");
        System.out.println("✗ Instant → LocalDate (directly) - NO METHOD");
        System.out.println("✗ Instant → LocalTime (directly) - NO METHOD");
        System.out.println("\n✓ Must go through ZonedDateTime or LocalDateTime:");
        LocalDate date = instant.atZone(nyZone).toLocalDate();
        LocalTime time = instant.atZone(nyZone).toLocalTime();
        System.out.println("instant.atZone(zone).toLocalDate() = " + date);
        System.out.println("instant.atZone(zone).toLocalTime() = " + time);

        // LocalDateTime → Instant (need timezone)
        System.out.println("\n--- LocalDateTime → Instant ---");
        LocalDateTime localDt = LocalDateTime.of(2024, 1, 15, 10, 30);
        System.out.println("LocalDateTime: " + localDt + " (no timezone!)");
        // Can't convert directly - need timezone
        Instant fromLocal = localDt.atZone(nyZone).toInstant();
        System.out.println("localDt.atZone(America/New_York).toInstant() = " + fromLocal);
        System.out.println("  → Interprets as New York time, converts to UTC");

        System.out.println("\n*** EXAM TIP: Instant is always UTC, need timezone for conversions! ***");
        System.out.println();
    }

    /**
     * INSTANT COMPARISONS AND OPERATIONS
     *
     * Comparing Instants and performing calculations
     */
    private static void instantComparisons() {
        System.out.println("=== INSTANT COMPARISONS ===");

        Instant instant1 = Instant.parse("2024-01-15T10:00:00Z");
        Instant instant2 = Instant.parse("2024-01-15T14:00:00Z");
        Instant instant3 = Instant.parse("2024-01-15T10:00:00Z");

        System.out.println("instant1: " + instant1);
        System.out.println("instant2: " + instant2);
        System.out.println("instant3: " + instant3);

        // equals()
        System.out.println("\n--- equals() ---");
        System.out.println("instant1.equals(instant2) = " + instant1.equals(instant2)); // false
        System.out.println("instant1.equals(instant3) = " + instant1.equals(instant3)); // true

        // compareTo()
        System.out.println("\n--- compareTo() ---");
        System.out.println("instant1.compareTo(instant2) = " + instant1.compareTo(instant2)); // negative
        System.out.println("instant2.compareTo(instant1) = " + instant2.compareTo(instant1)); // positive
        System.out.println("instant1.compareTo(instant3) = " + instant1.compareTo(instant3)); // 0

        // isBefore(), isAfter()
        System.out.println("\n--- isBefore() / isAfter() ---");
        System.out.println("instant1.isBefore(instant2) = " + instant1.isBefore(instant2)); // true
        System.out.println("instant1.isAfter(instant2) = " + instant1.isAfter(instant2));   // false
        System.out.println("instant2.isAfter(instant1) = " + instant2.isAfter(instant1));   // true

        // Duration.between()
        System.out.println("\n--- Duration.between() ---");
        Duration between = Duration.between(instant1, instant2);
        System.out.println("Duration.between(instant1, instant2) = " + between);
        System.out.println("  Hours: " + between.toHours());

        // EXAM TIP: Period.between() doesn't work with Instant
        System.out.println("\n--- EXAM TRAP: Period.between() ---");
        System.out.println("Period.between() requires LocalDate (not Instant)");
        System.out.println("✗ Period.between(instant1, instant2) - DOES NOT COMPILE");
        System.out.println("✓ Period.between(localDate1, localDate2) - OK");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * DAYLIGHT SAVINGS TIME - SPRING FORWARD BASICS
     * ═══════════════════════════════════════════════════════════════════════
     *
     * SPRING FORWARD (2nd Sunday in March in US)
     * - Clocks jump from 2:00 AM → 3:00 AM
     * - We "lose" an hour
     * - Offset changes from UTC-5 (EST) to UTC-4 (EDT)
     *
     * Example: March 10, 2024
     */
    private static void dstSpringForwardBasics() {
        System.out.println("=== DST SPRING FORWARD BASICS ===");
        System.out.println("Spring Forward: 2nd Sunday in March");
        System.out.println("Clocks jump from 2:00 AM → 3:00 AM");
        System.out.println("We LOSE 1 hour\n");

        ZoneId zone = ZoneId.of("America/New_York");

        // Day before DST (March 9, 2024) - normal 24-hour day
        System.out.println("--- Day BEFORE Spring Forward (March 9, 2024) ---");
        ZonedDateTime before = ZonedDateTime.of(2024, 3, 9, 10, 0, 0, 0, zone);
        System.out.println("March 9 at 10:00 AM: " + before);
        System.out.println("  Offset: " + before.getOffset());  // -05:00 (EST)

        // Day OF DST (March 10, 2024) - only 23 hours!
        System.out.println("\n--- Day OF Spring Forward (March 10, 2024) ---");
        ZonedDateTime springForward = ZonedDateTime.of(2024, 3, 10, 1, 30, 0, 0, zone);
        System.out.println("March 10 at 1:30 AM (before DST): " + springForward);
        System.out.println("  Offset: " + springForward.getOffset());  // -05:00 (EST)

        ZonedDateTime after = ZonedDateTime.of(2024, 3, 10, 3, 30, 0, 0, zone);
        System.out.println("\nMarch 10 at 3:30 AM (after DST): " + after);
        System.out.println("  Offset: " + after.getOffset());  // -04:00 (EDT)

        System.out.println("\n*** Note: 2:00-2:59 AM DO NOT EXIST on March 10! ***");

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * DST SPRING FORWARD - INVALID TIME HANDLING
     * ═══════════════════════════════════════════════════════════════════════
     *
     * *** MOST IMPORTANT EXAM TRAP ***
     *
     * What happens when you create a time that doesn't exist?
     * - Times 2:00-2:59 AM don't exist on Spring Forward day
     * - Java AUTOMATICALLY ADJUSTS to 3:00-3:59 AM!
     *
     * WALKTHROUGH:
     * You try to create 2:30 AM on March 10, 2024
     * → Java knows this time doesn't exist (DST gap)
     * → Java moves it forward 1 hour to 3:30 AM
     * → Offset is EDT (UTC-4), not EST (UTC-5)
     */
    private static void dstSpringForwardInvalidTime() {
        System.out.println("=== DST SPRING FORWARD - INVALID TIME ===");
        System.out.println("*** CRITICAL EXAM KNOWLEDGE ***\n");

        System.out.println("SCENARIO: Creating a time that doesn't exist");
        System.out.println("March 10, 2024 - Spring Forward day");
        System.out.println("Times 2:00-2:59 AM do NOT exist\n");

        ZoneId zone = ZoneId.of("America/New_York");

        // Try to create 2:30 AM (doesn't exist!)
        System.out.println("STEP 1: Try to create 2:30 AM");
        System.out.println("  ZonedDateTime.of(2024, 3, 10, 2, 30, 0, 0, zone)");

        ZonedDateTime invalid = ZonedDateTime.of(2024, 3, 10, 2, 30, 0, 0, zone);

        System.out.println("\nSTEP 2: Java adjusts the time");
        System.out.println("  Result: " + invalid);
        System.out.println("  → Notice the time is 3:30 AM, NOT 2:30 AM!");
        System.out.println("  → Offset is " + invalid.getOffset() + " (EDT, not EST)");

        System.out.println("\nEXPLANATION:");
        System.out.println("  • You asked for 2:30 AM");
        System.out.println("  • This time doesn't exist (DST gap)");
        System.out.println("  • Java automatically moves it forward 1 hour");
        System.out.println("  • Result: 3:30 AM EDT");

        // More examples
        System.out.println("\n--- More Examples ---");
        ZonedDateTime t1 = ZonedDateTime.of(2024, 3, 10, 2, 0, 0, 0, zone);
        ZonedDateTime t2 = ZonedDateTime.of(2024, 3, 10, 2, 15, 0, 0, zone);
        ZonedDateTime t3 = ZonedDateTime.of(2024, 3, 10, 2, 45, 0, 0, zone);
        ZonedDateTime t4 = ZonedDateTime.of(2024, 3, 10, 2, 59, 0, 0, zone);

        System.out.println("Request 2:00 AM → Get: " + t1.toLocalTime());  // 3:00 AM
        System.out.println("Request 2:15 AM → Get: " + t2.toLocalTime());  // 3:15 AM
        System.out.println("Request 2:45 AM → Get: " + t3.toLocalTime());  // 3:45 AM
        System.out.println("Request 2:59 AM → Get: " + t4.toLocalTime());  // 3:59 AM

        System.out.println("\n*** EXAM TIP: Invalid times are ADJUSTED, not exception! ***");
        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * DST SPRING FORWARD - ADDING TIME
     * ═══════════════════════════════════════════════════════════════════════
     *
     * WALKTHROUGH: What happens when adding time crosses DST boundary?
     *
     * SCENARIO: Start at 1:30 AM, add 1 hour
     * Expected: 2:30 AM
     * Reality: Clock jumps at 2:00 AM, so result is 3:30 AM!
     *
     * WHY?
     * - 1:30 AM + 1 hour = 2:30 AM (mathematically)
     * - But 2:30 AM doesn't exist (DST gap)
     * - Clock jumps from 2:00 AM → 3:00 AM
     * - So we land at 3:30 AM
     */
    private static void dstSpringForwardAddingTime() {
        System.out.println("=== DST SPRING FORWARD - ADDING TIME ===");
        System.out.println("*** VERY COMMON EXAM QUESTION ***\n");

        System.out.println("SCENARIO: Adding time that crosses DST boundary");
        System.out.println("March 10, 2024 - Spring Forward day\n");

        ZoneId zone = ZoneId.of("America/New_York");

        // Start before DST
        ZonedDateTime start = ZonedDateTime.of(2024, 3, 10, 1, 30, 0, 0, zone);
        System.out.println("STEP 1: Start time");
        System.out.println("  " + start);
        System.out.println("  Time: " + start.toLocalTime());
        System.out.println("  Offset: " + start.getOffset() + " (EST)");

        // Add 1 hour
        System.out.println("\nSTEP 2: Add 1 hour");
        System.out.println("  start.plusHours(1)");
        System.out.println("  Expected: 2:30 AM");
        System.out.println("  But 2:30 AM doesn't exist!");

        ZonedDateTime result = start.plusHours(1);

        System.out.println("\nSTEP 3: Result");
        System.out.println("  " + result);
        System.out.println("  Time: " + result.toLocalTime());
        System.out.println("  → Time is 3:30 AM, NOT 2:30 AM!");
        System.out.println("  Offset: " + result.getOffset() + " (EDT)");

        System.out.println("\nEXPLANATION:");
        System.out.println("  • Started at 1:30 AM EST");
        System.out.println("  • Added 1 hour → should be 2:30 AM");
        System.out.println("  • But at 2:00 AM, clocks jump to 3:00 AM");
        System.out.println("  • So 1:30 AM + 1 hour = 3:30 AM EDT");
        System.out.println("  • We 'skipped over' the 2:00-3:00 hour");

        // The day is only 23 hours long!
        System.out.println("\n--- The Day is Only 23 Hours! ---");
        ZonedDateTime midnight = ZonedDateTime.of(2024, 3, 10, 0, 0, 0, 0, zone);
        ZonedDateTime nextMidnight = midnight.plusDays(1);
        Duration dayLength = Duration.between(midnight, nextMidnight);
        System.out.println("March 10 midnight to March 11 midnight:");
        System.out.println("  Duration: " + dayLength);
        System.out.println("  Hours: " + dayLength.toHours());  // 23 hours!

        System.out.println("\n*** EXAM TIP: Adding hours across DST = unexpected time! ***");
        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * DAYLIGHT SAVINGS TIME - FALL BACK BASICS
     * ═══════════════════════════════════════════════════════════════════════
     *
     * FALL BACK (1st Sunday in November in US)
     * - Clocks jump from 2:00 AM → 1:00 AM
     * - We "gain" an hour
     * - Offset changes from UTC-4 (EDT) to UTC-5 (EST)
     * - Times 1:00-1:59 AM occur TWICE!
     *
     * Example: November 3, 2024
     */
    private static void dstFallBackBasics() {
        System.out.println("=== DST FALL BACK BASICS ===");
        System.out.println("Fall Back: 1st Sunday in November");
        System.out.println("Clocks jump from 2:00 AM → 1:00 AM");
        System.out.println("We GAIN 1 hour");
        System.out.println("Times 1:00-1:59 AM occur TWICE!\n");

        ZoneId zone = ZoneId.of("America/New_York");

        // Day before DST (November 2, 2024) - normal 24-hour day
        System.out.println("--- Day BEFORE Fall Back (November 2, 2024) ---");
        ZonedDateTime before = ZonedDateTime.of(2024, 11, 2, 10, 0, 0, 0, zone);
        System.out.println("November 2 at 10:00 AM: " + before);
        System.out.println("  Offset: " + before.getOffset());  // -04:00 (EDT)

        // Day OF DST (November 3, 2024) - 25 hours!
        System.out.println("\n--- Day OF Fall Back (November 3, 2024) ---");
        ZonedDateTime fallBack = ZonedDateTime.of(2024, 11, 3, 0, 30, 0, 0, zone);
        System.out.println("November 3 at 0:30 AM: " + fallBack);
        System.out.println("  Offset: " + fallBack.getOffset());  // -04:00 (EDT still)

        // The day is 25 hours long!
        ZonedDateTime midnight = ZonedDateTime.of(2024, 11, 3, 0, 0, 0, 0, zone);
        ZonedDateTime nextMidnight = midnight.plusDays(1);
        Duration dayLength = Duration.between(midnight, nextMidnight);
        System.out.println("\nNovember 3 midnight to November 4 midnight:");
        System.out.println("  Duration: " + dayLength);
        System.out.println("  Hours: " + dayLength.toHours());  // 25 hours!

        System.out.println("\n*** Note: 1:00-1:59 AM occur TWICE on November 3! ***");
        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * DST FALL BACK - AMBIGUOUS TIME HANDLING
     * ═══════════════════════════════════════════════════════════════════════
     *
     * *** VERY TRICKY EXAM TRAP ***
     *
     * What happens when you create a time that exists TWICE?
     * - Times 1:00-1:59 AM occur twice on Fall Back day
     * - First occurrence: EDT (UTC-4)
     * - Second occurrence: EST (UTC-5)
     *
     * WALKTHROUGH:
     * You create 1:30 AM on November 3, 2024
     * → This time exists twice!
     * → Java defaults to FIRST occurrence (EDT, before the change)
     * → To get second occurrence, need to specify offset or add time
     */
    private static void dstFallBackAmbiguousTime() {
        System.out.println("=== DST FALL BACK - AMBIGUOUS TIME ===");
        System.out.println("*** CRITICAL EXAM KNOWLEDGE ***\n");

        System.out.println("SCENARIO: Creating a time that exists TWICE");
        System.out.println("November 3, 2024 - Fall Back day");
        System.out.println("Times 1:00-1:59 AM occur twice\n");

        ZoneId zone = ZoneId.of("America/New_York");

        // Create 1:30 AM (ambiguous!)
        System.out.println("STEP 1: Create 1:30 AM");
        System.out.println("  ZonedDateTime.of(2024, 11, 3, 1, 30, 0, 0, zone)");

        ZonedDateTime ambiguous = ZonedDateTime.of(2024, 11, 3, 1, 30, 0, 0, zone);

        System.out.println("\nSTEP 2: Java uses FIRST occurrence");
        System.out.println("  Result: " + ambiguous);
        System.out.println("  Time: " + ambiguous.toLocalTime());
        System.out.println("  Offset: " + ambiguous.getOffset() + " (EDT - before change)");

        System.out.println("\nEXPLANATION:");
        System.out.println("  • You asked for 1:30 AM");
        System.out.println("  • This time occurs twice!");
        System.out.println("  • Java defaults to FIRST occurrence (EDT)");
        System.out.println("  • This is 1:30 AM BEFORE the clocks fall back");

        // To get SECOND occurrence, specify offset
        System.out.println("\n--- Getting the SECOND occurrence ---");
        System.out.println("STEP 3: Specify EST offset to get second occurrence");

        ZonedDateTime second = ZonedDateTime.of(
            LocalDateTime.of(2024, 11, 3, 1, 30),
            zone
        ).withEarlierOffsetAtOverlap(); // Use method to get earlier offset (EST)

        // Actually, let's create it properly
        LocalDateTime ldt = LocalDateTime.of(2024, 11, 3, 1, 30);
        ZonedDateTime first = ZonedDateTime.of(ldt, zone);
        ZonedDateTime secondOccurrence = first.plusHours(1); // This gets us past the overlap

        System.out.println("  First 1:30 AM:  " + first + " (EDT)");
        System.out.println("  One hour later: " + secondOccurrence);

        // Better example - showing both occurrences
        System.out.println("\n--- Timeline Walkthrough ---");
        ZonedDateTime t1 = ZonedDateTime.of(2024, 11, 3, 1, 0, 0, 0, zone);
        System.out.println("1:00 AM (first time):  " + t1 + " " + t1.getOffset());

        ZonedDateTime t2 = t1.plusMinutes(30);
        System.out.println("1:30 AM (first time):  " + t2 + " " + t2.getOffset());

        ZonedDateTime t3 = t2.plusMinutes(30);
        System.out.println("2:00 AM (becomes 1:00): " + t3 + " " + t3.getOffset());
        System.out.println("  → Notice: Still shows 1:00 AM but offset changed!");

        ZonedDateTime t4 = t3.plusMinutes(30);
        System.out.println("1:30 AM (second time): " + t4 + " " + t4.getOffset());

        System.out.println("\n*** EXAM TIP: Default is FIRST occurrence (earlier offset) ***");
        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * DST FALL BACK - ADDING TIME
     * ═══════════════════════════════════════════════════════════════════════
     *
     * WALKTHROUGH: What happens when adding time crosses DST boundary?
     *
     * SCENARIO: Start at 1:30 AM EDT, add 1 hour
     * Expected: 2:30 AM
     * Reality: After 1 hour, clock falls back, so we're at 1:30 AM EST!
     *
     * WHY?
     * - We're at 1:30 AM EDT (first occurrence)
     * - Add 1 hour → clock position reaches 2:30 AM
     * - But at 2:00 AM, clocks fall back to 1:00 AM
     * - So actual time is 1:30 AM EST (second occurrence)
     */
    private static void dstFallBackAddingTime() {
        System.out.println("=== DST FALL BACK - ADDING TIME ===");
        System.out.println("*** VERY COMMON EXAM QUESTION ***\n");

        System.out.println("SCENARIO: Adding time that crosses DST boundary");
        System.out.println("November 3, 2024 - Fall Back day\n");

        ZoneId zone = ZoneId.of("America/New_York");

        // Start at first occurrence of 1:30 AM
        ZonedDateTime start = ZonedDateTime.of(2024, 11, 3, 1, 30, 0, 0, zone);
        System.out.println("STEP 1: Start time (first 1:30 AM)");
        System.out.println("  " + start);
        System.out.println("  Time: " + start.toLocalTime());
        System.out.println("  Offset: " + start.getOffset() + " (EDT)");

        // Add 1 hour
        System.out.println("\nSTEP 2: Add 1 hour");
        System.out.println("  start.plusHours(1)");
        System.out.println("  Expected: 2:30 AM");
        System.out.println("  But clocks fall back at 2:00 AM!");

        ZonedDateTime result = start.plusHours(1);

        System.out.println("\nSTEP 3: Result");
        System.out.println("  " + result);
        System.out.println("  Time: " + result.toLocalTime());
        System.out.println("  → Time is 1:30 AM again!");
        System.out.println("  Offset: " + result.getOffset() + " (EST)");

        System.out.println("\nEXPLANATION:");
        System.out.println("  • Started at 1:30 AM EDT (first occurrence)");
        System.out.println("  • Added 1 hour of real time");
        System.out.println("  • At 2:00 AM, clocks fell back to 1:00 AM");
        System.out.println("  • So 1:30 AM EDT + 1 hour = 1:30 AM EST");
        System.out.println("  • Same clock time, but different offset!");

        // Showing the full sequence
        System.out.println("\n--- Full Sequence ---");
        ZonedDateTime t1 = ZonedDateTime.of(2024, 11, 3, 1, 0, 0, 0, zone);
        System.out.println("Start: 1:00 AM " + t1.getOffset());

        for (int i = 1; i <= 4; i++) {
            t1 = t1.plusMinutes(30);
            System.out.println("+" + (i * 30) + " min: " + t1.toLocalTime() + " " + t1.getOffset());
        }

        System.out.println("\n*** EXAM TIP: Adding hours gives same clock time but different offset! ***");
        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * DST - PERIOD VS DURATION
     * ═══════════════════════════════════════════════════════════════════════
     *
     * *** CRITICAL EXAM CONCEPT ***
     *
     * Period = Date-based (adds calendar days)
     * Duration = Time-based (adds actual hours)
     *
     * DIFFERENCE MATTERS WITH DST!
     *
     * WALKTHROUGH:
     * Spring Forward (March 10, 2024 has only 23 hours)
     *
     * Adding Period.ofDays(1):
     * - Adds 1 calendar day
     * - March 10 at 1:00 AM + 1 day = March 11 at 1:00 AM
     * - Same wall clock time, even though day was only 23 hours
     *
     * Adding Duration.ofHours(24):
     * - Adds 24 actual hours
     * - March 10 at 1:00 AM + 24 hours = March 11 at 2:00 AM
     * - Different wall clock time because we added real time
     */
    private static void dstPeriodVsDuration() {
        System.out.println("=== DST - PERIOD VS DURATION ===");
        System.out.println("*** MOST IMPORTANT DST CONCEPT FOR EXAM ***\n");

        System.out.println("CRITICAL DIFFERENCE:");
        System.out.println("  Period = Calendar-based (adds calendar days)");
        System.out.println("  Duration = Time-based (adds actual hours)\n");

        ZoneId zone = ZoneId.of("America/New_York");

        // SPRING FORWARD SCENARIO
        System.out.println("=== SPRING FORWARD - March 10, 2024 ===");
        System.out.println("(Day has only 23 hours due to DST)\n");

        ZonedDateTime springStart = ZonedDateTime.of(2024, 3, 10, 1, 0, 0, 0, zone);
        System.out.println("START: " + springStart);
        System.out.println("  " + springStart.toLocalDateTime());

        // Add Period.ofDays(1)
        System.out.println("\nSCENARIO 1: Add Period.ofDays(1)");
        ZonedDateTime periodResult = springStart.plus(Period.ofDays(1));
        System.out.println("  Result: " + periodResult);
        System.out.println("  " + periodResult.toLocalDateTime());
        System.out.println("  → Same time (1:00 AM), next calendar day");

        // Add Duration.ofHours(24)
        System.out.println("\nSCENARIO 2: Add Duration.ofHours(24)");
        ZonedDateTime durationResult = springStart.plus(Duration.ofHours(24));
        System.out.println("  Result: " + durationResult);
        System.out.println("  " + durationResult.toLocalDateTime());
        System.out.println("  → Different time (2:00 AM), 24 actual hours later");

        System.out.println("\nWHY THE DIFFERENCE?");
        System.out.println("  • March 10 only has 23 hours (2:00 AM doesn't exist)");
        System.out.println("  • Period.ofDays(1): Adds 1 calendar day");
        System.out.println("    → March 10 → March 11, keep same time (1:00 AM)");
        System.out.println("  • Duration.ofHours(24): Adds 24 actual hours");
        System.out.println("    → 1:00 AM + 24 real hours = 2:00 AM next day");
        System.out.println("    → (because 1 calendar day was only 23 hours)");

        // FALL BACK SCENARIO
        System.out.println("\n=== FALL BACK - November 3, 2024 ===");
        System.out.println("(Day has 25 hours due to DST)\n");

        ZonedDateTime fallStart = ZonedDateTime.of(2024, 11, 3, 1, 0, 0, 0, zone);
        System.out.println("START: " + fallStart);
        System.out.println("  " + fallStart.toLocalDateTime());

        // Add Period.ofDays(1)
        System.out.println("\nSCENARIO 1: Add Period.ofDays(1)");
        ZonedDateTime periodResult2 = fallStart.plus(Period.ofDays(1));
        System.out.println("  Result: " + periodResult2);
        System.out.println("  " + periodResult2.toLocalDateTime());
        System.out.println("  → Same time (1:00 AM), next calendar day");

        // Add Duration.ofHours(24)
        System.out.println("\nSCENARIO 2: Add Duration.ofHours(24)");
        ZonedDateTime durationResult2 = fallStart.plus(Duration.ofHours(24));
        System.out.println("  Result: " + durationResult2);
        System.out.println("  " + durationResult2.toLocalDateTime());
        System.out.println("  → Different time (12:00 AM), 24 actual hours later");

        System.out.println("\nWHY THE DIFFERENCE?");
        System.out.println("  • November 3 has 25 hours (1:00-2:00 AM repeats)");
        System.out.println("  • Period.ofDays(1): Adds 1 calendar day");
        System.out.println("    → November 3 → November 4, keep same time (1:00 AM)");
        System.out.println("  • Duration.ofHours(24): Adds 24 actual hours");
        System.out.println("    → 1:00 AM + 24 real hours = 12:00 AM (midnight) next day");
        System.out.println("    → (because 1 calendar day was 25 hours)");

        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║ *** EXAM GOLDEN RULE ***                                      ║");
        System.out.println("║                                                               ║");
        System.out.println("║ Period = Calendar time (preserves clock time across DST)     ║");
        System.out.println("║ Duration = Real time (adds actual hours)                     ║");
        System.out.println("║                                                               ║");
        System.out.println("║ On DST days:                                                  ║");
        System.out.println("║ • Period.ofDays(1) ≠ Duration.ofHours(24)                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
}
