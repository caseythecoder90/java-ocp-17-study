package ch04coreapis;

import java.util.Arrays;

/**
 * ARRAYS METHODS - Common Methods Guide for OCP Java 17 Exam
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * IMPORTANT: java.util.Arrays class
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * The Arrays class provides static utility methods for working with arrays.
 * You MUST import java.util.Arrays to use these methods!
 *
 * KEY METHODS COVERED:
 * 1. compare()      - Compare two arrays lexicographically
 * 2. mismatch()     - Find first index where arrays differ
 * 3. binarySearch() - Search for element in sorted array (remember it must be sorted!)
 * 4. sort()         - Sort array in ascending order
 *
 * ═══════════════════════════════════════════════════════════════════════════
 * QUICK REFERENCE TABLE
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * METHOD           RETURNS      REQUIREMENT           NOTES
 * ────────────────────────────────────────────────────────────────────────────
 * compare()        int          None                  Compares lexicographically
 *                               (-1, 0, +1)           Handles different lengths
 *
 * mismatch()       int          None                  Returns index of first
 *                               (-1 if equal)         difference, or -1 if equal
 *
 * binarySearch()   int          MUST be sorted!       Returns index if found
 *                               (index or             Returns -(insertion)-1
 *                               -insertion-1)         if not found
 *
 * sort()           void         None                  Sorts in ascending order
 *                               (modifies array)      Can sort range or full array
 * ────────────────────────────────────────────────────────────────────────────
 */
public class ArraysMethods {

    public static void main(String[] args) {
        System.out.println("=== ARRAYS METHODS PRACTICE ===\n");

        compareMethod();
        compareWithDifferentLengths();
        compareEdgeCases();
        mismatchMethod();
        mismatchWithDifferentLengths();
        sortMethod();
        sortRanges();
        binarySearchMethod();
        binarySearchEdgeCases();
        binarySearchOnUnsorted();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * Arrays.compare() - Compare arrays lexicographically
     * ═══════════════════════════════════════════════════════════════════════
     *
     * Signature: static int compare(array1, array2)
     *
     * RETURN VALUES:
     * - Negative (usually -1): array1 < array2 (comes before)
     * - Zero (0):              array1 == array2 (equal)
     * - Positive (usually 1):  array1 > array2 (comes after)
     *
     * COMPARISON RULES:
     * 1. Compares element by element from left to right
     * 2. First different element determines result
     * 3. If one array is prefix of another, shorter one is "smaller"
     * 4. null elements: null < any value
     * 5. Arrays with null are compared by reference rules
     *
     * EXAM TIP: Works for all array types (int[], String[], etc.)
     */
    private static void compareMethod() {
        System.out.println("=== Arrays.compare() - Basic Comparison ===");

        // Equal arrays
        int[] a1 = {1, 2, 3};
        int[] a2 = {1, 2, 3};
        System.out.println("compare([1,2,3], [1,2,3]) = " + Arrays.compare(a1, a2)); // 0

        // First array smaller (first different element)
        int[] a3 = {1, 2, 3};
        int[] a4 = {1, 2, 4};
        System.out.println("compare([1,2,3], [1,2,4]) = " + Arrays.compare(a3, a4)); // -1 (3 < 4)

        // First array larger
        int[] a5 = {1, 5, 3};
        int[] a6 = {1, 2, 3};
        System.out.println("compare([1,5,3], [1,2,3]) = " + Arrays.compare(a5, a6)); // 1 (5 > 2)

        // String arrays - lexicographic comparison
        String[] s1 = {"apple", "banana"};
        String[] s2 = {"apple", "cherry"};
        System.out.println("compare([\"apple\",\"banana\"], [\"apple\",\"cherry\"]) = "
                + Arrays.compare(s1, s2)); // -1 ("banana" < "cherry")

        // String arrays - case matters!
        String[] s3 = {"Apple"};
        String[] s4 = {"apple"};
        System.out.println("compare([\"Apple\"], [\"apple\"]) = "
                + Arrays.compare(s3, s4)); // -1 (uppercase < lowercase in ASCII)

        System.out.println();
    }

    /**
     * Arrays.compare() - Different Lengths
     *
     * RULE: If one array is a prefix of another, the shorter array is "smaller"
     */
    private static void compareWithDifferentLengths() {
        System.out.println("=== Arrays.compare() - Different Lengths ===");

        // Shorter array is prefix - shorter is smaller
        int[] a1 = {1, 2};
        int[] a2 = {1, 2, 3};
        System.out.println("compare([1,2], [1,2,3]) = " + Arrays.compare(a1, a2)); // -1

        // Longer array comes first - longer is larger
        int[] a3 = {1, 2, 3};
        int[] a4 = {1, 2};
        System.out.println("compare([1,2,3], [1,2]) = " + Arrays.compare(a3, a4)); // 1

        // Different lengths, but difference found before end **
        int[] a5 = {1, 5};
        int[] a6 = {1, 2, 3, 4};
        System.out.println("compare([1,5], [1,2,3,4]) = " + Arrays.compare(a5, a6)); // 1 (5 > 2)

        System.out.println();
    }

    /**
     * Arrays.compare() - Edge Cases
     *
     * EXAM TRAPS:
     * - Empty arrays are equal to each other
     * - Empty array < non-empty array
     * - Single element comparison works same as multi-element
     */
    private static void compareEdgeCases() {
        System.out.println("=== Arrays.compare() - Edge Cases ===");

        // Empty arrays
        int[] empty1 = {};
        int[] empty2 = {};
        System.out.println("compare([], []) = " + java.util.Arrays.compare(empty1, empty2)); // 0

        // Empty vs non-empty
        int[] empty = {};
        int[] nonEmpty = {1};
        System.out.println("compare([], [1]) = " + java.util.Arrays.compare(empty, nonEmpty)); // -1

        // Single element
        int[] single1 = {5};
        int[] single2 = {5};
        System.out.println("compare([5], [5]) = " + java.util.Arrays.compare(single1, single2)); // 0

        int[] single3 = {3};
        int[] single4 = {7};
        System.out.println("compare([3], [7]) = " + java.util.Arrays.compare(single3, single4)); // -1

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * Arrays.mismatch() - Find first index where arrays differ
     * ═══════════════════════════════════════════════════════════════════════
     *
     * Signature: static int mismatch(array1, array2)
     *
     * RETURN VALUES:
     * - -1:              Arrays are equal
     * - index (0+):      First position where arrays differ
     *
     * COMPARISON RULES:
     * 1. Compares element by element from index 0
     * 2. Returns index of first mismatch
     * 3. If one array is prefix, returns length of shorter array
     * 4. If arrays are equal, returns -1
     *
     * EXAM TIP: Very useful for finding WHERE arrays differ, not just IF
     */
    private static void mismatchMethod() {
        System.out.println("=== Arrays.mismatch() - Basic Usage ===");

        // Equal arrays
        int[] a1 = {1, 2, 3};
        int[] a2 = {1, 2, 3};
        System.out.println("mismatch([1,2,3], [1,2,3]) = " + Arrays.mismatch(a1, a2)); // -1

        // Difference at first position
        int[] a3 = {1, 2, 3};
        int[] a4 = {9, 2, 3};
        System.out.println("mismatch([1,2,3], [9,2,3]) = " + Arrays.mismatch(a3, a4)); // 0

        // Difference at middle position
        int[] a5 = {1, 2, 3};
        int[] a6 = {1, 5, 3};
        System.out.println("mismatch([1,2,3], [1,5,3]) = " + Arrays.mismatch(a5, a6)); // 1

        // Difference at last position
        int[] a7 = {1, 2, 3};
        int[] a8 = {1, 2, 9};
        System.out.println("mismatch([1,2,3], [1,2,9]) = " + Arrays.mismatch(a7, a8)); // 2

        // String arrays
        String[] s1 = {"apple", "banana", "cherry"};
        String[] s2 = {"apple", "blueberry", "cherry"};
        System.out.println("mismatch([\"apple\",\"banana\",\"cherry\"], [\"apple\",\"blueberry\",\"cherry\"]) = "
                + Arrays.mismatch(s1, s2)); // 1

        System.out.println();
    }

    /**
     * Arrays.mismatch() - Different Lengths
     *
     * RULE: If one array is prefix of another, returns length of shorter array
     */
    private static void mismatchWithDifferentLengths() {
        System.out.println("=== Arrays.mismatch() - Different Lengths ===");

        // First array is prefix
        int[] a1 = {1, 2};
        int[] a2 = {1, 2, 3};
        System.out.println("mismatch([1,2], [1,2,3]) = " + Arrays.mismatch(a1, a2)); // 2 (length of shorter)

        // Second array is prefix
        int[] a3 = {1, 2, 3, 4};
        int[] a4 = {1, 2, 3};
        System.out.println("mismatch([1,2,3,4], [1,2,3]) = " + Arrays.mismatch(a3, a4)); // 3 (length of shorter)

        // Difference before length difference matters **
        int[] a5 = {1, 5};
        int[] a6 = {1, 2, 3, 4};
        System.out.println("mismatch([1,5], [1,2,3,4]) = " + Arrays.mismatch(a5, a6)); // 1 (first difference)

        // Empty array vs non-empty
        int[] empty = {};
        int[] nonEmpty = {1, 2};
        System.out.println("mismatch([], [1,2]) = " + java.util.Arrays.mismatch(empty, nonEmpty)); // 0 (length of empty)

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * Arrays.sort() - Sort array in ascending order
     * ═══════════════════════════════════════════════════════════════════════
     *
     * Signature: static void sort(array)
     *
     * KEY POINTS:
     * 1. Returns void - modifies the array IN PLACE
     * 2. Sorts in ascending (natural) order
     * 3. Works with primitives (int, double, etc.) and objects (String, etc.)
     * 4. For objects, uses natural ordering (Comparable) or comparator
     * 5. Strings: numbers < uppercase < lowercase
     *
     * EXAM TRAP: sort() modifies the original array, doesn't return a new one!
     */
    private static void sortMethod() {
        System.out.println("=== Arrays.sort() - Basic Usage ===");

        // Integer array
        int[] nums = {5, 2, 8, 1, 9};
        System.out.println("Before sort: " + java.util.Arrays.toString(nums));
        java.util.Arrays.sort(nums);
        System.out.println("After sort:  " + java.util.Arrays.toString(nums)); // [1, 2, 5, 8, 9]

        // String array - natural ordering
        String[] words = {"banana", "apple", "cherry", "date"};
        System.out.println("\nBefore sort: " + java.util.Arrays.toString(words));
        java.util.Arrays.sort(words);
        System.out.println("After sort:  " + java.util.Arrays.toString(words)); // [apple, banana, cherry, date]

        // String array with mixed case - EXAM TRAP!
        String[] mixed = {"Zebra", "apple", "Apple", "zebra", "123"};
        System.out.println("\nBefore sort: " + java.util.Arrays.toString(mixed));
        java.util.Arrays.sort(mixed);
        System.out.println("After sort:  " + java.util.Arrays.toString(mixed));
        // [123, Apple, Zebra, apple, zebra]
        // Order: numbers < Uppercase < lowercase

        // Already sorted - still works
        int[] sorted = {1, 2, 3, 4, 5};
        System.out.println("\nAlready sorted: " + java.util.Arrays.toString(sorted));
        java.util.Arrays.sort(sorted);
        System.out.println("After sort:     " + java.util.Arrays.toString(sorted)); // [1, 2, 3, 4, 5]

        // Reverse sorted
        int[] reverse = {5, 4, 3, 2, 1};
        System.out.println("\nReverse sorted: " + java.util.Arrays.toString(reverse));
        java.util.Arrays.sort(reverse);
        System.out.println("After sort:     " + java.util.Arrays.toString(reverse)); // [1, 2, 3, 4, 5]

        // Array with duplicates
        int[] duplicates = {3, 1, 4, 1, 5, 9, 2, 6, 5};
        System.out.println("\nWith duplicates: " + java.util.Arrays.toString(duplicates));
        java.util.Arrays.sort(duplicates);
        System.out.println("After sort:      " + java.util.Arrays.toString(duplicates)); // [1, 1, 2, 3, 4, 5, 5, 6, 9]

        System.out.println();
    }

    /**
     * Arrays.sort() - Sorting Ranges
     *
     * Signature: static void sort(array, fromIndex, toIndex)
     *
     * KEY POINTS:
     * - fromIndex: inclusive (starts at this index)
     * - toIndex: exclusive (stops before this index)
     * - Only sorts elements in the specified range
     * - Rest of array remains unchanged
     *
     * EXAM TIP: Remember toIndex is EXCLUSIVE, just like substring()!
     */
    private static void sortRanges() {
        System.out.println("=== Arrays.sort() - Sorting Ranges ===");

        // Sort middle portion
        int[] nums = {9, 5, 1, 8, 2, 7, 3};
        System.out.println("Original:           " + java.util.Arrays.toString(nums));
        java.util.Arrays.sort(nums, 2, 5); // Sort indices 2, 3, 4 (not 5!)
        System.out.println("After sort(2, 5):   " + java.util.Arrays.toString(nums));
        // [9, 5, 1, 2, 8, 7, 3] - only indices 2-4 sorted

        // Sort from beginning
        int[] nums2 = {5, 3, 8, 1, 9};
        System.out.println("\nOriginal:           " + java.util.Arrays.toString(nums2));
        java.util.Arrays.sort(nums2, 0, 3); // Sort indices 0, 1, 2
        System.out.println("After sort(0, 3):   " + java.util.Arrays.toString(nums2));
        // [3, 5, 8, 1, 9] - only first 3 elements sorted

        // Sort to end
        int[] nums3 = {9, 8, 7, 6, 5};
        System.out.println("\nOriginal:           " + java.util.Arrays.toString(nums3));
        java.util.Arrays.sort(nums3, 2, 5); // Sort indices 2, 3, 4
        System.out.println("After sort(2, 5):   " + java.util.Arrays.toString(nums3));
        // [9, 8, 5, 6, 7] - only last 3 elements sorted

        // Edge case: sort single element
        int[] nums4 = {5, 3, 8, 1, 9};
        System.out.println("\nOriginal:           " + java.util.Arrays.toString(nums4));
        java.util.Arrays.sort(nums4, 2, 3); // Sort only index 2
        System.out.println("After sort(2, 3):   " + java.util.Arrays.toString(nums4));
        // [5, 3, 8, 1, 9] - single element, no change

        // Edge case: sort nothing (fromIndex == toIndex)
        int[] nums5 = {5, 3, 8, 1, 9};
        System.out.println("\nOriginal:           " + java.util.Arrays.toString(nums5));
        java.util.Arrays.sort(nums5, 2, 2); // Sort nothing
        System.out.println("After sort(2, 2):   " + java.util.Arrays.toString(nums5));
        // [5, 3, 8, 1, 9] - no change

        System.out.println();
    }

    /**
     * ═══════════════════════════════════════════════════════════════════════
     * Arrays.binarySearch() - Search sorted array efficiently
     * ═══════════════════════════════════════════════════════════════════════
     *
     * Signature: static int binarySearch(array, target)
     *
     * CRITICAL REQUIREMENT: Array MUST be sorted first!
     *
     * RETURN VALUES:
     * - If found:     Returns the index of the element
     * - If not found: Returns -(insertion point) - 1
     *
     * INSERTION POINT: The index where the element would be inserted to keep
     *                  the array sorted
     *
     * FORMULA FOR NOT FOUND:
     *   result = -(insertion_point) - 1
     *   insertion_point = -result - 1
     *
     * EXAM TRAP: If array is NOT sorted, results are unpredictable!
     */
    private static void binarySearchMethod() {
        System.out.println("=== Arrays.binarySearch() - Basic Usage ===");

        // Sorted array - elements found
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        System.out.println("Sorted array: " + java.util.Arrays.toString(nums));

        System.out.println("binarySearch(3) = " + java.util.Arrays.binarySearch(nums, 3)); // 2 (found at index 2)
        System.out.println("binarySearch(7) = " + java.util.Arrays.binarySearch(nums, 7)); // 6 (found at index 6)
        System.out.println("binarySearch(1) = " + java.util.Arrays.binarySearch(nums, 1)); // 0 (found at index 0)
        System.out.println("binarySearch(9) = " + java.util.Arrays.binarySearch(nums, 9)); // 8 (found at index 8)

        // Element not found - returns -(insertion point) - 1
        System.out.println("\n--- Elements Not Found ---");

        // Should be inserted at index 0
        System.out.println("binarySearch(0) = " + java.util.Arrays.binarySearch(nums, 0));
        // -1 (would insert at 0: -(0)-1 = -1)

        // Should be inserted at index 5 (between 5 and 6)
        System.out.println("binarySearch(5.5) would give... let's try 5:");
        int[] nums2 = {1, 2, 3, 4, 5, 7, 8}; // missing 6
        System.out.println("Array: " + java.util.Arrays.toString(nums2));
        System.out.println("binarySearch(6) = " + java.util.Arrays.binarySearch(nums2, 6));
        // -6 (would insert at 5: -(5)-1 = -6)

        // Should be inserted at end
        System.out.println("binarySearch(10) = " + java.util.Arrays.binarySearch(nums, 10));
        // -10 (would insert at 9: -(9)-1 = -10)

        // Calculating insertion point from result
        System.out.println("\n--- Calculating Insertion Point ---");
        int result = java.util.Arrays.binarySearch(nums2, 6);
        int insertionPoint = -result - 1;
        System.out.println("Search for 6 returned: " + result);
        System.out.println("Insertion point: " + insertionPoint); // 5

        // String array
        System.out.println("\n--- String Arrays ---");
        String[] words = {"apple", "banana", "cherry", "date", "grape"};
        System.out.println("Sorted array: " + java.util.Arrays.toString(words));
        System.out.println("binarySearch(\"cherry\") = " + java.util.Arrays.binarySearch(words, "cherry")); // 2
        System.out.println("binarySearch(\"fig\") = " + java.util.Arrays.binarySearch(words, "fig"));
        // -5 (would insert at 4: -(4)-1 = -5)

        System.out.println();
    }

    /**
     * Arrays.binarySearch() - Edge Cases
     *
     * EXAM TRAPS:
     * - Empty array returns -1 (would insert at 0: -(0)-1 = -1)
     * - Duplicates: returns index of one match (unspecified which)
     * - Search in range: binarySearch(array, fromIndex, toIndex, target)
     */
    private static void binarySearchEdgeCases() {
        System.out.println("=== Arrays.binarySearch() - Edge Cases ===");

        // Empty array
        int[] empty = {};
        System.out.println("Empty array: " + java.util.Arrays.toString(empty));
        System.out.println("binarySearch(5) = " + java.util.Arrays.binarySearch(empty, 5)); // -1

        // Single element - found
        int[] single = {5};
        System.out.println("\nSingle element [5]:");
        System.out.println("binarySearch(5) = " + java.util.Arrays.binarySearch(single, 5)); // 0

        // Single element - not found (before)
        System.out.println("binarySearch(3) = " + java.util.Arrays.binarySearch(single, 3)); // -1 (insert at 0)

        // Single element - not found (after)
        System.out.println("binarySearch(7) = " + java.util.Arrays.binarySearch(single, 7)); // -2 (insert at 1)

        // Array with duplicates - unspecified which is returned
        int[] duplicates = {1, 2, 2, 2, 3, 4};
        System.out.println("\nWith duplicates: " + java.util.Arrays.toString(duplicates));
        System.out.println("binarySearch(2) = " + java.util.Arrays.binarySearch(duplicates, 2));
        // Could return 1, 2, or 3 (any index with value 2)

        // Search in range
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8};
        System.out.println("\nArray: " + java.util.Arrays.toString(nums));
        System.out.println("binarySearch(array, 2, 6, 5) = " + java.util.Arrays.binarySearch(nums, 2, 6, 5));
        // 4 (found at index 4, searching only indices 2-5)

        System.out.println("binarySearch(array, 2, 6, 7) = " + java.util.Arrays.binarySearch(nums, 2, 6, 7));
        // -7 (not in range, would insert at 6: -(6)-1 = -7)

        System.out.println();
    }

    /**
     * Arrays.binarySearch() - Unsorted Array Trap!
     *
     * CRITICAL EXAM TIP:
     * If you call binarySearch() on an UNSORTED array, the results are
     * UNPREDICTABLE and UNRELIABLE. It might return wrong indices, -1,
     * or random negative values.
     *
     * ALWAYS sort the array first!
     */
    private static void binarySearchOnUnsorted() {
        System.out.println("=== Arrays.binarySearch() - UNSORTED Array Trap! ===");

        // Unsorted array - WRONG RESULTS!
        int[] unsorted = {5, 3, 7, 1, 9, 2, 8};
        System.out.println("UNSORTED array: " + java.util.Arrays.toString(unsorted));
        System.out.println("binarySearch(5) on unsorted = " + java.util.Arrays.binarySearch(unsorted, 5));
        // UNPREDICTABLE! Don't trust this result!

        System.out.println("binarySearch(1) on unsorted = " + java.util.Arrays.binarySearch(unsorted, 1));
        // UNPREDICTABLE! Don't trust this result!

        // CORRECT approach - sort first!
        System.out.println("\n--- CORRECT: Sort First ---");
        int[] sorted = {5, 3, 7, 1, 9, 2, 8};
        System.out.println("Before sort: " + java.util.Arrays.toString(sorted));
        java.util.Arrays.sort(sorted);
        System.out.println("After sort:  " + java.util.Arrays.toString(sorted));

        System.out.println("binarySearch(5) on sorted = " + java.util.Arrays.binarySearch(sorted, 5)); // 3
        System.out.println("binarySearch(1) on sorted = " + java.util.Arrays.binarySearch(sorted, 1)); // 0
        System.out.println("binarySearch(6) on sorted = " + java.util.Arrays.binarySearch(sorted, 6));
        // -5 (not found, would insert at 4: -(4)-1 = -5)

        System.out.println("\n*** REMEMBER: ALWAYS sort before binarySearch()! ***");
        System.out.println();
    }
}
