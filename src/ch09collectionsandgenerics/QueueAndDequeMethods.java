package ch09collectionsandgenerics;

import java.util.*;

/**
 * Queue and Deque Methods
 *
 * ===== METHODS THAT THROW EXCEPTIONS =====
 *
 * Queue methods:
 * - boolean add(E e)           - Adds element, throws IllegalStateException if capacity restricted
 * - E element()                - Returns head, throws NoSuchElementException if empty
 * - E remove()                 - Removes and returns head, throws NoSuchElementException if empty
 *
 * Deque methods (double-ended):
 * - void addFirst(E e)         - Adds to front, throws IllegalStateException if capacity restricted
 * - void addLast(E e)          - Adds to end, throws IllegalStateException if capacity restricted
 * - E getFirst()               - Returns first element, throws NoSuchElementException if empty
 * - E getLast()                - Returns last element, throws NoSuchElementException if empty
 * - E removeFirst()            - Removes and returns first, throws NoSuchElementException if empty
 * - E removeLast()             - Removes and returns last, throws NoSuchElementException if empty
 *
 * Stack methods:
 * - void push(E e)             - Pushes element (adds to front), throws IllegalStateException if capacity restricted
 * - E pop()                    - Pops element (removes from front), throws NoSuchElementException if empty
 * - E peek()                   - Returns top element without removing (does NOT throw exception if empty - returns null)
 *
 * ===== METHODS THAT RETURN NULL/FALSE (DO NOT THROW EXCEPTIONS) =====
 *
 * Queue methods:
 * - boolean offer(E e)         - Adds element, returns false if can't add
 * - E peek()                   - Returns head, returns null if empty
 * - E poll()                   - Removes and returns head, returns null if empty
 *
 * Deque methods (double-ended):
 * - boolean offerFirst(E e)    - Adds to front, returns false if can't add
 * - boolean offerLast(E e)     - Adds to end, returns false if can't add
 * - E peekFirst()              - Returns first element, returns null if empty
 * - E peekLast()               - Returns last element, returns null if empty
 * - E pollFirst()              - Removes and returns first, returns null if empty
 * - E pollLast()               - Removes and returns last, returns null if empty
 *
 * ===== SUMMARY TABLE =====
 *
 * Operation         | Throws Exception    | Returns null/false
 * ------------------|---------------------|--------------------
 * Add to end        | add(e)              | offer(e)
 * Remove from front | remove()            | poll()
 * Examine front     | element()           | peek()
 * Add to front      | addFirst(e)         | offerFirst(e)
 * Add to end        | addLast(e)          | offerLast(e)
 * Remove from front | removeFirst()       | pollFirst()
 * Remove from end   | removeLast()        | pollLast()
 * Examine front     | getFirst()          | peekFirst()
 * Examine end       | getLast()           | peekLast()
 * Push (stack)      | push(e)             | offerFirst(e)
 * Pop (stack)       | pop()               | pollFirst()
 *
 * NOTE: peek() for stack does NOT throw exception (returns null if empty)
 */
public class QueueAndDequeMethods {

    public static void main(String[] args) {
        demonstrateQueueExceptionMethods();
        demonstrateQueueSafeMethods();
        demonstrateDequeExceptionMethods();
        demonstrateDequeSafeMethods();
        demonstrateStackMethods();
        demonstrateComparison();
    }

    // ===== QUEUE METHODS - THROW EXCEPTIONS =====

    public static void demonstrateQueueExceptionMethods() {
        System.out.println("=== QUEUE - EXCEPTION METHODS ===\n");

        Queue<String> queue = new LinkedList<>();

        // boolean add(E e) - throws if capacity restricted
        queue.add("First");
        queue.add("Second");
        queue.add("Third");
        System.out.println("After add(): " + queue);

        // E element() - throws NoSuchElementException if empty
        System.out.println("element(): " + queue.element() + " (head element)");

        // E remove() - throws NoSuchElementException if empty
        System.out.println("remove(): " + queue.remove() + " (removed)");
        System.out.println("After remove(): " + queue);

        // Exception when empty
        queue.clear();
        try {
            queue.element();
        } catch (NoSuchElementException e) {
            System.out.println("element() on empty queue: NoSuchElementException ✗");
        }

        try {
            queue.remove();
        } catch (NoSuchElementException e) {
            System.out.println("remove() on empty queue: NoSuchElementException ✗");
        }
    }

    // ===== QUEUE METHODS - RETURN NULL/FALSE =====

    public static void demonstrateQueueSafeMethods() {
        System.out.println("\n=== QUEUE - SAFE METHODS (null/false) ===\n");

        Queue<String> queue = new LinkedList<>();

        // boolean offer(E e) - returns false if can't add
        System.out.println("offer('First'): " + queue.offer("First"));
        System.out.println("offer('Second'): " + queue.offer("Second"));
        System.out.println("offer('Third'): " + queue.offer("Third"));
        System.out.println("After offer(): " + queue);

        // E peek() - returns null if empty
        System.out.println("peek(): " + queue.peek() + " (head element, not removed)");
        System.out.println("Queue unchanged: " + queue);

        // E poll() - returns null if empty
        System.out.println("poll(): " + queue.poll() + " (removed)");
        System.out.println("After poll(): " + queue);

        // Returns null when empty (no exception)
        queue.clear();
        System.out.println("\nOn empty queue:");
        System.out.println("peek(): " + queue.peek() + " (returns null) ✓");
        System.out.println("poll(): " + queue.poll() + " (returns null) ✓");
    }

    // ===== DEQUE METHODS - THROW EXCEPTIONS =====

    public static void demonstrateDequeExceptionMethods() {
        System.out.println("\n=== DEQUE - EXCEPTION METHODS ===\n");

        Deque<String> deque = new LinkedList<>();

        // void addFirst(E e) - throws if capacity restricted
        deque.addFirst("Middle");
        deque.addFirst("First");
        System.out.println("After addFirst(): " + deque);

        // void addLast(E e) - throws if capacity restricted
        deque.addLast("Last");
        System.out.println("After addLast(): " + deque);

        // E getFirst() - throws NoSuchElementException if empty
        System.out.println("getFirst(): " + deque.getFirst());

        // E getLast() - throws NoSuchElementException if empty
        System.out.println("getLast(): " + deque.getLast());

        // E removeFirst() - throws NoSuchElementException if empty
        System.out.println("removeFirst(): " + deque.removeFirst());
        System.out.println("After removeFirst(): " + deque);

        // E removeLast() - throws NoSuchElementException if empty
        System.out.println("removeLast(): " + deque.removeLast());
        System.out.println("After removeLast(): " + deque);

        // Exceptions when empty
        deque.clear();
        try {
            deque.getFirst();
        } catch (NoSuchElementException e) {
            System.out.println("\ngetFirst() on empty: NoSuchElementException ✗");
        }

        try {
            deque.getLast();
        } catch (NoSuchElementException e) {
            System.out.println("getLast() on empty: NoSuchElementException ✗");
        }

        try {
            deque.removeFirst();
        } catch (NoSuchElementException e) {
            System.out.println("removeFirst() on empty: NoSuchElementException ✗");
        }

        try {
            deque.removeLast();
        } catch (NoSuchElementException e) {
            System.out.println("removeLast() on empty: NoSuchElementException ✗");
        }
    }

    // ===== DEQUE METHODS - RETURN NULL/FALSE =====

    public static void demonstrateDequeSafeMethods() {
        System.out.println("\n=== DEQUE - SAFE METHODS (null/false) ===\n");

        Deque<String> deque = new LinkedList<>();

        // boolean offerFirst(E e) - returns false if can't add
        System.out.println("offerFirst('Second'): " + deque.offerFirst("Second"));
        System.out.println("offerFirst('First'): " + deque.offerFirst("First"));
        System.out.println("After offerFirst(): " + deque);

        // boolean offerLast(E e) - returns false if can't add
        System.out.println("offerLast('Third'): " + deque.offerLast("Third"));
        System.out.println("After offerLast(): " + deque);

        // E peekFirst() - returns null if empty
        System.out.println("peekFirst(): " + deque.peekFirst());

        // E peekLast() - returns null if empty
        System.out.println("peekLast(): " + deque.peekLast());

        // E pollFirst() - returns null if empty
        System.out.println("pollFirst(): " + deque.pollFirst());
        System.out.println("After pollFirst(): " + deque);

        // E pollLast() - returns null if empty
        System.out.println("pollLast(): " + deque.pollLast());
        System.out.println("After pollLast(): " + deque);

        // Returns null when empty (no exception)
        deque.clear();
        System.out.println("\nOn empty deque:");
        System.out.println("peekFirst(): " + deque.peekFirst() + " ✓");
        System.out.println("peekLast(): " + deque.peekLast() + " ✓");
        System.out.println("pollFirst(): " + deque.pollFirst() + " ✓");
        System.out.println("pollLast(): " + deque.pollLast() + " ✓");
    }

    // ===== STACK METHODS =====

    public static void demonstrateStackMethods() {
        System.out.println("\n=== STACK METHODS ===\n");

        Deque<String> stack = new LinkedList<>();

        // void push(E e) - throws if capacity restricted
        stack.push("First");
        stack.push("Second");
        stack.push("Third");
        System.out.println("After push(): " + stack + " (LIFO - Last In First Out)");

        // E peek() - returns null if empty (does NOT throw exception!)
        System.out.println("peek(): " + stack.peek() + " (top element, not removed)");
        System.out.println("Stack unchanged: " + stack);

        // E pop() - throws NoSuchElementException if empty
        System.out.println("pop(): " + stack.pop() + " (removed)");
        System.out.println("After pop(): " + stack);

        // Pop remaining
        stack.pop();
        stack.pop();

        // peek() returns null when empty (no exception)
        System.out.println("\nOn empty stack:");
        System.out.println("peek(): " + stack.peek() + " (returns null) ✓");

        // pop() throws when empty
        try {
            stack.pop();
        } catch (NoSuchElementException e) {
            System.out.println("pop() on empty: NoSuchElementException ✗");
        }

        // push() can throw if capacity restricted
        try {
            stack.push("New");
            System.out.println("push('New'): " + stack + " ✓");
        } catch (IllegalStateException e) {
            System.out.println("push() when full: IllegalStateException ✗");
        }
    }

    // ===== COMPARISON TABLE =====

    public static void demonstrateComparison() {
        System.out.println("\n=== COMPARISON TABLE ===\n");

        System.out.println("┌──────────────────┬─────────────────────┬────────────────────┐");
        System.out.println("│ Operation        │ Throws Exception    │ Returns null/false │");
        System.out.println("├──────────────────┼─────────────────────┼────────────────────┤");
        System.out.println("│ Add to end       │ add(e)              │ offer(e)           │");
        System.out.println("│ Remove from front│ remove()            │ poll()             │");
        System.out.println("│ Examine front    │ element()           │ peek()             │");
        System.out.println("│ Add to front     │ addFirst(e)         │ offerFirst(e)      │");
        System.out.println("│ Add to end       │ addLast(e)          │ offerLast(e)       │");
        System.out.println("│ Remove from front│ removeFirst()       │ pollFirst()        │");
        System.out.println("│ Remove from end  │ removeLast()        │ pollLast()         │");
        System.out.println("│ Examine front    │ getFirst()          │ peekFirst()        │");
        System.out.println("│ Examine end      │ getLast()           │ peekLast()         │");
        System.out.println("│ Push (stack)     │ push(e)             │ offerFirst(e)      │");
        System.out.println("│ Pop (stack)      │ pop()               │ pollFirst()        │");
        System.out.println("│ Peek (stack)     │ peek() [NO THROW]   │ -                  │");
        System.out.println("└──────────────────┴─────────────────────┴────────────────────┘");

        System.out.println("\nKEY EXAM POINTS:");
        System.out.println("- Exception methods: add, remove, element, addFirst/Last, getFirst/Last, removeFirst/Last, push, pop");
        System.out.println("- Safe methods: offer, poll, peek, offerFirst/Last, pollFirst/Last, peekFirst/Last");
        System.out.println("- EXCEPTION: stack peek() does NOT throw (returns null)");
        System.out.println("- LinkedList implements both Queue and Deque");
    }
}
