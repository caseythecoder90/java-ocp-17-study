package ch01buildingblocks;

/**
 * TEXT BLOCKS - Complete Guide for OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * RULE 1: Opening """ MUST be followed immediately by a line terminator
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * VALID:
 *   String text = """
 *                 content here
 *                 """;
 *
 * INVALID (DOES NOT COMPILE):
 *   String text = """content here
 *                 """;
 *
 * The opening """ CANNOT have any text after it on the same line!
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * RULE 2: INCIDENTAL vs ESSENTIAL Whitespace
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * VERTICAL LINE CONCEPT:
 * - Find the leftmost position of content across ALL lines
 * - Draw an imaginary vertical line at that position
 * - Everything LEFT of that line = INCIDENTAL whitespace (removed)
 * - Everything RIGHT of that line = ESSENTIAL whitespace (kept)
 * - The closing """ position also affects where the vertical line is!
 *
 * Example visualization:
 *
 *         String s = """
 *         ····|Hello
 *         ····|··World
 *         ····|""";
 *              ^
 *              Vertical line is here (at position of """)
 *
 * Result: "Hello\n  World\n"
 *         - The 4 spaces before "Hello" are INCIDENTAL (removed)
 *         - The 2 spaces before "World" are ESSENTIAL (kept)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * RULE 3: Closing """ Position and Trailing Newlines
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * Case A: Closing """ on its OWN line
 *   String s = """
 *              line1
 *              line2
 *              """;
 *   Result: "line1\nline2\n"  (HAS trailing newline)
 *
 * Case B: Closing """ on SAME line as last content
 *   String s = """
 *              line1
 *              line2""";
 *   Result: "line1\nline2"  (NO trailing newline)
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * RULE 4: Escape Sequences in Text Blocks
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * These work the same as in regular strings:
 * - \n  = newline
 * - \t  = tab
 * - \"  = double quote (though you can use " directly in text blocks)
 * - \\  = backslash
 * - \s  = single space (NEW in Java 13+, useful for preserving trailing spaces)
 *
 * Special for text blocks:
 * - \   = line continuation (backslash at end of line removes the newline)
 * - """  = can be used directly (no escape needed for one or two quotes)
 * - \"\"\" = needed if you want three quotes in the text
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * RULE 5: Line Continuation Character (\)
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * A backslash at the END of a line removes the newline character.
 *
 * WITHOUT line continuation:
 *   String s = """
 *              Hello
 *              World""";
 *   Result: "Hello\nWorld"
 *
 * WITH line continuation:
 *   String s = """
 *              Hello\
 *              World""";
 *   Result: "HelloWorld"  (no newline between them)
 */
public class TextBlocksPractice {

    public static void main(String[] args) {
        System.out.println("=== TEXT BLOCKS PRACTICE ===\n");

        // Example 1: Basic text block
        example01_BasicTextBlock();

        // Example 2: Incidental whitespace removal
        example02_IncidentalWhitespace();

        // Example 3: Essential whitespace preservation
        example03_EssentialWhitespace();

        // Example 4: Closing delimiter position
        example04_ClosingDelimiterPosition();

        // Example 5: Escape sequences
        example05_EscapeSequences();

        // Example 6: Line continuation
        example06_LineContinuation();

        // Example 7: Common exam traps
        example07_ExamTraps();

        // Example 8: Practice questions
        example08_PracticeQuestions();
    }

    /**
     * Example 1: Basic Text Block
     */
    private static void example01_BasicTextBlock() {
        System.out.println("--- Example 1: Basic Text Block ---");

        String textBlock = """
                Hello World
                This is a text block
                """;

        System.out.println("Result:");
        System.out.println(textBlock);
        System.out.println("Length: " + textBlock.length());
        System.out.println("Actual content: " + textBlock.replace("\n", "\\n"));
        System.out.println();
    }

    /**
     * Example 2: Incidental Whitespace Removal
     *
     * The vertical line is determined by the LEFTMOST content (including closing """)
     */
    private static void example02_IncidentalWhitespace() {
        System.out.println("--- Example 2: Incidental Whitespace ---");

        // All lines are indented 16 spaces (because of code formatting)
        // But the compiler finds the leftmost content and removes that indentation
        String s1 = """
                Apple
                Banana
                Cherry
                """;

        System.out.println("s1 result:");
        System.out.println(s1);
        System.out.println("s1 as escaped: " + s1.replace("\n", "\\n"));

        // Now watch what happens when we indent the closing """
        // The vertical line moves LEFT because """ is now the leftmost
        String s2 = """
                    Apple
                    Banana
            """;

        System.out.println("\ns2 result (closing \"\"\" less indented):");
        System.out.println(s2);
        System.out.println("s2 as escaped: " + s2.replace("\n", "\\n"));
        System.out.println("Notice the leading spaces before Apple and Banana!");
        System.out.println("Length: " + s2.length());
        System.out.println();
    }

    /**
     * Example 3: Essential Whitespace Preservation
     *
     * Spaces to the RIGHT of the vertical line are kept
     */
    private static void example03_EssentialWhitespace() {
        System.out.println("--- Example 3: Essential Whitespace ---");

        // Vertical line is at position of closing """
        String poem = """
                Roses are red
                  Violets are blue
                    Sugar is sweet
                """;

        System.out.println("Poem:");
        System.out.println(poem);
        System.out.println("As escaped: " + poem.replace("\n", "\\n").replace(" ", "·"));
        System.out.println("See the preserved indentation!");
        System.out.println();
    }

    /**
     * Example 4: Closing Delimiter Position
     *
     * CRITICAL FOR EXAM: Position of closing """ affects trailing newline
     */
    private static void example04_ClosingDelimiterPosition() {
        System.out.println("--- Example 4: Closing Delimiter Position ---");

        // Closing """ on its OWN line = trailing newline included
        String withTrailingNewline = """
                Line 1
                Line 2
                """;

        // Closing """ on SAME line as content = NO trailing newline
        String noTrailingNewline = """
                Line 1
                Line 2""";

        System.out.println("With trailing newline:");
        System.out.println(withTrailingNewline.replace("\n", "\\n"));
        System.out.println("Result: \"Line 1\\nLine 2\\n\"\n");

        System.out.println("WITHOUT trailing newline:");
        System.out.println(noTrailingNewline.replace("\n", "\\n"));
        System.out.println("Result: \"Line 1\\nLine 2\"");
        System.out.println();
    }

    /**
     * Example 5: Escape Sequences
     */
    private static void example05_EscapeSequences() {
        System.out.println("--- Example 5: Escape Sequences ---");

        // \n = explicit newline (in addition to actual newlines)
        String s1 = """
                First\nSecond
                Third
                """;
        System.out.println("Using \\n:");
        System.out.println(s1);

        // \t = tab character
        String s2 = """
                Name\tAge
                Alice\t30
                Bob\t25
                """;
        System.out.println("Using \\t:");
        System.out.println(s2);

        // \s = space (useful for trailing spaces that might get trimmed)
        String s3 = """
                Hello\s\s
                World
                """;
        System.out.println("Using \\s (preserves trailing spaces):");
        System.out.println(s3.replace(" ", "·").replace("\n", "\\n"));

        // \" = double quote (though not required for single quotes)
        String s4 = """
                She said "Hello"
                He said \"Hi\"
                """;
        System.out.println("Quotes in text blocks:");
        System.out.println(s4);

        // \\ = backslash
        String s5 = """
                Windows path: C:\\Users\\casey
                """;
        System.out.println("Backslash:");
        System.out.println(s5);
        System.out.println();
    }

    /**
     * Example 6: Line Continuation Character (\)
     *
     * VERY IMPORTANT FOR EXAM!
     * Backslash at END of line removes the newline
     */
    private static void example06_LineContinuation() {
        System.out.println("--- Example 6: Line Continuation ---");

        // WITHOUT line continuation
        String s1 = """
                Hello
                World
                """;
        System.out.println("Without line continuation:");
        System.out.println(s1.replace("\n", "\\n"));
        System.out.println("Result: \"Hello\\nWorld\\n\"\n");

        // WITH line continuation
        String s2 = """
                Hello\
                World
                """;
        System.out.println("With line continuation:");
        System.out.println(s2.replace("\n", "\\n"));
        System.out.println("Result: \"HelloWorld\\n\"\n");

        // Multiple line continuations
        String s3 = """
                This is a \
                very long \
                sentence.
                """;
        System.out.println("Multiple line continuations:");
        System.out.println(s3.replace("\n", "\\n"));
        System.out.println("Result: \"This is a very long sentence.\\n\"\n");
        System.out.println("Length: " + s3.length());

        // Line continuation at end removes trailing newline too
        String s4 = """
                Line 1
                Line 2\
                """;
        System.out.println("Line continuation on last line:");
        System.out.println(s4.replace("\n", "\\n"));
        System.out.println("Result: \"Line 1\\nLine 2\"");
        System.out.println();
    }

    /**
     * Example 7: Common Exam Traps
     */
    private static void example07_ExamTraps() {
        System.out.println("--- Example 7: Common Exam Traps ---");

        System.out.println("TRAP 1: Text after opening \"\"\" - DOES NOT COMPILE!");
        // String bad = """Hello    // COMPILATION ERROR!
        //              World""";
        System.out.println("  ❌ String bad = \"\"\"Hello");
        System.out.println("                   World\"\"\";");
        System.out.println();

        System.out.println("TRAP 2: Closing \"\"\" position affects indentation");
        String trap2a = """
                    Hello
                    World
                """;  // Closing """ at column 16

        String trap2b = """
                    Hello
                    World
                    """;  // Closing """ at column 20

        System.out.println("  trap2a (closing \"\"\" less indented):");
        System.out.println("  [" + trap2a.replace("\n", "\\n").replace(" ", "·") + "]");

        System.out.println("  trap2b (closing \"\"\" more indented):");
        System.out.println("  [" + trap2b.replace("\n", "\\n").replace(" ", "·") + "]");
        System.out.println();

        System.out.println("TRAP 3: Empty text blocks");
        String empty1 = """
                """;  // Contains one newline
        // String empty2 = """""";  // DOES NOT COMPILE - needs newline after opening

        System.out.println("  empty1 length: " + empty1.length());
        System.out.println("  empty1 content: [" + empty1.replace("\n", "\\n") + "]");
        System.out.println("  ❌ String empty2 = \"\"\"\"\"\"; // DOES NOT COMPILE!");
        System.out.println();

        System.out.println("TRAP 4: Trailing spaces without \\s are removed");
        String trap4 = """
                Hello
                World
                """;
        System.out.println("  [" + trap4.replace("\n", "\\n").replace(" ", "·") + "]");
        System.out.println("  Notice: trailing spaces after 'Hello' are removed!");
        System.out.println();
    }

    /**
     * Example 8: Practice Questions
     *
     * Try to predict the output BEFORE running!
     */
    private static void example08_PracticeQuestions() {
        System.out.println("--- Example 8: Practice Questions ---");

        System.out.println("\nQuestion 1: What is the output?");
        String q1 = """
                ABC
                  DEF
                """;
        System.out.println("Answer: [" + q1.replace("\n", "\\n").replace(" ", "·") + "]");

        System.out.println("\nQuestion 2: What is the output?");
        String q2 = """
                ABC
                  DEF
            """;
        System.out.println("Answer: [" + q2.replace("\n", "\\n").replace(" ", "·") + "]");

        System.out.println("\nQuestion 3: What is the output?");
        String q3 = """
                ABC\
                DEF
                """;
        System.out.println("Answer: [" + q3.replace("\n", "\\n").replace(" ", "·") + "]");

        System.out.println("\nQuestion 4: What is the length?");
        String q4 = """
                A
                B
                """;
        System.out.println("Answer: " + q4.length() + " (characters: A, \\n, B, \\n)");

        System.out.println("\nQuestion 5: What is the length?");
        String q5 = """
                A
                B""";
        System.out.println("Answer: " + q5.length() + " (characters: A, \\n, B)");

        System.out.println("\nQuestion 6: What is the output?");
        String q6 = """
                    Line1
                    Line2
                """;
        System.out.println("Answer: [" + q6.replace("\n", "\\n").replace(" ", "·") + "]");
        System.out.println("(Note the leading spaces!)");
    }

    /**
     * SUMMARY FOR OCP EXAM:
     *
     * 1. Opening """ MUST be followed by newline (no text allowed)
     *
     * 2. VERTICAL LINE determines incidental vs essential whitespace:
     *    - Find leftmost character position (including closing """)
     *    - Left of line = incidental (removed)
     *    - Right of line = essential (kept)
     *
     * 3. Closing """ position:
     *    - On own line → trailing \n included
     *    - On same line as content → NO trailing \n
     *
     * 4. Escape sequences:
     *    - \n, \t, \\, \" work as normal
     *    - \s = single space (preserves trailing)
     *    - \ at end of line = line continuation (removes newline)
     *
     * 5. Common mistakes to watch for:
     *    - Text after opening """ = DOES NOT COMPILE
     *    - Closing """ position changes indentation reference point
     *    - Trailing spaces removed unless using \s
     *    - Empty text block needs newline: """ \n """
     */
}
