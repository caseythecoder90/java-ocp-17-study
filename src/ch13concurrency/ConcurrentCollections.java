package ch13concurrency;

import java.util.*;
import java.util.concurrent.*;

/**
 * CONCURRENT COLLECTIONS
 * ======================
 *
 * Thread-safe collections designed for concurrent access without external synchronization.
 *
 *
 * ============================================================================
 * WHY CONCURRENT COLLECTIONS?
 * ============================================================================
 *
 * PROBLEMS WITH REGULAR COLLECTIONS:
 * - Not thread-safe (ArrayList, HashMap, etc.)
 * - Race conditions and corrupted data with concurrent access
 * - Need external synchronization
 *
 * PROBLEMS WITH SYNCHRONIZED COLLECTIONS:
 * - Use single lock for entire collection
 * - Poor performance under high concurrency
 * - May still get ConcurrentModificationException during iteration
 *
 * CONCURRENT COLLECTIONS BENEFITS:
 * - Thread-safe without external synchronization
 * - Better performance via lock striping and other techniques
 * - No ConcurrentModificationException during iteration
 * - Weakly consistent iterators (may reflect changes after creation)
 *
 *
 * ============================================================================
 * CONCURRENT COLLECTIONS - KNOW THESE!
 * ============================================================================
 *
 * COMPREHENSIVE TABLE (MEMORIZE FOR EXAM):
 * ┌─────────────────────────┬─────────────────┬──────────┬───────────┐
 * │ Class                   │ Interface       │ Sorted?  │ Blocking? │
 * ├─────────────────────────┼─────────────────┼──────────┼───────────┤
 * │ ConcurrentHashMap       │ Map             │ NO       │ NO        │
 * ├─────────────────────────┼─────────────────┼──────────┼───────────┤
 * │ ConcurrentLinkedQueue   │ Queue           │ NO       │ NO        │
 * ├─────────────────────────┼─────────────────┼──────────┼───────────┤
 * │ ConcurrentSkipListMap   │ SortedMap       │ YES      │ NO        │
 * │                         │ NavigableMap    │          │           │
 * ├─────────────────────────┼─────────────────┼──────────┼───────────┤
 * │ ConcurrentSkipListSet   │ SortedSet       │ YES      │ NO        │
 * │                         │ NavigableSet    │          │           │
 * ├─────────────────────────┼─────────────────┼──────────┼───────────┤
 * │ CopyOnWriteArrayList    │ List            │ NO       │ NO        │
 * ├─────────────────────────┼─────────────────┼──────────┼───────────┤
 * │ CopyOnWriteArraySet     │ Set             │ NO       │ NO        │
 * ├─────────────────────────┼─────────────────┼──────────┼───────────┤
 * │ LinkedBlockingQueue     │ BlockingQueue   │ NO       │ YES       │
 * └─────────────────────────┴─────────────────┴──────────┴───────────┘
 *
 * MEMORY AIDS:
 * - "SkipList" collections are SORTED
 * - "Blocking" in the name = BLOCKING (only LinkedBlockingQueue)
 * - "CopyOnWrite" = copy entire collection on modification (expensive writes)
 *
 *
 * ============================================================================
 * 1. CONCURRENTHASHMAP
 * ============================================================================
 *
 * Thread-safe Map implementation with better concurrency than Hashtable.
 *
 * CHARACTERISTICS:
 * - Implements Map interface
 * - NOT sorted
 * - NOT blocking
 * - Uses lock striping (multiple locks for different segments)
 * - Does NOT allow null keys or values
 *
 * WHEN TO USE:
 * - Concurrent reads and writes to a map
 * - High contention scenarios
 * - No need for sorting
 *
 * EXAMPLE:
 *   Map<String, Integer> map = new ConcurrentHashMap<>();
 *   map.put("key", 1);  // Thread-safe
 *   map.get("key");     // Thread-safe
 *
 * SPECIAL METHODS (exam may ask):
 *   V putIfAbsent(K key, V value)  // Only put if key not present
 *   boolean remove(K key, V value)  // Only remove if key maps to value
 *   V replace(K key, V value)       // Only replace if key present
 *
 *
 * ============================================================================
 * 2. CONCURRENTLINKEDQUEUE
 * ============================================================================
 *
 * Thread-safe unbounded FIFO queue based on linked nodes.
 *
 * CHARACTERISTICS:
 * - Implements Queue interface
 * - NOT sorted (FIFO order)
 * - NOT blocking (returns null/false if operation can't complete immediately)
 * - Unbounded (grows as needed)
 * - Does NOT allow null elements
 *
 * WHEN TO USE:
 * - Producer-consumer scenarios without blocking
 * - Need FIFO ordering
 * - Unbounded queue acceptable
 *
 * EXAMPLE:
 *   Queue<String> queue = new ConcurrentLinkedQueue<>();
 *   queue.offer("item1");  // Add to tail
 *   String item = queue.poll();  // Remove from head (returns null if empty)
 *
 *
 * ============================================================================
 * 3. CONCURRENTSKIPLISTMAP
 * ============================================================================
 *
 * Thread-safe sorted map based on skip list data structure.
 *
 * CHARACTERISTICS:
 * - Implements SortedMap and NavigableMap interfaces
 * - SORTED (natural order or Comparator)
 * - NOT blocking
 * - Concurrent alternative to TreeMap
 * - Does NOT allow null keys (null values are allowed)
 *
 * WHEN TO USE:
 * - Need sorted map with concurrent access
 * - Range queries (subMap, headMap, tailMap)
 * - NavigableMap operations (lowerKey, higherKey, etc.)
 *
 * EXAMPLE:
 *   NavigableMap<Integer, String> map = new ConcurrentSkipListMap<>();
 *   map.put(3, "three");
 *   map.put(1, "one");
 *   map.put(2, "two");
 *   // Iteration is in sorted order: 1, 2, 3
 *
 *
 * ============================================================================
 * 4. CONCURRENTSKIPLISTSET
 * ============================================================================
 *
 * Thread-safe sorted set based on skip list.
 *
 * CHARACTERISTICS:
 * - Implements SortedSet and NavigableSet interfaces
 * - SORTED (natural order or Comparator)
 * - NOT blocking
 * - Concurrent alternative to TreeSet
 * - Does NOT allow null elements
 *
 * WHEN TO USE:
 * - Need sorted set with concurrent access
 * - Range queries
 * - NavigableSet operations (lower, higher, floor, ceiling)
 *
 * EXAMPLE:
 *   NavigableSet<Integer> set = new ConcurrentSkipListSet<>();
 *   set.add(3);
 *   set.add(1);
 *   set.add(2);
 *   // Iteration is in sorted order: 1, 2, 3
 *
 *
 * ============================================================================
 * 5. COPYONWRITEARRAYLIST
 * ============================================================================
 *
 * Thread-safe List where all mutative operations create a new copy of array.
 *
 * CHARACTERISTICS:
 * - Implements List interface
 * - NOT sorted (maintains insertion order)
 * - NOT blocking
 * - EXPENSIVE writes (copies entire array)
 * - CHEAP reads (no locking)
 * - Allows null elements
 *
 * COPY-ON-WRITE MECHANISM:
 * - Every write operation creates a new internal array
 * - Readers use the old array (no locking needed)
 * - Writes are very expensive, reads are very cheap
 *
 * WHEN TO USE:
 * - Many reads, few writes
 * - Iteration more common than modification
 * - Example: Event listener lists
 *
 * EXAMPLE:
 *   List<String> list = new CopyOnWriteArrayList<>();
 *   list.add("item1");  // Creates new internal array
 *   list.add("item2");  // Creates another new internal array
 *
 *   // Safe iteration even during concurrent modifications
 *   for (String item : list) {
 *       // No ConcurrentModificationException
 *   }
 *
 *
 * ============================================================================
 * 6. COPYONWRITEARRAYSET
 * ============================================================================
 *
 * Thread-safe Set implemented using CopyOnWriteArrayList.
 *
 * CHARACTERISTICS:
 * - Implements Set interface
 * - NOT sorted
 * - NOT blocking
 * - EXPENSIVE writes (copies entire array)
 * - CHEAP reads
 * - Allows null element
 * - Internally uses CopyOnWriteArrayList
 *
 * WHEN TO USE:
 * - Same as CopyOnWriteArrayList
 * - Need set uniqueness guarantee
 * - Many reads, few writes
 *
 * EXAMPLE:
 *   Set<String> set = new CopyOnWriteArraySet<>();
 *   set.add("item");
 *
 *
 * ============================================================================
 * 7. LINKEDBLOCKINGQUEUE
 * ============================================================================
 *
 * Thread-safe optionally-bounded FIFO blocking queue.
 *
 * CHARACTERISTICS:
 * - Implements BlockingQueue interface
 * - NOT sorted (FIFO order)
 * - BLOCKING (waits if queue full/empty)
 * - Optionally bounded (can set max capacity)
 * - Does NOT allow null elements
 *
 * BLOCKING OPERATIONS:
 * - put(E e): Blocks if queue is full
 * - take(): Blocks if queue is empty
 * - offer(E e, timeout, unit): Waits for space up to timeout
 * - poll(timeout, unit): Waits for element up to timeout
 *
 * WHEN TO USE:
 * - Producer-consumer with blocking behavior
 * - Want threads to wait when queue full/empty
 * - Work queue pattern
 *
 * EXAMPLE:
 *   BlockingQueue<String> queue = new LinkedBlockingQueue<>(10);
 *   queue.put("item");  // Blocks if queue full
 *   String item = queue.take();  // Blocks if queue empty
 *
 *
 * ============================================================================
 * SYNCHRONIZED COLLECTIONS (COLLECTIONS CLASS)
 * ============================================================================
 *
 * The Collections class provides static methods to wrap collections in
 * synchronized wrappers. These are older, less performant alternatives to
 * concurrent collections.
 *
 * WHY USE SYNCHRONIZED COLLECTIONS:
 * - Legacy code compatibility
 * - Simple thread-safety for low-contention scenarios
 * - Need iterator consistency (with manual synchronization)
 *
 * DISADVANTAGES:
 * - Single lock for entire collection (poor concurrency)
 * - Manual synchronization needed for iteration
 * - Still can get ConcurrentModificationException
 *
 *
 * SYNCHRONIZED WRAPPER METHODS (KNOW THESE SIGNATURES!):
 * ════════════════════════════════════════════════════════
 *
 * 1. synchronizedCollection
 * ─────────────────────────
 *   static <T> Collection<T> synchronizedCollection(Collection<T> c)
 *
 * - Wraps any Collection
 * - Returns synchronized Collection view
 *
 * EXAMPLE:
 *   Collection<String> col = new ArrayList<>();
 *   Collection<String> syncCol = Collections.synchronizedCollection(col);
 *
 *
 * 2. synchronizedList
 * ───────────────────
 *   static <T> List<T> synchronizedList(List<T> list)
 *
 * - Wraps any List
 * - Returns synchronized List view
 *
 * EXAMPLE:
 *   List<String> list = new ArrayList<>();
 *   List<String> syncList = Collections.synchronizedList(list);
 *
 *
 * 3. synchronizedMap
 * ──────────────────
 *   static <K,V> Map<K,V> synchronizedMap(Map<K,V> m)
 *
 * - Wraps any Map
 * - Returns synchronized Map view
 *
 * EXAMPLE:
 *   Map<String, Integer> map = new HashMap<>();
 *   Map<String, Integer> syncMap = Collections.synchronizedMap(map);
 *
 *
 * 4. synchronizedNavigableMap
 * ───────────────────────────
 *   static <K,V> NavigableMap<K,V> synchronizedNavigableMap(NavigableMap<K,V> m)
 *
 * - Wraps NavigableMap (e.g., TreeMap)
 * - Returns synchronized NavigableMap view
 *
 * EXAMPLE:
 *   NavigableMap<Integer, String> map = new TreeMap<>();
 *   NavigableMap<Integer, String> syncMap =
 *       Collections.synchronizedNavigableMap(map);
 *
 *
 * 5. synchronizedNavigableSet
 * ───────────────────────────
 *   static <T> NavigableSet<T> synchronizedNavigableSet(NavigableSet<T> s)
 *
 * - Wraps NavigableSet (e.g., TreeSet)
 * - Returns synchronized NavigableSet view
 *
 * EXAMPLE:
 *   NavigableSet<Integer> set = new TreeSet<>();
 *   NavigableSet<Integer> syncSet = Collections.synchronizedNavigableSet(set);
 *
 *
 * 6. synchronizedSet
 * ──────────────────
 *   static <T> Set<T> synchronizedSet(Set<T> s)
 *
 * - Wraps any Set
 * - Returns synchronized Set view
 *
 * EXAMPLE:
 *   Set<String> set = new HashSet<>();
 *   Set<String> syncSet = Collections.synchronizedSet(set);
 *
 *
 * 7. synchronizedSortedMap
 * ────────────────────────
 *   static <K,V> SortedMap<K,V> synchronizedSortedMap(SortedMap<K,V> m)
 *
 * - Wraps SortedMap
 * - Returns synchronized SortedMap view
 *
 * EXAMPLE:
 *   SortedMap<Integer, String> map = new TreeMap<>();
 *   SortedMap<Integer, String> syncMap =
 *       Collections.synchronizedSortedMap(map);
 *
 *
 * 8. synchronizedSortedSet
 * ────────────────────────
 *   static <T> SortedSet<T> synchronizedSortedSet(SortedSet<T> s)
 *
 * - Wraps SortedSet
 * - Returns synchronized SortedSet view
 *
 * EXAMPLE:
 *   SortedSet<Integer> set = new TreeSet<>();
 *   SortedSet<Integer> syncSet = Collections.synchronizedSortedSet(set);
 *
 *
 * SYNCHRONIZED COLLECTION ITERATION:
 * ══════════════════════════════════
 *
 * CRITICAL: Must manually synchronize when iterating!
 *
 * WRONG (not thread-safe):
 *   List<String> syncList = Collections.synchronizedList(new ArrayList<>());
 *   for (String s : syncList) {  // NOT synchronized!
 *       // May throw ConcurrentModificationException
 *   }
 *
 * CORRECT:
 *   List<String> syncList = Collections.synchronizedList(new ArrayList<>());
 *   synchronized (syncList) {  // Must lock on the collection
 *       for (String s : syncList) {
 *           // Safe iteration
 *       }
 *   }
 *
 *
 * ============================================================================
 * CONCURRENT VS SYNCHRONIZED COLLECTIONS
 * ============================================================================
 *
 * ┌──────────────────────┬─────────────────────┬─────────────────────────┐
 * │ Aspect               │ Concurrent          │ Synchronized            │
 * ├──────────────────────┼─────────────────────┼─────────────────────────┤
 * │ Locking Strategy     │ Lock striping,      │ Single lock for entire  │
 * │                      │ segments            │ collection              │
 * ├──────────────────────┼─────────────────────┼─────────────────────────┤
 * │ Iteration            │ Weakly consistent,  │ Manual synchronization  │
 * │                      │ no exceptions       │ required                │
 * ├──────────────────────┼─────────────────────┼─────────────────────────┤
 * │ Performance          │ Better under high   │ Poor under high         │
 * │                      │ contention          │ contention              │
 * ├──────────────────────┼─────────────────────┼─────────────────────────┤
 * │ Null Values          │ Generally NO        │ Depends on backing      │
 * │                      │ (except CopyOnWrite)│ collection              │
 * ├──────────────────────┼─────────────────────┼─────────────────────────┤
 * │ Fail-Fast Iterator   │ NO (weakly          │ YES (if not manually    │
 * │                      │ consistent)         │ synchronized)           │
 * └──────────────────────┴─────────────────────┴─────────────────────────┘
 *
 * PREFER: Concurrent collections for new code
 * USE: Synchronized collections for legacy code or simple scenarios
 *
 *
 * ============================================================================
 * CODE EXAMPLES
 * ============================================================================
 */
public class ConcurrentCollections {

    /**
     * Demonstrates ConcurrentHashMap
     */
    public static void demonstrateConcurrentHashMap() {
        System.out.println("=== ConcurrentHashMap ===");

        Map<String, Integer> map = new ConcurrentHashMap<>();

        // Thread-safe operations
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);

        // Special atomic methods
        map.putIfAbsent("D", 4);  // Only if not present
        map.putIfAbsent("A", 10);  // A already exists - not updated

        System.out.println("Map: " + map);
        System.out.println("A value (still 1): " + map.get("A"));

        // Safe concurrent iteration
        for (String key : map.keySet()) {
            System.out.println(key + " = " + map.get(key));
        }
        System.out.println();
    }

    /**
     * Demonstrates ConcurrentLinkedQueue
     */
    public static void demonstrateConcurrentLinkedQueue() {
        System.out.println("=== ConcurrentLinkedQueue ===");

        Queue<String> queue = new ConcurrentLinkedQueue<>();

        queue.offer("First");
        queue.offer("Second");
        queue.offer("Third");

        System.out.println("Queue: " + queue);
        System.out.println("Poll: " + queue.poll());  // First (FIFO)
        System.out.println("Peek: " + queue.peek());  // Second (doesn't remove)
        System.out.println("Queue after operations: " + queue);
        System.out.println();
    }

    /**
     * Demonstrates ConcurrentSkipListMap (sorted)
     */
    public static void demonstrateConcurrentSkipListMap() {
        System.out.println("=== ConcurrentSkipListMap ===");

        NavigableMap<Integer, String> map = new ConcurrentSkipListMap<>();

        map.put(3, "Three");
        map.put(1, "One");
        map.put(5, "Five");
        map.put(2, "Two");

        System.out.println("Sorted map: " + map);  // {1=One, 2=Two, 3=Three, 5=Five}

        // NavigableMap operations
        System.out.println("Lower key than 3: " + map.lowerKey(3));  // 2
        System.out.println("Higher key than 3: " + map.higherKey(3));  // 5
        System.out.println("SubMap [2,4): " + map.subMap(2, 4));  // {2=Two, 3=Three}
        System.out.println();
    }

    /**
     * Demonstrates ConcurrentSkipListSet (sorted)
     */
    public static void demonstrateConcurrentSkipListSet() {
        System.out.println("=== ConcurrentSkipListSet ===");

        NavigableSet<Integer> set = new ConcurrentSkipListSet<>();

        set.add(3);
        set.add(1);
        set.add(5);
        set.add(2);

        System.out.println("Sorted set: " + set);  // [1, 2, 3, 5]

        // NavigableSet operations
        System.out.println("Lower than 3: " + set.lower(3));  // 2
        System.out.println("Higher than 3: " + set.higher(3));  // 5
        System.out.println("Descending set: " + set.descendingSet());  // [5, 3, 2, 1]
        System.out.println();
    }

    /**
     * Demonstrates CopyOnWriteArrayList
     */
    public static void demonstrateCopyOnWriteArrayList() {
        System.out.println("=== CopyOnWriteArrayList ===");

        List<String> list = new CopyOnWriteArrayList<>();

        list.add("A");
        list.add("B");
        list.add("C");

        System.out.println("List: " + list);

        // Safe iteration during modification
        System.out.println("Iterating while modifying:");
        for (String item : list) {
            System.out.println("  " + item);
            list.add("D");  // Modifying during iteration - NO exception!
        }

        System.out.println("List after iteration: " + list);
        System.out.println("Note: Iterator used snapshot from before modifications");
        System.out.println();
    }

    /**
     * Demonstrates CopyOnWriteArraySet
     */
    public static void demonstrateCopyOnWriteArraySet() {
        System.out.println("=== CopyOnWriteArraySet ===");

        Set<String> set = new CopyOnWriteArraySet<>();

        set.add("X");
        set.add("Y");
        set.add("Z");
        set.add("X");  // Duplicate - not added

        System.out.println("Set: " + set);
        System.out.println("Size: " + set.size());  // 3 (no duplicate)
        System.out.println();
    }

    /**
     * Demonstrates LinkedBlockingQueue (blocking)
     */
    public static void demonstrateLinkedBlockingQueue() throws InterruptedException {
        System.out.println("=== LinkedBlockingQueue ===");

        BlockingQueue<String> queue = new LinkedBlockingQueue<>(2);  // Capacity 2

        // Non-blocking operations
        queue.offer("Item1");
        queue.offer("Item2");
        boolean added = queue.offer("Item3");  // Queue full - returns false
        System.out.println("Item3 added: " + added);  // false

        System.out.println("Queue: " + queue);

        // Blocking operations (in separate thread to avoid blocking main)
        Thread consumer = new Thread(() -> {
            try {
                Thread.sleep(1000);  // Wait before consuming
                System.out.println("Consumer taking: " + queue.take());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        consumer.start();

        System.out.println("Producer putting Item3 (will block until space available)...");
        queue.put("Item3");  // Blocks until space available
        System.out.println("Item3 successfully added");

        consumer.join();
        System.out.println();
    }

    /**
     * Demonstrates synchronized collections
     */
    public static void demonstrateSynchronizedCollections() {
        System.out.println("=== Synchronized Collections ===");

        // Synchronized List
        List<String> list = new ArrayList<>();
        List<String> syncList = Collections.synchronizedList(list);
        syncList.add("A");
        syncList.add("B");

        // MUST synchronize for iteration
        synchronized (syncList) {
            for (String s : syncList) {
                System.out.println("  " + s);
            }
        }

        // Synchronized Map
        Map<String, Integer> map = new HashMap<>();
        Map<String, Integer> syncMap = Collections.synchronizedMap(map);
        syncMap.put("key", 1);
        System.out.println("Sync map: " + syncMap);

        // Synchronized Set
        Set<Integer> set = new HashSet<>();
        Set<Integer> syncSet = Collections.synchronizedSet(set);
        syncSet.add(1);
        syncSet.add(2);
        System.out.println("Sync set: " + syncSet);

        System.out.println();
    }

    /**
     * Demonstrates concurrent modification safety
     */
    public static void demonstrateConcurrentModification() {
        System.out.println("=== Concurrent Modification ===");

        // Regular ArrayList - throws ConcurrentModificationException
        System.out.println("Regular ArrayList:");
        List<String> regularList = new ArrayList<>(Arrays.asList("A", "B", "C"));
        try {
            for (String s : regularList) {
                regularList.remove(s);  // Throws ConcurrentModificationException!
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("  ConcurrentModificationException thrown!");
        }

        // CopyOnWriteArrayList - NO exception
        System.out.println("\nCopyOnWriteArrayList:");
        List<String> cowList = new CopyOnWriteArrayList<>(Arrays.asList("A", "B", "C"));
        for (String s : cowList) {
            cowList.remove(s);  // No exception - uses snapshot
        }
        System.out.println("  No exception - but items not removed from iterator's snapshot");
        System.out.println("  Final list: " + cowList);
        System.out.println();
    }

    public static void main(String[] args) throws InterruptedException {
        demonstrateConcurrentHashMap();
        demonstrateConcurrentLinkedQueue();
        demonstrateConcurrentSkipListMap();
        demonstrateConcurrentSkipListSet();
        demonstrateCopyOnWriteArrayList();
        demonstrateCopyOnWriteArraySet();
        demonstrateLinkedBlockingQueue();
        demonstrateSynchronizedCollections();
        demonstrateConcurrentModification();
    }
}

/*
 * ============================================================================
 * EXAM SUMMARY - CONCURRENT COLLECTIONS
 * ============================================================================
 *
 * CONCURRENT COLLECTIONS TABLE (memorize!):
 * ┌─────────────────────────┬─────────────────┬──────────┬───────────┐
 * │ Class                   │ Interface       │ Sorted?  │ Blocking? │
 * ├─────────────────────────┼─────────────────┼──────────┼───────────┤
 * │ ConcurrentHashMap       │ Map             │ NO       │ NO        │
 * │ ConcurrentLinkedQueue   │ Queue           │ NO       │ NO        │
 * │ ConcurrentSkipListMap   │ NavigableMap    │ YES      │ NO        │
 * │ ConcurrentSkipListSet   │ NavigableSet    │ YES      │ NO        │
 * │ CopyOnWriteArrayList    │ List            │ NO       │ NO        │
 * │ CopyOnWriteArraySet     │ Set             │ NO       │ NO        │
 * │ LinkedBlockingQueue     │ BlockingQueue   │ NO       │ YES       │
 * └─────────────────────────┴─────────────────┴──────────┴───────────┘
 *
 * MEMORY AIDS:
 * - "SkipList" = SORTED
 * - "Blocking" in name = BLOCKING
 * - "CopyOnWrite" = expensive writes, cheap reads
 *
 * SYNCHRONIZED WRAPPER METHODS (Collections class):
 * - synchronizedCollection(Collection<T>)
 * - synchronizedList(List<T>)
 * - synchronizedMap(Map<K,V>)
 * - synchronizedNavigableMap(NavigableMap<K,V>)
 * - synchronizedNavigableSet(NavigableSet<T>)
 * - synchronizedSet(Set<T>)
 * - synchronizedSortedMap(SortedMap<K,V>)
 * - synchronizedSortedSet(SortedSet<T>)
 *
 * SYNCHRONIZED ITERATION:
 * - MUST manually synchronize on collection:
 *   synchronized (syncList) {
 *       for (String s : syncList) { }
 *   }
 *
 * NULL VALUES:
 * - Most concurrent collections do NOT allow null (except CopyOnWrite)
 * - ConcurrentHashMap: NO null keys or values
 * - ConcurrentLinkedQueue: NO null
 * - SkipList collections: NO null
 * - CopyOnWrite collections: YES null allowed
 * - LinkedBlockingQueue: NO null
 *
 * CHOOSE:
 * - High concurrency → Concurrent collections
 * - Legacy/simple → Synchronized collections
 * - Many reads, few writes → CopyOnWrite collections
 * - Need blocking → LinkedBlockingQueue
 * - Need sorting → ConcurrentSkipList collections
 */
